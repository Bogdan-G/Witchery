package com.emoniph.witchery.client.model;

import com.emoniph.witchery.blocks.BlockKettle;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelKettle extends ModelBase {

   ModelRenderer sideFront;
   ModelRenderer sideBack;
   ModelRenderer sideLeft;
   ModelRenderer sideRight;
   ModelRenderer sideBottom;
   ModelRenderer crossbar;
   ModelRenderer[] liquid;
   ModelRenderer chainLF;
   ModelRenderer chainLB;
   ModelRenderer chainRF;
   ModelRenderer chainRB;
   ModelRenderer bottle1;
   ModelRenderer bottle2;
   private int ticks;


   public ModelKettle() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.setTextureOffset("bottle1.bottle1Body", 52, 5);
      this.setTextureOffset("bottle1.bottle1Neck", 60, 3);
      this.setTextureOffset("bottle1.bottle1Top", 56, 0);
      this.setTextureOffset("bottle2.bottle2Body", 52, 5);
      this.setTextureOffset("bottle2.bottle2Neck", 60, 3);
      this.setTextureOffset("bottle2.bottle2Top", 56, 0);
      this.sideFront = new ModelRenderer(this, 0, 0);
      this.sideFront.addBox(0.0F, 0.0F, 0.0F, 9, 6, 1);
      this.sideFront.setRotationPoint(-5.0F, 18.0F, -5.0F);
      this.sideFront.setTextureSize(64, 64);
      this.sideFront.mirror = true;
      this.setRotation(this.sideFront, 0.0F, 0.0F, 0.0F);
      this.sideBack = new ModelRenderer(this, 0, 0);
      this.sideBack.addBox(0.0F, 0.0F, 0.0F, 9, 6, 1);
      this.sideBack.setRotationPoint(-4.0F, 18.0F, 4.0F);
      this.sideBack.setTextureSize(64, 64);
      this.sideBack.mirror = true;
      this.setRotation(this.sideBack, 0.0F, 0.0F, 0.0F);
      this.sideLeft = new ModelRenderer(this, 0, 0);
      this.sideLeft.addBox(0.0F, 0.0F, 0.0F, 9, 6, 1);
      this.sideLeft.setRotationPoint(-5.0F, 18.0F, 5.0F);
      this.sideLeft.setTextureSize(64, 64);
      this.sideLeft.mirror = true;
      this.setRotation(this.sideLeft, 0.0F, 1.570796F, 0.0F);
      this.sideRight = new ModelRenderer(this, 0, 0);
      this.sideRight.addBox(0.0F, 0.0F, 0.0F, 9, 6, 1);
      this.sideRight.setRotationPoint(4.0F, 18.0F, 4.0F);
      this.sideRight.setTextureSize(64, 64);
      this.sideRight.mirror = true;
      this.setRotation(this.sideRight, 0.0F, 1.570796F, 0.0F);
      this.sideBottom = new ModelRenderer(this, 13, 0);
      this.sideBottom.addBox(0.0F, 0.0F, 0.0F, 8, 1, 8);
      this.sideBottom.setRotationPoint(-4.0F, 23.0F, -4.0F);
      this.sideBottom.setTextureSize(64, 64);
      this.sideBottom.mirror = true;
      this.setRotation(this.sideBottom, 0.0F, 0.0F, 0.0F);
      this.crossbar = new ModelRenderer(this, 0, 10);
      this.crossbar.addBox(-4.0F, 0.0F, 0.0F, 24, 2, 2);
      this.crossbar.setRotationPoint(-8.0F, 8.05F, -1.0F);
      this.crossbar.setTextureSize(64, 64);
      this.crossbar.mirror = true;
      this.setRotation(this.crossbar, 0.0F, 0.0F, 0.0F);
      this.liquid = new ModelRenderer[8];

      for(int i = 0; i < this.liquid.length; ++i) {
         this.liquid[i] = new ModelRenderer(this, i < 4?i * 16 - 8:(i - 4) * 16 - 8, i < 4?16:32);
         this.liquid[i].addBox(0.0F, 0.0F, 0.0F, 8, 0, 8);
         this.liquid[i].setRotationPoint(-4.0F, 20.0F, -4.0F);
         this.liquid[i].setTextureSize(64, 64);
         this.liquid[i].mirror = true;
         this.setRotation(this.liquid[i], 0.0F, 0.0F, 0.0F);
      }

      this.chainLF = new ModelRenderer(this, 0, 15);
      this.chainLF.addBox(0.0F, -0.5F, 0.0F, 11, 1, 0);
      this.chainLF.setRotationPoint(0.0F, 9.0F, 0.0F);
      this.chainLF.setTextureSize(64, 64);
      this.chainLF.mirror = true;
      this.chainLB = new ModelRenderer(this, 0, 15);
      this.chainLB.addBox(0.0F, -0.5F, 0.0F, 11, 1, 0);
      this.chainLB.setRotationPoint(0.0F, 9.0F, 0.0F);
      this.chainLB.setTextureSize(64, 64);
      this.chainLB.mirror = true;
      this.chainRF = new ModelRenderer(this, 0, 15);
      this.chainRF.addBox(0.0F, -0.5F, 0.0F, 11, 1, 0);
      this.chainRF.setRotationPoint(0.0F, 9.0F, 0.0F);
      this.chainRF.setTextureSize(64, 64);
      this.chainRF.mirror = true;
      this.chainRB = new ModelRenderer(this, 0, 15);
      this.chainRB.addBox(0.0F, -0.5F, 0.0F, 11, 1, 0);
      this.chainRB.setRotationPoint(0.0F, 9.0F, 0.0F);
      this.chainRB.setTextureSize(64, 64);
      this.chainRB.mirror = true;
      this.chainRB.mirror = false;
      this.setRotation(this.chainRB, 0.0F, -0.4F, 1.1F);
      this.setRotation(this.chainLB, 0.0F, 0.4F, 1.1F);
      this.setRotation(this.chainRF, 0.0F, 0.4F, 2.05F);
      this.setRotation(this.chainLF, 0.0F, -2.75F, -1.1F);
      this.bottle1 = new ModelRenderer(this, "bottle1");
      this.bottle1.setRotationPoint(-4.0F, 13.0F, -6.0F);
      this.setRotation(this.bottle1, 0.0F, 0.0F, 0.0F);
      this.bottle1.mirror = true;
      this.bottle1.addBox("bottle1Body", 0.0F, 2.0F, 0.0F, 3, 3, 3);
      this.bottle1.addBox("bottle1Neck", 1.0F, 1.0F, 1.0F, 1, 1, 1);
      this.bottle1.addBox("bottle1Top", 0.5F, 0.0F, 0.5F, 2, 1, 2);
      this.bottle2 = new ModelRenderer(this, "bottle2");
      this.bottle2.setRotationPoint(0.0F, 13.0F, -6.0F);
      this.setRotation(this.bottle2, 0.0F, 0.0F, 0.0F);
      this.bottle2.mirror = true;
      this.bottle2.addBox("bottle2Body", 0.0F, 2.0F, 0.0F, 3, 3, 3);
      this.bottle2.addBox("bottle2Neck", 1.0F, 1.0F, 1.0F, 1, 1, 1);
      this.bottle2.addBox("bottle2Top", 0.5F, 0.0F, 0.5F, 2, 1, 2);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, BlockKettle.TileEntityKettle kettleTileEntity) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.sideFront.render(f5);
      this.sideBack.render(f5);
      this.sideLeft.render(f5);
      this.sideRight.render(f5);
      this.sideBottom.render(f5);
      if(kettleTileEntity != null && kettleTileEntity.getWorldObj() != null) {
         int posX = MathHelper.floor_double((double)kettleTileEntity.xCoord);
         int posY = MathHelper.floor_double((double)kettleTileEntity.yCoord);
         int posZ = MathHelper.floor_double((double)kettleTileEntity.zCoord);
         if(!kettleTileEntity.getWorldObj().getBlock(posX, posY + 1, posZ).getMaterial().isSolid()) {
            this.crossbar.render(f5);
         }

         this.chainLF.render(f5);
         this.chainLB.render(f5);
         this.chainRF.render(f5);
         this.chainRB.render(f5);
         int bottles = kettleTileEntity.bottleCount();
         if(bottles > 0) {
            this.bottle1.render(f5);
            if(bottles > 1) {
               this.bottle2.render(f5);
            }
         }

         this.setRotation(this.chainRB, 0.0F, -0.4F, 1.1F);
         this.setRotation(this.chainLB, 0.0F, 0.4F, 1.1F);
         this.setRotation(this.chainRF, 0.0F, 0.4F, 2.05F);
         this.setRotation(this.chainLF, 0.0F, -2.75F, -1.1F);
         if(kettleTileEntity.isFilled()) {
            if(this.ticks >= 79) {
               this.ticks = 0;
            }

            ++this.ticks;
            int color = 0;
            float factor = 1.0F;
            if(kettleTileEntity.isRuined()) {
               color = -8429824;
               GL11.glColor4f(1.0F, 0.5F, 0.2F, 0.5F);
            } else if(kettleTileEntity.isReady()) {
               color = kettleTileEntity.getLiquidColor();
            } else if(kettleTileEntity.isBrewing()) {
               color = kettleTileEntity.getLiquidColor();
               factor = 0.5F;
            }

            if(color == 0) {
               color = -13148989;
               factor = 1.0F;
            }

            float red = (float)(color >>> 16 & 255) / 256.0F * factor;
            float green = (float)(color >>> 8 & 255) / 256.0F * factor;
            float blue = (float)(color & 255) / 256.0F * factor;
            GL11.glColor4f(red, green, blue, 1.0F);
            this.liquid[(int)Math.floor((double)(this.ticks / 20))].render(f5);
         }
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
