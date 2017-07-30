package com.emoniph.witchery.client.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelDarkMark extends ModelBase {

   private ModelRenderer skull;


   public ModelDarkMark() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.setTextureOffset("skull.top", 0, 0);
      this.setTextureOffset("skull.bottom", 0, 29);
      this.skull = new ModelRenderer(this, "skull");
      this.skull.setRotationPoint(0.0F, 20.0F, 0.0F);
      this.setRotation(this.skull, 0.0F, 0.0F, 0.0F);
      this.skull.mirror = true;
      this.skull.addBox("top", -8.0F, -12.0F, -8.0F, 16, 12, 16);
      this.skull.addBox("bottom", -5.0F, 0.0F, -8.0F, 10, 4, 12);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      GL11.glTranslatef(0.0F, -1.0F, 0.0F);
      this.skull.render(f5);
   }

   private void setRotation(ModelRenderer model, float x, float y, float z) {
      model.rotateAngleX = x;
      model.rotateAngleY = y;
      model.rotateAngleZ = z;
   }

   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
      this.skull.rotateAngleY = par4 / 57.295776F;
      this.skull.rotateAngleX = par5 / 57.295776F;
   }
}
