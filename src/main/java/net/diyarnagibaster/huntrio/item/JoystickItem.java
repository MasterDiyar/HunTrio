package net.diyarnagibaster.huntrio.item;

import net.diyarnagibaster.huntrio.component.ModDataComponents;
import net.diyarnagibaster.huntrio.entity.DroneEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class JoystickItem extends Item {

    public JoystickItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override @NotNull
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (!(interactionTarget instanceof DroneEntity drone))
            return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
        stack.set(ModDataComponents.BOUND_DRONE.get(), drone.getUUID());


        player.setItemInHand(usedHand, stack);



        return InteractionResult.sidedSuccess(player.level().isClientSide());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        UUID boundUuid = stack.get(ModDataComponents.BOUND_DRONE.get());

        if (level.isClientSide() && boundUuid != null && player.isShiftKeyDown()) {
            net.minecraft.world.phys.AABB searchArea = player.getBoundingBox().inflate(768.0D);

            java.util.List<DroneEntity> drones = level.getEntitiesOfClass(
                    DroneEntity.class,
                    searchArea,
                    entity -> entity.getUUID().equals(boundUuid));
            if (!drones.isEmpty()) {
                DroneEntity drone = drones.getFirst();
                if (net.minecraft.client.Minecraft.getInstance().getCameraEntity() == drone)
                    net.minecraft.client.Minecraft.getInstance().setCameraEntity(player);
                else
                    net.minecraft.client.Minecraft.getInstance().setCameraEntity(drone);
            }
        }

        if (boundUuid != null && !level.isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) level;
            Entity entity = serverLevel.getEntity(boundUuid);

            if (entity instanceof DroneEntity drone) {
                drone.setActive(!drone.isActive());

                String state = drone.isActive() ? "ВКЛЮЧЕН" : "ВЫКЛЮЧЕН";
                player.displayClientMessage(Component.literal("Дрон " + state), true);
            } else {
                player.displayClientMessage(Component.literal("Связь с дроном потеряна (уничтожен или слишком далеко)"), true);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}