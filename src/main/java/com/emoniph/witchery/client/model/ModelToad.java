package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityToad;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelToad extends ModelBase {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer armRight;
   ModelRenderer armLeft;
   ModelRenderer legRight;
   ModelRenderer legLeft;


   public ModelToad() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.setTextureOffset("head.nose", 0, 5);
      this.setTextureOffset("head.eyeRight", 0, 0);
      this.setTextureOffset("head.eyeLeft", 8, 0);
      this.setTextureOffset("legRight.thighRight", 0, 20);
      this.setTextureOffset("legRight.footRight", 0, 26);
      this.setTextureOffset("legLeft.thighLeft", 11, 20);
      this.setTextureOffset("legLeft.footLeft", 0, 26);
      this.head = new ModelRenderer(this, "head");
      this.head.setRotationPoint(0.0F, 20.0F, -1.0F);
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.head.mirror = true;
      this.head.addBox("nose", -2.0F, -1.0F, -4.0F, 4, 2, 4);
      this.head.addBox("eyeRight", -2.5F, -3.0F, -3.0F, 2, 2, 2);
      this.head.addBox("eyeLeft", 0.5F, -3.0F, -3.0F, 2, 2, 2);
      this.body = new ModelRenderer(this, 0, 12);
      this.body.addBox(-2.0F, -1.0F, 0.0F, 4, 2, 5);
      this.body.setRotationPoint(0.0F, 20.0F, -1.0F);
      this.body.setTextureSize(32, 32);
      this.body.mirror = true;
      this.setRotation(this.body, -0.4833219F, 0.0F, 0.0F);
      this.armRight = new ModelRenderer(this, 13, 26);
      this.armRight.addBox(-1.0F, 0.0F, 0.0F, 1, 4, 1);
      this.armRight.setRotationPoint(-2.0F, 20.0F, -1.0F);
      this.armRight.setTextureSize(32, 32);
      this.armRight.mirror = true;
      this.setRotation(this.armRight, -0.3346075F, 0.0F, 0.0F);
      this.armLeft = new ModelRenderer(this, 18, 26);
      this.armLeft.addBox(0.0F, 0.0F, 0.0F, 1, 4, 1);
      this.armLeft.setRotationPoint(2.0F, 20.0F, -1.0F);
      this.armLeft.setTextureSize(32, 32);
      this.armLeft.mirror = true;
      this.setRotation(this.armLeft, -0.3346075F, 0.0F, 0.0F);
      this.legRight = new ModelRenderer(this, "legRight");
      this.legRight.setRotationPoint(-2.0F, 23.0F, 3.0F);
      this.setRotation(this.legRight, 0.0F, 0.0F, 0.0F);
      this.legRight.mirror = true;
      this.legRight.addBox("thighRight", -2.0F, -1.0F, -2.0F, 2, 2, 3);
      this.legRight.addBox("footRight", -3.0F, 1.0F, -4.0F, 3, 0, 3);
      this.legLeft = new ModelRenderer(this, "legLeft");
      this.legLeft.setRotationPoint(2.0F, 23.0F, 3.0F);
      this.setRotation(this.legLeft, 0.0F, 0.0F, 0.0F);
      this.legLeft.mirror = true;
      this.legLeft.addBox("thighLeft", 0.0F, -1.0F, -2.0F, 2, 2, 3);
      this.legLeft.addBox("footLeft", 0.0F, 1.0F, -4.0F, 3, 0, 3);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.head.rotateAngleY = f3 / 57.295776F;
      this.head.rotateAngleX = f4 / 57.295776F;
      if(super.isChild) {
         float p6 = 2.0F;
         GL11.glPushMatrix();
         GL11.glScalef(1.5F / p6, 1.5F / p6, 1.5F / p6);
         GL11.glTranslatef(0.0F, 10.0F * f5, 0.0F);
         this.head.render(f5);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glScalef(1.0F / p6, 1.0F / p6, 1.0F / p6);
         GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
         this.body.render(f5);
         this.armRight.render(f5);
         this.armLeft.render(f5);
         this.legRight.render(f5);
         this.legLeft.render(f5);
         GL11.glPopMatrix();
      } else {
         this.head.render(f5);
         this.body.render(f5);
         this.armRight.render(f5);
         this.armLeft.render(f5);
         this.legRight.render(f5);
         this.legLeft.render(f5);
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

   public void setLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
      EntityToad toad = (EntityToad)par1EntityLivingBase;
      if(toad.isSitting()) {
         this.legLeft.rotateAngleX = -0.3926991F;
         this.legRight.rotateAngleX = this.legLeft.rotateAngleX;
      } else {
         this.legLeft.rotateAngleX = MathHelper.cos(par2 * 0.6662F) * 1.4F * par3 + ((double)par3 <= 0.1D && (double)par3 >= -0.1D?0.0F:0.5F);
         this.legRight.rotateAngleX = this.legLeft.rotateAngleX;
         this.armLeft.rotateAngleX = MathHelper.cos(par2 * 0.6662F + 3.1415927F) * 1.4F * par3;
         this.armRight.rotateAngleX = MathHelper.cos(par2 * 0.6662F) * 1.4F * par3;
      }

   }
}
