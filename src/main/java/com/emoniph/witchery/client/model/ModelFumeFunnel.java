package com.emoniph.witchery.client.model;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockWitchesOven;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class ModelFumeFunnel extends ModelBase {

   ModelRenderer chimney;
   ModelRenderer chimneyTop;
   ModelRenderer base;
   ModelRenderer body;
   ModelRenderer tubeLeft;
   ModelRenderer tubeRight;
   ModelRenderer pipeTop2;
   ModelRenderer pipeTop3;
   ModelRenderer pipeTop4;
   ModelRenderer pipeTop5;
   ModelRenderer pipeBottom1;
   ModelRenderer pipeBottom2;
   ModelRenderer pipeBottom3;
   ModelRenderer pipeBottom4;
   ModelRenderer pipeTop1;
   ModelRenderer top1;
   ModelRenderer pipeBottom5;
   ModelRenderer top2;
   ModelRenderer filterLeft;
   ModelRenderer filterRight;
   ModelRenderer filterMid;
   ModelRenderer filterCase;
   final boolean filtered;


   public ModelFumeFunnel(boolean filtered) {
      this.filtered = filtered;
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.base = new ModelRenderer(this, 0, 51);
      this.base.addBox(0.0F, 0.0F, 0.0F, 12, 1, 12);
      this.base.setRotationPoint(-6.0F, 23.0F, -6.0F);
      this.base.setTextureSize(64, 64);
      this.base.mirror = true;
      this.setRotation(this.base, 0.0F, 0.0F, 0.0F);
      this.body = new ModelRenderer(this, 4, 27);
      this.body.addBox(0.0F, 0.0F, 0.0F, 10, 11, 10);
      this.body.setRotationPoint(-5.0F, 12.0F, -5.0F);
      this.body.setTextureSize(64, 64);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.tubeLeft = new ModelRenderer(this, 1, 18);
      this.tubeLeft.addBox(0.0F, 0.0F, 0.0F, 5, 2, 2);
      this.tubeLeft.setRotationPoint(-10.0F, 17.0F, -1.0F);
      this.tubeLeft.setTextureSize(64, 64);
      this.tubeLeft.mirror = true;
      this.setRotation(this.tubeLeft, 0.0F, 0.0F, 0.0F);
      this.tubeRight = new ModelRenderer(this, 1, 18);
      this.tubeRight.addBox(0.0F, 1.0F, 0.0F, 5, 2, 2);
      this.tubeRight.setRotationPoint(5.0F, 18.0F, 1.0F);
      this.tubeRight.setTextureSize(64, 64);
      this.tubeRight.mirror = true;
      this.setRotation(this.tubeRight, 0.0F, 0.0F, 0.0F);
      this.pipeTop2 = new ModelRenderer(this, 0, 0);
      this.pipeTop2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 6);
      this.pipeTop2.setRotationPoint(-4.0F, 8.0F, -3.0F);
      this.pipeTop2.setTextureSize(64, 64);
      this.pipeTop2.mirror = true;
      this.setRotation(this.pipeTop2, 0.0F, 0.0F, 0.0F);
      this.pipeTop3 = new ModelRenderer(this, 0, 0);
      this.pipeTop3.addBox(0.0F, 0.0F, 0.0F, 11, 1, 1);
      this.pipeTop3.setRotationPoint(-3.0F, 8.0F, -3.0F);
      this.pipeTop3.setTextureSize(64, 64);
      this.pipeTop3.mirror = true;
      this.setRotation(this.pipeTop3, 0.0F, 0.0F, 0.0F);
      this.pipeTop4 = new ModelRenderer(this, 0, 0);
      this.pipeTop4.addBox(0.0F, 0.0F, 0.0F, 1, 11, 1);
      this.pipeTop4.setRotationPoint(7.0F, 9.0F, -3.0F);
      this.pipeTop4.setTextureSize(64, 64);
      this.pipeTop4.mirror = true;
      this.setRotation(this.pipeTop4, 0.0F, 0.0F, 0.0F);
      this.pipeTop5 = new ModelRenderer(this, 0, 0);
      this.pipeTop5.addBox(0.0F, 0.0F, 0.0F, 2, 3, 3);
      this.pipeTop5.setRotationPoint(5.0F, 18.0F, -4.0F);
      this.pipeTop5.setTextureSize(64, 64);
      this.pipeTop5.mirror = true;
      this.setRotation(this.pipeTop5, 0.0F, 0.0F, 0.0F);
      this.pipeBottom1 = new ModelRenderer(this, 0, 0);
      this.pipeBottom1.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1);
      this.pipeBottom1.setRotationPoint(-7.0F, 13.0F, -3.0F);
      this.pipeBottom1.setTextureSize(64, 64);
      this.pipeBottom1.mirror = true;
      this.setRotation(this.pipeBottom1, 0.0F, 0.0F, 0.0F);
      this.pipeBottom2 = new ModelRenderer(this, 0, 0);
      this.pipeBottom2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 4);
      this.pipeBottom2.setRotationPoint(-7.0F, 20.0F, -7.0F);
      this.pipeBottom2.setTextureSize(64, 64);
      this.pipeBottom2.mirror = true;
      this.setRotation(this.pipeBottom2, 0.0F, 0.0F, 0.0F);
      this.pipeBottom3 = new ModelRenderer(this, 0, 0);
      this.pipeBottom3.addBox(0.0F, 0.0F, 0.0F, 5, 1, 1);
      this.pipeBottom3.setRotationPoint(-6.0F, 20.0F, -7.0F);
      this.pipeBottom3.setTextureSize(64, 64);
      this.pipeBottom3.mirror = true;
      this.setRotation(this.pipeBottom3, 0.0F, 0.0F, 0.0F);
      this.pipeBottom4 = new ModelRenderer(this, 0, 0);
      this.pipeBottom4.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
      this.pipeBottom4.setRotationPoint(-2.0F, 21.0F, -7.0F);
      this.pipeBottom4.setTextureSize(64, 64);
      this.pipeBottom4.mirror = true;
      this.setRotation(this.pipeBottom4, 0.0F, 0.0F, 0.0F);
      this.pipeTop1 = new ModelRenderer(this, 0, 0);
      this.pipeTop1.addBox(0.0F, 0.0F, 0.0F, 1, 3, 1);
      this.pipeTop1.setRotationPoint(-4.0F, 8.0F, 3.0F);
      this.pipeTop1.setTextureSize(64, 64);
      this.pipeTop1.mirror = true;
      this.setRotation(this.pipeTop1, 0.0F, 0.0F, 0.0F);
      this.top1 = new ModelRenderer(this, 0, 51);
      this.top1.addBox(0.0F, 0.0F, 0.0F, 12, 1, 12);
      this.top1.setRotationPoint(-6.0F, 11.0F, -6.0F);
      this.top1.setTextureSize(64, 64);
      this.top1.mirror = true;
      this.setRotation(this.top1, 0.0F, 0.0F, 0.0F);
      this.pipeBottom5 = new ModelRenderer(this, 0, 0);
      this.pipeBottom5.addBox(0.0F, 0.0F, 0.0F, 1, 7, 1);
      this.pipeBottom5.setRotationPoint(-7.0F, 14.0F, -3.0F);
      this.pipeBottom5.setTextureSize(64, 64);
      this.pipeBottom5.mirror = true;
      this.setRotation(this.pipeBottom5, 0.0F, 0.0F, 0.0F);
      this.top2 = new ModelRenderer(this, 37, 55);
      this.top2.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6);
      this.top2.setRotationPoint(-3.0F, 10.0F, -3.0F);
      this.top2.setTextureSize(64, 64);
      this.top2.mirror = true;
      this.setRotation(this.top2, 0.0F, 0.0F, 0.0F);
      this.filterLeft = new ModelRenderer(this, 0, 0);
      this.filterLeft.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2);
      this.filterLeft.setRotationPoint(-4.0F, 14.0F, -7.0F);
      this.filterLeft.setTextureSize(64, 64);
      this.filterLeft.mirror = true;
      this.setRotation(this.filterLeft, 0.0F, 0.0F, 0.0F);
      this.filterRight = new ModelRenderer(this, 0, 0);
      this.filterRight.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2);
      this.filterRight.setRotationPoint(3.0F, 14.0F, -7.0F);
      this.filterRight.setTextureSize(64, 64);
      this.filterRight.mirror = true;
      this.setRotation(this.filterRight, 0.0F, 0.0F, 0.0F);
      this.filterMid = new ModelRenderer(this, 24, 0);
      this.filterMid.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1);
      this.filterMid.setRotationPoint(-3.0F, 14.0F, -7.0F);
      this.filterMid.setTextureSize(64, 64);
      this.filterMid.mirror = true;
      this.setRotation(this.filterMid, 0.0F, 0.0F, 0.0F);
      this.filterCase = new ModelRenderer(this, 25, 3);
      this.filterCase.addBox(0.0F, 0.0F, 0.0F, 4, 3, 2);
      this.filterCase.setRotationPoint(-2.0F, 13.0F, -8.0F);
      this.filterCase.setTextureSize(64, 64);
      this.filterCase.mirror = true;
      this.setRotation(this.filterCase, 0.0F, 0.0F, 0.0F);
      this.chimney = new ModelRenderer(this, 27, 13);
      this.chimney.addBox(0.0F, 0.0F, 0.0F, 4, 10, 4);
      this.chimney.setRotationPoint(-2.0F, 14.0F, 3.0F);
      this.chimney.setTextureSize(64, 128);
      this.chimney.mirror = true;
      this.setRotation(this.chimney, 0.0F, 0.0F, 0.0F);
      this.chimneyTop = new ModelRenderer(this, 40, 7);
      this.chimneyTop.addBox(0.0F, 0.0F, 0.0F, 6, 3, 6);
      this.chimneyTop.setRotationPoint(-3.0F, 11.0F, 2.0F);
      this.chimneyTop.setTextureSize(64, 128);
      this.chimneyTop.mirror = true;
      this.setRotation(this.chimneyTop, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, TileEntity tile) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      boolean validTileEntity = tile != null && tile.getWorldObj() != null;
      boolean renderWideBody = true;
      if(validTileEntity) {
         int meta = tile.getBlockMetadata();
         switch(meta) {
         case 2:
            this.renderLeftGubbinsIfConnected(tile.getWorldObj(), tile.xCoord + 1, tile.yCoord, tile.zCoord, f5);
            this.renderRightGubbinsIfConnected(tile.getWorldObj(), tile.xCoord - 1, tile.yCoord, tile.zCoord, f5);
            break;
         case 3:
            this.renderLeftGubbinsIfConnected(tile.getWorldObj(), tile.xCoord - 1, tile.yCoord, tile.zCoord, f5);
            this.renderRightGubbinsIfConnected(tile.getWorldObj(), tile.xCoord + 1, tile.yCoord, tile.zCoord, f5);
            break;
         case 4:
            this.renderLeftGubbinsIfConnected(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord - 1, f5);
            this.renderRightGubbinsIfConnected(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord + 1, f5);
            break;
         case 5:
            this.renderLeftGubbinsIfConnected(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord + 1, f5);
            this.renderRightGubbinsIfConnected(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord - 1, f5);
         }

         Block block = tile.getWorldObj().getBlock(tile.xCoord, tile.yCoord - 1, tile.zCoord);
         if(BlockWitchesOven.isOven(block)) {
            this.chimney.render(f5);
            this.chimneyTop.render(f5);
            renderWideBody = false;
         }
      }

      if(renderWideBody) {
         this.base.render(f5);
         this.body.render(f5);
         this.top1.render(f5);
         this.top2.render(f5);
         if(this.filtered || validTileEntity && tile.getBlockType() == Witchery.Blocks.OVEN_FUMEFUNNEL_FILTERED) {
            this.filterLeft.render(f5);
            this.filterRight.render(f5);
            this.filterMid.render(f5);
            this.filterCase.render(f5);
         }
      }

   }

   private void renderLeftGubbinsIfConnected(World world, int xCoord, int yCoord, int zCoord, float f5) {
      Block block = world.getBlock(xCoord, yCoord, zCoord);
      if(BlockWitchesOven.isOven(block)) {
         this.tubeLeft.render(f5);
         this.pipeTop1.render(f5);
         this.pipeTop2.render(f5);
         this.pipeTop3.render(f5);
         this.pipeTop4.render(f5);
         this.pipeTop5.render(f5);
      }

   }

   private void renderRightGubbinsIfConnected(World world, int xCoord, int yCoord, int zCoord, float f5) {
      Block block = world.getBlock(xCoord, yCoord, zCoord);
      if(BlockWitchesOven.isOven(block)) {
         this.tubeRight.render(f5);
         this.pipeBottom1.render(f5);
         this.pipeBottom2.render(f5);
         this.pipeBottom3.render(f5);
         this.pipeBottom4.render(f5);
         this.pipeBottom5.render(f5);
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
