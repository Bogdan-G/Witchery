package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockPerpetualIceDoor extends BlockDoor {

   public BlockPerpetualIceDoor() {
      super(Material.ice);
      this.disableStats();
      this.setHardness(2.0F);
      this.setResistance(5.0F);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   @SideOnly(Side.CLIENT)
   public int getRenderBlockPass() {
      return 1;
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      ArrayList ret = new ArrayList();
      int count = this.quantityDropped(metadata, fortune, world.rand);

      for(int i = 0; i < count; ++i) {
         ret.add(Witchery.Items.GENERIC.itemDoorIce.createStack());
      }

      return ret;
   }

   public boolean canPlaceBlockAt(World world, int x, int y, int z) {
      return super.canPlaceBlockAt(world, x, y, z) || world.getBlock(x, y - 1, z) == Witchery.Blocks.PERPETUAL_ICE;
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      int l = world.getBlockMetadata(x, y, z);
      if((l & 8) == 0) {
         boolean flag = false;
         if(world.getBlock(x, y + 1, z) != this) {
            world.setBlockToAir(x, y, z);
            flag = true;
         }

         if(!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !this.canBlockStay(world, x, y - 1, z)) {
            world.setBlockToAir(x, y, z);
            flag = true;
            if(world.getBlock(x, y + 1, z) == this) {
               world.setBlockToAir(x, y + 1, z);
            }
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
         if(world.getBlock(x, y - 1, z) != this) {
            world.setBlockToAir(x, y, z);
         }

         if(block != this) {
            this.onNeighborBlockChange(world, x, y - 1, z, block);
         }
      }

   }
}
