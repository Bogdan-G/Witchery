package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.RenderUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderInfusionEnergyBar {

   int xsize = 8;
   int ysize = 32;
   private static final ResourceLocation GLASS = new ResourceLocation("witchery", "textures/gui/glass.png");
   private static final ResourceLocation BLOCK_TEXTURES = new ResourceLocation("textures/atlas/blocks.png");
   private static final ResourceLocation CREATURES = new ResourceLocation("witchery", "textures/gui/creatures.png");
   final boolean primary;


   public RenderInfusionEnergyBar(boolean primary) {
      this.primary = primary;
   }

   public void draw(double xpos, double ypos, double value, EntityPlayer player, int powerID) {
      Minecraft mc = Minecraft.getMinecraft();
      mc.getTextureManager().bindTexture(BLOCK_TEXTURES);
      GL11.glPushMatrix();

      try {
         RenderUtil.blend(true);
         this.drawFluid(xpos, ypos, value, this.primary?Infusion.Registry.instance().get(powerID).getPowerBarIcon(player, 0):Blocks.clay.getIcon(0, 0));
         byte iconOffsetX = 0;
         int iconOffsetY = (powerID - 1) * 8;
         if(this.primary) {
            this.drawGlass(xpos, ypos);
            iconOffsetX = 8;
         }

         byte width = 8;
         byte height = 8;
         int xPosition = MathHelper.floor_double(xpos);
         int yPosition = MathHelper.floor_double(ypos) + 33;
         mc.getTextureManager().bindTexture(CREATURES);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.drawTexturedModalRect(xPosition, yPosition, iconOffsetX, iconOffsetY, width, height);
      } finally {
         RenderUtil.blend(false);
         GL11.glPopMatrix();
      }

   }

   public void drawFluid(double xpos, double ypos, double value, IIcon icon) {
      double bottomY = ypos + (double)this.ysize;
      double topY = ypos + (double)this.ysize * (1.0D - value);
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      if(this.primary) {
         while(bottomY - 8.0D > topY) {
            drawIconPartial(xpos * 2.0D, (bottomY - 8.0D) * 2.0D, icon, 0.0D, 0.0D, 16.0D, 16.0D);
            bottomY -= 8.0D;
         }

         drawIconPartial(xpos * 2.0D, (bottomY - 8.0D) * 2.0D, icon, 0.0D, (topY - bottomY + 8.0D) * 2.0D, 16.0D, 16.0D);
      } else {
         for(int i = 0; (double)i < value; ++i) {
            drawIconPartial(xpos * 2.0D, (bottomY - (double)(i * 2)) * 2.0D - 2.0D, icon, 0.0D, 0.0D, 16.0D, 2.0D);
         }
      }

      GL11.glScaled(2.0D, 2.0D, 2.0D);
   }

   public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6) {
      double zLevel = 0.0D;
      float f = 0.00390625F;
      float f1 = 0.00390625F;
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
      tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
      tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
      tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
      tessellator.draw();
   }

   public static void drawIconPartial(double x, double y, IIcon icon, double left, double top, double right, double bottom) {
      if(icon != null) {
         RenderUtil.render2d(true);
         GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
         Tessellator tess = Tessellator.instance;
         tess.startDrawingQuads();
         float u1 = icon.getMinU();
         float v1 = icon.getMinV();
         float u2 = icon.getMaxU();
         float v2 = icon.getMaxV();
         double xoffset1 = left * (double)(u2 - u1) / 16.0D;
         double yoffset1 = top * (double)(v2 - v1) / 16.0D;
         double xoffset2 = right * (double)(u2 - u1) / 16.0D;
         double yoffset2 = bottom * (double)(v2 - v1) / 16.0D;
         tess.addVertexWithUV(x + left, y + top, 0.0D, (double)u1 + xoffset1, (double)v1 + yoffset1);
         tess.addVertexWithUV(x + left, y + bottom, 0.0D, (double)u1 + xoffset1, (double)v1 + yoffset2);
         tess.addVertexWithUV(x + right, y + bottom, 0.0D, (double)u1 + xoffset2, (double)v1 + yoffset2);
         tess.addVertexWithUV(x + right, y + top, 0.0D, (double)u1 + xoffset2, (double)v1 + yoffset1);
         tess.draw();
         RenderUtil.render2d(false);
      }
   }

   public void drawGlass(double xpos, double ypos) {
      Minecraft.getMinecraft().getTextureManager().bindTexture(GLASS);
      GL11.glBegin(7);
      GL11.glTexCoord2d(0.0D, 0.0D);
      GL11.glVertex2d(xpos, ypos);
      GL11.glTexCoord2d(0.0D, 1.0D);
      GL11.glVertex2d(xpos, ypos + (double)this.ysize);
      GL11.glTexCoord2d(1.0D, 1.0D);
      GL11.glVertex2d(xpos + (double)this.xsize, ypos + (double)this.ysize);
      GL11.glTexCoord2d(1.0D, 0.0D);
      GL11.glVertex2d(xpos + (double)this.xsize, ypos);
      GL11.glEnd();
   }

}
