package com.emoniph.witchery.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGrassper extends ModelBase {

   ModelRenderer stalkTop;
   ModelRenderer leafRight;
   ModelRenderer leafFront;
   ModelRenderer leafback;
   ModelRenderer leafLeft;
   ModelRenderer petalBackRight;
   ModelRenderer stalkBottom;
   ModelRenderer petalFrontRight;
   ModelRenderer petalBackLeft;
   ModelRenderer petalFrontLeft;


   public ModelGrassper() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.stalkTop = new ModelRenderer(this, 0, 4);
      this.stalkTop.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 2);
      this.stalkTop.setRotationPoint(2.0F, 21.0F, 0.0F);
      this.stalkTop.setTextureSize(64, 64);
      this.stalkTop.mirror = true;
      this.setRotation(this.stalkTop, 0.0F, 0.0F, -0.5235988F);
      this.leafRight = new ModelRenderer(this, 0, 8);
      this.leafRight.addBox(0.0F, 0.0F, -4.0F, 8, 0, 8);
      this.leafRight.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.leafRight.setTextureSize(64, 64);
      this.leafRight.mirror = true;
      this.leafFront = new ModelRenderer(this, 0, 0);
      this.leafFront.addBox(-4.0F, 0.0F, -8.0F, 8, 0, 8);
      this.leafFront.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.leafFront.setTextureSize(64, 64);
      this.leafFront.mirror = true;
      this.leafback = new ModelRenderer(this, 0, 0);
      this.leafback.addBox(-4.0F, 0.0F, -8.0F, 8, 0, 8);
      this.leafback.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.leafback.setTextureSize(64, 64);
      this.leafback.mirror = true;
      this.leafLeft = new ModelRenderer(this, 0, 8);
      this.leafLeft.addBox(0.0F, 0.0F, -4.0F, 8, 0, 8);
      this.leafLeft.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.leafLeft.setTextureSize(64, 64);
      this.leafLeft.mirror = true;
      this.petalBackRight = new ModelRenderer(this, 0, 0);
      this.petalBackRight.addBox(-1.0F, -2.0F, 0.0F, 1, 2, 1);
      this.petalBackRight.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.petalBackRight.setTextureSize(64, 64);
      this.petalBackRight.mirror = true;
      this.setRotation(this.petalBackRight, -0.5235988F, 0.0F, -0.7853982F);
      this.stalkBottom = new ModelRenderer(this, 0, 10);
      this.stalkBottom.addBox(-1.0F, -4.0F, -1.0F, 2, 4, 2);
      this.stalkBottom.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.stalkBottom.setTextureSize(64, 64);
      this.stalkBottom.mirror = true;
      this.setRotation(this.stalkBottom, 0.0F, 0.0F, 0.5235988F);
      this.petalFrontRight = new ModelRenderer(this, 0, 0);
      this.petalFrontRight.addBox(-1.0F, -2.0F, -1.0F, 1, 2, 1);
      this.petalFrontRight.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.petalFrontRight.setTextureSize(64, 64);
      this.petalFrontRight.mirror = true;
      this.setRotation(this.petalFrontRight, 0.5235988F, 0.0F, -0.7853982F);
      this.petalBackLeft = new ModelRenderer(this, 0, 0);
      this.petalBackLeft.addBox(0.0F, -2.0F, 0.0F, 1, 2, 1);
      this.petalBackLeft.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.petalBackLeft.setTextureSize(64, 64);
      this.petalBackLeft.mirror = true;
      this.setRotation(this.petalBackLeft, -0.3490659F, 0.0F, 0.2617994F);
      this.petalFrontLeft = new ModelRenderer(this, 0, 0);
      this.petalFrontLeft.addBox(0.0F, -2.0F, -1.0F, 1, 2, 1);
      this.petalFrontLeft.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.petalFrontLeft.setTextureSize(64, 64);
      this.petalFrontLeft.mirror = true;
      this.setRotation(this.petalFrontLeft, 0.3490659F, 0.0F, 0.2617994F);
      this.setRotation(this.leafRight, 0.0F, 3.141593F, 0.5235988F);
      this.setRotation(this.leafLeft, 0.0F, 0.0F, -0.5235988F);
      this.setRotation(this.leafFront, -0.5235988F, 0.0F, 0.0F);
      this.setRotation(this.leafback, -0.5235988F, -3.141593F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.stalkTop.render(f5);
      this.leafRight.render(f5);
      this.leafFront.render(f5);
      this.leafback.render(f5);
      this.leafLeft.render(f5);
      this.petalBackRight.render(f5);
      this.stalkBottom.render(f5);
      this.petalFrontRight.render(f5);
      this.petalBackLeft.render(f5);
      this.petalFrontLeft.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {}
}
