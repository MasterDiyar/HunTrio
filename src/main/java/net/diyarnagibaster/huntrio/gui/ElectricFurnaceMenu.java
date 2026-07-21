package net.diyarnagibaster.huntrio.gui;


import net.diyarnagibaster.huntrio.blocks.ModBlocks;
import net.diyarnagibaster.huntrio.entity.ElectricFurnaceBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ElectricFurnaceMenu extends AbstractContainerMenu {
    private final ElectricFurnaceBlockEntity blockEntity;
    private final ContainerData data;

    public ElectricFurnaceMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, playerInv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    // Конструктор для Сервера
    public ElectricFurnaceMenu(int containerId, Inventory playerInv, BlockEntity entity, ContainerData data) {
        super(ModMenus.ELECTRIC_FURNACE_MENU.get(), containerId);
        this.blockEntity = (ElectricFurnaceBlockEntity) entity;
        this.data = data;

        // Добавляем данные для синхронизации (полоска энергии)
        this.addDataSlots(this.data);

        // 1. Добавляем наши 5 слотов машины (Координаты X и Y настроишь под свою картинку)
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 44, 20)); // Вход 1
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, 62, 20)); // Вход 2
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 2, 8, 53));  // Батарея
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 3, 116, 35)); // Выход 1
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 4, 134, 35)); // Выход 2

        // 2. Добавляем 36 слотов инвентаря игрока (Стандартная сетка)
        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
    }

    // Вспомогательные методы для сетки игрока
    private void addPlayerInventory(Inventory playerInv) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInv, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInv) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInv, i, 8 + i * 18, 142));
        }
    }

    // Методы для экрана (чтобы рисовать полоску энергии)
    public int getEnergy() { return this.data.get(0); }
    public int getMaxEnergy() { return this.data.get(1); }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        // Логика Shift-клика (оставим базовой, чтобы игра не крашилась)
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack itemstack1 = slot.getItem();
        ItemStack itemstack = itemstack1.copy();
        if (index < 5) { // Если кликнули в печке -> кидаем в инвентарь
            if (!this.moveItemStackTo(itemstack1, 5, this.slots.size(), true)) return ItemStack.EMPTY;
        } else if (!this.moveItemStackTo(itemstack1, 0, 5, false)) { // Из инвентаря -> в печку
            return ItemStack.EMPTY;
        }
        if (itemstack1.isEmpty()) slot.set(ItemStack.EMPTY); else slot.setChanged();
        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, ModBlocks.ELECTRIC_FURNACE.get());
    }
}