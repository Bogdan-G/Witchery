package com.emoniph.witchery.entity;

import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.entity.ai.EntityAIFlyerAttackOnCollide;
import com.emoniph.witchery.entity.ai.EntityAIFlyerLand;
import com.emoniph.witchery.entity.ai.EntityAISitAndStay;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityLostSoul extends EntitySpirit {

   private int timeToLive = -1;


   public void setTimeToLive(int i) {
      this.timeToLive = i;
   }

   public boolean isTemp() {
      return this.timeToLive != -1;
   }

   public EntityLostSoul(World world) {
      super(world);
      super.tasks.taskEntries.clear();
      super.targetTasks.taskEntries.clear();
      super.tasks.addTask(1, new EntityAISitAndStay(this));
      super.tasks.addTask(2, new EntityAIFlyerAttackOnCollide(this, 1.0D, true));
      super.tasks.addTask(9, new EntityAIFlyerLand(this, 0.8D, true));
      super.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F, 0.2F));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("FeatherColor", this.getFeatherColor());
      par1NBTTagCompound.setByte("SoulType", (byte)this.getSoulType());
      par1NBTTagCompound.setInteger("SuicideIn", this.timeToLive);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      if(par1NBTTagCompound.hasKey("FeatherColor")) {
         this.setFeatherColor(par1NBTTagCompound.getInteger("FeatherColor"));
      }

      if(par1NBTTagCompound.hasKey("SoulType")) {
         this.setSoulType(par1NBTTagCompound.getByte("SoulType"));
      }

      if(par1NBTTagCompound.hasKey("SuicideIn")) {
         this.timeToLive = par1NBTTagCompound.getInteger("SuicideIn");
      } else {
         this.timeToLive = -1;
      }

   }

   public void setInWeb() {}

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(22, Integer.valueOf(super.worldObj.rand.nextInt(3)));
      switch(this.getSoulType()) {
      case 0:
         this.setFeatherColor(16711680);
         break;
      case 1:
         this.setFeatherColor('\uff00');
         break;
      case 2:
         this.setFeatherColor(255);
      }

   }

   public int getSoulType() {
      return super.dataWatcher.getWatchableObjectInt(22);
   }

   public void setSoulType(int par1) {
      super.dataWatcher.updateObject(22, Integer.valueOf(par1));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
   }

   public boolean attackEntityAsMob(Entity targetEntity) {
      float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      int i = 0;
      if(targetEntity instanceof EntityLivingBase) {
         f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)targetEntity);
         i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)targetEntity);
      }

      DamageSource source = null;
      if(super.worldObj.rand.nextInt(4) == 0) {
         switch(this.getSoulType()) {
         case 0:
            source = DamageSource.inFire;
            break;
         case 1:
            source = DamageSource.causeMobDamage(this);
            break;
         case 2:
            source = DamageSource.magic;
         }
      }

      if(source == null) {
         source = DamageSource.causeMobDamage(this);
      }

      boolean flag = targetEntity.attackEntityFrom(source, f);
      if(flag) {
         if(i > 0) {
            targetEntity.addVelocity((double)(-MathHelper.sin(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F));
            super.motionX *= 0.6D;
            super.motionZ *= 0.6D;
         }

         int j = EnchantmentHelper.getFireAspectModifier(this);
         if(j > 0) {
            targetEntity.setFire(j * 4);
         }

         if(targetEntity instanceof EntityLivingBase) {
            EnchantmentHelper.func_151384_a((EntityLivingBase)targetEntity, this);
         }

         EnchantmentHelper.func_151385_b(this, targetEntity);
      }

      return flag;
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      float MAX_DAMAGE = 15.0F;
      switch(this.getSoulType()) {
      case 0:
         if(source.isFireDamage() || source.isExplosion()) {
            return super.attackEntityFrom(source, Math.min(damage, 15.0F));
         }
         break;
      case 1:
         if(!source.isProjectile() && !source.isMagicDamage() && !source.isFireDamage() && !source.isExplosion() && source != DamageSource.inWall && source != DamageSource.cactus && source != DamageSource.drown && source != DamageSource.wither) {
            return super.attackEntityFrom(source, Math.min(damage, 15.0F));
         }
         break;
      case 2:
         if(source.isMagicDamage()) {
            return super.attackEntityFrom(source, Math.min(damage, 15.0F));
         }
      }

      return false;
   }

   protected void updateAITick() {
      super.updateAITick();
      if(!super.worldObj.isRemote && this.timeToLive != -1 && --this.timeToLive <= 0) {
         this.setDead();
      }

   }

   public void onUpdate() {
      super.onUpdate();
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.lostsoul.name");
   }
}
