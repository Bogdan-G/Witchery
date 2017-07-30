package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.IHandleRenderLiving;
import com.emoniph.witchery.brewing.potions.ModelOverlayRenderer;
import com.emoniph.witchery.brewing.potions.PotionBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.lwjgl.opengl.GL11;

public class PotionWrappedInVine extends PotionBase implements IHandleLivingHurt, IHandleRenderLiving {

   @SideOnly(Side.CLIENT)
   private static ResourceLocation TEXTURE;


   public PotionWrappedInVine(int id, int color) {
      super(id, true, color);
   }

   public void postContructInitialize() {
      this.setIncurable();
   }

   public boolean handleAllHurtEvents() {
      return false;
   }

   public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
      if(!event.entity.worldObj.isRemote && event.source.isFireDamage()) {
         event.ammount *= (float)Math.min(amplifier + 1, 4);
      }

   }

   @SideOnly(Side.CLIENT)
   public void onLivingRender(World world, EntityLivingBase entity, Post event, int amplifier) {
      if(TEXTURE == null) {
         TEXTURE = new ResourceLocation("witchery", "textures/entities/vine_overlay.png");
      }

      GL11.glPushMatrix();
      Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
      ModelOverlayRenderer.render(entity, event.x, event.y, event.z, event.renderer);
      GL11.glPopMatrix();
   }
}
