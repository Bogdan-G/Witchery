package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelGoblinClothes extends ModelBiped {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer rightarm;
   ModelRenderer leftarm;
   ModelRenderer rightleg;
   ModelRenderer leftleg;
   ModelRenderer quiver;
   ModelRenderer arrow1;
   ModelRenderer feathers;
   ModelRenderer arrow2;


   public ModelGoblinClothes(float scale) {
      super(scale, 0.0F, 64, 32);
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.quiver = new ModelRenderer(this, 33, 0);
      this.quiver.addBox(-2.0F, -3.0F, 0.0F, 4, 7, 1);
      this.quiver.setRotationPoint(0.0F, 7.0F, 2.0F);
      this.quiver.setTextureSize(64, 32);
      this.quiver.mirror = true;
      this.setRotation(this.quiver, 0.0F, 0.0F, -0.3490659F);
      this.arrow1 = new ModelRenderer(this, 44, 4);
      this.arrow1.addBox(0.5F, -5.0F, 0.0F, 1, 2, 1);
      this.arrow1.setRotationPoint(0.0F, 7.0F, 2.0F);
      this.arrow1.setTextureSize(64, 32);
      this.arrow1.mirror = true;
      this.setRotation(this.arrow1, 0.0F, 0.0F, -0.3490659F);
      this.feathers = new ModelRenderer(this, 44, 0);
      this.feathers.addBox(-2.0F, -7.0F, 0.0F, 4, 2, 1);
      this.feathers.setRotationPoint(0.0F, 7.0F, 2.0F);
      this.feathers.setTextureSize(64, 32);
      this.feathers.mirror = true;
      this.setRotation(this.feathers, 0.0F, 0.0F, -0.3490659F);
      this.arrow2 = new ModelRenderer(this, 44, 4);
      this.arrow2.addBox(-1.5F, -5.0F, 0.0F, 1, 2, 1);
      this.arrow2.setRotationPoint(0.0F, 7.0F, 2.0F);
      this.arrow2.setTextureSize(64, 32);
      this.arrow2.mirror = true;
      this.setRotation(this.arrow2, 0.0F, 0.0F, -0.3490659F);
      super.bipedBody.addChild(this.quiver);
      super.bipedBody.addChild(this.arrow1);
      super.bipedBody.addChild(this.arrow2);
      super.bipedBody.addChild(this.feathers);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }
}
