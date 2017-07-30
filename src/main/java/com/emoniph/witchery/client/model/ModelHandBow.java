package com.emoniph.witchery.client.model;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemGeneral;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelHandBow extends ModelBase {

   ModelRenderer stockTop;
   ModelRenderer stockBottom;
   ModelRenderer stockCatch;
   ModelRenderer grip;
   ModelRenderer cross;
   ModelRenderer drawnCrossOuterR;
   ModelRenderer drawnCrossInnerR;
   ModelRenderer drawnCrossOuterL;
   ModelRenderer drawnCrossInnerL;
   ModelRenderer drawnStringInnerR;
   ModelRenderer drawnStringMidR;
   ModelRenderer drawnStringOuterR;
   ModelRenderer drawnStringInnerL;
   ModelRenderer drawnStringMidL;
   ModelRenderer drawnStringOuterL;
   ModelRenderer drawnStringCenter;
   ModelRenderer boltStake;
   ModelRenderer boltDraining;
   ModelRenderer boltHoly;
   ModelRenderer boltSplitting;
   ModelRenderer boltSplitting2;
   ModelRenderer boltSilver;


   public ModelHandBow() {
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.stockTop = new ModelRenderer(this, 2, 2);
      this.stockTop.addBox(-1.0F, 0.0F, -5.0F, 2, 1, 7);
      this.stockTop.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.stockTop.setTextureSize(64, 32);
      this.stockTop.mirror = true;
      this.setRotation(this.stockTop, 0.0F, 0.0F, 0.0F);
      this.stockBottom = new ModelRenderer(this, 0, 10);
      this.stockBottom.addBox(-0.5F, 0.0F, -6.0F, 1, 1, 8);
      this.stockBottom.setRotationPoint(0.0F, 1.0F, 0.0F);
      this.stockBottom.setTextureSize(64, 32);
      this.stockBottom.mirror = true;
      this.setRotation(this.stockBottom, 0.0F, 0.0F, 0.0F);
      this.stockCatch = new ModelRenderer(this, 1, 11);
      this.stockCatch.addBox(-0.5F, 0.0F, 0.0F, 1, 1, 1);
      this.stockCatch.setRotationPoint(0.0F, -1.0F, 1.0F);
      this.stockCatch.setTextureSize(64, 32);
      this.stockCatch.mirror = true;
      this.setRotation(this.stockCatch, 0.0F, 0.0F, 0.0F);
      this.grip = new ModelRenderer(this, 0, 3);
      this.grip.addBox(-0.5F, 0.0F, -1.0F, 1, 3, 2);
      this.grip.setRotationPoint(0.0F, 2.0F, 0.0F);
      this.grip.setTextureSize(64, 32);
      this.grip.mirror = true;
      this.setRotation(this.grip, 0.0F, 0.0F, 0.0F);
      this.cross = new ModelRenderer(this, 1, 19);
      this.cross.addBox(-3.0F, 0.0F, 0.0F, 6, 1, 2);
      this.cross.setRotationPoint(0.0F, 0.0F, -7.0F);
      this.cross.setTextureSize(64, 32);
      this.cross.mirror = true;
      this.setRotation(this.cross, 0.0F, 0.0F, 0.0F);
      this.drawnCrossOuterR = new ModelRenderer(this, 0, 14);
      this.drawnCrossOuterR.addBox(-1.0F, 0.0F, 0.0F, 1, 1, 2);
      this.drawnCrossOuterR.setRotationPoint(-4.0F, 0.0F, -4.0F);
      this.drawnCrossOuterR.setTextureSize(64, 32);
      this.drawnCrossOuterR.mirror = true;
      this.setRotation(this.drawnCrossOuterR, 0.0F, 0.0F, 0.0F);
      this.drawnCrossInnerR = new ModelRenderer(this, 0, 14);
      this.drawnCrossInnerR.addBox(-1.0F, 0.0F, 0.0F, 1, 1, 2);
      this.drawnCrossInnerR.setRotationPoint(-3.0F, 0.0F, -6.0F);
      this.drawnCrossInnerR.setTextureSize(64, 32);
      this.drawnCrossInnerR.mirror = true;
      this.setRotation(this.drawnCrossInnerR, 0.0F, 0.0F, 0.0F);
      this.drawnCrossOuterL = new ModelRenderer(this, 0, 14);
      this.drawnCrossOuterL.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2);
      this.drawnCrossOuterL.setRotationPoint(4.0F, 0.0F, -4.0F);
      this.drawnCrossOuterL.setTextureSize(64, 32);
      this.drawnCrossOuterL.mirror = true;
      this.setRotation(this.drawnCrossOuterL, 0.0F, 0.0F, 0.0F);
      this.drawnCrossInnerL = new ModelRenderer(this, 0, 14);
      this.drawnCrossInnerL.addBox(0.0F, 0.0F, 0.0F, 1, 1, 2);
      this.drawnCrossInnerL.setRotationPoint(3.0F, 0.0F, -6.0F);
      this.drawnCrossInnerL.setTextureSize(64, 32);
      this.drawnCrossInnerL.mirror = true;
      this.setRotation(this.drawnCrossInnerL, 0.0F, 0.0F, 0.0F);
      this.drawnStringInnerR = new ModelRenderer(this, 0, 0);
      this.drawnStringInnerR.addBox(-1.0F, 0.0F, 0.0F, 1, 1, 1);
      this.drawnStringInnerR.setRotationPoint(-2.0F, 0.0F, -1.0F);
      this.drawnStringInnerR.setTextureSize(64, 32);
      this.drawnStringInnerR.mirror = true;
      this.setRotation(this.drawnStringInnerR, 0.0F, 0.0F, 0.0F);
      this.drawnStringMidR = new ModelRenderer(this, 0, 0);
      this.drawnStringMidR.addBox(-1.0F, 0.0F, 0.0F, 1, 1, 1);
      this.drawnStringMidR.setRotationPoint(-1.0F, 0.0F, 0.0F);
      this.drawnStringMidR.setTextureSize(64, 32);
      this.drawnStringMidR.mirror = true;
      this.setRotation(this.drawnStringMidR, 0.0F, 0.0F, 0.0F);
      this.drawnStringOuterR = new ModelRenderer(this, 0, 0);
      this.drawnStringOuterR.addBox(-1.0F, 0.0F, 0.0F, 1, 1, 1);
      this.drawnStringOuterR.setRotationPoint(-3.0F, 0.0F, -2.0F);
      this.drawnStringOuterR.setTextureSize(64, 32);
      this.drawnStringOuterR.mirror = true;
      this.setRotation(this.drawnStringOuterR, 0.0F, 0.0F, 0.0F);
      this.drawnStringInnerL = new ModelRenderer(this, 0, 0);
      this.drawnStringInnerL.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
      this.drawnStringInnerL.setRotationPoint(2.0F, 0.0F, -1.0F);
      this.drawnStringInnerL.setTextureSize(64, 32);
      this.drawnStringInnerL.mirror = true;
      this.setRotation(this.drawnStringInnerL, 0.0F, 0.0F, 0.0F);
      this.drawnStringMidL = new ModelRenderer(this, 0, 0);
      this.drawnStringMidL.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
      this.drawnStringMidL.setRotationPoint(1.0F, 0.0F, 0.0F);
      this.drawnStringMidL.setTextureSize(64, 32);
      this.drawnStringMidL.mirror = true;
      this.setRotation(this.drawnStringMidL, 0.0F, 0.0F, 0.0F);
      this.drawnStringOuterL = new ModelRenderer(this, 0, 0);
      this.drawnStringOuterL.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1);
      this.drawnStringOuterL.setRotationPoint(3.0F, 0.0F, -2.0F);
      this.drawnStringOuterL.setTextureSize(64, 32);
      this.drawnStringOuterL.mirror = true;
      this.setRotation(this.drawnStringOuterL, 0.0F, 0.0F, 0.0F);
      this.drawnStringCenter = new ModelRenderer(this, 4, 0);
      this.drawnStringCenter.addBox(-1.5F, 0.0F, -0.5F, 3, 1, 1);
      this.drawnStringCenter.setRotationPoint(0.0F, -0.1F, 1.0F);
      this.drawnStringCenter.setTextureSize(64, 32);
      this.drawnStringCenter.mirror = true;
      this.setRotation(this.drawnStringCenter, 0.0F, 0.0174533F, 0.0F);
      this.boltStake = new ModelRenderer(this, 0, 22);
      this.boltStake.addBox(-0.5F, 0.5F, -6.0F, 1, 1, 9);
      this.boltStake.setRotationPoint(0.0F, -1.0F, -2.0F);
      this.boltStake.setTextureSize(64, 32);
      this.boltStake.mirror = true;
      this.setRotation(this.boltStake, 0.0F, 0.0F, 0.0F);
      this.boltDraining = new ModelRenderer(this, 20, 22);
      this.boltDraining.addBox(-0.5F, 0.5F, -6.0F, 1, 1, 9);
      this.boltDraining.setRotationPoint(0.0F, -1.0F, -2.0F);
      this.boltDraining.setTextureSize(64, 32);
      this.boltDraining.mirror = true;
      this.setRotation(this.boltDraining, 0.0F, 0.0F, 0.0F);
      this.boltHoly = new ModelRenderer(this, 40, 22);
      this.boltHoly.addBox(-0.5F, 0.5F, -6.0F, 1, 1, 9);
      this.boltHoly.setRotationPoint(0.0F, -1.0F, -2.0F);
      this.boltHoly.setTextureSize(64, 32);
      this.boltHoly.mirror = true;
      this.setRotation(this.boltHoly, 0.0F, 0.0F, 0.0F);
      this.boltSplitting = new ModelRenderer(this, 20, 12);
      this.boltSplitting.addBox(-0.5F, 0.5F, -6.0F, 1, 1, 9);
      this.boltSplitting.setRotationPoint(0.0F, -1.0F, -2.0F);
      this.boltSplitting.setTextureSize(64, 32);
      this.boltSplitting.mirror = true;
      this.setRotation(this.boltSplitting, 0.0F, 0.0F, 0.0F);
      this.boltSplitting2 = new ModelRenderer(this, 17, 11);
      this.boltSplitting2.addBox(-0.5F, 0.5F, -6.0F, 2, 1, 4);
      this.boltSplitting2.setRotationPoint(-0.5F, -1.5F, -1.0F);
      this.boltSplitting2.setTextureSize(64, 32);
      this.boltSplitting2.mirror = true;
      this.setRotation(this.boltSplitting2, 0.0F, 0.0F, 0.0F);
      this.boltSilver = new ModelRenderer(this, 40, 12);
      this.boltSilver.addBox(-0.5F, 0.5F, -6.0F, 1, 1, 9);
      this.boltSilver.setRotationPoint(0.0F, -1.0F, -2.0F);
      this.boltSilver.setTextureSize(64, 32);
      this.boltSilver.mirror = true;
      this.setRotation(this.boltSplitting, 0.0F, 0.0F, 0.0F);
      this.cross.rotationPointY = -0.3F;
      this.drawnCrossInnerL.rotationPointY = this.drawnCrossInnerR.rotationPointY = -0.15F;
      this.drawnStringMidL.rotationPointY = this.drawnStringMidR.rotationPointY = -0.1F;
      this.drawnStringInnerL.rotationPointY = this.drawnStringInnerR.rotationPointY = -0.05F;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, ItemGeneral.BoltType boltType, int useCount) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.stockTop.render(f5);
      this.stockBottom.render(f5);
      this.stockCatch.render(f5);
      this.grip.render(f5);
      this.cross.render(f5);
      if(useCount > 10) {
         this.drawnCrossInnerL.rotationPointZ = this.drawnCrossInnerR.rotationPointZ = -6.0F;
         this.drawnCrossOuterL.rotationPointZ = this.drawnCrossOuterR.rotationPointZ = -4.0F;
         this.drawnStringInnerL.rotationPointZ = this.drawnStringInnerR.rotationPointZ = -1.0F;
         this.drawnStringMidL.rotationPointZ = this.drawnStringMidR.rotationPointZ = 0.0F;
         this.drawnStringOuterL.rotationPointZ = this.drawnStringOuterR.rotationPointZ = -2.0F;
         this.drawnStringCenter.rotationPointZ = 1.0F;
      } else if(useCount > 5) {
         this.drawnCrossInnerL.rotationPointZ = this.drawnCrossInnerR.rotationPointZ = -6.0F;
         this.drawnCrossOuterL.rotationPointZ = this.drawnCrossOuterR.rotationPointZ = -5.0F;
         this.drawnStringInnerL.rotationPointZ = this.drawnStringInnerR.rotationPointZ = -2.0F;
         this.drawnStringMidL.rotationPointZ = this.drawnStringMidR.rotationPointZ = -2.0F;
         this.drawnStringOuterL.rotationPointZ = this.drawnStringOuterR.rotationPointZ = -3.0F;
         this.drawnStringCenter.rotationPointZ = -1.0F;
      } else if(useCount == 0) {
         this.drawnCrossInnerL.rotationPointZ = this.drawnCrossInnerR.rotationPointZ = -7.0F;
         this.drawnCrossOuterL.rotationPointZ = this.drawnCrossOuterR.rotationPointZ = -6.0F;
         this.drawnStringInnerL.rotationPointZ = this.drawnStringInnerR.rotationPointZ = -4.0F;
         this.drawnStringMidL.rotationPointZ = this.drawnStringMidR.rotationPointZ = -4.0F;
         this.drawnStringOuterL.rotationPointZ = this.drawnStringOuterR.rotationPointZ = -4.0F;
         this.drawnStringCenter.rotationPointZ = -3.25F;
      }

      this.drawnCrossOuterR.render(f5);
      this.drawnCrossOuterL.render(f5);
      this.drawnCrossInnerR.render(f5);
      this.drawnCrossInnerL.render(f5);
      this.drawnStringInnerR.render(f5);
      this.drawnStringMidR.render(f5);
      this.drawnStringOuterR.render(f5);
      this.drawnStringInnerL.render(f5);
      this.drawnStringMidL.render(f5);
      this.drawnStringOuterL.render(f5);
      this.drawnStringCenter.render(f5);
      if(boltType == Witchery.Items.GENERIC.itemBoltStake) {
         this.boltStake.render(f5);
      } else if(boltType == Witchery.Items.GENERIC.itemBoltAntiMagic) {
         this.boltDraining.render(f5);
      } else if(boltType == Witchery.Items.GENERIC.itemBoltHoly) {
         this.boltHoly.render(f5);
      } else if(boltType == Witchery.Items.GENERIC.itemBoltSilver) {
         this.boltSilver.render(f5);
      } else if(boltType == Witchery.Items.GENERIC.itemBoltSplitting) {
         this.boltSplitting.render(f5);
         this.boltSplitting2.render(f5);
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
