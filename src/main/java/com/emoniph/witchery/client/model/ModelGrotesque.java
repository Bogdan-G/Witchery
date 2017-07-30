package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelGrotesque extends ModelBase {

   public ModelRenderer head;


   public ModelGrotesque() {
      super.textureWidth = 128;
      super.textureHeight = 32;
      this.setTextureOffset("head.face", 0, 0);
      this.setTextureOffset("head.leftHorn", 0, 16);
      this.setTextureOffset("head.rightHorn", 0, 16);
      this.setTextureOffset("head.leftTusk", 4, 16);
      this.setTextureOffset("head.rightTusk", 4, 16);
      this.setTextureOffset("head.snout", 20, 16);
      this.setTextureOffset("head.bottomLip", 8, 16);
      this.head = new ModelRenderer(this, "head");
      this.head.setTextureOffset(0, 0);
      this.head.addBox("face", -4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.head.addBox("leftHorn", 4.0F, -12.0F, -0.5F, 1, 8, 1);
      this.head.addBox("rightHorn", -5.0F, -12.0F, -0.5F, 1, 8, 1);
      this.head.addBox("leftTusk", 1.0F, -4.0F, -5.0F, 1, 2, 1);
      this.head.addBox("bottomLip", -2.0F, -2.0F, -6.0F, 4, 1, 2);
      this.head.addBox("snout", -1.0F, -6.0F, -6.0F, 2, 3, 2);
      this.head.addBox("rightTusk", -2.0F, -4.0F, -5.0F, 1, 2, 1);
      this.head.setRotationPoint(0.0F, -9.0F, 0.0F);
      this.head.setTextureSize(128, 32);
      this.head.mirror = true;
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      GL11.glTranslatef(0.0F, 0.735F, 0.0F);
      float scale = 1.3F;
      GL11.glScalef(scale, scale, scale);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.head.render(f5);
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
      this.head.rotateAngleY = par4 / 57.295776F;
      this.head.rotateAngleX = par5 / 57.295776F;
   }
}
