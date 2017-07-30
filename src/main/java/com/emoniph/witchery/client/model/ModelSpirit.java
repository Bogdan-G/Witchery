package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelSpirit extends ModelBase {

   ModelRenderer Piece1;


   public ModelSpirit() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.setTextureOffset("Piece1.Shape1", 2, 5);
      this.setTextureOffset("Piece1.Shape2", 2, 21);
      this.setTextureOffset("Piece1.Shape3", 0, 12);
      this.setTextureOffset("Piece1.Shape4", 6, 0);
      this.setTextureOffset("Piece1.Shape5", 6, 28);
      this.Piece1 = new ModelRenderer(this, "Piece1");
      this.Piece1.setRotationPoint(0.0F, 21.0F, 0.0F);
      this.setRotation(this.Piece1, 0.0F, 0.0F, 0.0F);
      this.Piece1.mirror = true;
      this.Piece1.addBox("Shape1", -2.5F, -2.0F, -2.5F, 5, 1, 5);
      this.Piece1.addBox("Shape2", -2.5F, 1.0F, -2.5F, 5, 1, 5);
      this.Piece1.addBox("Shape3", -3.0F, -1.0F, -3.0F, 6, 2, 6);
      this.Piece1.addBox("Shape4", -1.5F, -3.0F, -1.5F, 3, 1, 3);
      this.Piece1.addBox("Shape5", -1.5F, 2.0F, -1.5F, 3, 1, 3);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      float SCALE = 0.5F;
      GL11.glTranslatef(0.0F, 0.65F, 0.0F);
      GL11.glScalef(SCALE, SCALE, SCALE);
      this.Piece1.render(f5);
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
