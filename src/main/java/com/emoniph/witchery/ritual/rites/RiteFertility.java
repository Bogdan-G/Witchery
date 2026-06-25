package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.ritual.rites.RiteExpandingEffect;
import com.emoniph.witchery.util.Dye;
import com.emoniph.witchery.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class RiteFertility extends RiteExpandingEffect {

   public RiteFertility(int radius, int height) {
      super(radius, height, false);
   }

   public void doBlockAction(World world, int posX, int posY, int posZ, int currentRadius, EntityPlayer player, boolean enhanced) {
      Block blockID = world.getBlock(posX, posY, posZ);
      if((blockID != Blocks.dirt || blockID != Blocks.grass || blockID != Blocks.mycelium || blockID != Blocks.farmland || world.rand.nextInt(5) == 0) && player != null) {
         ItemDye.applyBonemeal(Dye.BONE_MEAL.createStack(), world, posX, posY, posZ, player);
      }

   }

   public boolean doRadiusAction(World world, int posX, int posY, int posZ, int radius, EntityPlayer player, boolean enhanced) {
      double radiusSq = (double)(radius * radius);
      double minSq = (double)Math.max(0, (radius - 1) * (radius - 1));
      Iterator villagersToZombify = world.playerEntities.iterator();

      while(villagersToZombify.hasNext()) {
         Object i$ = villagersToZombify.next();
         EntityPlayer victim = (EntityPlayer)i$;
         double entityvillager = victim.getDistanceSq(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ);
         if(entityvillager > minSq && entityvillager <= radiusSq) {
            if(victim.isPotionActive(Potion.confusion)) {
               victim.removePotionEffect(Potion.confusion.id);
            }

            if(victim.isPotionActive(Potion.blindness)) {
               victim.removePotionEffect(Potion.blindness.id);
            }

            if(victim.isPotionActive(Potion.poison)) {
               victim.removePotionEffect(Potion.poison.id);
            }

            if(enhanced) {
               victim.addPotionEffect(new PotionEffect(Potion.regeneration.id, 300, 1));
               victim.addPotionEffect(new PotionEffect(Potion.field_76443_y.id, 2400));
            }
         }
      }

      ArrayList villagersToZombify1 = new ArrayList();
      Iterator i$1 = world.loadedEntityList.iterator();

      while(i$1.hasNext()) {
         Object victim1 = i$1.next();
         if(victim1 instanceof EntityZombie) {
            EntityZombie entityvillager1 = (EntityZombie)victim1;
            if(entityvillager1.isVillager()) {
               double distanceSq = entityvillager1.getDistanceSq(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ);
               if(distanceSq > minSq && distanceSq <= radiusSq) {
                  Log.instance().debug(String.format("Try curing zombie %f %f %f", new Object[]{Double.valueOf(distanceSq), Double.valueOf(minSq), Double.valueOf(radiusSq)}));
                  villagersToZombify1.add(entityvillager1);
               }
            }
         }
      }

      i$1 = villagersToZombify1.iterator();

      while(i$1.hasNext()) {
         EntityZombie victim2 = (EntityZombie)i$1.next();
         EntityVillager entityvillager2 = new EntityVillager(world);
         entityvillager2.copyLocationAndAnglesFrom(victim2);
         entityvillager2.onSpawnWithEgg((IEntityLivingData)null);
         entityvillager2.setLookingForHome();
         if(victim2.isChild()) {
            entityvillager2.setGrowingAge(-24000);
         }

         world.removeEntity(victim2);
         world.spawnEntityInWorld(entityvillager2);
         entityvillager2.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
         world.playAuxSFXAtEntity((EntityPlayer)null, 1017, (int)entityvillager2.posX, (int)entityvillager2.posY, (int)entityvillager2.posZ, 0);
      }

      return true;
   }
}
