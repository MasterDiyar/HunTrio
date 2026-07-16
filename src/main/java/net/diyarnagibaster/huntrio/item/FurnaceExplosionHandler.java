package net.diyarnagibaster.huntrio.item;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.item.ModItems;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;

@EventBusSubscriber(modid = HunTrio.MODID)
public class FurnaceExplosionHandler {

    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();

        if (level.isClientSide) return;

        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof AbstractFurnaceBlockEntity furnace) {

            ItemStack inputItem = furnace.getItem(0);

            BlockState furnaceState = level.getBlockState(pos);

            boolean isFurnaceLit = furnaceState.hasProperty(BlockStateProperties.LIT) &&
                    furnaceState.getValue(BlockStateProperties.LIT);

            if (isExplosiveDust(inputItem) && isFurnaceLit) {

                level.explode(
                        null,
                        pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                        4.0F,
                        Level.ExplosionInteraction.BLOCK
                );

                event.setCanceled(true);
            }
        }
    }

    private static boolean isExplosiveDust(ItemStack stack) {
        return stack.getItem() == ModItems.ALUMINIUM.get();
    }
}