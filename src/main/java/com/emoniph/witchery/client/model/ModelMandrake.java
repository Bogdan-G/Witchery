package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityMandrake;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

@SideOnly(Side.CLIENT)
public class ModelMandrake extends ModelBase {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer rightarm;
   ModelRenderer leftarm;
   ModelRenderer rightleg;
   ModelRenderer leftleg;


   public ModelMandrake() {
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.setTextureOffset("head.face", 0, 8);
      this.setTextureOffset("head.leaves", 0, 0);
      this.setTextureOffset("body.bodyChest", 21, 0);
      this.setTextureOffset("body.bodyBelly", 17, 7);
      this.head = new ModelRenderer(this, "head");
      this.head.setRotationPoint(0.0F, 16.0F, 0.0F);
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.head.mirror = true;
      this.head.addBox("face", -2.0F, -4.0F, -2.0F, 4, 4, 4);
      this.head.addBox("leaves", -4.0F, -12.0F, 0.0F, 8, 8, 0);
      this.body = new ModelRenderer(this, "body");
      this.body.setRotationPoint(0.0F, 16.0F, 0.0F);
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.body.mirror = true;
      this.body.addBox("bodyChest", -2.5F, 0.0F, -2.5F, 5, 2, 5);
      this.body.addBox("bodyBelly", -3.5F, 2.0F, -3.5F, 7, 3, 7);
      this.rightarm = new ModelRenderer(this, 37, 0);
      this.rightarm.addBox(-1.0F, 0.0F, -0.5F, 1, 3, 1);
      this.rightarm.setRotationPoint(-2.0F, 17.0F, 0.0F);
      this.rightarm.setTextureSize(64, 32);
      this.rightarm.mirror = true;
      this.setRotation(this.rightarm, 0.0F, 0.0F, 1.047198F);
      this.leftarm = new ModelRenderer(this, 37, 0);
      this.leftarm.addBox(0.0F, 0.0F, -0.5F, 1, 3, 1);
      this.leftarm.setRotationPoint(2.0F, 17.0F, 0.0F);
      this.leftarm.setTextureSize(64, 32);
      this.leftarm.mirror = true;
      this.setRotation(this.leftarm, 0.0F, 0.0F, -1.047198F);
      this.rightleg = new ModelRenderer(this, 27, 18);
      this.rightleg.addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2);
      this.rightleg.setRotationPoint(-1.0F, 21.0F, 0.0F);
      this.rightleg.setTextureSize(64, 32);
      this.rightleg.mirror = true;
      this.setRotation(this.rightleg, 0.0F, 0.0F, 0.0F);
      this.leftleg = new ModelRenderer(this, 27, 18);
      this.leftleg.addBox(-1.0F, 0.0F, -1.0F, 2, 3, 2);
      this.leftleg.setRotationPoint(1.0F, 21.0F, 0.0F);
      this.leftleg.setTextureSize(64, 32);
      this.leftleg.mirror = true;
      this.setRotation(this.leftleg, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.head.render(f5);
      this.body.render(f5);
      this.rightarm.render(f5);
      this.leftarm.render(f5);
      this.rightleg.render(f5);
      this.leftleg.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }

   public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float par4) {
      EntityMandrake entityDemon = (EntityMandrake)par1EntityLiving;
      this.rightarm.rotateAngleX = (-0.2F + 1.5F * this.func_78172_a(par2, 13.0F)) * par3;
      this.leftarm.rotateAngleX = (-0.2F - 1.5F * this.func_78172_a(par2, 13.0F)) * par3;
   }

   private float func_78172_a(float par1, float par2) {
      return (Math.abs(par1 % par2 - par2 * 0.5F) - par2 * 0.25F) / (par2 * 0.25F);
   }
}
