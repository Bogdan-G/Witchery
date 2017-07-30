package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.BlockUtil;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWitchDoor extends BlockDoor {

   public BlockWitchDoor() {
      super(Material.wood);
      this.disableStatsThunk();
      this.setStepSound(Block.soundTypeWood);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   public void breakBlock(World world, int posX, int posY, int posZ, Block block, int metadata) {
      if(block == Witchery.Blocks.DOOR_ALDER) {
         int i1 = this.func_150012_g(world, posX, posY, posZ);
         if((i1 & 8) != 0) {
            --posY;
         }

         this.notifyNeighborsOfBlockChange(world, posX, posY, posZ);
      }

      super.breakBlock(world, posX, posY, posZ, block, metadata);
   }

   public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int par6, float par7, float par8, float par9) {
      if(this == Witchery.Blocks.DOOR_ALDER) {
         boolean result = super.onBlockActivated(world, posX, posY, posZ, player, par6, par7, par8, par9);
         int i1 = this.func_150012_g(world, posX, posY, posZ);
         if((i1 & 8) != 0) {
            --posY;
         }

         this.notifyNeighborsOfBlockChange(world, posX, posY, posZ);
         return result;
      } else {
         return this.hasKeyForDoor(world, posX, posY, posZ, player)?super.onBlockActivated(world, posX, posY, posZ, player, par6, par7, par8, par9):false;
      }
   }

   public void onBlockHarvested(World world, int posX, int posY, int posZ, int par5, EntityPlayer player) {
      if(this == Witchery.Blocks.DOOR_ROWAN) {
         ItemStack stack;
         if(this.hasKeyForDoor(world, posX, posY, posZ, player)) {
            stack = Witchery.Items.GENERIC.itemDoorRowan.createStack();
         } else {
            stack = new ItemStack(Items.stick, 24);
         }

         float f = 0.7F;
         double d0 = (double)(world.rand.nextFloat() * 0.7F) + 0.15000000596046448D;
         double d1 = (double)(world.rand.nextFloat() * 0.7F) + 0.15000000596046448D;
         double d2 = (double)(world.rand.nextFloat() * 0.7F) + 0.15000000596046448D;
         EntityItem entityitem = new EntityItem(world, (double)posX + d0, (double)posY + d1, (double)posZ + d2, stack);
         entityitem.delayBeforeCanPickup = 10;
         if(!world.isRemote) {
            world.spawnEntityInWorld(entityitem);
         }
      }

      super.onBlockHarvested(world, posX, posY, posZ, par5, player);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      if(this == Witchery.Blocks.DOOR_ROWAN) {
         ArrayList drops = new ArrayList();
         return drops;
      } else {
         return super.getDrops(world, x, y, z, metadata, fortune);
      }
   }

   private boolean hasKeyForDoor(World world, int posX, int posY, int posZ, EntityPlayer player) {
      for(int slot = 0; slot < player.inventory.mainInventory.length; ++slot) {
         ItemStack itemstack = player.inventory.mainInventory[slot];
         if(itemstack != null) {
            NBTTagCompound nbtTag = itemstack.getTagCompound();
            if(itemstack != null && nbtTag != null) {
               int i1 = this.func_150012_g(world, posX, posY, posZ);
               int i;
               if(Witchery.Items.GENERIC.itemDoorKey.isMatch(itemstack)) {
                  if(nbtTag.hasKey("doorX") && nbtTag.hasKey("doorY") && nbtTag.hasKey("doorZ")) {
                     int keyList = nbtTag.getInteger("doorX");
                     i = nbtTag.getInteger("doorY") + ((i1 & 8) != 0?1:0);
                     int keyTag = nbtTag.getInteger("doorZ");
                     if(keyList == posX && i == posY && keyTag == posZ && (!nbtTag.hasKey("doorD") || nbtTag.getInteger("doorD") == world.provider.dimensionId)) {
                        return true;
                     }
                  }
               } else if(Witchery.Items.GENERIC.itemDoorKeyring.isMatch(itemstack) && nbtTag.hasKey("doorKeys")) {
                  NBTTagList var16 = nbtTag.getTagList("doorKeys", 10);
                  if(var16 != null) {
                     for(i = 0; i < var16.tagCount(); ++i) {
                        NBTTagCompound var17 = var16.getCompoundTagAt(i);
                        if(var17 != null && var17.hasKey("doorX") && var17.hasKey("doorY") && var17.hasKey("doorZ")) {
                           int doorX = var17.getInteger("doorX");
                           int doorY = var17.getInteger("doorY") + ((i1 & 8) != 0?1:0);
                           int doorZ = var17.getInteger("doorZ");
                           if(doorX == posX && doorY == posY && doorZ == posZ && (!var17.hasKey("doorD") || var17.getInteger("doorD") == world.provider.dimensionId)) {
                              return true;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      int l = world.getBlockMetadata(x, y, z);
      if((l & 8) == 0) {
         boolean flag = false;
         if(world.getBlock(x, y + 1, z) != this) {
            world.setBlockToAir(x, y, z);
            flag = true;
         }

         if(!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z)) {
            ;
         }

         if(flag) {
            if(!world.isRemote) {
               this.dropBlockAsItem(world, x, y, z, l, 0);
            }
         } else {
            boolean flag1 = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
            if((flag1 || block.canProvidePower()) && block != this) {
               this.func_150014_a(world, x, y, z, flag1);
            }
         }
      } else {
         super.onNeighborBlockChange(world, x, y, z, block);
      }

   }

   protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack) {
      super.dropBlockAsItem(world, x, y, z, stack);
   }

   public boolean onBlockActivatedNormally(World world, int posX, int posY, int posZ, EntityPlayer player, int par6, float par7, float par8, float par9) {
      boolean result = super.onBlockActivated(world, posX, posY, posZ, player, par6, par7, par8, par9);
      if(this == Witchery.Blocks.DOOR_ALDER) {
         int i1 = this.func_150012_g(world, posX, posY, posZ);
         if((i1 & 8) != 0) {
            --posY;
         }

         this.notifyNeighborsOfBlockChange(world, posX, posY, posZ);
      }

      return result;
   }

   private void notifyNeighborsOfBlockChange(World world, int posX, int posY, int posZ) {
      world.notifyBlocksOfNeighborChange(posX, posY, posZ, this);
      world.notifyBlocksOfNeighborChange(posX, posY - 1, posZ, this);
   }

   public Block disableStatsThunk() {
      return this.disableStats();
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      return block == Witchery.Blocks.DOOR_ALDER?Witchery.Items.GENERIC.itemDoorAlder.createStack():Witchery.Items.GENERIC.itemDoorRowan.createStack();
   }

   public void func_150014_a(World world, int x, int y, int z, boolean par5) {
      if(this != Witchery.Blocks.DOOR_ALDER && !par5) {
         super.func_150014_a(world, x, y, z, par5);
      }

   }

   public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
      return this == Witchery.Blocks.DOOR_ALDER?(this.func_150015_f(world, x, y, z)?15:0):super.isProvidingWeakPower(world, x, y, z, side);
   }

   public int isProvidingStrongPower(IBlockAccess par1IBlockAccess, int posX, int posY, int posZ, int side) {
      return this == Witchery.Blocks.DOOR_ALDER?(side == 1?this.isProvidingWeakPower(par1IBlockAccess, posX, posY, posZ, side):0):super.isProvidingStrongPower(par1IBlockAccess, posX, posY, posZ, side);
   }

   public boolean canProvidePower() {
      return this == Witchery.Blocks.DOOR_ALDER;
   }

   public Item getItemDropped(int metadata, Random rand, int fortune) {
      return (metadata & 8) != 0?null:Witchery.Items.GENERIC;
   }

   public int damageDropped(int metadata) {
      return (metadata & 8) != 0?0:(this == Witchery.Blocks.DOOR_ALDER?Witchery.Items.GENERIC.itemDoorAlder.damageValue:Witchery.Items.GENERIC.itemDoorRowan.damageValue);
   }
}
