package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelCaneSword extends ModelBase {

   private ModelRenderer sheath;
   private ModelRenderer ball;
   private ModelRenderer blade1;
   private ModelRenderer blade2;
   private ModelRenderer blade3;


   public ModelCaneSword() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.blade3 = new ModelRenderer(this, 24, 0);
      this.blade3.setRotationPoint(-5.8F, 11.0F, -1.6F);
      this.blade3.addBox(-1.0F, -17.0F, -1.0F, 2, 2, 2, 0.0F);
      this.blade2 = new ModelRenderer(this, 24, 5);
      this.blade2.setRotationPoint(-5.8F, 11.0F, -1.6F);
      this.blade2.addBox(-1.0F, -15.0F, -1.0F, 2, 4, 2, 0.0F);
      this.ball = new ModelRenderer(this, 0, 0);
      this.ball.setRotationPoint(-5.8F, 10.0F, -1.6F);
      this.ball.addBox(-1.5F, -1.0F, -1.5F, 3, 2, 3, 0.0F);
      this.blade1 = new ModelRenderer(this, 24, 12);
      this.blade1.setRotationPoint(-5.8F, 8.0F, -1.6F);
      this.blade1.addBox(-1.0F, -8.0F, -1.0F, 2, 9, 2, 0.0F);
      this.sheath = new ModelRenderer(this, 0, 6);
      this.sheath.setRotationPoint(-5.8F, 11.0F, -1.6F);
      this.sheath.addBox(-1.0F, 0.0F, -1.0F, 2, 13, 2, 0.0F);
   }

   private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, boolean firstPerson, boolean deployed) {
      if(deployed) {
         GL11.glPushMatrix();
         GL11.glTranslatef(this.blade3.offsetX, this.blade3.offsetY, this.blade3.offsetZ);
         GL11.glTranslatef(this.blade3.rotationPointX * f5, this.blade3.rotationPointY * f5, this.blade3.rotationPointZ * f5);
         GL11.glTranslatef(0.0F, 3.15F, 0.0F);
         GL11.glScaled(0.2D, 4.3D, 0.2D);
         GL11.glTranslatef(-this.blade3.offsetX, -this.blade3.offsetY, -this.blade3.offsetZ);
         GL11.glTranslatef(-this.blade3.rotationPointX * f5, -this.blade3.rotationPointY * f5, -this.blade3.rotationPointZ * f5);
         this.blade3.render(f5);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glTranslatef(this.blade2.offsetX, this.blade2.offsetY, this.blade2.offsetZ);
         GL11.glTranslatef(this.blade2.rotationPointX * f5, this.blade2.rotationPointY * f5, this.blade2.rotationPointZ * f5);
         GL11.glTranslatef(0.0F, 2.1F, 0.0F);
         GL11.glScaled(0.15D, 3.7D, 0.7D);
         GL11.glTranslatef(-this.blade2.offsetX, -this.blade2.offsetY, -this.blade2.offsetZ);
         GL11.glTranslatef(-this.blade2.rotationPointX * f5, -this.blade2.rotationPointY * f5, -this.blade2.rotationPointZ * f5);
         this.blade2.render(f5);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glTranslatef(this.blade1.offsetX, this.blade1.offsetY, this.blade1.offsetZ);
         GL11.glTranslatef(this.blade1.rotationPointX * f5, this.blade1.rotationPointY * f5, this.blade1.rotationPointZ * f5);
         GL11.glScaled(0.21D, 1.4D, 0.5D);
         GL11.glTranslatef(-this.blade1.offsetX, -this.blade1.offsetY, -this.blade1.offsetZ);
         GL11.glTranslatef(-this.blade1.rotationPointX * f5, -this.blade1.rotationPointY * f5, -this.blade1.rotationPointZ * f5);
         this.blade1.render(f5);
         GL11.glPopMatrix();
      }

      GL11.glPushMatrix();
      GL11.glTranslatef(this.ball.offsetX, this.ball.offsetY, this.ball.offsetZ);
      GL11.glTranslatef(this.ball.rotationPointX * f5, this.ball.rotationPointY * f5, this.ball.rotationPointZ * f5);
      GL11.glScaled(0.8D, 1.1D, 0.8D);
      GL11.glTranslatef(-this.ball.offsetX, -this.ball.offsetY, -this.ball.offsetZ);
      GL11.glTranslatef(-this.ball.rotationPointX * f5, -this.ball.rotationPointY * f5, -this.ball.rotationPointZ * f5);
      this.ball.render(f5);
      GL11.glPopMatrix();
      if(!deployed) {
         GL11.glPushMatrix();
         GL11.glTranslatef(this.sheath.offsetX, this.sheath.offsetY, this.sheath.offsetZ);
         GL11.glTranslatef(this.sheath.rotationPointX * f5, this.sheath.rotationPointY * f5, this.sheath.rotationPointZ * f5);
         GL11.glScaled(0.8D, 1.0D, 0.8D);
         GL11.glTranslatef(-this.sheath.offsetX, -this.sheath.offsetY, -this.sheath.offsetZ);
         GL11.glTranslatef(-this.sheath.rotationPointX * f5, -this.sheath.rotationPointY * f5, -this.sheath.rotationPointZ * f5);
         this.sheath.render(f5);
         GL11.glPopMatrix();
      }

   }
}
