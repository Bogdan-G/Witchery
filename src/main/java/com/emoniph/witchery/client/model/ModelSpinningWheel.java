package com.emoniph.witchery.client.model;

import com.emoniph.witchery.blocks.BlockSpinningWheel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelSpinningWheel extends ModelBase {

   private ModelRenderer seat;
   private ModelRenderer legBackRight;
   private ModelRenderer legBackLeft;
   private ModelRenderer legFrontRight;
   private ModelRenderer legFrontLeft;
   private ModelRenderer thread;
   private ModelRenderer threadPole;
   private ModelRenderer armRight;
   private ModelRenderer armLeft;
   private ModelRenderer wheel;


   public ModelSpinningWheel() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.setTextureOffset("wheel.spokes", 0, -6);
      this.setTextureOffset("wheel.top", 0, 7);
      this.setTextureOffset("wheel.bottom", 0, 7);
      this.setTextureOffset("wheel.back", 23, 5);
      this.setTextureOffset("wheel.front", 23, 5);
      this.seat = new ModelRenderer(this, 0, 0);
      this.seat.addBox(-2.0F, -1.0F, -7.0F, 4, 1, 14);
      this.seat.setRotationPoint(0.0F, 18.0F, 0.0F);
      this.seat.setTextureSize(64, 64);
      this.seat.mirror = true;
      this.setRotation(this.seat, 0.2602503F, 0.0F, 0.0F);
      this.legBackRight = new ModelRenderer(this, 32, 0);
      this.legBackRight.addBox(-1.0F, 0.0F, 0.0F, 1, 9, 1);
      this.legBackRight.setRotationPoint(-1.0F, 16.0F, 5.0F);
      this.legBackRight.setTextureSize(64, 64);
      this.legBackRight.mirror = true;
      this.setRotation(this.legBackRight, 0.1745329F, 0.0F, 0.1745329F);
      this.legBackLeft = new ModelRenderer(this, 32, 0);
      this.legBackLeft.addBox(0.0F, 0.0F, 0.0F, 1, 9, 1);
      this.legBackLeft.setRotationPoint(1.0F, 16.0F, 5.0F);
      this.legBackLeft.setTextureSize(64, 64);
      this.legBackLeft.mirror = true;
      this.setRotation(this.legBackLeft, 0.1745329F, 0.0F, -0.1745329F);
      this.legFrontRight = new ModelRenderer(this, 0, 6);
      this.legFrontRight.addBox(-1.0F, 0.0F, 0.0F, 1, 6, 1);
      this.legFrontRight.setRotationPoint(-1.0F, 19.0F, -6.0F);
      this.legFrontRight.setTextureSize(64, 64);
      this.legFrontRight.mirror = true;
      this.setRotation(this.legFrontRight, -0.1745329F, 0.0F, 0.1745329F);
      this.legFrontLeft = new ModelRenderer(this, 0, 6);
      this.legFrontLeft.addBox(0.0F, 0.0F, 0.0F, 1, 6, 1);
      this.legFrontLeft.setRotationPoint(1.0F, 19.0F, -6.0F);
      this.legFrontLeft.setTextureSize(64, 64);
      this.legFrontLeft.mirror = true;
      this.setRotation(this.legFrontLeft, -0.1745329F, 0.0F, -0.1745329F);
      this.thread = new ModelRenderer(this, 23, 0);
      this.thread.addBox(-1.0F, -3.0F, -1.0F, 2, 3, 2);
      this.thread.setRotationPoint(0.0F, 12.0F, 5.0F);
      this.thread.setTextureSize(64, 64);
      this.thread.mirror = true;
      this.setRotation(this.thread, 0.0F, 0.0F, 0.0F);
      this.threadPole = new ModelRenderer(this, 9, 7);
      this.threadPole.addBox(-0.5F, 0.0F, -0.5F, 1, 4, 1);
      this.threadPole.setRotationPoint(0.0F, 12.0F, 5.0F);
      this.threadPole.setTextureSize(64, 64);
      this.threadPole.mirror = true;
      this.setRotation(this.threadPole, 0.0F, 0.0F, 0.0F);
      this.armRight = new ModelRenderer(this, 28, 6);
      this.armRight.addBox(-0.5F, -7.0F, -0.5F, 1, 7, 1);
      this.armRight.setRotationPoint(-1.0F, 18.0F, -2.0F);
      this.armRight.setTextureSize(64, 64);
      this.armRight.mirror = true;
      this.setRotation(this.armRight, 0.2268928F, 0.0F, 0.0F);
      this.armLeft = new ModelRenderer(this, 28, 6);
      this.armLeft.addBox(-0.5F, -7.0F, -0.5F, 1, 7, 1);
      this.armLeft.setRotationPoint(1.0F, 18.0F, -2.0F);
      this.armLeft.setTextureSize(64, 64);
      this.armLeft.mirror = true;
      this.setRotation(this.armLeft, 0.2268928F, 0.0F, 0.0F);
      this.wheel = new ModelRenderer(this, "wheel");
      this.wheel.setRotationPoint(0.0F, 12.0F, -3.5F);
      this.setRotation(this.wheel, 0.0F, 0.0F, 0.0F);
      this.wheel.mirror = true;
      this.wheel.addBox("spokes", 0.0F, -3.0F, -3.0F, 0, 6, 6);
      this.wheel.addBox("top", -0.5F, -4.0F, -3.0F, 1, 1, 6);
      this.wheel.addBox("bottom", -0.5F, 3.0F, -3.0F, 1, 1, 6);
      this.wheel.addBox("back", -0.5F, -4.0F, 3.0F, 1, 8, 1);
      this.wheel.addBox("front", -0.5F, -4.0F, -4.0F, 1, 8, 1);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.render(entity, f, f1, f2, f3, f4, f5, (BlockSpinningWheel.TileEntitySpinningWheel)null);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, BlockSpinningWheel.TileEntitySpinningWheel spinningWheel) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity, spinningWheel);
      this.seat.render(f5);
      this.legBackRight.render(f5);
      this.legBackLeft.render(f5);
      this.legFrontRight.render(f5);
      this.legFrontLeft.render(f5);
      this.thread.render(f5);
      this.threadPole.render(f5);
      this.armRight.render(f5);
      this.armLeft.render(f5);
      this.wheel.render(f5);
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity, (BlockSpinningWheel.TileEntitySpinningWheel)null);
   }

   private void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity, BlockSpinningWheel.TileEntitySpinningWheel spinningWheel) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      if(spinningWheel != null) {
         boolean spinning = spinningWheel.getCookTime() > 0 && spinningWheel.getCookTime() < spinningWheel.getTotalCookTime() && spinningWheel.powerLevel > 0;
         Minecraft.getMinecraft();
         long ticks = Minecraft.getSystemTime() / 25L;
         this.wheel.rotateAngleX = spinning?(float)(-(ticks / 3L) % 360L):0.0F;
         this.thread.rotateAngleY = spinning?(float)(ticks / 2L % 360L):0.0F;
      }

   }
}
