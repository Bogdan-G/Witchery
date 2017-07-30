package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelHuntsmanSpear extends ModelBase {

   ModelRenderer shaft;
   ModelRenderer headFront;
   ModelRenderer headSide;


   public ModelHuntsmanSpear() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.shaft = new ModelRenderer(this, 0, 0);
      this.shaft.addBox(-0.5F, -18.0F, -0.5F, 1, 32, 1);
      this.shaft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.shaft.setTextureSize(64, 64);
      this.shaft.mirror = true;
      this.setRotation(this.shaft, 0.0F, 0.0F, 0.0F);
      this.headFront = new ModelRenderer(this, 6, 3);
      this.headFront.addBox(-1.5F, -6.0F, 0.0F, 3, 6, 0);
      this.headFront.setRotationPoint(0.0F, -17.0F, 0.0F);
      this.headFront.setTextureSize(64, 64);
      this.headFront.mirror = true;
      this.setRotation(this.headFront, 0.0F, 0.0F, 0.0F);
      this.headSide = new ModelRenderer(this, 6, 0);
      this.headSide.addBox(0.0F, -6.0F, -1.5F, 0, 6, 3);
      this.headSide.setRotationPoint(0.0F, -17.0F, 0.0F);
      this.headSide.setTextureSize(64, 64);
      this.headSide.mirror = true;
      this.setRotation(this.headSide, 0.0F, 0.0F, 0.0F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.shaft.render(f5);
      this.headFront.render(f5);
      this.headSide.render(f5);
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }
}
