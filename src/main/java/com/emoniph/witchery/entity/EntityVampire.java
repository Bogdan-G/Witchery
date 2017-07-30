package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBloodCrucible;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.ExtendedVillager;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityVampire extends EntityCreature implements IEntitySelector, IHandleDT {

   private Village villageObj;
   private ChunkCoordinates coffinPos = new ChunkCoordinates(0, 0, 0);
   float damageDone = 0.0F;


   public EntityVampire(World world) {
      super(world);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setBreakDoors(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIRestrictSun(this));
      super.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
      super.tasks.addTask(8, new EntityAIRestrictOpenDoor(this));
      super.tasks.addTask(8, new EntityAIAttackOnCollide(this, EntityLivingBase.class, 1.2D, false));
      super.tasks.addTask(9, new EntityAIOpenDoor(this, true));
      super.tasks.addTask(10, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(12, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityCreature.class, 0, false, true, this));
      super.experienceValue = 20;
   }

   public boolean isEntityApplicable(Entity entity) {
      return entity instanceof EntityVillager && this.villageObj != null || entity instanceof EntityPlayer && !ExtendedPlayer.get((EntityPlayer)entity).isVampire();
   }

   protected void updateAITick() {
      super.updateAITick();
      if(!super.worldObj.isRemote) {
         if(super.worldObj.isDaytime()) {
            if(this.getAITarget() == null) {
               this.setAttackTarget((EntityLivingBase)null);
            }

            if(super.ticksExisted % 100 == 2) {
               this.villageObj = null;
               this.damageDone = 0.0F;
               if(this.getDistanceSq((double)this.coffinPos.posX, (double)this.coffinPos.posY, (double)this.coffinPos.posZ) > 16.0D) {
                  ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, this, 0.8D, 1.5D, 16);
                  EntityUtil.moveToBlockPositionAndUpdate(this, this.coffinPos.posX, this.coffinPos.posY, this.coffinPos.posZ, 8);
                  ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, this, 0.8D, 1.5D, 16);
                  this.setHomeArea(this.coffinPos.posX, this.coffinPos.posY, this.coffinPos.posZ, 4);
               }
            }

            if(super.ticksExisted % 20 == 2 && CreatureUtil.isInSunlight(this)) {
               this.setFire(2);
            }
         } else if(this.damageDone >= 20.0F) {
            if(this.villageObj != null) {
               this.setAttackTarget((EntityLivingBase)null);
               this.setRevengeTarget((EntityLivingBase)null);
               this.villageObj = null;
               ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, this, 0.8D, 1.5D, 16);
               EntityUtil.moveToBlockPositionAndUpdate(this, this.coffinPos.posX, this.coffinPos.posY, this.coffinPos.posZ, 8);
               ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, this, 0.8D, 1.5D, 16);
               this.setHomeArea(this.coffinPos.posX, this.coffinPos.posY, this.coffinPos.posZ, 4);
               this.tryFillBloodCrucible();
            }
         } else if(this.villageObj == null && super.ticksExisted % 500 == 2) {
            this.villageObj = super.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY), MathHelper.floor_double(super.posZ), 128);
            if(this.villageObj != null) {
               ChunkCoordinates townPos = this.villageObj.getCenter();
               ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, this, 0.8D, 1.5D, 16);
               EntityUtil.moveToBlockPositionAndUpdate(this, townPos.posX, townPos.posY, townPos.posZ, 8);
               ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, this, 0.8D, 1.5D, 16);
               this.setHomeArea(townPos.posX, townPos.posY, townPos.posZ, this.villageObj.getVillageRadius());
            }
         }
      }

   }

   public void tryFillBloodCrucible() {
      boolean r = true;

      for(int x = this.coffinPos.posX - 6; x <= this.coffinPos.posX + 6; ++x) {
         for(int z = this.coffinPos.posZ - 6; z <= this.coffinPos.posZ + 6; ++z) {
            for(int y = this.coffinPos.posY - 6; y <= this.coffinPos.posY + 6; ++y) {
               if(super.worldObj.getBlock(x, y, z) == Witchery.Blocks.BLOOD_CRUCIBLE) {
                  BlockBloodCrucible.TileEntityBloodCrucible crucible = (BlockBloodCrucible.TileEntityBloodCrucible)BlockUtil.getTileEntity(super.worldObj, x, y, z, BlockBloodCrucible.TileEntityBloodCrucible.class);
                  if(crucible != null) {
                     crucible.increaseBloodLevel();
                  }

                  return;
               }
            }
         }
      }

   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   public void setStalkingArea(int p_110171_1_, int p_110171_2_, int p_110171_3_) {
      this.coffinPos.set(p_110171_1_, p_110171_2_, p_110171_3_);
   }

   protected String getSwimSound() {
      return "game.hostile.swim";
   }

   protected String getSplashSound() {
      return "game.hostile.swim.splash";
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(13, new Byte((byte)0));
      super.dataWatcher.addObject(14, new Integer(500));
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected String getLivingSound() {
      return "mob.villager.idle";
   }

   protected String getHurtSound() {
      return "mob.villager.hit";
   }

   protected String getDeathSound() {
      return "mob.villager.death";
   }

   protected float getSoundPitch() {
      return 0.6F;
   }

   public void onLivingUpdate() {
      this.updateArmSwingProgress();
      float f = this.getBrightness(1.0F);
      if(f > 0.5F) {
         super.entityAge += 2;
      }

      super.onLivingUpdate();
   }

   public boolean attackEntityAsMob(Entity entity) {
      float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      int i = 0;
      if(entity instanceof EntityLivingBase) {
         f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)entity);
         i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)entity);
      }

      boolean flag;
      int j;
      if(entity instanceof EntityVillager) {
         ExtendedVillager needsBlood = ExtendedVillager.get((EntityVillager)entity);
         if(needsBlood != null && super.worldObj.rand.nextInt(10) == 0) {
            this.damageDone += 4.0F;
            j = needsBlood.takeBlood(30, this);
            if(j > 0) {
               this.heal(4.0F);
               ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, super.worldObj, entity.posX, entity.posY + (double)entity.height * 0.8D, entity.posZ, 0.5D, 0.2D, 16);
            }
         }

         flag = true;
      } else {
         boolean needsBlood1 = this.damageDone < 20.0F;
         flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
         if(flag) {
            if(i > 0) {
               entity.addVelocity((double)(-MathHelper.sin(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F));
               super.motionX *= 0.6D;
               super.motionZ *= 0.6D;
            }

            j = EnchantmentHelper.getFireAspectModifier(this);
            if(j > 0) {
               entity.setFire(j * 4);
            }

            if(entity instanceof EntityLivingBase) {
               EnchantmentHelper.func_151384_a((EntityLivingBase)entity, this);
            }

            EnchantmentHelper.func_151385_b(this, entity);
         }
      }

      return flag;
   }

   protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
      if(super.attackTime <= 0 && p_70785_2_ < 2.0F && p_70785_1_.boundingBox.maxY > super.boundingBox.minY && p_70785_1_.boundingBox.minY < super.boundingBox.maxY) {
         super.attackTime = 20;
         this.attackEntityAsMob(p_70785_1_);
      }

   }

   protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {}

   public void updateRidden() {
      super.updateRidden();
      if(super.ridingEntity instanceof EntityCreature) {
         EntityCreature entitycreature = (EntityCreature)super.ridingEntity;
         super.renderYawOffset = entitycreature.renderYawOffset;
      }

   }

   protected String func_146067_o(int p_146067_1_) {
      return p_146067_1_ > 4?"game.hostile.hurt.fall.big":"game.hostile.hurt.fall.small";
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      return super.attackEntityFrom(source, damage);
   }

   public float getCapDT(DamageSource source, float damage) {
      return 0.0F;
   }

   public void onDeath(DamageSource source) {
      if(CreatureUtil.checkForVampireDeath(this, source)) {
         super.onDeath(source);
      }
   }

   protected Item getDropItem() {
      return Items.shears;
   }

   protected boolean canDespawn() {
      return false;
   }

   protected void dropRareDrop(int p_70600_1_) {}

   protected void addRandomArmor() {
      this.setCurrentItemOrArmor(1, new ItemStack(Witchery.Items.VAMPIRE_BOOTS));
      boolean male = super.worldObj.rand.nextBoolean();
      if(male) {
         this.setCurrentItemOrArmor(2, new ItemStack(super.worldObj.rand.nextInt(3) == 0?Witchery.Items.VAMPIRE_LEGS_KILT:Witchery.Items.VAMPIRE_LEGS));
         this.setCurrentItemOrArmor(3, new ItemStack(super.worldObj.rand.nextInt(3) == 0?Witchery.Items.VAMPIRE_COAT_CHAIN:Witchery.Items.VAMPIRE_COAT));
      } else {
         this.setCurrentItemOrArmor(2, new ItemStack(super.worldObj.rand.nextInt(4) != 0?Witchery.Items.VAMPIRE_LEGS_KILT:Witchery.Items.VAMPIRE_LEGS));
         this.setCurrentItemOrArmor(3, new ItemStack(super.worldObj.rand.nextInt(3) == 0?Witchery.Items.VAMPIRE_COAT_FEMALE_CHAIN:Witchery.Items.VAMPIRE_COAT_FEMALE));
      }

   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.vampire.name");
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
      p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);
      this.addRandomArmor();
      this.coffinPos.set((int)super.posX, (int)super.posY, (int)super.posZ);
      return p_110161_1_;
   }

   public int getGuardType() {
      return super.dataWatcher.getWatchableObjectByte(13);
   }

   public void setGuardType(int p_82201_1_) {
      super.dataWatcher.updateObject(13, Byte.valueOf((byte)p_82201_1_));
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      if(nbtRoot.hasKey("GuardType", 99)) {
         byte b0 = nbtRoot.getByte("GuardType");
         this.setGuardType(b0);
      }

      this.coffinPos.set(nbtRoot.getInteger("BaseX"), nbtRoot.getInteger("BaseY"), nbtRoot.getInteger("BaseZ"));
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setByte("GuardType", (byte)this.getGuardType());
      ChunkCoordinates home = this.getHomePosition();
      nbtRoot.setInteger("BaseX", this.coffinPos.posX);
      nbtRoot.setInteger("BaseY", this.coffinPos.posY);
      nbtRoot.setInteger("BaseZ", this.coffinPos.posZ);
   }

   public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
      super.setCurrentItemOrArmor(p_70062_1_, p_70062_2_);
   }

   public double getYOffset() {
      return super.getYOffset() - 0.5D;
   }
}
