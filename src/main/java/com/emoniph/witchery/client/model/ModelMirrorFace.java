package com.emoniph.witchery.client.model;

import com.emoniph.witchery.util.RenderUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelMirrorFace extends ModelBase {

   public ModelRenderer bipedHead;


   public ModelMirrorFace() {
      this(0.0F);
   }

   public ModelMirrorFace(float p_i1148_1_) {
      this(p_i1148_1_, 0.0F, 64, 32);
   }

   public ModelMirrorFace(float p_i1149_1_, float p_i1149_2_, int p_i1149_3_, int p_i1149_4_) {
      super.textureWidth = p_i1149_3_;
      super.textureHeight = p_i1149_4_;
      this.bipedHead = new ModelRenderer(this, 0, 0);
      this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, p_i1149_1_);
      this.bipedHead.setRotationPoint(0.0F, 24.0F + p_i1149_2_, 0.0F);
   }

   public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
      this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
      this.bipedHead.setRotationPoint(0.0F, 24.0F, 0.0F);
      float scale = 0.75F;
      GL11.glScalef(scale, scale, scale);
      GL11.glTranslatef(0.0F, 0.4F, 0.0F);
      RenderUtil.blend(true);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
      this.bipedHead.render(p_78088_7_);
      RenderUtil.blend(false);
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
      this.bipedHead.rotateAngleY = p_78087_4_ / 57.295776F;
      this.bipedHead.rotateAngleX = p_78087_5_ / 57.295776F;
   }
}
