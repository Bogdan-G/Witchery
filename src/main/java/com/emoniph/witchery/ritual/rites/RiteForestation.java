package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
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

public class RiteForestation extends Rite {

   private final int radius;
   private final int height;
   private final int duration;
   private final Block block;
   private final int metadata;


   public RiteForestation(int radius, int height, int duration, Block block, int protoMeta) {
      this.radius = radius;
      this.height = height;
      this.duration = duration;
      this.block = block;
      this.metadata = protoMeta;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteForestation.StepForestation(this, intialStage));
   }

   private static class StepForestation extends RitualStep {

      private final RiteForestation rite;
      private int stage = 0;
      private EntityPlayer fakePlayer = null;


      public StepForestation(RiteForestation rite, int initialStage) {
         super(true);
         this.rite = rite;
         this.stage = initialStage;
      }

      public int getCurrentStage() {
         return (byte)this.stage;
      }

      public boolean isAirOrReplaceableBlock(World world, int x, int y, int z) {
         Block blockID = world.getBlock(x, y, z);
         if(blockID == Blocks.air) {
            return true;
         } else {
            Material block = blockID.getMaterial();
            return block == null?false:(block.isLiquid()?false:block.isReplaceable());
         }
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else if(world.isRemote) {
            return RitualStep.Result.COMPLETED;
         } else if(++this.stage < this.rite.duration + ritual.covenSize * 5) {
            int modradius = this.rite.radius + ritual.covenSize * 2;
            int modradiussq = (modradius + 1) * (modradius + 1);
            --posY;
            int x = posX - modradius + world.rand.nextInt(modradius * 2);
            int z = posZ - modradius + world.rand.nextInt(modradius * 2);
            int y = -1;
            if(Coord.distanceSq((double)x, 1.0D, (double)z, (double)posX, 1.0D, (double)posZ) > (double)modradiussq) {
               x = posX - modradius + world.rand.nextInt(modradius * 2);
               z = posZ - modradius + world.rand.nextInt(modradius * 2);
               if(Coord.distanceSq((double)x, 1.0D, (double)z, (double)posX, 1.0D, (double)posZ) > (double)modradiussq) {
                  return RitualStep.Result.UPKEEP;
               }
            }

            world.playAuxSFX(2005, posX, posY + 2, posZ, 0);
            Material material = world.getBlock(x, posY, z).getMaterial();
            if(material != null && material.isSolid() && world.isAirBlock(x, posY + 1, z)) {
               y = posY;
            } else {
               for(int h = 1; h < this.rite.height; ++h) {
                  material = world.getBlock(x, posY + h, z).getMaterial();
                  if(material != null && material.isSolid() && this.isAirOrReplaceableBlock(world, x, posY + h + 1, z)) {
                     y = posY + h;
                     break;
                  }

                  material = world.getBlock(x, posY - h, z).getMaterial();
                  if(material != null && material.isSolid() && this.isAirOrReplaceableBlock(world, x, posY - h + 1, z)) {
                     y = posY - h;
                     break;
                  }
               }
            }

            if(y != -1) {
               world.playAuxSFX(2005, x, y + 1, z, 0);
               this.drawPixel(world, x, z, y, false);
            }

            return RitualStep.Result.UPKEEP;
         } else {
            return RitualStep.Result.COMPLETED;
         }
      }

      protected void drawPixel(World world, int x, int z, int y, boolean lower) {
         Block blockID = world.getBlock(x, y, z);
         boolean wasGrass = blockID == Blocks.grass;
         Material materialAbove = world.getBlock(x, y + 1, z).getMaterial();
         if(materialAbove != null && !materialAbove.isSolid()) {
            (new MutableBlock(this.rite.block, this.rite.metadata)).mutate(world, x, y + 1, z, false);
            int count = 0;
            if((this.fakePlayer == null || this.fakePlayer.worldObj != world) && world instanceof WorldServer) {
               this.fakePlayer = new FakePlayer((WorldServer)world, new GameProfile(UUID.randomUUID(), "[Minecraft]"));
            }

            ItemDye.applyBonemeal(Dye.BONE_MEAL.createStack(), world, x, y + 1, z, this.fakePlayer);

            for(Block saplingBlockID = world.getBlock(x, y + 1, z); (saplingBlockID == Blocks.sapling || saplingBlockID == Witchery.Blocks.SAPLING) && count++ < 10; saplingBlockID = world.getBlock(x, y + 1, z)) {
               ItemDye.applyBonemeal(Dye.BONE_MEAL.createStack(), world, x, y + 1, z, this.fakePlayer);
            }
         }

      }
   }
}
