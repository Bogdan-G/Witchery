package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseBush;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.util.Config;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockCotton extends BlockBaseBush {

   public BlockCotton() {
      super(Material.plants);
      this.setHardness(0.0F);
      this.setStepSound(Block.soundTypeGrass).setTickRandomly(true);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      if(world.provider.dimensionId == Config.instance().dimensionDreamID && world.provider instanceof WorldProviderDreamWorld && ((WorldProviderDreamWorld)world.provider).isNightmare()) {
         ArrayList ret = new ArrayList();
         int count = this.quantityDropped(metadata, fortune, world.rand);

         for(int i = 0; i < count; ++i) {
            ret.add(Witchery.Items.GENERIC.itemDisturbedCotton.createStack());
         }

         return ret;
      } else {
         return super.getDrops(world, x, y, z, metadata, fortune);
      }
   }

   public boolean canBlockSpread(World world, int posX, int posY, int posZ) {
      if(world.provider.dimensionId == Config.instance().dimensionDreamID && this.canBlockStay(world, posX, posY, posZ)) {
         Block blockBelow = world.getBlock(posX, posY - 1, posZ);
         return blockBelow == Blocks.dirt || blockBelow == Blocks.grass;
      } else {
         return false;
      }
   }

   private boolean isBlockMatch(World world, int x, int y, int z, Block block, int metadata) {
      return world.getBlock(x, y, z) == block && world.getBlockMetadata(x, y, z) == metadata;
   }

   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if(!par1World.isRemote && par1World.rand.nextInt(6) == 0) {
         if(par1World.provider.dimensionId != Config.instance().dimensionDreamID || !this.isBlockMatch(par1World, par2 + 1, par3 - 1, par4, Witchery.Blocks.FLOWING_SPIRIT, 0) && !this.isBlockMatch(par1World, par2 - 1, par3 - 1, par4, Witchery.Blocks.FLOWING_SPIRIT, 0) && !this.isBlockMatch(par1World, par2, par3 - 1, par4 + 1, Witchery.Blocks.FLOWING_SPIRIT, 0) && !this.isBlockMatch(par1World, par2, par3 - 1, par4 - 1, Witchery.Blocks.FLOWING_SPIRIT, 0)) {
            return;
         }

         byte b0 = 4;
         int l = 5;

         int i1;
         int j1;
         int k1;
         for(i1 = par2 - b0; i1 <= par2 + b0; ++i1) {
            for(j1 = par4 - b0; j1 <= par4 + b0; ++j1) {
               for(k1 = par3 - 1; k1 <= par3 + 1; ++k1) {
                  if(par1World.getBlock(i1, k1, j1) == this) {
                     --l;
                     if(l <= 0) {
                        return;
                     }
                  }
               }
            }
         }

         i1 = par2 + par5Random.nextInt(3) - 1;
         j1 = par3 + par5Random.nextInt(2) - par5Random.nextInt(2);
         k1 = par4 + par5Random.nextInt(3) - 1;

         for(int l1 = 0; l1 < 4; ++l1) {
            if(par1World.isAirBlock(i1, j1, k1) && this.canBlockSpread(par1World, i1, j1, k1)) {
               par2 = i1;
               par3 = j1;
               par4 = k1;
            }

            i1 = par2 + par5Random.nextInt(3) - 1;
            j1 = par3 + par5Random.nextInt(2) - par5Random.nextInt(2);
            k1 = par4 + par5Random.nextInt(3) - 1;
         }

         if(par1World.isAirBlock(i1, j1, k1) && this.canBlockSpread(par1World, i1, j1, k1)) {
            par1World.setBlock(i1, j1, k1, this, 0, 2);
         }
      }

   }
}
