package net.diyarnagibaster.huntrio.events;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = HunTrio.MODID)
public class KnowledgeOfAgesHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player == null || event.getLevel().isClientSide() || player.isCreative()) return;

        ItemStack tool = player.getMainHandItem();
        var registryAccess = event.getLevel().registryAccess();
        var enchantRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);

        // 1. БЕЗОПАСНО пытаемся получить зачарование
        var knowledgeEnchantOpt = enchantRegistry.getHolder(net.diyarnagibaster.huntrio.enchantments.ModEnchantments.KNOWLEDGE_OF_AGES);

        // Если зачарование не найдено в реестре (нет JSON-файла), просто прерываем код
        if (knowledgeEnchantOpt.isEmpty()) return;

        // 2. Получаем уровень зачарования, передавая найденный Holder
        int knowledgeLevel = EnchantmentHelper.getTagEnchantmentLevel(knowledgeEnchantOpt.get(), tool);

        // Если зачарование есть на инструменте
        if (knowledgeLevel > 0) {
            BlockState state = event.getState();
            BlockPos pos = event.getPos();
            Level level = (Level) event.getLevel();

            ItemStack dropStack = getSecondaryDrop(state);

            if (!dropStack.isEmpty()) {
                float dropChance = 0.25F + ((knowledgeLevel - 1) * 0.10F);

                if (level.random.nextFloat() < dropChance) {
                    // Безопасно получаем удачу (Удача точно есть в ванильной игре, но для надежности тоже лучше использовать getHolder)
                    var fortuneEnchantOpt = enchantRegistry.getHolder(Enchantments.FORTUNE);
                    int fortuneLevel = 0;
                    if (fortuneEnchantOpt.isPresent()) {
                        fortuneLevel = EnchantmentHelper.getTagEnchantmentLevel(fortuneEnchantOpt.get(), tool);
                    }

                    int count = calculateFortuneDrop(level, fortuneLevel);
                    dropStack.setCount(count);

                    Block.popResource(level, pos, dropStack);
                }
            }
        }
    }

    private static ItemStack getSecondaryDrop(BlockState state) {
        if (state.is(Blocks.DEEPSLATE_IRON_ORE))
            return new ItemStack(ModItems.NICKEL.get());
        if (state.is(Blocks.REDSTONE_ORE) || state.is(Blocks.DEEPSLATE_REDSTONE_ORE)) {
            return new ItemStack(ModItems.MANGANESE.get());
        }
        if (state.is(Blocks.COPPER_ORE) || state.is(Blocks.DEEPSLATE_COPPER_ORE)) {
            return new ItemStack(ModItems.COBALT.get());
        }
        if (state.is(Blocks.LAPIS_ORE) || state.is(Blocks.DEEPSLATE_LAPIS_ORE)) {
            return new ItemStack(ModItems.SULFUR.get());
        }
        if (state.is(Blocks.GOLD_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE) || state.is(Blocks.NETHER_GOLD_ORE)) {
            return new ItemStack(ModItems.TELLURIUM.get());
        }

        return ItemStack.EMPTY;
    }
    private static int calculateFortuneDrop(Level level, int fortuneLevel) {
        if (fortuneLevel <= 0) return 1;


        int bonus = level.random.nextInt(fortuneLevel + 2) - 1;
        if (bonus < 0) {
            bonus = 0;
        }
        return 1 + bonus;
    }
}