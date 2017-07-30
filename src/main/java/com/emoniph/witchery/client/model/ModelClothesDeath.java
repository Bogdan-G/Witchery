package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelClothesDeath extends ModelBiped {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer rightarm;
   ModelRenderer leftarm;
   ModelRenderer rightleg;
   ModelRenderer leftleg;
   ModelRenderer cowl;
   ModelRenderer robe;
   ModelRenderer rightsleave;
   ModelRenderer leftsleave;


   public ModelClothesDeath(float scale) {
      super(0.0F, 0.0F, 128, 64);
      byte off = 56;
      this.head = new ModelRenderer(this, 0 + off, 0);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
      this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.head.setTextureSize(128, 64);
      this.head.mirror = true;
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      super.bipedHead.addChild(this.head);
      this.cowl = new ModelRenderer(this, 0, 45);
      this.cowl.addBox(-4.5F, -8.5F, -4.5F, 9, 10, 9, 0.4F);
      this.cowl.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.cowl.setTextureSize(128, 64);
      this.cowl.mirror = true;
      this.setRotation(this.cowl, 0.0F, 0.0F, 0.0F);
      super.bipedHead.addChild(this.cowl);
      this.body = new ModelRenderer(this, 16 + off, 16);
      this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
      this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.body.setTextureSize(128, 64);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      super.bipedBody.addChild(this.body);
      this.rightarm = new ModelRenderer(this, 40 + off, 16);
      this.rightarm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.05F);
      this.rightarm.setTextureSize(128, 64);
      this.rightarm.mirror = true;
      this.setRotation(this.rightarm, 0.0F, 0.0F, 0.0F);
      super.bipedRightArm.addChild(this.rightarm);
      this.leftarm = new ModelRenderer(this, 40 + off, 16);
      this.leftarm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.05F);
      this.leftarm.setTextureSize(128, 64);
      this.leftarm.mirror = true;
      this.setRotation(this.leftarm, 0.0F, 0.0F, 0.0F);
      super.bipedLeftArm.addChild(this.leftarm);
      this.rightleg = new ModelRenderer(this, 0 + off, 16);
      this.rightleg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.05F);
      this.rightleg.setTextureSize(128, 64);
      this.rightleg.mirror = true;
      this.setRotation(this.rightleg, 0.0F, 0.0F, 0.0F);
      super.bipedRightLeg.addChild(this.rightleg);
      this.leftleg = new ModelRenderer(this, 0 + off, 16);
      this.leftleg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.05F);
      this.leftleg.setTextureSize(128, 64);
      this.leftleg.mirror = true;
      this.setRotation(this.leftleg, 0.0F, 0.0F, 0.0F);
      super.bipedLeftLeg.addChild(this.leftleg);
      this.robe = new ModelRenderer(this, 36, 37);
      this.robe.addBox(-4.5F, 0.0F, -2.5F, 9, 22, 5, 0.1F);
      this.robe.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.robe.setTextureSize(128, 64);
      this.robe.mirror = true;
      this.setRotation(this.robe, 0.0F, 0.0F, 0.0F);
      super.bipedBody.addChild(this.robe);
      this.rightsleave = new ModelRenderer(this, 64, 50);
      this.rightsleave.addBox(-3.0F, -2.0F, -2.0F, 4, 10, 4, 0.1F);
      this.rightsleave.setTextureSize(128, 64);
      this.rightsleave.mirror = true;
      this.setRotation(this.rightsleave, 0.0F, 0.0F, 0.0F);
      super.bipedRightArm.addChild(this.rightsleave);
      this.leftsleave = new ModelRenderer(this, 64, 50);
      this.leftsleave.addBox(-1.0F, -2.0F, -2.0F, 4, 10, 4, 0.1F);
      this.leftsleave.setTextureSize(128, 64);
      this.leftsleave.mirror = true;
      this.setRotation(this.leftsleave, 0.0F, 0.0F, 0.0F);
      super.bipedLeftArm.addChild(this.leftsleave);
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
