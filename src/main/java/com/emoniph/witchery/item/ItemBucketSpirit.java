package com.emoniph.witchery.item;

import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemBucketSpirit extends ItemBase {

   private Block fluidBlock;


   public ItemBucketSpirit() {
      this.fluidBlock = Blocks.air;
      this.setMaxStackSize(1);
      this.setMaxDamage(0);
   }

   public ItemBucketSpirit setFluidBlock(Block fluidBlock) {
      this.fluidBlock = fluidBlock;
      return this;
   }

   public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
      MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);
      if(movingobjectposition == null) {
         return item;
      } else {
         if(movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
            int x = movingobjectposition.blockX;
            int y = movingobjectposition.blockY;
            int z = movingobjectposition.blockZ;
            if(!world.canMineBlock(player, x, y, z)) {
               return item;
            }

            if(movingobjectposition.sideHit == 0) {
               --y;
            }

            if(movingobjectposition.sideHit == 1) {
               ++y;
            }

            if(movingobjectposition.sideHit == 2) {
               --z;
            }

            if(movingobjectposition.sideHit == 3) {
               ++z;
            }

            if(movingobjectposition.sideHit == 4) {
               --x;
            }

            if(movingobjectposition.sideHit == 5) {
               ++x;
            }

            if(!player.canPlayerEdit(x, y, z, movingobjectposition.sideHit, item)) {
               return item;
            }

            if(this.tryPlaceContainedLiquid(world, x, y, z) && !player.capabilities.isCreativeMode) {
               return new ItemStack(Items.bucket);
            }
         }

         return item;
      }
   }

   private boolean tryPlaceContainedLiquid(World world, int x, int y, int z) {
      Material material = world.getBlock(x, y, z).getMaterial();
      boolean flag = !material.isSolid();
      if(!world.isAirBlock(x, y, z) && !flag) {
         return false;
      } else {
         if(!world.isRemote && flag && !material.isLiquid()) {
            world.func_147480_a(x, y, z, true);
         }

         BlockUtil.setBlock(world, x, y, z, this.fluidBlock, 0, 3);
         return true;
      }
   }
}
