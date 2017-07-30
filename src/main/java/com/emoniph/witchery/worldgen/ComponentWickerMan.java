package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.worldgen.WitcheryComponent;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class ComponentWickerMan extends WitcheryComponent {

   public static final int DIM_X = 6;
   public static final int DIM_Y = 8;
   public static final int DIM_Z = 5;


   public ComponentWickerMan() {}

   public ComponentWickerMan(int direction, Random random, int x, int z) {
      super(direction, random, x, z, 6, 8, 5);
   }

   public boolean addComponentParts(World world, Random random) {
      BiomeGenBase biom = world.getBiomeGenForCoords(this.getXWithOffset(0, 0), this.getZWithOffset(0, 0));
      int groundAvg = this.calcGroundHeight(world, super.boundingBox);
      if(groundAvg < 0) {
         return true;
      } else {
         super.boundingBox.offset(0, groundAvg - super.boundingBox.maxY + 8 - 1, 0);
         Block wicker = Blocks.hay_block;
         Object plant = Blocks.red_flower;
         Object groundID;
         Object undergroundID;
         if(biom.biomeID != BiomeGenBase.desert.biomeID && biom.biomeID != BiomeGenBase.desertHills.biomeID && biom.biomeID != BiomeGenBase.beach.biomeID) {
            groundID = Blocks.grass;
            undergroundID = Blocks.dirt;
         } else {
            groundID = Blocks.sand;
            undergroundID = Blocks.sand;
            plant = Blocks.deadbush;
         }

         boolean flip = super.coordBaseMode == 0 || super.coordBaseMode == 2;
         this.fillWithAir(world, super.boundingBox, 1, 1, 0, 4, 7, 4);
         this.fillWithAir(world, super.boundingBox, 0, 1, 2, 5, 7, 2);
         this.fillWithBlocks(world, super.boundingBox, 1, 0, 0, 4, 0, 4, (Block)groundID, (Block)groundID, false);
         this.fillWithBlocks(world, super.boundingBox, 0, 0, 2, 5, 0, 2, (Block)groundID, (Block)groundID, false);
         byte ground = 1;
         this.place((Block)plant, 0, 1, ground, 0, super.boundingBox, world);
         this.place((Block)plant, 0, 4, ground, 0, super.boundingBox, world);
         this.place((Block)plant, 0, 0, ground, 2, super.boundingBox, world);
         this.place((Block)plant, 0, 5, ground, 2, super.boundingBox, world);
         this.place((Block)plant, 0, 4, ground, 4, super.boundingBox, world);
         this.place((Block)plant, 0, 1, ground, 4, super.boundingBox, world);
         boolean horz = false;
         int vert = flip?4:8;
         int flat = flip?8:4;
         int spawnables = Config.instance().strawmanSpawnerRules.length;
         String spawnable = spawnables > 0?Config.instance().strawmanSpawnerRules[world.rand.nextInt(spawnables)]:"Zombie";
         this.setSpawner(2, 0, 2, spawnable != null && !spawnable.isEmpty()?spawnable:"Zombie", world);
         this.place(wicker, vert, 2, ground, 2, super.boundingBox, world);
         this.place(wicker, vert, 3, ground, 2, super.boundingBox, world);
         this.place(wicker, vert, 2, ground + 1, 2, super.boundingBox, world);
         this.place(wicker, vert, 3, ground + 1, 2, super.boundingBox, world);
         this.place(wicker, flat, 1, ground + 2, 2, super.boundingBox, world);
         this.place(wicker, vert, 2, ground + 2, 2, super.boundingBox, world);
         this.place(wicker, vert, 3, ground + 2, 2, super.boundingBox, world);
         this.place(wicker, flat, 4, ground + 2, 2, super.boundingBox, world);
         this.place(wicker, vert, 1, ground + 3, 2, super.boundingBox, world);
         this.place(wicker, 0, 2, ground + 3, 2, super.boundingBox, world);
         this.place(wicker, 0, 3, ground + 3, 2, super.boundingBox, world);
         this.place(wicker, vert, 4, ground + 3, 2, super.boundingBox, world);
         this.place(wicker, 0, 1, ground + 4, 2, super.boundingBox, world);
         this.place(wicker, 0, 2, ground + 4, 2, super.boundingBox, world);
         this.place(wicker, 0, 3, ground + 4, 2, super.boundingBox, world);
         this.place(wicker, 0, 4, ground + 4, 2, super.boundingBox, world);
         this.place(wicker, flat, 2, ground + 5, 2, super.boundingBox, world);
         this.place(wicker, flat, 3, ground + 5, 2, super.boundingBox, world);
         this.place(wicker, flat, 2, ground + 6, 2, super.boundingBox, world);
         this.place(wicker, flat, 3, ground + 6, 2, super.boundingBox, world);

         for(int x = 0; x < 6; ++x) {
            for(int z = 0; z < 5; ++z) {
               this.func_151554_b(world, (Block)undergroundID, 0, x, 0, z, super.boundingBox);
               this.clearCurrentPositionBlocksUpwards(world, x, 8, z, super.boundingBox);
            }
         }

         return true;
      }
   }
}
