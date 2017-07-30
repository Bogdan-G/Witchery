package com.emoniph.witchery.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMysticBranch extends ModelBase {

   ModelRenderer Shape1;
   ModelRenderer Shape2;
   ModelRenderer Shape3;
   ModelRenderer Shape4;
   ModelRenderer Shape5;
   ModelRenderer Shape6;


   public ModelMysticBranch() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.Shape1 = new ModelRenderer(this, 12, 0);
      this.Shape1.addBox(0.0F, -4.0F, 0.0F, 1, 4, 1);
      this.Shape1.setRotationPoint(0.0F, -8.0F, 0.0F);
      this.Shape1.setTextureSize(32, 32);
      this.Shape1.mirror = true;
      this.setRotation(this.Shape1, 0.1858931F, 0.0F, 0.2230717F);
      this.Shape2 = new ModelRenderer(this, 20, 0);
      this.Shape2.addBox(-0.5F, -5.2F, -1.2F, 1, 5, 1);
      this.Shape2.setRotationPoint(1.0F, -9.0F, 1.0F);
      this.Shape2.setTextureSize(32, 32);
      this.Shape2.mirror = true;
      this.setRotation(this.Shape2, -0.2230717F, 0.0F, -0.4089647F);
      this.Shape3 = new ModelRenderer(this, 4, 0);
      this.Shape3.addBox(-1.0F, -6.0F, 0.0F, 1, 6, 1);
      this.Shape3.setRotationPoint(1.0F, -4.0F, 0.0F);
      this.Shape3.setTextureSize(32, 32);
      this.Shape3.mirror = true;
      this.setRotation(this.Shape3, 0.2230717F, -0.0371786F, 0.4089647F);
      this.Shape4 = new ModelRenderer(this, 8, 0);
      this.Shape4.addBox(-1.0F, -3.5F, -0.5F, 1, 4, 1);
      this.Shape4.setRotationPoint(1.0F, -4.9F, 1.0F);
      this.Shape4.setTextureSize(32, 32);
      this.Shape4.mirror = true;
      this.setRotation(this.Shape4, -0.5948578F, 0.0F, -0.4089647F);
      this.Shape5 = new ModelRenderer(this, 16, 0);
      this.Shape5.addBox(-0.2F, -4.8F, 0.4F, 1, 5, 1);
      this.Shape5.setRotationPoint(1.0F, -12.0F, -1.0F);
      this.Shape5.setTextureSize(32, 32);
      this.Shape5.mirror = true;
      this.setRotation(this.Shape5, -0.3717861F, 0.0F, 0.0F);
      this.Shape6 = new ModelRenderer(this, 0, 0);
      this.Shape6.addBox(0.0F, -8.0F, 0.0F, 1, 8, 1);
      this.Shape6.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.Shape6.setTextureSize(32, 32);
      this.Shape6.mirror = true;
      this.setRotation(this.Shape6, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.Shape1.render(f5);
      this.Shape2.render(f5);
      this.Shape3.render(f5);
      this.Shape4.render(f5);
      this.Shape5.render(f5);
      this.Shape6.render(f5);
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
