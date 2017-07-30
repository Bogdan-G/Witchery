package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockAltar;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.crafting.BrazierRecipes;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

public class BlockBrazier extends BlockBaseContainer {

   public BlockBrazier() {
      super(Material.iron, BlockBrazier.TileEntityBrazier.class);
      this.setHardness(3.5F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.95F, 0.8F);
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

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if(tile != null && tile instanceof BlockBrazier.TileEntityBrazier) {
         BlockBrazier.TileEntityBrazier brazier = (BlockBrazier.TileEntityBrazier)tile;
         if(brazier.isBurning()) {
            double d0 = (double)((float)x + 0.4F + (float)rand.nextInt(3) * 0.1F);
            double d1 = (double)((float)y + 1.1F + (float)rand.nextInt(2) * 0.1F);
            double d2 = (double)((float)z + 0.4F + (float)rand.nextInt(3) * 0.1F);
            world.spawnParticle(ParticleEffect.FLAME.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
         }
      }

   }

   public void breakBlock(World world, int x, int y, int z, Block oldBlockID, int oldBlockMetadata) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if(tile != null && tile instanceof BlockBrazier.TileEntityBrazier) {
         BlockBrazier.TileEntityBrazier brazier = (BlockBrazier.TileEntityBrazier)tile;
         if(!brazier.isBurning()) {
            for(int j1 = 0; j1 < brazier.getSizeInventory(); ++j1) {
               ItemStack itemstack = brazier.getStackInSlot(j1);
               this.dropItemFromBrokenBlock(world, x, y, z, itemstack);
               world.func_147453_f(x, y, z, oldBlockID);
            }
         } else {
            this.dropItemFromBrokenBlock(world, x, y, z, Witchery.Items.GENERIC.itemAshWood.createStack());
            world.func_147453_f(x, y, z, oldBlockID);
         }
      }

      super.breakBlock(world, x, y, z, oldBlockID, oldBlockMetadata);
   }

   private void dropItemFromBrokenBlock(World world, int x, int y, int z, ItemStack itemstack) {
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

   public boolean hasComparatorInputOverride() {
      return true;
   }

   public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
      return Container.calcRedstoneFromInventory((IInventory)world.getTileEntity(x, y, z));
   }

