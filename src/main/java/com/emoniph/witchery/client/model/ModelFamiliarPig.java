package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityFamiliar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelPig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelFamiliarPig extends ModelPig {

   int field_78163_i;


   public ModelFamiliarPig() {
      this(0.0F);
   }

   public ModelFamiliarPig(float par1) {
      super(par1);
      this.field_78163_i = 1;
   }

   public void setLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
      EntityFamiliar entityocelot = (EntityFamiliar)par1EntityLivingBase;
      super.head.setRotationPoint(0.0F, 12.0F, -6.0F);
      super.body.setRotationPoint(0.0F, 11.0F, 2.0F);
      super.leg1.setRotationPoint(-3.0F, 18.0F, 7.0F);
      super.leg2.setRotationPoint(3.0F, 18.0F, 7.0F);
      super.leg3.setRotationPoint(-3.0F, 18.0F, -5.0F);
      super.leg4.setRotationPoint(3.0F, 18.0F, -5.0F);
      if(entityocelot.isSitting()) {
         super.body.rotateAngleX = 0.7853982F;
         super.body.rotationPointY += 3.5F;
         super.body.rotationPointZ += 0.0F;
         super.leg1.rotateAngleX = super.leg2.rotateAngleX = -0.15707964F;
         super.leg1.rotationPointY = super.leg2.rotationPointY = 15.8F;
         super.leg1.rotationPointZ = super.leg2.rotationPointZ = -7.0F;
         super.leg3.rotateAngleX = super.leg4.rotateAngleX = -1.5707964F;
         super.leg3.rotationPointY = super.leg4.rotationPointY = 21.0F;
         super.leg3.rotationPointZ = super.leg4.rotationPointZ = 1.0F;
         this.field_78163_i = 3;
      } else {
         this.field_78163_i = 1;
      }

   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
      super.head.rotateAngleX = par5 / 57.295776F;
      super.head.rotateAngleY = par4 / 57.295776F;
      if(this.field_78163_i != 3) {
         super.body.rotateAngleX = 1.5707964F;
         super.leg3.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.0F * par2;
         super.leg4.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 1.0F * par2;
         super.leg1.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 1.0F * par2;
         super.leg2.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.0F * par2;
      }

   }
}
