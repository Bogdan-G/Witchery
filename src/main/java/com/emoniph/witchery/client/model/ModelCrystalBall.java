package com.emoniph.witchery.client.model;

import com.emoniph.witchery.util.RenderUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelCrystalBall extends ModelBase {

   ModelRenderer baseBottom;
   ModelRenderer baseMiddle;
   ModelRenderer baseTop;
   ModelRenderer globeInner;
   ModelRenderer globeMiddle;
   ModelRenderer globeOuter;


   public ModelCrystalBall() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.baseBottom = new ModelRenderer(this, 0, 25);
      this.baseBottom.addBox(0.0F, 0.0F, 0.0F, 6, 1, 6);
      this.baseBottom.setRotationPoint(-3.0F, 23.0F, -3.0F);
      this.baseBottom.setTextureSize(32, 32);
      this.baseBottom.mirror = true;
      this.setRotation(this.baseBottom, 0.0F, 0.0F, 0.0F);
      this.baseMiddle = new ModelRenderer(this, 0, 20);
      this.baseMiddle.addBox(0.0F, 0.0F, 0.0F, 4, 1, 4);
      this.baseMiddle.setRotationPoint(-2.0F, 22.0F, -2.0F);
      this.baseMiddle.setTextureSize(32, 32);
      this.baseMiddle.mirror = true;
      this.setRotation(this.baseMiddle, 0.0F, 0.0F, 0.0F);
      this.baseTop = new ModelRenderer(this, 0, 17);
      this.baseTop.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
      this.baseTop.setRotationPoint(-1.0F, 21.0F, -1.0F);
      this.baseTop.setTextureSize(32, 32);
      this.baseTop.mirror = true;
      this.setRotation(this.baseTop, 0.0F, 0.0F, 0.0F);
      this.globeInner = new ModelRenderer(this, 4, 0);
      this.globeInner.addBox(0.0F, 0.0F, 0.0F, 2, 2, 2);
      this.globeInner.setRotationPoint(-1.0F, 17.0F, -1.0F);
      this.globeInner.setTextureSize(32, 32);
      this.globeInner.mirror = true;
      this.setRotation(this.globeInner, 0.0F, 0.0F, 0.0F);
      this.globeMiddle = new ModelRenderer(this, 12, 0);
      this.globeMiddle.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4);
      this.globeMiddle.setRotationPoint(-2.0F, 16.0F, -2.0F);
      this.globeMiddle.setTextureSize(32, 32);
      this.globeMiddle.mirror = true;
      this.setRotation(this.globeMiddle, 0.0F, 0.0F, 0.0F);
      this.globeOuter = new ModelRenderer(this, 8, 8);
      this.globeOuter.addBox(0.0F, 0.0F, 0.0F, 6, 6, 6);
      this.globeOuter.setRotationPoint(-3.0F, 15.0F, -3.0F);
      this.globeOuter.setTextureSize(32, 32);
      this.globeOuter.mirror = true;
      this.setRotation(this.globeOuter, 0.0F, 0.0F, 0.0F);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, TileEntity tile) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.baseBottom.render(f5);
      this.baseMiddle.render(f5);
      this.baseTop.render(f5);
      RenderUtil.blend(true);
      if(tile != null && tile.getWorldObj() != null) {
         long time = tile.getWorldObj().getWorldTime();
         long scale = 100L - Math.abs(time % 160L - 80L);
         GL11.glColor3f(0.01F * (float)scale, 0.01F * (float)scale, 0.01F * (float)scale);
      }

      this.globeInner.render(f5);
      GL11.glColor3f(0.8F, 0.8F, 1.0F);
      this.globeMiddle.render(f5);
      this.globeOuter.render(f5);
      RenderUtil.blend(false);
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }
}
