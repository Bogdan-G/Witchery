package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.crafting.SpinningRecipes;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Coord;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSpinningWheel extends BlockBaseContainer {

   public BlockSpinningWheel() {
      super(Material.wood, BlockSpinningWheel.TileEntitySpinningWheel.class);
      this.setHardness(3.5F);
      this.setStepSound(Block.soundTypeWood);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
      BlockUtil.setBlockDefaultDirection(world, x, y, z);
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return true;
      } else {
         TileEntity tile = world.getTileEntity(x, y, z);
         if(tile != null && tile instanceof BlockSpinningWheel.TileEntitySpinningWheel) {
            BlockSpinningWheel.TileEntitySpinningWheel spinningWheel = (BlockSpinningWheel.TileEntitySpinningWheel)tile;
            player.openGui(Witchery.instance, 4, world, x, y, z);
         }

         return true;
      }
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {}

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
      int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      switch(l) {
      case 0:
      default:
         world.setBlockMetadataWithNotify(x, y, z, 2, 2);
         break;
      case 1:
         world.setBlockMetadataWithNotify(x, y, z, 5, 2);
         break;
      case 2:
         world.setBlockMetadataWithNotify(x, y, z, 3, 2);
         break;
      case 3:
         world.setBlockMetadataWithNotify(x, y, z, 4, 2);
      }

   }

   public void breakBlock(World world, int x, int y, int z, Block oldBlockID, int oldBlockMetadata) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if(tile != null && tile instanceof BlockSpinningWheel.TileEntitySpinningWheel) {
         BlockSpinningWheel.TileEntitySpinningWheel tileentityfurnace = (BlockSpinningWheel.TileEntitySpinningWheel)tile;
         if(tileentityfurnace != null) {
            for(int j1 = 0; j1 < tileentityfurnace.getSizeInventory(); ++j1) {
               ItemStack itemstack = tileentityfurnace.getStackInSlot(j1);
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
                     EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
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

            world.func_147453_f(x, y, z, oldBlockID);
         }
      }

      super.breakBlock(world, x, y, z, oldBlockID, oldBlockMetadata);
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
      return Container.calcRedstoneFromInventory((IInventory)world.getTileEntity(x, y, z));
   }

   public static class ContainerSpinningWheel extends Container {

      private BlockSpinningWheel.TileEntitySpinningWheel furnace;
      private int lastCookTime;
      private int lastPowerLevel;


      public ContainerSpinningWheel(InventoryPlayer par1InventoryPlayer, BlockSpinningWheel.TileEntitySpinningWheel par2TileEntityFurnace) {
         this.furnace = par2TileEntityFurnace;
         this.addSlotToContainer(new Slot(par2TileEntityFurnace, 0, 56, 20));
         this.addSlotToContainer(new Slot(par2TileEntityFurnace, 1, 56, 53));
         this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 2, 118, 21));
         this.addSlotToContainer(new Slot(par2TileEntityFurnace, 3, 74, 53));
         this.addSlotToContainer(new Slot(par2TileEntityFurnace, 4, 92, 53));

         int i;
         for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
               this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
         }

         for(i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 142));
         }

      }

      public void addCraftingToCrafters(ICrafting par1ICrafting) {
         super.addCraftingToCrafters(par1ICrafting);
         par1ICrafting.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
         par1ICrafting.sendProgressBarUpdate(this, 1, this.furnace.powerLevel);
      }

      public void detectAndSendChanges() {
         super.detectAndSendChanges();

         for(int i = 0; i < super.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);
            if(this.lastCookTime != this.furnace.furnaceCookTime) {
               icrafting.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
            }

            if(this.lastPowerLevel != this.furnace.powerLevel) {
               icrafting.sendProgressBarUpdate(this, 1, this.furnace.powerLevel);
            }
         }

         this.lastCookTime = this.furnace.furnaceCookTime;
         this.lastPowerLevel = this.furnace.powerLevel;
      }

      @SideOnly(Side.CLIENT)
      public void updateProgressBar(int par1, int par2) {
         if(par1 == 0) {
            this.furnace.furnaceCookTime = par2;
         }

         if(par1 == 1) {
            this.furnace.powerLevel = par2;
         }

      }

      public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
         return this.furnace.isUseableByPlayer(par1EntityPlayer);
      }

      public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
         ItemStack itemstack = null;
         Slot slot = (Slot)super.inventorySlots.get(slotIndex);
         if(slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(slotIndex == 2) {
               if(!this.mergeItemStack(itemstack1, 5, 41, true)) {
                  return null;
               }

               slot.onSlotChange(itemstack1, itemstack);
            } else if(slotIndex != 1 && slotIndex != 0 && slotIndex != 4 && slotIndex != 3) {
               if(SpinningRecipes.instance().findRecipeUsingFibre(itemstack1) != null && (this.furnace.getStackInSlot(0) == null || this.furnace.getStackInSlot(0).isItemEqual(itemstack1))) {
                  if(!this.mergeItemStack(itemstack1, 0, 1, false)) {
                     return null;
                  }
               } else if(SpinningRecipes.instance().findRecipeUsing(itemstack1) != null) {
                  if(!this.mergeItemStack(itemstack1, 1, 2, false) && !this.mergeItemStack(itemstack1, 3, 4, false) && !this.mergeItemStack(itemstack1, 4, 5, false)) {
                     return null;
                  }
               } else if(slotIndex >= 5 && slotIndex < 32) {
                  if(!this.mergeItemStack(itemstack1, 32, 41, false)) {
                     return null;
                  }
               } else if(slotIndex >= 32 && slotIndex < 41 && !this.mergeItemStack(itemstack1, 5, 32, false)) {
                  return null;
               }
            } else if(!this.mergeItemStack(itemstack1, 5, 41, false)) {
               return null;
            }

            if(itemstack1.stackSize == 0) {
               slot.putStack((ItemStack)null);
            } else {
               slot.onSlotChanged();
            }

            if(itemstack1.stackSize == itemstack.stackSize) {
               return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
         }

         return itemstack;
      }
   }

   public static class TileEntitySpinningWheel extends TileEntityBase implements ISidedInventory {

      private ItemStack[] slots = new ItemStack[5];
      public int furnaceCookTime = 0;
      private final int TICKS_PER_SPIN = 20;
      private final int SPINS_PER_STEP = 3;
      private final int STEPS_TO_COMPLETE = 5;
      Coord powerSourceCoord;
      static final int POWER_SOURCE_RADIUS = 16;
      static final float POWER_PER_TICK = 0.6F;
      public int powerLevel;
      private static final int SLOT_TO_SPIN = 0;
      private static final int SLOT_SPUN = 2;
      private static final int SLOT_FUEL = 1;
      private static final int SLOT_BY_PRODUCT = 3;
      private static final int SLOT_JARS = 4;
      private static final int[] slots_top = new int[]{0};
      private static final int[] slots_bottom = new int[]{4, 1, 3};
      private static final int[] slots_sides = new int[]{3, 2, 4, 1};


      public int getSizeInventory() {
         return this.slots.length;
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

         this.furnaceCookTime = nbtRoot.getShort("CookTime");
         this.powerLevel = nbtRoot.getShort("PowerLevel");
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         nbtRoot.setShort("CookTime", (short)this.furnaceCookTime);
         nbtRoot.setShort("PowerLevel", (short)this.powerLevel);
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

      @SideOnly(Side.CLIENT)
      public int getCookProgressScaled(int par1) {
         return this.furnaceCookTime * par1 / this.getTotalCookTime();
      }

      public int getTotalCookTime() {
         boolean time = true;
         return 300;
      }

      public int getCookTime() {
         return this.furnaceCookTime;
      }

      IPowerSource getPowerSource() {
         if(this.powerSourceCoord != null && super.ticks % 100L != 0L) {
            TileEntity tileEntity = this.powerSourceCoord.getBlockTileEntity(super.worldObj);
            if(!(tileEntity instanceof BlockAltar.TileEntityAltar)) {
               return this.findNewPowerSource();
            } else {
               BlockAltar.TileEntityAltar altarTileEntity = (BlockAltar.TileEntityAltar)tileEntity;
               return (IPowerSource)(!altarTileEntity.isValid()?this.findNewPowerSource():altarTileEntity);
            }
         } else {
            return this.findNewPowerSource();
         }
      }

      private IPowerSource findNewPowerSource() {
         ArrayList sources = PowerSources.instance() != null?PowerSources.instance().get(super.worldObj, new Coord(this), 16):null;
         return sources != null && sources.size() > 0?((PowerSources.RelativePowerSource)sources.get(0)).source():null;
      }

      public void updateEntity() {
         super.updateEntity();
         boolean update = false;
         boolean cooking = this.furnaceCookTime > 0;
         if(!super.worldObj.isRemote) {
            boolean powered = this.powerLevel > 0;
            IPowerSource powerSource;
            if(this.canSmelt()) {
               powerSource = this.getPowerSource();
               if(powerSource != null && !powerSource.isLocationEqual(this.powerSourceCoord)) {
                  this.powerSourceCoord = powerSource.getLocation();
               } else {
                  this.powerSourceCoord = null;
               }

               this.powerLevel = powerSource == null?0:1;
               if(powerSource != null && powerSource.consumePower(0.6F)) {
                  update = this.furnaceCookTime == 0;
                  ++this.furnaceCookTime;
                  if(this.furnaceCookTime == this.getTotalCookTime()) {
                     this.furnaceCookTime = 0;
                     this.smeltItem();
                     update = true;
                  }

                  if(powered != this.powerLevel > 0) {
                     update = true;
                  }
               } else {
                  this.powerLevel = 0;
                  if(powered != this.powerLevel > 0) {
                     update = true;
                  }
               }
            } else {
               if(super.ticks % 40L == 0L) {
                  powerSource = this.getPowerSource();
                  if(powerSource != null && !powerSource.isLocationEqual(this.powerSourceCoord)) {
                     this.powerSourceCoord = powerSource.getLocation();
                  }

                  this.powerLevel = powerSource == null?0:1;
               }

               update = this.furnaceCookTime > 0 || powered != this.powerLevel > 0;
               this.furnaceCookTime = 0;
            }
         }

         if(update) {
            this.markDirty();
         }

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

      private boolean canSmelt() {
         if(this.slots[0] == null) {
            return false;
         } else {
            SpinningRecipes.SpinningRecipe recipe = SpinningRecipes.instance().getRecipe(this.slots[0], new ItemStack[]{this.slots[1], this.slots[3], this.slots[4]});
            if(recipe == null) {
               return false;
            } else if(this.slots[2] == null) {
               return true;
            } else {
               ItemStack itemstack = recipe.getResult();
               if(!this.slots[2].isItemEqual(itemstack)) {
                  return false;
               } else {
                  int result = this.slots[2].stackSize + itemstack.stackSize;
                  return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize();
               }
            }
         }
      }

      public void smeltItem() {
         if(this.canSmelt()) {
            SpinningRecipes.SpinningRecipe recipe = SpinningRecipes.instance().getRecipe(this.slots[0], new ItemStack[]{this.slots[1], this.slots[3], this.slots[4]});
            ItemStack itemstack = recipe.getResult();
            if(this.slots[2] == null) {
               this.slots[2] = itemstack.copy();
            } else if(this.slots[2].isItemEqual(itemstack)) {
               this.slots[2].stackSize += itemstack.stackSize;
            }

            this.slots[0].stackSize -= recipe.fibre.stackSize;
            if(this.slots[0].stackSize <= 0) {
               this.slots[0] = null;
            }

            ArrayList available = recipe.getMutableModifiersList();
            this.updateIfContained(available, 1);
            this.updateIfContained(available, 3);
            this.updateIfContained(available, 4);
         }

      }

      private void updateIfContained(ArrayList available, int slot) {
         if(this.slots[slot] != null) {
            for(int i = 0; i < available.size(); ++i) {
               if(((ItemStack)available.get(i)).isItemEqual(this.slots[slot])) {
                  --this.slots[slot].stackSize;
                  if(this.slots[slot].stackSize <= 0) {
                     this.slots[slot] = null;
                  }

                  available.remove(i);
                  return;
               }
            }
         }

      }

      public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
         return super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) != this?false:par1EntityPlayer.getDistanceSq((double)super.xCoord + 0.5D, (double)super.yCoord + 0.5D, (double)super.zCoord + 0.5D) <= 64.0D;
      }

      public void openInventory() {}

      public void closeInventory() {}

      public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
         return slot != 2;
      }

      public int[] getAccessibleSlotsFromSide(int side) {
         return BlockSide.BOTTOM.isEqual(side)?slots_bottom:(BlockSide.TOP.isEqual(side)?slots_top:slots_sides);
      }

      public boolean canInsertItem(int slot, ItemStack itemstack, int par3) {
         return this.isItemValidForSlot(slot, itemstack);
      }

      public boolean canExtractItem(int slot, ItemStack stack, int side) {
         return BlockSide.TOP.isEqual(side)?false:(BlockSide.BOTTOM.isEqual(side)?false:slot == 2);
      }

   }
}
