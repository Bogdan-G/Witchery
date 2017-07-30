package com.emoniph.witchery.client.model;

import com.emoniph.witchery.blocks.BlockFetish;
import com.emoniph.witchery.client.model.ModelBroom;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelFetishScarecrow extends ModelBase {

   ModelRenderer poleVertical;
   ModelRenderer poleHorizontal;
   ModelRenderer head;
   ModelRenderer headInner;
   ModelRenderer body;
   ModelRenderer armLeft;
   ModelRenderer armRight;
   ModelRenderer armLeftInner;
   ModelRenderer armRightInner;


   public ModelFetishScarecrow() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.poleVertical = new ModelRenderer(this, 0, 2);
      this.poleVertical.addBox(-1.0F, 0.0F, -1.0F, 2, 15, 2);
      this.poleVertical.setRotationPoint(0.0F, 9.0F, 0.0F);
      this.poleVertical.setTextureSize(64, 64);
      this.poleVertical.mirror = true;
      this.setRotation(this.poleVertical, 0.0F, 0.0F, 0.0F);
      this.poleHorizontal = new ModelRenderer(this, 0, 0);
      this.poleHorizontal.addBox(-8.0F, 0.0F, -0.5F, 16, 1, 1);
      this.poleHorizontal.setRotationPoint(0.0F, 13.0F, 0.0F);
      this.poleHorizontal.setTextureSize(64, 64);
      this.poleHorizontal.mirror = true;
      this.setRotation(this.poleHorizontal, 0.0F, 0.0F, 0.0F);
      this.head = new ModelRenderer(this, 12, 21);
      this.head.addBox(-2.0F, -4.0F, -2.0F, 4, 5, 4);
      this.head.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.head.setTextureSize(64, 64);
      this.head.mirror = true;
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.headInner = new ModelRenderer(this, 29, 25);
      this.headInner.addBox(-2.0F, -4.0F, -1.9F, 4, 5, 0);
      this.headInner.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.headInner.setTextureSize(64, 64);
      this.headInner.mirror = true;
      this.setRotation(this.headInner, 0.0F, 0.0F, 0.0F);
      this.body = new ModelRenderer(this, 8, 2);
      this.body.addBox(-3.0F, 0.0F, -1.5F, 6, 9, 3);
      this.body.setRotationPoint(0.0F, 12.5F, 0.0F);
      this.body.setTextureSize(64, 64);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.armLeft = new ModelRenderer(this, 0, 23);
      this.armLeft.addBox(0.0F, -0.5F, -1.5F, 3, 4, 3);
      this.armLeft.setRotationPoint(3.0F, 13.0F, 0.0F);
      this.armLeft.setTextureSize(64, 64);
      this.armLeft.mirror = true;
      this.setRotation(this.armLeft, 0.0F, 0.0F, 0.0F);
      this.armLeftInner = new ModelRenderer(this, 29, 25);
      this.armLeftInner.addBox(2.9F, -0.5F, -1.5F, 0, 4, 3);
      this.armLeftInner.setRotationPoint(3.0F, 13.0F, 0.0F);
      this.armLeftInner.setTextureSize(64, 64);
      this.armLeftInner.mirror = true;
      this.setRotation(this.armLeftInner, 0.0F, 0.0F, 0.0F);
      this.armRight = new ModelRenderer(this, 0, 23);
      this.armRight.addBox(-3.0F, -0.5F, -1.5F, 3, 4, 3);
      this.armRight.setRotationPoint(-3.0F, 13.0F, 0.0F);
      this.armRight.setTextureSize(64, 64);
      this.armRight.mirror = true;
      this.setRotation(this.armRight, 0.0F, 0.0F, 0.0F);
      this.armRightInner = new ModelRenderer(this, 29, 25);
      this.armRightInner.addBox(-2.9F, -0.5F, -1.5F, 0, 4, 3);
      this.armRightInner.setRotationPoint(-3.0F, 13.0F, 0.0F);
      this.armRightInner.setTextureSize(64, 64);
      this.armRightInner.mirror = true;
      this.setRotation(this.armRightInner, 0.0F, 0.0F, 0.0F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, BlockFetish.TileEntityFetish tile) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.poleVertical.render(f5);
      this.poleHorizontal.render(f5);
      this.headInner.render(f5);
      this.armLeftInner.render(f5);
      this.armRightInner.render(f5);
      int colorIndex = 9;
      float alpha = 1.0F;
      if(tile != null) {
         int color = tile.getColor();
         if(color >= 0 && color <= 15) {
            colorIndex = color;
         }

         if(tile.isSpectral()) {
            alpha = 0.7F;
         }
      }

      GL11.glColor4f(ModelBroom.fleeceColorTable[colorIndex][0], ModelBroom.fleeceColorTable[colorIndex][1], ModelBroom.fleeceColorTable[colorIndex][2], alpha);
      this.head.render(f5);
      this.body.render(f5);
      this.armLeft.render(f5);
      this.armRight.render(f5);
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }
}
