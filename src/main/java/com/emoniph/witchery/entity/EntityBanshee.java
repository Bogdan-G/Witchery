package com.emoniph.witchery.entity;

import com.emoniph.witchery.entity.EntitySummonedUndead;
import com.emoniph.witchery.item.ItemEarmuffs;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.TimeUtil;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityBanshee extends EntitySummonedUndead {

   public EntityBanshee(World par1World) {
      super(par1World);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setAvoidSun(true);
      this.getNavigator().setBreakDoors(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityLivingBase.class, 0.3D, false));
      super.tasks.addTask(3, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(5, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(0.0D);
   }

   protected void entityInit() {
      super.entityInit();
   }

   protected boolean isAIEnabled() {
      return true;
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      boolean startedScreaming = false;
      if(!super.worldObj.isRemote && (TimeUtil.secondsElapsed(5, (long)super.ticksExisted) || this.isScreaming() && TimeUtil.ticksElapsed(20, (long)super.ticksExisted))) {
         double RADIUS = 6.0D;
         double RADIUS_SQ = 36.0D;
         AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(super.posX - 6.0D, super.posY - 6.0D, super.posZ - 6.0D, super.posX + 6.0D, super.posY + 6.0D, super.posZ + 6.0D);
         List players = super.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
         boolean playersFound = false;
         Iterator i$ = players.iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            EntityLivingBase player = (EntityLivingBase)obj;
            if(this.getDistanceSqToEntity(player) <= 36.0D && (player == this.getAttackTarget() || player == super.entityToAttack || player instanceof EntityPlayer)) {
               playersFound = true;
               if(!this.isScreaming()) {
                  this.setScreaming(true);
                  startedScreaming = true;
               }

               if(!(player instanceof EntityPlayer) || !ItemEarmuffs.isHelmWorn((EntityPlayer)player)) {
                  float maxHealth = player.getMaxHealth();
                  EntityUtil.touchOfDeath(player, this, Math.max(0.1F * maxHealth, 1.0F));
               }
            }
         }

         if(!playersFound && this.isScreaming()) {
            this.setScreaming(false);
         }
      }

      if((startedScreaming || TimeUtil.secondsElapsed(3, (long)super.ticksExisted)) && this.isScreaming()) {
         this.playSound("witchery:mob.banshee.banshee_scream", 1.0F, super.worldObj.rand.nextFloat() * 0.3F + 0.7F);
      }

   }

   public void onUpdate() {
      super.onUpdate();
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      boolean flag = super.attackEntityAsMob(par1Entity);
      return flag;
   }

   protected String getLivingSound() {
      return null;
   }

   protected String getHurtSound() {
      return "witchery:mob.spectre.spectre_hit";
   }

   protected String getDeathSound() {
      return "witchery:mob.spectre.spectre_hit";
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.banshee.name");
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      IEntityLivingData par1EntityLivingData1 = super.onSpawnWithEgg(par1EntityLivingData);
      return (IEntityLivingData)par1EntityLivingData1;
   }
}
