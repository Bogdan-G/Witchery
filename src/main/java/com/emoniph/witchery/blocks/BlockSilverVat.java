package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSilverVat extends BlockBaseContainer {

   public static final ItemStack GOLD_INGOT = new ItemStack(Items.gold_ingot);


   public BlockSilverVat() {
      super(Material.iron, BlockSilverVat.TileEntitySilverVat.class);
      this.setHardness(8.0F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.64F, 1.0F);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public void onBlockAdded(World world, int x, int y, int z) {
      super.onBlockAdded(world, x, y, z);
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
      TileEntity tile = world.getTileEntity(x, y, z);
      return tile != null && tile instanceof IInventory?Container.calcRedstoneFromInventory((IInventory)tile):0;
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(!world.isRemote) {
         BlockSilverVat.TileEntitySilverVat tile = (BlockSilverVat.TileEntitySilverVat)BlockUtil.getTileEntity(world, x, y, z, BlockSilverVat.TileEntitySilverVat.class);
         if(tile != null) {
            ItemStack stack = tile.getStackInSlot(0);
            if(stack != null) {
               EntityItem entity = new EntityItem(world, player.posX, player.posY + 1.0D, player.posZ, stack);
               entity.motionX = entity.motionY = entity.motionZ = 0.0D;
               world.spawnEntityInWorld(entity);
               tile.setInventorySlotContents(0, (ItemStack)null);
               tile.markBlockForUpdate(true);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void onNeighborChange(IBlockAccess world, int x, int y, int z, int tileX, int tileY, int tileZ) {
      if(y == tileY && (x == tileX || z == tileZ)) {
         BlockSilverVat.TileEntitySilverVat vat = (BlockSilverVat.TileEntitySilverVat)BlockUtil.getTileEntity(world, x, y, z, BlockSilverVat.TileEntitySilverVat.class);
         if(vat != null && !vat.reenterLock) {
            vat.reenterLock = true;

            try {
               TileEntity tile = world.getTileEntity(tileX, tileY, tileZ);
               if(tile != null && tile instanceof ISidedInventory) {
                  ISidedInventory inv = (ISidedInventory)tile;
                  int offsetX = x - tileX;
                  int offsetZ = z - tileZ;
                  int side = offsetX == 0?(offsetZ > 0?3:2):(offsetX > 0?5:4);

                  for(int slot = 0; slot < inv.getSizeInventory(); ++slot) {
                     if(inv.canExtractItem(slot, GOLD_INGOT, side) && !inv.canInsertItem(slot, GOLD_INGOT, side)) {
                        ItemStack stack = inv.getStackInSlot(slot);
                        if(stack != null && stack.getItem() == GOLD_INGOT.getItem()) {
                           if(stack.stackSize > vat.getLastStackSizeForSide(side) && vat.getWorldObj().rand.nextInt(5) == 0) {
                              ItemStack silver = vat.getStackInSlot(0);
                              if(silver == null) {
                                 silver = Witchery.Items.GENERIC.itemSilverDust.createStack();
                                 vat.setInventorySlotContents(0, silver);
                                 vat.markBlockForUpdate(true);
                              } else if(silver.stackSize < silver.getMaxStackSize()) {
                                 ++silver.stackSize;
                                 vat.markBlockForUpdate(true);
                              }
                           }

                           vat.setLastStackSizeForSide(side, stack.stackSize);
                           break;
                        }
                     }
                  }
               }
            } finally {
               vat.reenterLock = false;
            }
         }
      }

   }


   public static class TileEntitySilverVat extends TileEntity implements IInventory {

      private ItemStack[] slots = new ItemStack[1];
      private final int[] sides = new int[6];
      private boolean reenterLock;


      public boolean canUpdate() {
         return false;
      }

      public void markBlockForUpdate(boolean notifyNeighbours) {
         super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         if(notifyNeighbours && super.worldObj != null) {
            super.worldObj.notifyBlockChange(super.xCoord, super.yCoord, super.zCoord, this.getBlockType());
         }

      }

      public int getSizeInventory() {
         return this.slots.length;
      }

      public void setLastStackSizeForSide(int side, int stackSize) {
         this.sides[side] = stackSize;
      }

      public int getLastStackSizeForSide(int side) {
         return this.sides[side];
      }

      public ItemStack getStackInSlot(int slot) {
         return this.slots[slot];
      }

      public ItemStack decrStackSize(int slot, int quantity) {
         if(this.slots[slot] != null) {
            ItemStack itemstack;
            if(this.slots[slot].stackSize <= quantity) {
               itemstack = this.slots[slot];
               this.slots[slot] = null;
               return itemstack;
            } else {
               itemstack = this.slots[slot].splitStack(quantity);
               if(this.slots[slot].stackSize == 0) {
                  this.slots[slot] = null;
               }

               return itemstack;
            }
         } else {
            return null;
         }
      }

      public ItemStack getStackInSlotOnClosing(int slot) {
         if(this.slots[slot] != null) {
            ItemStack itemstack = this.slots[slot];
            this.slots[slot] = null;
            return itemstack;
         } else {
            return null;
         }
      }

      public void setInventorySlotContents(int slot, ItemStack stack) {
         this.slots[slot] = stack;
         if(stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
         }

      }

      public String getInventoryName() {
         return this.getBlockType().getLocalizedName();
      }

      public boolean hasCustomInventoryName() {
         return true;
      }

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
         NBTTagList nbtSlotList = nbtRoot.getTagList("Items", 10);
         this.slots = new ItemStack[this.getSizeInventory()];

         for(int i = 0; i < nbtSlotList.tagCount(); ++i) {
            NBTTagCompound nbtSlot = nbtSlotList.getCompoundTagAt(i);
            byte b0 = nbtSlot.getByte("Slot");
            if(b0 >= 0 && b0 < this.slots.length) {
               this.slots[b0] = ItemStack.loadItemStackFromNBT(nbtSlot);
            }
         }

      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         NBTTagList nbtSlotList = new NBTTagList();

         for(int i = 0; i < this.slots.length; ++i) {
            if(this.slots[i] != null) {
               NBTTagCompound nbtSlot = new NBTTagCompound();
               nbtSlot.setByte("Slot", (byte)i);
               this.slots[i].writeToNBT(nbtSlot);
               nbtSlotList.appendTag(nbtSlot);
            }
         }

         nbtRoot.setTag("Items", nbtSlotList);
      }

      public int getInventoryStackLimit() {
         return 64;
      }

      public void markDirty() {
         super.markDirty();
         if(!super.worldObj.isRemote) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         }

      }

      public Packet getDescriptionPacket() {
         NBTTagCompound nbtTag = new NBTTagCompound();
         this.writeToNBT(nbtTag);
         return new S35PacketUpdateTileEntity(super.xCoord, super.yCoord, super.zCoord, 1, nbtTag);
      }

      public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
         super.onDataPacket(net, packet);
         this.readFromNBT(packet.func_148857_g());
         super.worldObj.func_147479_m(super.xCoord, super.yCoord, super.zCoord);
      }

      public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
         return super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) != this?false:par1EntityPlayer.getDistanceSq((double)super.xCoord + 0.5D, (double)super.yCoord + 0.5D, (double)super.zCoord + 0.5D) <= 64.0D;
      }

      public void openInventory() {}

      public void closeInventory() {}

      public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
         return false;
      }
   }
}
