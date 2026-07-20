package net.diyarnagibaster.huntrio.client;

import net.diyarnagibaster.huntrio.HunTrio;
import net.diyarnagibaster.huntrio.client.model.DroneMK1;
import net.diyarnagibaster.huntrio.entity.DroneEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class DroneRenderer extends MobRenderer<DroneEntity, DroneMK1<DroneEntity>> {

    private static final ResourceLocation DRONE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(HunTrio.MODID, "textures/entities/drone_lopast.png");

    public DroneRenderer(EntityRendererProvider.Context context) {
        super(context, new DroneMK1<>(context.bakeLayer(DroneMK1.LAYER_LOCATION)), 0.33f);
    }

    @Override
    public ResourceLocation getTextureLocation(DroneEntity entity) {
        return DRONE_TEXTURE;
    }


}