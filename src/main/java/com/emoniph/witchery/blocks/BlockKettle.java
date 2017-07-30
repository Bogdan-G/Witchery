package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.crafting.KettleRecipes;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class BlockKettle extends BlockBaseContainer {

   static final int POWER_SOURCE_RADIUS = 16;


   public BlockKettle() {
      super(Material.anvil, BlockKettle.TileEntityKettle.class);
      this.setHardness(2.0F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      float f = 0.0625F;
      return AxisAlignedBB.getBoundingBox((double)((float)x + f), (double)y, (double)((float)z + f), (double)((float)(x + 1) - f), (double)((float)(y + 1) - f), (double)((float)(z + 1) - f));
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
      this.func_111046_k(par1World, par2, par3, par4);
   }

   private boolean func_111046_k(World par1World, int par2, int par3, int par4) {
      if(!this.canBlockStay(par1World, par2, par3, par4)) {
         par1World.setBlockToAir(par2, par3, par4);
         return false;
      } else {
         return true;
      }
   }

   public boolean canBlockStay(World world, int x, int y, int z) {
      return true;
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

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return false;
   }

   private static IPowerSource findNewPowerSource(World world, int posX, int posY, int posZ) {
      ArrayList sources = PowerSources.instance() != null?PowerSources.instance().get(world, new Coord(posX, posY, posZ), 16):null;
      return sources != null && sources.size() > 0?((PowerSources.RelativePowerSource)sources.get(0)).source():null;
   }

   private static ItemStack consumeItem(ItemStack stack) {
      if(stack.stackSize == 1) {
         return stack.getItem().hasContainerItem(stack)?stack.getItem().getContainerItem(stack):null;
      } else {
         stack.splitStack(1);
         return stack;
      }
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      BlockKettle.TileEntityKettle tileEntity = (BlockKettle.TileEntityKettle)world.getTileEntity(x, y, z);
      if(tileEntity != null) {
         double d0 = (double)((float)x + 0.45F);
         double d1 = (double)((float)y + 0.4F);
         double d2 = (double)((float)z + 0.5F);
         if(tileEntity.isRuined()) {
            world.spawnParticle(ParticleEffect.LARGE_SMOKE.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
         } else if(tileEntity.isReady()) {
            world.spawnParticle(ParticleEffect.SLIME.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            if(tileEntity.isPowered) {
               world.spawnParticle(ParticleEffect.SPELL.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            } else {
               world.spawnParticle(ParticleEffect.MOB_SPELL.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
         } else if(tileEntity.isBrewing()) {
            world.spawnParticle(ParticleEffect.MOB_SPELL.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
         }
      }

   }

   public boolean tryFillWith(World world, int x, int y, int z, FluidStack fluidStack) {
      if(world.isRemote) {
         return true;
      } else {
         TileEntity tile = world.getTileEntity(x, y, z);
         if(tile != null && tile instanceof BlockKettle.TileEntityKettle) {
            BlockKettle.TileEntityKettle tank = (BlockKettle.TileEntityKettle)tile;
            if(tank != null && tank.canFill(ForgeDirection.UNKNOWN, fluidStack.getFluid())) {
               int qty = tank.fill(ForgeDirection.UNKNOWN, fluidStack, true);
               fluidStack.amount -= qty;
               if(fluidStack.amount < 0) {
                  fluidStack.amount = 0;
               }

               if(qty > 0) {
                  world.markBlockForUpdate(x, y, z);
                  SoundEffect.WATER_SWIM.playAt(world, (double)x, (double)y, (double)z);
               }

               return qty > 0;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int par6, float par7, float par8, float par9) {
      ItemStack current = player.inventory.getCurrentItem();
      if(current != null) {
         TileEntity tile = world.getTileEntity(posX, posY, posZ);
         if(tile == null || !(tile instanceof BlockKettle.TileEntityKettle)) {
            return false;
         }

         BlockKettle.TileEntityKettle tank = (BlockKettle.TileEntityKettle)tile;
         FluidStack liquid;
         if(current.getItem() == Items.glass_bottle && tank.isReady()) {
            if(KettleRecipes.instance().isBrewableBy(tank.furnaceItemStacks[6], player)) {
               liquid = null;

               ItemStack var20;
               try {
                  tank.setConsumeBottle(false);
                  var20 = tank.decrStackSize(6, 1);
               } finally {
                  tank.setConsumeBottle(true);
               }

               double var22 = 0.0D;
               double bonusChance2 = 0.0D;
               if(player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() == Witchery.Items.WITCH_HAT) {
                  var22 += 0.35D;
               } else if(player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() == Witchery.Items.BABAS_HAT) {
                  var22 += 0.25D;
                  bonusChance2 += 0.25D;
               }

               if(!Witchery.Items.GENERIC.itemBrewOfRaising.isMatch(var20) && Witchery.Items.WITCH_ROBES.isRobeWorn(player)) {
                  var22 += 0.35D;
               } else if(Witchery.Items.GENERIC.itemBrewOfRaising.isMatch(var20) && Witchery.Items.NECROMANCERS_ROBES.isRobeWorn(player)) {
                  var22 += 0.35D;
               }

               if(Familiar.hasActiveBrewMasteryFamiliar(player)) {
                  var22 += 0.05D;
                  if(player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() == Witchery.Items.BABAS_HAT) {
                     bonusChance2 += 0.05D;
                  }
               }

               if(var22 > 0.0D && world.rand.nextDouble() <= var22) {
                  var20.stackSize += KettleRecipes.instance().getHatBonus(var20);
               }

               if(bonusChance2 > 0.0D && world.rand.nextDouble() <= bonusChance2) {
                  var20.stackSize += KettleRecipes.instance().getHatBonus(var20);
               }

               if(!world.isRemote) {
                  if(current.stackSize == 1) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, var20);
                     if(player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                     }
                  } else {
                     if(!player.inventory.addItemStackToInventory(var20)) {
                        world.spawnEntityInWorld(new EntityItem(world, (double)posX + 0.5D, (double)posY + 1.5D, (double)posZ + 0.5D, var20));
                     } else if(player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                     }

                     --current.stackSize;
                     if(current.stackSize <= 0) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                     }
                  }
               }

               SoundEffect.WATER_SWIM.playAtPlayer(world, player);
            }

            return true;
         }

         liquid = FluidContainerRegistry.getFluidForFilledItem(current);
         if(liquid != null) {
            if(tank.canFill(ForgeDirection.UNKNOWN, liquid.getFluid())) {
               int var21 = tank.fill(ForgeDirection.UNKNOWN, liquid, true);
               if(var21 != 0 && !player.capabilities.isCreativeMode) {
                  player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(current));
               }

               tank.reset(false);
               SoundEffect.WATER_SWIM.playAtPlayer(world, player);
            }

            return true;
         }

         if(current.getItem() == Witchery.Items.BREW_ENDLESS_WATER) {
            if(this.tryFillWith(world, posX, posY, posZ, new FluidStack(FluidRegistry.WATER, 1000))) {
               current.damageItem(1, player);
            }

            return true;
         }

         FluidStack available = tank.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid;
         if(available != null) {
            ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);
            liquid = FluidContainerRegistry.getFluidForFilledItem(filled);
            if(liquid != null) {
               if(!player.capabilities.isCreativeMode) {
                  if(current.stackSize > 1) {
                     if(!player.inventory.addItemStackToInventory(filled)) {
                        return false;
                     }

                     player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(current));
                  } else {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, consumeItem(current));
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, filled);
                  }
               }

               tank.drain(ForgeDirection.UNKNOWN, liquid.amount, true);
               tank.reset(false);
               SoundEffect.WATER_SWIM.playAtPlayer(world, player);
               return true;
            }
         }
      }

      return false;
   }

   public void onEntityCollidedWithBlock(World world, int posX, int posY, int posZ, Entity entity) {
      if(!world.isRemote && entity instanceof EntityItem) {
         BlockKettle.TileEntityKettle tileEntity = (BlockKettle.TileEntityKettle)world.getTileEntity(posX, posY, posZ);
         if(tileEntity != null) {
            EntityItem itemEntity = (EntityItem)entity;
            if(itemEntity.getEntityItem().getItem() == Items.glass_bottle) {
               ItemStack spaceFound = tileEntity.getStackInSlot(7);
               if(spaceFound == null) {
                  tileEntity.setInventorySlotContents(7, itemEntity.getEntityItem());
                  itemEntity.setDead();
               } else if(spaceFound.stackSize + itemEntity.getEntityItem().stackSize <= tileEntity.getInventoryStackLimit()) {
                  spaceFound.stackSize += itemEntity.getEntityItem().stackSize;
                  tileEntity.setInventorySlotContents(7, spaceFound);
                  itemEntity.setDead();
               }
            } else if(tileEntity.isFilled()) {
               boolean var10 = false;

               for(int i = 0; i < tileEntity.getSizeInventory() - 2; ++i) {
                  if(tileEntity.getStackInSlot(i) == null) {
                     tileEntity.setInventorySlotContents(i, itemEntity.getEntityItem());
                     var10 = true;
                     break;
                  }
               }

               if(!var10 && !tileEntity.isRuined()) {
                  tileEntity.setRuined();
               }

               itemEntity.setDead();
               ParticleEffect.SPLASH.send(SoundEffect.WATER_SPLASH, world, (double)posX + 0.5D, (double)posY + 0.2D, (double)posZ + 0.5D, 0.5D, 0.5D, 5);
            }
         }
      }

   }

   public static class TileEntityKettle extends TileEntityBase implements ISidedInventory, IFluidHandler {

      private static final int RESULT_SLOT = 6;
      private static final int BOTTLE_SLOT = 7;
      private ItemStack[] furnaceItemStacks = new ItemStack[8];
      private boolean isRuined = false;
      private boolean isPowered = false;
      private int color;
      private static final int[] side_slots = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
      private int lastExtractionQuantity = 0;
      private boolean consumeBottles = true;
      private FluidTank tank = new FluidTank(1000);


      public void updateEntity() {
         super.updateEntity();
         if(!super.worldObj.isRemote && !this.isRuined && super.ticks % 20L == 0L && this.isFilled() && (this.someFilled() || this.furnaceItemStacks[6] != null)) {
            boolean sendPacket = false;
            if(super.worldObj.getBlock(super.xCoord, super.yCoord - 1, super.zCoord).getMaterial() != Material.fire) {
               this.isRuined = true;
               this.color = 0;
               this.furnaceItemStacks[6] = null;
            } else if(this.furnaceItemStacks[6] == null) {
               KettleRecipes.KettleRecipe recipe;
               boolean wasPowered;
               float powerNeeded;
               IPowerSource powerSource;
               if(this.allFilled()) {
                  recipe = KettleRecipes.instance().getResult(this.furnaceItemStacks, this.furnaceItemStacks.length - 2, false, super.worldObj);
                  if(recipe == null) {
                     this.color = 0;
                     this.isRuined = true;
                     this.furnaceItemStacks[6] = null;
                  } else {
                     this.color = recipe.getColor();
                     wasPowered = this.isPowered;
                     powerNeeded = recipe.getRequiredPower();
                     if(powerNeeded == 0.0F) {
                        this.isPowered = true;
                     } else {
                        powerSource = BlockKettle.findNewPowerSource(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
                        this.isPowered = powerSource != null && powerSource.consumePower(powerNeeded);
                     }

                     if(this.isPowered) {
                        this.furnaceItemStacks[6] = recipe.getOutput((EntityPlayer)null, true);

                        for(int var6 = 0; var6 < this.furnaceItemStacks.length - 2; ++var6) {
                           this.furnaceItemStacks[var6] = null;
                        }
                     }

                     sendPacket = this.isPowered || wasPowered != this.isPowered;
                  }
               } else {
                  recipe = KettleRecipes.instance().getResult(this.furnaceItemStacks, this.furnaceItemStacks.length - 2, true, super.worldObj);
                  if(recipe != null && recipe.getColor() != 0) {
                     if(recipe.getColor() != this.color) {
                        this.color = recipe.getColor();
                        sendPacket = true;
                     }
                  } else {
                     this.color = 0;
                     this.isRuined = true;
                     this.furnaceItemStacks[6] = null;
                  }

                  if(!this.isRuined) {
                     wasPowered = this.isPowered;
                     powerNeeded = recipe.getRequiredPower();
                     if(powerNeeded == 0.0F) {
                        this.isPowered = true;
                     } else {
                        powerSource = BlockKettle.findNewPowerSource(super.worldObj, super.xCoord, super.yCoord, super.zCoord);
                        this.isPowered = powerSource != null && powerSource.getCurrentPower() >= powerNeeded;
                     }

                     sendPacket = wasPowered != this.isPowered;
                  }
               }
            }

            if(this.isRuined || sendPacket) {
               super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            }
         }

      }

      public void reset(boolean flushWater) {
         if(!super.worldObj.isRemote) {
            Log.instance().debug(String.format("Reset kettle %s", new Object[]{flushWater?"Flush":"No Flush"}));
            if(flushWater) {
               FluidStack i = this.tank.drain(this.tank.getFluidAmount(), true);
               Log.instance().debug(String.format("Drained %d remaining %d of  %d", new Object[]{Integer.valueOf(i.amount), Integer.valueOf(this.tank.getFluidAmount()), Integer.valueOf(this.tank.getCapacity())}));
            }

            this.isRuined = false;
            this.isPowered = false;

            for(int var3 = 0; var3 < this.furnaceItemStacks.length - 1; ++var3) {
               this.furnaceItemStacks[var3] = null;
            }

            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         }

      }

      public boolean allFilled() {
         for(int i = 0; i < this.furnaceItemStacks.length - 2; ++i) {
            if(this.furnaceItemStacks[i] == null) {
               return false;
            }
         }

         return true;
      }

      public boolean someFilled() {
         for(int i = 0; i < this.furnaceItemStacks.length - 2; ++i) {
            if(this.furnaceItemStacks[i] != null) {
               return true;
            }
         }

         return false;
      }

      public int getSizeInventory() {
         return this.furnaceItemStacks.length;
      }

      public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
         Log.instance().debug(String.format("isItemValidForSlot(%d, %s)", new Object[]{Integer.valueOf(slot), itemstack.toString()}));
         ItemStack stackInSlot = this.getStackInSlot(slot);
         return slot == 6?true:(slot == 7?itemstack.getItem() == Items.glass_bottle && (stackInSlot != null?stackInSlot.stackSize:0) + itemstack.stackSize <= this.getInventoryStackLimit():this.getStackInSlot(6) == null && (stackInSlot != null?stackInSlot.stackSize:0) + itemstack.stackSize <= this.getInventoryStackLimit());
      }

      public int[] getAccessibleSlotsFromSide(int var1) {
         return side_slots;
      }

      public boolean canInsertItem(int slot, ItemStack stack, int side) {
         ItemStack stackInSlot = this.getStackInSlot(slot);
         return slot == 6?false:(slot == 7?stack.getItem() == Items.glass_bottle && (stackInSlot != null?stackInSlot.stackSize:0) + stack.stackSize <= this.getInventoryStackLimit():stack.getItem() != Items.glass_bottle && this.getStackInSlot(6) == null && this.isFilled());
      }

      public boolean canExtractItem(int slot, ItemStack stack, int side) {
         Log.instance().debug(String.format("canExtract(%d, %s, %d)", new Object[]{Integer.valueOf(slot), stack.toString(), Integer.valueOf(side)}));
         ItemStack bottles = this.getStackInSlot(7);
         boolean canExtract = slot == 6 && this.isFilled() && this.isReady() && bottles != null && bottles.stackSize >= stack.stackSize;
         if(canExtract) {
            if(!KettleRecipes.instance().isBrewableBy(stack, (EntityPlayer)null)) {
               return false;
            }

            this.lastExtractionQuantity = stack.stackSize;
         }

         return canExtract;
      }

      public int getLiquidColor() {
         return this.color;
      }

      public ItemStack getStackInSlot(int par1) {
         return this.furnaceItemStacks[par1];
      }

      public void setInventorySlotContents(int slot, ItemStack stack) {
         Log.instance().debug("setInventorySlotContents");
         if(slot == 6 && this.consumeBottles) {
            ItemStack resultStack = this.getStackInSlot(6);
            ItemStack bottleStack = this.getStackInSlot(7);
            if(stack == null && resultStack != null && bottleStack != null) {
               bottleStack.stackSize -= resultStack.stackSize;
               if(bottleStack.stackSize <= 0) {
                  this.furnaceItemStacks[7] = null;
               }
            } else if(stack != null && resultStack != null && bottleStack != null) {
               int reduction = resultStack.stackSize - stack.stackSize;
               if(reduction == 0) {
                  reduction = this.lastExtractionQuantity;
               }

               this.lastExtractionQuantity = 0;
               Log.instance().debug(String.format("bottles; %d %s %s", new Object[]{Integer.valueOf(reduction), stack.toString(), resultStack.toString()}));
               bottleStack.stackSize -= reduction;
               if(bottleStack.stackSize <= 0) {
                  this.furnaceItemStacks[7] = null;
               }
            }
         }

         this.furnaceItemStacks[slot] = stack;
         if(stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
         } else if(stack == null && slot == 6) {
            this.reset(true);
            return;
         }

         if(!super.worldObj.isRemote) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         }

      }

      public void setConsumeBottle(boolean consume) {
         this.consumeBottles = consume;
      }

      public ItemStack decrStackSize(int slot, int quantity) {
         Log.instance().debug("decrStackSize");
         if(this.furnaceItemStacks[slot] != null) {
            ItemStack bottles = this.getStackInSlot(7);
            if(this.consumeBottles && bottles != null) {
               bottles.stackSize -= quantity;
            }

            if(bottles != null && bottles.stackSize <= 0) {
               this.furnaceItemStacks[7] = null;
            }

            ItemStack itemstack;
            if(this.furnaceItemStacks[slot].stackSize <= quantity) {
               itemstack = this.furnaceItemStacks[slot];
               this.furnaceItemStacks[slot] = null;
               if(slot == 6) {
                  this.reset(true);
               } else if(!super.worldObj.isRemote) {
                  super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
               }

               return itemstack;
            } else {
               itemstack = this.furnaceItemStacks[slot].splitStack(quantity);
               if(this.furnaceItemStacks[slot].stackSize == 0) {
                  this.furnaceItemStacks[slot] = null;
                  if(slot == 6) {
                     this.reset(true);
                  } else if(!super.worldObj.isRemote) {
                     super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
                  }
               } else if(!super.worldObj.isRemote) {
                  super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
               }

               return itemstack;
            }
         } else {
            return null;
         }
      }

      public ItemStack getStackInSlotOnClosing(int par1) {
         Log.instance().debug("getStackInSlotOnClosing");
         if(this.furnaceItemStacks[par1] != null) {
            ItemStack itemstack = this.furnaceItemStacks[par1];
            this.furnaceItemStacks[par1] = null;
            if(par1 == 6) {
               this.reset(true);
            } else if(!super.worldObj.isRemote) {
               super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            }

            return itemstack;
         } else {
            return null;
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
         if(this.tank.getFluidAmount() > 0) {
            this.tank.drain(this.tank.getFluidAmount(), true);
         }

         this.tank.readFromNBT(par1NBTTagCompound);
         NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items", 10);
         this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

         for(int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");
            if(b0 >= 0 && b0 < this.furnaceItemStacks.length) {
               this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
         }

         this.isRuined = par1NBTTagCompound.getBoolean("Ruined");
         this.isPowered = par1NBTTagCompound.getBoolean("Powered");
         this.color = par1NBTTagCompound.getInteger("LiquidColor");
      }

      public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
         super.writeToNBT(par1NBTTagCompound);
         par1NBTTagCompound.setBoolean("Ruined", this.isRuined);
         par1NBTTagCompound.setBoolean("Powered", this.isPowered);
         par1NBTTagCompound.setInteger("LiquidColor", this.color);
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
         this.tank.writeToNBT(par1NBTTagCompound);
      }

      public int getInventoryStackLimit() {
         return 64;
      }

      public void openInventory() {}

      public void closeInventory() {}

      public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
         return super.worldObj.getTileEntity(super.xCoord, super.yCoord, super.zCoord) != this?false:par1EntityPlayer.getDistanceSq((double)super.xCoord + 0.5D, (double)super.yCoord + 0.5D, (double)super.zCoord + 0.5D) <= 64.0D;
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

      public boolean isFilled() {
         return this.tank.getFluidAmount() == this.tank.getCapacity();
      }

      public boolean isBrewing() {
         return this.isFilled() && this.someFilled() && !this.isRuined();
      }

      public boolean isReady() {
         return !this.isRuined() && this.furnaceItemStacks[6] != null;
      }

      public boolean isRuined() {
         return this.isRuined;
      }

      public void setRuined() {
         this.isRuined = true;
         super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
      }

      public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
         int result = this.tank.fill(resource, doFill);
         return result;
      }

      public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
         return resource != null && resource.isFluidEqual(this.tank.getFluid())?this.tank.drain(resource.amount, doDrain):null;
      }

      public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
         return this.tank.drain(maxDrain, doDrain);
      }

      public boolean canFill(ForgeDirection from, Fluid fluid) {
         return fluid == null?false:fluid.getName().equals(FluidRegistry.WATER.getName());
      }

      public boolean canDrain(ForgeDirection from, Fluid fluid) {
         return fluid == null?false:fluid.getName().equals(FluidRegistry.WATER.getName());
      }

      public FluidTankInfo[] getTankInfo(ForgeDirection from) {
         return new FluidTankInfo[]{this.tank.getInfo()};
      }

      public int bottleCount() {
         ItemStack stack = this.getStackInSlot(7);
         return stack != null?stack.stackSize:0;
      }

   }
}
