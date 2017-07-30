package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelWitchHand extends ModelBase {

   ModelRenderer wrist;
   ModelRenderer palmUpper;
   ModelRenderer palmLower;
   ModelRenderer finger1Upper;
   ModelRenderer finger2Upper;
   ModelRenderer finger3Upper;
   ModelRenderer finger1Lower;
   ModelRenderer finger2Lower;
   ModelRenderer finger3Lower;
   ModelRenderer rightPalm;
   ModelRenderer rightFingerUpper;
   ModelRenderer rightFingerLower;
   ModelRenderer rightThumbUpper;
   ModelRenderer rightThumbLower;
   ModelRenderer leftPalm;
   ModelRenderer leftFingerUpper;
   ModelRenderer leftFingerLower;
   ModelRenderer leftThumbUpper;
   ModelRenderer leftThumbLower;
   ModelRenderer scythe;


   public ModelWitchHand(float scale) {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.wrist = new ModelRenderer(this, 0, 0);
      this.wrist.addBox(0.0F, 0.0F, 0.0F, 5, 5, 2);
      this.wrist.setRotationPoint(-3.0F, 0.0F, 0.0F);
      this.wrist.setTextureSize(64, 64);
      this.wrist.mirror = true;
      this.setRotation(this.wrist, 0.0F, 0.0F, 0.0F);
      this.palmUpper = new ModelRenderer(this, 0, 7);
      this.palmUpper.addBox(0.0F, 0.0F, 0.0F, 5, 1, 5, scale);
      this.palmUpper.setRotationPoint(-3.0F, 0.0F, -5.0F);
      this.palmUpper.setTextureSize(64, 64);
      this.palmUpper.mirror = true;
      this.setRotation(this.palmUpper, 0.0F, 0.0F, 0.0F);
      this.palmLower = new ModelRenderer(this, 0, 13);
      this.palmLower.addBox(0.0F, 0.0F, 0.0F, 5, 2, 3, scale);
      this.palmLower.setRotationPoint(-3.0F, 1.0F, -3.0F);
      this.palmLower.setTextureSize(64, 64);
      this.palmLower.mirror = true;
      this.setRotation(this.palmLower, 0.0F, 0.0F, 0.0F);
      this.finger1Upper = new ModelRenderer(this, 0, 18);
      this.finger1Upper.addBox(0.0F, 0.0F, -2.0F, 1, 1, 4, scale);
      this.finger1Upper.setRotationPoint(-3.0F, 1.0F, -7.0F);
      this.finger1Upper.setTextureSize(64, 64);
      this.finger1Upper.mirror = true;
      this.setRotation(this.finger1Upper, 0.4833219F, 0.0F, 0.0F);
      this.finger2Upper = new ModelRenderer(this, 6, 19);
      this.finger2Upper.addBox(0.0F, 0.0F, -2.0F, 1, 1, 4, scale);
      this.finger2Upper.setRotationPoint(-1.0F, 1.0F, -7.0F);
      this.finger2Upper.setTextureSize(64, 64);
      this.finger2Upper.mirror = true;
      this.setRotation(this.finger2Upper, 0.4833219F, 0.0F, 0.0F);
      this.finger3Upper = new ModelRenderer(this, 12, 18);
      this.finger3Upper.addBox(0.0F, 0.0F, -2.0F, 1, 1, 4, scale);
      this.finger3Upper.setRotationPoint(1.0F, 1.0F, -7.0F);
      this.finger3Upper.setTextureSize(64, 64);
      this.finger3Upper.mirror = true;
      this.setRotation(this.finger3Upper, 0.4833219F, 0.0F, 0.0F);
      this.finger1Lower = new ModelRenderer(this, 0, 23);
      this.finger1Lower.addBox(0.0F, 0.0F, -4.0F, 1, 1, 4, scale);
      this.finger1Lower.setRotationPoint(-3.0F, 2.0F, -9.0F);
      this.finger1Lower.setTextureSize(64, 64);
      this.finger1Lower.mirror = true;
      this.setRotation(this.finger1Lower, 2.044824F, 0.0F, 0.0F);
      this.finger2Lower = new ModelRenderer(this, 6, 24);
      this.finger2Lower.addBox(0.0F, 0.0F, -4.0F, 1, 1, 4, scale);
      this.finger2Lower.setRotationPoint(-1.0F, 2.0F, -9.0F);
      this.finger2Lower.setTextureSize(64, 64);
      this.finger2Lower.mirror = true;
      this.setRotation(this.finger2Lower, 2.044824F, 0.0F, 0.0F);
      this.finger3Lower = new ModelRenderer(this, 12, 23);
      this.finger3Lower.addBox(0.0F, 0.0F, -4.0F, 1, 1, 4, scale);
      this.finger3Lower.setRotationPoint(1.0F, 2.0F, -9.0F);
      this.finger3Lower.setTextureSize(64, 64);
      this.finger3Lower.mirror = true;
      this.setRotation(this.finger3Lower, 2.044824F, 0.0F, 0.0F);
      this.rightPalm = new ModelRenderer(this, 16, 0);
      this.rightPalm.addBox(0.0F, 0.0F, 0.0F, 2, 1, 6, scale);
      this.rightPalm.setRotationPoint(2.0F, 0.0F, -5.0F);
      this.rightPalm.setTextureSize(64, 64);
      this.rightPalm.mirror = true;
      this.setRotation(this.rightPalm, 0.0F, 0.0F, 0.0F);
      this.rightFingerUpper = new ModelRenderer(this, 20, 7);
      this.rightFingerUpper.addBox(0.0F, 0.0F, -4.0F, 1, 1, 4, scale);
      this.rightFingerUpper.setRotationPoint(3.0F, 0.0F, -5.0F);
      this.rightFingerUpper.setTextureSize(64, 64);
      this.rightFingerUpper.mirror = true;
      this.setRotation(this.rightFingerUpper, -0.5205006F, 0.0F, 0.0F);
      this.rightFingerLower = new ModelRenderer(this, 20, 12);
      this.rightFingerLower.addBox(0.0F, 0.0F, 0.0F, 1, 1, 4, scale);
      this.rightFingerLower.setRotationPoint(3.0F, -1.0F, -8.0F);
      this.rightFingerLower.setTextureSize(64, 64);
      this.rightFingerLower.mirror = true;
      this.setRotation(this.rightFingerLower, -2.732628F, 0.0F, 0.0F);
      this.rightThumbUpper = new ModelRenderer(this, 22, 17);
      this.rightThumbUpper.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3, scale);
      this.rightThumbUpper.setRotationPoint(3.5F, 1.0F, 0.0F);
      this.rightThumbUpper.setTextureSize(64, 64);
      this.rightThumbUpper.mirror = true;
      this.setRotation(this.rightThumbUpper, -2.7F, -0.7F, -0.669215F);
      this.rightThumbLower = new ModelRenderer(this, 22, 21);
      this.rightThumbLower.addBox(0.0F, -1.0F, -1.0F, 1, 1, 3, scale);
      this.rightThumbLower.setRotationPoint(5.0F, 3.0F, -2.0F);
      this.rightThumbLower.setTextureSize(64, 64);
      this.rightThumbLower.mirror = true;
      this.setRotation(this.rightThumbLower, -1.896109F, 0.2602503F, 0.3717861F);
      this.leftPalm = new ModelRenderer(this, 16, 0);
      this.leftPalm.addBox(0.0F, 0.0F, 0.0F, 2, 1, 6, scale);
      this.leftPalm.setRotationPoint(-5.0F, 0.0F, -5.0F);
      this.leftPalm.setTextureSize(64, 64);
      this.leftPalm.mirror = true;
      this.setRotation(this.leftPalm, 0.0F, 0.0F, 0.0F);
      this.leftFingerUpper = new ModelRenderer(this, 20, 7);
      this.leftFingerUpper.addBox(0.0F, 0.0F, -4.0F, 1, 1, 4, scale);
      this.leftFingerUpper.setRotationPoint(-5.0F, 0.0F, -5.0F);
      this.leftFingerUpper.setTextureSize(64, 64);
      this.leftFingerUpper.mirror = true;
      this.setRotation(this.leftFingerUpper, -0.5205006F, 0.0F, 0.0F);
      this.leftFingerLower = new ModelRenderer(this, 20, 12);
      this.leftFingerLower.addBox(0.0F, 0.0F, 0.0F, 1, 1, 4, scale);
      this.leftFingerLower.setRotationPoint(-5.0F, -1.0F, -8.0F);
      this.leftFingerLower.setTextureSize(64, 64);
      this.leftFingerLower.mirror = true;
      this.setRotation(this.leftFingerLower, -2.732628F, 0.0F, 0.0F);
      this.leftThumbUpper = new ModelRenderer(this, 22, 17);
      this.leftThumbUpper.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3, scale);
      this.leftThumbUpper.setRotationPoint(-5.0F, 1.0F, 0.0F);
      this.leftThumbUpper.setTextureSize(64, 64);
      this.leftThumbUpper.mirror = true;
      this.setRotation(this.leftThumbUpper, -1.7F, 0.8F, 0.148711F);
      this.leftThumbLower = new ModelRenderer(this, 22, 21);
      this.leftThumbLower.addBox(0.0F, 0.0F, 0.0F, 1, 1, 3, scale);
      this.leftThumbLower.setRotationPoint(-6.0F, 4.0F, -1.0F);
      this.leftThumbLower.setTextureSize(64, 64);
      this.leftThumbLower.mirror = true;
      this.setRotation(this.leftThumbLower, -2.082002F, 0.0371828F, -0.6320403F);
      this.setTextureOffset("scythe.shaft", 58, 5);
      this.setTextureOffset("scythe.blade", 36, 0);
      this.scythe = new ModelRenderer(this, "scythe");
      this.scythe.setRotationPoint(-6.0F, 10.0F, 0.0F);
      this.setRotation(this.scythe, 0.0F, 0.0F, 0.0F);
      this.scythe.mirror = true;
      this.scythe.addBox("shaft", -0.5F, -16.0F, -0.5F, 1, 35, 1);
      this.scythe.addBox("blade", 0.0F, -16.0F, 0.0F, 13, 4, 0);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, boolean firstPerson, boolean deployed) {
      this.rightFingerUpper.setRotationPoint(3.0F, 0.0F, -5.0F);
      this.rightFingerLower.setRotationPoint(3.0F, -1.0F, -8.0F);
      this.leftFingerUpper.setRotationPoint(-5.0F, 0.0F, -5.0F);
      this.leftFingerLower.setRotationPoint(-5.0F, -1.0F, -8.0F);
      if(deployed) {
         this.rightFingerUpper.setRotationPoint(3.0F, 4.0F, -4.0F);
         this.rightFingerLower.setRotationPoint(3.0F, 1.0F, -4.0F);
         this.leftFingerUpper.setRotationPoint(-5.0F, 4.0F, -4.0F);
         this.leftFingerLower.setRotationPoint(-5.0F, 1.0F, -4.0F);
      }

      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.wrist.renderWithRotation(f5);
      this.palmUpper.renderWithRotation(f5);
      this.palmLower.renderWithRotation(f5);
      this.finger1Upper.renderWithRotation(f5);
      this.finger2Upper.renderWithRotation(f5);
      this.finger3Upper.renderWithRotation(f5);
      this.finger1Lower.renderWithRotation(f5);
      this.finger2Lower.renderWithRotation(f5);
      this.finger3Lower.renderWithRotation(f5);
      if(firstPerson) {
         this.rightPalm.renderWithRotation(f5);
         this.rightFingerUpper.renderWithRotation(f5);
         this.rightFingerLower.renderWithRotation(f5);
         this.rightThumbUpper.renderWithRotation(f5);
         this.rightThumbLower.renderWithRotation(f5);
      } else {
         this.leftPalm.renderWithRotation(f5);
         this.leftFingerUpper.renderWithRotation(f5);
         this.leftFingerLower.renderWithRotation(f5);
         this.leftThumbUpper.renderWithRotation(f5);
         this.leftThumbLower.renderWithRotation(f5);
      }

      if(deployed) {
         GL11.glScalef(1.2F, 1.2F, 1.2F);
         this.scythe.rotateAngleZ = -1.5707964F;
         this.scythe.rotationPointX = -5.0F;
         this.scythe.rotationPointZ = -3.0F;
         this.scythe.rotationPointY = 2.0F;
         this.scythe.rotateAngleX = 3.1415927F;
         this.scythe.rotateAngleY = 0.0F;
         if(firstPerson) {
            this.scythe.rotateAngleY = -3.1415927F;
            this.scythe.rotationPointX = 6.0F;
         }

         this.scythe.renderWithRotation(f5);
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
