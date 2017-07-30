package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.model.ModelSpectre;
import com.emoniph.witchery.entity.EntitySpectre;
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
public class RenderSpectre extends RenderLiving {

   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/spectre.png");


   public RenderSpectre() {
      super(new ModelSpectre(true), 0.0F);
   }

   public void doRenderNightmare(EntitySpectre entity, double par2, double par4, double par6, float par8, float par9) {
      GL11.glPushMatrix();
      RenderUtil.blend(true);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, entity.isObscured()?0.15F:0.6F);
      super.doRender(entity, par2, par4, par6, par8, par9);
      RenderUtil.blend(false);
      GL11.glPopMatrix();
   }

   protected void rotateNightmareCorpse(EntitySpectre entity, float par2, float par3, float par4) {
      super.rotateCorpse(entity, par2, par3, par4);
   }

   public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderNightmare((EntitySpectre)entity, par2, par4, par6, par8, par9);
   }

   protected void rotateCorpse(EntityLivingBase entity, float par2, float par3, float par4) {
      this.rotateNightmareCorpse((EntitySpectre)entity, par2, par3, par4);
   }

   public void doRender(EntityLivingBase par1, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderNightmare((EntitySpectre)par1, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderNightmare((EntitySpectre)entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.func_110832_a((EntitySpectre)par1Entity);
   }

   protected ResourceLocation func_110832_a(EntitySpectre par1Entity) {
      return TEXTURE_URL;
   }

}
