package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.model.ModelSpirit;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.util.RenderUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSpirit extends RenderLiving {

   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/spirit.png");


   public RenderSpirit() {
      super(new ModelSpirit(), 0.0F);
   }

   public void doRenderSpirit(EntitySpirit entity, double par2, double par4, double par6, float par8, float par9) {
      GL11.glPushMatrix();
      RenderUtil.blend(true);
      int color = entity.getFeatherColor();
      if(color > 0) {
         float red = (float)(color >> 16 & 255) / 255.0F;
         float green = (float)(color >> 8 & 255) / 255.0F;
         float blue = (float)(color & 255) / 255.0F;
         GL11.glColor4f(red, green, blue, 0.6F);
      } else {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.6F);
      }

      super.doRender(entity, par2, par4, par6, par8, par9);
      RenderUtil.blend(false);
      GL11.glPopMatrix();
   }

   protected void rotateSpiritCorpse(EntitySpirit entity, float par2, float par3, float par4) {
      super.rotateCorpse(entity, par2, par3, par4);
   }

   public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderSpirit((EntitySpirit)entity, par2, par4, par6, par8, par9);
   }

   protected void rotateCorpse(EntityLivingBase entity, float par2, float par3, float par4) {
      this.rotateSpiritCorpse((EntitySpirit)entity, par2, par3, par4);
   }

   public void doRender(EntityLivingBase par1, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderSpirit((EntitySpirit)par1, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderSpirit((EntitySpirit)entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.func_110832_a((EntitySpirit)par1Entity);
   }

   protected ResourceLocation func_110832_a(EntitySpirit par1Entity) {
      return TEXTURE_URL;
   }

}
