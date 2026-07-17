package net.diyarnagibaster.huntrio.events;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.item.CustomBrushItem;
import net.diyarnagibaster.huntrio.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@EventBusSubscriber(modid = HunTrio.MODID)
public class BrushInteractionHandler {

    private static final Random RANDOM = new Random();
    private static final Map<UUID, Integer> brushProgress = new HashMap<>();
    private static final Map<UUID, BlockPos> brushingBlock = new HashMap<>();

    // Срабатывает КАЖДЫЙ ТИК (20 раз в секунду), пока игрок зажимает ПКМ с кистью
    @SubscribeEvent
    public static void onBrushTick(LivingEntityUseItemEvent.Tick event) {
        if (!(event.getEntity() instanceof Player player)) return;

        ItemStack itemStack = event.getItem();
        // Убеждаемся, что игрок "использует" именно кисть
        if (!(itemStack.getItem() instanceof BrushItem)) return;

        Level level = player.level();
        UUID playerId = player.getUUID();

        // Безопасный серверный RayTrace (определяем, на какой блок смотрит игрок)
        // Мы "стреляем" лучом из глаз игрока на 5 блоков вперед
        Vec3 eyePosition = player.getEyePosition();
        Vec3 lookVector = player.getViewVector(1.0F);
        Vec3 endPosition = eyePosition.add(lookVector.scale(5.0D));
        BlockHitResult hitResult = level.clip(new ClipContext(eyePosition, endPosition, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

        // Если игрок смотрит в воздух или на моба — сбрасываем прогресс
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            resetProgress(playerId);
            return;
        }

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        // Если блок не грязь и не уголь — сбрасываем прогресс
        if (!state.is(Blocks.DIRT) && !state.is(Blocks.COAL_BLOCK)) {
            resetProgress(playerId);
            return;
        }

        // Если игрок во время чистки дернул камерой на соседний блок — начинаем заново
        if (!pos.equals(brushingBlock.get(playerId))) {
            brushingBlock.put(playerId, pos);
            brushProgress.put(playerId, 0);
        }

        // Увеличиваем таймер
        int progress = brushProgress.getOrDefault(playerId, 0) + 1;
        brushProgress.put(playerId, progress);

        // ================== ЛОГИКА ДЛЯ УГОЛЬНОГО БЛОКА ==================
        if (state.is(Blocks.COAL_BLOCK)) {
            // Каждые 10 тиков (полсекунды) проигрываем звук шорканья
            if (progress % 10 == 0 && !level.isClientSide()) {
                level.playSound(null, pos, SoundEvents.BRUSH_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            // 40 тиков = ровно 2 секунды зажатия
            if (progress >= 40) {
                if (!level.isClientSide()) {
                    ItemStack dust = new ItemStack(ModItems.GRAPHITE_DUST.get(), 9);
                    Block.popResource(level, pos, dust);
                    level.destroyBlock(pos, false);
                    itemStack.hurtAndBreak(9, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
                }
                player.stopUsingItem(); // Автоматически прерываем анимацию чистки
                resetProgress(playerId);
            }
        }

        // ================== ЛОГИКА ДЛЯ ГРЯЗИ ==================
        else if (state.is(Blocks.DIRT)) {
            // Каждые 10 тиков (полсекунды) звук и шанс потратить прочность
            if (progress % 10 == 0 && !level.isClientSide()) {
                level.playSound(null, pos, SoundEvents.BRUSH_SAND, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (RANDOM.nextBoolean()) {
                    itemStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
                }
            }

            // 60 тиков = ровно 3 секунды зажатия (можешь изменить на 40 или любое другое)
            if (progress >= 60) {
                if (!level.isClientSide()) {
                    int dropChance = 30;
                    if (itemStack.getItem() instanceof CustomBrushItem customBrush) {
                        dropChance = customBrush.getBaseDropChance();
                    }

                    var fortuneEnchantment = level.registryAccess()
                            .registryOrThrow(Registries.ENCHANTMENT)
                            .getHolderOrThrow(Enchantments.FORTUNE);
                    int fortuneLevel = EnchantmentHelper.getTagEnchantmentLevel(fortuneEnchantment, itemStack);
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
                    level.playSound(null, pos, SoundEvents.BRUSH_GENERIC, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
                player.stopUsingItem(); // Автоматически прерываем анимацию чистки
                resetProgress(playerId);
            }
        }
    }

    // Это событие поймает момент, когда игрок отпустил мышку ДО того,
    // как блок очистился (чтобы прогресс не "сохранялся" в воздухе)
    @SubscribeEvent
    public static void onBrushStop(LivingEntityUseItemEvent.Stop event) {
        if (event.getEntity() instanceof Player player) {
            resetProgress(player.getUUID());
        }
    }

    // Вспомогательный метод для быстрой очистки памяти
    private static void resetProgress(UUID playerId) {
        brushProgress.remove(playerId);
        brushingBlock.remove(playerId);
    }
}