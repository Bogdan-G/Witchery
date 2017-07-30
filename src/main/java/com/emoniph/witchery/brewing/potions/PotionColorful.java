package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.IHandlePreRenderLiving;
import com.emoniph.witchery.brewing.potions.IHandleRenderLiving;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.Dye;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import org.lwjgl.opengl.GL11;

public class PotionColorful extends PotionBase implements IHandlePreRenderLiving, IHandleRenderLiving {

   public PotionColorful(int id, int color) {
      super(id, true, color);
      this.setIncurable();
      this.hideInventoryText();
   }

   @SideOnly(Side.CLIENT)
   public void onLivingRender(World world, EntityLivingBase entity, Pre event, int amplifier) {
      GL11.glPushMatrix();
      Dye dye = Dye.DYES[Math.min(amplifier, Dye.DYES.length - 1)];
      float red = (float)(dye.rgb >>> 16 & 255) / 256.0F;
      float green = (float)(dye.rgb >>> 8 & 255) / 256.0F;
      float blue = (float)(dye.rgb & 255) / 256.0F;
      GL11.glColor3f(red, green, blue);
   }

   public void onLivingRender(World world, EntityLivingBase entity, Post event, int amplifier) {
      GL11.glPopMatrix();
   }

   public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
      Dye dye = Dye.DYES[Math.min(effect.getAmplifier(), Dye.DYES.length - 1)];
      String label = Witchery.resource("witchery:color." + dye.unlocalizedName);
      mc.fontRenderer.drawStringWithShadow(label, x + 10 + 18, y + 6, 16777215);
      String duration = Potion.getDurationString(effect);
      mc.fontRenderer.drawStringWithShadow(duration, x + 10 + 18, y + 6 + 10, 8355711);
   }
}
