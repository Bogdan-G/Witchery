package com.emoniph.witchery.client.model;

import com.emoniph.witchery.blocks.BlockSilverVat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelSilverVat extends ModelBase {

   public ModelRenderer base;
   public ModelRenderer sideBack;
   public ModelRenderer sideFront;
   public ModelRenderer sideLeft;
   public ModelRenderer sideRight;
   public ModelRenderer spoutLowerLeft;
   public ModelRenderer spoutUpperLeft;
   public ModelRenderer spoutUpperFront;
   public ModelRenderer spoutLowerFront;
   public ModelRenderer spoutUpperRight;
   public ModelRenderer spoutUpperBack;
   public ModelRenderer spoutLowerRight;
   public ModelRenderer silver1;
   public ModelRenderer spoutLowerBack;
   public ModelRenderer silver2;
   public ModelRenderer silver3;
   public ModelRenderer silver4;
   public ModelRenderer silver5;
   public ModelRenderer silver6;
   public ModelRenderer silver7;
   public ModelRenderer silver8;
   private final ModelRenderer[] silver;
   int capacity;


   public ModelSilverVat() {
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.base = new ModelRenderer(this, 0, 19);
      this.base.setRotationPoint(-6.0F, 23.0F, -6.0F);
      this.base.addBox(0.0F, 0.0F, 0.0F, 12, 1, 12, 0.0F);
      this.spoutLowerRight = new ModelRenderer(this, 15, 0);
      this.spoutLowerRight.setRotationPoint(-5.2F, 16.0F, -0.5F);
      this.spoutLowerRight.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
      this.spoutLowerFront = new ModelRenderer(this, 15, 0);
      this.spoutLowerFront.setRotationPoint(-0.5F, 16.0F, -5.2F);
      this.spoutLowerFront.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
      this.silver2 = new ModelRenderer(this, 0, 6);
      this.silver2.setRotationPoint(1.6F, 19.0F, -2.1F);
      this.silver2.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
      this.sideRight = new ModelRenderer(this, 38, 10);
      this.sideRight.setRotationPoint(-7.0F, 16.0F, -6.0F);
      this.sideRight.addBox(0.0F, 0.0F, 0.0F, 1, 8, 12, 0.0F);
      this.spoutUpperLeft = new ModelRenderer(this, 15, 3);
      this.spoutUpperLeft.setRotationPoint(4.0F, 14.0F, -1.5F);
      this.spoutUpperLeft.addBox(0.0F, 0.0F, 0.0F, 4, 2, 3, 0.0F);
      this.sideFront = new ModelRenderer(this, 34, 0);
      this.sideFront.setRotationPoint(-7.0F, 16.0F, -7.0F);
      this.sideFront.addBox(0.0F, 0.0F, 0.0F, 14, 8, 1, 0.0F);
      this.silver7 = new ModelRenderer(this, 0, 22);
      this.silver7.setRotationPoint(-0.5F, 19.0F, 2.0F);
      this.silver7.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
      this.spoutUpperRight = new ModelRenderer(this, 15, 3);
      this.spoutUpperRight.setRotationPoint(-8.0F, 14.0F, -1.5F);
      this.spoutUpperRight.addBox(0.0F, 0.0F, 0.0F, 4, 2, 3, 0.0F);
      this.sideBack = new ModelRenderer(this, 34, 0);
      this.sideBack.setRotationPoint(-7.0F, 16.0F, 6.0F);
      this.sideBack.addBox(0.0F, 0.0F, 0.0F, 14, 8, 1, 0.0F);
      this.spoutUpperFront = new ModelRenderer(this, 15, 9);
      this.spoutUpperFront.setRotationPoint(-1.5F, 14.0F, -8.0F);
      this.spoutUpperFront.addBox(0.0F, 0.0F, 0.0F, 3, 2, 4, 0.0F);
      this.spoutLowerLeft = new ModelRenderer(this, 15, 0);
      this.spoutLowerLeft.setRotationPoint(4.2F, 16.0F, -0.5F);
      this.spoutLowerLeft.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
      this.silver8 = new ModelRenderer(this, 0, 25);
      this.silver8.setRotationPoint(-1.2F, 19.0F, -0.3F);
      this.silver8.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
      this.silver3 = new ModelRenderer(this, 0, 9);
      this.silver3.setRotationPoint(-3.8F, 19.1F, 3.1F);
      this.silver3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2, 0.0F);
      this.silver6 = new ModelRenderer(this, 0, 19);
      this.silver6.setRotationPoint(-4.6F, 19.1F, -1.6F);
      this.silver6.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1, 0.0F);
      this.spoutLowerBack = new ModelRenderer(this, 15, 0);
      this.spoutLowerBack.setRotationPoint(-0.5F, 16.0F, 4.2F);
      this.spoutLowerBack.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
      this.silver1 = new ModelRenderer(this, 0, 3);
      this.silver1.setRotationPoint(-2.2F, 19.3F, -3.9F);
      this.silver1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
      this.silver4 = new ModelRenderer(this, 0, 13);
      this.silver4.setRotationPoint(-3.4F, 18.8F, 0.9F);
      this.silver4.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
      this.spoutUpperBack = new ModelRenderer(this, 15, 9);
      this.spoutUpperBack.setRotationPoint(-1.5F, 14.0F, 4.0F);
      this.spoutUpperBack.addBox(0.0F, 0.0F, 0.0F, 3, 2, 4, 0.0F);
      this.sideLeft = new ModelRenderer(this, 38, 10);
      this.sideLeft.setRotationPoint(6.0F, 16.0F, -6.0F);
      this.sideLeft.addBox(0.0F, 0.0F, 0.0F, 1, 8, 12, 0.0F);
      this.silver5 = new ModelRenderer(this, 0, 16);
      this.silver5.setRotationPoint(1.6F, 19.0F, -0.1F);
      this.silver5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
      this.silver = new ModelRenderer[]{this.silver1, this.silver2, this.silver3, this.silver4, this.silver5, this.silver6, this.silver7, this.silver8};
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, BlockSilverVat.TileEntitySilverVat te) {
      this.base.render(f5);
      this.sideRight.render(f5);
      this.sideFront.render(f5);
      this.sideBack.render(f5);
      this.sideLeft.render(f5);
      boolean isWorld = te != null && te.getWorldObj() != null;
      if(!isWorld || te.getWorldObj().getBlock(te.xCoord - 1, te.yCoord, te.zCoord).hasTileEntity()) {
         this.spoutUpperLeft.render(f5);
         this.spoutLowerLeft.render(f5);
      }

      if(!isWorld || te.getWorldObj().getBlock(te.xCoord + 1, te.yCoord, te.zCoord).hasTileEntity()) {
         this.spoutUpperRight.render(f5);
         this.spoutLowerRight.render(f5);
      }

      if(!isWorld || te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord - 1).hasTileEntity()) {
         this.spoutUpperFront.render(f5);
         this.spoutLowerFront.render(f5);
      }

      if(!isWorld || te.getWorldObj().getBlock(te.xCoord, te.yCoord, te.zCoord + 1).hasTileEntity()) {
         this.spoutUpperBack.render(f5);
         this.spoutLowerBack.render(f5);
      }

      int capacity = isWorld?(te.getStackInSlot(0) != null && te.getStackInSlot(0).stackSize > 0?Math.max(te.getStackInSlot(0).stackSize / 8, 1):0):0;

      for(int i = 0; i < capacity; ++i) {
         this.silver[i].render(f5);
      }

   }

   public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }

   public void setCapactiy(int i) {
      this.capacity = i;
   }
}
