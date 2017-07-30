package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffectProjectile;
import com.emoniph.witchery.util.RenderUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSpellEffect extends Render {

   private float field_77002_a;
   private static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation("witchery", "textures/entities/spelleffect.png");


   public RenderSpellEffect(float par1) {
      this.field_77002_a = par1;
   }

   public void doRenderSpellEffect(EntitySpellEffect effectEntity, double par2, double par4, double par6, float par8, float par9) {
      GL11.glPushMatrix();
      this.bindEntityTexture(effectEntity);
      GL11.glTranslatef((float)par2, (float)par4, (float)par6);
      RenderUtil.blend(true);
      float scale = 1.0F;
      int color = 16711680;
      IIcon icon2 = Items.snowball.getIconFromDamage(0);
      SymbolEffect effect = EffectRegistry.instance().getEffect(effectEntity.getEffectID());
      if(effect != null && effect instanceof SymbolEffectProjectile) {
         SymbolEffectProjectile f2 = (SymbolEffectProjectile)effect;
         color = f2.getColor();
         scale = f2.getSize();
      }

      float f21 = this.field_77002_a * scale * 0.65F;
      GL11.glScalef(f21 / 1.0F, f21 / 1.0F, f21 / 1.0F);
      float red = (float)(color >>> 16 & 255) / 256.0F;
      float green = (float)(color >>> 8 & 255) / 256.0F;
      float blue = (float)(color & 255) / 256.0F;
      GL11.glColor4f(red, green, blue, 0.55F);
      Tessellator tessellator = Tessellator.instance;
      float f3 = icon2.getMinU();
      float f4 = icon2.getMaxU();
      float f5 = icon2.getMinV();
      float f6 = icon2.getMaxV();
      float f7 = 1.0F;
      float f8 = 0.5F;
      float f9 = 0.25F;
      GL11.glRotatef(180.0F - super.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-super.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      tessellator.addVertexWithUV((double)(0.0F - f8), (double)(0.0F - f9), 0.0D, (double)f3, (double)f6);
      tessellator.addVertexWithUV((double)(f7 - f8), (double)(0.0F - f9), 0.0D, (double)f4, (double)f6);
      tessellator.addVertexWithUV((double)(f7 - f8), (double)(1.0F - f9), 0.0D, (double)f4, (double)f5);
      tessellator.addVertexWithUV((double)(0.0F - f8), (double)(1.0F - f9), 0.0D, (double)f3, (double)f5);
      tessellator.draw();
      RenderUtil.blend(false);
      GL11.glPopMatrix();
   }

   protected ResourceLocation getSpellEffectTextures(EntitySpellEffect effect) {
      return TextureMap.locationItemsTexture;
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.getSpellEffectTextures((EntitySpellEffect)par1Entity);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderSpellEffect((EntitySpellEffect)par1Entity, par2, par4, par6, par8, par9);
   }

}
