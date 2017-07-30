package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.ai.EntityAIDimensionalFollowOwner;
import com.emoniph.witchery.entity.ai.EntityAISitAndStay;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.familiar.IFamiliar;
import com.emoniph.witchery.util.TameableUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityWitchCat extends EntityOcelot implements IFamiliar {

   public EntityWitchCat(World par1World) {
      super(par1World);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.removeTask(((EntityAITaskEntry)super.tasks.taskEntries.get(4)).action);
      super.tasks.removeTask(((EntityAITaskEntry)super.tasks.taskEntries.get(1)).action);
      super.tasks.addTask(1, new EntityAISitAndStay(this));
      super.tasks.addTask(5, new EntityAIDimensionalFollowOwner(this, 1.0D, 10.0F, 5.0F));
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setByte("Familiar", (byte)(this.isFamiliar()?1:0));
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      if(par1NBTTagCompound.hasKey("Familiar")) {
         this.setFamiliar(par1NBTTagCompound.getByte("Familiar") > 0);
      }

   }

   public void onUpdate() {
      super.isImmuneToFire = this.isFamiliar();
      super.onUpdate();
   }

   public int getTotalArmorValue() {
      return super.getTotalArmorValue() + (this.isFamiliar()?5:0);
   }

   public EntityLivingBase getOwner() {
      return this.isFamiliar() && !super.worldObj.isRemote?TameableUtil.getOwnerAccrossDimensions(this):super.getOwner();
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(26, Byte.valueOf((byte)0));
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      boolean sitting = this.isSitting();
      boolean result = super.attackEntityFrom(par1DamageSource, par2);
      if(sitting && this.isFamiliar()) {
         this.setSitting(true);
      }

      return result;
   }

   public boolean isFamiliar() {
      return super.dataWatcher.getWatchableObjectByte(26) > 0;
   }

   public void setFamiliar(boolean familiar) {
      super.dataWatcher.updateObject(26, Byte.valueOf((byte)(familiar?1:0)));
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.cat.name");
   }

   protected int decreaseAirSupply(int par1) {
      return par1;
   }

   public boolean interact(EntityPlayer par1EntityPlayer) {
      ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
      if(this.isTamed()) {
         if(TameableUtil.isOwner(this, par1EntityPlayer) && this.isFamiliar() && par1EntityPlayer.isSneaking() && this.isSitting()) {
            if(!super.worldObj.isRemote) {
               Familiar.dismissFamiliar(par1EntityPlayer, this);
            }

            return true;
         }

         if(TameableUtil.isOwner(this, par1EntityPlayer)) {
            if(itemstack == null || !this.isBreedingItem(itemstack) && itemstack.getItem() != Items.name_tag && itemstack.getItem() != Witchery.Items.POLYNESIA_CHARM && itemstack.getItem() != Witchery.Items.DEVILS_TONGUE_CHARM) {
               if(!super.worldObj.isRemote) {
                  this.setSitting(!this.isSitting());
               }

               return true;
            }

            if(itemstack != null && this.isBreedingItem(itemstack) && this.getHealth() < this.getMaxHealth()) {
               if(!super.worldObj.isRemote) {
                  if(!par1EntityPlayer.capabilities.isCreativeMode) {
                     --itemstack.stackSize;
                  }

                  this.heal(10.0F);
                  if(itemstack.stackSize <= 0) {
                     par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                  }
               }

               return true;
            }
         }
      } else if(itemstack != null && itemstack.getItem() == Items.fish && par1EntityPlayer.getDistanceSqToEntity(this) < 9.0D) {
         if(!par1EntityPlayer.capabilities.isCreativeMode) {
            --itemstack.stackSize;
         }

         if(itemstack.stackSize <= 0) {
            par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
         }

         if(!super.worldObj.isRemote) {
            if(super.rand.nextInt(3) == 0) {
               this.setTamed(true);
               this.setTameSkin(1 + super.worldObj.rand.nextInt(3));
               TameableUtil.setOwner(this, par1EntityPlayer);
               this.playTameEffect(true);
               this.func_110163_bv();
               this.setSitting(true);
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

   public void setMaxHealth(float maxHealth) {
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)maxHealth);
      this.setHealth(maxHealth);
      this.setFamiliar(true);
   }

   public void cloneOcelot(EntityOcelot oldCat) {
      if(oldCat.hasCustomNameTag()) {
         this.setCustomNameTag(oldCat.getCustomNameTag());
      }

      this.setLocationAndAngles(oldCat.posX, oldCat.posY, oldCat.posZ, oldCat.rotationYaw, oldCat.rotationPitch);
      TameableUtil.cloneOwner(this, oldCat);
      this.setTamed(true);
      this.setSitting(oldCat.isSitting());
      double oldMaxHealth = oldCat.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(oldMaxHealth);
      this.setHealth(oldCat.getHealth());
   }

   public void clearFamiliar() {
      this.setFamiliar(false);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
      this.setHealth(10.0F);
   }
}
