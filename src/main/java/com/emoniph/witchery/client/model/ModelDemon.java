package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityDemon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

@SideOnly(Side.CLIENT)
public class ModelDemon extends ModelBase {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer rightarm;
   ModelRenderer leftarm;
   ModelRenderer rightleg;
   ModelRenderer leftleg;
   ModelRenderer wingRight;
   ModelRenderer wingLeft;


   public ModelDemon() {
      super.textureWidth = 128;
      super.textureHeight = 32;
      this.setTextureOffset("head.face", 0, 0);
      this.setTextureOffset("head.leftHorn", 0, 16);
      this.setTextureOffset("head.rightHorn", 0, 16);
      this.setTextureOffset("head.leftTusk", 4, 16);
      this.setTextureOffset("head.rightTusk", 4, 16);
      this.setTextureOffset("head.snout", 20, 16);
      this.setTextureOffset("head.bottomLip", 8, 16);
      this.head = new ModelRenderer(this, "head");
      this.head.setTextureOffset(0, 0);
      this.head.addBox("face", -4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.head.addBox("leftHorn", 4.0F, -12.0F, -0.5F, 1, 8, 1);
      this.head.addBox("rightHorn", -5.0F, -12.0F, -0.5F, 1, 8, 1);
      this.head.addBox("leftTusk", 1.0F, -4.0F, -5.0F, 1, 2, 1);
      this.head.addBox("bottomLip", -2.0F, -2.0F, -6.0F, 4, 1, 2);
      this.head.addBox("snout", -1.0F, -6.0F, -6.0F, 2, 3, 2);
      this.head.addBox("rightTusk", -2.0F, -4.0F, -5.0F, 1, 2, 1);
      this.head.setRotationPoint(0.0F, -9.0F, 0.0F);
      this.head.setTextureSize(128, 32);
      this.head.mirror = true;
      this.body = new ModelRenderer(this, 64, 0);
      this.body.addBox(-4.0F, 0.0F, -3.0F, 8, 14, 6);
      this.body.setRotationPoint(0.0F, -9.0F, 0.0F);
      this.body.setTextureSize(128, 32);
      this.body.mirror = true;
      this.rightarm = new ModelRenderer(this, 48, 0);
      this.rightarm.addBox(-3.0F, -2.0F, -2.0F, 4, 20, 4);
      this.rightarm.setRotationPoint(-5.0F, -7.0F, 0.0F);
      this.rightarm.setTextureSize(128, 32);
      this.rightarm.mirror = true;
      this.leftarm = new ModelRenderer(this, 48, 0);
      this.leftarm.addBox(-1.0F, -2.0F, -2.0F, 4, 20, 4);
      this.leftarm.setRotationPoint(5.0F, -7.0F, 0.0F);
      this.leftarm.setTextureSize(128, 32);
      this.leftarm.mirror = true;
      this.rightleg = new ModelRenderer(this, 32, 0);
      this.rightleg.addBox(-2.0F, 0.0F, -2.0F, 4, 20, 4);
      this.rightleg.setRotationPoint(-2.0F, 4.0F, 0.0F);
      this.rightleg.setTextureSize(128, 32);
      this.rightleg.mirror = true;
      this.leftleg = new ModelRenderer(this, 32, 0);
      this.leftleg.addBox(-2.0F, 0.0F, -2.0F, 4, 20, 4);
      this.leftleg.setRotationPoint(2.0F, 4.0F, 0.0F);
      this.leftleg.setTextureSize(128, 32);
      this.leftleg.mirror = false;
      this.wingRight = new ModelRenderer(this, 93, 0);
      this.wingRight.addBox(0.0F, 0.0F, 0.0F, 14, 21, 0);
      this.wingRight.setRotationPoint(1.0F, -8.0F, 3.0F);
      this.wingRight.setTextureSize(128, 32);
      this.wingRight.mirror = true;
      this.setRotation(this.wingRight, 0.3047198F, -0.6698132F, -0.6283185F);
      this.wingLeft = new ModelRenderer(this, 93, 0);
      this.wingLeft.addBox(0.0F, 0.0F, 0.0F, 14, 21, 0);
      this.wingLeft.setRotationPoint(-1.0F, -8.0F, 3.0F);
      this.wingLeft.setTextureSize(128, 32);
      this.wingLeft.mirror = true;
      this.setRotation(this.wingLeft, -0.3047198F, 3.811406F, 0.6283185F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.head.render(f5);
      this.body.render(f5);
      this.rightarm.render(f5);
      this.leftarm.render(f5);
      this.rightleg.render(f5);
      this.leftleg.render(f5);
      this.wingLeft.render(f5);
      this.wingRight.render(f5);
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
      this.head.rotateAngleY = par4 / 57.295776F;
      this.head.rotateAngleX = par5 / 57.295776F;
      this.leftleg.rotateAngleX = -1.5F * this.func_78172_a(par1, 13.0F) * par2;
      this.rightleg.rotateAngleX = 1.5F * this.func_78172_a(par1, 13.0F) * par2;
      this.leftleg.rotateAngleY = 0.0F;
      this.rightleg.rotateAngleY = 0.0F;
   }

   public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
      EntityDemon entityDemon = (EntityDemon)par1EntityLiving;
      int i = entityDemon.getAttackTimer();
      if(i > 0) {
         this.rightarm.rotateAngleX = -2.0F + 1.5F * this.func_78172_a((float)i - par4, 10.0F);
      } else {
         this.rightarm.rotateAngleX = (-0.2F + 1.5F * this.func_78172_a(par2, 13.0F)) * par3;
         this.leftarm.rotateAngleX = (-0.2F - 1.5F * this.func_78172_a(par2, 13.0F)) * par3;
      }

   }

   private float func_78172_a(float par1, float par2) {
      return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
   }
}
