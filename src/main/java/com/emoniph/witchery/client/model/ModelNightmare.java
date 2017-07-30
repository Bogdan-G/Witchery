package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityNightmare;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelNightmare extends ModelBase {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer leftarm;
   ModelRenderer leg7;
   ModelRenderer leg6;
   ModelRenderer leg5;
   ModelRenderer leg4;
   ModelRenderer leg3;
   ModelRenderer leg2;
   ModelRenderer leg1;
   ModelRenderer rightfingerlittle;
   ModelRenderer rightfingerindex;
   ModelRenderer rightfingerthumb;
   ModelRenderer rightarm;
   ModelRenderer leftfingerlittle;
   ModelRenderer leftfingerindex;
   ModelRenderer leftfingerthumb;


   public ModelNightmare() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-4.0F, -8.0F, -3.0F, 8, 8, 6);
      this.head.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.head.setTextureSize(64, 64);
      this.head.mirror = true;
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.body = new ModelRenderer(this, 16, 16);
      this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
      this.body.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.body.setTextureSize(64, 64);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.leftarm = new ModelRenderer(this, 40, 16);
      this.leftarm.addBox(-1.0F, -2.0F, -1.0F, 2, 16, 2);
      this.leftarm.setRotationPoint(5.0F, -4.0F, 0.0F);
      this.leftarm.setTextureSize(64, 64);
      this.leftarm.mirror = true;
      this.setRotation(this.leftarm, 0.0F, 0.0F, 0.0F);
      this.leg7 = new ModelRenderer(this, 12, 16);
      this.leg7.addBox(0.0F, 0.0F, 0.0F, 1, 16, 1);
      this.leg7.setRotationPoint(-1.0F, 6.0F, 0.0F);
      this.leg7.setTextureSize(64, 64);
      this.leg7.mirror = true;
      this.setRotation(this.leg7, 0.0F, 0.0F, 0.0F);
      this.leg6 = new ModelRenderer(this, 8, 16);
      this.leg6.addBox(0.0F, 0.0F, 0.0F, 1, 17, 1);
      this.leg6.setRotationPoint(1.0F, 6.0F, -1.0F);
      this.leg6.setTextureSize(64, 64);
      this.leg6.mirror = true;
      this.setRotation(this.leg6, 0.0F, 0.0F, 0.0F);
      this.leg5 = new ModelRenderer(this, 4, 16);
      this.leg5.addBox(0.0F, 0.0F, 0.0F, 1, 12, 1);
      this.leg5.setRotationPoint(-3.0F, 6.0F, 0.0F);
      this.leg5.setTextureSize(64, 64);
      this.leg5.mirror = true;
      this.setRotation(this.leg5, 0.0F, 0.0F, 0.0F);
      this.leg4 = new ModelRenderer(this, 8, 16);
      this.leg4.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1);
      this.leg4.setRotationPoint(-2.0F, 6.0F, -1.0F);
      this.leg4.setTextureSize(64, 64);
      this.leg4.mirror = true;
      this.setRotation(this.leg4, 0.0F, 0.0F, 0.0F);
      this.leg3 = new ModelRenderer(this, 12, 16);
      this.leg3.addBox(0.0F, 0.0F, 0.0F, 1, 11, 1);
      this.leg3.setRotationPoint(0.0F, 6.0F, -1.0F);
      this.leg3.setTextureSize(64, 64);
      this.leg3.mirror = true;
      this.setRotation(this.leg3, 0.0F, 0.0F, 0.0F);
      this.leg2 = new ModelRenderer(this, 0, 16);
      this.leg2.addBox(0.0F, 0.0F, 0.0F, 1, 14, 1);
      this.leg2.setRotationPoint(2.0F, 6.0F, 0.0F);
      this.leg2.setTextureSize(64, 64);
      this.leg2.mirror = true;
      this.setRotation(this.leg2, 0.0F, 0.0F, 0.0F);
      this.leg1 = new ModelRenderer(this, 0, 16);
      this.leg1.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
      this.leg1.setRotationPoint(-3.0F, 6.0F, -1.0F);
      this.leg1.setTextureSize(64, 64);
      this.leg1.mirror = true;
      this.setRotation(this.leg1, 0.0F, 0.0F, 0.0F);
      this.rightfingerlittle = new ModelRenderer(this, 0, 46);
      this.rightfingerlittle.addBox(-1.0F, 0.0F, -1.0F, 1, 6, 1);
      this.rightfingerlittle.setRotationPoint(-5.0F, 8.0F, 0.0F);
      this.rightfingerlittle.setTextureSize(64, 64);
      this.rightfingerlittle.mirror = true;
      this.setRotation(this.rightfingerlittle, -0.148353F, 0.0F, 0.1134464F);
      this.rightfingerindex = new ModelRenderer(this, 4, 46);
      this.rightfingerindex.addBox(0.0F, 0.0F, -1.0F, 1, 6, 1);
      this.rightfingerindex.setRotationPoint(-5.0F, 8.0F, 0.0F);
      this.rightfingerindex.setTextureSize(64, 64);
      this.rightfingerindex.mirror = true;
      this.setRotation(this.rightfingerindex, -0.148353F, 0.0F, -0.1134464F);
      this.rightfingerthumb = new ModelRenderer(this, 8, 46);
      this.rightfingerthumb.addBox(-0.5F, 0.0F, 0.0F, 1, 6, 1);
      this.rightfingerthumb.setRotationPoint(-5.0F, 8.0F, 0.0F);
      this.rightfingerthumb.setTextureSize(64, 64);
      this.rightfingerthumb.mirror = true;
      this.setRotation(this.rightfingerthumb, 0.1396263F, 0.0F, 0.0F);
      this.rightarm = new ModelRenderer(this, 40, 16);
      this.rightarm.addBox(-1.0F, -2.0F, -1.0F, 2, 16, 2);
      this.rightarm.setRotationPoint(-5.0F, -4.0F, 0.0F);
      this.rightarm.setTextureSize(64, 64);
      this.rightarm.mirror = true;
      this.setRotation(this.rightarm, 0.0F, 0.0F, 0.0F);
      this.leftfingerlittle = new ModelRenderer(this, 8, 53);
      this.leftfingerlittle.addBox(0.0F, 0.0F, -1.0F, 1, 6, 1);
      this.leftfingerlittle.setRotationPoint(5.0F, 8.0F, 0.0F);
      this.leftfingerlittle.setTextureSize(64, 64);
      this.leftfingerlittle.mirror = true;
      this.setRotation(this.leftfingerlittle, -0.148353F, 0.0F, -0.1134464F);
      this.leftfingerindex = new ModelRenderer(this, 4, 53);
      this.leftfingerindex.addBox(-1.0F, 0.0F, -1.0F, 1, 6, 1);
      this.leftfingerindex.setRotationPoint(5.0F, 8.0F, 0.0F);
      this.leftfingerindex.setTextureSize(64, 64);
      this.leftfingerindex.mirror = true;
      this.setRotation(this.leftfingerindex, -0.148353F, 0.0F, 0.1134464F);
      this.leftfingerthumb = new ModelRenderer(this, 0, 53);
      this.leftfingerthumb.addBox(-0.5F, 0.0F, 0.0F, 1, 6, 1);
      this.leftfingerthumb.setRotationPoint(5.0F, 8.0F, 0.0F);
      this.leftfingerthumb.setTextureSize(64, 64);
      this.leftfingerthumb.mirror = true;
      this.setRotation(this.leftfingerthumb, 0.1396263F, 0.0F, 0.0F);
      this.leftarm.addChild(this.leftfingerindex);
      this.leftarm.addChild(this.leftfingerlittle);
      this.leftarm.addChild(this.leftfingerthumb);
      this.rightarm.addChild(this.rightfingerindex);
      this.rightarm.addChild(this.rightfingerlittle);
      this.rightarm.addChild(this.rightfingerthumb);
      this.leftfingerlittle.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.leftfingerindex.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.leftfingerthumb.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.rightfingerlittle.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.rightfingerindex.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.rightfingerthumb.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.leg1.rotationPointX = -2.0F;
      this.leg2.rotationPointX = 2.0F;
      this.leg3.rotationPointX = 1.0F;
      this.leg4.rotationPointX = -1.0F;
      this.leg5.rotationPointX = -2.0F;
      this.leg6.rotationPointX = 3.0F;
      this.leg7.rotationPointX = 0.0F;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.head.render(f5);
      this.body.render(f5);
      this.leftarm.render(f5);
      this.leg7.render(f5);
      this.leg6.render(f5);
      this.leg5.render(f5);
      this.leg4.render(f5);
      this.leg3.render(f5);
      this.leg2.render(f5);
      this.leg1.render(f5);
      this.rightarm.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
      EntityNightmare entityDemon = (EntityNightmare)par1EntityLiving;
      int i = entityDemon.getAttackTimer();
      if(i > 0) {
         this.rightarm.rotateAngleX = -1.5F + 0.8F * this.func_78172_a((float)i - par4, 15.0F);
         this.leftarm.rotateAngleX = -1.5F + 0.8F * this.func_78172_a((float)i - par4, 15.0F);
         this.rightarm.rotateAngleZ = -(-1.5F + 1.5F * this.func_78172_a((float)i - par4, 15.0F));
         this.leftarm.rotateAngleZ = -1.5F + 1.5F * this.func_78172_a((float)i - par4, 15.0F);
      } else {
         this.leftarm.rotateAngleX = -0.1F;
         this.rightarm.rotateAngleX = -0.1F;
         this.leftarm.rotateAngleZ = 0.0F;
         this.rightarm.rotateAngleZ = 0.0F;
      }

   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
      this.head.rotateAngleY = par4 / 57.295776F;
      this.head.rotateAngleX = par5 / 57.295776F;
      float f6 = 0.01F * (float)(entity.getEntityId() % 10);
      this.leg7.rotateAngleX = -1.8F + MathHelper.sin((float)entity.ticksExisted * f6) * 4.5F * 3.1415927F / 180.0F;
      this.leg7.rotateAngleY = -1.7F;
      this.leg7.rotateAngleZ = 1.839205F + MathHelper.cos((float)entity.ticksExisted * f6) * 2.5F * 3.1415927F / 180.0F;
      f6 = 0.01F * (float)(entity.getEntityId() % 10);
      this.leg6.rotateAngleX = -1.8F + MathHelper.sin((float)entity.ticksExisted * f6) * 4.5F * 3.1415927F / 180.0F;
      this.leg6.rotateAngleY = -1.7F;
      this.leg6.rotateAngleZ = 1.839205F + MathHelper.cos((float)entity.ticksExisted * f6) * 2.5F * 3.1415927F / 180.0F;
      f6 = 0.01F * (float)(entity.getEntityId() % 12);
      this.leg5.rotateAngleX = -1.8F + MathHelper.sin((float)entity.ticksExisted * f6) * 4.5F * 3.1415927F / 180.0F;
      this.leg5.rotateAngleY = -1.7F;
      this.leg5.rotateAngleZ = 1.839205F + MathHelper.cos((float)entity.ticksExisted * f6) * 2.5F * 3.1415927F / 180.0F;
      f6 = 0.01F * (float)(entity.getEntityId() % 10);
      this.leg4.rotateAngleX = -1.8F + MathHelper.sin((float)entity.ticksExisted * f6) * 4.5F * 3.1415927F / 180.0F;
      this.leg4.rotateAngleY = -1.7F;
      this.leg4.rotateAngleZ = 1.839205F + MathHelper.cos((float)entity.ticksExisted * f6) * 2.5F * 3.1415927F / 180.0F;
      f6 = 0.01F * (float)(entity.getEntityId() % 13);
      this.leg3.rotateAngleX = -1.8F + MathHelper.sin((float)entity.ticksExisted * f6) * 4.5F * 3.1415927F / 180.0F;
      this.leg3.rotateAngleY = -1.7F;
      this.leg3.rotateAngleZ = 1.839205F + MathHelper.cos((float)entity.ticksExisted * f6) * 2.5F * 3.1415927F / 180.0F;
      f6 = 0.01F * (float)(entity.getEntityId() % 12);
      this.leg2.rotateAngleX = -1.8F + MathHelper.sin((float)entity.ticksExisted * f6) * 4.5F * 3.1415927F / 180.0F;
      this.leg2.rotateAngleY = -1.7F;
      this.leg2.rotateAngleZ = 1.839205F + MathHelper.cos((float)entity.ticksExisted * f6) * 2.5F * 3.1415927F / 180.0F;
      f6 = 0.01F * (float)(entity.getEntityId() % 12);
      this.leg1.rotateAngleX = -1.8F + MathHelper.sin((float)entity.ticksExisted * f6) * 4.5F * 3.1415927F / 180.0F;
      this.leg1.rotateAngleY = -1.7F;
      this.leg1.rotateAngleZ = 1.839205F + MathHelper.cos((float)entity.ticksExisted * f6) * 2.5F * 3.1415927F / 180.0F;
   }

   private float func_78172_a(float par1, float par2) {
      return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
   }
}
