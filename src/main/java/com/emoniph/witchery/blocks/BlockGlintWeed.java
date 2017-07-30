package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseBush;
import com.emoniph.witchery.util.ParticleEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockGlintWeed extends BlockBaseBush {

   public BlockGlintWeed() {
      super(Material.plants);
      this.setLightLevel(0.9375F);
      this.setHardness(0.0F);
      this.setStepSound(Block.soundTypeGrass);
      this.disableStats();
      this.setTickRandomly(true);
      float f = 0.45F;
      this.setBlockBounds(0.050000012F, 0.0F, 0.050000012F, 0.95F, 1.0F, 0.95F);
   }

   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if(!par1World.isRemote && par1World.rand.nextInt(6) == 0) {
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

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      double d0 = (double)((float)x + 0.4F + (float)rand.nextInt(3) * 0.1F);
      double d1 = (double)((float)y + 0.4F + (float)rand.nextInt(3) * 0.1F);
      double d2 = (double)((float)z + 0.4F + (float)rand.nextInt(3) * 0.1F);
      world.spawnParticle(ParticleEffect.FLAME.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
   }

   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      return super.canPlaceBlockAt(par1World, par2, par3, par4) && this.canBlockStay(par1World, par2, par3, par4);
   }

   protected boolean canPlaceBlockOn(Block block) {
      return block != null && block.isOpaqueCube();
   }

   public boolean canBlockSpread(World world, int posX, int posY, int posZ) {
      Block block = world.getBlock(posX, posY - 1, posZ);
      return this.canBlockStay(world, posX, posY, posZ) && (block == Blocks.dirt || block == Blocks.grass || block == Blocks.mycelium || block == Blocks.sand || block == Blocks.farmland);
   }

   public boolean canBlockStay(World world, int posX, int posY, int posZ) {
      Material material = world.getBlock(posX, posY - 1, posZ).getMaterial();
      Material material2 = world.getBlock(posX, posY + 1, posZ).getMaterial();
      return material != null && material.isSolid() || material2 != null && material2.isSolid();
   }
}
