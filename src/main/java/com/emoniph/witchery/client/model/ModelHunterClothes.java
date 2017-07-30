package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelHunterClothes extends ModelBiped {

   ModelRenderer coat;
   ModelRenderer hatBrim;
   ModelRenderer hatTop;
   ModelRenderer hatMid;


   public ModelHunterClothes(float scale, boolean shoulders) {
      super(scale, 0.0F, 128, 64);
      float hatScale = 0.52F;
      this.hatBrim = new ModelRenderer(this, 0, 50);
      this.hatBrim.addBox(-6.5F, 0.0F, -6.5F, 13, 1, 13, hatScale - 0.2F);
      this.hatBrim.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.hatBrim.setTextureSize(128, 64);
      this.hatBrim.mirror = true;
      this.setRotation(this.hatBrim, 0.0F, 0.0F, 0.0F);
      super.bipedHead.addChild(this.hatBrim);
      this.hatMid = new ModelRenderer(this, 40, 52);
      this.hatMid.addBox(-4.0F, 0.0F, -4.0F, 8, 2, 8, hatScale);
      this.hatMid.setRotationPoint(0.0F, -2.0F, 0.0F);
      this.hatMid.setTextureSize(128, 64);
      this.hatMid.mirror = true;
      this.setRotation(this.hatMid, 0.0F, 0.0F, 0.0F);
      this.hatBrim.addChild(this.hatMid);
      this.hatTop = new ModelRenderer(this, 12, 41);
      this.hatTop.addBox(-3.5F, 0.0F, -3.5F, 7, 2, 7, hatScale);
      this.hatTop.setRotationPoint(0.0F, -2.0F, 0.0F);
      this.hatTop.setTextureSize(128, 64);
      this.hatTop.mirror = true;
      this.setRotation(this.hatTop, 0.0F, 0.0F, 0.0F);
      this.hatMid.addChild(this.hatTop);
      this.coat = new ModelRenderer(this, 41, 33);
      this.coat.addBox(-5.5F, 0.0F, -3.0F, 11, 10, 6, -0.3F);
      this.coat.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.coat.setTextureSize(128, 64);
      this.coat.mirror = true;
      this.setRotation(this.coat, 0.0F, 0.0F, 0.0F);
      super.bipedBody.addChild(this.coat);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
