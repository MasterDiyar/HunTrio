package net.diyarnagibaster.huntrio.blocks;

import com.mojang.serialization.MapCodec;
import net.diyarnagibaster.huntrio.entity.ResearchTableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ResearchTableBlock extends BaseEntityBlock {

    public ResearchTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ResearchTableBlockEntity(pPos, pState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL; // Чтобы блок рендерился нормально
    }

    // 1. Клик с предметом в руке (Положить или Крафт)
    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if (pLevel.isClientSide()) return ItemInteractionResult.SUCCESS;
        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (be instanceof ResearchTableBlockEntity tableEntity) {
            ItemStack handItem = pPlayer.getItemInHand(pHand);

            if (!tableEntity.inventory.getStackInSlot(0).isEmpty() && !tableEntity.inventory.getStackInSlot(1).isEmpty()) {
                if (handItem.is(Items.REDSTONE)) {
                    pPlayer.getItemInHand(pHand).shrink(1);
                    tableEntity.inventory.extractItem(0, 1, false);
                    tableEntity.inventory.extractItem(1, 1, false);

                    ItemStack result = new ItemStack(Items.DIAMOND);
                    ItemEntity itemEntity = new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 1.2, pPos.getZ() + 0.5, result);
                    pLevel.addFreshEntity(itemEntity);

                    return ItemInteractionResult.SUCCESS;
                }
            }

            // Логика добавления предмета на стол
            for (int i = 0; i < tableEntity.inventory.getSlots(); i++) {
                if (tableEntity.inventory.getStackInSlot(i).isEmpty()) {
                    ItemStack stackToInsert = handItem.copyWithCount(1);
                    tableEntity.inventory.insertItem(i, stackToInsert, false);
                    pPlayer.getItemInHand(pHand).shrink(1); // Забираем 1 предмет из руки
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override @NotNull
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (pLevel.isClientSide())
            return InteractionResult.SUCCESS;

        BlockEntity be = pLevel.getBlockEntity(pPos);
        if (!(be instanceof ResearchTableBlockEntity tableEntity))
            return InteractionResult.PASS;

        for (int i = tableEntity.inventory.getSlots() - 1; i >= 0; i--) {
            // Печатаем шаг ДО любых проверок
            System.out.println("== ЦИКЛ ДОШЕЛ ДО i = " + i + " ==");

            ItemStack stackInSlot = tableEntity.inventory.getStackInSlot(i);
            System.out.println("В слоте " + i + " лежит: " + (stackInSlot.isEmpty() ? "ПУСТО" : stackInSlot.getDisplayName().getString()));

            // Если слот пустой, идем к следующему
            if (stackInSlot.isEmpty()) {
                System.out.println("Слот " + i + " пуст, пропускаем (continue).");
                continue;
            }

            // Если дошли сюда, значит предмет есть
            System.out.println("Извлекаем предмет из слота " + i + "!");
            ItemStack extracted = tableEntity.inventory.extractItem(i, 1, false);

            if (!pPlayer.getInventory().add(extracted)) {
                System.out.println("Инвентарь полон, бросаем на пол.");
                pPlayer.drop(extracted, false);
            } else {
                System.out.println("Предмет успешно отдан в инвентарь игрока.");
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof ResearchTableBlockEntity tableEntity) {
                for (int i = 0; i < tableEntity.inventory.getSlots(); i++) {
                    ItemStack stack = tableEntity.inventory.getStackInSlot(i);
                    if (stack.isEmpty()) continue;

                    pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, stack));
                }
            }
            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }
}