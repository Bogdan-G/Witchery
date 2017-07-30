package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.Witchery;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenWitchTree extends WorldGenerator {

   private final int minTreeHeight;
   private final boolean vinesGrow;
   private final int metaWood;
   private final int metaLeaves;
   private final int spread;


   public WorldGenWitchTree(boolean update, int minHeight, int woodMeta, int leavesMeta, int spread, boolean growVines) {
      super(update);
      this.minTreeHeight = minHeight;
      this.metaWood = woodMeta;
      this.metaLeaves = leavesMeta;
      this.vinesGrow = growVines;
      this.spread = spread;
   }

   public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
      int l = par2Random.nextInt(3) + this.minTreeHeight;
      boolean flag = true;
      if(par4 >= 1 && par4 + l + 1 <= 256) {
         byte b0;
         int j1;
         for(int i1 = par4; i1 <= par4 + 1 + l; ++i1) {
            b0 = 1;
            if(i1 == par4) {
               b0 = 0;
            }

            if(i1 >= par4 + 1 + l - 2) {
               b0 = 2;
            }

            for(int soil = par3 - b0; soil <= par3 + b0 && flag; ++soil) {
               for(j1 = par5 - b0; j1 <= par5 + b0 && flag; ++j1) {
                  if(i1 >= 0 && i1 < 256) {
                     Block isSoil = par1World.getBlock(soil, i1, j1);
                     boolean b1 = par1World.isAirBlock(soil, i1, j1);
                     if(!b1 && !isSoil.isLeaves(par1World, soil, i1, j1) && isSoil != Blocks.grass && isSoil != Blocks.dirt && !isSoil.isWood(par1World, soil, i1, j1)) {
                        flag = false;
                     }
                  } else {
                     flag = false;
                  }
               }
            }
         }

         if(!flag) {
            return false;
         } else {
            Block var21 = par1World.getBlock(par3, par4 - 1, par5);
            boolean var22 = var21 != null && var21.canSustainPlant(par1World, par3, par4 - 1, par5, ForgeDirection.UP, (IPlantable)Witchery.Blocks.SAPLING);
            if(var22 && par4 < 256 - l - 1) {
               var21.onPlantGrow(par1World, par3, par4 - 1, par5, par3, par4, par5);
               b0 = 3;
               byte var23 = 0;

               int k1;
               int i2;
               int k2;
               int j2;
               for(j1 = par4 - b0 + l; j1 <= par4 + l; ++j1) {
                  k1 = j1 - (par4 + l);
                  i2 = var23 + 1 - k1 / 2 + this.spread;

                  for(j2 = par3 - i2; j2 <= par3 + i2; ++j2) {
                     k2 = j2 - par3;

                     for(int block = par5 - i2; block <= par5 + i2; ++block) {
                        int i3 = block - par5;
                        if(Math.abs(k2) != i2 || Math.abs(i3) != i2 || par2Random.nextInt(2) != 0 && k1 != 0) {
                           Block block1 = par1World.getBlock(j2, j1, block);
                           if(block1 == Blocks.air || block1.canBeReplacedByLeaves(par1World, j2, j1, block)) {
                              this.setBlockAndNotifyAdequately(par1World, j2, j1, block, Witchery.Blocks.LEAVES, this.metaLeaves);
                           }
                        }
                     }
                  }
               }

               Block var24;
               for(j1 = 0; j1 < l; ++j1) {
                  var24 = par1World.getBlock(par3, par4 + j1, par5);
                  if(var24 == Blocks.air || var24.isLeaves(par1World, par3, par4 + j1, par5)) {
                     this.setBlockAndNotifyAdequately(par1World, par3, par4 + j1, par5, Witchery.Blocks.LOG, this.metaWood);
                     if(this.vinesGrow && j1 > 0) {
                        if(par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 - 1, par4 + j1, par5)) {
                           this.setBlockAndNotifyAdequately(par1World, par3 - 1, par4 + j1, par5, Blocks.vine, 8);
                        }

                        if(par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 1, par4 + j1, par5)) {
                           this.setBlockAndNotifyAdequately(par1World, par3 + 1, par4 + j1, par5, Blocks.vine, 2);
                        }

                        if(par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + j1, par5 - 1)) {
                           this.setBlockAndNotifyAdequately(par1World, par3, par4 + j1, par5 - 1, Blocks.vine, 1);
                        }

                        if(par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + j1, par5 + 1)) {
                           this.setBlockAndNotifyAdequately(par1World, par3, par4 + j1, par5 + 1, Blocks.vine, 4);
                        }
                     }
                  }
               }

               if(this.vinesGrow) {
                  for(j1 = par4 - 3 + l; j1 <= par4 + l; ++j1) {
                     k1 = j1 - (par4 + l);
                     i2 = 2 - k1 / 2;

                     for(j2 = par3 - i2; j2 <= par3 + i2; ++j2) {
                        for(k2 = par5 - i2; k2 <= par5 + i2; ++k2) {
                           var24 = par1World.getBlock(j2, j1, k2);
                           if(var24 != null && var24.isLeaves(par1World, j2, j1, k2)) {
                              if(par2Random.nextInt(4) == 0 && par1World.isAirBlock(j2 - 1, j1, k2)) {
                                 this.growVines(par1World, j2 - 1, j1, k2, 8);
                              }

                              if(par2Random.nextInt(4) == 0 && par1World.isAirBlock(j2 + 1, j1, k2)) {
                                 this.growVines(par1World, j2 + 1, j1, k2, 2);
                              }

                              if(par2Random.nextInt(4) == 0 && par1World.isAirBlock(j2, j1, k2 - 1)) {
                                 this.growVines(par1World, j2, j1, k2 - 1, 1);
                              }

                              if(par2Random.nextInt(4) == 0 && par1World.isAirBlock(j2, j1, k2 + 1)) {
                                 this.growVines(par1World, j2, j1, k2 + 1, 4);
                              }
                           }
                        }
                     }
                  }

                  if(par2Random.nextInt(5) == 0 && l > 5) {
                     for(j1 = 0; j1 < 2; ++j1) {
                        for(k1 = 0; k1 < 4; ++k1) {
                           if(par2Random.nextInt(4 - j1) == 0) {
                              i2 = par2Random.nextInt(3);
                              this.setBlockAndNotifyAdequately(par1World, par3 + Direction.offsetX[Direction.rotateOpposite[k1]], par4 + l - 5 + j1, par5 + Direction.offsetZ[Direction.rotateOpposite[k1]], Blocks.cocoa, i2 << 2 | k1);
                           }
                        }
                     }
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private void growVines(World par1World, int par2, int par3, int par4, int par5) {
      this.setBlockAndNotifyAdequately(par1World, par2, par3, par4, Blocks.vine, par5);
      int i1 = 4;

      while(true) {
         --par3;
         if(!par1World.isAirBlock(par2, par3, par4) || i1 <= 0) {
            return;
         }

         this.setBlockAndNotifyAdequately(par1World, par2, par3, par4, Blocks.vine, par5);
         --i1;
      }
   }
}
