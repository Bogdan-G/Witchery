package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionSpectralSight extends PotionBase implements IHandleLivingUpdate {

   public PotionSpectralSight(int id, int color) {
      super(id, color);
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(entity instanceof EntityPlayer && world.getTotalWorldTime() % 10L == 0L) {
         double range = (double)(6 + 3 * amplifier);
         AxisAlignedBB bounds = entity.boundingBox.expand(range, range, range);
         List nearby = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
         if(nearby != null) {
            for(int i = 0; i < nearby.size(); ++i) {
               EntityLivingBase other = (EntityLivingBase)nearby.get(i);
               if(other != entity && other.isInvisible()) {
                  for(int p = 0; p < 6; ++p) {
                     double ox = other.posX + (world.rand.nextDouble() - 0.5D) * (double)other.width;
                     double oy = other.posY + world.rand.nextDouble() * (double)other.height;
                     double oz = other.posZ + (world.rand.nextDouble() - 0.5D) * (double)other.width;
                     world.spawnParticle("witchMagic", ox, oy, oz, 0.0D, 0.0D, 0.0D);
                  }
               }
            }
         }
      }

   }
}
