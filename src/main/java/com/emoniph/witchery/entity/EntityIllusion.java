package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.network.PacketSound;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public abstract class EntityIllusion extends EntityMob {

   private EntityPlayer victimPlayer = null;


   public EntityIllusion(World world) {
      super(world);
      super.isImmuneToFire = true;
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
      super.tasks.addTask(3, new EntityAIWander(this, 0.8D));
      super.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(5, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.illusion.name");
   }

   protected SoundEffect getFakeLivingSound() {
      return SoundEffect.NONE;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(0.0D);
   }

   public boolean isAIEnabled() {
      return true;
   }

   public int getTalkInterval() {
      return super.getTalkInterval() * 2;
   }

   public EntityLivingBase getAttackTarget() {
      return super.worldObj.getPlayerEntityByName(this.getVictimName());
   }

   public int getMaxSafePointTries() {
      return this.getAttackTarget() == null?3:3 + (int)(this.getHealth() - 1.0F);
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(17, "");
      super.dataWatcher.addObject(18, Byte.valueOf((byte)0));
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      if(this.getVictimName() == null) {
         par1NBTTagCompound.setString("Victim", "");
      } else {
         par1NBTTagCompound.setString("Victim", this.getVictimName());
      }

      par1NBTTagCompound.setInteger("IllusionType", this.getIllusionType());
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      String s = par1NBTTagCompound.getString("Victim");
      if(s.length() > 0) {
         this.setVictim(s);
      }

      this.setIllusionType(par1NBTTagCompound.getInteger("IllusionType"));
   }

   public String getVictimName() {
      return super.dataWatcher.getWatchableObjectString(17);
   }

   public void setVictim(String par1Str) {
      super.dataWatcher.updateObject(17, par1Str);
   }

   public int getIllusionType() {
      return super.dataWatcher.getWatchableObjectByte(18);
   }

   public void setIllusionType(int par1) {
      super.dataWatcher.updateObject(18, Byte.valueOf((byte)par1));
   }

   public void onUpdate() {
      super.onUpdate();
      if(!super.worldObj.isRemote) {
         if(super.worldObj.rand.nextInt(15) == 0) {
            float sound = this.getHealth() - 1.0F;
            if((double)sound <= 0.5D) {
               this.setDead();
            } else {
               this.setHealth(sound);
            }
         }

         if(super.worldObj.rand.nextInt(40) == 0) {
            SoundEffect sound1 = this.getFakeLivingSound();
            if(this.victimPlayer == null) {
               this.victimPlayer = super.worldObj.getPlayerEntityByName(this.getVictimName());
            }

            if(this.victimPlayer != null && sound1 != null && sound1 != SoundEffect.NONE && this.victimPlayer.getDistanceSqToEntity(this) < 64.0D) {
               Witchery.packetPipeline.sendTo((IMessage)(new PacketSound(sound1, this, 1.0F, 1.0F)), this.victimPlayer);
            }
         }
      }

   }

   protected String getLivingSound() {
      return null;
   }

   protected String getHurtSound() {
      return null;
   }

   protected String getDeathSound() {
      return null;
   }

   public boolean attackEntityAsMob(Entity entity) {
      return true;
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      return false;
   }

   protected void dropFewItems(boolean par1, int par2) {}
}
