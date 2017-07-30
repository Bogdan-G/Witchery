package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntitySummonedUndead;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelSpectre extends ModelBase {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer rightarm;
   ModelRenderer leftarm;
   ModelRenderer robeUpper;
   ModelRenderer robeLower;
   ModelRenderer mouth;
   private final boolean reachingArms;


   public ModelSpectre(boolean reachingArms) {
      this.reachingArms = reachingArms;
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.head = new ModelRenderer(this, 0, 16);
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.head.setTextureSize(64, 32);
      this.head.mirror = true;
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.mouth = new ModelRenderer(this, 56, 0);
      this.mouth.addBox(0.0F, 0.0F, 0.0F, 4, 5, 0);
      this.mouth.setRotationPoint(-2.0F, -4.0F, -4.02F);
      this.mouth.setTextureSize(64, 32);
      this.mouth.mirror = true;
      this.setRotation(this.mouth, 0.0F, 0.0F, 0.0F);
      this.body = new ModelRenderer(this, 16, 0);
      this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 10, 4);
      this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.body.setTextureSize(64, 32);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.rightarm = new ModelRenderer(this, 0, 0);
      this.rightarm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
      this.rightarm.setRotationPoint(-5.0F, 2.0F, 0.0F);
      this.rightarm.setTextureSize(64, 32);
      this.rightarm.mirror = true;
      this.setRotation(this.rightarm, -1.396263F, 0.0F, 0.0F);
      this.leftarm = new ModelRenderer(this, 0, 0);
      this.leftarm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
      this.leftarm.setRotationPoint(5.0F, 2.0F, 0.0F);
      this.leftarm.setTextureSize(64, 32);
      this.leftarm.mirror = true;
      this.setRotation(this.leftarm, -1.396263F, 0.0F, 0.0F);
      this.robeUpper = new ModelRenderer(this, 38, 9);
      this.robeUpper.addBox(-4.0F, 0.0F, -2.0F, 8, 6, 5);
      this.robeUpper.setRotationPoint(0.0F, 10.0F, 0.0F);
      this.robeUpper.setTextureSize(64, 32);
      this.robeUpper.mirror = true;
      this.setRotation(this.robeUpper, 0.0F, 0.0F, 0.0F);
      this.robeLower = new ModelRenderer(this, 32, 20);
      this.robeLower.addBox(-5.0F, 0.0F, -2.0F, 10, 6, 6);
      this.robeLower.setRotationPoint(0.0F, 16.0F, 0.0F);
      this.robeLower.setTextureSize(64, 32);
      this.robeLower.mirror = true;
      this.setRotation(this.robeLower, 0.0F, 0.0F, 0.0F);
      this.head.addChild(this.mouth);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      boolean screaming = entity != null && entity instanceof EntitySummonedUndead && ((EntitySummonedUndead)entity).isScreaming();
      this.mouth.isHidden = !screaming;
      this.mouth.setRotationPoint(-2.0F, -4.0F, -4.02F);
      this.head.render(f5);
      this.body.render(f5);
      this.rightarm.render(f5);
      this.leftarm.render(f5);
      this.robeUpper.render(f5);
      this.robeLower.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      super.setRotationAngles(par1, par2, par3, par4, par5, par6, entity);
      this.head.rotateAngleY = par4 / 57.295776F;
      this.head.rotateAngleX = par5 / 57.295776F;
      if(this.reachingArms) {
         this.rightarm.rotateAngleX = -1.5F;
         this.leftarm.rotateAngleX = -1.5F;
      } else {
         if(entity != null && entity instanceof EntitySummonedUndead && ((EntitySummonedUndead)entity).isScreaming()) {
            this.rightarm.rotateAngleZ = 1.0F;
            this.leftarm.rotateAngleZ = -1.0F;
         } else {
            this.rightarm.rotateAngleZ = 0.0F;
            this.leftarm.rotateAngleZ = 0.0F;
         }

         this.rightarm.rotateAngleX = -0.2F;
         this.leftarm.rotateAngleX = -0.2F;
      }

      this.rightarm.rotateAngleY = 0.0F;
      this.leftarm.rotateAngleY = 0.0F;
      this.rightarm.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.leftarm.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
   }
}
