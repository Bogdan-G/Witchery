package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Dye;
import com.emoniph.witchery.util.MutableBlock;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class RiteNaturesPower extends Rite {

   private final int radius;
   private final int height;
   private final int duration;
   private final int expanse;


   public RiteNaturesPower(int radius, int height, int duration, int expanse) {
      this.radius = radius;
      this.height = height;
      this.duration = duration;
      this.expanse = expanse;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteNaturesPower.StepNaturesPower(this, intialStage));
   }

   private static class StepNaturesPower extends RitualStep {

      private final RiteNaturesPower rite;
      private int stage = 0;
      private EntityPlayer fakePlayer = null;


      public StepNaturesPower(RiteNaturesPower rite, int initialStage) {
         super(false);
         this.rite = rite;
         this.stage = initialStage;
      }

      public int getCurrentStage() {
         return (byte)this.stage;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else if(!world.isRemote) {
            if(++this.stage >= this.rite.duration + ritual.covenSize * 5) {
               return RitualStep.Result.COMPLETED;
            } else {
               int modradius = this.rite.radius + ritual.covenSize * 2;
               --posY;
               int x = posX - modradius + world.rand.nextInt(modradius * 2);
               int z = posZ - modradius + world.rand.nextInt(modradius * 2);
               int y = -1;
               world.playAuxSFX(2005, posX, posY + 2, posZ, 0);
               Material material = world.getBlock(x, posY, z).getMaterial();
               if(material != null && material.isSolid() && world.isAirBlock(x, posY + 1, z)) {
                  y = posY;
               } else {
                  for(int h = 1; h < this.rite.height; ++h) {
                     material = world.getBlock(x, posY + h, z).getMaterial();
                     if(material != null && material.isSolid() && world.isAirBlock(x, posY + h + 1, z)) {
                        y = posY + h;
                        break;
                     }

                     material = world.getBlock(x, posY - h, z).getMaterial();
                     if(material != null && material.isSolid() && (world.isAirBlock(x, posY - h + 1, z) || world.getBlock(x, posY - h + 1, z) == Blocks.snow)) {
                        y = posY - h;
                        break;
                     }
                  }
               }

               if(y != -1) {
                  world.playAuxSFX(2005, x, y + 1, z, 0);
                  this.drawFilledCircle(world, x, y, z, this.rite.expanse + 1);
               }

               return RitualStep.Result.UPKEEP;
            }
         } else {
            return RitualStep.Result.COMPLETED;
         }
      }

      protected void drawFilledCircle(World world, int x0, int y, int z0, int radius) {
         int x = radius;
         int z = 0;
         int radiusError = 1 - radius;

         while(x >= z) {
            this.drawLine(world, -x + x0, x + x0, z + z0, y, x0, z0, radius);
            this.drawLine(world, -z + x0, z + x0, x + z0, y, x0, z0, radius);
            this.drawLine(world, -x + x0, x + x0, -z + z0, y, x0, z0, radius);
            this.drawLine(world, -z + x0, z + x0, -x + z0, y, x0, z0, radius);
            ++z;
            if(radiusError < 0) {
               radiusError += 2 * z + 1;
            } else {
               --x;
               radiusError += 2 * (z - x + 1);
            }
         }

      }

      protected void drawLine(World world, int x1, int x2, int z, int y, int midX, int midZ, int radius) {
         int modX1 = radius > 1 && world.rand.nextInt(5) == 0?x1 + 1:x1;
         int modX2 = radius > 1 && world.rand.nextInt(5) == 0?x2 - 1:x2;
         boolean edgeZ = midZ + radius == z || midZ - radius == z;

         for(int done = modX1; done <= modX2; ++done) {
            this.drawPixel(world, done, z, y, done == modX1 || done == modX2 || edgeZ);
         }

         boolean var13 = true;
      }

      private boolean isNeighbourBlockID(World world, int x, int y, int z, Block blockID) {
         return world.getBlock(x + 1, y, z) == blockID?true:(world.getBlock(x - 1, y, z) == blockID?true:(world.getBlock(x, y, z + 1) == blockID?true:world.getBlock(x, y, z - 1) == blockID));
      }

      protected void drawPixel(World world, int x, int z, int y, boolean lower) {
         Object blockID = world.getBlock(x, y, z);
         int meta = world.getBlockMetadata(x, y, z);
         boolean wasGrass = blockID == Blocks.grass;
         Material materialAbove = world.getBlock(x, y + 1, z).getMaterial();
         if(materialAbove != null && !materialAbove.isSolid()) {
            if((blockID == Blocks.stone || blockID == Blocks.sand || blockID == Blocks.gravel || Config.instance().canReplaceNaturalBlock((Block)blockID, meta)) && world.rand.nextInt(8) != 0) {
               if(materialAbove != Material.vine && world.rand.nextDouble() <= (this.isNeighbourBlockID(world, x, y, z, Blocks.water)?0.7D:0.02D)) {
                  world.setBlock(x, y, z, Blocks.water);
               } else {
                  world.setBlock(x, y, z, Blocks.grass);
               }

               blockID = Blocks.grass;
            }

            if(materialAbove != Material.vine && blockID != Blocks.air && blockID != Blocks.leaves && blockID != Witchery.Blocks.LEAVES && world.rand.nextInt(4) == 0) {
               MutableBlock[] count = new MutableBlock[]{new MutableBlock(Blocks.sapling, 0), new MutableBlock(Blocks.sapling, 1), new MutableBlock(Blocks.sapling, 2), new MutableBlock(Blocks.sapling, 3), new MutableBlock(Witchery.Blocks.SAPLING, 0), new MutableBlock(Witchery.Blocks.SAPLING, 1), new MutableBlock(Witchery.Blocks.SAPLING, 2), new MutableBlock(Witchery.Blocks.EMBER_MOSS, 0), new MutableBlock(Blocks.tallgrass, 1), new MutableBlock(Blocks.tallgrass, 2), new MutableBlock(Blocks.brown_mushroom), new MutableBlock(Blocks.red_mushroom), new MutableBlock(Blocks.red_flower), new MutableBlock(Blocks.yellow_flower), new MutableBlock(Blocks.tallgrass, 1), new MutableBlock(Blocks.tallgrass, 2), new MutableBlock(Blocks.tallgrass, 1), new MutableBlock(Blocks.tallgrass, 2), new MutableBlock(Blocks.tallgrass, 1), new MutableBlock(Blocks.tallgrass, 2), new MutableBlock(Blocks.tallgrass, 1), new MutableBlock(Blocks.tallgrass, 2), new MutableBlock(Blocks.tallgrass, 1), new MutableBlock(Blocks.tallgrass, 2), new MutableBlock(Blocks.tallgrass, 1), new MutableBlock(Blocks.tallgrass, 2), new MutableBlock(Blocks.pumpkin, 0), new MutableBlock(Blocks.melon_block, 0), new MutableBlock(Witchery.Blocks.GLINT_WEED, 0)};
               count[world.rand.nextInt(count.length)].mutate(world, x, y + 1, z, false);
            }

            if(world.rand.nextInt(3) == 0) {
               int var12 = 0;
               if((this.fakePlayer == null || this.fakePlayer.worldObj != world) && world instanceof WorldServer) {
                  this.fakePlayer = new FakePlayer((WorldServer)world, new GameProfile(UUID.randomUUID(), "[Minecraft]"));
               }

               ItemDye.applyBonemeal(Dye.BONE_MEAL.createStack(), world, x, y + 1, z, this.fakePlayer);

               for(Block saplingBlockID = world.getBlock(x, y + 1, z); (saplingBlockID == Blocks.sapling || saplingBlockID == Witchery.Blocks.SAPLING) && var12++ < 8; saplingBlockID = world.getBlock(x, y + 1, z)) {
                  ItemDye.applyBonemeal(Dye.BONE_MEAL.createStack(), world, x, y + 1, z, this.fakePlayer);
               }
            }
         }

      }
   }
}
