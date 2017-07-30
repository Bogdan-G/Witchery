package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityVampire;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelVampireArmor extends ModelBiped {

   private ModelRenderer skirtFront;
   private ModelRenderer skirtMiddle;
   private ModelRenderer skirtMiddle2;
   private ModelRenderer skirtMiddle3;
   private ModelRenderer skirtBack;
   private ModelRenderer cloakMain;
   private ModelRenderer cloakLeft;
   private ModelRenderer cloakRight;
   public ModelRenderer hat;
   public ModelRenderer hatBrim;
   public ModelRenderer chest;
   private boolean legs;
   private boolean female;
   private boolean metal;
   ResourceLocation chain = new ResourceLocation("witchery", "textures/entities/vampirearmor_chain.png");


   public ModelVampireArmor(float scale, boolean legs, boolean female, boolean metal) {
      super(scale, 0.0F, 64, 96);
      this.legs = legs;
      this.female = female;
      this.metal = metal;
      this.skirtBack = new ModelRenderer(this, 26, 32);
      this.skirtBack.setRotationPoint(0.0F, 11.0F, 0.0F);
      this.skirtBack.addBox(-4.5F, 0.0F, -2.5F, 9, 12, 5, 0.0F);
      this.skirtFront = new ModelRenderer(this, 26, 50);
      this.skirtFront.setRotationPoint(0.0F, 11.0F, 0.0F);
      this.skirtFront.addBox(-4.5F, 0.0F, -2.5F, 9, 12, 5, 0.0F);
      this.skirtMiddle = new ModelRenderer(this, 26, 68);
      this.skirtMiddle.setRotationPoint(0.0F, 11.0F, 0.0F);
      this.skirtMiddle.addBox(-4.5F, 0.0F, -2.5F, 9, 12, 5, 0.0F);
      this.skirtMiddle2 = new ModelRenderer(this, 26, 68);
      this.skirtMiddle2.setRotationPoint(0.0F, 11.0F, 0.0F);
      this.skirtMiddle2.addBox(-4.5F, 0.0F, -2.5F, 9, 12, 5, 0.0F);
      this.skirtMiddle3 = new ModelRenderer(this, 26, 68);
      this.skirtMiddle3.setRotationPoint(0.0F, 11.0F, 0.0F);
      this.skirtMiddle3.addBox(-4.5F, 0.0F, -2.5F, 9, 12, 5, 0.0F);
      this.cloakLeft = new ModelRenderer(this, 0, 56);
      this.cloakLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.cloakLeft.addBox(-3.5F, -8.0F, 4.0F, 7, 7, 1, 0.0F);
      this.setRotateAngle(this.cloakLeft, -0.34906584F, 0.5108652F, 0.41086525F);
      this.cloakRight = new ModelRenderer(this, 0, 56);
      this.cloakRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.cloakRight.addBox(-3.5F, -8.0F, 4.0F, 7, 7, 1, 0.0F);
      this.setRotateAngle(this.cloakRight, -0.34906584F, -0.5108652F, -0.41086525F);
      this.cloakMain = new ModelRenderer(this, 0, 33);
      this.cloakMain.setRotationPoint(0.0F, 1.0F, 0.0F);
      this.cloakMain.addBox(-6.0F, 0.0F, 2.5F, 12, 22, 1, 0.0F);
      this.setRotateAngle(this.cloakMain, 0.045553092F, 0.0F, 0.0F);
      float hatScale = 0.6F;
      this.hatBrim = new ModelRenderer(this, 0, 85);
      this.hatBrim.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.hatBrim.addBox(-5.0F, -7.0F, -5.0F, 10, 1, 10, hatScale + 0.1F);
      this.hat = new ModelRenderer(this, 0, 67);
      this.hat.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.hat.addBox(-4.0F, -15.0F, -4.0F, 8, 8, 8, hatScale);
      if(!metal) {
         super.bipedHead.addChild(this.hat);
         super.bipedHead.addChild(this.hatBrim);
      }

      this.chest = new ModelRenderer(this, 16, 67);
      this.chest.addBox(-4.0F, -2.0F, -5.0F, 8, 4, 4, 0.0F);
      this.chest.setRotationPoint(0.0F, 2.0F, 0.0F);
      this.setRotateAngle(this.chest, 0.7853982F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      if(!this.metal) {
         super.bipedHeadwear.showModel = false;
      }

      if(this.legs) {
         if(!super.isRiding && super.bipedRightLeg.showModel && this.female) {
            this.skirtBack.render(f5);
            this.skirtFront.render(f5);
            this.skirtMiddle.render(f5);
            this.skirtMiddle2.render(f5);
            this.skirtMiddle3.render(f5);
         }
      } else if(super.bipedBody.showModel) {
         if(!(entity instanceof EntityVampire)) {
            this.cloakRight.render(f5);
            this.cloakLeft.render(f5);
            this.cloakMain.render(f5);
         }

         if(this.female) {
            this.chest.render(f5);
         }

         if(this.metal) {
            GL11.glPushMatrix();
            float scale = 1.06F;
            GL11.glScalef(scale, scale, scale);
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.chain);
            if(this.female) {
               this.chest.render(f5);
            }

            super.bipedBody.render(f5);
            GL11.glScalef(scale, scale, scale);
            super.bipedRightArm.rotationPointY = (float)((double)super.bipedRightArm.rotationPointY - 0.05D);
            super.bipedLeftArm.rotationPointY = (float)((double)super.bipedLeftArm.rotationPointY - 0.05D);
            super.bipedRightArm.render(f5);
            super.bipedLeftArm.render(f5);
            GL11.glPopMatrix();
         }
      }

   }

   public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
      super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
      super.bipedRightArm.rotationPointY = 2.0F;
      super.bipedLeftArm.rotationPointY = 2.0F;
      this.hat.rotationPointY = -0.5F;
      this.skirtBack.rotateAngleX = Math.max(super.bipedRightLeg.rotateAngleX, super.bipedLeftLeg.rotateAngleX);
      this.skirtMiddle.rotateAngleX = Math.max(super.bipedRightLeg.rotateAngleX, super.bipedLeftLeg.rotateAngleX) * 0.5F;
      this.skirtFront.rotateAngleX = Math.min(super.bipedRightLeg.rotateAngleX, super.bipedLeftLeg.rotateAngleX);
      this.skirtMiddle2.rotateAngleX = Math.min(super.bipedRightLeg.rotateAngleX, super.bipedLeftLeg.rotateAngleX) * 0.5F;
      if(super.isSneak) {
         this.skirtBack.rotationPointZ = this.skirtFront.rotationPointZ = this.skirtMiddle3.rotationPointZ = this.skirtMiddle.rotationPointZ = this.skirtMiddle2.rotationPointZ = 4.0F;
         this.skirtBack.rotationPointY = this.skirtFront.rotationPointY = this.skirtMiddle3.rotationPointY = this.skirtMiddle.rotationPointY = this.skirtMiddle2.rotationPointY = 8.0F;
         this.cloakMain.rotateAngleX = 0.6F;
      } else {
         this.skirtBack.rotationPointZ = this.skirtFront.rotationPointZ = this.skirtMiddle3.rotationPointZ = this.skirtMiddle.rotationPointZ = this.skirtMiddle2.rotationPointZ = 0.0F;
         this.skirtBack.rotationPointY = this.skirtFront.rotationPointY = this.skirtMiddle3.rotationPointY = this.skirtMiddle.rotationPointY = this.skirtMiddle2.rotationPointY = 11.0F;
         this.cloakMain.rotateAngleX = 0.045553092F;
         if((double)p_78087_2_ > 0.1D) {
            this.cloakMain.rotateAngleX = (float)((double)this.cloakMain.rotateAngleX + ((double)p_78087_2_ * 0.8D - 0.1D));
         }
      }

      if((double)super.bipedHead.rotateAngleX < -0.15D) {
         this.cloakLeft.rotateAngleX = super.bipedHead.rotateAngleX - 0.15F;
         this.cloakRight.rotateAngleX = super.bipedHead.rotateAngleX - 0.15F;
      } else {
         this.cloakLeft.rotateAngleX = this.cloakRight.rotateAngleX = -0.3F;
      }

   }
}
