package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Log;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class BlockPoppetShelf extends BlockBaseContainer {

   public BlockPoppetShelf() {
      super(Material.rock, BlockPoppetShelf.TileEntityPoppetShelf.class);
      this.setHardness(2.5F);
      this.setStepSound(Block.soundTypeStone);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.51F, 1.0F);
   }

   public void onBlockAdded(World world, int posX, int posY, int posZ) {
      super.onBlockAdded(world, posX, posY, posZ);
      BlockUtil.setBlockDefaultDirection(world, posX, posY, posZ);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
      return false;
   }

   public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int par6, float par7, float par8, float par9) {
      if(world.isRemote) {
         return true;
      } else {
         BlockPoppetShelf.TileEntityPoppetShelf tileEntity = (BlockPoppetShelf.TileEntityPoppetShelf)world.getTileEntity(posX, posY, posZ);
         if(tileEntity != null) {
            player.displayGUIChest(tileEntity);
         }

         return true;
      }
   }

   public void onNeighborBlockChange(World world, int posX, int posY, int posZ, Block par5) {
      boolean flag = world.isBlockIndirectlyGettingPowered(posX, posY, posZ) || world.isBlockIndirectlyGettingPowered(posX, posY + 1, posZ);
      int i1 = world.getBlockMetadata(posX, posY, posZ);
      boolean flag1 = (i1 & 8) != 0;
      if(flag && !flag1) {
         world.scheduleBlockUpdate(posX, posY, posZ, this, this.tickRate(world));
         world.setBlockMetadataWithNotify(posX, posY, posZ, i1 | 8, 4);
      } else if(!flag && flag1) {
         world.setBlockMetadataWithNotify(posX, posY, posZ, i1 & -9, 4);
      }

   }

   public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase entityLiving, ItemStack itemstack) {
      int l = BlockPistonBase.determineOrientation(world, posX, posY, posZ, entityLiving);
      world.setBlockMetadataWithNotify(posX, posY, posZ, l, 2);
   }

   public void breakBlock(World world, int posX, int posY, int posZ, Block par5, int par6) {
      BlockPoppetShelf.TileEntityPoppetShelf tileEntity = (BlockPoppetShelf.TileEntityPoppetShelf)world.getTileEntity(posX, posY, posZ);
      if(tileEntity != null) {
         for(int j1 = 0; j1 < tileEntity.getSizeInventory(); ++j1) {
            ItemStack itemstack = tileEntity.getStackInSlot(j1);
            if(itemstack != null) {
               float f = world.rand.nextFloat() * 0.8F + 0.1F;
               float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
               float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

               while(itemstack.stackSize > 0) {
                  int k1 = world.rand.nextInt(21) + 10;
                  if(k1 > itemstack.stackSize) {
                     k1 = itemstack.stackSize;
                  }

                  itemstack.stackSize -= k1;
                  EntityItem entityitem = new EntityItem(world, (double)((float)posX + f), (double)((float)posY + f1), (double)((float)posZ + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                  if(itemstack.hasTagCompound()) {
                     entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                  }

                  float f3 = 0.05F;
                  entityitem.motionX = (double)((float)world.rand.nextGaussian() * 0.05F);
                  entityitem.motionY = (double)((float)world.rand.nextGaussian() * 0.05F + 0.2F);
                  entityitem.motionZ = (double)((float)world.rand.nextGaussian() * 0.05F);
                  world.spawnEntityInWorld(entityitem);
               }
            }
         }

         world.func_147453_f(posX, posY, posZ, par5);
      }

      super.breakBlock(world, posX, posY, posZ, par5, par6);
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World world, int posX, int posY, int posZ, int par5) {
      return Container.calcRedstoneFromInventory((IInventory)world.getTileEntity(posX, posY, posZ));
   }

   public static class TileEntityPoppetShelf extends TileEntityBase implements IInventory {

      private ItemStack[] contents = new ItemStack[9];
      protected String customName;
      private Ticket chunkTicket;


      protected void initiate() {
         super.initiate();
         if(!super.worldObj.isRemote && this.chunkTicket == null) {
            this.chunkTicket = ForgeChunkManager.requestTicket(Witchery.instance, super.worldObj, Type.NORMAL);
            if(this.chunkTicket != null) {
               this.chunkTicket.getModData().setInteger("poppetX", super.xCoord);
               this.chunkTicket.getModData().setInteger("poppetY", super.yCoord);
               this.chunkTicket.getModData().setInteger("poppetZ", super.zCoord);
               this.forceChunkLoading(this.chunkTicket);
            } else {
               Log.instance().warning(String.format("The poppet shelf at %d, %d, %d failed to register a chunk loader.", new Object[]{Integer.valueOf(super.xCoord), Integer.valueOf(super.yCoord), Integer.valueOf(super.zCoord)}));
            }
         }

      }

      public void forceChunkLoading(Ticket ticket) {
         if(this.chunkTicket == null) {
            this.chunkTicket = ticket;
         }

         try {
            ForgeChunkManager.forceChunk(this.chunkTicket, new ChunkCoordIntPair(super.xCoord >> 4, super.zCoord >> 4));
         } catch (Exception var3) {
            Log.instance().warning(var3, String.format("Unexpected exception occurred forcing chunk loading for popet shelf at %d, %d, %d.", new Object[]{Integer.valueOf(super.xCoord), Integer.valueOf(super.yCoord), Integer.valueOf(super.zCoord)}));
         }

      }

      public void invalidate() {
         if(!super.worldObj.isRemote) {
            if(this.chunkTicket != null) {
               ForgeChunkManager.releaseTicket(this.chunkTicket);
            } else {
               Log.instance().warning(String.format("Chunk loader ticket is null for poppet shelf at %d, %d, %d.", new Object[]{Integer.valueOf(super.xCoord), Integer.valueOf(super.yCoord), Integer.valueOf(super.zCoord)}));
            }
         }

         super.invalidate();
      }

      public int getSizeInventory() {
         return 9;
      }

      public ItemStack getStackInSlot(int slot) {
         return this.contents[slot];
      }

      public ItemStack decrStackSize(int slot, int size) {
         if(this.contents[slot] != null) {
            ItemStack itemstack;
            if(this.contents[slot].stackSize <= size) {
               itemstack = this.contents[slot];
               this.contents[slot] = null;
               this.markDirty();
               return itemstack;
            } else {
               itemstack = this.contents[slot].splitStack(size);
               if(this.contents[slot].stackSize == 0) {
                  this.contents[slot] = null;
               }

               this.markDirty();
               return itemstack;
            }
         } else {
            return null;
         }
      }

      public ItemStack getStackInSlotOnClosing(int slot) {
         if(this.contents[slot] != null) {
            ItemStack itemstack = this.contents[slot];
            this.contents[slot] = null;
            return itemstack;
         } else {
            return null;
         }
      }

      public int getRandomStackFromInventory() {
         int i = -1;
         int j = 1;

         for(int k = 0; k < this.contents.length; ++k) {
            if(this.contents[k] != null && super.worldObj.rand.nextInt(j++) == 0) {
               i = k;
            }
         }

         return i;
      }

      public void setInventorySlotContents(int slot, ItemStack itemstack) {
         this.contents[slot] = itemstack;
         if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
         }

         this.markDirty();
      }

      public int addItem(ItemStack itemstack) {
         for(int i = 0; i < this.contents.length; ++i) {
            if(this.contents[i] == null) {
               this.setInventorySlotContents(i, itemstack);
               return i;
            }
         }

         return -1;
      }

      public void markDirty() {
         super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         super.markDirty();
      }

      public String getInventoryName() {
         return "container.witchery:poppetshelf";
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

      public boolean hasCustomInventoryName() {
         return false;
      }

      public void readFromNBT(NBTTagCompound nbtTag) {
         super.readFromNBT(nbtTag);
         NBTTagList nbttaglist = nbtTag.getTagList("Items", 10);
         this.contents = new ItemStack[this.getSizeInventory()];

         for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;
            if(j >= 0 && j < this.contents.length) {
               this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
         }

         if(nbtTag.hasKey("CustomName")) {
            this.customName = nbtTag.getString("CustomName");
         }

      }

      public void writeToNBT(NBTTagCompound nbtTag) {
         super.writeToNBT(nbtTag);
         NBTTagList nbttaglist = new NBTTagList();

         for(int i = 0; i < this.contents.length; ++i) {
            if(this.contents[i] != null) {
               NBTTagCompound nbttagcompound1 = new NBTTagCompound();
               nbttagcompound1.setByte("Slot", (byte)i);
               this.contents[i].writeToNBT(nbttagcompound1);
               nbttaglist.appendTag(nbttagcompound1);
            }
         }

         nbtTag.setTag("Items", nbttaglist);
         if(this.hasCustomInventoryName()) {
            nbtTag.setString("CustomName", this.customName);
         }

      }

      public int getInventoryStackLimit() {
         return 64;
      }

      public boolean isUseableByPlayer(EntityPlayer player) {
         return super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) != this?false:player.getDistanceSq((double)super.xCoord + 0.5D, (double)super.yCoord + 0.5D, (double)super.zCoord + 0.5D) <= 64.0D;
      }

      public void openInventory() {}

      public void closeInventory() {}

      public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
         return itemstack != null && itemstack.getItem() == Witchery.Items.POPPET;
      }
   }
}
