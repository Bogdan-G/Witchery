package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityReflection;
import com.emoniph.witchery.entity.EntityWolfman;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelWolfman extends ModelBase {

   public ModelRenderer headMain;
   public ModelRenderer bodyUpper;
   public ModelRenderer legRightUpper;
   public ModelRenderer legLeftUpper;
   public ModelRenderer armLeft;
   public ModelRenderer armRight;
   public ModelRenderer tail;
   public ModelRenderer bodyLower;
   public ModelRenderer legRightLower;
   public ModelRenderer legLeftLower;
   public int heldItemLeft;
   public int heldItemRight;
   public boolean isSneak;
   public boolean aimedBow;


   public ModelWolfman() {
      this(0.0F);
   }

   public ModelWolfman(float scale) {
      super.textureWidth = 64;
      super.textureHeight = 64;
      float headScale = 0.05F;
      this.headMain = new ModelRenderer(this, 0, 0);
      this.headMain.addBox(-3.0F, -6.0F, -2.0F, 6, 6, 4, 0.05F);
      this.headMain.setRotationPoint(0.0F, 0.0F, -2.0F);
      float f = 0.0F;
      this.headMain.setTextureOffset(16, 14).addBox(-3.0F, -8.0F, 1.0F, 2, 2, 1, 0.0F);
      this.headMain.setTextureOffset(16, 14).addBox(1.0F, -8.0F, 1.0F, 2, 2, 1, 0.0F);
      this.headMain.setTextureOffset(0, 10).addBox(-1.5F, -3.1F, -5.0F, 3, 3, 4, 0.0F);
      this.bodyUpper = new ModelRenderer(this, 0, 35);
      this.bodyUpper.setRotationPoint(0.0F, -0.1F, -2.0F);
      this.bodyUpper.addBox(-5.0F, 0.0F, -3.9F, 10, 7, 8, scale);
      this.setRotateAngle(this.bodyUpper, 0.4098033F, 0.0F, 0.0F);
      this.bodyLower = new ModelRenderer(this, 3, 50);
      this.bodyLower.setRotationPoint(0.0F, 5.0F, -1.5F);
      this.bodyLower.addBox(-4.0F, 2.0F, -2.3F, 8, 7, 5, scale);
      this.bodyUpper.addChild(this.bodyLower);
      this.tail = new ModelRenderer(this, 55, 52);
      this.tail.setRotationPoint(0.0F, 11.9F, 3.6F);
      this.tail.addBox(-1.0F, 0.0F, -1.0F, 2, 10, 2, scale);
      this.setRotateAngle(this.tail, 0.59184116F, 0.0F, 0.0F);
      this.legLeftUpper = new ModelRenderer(this, 38, 0);
      this.legLeftUpper.mirror = true;
      this.legLeftUpper.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.legLeftUpper.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, scale);
      this.setRotateAngle(this.legLeftUpper, -0.4098033F, 0.0F, 0.0F);
      this.legLeftLower = new ModelRenderer(this, 38, 13);
      this.legLeftLower.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.legLeftLower.addBox(-2.0F, 3.5F, 2.0F, 4, 8, 4, scale);
      this.legLeftUpper.addChild(this.legLeftLower);
      this.legRightUpper = new ModelRenderer(this, 38, 0);
      this.legRightUpper.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.legRightUpper.addBox(-2.0F, 0.0F, -2.0F, 4, 7, 4, scale);
      this.setRotateAngle(this.legRightUpper, -0.4098033F, 0.0F, 0.0F);
      this.legRightLower = new ModelRenderer(this, 38, 13);
      this.legRightLower.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.legRightLower.addBox(-2.0F, 3.5F, 2.0F, 4, 8, 4, scale);
      this.legRightUpper.addChild(this.legRightLower);
      this.armLeft = new ModelRenderer(this, 38, 46);
      this.armLeft.mirror = true;
      this.armLeft.setRotationPoint(6.0F, 2.0F, 0.0F);
      this.armLeft.addBox(-1.0F, -2.0F, -2.0F, 4, 14, 4, scale);
      this.armRight = new ModelRenderer(this, 38, 46);
      this.armRight.setRotationPoint(-5.8F, 2.0F, 0.0F);
      this.armRight.addBox(-3.0F, -2.0F, -2.0F, 4, 14, 4, scale);
   }

   public void render(Entity entity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, entity);
      this.headMain.render(p_78088_7_);
      this.bodyUpper.render(p_78088_7_);
      this.armRight.render(p_78088_7_);
      this.legLeftUpper.render(p_78088_7_);
      this.tail.render(p_78088_7_);
      this.armLeft.render(p_78088_7_);
      this.legRightUpper.render(p_78088_7_);
   }

   public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }

   public void setLivingAnimations(EntityLivingBase entity, float par2, float par3, float par4) {
      float i = 0.0F;
      if(entity instanceof EntityWolfman) {
         EntityWolfman wolfman = (EntityWolfman)entity;
         i = (float)wolfman.getAttackTimer();
         super.isRiding = wolfman.isSitting();
      } else if(entity instanceof EntityReflection) {
         EntityReflection wolfman1 = (EntityReflection)entity;
         i = (float)wolfman1.getAttackTimer();
      }

      if(i > 0.0F) {
         this.armRight.rotateAngleX = -2.0F + 1.5F * this.interpolateRotation(i - par4, 10.0F);
         this.armLeft.rotateAngleX = -1.0F + 0.9F * this.interpolateRotation(i - par4, 10.0F);
      } else {
         this.armRight.rotateAngleX = MathHelper.cos(par2 * 0.6662F + 3.1415927F) * 2.0F * par3 * 0.5F;
         this.armLeft.rotateAngleX = MathHelper.cos(par2 * 0.6662F) * 2.0F * par3 * 0.5F;
      }

   }

   private float interpolateRotation(float par1, float par2) {
      return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
      this.headMain.rotateAngleY = p_78087_4_ / 57.295776F;
      this.headMain.rotateAngleX = p_78087_5_ / 57.295776F;
      this.armRight.rotateAngleZ = 0.0F;
      this.armLeft.rotateAngleZ = 0.0F;
      this.legRightUpper.rotateAngleX = Math.max(-0.4098033F + MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_, -0.8F);
      this.legLeftUpper.rotateAngleX = Math.max(-0.4098033F + MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.4F * p_78087_2_, -0.8F);
      this.legRightUpper.rotateAngleY = 0.0F;
      this.legLeftUpper.rotateAngleY = 0.0F;
      if(super.isRiding) {
         this.armRight.rotateAngleX += -0.62831855F;
         this.armLeft.rotateAngleX += -0.62831855F;
         this.legRightUpper.rotateAngleX = -1.2566371F;
         this.legLeftUpper.rotateAngleX = -1.2566371F;
         this.legRightUpper.rotateAngleY = 0.31415927F;
         this.legLeftUpper.rotateAngleY = -0.31415927F;
      }

      if(this.heldItemLeft != 0) {
         this.armLeft.rotateAngleX = this.armLeft.rotateAngleX * 0.5F - 0.31415927F * (float)this.heldItemLeft;
      }

      if(this.heldItemRight != 0) {
         this.armRight.rotateAngleX = this.armRight.rotateAngleX * 0.5F - 0.31415927F * (float)this.heldItemRight;
      }

      this.armRight.rotateAngleY = 0.0F;
      this.armLeft.rotateAngleY = 0.0F;
      float f6;
      float f7;
      if(super.onGround > -9990.0F) {
         f6 = super.onGround;
         this.bodyUpper.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.1415927F * 2.0F) * 0.2F;
         this.armRight.rotationPointZ = MathHelper.sin(this.bodyUpper.rotateAngleY) * 5.0F;
         this.armRight.rotationPointX = -MathHelper.cos(this.bodyUpper.rotateAngleY) * 5.0F;
         this.armLeft.rotationPointZ = -MathHelper.sin(this.bodyUpper.rotateAngleY) * 5.0F;
         this.armLeft.rotationPointX = MathHelper.cos(this.bodyUpper.rotateAngleY) * 5.0F;
         this.armRight.rotateAngleY += this.bodyUpper.rotateAngleY;
         this.armLeft.rotateAngleY += this.bodyUpper.rotateAngleY;
         this.armLeft.rotateAngleX += this.bodyUpper.rotateAngleY;
         f6 = 1.0F - super.onGround;
         f6 *= f6;
         f6 *= f6;
         f6 = 1.0F - f6;
         f7 = MathHelper.sin(f6 * 3.1415927F);
         float f8 = MathHelper.sin(super.onGround * 3.1415927F) * -(this.headMain.rotateAngleX - 0.7F) * 0.75F;
         this.armRight.rotateAngleX = (float)((double)this.armRight.rotateAngleX - ((double)f7 * 1.2D + (double)f8));
         this.armRight.rotateAngleY += this.bodyUpper.rotateAngleY * 2.0F;
         this.armRight.rotateAngleZ = MathHelper.sin(super.onGround * 3.1415927F) * -0.4F;
      }

      if(this.isSneak) {
         this.bodyUpper.rotateAngleX = 0.5F;
         this.armRight.rotateAngleX += 0.4F;
         this.armLeft.rotateAngleX += 0.4F;
         this.legRightUpper.rotationPointZ = 4.0F;
         this.legLeftUpper.rotationPointZ = 4.0F;
         this.legRightUpper.rotationPointY = 9.0F;
         this.legLeftUpper.rotationPointY = 9.0F;
         this.headMain.rotationPointY = 0.0F;
      } else {
         this.setRotateAngle(this.bodyUpper, 0.4098033F, 0.0F, 0.0F);
         this.legRightUpper.rotationPointZ = 0.1F;
         this.legLeftUpper.rotationPointZ = 0.1F;
         this.legRightUpper.rotationPointY = 12.0F;
         this.legLeftUpper.rotationPointY = 12.0F;
         this.headMain.rotationPointY = 0.0F;
      }

      this.setRotateAngle(this.tail, 0.59184116F, 0.0F, 0.0F);
      if((double)p_78087_2_ > 0.1D) {
         this.tail.rotateAngleX = (float)((double)this.tail.rotateAngleX + ((double)p_78087_2_ - 0.1D));
         this.tail.rotateAngleZ += 5.0F * MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
      } else {
         this.tail.rotateAngleZ += 3.0F * MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
      }

      this.armRight.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
      this.armLeft.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
      this.armRight.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
      this.armLeft.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
      if(this.aimedBow) {
         f6 = 0.0F;
         f7 = 0.0F;
         this.armRight.rotateAngleZ = 0.0F;
         this.armLeft.rotateAngleZ = 0.0F;
         this.armRight.rotateAngleY = -(0.1F - f6 * 0.6F) + this.headMain.rotateAngleY;
         this.armLeft.rotateAngleY = 0.1F - f6 * 0.6F + this.headMain.rotateAngleY + 0.4F;
         this.armRight.rotateAngleX = -1.5707964F + this.headMain.rotateAngleX;
         this.armLeft.rotateAngleX = -1.5707964F + this.headMain.rotateAngleX;
         this.armRight.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
         this.armLeft.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
         this.armRight.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
         this.armLeft.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
         this.armRight.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
         this.armLeft.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
      }

   }
}
