package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityFollower;
import com.emoniph.witchery.util.ParticleEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityHellhound extends EntityMob implements IEntitySelector {

   private float field_70926_e;
   private float field_70924_f;
   private boolean isShaking;
   private boolean field_70928_h;
   private float timeWolfIsShaking;
   private float prevTimeWolfIsShaking;
   private int conversionTime;


   public EntityHellhound(World world) {
      super(world);
      super.isImmuneToFire = true;
      this.setSize(0.9F, 0.9F);
      this.getNavigator().setAvoidsWater(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4F));
      super.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0D, true));
      super.tasks.addTask(4, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(5, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntitySheep.class, 0, true, true));
      super.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, true, this));
      super.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, EntityFollower.class, 0, true, true, this));
   }

   public boolean isEntityApplicable(Entity entity) {
      double AGGRO_RANGE = 5.0D;
      return entity != null && entity.getDistanceSqToEntity(this) < 25.0D;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
   }

   public int getTotalArmorValue() {
      int i = super.getTotalArmorValue() + 2;
      if(i > 20) {
         i = 20;
      }

      return i;
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void updateAITick() {
      super.dataWatcher.updateObject(18, Float.valueOf(this.getHealth()));
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(18, new Float(this.getHealth()));
      super.dataWatcher.addObject(19, new Byte((byte)0));
      this.getDataWatcher().addObject(14, Byte.valueOf((byte)0));
   }

   protected void convertToWolf() {
      EntityWolf entityvillager = new EntityWolf(super.worldObj);
      entityvillager.copyLocationAndAnglesFrom(this);
      entityvillager.onSpawnWithEgg((IEntityLivingData)null);
      super.worldObj.removeEntity(this);
      super.worldObj.spawnEntityInWorld(entityvillager);
      entityvillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
      super.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
   }

   public boolean interact(EntityPlayer p_70085_1_) {
      ItemStack itemstack = p_70085_1_.getCurrentEquippedItem();
      if(itemstack != null && itemstack.getItem() == Items.golden_apple && itemstack.getItemDamage() == 0 && this.isPotionActive(Potion.weakness)) {
         if(!p_70085_1_.capabilities.isCreativeMode) {
            --itemstack.stackSize;
         }

         if(itemstack.stackSize <= 0) {
            p_70085_1_.inventory.setInventorySlotContents(p_70085_1_.inventory.currentItem, (ItemStack)null);
         }

         if(!super.worldObj.isRemote) {
            this.startConversion(super.rand.nextInt(1000) + 3600);
         }

         return true;
      } else {
         return false;
      }
   }

   protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_) {
      this.playSound("mob.wolf.step", 0.15F, 1.0F);
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setInteger("ConversionTime", this.isConverting()?this.conversionTime:-1);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      if(nbtRoot.hasKey("ConversionTime", 99) && nbtRoot.getInteger("ConversionTime") > -1) {
         this.startConversion(nbtRoot.getInteger("ConversionTime"));
      }

   }

   protected void startConversion(int p_82228_1_) {
      this.conversionTime = p_82228_1_;
      this.getDataWatcher().updateObject(14, Byte.valueOf((byte)1));
      this.removePotionEffect(Potion.weakness.id);
      this.addPotionEffect(new PotionEffect(Potion.damageBoost.id, p_82228_1_, Math.min(super.worldObj.difficultySetting.getDifficultyId() - 1, 0)));
      super.worldObj.setEntityState(this, (byte)16);
   }

   protected int getConversionTimeBoost() {
      int i = 1;
      if(super.rand.nextFloat() < 0.01F) {
         int j = 0;

         for(int k = (int)super.posX - 4; k < (int)super.posX + 4 && j < 14; ++k) {
            for(int l = (int)super.posY - 4; l < (int)super.posY + 4 && j < 14; ++l) {
               for(int i1 = (int)super.posZ - 4; i1 < (int)super.posZ + 4 && j < 14; ++i1) {
                  Block block = super.worldObj.getBlock(k, l, i1);
                  if(block == Blocks.iron_bars || block == Blocks.bed) {
                     if(super.rand.nextFloat() < 0.3F) {
                        ++i;
                     }

                     ++j;
                  }
               }
            }
         }
      }

      return i;
   }

   protected String getLivingSound() {
      return "mob.wolf.growl";
   }

   protected String getHurtSound() {
      return "mob.wolf.hurt";
   }

   protected String getDeathSound() {
      return "mob.wolf.death";
   }

   protected float getSoundVolume() {
      return 0.4F;
   }

   protected Item getDropItem() {
      return Item.getItemById(-1);
   }

   protected void dropFewItems(boolean recentlyHitByPlayer, int looting) {
      this.entityDropItem(Witchery.Items.GENERIC.itemDogTongue.createStack(), 0.0F);
      if(super.worldObj.rand.nextInt(12) <= Math.min(looting, 3)) {
         this.entityDropItem(new ItemStack(Witchery.Blocks.WOLFHEAD, 1, 1), 0.0F);
      }

   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if(!super.worldObj.isRemote && !super.isDead && this.isShaking && !this.field_70928_h && !this.hasPath() && super.onGround) {
         this.field_70928_h = true;
         this.timeWolfIsShaking = 0.0F;
         this.prevTimeWolfIsShaking = 0.0F;
         super.worldObj.setEntityState(this, (byte)8);
      }

   }

   public void onUpdate() {
      if(!super.worldObj.isRemote && this.isConverting()) {
         int f = this.getConversionTimeBoost();
         this.conversionTime -= f;
         if(this.conversionTime <= 0) {
            this.convertToWolf();
         }
      }

      super.onUpdate();
      if(!super.isDead) {
         this.field_70924_f = this.field_70926_e;
         if(this.func_70922_bv()) {
            this.field_70926_e += (1.0F - this.field_70926_e) * 0.4F;
         } else {
            this.field_70926_e += (0.0F - this.field_70926_e) * 0.4F;
         }

         if(this.func_70922_bv()) {
            super.numTicksToChaseTarget = 10;
         }

         if(this.isWet()) {
            this.isShaking = true;
            this.field_70928_h = false;
            this.timeWolfIsShaking = 0.0F;
            this.prevTimeWolfIsShaking = 0.0F;
         } else if((this.isShaking || this.field_70928_h) && this.field_70928_h) {
            if(this.timeWolfIsShaking == 0.0F) {
               this.playSound("mob.wolf.shake", this.getSoundVolume(), (super.rand.nextFloat() - super.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
            this.timeWolfIsShaking += 0.05F;
            if(this.prevTimeWolfIsShaking >= 2.0F) {
               this.isShaking = false;
               this.field_70928_h = false;
               this.prevTimeWolfIsShaking = 0.0F;
               this.timeWolfIsShaking = 0.0F;
            }

            if(this.timeWolfIsShaking > 0.4F) {
               float var6 = (float)super.boundingBox.minY;
               int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * 3.1415927F) * 7.0F);

               for(int j = 0; j < i; ++j) {
                  float f1 = (super.rand.nextFloat() * 2.0F - 1.0F) * super.width * 0.5F;
                  float f2 = (super.rand.nextFloat() * 2.0F - 1.0F) * super.width * 0.5F;
                  super.worldObj.spawnParticle("splash", super.posX + (double)f1, (double)(var6 + 0.8F), super.posZ + (double)f2, super.motionX, super.motionY, super.motionZ);
               }
            }
         }

         if(super.worldObj.isRemote && super.ticksExisted % 2 == 0) {
            super.worldObj.spawnParticle(ParticleEffect.FLAME.toString(), super.posX - (double)super.width * 0.35D + super.worldObj.rand.nextDouble() * (double)super.width * 0.7D, 0.5D + super.posY + super.worldObj.rand.nextDouble() * ((double)super.height - 0.1D), super.posZ - (double)super.width * 0.35D + super.worldObj.rand.nextDouble() * (double)super.width * 0.7D, 0.0D, 0.0D, 0.0D);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public boolean getWolfShaking() {
      return this.isShaking;
   }

   @SideOnly(Side.CLIENT)
   public float getShadingWhileShaking(float p_70915_1_) {
      return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
   }

   @SideOnly(Side.CLIENT)
   public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
      float f2 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;
      if(f2 < 0.0F) {
         f2 = 0.0F;
      } else if(f2 > 1.0F) {
         f2 = 1.0F;
      }

      return MathHelper.sin(f2 * 3.1415927F) * MathHelper.sin(f2 * 3.1415927F * 11.0F) * 0.15F * 3.1415927F;
   }

   public float getEyeHeight() {
      return super.height * 0.8F;
   }

   @SideOnly(Side.CLIENT)
   public float getInterestedAngle(float p_70917_1_) {
      return (this.field_70924_f + (this.field_70926_e - this.field_70924_f) * p_70917_1_) * 0.15F * 3.1415927F;
   }

   public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
      if(this.isEntityInvulnerable()) {
         return false;
      } else {
         Entity entity = p_70097_1_.getEntity();
         if(entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
            p_70097_2_ = (p_70097_2_ + 1.0F) / 2.0F;
         }

         return super.attackEntityFrom(p_70097_1_, p_70097_2_);
      }
   }

   public boolean attackEntityAsMob(Entity p_70652_1_) {
      boolean flag = super.attackEntityAsMob(p_70652_1_);
      if(flag) {
         int i = super.worldObj.difficultySetting.getDifficultyId();
         if(super.rand.nextFloat() < (float)i * 0.1F) {
            p_70652_1_.setFire(2 * i);
         }
      }

      return flag;
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte p_70103_1_) {
      if(p_70103_1_ == 8) {
         this.field_70928_h = true;
         this.timeWolfIsShaking = 0.0F;
         this.prevTimeWolfIsShaking = 0.0F;
      } else {
         super.handleHealthUpdate(p_70103_1_);
      }

   }

   @SideOnly(Side.CLIENT)
   public float getTailRotation() {
      return 1.5393804F;
   }

   public int getMaxSpawnedInChunk() {
      return super.getMaxSpawnedInChunk();
   }

   public void func_70918_i(boolean p_70918_1_) {
      if(p_70918_1_) {
         super.dataWatcher.updateObject(19, Byte.valueOf((byte)1));
      } else {
         super.dataWatcher.updateObject(19, Byte.valueOf((byte)0));
      }

   }

   public boolean func_70922_bv() {
      return super.dataWatcher.getWatchableObjectByte(19) == 1;
   }

   protected boolean canDespawn() {
      return !this.isConverting();
   }

   public boolean isConverting() {
      return this.getDataWatcher().getWatchableObjectByte(14) == 1;
   }

   public boolean getCanSpawnHere() {
      return super.worldObj.difficultySetting != EnumDifficulty.PEACEFUL && super.worldObj.checkNoEntityCollision(super.boundingBox) && super.worldObj.getCollidingBoundingBoxes(this, super.boundingBox).isEmpty() && !super.worldObj.isAnyLiquid(super.boundingBox);
   }
}
