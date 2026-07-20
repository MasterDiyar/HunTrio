package net.diyarnagibaster.huntrio.client;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.entity.DroneEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = HunTrio.MODID, value = Dist.CLIENT)
public class CameraEvents {

    @SubscribeEvent
    public static void onCameraRoll(ViewportEvent.ComputeCameraAngles event) {
        if (event.getCamera().getEntity() instanceof DroneEntity drone) {

            event.setRoll(drone.roll);
        }
    }
}