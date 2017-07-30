package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelWitchHunter extends ModelBiped {

   ModelRenderer coatBottom = new ModelRenderer(this, 1, 47);
   ModelRenderer hatBrim;
   ModelRenderer hatTop;


   public ModelWitchHunter() {
      super(0.0F, 0.0F, 64, 64);
      this.coatBottom.addBox(-5.0F, 0.0F, -2.5F, 10, 11, 5);
      this.coatBottom.setRotationPoint(0.0F, 11.0F, 0.0F);
      this.coatBottom.setTextureSize(64, 64);
      this.coatBottom.mirror = true;
      this.setRotation(this.coatBottom, 0.0F, 0.0F, 0.0F);
      super.bipedBody.addChild(this.coatBottom);
      this.hatBrim = new ModelRenderer(this, 0, 32);
      this.hatBrim.addBox(-7.0F, 0.0F, -7.0F, 14, 1, 14);
      this.hatBrim.setRotationPoint(0.0F, -6.0F, 0.0F);
      this.hatBrim.setTextureSize(64, 64);
      this.hatBrim.mirror = true;
      this.setRotation(this.hatBrim, 0.0F, 0.0F, 0.0F);
      super.bipedHead.addChild(this.hatBrim);
      this.hatTop = new ModelRenderer(this, 33, 48);
      this.hatTop.addBox(-3.0F, 0.0F, -3.0F, 6, 2, 6);
      this.hatTop.setRotationPoint(0.0F, -10.0F, 0.0F);
      this.hatTop.setTextureSize(64, 64);
      this.hatTop.mirror = true;
      this.setRotation(this.hatTop, 0.0F, 0.0F, 0.0F);
      super.bipedHead.addChild(this.hatTop);
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

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }
}
