package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.model.ModelDeath;
import com.emoniph.witchery.entity.EntityDeath;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderDeath extends RenderLiving {

   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/death.png");


   public RenderDeath() {
      super(new ModelDeath(), 0.5F);
   }

   public void doRenderDeath(EntityDeath entity, double par2, double par4, double par6, float par8, float par9) {
      BossStatus.setBossStatus(entity, true);
      super.doRender(entity, par2, par4, par6, par8, par9);
   }

   protected void rotateDeathCorpse(EntityDeath entity, float par2, float par3, float par4) {
      super.rotateCorpse(entity, par2, par3, par4);
   }

   public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderDeath((EntityDeath)entity, par2, par4, par6, par8, par9);
   }

   protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
      this.rotateDeathCorpse((EntityDeath)par1EntityLivingBase, par2, par3, par4);
   }

   public void doRender(EntityLivingBase par1, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderDeath((EntityDeath)par1, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderDeath((EntityDeath)entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.func_110832_a((EntityDeath)par1Entity);
   }

   protected ResourceLocation func_110832_a(EntityDeath par1Entity) {
      return TEXTURE_URL;
   }

}
