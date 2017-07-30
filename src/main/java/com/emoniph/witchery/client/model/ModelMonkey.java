package com.emoniph.witchery.client.model;

import com.emoniph.witchery.client.model.ModelOwl;
import com.emoniph.witchery.entity.EntityWingedMonkey;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelMonkey extends ModelBase {

   private ModelRenderer tail;
   private ModelRenderer armLeft;
   private ModelRenderer legRight;
   private ModelRenderer bodyShoulder;
   private ModelRenderer body;
   private ModelRenderer legLeft;
   private ModelRenderer head;
   private ModelRenderer armRight;
   private ModelRenderer wingRight;
   private ModelRenderer wingLeft;
   private ModelRenderer headFace;
   private ModelRenderer headNose;
   private ModelRenderer headEarLeft;
   private ModelRenderer headEarRight;


   public ModelMonkey() {
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.headEarRight = new ModelRenderer(this, 18, 14);
      this.headEarRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.headEarRight.addBox(-4.5F, -2.5F, -1.5F, 2, 3, 1, 0.0F);
      this.tail = new ModelRenderer(this, 18, 23);
      this.tail.addBox(-0.5F, -7.8F, -0.5F, 1, 8, 1, 0.0F);
      this.tail.setRotationPoint(0.0F, 18.5F, 5.3F);
      this.setRotateAngle(this.tail, -0.63739425F, 0.0F, 0.0F);
      this.armRight = new ModelRenderer(this, 0, 19);
      this.armRight.setRotationPoint(-3.5F, 14.0F, 0.0F);
      this.armRight.addBox(-2.0F, -1.1F, -1.0F, 2, 11, 2, 0.0F);
      this.setRotateAngle(this.armRight, -0.18203785F, 0.0F, 0.0F);
      this.legRight = new ModelRenderer(this, 9, 25);
      this.legRight.setRotationPoint(-1.4F, 19.0F, 4.7F);
      this.legRight.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2, 0.0F);
      this.bodyShoulder = new ModelRenderer(this, 32, 0);
      this.bodyShoulder.setRotationPoint(0.0F, 16.0F, 1.0F);
      this.bodyShoulder.addBox(-3.5F, -3.0F, -3.0F, 7, 5, 7, 0.0F);
      this.setRotateAngle(this.bodyShoulder, 1.5707964F, 0.0F, 0.0F);
      this.wingRight = new ModelRenderer(this, 28, 25);
      this.wingRight.setRotationPoint(-1.0F, 14.0F, 2.5F);
      this.wingRight.addBox(-12.0F, -0.5F, -3.0F, 12, 1, 6, 0.0F);
      this.setRotateAngle(this.wingRight, -0.68294734F, 0.3642502F, 0.5462881F);
      this.headEarLeft = new ModelRenderer(this, 18, 14);
      this.headEarLeft.mirror = true;
      this.headEarLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.headEarLeft.addBox(2.5F, -2.5F, -1.5F, 2, 3, 1, 0.0F);
      this.headFace = new ModelRenderer(this, 5, 12);
      this.headFace.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.headFace.addBox(-2.5F, -3.5F, -3.5F, 5, 5, 1, 0.0F);
      this.body = new ModelRenderer(this, 36, 12);
      this.body.setRotationPoint(0.0F, 15.8F, 2.0F);
      this.body.addBox(-2.5F, -2.0F, -3.0F, 5, 7, 5, 0.0F);
      this.setRotateAngle(this.body, 0.59184116F, 0.0F, 0.0F);
      this.legLeft = new ModelRenderer(this, 9, 25);
      this.legLeft.mirror = true;
      this.legLeft.setRotationPoint(1.4F, 19.0F, 4.7F);
      this.legLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2, 0.0F);
      this.armLeft = new ModelRenderer(this, 0, 19);
      this.armLeft.mirror = true;
      this.armLeft.setRotationPoint(4.0F, 14.0F, 0.0F);
      this.armLeft.addBox(-0.5F, -1.0F, -1.0F, 2, 11, 2, 0.0F);
      this.setRotateAngle(this.armLeft, -0.18203785F, 0.0F, 0.0F);
      this.wingLeft = new ModelRenderer(this, 28, 25);
      this.wingLeft.mirror = true;
      this.wingLeft.setRotationPoint(1.0F, 14.0F, 2.5F);
      this.wingLeft.addBox(0.0F, -0.5F, -3.0F, 12, 1, 6, 0.0F);
      this.setRotateAngle(this.wingLeft, -0.68294734F, -0.3642502F, -0.5462881F);
      this.head = new ModelRenderer(this, 0, 0);
      this.head.setRotationPoint(0.0F, 12.0F, -1.5F);
      this.head.addBox(-3.0F, -4.0F, -3.0F, 6, 6, 5, 0.0F);
      this.headNose = new ModelRenderer(this, 9, 19);
      this.headNose.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.headNose.addBox(-2.0F, -1.5F, -4.5F, 4, 3, 1, 0.0F);
      this.head.addChild(this.headEarRight);
      this.head.addChild(this.headEarLeft);
      this.head.addChild(this.headFace);
      this.head.addChild(this.headNose);
   }

   private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      EntityWingedMonkey entitybat = (EntityWingedMonkey)entity;
      this.armRight.rotateAngleZ = 0.0F;
      this.armLeft.rotateAngleZ = 0.0F;
      this.legRight.rotateAngleZ = 0.0F;
      this.legLeft.rotateAngleZ = 0.0F;
      boolean landed = entity.motionY == 0.0D && entity.motionX == 0.0D && entity.motionZ == 0.0D && ModelOwl.isLanded(entity);
      if(landed) {
         this.setRotateAngle(this.wingLeft, -0.68294734F, -0.3642502F, -0.5462881F);
         this.setRotateAngle(this.wingRight, -0.68294734F, 0.3642502F, 0.5462881F);
         this.armLeft.rotateAngleX = -0.18203785F;
         this.armRight.rotateAngleX = -0.18203785F;
         this.legLeft.rotateAngleX = 0.0F;
         this.legRight.rotateAngleX = 0.0F;
         this.wingLeft.rotationPointY = 14.0F;
         this.wingRight.rotationPointY = 14.0F;
      } else {
         this.wingRight.rotateAngleZ = MathHelper.cos(f2 * 0.5F) * 3.1415927F * 0.2F * 2.0F + 0.2F;
         this.wingRight.rotateAngleX = 0.0F;
         this.wingRight.rotationPointY = 12.0F;
         this.wingLeft.rotateAngleZ = -this.wingRight.rotateAngleZ;
         this.wingLeft.rotateAngleX = 0.0F;
         this.wingLeft.rotationPointY = 12.0F;
         this.armLeft.rotateAngleX = 0.2F;
         this.armRight.rotateAngleX = 0.2F;
         this.legLeft.rotateAngleX = 0.1F;
         this.legRight.rotateAngleX = 0.1F;
         this.armRight.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
         this.armLeft.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
         this.armRight.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
         this.armLeft.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
         this.legRight.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
         this.legLeft.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
      }

      if(entitybat.isSitting()) {
         this.legRight.rotateAngleX = -1.3F;
         this.legRight.rotationPointZ = 2.0F;
         this.legRight.rotationPointY = 21.0F;
         this.legLeft.rotateAngleX = -1.3F;
         this.legLeft.rotationPointZ = 2.0F;
         this.legLeft.rotationPointY = 21.0F;
         this.body.rotateAngleX = 0.1F;
         this.body.rotationPointY = 17.0F;
         this.tail.setRotationPoint(0.0F, 18.5F, 5.3F);
         this.tail.rotationPointZ = 4.0F;
         this.tail.rotationPointY = 20.0F;
         this.setRotateAngle(this.tail, -0.9F, 0.0F, 0.0F);
         this.armRight.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
         this.armLeft.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
         this.armRight.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
         this.armLeft.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
         if(!landed) {
            this.legRight.rotateAngleX = 0.0F;
            this.legLeft.rotateAngleX = 0.0F;
         }
      } else {
         this.body.rotateAngleX = 0.59184116F;
         this.body.setRotationPoint(0.0F, 15.8F, 2.0F);
         this.legRight.setRotationPoint(-1.4F, 19.0F, 4.7F);
         this.legLeft.setRotationPoint(1.4F, 19.0F, 4.7F);
         this.legRight.rotateAngleX = 0.0F;
         this.legLeft.rotateAngleX = 0.0F;
         this.tail.setRotationPoint(0.0F, 18.5F, 5.3F);
         this.setRotateAngle(this.tail, -0.63739425F, 0.0F, 0.0F);
      }

      if((double)f1 > 0.1D) {
         this.tail.rotateAngleX = (float)((double)this.tail.rotateAngleX + ((double)(-f1) - 0.1D));
         this.tail.rotateAngleZ += 5.0F * MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
      } else {
         this.tail.rotateAngleZ += 3.0F * MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
      }

      this.head.rotateAngleY = f3 / 57.295776F;
      this.head.rotateAngleX = f4 / 57.295776F;
      int i = entitybat.getAttackTimer();
      if(i > 0) {
         float di = 10.0F;
         this.armRight.rotateAngleX = -2.0F + 1.5F * (Math.abs(((float)i - f3) % 10.0F - di * 0.5F) - di * 0.25F) / (di * 0.25F);
      }

      this.tail.render(f5);
      this.armRight.render(f5);
      this.legRight.render(f5);
      this.bodyShoulder.render(f5);
      this.wingRight.render(f5);
      this.body.render(f5);
      this.legLeft.render(f5);
      this.armLeft.render(f5);
      this.wingLeft.render(f5);
      this.head.render(f5);
   }
}
