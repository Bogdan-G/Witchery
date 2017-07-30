package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityLeonard;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelLeonard extends ModelBase {

   private final ModelRenderer head;
   private final ModelRenderer snout;
   private final ModelRenderer beard;
   private final ModelRenderer earLeft;
   private final ModelRenderer earRight;
   private final ModelRenderer hornLeft;
   private final ModelRenderer hornMiddle;
   private final ModelRenderer hornRight;
   private final ModelRenderer neck;
   private final ModelRenderer body;
   private final ModelRenderer gownLowerRight;
   private final ModelRenderer rightarm;
   private final ModelRenderer leftarm;
   private final ModelRenderer rightleg;
   private final ModelRenderer leftleg;
   private final ModelRenderer gownLowerLeft;


   public ModelLeonard() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.neck = new ModelRenderer(this, 48, 0);
      this.neck.addBox(-2.0F, -1.0F, -2.0F, 4, 2, 4);
      this.neck.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.neck.setTextureSize(64, 64);
      this.neck.mirror = true;
      this.setRotation(this.neck, 0.1745329F, 0.0F, 0.0F);
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-3.0F, -5.0F, -1.0F, 6, 4, 4);
      this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.head.setTextureSize(64, 64);
      this.head.mirror = true;
      this.neck.addChild(this.head);
      this.setRotation(this.head, 0.1745329F, 0.0F, 0.0F);
      this.snout = new ModelRenderer(this, 16, 2);
      this.snout.addBox(-2.0F, -5.0F, -7.0F, 4, 4, 7);
      this.snout.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.snout.setTextureSize(64, 64);
      this.snout.mirror = true;
      this.setRotation(this.snout, 0.1745329F, 0.0F, 0.0F);
      this.head.addChild(this.snout);
      this.beard = new ModelRenderer(this, 0, 10);
      this.beard.addBox(-2.0F, -0.2F, -7.0F, 4, 2, 2);
      this.beard.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.beard.setTextureSize(64, 64);
      this.beard.mirror = true;
      this.setRotation(this.beard, -0.0113601F, 0.0F, 0.0F);
      this.head.addChild(this.beard);
      this.earLeft = new ModelRenderer(this, 38, 0);
      this.earLeft.addBox(3.5F, 1.0F, -0.5F, 1, 3, 1);
      this.earLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.earLeft.setTextureSize(64, 64);
      this.earLeft.mirror = true;
      this.setRotation(this.earLeft, -0.5129616F, -0.2617994F, -1.180008F);
      this.head.addChild(this.earLeft);
      this.earRight = new ModelRenderer(this, 38, 0);
      this.earRight.addBox(-4.5F, 1.0F, 0.5F, 1, 3, 1);
      this.earRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.earRight.setTextureSize(64, 64);
      this.earRight.mirror = true;
      this.setRotation(this.earRight, -0.3346075F, 0.0371786F, 1.226894F);
      this.head.addChild(this.earRight);
      this.hornLeft = new ModelRenderer(this, 43, 0);
      this.hornLeft.addBox(-0.5F, -12.0F, -0.5F, 1, 8, 1);
      this.hornLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.hornLeft.setTextureSize(64, 64);
      this.hornLeft.mirror = true;
      this.setRotation(this.hornLeft, -0.2268928F, 0.0F, 0.3665191F);
      this.head.addChild(this.hornLeft);
      this.hornMiddle = new ModelRenderer(this, 43, 0);
      this.hornMiddle.addBox(-0.5F, -10.0F, -0.5F, 1, 6, 1);
      this.hornMiddle.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.hornMiddle.setTextureSize(64, 64);
      this.hornMiddle.mirror = true;
      this.setRotation(this.hornMiddle, -0.2974289F, 0.0F, 0.0F);
      this.head.addChild(this.hornMiddle);
      this.hornRight = new ModelRenderer(this, 43, 0);
      this.hornRight.addBox(-0.5F, -12.0F, -0.5F, 1, 8, 1);
      this.hornRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.hornRight.setTextureSize(64, 64);
      this.hornRight.mirror = true;
      this.setRotation(this.hornRight, -0.2268928F, 0.0F, -0.3665191F);
      this.head.addChild(this.hornRight);
      this.body = new ModelRenderer(this, 16, 16);
      this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
      this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.body.setTextureSize(64, 64);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.gownLowerRight = new ModelRenderer(this, 0, 33);
      this.gownLowerRight.addBox(-5.0F, 12.0F, -2.5F, 5, 11, 5);
      this.gownLowerRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.gownLowerRight.setTextureSize(64, 64);
      this.gownLowerRight.mirror = true;
      this.setRotation(this.gownLowerRight, 0.0F, 0.0F, 0.0F);
      this.rightarm = new ModelRenderer(this, 40, 16);
      this.rightarm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
      this.rightarm.setRotationPoint(-5.0F, 2.0F, 0.0F);
      this.rightarm.setTextureSize(64, 64);
      this.rightarm.mirror = true;
      this.setRotation(this.rightarm, 0.0F, 0.0F, 0.0F);
      this.leftarm = new ModelRenderer(this, 40, 16);
      this.leftarm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
      this.leftarm.setRotationPoint(5.0F, 2.0F, 0.0F);
      this.leftarm.setTextureSize(64, 64);
      this.leftarm.mirror = true;
      this.setRotation(this.leftarm, 0.0F, 0.0F, 0.0F);
      this.rightleg = new ModelRenderer(this, 0, 16);
      this.rightleg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.rightleg.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.rightleg.setTextureSize(64, 64);
      this.rightleg.mirror = true;
      this.setRotation(this.rightleg, 0.0F, 0.0F, 0.0F);
      this.rightleg.addChild(this.gownLowerRight);
      this.leftleg = new ModelRenderer(this, 0, 16);
      this.leftleg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.leftleg.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.leftleg.setTextureSize(64, 64);
      this.leftleg.mirror = true;
      this.setRotation(this.leftleg, 0.0F, 0.0F, 0.0F);
      this.gownLowerLeft = new ModelRenderer(this, 21, 33);
      this.gownLowerLeft.addBox(0.0F, 12.0F, -2.5F, 5, 11, 5);
      this.gownLowerLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.gownLowerLeft.setTextureSize(64, 64);
      this.gownLowerLeft.mirror = true;
      this.setRotation(this.gownLowerLeft, 0.0F, 0.0F, 0.0F);
      this.leftleg.addChild(this.gownLowerLeft);
      this.neck.rotateAngleX = 0.1745329F;
      this.head.rotateAngleX = 0.1745329F;
      this.setRotation(this.earRight, -0.3346075F, 0.0371786F, 1.226894F);
      this.gownLowerLeft.setRotationPoint(-2.0F, -12.0F, 0.0F);
      this.gownLowerRight.setRotationPoint(2.0F, -12.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.neck.render(f5);
      this.body.render(f5);
      this.rightarm.render(f5);
      this.leftarm.render(f5);
      this.rightleg.render(f5);
      this.leftleg.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      this.neck.rotateAngleY = par4 / 57.295776F;
      this.neck.rotateAngleX = par5 / 57.295776F;
      this.rightarm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 2.0F * par2 * 0.5F;
      this.leftarm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
      this.rightarm.rotateAngleZ = 0.0F;
      this.leftarm.rotateAngleZ = 0.0F;
      this.rightleg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
      this.leftleg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 1.4F * par2;
      this.rightleg.rotateAngleY = 0.0F;
      this.leftleg.rotateAngleY = 0.0F;
      if(super.isRiding) {
         this.rightarm.rotateAngleX += -0.62831855F;
         this.leftarm.rotateAngleX += -0.62831855F;
         this.rightleg.rotateAngleX = -1.2566371F;
         this.leftleg.rotateAngleX = -1.2566371F;
         this.rightleg.rotateAngleY = 0.31415927F;
         this.leftleg.rotateAngleY = -0.31415927F;
      }

      this.rightarm.rotateAngleY = 0.0F;
      this.leftarm.rotateAngleY = 0.0F;
      float f6;
      float f7;
      if(super.onGround > -9990.0F) {
         f6 = super.onGround;
         this.body.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.1415927F * 2.0F) * 0.2F;
         this.rightarm.rotationPointZ = MathHelper.sin(this.body.rotateAngleY) * 5.0F;
         this.rightarm.rotationPointX = -MathHelper.cos(this.body.rotateAngleY) * 5.0F;
         this.leftarm.rotationPointZ = -MathHelper.sin(this.body.rotateAngleY) * 5.0F;
         this.leftarm.rotationPointX = MathHelper.cos(this.body.rotateAngleY) * 5.0F;
         this.rightarm.rotateAngleY += this.body.rotateAngleY;
         this.leftarm.rotateAngleY += this.body.rotateAngleY;
         this.leftarm.rotateAngleX += this.body.rotateAngleY;
         f6 = 1.0F - super.onGround;
         f6 *= f6;
         f6 *= f6;
         f6 = 1.0F - f6;
         f7 = MathHelper.sin(f6 * 3.1415927F);
         float sneaking = MathHelper.sin(super.onGround * 3.1415927F) * -(this.neck.rotateAngleX - 0.7F) * 0.75F;
         this.rightarm.rotateAngleX = (float)((double)this.rightarm.rotateAngleX - ((double)f7 * 1.2D + (double)sneaking));
         this.rightarm.rotateAngleY += this.body.rotateAngleY * 2.0F;
         this.rightarm.rotateAngleZ = MathHelper.sin(super.onGround * 3.1415927F) * -0.4F;
      }

      boolean sneaking1 = false;
      if(sneaking1) {
         this.body.rotateAngleX = 0.5F;
         this.rightarm.rotateAngleX += 0.4F;
         this.leftarm.rotateAngleX += 0.4F;
         this.rightleg.rotationPointZ = 4.0F;
         this.leftleg.rotationPointZ = 4.0F;
         this.rightleg.rotationPointY = 9.0F;
         this.leftleg.rotationPointY = 9.0F;
         this.neck.rotationPointY = 1.0F;
      } else {
         this.body.rotateAngleX = 0.0F;
         this.rightleg.rotationPointZ = 0.1F;
         this.leftleg.rotationPointZ = 0.1F;
         this.rightleg.rotationPointY = 12.0F;
         this.leftleg.rotationPointY = 12.0F;
         this.neck.rotationPointY = 0.0F;
      }

      this.rightarm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.leftarm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.rightarm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.leftarm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      boolean shootingBow = false;
      if(shootingBow) {
         f6 = 0.0F;
         f7 = 0.0F;
         this.rightarm.rotateAngleZ = 0.0F;
         this.leftarm.rotateAngleZ = 0.0F;
         this.rightarm.rotateAngleY = -(0.1F - f6 * 0.6F) + this.neck.rotateAngleY;
         this.leftarm.rotateAngleY = 0.1F - f6 * 0.6F + this.neck.rotateAngleY + 0.4F;
         this.rightarm.rotateAngleX = -1.5707964F + this.neck.rotateAngleX;
         this.leftarm.rotateAngleX = -1.5707964F + this.neck.rotateAngleX;
         this.rightarm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
         this.leftarm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
         this.rightarm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
         this.leftarm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
         this.rightarm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
         this.leftarm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      }

      EntityLeonard entityDemon = (EntityLeonard)entity;
      int i = entityDemon.getAttackTimer();
      if(i > 0) {
         float di = 10.0F;
         this.rightarm.rotateAngleX = -2.0F + 1.5F * (Math.abs(((float)i - par4) % 10.0F - di * 0.5F) - di * 0.25F) / (di * 0.25F);
      }

   }
}
