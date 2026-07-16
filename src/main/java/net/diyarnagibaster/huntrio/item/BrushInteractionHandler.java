package net.diyarnagibaster.huntrio.item;

import net.diyarnagibaster.huntrio.HunTrio;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@EventBusSubscriber(modid = HunTrio.MODID)
public class BrushInteractionHandler {

    private static final Random RANDOM = new Random();

    private static final Map<UUID, Integer> brushProgress = new HashMap<>();

    private static final Map<UUID, BlockPos> brushingBlock = new HashMap<>();

    @SubscribeEvent
    public static void onBrushUse(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockState state = level.getBlockState(pos);
        UUID playerId = player.getUUID();

        if (level.isClientSide()) return;

        if (state.is(Blocks.DIRT) && itemInHand.getItem() instanceof BrushItem) {

            if (!pos.equals(brushingBlock.get(playerId))) {
                brushProgress.put(playerId, 0);
                brushingBlock.put(playerId, pos);
            }
            int currentProgress = brushProgress.getOrDefault(playerId, 0) + 1;
            brushProgress.put(playerId, currentProgress);

            if (currentProgress % 3 == 0) {
                level.playSound(null, pos, SoundEvents.BRUSH_SAND, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (RANDOM.nextBoolean())
                    itemInHand.hurtAndBreak(1, player, Player.getSlotForHand(hand));
            }
            HunTrio.LOGGER.info("Прогресс очистки у игрока {}: {}", player.getName().getString(), currentProgress);

            if (currentProgress >= 6) {
                int dropChance = 30;
                var fortuneEnchantment = level.registryAccess()
                        .registryOrThrow(Registries.ENCHANTMENT)
                        .getHolderOrThrow(Enchantments.FORTUNE);
                int fortuneLevel = EnchantmentHelper.getTagEnchantmentLevel(fortuneEnchantment, itemInHand);
                if (fortuneLevel > 0) {
                    dropChance += (fortuneLevel * 5);
                }

                if (RANDOM.nextInt(100) < dropChance) {
                    ItemStack dust = new ItemStack(ModItems.ALUMINIUM.get(), 1);
                    ItemEntity itemEntity = new ItemEntity(
                            level, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, dust
                    );
                    level.addFreshEntity(itemEntity);
                }

                level.setBlockAndUpdate(pos, Blocks.COARSE_DIRT.defaultBlockState());

                brushProgress.remove(playerId);
                brushingBlock.remove(playerId);

                level.playSound(null, pos, SoundEvents.BRUSH_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);

        } else {
            brushProgress.remove(playerId);
            brushingBlock.remove(playerId);
        }
    }
}