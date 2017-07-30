package com.emoniph.witchery.client.model;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockDreamCatcher;
import com.emoniph.witchery.item.ItemGeneral;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelDreamCatcher extends ModelBase {

   final ModelRenderer frameLeft;
   final ModelRenderer frameRight;
   final ModelRenderer frameTop;
   final ModelRenderer frameBottom;
   final ModelRenderer[] nets;
   final ModelRenderer decoration;


   public ModelDreamCatcher() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.frameLeft = new ModelRenderer(this, 0, 2);
      this.frameLeft.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
      this.frameLeft.setRotationPoint(-4.0F, 10.0F, 7.0F);
      this.frameLeft.setTextureSize(32, 32);
      this.frameLeft.mirror = true;
      this.setRotation(this.frameLeft, 0.0F, 0.0F, 0.0F);
      this.frameRight = new ModelRenderer(this, 0, 2);
      this.frameRight.addBox(0.0F, 0.0F, 0.0F, 1, 8, 1);
      this.frameRight.setRotationPoint(3.0F, 10.0F, 7.0F);
      this.frameRight.setTextureSize(32, 32);
      this.frameRight.mirror = true;
      this.setRotation(this.frameRight, 0.0F, 0.0F, 0.0F);
      this.frameTop = new ModelRenderer(this, 0, 0);
      this.frameTop.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1);
      this.frameTop.setRotationPoint(-3.0F, 10.0F, 7.0F);
      this.frameTop.setTextureSize(32, 32);
      this.frameTop.mirror = true;
      this.setRotation(this.frameTop, 0.0F, 0.0F, 0.0F);
      this.frameBottom = new ModelRenderer(this, 0, 0);
      this.frameBottom.addBox(0.0F, 0.0F, 0.0F, 6, 1, 1);
      this.frameBottom.setRotationPoint(-3.0F, 17.0F, 7.0F);
      this.frameBottom.setTextureSize(32, 32);
      this.frameBottom.mirror = true;
      this.setRotation(this.frameBottom, 0.0F, 0.0F, 0.0F);
      this.nets = new ModelRenderer[Witchery.Items.GENERIC.weaves.size()];

      for(int i = 0; i < Witchery.Items.GENERIC.weaves.size(); ++i) {
         ItemGeneral.DreamWeave weave = (ItemGeneral.DreamWeave)Witchery.Items.GENERIC.weaves.get(i);
         this.nets[i] = new ModelRenderer(this, weave.textureOffsetX, weave.textureOffsetY);
         this.nets[i].addBox(0.0F, 0.0F, 0.0F, 6, 6, 0);
         this.nets[i].setRotationPoint(-3.0F, 11.0F, 8.0F);
         this.nets[i].setTextureSize(32, 32);
         this.nets[i].mirror = true;
         this.setRotation(this.nets[i], 0.0F, 0.0F, 0.0F);
      }

      this.decoration = new ModelRenderer(this, 0, 12);
      this.decoration.addBox(0.0F, 0.0F, 0.0F, 8, 6, 0);
      this.decoration.setRotationPoint(-4.0F, 18.0F, 7.0F);
      this.decoration.setTextureSize(32, 32);
      this.decoration.mirror = true;
      this.setRotation(this.decoration, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, BlockDreamCatcher.TileEntityDreamCatcher tileEntity) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.frameLeft.render(f5);
      this.frameRight.render(f5);
      this.frameTop.render(f5);
      this.frameBottom.render(f5);
      ItemGeneral.DreamWeave weave = tileEntity.getWeave();
      if(weave != null) {
         this.nets[weave.weaveID].render(f5);
      }

      this.decoration.render(f5);
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
