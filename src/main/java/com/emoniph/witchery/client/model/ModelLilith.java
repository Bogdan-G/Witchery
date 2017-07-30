package com.emoniph.witchery.client.model;

import com.emoniph.witchery.entity.EntityLilith;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelLilith extends ModelBase {

   public ModelRenderer legRight;
   public ModelRenderer legLeft;
   public ModelRenderer bodyChest;
   public ModelRenderer bodyWaist;
   public ModelRenderer skirt1;
   public ModelRenderer skirt2;
   public ModelRenderer bodyShoulders;
   public ModelRenderer armRight;
   public ModelRenderer armLeft;
   public ModelRenderer neck;
   public ModelRenderer head;
   public ModelRenderer legRightLower;
   public ModelRenderer legLeftLower;
   public ModelRenderer armRightLower;
   public ModelRenderer armRightWing;
   public ModelRenderer armLeftLower;
   public ModelRenderer armLeftWing;
   public ModelRenderer head2;
   public ModelRenderer hornRight;
   public ModelRenderer hornLeft;
   public ModelRenderer nose;
   public ModelRenderer toothRight;
   public ModelRenderer toothLeft;
   public ModelRenderer head3;


   public ModelLilith() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.neck = new ModelRenderer(this, 24, 0);
      this.neck.setRotationPoint(0.0F, -13.0F, 0.0F);
      this.neck.addBox(-1.5F, -1.5F, -1.5F, 3, 2, 3, 0.0F);
      this.legLeftLower = new ModelRenderer(this, 48, 47);
      this.legLeftLower.mirror = true;
      this.legLeftLower.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.legLeftLower.addBox(-2.0F, 8.0F, 2.0F, 4, 13, 4, 0.0F);
      this.hornLeft = new ModelRenderer(this, 52, 30);
      this.hornLeft.mirror = true;
      this.hornLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.hornLeft.addBox(1.0F, -12.3F, 0.0F, 6, 10, 0, 0.0F);
      this.setRotateAngle(this.hornLeft, -0.18203785F, 0.0F, 0.0F);
      this.bodyChest = new ModelRenderer(this, 17, 17);
      this.bodyChest.setRotationPoint(0.0F, -9.8F, -1.9F);
      this.bodyChest.addBox(-4.0F, -1.5F, -1.5F, 8, 3, 3, 0.0F);
      this.setRotateAngle(this.bodyChest, 0.7853982F, 0.0F, 0.0F);
      this.nose = new ModelRenderer(this, 41, 0);
      this.nose.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.nose.addBox(-0.5F, -3.6F, -4.0F, 1, 2, 1, 0.0F);
      this.armLeftLower = new ModelRenderer(this, 8, 25);
      this.armLeftLower.mirror = true;
      this.armLeftLower.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armLeftLower.addBox(-0.5F, 9.8F, 0.8F, 3, 13, 3, 0.0F);
      this.setRotateAngle(this.armLeftLower, -0.22759093F, 0.0F, 0.0F);
      this.skirt1 = new ModelRenderer(this, 0, 49);
      this.skirt1.setRotationPoint(0.0F, -0.9F, 0.0F);
      this.skirt1.addBox(-4.5F, 0.0F, -2.5F, 9, 10, 5, 0.0F);
      this.skirt2 = new ModelRenderer(this, 0, 49);
      this.skirt2.setRotationPoint(0.0F, -0.9F, 0.0F);
      this.skirt2.addBox(-4.5F, 0.0F, -2.5F, 9, 10, 5, 0.0F);
      this.armLeftWing = new ModelRenderer(this, 0, 13);
      this.armLeftWing.mirror = true;
      this.armLeftWing.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armLeftWing.addBox(1.0F, -19.6F, -12.7F, 0, 30, 4, 0.0F);
      this.setRotateAngle(this.armLeftWing, 2.5497515F, 0.17453292F, 0.0F);
      this.legRightLower = new ModelRenderer(this, 48, 47);
      this.legRightLower.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.legRightLower.addBox(-2.0F, 8.0F, 2.0F, 4, 13, 4, 0.0F);
      this.armLeft = new ModelRenderer(this, 0, 0);
      this.armLeft.mirror = true;
      this.armLeft.setRotationPoint(4.4F, -11.5F, 0.0F);
      this.armLeft.addBox(-0.5F, -1.5F, -1.5F, 3, 13, 3, 0.0F);
      this.hornRight = new ModelRenderer(this, 52, 30);
      this.hornRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.hornRight.addBox(-7.0F, -12.3F, 0.0F, 6, 10, 0, 0.0F);
      this.setRotateAngle(this.hornRight, -0.18203785F, 0.0F, 0.0F);
      this.legLeft = new ModelRenderer(this, 36, 30);
      this.legLeft.mirror = true;
      this.legLeft.setRotationPoint(2.1F, 2.5F, 0.0F);
      this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 13, 4, 0.0F);
      this.setRotateAngle(this.legLeft, -0.27314404F, 0.0F, 0.0F);
      this.bodyShoulders = new ModelRenderer(this, 15, 6);
      this.bodyShoulders.setRotationPoint(0.0F, -12.7F, 0.0F);
      this.bodyShoulders.addBox(-4.0F, 0.0F, -2.0F, 8, 6, 4, 0.0F);
      this.armRightLower = new ModelRenderer(this, 8, 25);
      this.armRightLower.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armRightLower.addBox(-2.5F, 9.8F, 0.8F, 3, 13, 3, 0.0F);
      this.setRotateAngle(this.armRightLower, -0.22759093F, 0.0F, 0.0F);
      this.head3 = new ModelRenderer(this, 44, 22);
      this.head3.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.head3.addBox(-2.0F, -4.7F, 5.6F, 4, 4, 4, 0.0F);
      this.head2 = new ModelRenderer(this, 42, 12);
      this.head2.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.head2.addBox(-2.5F, -5.5F, 1.0F, 5, 5, 5, 0.0F);
      this.setRotateAngle(this.head2, -0.18203785F, 0.0F, 0.0F);
      this.armRightWing = new ModelRenderer(this, 0, 13);
      this.armRightWing.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.armRightWing.addBox(-1.0F, -19.6F, -12.7F, 0, 30, 4, 0.0F);
      this.setRotateAngle(this.armRightWing, 2.5497515F, -0.17453292F, 0.0F);
      this.legRight = new ModelRenderer(this, 36, 30);
      this.legRight.setRotationPoint(-2.1F, 2.5F, 0.0F);
      this.legRight.addBox(-2.0F, 0.0F, -2.0F, 4, 13, 4, 0.0F);
      this.setRotateAngle(this.legRight, -0.27314404F, 0.0F, 0.0F);
      this.armRight = new ModelRenderer(this, 0, 0);
      this.armRight.setRotationPoint(-4.5F, -11.5F, 0.0F);
      this.armRight.addBox(-2.5F, -1.5F, -1.5F, 3, 13, 3, 0.0F);
      this.toothLeft = new ModelRenderer(this, 20, 0);
      this.toothLeft.mirror = true;
      this.toothLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.toothLeft.addBox(0.5F, -1.6F, -3.6F, 1, 3, 1, -0.35F);
      this.toothRight = new ModelRenderer(this, 20, 0);
      this.toothRight.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.toothRight.addBox(-1.5F, -1.6F, -3.6F, 1, 3, 1, -0.35F);
      this.bodyWaist = new ModelRenderer(this, 20, 24);
      this.bodyWaist.setRotationPoint(0.0F, -7.5F, 0.0F);
      this.bodyWaist.addBox(-3.0F, 0.0F, -1.0F, 6, 10, 2, 0.0F);
      this.head = new ModelRenderer(this, 40, 0);
      this.head.setRotationPoint(0.0F, -13.5F, 0.0F);
      this.head.addBox(-3.0F, -6.0F, -3.0F, 6, 6, 6, 0.0F);
      this.legLeft.addChild(this.legLeftLower);
      this.head.addChild(this.hornLeft);
      this.head.addChild(this.nose);
      this.armLeft.addChild(this.armLeftLower);
      this.armLeft.addChild(this.armLeftWing);
      this.legRight.addChild(this.legRightLower);
      this.head.addChild(this.hornRight);
      this.armRight.addChild(this.armRightLower);
      this.head2.addChild(this.head3);
      this.head.addChild(this.head2);
      this.armRight.addChild(this.armRightWing);
      this.head.addChild(this.toothLeft);
      this.head.addChild(this.toothRight);
   }

   private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.rotateAngleX = x;
      modelRenderer.rotateAngleY = y;
      modelRenderer.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.neck.render(f5);
      this.bodyChest.render(f5);
      this.skirt1.render(f5);
      this.skirt2.render(f5);
      this.armLeft.render(f5);
      this.legLeft.render(f5);
      this.bodyShoulders.render(f5);
      this.legRight.render(f5);
      this.armRight.render(f5);
      this.bodyWaist.render(f5);
      this.head.render(f5);
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      this.head.rotateAngleY = par4 / 57.295776F;
      this.head.rotateAngleX = par5 / 57.295776F;
      this.armRight.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 2.0F * par2 * 0.5F;
      this.armLeft.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
      this.armRight.rotateAngleZ = 0.0F;
      this.armLeft.rotateAngleZ = 0.0F;
      this.legRight.rotateAngleX = Math.max(MathHelper.cos(par1 * 0.6662F) * 1.4F * par2 - 0.27314404F, -0.8F);
      this.legLeft.rotateAngleX = Math.max(MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 1.4F * par2 - 0.27314404F, -0.8F);
      this.legRight.rotateAngleY = 0.0F;
      this.legLeft.rotateAngleY = 0.0F;
      this.skirt1.rotateAngleX = Math.min(this.legRight.rotateAngleX, this.legLeft.rotateAngleX);
      this.skirt2.rotateAngleX = Math.max(Math.max(this.legRight.rotateAngleX, this.legLeft.rotateAngleX), 0.2F);
      if(super.isRiding) {
         this.armRight.rotateAngleX += -0.62831855F;
         this.armLeft.rotateAngleX += -0.62831855F;
         this.legRight.rotateAngleX = -1.2566371F;
         this.legLeft.rotateAngleX = -1.2566371F;
         this.legRight.rotateAngleY = 0.31415927F;
         this.legLeft.rotateAngleY = -0.31415927F;
      }

      this.armRight.rotateAngleY = 0.0F;
      this.armLeft.rotateAngleY = 0.0F;
      this.armRight.rotateAngleZ += MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.armLeft.rotateAngleZ -= MathHelper.cos(par3 * 0.09F) * 0.05F + 0.05F;
      this.armRight.rotateAngleX += MathHelper.sin(par3 * 0.067F) * 0.05F;
      this.armLeft.rotateAngleX -= MathHelper.sin(par3 * 0.067F) * 0.05F;
      EntityLilith entityDemon = (EntityLilith)entity;
      int i = entityDemon.getAttackTimer();
      if(i > 0) {
         float di = 10.0F;
         this.armRight.rotateAngleX = -2.0F + 1.5F * (Math.abs(((float)i - par4) % 10.0F - di * 0.5F) - di * 0.25F) / (di * 0.25F);
      }

   }
}
