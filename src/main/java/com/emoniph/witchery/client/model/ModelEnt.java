package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityEnt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelEnt extends ModelBase {

   ModelRenderer ArmLeft;
   ModelRenderer ArmRight;
   ModelRenderer Body;
   ModelRenderer Face;
   ModelRenderer Leg8;
   ModelRenderer Leg6;
   ModelRenderer Leg4;
   ModelRenderer Leg2;
   ModelRenderer Leg7;
   ModelRenderer Leg5;
   ModelRenderer Leg3;
   ModelRenderer Leg1;
   ModelRenderer LeavesBase;
   ModelRenderer LeavesTop;
   ModelRenderer LeavesBaseInner;
   ModelRenderer LeavesTopInner;


   public ModelEnt() {
      super.textureWidth = 256;
      super.textureHeight = 256;
      this.ArmLeft = new ModelRenderer(this, 82, 0);
      this.ArmLeft.addBox(0.0F, -22.0F, -3.0F, 6, 24, 6);
      this.ArmLeft.setRotationPoint(8.0F, -4.0F, 0.0F);
      this.ArmLeft.setTextureSize(256, 256);
      this.ArmLeft.mirror = true;
      this.setRotation(this.ArmLeft, 0.0F, 0.0F, 0.0F);
      this.ArmRight = new ModelRenderer(this, 82, 0);
      this.ArmRight.addBox(-6.0F, -22.0F, -3.0F, 6, 24, 6);
      this.ArmRight.setRotationPoint(-8.0F, -4.0F, 0.0F);
      this.ArmRight.setTextureSize(256, 256);
      this.ArmRight.mirror = true;
      this.setRotation(this.ArmRight, 0.0F, 0.0F, 0.0F);
      this.Body = new ModelRenderer(this, 0, 50);
      this.Body.addBox(-8.0F, -46.0F, -8.0F, 16, 48, 16);
      this.Body.setRotationPoint(0.0F, 20.0F, 0.0F);
      this.Body.setTextureSize(256, 256);
      this.Body.mirror = true;
      this.setRotation(this.Body, 0.0F, 0.0F, 0.0F);
      this.Face = new ModelRenderer(this, 0, 116);
      this.Face.addBox(-8.0F, -46.0F, -9.0F, 16, 24, 16);
      this.Face.setRotationPoint(0.0F, 20.0F, 0.0F);
      this.Face.setTextureSize(256, 256);
      this.Face.mirror = true;
      this.setRotation(this.Face, 0.0F, 0.0F, 0.0F);
      this.Leg8 = new ModelRenderer(this, 18, 0);
      this.Leg8.addBox(-3.0F, -1.0F, -1.0F, 16, 2, 2);
      this.Leg8.setRotationPoint(4.0F, 20.0F, -1.0F);
      this.Leg8.setTextureSize(256, 256);
      this.Leg8.mirror = true;
      this.setRotation(this.Leg8, 0.0F, 0.5759587F, 0.1919862F);
      this.Leg6 = new ModelRenderer(this, 18, 0);
      this.Leg6.addBox(-3.0F, -1.0F, -1.0F, 16, 2, 2);
      this.Leg6.setRotationPoint(4.0F, 20.0F, 0.0F);
      this.Leg6.setTextureSize(256, 256);
      this.Leg6.mirror = true;
      this.setRotation(this.Leg6, 0.0F, 0.2792527F, 0.1919862F);
      this.Leg4 = new ModelRenderer(this, 18, 0);
      this.Leg4.addBox(-3.0F, -1.0F, -1.0F, 16, 2, 2);
      this.Leg4.setRotationPoint(4.0F, 20.0F, 1.0F);
      this.Leg4.setTextureSize(256, 256);
      this.Leg4.mirror = true;
      this.setRotation(this.Leg4, 0.0F, -0.2792527F, 0.1919862F);
      this.Leg2 = new ModelRenderer(this, 18, 0);
      this.Leg2.addBox(-3.0F, -1.0F, -1.0F, 16, 2, 2);
      this.Leg2.setRotationPoint(4.0F, 20.0F, 2.0F);
      this.Leg2.setTextureSize(256, 256);
      this.Leg2.mirror = true;
      this.setRotation(this.Leg2, 0.0F, -0.5759587F, 0.1919862F);
      this.Leg7 = new ModelRenderer(this, 18, 0);
      this.Leg7.addBox(-13.0F, -1.0F, -1.0F, 16, 2, 2);
      this.Leg7.setRotationPoint(-4.0F, 20.0F, -1.0F);
      this.Leg7.setTextureSize(256, 256);
      this.Leg7.mirror = true;
      this.setRotation(this.Leg7, 0.0F, -0.5759587F, -0.1919862F);
      this.Leg5 = new ModelRenderer(this, 18, 0);
      this.Leg5.addBox(-13.0F, -1.0F, -1.0F, 16, 2, 2);
      this.Leg5.setRotationPoint(-4.0F, 20.0F, 0.0F);
      this.Leg5.setTextureSize(256, 256);
      this.Leg5.mirror = true;
      this.setRotation(this.Leg5, 0.0F, -0.2792527F, -0.1919862F);
      this.Leg3 = new ModelRenderer(this, 18, 0);
      this.Leg3.addBox(-13.0F, -1.0F, -1.0F, 16, 2, 2);
      this.Leg3.setRotationPoint(-4.0F, 20.0F, 1.0F);
      this.Leg3.setTextureSize(256, 256);
      this.Leg3.mirror = true;
      this.setRotation(this.Leg3, 0.0F, 0.2792527F, -0.1919862F);
      this.Leg1 = new ModelRenderer(this, 18, 0);
      this.Leg1.addBox(-13.0F, -1.0F, -1.0F, 16, 2, 2);
      this.Leg1.setRotationPoint(-4.0F, 20.0F, 2.0F);
      this.Leg1.setTextureSize(256, 256);
      this.Leg1.mirror = true;
      this.setRotation(this.Leg1, 0.0F, 0.5759587F, -0.1919862F);
      this.LeavesBase = new ModelRenderer(this, 0, 180);
      this.LeavesBase.addBox(0.0F, 0.0F, 0.0F, 60, 16, 60);
      this.LeavesBase.setRotationPoint(-30.0F, -42.0F, -30.0F);
      this.LeavesBase.setTextureSize(256, 256);
      this.LeavesBase.mirror = true;
      this.setRotation(this.LeavesBase, 0.0F, 0.0F, 0.0F);
      this.LeavesTop = new ModelRenderer(this, 56, 130);
      this.LeavesTop.addBox(0.0F, 0.0F, 0.0F, 32, 16, 32);
      this.LeavesTop.setRotationPoint(-16.0F, -58.0F, -16.0F);
      this.LeavesTop.setTextureSize(256, 256);
      this.LeavesTop.mirror = true;
      this.setRotation(this.LeavesTop, 0.0F, 0.0F, 0.0F);
      this.LeavesBaseInner = new ModelRenderer(this, 24, 59);
      this.LeavesBaseInner.addBox(0.0F, 0.0F, 0.0F, 56, 14, 56);
      this.LeavesBaseInner.setRotationPoint(-28.0F, -41.0F, -28.0F);
      this.LeavesBaseInner.setTextureSize(64, 32);
      this.LeavesBaseInner.mirror = true;
      this.setRotation(this.LeavesBaseInner, 0.0F, 0.0F, 0.0F);
      this.LeavesTopInner = new ModelRenderer(this, 108, 14);
      this.LeavesTopInner.addBox(0.0F, 0.0F, 0.0F, 28, 14, 28);
      this.LeavesTopInner.setRotationPoint(-14.0F, -57.0F, -14.0F);
      this.LeavesTopInner.setTextureSize(64, 32);
      this.LeavesTopInner.mirror = true;
      this.setRotation(this.LeavesTopInner, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.ArmLeft.render(f5);
      this.ArmRight.render(f5);
      this.Body.render(f5);
      this.Leg8.render(f5);
      this.Leg6.render(f5);
      this.Leg4.render(f5);
      this.Leg2.render(f5);
      this.Leg7.render(f5);
      this.Leg5.render(f5);
      this.Leg3.render(f5);
      this.Leg1.render(f5);
      this.LeavesBaseInner.render(f5);
      this.LeavesTopInner.render(f5);
      this.LeavesBase.render(f5);
      this.LeavesTop.render(f5);
      if(entity != null && entity instanceof EntityEnt && ((EntityEnt)entity).isScreaming()) {
         this.Face.render(f5);
      }

   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
      EntityEnt entity = (EntityEnt)par1EntityLiving;
      int i = entity.getAttackTimer();
      if(i > 0) {
         this.ArmRight.rotateAngleX = 3.0F - 1.3F * this.func_78172_a((float)i - par4, 10.0F);
         this.ArmLeft.rotateAngleX = 2.5F - 1.2F * this.func_78172_a((float)i - par4, 10.0F);
      } else {
         this.ArmRight.rotateAngleX = 0.0F;
         this.ArmLeft.rotateAngleX = 0.0F;
         this.ArmRight.rotateAngleZ = (-0.2F + 0.1F * this.func_78172_a(par2, 13.0F)) * par3 - 0.1F;
         this.ArmLeft.rotateAngleZ = (0.2F - 0.1F * this.func_78172_a(par2, 13.0F)) * par3 + 0.1F;
      }

   }

   private float func_78172_a(float par1, float par2) {
      return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
   }

   public void setRotationAngles(float par1, float par2, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(par1, par2, f2, f3, f4, f5, entity);
      float f6 = 0.7853982F;
      this.Leg1.rotateAngleZ = -f6;
      this.Leg2.rotateAngleZ = f6;
      this.Leg3.rotateAngleZ = -f6 * 0.74F;
      this.Leg4.rotateAngleZ = f6 * 0.74F;
      this.Leg5.rotateAngleZ = -f6 * 0.74F;
      this.Leg6.rotateAngleZ = f6 * 0.74F;
      this.Leg7.rotateAngleZ = -f6;
      this.Leg8.rotateAngleZ = f6;
      float f7 = -0.0F;
      float f8 = 0.3926991F;
      this.Leg1.rotateAngleY = f8 * 2.0F + f7;
      this.Leg2.rotateAngleY = -f8 * 2.0F - f7;
      this.Leg3.rotateAngleY = f8 * 1.0F + f7;
      this.Leg4.rotateAngleY = -f8 * 1.0F - f7;
      this.Leg5.rotateAngleY = -f8 * 1.0F + f7;
      this.Leg6.rotateAngleY = f8 * 1.0F - f7;
      this.Leg7.rotateAngleY = -f8 * 2.0F + f7;
      this.Leg8.rotateAngleY = f8 * 2.0F - f7;
      float f9 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 0.0F) * 0.4F) * par2;
      float f10 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 3.1415927F) * 0.4F) * par2;
      float f11 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 1.5707964F) * 0.4F) * par2;
      float f12 = -(MathHelper.cos(par1 * 0.6662F * 2.0F + 4.712389F) * 0.4F) * par2;
      float f13 = Math.abs(MathHelper.sin(par1 * 0.6662F + 0.0F) * 0.4F) * par2;
      float f14 = Math.abs(MathHelper.sin(par1 * 0.6662F + 3.1415927F) * 0.4F) * par2;
      float f15 = Math.abs(MathHelper.sin(par1 * 0.6662F + 1.5707964F) * 0.4F) * par2;
      float f16 = Math.abs(MathHelper.sin(par1 * 0.6662F + 4.712389F) * 0.4F) * par2;
      this.Leg1.rotateAngleY += f9;
      this.Leg2.rotateAngleY += -f9;
      this.Leg3.rotateAngleY += f10;
      this.Leg4.rotateAngleY += -f10;
      this.Leg5.rotateAngleY += f11;
      this.Leg6.rotateAngleY += -f11;
      this.Leg7.rotateAngleY += f12;
      this.Leg8.rotateAngleY += -f12;
      this.Leg1.rotateAngleZ += f13;
      this.Leg2.rotateAngleZ += -f13;
      this.Leg3.rotateAngleZ += f14;
      this.Leg4.rotateAngleZ += -f14;
      this.Leg5.rotateAngleZ += f15;
      this.Leg6.rotateAngleZ += -f15;
      this.Leg7.rotateAngleZ += f16;
      this.Leg8.rotateAngleZ += -f16;
   }
}
