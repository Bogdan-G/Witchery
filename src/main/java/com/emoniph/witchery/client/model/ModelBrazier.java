package com.emoniph.witchery.client.model;

import com.emoniph.witchery.blocks.BlockBrazier;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelBrazier extends ModelBase {

   ModelRenderer leg1;
   ModelRenderer leg2;
   ModelRenderer leg3;
   ModelRenderer leg4;
   ModelRenderer foot3;
   ModelRenderer foot2;
   ModelRenderer foot1;
   ModelRenderer foot4;
   ModelRenderer ash;
   ModelRenderer panSide1;
   ModelRenderer panSide2;
   ModelRenderer panSide3;
   ModelRenderer panSide4;
   ModelRenderer footBase;
   ModelRenderer panBase;


   public ModelBrazier() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.leg1 = new ModelRenderer(this, 0, 0);
      this.leg1.addBox(-0.5F, 0.0F, -0.5F, 1, 11, 1);
      this.leg1.setRotationPoint(0.7F, 10.0F, -0.74F);
      this.leg1.setTextureSize(64, 64);
      this.leg1.mirror = true;
      this.setRotation(this.leg1, 0.0F, 0.0F, 0.0F);
      this.leg2 = new ModelRenderer(this, 0, 0);
      this.leg2.addBox(-0.5F, 0.0F, -0.5F, 1, 11, 1);
      this.leg2.setRotationPoint(-0.7F, 10.0F, -0.7F);
      this.leg2.setTextureSize(64, 64);
      this.leg2.mirror = true;
      this.setRotation(this.leg2, 0.0F, 0.0F, 0.0F);
      this.leg3 = new ModelRenderer(this, 0, 0);
      this.leg3.addBox(-0.5F, 0.0F, -0.5F, 1, 11, 1);
      this.leg3.setRotationPoint(-0.7F, 10.0F, 0.7F);
      this.leg3.setTextureSize(64, 64);
      this.leg3.mirror = true;
      this.setRotation(this.leg3, 0.0F, 0.0F, 0.0F);
      this.leg4 = new ModelRenderer(this, 0, 0);
      this.leg4.addBox(-0.5F, 0.0F, -0.5F, 1, 11, 1);
      this.leg4.setRotationPoint(0.7F, 10.0F, 0.7F);
      this.leg4.setTextureSize(64, 64);
      this.leg4.mirror = true;
      this.setRotation(this.leg4, 0.0F, 0.0F, 0.0F);
      this.foot3 = new ModelRenderer(this, 0, 13);
      this.foot3.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1);
      this.foot3.setRotationPoint(-0.7F, 21.0F, 0.7F);
      this.foot3.setTextureSize(64, 64);
      this.foot3.mirror = true;
      this.setRotation(this.foot3, 0.7853982F, 0.0F, 0.7853982F);
      this.foot2 = new ModelRenderer(this, 0, 13);
      this.foot2.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1);
      this.foot2.setRotationPoint(-0.7F, 21.0F, -0.7F);
      this.foot2.setTextureSize(64, 64);
      this.foot2.mirror = true;
      this.setRotation(this.foot2, -0.7853982F, 0.0F, 0.7853982F);
      this.foot1 = new ModelRenderer(this, 0, 13);
      this.foot1.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1);
      this.foot1.setRotationPoint(0.7F, 21.0F, -0.7F);
      this.foot1.setTextureSize(64, 64);
      this.foot1.mirror = true;
      this.setRotation(this.foot1, -0.7853982F, 0.0F, -0.7853982F);
      this.foot4 = new ModelRenderer(this, 0, 13);
      this.foot4.addBox(-0.5F, 0.0F, -0.5F, 1, 5, 1);
      this.foot4.setRotationPoint(0.7F, 21.0F, 0.7F);
      this.foot4.setTextureSize(64, 64);
      this.foot4.mirror = true;
      this.setRotation(this.foot4, 0.7853982F, 0.0F, -0.7853982F);
      this.ash = new ModelRenderer(this, 0, 20);
      this.ash.addBox(-2.5F, 0.0F, -2.5F, 5, 0, 5);
      this.ash.setRotationPoint(0.0F, 9.7F, 0.0F);
      this.ash.setTextureSize(64, 64);
      this.ash.mirror = true;
      this.setRotation(this.ash, 0.0F, 0.0F, 0.0F);
      this.panSide1 = new ModelRenderer(this, 5, 12);
      this.panSide1.addBox(-0.5F, -0.5F, -3.0F, 1, 1, 6);
      this.panSide1.setRotationPoint(3.0F, 9.5F, 0.0F);
      this.panSide1.setTextureSize(64, 64);
      this.panSide1.mirror = true;
      this.setRotation(this.panSide1, 0.0F, 0.0F, 0.0F);
      this.panSide2 = new ModelRenderer(this, 4, 26);
      this.panSide2.addBox(-3.0F, -0.5F, -0.5F, 6, 1, 1);
      this.panSide2.setRotationPoint(0.0F, 9.5F, 3.0F);
      this.panSide2.setTextureSize(64, 64);
      this.panSide2.mirror = true;
      this.setRotation(this.panSide2, 0.0F, 0.0F, 0.0F);
      this.panSide4 = new ModelRenderer(this, 4, 26);
      this.panSide4.addBox(-3.0F, -0.5F, -0.5F, 6, 1, 1);
      this.panSide4.setRotationPoint(0.0F, 9.5F, -3.0F);
      this.panSide4.setTextureSize(64, 64);
      this.panSide4.mirror = true;
      this.setRotation(this.panSide4, 0.0F, 0.0F, 0.0F);
      this.panSide3 = new ModelRenderer(this, 5, 12);
      this.panSide3.addBox(-0.5F, -0.5F, -3.0F, 1, 1, 6);
      this.panSide3.setRotationPoint(-3.0F, 9.5F, 0.0F);
      this.panSide3.setTextureSize(64, 64);
      this.panSide3.mirror = true;
      this.setRotation(this.panSide3, 0.0F, 0.0F, 0.0F);
      this.footBase = new ModelRenderer(this, 6, 0);
      this.footBase.addBox(-1.5F, -0.5F, -1.5F, 3, 1, 3);
      this.footBase.setRotationPoint(0.0F, 21.0F, 0.0F);
      this.footBase.setTextureSize(64, 64);
      this.footBase.mirror = true;
      this.setRotation(this.footBase, 0.0F, 0.0F, 0.0F);
      this.panBase = new ModelRenderer(this, 6, 5);
      this.panBase.addBox(-3.0F, 0.0F, -3.0F, 6, 1, 6);
      this.panBase.setRotationPoint(0.0F, 9.95F, 0.0F);
      this.panBase.setTextureSize(64, 64);
      this.panBase.mirror = true;
      this.setRotation(this.panBase, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, BlockBrazier.TileEntityBrazier tile) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.leg1.render(f5);
      this.leg2.render(f5);
      this.leg3.render(f5);
      this.leg4.render(f5);
      this.foot3.render(f5);
      this.foot2.render(f5);
      this.foot1.render(f5);
      this.foot4.render(f5);
      this.panSide1.render(f5);
      this.panSide2.render(f5);
      this.panSide3.render(f5);
      this.panSide4.render(f5);
      this.footBase.render(f5);
      this.panBase.render(f5);
      this.panSide4.rotateAngleX = 0.0F;
      this.panSide2.rotateAngleX = 0.0F;
      this.panSide1.rotateAngleZ = 0.0F;
      this.panSide3.rotateAngleZ = 0.0F;
      if(tile != null) {
         int ingredientCount = tile.getIngredientCount();
         if(ingredientCount > 0) {
            this.ash.rotationPointY = 9.7F - (float)(ingredientCount - 1) * 0.1F;
            this.ash.render(f5);
         }
      }

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
