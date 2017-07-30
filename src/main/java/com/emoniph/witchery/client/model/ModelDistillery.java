package com.emoniph.witchery.client.model;

import com.emoniph.witchery.blocks.BlockDistillery;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

@SideOnly(Side.CLIENT)
public class ModelDistillery extends ModelBase {

   ModelRenderer stillBase;
   ModelRenderer stillMiddle;
   ModelRenderer stillTop;
   ModelRenderer stillBend;
   ModelRenderer stillTube;
   ModelRenderer frameTop;
   ModelRenderer frameLeft;
   ModelRenderer frameRight;
   ModelRenderer frameBase;
   ModelRenderer bottle1;
   ModelRenderer bottle2;
   ModelRenderer bottle3;
   ModelRenderer bottle4;


   public ModelDistillery() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.setTextureOffset("bottle1.bottle1Body", 52, 26);
      this.setTextureOffset("bottle1.bottle1Neck", 60, 24);
      this.setTextureOffset("bottle1.bottle1Top", 56, 21);
      this.setTextureOffset("bottle2.bottle2Body", 52, 26);
      this.setTextureOffset("bottle2.bottle2Neck", 60, 24);
      this.setTextureOffset("bottle2.bottle2Top", 56, 21);
      this.setTextureOffset("bottle3.bottle3Body", 52, 26);
      this.setTextureOffset("bottle3.bottle3Neck", 60, 24);
      this.setTextureOffset("bottle3.bottle3Top", 56, 21);
      this.setTextureOffset("bottle4.bottle4Body", 52, 26);
      this.setTextureOffset("bottle4.bottle4Neck", 60, 24);
      this.setTextureOffset("bottle4.bottle4Top", 56, 21);
      this.stillBase = new ModelRenderer(this, 0, 16);
      this.stillBase.addBox(0.0F, 0.0F, 0.0F, 10, 6, 10);
      this.stillBase.setRotationPoint(-5.0F, 18.0F, -2.0F);
      this.stillBase.setTextureSize(64, 64);
      this.stillBase.mirror = true;
      this.setRotation(this.stillBase, 0.0F, 0.0F, 0.0F);
      this.stillMiddle = new ModelRenderer(this, 0, 6);
      this.stillMiddle.addBox(0.0F, 0.0F, 0.0F, 6, 4, 6);
      this.stillMiddle.setRotationPoint(-3.0F, 14.0F, 0.0F);
      this.stillMiddle.setTextureSize(64, 64);
      this.stillMiddle.mirror = true;
      this.setRotation(this.stillMiddle, 0.0F, 0.0F, 0.0F);
      this.stillTop = new ModelRenderer(this, 25, 9);
      this.stillTop.addBox(0.0F, 0.0F, 0.0F, 4, 3, 4);
      this.stillTop.setRotationPoint(-2.0F, 11.0F, 1.0F);
      this.stillTop.setTextureSize(64, 64);
      this.stillTop.mirror = true;
      this.setRotation(this.stillTop, 0.0F, 0.0F, 0.0F);
      this.stillBend = new ModelRenderer(this, 0, 0);
      this.stillBend.addBox(0.0F, 0.0F, 0.0F, 2, 2, 4);
      this.stillBend.setRotationPoint(-1.0F, 9.0F, -1.0F);
      this.stillBend.setTextureSize(64, 64);
      this.stillBend.mirror = true;
      this.setRotation(this.stillBend, 0.0F, 0.0F, 0.0F);
      this.stillTube = new ModelRenderer(this, 46, 10);
      this.stillTube.addBox(-0.5F, -0.5F, 0.0F, 1, 1, 8);
      this.stillTube.setRotationPoint(0.0F, 10.0F, 0.0F);
      this.stillTube.setTextureSize(64, 64);
      this.stillTube.mirror = true;
      this.setRotation(this.stillTube, -2.341978F, 0.0F, 0.0F);
      this.frameTop = new ModelRenderer(this, 30, 6);
      this.frameTop.addBox(0.0F, 0.0F, 0.0F, 16, 1, 1);
      this.frameTop.setRotationPoint(-8.0F, 15.0F, -6.0F);
      this.frameTop.setTextureSize(64, 64);
      this.frameTop.mirror = true;
      this.setRotation(this.frameTop, 0.0F, 0.0F, 0.0F);
      this.frameLeft = new ModelRenderer(this, 47, 24);
      this.frameLeft.addBox(0.0F, 0.0F, 0.0F, 1, 7, 1);
      this.frameLeft.setRotationPoint(-8.0F, 16.0F, -6.0F);
      this.frameLeft.setTextureSize(64, 64);
      this.frameLeft.mirror = true;
      this.setRotation(this.frameLeft, 0.0F, 0.0F, 0.0F);
      this.frameRight = new ModelRenderer(this, 47, 24);
      this.frameRight.addBox(0.0F, 0.0F, 0.0F, 1, 7, 1);
      this.frameRight.setRotationPoint(7.0F, 16.0F, -6.0F);
      this.frameRight.setTextureSize(64, 64);
      this.frameRight.mirror = true;
      this.setRotation(this.frameRight, 0.0F, 0.0F, 0.0F);
      this.frameBase = new ModelRenderer(this, 22, 0);
      this.frameBase.addBox(0.0F, 0.0F, 0.0F, 16, 1, 5);
      this.frameBase.setRotationPoint(-8.0F, 23.0F, -8.0F);
      this.frameBase.setTextureSize(64, 64);
      this.frameBase.mirror = true;
      this.setRotation(this.frameBase, 0.0F, 0.0F, 0.0F);
      this.bottle1 = new ModelRenderer(this, "bottle1");
      this.bottle1.setRotationPoint(-7.0F, 16.0F, -7.0F);
      this.setRotation(this.bottle1, 0.0F, 0.0F, 0.0F);
      this.bottle1.mirror = true;
      this.bottle1.addBox("bottle1Body", 0.0F, 2.0F, 0.0F, 3, 3, 3);
      this.bottle1.addBox("bottle1Neck", 1.0F, 1.0F, 1.0F, 1, 1, 1);
      this.bottle1.addBox("bottle1Top", 0.5F, 0.0F, 0.5F, 2, 1, 2);
      this.bottle2 = new ModelRenderer(this, "bottle2");
      this.bottle2.setRotationPoint(-3.3F, 16.0F, -7.0F);
      this.setRotation(this.bottle2, 0.0174533F, 0.0F, 0.0F);
      this.bottle2.mirror = true;
      this.bottle2.addBox("bottle2Body", 0.0F, 2.0F, 0.0F, 3, 3, 3);
      this.bottle2.addBox("bottle2Neck", 1.0F, 1.0F, 1.0F, 1, 1, 1);
      this.bottle2.addBox("bottle2Top", 0.5F, 0.0F, 0.5F, 2, 1, 2);
      this.bottle3 = new ModelRenderer(this, "bottle3");
      this.bottle3.setRotationPoint(0.4F, 16.0F, -7.0F);
      this.setRotation(this.bottle3, 0.0F, 0.0F, 0.0F);
      this.bottle3.mirror = true;
      this.bottle3.addBox("bottle3Body", 0.0F, 2.0F, 0.0F, 3, 3, 3);
      this.bottle3.addBox("bottle3Neck", 1.0F, 1.0F, 1.0F, 1, 1, 1);
      this.bottle3.addBox("bottle3Top", 0.5F, 0.0F, 0.5F, 2, 1, 2);
      this.bottle4 = new ModelRenderer(this, "bottle4");
      this.bottle4.setRotationPoint(4.0F, 16.0F, -7.0F);
      this.setRotation(this.bottle4, 0.0F, 0.0F, 0.0F);
      this.bottle4.mirror = true;
      this.bottle4.addBox("bottle4Body", 0.0F, 2.0F, 0.0F, 3, 3, 3);
      this.bottle4.addBox("bottle4Neck", 1.0F, 1.0F, 1.0F, 1, 1, 1);
      this.bottle4.addBox("bottle4Top", 0.5F, 0.0F, 0.5F, 2, 1, 2);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, TileEntity tileEntity) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.stillBase.render(f5);
      this.stillMiddle.render(f5);
      this.stillTop.render(f5);
      this.stillBend.render(f5);
      this.stillTube.render(f5);
      this.frameTop.render(f5);
      this.frameLeft.render(f5);
      this.frameRight.render(f5);
      this.frameBase.render(f5);
      if(tileEntity != null && tileEntity.getWorldObj() != null) {
         BlockDistillery.TileEntityDistillery te = (BlockDistillery.TileEntityDistillery)tileEntity;
         ModelRenderer[] bottles = new ModelRenderer[]{this.bottle1, this.bottle2, this.bottle3, this.bottle4};
         ItemStack jars = te.getStackInSlot(2);
         if(jars != null) {
            for(int i = 0; i < jars.stackSize && i < bottles.length; ++i) {
               bottles[i].render(f5);
            }
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
