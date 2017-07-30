package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelVampire extends ModelBiped {

   public ModelRenderer bipedHeadNose;
   public ModelRenderer bipedBodyRobe;


   public ModelVampire() {
      super(0.0F, 0.0F, 64, 64);
      super.bipedHead = new ModelRenderer(this, 28, 46);
      super.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
      super.bipedHead.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
      this.bipedHeadNose = new ModelRenderer(this, 52, 46);
      this.bipedHeadNose.setRotationPoint(0.0F, -2.0F, 0.0F);
      this.bipedHeadNose.addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);
      this.bipedBodyRobe = new ModelRenderer(this, 0, 38);
      this.bipedBodyRobe.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bipedBodyRobe.addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, 0.5F);
      super.bipedHead.addChild(this.bipedHeadNose);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
   }

   public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
      super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
      super.bipedHeadwear.rotationPointY += 0.3F;
   }
}
