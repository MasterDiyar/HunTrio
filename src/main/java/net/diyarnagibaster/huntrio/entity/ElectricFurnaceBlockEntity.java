package net.diyarnagibaster.huntrio.entity;

import net.diyarnagibaster.huntrio.gui.ElectricFurnaceMenu;
import net.diyarnagibaster.huntrio.item.ElectroItem;
import net.diyarnagibaster.huntrio.recipe.ElectricFurnaceInput;
import net.diyarnagibaster.huntrio.recipe.ElectricFurnaceRecipe;
import net.diyarnagibaster.huntrio.research.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class ElectricFurnaceBlockEntity extends BlockEntity implements MenuProvider {

    public int progress = 0;
    public int maxProgress = 0;

    public final ItemStackHandler inventory = new ItemStackHandler(5) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case 3, 4 -> false; // Слоты 3 и 4 (Выход) — строго запрещаем класть что-либо
                case 2 -> stack.getItem() instanceof ElectroItem; // Слот 1 (Батарея) — только ElectroItem
                default -> super.isItemValid(slot, stack); // Слоты 0 и 2 (Вход) — разрешаем всё
            };
        }
    };

    public final EnergyStorage energyStorage = new EnergyStorage(50000, 1000, 0) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if (received > 0 && !simulate) setChanged();
            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int extracted = super.extractEnergy(maxExtract, simulate);
            if (extracted > 0 && !simulate) setChanged();
            return extracted;
        }

        public void setEnergyForLoad(int energyAmount) {
            this.energy = energyAmount;
        }
    };

    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTRIC_FURNACE.get(), pos, state);
    }

    public void tick() {
        if (level == null || level.isClientSide) return;

        boolean isDirty = false;

        // Будем выводить дебаг-сообщения только раз в секунду (каждые 20 тиков)
        boolean shouldDebug = (level.getGameTime() % 20 == 0);

        if (shouldDebug) {
            System.out.println("=== [DEBUG ПЕЧИ] ===");
            System.out.println("Энергия внутри печи: " + energyStorage.getEnergyStored() + " / " + energyStorage.getMaxEnergyStored());
        }

        // --- ЛОГИКА БАТАРЕИ ---
        ItemStack batteryStack = inventory.getStackInSlot(2);
        if (!batteryStack.isEmpty()) {
            if (shouldDebug) System.out.println("[Батарея] Предмет в слоте 2: " + batteryStack.getItem());

            IEnergyStorage batteryEnergy = batteryStack.getCapability(Capabilities.EnergyStorage.ITEM, null);

            if (batteryEnergy == null) {
                if (shouldDebug) System.out.println("[Батарея] ОШИБКА: Capability энергии = NULL (Игра не считает это батарейкой!)");
            } else {
                if (shouldDebug) System.out.println("[Батарея] Заряд предмета: " + batteryEnergy.getEnergyStored());

                if (batteryEnergy.canExtract()) {
                    int energyNeeded = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
                    if (energyNeeded > 0) {
                        int extracted = batteryEnergy.extractEnergy(Math.min(energyNeeded, 1000), false);
                        if (extracted > 0) {
                            if (shouldDebug) System.out.println("[Батарея] Успешно высосано: " + extracted + " FE");
                            energyStorage.receiveEnergy(extracted, false);
                            inventory.setStackInSlot(2, batteryStack);
                            isDirty = true;
                        } else {
                            if (shouldDebug) System.out.println("[Батарея] Метод extractEnergy вернул 0!");
                        }
                    }
                } else {
                    if (shouldDebug) System.out.println("[Батарея] ОШИБКА: canExtract() вернул false!");
                }
            }
        } else {
            if (shouldDebug) System.out.println("[Батарея] Слот 2 пуст.");
        }

        // --- ЛОГИКА КРАФТА ---
        ItemStack input1 = inventory.getStackInSlot(0);
        ItemStack input2 = inventory.getStackInSlot(1);

        if (input1.isEmpty() && input2.isEmpty()) {
            if (shouldDebug) System.out.println("[Крафт] Слоты 0 и 1 пусты. Ждем.");
            if (this.progress > 0) {
                this.progress = 0;
                setChanged();
            }
            return;
        }

        if (shouldDebug) System.out.println("[Крафт] Пытаемся найти рецепт для: " + input1.getItem() + " + " + input2.getItem());

        ElectricFurnaceInput recipeInput = new ElectricFurnaceInput(input1, input2);
        var recipeHolder = level.getRecipeManager().getRecipeFor(ModRecipes.ELECTRIC_SMELTING_TYPE.get(), recipeInput, level);

        if (recipeHolder.isPresent()) {
            ElectricFurnaceRecipe recipe = recipeHolder.get().value();
            this.maxProgress = recipe.processingTime();
            int fePerTick = recipe.energyCost() / this.maxProgress;

            if (shouldDebug) System.out.println("[Крафт] Рецепт НАЙДЕН! Требуется: " + fePerTick + " FE/тик. Прогресс: " + this.progress + "/" + this.maxProgress);

            boolean hasEnergy = energyStorage.getEnergyStored() >= fePerTick;
            boolean hasSpace = canInsertItemIntoOutputs(recipe.output1(), recipe.output2());

            if (!hasEnergy && shouldDebug) System.out.println("[Крафт] ОШИБКА: Не хватает энергии для крафта!");
            if (!hasSpace && shouldDebug) System.out.println("[Крафт] ОШИБКА: Нет места в слотах выхода!");

            if (hasEnergy && hasSpace) {
                energyStorage.extractEnergy(fePerTick, false);
                this.progress++;
                isDirty = true;

                if (this.progress >= this.maxProgress) {
                    if (shouldDebug) System.out.println("[Крафт] ГОТОВО! Выдаем предмет.");
                    craftItem(recipe);
                    this.progress = 0;
                }
            }
        } else {
            if (shouldDebug) System.out.println("[Крафт] ОШИБКА: Рецепт НЕ НАЙДЕН в JSON!");
            if (this.progress > 0) {
                this.progress = 0;
                isDirty = true;
            }
        }

        if (isDirty) {
            setChanged();
        }
    }

    private boolean canInsertItemIntoOutputs(ItemStack out1, ItemStack out2) {
        return canInsert(out1, 3) && canInsert(out2, 4);
    }

    private boolean canInsert(ItemStack outputStack, int outputSlot) {
        if (outputStack.isEmpty()) return true; // Если предмет пустой (например, нет второго выхода) — всё ок
        ItemStack currentOutput = inventory.getStackInSlot(outputSlot);
        if (currentOutput.isEmpty()) return true;
        if (!ItemStack.isSameItemSameComponents(currentOutput, outputStack)) return false;

        return currentOutput.getCount() + outputStack.getCount() <= inventory.getSlotLimit(outputSlot);
    }

    private void craftItem(ElectricFurnaceRecipe recipe) {
        inventory.getStackInSlot(0).shrink(1);
        inventory.getStackInSlot(1).shrink(1);

        insertOutput(recipe.output1(), 3);
        insertOutput(recipe.output2(), 4);
    }
    private void insertOutput(ItemStack outputStack, int outputSlot) {
        if (outputStack.isEmpty()) return;

        ItemStack currentOutput = inventory.getStackInSlot(outputSlot);
        if (currentOutput.isEmpty())
            inventory.setStackInSlot(outputSlot, outputStack.copy());
        else
            currentOutput.grow(outputStack.getCount());
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("energy", energyStorage.getEnergyStored());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        energyStorage.receiveEnergy(tag.getInt("energy"), true);
    }

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> energyStorage.getEnergyStored();
                case 1 -> energyStorage.getMaxEnergyStored();
                case 2 -> progress;
                case 3 -> maxProgress;
                default -> 0;
            };
        }
        @Override
        public void set(int index, int value) {
            switch (index) {
                case 2 -> progress = value;
                case 3 -> maxProgress = value;
            }
        }
        @Override
        public int getCount() {
            return 4;
        }
    };

    // 3. Реализуем методы MenuProvider
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.huntrio.electric_furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ElectricFurnaceMenu(containerId, playerInventory, this, this.data);
    }
}