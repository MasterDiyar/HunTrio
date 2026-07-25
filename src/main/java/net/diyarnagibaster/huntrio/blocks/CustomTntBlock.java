package net.diyarnagibaster.huntrio.blocks;

import net.diyarnagibaster.huntrio.entity.CustomTntEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import javax.annotation.Nullable;

public class CustomTntBlock extends Block {

    public CustomTntBlock(Properties properties) {
        super(properties);
    }

    public static void explode(Level level, BlockPos pos, @Nullable LivingEntity igniter) {
        if (!level.isClientSide()) {
            CustomTntEntity tnt = new CustomTntEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, igniter);
            level.addFreshEntity(tnt);
            level.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean isMoving) {
        if (level.hasNeighborSignal(pos)) {
            explode(level, pos, null);
            level.removeBlock(pos, false);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE)) {
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        } else {
            explode(level, pos, player);
            level.removeBlock(pos, false);

            if (stack.is(Items.FLINT_AND_STEEL)) {
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
            } else {
                stack.shrink(1);
            }

            player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide()) {
            BlockPos blockpos = hit.getBlockPos();
            if (projectile.isOnFire() && projectile.mayInteract(level, blockpos)) {
                explode(level, blockpos, projectile.getOwner() instanceof LivingEntity living ? living : null);
                level.removeBlock(blockpos, false);
            }
        }
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, net.minecraft.world.level.Explosion explosion) {
        if (!level.isClientSide()) {
            CustomTntEntity tnt = new CustomTntEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, explosion.getIndirectSourceEntity());
            int fuse = tnt.getFuse();
            tnt.setFuse((short)(level.random.nextInt(fuse / 4) + fuse / 8));
            level.addFreshEntity(tnt);
        }
    }
}
