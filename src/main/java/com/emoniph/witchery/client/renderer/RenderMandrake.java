package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.entity.EntityMandrake;
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
public class RenderMandrake extends RenderLiving {

   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/mandrake.png");


   public RenderMandrake(ModelBase par1ModelBase, float par2) {
      super(par1ModelBase, par2);
   }

   public void doRenderMandrake(EntityMandrake entity, double par2, double par4, double par6, float par8, float par9) {
      super.doRender(entity, par2, par4, par6, par8, par9);
   }

   protected void rotateMandrakeCorpse(EntityMandrake entity, float par2, float par3, float par4) {
      super.rotateCorpse(entity, par2, par3, par4);
      if((double)entity.limbSwingAmount >= 0.01D) {
         float f3 = 13.0F;
         float f4 = entity.limbSwing - entity.limbSwingAmount * (1.0F - par4) + 6.0F;
         float f5 = (Math.abs(f4 % f3 - f3 * 0.5F) - f3 * 0.25F) / (f3 * 0.25F);
         GL11.glRotatef(6.5F * f5, 0.0F, 0.0F, 1.0F);
      }

   }

   public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderMandrake((EntityMandrake)entity, par2, par4, par6, par8, par9);
   }

   protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
      this.rotateMandrakeCorpse((EntityMandrake)par1EntityLivingBase, par2, par3, par4);
   }

   public void doRender(EntityLivingBase par1, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderMandrake((EntityMandrake)par1, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderMandrake((EntityMandrake)entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.func_110832_a((EntityMandrake)par1Entity);
   }

   protected ResourceLocation func_110832_a(EntityMandrake par1Entity) {
      return TEXTURE_URL;
   }

}
