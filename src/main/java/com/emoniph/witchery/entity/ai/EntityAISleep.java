package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.common.ExtendedVillager;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;

public class EntityAISleep extends EntityAIBase {

   private EntityVillager villager;
   private VillageDoorInfo doorInfo;
   private int insidePosX = -1;
   private int insidePosZ = -1;
   private World world;
   Village village;


   public EntityAISleep(EntityVillager villager) {
      this.villager = villager;
      this.world = villager.worldObj;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      long time = this.world.getWorldTime() % 24000L;
      if(time >= 13000L && time < 23999L && this.villager.hurtTime <= 0) {
         if(this.villager.getRNG().nextInt(50) != 0) {
            return false;
         } else {
            int i = MathHelper.floor_double(this.villager.posX);
            int j = MathHelper.floor_double(this.villager.posY);
            int k = MathHelper.floor_double(this.villager.posZ);
            Village village = this.world.villageCollectionObj.findNearestVillage(i, j, k, 14);
            if(village == null) {
               return false;
            } else {
               this.doorInfo = village.findNearestDoorUnrestricted(i, j, k);
               float DOOR_DIST = 4.0F;
               boolean inside = this.villager.getDistanceSq((double)this.doorInfo.getInsidePosX() + 0.5D, (double)this.doorInfo.getInsidePosY(), (double)this.doorInfo.getInsidePosZ() + 0.5D) < 16.0D;
               if(this.villager.worldObj.canBlockSeeTheSky(i, j, k)) {
                  return false;
               } else {
                  int count = 0;

                  int x;
                  int z;
                  for(x = i - 1; x <= i + 1; ++x) {
                     for(z = k - 1; z <= k + 1; ++z) {
                        if(!this.villager.worldObj.canBlockSeeTheSky(x, j, z) && this.villager.worldObj.getBlock(x, j + 1, z).getMaterial().isReplaceable()) {
                           ++count;
                        }
                     }
                  }

                  if(count < 4) {
                     return false;
                  } else {
                     boolean var12 = true;
                     count = 0;

                     for(x = -1; x <= 1; ++x) {
                        for(z = -1; z <= 1; ++z) {
                           if(!this.world.getBlock(x + i, j - 1, z + k).isReplaceable(this.world, x + i, j - 1, z + k)) {
                              ++count;
                           }
                        }
                     }

                     return count >= 6;
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {
      ExtendedVillager ext = ExtendedVillager.get(this.villager);
      if(ext != null) {
         ext.setSleeping(true);
      }

   }

   public void resetTask() {
      this.village = null;
      ExtendedVillager ext = ExtendedVillager.get(this.villager);
      if(ext != null) {
         ext.setSleeping(false);
      }

   }

   public boolean continueExecuting() {
      long time = this.world.getWorldTime() % 24000L;
      return time > 13000L && time < 23999L && this.villager.hurtTime == 0;
   }

   public void updateTask() {
      ExtendedVillager ext = ExtendedVillager.get(this.villager);
      if(ext != null) {
         ext.incrementSleepingTicks();
      }

   }
}
