package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.SlotClayJar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWitchesOven extends BlockBaseContainer {

   private final Random furnaceRand = new Random();
   private final boolean isActive;
   private static boolean keepFurnaceInventory;


   public BlockWitchesOven(boolean burning) {
      super(Material.iron, BlockWitchesOven.TileEntityWitchesOven.class);
      super.registerTileEntity = !burning;
      super.registerWithCreateTab = !burning;
      this.isActive = burning;
      this.setHardness(3.5F);
      this.setStepSound(Block.soundTypeMetal);
      if(this.isActive) {
         this.setLightLevel(0.875F);
      }

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

   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
      return Item.getItemFromBlock(Witchery.Blocks.OVEN_IDLE);
   }

   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      super.onBlockAdded(par1World, par2, par3, par4);
      BlockUtil.setBlockDefaultDirection(par1World, par2, par3, par4);
   }

   public static boolean isOven(Block block) {
      return block == Witchery.Blocks.OVEN_IDLE || block == Witchery.Blocks.OVEN_BURNING;
   }

   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      if(par1World.isRemote) {
         return true;
      } else {
         TileEntity tileentityfurnace = par1World.getTileEntity(par2, par3, par4);
         if(tileentityfurnace != null) {
            par5EntityPlayer.openGui(Witchery.instance, 2, par1World, par2, par3, par4);
         }

         return true;
      }
   }

   public static void updateWitchesOvenBlockState(boolean par0, World par1World, int par2, int par3, int par4) {
      int l = par1World.getBlockMetadata(par2, par3, par4);
      TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
      keepFurnaceInventory = true;
      if(par0) {
         par1World.setBlock(par2, par3, par4, Witchery.Blocks.OVEN_BURNING);
      } else {
         par1World.setBlock(par2, par3, par4, Witchery.Blocks.OVEN_IDLE);
      }

      keepFurnaceInventory = false;
      par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
      if(tileentity != null) {
         tileentity.validate();
         par1World.setTileEntity(par2, par3, par4, tileentity);
      }

   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if(this.isActive) {
         int l = par1World.getBlockMetadata(par2, par3, par4);
         float f = (float)par2 + 0.5F;
         float f1 = (float)par3 + 0.2F + par5Random.nextFloat() * 6.0F / 16.0F;
         float f2 = (float)par4 + 0.5F;
         float f3 = 0.52F;
         float f4 = par5Random.nextFloat() * 0.6F - 0.3F;
         if(l == 4) {
            par1World.spawnParticle("smoke", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle("flame", (double)(f - f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
         } else if(l == 5) {
            par1World.spawnParticle("smoke", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle("flame", (double)(f + f3), (double)f1, (double)(f2 + f4), 0.0D, 0.0D, 0.0D);
         } else if(l == 2) {
            par1World.spawnParticle("smoke", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle("flame", (double)(f + f4), (double)f1, (double)(f2 - f3), 0.0D, 0.0D, 0.0D);
         } else if(l == 3) {
            par1World.spawnParticle("smoke", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D);
            par1World.spawnParticle("flame", (double)(f + f4), (double)f1, (double)(f2 + f3), 0.0D, 0.0D, 0.0D);
         }
      }

   }

   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
      int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      if(l == 0) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
      }

      if(l == 1) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
      }

      if(l == 2) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
      }

      if(l == 3) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
      }

   }

   public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
      if(!keepFurnaceInventory) {
         par1World.getTileEntity(par2, par3, par4);
         BlockWitchesOven.TileEntityWitchesOven tileentityfurnace = (BlockWitchesOven.TileEntityWitchesOven)BlockUtil.getTileEntity(par1World, par2, par3, par4, BlockWitchesOven.TileEntityWitchesOven.class);
         if(tileentityfurnace != null) {
            for(int j1 = 0; j1 < tileentityfurnace.getSizeInventory(); ++j1) {
               ItemStack itemstack = tileentityfurnace.getStackInSlot(j1);
               if(itemstack != null) {
                  float f = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                  float f1 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;
                  float f2 = this.furnaceRand.nextFloat() * 0.8F + 0.1F;

                  while(itemstack.stackSize > 0) {
                     int k1 = this.furnaceRand.nextInt(21) + 10;
                     if(k1 > itemstack.stackSize) {
                        k1 = itemstack.stackSize;
                     }

                     itemstack.stackSize -= k1;
                     EntityItem entityitem = new EntityItem(par1World, (double)((float)par2 + f), (double)((float)par3 + f1), (double)((float)par4 + f2), new ItemStack(itemstack.getItem(), k1, itemstack.getItemDamage()));
                     if(itemstack.hasTagCompound()) {
                        entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                     }

                     float f3 = 0.05F;
                     entityitem.motionX = (double)((float)this.furnaceRand.nextGaussian() * f3);
                     entityitem.motionY = (double)((float)this.furnaceRand.nextGaussian() * f3 + 0.2F);
                     entityitem.motionZ = (double)((float)this.furnaceRand.nextGaussian() * f3);
                     par1World.spawnEntityInWorld(entityitem);
                  }
               }
            }

            par1World.func_147453_f(par2, par3, par4, par5);
         }
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
      TileEntity te = par1World.getTileEntity(par2, par3, par4);
      return te != null && te instanceof IInventory?Container.calcRedstoneFromInventory((IInventory)te):0;
   }

   public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
      return Item.getItemFromBlock(Witchery.Blocks.OVEN_IDLE);
   }

   public static class ContainerWitchesOven extends Container {

      private BlockWitchesOven.TileEntityWitchesOven furnace;
      private int lastCookTime;
      private int lastBurnTime;
      private int lastItemBurnTime;


      public ContainerWitchesOven(InventoryPlayer par1InventoryPlayer, BlockWitchesOven.TileEntityWitchesOven par2TileEntityFurnace) {
         this.furnace = par2TileEntityFurnace;
         this.addSlotToContainer(new Slot(par2TileEntityFurnace, 0, 56, 17));
         this.addSlotToContainer(new Slot(par2TileEntityFurnace, 1, 56, 53));
         this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 2, 118, 21));
         this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 3, 118, 53));
         this.addSlotToContainer(new SlotClayJar(par2TileEntityFurnace, 4, 83, 53));

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
         par1ICrafting.sendProgressBarUpdate(this, 1, this.furnace.furnaceBurnTime);
         par1ICrafting.sendProgressBarUpdate(this, 2, this.furnace.currentItemBurnTime);
      }

      public void detectAndSendChanges() {
         super.detectAndSendChanges();

         for(int i = 0; i < super.crafters.size(); ++i) {
            ICrafting icrafting = (ICrafting)super.crafters.get(i);
            if(this.lastCookTime != this.furnace.furnaceCookTime) {
               icrafting.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
            }

            if(this.lastBurnTime != this.furnace.furnaceBurnTime) {
               icrafting.sendProgressBarUpdate(this, 1, this.furnace.furnaceBurnTime);
            }

            if(this.lastItemBurnTime != this.furnace.currentItemBurnTime) {
               icrafting.sendProgressBarUpdate(this, 2, this.furnace.currentItemBurnTime);
            }
         }

         this.lastCookTime = this.furnace.furnaceCookTime;
         this.lastBurnTime = this.furnace.furnaceBurnTime;
         this.lastItemBurnTime = this.furnace.currentItemBurnTime;
      }

      @SideOnly(Side.CLIENT)
      public void updateProgressBar(int par1, int par2) {
         if(par1 == 0) {
            this.furnace.furnaceCookTime = par2;
         }

         if(par1 == 1) {
            this.furnace.furnaceBurnTime = par2;
         }

         if(par1 == 2) {
            this.furnace.currentItemBurnTime = par2;
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
            if(slotIndex != 2 && slotIndex != 3) {
               if(slotIndex != 1 && slotIndex != 0 && slotIndex != 4) {
                  if(FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
                     if(!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return null;
                     }
                  } else if(TileEntityFurnace.isItemFuel(itemstack1)) {
                     if(!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return null;
                     }
                  } else if(Witchery.Items.GENERIC.itemEmptyClayJar.isMatch(itemstack1)) {
                     if(!this.mergeItemStack(itemstack1, 4, 5, false)) {
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
            } else {
               if(!this.mergeItemStack(itemstack1, 5, 41, true)) {
                  return null;
               }

               slot.onSlotChange(itemstack1, itemstack);
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

   public static class TileEntityWitchesOven extends TileEntity implements ISidedInventory {

      private ItemStack[] furnaceItemStacks = new ItemStack[5];
      public int furnaceBurnTime;
      public int currentItemBurnTime;
      public int furnaceCookTime;
      static final int COOK_TIME = 180;
      private static final double FUNNEL_CHANCE = 0.25D;
      private static final double FILTERED_FUNNEL_CHANCE = 0.3D;
      private static final double DOUBLED_FILTERED_FUNNEL_CHANCE = 0.8D;
      private static final int SLOT_TO_COOK = 0;
      private static final int SLOT_FUEL = 1;
      private static final int SLOT_COOKED = 2;
      private static final int SLOT_BY_PRODUCT = 3;
      private static final int SLOT_JARS = 4;
      private static final int[] slots_top = new int[]{0, 4};
      private static final int[] slots_bottom = new int[]{4, 1};
      private static final int[] slots_sides = new int[]{3, 2, 4, 1};


      public int getSizeInventory() {
         return this.furnaceItemStacks.length;
      }

      public ItemStack getStackInSlot(int par1) {
         return this.furnaceItemStacks[par1];
      }

      public ItemStack decrStackSize(int par1, int par2) {
         if(this.furnaceItemStacks[par1] != null) {
            ItemStack itemstack;
            if(this.furnaceItemStacks[par1].stackSize <= par2) {
               itemstack = this.furnaceItemStacks[par1];
               this.furnaceItemStacks[par1] = null;
               return itemstack;
            } else {
               itemstack = this.furnaceItemStacks[par1].splitStack(par2);
               if(this.furnaceItemStacks[par1].stackSize == 0) {
                  this.furnaceItemStacks[par1] = null;
               }

               return itemstack;
            }
         } else {
            return null;
         }
      }

      public ItemStack getStackInSlotOnClosing(int par1) {
         if(this.furnaceItemStacks[par1] != null) {
            ItemStack itemstack = this.furnaceItemStacks[par1];
            this.furnaceItemStacks[par1] = null;
            return itemstack;
         } else {
            return null;
         }
      }

      public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
         this.furnaceItemStacks[par1] = par2ItemStack;
         if(par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
         }

      }

      public String getInventoryName() {
         return this.getBlockType().getLocalizedName();
      }

      public boolean hasCustomInventoryName() {
         return true;
      }

      public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
         super.readFromNBT(par1NBTTagCompound);
         NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
         this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

         for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");
            if(b0 >= 0 && b0 < this.furnaceItemStacks.length) {
               this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
         }

         this.furnaceBurnTime = par1NBTTagCompound.getShort("BurnTime");
         this.furnaceCookTime = par1NBTTagCompound.getShort("CookTime");
         this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
      }

      public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
         super.writeToNBT(par1NBTTagCompound);
         par1NBTTagCompound.setShort("BurnTime", (short)this.furnaceBurnTime);
         par1NBTTagCompound.setShort("CookTime", (short)this.furnaceCookTime);
         NBTTagList nbttaglist = new NBTTagList();

         for(int i = 0; i < this.furnaceItemStacks.length; ++i) {
            if(this.furnaceItemStacks[i] != null) {
               NBTTagCompound nbttagcompound1 = new NBTTagCompound();
               nbttagcompound1.setByte("Slot", (byte)i);
               this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
               nbttaglist.appendTag(nbttagcompound1);
            }
         }

         par1NBTTagCompound.setTag("Items", nbttaglist);
      }

      public int getInventoryStackLimit() {
         return 64;
      }

      @SideOnly(Side.CLIENT)
      public int getCookProgressScaled(int par1) {
         return this.furnaceCookTime * par1 / this.getCookTime();
      }

      @SideOnly(Side.CLIENT)
      public int getBurnTimeRemainingScaled(int par1) {
         if(this.currentItemBurnTime == 0) {
            this.currentItemBurnTime = 200;
         }

         return this.furnaceBurnTime * par1 / this.currentItemBurnTime;
      }

      public boolean isBurning() {
         return this.furnaceBurnTime > 0;
      }

      public void updateEntity() {
         boolean flag = this.furnaceBurnTime > 0;
         boolean flag1 = false;
         if(this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
         }

         if(!super.worldObj.isRemote) {
            if(this.furnaceBurnTime == 0 && this.canSmelt()) {
               this.currentItemBurnTime = this.furnaceBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
               if(this.furnaceBurnTime > 0) {
                  flag1 = true;
                  if(this.furnaceItemStacks[1] != null) {
                     --this.furnaceItemStacks[1].stackSize;
                     if(this.furnaceItemStacks[1].stackSize == 0) {
                        this.furnaceItemStacks[1] = this.furnaceItemStacks[1].getItem().getContainerItem(this.furnaceItemStacks[1]);
                     }
                  }
               }
            }

            if(this.isBurning() && this.canSmelt()) {
               ++this.furnaceCookTime;
               if(this.furnaceCookTime >= this.getCookTime()) {
                  this.furnaceCookTime = 0;
                  this.smeltItem();
                  flag1 = true;
               }
            } else {
               this.furnaceCookTime = 0;
            }

            if(flag != this.furnaceBurnTime > 0) {
               flag1 = true;
               BlockWitchesOven.updateWitchesOvenBlockState(this.furnaceBurnTime > 0, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
            }
         }

         if(flag1) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         }

      }

      private boolean canSmelt() {
         if(this.furnaceItemStacks[0] == null) {
            return false;
         } else {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if(itemstack == null) {
               return false;
            } else {
               Item item = itemstack.getItem();
               if(item != Items.coal && !(item instanceof ItemFood) && !Witchery.Items.GENERIC.itemAshWood.isMatch(itemstack)) {
                  return false;
               } else if(this.furnaceItemStacks[2] == null) {
                  return true;
               } else if(!this.furnaceItemStacks[2].isItemEqual(itemstack)) {
                  return false;
               } else {
                  int result = this.furnaceItemStacks[2].stackSize + itemstack.stackSize;
                  return result <= this.getInventoryStackLimit() && result <= itemstack.getMaxStackSize();
               }
            }
         }
      }

      public void smeltItem() {
         if(this.canSmelt()) {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if(this.furnaceItemStacks[2] == null) {
               this.furnaceItemStacks[2] = itemstack.copy();
            } else if(this.furnaceItemStacks[2].isItemEqual(itemstack)) {
               this.furnaceItemStacks[2].stackSize += itemstack.stackSize;
            }

            this.generateByProduct(itemstack);
            --this.furnaceItemStacks[0].stackSize;
            if(this.furnaceItemStacks[0].stackSize <= 0) {
               this.furnaceItemStacks[0] = null;
            }
         }

      }

      private int getFumeFunnels() {
         int funnels = 0;
         int meta = super.worldObj.getBlockMetadata(super.xCoord, super.yCoord, super.zCoord);
         switch(meta) {
         case 2:
         case 3:
            funnels += this.isFumeFunnel(super.xCoord - 1, super.yCoord, super.zCoord, meta)?1:0;
            funnels += this.isFumeFunnel(super.xCoord + 1, super.yCoord, super.zCoord, meta)?1:0;
            break;
         case 4:
         case 5:
            funnels += this.isFumeFunnel(super.xCoord, super.yCoord, super.zCoord - 1, meta)?1:0;
            funnels += this.isFumeFunnel(super.xCoord, super.yCoord, super.zCoord + 1, meta)?1:0;
         }

         funnels += this.isFumeFunnel(super.xCoord, super.yCoord + 1, super.zCoord, meta)?1:0;
         return funnels;
      }

      private boolean isFumeFunnel(int xCoord, int yCoord, int zCoord, int meta) {
         Block block = super.worldObj.getBlock(xCoord, yCoord, zCoord);
         return (block == Witchery.Blocks.OVEN_FUMEFUNNEL || block == Witchery.Blocks.OVEN_FUMEFUNNEL_FILTERED) && super.worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == meta;
      }

      private double getFumeFunnelsChance() {
         double funnels = 0.0D;
         switch(super.worldObj.getBlockMetadata(super.xCoord, super.yCoord, super.zCoord)) {
         case 2:
            funnels += this.getFumeFunnelChance(super.xCoord + 1, super.yCoord, super.zCoord, 2);
            funnels += this.getFumeFunnelChance(super.xCoord - 1, super.yCoord, super.zCoord, 2);
            break;
         case 3:
            funnels += this.getFumeFunnelChance(super.xCoord + 1, super.yCoord, super.zCoord, 3);
            funnels += this.getFumeFunnelChance(super.xCoord - 1, super.yCoord, super.zCoord, 3);
            break;
         case 4:
            funnels += this.getFumeFunnelChance(super.xCoord, super.yCoord, super.zCoord + 1, 4);
            funnels += this.getFumeFunnelChance(super.xCoord, super.yCoord, super.zCoord - 1, 4);
            break;
         case 5:
            funnels += this.getFumeFunnelChance(super.xCoord, super.yCoord, super.zCoord + 1, 5);
            funnels += this.getFumeFunnelChance(super.xCoord, super.yCoord, super.zCoord - 1, 5);
         }

         return funnels;
      }

      private double getFumeFunnelChance(int x, int y, int z, int meta) {
         Block block = super.worldObj.getBlock(x, y, z);
         if(block == Witchery.Blocks.OVEN_FUMEFUNNEL) {
            if(super.worldObj.getBlockMetadata(x, y, z) == meta) {
               return 0.25D;
            }
         } else if(block == Witchery.Blocks.OVEN_FUMEFUNNEL_FILTERED && super.worldObj.getBlockMetadata(x, y, z) == meta) {
            return Config.instance().doubleFumeFilterChance?0.8D:0.3D;
         }

         return 0.0D;
      }

      private int getCookTime() {
         int time = 180 - 20 * this.getFumeFunnels();
         return time;
      }

      private void generateByProduct(ItemStack itemstack) {
         try {
            double e = 0.3D;
            double funnels = this.getFumeFunnelsChance();
            Log.instance().debug("" + this.furnaceItemStacks[0] + ": " + this.furnaceItemStacks[0].getItem().getUnlocalizedName());
            if(super.worldObj.rand.nextDouble() <= Math.min(0.3D + funnels, 1.0D) && this.furnaceItemStacks[4] != null) {
               if(this.furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sapling) && this.furnaceItemStacks[0].getItemDamage() != 3) {
                  switch(this.furnaceItemStacks[0].getItemDamage()) {
                  case 0:
                     this.createByProduct(Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(1));
                     break;
                  case 1:
                     this.createByProduct(Witchery.Items.GENERIC.itemHintOfRebirth.createStack(1));
                     break;
                  case 2:
                     this.createByProduct(Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(1));
                  }
               } else if(this.furnaceItemStacks[0].getItem() == Item.getItemFromBlock(Witchery.Blocks.SAPLING)) {
                  switch(this.furnaceItemStacks[0].getItemDamage()) {
                  case 0:
                     this.createByProduct(Witchery.Items.GENERIC.itemWhiffOfMagic.createStack(1));
                     break;
                  case 1:
                     this.createByProduct(Witchery.Items.GENERIC.itemReekOfMisfortune.createStack(1));
                     break;
                  case 2:
                     this.createByProduct(Witchery.Items.GENERIC.itemOdourOfPurity.createStack(1));
                  }
               } else if(this.furnaceItemStacks[0].getUnlocalizedName().equals("tile.bop.saplings") && this.furnaceItemStacks[0].getItemDamage() == 6) {
                  this.createByProduct(Witchery.Items.GENERIC.itemHintOfRebirth.createStack(1));
               } else if(this.furnaceItemStacks[0].hasTagCompound() && this.furnaceItemStacks[0].getTagCompound().hasKey("Genome")) {
                  NBTBase tag = this.furnaceItemStacks[0].getTagCompound().getTag("Genome");
                  if(tag != null && tag instanceof NBTTagCompound) {
                     NBTTagCompound compound = (NBTTagCompound)tag;
                     if(compound.hasKey("Chromosomes") && compound.getTag("Chromosomes") instanceof NBTTagList) {
                        NBTTagList list = compound.getTagList("Chromosomes", 10);
                        if(list != null && list.tagCount() > 0) {
                           NBTTagCompound chromoBase = list.getCompoundTagAt(0);
                           if(chromoBase != null && chromoBase instanceof NBTTagCompound) {
                              NBTTagCompound chromosome = (NBTTagCompound)chromoBase;
                              if(chromosome.hasKey("UID0")) {
                                 String treeType = chromosome.getString("UID0");
                                 if(treeType != null) {
                                    Log.instance().debug("Forestry tree: " + treeType);
                                    if(treeType.equals("forestry.treeOak")) {
                                       this.createByProduct(Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(1));
                                    } else if(treeType.equals("forestry.treeSpruce")) {
                                       this.createByProduct(Witchery.Items.GENERIC.itemHintOfRebirth.createStack(1));
                                    } else if(treeType.equals("forestry.treeBirch")) {
                                       this.createByProduct(Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(1));
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               } else {
                  this.createByProduct(Witchery.Items.GENERIC.itemFoulFume.createStack(1));
               }
            }
         } catch (Throwable var12) {
            Log.instance().warning(var12, "Exception occured while generating a by product from a witches oven");
         }

      }

      private void createByProduct(ItemStack byProduct) {
         boolean BY_PRODUCT_INDEX = true;
         if(this.furnaceItemStacks[3] == null) {
            this.furnaceItemStacks[3] = byProduct;
            if(--this.furnaceItemStacks[4].stackSize <= 0) {
               this.furnaceItemStacks[4] = null;
            }
         } else if(this.furnaceItemStacks[3].isItemEqual(byProduct) && this.furnaceItemStacks[3].stackSize + byProduct.stackSize < this.furnaceItemStacks[3].getMaxStackSize()) {
            this.furnaceItemStacks[3].stackSize += byProduct.stackSize;
            if(--this.furnaceItemStacks[4].stackSize <= 0) {
               this.furnaceItemStacks[4] = null;
            }
         }

      }

      public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
         return super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) != this?false:par1EntityPlayer.getDistanceSq((double)super.xCoord + 0.5D, (double)super.yCoord + 0.5D, (double)super.zCoord + 0.5D) <= 64.0D;
      }

      public void openInventory() {}

      public void closeInventory() {}

      public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
         return slot != 2 && slot != 3?(slot == 1?TileEntityFurnace.isItemFuel(itemstack):(slot == 4?Witchery.Items.GENERIC.itemEmptyClayJar.isMatch(itemstack):slot != 0 || !Witchery.Items.GENERIC.itemEmptyClayJar.isMatch(itemstack))):false;
      }

      public int[] getAccessibleSlotsFromSide(int side) {
         return BlockSide.BOTTOM.isEqual(side)?slots_bottom:(BlockSide.TOP.isEqual(side)?slots_top:slots_sides);
      }

      public boolean canInsertItem(int slot, ItemStack itemstack, int par3) {
         return this.isItemValidForSlot(slot, itemstack);
      }

      public boolean canExtractItem(int slot, ItemStack stack, int side) {
         return BlockSide.TOP.isEqual(side)?false:(BlockSide.BOTTOM.isEqual(side)?slot == 1 && stack.getItem() == Items.bucket:slot == 3 || slot == 2);
      }

   }
}
