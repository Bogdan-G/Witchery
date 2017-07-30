package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelCandelabra extends ModelBase {

   ModelRenderer candleLeft;
   ModelRenderer candleRight;
   ModelRenderer candleFront;
   ModelRenderer candleBack;
   ModelRenderer candleMiddle;
   ModelRenderer supportLR;
   ModelRenderer supportFB;
   ModelRenderer sconceLeft;
   ModelRenderer sconceRight;
   ModelRenderer sconceFront;
   ModelRenderer sconceBack;
   ModelRenderer sconceMiddle;
   ModelRenderer baseTop;
   ModelRenderer baseBottom;


   public ModelCandelabra() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.candleLeft = new ModelRenderer(this, 0, 0);
      this.candleLeft.addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
      this.candleLeft.setRotationPoint(-6.0F, 11.0F, -1.0F);
      this.candleLeft.setTextureSize(32, 32);
      this.candleLeft.mirror = true;
      this.setRotation(this.candleLeft, 0.0F, 0.0F, 0.0F);
      this.candleRight = new ModelRenderer(this, 0, 0);
      this.candleRight.addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
      this.candleRight.setRotationPoint(4.0F, 11.0F, -1.0F);
      this.candleRight.setTextureSize(32, 32);
      this.candleRight.mirror = true;
      this.setRotation(this.candleRight, 0.0F, 0.0F, 0.0F);
      this.candleFront = new ModelRenderer(this, 0, 0);
      this.candleFront.addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
      this.candleFront.setRotationPoint(-1.0F, 11.0F, -6.0F);
      this.candleFront.setTextureSize(32, 32);
      this.candleFront.mirror = true;
      this.setRotation(this.candleFront, 0.0F, 0.0F, 0.0F);
      this.candleBack = new ModelRenderer(this, 0, 0);
      this.candleBack.addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
      this.candleBack.setRotationPoint(-1.0F, 11.0F, 4.0F);
      this.candleBack.setTextureSize(32, 32);
      this.candleBack.mirror = true;
      this.setRotation(this.candleBack, 0.0F, 0.0F, 0.0F);
      this.candleMiddle = new ModelRenderer(this, 0, 0);
      this.candleMiddle.addBox(0.0F, 0.0F, 0.0F, 2, 13, 2);
      this.candleMiddle.setRotationPoint(-1.0F, 9.0F, -1.0F);
      this.candleMiddle.setTextureSize(32, 32);
      this.candleMiddle.mirror = true;
      this.setRotation(this.candleMiddle, 0.0F, 0.0F, 0.0F);
      this.supportLR = new ModelRenderer(this, 0, 17);
      this.supportLR.addBox(0.0F, 0.0F, 0.0F, 12, 1, 2);
      this.supportLR.setRotationPoint(-6.0F, 19.0F, -1.0F);
      this.supportLR.setTextureSize(32, 32);
      this.supportLR.mirror = true;
      this.setRotation(this.supportLR, 0.0F, 0.0F, 0.0F);
      this.supportFB = new ModelRenderer(this, 0, 4);
      this.supportFB.addBox(0.0F, 0.0F, 0.0F, 2, 1, 12);
      this.supportFB.setRotationPoint(-1.0F, 19.0F, -6.0F);
      this.supportFB.setTextureSize(32, 32);
      this.supportFB.mirror = true;
      this.setRotation(this.supportFB, 0.0F, 0.0F, 0.0F);
      this.sconceLeft = new ModelRenderer(this, 0, 20);
      this.sconceLeft.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
      this.sconceLeft.setRotationPoint(-6.5F, 17.0F, -1.5F);
      this.sconceLeft.setTextureSize(32, 32);
      this.sconceLeft.mirror = true;
      this.setRotation(this.sconceLeft, 0.0F, 0.0F, 0.0F);
      this.sconceRight = new ModelRenderer(this, 0, 20);
      this.sconceRight.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
      this.sconceRight.setRotationPoint(3.5F, 17.0F, -1.5F);
      this.sconceRight.setTextureSize(32, 32);
      this.sconceRight.mirror = true;
      this.setRotation(this.sconceRight, 0.0F, 0.0F, 0.0F);
      this.sconceFront = new ModelRenderer(this, 0, 20);
      this.sconceFront.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
      this.sconceFront.setRotationPoint(-1.5F, 17.0F, -6.5F);
      this.sconceFront.setTextureSize(32, 32);
      this.sconceFront.mirror = true;
      this.setRotation(this.sconceFront, 0.0F, 0.0F, 0.0F);
      this.sconceBack = new ModelRenderer(this, 0, 20);
      this.sconceBack.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
      this.sconceBack.setRotationPoint(-1.5F, 17.0F, 3.5F);
      this.sconceBack.setTextureSize(32, 32);
      this.sconceBack.mirror = true;
      this.setRotation(this.sconceBack, 0.0F, 0.0F, 0.0F);
      this.sconceMiddle = new ModelRenderer(this, 0, 20);
      this.sconceMiddle.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
      this.sconceMiddle.setRotationPoint(-1.5F, 15.0F, -1.5F);
      this.sconceMiddle.setTextureSize(32, 32);
      this.sconceMiddle.mirror = true;
      this.setRotation(this.sconceMiddle, 0.0F, 0.0F, 0.0F);
      this.baseTop = new ModelRenderer(this, 12, 20);
      this.baseTop.addBox(0.0F, 0.0F, 0.0F, 3, 1, 3);
      this.baseTop.setRotationPoint(-1.5F, 22.0F, -1.5F);
      this.baseTop.setTextureSize(32, 32);
      this.baseTop.mirror = true;
      this.setRotation(this.baseTop, 0.0F, 0.0F, 0.0F);
      this.baseBottom = new ModelRenderer(this, 8, 24);
      this.baseBottom.addBox(-2.5F, 0.0F, -2.5F, 5, 1, 5);
      this.baseBottom.setRotationPoint(0.0F, 23.0F, 0.0F);
      this.baseBottom.setTextureSize(32, 32);
      this.baseBottom.mirror = true;
      this.setRotation(this.baseBottom, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.candleLeft.render(f5);
      this.candleRight.render(f5);
      this.candleFront.render(f5);
      this.candleBack.render(f5);
      this.candleMiddle.render(f5);
      this.supportLR.render(f5);
      this.supportFB.render(f5);
      this.sconceLeft.render(f5);
      this.sconceRight.render(f5);
      this.sconceFront.render(f5);
      this.sconceBack.render(f5);
      this.sconceMiddle.render(f5);
      this.baseTop.render(f5);
      this.baseBottom.render(f5);
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
