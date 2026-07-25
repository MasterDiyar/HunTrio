package net.diyarnagibaster.huntrio.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.diyarnagibaster.huntrio.blocks.ModBlocks;
import net.diyarnagibaster.huntrio.entity.CustomTntEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class CustomTntRenderer extends EntityRenderer<CustomTntEntity> {
    private final BlockRenderDispatcher blockRenderer;

    public CustomTntRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5F;
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(CustomTntEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.5D, 0.0D);

        int fuse = entity.getFuse();

        // Эффект раздувания перед взрывом (когда остается меньше 10 тиков)
        if ((float)fuse - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)fuse - partialTicks + 1.0F) / 10.0F;
            f = Mth.clamp(f, 0.0F, 1.0F);
            f *= f;
            f *= f;
            float scale = 1.0F + f * 0.3F;
            poseStack.scale(scale, scale, scale);
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5D, -0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));

        BlockState blockState = ModBlocks.LITHIUM_TNT.get().defaultBlockState();

        TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, blockState, poseStack, buffer, packedLight, fuse / 5 % 2 == 0);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CustomTntEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}