package com.emoniph.witchery.client.model;

import com.emoniph.witchery.blocks.BlockFetish;
import com.emoniph.witchery.client.model.ModelBroom;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelFetishTrent extends ModelBase {

   ModelRenderer body;
   ModelRenderer armLeft;
   ModelRenderer armRight;
   ModelRenderer legLeftUpper;
   ModelRenderer legLeftLower;
   ModelRenderer legRightUpper;
   ModelRenderer legRightLower;
   ModelRenderer headdress1;
   ModelRenderer headdress2;
   ModelRenderer headdress3;
   ModelRenderer face;


   public ModelFetishTrent() {
      super.textureWidth = 64;
      super.textureHeight = 64;
      this.body = new ModelRenderer(this, 0, 14);
      this.body.addBox(-3.0F, 0.0F, -3.0F, 6, 9, 6);
      this.body.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.body.setTextureSize(64, 64);
      this.body.mirror = true;
      this.setRotation(this.body, 0.0F, 0.0F, 0.0F);
      this.face = new ModelRenderer(this, 18, 1);
      this.face.addBox(-3.0F, 1.0F, -2.9F, 6, 7, 0);
      this.face.setRotationPoint(0.0F, 12.0F, 0.0F);
      this.face.setTextureSize(64, 64);
      this.face.mirror = true;
      this.setRotation(this.face, 0.0F, 0.0F, 0.0F);
      this.armLeft = new ModelRenderer(this, 0, 0);
      this.armLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2);
      this.armLeft.setRotationPoint(2.0F, 13.0F, 0.0F);
      this.armLeft.setTextureSize(64, 64);
      this.armLeft.mirror = true;
      this.setRotation(this.armLeft, -0.1858931F, 0.0F, -0.7435722F);
      this.armRight = new ModelRenderer(this, 0, 0);
      this.armRight.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2);
      this.armRight.setRotationPoint(-2.0F, 13.0F, 0.0F);
      this.armRight.setTextureSize(64, 64);
      this.armRight.mirror = true;
      this.setRotation(this.armRight, -0.1858931F, 0.0F, 0.8551081F);
      this.legLeftUpper = new ModelRenderer(this, 9, 0);
      this.legLeftUpper.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2);
      this.legLeftUpper.setRotationPoint(2.0F, 18.0F, 0.0F);
      this.legLeftUpper.setTextureSize(64, 64);
      this.legLeftUpper.mirror = true;
      this.setRotation(this.legLeftUpper, -0.1487144F, 0.0F, -0.2602503F);
      this.legLeftLower = new ModelRenderer(this, 11, 8);
      this.legLeftLower.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1);
      this.legLeftLower.setRotationPoint(3.0F, 21.0F, -0.5F);
      this.legLeftLower.setTextureSize(64, 64);
      this.legLeftLower.mirror = true;
      this.setRotation(this.legLeftLower, 0.0743572F, 0.0F, -0.1115358F);
      this.legRightUpper = new ModelRenderer(this, 9, 0);
      this.legRightUpper.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2);
      this.legRightUpper.setRotationPoint(-2.0F, 18.0F, 0.0F);
      this.legRightUpper.setTextureSize(64, 64);
      this.legRightUpper.mirror = true;
      this.setRotation(this.legRightUpper, 0.1858931F, 0.0F, 0.3346075F);
      this.legRightLower = new ModelRenderer(this, 11, 8);
      this.legRightLower.addBox(-0.5F, 0.0F, -0.5F, 1, 3, 1);
      this.legRightLower.setRotationPoint(-3.0F, 21.0F, 0.5F);
      this.legRightLower.setTextureSize(64, 64);
      this.legRightLower.mirror = true;
      this.setRotation(this.legRightLower, 0.1858931F, 0.0F, 0.2230717F);
      this.headdress1 = new ModelRenderer(this, 0, 30);
      this.headdress1.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2);
      this.headdress1.setRotationPoint(0.0F, 13.0F, 1.0F);
      this.headdress1.setTextureSize(64, 64);
      this.headdress1.mirror = true;
      this.setRotation(this.headdress1, 0.1115358F, 0.0F, -2.862753F);
      this.headdress2 = new ModelRenderer(this, 0, 30);
      this.headdress2.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2);
      this.headdress2.setRotationPoint(-1.0F, 13.0F, 0.0F);
      this.headdress2.setTextureSize(64, 64);
      this.headdress2.mirror = true;
      this.setRotation(this.headdress2, 0.3717861F, 0.0F, 2.639681F);
      this.headdress3 = new ModelRenderer(this, 0, 30);
      this.headdress3.addBox(-1.0F, 0.0F, -1.0F, 2, 5, 2);
      this.headdress3.setRotationPoint(-1.0F, 13.0F, 0.0F);
      this.headdress3.setTextureSize(64, 64);
      this.headdress3.mirror = true;
      this.setRotation(this.headdress3, -0.4461433F, 0.0F, 2.862753F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5, BlockFetish.TileEntityFetish tile) {
      super.render(entity, f, f1, f2, f3, f4, f5);
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.body.render(f5);
      this.armLeft.render(f5);
      this.armRight.render(f5);
      this.legLeftUpper.render(f5);
      this.legLeftLower.render(f5);
      this.legRightUpper.render(f5);
      this.legRightLower.render(f5);
      this.headdress1.render(f5);
      this.headdress2.render(f5);
      this.headdress3.render(f5);
      int colorIndex = 9;
      if(tile != null) {
         int color = tile.getColor();
         if(color >= 0 && color <= 15) {
            colorIndex = color;
         }
      }

      GL11.glColor4f(ModelBroom.fleeceColorTable[colorIndex][0], ModelBroom.fleeceColorTable[colorIndex][1], ModelBroom.fleeceColorTable[colorIndex][2], 1.0F);
      this.face.render(f5);
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
