package net.diyarnagibaster.huntrio.client;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.client.model.DroneMK1;
import net.diyarnagibaster.huntrio.entity.DroneEntity;
import net.diyarnagibaster.huntrio.entity.ModEntities;
import net.diyarnagibaster.huntrio.gui.ElectricFurnaceScreen;
import net.diyarnagibaster.huntrio.gui.ModMenus;
import net.diyarnagibaster.huntrio.server.DroneControlPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = HunTrio.MODID)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.DRONE_MK1.get(), DroneRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        // Говорим игре: "Зарегистрируй геометрию дрона, которую мы описали в классе DroneMK1"
        event.registerLayerDefinition(DroneMK1.LAYER_LOCATION, DroneMK1::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null && mc.getCameraEntity() instanceof DroneEntity drone) {
            boolean shift = mc.options.keyShift.isDown();
            if (mc.options.keyUse.isDown() && shift  ){
                mc.setCameraEntity(mc.player);
                mc.player.displayClientMessage(net.minecraft.network.chat.Component.literal("Связь с дроном потеряна!"), true);

                mc.player.input.forwardImpulse = 0;
                mc.player.input.leftImpulse = 0;
                return;
            }

            if (drone.isRemoved() || drone.isDeadOrDying() || mc.player.distanceTo(drone) > 384) {
                mc.setCameraEntity(mc.player);
                mc.player.displayClientMessage(net.minecraft.network.chat.Component.literal("Связь с дроном потеряна!"), true);

                mc.player.input.forwardImpulse = 0;
                mc.player.input.leftImpulse = 0;
                return;
            }

            boolean w = mc.options.keyUp.isDown();
            boolean s = mc.options.keyDown.isDown();
            boolean a = mc.options.keyLeft.isDown();
            boolean d = mc.options.keyRight.isDown();
            boolean space = mc.options.keyJump.isDown();


            PacketDistributor.sendToServer(new DroneControlPayload(w, s, a, d, space, shift));

            mc.player.input.forwardImpulse = 0;
            mc.player.input.leftImpulse = 0;
            mc.player.input.jumping = false;
            mc.player.input.shiftKeyDown = false;
        }
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.ELECTRIC_FURNACE_MENU.get(), ElectricFurnaceScreen::new);
    }
}