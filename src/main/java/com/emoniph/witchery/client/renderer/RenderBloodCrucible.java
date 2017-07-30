package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.blocks.BlockBloodCrucible;
import com.emoniph.witchery.client.model.ModelBloodCrucible;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBloodCrucible extends TileEntitySpecialRenderer {

   final ModelBloodCrucible model = new ModelBloodCrucible();
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/blocks/bloodcrucible.png");


   public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
      BlockBloodCrucible.TileEntityBloodCrucible crucible = (BlockBloodCrucible.TileEntityBloodCrucible)tileEntity;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      GL11.glTranslatef(0.0F, -0.5F, 0.0F);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      this.bindTexture(TEXTURE_URL);
      this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      float percentFilled = crucible.getPercentFilled();
      if(percentFilled > 0.0F) {
         this.bindTexture(TextureMap.locationBlocksTexture);
         GL11.glPushMatrix();
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(3008);
         int color = 16711680;
         float red = (float)(color >>> 16 & 255) / 256.0F;
         float green = (float)(color >>> 8 & 255) / 256.0F;
         float blue = (float)(color & 255) / 256.0F;
         GL11.glColor4f(red, green, blue, 0.9F);
         float w = -0.1875F;
         float depth = -0.1F + -0.2F * percentFilled;
         GL11.glTranslatef(w, depth, -w);
         GL11.glRotatef(270.0F, 1.0F, 0.0F, 0.0F);
         float s = 0.0234375F;
         GL11.glScalef(0.0234375F, 0.0234375F, 0.0234375F);
         IIcon icon = Blocks.lava.getIcon(0, 0);
         boolean x = false;
         boolean y = false;
         boolean u = true;
         boolean v = true;
         Tessellator tessellator = Tessellator.instance;
         tessellator.startDrawingQuads();
         tessellator.setBrightness(200);
         tessellator.addVertexWithUV(0.0D, 16.0D, 0.0D, (double)icon.getMinU(), (double)icon.getMaxV());
         tessellator.addVertexWithUV(16.0D, 16.0D, 0.0D, (double)icon.getMaxU(), (double)icon.getMaxV());
         tessellator.addVertexWithUV(16.0D, 0.0D, 0.0D, (double)icon.getMaxU(), (double)icon.getMinV());
         tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)icon.getMinU(), (double)icon.getMinV());
         tessellator.draw();
         GL11.glEnable(3008);
         GL11.glDisable(3042);
         GL11.glPopMatrix();
      }

      GL11.glPopMatrix();
   }

}
