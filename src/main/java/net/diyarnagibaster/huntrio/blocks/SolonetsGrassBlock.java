package net.diyarnagibaster.huntrio.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;

public class SolonetsGrassBlock extends GrassBlock {

    public SolonetsGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos targetPos = pos.above();

        labelLoop:
        for (int i = 0; i < 128; i++) {
            BlockPos currentPos = targetPos;

            // Алгоритм разброса растений вокруг блока (как в ванилле)
            for (int j = 0; j < i / 16; j++) {
                currentPos = currentPos.offset(
                        random.nextInt(3) - 1,
                        (random.nextInt(3) - 1) * random.nextInt(3) / 2,
                        random.nextInt(3) - 1
                );

                // Проверяем, находится ли растительность именно на Солонце
                if (!level.getBlockState(currentPos.below()).is(this) || level.getBlockState(currentPos).isCollisionShapeFullBlock(level, currentPos)) {
                    continue labelLoop;
                }
            }

            BlockState currentState = level.getBlockState(currentPos);

            // Если клетка пустая — сажаем растение
            if (currentState.isAir()) {
                BlockState stateToPlace;

                // Шанс 20% (1 из 5) вырастить цветок, иначе сажаем обычную траву
                if (random.nextInt(5) == 0) {
                    stateToPlace = getRandomSolonetsFlower(random);
                } else {
                    stateToPlace = Blocks.SHORT_GRASS.defaultBlockState(); // Можно заменить на свою кастомную сухую траву
                }

                // Проверяем, может ли растение выжить на этом блоке
                if (stateToPlace.canSurvive(level, currentPos)) {
                    level.setBlock(currentPos, stateToPlace, 3);
                }
            }
        }
    }

    /**
     * Выбор случайного цветка для солонца.
     * Сюда удобно добавлять новые цветы в будущем!
     */
    private BlockState getRandomSolonetsFlower(RandomSource random) {
        int roll = random.nextInt(1); // В будущем увеличивай число по количеству цветов (например, random.nextInt(3))

        switch (roll) {
            case 0:
            default:
                return ModBlocks.CICHORIUM.get().defaultBlockState();
            // case 1:
            //     return ModBlocks.WORMWOOD.get().defaultBlockState(); // Пример: Полынь
        }
    }

    private static boolean canBeSolonetsGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = levelReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(
                    levelReader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(levelReader, blockpos)
            );
            return i < levelReader.getMaxLightLevel();
        }
    }

    private static boolean canPropagateSolonets(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return canBeSolonetsGrass(state, level, pos) && !level.getFluidState(blockpos).is(FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canBeSolonetsGrass(state, level, pos)) {
            if (!level.isAreaLoaded(pos, 1)) return;
            level.setBlockAndUpdate(pos, ModBlocks.SOLONETS.get().defaultBlockState());

        } else {
            if (!level.isAreaLoaded(pos, 3)) return;

            if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
                BlockState blockstate = this.defaultBlockState();

                for (int i = 0; i < 4; i++) {
                    BlockPos blockpos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    BlockState targetState = level.getBlockState(blockpos);
                    if (targetState.is(ModBlocks.SOLONETS.get()) && canPropagateSolonets(blockstate, level, blockpos)) {

                        level.setBlockAndUpdate(
                                blockpos, blockstate.setValue(SNOWY, level.getBlockState(blockpos.above()).is(Blocks.SNOW))
                        );
                    }
                }
            }
        }
    }
}