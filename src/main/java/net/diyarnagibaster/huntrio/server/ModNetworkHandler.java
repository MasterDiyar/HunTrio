package net.diyarnagibaster.huntrio.server;

import net.diyarnagibaster.huntrio.HunTrio;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = HunTrio.MODID)
public class ModNetworkHandler {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar(HunTrio.MODID).playToServer(
                DroneControlPayload.TYPE,
                DroneControlPayload.STREAM_CODEC,
                DroneControlPayload::handleData
        );
    }
}
