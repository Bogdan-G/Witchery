package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.ai.EntityAIDimensionalFollowOwner;
import com.emoniph.witchery.entity.ai.EntityAISitAndStay;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.familiar.IFamiliar;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityToad extends EntityTameable implements IFamiliar {

   private int timeToLive = -1;
   private boolean poisoned = false;


   public EntityToad(World par1World) {
      super(par1World);
      this.setSize(0.8F, 0.8F);
      this.getNavigator().setCanSwim(true);
      this.getNavigator().setAvoidsWater(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAISitAndStay(this));
      super.tasks.addTask(3, new EntityAIDimensionalFollowOwner(this, 1.0D, 10.0F, 2.0F));
      super.tasks.addTask(4, new EntityAITempt(this, 1.25D, Items.rotten_flesh, false));
      super.tasks.addTask(6, new EntityAIMate(this, 1.0D));
      super.tasks.addTask(7, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(9, new EntityAILookIdle(this));
      this.setTamed(false);
   }

   public void setTimeToLive(int i, boolean poisoned) {
      this.timeToLive = i;
      this.poisoned = poisoned;
   }

   public boolean isTemp() {
      return this.timeToLive != -1;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000001192092895D);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
   }

   public int getTotalArmorValue() {
      return super.getTotalArmorValue() + (this.isFamiliar()?5:0);
   }

   public int getTalkInterval() {
      return super.getTalkInterval() * 2;
   }

   public void setMaxHealth(float maxHealth) {
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)maxHealth);
      this.setHealth(maxHealth);
      this.setFamiliar(true);
   }

   public EntityLivingBase getOwner() {
      return this.isFamiliar() && !super.worldObj.isRemote?TameableUtil.getOwnerAccrossDimensions(this):super.getOwner();
   }

   protected int decreaseAirSupply(int par1) {
      return par1;
   }

   protected void fall(float par1) {}

   public boolean isAIEnabled() {
      return true;
   }

   protected void updateAITick() {
      super.updateAITick();
      super.dataWatcher.updateObject(18, Float.valueOf(this.getHealth()));
      if(super.worldObj != null && !super.isDead && !super.worldObj.isRemote && this.timeToLive != -1 && --this.timeToLive == 0) {
         this.setDead();
         if(this.poisoned) {
            AxisAlignedBB axisalignedbb = super.boundingBox.expand(3.0D, 2.0D, 3.0D);
            List list1 = super.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
            if(list1 != null && !list1.isEmpty()) {
               Iterator iterator = list1.iterator();

               while(iterator.hasNext()) {
                  EntityLivingBase entitylivingbase = (EntityLivingBase)iterator.next();
                  double d0 = this.getDistanceSqToEntity(entitylivingbase);
                  if(d0 < 9.0D) {
                     double d1 = 1.0D - Math.sqrt(d0) / 3.0D;
                     entitylivingbase.addPotionEffect(new PotionEffect(Potion.poison.id, 60, 0));
                  }
               }
            }

            ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_BIG, this, 1.0D, 1.0D, 16);
         }

         ParticleEffect.MOB_SPELL.send(SoundEffect.NONE, this, 0.5D, 0.5D, 16);
      }

   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(18, new Float(this.getHealth()));
      super.dataWatcher.addObject(19, new Byte((byte)0));
      super.dataWatcher.addObject(20, new Byte((byte)BlockColored.func_150032_b(super.worldObj != null?super.worldObj.rand.nextInt(16):(new Random()).nextInt(16))));
      super.dataWatcher.addObject(26, Byte.valueOf((byte)0));
   }

   public boolean isFamiliar() {
      return super.dataWatcher.getWatchableObjectByte(26) > 0;
   }

   public void setFamiliar(boolean familiar) {
      super.dataWatcher.updateObject(26, Byte.valueOf((byte)(familiar?1:0)));
   }

   protected void func_145780_a(int par1, int par2, int par3, Block par4) {
      this.playSound("mob.slime.small", 0.15F, 1.0F);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setByte("SkinColor", (byte)this.getSkinColor());
      par1NBTTagCompound.setByte("Familiar", (byte)(this.isFamiliar()?1:0));
      par1NBTTagCompound.setInteger("SuicideIn", this.timeToLive);
      par1NBTTagCompound.setBoolean("Poisonous", this.poisoned);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      if(par1NBTTagCompound.hasKey("SkinColor")) {
         this.setSkinColor(par1NBTTagCompound.getByte("SkinColor"));
      }

      if(par1NBTTagCompound.hasKey("Familiar")) {
         this.setFamiliar(par1NBTTagCompound.getByte("Familiar") > 0);
      }

      if(par1NBTTagCompound.hasKey("SuicideIn")) {
         this.timeToLive = par1NBTTagCompound.getInteger("SuicideIn");
      } else {
         this.timeToLive = -1;
      }

      if(par1NBTTagCompound.hasKey("Poisonous")) {
         this.poisoned = par1NBTTagCompound.getBoolean("Poisonous");
      } else {
         this.poisoned = false;
      }

   }

   protected String getLivingSound() {
      return "witchery:mob.toad.toad_croak";
   }

   protected String getHurtSound() {
      return "witchery:mob.toad.toad_hurt";
   }

   protected String getDeathSound() {
      return "witchery:mob.toad.toad_hurt";
   }

   protected float getSoundVolume() {
      return 0.4F;
   }

   protected Item getDropItem() {
      return !this.isTemp()?Items.slime_ball:super.getDropItem();
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
   }

   public void onUpdate() {
      super.isImmuneToFire = this.isFamiliar();
      super.onUpdate();
      if(!this.isSitting() && !super.worldObj.isRemote && (super.motionX != 0.0D || super.motionZ != 0.0D) && !this.isInWater()) {
         this.getJumpHelper().setJumping();
      }

   }

   public float getEyeHeight() {
      return super.height * 0.8F;
   }

   public int getVerticalFaceSpeed() {
      return this.isSitting()?20:super.getVerticalFaceSpeed();
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      if(this.isEntityInvulnerable()) {
         return false;
      } else {
         Entity entity = par1DamageSource.getEntity();
         if(!this.isFamiliar()) {
            this.setSitting(false);
         }

         if(entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
            par2 = (par2 + 1.0F) / 2.0F;
         }

         return super.attackEntityFrom(par1DamageSource, par2);
      }
   }

   public void setTamed(boolean par1) {
      super.setTamed(par1);
   }

   public boolean interact(EntityPlayer par1EntityPlayer) {
      if(this.isTemp()) {
         return true;
      } else {
         ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
         if(this.isTamed()) {
            if(TameableUtil.isOwner(this, par1EntityPlayer) && this.isFamiliar() && par1EntityPlayer.isSneaking() && this.isSitting()) {
               if(!super.worldObj.isRemote) {
                  Familiar.dismissFamiliar(par1EntityPlayer, this);
               }

               return true;
            }

            if(itemstack != null) {
               if(itemstack.getItem() == Items.rotten_flesh && this.getHealth() < this.getMaxHealth()) {
                  if(!par1EntityPlayer.capabilities.isCreativeMode) {
                     --itemstack.stackSize;
                  }

                  this.heal(10.0F);
                  if(itemstack.stackSize <= 0) {
                     par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                  }

                  return true;
               }

               if(itemstack.getItem() == Items.dye) {
                  int i = BlockColored.func_150032_b(itemstack.getItemDamage());
                  if(i != this.getSkinColor()) {
                     this.setSkinColor(i);
                     if(!par1EntityPlayer.capabilities.isCreativeMode && --itemstack.stackSize <= 0) {
                        par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                     }

                     return true;
                  }
               } else if(itemstack.getItem() == Items.name_tag || itemstack.getItem() == Witchery.Items.POLYNESIA_CHARM || itemstack.getItem() == Witchery.Items.DEVILS_TONGUE_CHARM) {
                  return false;
               }
            }

            if(TameableUtil.isOwner(this, par1EntityPlayer) && !this.isBreedingItem(itemstack)) {
               if(!super.worldObj.isRemote) {
                  this.setSitting(!this.isSitting());
                  this.getJumpHelper().doJump();
                  super.isJumping = false;
                  this.setPathToEntity((PathEntity)null);
                  this.setTarget((Entity)null);
                  this.setAttackTarget((EntityLivingBase)null);
               }

               return true;
            }
         } else if(itemstack != null && itemstack.getItem() == Items.rotten_flesh) {
            if(!par1EntityPlayer.capabilities.isCreativeMode) {
               --itemstack.stackSize;
            }

            if(itemstack.stackSize <= 0) {
               par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
            }

            if(!super.worldObj.isRemote) {
               if(super.rand.nextInt(3) == 0) {
                  this.setTamed(true);
                  this.func_110163_bv();
                  this.setPathToEntity((PathEntity)null);
                  this.setAttackTarget((EntityLivingBase)null);
                  this.setSitting(true);
                  TameableUtil.setOwner(this, par1EntityPlayer);
                  this.playTameEffect(true);
                  super.worldObj.setEntityState(this, (byte)7);
               } else {
                  this.playTameEffect(false);
                  super.worldObj.setEntityState(this, (byte)6);
               }
            }

            return true;
         }

         return super.interact(par1EntityPlayer);
      }
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.toad.name");
   }

   public boolean isBreedingItem(ItemStack par1ItemStack) {
      return par1ItemStack != null && par1ItemStack.getItem() == Items.rotten_flesh;
   }

   public int getSkinColor() {
      return super.dataWatcher.getWatchableObjectByte(20) & 15;
   }

   public void setSkinColor(int par1) {
      super.dataWatcher.updateObject(20, Byte.valueOf((byte)(par1 & 15)));
   }

   public EntityToad spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
      EntityToad entity = new EntityToad(super.worldObj);
      if(TameableUtil.hasOwner(this)) {
         entity.func_110163_bv();
         entity.setSkinColor(this.getSkinColor());
      }

      return entity;
   }

   public boolean canMateWith(EntityAnimal par1EntityAnimal) {
      if(par1EntityAnimal == this) {
         return false;
      } else if(!this.isTamed()) {
         return false;
      } else if(!(par1EntityAnimal instanceof EntityToad)) {
         return false;
      } else {
         EntityToad entity = (EntityToad)par1EntityAnimal;
         return !entity.isTamed()?false:(entity.isSitting()?false:this.isInLove() && entity.isInLove());
      }
   }

   public boolean func_70922_bv() {
      return super.dataWatcher.getWatchableObjectByte(19) == 1;
   }

   protected boolean canDespawn() {
      return false;
   }

   public boolean func_142018_a(EntityLivingBase par1EntityLivingBase, EntityLivingBase par2EntityLivingBase) {
      if(!(par1EntityLivingBase instanceof EntityCreeper) && !(par1EntityLivingBase instanceof EntityGhast)) {
         if(par1EntityLivingBase instanceof EntityToad) {
            EntityToad entity = (EntityToad)par1EntityLivingBase;
            if(entity.isTamed() && entity.getOwner() == par2EntityLivingBase) {
               return false;
            }
         }

         return par1EntityLivingBase instanceof EntityPlayer && par2EntityLivingBase instanceof EntityPlayer && !((EntityPlayer)par2EntityLivingBase).canAttackPlayer((EntityPlayer)par1EntityLivingBase)?false:!(par1EntityLivingBase instanceof EntityHorse) || !((EntityHorse)par1EntityLivingBase).isTame();
      } else {
         return false;
      }
   }

   public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
      return this.spawnBabyAnimal(par1EntityAgeable);
   }

   public void clearFamiliar() {
      this.setFamiliar(false);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
      this.setHealth(10.0F);
   }
}
