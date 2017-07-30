package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityPoltergeist;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelPoltergeist extends ModelBase {

   ModelRenderer bipedHead;
   ModelRenderer bipedBody;
   ModelRenderer bipedRightArm;
   ModelRenderer bipedRightArm2;
   ModelRenderer bipedLeftArm;
   ModelRenderer bipedLeftArm2;
   ModelRenderer bipedRightLeg;
   ModelRenderer bipedLeftLeg;


   public ModelPoltergeist() {
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.bipedHead = new ModelRenderer(this, 0, 0);
      this.bipedHead.addBox(-4.0F, -8.0F, -3.0F, 8, 8, 6);
      this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bipedHead.setTextureSize(64, 32);
      this.bipedHead.mirror = true;
      this.setRotation(this.bipedHead, 0.0F, 0.0F, 0.0F);
      this.bipedBody = new ModelRenderer(this, 16, 16);
      this.bipedBody.addBox(-4.0F, 0.0F, -1.0F, 8, 11, 2);
      this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bipedBody.setTextureSize(64, 32);
      this.bipedBody.mirror = true;
      this.setRotation(this.bipedBody, 0.0F, 0.0F, 0.0F);
      this.bipedRightArm = new ModelRenderer(this, 40, 0);
      this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 18, 2);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
      this.bipedRightArm.setTextureSize(64, 32);
      this.bipedRightArm.mirror = true;
      this.setRotation(this.bipedRightArm, 0.0F, 0.0F, 0.0F);
      this.bipedRightArm2 = new ModelRenderer(this, 40, 0);
      this.bipedRightArm2.addBox(-1.0F, -2.0F, -1.0F, 2, 18, 2);
      this.bipedRightArm2.setRotationPoint(-5.0F, 2.0F, 0.0F);
      this.bipedRightArm2.setTextureSize(64, 32);
      this.bipedRightArm2.mirror = true;
      this.setRotation(this.bipedRightArm2, 0.0F, 0.0F, 0.0F);
      this.bipedLeftArm = new ModelRenderer(this, 40, 0);
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 18, 2);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
      this.bipedLeftArm.setTextureSize(64, 32);
      this.bipedLeftArm.mirror = true;
      this.setRotation(this.bipedLeftArm, 0.0F, 0.0F, 0.0F);
      this.bipedLeftArm2 = new ModelRenderer(this, 40, 0);
      this.bipedLeftArm2.addBox(-1.0F, -2.0F, -1.0F, 2, 18, 2);
      this.bipedLeftArm2.setRotationPoint(5.0F, 2.0F, 0.0F);
      this.bipedLeftArm2.setTextureSize(64, 32);
      this.bipedLeftArm2.mirror = true;
      this.setRotation(this.bipedLeftArm2, 0.0F, 0.0F, 0.0F);
      this.bipedRightLeg = new ModelRenderer(this, 0, 16);
      this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 13, 2);
      this.bipedRightLeg.setRotationPoint(-2.0F, 11.0F, 0.0F);
      this.bipedRightLeg.setTextureSize(64, 32);
      this.bipedRightLeg.mirror = true;
      this.setRotation(this.bipedRightLeg, 0.0F, 0.0F, 0.0F);
      this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
      this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 13, 2);
      this.bipedLeftLeg.setRotationPoint(2.0F, 11.0F, 0.0F);
      this.bipedLeftLeg.setTextureSize(64, 32);
      this.bipedLeftLeg.mirror = true;
      this.setRotation(this.bipedLeftLeg, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.bipedHead.render(f5);
      this.bipedBody.render(f5);
      this.bipedRightArm.render(f5);
      this.bipedRightArm2.render(f5);
      this.bipedLeftArm.render(f5);
      this.bipedLeftArm2.render(f5);
      this.bipedRightLeg.render(f5);
      this.bipedLeftLeg.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
      this.bipedHead.rotateAngleY = par4 / 57.295776F;
      this.bipedHead.rotateAngleX = par5 / 57.295776F;
      this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 2.0F * par2 * 0.5F;
      this.bipedRightArm2.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 2.0F * par2 * 0.25F;
      this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
      this.bipedLeftArm2.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.25F;
      this.bipedRightArm.rotateAngleY = 0.0F;
      this.bipedRightArm2.rotateAngleY = 0.0F;
      this.bipedLeftArm.rotateAngleY = 0.0F;
      this.bipedLeftArm2.rotateAngleY = 0.0F;
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedRightArm2.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm2.rotateAngleZ = 0.0F;
      this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
      this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 1.4F * par2;
      this.bipedRightLeg.rotateAngleY = 0.0F;
      this.bipedLeftLeg.rotateAngleY = 0.0F;
      this.bipedBody.rotateAngleX = 0.0F;
      this.bipedRightLeg.rotationPointZ = 0.1F;
      this.bipedLeftLeg.rotationPointZ = 0.1F;
      this.bipedRightLeg.rotationPointY = 12.0F;
      this.bipedLeftLeg.rotationPointY = 12.0F;
      this.bipedHead.rotationPointY = 0.0F;
      this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.bipedRightArm2.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.bipedLeftArm2.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.bipedRightArm2.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.bipedLeftArm2.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      EntityPoltergeist entityDemon = (EntityPoltergeist)entity;
      int i = entityDemon.getAttackTimer();
      if(i > 0) {
         this.bipedRightArm.rotateAngleX = -1.5F + 0.8F * this.func_78172_a((float)i - par4, 15.0F);
         this.bipedLeftArm.rotateAngleX = -1.5F + 0.8F * this.func_78172_a((float)i - par4, 15.0F);
         this.bipedRightArm2.rotateAngleX = -1.5F + 0.8F * this.func_78172_a((float)i - par4, 15.0F);
         this.bipedLeftArm2.rotateAngleX = -1.5F + 0.8F * this.func_78172_a((float)i - par4, 15.0F);
         this.bipedRightArm.rotateAngleZ = -(-1.5F + 1.5F * this.func_78172_a((float)i - par4, 15.0F));
         this.bipedLeftArm.rotateAngleZ = -1.5F + 1.5F * this.func_78172_a((float)i - par4, 15.0F);
      }

   }

   public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {}

   private float func_78172_a(float par1, float par2) {
      return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
   }
}
