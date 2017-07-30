package com.emoniph.witchery.client.model;

import com.emoniph.witchery.blocks.BlockChalice;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelChalice extends ModelBase {

   ModelRenderer chalice;
   ModelRenderer liquid;


   public ModelChalice() {
      super.textureWidth = 32;
      super.textureHeight = 32;
      this.setTextureOffset("chalice.sideRight", 0, -5);
      this.setTextureOffset("chalice.sideLeft", 0, -5);
      this.setTextureOffset("chalice.sideBack", 0, 0);
      this.setTextureOffset("chalice.sideFront", 0, 0);
      this.setTextureOffset("chalice.sideBottom", -5, 4);
      this.setTextureOffset("chalice.neck", 4, 10);
      this.setTextureOffset("chalice.base", 0, 13);
      this.chalice = new ModelRenderer(this, "chalice");
      this.chalice.setRotationPoint(-1.0F, 23.0F, -1.0F);
      this.setRotation(this.chalice, 0.0F, 0.0F, 0.0F);
      this.chalice.mirror = true;
      this.chalice.addBox("sideRight", 4.0F, -6.0F, -1.0F, 0, 4, 5);
      this.chalice.addBox("sideLeft", -1.0F, -6.0F, -1.0F, 0, 4, 5);
      this.chalice.addBox("sideBack", -1.0F, -6.0F, 4.0F, 5, 4, 0);
      this.chalice.addBox("sideFront", -1.0F, -6.0F, -1.0F, 5, 4, 0);
      this.chalice.addBox("sideBottom", -1.0F, -2.0F, -1.0F, 5, 0, 5);
      this.chalice.addBox("neck", 1.0F, -2.0F, 1.0F, 1, 2, 1);
      this.chalice.addBox("base", 0.0F, 0.0F, 0.0F, 3, 1, 3);
      this.liquid = new ModelRenderer(this, -4, 18);
      this.liquid.addBox(0.0F, 0.0F, 0.0F, 5, 0, 5);
      this.liquid.setRotationPoint(-2.0F, 19.0F, -2.0F);
      this.liquid.setTextureSize(32, 32);
      this.liquid.mirror = true;
      this.setRotation(this.liquid, 0.0F, 0.0F, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, BlockChalice.TileEntityChalice tileEntityChalice) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.chalice.render(f5);
      if(tileEntityChalice != null && tileEntityChalice.isFilled()) {
         this.liquid.render(f5);
      }

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
