package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityLordOfTorment;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelLordOfTorment extends ModelBase {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer rightarm1;
   ModelRenderer rightarm2;
   ModelRenderer leftarm1;
   ModelRenderer leftarm2;
   ModelRenderer rightleg;
   ModelRenderer leftleg;
   ModelRenderer wingsLeft;
   ModelRenderer wingsRight;
   ModelRenderer hornLeft;
   ModelRenderer hornRight;


   public ModelLordOfTorment() {
      super.textureWidth = 128;
      super.textureHeight = 128;
      this.setTextureOffset("head.skull", 0, 0);
      this.setTextureOffset("head.beard1", 34, 0);
      this.setTextureOffset("head.beard2", 34, 0);
      this.setTextureOffset("head.beard3", 34, 0);
      this.setTextureOffset("head.beard4", 34, 0);
      this.setTextureOffset("head.beard5", 34, 0);
      this.setTextureOffset("head.beard6", 34, 0);
      this.setTextureOffset("head.nose", 40, 0);
      this.setTextureOffset("head.nose2", 40, 6);
      this.head = new ModelRenderer(this, "head");
      this.head.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.head.mirror = true;
      this.head.addBox("skull", -4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.head.addBox("beard1", -3.0F, 0.0F, -4.0F, 1, 7, 1);
      this.head.addBox("beard2", -2.0F, 0.0F, -5.0F, 1, 5, 1);
      this.head.addBox("beard3", -1.0F, 0.0F, -4.0F, 1, 9, 1);
      this.head.addBox("beard4", 0.0F, 0.0F, -5.0F, 1, 6, 1);
      this.head.addBox("beard5", 1.0F, 0.0F, -4.0F, 1, 4, 1);
      this.head.addBox("beard6", 2.0F, 0.0F, -5.0F, 1, 8, 1);
      this.head.addBox("nose", -3.0F, -4.0F, -5.0F, 6, 4, 1);
      this.head.addBox("nose2", -2.0F, -6.0F, -5.0F, 4, 2, 1);
      this.hornRight = new ModelRenderer(this, 55, 0);
      this.hornRight.addBox(-2.0F, -15.0F, 0.0F, 1, 9, 1);
      this.hornRight.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.hornRight.setTextureSize(128, 128);
      this.hornRight.mirror = true;
      this.setRotation(this.hornRight, 0.5948578F, 0.0F, -0.1858931F);
      this.head.addChild(this.hornRight);
      this.hornLeft = new ModelRenderer(this, 55, 0);
      this.hornLeft.addBox(1.0F, -15.0F, 0.0F, 1, 9, 1);
      this.hornLeft.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.hornLeft.setTextureSize(128, 128);
      this.hornLeft.mirror = true;
      this.setRotation(this.hornLeft, 0.5948578F, 0.0F, 0.1858931F);
      this.head.addChild(this.hornLeft);
      this.body = new ModelRenderer(this, 16, 16);
      this.body.addBox(-4.0F, -6.0F, -2.0F, 8, 14, 4);
      this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.body.setTextureSize(128, 128);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.rightarm1 = new ModelRenderer(this, 40, 16);
      this.rightarm1.addBox(-3.0F, -2.0F, -2.0F, 4, 20, 4);
      this.rightarm1.setRotationPoint(-5.0F, -4.0F, 0.0F);
      this.rightarm1.setTextureSize(128, 128);
      this.rightarm1.mirror = true;
      this.setRotation(this.rightarm1, 0.0F, 0.0F, 0.0F);
      this.rightarm2 = new ModelRenderer(this, 40, 16);
      this.rightarm2.addBox(-3.0F, -2.0F, -2.0F, 4, 20, 4);
      this.rightarm2.setRotationPoint(-5.0F, -4.0F, 0.0F);
      this.rightarm2.setTextureSize(128, 128);
      this.rightarm2.mirror = true;
      this.setRotation(this.rightarm2, 0.0F, 0.0F, 0.0F);
      this.leftarm1 = new ModelRenderer(this, 40, 16);
      this.leftarm1.addBox(-1.0F, -2.0F, -2.0F, 4, 20, 4);
      this.leftarm1.setRotationPoint(5.0F, -4.0F, 0.0F);
      this.leftarm1.setTextureSize(128, 128);
      this.leftarm1.mirror = true;
      this.setRotation(this.leftarm1, 0.0F, 0.0F, 0.0F);
      this.leftarm2 = new ModelRenderer(this, 40, 16);
      this.leftarm2.addBox(-1.0F, -2.0F, -2.0F, 4, 20, 4);
      this.leftarm2.setRotationPoint(5.0F, -4.0F, 0.0F);
      this.leftarm2.setTextureSize(128, 128);
      this.leftarm2.mirror = true;
      this.setRotation(this.leftarm2, 0.0F, 0.0F, 0.0F);
      this.rightleg = new ModelRenderer(this, 0, 16);
      this.rightleg.addBox(-2.0F, 0.0F, -2.0F, 4, 15, 4);
      this.rightleg.setRotationPoint(-2.0F, 8.0F, 0.0F);
      this.rightleg.setTextureSize(128, 128);
      this.rightleg.mirror = true;
      this.setRotation(this.rightleg, 0.0F, 0.0F, 0.0F);
      this.leftleg = new ModelRenderer(this, 0, 16);
      this.leftleg.addBox(-2.0F, 0.0F, -2.0F, 4, 15, 4);
      this.leftleg.setRotationPoint(2.0F, 8.0F, 0.0F);
      this.leftleg.setTextureSize(128, 128);
      this.leftleg.mirror = true;
      this.setRotation(this.leftleg, 0.0F, 0.0F, 0.0F);
      this.wingsLeft = new ModelRenderer(this, 0, 42);
      this.wingsLeft.addBox(-20.0F, -20.0F, 0.0F, 20, 40, 0);
      this.wingsLeft.setRotationPoint(0.0F, 1.0F, 5.0F);
      this.wingsLeft.setTextureSize(128, 128);
      this.wingsLeft.mirror = true;
      this.setRotation(this.wingsLeft, 0.0F, 0.0F, 0.0F);
      this.wingsRight = new ModelRenderer(this, 0, 82);
      this.wingsRight.addBox(0.0F, -20.0F, 0.0F, 20, 40, 0);
      this.wingsRight.setRotationPoint(0.0F, 1.0F, 5.0F);
      this.wingsRight.setTextureSize(128, 128);
      this.wingsRight.mirror = true;
      this.setRotation(this.wingsRight, 0.0F, 0.0F, 0.0F);
      this.hornRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.hornLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.head.render(f5);
      this.body.render(f5);
      this.rightarm1.render(f5);
      this.rightarm2.render(f5);
      this.leftarm1.render(f5);
      this.leftarm2.render(f5);
      this.rightleg.render(f5);
      this.leftleg.render(f5);
      this.wingsLeft.render(f5);
      this.wingsRight.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      this.head.rotateAngleY = par4 / 57.295776F;
      this.head.rotateAngleX = par5 / 57.295776F;
      this.rightarm1.rotateAngleX = MathHelper.cos(3.8077927F) * 2.0F * par2 * 0.5F;
      this.rightarm2.rotateAngleX = MathHelper.cos(3.8077927F) * 2.0F * par2 * 0.25F;
      this.leftarm1.rotateAngleX = MathHelper.cos(0.6662F) * 2.0F * par2 * 0.5F;
      this.leftarm2.rotateAngleX = MathHelper.cos(0.6662F) * 2.0F * par2 * 0.25F;
      boolean inMotion = entity.motionX > 0.0D || entity.motionZ > 0.0D;
      if(inMotion) {
         this.wingsLeft.rotateAngleY = 0.4F;
         this.wingsRight.rotateAngleY = -0.4F;
      } else {
         this.wingsLeft.rotateAngleY = MathHelper.cos(3.8077927F) * 2.0F * par2 * 0.5F + MathHelper.cos(par3 * 0.09F) * 0.3F;
         this.wingsRight.rotateAngleY = MathHelper.cos(3.8077927F) * 2.0F * par2 * 0.5F - MathHelper.cos(par3 * 0.09F) * 0.3F;
      }

      this.rightarm1.rotateAngleY = 0.0F;
      this.rightarm2.rotateAngleY = 0.0F;
      this.leftarm1.rotateAngleY = 0.0F;
      this.leftarm2.rotateAngleY = 0.0F;
      this.rightarm1.rotateAngleZ = 0.0F;
      this.rightarm2.rotateAngleZ = 0.0F;
      this.leftarm1.rotateAngleZ = 0.0F;
      this.leftarm2.rotateAngleZ = 0.0F;
      this.rightarm1.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.rightarm2.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.leftarm1.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.leftarm2.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.rightarm1.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.rightarm2.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.leftarm1.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.leftarm2.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      EntityLordOfTorment entityDemon = (EntityLordOfTorment)entity;
      int i = entityDemon.getAttackTimer();
      if(i > 0) {
         this.rightarm1.rotateAngleX = -1.5F + 0.8F * this.func_78172_a((float)i - par4, 15.0F);
         this.leftarm1.rotateAngleX = -1.5F + 0.8F * this.func_78172_a((float)i - par4, 15.0F);
         this.rightarm1.rotateAngleZ = -(-1.5F + 1.5F * this.func_78172_a((float)i - par4, 15.0F));
         this.leftarm1.rotateAngleZ = -1.5F + 1.5F * this.func_78172_a((float)i - par4, 15.0F);
         this.rightarm2.rotateAngleZ = -(-1.0F + 1.5F * this.func_78172_a((float)i - par4, 15.0F));
         this.leftarm2.rotateAngleZ = -1.0F + 1.5F * this.func_78172_a((float)i - par4, 15.0F);
      }

   }

   private float func_78172_a(float par1, float par2) {
      return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
   }
}
