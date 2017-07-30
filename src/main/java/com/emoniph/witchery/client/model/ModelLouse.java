package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelLouse extends ModelBase {

   private ModelRenderer[] silverfishBodyParts = new ModelRenderer[7];
   private ModelRenderer[] silverfishWings;
   private float[] field_78170_c = new float[7];
   private static final int[][] silverfishBoxLength = new int[][]{{3, 2, 2}, {4, 3, 2}, {4, 3, 2}, {3, 3, 3}, {2, 2, 3}, {2, 1, 2}, {1, 1, 2}};
   private static final int[][] silverfishTexturePositions = new int[][]{{0, 0}, {0, 4}, {0, 9}, {0, 16}, {0, 22}, {11, 0}, {13, 4}};


   public ModelLouse() {
      float f = -3.5F;

      for(int i = 0; i < this.silverfishBodyParts.length; ++i) {
         this.silverfishBodyParts[i] = new ModelRenderer(this, silverfishTexturePositions[i][0], silverfishTexturePositions[i][1]);
         this.silverfishBodyParts[i].addBox((float)silverfishBoxLength[i][0] * -0.5F, 0.0F, (float)silverfishBoxLength[i][2] * -0.5F, silverfishBoxLength[i][0], silverfishBoxLength[i][1], silverfishBoxLength[i][2]);
         this.silverfishBodyParts[i].setRotationPoint(0.0F, (float)(24 - silverfishBoxLength[i][1]), f);
         this.field_78170_c[i] = f;
         if(i < this.silverfishBodyParts.length - 1) {
            f += (float)(silverfishBoxLength[i][2] + silverfishBoxLength[i + 1][2]) * 0.5F;
         }
      }

      this.silverfishWings = new ModelRenderer[3];
      this.silverfishWings[0] = new ModelRenderer(this, 20, 0);
      this.silverfishWings[0].addBox(-5.0F, 0.0F, (float)silverfishBoxLength[2][2] * -0.5F, 10, 8, silverfishBoxLength[2][2]);
      this.silverfishWings[0].setRotationPoint(0.0F, 16.0F, this.field_78170_c[2]);
      this.silverfishWings[1] = new ModelRenderer(this, 20, 11);
      this.silverfishWings[1].addBox(-3.0F, 0.0F, (float)silverfishBoxLength[4][2] * -0.5F, 6, 4, silverfishBoxLength[4][2]);
      this.silverfishWings[1].setRotationPoint(0.0F, 20.0F, this.field_78170_c[4]);
      this.silverfishWings[2] = new ModelRenderer(this, 20, 18);
      this.silverfishWings[2].addBox(-3.0F, 0.0F, (float)silverfishBoxLength[4][2] * -0.5F, 6, 5, silverfishBoxLength[1][2]);
      this.silverfishWings[2].setRotationPoint(0.0F, 19.0F, this.field_78170_c[1]);
   }

   public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
      this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1Entity);

      int i;
      for(i = 0; i < this.silverfishBodyParts.length; ++i) {
         this.silverfishBodyParts[i].render(par7);
      }

      for(i = 0; i < this.silverfishWings.length; ++i) {
         this.silverfishWings[i].render(par7);
      }

   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
      for(int i = 0; i < this.silverfishBodyParts.length; ++i) {
         this.silverfishBodyParts[i].rotateAngleY = MathHelper.cos(par3 * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.05F * (float)(1 + Math.abs(i - 2));
         this.silverfishBodyParts[i].rotationPointX = MathHelper.sin(par3 * 0.9F + (float)i * 0.15F * 3.1415927F) * 3.1415927F * 0.2F * (float)Math.abs(i - 2);
      }

      this.silverfishWings[0].rotateAngleY = this.silverfishBodyParts[2].rotateAngleY;
      this.silverfishWings[1].rotateAngleY = this.silverfishBodyParts[4].rotateAngleY;
      this.silverfishWings[1].rotationPointX = this.silverfishBodyParts[4].rotationPointX;
      this.silverfishWings[2].rotateAngleY = this.silverfishBodyParts[1].rotateAngleY;
      this.silverfishWings[2].rotationPointX = this.silverfishBodyParts[1].rotationPointX;
   }

}
