package com.emoniph.witchery.entity;

import com.emoniph.witchery.blocks.BlockBrazier;
import com.emoniph.witchery.blocks.BlockKettle;
import com.emoniph.witchery.entity.EntitySummonedUndead;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityPoltergeist extends EntitySummonedUndead {

   private int attackTimer;


   public EntityPoltergeist(World par1World) {
      super(par1World);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setBreakDoors(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
      super.tasks.addTask(3, new EntityAIOpenDoor(this, true));
      super.tasks.addTask(4, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(6, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(20.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
   }

   protected void entityInit() {
      super.entityInit();
   }

   protected boolean isAIEnabled() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public int getAttackTimer() {
      return this.attackTimer;
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if(par1 == 4) {
         this.attackTimer = 15;
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if(this.attackTimer > 0) {
         --this.attackTimer;
      }

      if(TimeUtil.secondsElapsed(5, (long)super.ticksExisted)) {
         double RADIUS = 16.0D;
         double RADIUS_SQ = 256.0D;
         double THROW_RANGE = 3.0D;
         double THROW_RANGE_SQ = 9.0D;
         double EVIL_RANGE = 8.0D;
         double EVIL_RANGE_SQ = 64.0D;
         double MAX_SPEED = 0.6D;
         AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(super.posX - 16.0D, super.posY - 16.0D, super.posZ - 16.0D, super.posX + 16.0D, super.posY + 16.0D, super.posZ + 16.0D);
         List hangingItems = super.worldObj.getEntitiesWithinAABB(EntityHanging.class, bounds);
         Iterator summoner = hangingItems.iterator();

         while(summoner.hasNext()) {
            Object droppedItems = summoner.next();
            EntityHanging i$ = (EntityHanging)droppedItems;
            if(this.getDistanceSqToEntity(i$) <= 256.0D) {
               if(this.getDistanceSqToEntity(i$) <= 9.0D) {
                  if(!super.worldObj.isRemote) {
                     i$.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
                  }

                  this.attackTimer = 15;
                  super.worldObj.setEntityState(this, (byte)4);
               } else {
                  this.getNavigator().tryMoveToXYZ(i$.posX, i$.posY, i$.posZ, 1.0D);
               }

               return;
            }
         }

         EntityPlayer var29 = this.getSummoner();
         if(var29 != null && this.getDistanceSqToEntity(var29) <= 64.0D) {
            TileEntity var32 = null;
            double var30 = -1.0D;
            Iterator dropped = super.worldObj.loadedTileEntityList.iterator();

            while(dropped.hasNext()) {
               Object indices = dropped.next();
               if(indices instanceof IInventory && !(indices instanceof BlockKettle.TileEntityKettle) && !(indices instanceof BlockBrazier.TileEntityBrazier)) {
                  TileEntity slot = (TileEntity)indices;
                  double stack = this.getDistanceSq(0.5D + (double)slot.xCoord, 0.5D + (double)slot.yCoord, 0.5D + (double)slot.zCoord);
                  if(stack <= 256.0D) {
                     IInventory inventory = (IInventory)slot;
                     ArrayList indices1 = new ArrayList();

                     for(int i = 0; i < inventory.getSizeInventory(); ++i) {
                        if(inventory.getStackInSlot(i) != null) {
                           indices1.add(Integer.valueOf(i));
                        }
                     }

                     if(indices1.size() > 0 && (var32 == null || stack < var30)) {
                        var32 = slot;
                        var30 = stack;
                     }
                  }
               }
            }

            if(var32 != null) {
               IInventory var36 = (IInventory)var32;
               ArrayList var34 = new ArrayList();

               int var35;
               for(var35 = 0; var35 < var36.getSizeInventory(); ++var35) {
                  if(var36.getStackInSlot(var35) != null) {
                     var34.add(Integer.valueOf(var35));
                  }
               }

               if(var34.size() > 0) {
                  if(this.getDistanceSq(0.5D + (double)var32.xCoord, 0.5D + (double)var32.yCoord, 0.5D + (double)var32.zCoord) <= 9.0D) {
                     if(!super.worldObj.isRemote) {
                        var35 = ((Integer)var34.get(super.worldObj.rand.nextInt(var34.size()))).intValue();
                        ItemStack var38 = var36.getStackInSlot(var35);
                        if(var38.stackSize > 1) {
                           --var38.stackSize;
                           var38 = var38.copy();
                           var38.stackSize = 1;
                        } else {
                           var36.setInventorySlotContents(var35, (ItemStack)null);
                        }

                        EntityItem itemEntity = new EntityItem(super.worldObj, 0.5D + (double)var32.xCoord, 0.5D + (double)var32.yCoord, 0.5D + (double)var32.zCoord, var38);
                        super.worldObj.spawnEntityInWorld(itemEntity);
                        itemEntity.lifespan = TimeUtil.minsToTicks(15);
                        itemEntity.motionX = -0.3D + super.worldObj.rand.nextDouble() * 0.6D;
                        itemEntity.motionY = 0.1D + super.worldObj.rand.nextDouble() * 0.2D;
                        itemEntity.motionZ = -0.3D + super.worldObj.rand.nextDouble() * 0.6D;
                     }

                     this.attackTimer = 15;
                     super.worldObj.setEntityState(this, (byte)4);
                  } else {
                     this.getNavigator().tryMoveToXYZ((double)var32.xCoord, (double)var32.yCoord, (double)var32.zCoord, 1.0D);
                  }

                  return;
               }
            }
         }

         List var31 = super.worldObj.getEntitiesWithinAABB(EntityItem.class, bounds);
         Iterator var33 = var31.iterator();

         while(var33.hasNext()) {
            Object obj = var33.next();
            EntityItem var37 = (EntityItem)obj;
            if(this.getDistanceSqToEntity(var37) <= 256.0D) {
               if(this.getDistanceSqToEntity(var37) <= 9.0D) {
                  if(!super.worldObj.isRemote) {
                     var37.motionX = -0.3D + super.worldObj.rand.nextDouble() * 0.6D;
                     var37.motionY = 0.1D + super.worldObj.rand.nextDouble() * 0.2D;
                     var37.motionZ = -0.3D + super.worldObj.rand.nextDouble() * 0.6D;
                  }

                  this.attackTimer = 15;
                  super.worldObj.setEntityState(this, (byte)4);
               } else {
                  this.getNavigator().tryMoveToXYZ(var37.posX, var37.posY, var37.posZ, 1.0D);
               }

               return;
            }
         }
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
      return "witchery:mob.spectre.spectre_die";
   }

   protected String getDeathSound() {
      return "witchery:mob.spectre.spectre_die";
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.poltergeist.name");
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      IEntityLivingData par1EntityLivingData1 = super.onSpawnWithEgg(par1EntityLivingData);
      this.addPotionEffect(new PotionEffect(Potion.invisibility.id, Integer.MAX_VALUE));
      return (IEntityLivingData)par1EntityLivingData1;
   }
}
