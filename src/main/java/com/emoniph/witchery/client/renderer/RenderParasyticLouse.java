package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.model.ModelLouse;
import com.emoniph.witchery.entity.EntityParasyticLouse;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderParasyticLouse extends RenderLiving {

   private static final ResourceLocation LouseTextures = new ResourceLocation("witchery", "textures/entities/louse.png");


   public RenderParasyticLouse() {
      super(new ModelLouse(), 0.3F);
   }

   protected float getLouseDeathRotation(EntityParasyticLouse par1EntityLouse) {
      return 180.0F;
   }

   public void renderLouse(EntityParasyticLouse par1EntityLouse, double par2, double par4, double par6, float par8, float par9) {
      super.doRender(par1EntityLouse, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getLouseTextures(EntityParasyticLouse par1EntityLouse) {
      return LouseTextures;
   }

   protected int shouldLouseRenderPass(EntityParasyticLouse par1EntityLouse, int par2, float par3) {
      return -1;
   }

   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderLouse((EntityParasyticLouse)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   protected float getDeathMaxRotation(EntityLivingBase par1EntityLivingBase) {
      return this.getLouseDeathRotation((EntityParasyticLouse)par1EntityLivingBase);
   }

   protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3) {
      return this.shouldLouseRenderPass((EntityParasyticLouse)par1EntityLivingBase, par2, par3);
   }

   public void renderPlayer(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9) {
      this.renderLouse((EntityParasyticLouse)par1EntityLivingBase, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.getLouseTextures((EntityParasyticLouse)par1Entity);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderLouse((EntityParasyticLouse)par1Entity, par2, par4, par6, par8, par9);
   }

}