   public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int par6, float par7, float par8, float par9) {
      if(world.isRemote) {
         return true;
      } else {
         TileEntity tile = world.getTileEntity(posX, posY, posZ);
         if(tile != null && tile instanceof BlockBrazier.TileEntityBrazier) {
            BlockBrazier.TileEntityBrazier brazier = (BlockBrazier.TileEntityBrazier)tile;
            ItemStack stack = player.getHeldItem();
            if(stack == null) {
               return false;
            }

            if(stack.getItem() == Items.potionitem && stack.getItemDamage() == 0) {
               if(!brazier.isEmpty()) {
                  brazier.reset();
                  if(!player.capabilities.isCreativeMode && player.inventory != null) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.glass_bottle));
                     if(player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                     }
                  }

                  SoundEffect.WATER_SPLASH.playAtPlayer(world, player);
               }
            } else if(stack.getItem() == Items.water_bucket) {
               if(!brazier.isEmpty()) {
                  brazier.reset();
                  if(!player.capabilities.isCreativeMode && player.inventory != null) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Items.bucket));
                     if(player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                     }
                  }

                  SoundEffect.WATER_SPLASH.playAtPlayer(world, player);
               }
            } else {
               if(stack.getItem() == Items.flint_and_steel) {
                  if(!brazier.isEmpty()) {
                     brazier.begin();
                  }

                  return false;
               }

               boolean added = false;

               for(int i = 0; i < brazier.getSizeInventory() - 1; ++i) {
                  if(brazier.getStackInSlot(i) == null) {
                     if(!player.capabilities.isCreativeMode && player.inventory != null) {
                        ItemStack newStack = stack.splitStack(1);
                        brazier.setInventorySlotContents(i, newStack);
                        if(stack.stackSize == 0) {
                           player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                        }
                     } else {
                        brazier.setInventorySlotContents(i, new ItemStack(stack.getItem(), 1, stack.getItemDamage()));
                     }

                     added = true;
                     break;
                  }
               }
            }
         }

         return true;
      }
   }

   public static void tryIgnite(World world, int x, int y, int z) {
      TileEntity tile = world.getTileEntity(x, y, z);
      if(tile != null && tile instanceof BlockBrazier.TileEntityBrazier) {
         BlockBrazier.TileEntityBrazier brazier = (BlockBrazier.TileEntityBrazier)tile;
         if(!brazier.isEmpty()) {
            brazier.begin();
         }
      }

   }

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
      boolean flag = par1World.isBlockIndirectlyGettingPowered(par2, par3, par4);
      TileEntity tile = par1World.getTileEntity(par2, par3, par4);
      if(tile != null && tile instanceof BlockBrazier.TileEntityBrazier) {
         BlockBrazier.TileEntityBrazier brazier = (BlockBrazier.TileEntityBrazier)tile;
         if(brazier.previousRedstoneState != flag && flag && !brazier.isEmpty()) {
            brazier.begin();
         }

         brazier.previousRedstoneState = flag;
      }

   }

   public static class TileEntityBrazier extends TileEntityBase implements ISidedInventory {

      private ItemStack[] slots = new ItemStack[4];
      private int furnaceCookTime = 0;
      public boolean previousRedstoneState;
      private Coord powerSourceCoord;
      private static final int POWER_SOURCE_RADIUS = 16;
      private static final float POWER_PER_TICK = 1.0F;
      public int powerLevel;
      private long storage;
      private static final int SLOT_1 = 0;
      private static final int SLOT_2 = 1;
      private static final int SLOT_3 = 2;
      private static final int SLOT_RESULT = 3;
      private static final int[] slots_top = new int[]{0, 1, 2};
      private static final int[] slots_bottom = new int[]{0, 1, 2};
      private static final int[] slots_sides = new int[]{0, 1, 2};


      public int getSizeInventory() {
         return this.slots.length;
      }

      public void begin() {
         this.setInventorySlotContents(3, Witchery.Items.GENERIC.itemAshWood.createStack());
      }

      public ItemStack getStackInSlot(int slot) {
         return this.slots[slot];
      }

      public boolean isBurning() {
         for(int i = 0; i < this.getSizeInventory(); ++i) {
            if(this.getStackInSlot(i) == null) {
               return false;
            }
         }

         return true;
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

      public boolean isFull() {
         if(this.getStackInSlot(3) != null) {
            return true;
         } else {
            for(int slot = 0; slot < 3; ++slot) {
               if(this.getStackInSlot(slot) == null) {
                  return false;
               }
            }

            return true;
         }
      }

      public boolean isEmpty() {
         for(int slot = 0; slot < 3; ++slot) {
            if(this.getStackInSlot(slot) != null) {
               return false;
            }
         }

         return true;
      }

      public int getIngredientCount() {
         int count = 0;

         for(int slot = 0; slot < 3; ++slot) {
            if(this.getStackInSlot(slot) != null) {
               ++count;
            }
         }

         return count;
      }

      public void reset() {
         for(int slot = 0; slot < this.getSizeInventory(); ++slot) {
            this.setInventorySlotContents(slot, (ItemStack)null);
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

         this.markDirty();
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
         this.storage = nbtRoot.getLong("PowerStorage");
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         nbtRoot.setShort("CookTime", (short)this.furnaceCookTime);
         nbtRoot.setShort("PowerLevel", (short)this.powerLevel);
         nbtRoot.setLong("PowerStorage", this.storage);
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

      private IPowerSource getPowerSource() {
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
            BrazierRecipes.BrazierRecipe recipe = BrazierRecipes.instance().getRecipe(new ItemStack[]{this.slots[0], this.slots[1], this.slots[2]});
            IPowerSource powerSource;
            if(recipe != null && this.getStackInSlot(3) != null) {
               powerSource = this.getPowerSource();
               if(powerSource != null && !powerSource.isLocationEqual(this.powerSourceCoord)) {
                  this.powerSourceCoord = powerSource.getLocation();
               } else {
                  this.powerSourceCoord = null;
               }

               boolean needsPower = recipe.getNeedsPower();
               this.powerLevel = needsPower && powerSource == null?0:1;
               if(recipe.getNeedsPower() && (powerSource == null || !powerSource.consumePower(1.0F))) {
                  this.powerLevel = 0;
                  if(powered != this.powerLevel > 0) {
                     update = true;
                  }
               } else {
                  update = this.furnaceCookTime == 0;
                  ++this.furnaceCookTime;
                  if((long)this.furnaceCookTime == (long)recipe.burnTicks + this.storage * 400L) {
                     this.furnaceCookTime = 0;
                     recipe.onBurnt(super.worldObj, super.xCoord, super.yCoord, super.zCoord, super.ticks, this);
                     this.setInventorySlotContents(0, (ItemStack)null);
                     this.setInventorySlotContents(1, (ItemStack)null);
                     this.setInventorySlotContents(2, (ItemStack)null);
                     update = true;
                  } else {
                     this.storage += (long)recipe.onBurning(super.worldObj, super.xCoord, super.yCoord, super.zCoord, super.ticks, this);
                     if(this.storage == Long.MAX_VALUE) {
                        this.storage = 0L;
                     }
                  }

                  if(powered != this.powerLevel > 0) {
                     update = true;
                  }
               }
            } else {
               if(this.getStackInSlot(3) != null) {
                  this.reset();
                  ParticleEffect.SMOKE.send(SoundEffect.RANDOM_FIZZ, super.worldObj, 0.5D + (double)super.xCoord, 1.0D + (double)super.yCoord, 0.5D + (double)super.zCoord, 0.5D, 0.5D, 8);
               }

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

      public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
         return super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) != this?false:par1EntityPlayer.getDistanceSq((double)super.xCoord + 0.5D, (double)super.yCoord + 0.5D, (double)super.zCoord + 0.5D) <= 64.0D;
      }

      public void openInventory() {}

      public void closeInventory() {}

      public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
         return slot != 3 && itemstack != null?(itemstack.stackSize != 1?false:(slot >= 0 && slot < this.slots.length?this.slots[slot] == null:false)):false;
      }

      public int[] getAccessibleSlotsFromSide(int side) {
         return BlockSide.BOTTOM.isEqual(side)?slots_bottom:(BlockSide.TOP.isEqual(side)?slots_top:slots_sides);
      }

      public boolean canInsertItem(int slot, ItemStack itemstack, int par3) {
         return this.isItemValidForSlot(slot, itemstack);
      }

      public boolean canExtractItem(int slot, ItemStack stack, int side) {
         return false;
      }

   }
}
