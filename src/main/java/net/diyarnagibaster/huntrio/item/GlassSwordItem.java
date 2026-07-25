package net.diyarnagibaster.huntrio.item;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class GlassSwordItem extends SwordItem {

    // Шанс мгновенного разлетания на осколки при ударе (12% = ~1 из 8 ударов)
    private static final float SHATTER_CHANCE = 0.04F;

    public GlassSwordItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);

        if (!attacker.level().isClientSide()) {
            if (attacker.level().random.nextFloat() < SHATTER_CHANCE) {

                attacker.level().playSound(
                        null,
                        attacker.getX(), attacker.getY(), attacker.getZ(),
                        SoundEvents.GLASS_BREAK,
                        SoundSource.PLAYERS,
                        1.0F, 1.0F
                );

                if (attacker.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            new ItemParticleOption(ParticleTypes.ITEM, stack),
                            target.getX(), target.getY(0.5D), target.getZ(),
                            20, // Количество осколков
                            0.2D, 0.2D, 0.2D, 0.15D
                    );
                }

                stack.hurtAndBreak(stack.getMaxDamage(), attacker, EquipmentSlot.MAINHAND);
            }
        }

        return result;
    }
}