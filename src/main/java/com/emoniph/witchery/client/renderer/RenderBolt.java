package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.entity.EntityBolt;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBolt extends Render {

   private static final ResourceLocation arrowTextures = new ResourceLocation("witchery", "textures/entities/bolt.png");
   private static final ResourceLocation arrowTextures2 = new ResourceLocation("witchery", "textures/entities/bolt2.png");
   private static final ResourceLocation arrowTextures3 = new ResourceLocation("witchery", "textures/entities/bolt3.png");
   private static final ResourceLocation arrowTextures4 = new ResourceLocation("witchery", "textures/entities/bolt4.png");


   public void renderArrow(EntityBolt par1EntityArrow, double par2, double par4, double par6, float par8, float par9) {
      this.bindEntityTexture(par1EntityArrow);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2, (float)par4, (float)par6);
      GL11.glRotatef(par1EntityArrow.prevRotationYaw + (par1EntityArrow.rotationYaw - par1EntityArrow.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(par1EntityArrow.prevRotationPitch + (par1EntityArrow.rotationPitch - par1EntityArrow.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
      Tessellator tessellator = Tessellator.instance;
      byte b0 = 0;
      float f2 = 0.0F;
      float f3 = 0.3F;
      float f4 = (float)(0 + b0 * 10) / 32.0F;
      float f5 = (float)(5 + b0 * 10) / 32.0F;
      float f6 = 0.0F;
      float f7 = 0.15625F;
      float f8 = (float)(5 + b0 * 10) / 32.0F;
      float f9 = (float)(10 + b0 * 10) / 32.0F;
      float f10 = 0.05625F;
      GL11.glEnable('\u803a');
      float f11 = (float)par1EntityArrow.arrowShake - par9;
      if(f11 > 0.0F) {
         float i = -MathHelper.sin(f11 * 3.0F) * f11;
         GL11.glRotatef(i, 0.0F, 0.0F, 1.0F);
      }

      GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(f10, f10, f10);
      GL11.glTranslatef(-3.0F, 0.0F, 0.0F);
      GL11.glNormal3f(f10, 0.0F, 0.0F);
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)f6, (double)f8);
      tessellator.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)f7, (double)f8);
      tessellator.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)f7, (double)f9);
      tessellator.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)f6, (double)f9);
      tessellator.draw();
      GL11.glNormal3f(-f10, 0.0F, 0.0F);
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)f6, (double)f8);
      tessellator.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)f7, (double)f8);
      tessellator.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)f7, (double)f9);
      tessellator.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)f6, (double)f9);
      tessellator.draw();
      GL11.glTranslatef(0.9F, 0.0F, 0.0F);

      for(int var23 = 0; var23 < 4; ++var23) {
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         GL11.glNormal3f(0.0F, 0.0F, f10);
         tessellator.startDrawingQuads();
         tessellator.addVertexWithUV(-8.0D, -2.0D, 0.0D, (double)f2, (double)f4);
         tessellator.addVertexWithUV(8.0D, -2.0D, 0.0D, (double)f3, (double)f4);
         tessellator.addVertexWithUV(8.0D, 2.0D, 0.0D, (double)f3, (double)f5);
         tessellator.addVertexWithUV(-8.0D, 2.0D, 0.0D, (double)f2, (double)f5);
         tessellator.draw();
      }

      GL11.glDisable('\u803a');
      GL11.glPopMatrix();
   }

   protected ResourceLocation getArrowTextures(EntityBolt bolt) {
      return bolt.isHolyDamage()?arrowTextures3:(bolt.isSilverDamage()?arrowTextures4:(bolt.isDraining()?arrowTextures2:arrowTextures));
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.getArrowTextures((EntityBolt)par1Entity);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderArrow((EntityBolt)par1Entity, par2, par4, par6, par8, par9);
   }

}
