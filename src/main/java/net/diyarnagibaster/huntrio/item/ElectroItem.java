package net.diyarnagibaster.huntrio.item;

import net.diyarnagibaster.huntrio.component.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.energy.IEnergyStorage;

import java.util.List;

public class ElectroItem extends Item {
    public final int capacity;
    public final int maxReceive;
    public final int maxExtract;

    public ElectroItem(Properties properties, int capacity, int maxReceive, int maxExtract) {
        super(properties);
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public ItemStack getChargedStack() {
        ItemStack stack = new ItemStack(this);
        // Записываем максимальную энергию в наш компонент
        stack.set(ModDataComponents.ENERGY.get(), this.capacity);
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int currentEnergy = stack.getOrDefault(ModDataComponents.ENERGY.get(), 0);
        tooltipComponents.add(Component.literal("Энергия: " + currentEnergy + " / " + capacity + " FE").withStyle(ChatFormatting.RED));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    public static class EnergyStorageWrapper implements IEnergyStorage {
        private final ItemStack stack;
        private final ElectroItem item;

        public EnergyStorageWrapper(ItemStack stack, ElectroItem item) {
            this.stack = stack;
            this.item = item;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!canReceive()) return 0;
            int current = getEnergyStored();
            int energyReceived = Math.min(item.capacity - current, Math.min(item.maxReceive, maxReceive));
            if (!simulate && energyReceived > 0) {
                stack.set(ModDataComponents.ENERGY.get(), current + energyReceived);
            }
            return energyReceived;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            if (!canExtract()) return 0;
            int current = getEnergyStored();
            int energyExtracted = Math.min(current, Math.min(item.maxExtract, maxExtract));
            if (!simulate && energyExtracted > 0) {
                stack.set(ModDataComponents.ENERGY.get(), current - energyExtracted);
            }
            return energyExtracted;
        }

        @Override
        public int getEnergyStored() {
            // Если компонента еще нет (предмет только скрафтили), вернет 0
            return stack.getOrDefault(ModDataComponents.ENERGY.get(), 0);
        }

        @Override
        public int getMaxEnergyStored() { return item.capacity; }
        @Override public boolean canExtract() { return item.maxExtract > 0; }
        @Override public boolean canReceive() { return item.maxReceive > 0; }


    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.ENERGY.get(), 0) < this.capacity;

    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int currentEnergy = stack.getOrDefault(ModDataComponents.ENERGY.get(), 0);
        return Math.round(13.0F * currentEnergy / this.capacity);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int currentEnergy = stack.getOrDefault(ModDataComponents.ENERGY.get(), 0);
        float fillRatio = (float) currentEnergy / this.capacity; // Получаем значение от 0.0 до 1.0

        // Mth.hsvToRgb принимает параметры HSB (Hue, Saturation, Brightness).
        // Делим fillRatio на 3.0F, чтобы ограничить спектр:
        // 0.0 / 3.0 = 0.0 (Красный цвет - пустая)
        // 0.5 / 3.0 = ~0.16 (Желтый цвет - наполовину)
        // 1.0 / 3.0 = ~0.33 (Зеленый цвет - полная)
        return Mth.hsvToRgb(fillRatio / 3.0F+0.5f, 1.0F, 1.0F);
    }
}