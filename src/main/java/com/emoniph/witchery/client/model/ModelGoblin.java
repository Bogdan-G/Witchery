package com.emoniph.witchery.client.model;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityGoblin;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelGoblin extends ModelBase {

   public ModelRenderer bipedHead;
   public ModelRenderer bipedBody;
   public ModelRenderer bipedRightArm;
   public ModelRenderer bipedLeftArm;
   public ModelRenderer bipedRightLeg;
   public ModelRenderer bipedLeftLeg;
   public int heldItemLeft;
   public int heldItemRight;
   public boolean isSneak;
   public boolean aimedBow;


   public ModelGoblin() {
      this(0.0F);
   }

   public ModelGoblin(float scale) {
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.setTextureOffset("head.face", 0, 0);
      this.setTextureOffset("head.nose1", 34, 3);
      this.setTextureOffset("head.nose2", 34, 0);
      this.setTextureOffset("head.nose3", 33, 9);
      this.setTextureOffset("head.earTipLeft", 46, 0);
      this.setTextureOffset("head.earInnerLeft", 39, 0);
      this.setTextureOffset("head.earInnerRight", 39, 0);
      this.setTextureOffset("head.earTipRight", 46, 0);
      this.bipedHead = new ModelRenderer(this, "head");
      this.bipedHead.setRotationPoint(0.0F, 11.0F, 0.0F);
      this.setRotation(this.bipedHead, 0.0F, 0.0F, 0.0F);
      this.bipedHead.mirror = true;
      this.bipedHead.addBox("face", -4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.bipedHead.addBox("nose1", -0.5F, -6.0F, -5.0F, 1, 3, 1);
      this.bipedHead.addBox("nose2", -0.5F, -5.0F, -6.0F, 1, 1, 1);
      this.bipedHead.addBox("nose3", -0.5F, -4.0F, -7.0F, 1, 2, 2);
      this.bipedHead.addBox("earTipLeft", 6.0F, -7.0F, 0.0F, 2, 2, 1);
      this.bipedHead.addBox("earInnerLeft", 4.0F, -7.0F, 0.0F, 2, 3, 1);
      this.bipedHead.addBox("earInnerRight", -6.0F, -7.0F, 0.0F, 2, 3, 1);
      this.bipedHead.addBox("earTipRight", -8.0F, -7.0F, 0.0F, 2, 2, 1);
      this.bipedBody = new ModelRenderer(this, 16, 16);
      this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 7, 4, scale);
      this.bipedBody.setRotationPoint(0.0F, 11.0F, 0.0F);
      this.bipedBody.setTextureSize(64, 32);
      this.bipedBody.mirror = true;
      this.setRotation(this.bipedBody, 0.0F, 0.0F, 0.0F);
      this.bipedRightArm = new ModelRenderer(this, 40, 16);
      this.bipedRightArm.addBox(-3.0F, -3.0F, -2.0F, 4, 12, 4, scale);
      this.bipedRightArm.setRotationPoint(-5.0F, 12.0F, 0.0F);
      this.bipedRightArm.setTextureSize(64, 32);
      this.bipedRightArm.mirror = true;
      this.setRotation(this.bipedRightArm, 0.0F, 0.0F, 0.0F);
      this.bipedLeftArm = new ModelRenderer(this, 40, 16);
      this.bipedLeftArm.addBox(-1.0F, -3.0F, -2.0F, 4, 12, 4, scale);
      this.bipedLeftArm.setRotationPoint(5.0F, 12.0F, 0.0F);
      this.bipedLeftArm.setTextureSize(64, 32);
      this.bipedLeftArm.mirror = true;
      this.setRotation(this.bipedLeftArm, 0.0F, 0.0F, 0.0F);
      this.bipedRightLeg = new ModelRenderer(this, 0, 16);
      this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, scale);
      this.bipedRightLeg.setRotationPoint(-2.0F, 18.0F, 0.0F);
      this.bipedRightLeg.setTextureSize(64, 32);
      this.bipedRightLeg.mirror = true;
      this.setRotation(this.bipedRightLeg, 0.0F, 0.0F, 0.0F);
      this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
      this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 6, 4, scale);
      this.bipedLeftLeg.setRotationPoint(2.0F, 18.0F, 0.0F);
      this.bipedLeftLeg.setTextureSize(64, 32);
      this.bipedLeftLeg.mirror = true;
      this.setRotation(this.bipedLeftLeg, 0.0F, 0.0F, 0.0F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);
      if(super.isChild) {
         float f6 = 2.0F;
         GL11.glPushMatrix();
         GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
         GL11.glTranslatef(0.0F, 16.0F * par7, 0.0F);
         this.bipedHead.render(par7);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
         GL11.glTranslatef(0.0F, 24.0F * par7, 0.0F);
         this.bipedBody.render(par7);
         this.bipedRightArm.render(par7);
         this.bipedLeftArm.render(par7);
         this.bipedRightLeg.render(par7);
         this.bipedLeftLeg.render(par7);
         GL11.glPopMatrix();
      } else {
         this.bipedHead.render(par7);
         this.bipedBody.render(par7);
         this.bipedRightArm.render(par7);
         this.bipedLeftArm.render(par7);
         this.bipedRightLeg.render(par7);
         this.bipedLeftLeg.render(par7);
      }

   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
      this.bipedHead.rotateAngleY = par4 / 57.295776F;
      this.bipedHead.rotateAngleX = par5 / 57.295776F;
      this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 2.0F * par2 * 0.5F;
      this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
      this.bipedRightArm.rotateAngleZ = 0.0F;
      this.bipedLeftArm.rotateAngleZ = 0.0F;
      this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
      this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 1.4F * par2;
      this.bipedRightLeg.rotateAngleY = 0.0F;
      this.bipedLeftLeg.rotateAngleY = 0.0F;
      if(super.isRiding) {
         this.bipedRightArm.rotateAngleX += -0.62831855F;
         this.bipedLeftArm.rotateAngleX += -0.62831855F;
         this.bipedRightLeg.rotateAngleX = -1.2566371F;
         this.bipedLeftLeg.rotateAngleX = -1.2566371F;
         this.bipedRightLeg.rotateAngleY = 0.31415927F;
         this.bipedLeftLeg.rotateAngleY = -0.31415927F;
      }

      if(this.heldItemLeft != 0) {
         this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.31415927F * (float)this.heldItemLeft;
      }

      if(this.heldItemRight != 0) {
         this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.31415927F * (float)this.heldItemRight;
         if(par7Entity != null && par7Entity instanceof EntityGoblin) {
            EntityGoblin f6 = (EntityGoblin)par7Entity;
            if(f6.isWorking()) {
               if(f6.getHeldItem() != null && f6.getHeldItem().getItem() == Witchery.Items.KOBOLDITE_PICKAXE) {
                  this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - (double)(par7Entity.ticksExisted % 6) * 0.3D);
               } else {
                  this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - (double)(par7Entity.ticksExisted % 20) * 0.1D);
               }
            }
         }
      }

      this.bipedRightArm.rotateAngleY = 0.0F;
      this.bipedLeftArm.rotateAngleY = 0.0F;
      float f7;
      float f61;
      if(super.onGround > -9990.0F) {
         f61 = super.onGround;
         this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(f61) * 3.1415927F * 2.0F) * 0.2F;
         this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
         this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
         this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
         this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
         f61 = 1.0F - super.onGround;
         f61 *= f61;
         f61 *= f61;
         f61 = 1.0F - f61;
         f7 = MathHelper.sin(f61 * 3.1415927F);
         float isWorshipping = MathHelper.sin(super.onGround * 3.1415927F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
         this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)f7 * 1.2D + (double)isWorshipping));
         this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
         this.bipedRightArm.rotateAngleZ = MathHelper.sin(super.onGround * 3.1415927F) * -0.4F;
      }

      boolean isWorshipping1 = par7Entity != null && par7Entity instanceof EntityGoblin && ((EntityGoblin)par7Entity).isWorshipping();
      if(!this.isSneak && !isWorshipping1) {
         this.bipedBody.rotateAngleX = 0.0F;
         this.bipedRightLeg.rotationPointZ = 0.1F;
         this.bipedLeftLeg.rotationPointZ = 0.1F;
         this.bipedRightLeg.rotationPointY = 18.0F;
         this.bipedLeftLeg.rotationPointY = 18.0F;
         this.bipedHead.rotationPointY = 11.0F;
         this.bipedBody.rotationPointY = 11.0F;
      } else {
         this.bipedBody.rotateAngleX = 0.5F;
         this.bipedRightArm.rotateAngleX -= 2.2F;
         this.bipedLeftArm.rotateAngleX -= 2.2F;
         this.bipedRightLeg.rotationPointZ = 3.0F;
         this.bipedLeftLeg.rotationPointZ = 3.0F;
         this.bipedHead.rotateAngleX = 0.5F;
         this.bipedRightLeg.rotationPointY = 18.0F;
         this.bipedLeftLeg.rotationPointY = 18.0F;
         this.bipedHead.rotationPointY = 13.0F;
         this.bipedBody.rotationPointY = 13.0F;
      }

      this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      if(this.aimedBow) {
         f61 = 0.0F;
         f7 = 0.0F;
         this.bipedRightArm.rotateAngleZ = 0.0F;
         this.bipedLeftArm.rotateAngleZ = 0.0F;
         this.bipedRightArm.rotateAngleY = -(0.1F - f61 * 0.6F) + this.bipedHead.rotateAngleY;
         this.bipedLeftArm.rotateAngleY = 0.1F - f61 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
         this.bipedRightArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
         this.bipedLeftArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
         this.bipedRightArm.rotateAngleX -= f61 * 1.2F - f7 * 0.4F;
         this.bipedLeftArm.rotateAngleX -= f61 * 1.2F - f7 * 0.4F;
         this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
         this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
         this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
         this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      }

   }
}
