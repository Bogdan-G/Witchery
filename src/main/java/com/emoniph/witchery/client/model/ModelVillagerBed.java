package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelVillagerBed extends ModelBase {

   public ModelRenderer base;
   public ModelRenderer head;
   public ModelRenderer legFL;
   public ModelRenderer legFR;
   public ModelRenderer legBL;
   public ModelRenderer legBR;


   public ModelVillagerBed() {
      super.textureWidth = 128;
      super.textureHeight = 32;
      this.legBR = new ModelRenderer(this, 0, 0);
      this.legBR.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.legBR.addBox(-5.0F, 1.0F, 14.0F, 1, 3, 1, 0.0F);
      this.base = new ModelRenderer(this, 0, 0);
      this.base.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.base.addBox(-5.0F, 0.0F, -15.0F, 10, 1, 30, 0.0F);
      this.head = new ModelRenderer(this, 0, 4);
      this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.head.addBox(-5.0F, -4.0F, 14.0F, 10, 4, 1, 0.0F);
      this.legFL = new ModelRenderer(this, 0, 0);
      this.legFL.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.legFL.addBox(4.0F, 1.0F, -15.0F, 1, 3, 1, 0.0F);
      this.legBL = new ModelRenderer(this, 0, 0);
      this.legBL.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.legBL.addBox(4.0F, 1.0F, 14.0F, 1, 3, 1, 0.0F);
      this.legFR = new ModelRenderer(this, 0, 0);
      this.legFR.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.legFR.addBox(-5.0F, 1.0F, -15.0F, 1, 3, 1, 0.0F);
      this.base.addChild(this.legBR);
      this.base.addChild(this.head);
      this.base.addChild(this.legFL);
      this.base.addChild(this.legBL);
      this.base.addChild(this.legFR);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.base.render(f5);
   }

   public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }
}
