package net.diyarnagibaster.huntrio.gui;


import net.diyarnagibaster.huntrio.blocks.ModBlocks;
import net.diyarnagibaster.huntrio.entity.ElectricFurnaceBlockEntity;
import net.diyarnagibaster.huntrio.item.ElectroItem;
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
        this(containerId, playerInv, playerInv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(4));
    }

    // Конструктор для Сервера
    public ElectricFurnaceMenu(int containerId, Inventory playerInv, BlockEntity entity, ContainerData data) {
        super(ModMenus.ELECTRIC_FURNACE_MENU.get(), containerId);
        this.blockEntity = (ElectricFurnaceBlockEntity) entity;
        this.data = data;

        this.addDataSlots(this.data);

        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 0, 44, 20));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 1, 62, 20));
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 2, 8, 53){
            @Override public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof ElectroItem;}});
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 3, 116, 35){@Override
        public boolean mayPlace(ItemStack stack) {return false;}});
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, 4, 134, 35) {@Override
        public boolean mayPlace(ItemStack stack) {return false;}});


        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
    }

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

    public int getEnergy() { return this.data.get(0); }
    public int getMaxEnergy() { return this.data.get(1); }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stackInSlot = slot.getItem();
        ItemStack originalStack = stackInSlot.copy();

        if (index < 5) {
            if (!this.moveItemStackTo(stackInSlot, 5, this.slots.size(), true))
                return ItemStack.EMPTY;
        }
        else {
            if (stackInSlot.getItem() instanceof ElectroItem) {
                if (!this.moveItemStackTo(stackInSlot, 2, 3, false))
                    return ItemStack.EMPTY;
            }
            else {
                if (!this.moveItemStackTo(stackInSlot, 0, 1, false) &&
                        !this.moveItemStackTo(stackInSlot, 1, 2, false))
                    return ItemStack.EMPTY;
            }
        }

        if (stackInSlot.isEmpty())
            slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return originalStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, ModBlocks.ELECTRIC_FURNACE.get());
    }
}