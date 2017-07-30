package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelCoffin extends ModelBase {

   public ModelRenderer sideLeft;
   public ModelRenderer sideRight;
   public ModelRenderer sideEnd;
   public ModelRenderer base;
   public ModelRenderer baseLower;
   public ModelRenderer lid;
   public ModelRenderer lidMid;
   public ModelRenderer lidTop;


   public ModelCoffin() {
      super.textureWidth = 128;
      super.textureHeight = 64;
      this.lid = new ModelRenderer(this, 60, 0);
      this.lid.setRotationPoint(-7.0F, -5.0F, 0.0F);
      this.lid.addBox(-1.0F, 0.0F, -8.0F, 16, 1, 16, 0.0F);
      this.lidTop = new ModelRenderer(this, 67, 35);
      this.lidTop.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.lidTop.addBox(1.0F, -2.0F, -8.0F, 12, 1, 14, 0.0F);
      this.lidMid = new ModelRenderer(this, 64, 18);
      this.lidMid.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.lidMid.addBox(0.0F, -1.0F, -8.0F, 14, 1, 15, 0.0F);
      this.lid.addChild(this.lidTop);
      this.lid.addChild(this.lidMid);
      this.sideEnd = new ModelRenderer(this, 33, 51);
      this.sideEnd.setRotationPoint(0.0F, -4.0F, 0.0F);
      this.sideEnd.addBox(-6.0F, 0.0F, 6.0F, 12, 7, 1, 0.0F);
      this.sideRight = new ModelRenderer(this, 0, 37);
      this.sideRight.setRotationPoint(0.0F, -4.0F, 0.0F);
      this.sideRight.addBox(-7.0F, 0.0F, -8.0F, 1, 7, 15, 0.0F);
      this.sideLeft = new ModelRenderer(this, 0, 37);
      this.sideLeft.mirror = true;
      this.sideLeft.setRotationPoint(0.0F, -4.0F, 0.0F);
      this.sideLeft.addBox(6.0F, 0.0F, -8.0F, 1, 7, 15, 0.0F);
      this.baseLower = new ModelRenderer(this, 0, 20);
      this.baseLower.setRotationPoint(0.0F, 8.0F, 0.0F);
      this.baseLower.addBox(-8.0F, 0.0F, -8.0F, 16, 1, 16, 0.0F);
      this.base = new ModelRenderer(this, 0, 0);
      this.base.setRotationPoint(0.0F, 3.0F, 0.0F);
      this.base.addBox(-7.0F, 0.0F, -8.0F, 14, 5, 15, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.sideLeft.render(f5);
      this.baseLower.render(f5);
      this.sideEnd.render(f5);
      this.base.render(f5);
      this.sideRight.render(f5);
      this.lid.render(f5);
   }

   public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }
}
