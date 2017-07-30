package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityImp;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelImp extends ModelBase {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer rightarm;
   ModelRenderer leftarm;
   ModelRenderer rightleg;
   ModelRenderer leftleg;
   ModelRenderer chest;
   ModelRenderer hornLeft;
   ModelRenderer hornRight;
   ModelRenderer nose;
   ModelRenderer wingRight;
   ModelRenderer wingLeft;


   public ModelImp() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-5.0F, -8.0F, -4.0F, 10, 8, 10);
      this.head.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.head.setTextureSize(64, 64);
      this.head.mirror = true;
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.body = new ModelRenderer(this, 0, 48);
      this.body.addBox(-4.0F, 0.0F, -4.0F, 8, 9, 7);
      this.body.setRotationPoint(0.0F, 9.0F, 0.0F);
      this.body.setTextureSize(64, 64);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.rightarm = new ModelRenderer(this, 41, 0);
      this.rightarm.addBox(-2.0F, -2.0F, -1.5F, 3, 13, 3);
      this.rightarm.setRotationPoint(-5.0F, 11.0F, 0.0F);
      this.rightarm.setTextureSize(64, 64);
      this.rightarm.mirror = true;
      this.setRotation(this.rightarm, 0.0F, 0.0F, 0.0F);
      this.leftarm = new ModelRenderer(this, 41, 0);
      this.leftarm.addBox(-1.0F, -2.0F, -1.5F, 3, 13, 3);
      this.leftarm.setRotationPoint(5.0F, 11.0F, 0.0F);
      this.leftarm.setTextureSize(64, 64);
      this.leftarm.mirror = true;
      this.setRotation(this.leftarm, 0.0F, 0.0F, 0.0F);
      this.rightleg = new ModelRenderer(this, 33, 48);
      this.rightleg.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3);
      this.rightleg.setRotationPoint(-1.5F, 18.0F, 0.0F);
      this.rightleg.setTextureSize(64, 64);
      this.rightleg.mirror = true;
      this.setRotation(this.rightleg, 0.0F, 0.0F, 0.0F);
      this.leftleg = new ModelRenderer(this, 33, 48);
      this.leftleg.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3);
      this.leftleg.setRotationPoint(1.5F, 18.0F, 0.0F);
      this.leftleg.setTextureSize(64, 64);
      this.leftleg.mirror = true;
      this.setRotation(this.leftleg, 0.0F, 0.0F, 0.0F);
      this.chest = new ModelRenderer(this, 4, 41);
      this.chest.addBox(-4.0F, 0.0F, -2.0F, 6, 2, 4);
      this.chest.setRotationPoint(1.0F, 8.0F, 0.0F);
      this.chest.setTextureSize(64, 64);
      this.chest.mirror = true;
      this.setRotation(this.chest, 0.0F, 0.0F, 0.0F);
      this.hornLeft = new ModelRenderer(this, 0, 21);
      this.hornLeft.addBox(-1.0F, -5.0F, -1.0F, 2, 5, 2);
      this.hornLeft.setRotationPoint(4.0F, 2.0F, 0.0F);
      this.hornLeft.setTextureSize(64, 64);
      this.hornLeft.mirror = true;
      this.setRotation(this.hornLeft, 0.4089647F, 0.0F, 0.7435722F);
      this.head.addChild(this.hornLeft);
      this.hornRight = new ModelRenderer(this, 0, 21);
      this.hornRight.addBox(-1.0F, -5.0F, -1.0F, 2, 5, 2);
      this.hornRight.setRotationPoint(-4.0F, 2.0F, 0.0F);
      this.hornRight.setTextureSize(64, 64);
      this.hornRight.mirror = true;
      this.setRotation(this.hornRight, 0.4089647F, 0.0F, -0.7435722F);
      this.head.addChild(this.hornRight);
      this.nose = new ModelRenderer(this, 9, 21);
      this.nose.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2);
      this.nose.setRotationPoint(0.0F, 3.0F, -3.0F);
      this.nose.setTextureSize(64, 64);
      this.nose.mirror = true;
      this.setRotation(this.nose, -0.9666439F, 0.0F, 0.0F);
      this.head.addChild(this.nose);
      this.wingRight = new ModelRenderer(this, 23, 21);
      this.wingRight.addBox(0.0F, 0.0F, 0.0F, 14, 21, 0);
      this.wingRight.setRotationPoint(1.0F, -8.0F, 3.0F);
      this.wingRight.setTextureSize(128, 32);
      this.wingRight.mirror = true;
      this.setRotation(this.wingRight, 0.3047198F, -0.6698132F, -0.6283185F);
      this.wingLeft = new ModelRenderer(this, 23, 21);
      this.wingLeft.addBox(0.0F, 0.0F, 0.0F, 14, 21, 0);
      this.wingLeft.setRotationPoint(-1.0F, -8.0F, 3.0F);
      this.wingLeft.setTextureSize(128, 32);
      this.wingLeft.mirror = true;
      this.setRotation(this.wingLeft, -0.3047198F, 3.811406F, 0.6283185F);
      this.wingRight.setRotationPoint(-2.0F, 10.0F, -1.0F);
      this.wingLeft.setRotationPoint(2.0F, 10.0F, -1.0F);
      this.leftleg.setRotationPoint(1.5F, 18.0F, 0.0F);
      this.rightleg.setRotationPoint(-1.5F, 18.0F, 0.0F);
      this.chest.setRotationPoint(1.0F, 8.0F, 0.0F);
      this.head.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.hornRight.setRotationPoint(-4.0F, -5.0F, 0.0F);
      this.hornLeft.setRotationPoint(4.0F, -5.0F, 0.0F);
      this.nose.setRotationPoint(0.0F, -4.0F, -3.0F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      boolean scaled = false;
      if(entity != null && entity instanceof EntityImp) {
         EntityImp imp = (EntityImp)entity;
         if(imp.isPowered()) {
            scaled = true;
            GL11.glScalef(1.5F, 1.0F, 1.5F);
         }
      }

      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.leftleg.setRotationPoint(1.5F, 18.0F, 0.0F);
      this.rightleg.setRotationPoint(-1.5F, 18.0F, 0.0F);
      this.chest.setRotationPoint(1.0F, 8.0F, 0.0F);
      this.head.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.hornRight.setRotationPoint(-4.0F, -5.0F, 0.0F);
      this.hornLeft.setRotationPoint(4.0F, -5.0F, 0.0F);
      this.nose.setRotationPoint(0.0F, -4.0F, -3.0F);
      this.head.render(f5);
      this.body.render(f5);
      this.rightarm.render(f5);
      this.leftarm.render(f5);
      this.rightleg.render(f5);
      this.leftleg.render(f5);
      this.body.render(f5);
      this.chest.render(f5);
      this.wingLeft.render(f5);
      this.wingRight.render(f5);
      if(scaled) {
         GL11.glScalef(1.0F, 1.0F, 1.0F);
      }

   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
      this.head.rotateAngleY = par4 / 57.295776F;
      this.head.rotateAngleX = par5 / 57.295776F;
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
      if(super.onGround > -9990.0F) {
         float f6 = super.onGround;
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
         float f7 = MathHelper.sin(f6 * 3.1415927F);
         float f8 = MathHelper.sin(super.onGround * 3.1415927F) * -(this.head.rotateAngleX - 0.7F) * 0.75F;
         this.rightarm.rotateAngleX = (float)((double)this.rightarm.rotateAngleX - ((double)f7 * 1.2D + (double)f8));
         this.rightarm.rotateAngleY += this.body.rotateAngleY * 2.0F;
         this.rightarm.rotateAngleZ = MathHelper.sin(super.onGround * 3.1415927F) * -0.4F;
      }

      this.body.rotateAngleX = 0.0F;
      this.rightleg.rotationPointZ = 0.1F;
      this.leftleg.rotationPointZ = 0.1F;
      this.rightleg.rotationPointY = 12.0F;
      this.leftleg.rotationPointY = 12.0F;
      this.head.rotationPointY = 0.0F;
      this.rightarm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.leftarm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.rightarm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.leftarm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
   }
}
