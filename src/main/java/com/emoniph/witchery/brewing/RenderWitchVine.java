package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderWitchVine implements ISimpleBlockRenderingHandler {

   public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {}

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      Tessellator tessellator = Tessellator.instance;
      IIcon iicon = renderer.getBlockIconFromSide(block, 0);
      if(renderer.hasOverrideBlockTexture()) {
         iicon = renderer.overrideBlockTexture;
      }

      tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
      int color = block.colorMultiplier(renderer.blockAccess, x, y, z);
      float f = (float)(color >> 16 & 255) / 255.0F;
      float f1 = (float)(color >> 8 & 255) / 255.0F;
      float f2 = (float)(color & 255) / 255.0F;
      tessellator.setColorOpaque_F(f, f1, f2);
      double d0 = (double)iicon.getMinU();
      double d1 = (double)iicon.getMinV();
      double d2 = (double)iicon.getMaxU();
      double d3 = (double)iicon.getMaxV();
      int l = renderer.blockAccess.getBlockMetadata(x, y, z);
      double d4 = 0.0D;
      double d5 = 0.05000000074505806D;
      if(l == 5) {
         tessellator.addVertexWithUV((double)x + d5, (double)(y + 1) + d4, (double)(z + 1) + d4, d0, d1);
         tessellator.addVertexWithUV((double)x + d5, (double)(y + 0) - d4, (double)(z + 1) + d4, d0, d3);
         tessellator.addVertexWithUV((double)x + d5, (double)(y + 0) - d4, (double)(z + 0) - d4, d2, d3);
         tessellator.addVertexWithUV((double)x + d5, (double)(y + 1) + d4, (double)(z + 0) - d4, d2, d1);
         tessellator.draw();
         tessellator.startDrawingQuads();
         tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
         tessellator.setColorOpaque_F(f, f1, f2);
         tessellator.addVertexWithUV((double)x - d5, (double)(y + 0) - d4, (double)(z + 1) + d4, d2, d3);
         tessellator.addVertexWithUV((double)x - d5, (double)(y + 1) + d4, (double)(z + 1) + d4, d2, d1);
         tessellator.addVertexWithUV((double)x - d5, (double)(y + 1) + d4, (double)(z + 0) - d4, d0, d1);
         tessellator.addVertexWithUV((double)x - d5, (double)(y + 0) - d4, (double)(z + 0) - d4, d0, d3);
      }

      if(l == 4) {
         tessellator.addVertexWithUV((double)(x + 1) - d5, (double)(y + 0) - d4, (double)(z + 1) + d4, d2, d3);
         tessellator.addVertexWithUV((double)(x + 1) - d5, (double)(y + 1) + d4, (double)(z + 1) + d4, d2, d1);
         tessellator.addVertexWithUV((double)(x + 1) - d5, (double)(y + 1) + d4, (double)(z + 0) - d4, d0, d1);
         tessellator.addVertexWithUV((double)(x + 1) - d5, (double)(y + 0) - d4, (double)(z + 0) - d4, d0, d3);
         tessellator.draw();
         tessellator.startDrawingQuads();
         tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
         tessellator.setColorOpaque_F(f, f1, f2);
         tessellator.addVertexWithUV((double)x + 1.0D + d5, (double)(y + 1) + d4, (double)(z + 1) + d4, d0, d1);
         tessellator.addVertexWithUV((double)x + 1.0D + d5, (double)(y + 0) - d4, (double)(z + 1) + d4, d0, d3);
         tessellator.addVertexWithUV((double)x + 1.0D + d5, (double)(y + 0) - d4, (double)(z + 0) - d4, d2, d3);
         tessellator.addVertexWithUV((double)x + 1.0D + d5, (double)(y + 1) + d4, (double)(z + 0) - d4, d2, d1);
      }

      if(l == 3) {
         tessellator.addVertexWithUV((double)(x + 1) + d4, (double)(y + 0) - d4, (double)z + d5, d2, d3);
         tessellator.addVertexWithUV((double)(x + 1) + d4, (double)(y + 1) + d4, (double)z + d5, d2, d1);
         tessellator.addVertexWithUV((double)(x + 0) - d4, (double)(y + 1) + d4, (double)z + d5, d0, d1);
         tessellator.addVertexWithUV((double)(x + 0) - d4, (double)(y + 0) - d4, (double)z + d5, d0, d3);
         tessellator.draw();
         tessellator.startDrawingQuads();
         tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
         tessellator.setColorOpaque_F(f, f1, f2);
         tessellator.addVertexWithUV((double)(x + 1) + d4, (double)(y + 1) + d4, (double)z - d5, d0, d1);
         tessellator.addVertexWithUV((double)(x + 1) + d4, (double)(y + 0) - d4, (double)z - d5, d0, d3);
         tessellator.addVertexWithUV((double)(x + 0) - d4, (double)(y + 0) - d4, (double)z - d5, d2, d3);
         tessellator.addVertexWithUV((double)(x + 0) - d4, (double)(y + 1) + d4, (double)z - d5, d2, d1);
      }

      if(l == 2) {
         tessellator.addVertexWithUV((double)(x + 1) + d4, (double)(y + 1) + d4, (double)(z + 1) - d5, d0, d1);
         tessellator.addVertexWithUV((double)(x + 1) + d4, (double)(y + 0) - d4, (double)(z + 1) - d5, d0, d3);
         tessellator.addVertexWithUV((double)(x + 0) - d4, (double)(y + 0) - d4, (double)(z + 1) - d5, d2, d3);
         tessellator.addVertexWithUV((double)(x + 0) - d4, (double)(y + 1) + d4, (double)(z + 1) - d5, d2, d1);
         tessellator.draw();
         tessellator.startDrawingQuads();
         tessellator.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
         tessellator.setColorOpaque_F(f, f1, f2);
         tessellator.addVertexWithUV((double)(x + 1) + d4, (double)(y + 0) - d4, (double)z + 1.0D + d5, d2, d3);
         tessellator.addVertexWithUV((double)(x + 1) + d4, (double)(y + 1) + d4, (double)z + 1.0D + d5, d2, d1);
         tessellator.addVertexWithUV((double)(x + 0) - d4, (double)(y + 1) + d4, (double)z + 1.0D + d5, d0, d1);
         tessellator.addVertexWithUV((double)(x + 0) - d4, (double)(y + 0) - d4, (double)z + 1.0D + d5, d0, d3);
      }

      return true;
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return false;
   }

   public int getRenderId() {
      return Witchery.proxy.getVineRenderId();
   }
}
