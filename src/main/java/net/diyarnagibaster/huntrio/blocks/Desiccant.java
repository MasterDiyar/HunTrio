package net.diyarnagibaster.huntrio.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class Desiccant extends Block {

    public Desiccant(Properties properties){
        super(properties);
    }
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (stack.is(Items.SAND)) {

            if (!level.isClientSide()) {
                if (!player.isCreative()) {
                    stack.shrink(1);
                }

                // 2. Меняем текущий блок на другой (например, на стекло)
                level.setBlockAndUpdate(pos, Blocks.GLASS.defaultBlockState());

                // 3. Проигрываем звук
                level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            // Возвращаем SUCCESS_ITEM, чтобы игра поняла, что предмет был успешно использован
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
