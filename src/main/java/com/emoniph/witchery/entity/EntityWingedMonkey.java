package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityFlyingTameable;
import com.emoniph.witchery.entity.ai.EntityAIFlyerAttackOnCollide;
import com.emoniph.witchery.entity.ai.EntityAIFlyerFlyToWaypoint;
import com.emoniph.witchery.entity.ai.EntityAIFlyerFollowOwner;
import com.emoniph.witchery.entity.ai.EntityAIFlyerLand;
import com.emoniph.witchery.entity.ai.EntityAIFlyerMate;
import com.emoniph.witchery.entity.ai.EntityAIFlyerWander;
import com.emoniph.witchery.entity.ai.EntityAIFlyingTempt;
import com.emoniph.witchery.entity.ai.EntityAISitAndStay;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import com.emoniph.witchery.util.Waypoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityWingedMonkey extends EntityFlyingTameable {

   private int attackTimer;
   public EntityAIFlyingTempt aiTempt;
   private int timeToLive = -1;
   private static final ItemStack[] TEMPTATIONS = new ItemStack[]{new ItemStack(Items.porkchop), new ItemStack(Items.beef)};


   public EntityWingedMonkey(World world) {
      super(world);
      this.setSize(0.6F, 0.8F);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISitAndStay(this));
      super.tasks.addTask(2, new EntityAIFlyerFlyToWaypoint(this, EntityAIFlyerFlyToWaypoint.CarryRequirement.ENTITY_LIVING));
      super.tasks.addTask(3, new EntityAIFlyerAttackOnCollide(this, 1.0D, true));
      super.tasks.addTask(4, this.aiTempt = new EntityAIFlyingTempt(this, 0.6D, TEMPTATIONS, true));
      super.tasks.addTask(5, new EntityAIFlyerFollowOwner(this, 1.0D, 14.0F, 5.0F));
      super.tasks.addTask(8, new EntityAIFlyerMate(this, 0.8D));
      super.tasks.addTask(9, new EntityAIFlyerLand(this, 0.8D, true));
      super.tasks.addTask(10, new EntityAIFlyerWander(this, 0.8D, 10.0D));
      super.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F, 0.2F));
      super.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
      super.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
      super.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
   }

   public int getTotalArmorValue() {
      return super.getTotalArmorValue() + 5;
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setByte("FeatherColor", (byte)this.getFeatherColor());
      nbtRoot.setInteger("SuicideIn", this.timeToLive);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      if(nbtRoot.hasKey("FeatherColor")) {
         this.setFeatherColor(nbtRoot.getByte("FeatherColor"));
      }

      if(nbtRoot.hasKey("SuicideIn")) {
         this.timeToLive = nbtRoot.getInteger("SuicideIn");
      } else {
         this.timeToLive = -1;
      }

   }

   public void onUpdate() {
      super.onUpdate();
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(18, Byte.valueOf((byte)0));
      super.dataWatcher.addObject(21, Byte.valueOf((byte)(super.worldObj.rand.nextInt(100) == 0?0:super.worldObj.rand.nextInt(15) + 1)));
   }

   protected int decreaseAirSupply(int par1) {
      return par1;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
   }

   protected boolean canDespawn() {
      return false;
   }

   public boolean isAIEnabled() {
      return true;
   }

   public void updateAITick() {
      this.getNavigator().clearPathEntity();
      super.updateAITick();
      if(super.worldObj != null && !super.isDead && !super.worldObj.isRemote && this.timeToLive != -1 && (--this.timeToLive == 0 || this.getAttackTarget() == null || this.getAttackTarget().isDead)) {
         ParticleEffect.EXPLODE.send(SoundEffect.NONE, this, 1.0D, 1.0D, 16);
         this.setDead();
      }

   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if(this.attackTimer > 0) {
         --this.attackTimer;
      }

   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if(par1 == 4) {
         this.attackTimer = 10;
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   @SideOnly(Side.CLIENT)
   public int getAttackTimer() {
      return this.attackTimer;
   }

   protected void dropFewItems(boolean par1, int par2) {
      if(!this.isTemp()) {
         int var3 = super.rand.nextInt(3) + super.rand.nextInt(1 + par2);

         for(int var4 = 0; var4 < var3; ++var4) {
            this.entityDropItem(new ItemStack(Items.feather), 0.0F);
         }
      }

   }

   public int getTalkInterval() {
      return super.getTalkInterval() * 2;
   }

   protected String getLivingSound() {
      return "witchery:mob.monkey.say";
   }

   protected String getHurtSound() {
      return "witchery:mob.monkey.hit";
   }

   protected String getDeathSound() {
      return "witchery:mob.monkey.death";
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      this.attackTimer = 10;
      super.worldObj.setEntityState(this, (byte)4);
      return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2.0F);
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      if(this.isEntityInvulnerable()) {
         return false;
      } else {
         this.setSitting(false);
         return super.attackEntityFrom(par1DamageSource, par2);
      }
   }

   public double getMountedYOffset() {
      return super.riddenByEntity != null?(double)(-super.riddenByEntity.height) * 0.6D:0.0D;
   }

   public boolean shouldRiderSit() {
      return false;
   }

   public boolean interact(EntityPlayer player) {
      if(this.isTemp()) {
         return true;
      } else {
         ItemStack itemstack = player.inventory.getCurrentItem();
         if(this.isTamed()) {
            if(TameableUtil.isOwner(this, player) && !this.isBreedingItem(itemstack)) {
               if(itemstack != null && itemstack.getItem() == Items.dye) {
                  if(!super.worldObj.isRemote) {
                     int var4 = BlockColored.func_150032_b(itemstack.getItemDamage());
                     this.setFeatherColor(var4);
                     if(!player.capabilities.isCreativeMode) {
                        --itemstack.stackSize;
                     }

                     if(itemstack.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                     }
                  }
               } else {
                  if(itemstack != null && (itemstack.getItem() == Items.name_tag || itemstack.getItem() == Witchery.Items.POLYNESIA_CHARM || itemstack.getItem() == Witchery.Items.DEVILS_TONGUE_CHARM)) {
                     return false;
                  }

                  if(!Witchery.Items.GENERIC.itemWaystonePlayerBound.isMatch(itemstack) && !Witchery.Items.GENERIC.itemWaystoneBound.isMatch(itemstack)) {
                     if(itemstack != null && Witchery.Items.TAGLOCK_KIT.isTaglockPresent(itemstack, Integer.valueOf(1))) {
                        super.waypoint = itemstack.copy();
                        super.homeX = super.posX;
                        super.homeY = super.posY;
                        super.homeZ = super.posZ;
                        ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_ORB, this, 1.0D, 1.0D, 16);
                        if(!player.capabilities.isCreativeMode) {
                           --itemstack.stackSize;
                        }
                     } else if(this.getDistanceSqToEntity(player) < 9.0D && !super.worldObj.isRemote) {
                        this.setSitting(!this.isSitting());
                     }
                  } else {
                     super.waypoint = itemstack.copy();
                     super.homeX = 0.0D;
                     super.homeY = 0.0D;
                     super.homeZ = 0.0D;
                     Waypoint wp = this.getWaypoint();
                     super.homeX = wp.X;
                     super.homeY = wp.Y;
                     super.homeZ = wp.Z;
                     Witchery.Items.GENERIC.bindToLocation(super.worldObj, (int)super.posX, (int)super.posY, (int)super.posZ, super.worldObj.provider.dimensionId, super.worldObj.provider.getDimensionName(), super.waypoint);
                     if(!super.worldObj.isRemote && this.isSitting()) {
                        this.setSitting(false);
                     }

                     player.mountEntity(this);
                     ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_ORB, this, 1.0D, 1.0D, 16);
                  }
               }

               return true;
            } else if(TameableUtil.isOwner(this, player) && this.isBreedingItem(itemstack) && this.getHealth() < this.getMaxHealth()) {
               if(!super.worldObj.isRemote) {
                  this.heal(10.0F);
                  if(!player.capabilities.isCreativeMode) {
                     --itemstack.stackSize;
                  }

                  if(itemstack.stackSize <= 0) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                  }
               }

               return true;
            } else {
               return super.interact(player);
            }
         } else if(itemstack != null && (itemstack.getItem() == Items.porkchop || itemstack.getItem() == Items.beef) && player.getDistanceSqToEntity(this) < 9.0D) {
            if(!player.capabilities.isCreativeMode) {
               --itemstack.stackSize;
            }

            if(itemstack.stackSize <= 0) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            }

            if(!super.worldObj.isRemote) {
               if(super.rand.nextInt(3) == 0) {
                  this.setTamed(true);
                  this.setTameSkin(1 + super.worldObj.rand.nextInt(3));
                  TameableUtil.setOwner(this, player);
                  this.func_110163_bv();
                  this.playTameEffect(true);
                  this.setSitting(true);
                  super.worldObj.setEntityState(this, (byte)7);
               } else {
                  this.playTameEffect(false);
                  super.worldObj.setEntityState(this, (byte)6);
               }
            }

            return true;
         } else {
            return !this.isBreedingItem(itemstack)?super.interact(player):false;
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public IIcon getItemIcon(ItemStack stack, int pass) {
      return stack.getItem().requiresMultipleRenderPasses()?stack.getItem().getIcon(stack, pass):stack.getIconIndex();
   }

   public EntityWingedMonkey spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
      EntityWingedMonkey entityocelot = new EntityWingedMonkey(super.worldObj);
      if(this.isTamed()) {
         entityocelot.func_110163_bv();
         entityocelot.setTameSkin(this.getTameSkin());
         entityocelot.setFeatherColor(this.getFeatherColor());
      }

      return entityocelot;
   }

   public boolean isBreedingItem(ItemStack itemstack) {
      return itemstack != null && (itemstack.getItem() == Items.porkchop || itemstack.getItem() == Items.beef);
   }

   public boolean canMateWith(EntityAnimal par1EntityAnimal) {
      if(par1EntityAnimal == this) {
         return false;
      } else if(!this.isTamed()) {
         return false;
      } else if(!(par1EntityAnimal instanceof EntityWingedMonkey)) {
         return false;
      } else {
         EntityWingedMonkey entityocelot = (EntityWingedMonkey)par1EntityAnimal;
         return !entityocelot.isTamed()?false:this.isInLove() && entityocelot.isInLove();
      }
   }

   public int getFeatherColor() {
      return super.dataWatcher.getWatchableObjectByte(21) & 15;
   }

   public void setFeatherColor(int par1) {
      byte b0 = super.dataWatcher.getWatchableObjectByte(16);
      super.dataWatcher.updateObject(21, Byte.valueOf((byte)(b0 & 240 | par1 & 15)));
   }

   public int getTameSkin() {
      return super.dataWatcher.getWatchableObjectByte(18);
   }

   public void setTameSkin(int par1) {
      super.dataWatcher.updateObject(18, Byte.valueOf((byte)par1));
   }

   public boolean getCanSpawnHere() {
      if(super.worldObj.rand.nextInt(3) == 0) {
         return false;
      } else {
         if(super.worldObj.checkNoEntityCollision(super.boundingBox) && super.worldObj.getCollidingBoundingBoxes(this, super.boundingBox).isEmpty() && !super.worldObj.isAnyLiquid(super.boundingBox)) {
            int i = MathHelper.floor_double(super.posX);
            int j = MathHelper.floor_double(super.boundingBox.minY);
            int k = MathHelper.floor_double(super.posZ);
            if(j < 63) {
               return false;
            }

            Block block = super.worldObj.getBlock(i, j - 1, k);
            if(block == Blocks.grass || block != null && block.isLeaves(super.worldObj, i, j - 1, k)) {
               return true;
            }
         }

         return false;
      }
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.wingedmonkey.name");
   }

   public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
      return this.spawnBabyAnimal(par1EntityAgeable);
   }

   public void setTimeToLive(int i) {
      this.timeToLive = i;
   }

   public boolean isTemp() {
      return this.timeToLive != -1;
   }

}
