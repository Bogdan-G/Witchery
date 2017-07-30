package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.World;

public class EntityAIZombieMateNow extends EntityAIBase {

   private EntityZombie zombieObj;
   private EntityZombie mate;
   private World worldObj;
   private int matingTimeout;
   private boolean mating;
   private boolean begin;


   public EntityAIZombieMateNow(EntityZombie zombie) {
      this.zombieObj = zombie;
      this.worldObj = zombie.worldObj;
      this.setMutexBits(3);
   }

   public void beginMating() {
      this.begin = true;
   }

   public boolean shouldExecute() {
      if(!this.begin) {
         return false;
      } else {
         EntityZombie zombie = (EntityZombie)EntityUtil.findNearestEntityWithinAABB(this.worldObj, EntityZombie.class, this.zombieObj.boundingBox.expand(8.0D, 3.0D, 8.0D), this.zombieObj);
         if(zombie != null && !zombie.isChild()) {
            this.mate = zombie;
            return true;
         } else {
            return false;
         }
      }
   }

   public void startExecuting() {
      this.matingTimeout = 600;
      this.mating = true;
      this.begin = false;
   }

   public void resetTask() {
      this.mate = null;
      this.mating = false;
      this.begin = false;
   }

   public boolean continueExecuting() {
      boolean keepGoing = this.matingTimeout >= 0;
      return keepGoing;
   }

   public void updateTask() {
      --this.matingTimeout;
      this.zombieObj.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
      if(this.zombieObj.getDistanceSqToEntity(this.mate) > 2.25D) {
         this.zombieObj.getNavigator().tryMoveToEntityLiving(this.mate, 1.4D);
      } else if(this.matingTimeout == 0 && this.mating) {
         this.giveBirth();
      }

   }

   private void giveBirth() {
      ParticleEffect.HEART.send(SoundEffect.NONE, this.mate, 1.0D, 2.0D, 8);
      this.zombieObj.setVillager(true);
      this.mate.setVillager(true);
      EntityZombie baby = new EntityZombie(this.worldObj);
      baby.setLocationAndAngles(this.mate.posX, this.mate.posY, this.mate.posZ, 0.0F, 0.0F);
      baby.setChild(true);
      this.worldObj.spawnEntityInWorld(baby);
   }
}
