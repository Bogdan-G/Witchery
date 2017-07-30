package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.entity.EntityIllusion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderIllusion extends RenderLiving {

   private final ResourceLocation textures;


   public RenderIllusion(ModelBase model, ResourceLocation resource) {
      super(model, 0.5F);
      super.shadowSize = 0.0F;
      this.textures = resource;
   }

   public void renderLivingIllusion(EntityIllusion illusionEntity, double par2, double par4, double par6, float par8, float par9) {
      if(Minecraft.getMinecraft().thePlayer.getCommandSenderName().equals(illusionEntity.getVictimName())) {
         super.doRender(illusionEntity, par2, par4, par6, par8, par9);
      }

   }

   protected void preRenderIllusion(EntityIllusion illusionEntity, float par2) {
      super.preRenderCallback(illusionEntity, par2);
   }

   public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderLivingIllusion((EntityIllusion)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2) {
      this.preRenderIllusion((EntityIllusion)par1EntityLivingBase, par2);
   }

   public void doRender(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9) {
      this.renderLivingIllusion((EntityIllusion)par1EntityLivingBase, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.textures;
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderLivingIllusion((EntityIllusion)par1Entity, par2, par4, par6, par8, par9);
   }
}
