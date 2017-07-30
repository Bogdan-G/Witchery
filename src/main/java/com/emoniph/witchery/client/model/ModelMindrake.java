package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelMindrake extends ModelBase {

   ModelRenderer leaves;
   ModelRenderer bodyTop;
   ModelRenderer bodyBottom;
   ModelRenderer legLeft;
   ModelRenderer legRight;


   public ModelMindrake() {
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.leaves = new ModelRenderer(this, 0, 0);
      this.leaves.addBox(-3.0F, 0.0F, 0.0F, 6, 6, 0);
      this.leaves.setRotationPoint(0.0F, 13.0F, 0.0F);
      this.leaves.setTextureSize(64, 32);
      this.leaves.mirror = true;
      this.setRotation(this.leaves, 0.0F, 0.0F, 0.0F);
      this.bodyTop = new ModelRenderer(this, 0, 7);
      this.bodyTop.addBox(-1.5F, 0.0F, -1.5F, 3, 1, 3);
      this.bodyTop.setRotationPoint(0.0F, 19.0F, 0.0F);
      this.bodyTop.setTextureSize(64, 32);
      this.bodyTop.mirror = true;
      this.setRotation(this.bodyTop, 0.0F, 0.0F, 0.0F);
      this.bodyBottom = new ModelRenderer(this, 0, 12);
      this.bodyBottom.addBox(-2.0F, 0.0F, -2.0F, 4, 3, 4);
      this.bodyBottom.setRotationPoint(0.0F, 20.0F, 0.0F);
      this.bodyBottom.setTextureSize(64, 32);
      this.bodyBottom.mirror = true;
      this.setRotation(this.bodyBottom, 0.0F, 0.0F, 0.0F);
      this.legLeft = new ModelRenderer(this, 0, 20);
      this.legLeft.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1);
      this.legLeft.setRotationPoint(1.0F, 23.0F, 0.0F);
      this.legLeft.setTextureSize(64, 32);
      this.legLeft.mirror = true;
      this.setRotation(this.legLeft, 0.0F, 0.0F, 0.0F);
      this.legRight = new ModelRenderer(this, 0, 20);
      this.legRight.addBox(-0.5F, 0.0F, -0.5F, 1, 1, 1);
      this.legRight.setRotationPoint(-1.0F, 23.0F, 0.0F);
      this.legRight.setTextureSize(64, 32);
      this.legRight.mirror = true;
      this.setRotation(this.legRight, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.leaves.render(f5);
      this.bodyTop.render(f5);
      this.bodyBottom.render(f5);
      this.legLeft.render(f5);
      this.legRight.render(f5);
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
