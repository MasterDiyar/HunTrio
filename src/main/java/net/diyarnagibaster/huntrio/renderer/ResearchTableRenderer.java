package net.diyarnagibaster.huntrio.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.diyarnagibaster.huntrio.entity.ResearchTableBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ResearchTableRenderer implements BlockEntityRenderer<ResearchTableBlockEntity> {

    public ResearchTableRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ResearchTableBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        int lightAbove = 0;
        if (pBlockEntity.getLevel() != null)
            lightAbove = LevelRenderer.getLightColor(pBlockEntity.getLevel(), pBlockEntity.getBlockPos().above());

        ItemStack stack1 = pBlockEntity.inventory.getStackInSlot(0);
        if (!stack1.isEmpty()) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.3f, 1.05f, 0.5f);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(90f));
            pPoseStack.scale(0.5f, 0.5f, 0.5f);

            Minecraft.getInstance().getItemRenderer().renderStatic(stack1, ItemDisplayContext.FIXED, lightAbove, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 0);
            pPoseStack.popPose();
        }

        ItemStack stack2 = pBlockEntity.inventory.getStackInSlot(1);
        if (!stack2.isEmpty()) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.7f, 1.05f, 0.5f);
            pPoseStack.mulPose(Axis.XP.rotationDegrees(90f));
            pPoseStack.scale(0.5f, 0.5f, 0.5f);

            Minecraft.getInstance().getItemRenderer().renderStatic(stack2, ItemDisplayContext.FIXED, lightAbove, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pBlockEntity.getLevel(), 0);
            pPoseStack.popPose();
        }
    }
}