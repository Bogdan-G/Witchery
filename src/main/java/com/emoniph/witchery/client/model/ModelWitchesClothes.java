package com.emoniph.witchery.client.model;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemWitchesClothes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelWitchesClothes extends ModelBiped {

   ModelRenderer hat;
   ModelRenderer torso;
   ModelRenderer bottomBack;
   ModelRenderer bottomRight;
   ModelRenderer bottomLeft;
   ModelRenderer Shape1;
   ModelRenderer Shape2;
   ModelRenderer Shape3;
   ModelRenderer headRight1;
   ModelRenderer headLeft1;
   ModelRenderer legRightF;
   ModelRenderer legLeftF;
   ModelRenderer legRightB;
   ModelRenderer legLeftB;
   ModelRenderer bodyF;
   ModelRenderer bodyB;
   ModelRenderer armRightF;
   ModelRenderer armLeftF;
   ModelRenderer armRightB;
   ModelRenderer armLeftB;
   ModelRenderer armLeftOut;
   ModelRenderer armRightOut;
   ModelRenderer spikeLowerRight;
   ModelRenderer spikeLowerLeft;
   ModelRenderer spikeUpperLeft;
   ModelRenderer spikeUpperRight;
   ModelRenderer shoulderRight;
   ModelRenderer shoulderLeft;
   private ModelRenderer babasHat;


   public ModelWitchesClothes(float scale, boolean shoulders) {
      super(scale, 0.0F, 128, 64);
      this.setTextureOffset("hat.hatBrim", 0, 49);
      this.setTextureOffset("hat.hatCollar", 0, 36);
      this.setTextureOffset("hat.hatBody", 31, 34);
      this.setTextureOffset("hat.hatPoint", 50, 34);
      this.hat = new ModelRenderer(this, "hat");
      this.hat.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.setRotation(this.hat, 0.0F, 0.0F, 0.0F);
      this.hat.mirror = true;
      this.hat.addBox("hatBrim", -7.0F, -7.0F, -7.0F, 14, 1, 14);
      this.hat.addBox("hatCollar", -5.0F, -9.0F, -5.0F, 10, 2, 10);
      this.hat.addBox("hatBody", -3.0F, -14.0F, -3.0F, 6, 5, 6);
      this.hat.addBox("hatPoint", -1.0F, -17.0F, -1.0F, 2, 3, 2);
      super.bipedHead.addChild(this.hat);
      this.babasHat = new ModelRenderer(this, 72, 48);
      this.babasHat.setRotationPoint(-7.0F, -8.0F, -7.0F);
      this.babasHat.addBox(0.0F, 0.0F, 0.0F, 14, 2, 14, 0.52F);
      this.setRotation(this.babasHat, 0.0F, 0.0F, 0.0F);
      this.babasHat.mirror = true;
      super.bipedHead.addChild(this.babasHat);
      ModelRenderer modelrenderer = new ModelRenderer(this, 83, 29);
      modelrenderer.setRotationPoint(3.75F, -4.0F, 4.0F);
      modelrenderer.addBox(0.0F, 0.0F, 0.0F, 7, 4, 7, 0.4F);
      modelrenderer.rotateAngleX = -0.05235988F;
      modelrenderer.rotateAngleZ = 0.02617994F;
      this.babasHat.addChild(modelrenderer);
      ModelRenderer modelrenderer1 = new ModelRenderer(this, 83, 40);
      modelrenderer1.setRotationPoint(1.75F, -4.0F, 2.0F);
      modelrenderer1.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
      modelrenderer1.rotateAngleX = -0.10471976F;
      modelrenderer1.rotateAngleZ = 0.05235988F;
      modelrenderer.addChild(modelrenderer1);
      ModelRenderer modelrenderer2 = new ModelRenderer(this, 81, 48);
      modelrenderer2.setRotationPoint(1.75F, -2.0F, 2.0F);
      modelrenderer2.addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.25F);
      modelrenderer2.rotateAngleX = -0.20943952F;
      modelrenderer2.rotateAngleZ = 0.10471976F;
      modelrenderer1.addChild(modelrenderer2);
      this.torso = new ModelRenderer(this, 43, 46);
      this.torso.addBox(-4.0F, 0.0F, -2.0F, 8, 6, 4, scale);
      this.torso.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.torso.mirror = true;
      this.setRotation(this.torso, 0.0F, 0.0F, 0.0F);
      super.bipedBody.addChild(this.torso);
      if(shoulders) {
         this.Shape1 = new ModelRenderer(this, 61, 32);
         this.Shape1.addBox(0.0F, 0.0F, 0.0F, 5, 1, 6, scale + 0.1F);
         this.Shape1.setRotationPoint(-9.0F, 0.0F, -3.0F);
         this.Shape1.mirror = true;
         this.Shape2 = new ModelRenderer(this, 61, 39);
         this.Shape2.addBox(0.0F, 0.0F, 0.0F, 5, 1, 6, scale + 0.1F);
         this.Shape2.setRotationPoint(4.0F, 0.0F, -3.0F);
         this.Shape2.mirror = true;
         this.Shape2.setRotationPoint(0.0F, -2.0F, -3.0F);
         this.Shape1.setRotationPoint(-4.0F, -2.0F, -3.0F);
         super.bipedRightArm.addChild(this.Shape1);
         super.bipedLeftArm.addChild(this.Shape2);
      }

      this.headRight1 = new ModelRenderer(this, 124, 0);
      this.headRight1.addBox(-0.5F, -5.0F, -0.5F, 1, 5, 1);
      this.headRight1.setRotationPoint(-4.0F, 0.0F, 0.0F);
      this.headRight1.setTextureSize(64, 128);
      this.headRight1.mirror = true;
      this.setRotation(this.headRight1, -0.1487144F, 0.0F, -0.4089647F);
      this.headLeft1 = new ModelRenderer(this, 124, 0);
      this.headLeft1.addBox(-0.5F, -5.0F, -0.5F, 1, 5, 1);
      this.headLeft1.setRotationPoint(4.0F, 0.0F, 0.0F);
      this.headLeft1.setTextureSize(64, 128);
      this.headLeft1.mirror = true;
      this.setRotation(this.headLeft1, -0.1487144F, 0.0F, 0.4089647F);
      this.legRightF = new ModelRenderer(this, 95, 0);
      this.legRightF.addBox(0.0F, 0.0F, 0.0F, 2, 7, 1);
      this.legRightF.setRotationPoint(-4.0F, 13.0F, -3.0F);
      this.legRightF.setTextureSize(64, 128);
      this.legRightF.mirror = true;
      this.setRotation(this.legRightF, 0.0F, 0.0F, -0.2230717F);
      this.legLeftF = new ModelRenderer(this, 95, 0);
      this.legLeftF.addBox(0.0F, 0.0F, 0.0F, 2, 7, 1);
      this.legLeftF.setRotationPoint(1.0F, 13.0F, -3.0F);
      this.legLeftF.setTextureSize(64, 128);
      this.legLeftF.mirror = true;
      this.setRotation(this.legLeftF, 0.0F, 0.0F, 0.1230717F);
      this.legRightB = new ModelRenderer(this, 95, 0);
      this.legRightB.addBox(0.0F, 0.0F, 0.0F, 2, 7, 1);
      this.legRightB.setRotationPoint(-4.0F, 13.0F, 2.0F);
      this.legRightB.setTextureSize(64, 128);
      this.legRightB.mirror = true;
      this.setRotation(this.legRightB, 0.0F, 0.0F, -0.2230717F);
      this.legLeftB = new ModelRenderer(this, 95, 0);
      this.legLeftB.addBox(0.0F, 0.0F, 0.0F, 2, 7, 1);
      this.legLeftB.setRotationPoint(1.0F, 13.0F, 2.0F);
      this.legLeftB.setTextureSize(64, 128);
      this.legLeftB.mirror = true;
      this.setRotation(this.legLeftB, 0.0F, 0.0F, 0.1230717F);
      this.bodyF = new ModelRenderer(this, 111, 0);
      this.bodyF.addBox(0.0F, 0.0F, 0.0F, 6, 9, 1);
      this.bodyF.setRotationPoint(-2.5F, 1.0F, -3.1F);
      this.bodyF.setTextureSize(64, 128);
      this.bodyF.mirror = true;
      this.setRotation(this.bodyF, 0.0F, 0.0F, 0.1487144F);
      this.bodyB = new ModelRenderer(this, 111, 0);
      this.bodyB.addBox(0.0F, 0.0F, 0.0F, 6, 9, 1);
      this.bodyB.setRotationPoint(-2.5F, 1.0F, 2.1F);
      this.bodyB.setTextureSize(64, 128);
      this.bodyB.mirror = true;
      this.setRotation(this.bodyB, 0.0F, 0.0F, 0.0887144F);
      this.armRightF = new ModelRenderer(this, 102, 0);
      this.armRightF.addBox(0.0F, 0.0F, 0.0F, 3, 7, 1);
      this.armRightF.setRotationPoint(-8.0F, 3.0F, -3.0F);
      this.armRightF.setTextureSize(64, 128);
      this.armRightF.mirror = true;
      this.setRotation(this.armRightF, 0.0F, 0.0F, -0.1487144F);
      this.armLeftF = new ModelRenderer(this, 102, 0);
      this.armLeftF.addBox(0.0F, 0.0F, 0.0F, 3, 6, 1);
      this.armLeftF.setRotationPoint(5.0F, 2.0F, -3.0F);
      this.armLeftF.setTextureSize(64, 128);
      this.armLeftF.mirror = true;
      this.setRotation(this.armLeftF, 0.0F, 0.0F, 0.0687144F);
      this.armRightB = new ModelRenderer(this, 102, 0);
      this.armRightB.addBox(0.0F, 0.0F, 0.0F, 3, 7, 1);
      this.armRightB.setRotationPoint(-8.0F, 3.0F, 2.0F);
      this.armRightB.setTextureSize(64, 128);
      this.armRightB.mirror = true;
      this.setRotation(this.armRightB, 0.0F, 0.0F, -0.1487144F);
      this.armLeftB = new ModelRenderer(this, 102, 0);
      this.armLeftB.addBox(0.0F, 0.0F, 0.0F, 3, 6, 1);
      this.armLeftB.setRotationPoint(5.0F, 2.0F, 2.0F);
      this.armLeftB.setTextureSize(64, 128);
      this.armLeftB.mirror = true;
      this.setRotation(this.armLeftB, 0.0F, 0.0F, 0.0687144F);
      this.armLeftOut = new ModelRenderer(this, 120, 0);
      this.armLeftOut.addBox(0.0F, 0.0F, 0.0F, 1, 7, 3);
      this.armLeftOut.setRotationPoint(8.0F, 2.0F, -1.5F);
      this.armLeftOut.setTextureSize(128, 64);
      this.armLeftOut.mirror = true;
      this.setRotation(this.armLeftOut, 0.0371786F, 0.0F, 0.0F);
      this.armRightOut = new ModelRenderer(this, 120, 0);
      this.armRightOut.addBox(0.0F, 0.0F, 0.0F, 1, 6, 3);
      this.armRightOut.setRotationPoint(-9.0F, 2.0F, -1.0F);
      this.armRightOut.setTextureSize(128, 64);
      this.armRightOut.mirror = true;
      this.setRotation(this.armRightOut, -0.1858931F, 0.0F, 0.0F);
      this.spikeLowerRight = new ModelRenderer(this, 120, 0);
      this.spikeLowerRight.addBox(-0.5F, -6.0F, -0.5F, 1, 6, 1);
      this.spikeLowerRight.setRotationPoint(-1.0F, 7.0F, 2.0F);
      this.spikeLowerRight.setTextureSize(128, 64);
      this.spikeLowerRight.mirror = true;
      this.setRotation(this.spikeLowerRight, -0.7807508F, -0.1858931F, 0.0F);
      this.spikeLowerLeft = new ModelRenderer(this, 120, 0);
      this.spikeLowerLeft.addBox(-0.5F, -6.0F, -0.5F, 1, 6, 1);
      this.spikeLowerLeft.setRotationPoint(1.0F, 7.0F, 2.0F);
      this.spikeLowerLeft.setTextureSize(128, 64);
      this.spikeLowerLeft.mirror = true;
      this.setRotation(this.spikeLowerLeft, -0.7807508F, 0.1858931F, 0.0F);
      this.spikeUpperLeft = new ModelRenderer(this, 120, 0);
      this.spikeUpperLeft.addBox(-0.5F, -6.0F, -0.5F, 1, 6, 1);
      this.spikeUpperLeft.setRotationPoint(2.0F, 3.0F, 2.0F);
      this.spikeUpperLeft.setTextureSize(128, 64);
      this.spikeUpperLeft.mirror = true;
      this.setRotation(this.spikeUpperLeft, -0.7807508F, 0.1858931F, 0.0F);
      this.spikeUpperRight = new ModelRenderer(this, 120, 0);
      this.spikeUpperRight.addBox(-0.5F, -6.0F, -0.5F, 1, 6, 1);
      this.spikeUpperRight.setRotationPoint(-2.0F, 3.0F, 2.0F);
      this.spikeUpperRight.setTextureSize(128, 64);
      this.spikeUpperRight.mirror = true;
      this.setRotation(this.spikeUpperRight, -0.7807508F, -0.1858931F, 0.0F);
      this.shoulderRight = new ModelRenderer(this, 108, 0);
      this.shoulderRight.addBox(0.0F, 0.0F, 0.0F, 5, 1, 5);
      this.shoulderRight.setRotationPoint(-9.0F, -1.5F, -2.5F);
      this.shoulderRight.setTextureSize(128, 64);
      this.shoulderRight.mirror = true;
      this.setRotation(this.shoulderRight, 0.0371786F, -0.1115358F, -0.1230717F);
      this.shoulderLeft = new ModelRenderer(this, 108, 0);
      this.shoulderLeft.addBox(0.0F, 0.0F, 0.0F, 5, 1, 5);
      this.shoulderLeft.setRotationPoint(4.0F, -2.5F, -1.5F);
      this.shoulderLeft.setTextureSize(128, 64);
      this.shoulderLeft.mirror = true;
      this.setRotation(this.shoulderLeft, 0.0F, 0.2974289F, 0.1830717F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      EntityLivingBase living;
      ItemStack belt;
      if(entity != null && entity instanceof EntityLivingBase) {
         living = (EntityLivingBase)entity;
         belt = living.getEquipmentInSlot(4);
         if(belt != null && super.bipedHead.showModel) {
            boolean charge = belt.getItem() == Witchery.Items.BABAS_HAT;
            this.hat.showModel = !charge;
            this.babasHat.showModel = charge;
         }
      }

      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      if(entity != null && entity instanceof EntityLivingBase) {
         living = (EntityLivingBase)entity;
         belt = living.getEquipmentInSlot(2);
         if(belt != null && belt.getItem() == Witchery.Items.BARK_BELT && super.bipedBody.showModel) {
            int var14 = Math.min(Witchery.Items.BARK_BELT.getChargeLevel(belt), Witchery.Items.BARK_BELT.getMaxChargeLevel(living));
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            this.renderBark(f5, super.bipedBody, this.bodyF, var14 >= 1);
            this.renderBark(f5, super.bipedBody, this.bodyB, var14 >= 1);
            --var14;
            this.renderBark(f5, super.bipedBody, this.spikeLowerLeft, var14 >= 1);
            this.renderBark(f5, super.bipedBody, this.spikeLowerRight, var14 >= 1);
            this.renderBark(f5, super.bipedBody, this.spikeUpperLeft, var14 >= 1);
            this.renderBark(f5, super.bipedBody, this.spikeUpperRight, var14 >= 1);
            --var14;
            ItemStack shoes = living.getEquipmentInSlot(1);
            if(shoes != null && shoes.getItem() instanceof ItemWitchesClothes) {
               this.renderBark(f5, super.bipedRightLeg, this.legRightF, var14 >= 1 && super.bipedBody.showModel, true);
               this.renderBark(f5, super.bipedRightLeg, this.legRightB, var14 >= 1 && super.bipedBody.showModel, true);
               --var14;
               this.renderBark(f5, super.bipedLeftLeg, this.legLeftF, var14 >= 1 && super.bipedBody.showModel, true);
               this.renderBark(f5, super.bipedLeftLeg, this.legLeftB, var14 >= 1 && super.bipedBody.showModel, true);
               --var14;
            }

            ItemStack robes = living.getEquipmentInSlot(3);
            if(robes != null && robes.getItem() instanceof ItemWitchesClothes) {
               this.renderBark(f5, super.bipedRightArm, this.armRightF, var14 >= 1);
               this.renderBark(f5, super.bipedRightArm, this.armRightOut, var14 >= 1);
               this.renderBark(f5, super.bipedRightArm, this.armRightB, var14 >= 1);
               --var14;
               this.renderBark(f5, super.bipedLeftArm, this.armLeftB, var14 >= 1);
               this.renderBark(f5, super.bipedLeftArm, this.armLeftF, var14 >= 1);
               this.renderBark(f5, super.bipedLeftArm, this.armLeftOut, var14 >= 1);
               --var14;
            }

            ItemStack hat = living.getEquipmentInSlot(4);
            if(hat != null && hat.getItem() instanceof ItemWitchesClothes) {
               this.renderBark(f5, super.bipedRightArm, this.headRight1, var14 >= 1);
               this.renderBark(f5, super.bipedRightArm, this.shoulderRight, var14 >= 1);
               --var14;
               this.renderBark(f5, super.bipedLeftArm, this.headLeft1, var14 >= 1);
               this.renderBark(f5, super.bipedLeftArm, this.shoulderLeft, var14 >= 1);
            }
         }
      }

   }

   private void renderBark(float f5, ModelRenderer bodyPart, ModelRenderer barkPiece, boolean visible) {
      this.renderBark(f5, bodyPart, barkPiece, visible, false);
   }

   private void renderBark(float f5, ModelRenderer bodyPart, ModelRenderer barkPiece, boolean visible, boolean leg) {
      if(visible) {
         GL11.glTranslatef(bodyPart.offsetX, bodyPart.offsetY, bodyPart.offsetZ);
         if(bodyPart.rotateAngleX == 0.0F && bodyPart.rotateAngleY == 0.0F && bodyPart.rotateAngleZ == 0.0F && !leg) {
            if(bodyPart.rotationPointX == 0.0F && bodyPart.rotationPointY == 0.0F && bodyPart.rotationPointZ == 0.0F) {
               barkPiece.render(f5);
            } else {
               GL11.glTranslatef(bodyPart.rotationPointX * f5, bodyPart.rotationPointY * f5, bodyPart.rotationPointZ * f5);
               barkPiece.render(f5);
               GL11.glTranslatef(-bodyPart.rotationPointX * f5, -bodyPart.rotationPointY * f5, -bodyPart.rotationPointZ * f5);
            }
         } else {
            GL11.glPushMatrix();
            GL11.glTranslatef(bodyPart.rotationPointX * f5, bodyPart.rotationPointY * f5, bodyPart.rotationPointZ * f5);
            if(bodyPart.rotateAngleZ != 0.0F) {
               GL11.glRotatef(bodyPart.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if(bodyPart.rotateAngleY != 0.0F) {
               GL11.glRotatef(bodyPart.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if(bodyPart.rotateAngleX != 0.0F) {
               GL11.glRotatef(bodyPart.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glTranslatef(-bodyPart.rotationPointX * f5, -bodyPart.rotationPointY * f5, -bodyPart.rotationPointZ * f5);
            if(super.isSneak && leg) {
               GL11.glTranslatef(0.0F, -3.0F * f5, 4.0F * f5);
               barkPiece.render(f5);
            } else {
               barkPiece.render(f5);
            }

            GL11.glPopMatrix();
         }

         GL11.glTranslatef(-bodyPart.offsetX, -bodyPart.offsetY, -bodyPart.offsetZ);
      }

   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
