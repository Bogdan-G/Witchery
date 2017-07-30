package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelLeechChest extends ModelBase {

   public ModelRenderer chestBelow;
   public ModelRenderer chestLidBL;
   public ModelRenderer chestLidFR;
   public ModelRenderer chestLidBR;
   public ModelRenderer chestLidFL;
   public ModelRenderer sac1;
   public ModelRenderer sac2;
   public ModelRenderer sac3;


   public ModelLeechChest() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.chestBelow = new ModelRenderer(this, 0, 0);
      this.chestBelow.addBox(0.0F, 0.0F, 0.0F, 14, 9, 14);
      this.chestBelow.setRotationPoint(1.0F, 7.0F, 1.0F);
      this.chestBelow.setTextureSize(64, 64);
      this.chestBelow.mirror = true;
      this.setRotation(this.chestBelow, 0.0F, 0.0F, 0.0F);
      this.chestLidBL = new ModelRenderer(this, 28, 24);
      this.chestLidBL.addBox(-6.0F, -5.0F, -6.0F, 6, 5, 6);
      this.chestLidBL.setRotationPoint(14.0F, 7.0F, 14.0F);
      this.chestLidBL.setTextureSize(64, 64);
      this.chestLidBL.mirror = true;
      this.setRotation(this.chestLidBL, 0.0F, 0.0F, 0.0F);
      this.chestLidFR = new ModelRenderer(this, 0, 36);
      this.chestLidFR.addBox(0.0F, -5.0F, 0.0F, 6, 5, 6);
      this.chestLidFR.setRotationPoint(2.0F, 7.0F, 2.0F);
      this.chestLidFR.setTextureSize(64, 64);
      this.chestLidFR.mirror = true;
      this.setRotation(this.chestLidFR, 0.0F, 0.0F, 0.0F);
      this.chestLidBR = new ModelRenderer(this, 0, 24);
      this.chestLidBR.addBox(0.0F, -5.0F, -6.0F, 6, 5, 6);
      this.chestLidBR.setRotationPoint(2.0F, 7.0F, 14.0F);
      this.chestLidBR.setTextureSize(64, 64);
      this.chestLidBR.mirror = true;
      this.setRotation(this.chestLidBR, 0.0F, 0.0F, 0.0F);
      this.chestLidFL = new ModelRenderer(this, 28, 36);
      this.chestLidFL.addBox(-6.0F, -5.0F, 0.0F, 6, 5, 6);
      this.chestLidFL.setRotationPoint(14.0F, 7.0F, 2.0F);
      this.chestLidFL.setTextureSize(64, 64);
      this.chestLidFL.mirror = true;
      this.setRotation(this.chestLidFL, 0.0F, 0.0F, 0.0F);
      this.sac1 = new ModelRenderer(this, 0, 8);
      this.sac1.addBox(0.0F, 0.0F, 0.0F, 2, 3, 1);
      this.sac1.setRotationPoint(3.0F, 8.0F, 0.0F);
      this.sac1.setTextureSize(64, 64);
      this.sac1.mirror = true;
      this.setRotation(this.sac1, 0.0F, 0.0F, 0.0F);
      this.sac2 = new ModelRenderer(this, 0, 3);
      this.sac2.addBox(0.0F, 0.0F, 0.0F, 3, 2, 1);
      this.sac2.setRotationPoint(9.0F, 13.0F, 0.0F);
      this.sac2.setTextureSize(64, 64);
      this.sac2.mirror = true;
      this.setRotation(this.sac2, 0.0F, 0.0F, 0.0F);
      this.sac3 = new ModelRenderer(this, 0, 0);
      this.sac3.addBox(0.0F, 0.0F, 0.0F, 2, 1, 1);
      this.sac3.setRotationPoint(9.0F, 9.0F, 0.0F);
      this.sac3.setTextureSize(64, 64);
      this.sac3.mirror = true;
      this.setRotation(this.sac3, 0.0F, 0.0F, 0.0F);
   }

   public void renderAll(int count) {
      this.chestLidBL.render(0.0625F);
      this.chestLidFL.render(0.0625F);
      this.chestLidBR.render(0.0625F);
      this.chestLidFR.render(0.0625F);
      this.chestBelow.render(0.0625F);
      if(count >= 1) {
         this.sac1.render(0.0625F);
      }

      if(count >= 2) {
         this.sac2.render(0.0625F);
      }

      if(count >= 3) {
         this.sac3.render(0.0625F);
      }

   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }
}
