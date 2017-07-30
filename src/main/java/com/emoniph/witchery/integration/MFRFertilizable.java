package com.emoniph.witchery.integration;

import com.emoniph.witchery.blocks.BlockWitchSapling;
import com.emoniph.witchery.util.Log;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizable;

public class MFRFertilizable implements IFactoryFertilizable {

   private Block block;
   private int stages;


   public MFRFertilizable(Block block, int stages) {
      this.block = block;
      this.stages = stages;
   }

   public Block getPlant() {
      return this.block;
   }

   public boolean canFertilize(World world, int x, int y, int z, FertilizerType fertilizerType) {
      return fertilizerType == FertilizerType.GrowPlant && (this.stages == 0 || world.getBlockMetadata(x, y, z) < this.stages);
   }

   public boolean fertilize(World world, Random rand, int x, int y, int z, FertilizerType fertilizerType) {
      Block blockID = world.getBlock(x, y, z);
      Log.instance().debug(String.format("Fertilize %d, %d", new Object[]{blockID.getUnlocalizedName(), Integer.valueOf(this.stages)}));
      if(this.stages > 0) {
         int meta = world.getBlockMetadata(x, y, z);
         if(meta < this.stages) {
            int output = meta + rand.nextInt(3) + 1;
            if(output > this.stages) {
               output = this.stages;
            }

            world.setBlockMetadataWithNotify(x, y, z, output, 3);
            return true;
         }
      } else if(this.block instanceof BlockWitchSapling) {
         BlockWitchSapling var10000 = (BlockWitchSapling)this.block;
         BlockWitchSapling.growTree(world, x, y, z, world.rand);
         return world.getBlock(x, y, z) != this.block;
      }

      return false;
   }
}
