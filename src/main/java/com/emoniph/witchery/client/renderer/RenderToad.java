package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.entity.EntityToad;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderToad extends RenderLiving {

   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/toad.png");
   public static final float[][] fleeceColorTable = new float[][]{{1.0F, 1.0F, 1.0F}, {0.85F, 0.5F, 0.2F}, {0.7F, 0.3F, 0.85F}, {0.4F, 0.6F, 0.85F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.5F, 0.65F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.5F, 0.6F}, {0.5F, 0.25F, 0.7F}, {0.2F, 0.3F, 0.7F}, {0.4F, 0.3F, 0.2F}, {0.4F, 0.5F, 0.2F}, {0.6F, 0.2F, 0.2F}, {0.1F, 0.1F, 0.1F}};


   public RenderToad(ModelBase par1ModelBase, float par2) {
      super(par1ModelBase, par2);
      super.shadowSize = 0.3F;
   }

   public void doRenderToad(EntityToad entity, double par2, double par4, double par6, float par8, float par9) {
      float f1 = 1.0F;
      int j = entity.getSkinColor();
      if(j > 0) {
         float alpha = 0.84313726F;
         float bR = 0.065205686F;
         float bG = 0.04921184F;
         float bB = 0.038139176F;
         GL11.glColor3f(f1 * fleeceColorTable[j][0] + 0.065205686F, f1 * fleeceColorTable[j][1] + 0.04921184F, f1 * fleeceColorTable[j][2] + 0.038139176F);
      }

      super.doRender(entity, par2, par4, par6, par8, par9);
   }

   protected void rotateToadCorpse(EntityToad entity, float par2, float par3, float par4) {
      super.rotateCorpse(entity, par2, par3, par4);
   }

   public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderToad((EntityToad)entity, par2, par4, par6, par8, par9);
   }

   protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
      this.rotateToadCorpse((EntityToad)par1EntityLivingBase, par2, par3, par4);
   }

   public void doRender(EntityLivingBase par1, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderToad((EntityToad)par1, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderToad((EntityToad)entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return TEXTURE_URL;
   }

}
