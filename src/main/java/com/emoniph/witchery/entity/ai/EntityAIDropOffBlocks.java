package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.entity.EntityGoblin;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;

public class EntityAIDropOffBlocks extends EntityAIBase {

   protected final EntityGoblin entity;
   protected final double range;
   private TileEntity targetTile = null;


   public EntityAIDropOffBlocks(EntityGoblin entity, double range) {
      this.entity = entity;
      this.range = range;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      if((this.entity == null || this.entity.isWorshipping() || this.entity.getHeldItem() != null) && this.entity.getLeashed() && !(this.entity.getHeldItem().getItem() instanceof ItemTool)) {
         if(this.targetTile != null && !this.targetTile.isInvalid() && this.entity.getNavigator().getPathToXYZ((double)this.targetTile.xCoord, (double)this.targetTile.yCoord, (double)this.targetTile.zCoord) != null) {
            return true;
         } else {
            this.targetTile = null;
            if(this.entity.worldObj.rand.nextInt(60) != 0) {
               return false;
            } else {
               this.setTargetTile();
               return this.targetTile != null;
            }
         }
      } else {
         return false;
      }
   }

   public void startExecuting() {}

   private void setTargetTile() {
      this.targetTile = null;
      new ArrayList();
      double bestDist = Double.MAX_VALUE;
      double RANGE_SQ = this.range * this.range;

      for(int i = 0; i < this.entity.worldObj.loadedTileEntityList.size(); ++i) {
         try {
            Object e = this.entity.worldObj.loadedTileEntityList.get(i);
            if(e != null && e instanceof IInventory) {
               TileEntity tile = (TileEntity)e;
               if(!tile.isInvalid() && ((IInventory)tile).getSizeInventory() >= 27) {
                  double distSq = this.entity.getDistanceSq((double)tile.xCoord, (double)tile.yCoord, (double)tile.zCoord);
                  if(distSq <= RANGE_SQ && this.entity.getNavigator().getPathToXYZ((double)tile.xCoord, (double)tile.yCoord, (double)tile.zCoord) != null && distSq < bestDist) {
                     bestDist = distSq;
                     this.targetTile = tile;
                  }
               }
            }
         } catch (Throwable var11) {
            ;
         }
      }

   }

   public boolean continueExecuting() {
      return this.entity != null && !this.entity.isWorshipping() && this.entity.getHeldItem() != null && this.entity.getLeashed() && this.targetTile != null;
   }

   public void updateTask() {
      double SPEED = 0.6D;
      if(this.entity.getNavigator().noPath()) {
         this.setTargetTile();
         if(this.targetTile != null) {
            this.entity.getNavigator().tryMoveToXYZ((double)this.targetTile.xCoord, (double)this.targetTile.yCoord, (double)this.targetTile.zCoord, 0.6D);
         }
      }

      double DROP_RANGE = 2.5D;
      double DROP_RANGE_SQ = 6.25D;
      if(this.targetTile != null && this.entity.getDistanceSq((double)this.targetTile.xCoord + 0.5D, (double)this.targetTile.yCoord + 0.5D, (double)this.targetTile.zCoord + 0.5D) <= 6.25D) {
         IInventory inventory = (IInventory)this.targetTile;
         inventory.openInventory();
         if(this.addItemStackToInventory(this.entity.getHeldItem(), inventory) && this.entity.getHeldItem().stackSize == 0) {
            this.entity.setCurrentItemOrArmor(0, (ItemStack)null);
         }

         inventory.closeInventory();
      }

   }

