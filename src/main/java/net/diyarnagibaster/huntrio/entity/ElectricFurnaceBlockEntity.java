package net.diyarnagibaster.huntrio.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ElectricFurnaceBlockEntity extends BlockEntity {

    public final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
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

        // --- ЛОГИКА БАТАРЕЙКИ (Высасывание FE из предмета в слоте 1) ---
        ItemStack batteryStack = inventory.getStackInSlot(1);
        if (!batteryStack.isEmpty()) {
            // Ищем энергетическую "способность" у предмета. Передаем null в качестве контекста.
            IEnergyStorage batteryEnergy = batteryStack.getCapability(Capabilities.EnergyStorage.ITEM, null);

            if (batteryEnergy != null && batteryEnergy.canExtract()) {
                int energyNeeded = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();

                if (energyNeeded > 0) {
                    int extracted = batteryEnergy.extractEnergy(Math.min(energyNeeded, 1000), false);
                    if (extracted > 0) {
                        energyStorage.receiveEnergy(extracted, false);
                        isDirty = true;
                    }
                }
            }
        }

        ItemStack input = inventory.getStackInSlot(0);
        int fePerTick = 50;

        if (!input.isEmpty() && energyStorage.getEnergyStored() >= fePerTick) {

            energyStorage.extractEnergy(fePerTick, false);
            isDirty = true;
        }

        if (isDirty) {
            setChanged();
        }
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
}