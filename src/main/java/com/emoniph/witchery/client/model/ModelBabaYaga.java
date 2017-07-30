package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelBabaYaga extends ModelVillager {

   public boolean field_82900_g;
   private ModelRenderer field_82901_h = (new ModelRenderer(this)).setTextureSize(64, 128);
   private ModelRenderer witchHat;
   private ModelRenderer mortar;
   private ModelRenderer pestle;
   public ModelRenderer bipedCloak = new ModelRenderer(this, 94, 0);


   public ModelBabaYaga(float par1) {
      super(par1, 0.0F, 128, 128);
      this.bipedCloak.setTextureSize(128, 128);
      this.bipedCloak.addBox(0.0F, 0.0F, 0.0F, 8, 10, 0);
      this.bipedCloak.rotateAngleX = 0.1F;
      this.field_82901_h.setRotationPoint(0.0F, -2.0F, 0.0F);
      this.field_82901_h.setTextureOffset(0, 0).addBox(0.0F, 3.0F, -6.75F, 1, 1, 1, -0.15F);
      super.villagerNose.addChild(this.field_82901_h);
      this.witchHat = (new ModelRenderer(this)).setTextureSize(128, 128);
      this.witchHat.setRotationPoint(-7.0F, -10.03125F, -7.0F);
      this.witchHat.setTextureOffset(0, 98).addBox(0.0F, 0.0F, 0.0F, 14, 2, 14);
      super.villagerHead.addChild(this.witchHat);
      ModelRenderer modelrenderer = (new ModelRenderer(this)).setTextureSize(128, 128);
      modelrenderer.setRotationPoint(3.75F, -4.0F, 4.0F);
      modelrenderer.setTextureOffset(0, 76).addBox(0.0F, 0.0F, 0.0F, 7, 4, 7);
      modelrenderer.rotateAngleX = -0.05235988F;
      modelrenderer.rotateAngleZ = 0.02617994F;
      this.witchHat.addChild(modelrenderer);
      ModelRenderer modelrenderer1 = (new ModelRenderer(this)).setTextureSize(128, 128);
      modelrenderer1.setRotationPoint(1.75F, -4.0F, 2.0F);
      modelrenderer1.setTextureOffset(0, 87).addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
      modelrenderer1.rotateAngleX = -0.10471976F;
      modelrenderer1.rotateAngleZ = 0.05235988F;
      modelrenderer.addChild(modelrenderer1);
      ModelRenderer modelrenderer2 = (new ModelRenderer(this)).setTextureSize(128, 128);
      modelrenderer2.setRotationPoint(1.75F, -2.0F, 2.0F);
      modelrenderer2.setTextureOffset(0, 95).addBox(0.0F, 0.0F, 0.0F, 1, 2, 1, 0.25F);
      modelrenderer2.rotateAngleX = -0.20943952F;
      modelrenderer2.rotateAngleZ = 0.10471976F;
      modelrenderer1.addChild(modelrenderer2);
      this.setTextureOffset("mortar.bottom", 80, 88);
      this.setTextureOffset("mortar.top", 72, 107);
      this.setTextureOffset("pestle.upper", 124, 0);
      this.setTextureOffset("pestle.lower", 116, 13);
      this.mortar = new ModelRenderer(this, "mortar");
      this.mortar.setTextureSize(128, 128);
      this.mortar.setRotationPoint(-7.0F, 10.0F, -8.0F);
      this.setRotation(this.mortar, 0.0F, 0.0F, 0.0F);
      this.mortar.mirror = true;
      this.mortar.addBox("bottom", 1.0F, 7.0F, 2.0F, 12, 7, 12);
      this.mortar.addBox("top", 0.0F, 0.0F, 1.0F, 14, 7, 14);
      this.pestle = new ModelRenderer(this, "pestle");
      this.pestle.setTextureSize(128, 128);
      this.pestle.setRotationPoint(-3.0F, 6.0F, -4.0F);
      this.setRotation(this.pestle, -1.152537F, -2.305074F, 1.839205F);
      this.pestle.mirror = true;
      this.pestle.addBox("upper", -1.0F, -7.0F, 0.0F, 1, 12, 1);
      this.pestle.addBox("lower", -2.0F, 5.0F, -1.0F, 3, 12, 3);
   }

   public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      GL11.glTranslatef(0.0F, -0.2F, 0.0F);
      super.render(par1Entity, par2, par3, par4, par5, par6, par7);
      this.mortar.render(par7);
      this.pestle.render(par7);
      this.bipedCloak.render(par7);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
      super.villagerHead.rotateAngleY = par4 / 57.295776F;
      super.villagerHead.rotateAngleX = par5 / 57.295776F;
      super.villagerArms.rotationPointY = 3.0F;
      super.villagerArms.rotationPointZ = -1.0F;
      super.villagerArms.rotateAngleX = -0.75F;
      this.bipedCloak.setRotationPoint(-3.5F, -0.5F, 3.5F);
      super.villagerNose.offsetX = super.villagerNose.offsetY = super.villagerNose.offsetZ = 0.0F;
      float f6 = 0.01F * (float)(par7Entity.getEntityId() % 10);
      super.villagerNose.rotateAngleX = MathHelper.sin((float)par7Entity.ticksExisted * f6) * 4.5F * 3.1415927F / 180.0F;
      super.villagerNose.rotateAngleY = 0.0F;
      super.villagerNose.rotateAngleZ = MathHelper.cos((float)par7Entity.ticksExisted * f6) * 2.5F * 3.1415927F / 180.0F;
      if(this.field_82900_g) {
         super.villagerNose.rotateAngleX = -0.9F;
         super.villagerNose.offsetZ = -0.09375F;
         super.villagerNose.offsetY = 0.1875F;
      }

      this.pestle.rotateAngleX = -1.152537F + MathHelper.sin((float)par7Entity.ticksExisted * f6) * 4.5F * 3.1415927F / 180.0F;
      this.pestle.rotateAngleY = -2.305074F;
      this.pestle.rotateAngleZ = 1.839205F + MathHelper.cos((float)par7Entity.ticksExisted * f6) * 2.5F * 3.1415927F / 180.0F;
   }
}
