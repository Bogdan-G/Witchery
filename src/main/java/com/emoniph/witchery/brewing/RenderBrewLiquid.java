package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.BlockBrewLiquidEffect;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class RenderBrewLiquid implements ISimpleBlockRenderingHandler {

   public static final RenderBrewLiquid instance = new RenderBrewLiquid();
   static final float LIGHT_Y_NEG = 0.5F;
   static final float LIGHT_Y_POS = 1.0F;
   static final float LIGHT_XZ_NEG = 0.8F;
   static final float LIGHT_XZ_POS = 0.6F;
   static final double RENDER_OFFSET = 0.0010000000474974513D;


   public float getFluidHeightAverage(float[] flow) {
      float total = 0.0F;
      int count = 0;
      float end = 0.0F;

      for(int i = 0; i < flow.length; ++i) {
         if(flow[i] >= 0.875F && end != 1.0F) {
            end = flow[i];
         }

         if(flow[i] >= 0.0F) {
            total += flow[i];
            ++count;
         }
      }

      if(end == 0.0F) {
         end = total / (float)count;
      }

      return end;
   }

   public float getFluidHeightForRender(IBlockAccess world, int x, int y, int z, BlockBrewLiquidEffect block) {
      if(world.getBlock(x, y, z) == block) {
         if(world.getBlock(x, y - block.densityDir, z).getMaterial().isLiquid()) {
            return 1.0F;
         }

         if(world.getBlockMetadata(x, y, z) == block.getMaxRenderHeightMeta()) {
            return 0.875F;
         }
      }

      return !world.getBlock(x, y, z).getMaterial().isSolid() && world.getBlock(x, y - block.densityDir, z) == block?1.0F:block.getQuantaPercentage(world, x, y, z) * 0.875F;
   }

   public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      if(!(block instanceof BlockBrewLiquidEffect)) {
         return false;
      } else {
         Tessellator tessellator = Tessellator.instance;
         int color = block.colorMultiplier(world, x, y, z);
         float red = (float)(color >> 16 & 255) / 255.0F;
         float green = (float)(color >> 8 & 255) / 255.0F;
         float blue = (float)(color & 255) / 255.0F;
         BlockBrewLiquidEffect theFluid = (BlockBrewLiquidEffect)block;
         int bMeta = world.getBlockMetadata(x, y, z);
         boolean renderTop = world.getBlock(x, y - theFluid.densityDir, z) != theFluid;
         boolean renderBottom = block.shouldSideBeRendered(world, x, y + theFluid.densityDir, z, 0) && world.getBlock(x, y + theFluid.densityDir, z) != theFluid;
         boolean[] renderSides = new boolean[]{block.shouldSideBeRendered(world, x, y, z - 1, 2), block.shouldSideBeRendered(world, x, y, z + 1, 3), block.shouldSideBeRendered(world, x - 1, y, z, 4), block.shouldSideBeRendered(world, x + 1, y, z, 5)};
         if(!renderTop && !renderBottom && !renderSides[0] && !renderSides[1] && !renderSides[2] && !renderSides[3]) {
            return false;
         } else {
            boolean rendered = false;
            float flow11 = this.getFluidHeightForRender(world, x, y, z, theFluid);
            double heightNW;
            double heightSW;
            double heightSE;
            double heightNE;
            float x2;
            if(flow11 != 1.0F) {
               float rises = this.getFluidHeightForRender(world, x - 1, y, z - 1, theFluid);
               float side = this.getFluidHeightForRender(world, x - 1, y, z, theFluid);
               x2 = this.getFluidHeightForRender(world, x - 1, y, z + 1, theFluid);
               float z2 = this.getFluidHeightForRender(world, x, y, z - 1, theFluid);
               float iconFlow = this.getFluidHeightForRender(world, x, y, z + 1, theFluid);
               float ty1 = this.getFluidHeightForRender(world, x + 1, y, z - 1, theFluid);
               float flow21 = this.getFluidHeightForRender(world, x + 1, y, z, theFluid);
               float tx1 = this.getFluidHeightForRender(world, x + 1, y, z + 1, theFluid);
               heightNW = (double)this.getFluidHeightAverage(new float[]{rises, side, z2, flow11});
               heightSW = (double)this.getFluidHeightAverage(new float[]{side, x2, iconFlow, flow11});
               heightSE = (double)this.getFluidHeightAverage(new float[]{iconFlow, flow21, tx1, flow11});
               heightNE = (double)this.getFluidHeightAverage(new float[]{z2, ty1, flow21, flow11});
            } else {
               heightNW = (double)flow11;
               heightSW = (double)flow11;
               heightSE = (double)flow11;
               heightNE = (double)flow11;
            }

            boolean var51 = theFluid.densityDir == 1;
            double tx2;
            double ty2;
            double tz2;
            double tz1;
            float v1Flow;
            float v2Flow;
            double var59;
            double var56;
            if(renderer.renderAllFaces || renderTop) {
               rendered = true;
               IIcon var55 = this.getIcon(block.getIcon(1, bMeta));
               x2 = (float)BlockBrewLiquidEffect.getFlowDirection(world, x, y, z);
               if(x2 > -999.0F) {
                  var55 = this.getIcon(block.getIcon(2, bMeta));
               }

               heightNW -= 0.0010000000474974513D;
               heightSW -= 0.0010000000474974513D;
               heightSE -= 0.0010000000474974513D;
               heightNE -= 0.0010000000474974513D;
               double u1Flow;
               double var54;
               if(x2 < -999.0F) {
                  var56 = (double)var55.getInterpolatedU(0.0D);
                  tz1 = (double)var55.getInterpolatedV(0.0D);
                  var54 = var56;
                  tx2 = (double)var55.getInterpolatedV(16.0D);
                  ty2 = (double)var55.getInterpolatedU(16.0D);
                  u1Flow = tx2;
                  var59 = ty2;
                  tz2 = tz1;
               } else {
                  v1Flow = MathHelper.sin(x2) * 0.25F;
                  v2Flow = MathHelper.cos(x2) * 0.25F;
                  var56 = (double)var55.getInterpolatedU((double)(8.0F + (-v2Flow - v1Flow) * 16.0F));
                  tz1 = (double)var55.getInterpolatedV((double)(8.0F + (-v2Flow + v1Flow) * 16.0F));
                  var54 = (double)var55.getInterpolatedU((double)(8.0F + (-v2Flow + v1Flow) * 16.0F));
                  tx2 = (double)var55.getInterpolatedV((double)(8.0F + (v2Flow + v1Flow) * 16.0F));
                  ty2 = (double)var55.getInterpolatedU((double)(8.0F + (v2Flow + v1Flow) * 16.0F));
                  u1Flow = (double)var55.getInterpolatedV((double)(8.0F + (v2Flow - v1Flow) * 16.0F));
                  var59 = (double)var55.getInterpolatedU((double)(8.0F + (v2Flow - v1Flow) * 16.0F));
                  tz2 = (double)var55.getInterpolatedV((double)(8.0F + (-v2Flow - v1Flow) * 16.0F));
               }

               tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
               tessellator.setColorOpaque_F(1.0F * red, 1.0F * green, 1.0F * blue);
               if(!var51) {
                  tessellator.addVertexWithUV((double)(x + 0), (double)y + heightNW, (double)(z + 0), var56, tz1);
                  tessellator.addVertexWithUV((double)(x + 0), (double)y + heightSW, (double)(z + 1), var54, tx2);
                  tessellator.addVertexWithUV((double)(x + 1), (double)y + heightSE, (double)(z + 1), ty2, u1Flow);
                  tessellator.addVertexWithUV((double)(x + 1), (double)y + heightNE, (double)(z + 0), var59, tz2);
               } else {
                  tessellator.addVertexWithUV((double)(x + 1), (double)(y + 1) - heightNE, (double)(z + 0), var59, tz2);
                  tessellator.addVertexWithUV((double)(x + 1), (double)(y + 1) - heightSE, (double)(z + 1), ty2, u1Flow);
                  tessellator.addVertexWithUV((double)(x + 0), (double)(y + 1) - heightSW, (double)(z + 1), var54, tx2);
                  tessellator.addVertexWithUV((double)(x + 0), (double)(y + 1) - heightNW, (double)(z + 0), var56, tz1);
               }
            }

            if(renderer.renderAllFaces || renderBottom) {
               rendered = true;
               tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y - 1, z));
               if(!var51) {
                  tessellator.setColorOpaque_F(0.5F * red, 0.5F * green, 0.5F * blue);
                  renderer.renderFaceYNeg(block, (double)x, (double)y + 0.0010000000474974513D, (double)z, this.getIcon(block.getIcon(0, bMeta)));
               } else {
                  tessellator.setColorOpaque_F(1.0F * red, 1.0F * green, 1.0F * blue);
                  renderer.renderFaceYPos(block, (double)x, (double)y + 0.0010000000474974513D, (double)z, this.getIcon(block.getIcon(1, bMeta)));
               }
            }

            for(int var52 = 0; var52 < 4; ++var52) {
               int var53 = x;
               int var58 = z;
               switch(var52) {
               case 0:
                  var58 = z - 1;
                  break;
               case 1:
                  var58 = z + 1;
                  break;
               case 2:
                  var53 = x - 1;
                  break;
               case 3:
                  var53 = x + 1;
               }

               IIcon var57 = this.getIcon(block.getIcon(var52 + 2, bMeta));
               if(renderer.renderAllFaces || renderSides[var52]) {
                  rendered = true;
                  if(var52 == 0) {
                     var56 = heightNW;
                     ty2 = heightNE;
                     var59 = (double)x;
                     tx2 = (double)(x + 1);
                     tz1 = (double)z + 0.0010000000474974513D;
                     tz2 = (double)z + 0.0010000000474974513D;
                  } else if(var52 == 1) {
                     var56 = heightSE;
                     ty2 = heightSW;
                     var59 = (double)(x + 1);
                     tx2 = (double)x;
                     tz1 = (double)(z + 1) - 0.0010000000474974513D;
                     tz2 = (double)(z + 1) - 0.0010000000474974513D;
                  } else if(var52 == 2) {
                     var56 = heightSW;
                     ty2 = heightNW;
                     var59 = (double)x + 0.0010000000474974513D;
                     tx2 = (double)x + 0.0010000000474974513D;
                     tz1 = (double)(z + 1);
                     tz2 = (double)z;
                  } else {
                     var56 = heightNE;
                     ty2 = heightSE;
                     var59 = (double)(x + 1) - 0.0010000000474974513D;
                     tx2 = (double)(x + 1) - 0.0010000000474974513D;
                     tz1 = (double)z;
                     tz2 = (double)(z + 1);
                  }

                  float var60 = var57.getInterpolatedU(0.0D);
                  float u2Flow = var57.getInterpolatedU(8.0D);
                  v1Flow = var57.getInterpolatedV((1.0D - var56) * 16.0D * 0.5D);
                  v2Flow = var57.getInterpolatedV((1.0D - ty2) * 16.0D * 0.5D);
                  float v3Flow = var57.getInterpolatedV(8.0D);
                  tessellator.setBrightness(block.getMixedBrightnessForBlock(world, var53, y, var58));
                  float sideLighting = 1.0F;
                  if(var52 < 2) {
                     sideLighting = 0.8F;
                  } else {
                     sideLighting = 0.6F;
                  }

                  tessellator.setColorOpaque_F(1.0F * sideLighting * red, 1.0F * sideLighting * green, 1.0F * sideLighting * blue);
                  if(!var51) {
                     tessellator.addVertexWithUV(var59, (double)y + var56, tz1, (double)var60, (double)v1Flow);
                     tessellator.addVertexWithUV(tx2, (double)y + ty2, tz2, (double)u2Flow, (double)v2Flow);
                     tessellator.addVertexWithUV(tx2, (double)(y + 0), tz2, (double)u2Flow, (double)v3Flow);
                     tessellator.addVertexWithUV(var59, (double)(y + 0), tz1, (double)var60, (double)v3Flow);
                  } else {
                     tessellator.addVertexWithUV(var59, (double)(y + 1 - 0), tz1, (double)var60, (double)v3Flow);
                     tessellator.addVertexWithUV(tx2, (double)(y + 1 - 0), tz2, (double)u2Flow, (double)v3Flow);
                     tessellator.addVertexWithUV(tx2, (double)(y + 1) - ty2, tz2, (double)u2Flow, (double)v2Flow);
                     tessellator.addVertexWithUV(var59, (double)(y + 1) - var56, tz1, (double)var60, (double)v1Flow);
                  }
               }
            }

            renderer.renderMinY = 0.0D;
            renderer.renderMaxY = 1.0D;
            return rendered;
         }
      }
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return false;
   }

   public int getRenderId() {
      return Witchery.proxy.getBrewLiquidRenderId();
   }

   private IIcon getIcon(IIcon icon) {
      return (IIcon)(icon != null?icon:((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno"));
   }

}
