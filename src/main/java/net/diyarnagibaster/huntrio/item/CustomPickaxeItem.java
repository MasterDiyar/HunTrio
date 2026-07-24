package net.diyarnagibaster.huntrio.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class CustomPickaxeItem extends PickaxeItem {

    private final List<DropRule> dropRules;

    public record DropRule(Block targetBlock, Item dropItem, float baseChance) {}

    private CustomPickaxeItem(Tier tier, Properties properties, List<DropRule> dropRules) {
        super(tier, properties);
        this.dropRules = dropRules;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        boolean result = super.mineBlock(stack, level, state, pos, miningEntity);

        if (!level.isClientSide() && miningEntity instanceof Player player) {

            // Получаем уровень зачарования "Удача" (Fortune) для 1.21.1
            int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    level.registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(Enchantments.FORTUNE),
                    stack
            );

            // Проверяем все зарегистрированные правила onBreak
            for (DropRule rule : dropRules) {
                if (state.is(rule.targetBlock())) {

                    // Формула: Базовый шанс + (10% * уровень удачи)
                    // Пример: 0.2f (20%) с Удачей III превращается в 0.2 + (3 * 0.1) = 0.5 (50%)
                    float finalChance = rule.baseChance() + (fortuneLevel * 0.10f);

                    if (level.random.nextFloat() < finalChance) {
                        Block.popResource(level, pos, new ItemStack(rule.dropItem()));
                    }
                }
            }
        }

        return result;
    }

    // --- БИЛДЕР ---
    public static class Builder {
        private final Tier tier;
        private final Properties properties;
        private final List<DropRule> dropRules = new ArrayList<>();

        public Builder(Tier tier, Properties properties) {
            this.tier = tier;
            this.properties = properties;
        }

        /**
         * @param block Блок, который нужно сломать
         * @param dropItem Предмет, который должен выпасть
         * @param baseChance Базовый шанс (от 0.0f до 1.0f, где 0.2f = 20%)
         */
        public Builder onBreak(Block block, Item dropItem, float baseChance) {
            this.dropRules.add(new DropRule(block, dropItem, baseChance));
            return this;
        }

        public CustomPickaxeItem build() {
            return new CustomPickaxeItem(tier, properties, dropRules);
        }
    }
}