package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.IHandleRenderLiving;
import com.emoniph.witchery.brewing.potions.ModelOverlayRenderer;
import com.emoniph.witchery.brewing.potions.PotionBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import org.lwjgl.opengl.GL11;

public class PotionSpiked extends PotionBase implements IHandleLivingUpdate, IHandleRenderLiving {

   @SideOnly(Side.CLIENT)
   private static ResourceLocation TEXTURE;


   public PotionSpiked(int id, int color) {
      super(id, color);
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getTotalWorldTime() % 5L == 3L) {
         List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.boundingBox.expand(0.2D + 0.1D * (double)amplifier, 0.0D, 0.2D + 0.1D * (double)amplifier));
         Iterator i$ = entities.iterator();

         while(i$.hasNext()) {
            EntityLivingBase otherEntity = (EntityLivingBase)i$.next();
            if(otherEntity != entity) {
               otherEntity.attackEntityFrom(DamageSource.cactus, (float)(1 + amplifier));
            }
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void onLivingRender(World world, EntityLivingBase entity, Post event, int amplifier) {
      if(TEXTURE == null) {
         TEXTURE = new ResourceLocation("witchery", "textures/entities/cactus_overlay.png");
      }

      GL11.glPushMatrix();
      Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
      ModelOverlayRenderer.render(entity, event.x, event.y, event.z, event.renderer);
      GL11.glPopMatrix();
   }
}
