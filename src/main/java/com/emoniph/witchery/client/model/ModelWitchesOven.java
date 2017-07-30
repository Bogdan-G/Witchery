package com.emoniph.witchery.client.model;

import com.emoniph.witchery.blocks.BlockWitchesOven;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelWitchesOven extends ModelBase {

   ModelRenderer body;
   ModelRenderer lidBottom;
   ModelRenderer lidTop;
   ModelRenderer chimney;
   ModelRenderer chimneyTop;
   ModelRenderer legBackRight;
   ModelRenderer legFrontRight;
   ModelRenderer legBackLeft;
   ModelRenderer legFrontLeft;


   public ModelWitchesOven() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.setTextureOffset("legBackRight.legBackRightH", 0, 0);
      this.setTextureOffset("legBackRight.legBackRightV", 0, 2);
      this.setTextureOffset("legFrontRight.legFrontRightH", 0, 0);
      this.setTextureOffset("legFrontRight.legFrontRightV", 0, 2);
      this.setTextureOffset("legBackLeft.legBackLeftH", 0, 0);
      this.setTextureOffset("legBackLeft.legBackLeftV", 0, 2);
      this.setTextureOffset("legFrontLeft.legFrontLeftH", 0, 0);
      this.setTextureOffset("legFrontLeft.legFrontLeftV", 0, 2);
      this.body = new ModelRenderer(this, 0, 0);
      this.body.addBox(0.0F, 1.0F, 0.0F, 12, 8, 12);
      this.body.setRotationPoint(-6.0F, 14.0F, -6.0F);
      this.body.setTextureSize(64, 64);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.lidBottom = new ModelRenderer(this, 0, 20);
      this.lidBottom.addBox(0.0F, 0.0F, 0.0F, 14, 1, 14);
      this.lidBottom.setRotationPoint(-7.0F, 14.0F, -7.0F);
      this.lidBottom.setTextureSize(64, 64);
      this.lidBottom.mirror = true;
      this.setRotation(this.lidBottom, 0.0F, 0.0F, 0.0F);
      this.lidTop = new ModelRenderer(this, 8, 35);
      this.lidTop.addBox(0.0F, 0.0F, 0.0F, 10, 1, 10);
      this.lidTop.setRotationPoint(-5.0F, 13.0F, -5.0F);
      this.lidTop.setTextureSize(64, 64);
      this.lidTop.mirror = true;
      this.setRotation(this.lidTop, 0.0F, 0.0F, 0.0F);
      this.chimney = new ModelRenderer(this, 48, 0);
      this.chimney.addBox(0.0F, 0.0F, 0.0F, 4, 13, 4);
      this.chimney.setRotationPoint(-2.0F, 8.0F, 3.0F);
      this.chimney.setTextureSize(64, 64);
      this.chimney.mirror = true;
      this.setRotation(this.chimney, 0.0F, 0.0F, 0.0F);
      this.chimneyTop = new ModelRenderer(this, 38, 0);
      this.chimneyTop.addBox(0.0F, 0.0F, 0.0F, 4, 4, 1);
      this.chimneyTop.setRotationPoint(-2.0F, 8.0F, 7.0F);
      this.chimneyTop.setTextureSize(64, 64);
      this.chimneyTop.mirror = true;
      this.setRotation(this.chimneyTop, 0.0F, 0.0F, 0.0F);
      this.legBackRight = new ModelRenderer(this, "legBackRight");
      this.legBackRight.setRotationPoint(-5.0F, 21.0F, -7.0F);
      this.setRotation(this.legBackRight, 0.0F, 0.0F, 0.0F);
      this.legBackRight.mirror = true;
      this.legBackRight.addBox("legBackRightH", -2.0F, 0.0F, 0.0F, 2, 1, 1);
      this.legBackRight.addBox("legBackRightV", -3.0F, 0.0F, 0.0F, 1, 3, 1);
      this.legFrontRight = new ModelRenderer(this, "legFrontRight");
      this.legFrontRight.setRotationPoint(-5.0F, 21.0F, 6.0F);
      this.setRotation(this.legFrontRight, 0.0F, 0.0F, 0.0F);
      this.legFrontRight.mirror = true;
      this.legFrontRight.addBox("legFrontRightH", -2.0F, 0.0F, 0.0F, 2, 1, 1);
      this.legFrontRight.addBox("legFrontRightV", -3.0F, 0.0F, 0.0F, 1, 3, 1);
      this.legBackLeft = new ModelRenderer(this, "legBackLeft");
      this.legBackLeft.setRotationPoint(5.0F, 21.0F, -7.0F);
      this.setRotation(this.legBackLeft, 0.0F, 0.0F, 0.0F);
      this.legBackLeft.mirror = true;
      this.legBackLeft.addBox("legBackLeftH", 0.0F, 0.0F, 0.0F, 2, 1, 1);
      this.legBackLeft.addBox("legBackLeftV", 2.0F, 0.0F, 0.0F, 1, 3, 1);
      this.legFrontLeft = new ModelRenderer(this, "legFrontLeft");
      this.legFrontLeft.setRotationPoint(5.0F, 21.0F, 6.0F);
      this.setRotation(this.legFrontLeft, 0.0F, 0.0F, 0.0F);
      this.legFrontLeft.mirror = true;
      this.legFrontLeft.addBox("legFrontLeftH", 0.0F, 0.0F, 0.0F, 2, 1, 1);
      this.legFrontLeft.addBox("legFrontLeftV", 2.0F, 0.0F, 0.0F, 1, 3, 1);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, BlockWitchesOven.TileEntityWitchesOven te) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.body.render(f5);
      this.lidBottom.render(f5);
      this.lidTop.render(f5);
      this.chimney.render(f5);
      this.chimneyTop.render(f5);
      this.legBackRight.render(f5);
      this.legFrontRight.render(f5);
      this.legBackLeft.render(f5);
      this.legFrontLeft.render(f5);
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
