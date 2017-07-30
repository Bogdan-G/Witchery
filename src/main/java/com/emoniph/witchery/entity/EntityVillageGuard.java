package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.entity.ai.EntityAIDefendVillageGeneric;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityVillageGuard extends EntityCreature implements IRangedAttackMob, EntityAIDefendVillageGeneric.IVillageGuard, IEntitySelector {

   private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
   private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);
   private int homeCheckTimer;
   Village villageObj;


   public EntityVillageGuard(World world) {
      super(world);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setBreakDoors(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 0.6D, true));
      super.tasks.addTask(7, new EntityAIMoveTowardsRestriction(this, 1.0D));
      super.tasks.addTask(8, new EntityAIRestrictOpenDoor(this));
      super.tasks.addTask(9, new EntityAIOpenDoor(this, true));
      super.tasks.addTask(10, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(12, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIDefendVillageGeneric(this));
      super.targetTasks.addTask(2, new EntityAIHurtByTarget(this, true));
      super.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, false, true, this));
      if(world != null && !world.isRemote) {
         this.setCombatTask();
      }

      super.experienceValue = 5;
   }

   public boolean canAttackClass(Class p_70686_1_) {
      return EntityCreeper.class != p_70686_1_ && this.getClass() != p_70686_1_;
   }

   public boolean isEntityApplicable(Entity entity) {
      if((!(entity instanceof IMob) || entity instanceof EntityWitchHunter) && !(entity instanceof EntityGoblin)) {
         if(this.villageObj != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            EntityLivingBase target = player.getAITarget();
            if(target instanceof EntityPlayer && this.villageObj.getReputationForPlayer(target.getCommandSenderName()) == 10) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public int getBlood() {
      return super.dataWatcher.getWatchableObjectInt(14);
   }

   public void setBlood(int blood) {
      super.dataWatcher.updateObject(14, Integer.valueOf(MathHelper.clamp_int(blood, 0, 500)));
   }

   public int takeBlood(int quantity, EntityLivingBase player) {
      PotionEffect effect = this.getActivePotionEffect(Witchery.Potions.PARALYSED);
      boolean transfixed = effect != null && effect.getAmplifier() >= 4;
      int blood = this.getBlood();
      quantity = (int)Math.ceil((double)(0.66F * (float)quantity));
      int remainder = Math.max(blood - quantity, 0);
      int taken = blood - remainder;
      this.setBlood(remainder);
      if(blood < (int)Math.ceil(250.0D)) {
         this.attackEntityFrom(new EntityDamageSource(DamageSource.magic.getDamageType(), player), 2.0F);
      } else if(!transfixed) {
         this.attackEntityFrom(new EntityDamageSource(DamageSource.magic.getDamageType(), player), 0.5F);
      }

      return taken;
   }

   public void giveBlood(int quantity) {
      int blood = this.getBlood();
      if(blood < 500) {
         this.setBlood(blood + quantity);
      }

   }

   public Village getVillage() {
      return this.villageObj;
   }

   public EntityCreature getCreature() {
      return this;
   }

   protected void updateAITick() {
      if(--this.homeCheckTimer <= 0) {
         this.homeCheckTimer = 70 + super.rand.nextInt(50);
         this.villageObj = super.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY), MathHelper.floor_double(super.posZ), 32);
         if(this.villageObj == null) {
            this.detachHome();
         } else {
            ChunkCoordinates chunkcoordinates = this.villageObj.getCenter();
            this.setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, (int)((float)this.villageObj.getVillageRadius() * 1.5F));
            if(this.getAttackTarget() == null) {
               this.heal(1.0F);
               if(super.worldObj.rand.nextInt(4) == 0) {
                  this.giveBlood(1);
               }
            }
         }
      }

      super.updateAITick();
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
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
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
      return 0.8F;
   }

   public void onLivingUpdate() {
      this.updateArmSwingProgress();
      float f = this.getBrightness(1.0F);
      if(f > 0.5F) {
         super.entityAge += 2;
      }

      super.onLivingUpdate();
   }

   public boolean attackEntityFrom(DamageSource damageSource, float damage) {
      return damageSource.getEntity() != null && (damageSource.getEntity() instanceof EntityVillageGuard || damageSource.getEntity() instanceof EntityWitchHunter)?false:super.attackEntityFrom(damageSource, damage);
   }

   public boolean attackEntityAsMob(Entity p_70652_1_) {
      float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      int i = 0;
      if(p_70652_1_ instanceof EntityLivingBase) {
         f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)p_70652_1_);
         i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)p_70652_1_);
      }

      boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), f);
      if(flag) {
         if(i > 0) {
            p_70652_1_.addVelocity((double)(-MathHelper.sin(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F));
            super.motionX *= 0.6D;
            super.motionZ *= 0.6D;
         }

         int j = EnchantmentHelper.getFireAspectModifier(this);
         if(j > 0) {
            p_70652_1_.setFire(j * 4);
         }

         if(p_70652_1_ instanceof EntityLivingBase) {
            EnchantmentHelper.func_151384_a((EntityLivingBase)p_70652_1_, this);
         }

         EnchantmentHelper.func_151385_b(this, p_70652_1_);
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

   public void onDeath(DamageSource p_70645_1_) {
      if(super.attackingPlayer != null && this.villageObj != null) {
         this.villageObj.setReputationForPlayer(super.attackingPlayer.getCommandSenderName(), -5);
      }

      super.onDeath(p_70645_1_);
   }

   protected Item getDropItem() {
      return Items.arrow;
   }

   protected void dropRareDrop(int p_70600_1_) {
      this.entityDropItem(new ItemStack(Items.leather_chestplate, 1), 0.0F);
   }

   protected void addRandomArmor() {
      this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
      this.setCurrentItemOrArmor(1, new ItemStack(Items.leather_boots));
      this.setCurrentItemOrArmor(2, new ItemStack(Items.leather_leggings));
      this.setCurrentItemOrArmor(3, new ItemStack(super.worldObj.rand.nextInt(5) == 0?Items.iron_chestplate:Items.leather_chestplate));
      this.setCurrentItemOrArmor(4, new ItemStack(super.worldObj.rand.nextInt(5) == 0?Items.iron_helmet:Items.leather_helmet));
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.villageguard.name");
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_) {
      p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);
      this.addRandomArmor();
      return p_110161_1_;
   }

   public void setCombatTask() {
      super.tasks.removeTask(this.aiAttackOnCollide);
      super.tasks.removeTask(this.aiArrowAttack);
      ItemStack itemstack = this.getHeldItem();
      if(itemstack != null && itemstack.getItem() == Items.bow) {
         super.tasks.addTask(4, this.aiArrowAttack);
      } else {
         super.tasks.addTask(4, this.aiAttackOnCollide);
      }

   }

   public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
      PotionEffect effect = this.getActivePotionEffect(Witchery.Potions.PARALYSED);
      if(effect == null || effect.getAmplifier() < 4) {
         EntityArrow entityarrow = new EntityArrow(super.worldObj, this, target, 1.6F, (float)(16 - super.worldObj.difficultySetting.getDifficultyId() * 4));
         int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
         int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
         entityarrow.setDamage((double)(p_82196_2_ * 2.0F) + super.rand.nextGaussian() * 0.25D + (double)((float)super.worldObj.difficultySetting.getDifficultyId() * 0.11F));
         if(i > 0) {
            entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
         }

         if(j > 0) {
            entityarrow.setKnockbackStrength(j);
         }

         if(EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getGuardType() == 1) {
            entityarrow.setFire(100);
         }

         this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
         super.worldObj.spawnEntityInWorld(entityarrow);
      }
   }

   public int getGuardType() {
      return super.dataWatcher.getWatchableObjectByte(13);
   }

   public void setGuardType(int p_82201_1_) {
      super.dataWatcher.updateObject(13, Byte.valueOf((byte)p_82201_1_));
      super.isImmuneToFire = p_82201_1_ == 1;
      if(p_82201_1_ == 1) {
         this.setSize(0.72F, 2.34F);
      } else {
         this.setSize(0.6F, 1.8F);
      }

   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      if(nbtRoot.hasKey("GuardType", 99)) {
         byte b0 = nbtRoot.getByte("GuardType");
         this.setGuardType(b0);
      }

      this.setCombatTask();
      if(nbtRoot.hasKey("BloodLevel")) {
         this.setBlood(nbtRoot.getInteger("BloodLevel"));
      }

   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setByte("GuardType", (byte)this.getGuardType());
      nbtRoot.setInteger("BloodLevel", this.getBlood());
   }

   public void setCurrentItemOrArmor(int p_70062_1_, ItemStack p_70062_2_) {
      super.setCurrentItemOrArmor(p_70062_1_, p_70062_2_);
      if(!super.worldObj.isRemote && p_70062_1_ == 0) {
         this.setCombatTask();
      }

   }

   public double getYOffset() {
      return super.getYOffset() - 0.5D;
   }

   public static void createFrom(EntityVillager villager) {
      World world = villager.worldObj;
      EntityVillageGuard entity = new EntityVillageGuard(world);
      entity.func_110163_bv();
      entity.copyLocationAndAnglesFrom(villager);
      entity.onSpawnWithEgg((IEntityLivingData)null);
      world.removeEntity(villager);
      world.spawnEntityInWorld(entity);
      ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, entity, (double)entity.width, (double)entity.height, 16);
   }
}
