package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelPoppetChest extends ModelBase {

   ModelRenderer table;


   public ModelPoppetChest() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.table = new ModelRenderer(this, 0, 0);
      this.table.addBox(0.0F, 0.0F, 0.0F, 16, 8, 16);
      this.table.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.table.setTextureSize(64, 64);
      this.table.mirror = true;
      this.setRotation(this.table, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.table.render(f5);
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
