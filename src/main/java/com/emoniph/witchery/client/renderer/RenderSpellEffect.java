package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.particle.NaturePowerFX;
import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffectProjectile;
import com.emoniph.witchery.util.RenderUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSpellEffect extends Render {

   private float field_77002_a;
   private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("witchery", "textures/entities/spelleffect.png");
   private static final ResourceLocation ORB_TEXTURE = NaturePowerFX.particles;


   public RenderSpellEffect(float par1) {
      this.field_77002_a = par1;
   }

   public void doRenderSpellEffect(EntitySpellEffect effectEntity, double par2, double par4, double par6, float par8, float par9) {
      GL11.glPushMatrix();
      super.bindTexture(ORB_TEXTURE);
      GL11.glTranslatef((float)par2, (float)par4, (float)par6);
      RenderUtil.blend(true);
      GL11.glDepthMask(false);
      GL11.glBlendFunc(770, 1);
      float scale = 1.0F;
      int color = 16711680;
      SymbolEffect effect = EffectRegistry.instance().getEffect(effectEntity.getEffectID());
      if(effect != null && effect instanceof SymbolEffectProjectile) {
         SymbolEffectProjectile f2 = (SymbolEffectProjectile)effect;
         color = f2.getColor();
         scale = f2.getSize();
      }

      float age = (float)effectEntity.ticksExisted + par9;
      float pulse = 0.85F + 0.15F * MathHelper.sin(age * 0.3F);
      float f21 = this.field_77002_a * scale * 0.85F * pulse;
      float red = (float)(color >>> 16 & 255) / 255.0F;
      float green = (float)(color >>> 8 & 255) / 255.0F;
      float blue = (float)(color & 255) / 255.0F;

      // First 16px frame of power.png is a soft round glow (tile 0 of a 16x16 atlas grid).
      float u0 = 0.0F;
      float u1 = 0.0624375F;
      float v0 = 0.0F;
      float v1 = 0.0624375F;

      GL11.glRotatef(180.0F - super.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-super.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(age * 4.0F, 0.0F, 0.0F, 1.0F);

      Tessellator tessellator = Tessellator.instance;

      // Outer soft halo
      this.drawOrbQuad(tessellator, f21 * 1.0F, red, green, blue, 0.35F, u0, u1, v0, v1);
      // Bright inner core (counter-rotated for a shimmer effect)
      GL11.glRotatef(-age * 7.0F, 0.0F, 0.0F, 1.0F);
      float coreR = red + (1.0F - red) * 0.5F;
      float coreG = green + (1.0F - green) * 0.5F;
      float coreB = blue + (1.0F - blue) * 0.5F;
      this.drawOrbQuad(tessellator, f21 * 0.55F, coreR, coreG, coreB, 0.7F, u0, u1, v0, v1);

      GL11.glDepthMask(true);
      RenderUtil.blend(false);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   private void drawOrbQuad(Tessellator tessellator, float radius, float red, float green, float blue, float alpha, float u0, float u1, float v0, float v1) {
      GL11.glColor4f(red, green, blue, alpha);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      tessellator.addVertexWithUV((double)(-radius), (double)(-radius), 0.0D, (double)u0, (double)v1);
      tessellator.addVertexWithUV((double)radius, (double)(-radius), 0.0D, (double)u1, (double)v1);
      tessellator.addVertexWithUV((double)radius, (double)radius, 0.0D, (double)u1, (double)v0);
      tessellator.addVertexWithUV((double)(-radius), (double)radius, 0.0D, (double)u0, (double)v0);
      tessellator.draw();
   }

   protected ResourceLocation getSpellEffectTextures(EntitySpellEffect effect) {
      return ORB_TEXTURE;
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.getSpellEffectTextures((EntitySpellEffect)par1Entity);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderSpellEffect((EntitySpellEffect)par1Entity, par2, par4, par6, par8, par9);
   }

}
