package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityOwl;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelOwl extends ModelBase {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer rightarm;
   ModelRenderer leftarm;
   ModelRenderer rightleg;
   ModelRenderer leftleg;


   public ModelOwl() {
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.setTextureOffset("head.beak", 30, 0);
      this.setTextureOffset("head.hornRight", 37, 0);
      this.setTextureOffset("head.hornLeft", 37, 0);
      this.setTextureOffset("head.head1", 0, 0);
      this.head = new ModelRenderer(this, "head");
      this.head.setRotationPoint(0.0F, 15.0F, 0.0F);
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.head.mirror = true;
      this.head.addBox("hornRight", -5.0F, -7.0F, -1.0F, 1, 3, 2);
      this.head.addBox("hornLeft", 4.0F, -7.0F, -1.0F, 1, 3, 2);
      this.head.addBox("beak", -1.0F, -3.0F, -4.0F, 2, 3, 1);
      this.head.addBox("head1", -4.0F, -6.0F, -3.0F, 8, 6, 6);
      this.body = new ModelRenderer(this, 16, 16);
      this.body.addBox(-3.0F, 0.0F, -2.0F, 6, 8, 4);
      this.body.setRotationPoint(0.0F, 15.0F, 0.0F);
      this.body.setTextureSize(64, 32);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.rightarm = new ModelRenderer(this, 40, 16);
      this.rightarm.addBox(-1.0F, -1.0F, -2.0F, 1, 8, 4);
      this.rightarm.setRotationPoint(-3.0F, 16.0F, 0.0F);
      this.rightarm.setTextureSize(64, 32);
      this.rightarm.mirror = true;
      this.setRotation(this.rightarm, 0.0F, 0.0F, 0.0F);
      this.leftarm = new ModelRenderer(this, 40, 16);
      this.leftarm.addBox(0.0F, -1.0F, -2.0F, 1, 8, 4);
      this.leftarm.setRotationPoint(3.0F, 16.0F, 0.0F);
      this.leftarm.setTextureSize(64, 32);
      this.leftarm.mirror = true;
      this.setRotation(this.leftarm, 0.0F, 0.0F, 0.0F);
      this.rightleg = new ModelRenderer(this, 0, 16);
      this.rightleg.addBox(-1.0F, 0.0F, -2.0F, 2, 1, 4);
      this.rightleg.setRotationPoint(-2.0F, 23.0F, -1.0F);
      this.rightleg.setTextureSize(64, 32);
      this.rightleg.mirror = true;
      this.setRotation(this.rightleg, 0.0F, 0.0F, 0.0F);
      this.leftleg = new ModelRenderer(this, 0, 16);
      this.leftleg.addBox(-1.0F, 0.0F, -2.0F, 2, 1, 4);
      this.leftleg.setRotationPoint(2.0F, 23.0F, -1.0F);
      this.leftleg.setTextureSize(64, 32);
      this.leftleg.mirror = true;
      this.setRotation(this.leftleg, 0.0F, 0.0F, 0.0F);
      this.rightleg.setRotationPoint(-2.0F, 8.0F, -1.0F);
      this.leftleg.setRotationPoint(2.0F, 8.0F, -1.0F);
      this.body.addChild(this.leftleg);
      this.body.addChild(this.rightleg);
   }

   public static boolean isLanded(Entity entity) {
      Block block = entity.worldObj.getBlock(MathHelper.floor_double(entity.posX), (int)(entity.posY - 0.01D), MathHelper.floor_double(entity.posZ));
      Material material = block.getMaterial();
      return material == Material.leaves || material.isSolid();
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      EntityOwl entitybat = (EntityOwl)entity;
      if(entity.motionY == 0.0D && entity.motionX == 0.0D && entity.motionZ == 0.0D && isLanded(entity)) {
         this.body.rotateAngleX = 0.0F;
         this.leftarm.rotateAngleZ = 0.0F;
         this.rightarm.rotateAngleZ = 0.0F;
         this.rightleg.rotateAngleX = 0.0F;
         this.leftleg.rotateAngleX = 0.0F;
      } else {
         float f6 = 57.295776F;
         this.body.rotateAngleX = 0.7853982F + MathHelper.cos(f2 * 0.1F) * 0.15F;
         this.body.rotateAngleY = 0.0F;
         this.rightleg.rotateAngleX = 0.7853982F + MathHelper.cos(f2 * 0.1F) * 0.15F;
         this.leftleg.rotateAngleX = 0.7853982F + MathHelper.cos(f2 * 0.1F) * 0.15F;
         this.rightarm.rotateAngleZ = MathHelper.cos(f2 * 0.5F) * 3.1415927F * 0.2F * 2.0F + 1.4F;
         this.leftarm.rotateAngleZ = -this.rightarm.rotateAngleZ;
      }

      if(entitybat.isSitting()) {
         this.rightleg.rotateAngleY = 0.5F;
         this.leftleg.rotateAngleY = -this.rightleg.rotateAngleY;
      } else {
         this.rightleg.rotateAngleY = 0.1F;
         this.leftleg.rotateAngleY = -this.rightleg.rotateAngleY;
      }

      this.head.rotateAngleY = f3 / 57.295776F;
      this.head.rotateAngleX = f4 / 57.295776F;
      if(super.isChild) {
         float p6 = 2.0F;
         GL11.glPushMatrix();
         GL11.glScalef(1.5F / p6, 1.5F / p6, 1.5F / p6);
         GL11.glTranslatef(0.0F, 11.0F * f5, 0.0F);
         this.head.render(f5);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glScalef(1.0F / p6, 1.0F / p6, 1.0F / p6);
         GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
         this.body.render(f5);
         this.rightarm.render(f5);
         this.leftarm.render(f5);
         GL11.glPopMatrix();
      } else {
         this.head.render(f5);
         this.body.render(f5);
         this.rightarm.render(f5);
         this.leftarm.render(f5);
      }

   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }
}
