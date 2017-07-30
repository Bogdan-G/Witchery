package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.entity.EntityCorpse;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCorpse extends RenderLiving {

   public RenderCorpse() {
      super(new ModelBiped() {
         public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {}
      }, 0.0F);
   }

   public void doRenderCorpse(EntityCorpse entity, double par2, double par4, double par6, float par8, float par9) {
      GL11.glPushMatrix();
      super.doRender(entity, par2, par4, par6, par8, par9);
      GL11.glPopMatrix();
   }

   protected ResourceLocation getEntityTexture(EntityCorpse entity) {
      return entity.getLocationSkin();
   }

   protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
      GL11.glTranslatef(0.9F, 0.25F, 0.0F);
      GL11.glRotatef(this.getDeathMaxRotation(par1EntityLivingBase), 0.0F, 0.0F, 1.0F);
      GL11.glRotatef(this.getDeathMaxRotation(par1EntityLivingBase), 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);
      if(par1EntityLivingBase.deathTime > 0) {
         float s = ((float)par1EntityLivingBase.deathTime + par4 - 1.0F) / 20.0F * 1.6F;
         s = MathHelper.sqrt_float(s);
         if(s > 1.0F) {
            s = 1.0F;
         }

         GL11.glRotatef(s * this.getDeathMaxRotation(par1EntityLivingBase), 0.0F, 1.0F, 0.0F);
      } else {
         String s1 = EnumChatFormatting.getTextWithoutFormattingCodes(par1EntityLivingBase.getCommandSenderName());
         if((s1.equals("Dinnerbone") || s1.equals("Grumm")) && (!(par1EntityLivingBase instanceof EntityPlayer) || !((EntityPlayer)par1EntityLivingBase).getHideCape())) {
            GL11.glTranslatef(0.0F, par1EntityLivingBase.height + 0.1F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         }
      }

   }

   protected float renderSwingProgress(EntityLivingBase par1EntityLivingBase, float par2) {
      return 0.0F;
   }

   public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderCorpse((EntityCorpse)entity, par2, par4, par6, par8, par9);
   }

   public void doRender(EntityLivingBase par1, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderCorpse((EntityCorpse)par1, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderCorpse((EntityCorpse)entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.getEntityTexture((EntityCorpse)par1Entity);
   }
}
