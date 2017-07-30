package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelGoddess extends ModelBase {

   ModelRenderer head;
   ModelRenderer cleave;
   ModelRenderer body;
   ModelRenderer rightarm;
   ModelRenderer leftarm;
   ModelRenderer rightleg;
   ModelRenderer leftleg;
   ModelRenderer dressLower;
   ModelRenderer dressMiddle;


   public ModelGoddess() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.head = new ModelRenderer(this, 0, 0);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.head.setTextureSize(64, 64);
      this.head.mirror = true;
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.cleave = new ModelRenderer(this, 35, 0);
      this.cleave.addBox(0.0F, -2.0F, -2.0F, 8, 4, 4);
      this.cleave.setRotationPoint(-4.0F, 3.0F, -2.0F);
      this.cleave.setTextureSize(64, 64);
      this.cleave.mirror = true;
      this.setRotation(this.cleave, -0.7853982F, 0.0F, 0.0F);
      this.body = new ModelRenderer(this, 16, 16);
      this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
      this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.body.setTextureSize(64, 64);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.rightarm = new ModelRenderer(this, 40, 16);
      this.rightarm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
      this.rightarm.setRotationPoint(-5.0F, 2.0F, 0.0F);
      this.rightarm.setTextureSize(64, 64);
      this.rightarm.mirror = true;
      this.setRotation(this.rightarm, 0.0F, 0.0F, 0.0F);
      this.leftarm = new ModelRenderer(this, 40, 16);
      this.leftarm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
      this.leftarm.setRotationPoint(5.0F, 2.0F, 0.0F);
      this.leftarm.setTextureSize(64, 64);
      this.leftarm.mirror = true;
      this.setRotation(this.leftarm, 0.0F, 0.0F, 0.0F);
      this.rightleg = new ModelRenderer(this, 0, 16);
      this.rightleg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.rightleg.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.rightleg.setTextureSize(64, 64);
      this.rightleg.mirror = true;
      this.setRotation(this.rightleg, 0.0F, 0.0F, 0.0F);
      this.leftleg = new ModelRenderer(this, 0, 16);
      this.leftleg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.leftleg.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.leftleg.setTextureSize(64, 64);
      this.leftleg.mirror = true;
      this.setRotation(this.leftleg, 0.0F, 0.0F, 0.0F);
      this.dressLower = new ModelRenderer(this, 0, 45);
      this.dressLower.addBox(-6.0F, 0.0F, -4.0F, 12, 7, 8);
      this.dressLower.setRotationPoint(0.0F, 17.0F, 0.0F);
      this.dressLower.setTextureSize(64, 64);
      this.dressLower.mirror = true;
      this.setRotation(this.dressLower, 0.0F, 0.0F, 0.0F);
      this.dressMiddle = new ModelRenderer(this, 0, 33);
      this.dressMiddle.addBox(-5.0F, 0.0F, -3.0F, 10, 5, 6);
      this.dressMiddle.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.dressMiddle.setTextureSize(64, 64);
      this.dressMiddle.mirror = true;
      this.setRotation(this.dressMiddle, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.head.render(f5);
      this.cleave.render(f5);
      this.body.render(f5);
      this.rightarm.render(f5);
      this.leftarm.render(f5);
      this.rightleg.render(f5);
      this.leftleg.render(f5);
      this.dressLower.render(f5);
      this.dressMiddle.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.leftarm.rotateAngleX = -0.6F;
      this.leftarm.rotateAngleZ = -0.1F;
      this.leftarm.rotateAngleY = -0.6F;
      this.rightarm.rotateAngleX = -0.6F;
      this.rightarm.rotateAngleZ = 0.1F;
      this.rightarm.rotateAngleY = 0.6F;
   }
}
