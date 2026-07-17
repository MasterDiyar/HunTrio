package net.diyarnagibaster.huntrio.events;

import net.diyarnagibaster.huntrio.HunTrio;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = HunTrio.MODID)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerRenderers(net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(net.diyarnagibaster.huntrio.entity.ModBlockEntities.RESEARCH_TABLE_BE.get(), net.diyarnagibaster.huntrio.renderer.ResearchTableRenderer::new);
    }
}
