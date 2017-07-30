package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.IHandleRenderLiving;
import com.emoniph.witchery.brewing.potions.ModelOverlayRenderer;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.client.model.ModelGrotesque;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.ritual.rites.RiteProtectionCircleRepulsive;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import org.lwjgl.opengl.GL11;

public class PotionGrotesque extends PotionBase implements IHandleLivingUpdate, IHandleRenderLiving {

   @SideOnly(Side.CLIENT)
   private static ModelGrotesque DEMON_HEAD_MODEL;
   @SideOnly(Side.CLIENT)
   private static ResourceLocation TEXTURE;


   public PotionGrotesque(int id, int color) {
      super(id, color);
   }

   @SideOnly(Side.CLIENT)
   public void onLivingRender(World world, EntityLivingBase entity, Post event, int amplifier) {
      if(DEMON_HEAD_MODEL == null) {
         DEMON_HEAD_MODEL = new ModelGrotesque();
      }

      if(TEXTURE == null) {
         TEXTURE = new ResourceLocation("witchery", "textures/entities/Demon.png");
      }

      GL11.glPushMatrix();
      Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
      ModelOverlayRenderer.renderModel(event.entity, event.x, event.y, event.z, event.renderer, DEMON_HEAD_MODEL);
      GL11.glPopMatrix();
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getTotalWorldTime() % 5L == 3L) {
         float radius = 4.0F;
         float radiusSq = 16.0F;
         AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(entity.posX - 4.0D, entity.posY - 4.0D, entity.posZ - 4.0D, entity.posX + 4.0D, entity.posY + 4.0D, entity.posZ + 4.0D);
         List list = world.getEntitiesWithinAABB(EntityLiving.class, bounds);
         Iterator i$ = list.iterator();

         while(i$.hasNext()) {
            EntityLiving victim = (EntityLiving)i$.next();
            boolean canApply = entity != victim && !(victim instanceof EntityDemon) && !(victim instanceof IBossDisplayData) && !(victim instanceof EntityGolem);
            if(canApply && victim.getDistanceSqToEntity(entity) < 16.0D) {
               RiteProtectionCircleRepulsive.push(entity.worldObj, victim, entity.posX, entity.posY, entity.posZ);
            }
         }
      }

   }
}
