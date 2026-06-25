package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteRaiseVolcano extends Rite {

   private final int radius;
   private final int height;


   public RiteRaiseVolcano(int radius, int height) {
      this.radius = radius;
      this.height = height;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteRaiseVolcano.StepRaiseVolcano(this, intialStage));
   }

   private static class StepRaiseVolcano extends RitualStep {

      private final RiteRaiseVolcano rite;
      private int stage = 0;


      public StepRaiseVolcano(RiteRaiseVolcano rite, int initialStage) {
         super(true);
         this.rite = rite;
         this.stage = initialStage;
      }

      public int getCurrentStage() {
         return (byte)this.stage;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 15L != 0L) {
            return RitualStep.Result.STARTING;
         } else if(world.isRemote) {
            return RitualStep.Result.COMPLETED;
         } else {
            if(++this.stage == 1) {
               boolean height = false;

               for(int radius = posY; radius > 0 && !height; --radius) {
                  Block y = world.getBlock(posX, radius, posZ);
                  if(y == Blocks.lava && this.surroundedByBlocks(world, posX, radius, posZ, Blocks.lava, 2)) {
                     height = true;
                  } else if(y == Blocks.bedrock) {
                     break;
                  }
               }

               if(!height) {
                  SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
                  RiteRegistry.RiteError("witchery.rite.missinglava", ritual.getInitiatingPlayerName(), world);
                  return RitualStep.Result.ABORTED_REFUND;
               }

               ParticleEffect.PORTAL.send(SoundEffect.RANDOM_FIZZ, world, (double)posX, (double)posY, (double)posZ, 0.5D, 1.0D, 16);
            }

            int var17 = this.rite.height + 4 * ritual.covenSize;
            float var18 = (float)(this.rite.radius + 2 * ritual.covenSize);
            int var20;
            if(this.stage <= var17) {
               for(var20 = 1; var20 <= this.stage; ++var20) {
                  float blockID = var18 - (float)(var17 - this.stage - 1 + var20) * var18 / (float)var17;
                  AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)((float)posX - blockID), (double)(var20 + posY), (double)((float)posZ - blockID), (double)((float)posX + blockID), (double)(var20 + posY), (double)((float)posZ + blockID));
                  this.drawFilledCircle(world, posX, posZ, var20 + posY - 1, Math.max((int)Math.ceil((double)blockID), 1), var20, true);
                  if(this.stage == var17) {
                     int i$ = posY - 1;

                     for(int obj = 0; i$ > posY - 5; ++obj) {
                        this.drawFilledCircle(world, posX, posZ, i$, Math.max((int)var18 - obj, 2), var20, false);
                        --i$;
                     }
                  }

                  Iterator var21 = world.getEntitiesWithinAABB(Entity.class, bounds).iterator();

                  while(var21.hasNext()) {
                     Object var22 = var21.next();
                     Entity entity = (Entity)var22;
                     if(Coord.distance(entity.posX, entity.posY, entity.posZ, (double)posX, (double)(var20 + posY), (double)posZ) <= (double)blockID) {
                        Material material = world.getBlock((int)entity.posX, (int)entity.posY, (int)entity.posZ).getMaterial();
                        if(material.isSolid()) {
                           if(entity instanceof EntityLivingBase) {
                              ((EntityLivingBase)entity).setPositionAndUpdate(entity.posX, entity.posY + 1.0D, entity.posZ);
                           } else {
                              entity.setPosition(entity.posX, entity.posY + 1.0D, entity.posZ);
                           }
                        }
                     }
                  }
               }
            } else {
               if(this.stage >= var17 * 2) {
                  for(var20 = posY; var20 > 0; --var20) {
                     Block var19 = world.getBlock(posX, var20, posZ);
                     if(var19 == Blocks.lava || var19 == Blocks.flowing_lava || var19 == Blocks.bedrock) {
                        while(var19 == Blocks.lava || var19 == Blocks.flowing_lava) {
                           this.setToAirIfLava(world, posX, var20, posZ);
                           this.setToAirIfLava(world, posX + 1, var20, posZ);
                           this.setToAirIfLava(world, posX - 1, var20, posZ);
                           this.setToAirIfLava(world, posX, var20, posZ + 1);
                           this.setToAirIfLava(world, posX, var20, posZ - 1);
                           --var20;
                           var19 = world.getBlock(posX, var20, posZ);
                        }

                        return RitualStep.Result.COMPLETED;
                     }

                     world.setBlockToAir(posX, var20, posZ);
                  }

                  return RitualStep.Result.COMPLETED;
               }

               if(this.stage == var17 * 2 - 1) {
                  world.setBlock(posX, posY + this.stage - var17, posZ, Blocks.flowing_lava);
                  world.setBlock(posX, posY + 1, posZ, Blocks.lava);
                  if(this.rite.radius == 16) {
                     if(world.rand.nextInt(4) == 0) {
                        world.setBlock(posX, posY + 1 + this.stage - var17, posZ, Blocks.flowing_lava);
                     }
                  } else {
                     switch(world.rand.nextInt(8)) {
                     case 0:
                        world.setBlockToAir(posX + 1, posY + var17 - 1, posZ);
                        break;
                     case 1:
                        world.setBlockToAir(posX, posY + var17 - 1, posZ + 1);
                        break;
                     case 2:
                        world.setBlockToAir(posX - 1, posY + var17 - 1, posZ);
                        break;
                     case 3:
                        world.setBlockToAir(posX, posY + var17 - 1, posZ - 1);
                     }
                  }
               } else {
                  world.setBlock(posX, posY + 1, posZ, Blocks.stone);
                  world.setBlock(posX, posY + this.stage - var17, posZ, Blocks.lava);
               }
            }

            return RitualStep.Result.UPKEEP;
         }
      }

      private boolean surroundedByBlocks(World world, int x, int y, int z, Block blockID, int minCount) {
         byte count = 0;
         int count1 = count + (world.getBlock(x, y - 1, z) == blockID?1:0);
         count1 += world.getBlock(x - 1, y, z) == blockID?1:0;
         count1 += world.getBlock(x + 1, y - 1, z) == blockID?1:0;
         count1 += world.getBlock(x, y, z - 1) == blockID?1:0;
         count1 += world.getBlock(x, y, z + 1) == blockID?1:0;
         count1 += world.getBlock(x, y + 1, z + 1) == blockID?1:0;
         return count1 >= minCount;
      }

      private void setToAirIfLava(World world, int posX, int posY, int posZ) {
         Block blockID = world.getBlock(posX, posY, posZ);
         if(blockID == Blocks.lava || blockID == Blocks.flowing_lava) {
            world.setBlockToAir(posX, posY, posZ);
         }

      }

      protected void drawFilledCircle(World world, int x0, int z0, int y, int radius, int height, boolean replaceBlocks) {
         int x = radius;
         int z = 0;
         int radiusError = 1 - radius;

         while(x >= z) {
            this.drawLine(world, -x + x0, x + x0, z + z0, y, x0, z0, radius, height, replaceBlocks);
            this.drawLine(world, -z + x0, z + x0, x + z0, y, x0, z0, radius, height, replaceBlocks);
            this.drawLine(world, -x + x0, x + x0, -z + z0, y, x0, z0, radius, height, replaceBlocks);
            this.drawLine(world, -z + x0, z + x0, -x + z0, y, x0, z0, radius, height, replaceBlocks);
            ++z;
            if(radiusError < 0) {
               radiusError += 2 * z + 1;
            } else {
               --x;
               radiusError += 2 * (z - x + 1);
            }
         }

      }

      protected void drawLine(World world, int x1, int x2, int z, int y, int midX, int midZ, int radius, int height, boolean replaceBlocks) {
         int modX1 = radius > 1 && world.rand.nextInt(5) == 0?x1 + 1:x1;
         int modX2 = radius > 1 && world.rand.nextInt(5) == 0?x2 - 1:x2;
         boolean edgeZ = midZ + radius == z || midZ - radius == z;

         for(int done = modX1; done <= modX2; ++done) {
            if(done != midX || z != midZ) {
               this.drawPixel(world, done, z, y, (done == modX1 || done == modX2 || edgeZ) && height < 3, replaceBlocks);
            }
         }

         boolean var15 = true;
      }

      protected void drawPixel(World world, int x, int z, int y, boolean lower, boolean replaceBlocks) {
         if(replaceBlocks && BlockProtect.canBreak(x, y, z, world) || world.isAirBlock(x, y, z)) {
            world.setBlock(x, y, z, (Block)(lower && world.rand.nextInt(5) != 0?Blocks.grass:Blocks.stone));
         }

      }
   }
}
