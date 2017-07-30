package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelDeath extends ModelBase {

   ModelRenderer bipedHead;
   ModelRenderer bipedBody;
   ModelRenderer bipedRightArm;
   ModelRenderer bipedLeftArm;
   ModelRenderer bipedRightLeg;
   ModelRenderer bipedLeftLeg;
   ModelRenderer robe;
   ModelRenderer scythe;


   public ModelDeath() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.setTextureOffset("scythe.shaft", 58, 5);
      this.setTextureOffset("scythe.blade", 36, 0);
      this.bipedHead = new ModelRenderer(this, 27, 43);
      this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 10, 8);
      this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bipedHead.setTextureSize(64, 64);
      this.bipedHead.mirror = true;
      this.setRotation(this.bipedHead, 0.0F, 0.0F, 0.0F);
      this.bipedBody = new ModelRenderer(this, 16, 16);
      this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
      this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bipedBody.setTextureSize(64, 64);
      this.bipedBody.mirror = true;
      this.setRotation(this.bipedBody, 0.0F, 0.0F, 0.0F);
      this.bipedRightArm = new ModelRenderer(this, 40, 16);
      this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
      this.bipedRightArm.setTextureSize(64, 64);
      this.bipedRightArm.mirror = true;
      this.setRotation(this.bipedRightArm, 0.0F, 0.0F, 0.0F);
      this.bipedLeftArm = new ModelRenderer(this, 40, 16);
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
      this.bipedLeftArm.setTextureSize(64, 64);
      this.bipedLeftArm.mirror = true;
      this.setRotation(this.bipedLeftArm, 0.0F, 0.0F, 0.0F);
      this.bipedRightLeg = new ModelRenderer(this, 0, 16);
      this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2);
      this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.bipedRightLeg.setTextureSize(64, 64);
      this.bipedRightLeg.mirror = true;
      this.setRotation(this.bipedRightLeg, 0.0F, 0.0F, 0.0F);
      this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
      this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2);
      this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.bipedLeftLeg.setTextureSize(64, 64);
      this.bipedLeftLeg.mirror = true;
      this.setRotation(this.bipedLeftLeg, 0.0F, 0.0F, 0.0F);
      this.robe = new ModelRenderer(this, 0, 33);
      this.robe.addBox(-4.0F, 0.0F, -2.5F, 8, 23, 5);
      this.robe.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.robe.setTextureSize(64, 64);
      this.robe.mirror = true;
      this.setRotation(this.robe, 0.0F, 0.0F, 0.0F);
      this.scythe = new ModelRenderer(this, "scythe");
      this.scythe.setRotationPoint(-6.0F, 10.0F, 0.0F);
      this.setRotation(this.scythe, 0.0F, 0.0F, 0.0F);
      this.scythe.mirror = true;
      this.scythe.addBox("shaft", -0.5F, -16.0F, -0.5F, 1, 35, 1);
      this.scythe.addBox("blade", 0.0F, -16.0F, 0.0F, 13, 4, 0);
      this.bipedRightArm.addChild(this.scythe);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.bipedHead.render(f5);
      this.bipedBody.render(f5);
      this.bipedRightArm.render(f5);
      this.bipedLeftArm.render(f5);
      this.bipedRightLeg.render(f5);
      this.bipedLeftLeg.render(f5);
      GL11.glScalef(1.05F, 1.0F, 1.05F);
      this.robe.render(f5);
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
      this.scythe.rotationPointX = -0.8F;
      this.scythe.rotationPointZ = 0.0F;
      this.scythe.rotationPointY = 8.1F;
      this.scythe.rotateAngleX = 1.5707964F;
      this.bipedHead.rotateAngleY = par4 / 57.295776F;
      this.bipedHead.rotateAngleX = par5 / 57.295776F;
      this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 2.0F * par2 * 0.5F - 1.5707964F;
      this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
      this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 1.4F * par2;
      this.bipedRightLeg.rotateAngleY = 0.0F;
      this.bipedLeftLeg.rotateAngleY = 0.0F;
      this.bipedRightArm.rotateAngleY = 0.0F;
      this.bipedLeftArm.rotateAngleY = 0.0F;
      if(super.onGround > -9990.0F) {
         float f6 = super.onGround;
         this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f6) * 3.1415927F * 2.0F) * 0.2F;
         this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
         this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
         this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
         f6 = 1.0F - super.onGround;
         f6 *= f6;
         f6 *= f6;
         f6 = 1.0F - f6;
         float f7 = MathHelper.sin(f6 * 3.1415927F);
         float f8 = MathHelper.sin(super.onGround * 3.1415927F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
         this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)f7 * 1.2D + (double)f8));
         this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
         this.bipedRightArm.rotateAngleZ = MathHelper.sin(super.onGround * 3.1415927F) * -0.4F;
      }

      this.bipedBody.rotateAngleX = 0.0F;
      this.bipedRightLeg.rotationPointZ = 0.1F;
      this.bipedLeftLeg.rotationPointZ = 0.1F;
      this.bipedRightLeg.rotationPointY = 12.0F;
      this.bipedLeftLeg.rotationPointY = 12.0F;
      this.bipedHead.rotationPointY = 0.0F;
      this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
   }
}
