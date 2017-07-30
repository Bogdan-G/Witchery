package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelTreefyd extends ModelBase {

   ModelRenderer head;
   ModelRenderer body;
   ModelRenderer ShapeV;
   ModelRenderer leavesH;
   ModelRenderer base;
   ModelRenderer leg3;
   ModelRenderer leg4;
   ModelRenderer leg1;
   ModelRenderer leg2;


   public ModelTreefyd() {
      super.textureWidth = 64;
      super.textureHeight = 32;
      this.setTextureOffset("head.face", 0, 24);
      this.setTextureOffset("head.petals", 0, 0);
      this.setTextureOffset("head.tongue", 25, 18);
      this.head = new ModelRenderer(this, "head");
      this.head.setRotationPoint(0.0F, 3.0F, 0.0F);
      this.setRotation(this.head, 0.0F, 0.0F, 0.0F);
      this.head.mirror = true;
      this.head.addBox("face", -2.0F, -4.0F, -2.0F, 4, 4, 4);
      this.head.addBox("petals", -5.0F, -7.0F, 0.0F, 10, 10, 0);
      this.head.addBox("tongue", 0.0F, -3.0F, -6.0F, 0, 10, 4);
      this.body = new ModelRenderer(this, 16, 14);
      this.body.addBox(-1.0F, 0.0F, -1.0F, 2, 16, 2);
      this.body.setRotationPoint(0.0F, 3.0F, 0.0F);
      this.body.setTextureSize(64, 32);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.ShapeV = new ModelRenderer(this, 40, 6);
      this.ShapeV.addBox(0.0F, 0.0F, -6.0F, 0, 14, 12);
      this.ShapeV.setRotationPoint(0.0F, 6.0F, 0.0F);
      this.ShapeV.setTextureSize(64, 32);
      this.ShapeV.mirror = true;
      this.setRotation(this.ShapeV, 0.0F, 0.7853982F, 0.0F);
      this.leavesH = new ModelRenderer(this, 40, 0);
      this.leavesH.addBox(-6.0F, 0.0F, 0.0F, 12, 14, 0);
      this.leavesH.setRotationPoint(0.0F, 6.0F, 0.0F);
      this.leavesH.setTextureSize(64, 32);
      this.leavesH.mirror = true;
      this.setRotation(this.leavesH, 0.0F, 0.7853982F, 0.0F);
      this.base = new ModelRenderer(this, 15, 6);
      this.base.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6);
      this.base.setRotationPoint(-3.0F, 19.0F, -3.0F);
      this.base.setTextureSize(64, 32);
      this.base.mirror = true;
      this.setRotation(this.base, 0.0F, 0.0F, 0.0F);
      this.leg3 = new ModelRenderer(this, 0, 16);
      this.leg3.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4);
      this.leg3.setRotationPoint(-2.0F, 20.0F, -4.0F);
      this.leg3.setTextureSize(64, 32);
      this.leg3.mirror = true;
      this.setRotation(this.leg3, 0.0F, 0.0F, 0.0F);
      this.leg4 = new ModelRenderer(this, 0, 16);
      this.leg4.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4);
      this.leg4.setRotationPoint(2.0F, 20.0F, -4.0F);
      this.leg4.setTextureSize(64, 32);
      this.leg4.mirror = true;
      this.setRotation(this.leg4, 0.0F, 0.0F, 0.0F);
      this.leg1 = new ModelRenderer(this, 0, 16);
      this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4);
      this.leg1.setRotationPoint(-2.0F, 20.0F, 4.0F);
      this.leg1.setTextureSize(64, 32);
      this.leg1.mirror = true;
      this.setRotation(this.leg1, 0.0F, 0.0F, 0.0F);
      this.leg2 = new ModelRenderer(this, 0, 16);
      this.leg2.addBox(-2.0F, 0.0F, -2.0F, 4, 4, 4);
      this.leg2.setRotationPoint(2.0F, 20.0F, 4.0F);
      this.leg2.setTextureSize(64, 32);
      this.leg2.mirror = true;
      this.setRotation(this.leg2, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.head.render(f5);
      this.body.render(f5);
      this.ShapeV.render(f5);
      this.leavesH.render(f5);
      this.base.render(f5);
      this.leg3.render(f5);
      this.leg4.render(f5);
      this.leg1.render(f5);
      this.leg2.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity) {
      this.head.rotateAngleY = par4 / 57.295776F;
      this.head.rotateAngleX = par5 / 57.295776F;
      this.leg1.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
      this.leg2.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 1.4F * par2;
      this.leg3.rotateAngleX = MathHelper.cos(par1 * 0.6662F + 3.1415927F) * 1.4F * par2;
      this.leg4.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
   }
}
