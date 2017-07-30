package com.emoniph.witchery.entity;

import com.emoniph.witchery.ritual.rites.RiteProtectionCircleRepulsive;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.TimeUtil;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityDarkMark extends EntityLiving {

   private long ticksAlive = 0L;


   public EntityDarkMark(World world) {
      super(world);
      super.isImmuneToFire = true;
      this.setSize(2.0F, 2.0F);
      super.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 16.0F));
   }

   protected void entityInit() {
      super.entityInit();
   }

   protected int decreaseAirSupply(int air) {
      return air;
   }

   protected float getSoundVolume() {
      return 0.8F;
   }

   protected float getSoundPitch() {
      return 1.0F;
   }

   public int getTalkInterval() {
      return 80;
   }

   protected String getLivingSound() {
      return "witchery:mob.torment.laugh";
   }

   protected String getHurtSound() {
      return null;
   }

   protected String getDeathSound() {
      return null;
   }

   public boolean canBePushed() {
      return false;
   }

   protected void collideWithEntity(Entity par1Entity) {}

   protected void collideWithNearbyEntities() {}

   protected boolean isAIEnabled() {
      return true;
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      this.ticksAlive = Math.max((long)super.ticksExisted, ++this.ticksAlive);
      if(this.ticksAlive > (long)TimeUtil.minsToTicks(5)) {
         if(!super.worldObj.isRemote) {
            this.setDead();
         }
      } else if(super.worldObj.isRemote && TimeUtil.ticksElapsed(4, this.ticksAlive)) {
         double radius = 10.0D;
         AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(super.posX - 10.0D, 1.0D, super.posZ - 10.0D, super.posX + 10.0D, 255.0D, super.posZ + 10.0D);
         List list = super.worldObj.getEntitiesWithinAABB(EntityCreature.class, bounds);
         Iterator iterator = list.iterator();

         while(iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if(Coord.distance(entity.posX, 1.0D, entity.posZ, super.posX, 1.0D, super.posZ) <= 10.0D) {
               RiteProtectionCircleRepulsive.push(super.worldObj, entity, super.posX, entity.posY, super.posZ);
            }
         }
      }

   }

   public void onUpdate() {
      super.onUpdate();
      super.motionY = 0.0D;
      if(super.worldObj.isRemote) {
         for(int i = 0; i < 5; ++i) {
            super.worldObj.spawnParticle(ParticleEffect.LARGE_SMOKE.toString(), super.posX - 1.4D + super.worldObj.rand.nextDouble() * 2.8D, super.posY + super.worldObj.rand.nextDouble() * 2.0D, super.posZ - 1.4D + super.worldObj.rand.nextDouble() * 2.8D, 0.0D, 0.0D, 0.0D);
         }
      }

   }

   protected void dropFewItems(boolean par1, int par2) {}

   protected boolean canTriggerWalking() {
      return false;
   }

   protected void fall(float par1) {}

   protected void updateFallState(double par1, boolean par3) {}

   public boolean doesEntityNotTriggerPressurePlate() {
      return true;
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      return false;
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      this.ticksAlive = nbtRoot.getLong("WITCTicksAlive");
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setLong("WITCTicksAlive", this.ticksAlive);
   }
}
