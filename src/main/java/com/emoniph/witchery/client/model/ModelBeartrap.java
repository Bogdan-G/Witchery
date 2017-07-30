package com.emoniph.witchery.client.model;

import com.emoniph.witchery.blocks.BlockBeartrap;
import com.emoniph.witchery.util.Config;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelBeartrap extends ModelBase {

   public ModelRenderer plate;
   public ModelRenderer base;
   public ModelRenderer armFront;
   public ModelRenderer armBack;
   public ModelRenderer diskLeft;
   public ModelRenderer diskRight;
   public ModelRenderer armRightFront;
   public ModelRenderer armLeftFront;
   public ModelRenderer armTooth1Front;
   public ModelRenderer armTooth2Front;
   public ModelRenderer armTooth3Front;
   public ModelRenderer armTooth4Front;
   public ModelRenderer armTooth5Front;
   public ModelRenderer armRightBack;
   public ModelRenderer armLeftBack;
   public ModelRenderer armTooth1Back;
   public ModelRenderer armTooth2Back;
   public ModelRenderer armTooth3Back;
   public ModelRenderer armTooth4Back;
   public ModelRenderer armTooth5Back;


   public ModelBeartrap() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.armTooth4Back = new ModelRenderer(this, 0, 0);
      this.armTooth4Back.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armTooth4Back.addBox(1.5F, -2.0F, 6.0F, 1, 1, 1, 0.0F);
      this.plate = new ModelRenderer(this, 1, 0);
      this.plate.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.plate.addBox(-2.0F, -1.5F, -2.0F, 4, 1, 4, 0.0F);
      this.armFront = new ModelRenderer(this, 0, 9);
      this.armFront.setRotationPoint(0.0F, 23.99F, 0.0F);
      this.armFront.addBox(-4.5F, -1.0F, -7.0F, 9, 1, 1, 0.0F);
      this.armTooth2Front = new ModelRenderer(this, 0, 0);
      this.armTooth2Front.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armTooth2Front.addBox(-2.5F, -2.0F, -7.0F, 1, 1, 1, 0.0F);
      this.armLeftFront = new ModelRenderer(this, 0, 12);
      this.armLeftFront.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armLeftFront.addBox(3.5F, -1.0F, -6.0F, 1, 1, 6, 0.0F);
      this.armTooth4Front = new ModelRenderer(this, 0, 0);
      this.armTooth4Front.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armTooth4Front.addBox(1.5F, -2.0F, -7.0F, 1, 1, 1, 0.0F);
      this.armLeftBack = new ModelRenderer(this, 0, 12);
      this.armLeftBack.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armLeftBack.addBox(3.5F, -1.0F, 0.0F, 1, 1, 6, 0.0F);
      this.armTooth3Front = new ModelRenderer(this, 0, 0);
      this.armTooth3Front.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armTooth3Front.addBox(-0.5F, -2.0F, -7.0F, 1, 1, 1, 0.0F);
      this.armTooth3Back = new ModelRenderer(this, 0, 0);
      this.armTooth3Back.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armTooth3Back.addBox(-0.5F, -2.0F, 6.0F, 1, 1, 1, 0.0F);
      this.armTooth5Front = new ModelRenderer(this, 0, 0);
      this.armTooth5Front.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armTooth5Front.addBox(3.5F, -2.0F, -7.0F, 1, 1, 1, 0.0F);
      this.base = new ModelRenderer(this, 0, 20);
      this.base.setRotationPoint(0.0F, 23.99F, 0.0F);
      this.base.addBox(-5.0F, -1.0F, -0.5F, 10, 1, 1, 0.0F);
      this.armBack = new ModelRenderer(this, 0, 9);
      this.armBack.setRotationPoint(0.0F, 23.99F, 0.0F);
      this.armBack.addBox(-4.5F, -1.0F, 6.0F, 9, 1, 1, 0.0F);
      this.diskLeft = new ModelRenderer(this, 19, 3);
      this.diskLeft.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.diskLeft.addBox(3.7F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
      this.armTooth2Back = new ModelRenderer(this, 0, 0);
      this.armTooth2Back.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armTooth2Back.addBox(-2.5F, -2.0F, 6.0F, 1, 1, 1, 0.0F);
      this.armTooth1Back = new ModelRenderer(this, 0, 0);
      this.armTooth1Back.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armTooth1Back.addBox(-4.5F, -2.0F, 6.0F, 1, 1, 1, 0.0F);
      this.armTooth5Back = new ModelRenderer(this, 0, 0);
      this.armTooth5Back.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armTooth5Back.addBox(3.5F, -2.0F, 6.0F, 1, 1, 1, 0.0F);
      this.armRightBack = new ModelRenderer(this, 0, 12);
      this.armRightBack.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armRightBack.addBox(-4.5F, -1.0F, 0.0F, 1, 1, 6, 0.0F);
      this.armTooth1Front = new ModelRenderer(this, 0, 0);
      this.armTooth1Front.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armTooth1Front.addBox(-4.5F, -2.0F, -7.0F, 1, 1, 1, 0.0F);
      this.armRightFront = new ModelRenderer(this, 0, 12);
      this.armRightFront.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armRightFront.addBox(-4.5F, -1.0F, -6.0F, 1, 1, 6, 0.0F);
      this.diskRight = new ModelRenderer(this, 19, 3);
      this.diskRight.setRotationPoint(0.0F, 24.0F, 0.0F);
      this.diskRight.addBox(-4.7F, -2.0F, -1.0F, 1, 2, 2, 0.0F);
      this.armBack.addChild(this.armTooth4Back);
      this.armFront.addChild(this.armTooth2Front);
      this.armFront.addChild(this.armLeftFront);
      this.armFront.addChild(this.armTooth4Front);
      this.armBack.addChild(this.armLeftBack);
      this.armFront.addChild(this.armTooth3Front);
      this.armBack.addChild(this.armTooth3Back);
      this.armFront.addChild(this.armTooth5Front);
      this.armBack.addChild(this.armTooth2Back);
      this.armBack.addChild(this.armTooth1Back);
      this.armBack.addChild(this.armTooth5Back);
      this.armBack.addChild(this.armRightBack);
      this.armFront.addChild(this.armTooth1Front);
      this.armFront.addChild(this.armRightFront);
   }

   private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, BlockBeartrap.TileEntityBeartrap tile) {
      boolean inWorld = tile != null && tile.getWorldObj() != null;
      if(inWorld && !tile.isVisibleTo(Minecraft.getMinecraft().thePlayer)) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, Config.instance().mantrapAlpha);
      }

      this.base.render(f5);
      this.diskLeft.render(f5);
      this.diskRight.render(f5);
      if(inWorld && tile.isSprung()) {
         this.plate.rotationPointY = 23.8F;
      } else {
         this.plate.rotationPointY = 23.2F;
      }

      this.plate.render(f5);
      if(inWorld && tile.isSprung()) {
         this.armFront.rotateAngleX = -1.2F;
      } else {
         this.armFront.rotateAngleX = 0.0F;
      }

      this.armFront.render(f5);
      if(inWorld && tile.isSprung()) {
         this.armBack.rotateAngleX = 1.2F;
      } else {
         this.armBack.rotateAngleX = 0.0F;
      }

      this.armBack.render(f5);
   }
}
