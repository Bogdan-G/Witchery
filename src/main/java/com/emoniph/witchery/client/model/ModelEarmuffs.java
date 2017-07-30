package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelEarmuffs extends ModelBiped {

   private ModelRenderer earRight;
   private ModelRenderer earLeft;
   private ModelRenderer bandLeft;
   private ModelRenderer bandTop;
   private ModelRenderer bandRight;


   public ModelEarmuffs() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.bandTop = new ModelRenderer(this, 46, 38);
      this.bandTop.addBox(-4.0F, -10.0F, -0.5F, 8, 1, 1);
      this.bandTop.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bandTop.setTextureSize(64, 64);
      this.bandTop.mirror = true;
      this.setRotation(this.bandTop, 0.0F, 0.0F, 0.0F);
      this.earRight = new ModelRenderer(this, 33, 32);
      this.earRight.addBox(-6.0F, -6.0F, -2.0F, 2, 4, 4);
      this.earRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.earRight.setTextureSize(64, 64);
      this.earRight.mirror = true;
      this.setRotation(this.earRight, 0.0F, 0.0F, 0.0F);
      this.earLeft = new ModelRenderer(this, 33, 32);
      this.earLeft.addBox(4.0F, -6.0F, -2.0F, 2, 4, 4);
      this.earLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.earLeft.setTextureSize(64, 64);
      this.earLeft.mirror = true;
      this.setRotation(this.earLeft, 0.0F, 0.0F, 0.0F);
      this.bandLeft = new ModelRenderer(this, 46, 32);
      this.bandLeft.addBox(4.0F, -10.0F, -0.5F, 1, 4, 1);
      this.bandLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bandLeft.setTextureSize(64, 64);
      this.bandLeft.mirror = true;
      this.setRotation(this.bandLeft, 0.0F, 0.0F, 0.0F);
      this.bandRight = new ModelRenderer(this, 46, 32);
      this.bandRight.addBox(-5.0F, -10.0F, -0.5F, 1, 4, 1);
      this.bandRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bandRight.setTextureSize(64, 64);
      this.bandRight.mirror = true;
      this.setRotation(this.bandRight, 0.0F, 0.0F, 0.0F);
      super.bipedHead.addChild(this.earRight);
      super.bipedHead.addChild(this.earLeft);
      super.bipedHead.addChild(this.bandLeft);
      super.bipedHead.addChild(this.bandRight);
      super.bipedHead.addChild(this.bandTop);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }
}
