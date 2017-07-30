package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class RenderBrewGas implements ISimpleBlockRenderingHandler {

   public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {}

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      Tessellator tessellator = Tessellator.instance;
      int l = block.colorMultiplier(world, x, y, z);
      float red = (float)(l >> 16 & 255) / 255.0F;
      float green = (float)(l >> 8 & 255) / 255.0F;
      float blue = (float)(l & 255) / 255.0F;
      boolean flag = block.shouldSideBeRendered(world, x, y + 1, z, 1);
      boolean flag1 = block.shouldSideBeRendered(world, x, y - 1, z, 0);
      boolean[] aboolean = new boolean[]{block.shouldSideBeRendered(world, x, y, z - 1, 2), block.shouldSideBeRendered(world, x, y, z + 1, 3), block.shouldSideBeRendered(world, x - 1, y, z, 4), block.shouldSideBeRendered(world, x + 1, y, z, 5)};
      float opacityInner = 0.2F;
      float opacityOuter = 0.4F;
      renderer.renderAllFaces = true;
      if(!renderer.renderAllFaces && !flag && !flag1 && !aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3]) {
         return false;
      } else {
         boolean flag2 = false;
         float f3 = 0.5F;
         float f4 = 1.0F;
         float f5 = 0.8F;
         float f6 = 0.6F;
         double d0 = 0.0D;
         double d1 = 1.0D;
         Material material = block.getMaterial();
         int i1 = world.getBlockMetadata(x, y, z);
         double d2 = 1.0D;
         double d3 = 1.0D;
         double d4 = 1.0D;
         double d5 = 1.0D;
         double d6 = 0.0010000000474974513D;
         float f9;
         float f10;
         float f11;
         double d9;
         double d13;
         double d11;
         double d17;
         double d15;
         double d19;
         if(renderer.renderAllFaces || flag) {
            flag2 = true;
            IIcon k1 = renderer.getBlockIconFromSideAndMetadata(block, 1, i1);
            float l1 = 0.0F;
            if(l1 > -999.0F) {
               k1 = renderer.getBlockIconFromSideAndMetadata(block, 2, i1);
            }

            d2 -= d6;
            d3 -= d6;
            d4 -= d6;
            d5 -= d6;
            double j1;
            double f8;
            if(l1 < -999.0F) {
               j1 = (double)k1.getInterpolatedU(0.0D);
               d15 = (double)k1.getInterpolatedV(0.0D);
               d9 = j1;
               d17 = (double)k1.getInterpolatedV(16.0D);
               d11 = (double)k1.getInterpolatedU(16.0D);
               d19 = d17;
               d13 = d11;
               f8 = d15;
            } else {
               f9 = MathHelper.sin(l1) * 0.25F;
               f10 = MathHelper.cos(l1) * 0.25F;
               f11 = 8.0F;
               j1 = (double)k1.getInterpolatedU((double)(8.0F + (-f10 - f9) * 16.0F));
               d15 = (double)k1.getInterpolatedV((double)(8.0F + (-f10 + f9) * 16.0F));
               d9 = (double)k1.getInterpolatedU((double)(8.0F + (-f10 + f9) * 16.0F));
               d17 = (double)k1.getInterpolatedV((double)(8.0F + (f10 + f9) * 16.0F));
               d11 = (double)k1.getInterpolatedU((double)(8.0F + (f10 + f9) * 16.0F));
               d19 = (double)k1.getInterpolatedV((double)(8.0F + (f10 - f9) * 16.0F));
               d13 = (double)k1.getInterpolatedU((double)(8.0F + (f10 - f9) * 16.0F));
               f8 = (double)k1.getInterpolatedV((double)(8.0F + (-f10 - f9) * 16.0F));
            }

            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            tessellator.setColorRGBA_F(f4 * red, f4 * green, f4 * blue, flag?opacityOuter:opacityInner);
            tessellator.addVertexWithUV((double)(x + 0), (double)y + d2, (double)(z + 0), j1, d15);
            tessellator.addVertexWithUV((double)(x + 0), (double)y + d3, (double)(z + 1), d9, d17);
            tessellator.addVertexWithUV((double)(x + 1), (double)y + d4, (double)(z + 1), d11, d19);
            tessellator.addVertexWithUV((double)(x + 1), (double)y + d5, (double)(z + 0), d13, f8);
            tessellator.addVertexWithUV((double)(x + 0), (double)y + d2, (double)(z + 0), j1, d15);
            tessellator.addVertexWithUV((double)(x + 1), (double)y + d5, (double)(z + 0), d13, f8);
            tessellator.addVertexWithUV((double)(x + 1), (double)y + d4, (double)(z + 1), d11, d19);
            tessellator.addVertexWithUV((double)(x + 0), (double)y + d3, (double)(z + 1), d9, d17);
         }

         if(renderer.renderAllFaces || flag1) {
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y - 1, z));
            tessellator.setColorRGBA_F(f4 * red, f4 * green, f4 * blue, flag1?opacityOuter:opacityInner);
            renderer.renderFaceYNeg(block, (double)x, (double)y + d6, (double)z, renderer.getBlockIconFromSide(block, 0));
            flag2 = true;
         }

         for(int var62 = 0; var62 < 4; ++var62) {
            int var63 = x;
            int var61 = z;
            if(var62 == 0) {
               var61 = z - 1;
            }

            if(var62 == 1) {
               ++var61;
            }

            if(var62 == 2) {
               var63 = x - 1;
            }

            if(var62 == 3) {
               ++var63;
            }

            IIcon iicon1 = renderer.getBlockIconFromSideAndMetadata(block, var62 + 2, i1);
            if(renderer.renderAllFaces || aboolean[var62]) {
               if(var62 == 0) {
                  d9 = d2;
                  d11 = d5;
                  d13 = (double)x;
                  d17 = (double)(x + 1);
                  d15 = (double)z + d6;
                  d19 = (double)z + d6;
               } else if(var62 == 1) {
                  d9 = d4;
                  d11 = d3;
                  d13 = (double)(x + 1);
                  d17 = (double)x;
                  d15 = (double)(z + 1) - d6;
                  d19 = (double)(z + 1) - d6;
               } else if(var62 == 2) {
                  d9 = d3;
                  d11 = d2;
                  d13 = (double)x + d6;
                  d17 = (double)x + d6;
                  d15 = (double)(z + 1);
                  d19 = (double)z;
               } else {
                  d9 = d5;
                  d11 = d4;
                  d13 = (double)(x + 1) - d6;
                  d17 = (double)(x + 1) - d6;
                  d15 = (double)z;
                  d19 = (double)(z + 1);
               }

               flag2 = true;
               float var64 = iicon1.getInterpolatedU(0.0D);
               f9 = iicon1.getInterpolatedU(8.0D);
               f10 = iicon1.getInterpolatedV((1.0D - d9) * 16.0D * 0.5D);
               f11 = iicon1.getInterpolatedV((1.0D - d11) * 16.0D * 0.5D);
               float f12 = iicon1.getInterpolatedV(8.0D);
               tessellator.setBrightness(block.getMixedBrightnessForBlock(world, var63, y, var61));
               float f13 = 1.0F;
               float var10000 = f13 * (var62 < 2?f5:f6);
               tessellator.setColorRGBA_F(f4 * red, f4 * green, f4 * blue, aboolean[var62]?opacityOuter:opacityInner);
               tessellator.addVertexWithUV(d13, (double)y + d9, d15, (double)var64, (double)f10);
               tessellator.addVertexWithUV(d17, (double)y + d11, d19, (double)f9, (double)f11);
               tessellator.addVertexWithUV(d17, (double)(y + 0), d19, (double)f9, (double)f12);
               tessellator.addVertexWithUV(d13, (double)(y + 0), d15, (double)var64, (double)f12);
               tessellator.addVertexWithUV(d13, (double)(y + 0), d15, (double)var64, (double)f12);
               tessellator.addVertexWithUV(d17, (double)(y + 0), d19, (double)f9, (double)f12);
               tessellator.addVertexWithUV(d17, (double)y + d11, d19, (double)f9, (double)f11);
               tessellator.addVertexWithUV(d13, (double)y + d9, d15, (double)var64, (double)f10);
            }
         }

         renderer.renderMinY = d0;
         renderer.renderMaxY = d1;
         return flag2;
      }
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return false;
   }

   public int getRenderId() {
      return Witchery.proxy.getGasRenderId();
   }
}
