package net.diyarnagibaster.huntrio.blocks;

import net.diyarnagibaster.huntrio.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Desiccant extends Block {

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 0, 14, 2, 10, 16),
            Block.box(14, 0, 14, 16, 10, 16),
            Block.box(0, 0, 0, 2, 10, 2),
            Block.box(14, 0, 0, 16, 10, 2),
            Block.box(0, 10, 0, 16, 11, 16),
            Block.box(0, 11, 0, 16, 16, 2),
            Block.box(0, 11, 14, 16, 16, 16),
            Block.box(0, 11, 2, 2, 16, 14),
            Block.box(14, 11, 2, 16, 16, 14)
    );

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, 5);

    public Desiccant(Properties properties){
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(STAGE);}

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        int currentStage = state.getValue(STAGE);

        if (currentStage == 0 && stack.is(Items.WATER_BUCKET)) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(STAGE, 1), 3);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }
                level.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (currentStage == 1 && stack.is(Items.SAND)) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(STAGE, 2), 3);
                if (!player.isCreative()) stack.shrink(1);

                level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

                level.scheduleTick(pos, this, 100);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (currentStage == 3) {
            if (!level.isClientSide) {
                Block.popResource(level, pos, new ItemStack(ModItems.LITHIUM_ORE.get(), 1));

                level.setBlock(pos, state.setValue(STAGE, 0), 3);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (currentStage == 1 && stack.is(Items.DRIED_KELP_BLOCK)){
            if (!level.isClientSide){
                level.setBlock(pos, state.setValue(STAGE, 4),3);
                if (!player.isCreative()) stack.shrink(1);
                level.playSound(null, pos, SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 0.95F, 1.0F);
                level.scheduleTick(pos, this, 100);
            }
            return  ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (currentStage == 5 && stack.is(Items.GLASS_BOTTLE)) {
            if (!level.isClientSide) {

                Block.popResource(level, pos, new ItemStack(ModItems.SALT_JAR.get(), 1));
                if (!player.isCreative()) stack.shrink(1);
                level.setBlock(pos, state.setValue(STAGE, 0), 3);
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }



        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(STAGE) == 2) {

            if (random.nextInt(100) < 10) {
                level.setBlock(pos, state.setValue(STAGE, 3), 3);
            } else {
                level.setBlock(pos, state.setValue(STAGE, 0), 3);
            }

            level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        if (state.getValue(STAGE) == 4){
            if (random.nextInt(100) < 80) {
                level.setBlock(pos, state.setValue(STAGE, 5), 3);
            } else {
                level.setBlock(pos, state.setValue(STAGE, 0), 3);
            }

            level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }
}
