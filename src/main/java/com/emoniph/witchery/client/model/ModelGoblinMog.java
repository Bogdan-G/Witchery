package com.emoniph.witchery.client.model;

import com.emoniph.witchery.util.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ModelGoblinMog extends ModelBase {

   public ModelRenderer bipedBody;
   public ModelRenderer bipedRightArm;
   public ModelRenderer bipedLeftArm;
   public ModelRenderer bipedRightLeg;
   public ModelRenderer bipedLeftLeg;
   public ModelRenderer bipedChest;
   public ModelRenderer bipedSkirt;
   public ModelRenderer bipedHead;
   public int heldItemLeft;
   public int heldItemRight;
   public boolean isSneak;
   public boolean aimedBow;
   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");


   public ModelGoblinMog() {
      this(0.0F);
   }

   public ModelGoblinMog(float f) {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.setTextureOffset("bipedHead.face", 0, 0);
      this.setTextureOffset("bipedHead.tuskright", 0, 4);
      this.setTextureOffset("bipedHead.tuskleft", 0, 4);
      this.setTextureOffset("bipedHead.nose", 25, 0);
      this.setTextureOffset("bipedHead.lip", 34, 0);
      this.bipedBody = new ModelRenderer(this, 16, 16);
      this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
      this.bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.bipedBody.setTextureSize(64, 64);
      this.bipedBody.mirror = true;
      this.setRotation(this.bipedBody, 0.0F, 0.0F, 0.0F);
      this.bipedRightArm = new ModelRenderer(this, 40, 14);
      this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 14, 4);
      this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
      this.bipedRightArm.setTextureSize(64, 64);
      this.bipedRightArm.mirror = true;
      this.setRotation(this.bipedRightArm, 0.0F, 0.0F, 0.0F);
      this.bipedLeftArm = new ModelRenderer(this, 40, 14);
      this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 14, 4);
      this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
      this.bipedLeftArm.setTextureSize(64, 64);
      this.bipedLeftArm.mirror = true;
      this.setRotation(this.bipedLeftArm, 0.0F, 0.0F, 0.0F);
      this.bipedRightLeg = new ModelRenderer(this, 0, 16);
      this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.bipedRightLeg.setTextureSize(64, 64);
      this.bipedRightLeg.mirror = true;
      this.setRotation(this.bipedRightLeg, 0.0F, 0.0F, 0.0F);
      this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
      this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.bipedLeftLeg.setTextureSize(64, 64);
      this.bipedLeftLeg.mirror = true;
      this.setRotation(this.bipedLeftLeg, 0.0F, 0.0F, 0.0F);
      this.bipedChest = new ModelRenderer(this, 35, 5);
      this.bipedChest.addBox(-4.0F, -2.0F, -5.0F, 8, 4, 4);
      this.bipedChest.setRotationPoint(0.0F, 2.0F, 0.0F);
      this.bipedChest.setTextureSize(64, 64);
      this.bipedChest.mirror = true;
      this.setRotation(this.bipedChest, 0.7853982F, 0.0F, 0.0F);
      this.bipedSkirt = new ModelRenderer(this, 14, 34);
      this.bipedSkirt.addBox(-4.5F, 0.0F, -2.5F, 9, 11, 5);
      this.bipedSkirt.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.bipedSkirt.setTextureSize(64, 64);
      this.bipedSkirt.mirror = true;
      this.setRotation(this.bipedSkirt, 0.0F, 0.0F, 0.0F);
      this.bipedHead = new ModelRenderer(this, "bipedHead");
      this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.setRotation(this.bipedHead, 0.0F, 0.0F, 0.0F);
      this.bipedHead.mirror = true;
      this.bipedHead.addBox("face", -4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.bipedHead.addBox("tuskright", -2.0F, -4.0F, -5.0F, 1, 2, 1);
      this.bipedHead.addBox("tuskleft", 1.0F, -4.0F, -5.0F, 1, 2, 1);
      this.bipedHead.addBox("nose", -1.0F, -6.0F, -6.0F, 2, 3, 2);
      this.bipedHead.addBox("lip", -2.0F, -2.0F, -6.0F, 4, 1, 2);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
      this.doRender(par7);
      Minecraft mc = Minecraft.getMinecraft();
      if(mc.gameSettings.fancyGraphics && Config.instance().renderHuntsmanGlintEffect) {
         float f9 = (float)entity.ticksExisted;
         mc.renderEngine.bindTexture(RES_ITEM_GLINT);
         GL11.glEnable(3042);
         float f10 = 0.5F;
         GL11.glColor4f(f10, f10, f10, 1.0F);
         GL11.glDepthFunc(514);
         GL11.glDepthMask(false);

         for(int k = 0; k < 2; ++k) {
            GL11.glDisable(2896);
            float f11 = 0.76F;
            GL11.glColor4f(0.2F * f11, 0.7F * f11, 0.7F * f11, 1.0F);
            GL11.glBlendFunc(768, 1);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            float f12 = f9 * (0.001F + (float)k * 0.003F) * 20.0F;
            float f13 = 0.33333334F;
            GL11.glScalef(f13, f13, f13);
            GL11.glRotatef(30.0F - (float)k * 60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.0F, f12, 0.0F);
            GL11.glMatrixMode(5888);
            this.doRender(par7);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glMatrixMode(5890);
         GL11.glDepthMask(true);
         GL11.glLoadIdentity();
         GL11.glMatrixMode(5888);
         GL11.glEnable(2896);
         GL11.glDisable(3042);
         GL11.glDepthFunc(515);
      }

   }

   private void doRender(float par7) {
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
         this.bipedChest.render(par7);
         this.bipedSkirt.render(par7);
         this.bipedRightArm.render(par7);
         this.bipedLeftArm.render(par7);
         this.bipedRightLeg.render(par7);
         this.bipedLeftLeg.render(par7);
         GL11.glPopMatrix();
      } else {
         this.bipedHead.render(par7);
         this.bipedChest.render(par7);
         this.bipedBody.render(par7);
         this.bipedSkirt.render(par7);
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
      }

      this.bipedRightArm.rotateAngleY = 0.0F;
      this.bipedLeftArm.rotateAngleY = 0.0F;
      float f6;
      float f7;
      if(super.onGround > -9990.0F) {
         f6 = super.onGround;
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
         f7 = MathHelper.sin(f6 * 3.1415927F);
         float f8 = MathHelper.sin(super.onGround * 3.1415927F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
         this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)f7 * 1.2D + (double)f8));
         this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
         this.bipedRightArm.rotateAngleZ = MathHelper.sin(super.onGround * 3.1415927F) * -0.4F;
      }

      if(this.isSneak) {
         this.bipedBody.rotateAngleX = 0.5F;
         this.bipedRightArm.rotateAngleX += 0.4F;
         this.bipedLeftArm.rotateAngleX += 0.4F;
         this.bipedRightLeg.rotationPointZ = 4.0F;
         this.bipedLeftLeg.rotationPointZ = 4.0F;
         this.bipedRightLeg.rotationPointY = 9.0F;
         this.bipedLeftLeg.rotationPointY = 9.0F;
         this.bipedHead.rotationPointY = 1.0F;
      } else {
         this.bipedBody.rotateAngleX = 0.0F;
         this.bipedRightLeg.rotationPointZ = 0.1F;
         this.bipedLeftLeg.rotationPointZ = 0.1F;
         this.bipedRightLeg.rotationPointY = 12.0F;
         this.bipedLeftLeg.rotationPointY = 12.0F;
         this.bipedHead.rotationPointY = 0.0F;
      }

      this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      if(this.aimedBow) {
         f6 = 0.0F;
         f7 = 0.0F;
         this.bipedRightArm.rotateAngleZ = 0.0F;
         this.bipedLeftArm.rotateAngleZ = 0.0F;
         this.bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F) + this.bipedHead.rotateAngleY;
         this.bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
         this.bipedRightArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
         this.bipedLeftArm.rotateAngleX = -1.5707964F + this.bipedHead.rotateAngleX;
         this.bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
         this.bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
         this.bipedRightArm.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
         this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
         this.bipedRightArm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
         this.bipedLeftArm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      }

   }

}
