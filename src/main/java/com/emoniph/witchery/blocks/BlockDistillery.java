package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.crafting.DistilleryRecipes;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SlotClayJar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDistillery extends BlockBaseContainer {

   private final Random furnaceRand = new Random();
   private final boolean isActive;
   private static boolean keepFurnaceInventory;


   public BlockDistillery(boolean burning) {
      super(Material.iron, BlockDistillery.TileEntityDistillery.class);
      super.registerTileEntity = !burning;
      super.registerWithCreateTab = !burning;
      this.isActive = burning;
      this.setHardness(3.5F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      if(burning) {
         this.setLightLevel(0.4F);
      }

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

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister par1IconRegister) {
      super.blockIcon = par1IconRegister.registerIcon(this.getTextureName());
   }

   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
      return Item.getItemFromBlock(Witchery.Blocks.DISTILLERY_IDLE);
   }

   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      super.onBlockAdded(par1World, par2, par3, par4);
      BlockUtil.setBlockDefaultDirection(par1World, par2, par3, par4);
   }

   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      if(par1World.isRemote) {
         return true;
      } else {
         BlockDistillery.TileEntityDistillery tileentityfurnace = (BlockDistillery.TileEntityDistillery)par1World.getTileEntity(par2, par3, par4);
         if(tileentityfurnace != null) {
            par5EntityPlayer.openGui(Witchery.instance, 3, par1World, par2, par3, par4);
         }

         return true;
      }
   }

   public static void updateDistilleryBlockState(boolean par0, World par1World, int par2, int par3, int par4) {
      int l = par1World.getBlockMetadata(par2, par3, par4);
      TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
      keepFurnaceInventory = true;
      if(par0) {
         par1World.setBlock(par2, par3, par4, Witchery.Blocks.DISTILLERY_BURNING);
      } else {
         par1World.setBlock(par2, par3, par4, Witchery.Blocks.DISTILLERY_IDLE);
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
         double d0 = (double)((float)par2 + 0.4F + par5Random.nextFloat() * 0.2F);
         double d1 = (double)((float)par3 + 1.0F + par5Random.nextFloat() * 0.3F);
         double d2 = (double)((float)par4 + 0.4F + par5Random.nextFloat() * 0.2F);
         par1World.spawnParticle(ParticleEffect.INSTANT_SPELL.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
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
         BlockDistillery.TileEntityDistillery tileentityfurnace = (BlockDistillery.TileEntityDistillery)par1World.getTileEntity(par2, par3, par4);
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
      return Container.calcRedstoneFromInventory((IInventory)par1World.getTileEntity(par2, par3, par4));
   }

   public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
      return Item.getItemFromBlock(Witchery.Blocks.DISTILLERY_IDLE);
   }

   public static class TileEntityDistillery extends TileEntityBase implements ISidedInventory {

      private ItemStack[] furnaceItemStacks = new ItemStack[7];
      public int currentItemBurnTime;
      public int furnaceCookTime;
      public int powerLevel;
      static final int COOK_TIME = 800;
      Coord powerSourceCoord;
      static final int POWER_SOURCE_RADIUS = 16;
      static final float POWER_PER_TICK = 0.6F;
      private long lastUpdate = 0L;
      private boolean needUpdate = false;
      private static final int THROTTLE = 20;
      private static final int[] slots_top = new int[]{0, 1, 2};
      private static final int[] slots_bottom = new int[]{0, 1, 2};
      private static final int[] slots_sides = new int[]{0, 1, 2, 3, 4, 5, 6};


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

         this.furnaceCookTime = par1NBTTagCompound.getShort("CookTime");
      }

      public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
         super.writeToNBT(par1NBTTagCompound);
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
         return this.furnaceCookTime * par1 / 800;
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
         boolean flag1 = false;
         if(!super.worldObj.isRemote) {
            boolean cooking = this.furnaceCookTime > 0;
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
                  ++this.furnaceCookTime;
                  if(this.furnaceCookTime == 800) {
                     this.furnaceCookTime = 0;
                     this.smeltItem();
                     flag1 = true;
                  }
               } else {
                  this.powerLevel = 0;
               }
            } else {
               if(super.ticks % 40L == 0L) {
                  powerSource = this.getPowerSource();
                  if(powerSource != null && !powerSource.isLocationEqual(this.powerSourceCoord)) {
                     this.powerSourceCoord = powerSource.getLocation();
                  }

                  this.powerLevel = powerSource == null?0:1;
               }

               this.furnaceCookTime = 0;
            }

            if(cooking != this.furnaceCookTime > 0) {
               BlockDistillery.updateDistilleryBlockState(this.furnaceCookTime > 0 && this.powerLevel > 0, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
               this.lastUpdate = super.ticks;
               this.needUpdate = false;
            } else if(powered != this.powerLevel > 0) {
               if(super.ticks - this.lastUpdate > 20L) {
                  BlockDistillery.updateDistilleryBlockState(this.furnaceCookTime > 0 && this.powerLevel > 0, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
                  this.lastUpdate = super.ticks;
                  this.needUpdate = false;
               } else {
                  this.needUpdate = true;
               }
            } else if(this.needUpdate && super.ticks - this.lastUpdate > 20L) {
               BlockDistillery.updateDistilleryBlockState(this.furnaceCookTime > 0 && this.powerLevel > 0, super.worldObj, super.xCoord, super.yCoord, super.zCoord);
               this.lastUpdate = super.ticks;
               this.needUpdate = false;
            }

            if(flag1) {
               super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            }
         }

      }

      private boolean canSmelt() {
         DistilleryRecipes.DistilleryRecipe recipe = this.getActiveRecipe();
         if(recipe == null) {
            return false;
         } else {
            ItemStack[] itemstacks = recipe.getOutputs();

            for(int i = 0; i < itemstacks.length; ++i) {
               ItemStack current = this.furnaceItemStacks[i + 3];
               if(itemstacks[i] != null && current != null && current.isItemEqual(itemstacks[i])) {
                  int newSize = current.stackSize + itemstacks[i].stackSize;
                  if(newSize > this.getInventoryStackLimit() || newSize > current.getMaxStackSize()) {
                     return false;
                  }
               }
            }

            return true;
         }
      }

      public DistilleryRecipes.DistilleryRecipe getActiveRecipe() {
         if(this.furnaceItemStacks[0] == null && this.furnaceItemStacks[1] == null) {
            return null;
         } else {
            DistilleryRecipes.DistilleryRecipe recipe = DistilleryRecipes.instance().getDistillingResult(this.furnaceItemStacks[0], this.furnaceItemStacks[1], this.furnaceItemStacks[2]);
            return recipe;
         }
      }

      public void smeltItem() {
         if(this.canSmelt()) {
            DistilleryRecipes.DistilleryRecipe recipe = DistilleryRecipes.instance().getDistillingResult(this.furnaceItemStacks[0], this.furnaceItemStacks[1], this.furnaceItemStacks[2]);
            ItemStack[] itemstacks = recipe.getOutputs();

            for(int i = 0; i < itemstacks.length; ++i) {
               int furnaceIndex = i + 3;
               if(itemstacks[i] != null) {
                  if(this.furnaceItemStacks[furnaceIndex] == null) {
                     this.furnaceItemStacks[furnaceIndex] = itemstacks[i].copy();
                  } else if(this.furnaceItemStacks[furnaceIndex].isItemEqual(itemstacks[i])) {
                     this.furnaceItemStacks[furnaceIndex].stackSize += itemstacks[i].stackSize;
                  }
               }
            }

            if(this.furnaceItemStacks[0] != null) {
               --this.furnaceItemStacks[0].stackSize;
               if(this.furnaceItemStacks[0].stackSize <= 0) {
                  this.furnaceItemStacks[0] = null;
               }
            }

            if(this.furnaceItemStacks[1] != null) {
               --this.furnaceItemStacks[1].stackSize;
               if(this.furnaceItemStacks[1].stackSize <= 0) {
                  this.furnaceItemStacks[1] = null;
               }
            }

            if(this.furnaceItemStacks[2] != null) {
               this.furnaceItemStacks[2].stackSize -= recipe.getJars();
               if(this.furnaceItemStacks[2].stackSize <= 0) {
                  this.furnaceItemStacks[2] = null;
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
         return slot > 3?false:(slot == 2?Witchery.Items.GENERIC.itemEmptyClayJar.isMatch(itemstack):!Witchery.Items.GENERIC.itemEmptyClayJar.isMatch(itemstack));
      }

      public int[] getAccessibleSlotsFromSide(int side) {
         return BlockSide.BOTTOM.isEqual(side)?slots_bottom:(BlockSide.TOP.isEqual(side)?slots_top:slots_sides);
      }

      public boolean canInsertItem(int slot, ItemStack itemstack, int par3) {
         return this.isItemValidForSlot(slot, itemstack);
      }

      public boolean canExtractItem(int slot, ItemStack itemstack, int side) {
         return side != 0 && side != 1 && (slot == 3 || slot == 4 || slot == 5 || slot == 6);
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

   }

   public static class ContainerDistillery extends Container {

      private BlockDistillery.TileEntityDistillery furnace;
      private int lastCookTime;
      private int lastPowerLevel;


      public ContainerDistillery(InventoryPlayer par1InventoryPlayer, BlockDistillery.TileEntityDistillery par2TileEntityFurnace) {
         this.furnace = par2TileEntityFurnace;
         this.addSlotToContainer(new Slot(par2TileEntityFurnace, 0, 48, 16));
         this.addSlotToContainer(new Slot(par2TileEntityFurnace, 1, 48, 34));
         this.addSlotToContainer(new SlotClayJar(par2TileEntityFurnace, 2, 48, 54));
         this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 3, 110, 16));
         this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 4, 128, 16));
         this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 5, 110, 34));
         this.addSlotToContainer(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 6, 128, 34));

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
            if(slotIndex >= 3 && slotIndex <= 6) {
               if(!this.mergeItemStack(itemstack1, 7, 43, true)) {
                  return null;
               }

               slot.onSlotChange(itemstack1, itemstack);
            } else if(slotIndex != 1 && slotIndex != 0 && slotIndex != 2) {
               if(FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null) {
                  if(!this.mergeItemStack(itemstack1, 0, 2, false)) {
                     return null;
                  }
               } else if(Witchery.Items.GENERIC.itemEmptyClayJar.isMatch(itemstack1)) {
                  if(!this.mergeItemStack(itemstack1, 2, 3, false)) {
                     return null;
                  }
               } else if(slotIndex >= 7 && slotIndex < 34) {
                  if(!this.mergeItemStack(itemstack1, 34, 43, false)) {
                     return null;
                  }
               } else if(slotIndex >= 34 && slotIndex < 43 && !this.mergeItemStack(itemstack1, 7, 34, false)) {
                  return null;
               }
            } else if(!this.mergeItemStack(itemstack1, 7, 43, false)) {
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
}
