package com.emoniph.witchery.brewing;

import com.emoniph.witchery.util.BlockUtil;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockWitchLilyPad extends BlockLilyPad {

   private static final int[][] DIRECTIONS = new int[][]{{-1, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, -1}, {1, 0}, {1, 1}, {1, 1}};


   public BlockWitchLilyPad() {
      this.setHardness(0.0F);
      this.setStepSound(Block.soundTypeGrass);
   }

   public Block setBlockName(String blockName) {
      this.setCreativeTab((CreativeTabs)null);
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   public void onBlockAdded(World world, int x, int y, int z) {
      world.scheduleBlockUpdate(x, y, z, this, 20);
   }

   public void updateTick(World world, int x, int y, int z, Random rand) {
      if(!world.isRemote) {
         int meta = world.getBlockMetadata(x, y, z);
         if(this.canSpread(meta)) {
            int[] d = DIRECTIONS[world.rand.nextInt(DIRECTIONS.length)];
            if(this.canBlockStay(world, x + d[0], y, z + d[1])) {
               int growth = meta >> 2 & 3;
               int facing = meta & 3;
               if(world.rand.nextInt(growth) == 0) {
                  meta = ((growth - 1 & 3) << 2) + facing;
                  world.setBlockMetadataWithNotify(x, y, z, meta, 3);
               }

               world.setBlock(x + d[0], y, z + d[1], this, (growth - 1 & 3) << 2, 3);
            }

            world.scheduleBlockUpdate(x, y, z, this, 20);
         }
      }

      super.updateTick(world, x, y, z, rand);
   }

   private boolean canSpread(int meta) {
      boolean flag = (meta >> 2 & 3) > 0;
      return flag;
   }

   public int quantityDropped(Random rand) {
      return 1;
   }

   public Item getItemDropped(int metadata, Random rand, int fortune) {
      return rand.nextInt(4) == 0?Item.getItemFromBlock(Blocks.waterlily):null;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return new ItemStack(Blocks.waterlily);
   }

   protected boolean canSilkHarvest() {
      return false;
   }

}
