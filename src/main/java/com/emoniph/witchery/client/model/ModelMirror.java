package com.emoniph.witchery.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMirror extends ModelBase {

   public ModelRenderer backMiddle;
   public ModelRenderer backRight;
   public ModelRenderer backLeft;
   public ModelRenderer frameOuterRight;
   public ModelRenderer frameOuterCurveLowerRight;
   public ModelRenderer frameOuterCurveMidRight;
   public ModelRenderer frameOuterCurveUpperRight;
   public ModelRenderer frameOuterTop;
   public ModelRenderer frameOuterCurveUpperLeft;
   public ModelRenderer frameOuterCurveMidLeft;
   public ModelRenderer frameOuterCurveLowerLeft;
   public ModelRenderer frameOuterLeft;
   public ModelRenderer frameInnerRight;
   public ModelRenderer frameInnerCurveLowerRight;
   public ModelRenderer frameInnerCurveUpperRight;
   public ModelRenderer frameInnerTop;
   public ModelRenderer frameInnerCurveUpperRight_1;
   public ModelRenderer frameInnerCurveLowerLeft;
   public ModelRenderer frameInnerLeft;


   public ModelMirror() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.frameInnerRight = new ModelRenderer(this, 5, 5);
      this.frameInnerRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameInnerRight.addBox(-6.0F, -2.0F, -1.0F, 1, 10, 1, 0.0F);
      this.frameOuterCurveLowerLeft = new ModelRenderer(this, 5, 0);
      this.frameOuterCurveLowerLeft.mirror = true;
      this.frameOuterCurveLowerLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameOuterCurveLowerLeft.addBox(7.0F, -5.0F, -1.0F, 1, 2, 1, 0.0F);
      this.frameOuterLeft = new ModelRenderer(this, 0, 0);
      this.frameOuterLeft.mirror = true;
      this.frameOuterLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameOuterLeft.addBox(6.5F, -3.0F, -1.0F, 1, 11, 1, 0.0F);
      this.backRight = new ModelRenderer(this, 0, 17);
      this.backRight.setRotationPoint(0.0F, 0.0F, 1.0F);
      this.backRight.addBox(-7.0F, -6.0F, -1.0F, 2, 14, 1, 0.0F);
      this.backLeft = new ModelRenderer(this, 0, 17);
      this.backLeft.mirror = true;
      this.backLeft.setRotationPoint(0.0F, 0.0F, 1.0F);
      this.backLeft.addBox(5.0F, -6.0F, -1.0F, 2, 14, 1, 0.0F);
      this.frameOuterTop = new ModelRenderer(this, 4, 3);
      this.frameOuterTop.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameOuterTop.addBox(-5.0F, -7.5F, -1.0F, 10, 1, 1, 0.0F);
      this.frameInnerLeft = new ModelRenderer(this, 5, 5);
      this.frameInnerLeft.mirror = true;
      this.frameInnerLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameInnerLeft.addBox(5.0F, -2.0F, -1.0F, 1, 10, 1, 0.0F);
      this.frameInnerCurveLowerLeft = new ModelRenderer(this, 10, 6);
      this.frameInnerCurveLowerLeft.mirror = true;
      this.frameInnerCurveLowerLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameInnerCurveLowerLeft.addBox(4.0F, -3.0F, -1.0F, 1, 2, 1, 0.0F);
      this.frameInnerCurveLowerRight = new ModelRenderer(this, 10, 6);
      this.frameInnerCurveLowerRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameInnerCurveLowerRight.addBox(-5.0F, -3.0F, -1.0F, 1, 2, 1, 0.0F);
      this.frameOuterRight = new ModelRenderer(this, 0, 0);
      this.frameOuterRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameOuterRight.addBox(-7.5F, -3.0F, -1.0F, 1, 11, 1, 0.0F);
      this.frameOuterCurveUpperLeft = new ModelRenderer(this, 17, 0);
      this.frameOuterCurveUpperLeft.mirror = true;
      this.frameOuterCurveUpperLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameOuterCurveUpperLeft.addBox(5.0F, -7.0F, -1.0F, 1, 1, 1, 0.0F);
      this.frameInnerCurveUpperRight_1 = new ModelRenderer(this, 15, 6);
      this.frameInnerCurveUpperRight_1.mirror = true;
      this.frameInnerCurveUpperRight_1.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameInnerCurveUpperRight_1.addBox(3.0F, -4.0F, -1.0F, 1, 2, 1, 0.0F);
      this.frameOuterCurveLowerRight = new ModelRenderer(this, 5, 0);
      this.frameOuterCurveLowerRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameOuterCurveLowerRight.addBox(-8.0F, -5.0F, -1.0F, 1, 2, 1, 0.0F);
      this.frameInnerTop = new ModelRenderer(this, 10, 10);
      this.frameInnerTop.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameInnerTop.addBox(-3.0F, -4.0F, -1.0F, 6, 1, 1, 0.0F);
      this.frameInnerCurveUpperRight = new ModelRenderer(this, 15, 6);
      this.frameInnerCurveUpperRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameInnerCurveUpperRight.addBox(-4.0F, -4.0F, -1.0F, 1, 2, 1, 0.0F);
      this.frameOuterCurveMidRight = new ModelRenderer(this, 10, 0);
      this.frameOuterCurveMidRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameOuterCurveMidRight.addBox(-7.5F, -6.0F, -1.0F, 2, 1, 1, 0.0F);
      this.frameOuterCurveUpperRight = new ModelRenderer(this, 17, 0);
      this.frameOuterCurveUpperRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameOuterCurveUpperRight.addBox(-6.0F, -7.0F, -1.0F, 1, 1, 1, 0.0F);
      this.frameOuterCurveMidLeft = new ModelRenderer(this, 10, 0);
      this.frameOuterCurveMidLeft.mirror = true;
      this.frameOuterCurveMidLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.frameOuterCurveMidLeft.addBox(5.5F, -6.0F, -1.0F, 2, 1, 1, 0.0F);
      this.backMiddle = new ModelRenderer(this, 7, 16);
      this.backMiddle.setRotationPoint(0.0F, 0.0F, 1.0F);
      this.backMiddle.addBox(-5.0F, -7.0F, -1.0F, 10, 15, 1, 0.0F);
   }

   private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.frameInnerRight.render(f5);
      this.frameOuterCurveLowerLeft.render(f5);
      this.frameOuterLeft.render(f5);
      this.backRight.render(f5);
      this.backLeft.render(f5);
      this.frameOuterTop.render(f5);
      this.frameInnerLeft.render(f5);
      this.frameInnerCurveLowerLeft.render(f5);
      this.frameInnerCurveLowerRight.render(f5);
      this.frameOuterRight.render(f5);
      this.frameOuterCurveUpperLeft.render(f5);
      this.frameInnerCurveUpperRight_1.render(f5);
      this.frameOuterCurveLowerRight.render(f5);
      this.frameInnerTop.render(f5);
      this.frameInnerCurveUpperRight.render(f5);
      this.frameOuterCurveMidRight.render(f5);
      this.frameOuterCurveUpperRight.render(f5);
      this.frameOuterCurveMidLeft.render(f5);
      this.backMiddle.render(f5);
   }
}
