package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelBrewBottle extends ModelBase {

   private ModelRenderer Bottle;
   private ModelRenderer Stopper;


   public ModelBrewBottle() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.setTextureOffset("Bottle.BodyOuter", 0, 14);
      this.setTextureOffset("Bottle.BodyInner", 2, 8);
      this.setTextureOffset("Bottle.Neck", 4, 4);
      this.setTextureOffset("Bottle.Stopper", 2, 0);
      this.Bottle = new ModelRenderer(this, "Bottle");
      this.Bottle.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.setRotation(this.Bottle, 0.0F, 0.0F, 0.0F);
      this.Bottle.mirror = true;
      this.Bottle.addBox("BodyOuter", -1.5F, -2.0F, -1.5F, 3, 2, 3);
      this.Bottle.addBox("BodyInner", -1.0F, -2.5F, -1.0F, 2, 3, 2);
      this.Bottle.addBox("Neck", -0.5F, -4.0F, -0.5F, 1, 2, 1);
      this.Stopper = new ModelRenderer(this, 2, 0);
      this.Stopper.addBox(0.0F, 0.0F, 0.0F, 2, 1, 2);
      this.Stopper.setTextureSize(32, 32);
      this.Stopper.setRotationPoint(-1.0F, -4.5F, -1.0F);
      this.setRotation(this.Stopper, 0.0F, 0.0F, 0.0F);
      this.Stopper.mirror = true;
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.Bottle.render(f5);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      this.Stopper.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
      super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
   }
}