   public boolean addItemStackToInventory(final ItemStack par1ItemStack, IInventory inventory) {
      if(par1ItemStack != null && par1ItemStack.stackSize != 0 && par1ItemStack.getItem() != null) {
         try {
            int throwable;
            if(par1ItemStack.isItemDamaged()) {
               throwable = this.getFirstEmptyStack(inventory);
               if(throwable >= 0) {
                  inventory.setInventorySlotContents(throwable, ItemStack.copyItemStack(par1ItemStack));
                  par1ItemStack.stackSize = 0;
                  par1ItemStack.animationsToGo = 5;
                  return true;
               } else {
                  return false;
               }
            } else {
               do {
                  throwable = par1ItemStack.stackSize;
                  par1ItemStack.stackSize = this.storePartialItemStack(par1ItemStack, inventory);
               } while(par1ItemStack.stackSize > 0 && par1ItemStack.stackSize < throwable);

               return par1ItemStack.stackSize < throwable;
            }
         } catch (Throwable var6) {
            CrashReport crashreport = CrashReport.makeCrashReport(var6, "Adding item to inventory");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
            crashreportcategory.addCrashSection("Item ID", Integer.valueOf(Item.getIdFromItem(par1ItemStack.getItem())));
            crashreportcategory.addCrashSection("Item data", Integer.valueOf(par1ItemStack.getItemDamage()));
            crashreportcategory.addCrashSectionCallable("Item name", new Callable() {

               private static final String __OBFID = "CL_00001710";

               public String call() {
                  return par1ItemStack.getDisplayName();
               }
            });
            throw new ReportedException(crashreport);
         }
      } else {
         return false;
      }
   }

   public int getFirstEmptyStack(IInventory inventory) {
      for(int i = 0; i < inventory.getSizeInventory(); ++i) {
         if(inventory.getStackInSlot(i) == null) {
            return i;
         }
      }

      return -1;
   }

   private int storePartialItemStack(ItemStack par1ItemStack, IInventory inventory) {
      Item item = par1ItemStack.getItem();
      int i = par1ItemStack.stackSize;
      int j;
      if(par1ItemStack.getMaxStackSize() == 1) {
         j = this.getFirstEmptyStack(inventory);
         if(j < 0) {
            return i;
         } else {
            if(inventory.getStackInSlot(j) == null) {
               inventory.setInventorySlotContents(j, ItemStack.copyItemStack(par1ItemStack));
            }

            return 0;
         }
      } else {
         j = this.storeItemStack(par1ItemStack, inventory);
         if(j < 0) {
            j = this.getFirstEmptyStack(inventory);
         }

         if(j < 0) {
            return i;
         } else {
            if(inventory.getStackInSlot(j) == null) {
               inventory.setInventorySlotContents(j, new ItemStack(item, 0, par1ItemStack.getItemDamage()));
               if(par1ItemStack.hasTagCompound()) {
                  inventory.getStackInSlot(j).setTagCompound((NBTTagCompound)par1ItemStack.getTagCompound().copy());
               }
            }

            int k = i;
            if(i > inventory.getStackInSlot(j).getMaxStackSize() - inventory.getStackInSlot(j).stackSize) {
               k = inventory.getStackInSlot(j).getMaxStackSize() - inventory.getStackInSlot(j).stackSize;
            }

            if(k > 64 - inventory.getStackInSlot(j).stackSize) {
               k = 64 - inventory.getStackInSlot(j).stackSize;
            }

            if(k == 0) {
               return i;
            } else {
               i -= k;
               ItemStack var10000 = inventory.getStackInSlot(j);
               var10000.stackSize += k;
               inventory.getStackInSlot(j).animationsToGo = 5;
               return i;
            }
         }
      }
   }

   private int storeItemStack(ItemStack par1ItemStack, IInventory inventory) {
      for(int i = 0; i < inventory.getSizeInventory(); ++i) {
         if(inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).getItem() == par1ItemStack.getItem() && inventory.getStackInSlot(i).isStackable() && inventory.getStackInSlot(i).stackSize < inventory.getStackInSlot(i).getMaxStackSize() && inventory.getStackInSlot(i).stackSize < 64 && (!inventory.getStackInSlot(i).getHasSubtypes() || inventory.getStackInSlot(i).getItemDamage() == par1ItemStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(inventory.getStackInSlot(i), par1ItemStack)) {
            return i;
         }
      }

      return -1;
   }
}
