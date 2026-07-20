package net.diyarnagibaster.huntrio.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.diyarnagibaster.huntrio.HunTrio;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class DroneMK1<T extends Entity> extends EntityModel<T> {
	// ИСПРАВЛЕНО ДЛЯ 1.21.1: Используем fromNamespaceAndPath
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(HunTrio.MODID, "dronemk1"), "main"
	);

	private final ModelPart bb_main;

	public DroneMK1(ModelPart root) {
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create()
						.texOffs(0, 18).addBox(3.0F, -2.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 18).addBox(-7.0F, -2.0F, 4.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 18).addBox(-7.0F, -2.0F, -6.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 25).addBox(3.0F, -2.0F, 4.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
						.texOffs(0, 4).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}