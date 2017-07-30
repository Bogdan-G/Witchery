package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelWolfHead extends ModelBase {

   public ModelRenderer skeletonHead;


   public ModelWolfHead() {
      this(0, 35, 64, 32);
   }

   public ModelWolfHead(int textureOffsetX, int textureoffsetY, int textureWidth, int textureHeight) {
      super.textureWidth = textureWidth;
      super.textureHeight = textureHeight;
      this.skeletonHead = new ModelRenderer(this, 0, 0);
      this.skeletonHead.addBox(-3.0F, -6.0F, 0.0F, 6, 6, 4, 0.0F);
      this.skeletonHead.setRotationPoint(0.0F, 0.0F, 0.0F);
      float f = 0.0F;
      this.skeletonHead.setTextureOffset(16, 14).addBox(-3.0F, -8.0F, 3.0F, 2, 2, 1, f);
      this.skeletonHead.setTextureOffset(16, 14).addBox(1.0F, -8.0F, 3.0F, 2, 2, 1, f);
      this.skeletonHead.setTextureOffset(0, 10).addBox(-1.5F, -3.1F, -3.0F, 3, 3, 4, f);
   }

   public void render(Entity entity, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, entity);
      this.skeletonHead.render(p_78088_7_);
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
      super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
      this.skeletonHead.rotateAngleY = p_78087_4_ / 57.295776F;
      this.skeletonHead.rotateAngleX = p_78087_5_ / 57.295776F;
   }
}
