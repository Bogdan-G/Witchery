package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.worldgen.WitcheryComponent;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentCoven extends WitcheryComponent {

   public static final int DIM_X = 11;
   public static final int DIM_Y = 4;
   public static final int DIM_Z = 11;
   private int witchesSpawned = 0;


   public ComponentCoven() {}

   public ComponentCoven(int direction, Random random, int x, int z) {
      super(direction, random, x, z, 11, 4, 11);
   }

   private void spawnWitches(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6) {
      if(this.witchesSpawned < par6) {
         for(int i1 = this.witchesSpawned; i1 < par6; ++i1) {
            int j1 = this.getXWithOffset(par3 + i1, par5);
            int k1 = this.getYWithOffset(par4);
            int l1 = this.getZWithOffset(par3 + i1, par5);
            if(!par2StructureBoundingBox.isVecInside(j1, k1, l1)) {
               break;
            }

            ++this.witchesSpawned;
            if(par1World.rand.nextInt(10) != 0) {
               EntityCovenWitch entityvillager = new EntityCovenWitch(par1World);
               entityvillager.func_110163_bv();
               entityvillager.setLocationAndAngles((double)j1 + 0.5D, (double)k1, (double)l1 + 0.5D, 0.0F, 0.0F);
               par1World.spawnEntityInWorld(entityvillager);
            }
         }
      }

   }

   public boolean addComponentParts(World world, Random random) {
      BiomeGenBase biom = world.getBiomeGenForCoords(this.getXWithOffset(0, 0), this.getZWithOffset(0, 0));
      int groundAvg = this.calcGroundHeight(world, super.boundingBox);
      if(groundAvg < 0) {
         return true;
      } else {
         super.boundingBox.offset(0, groundAvg - super.boundingBox.maxY + 4 - 1, 0);
         if(!this.isWaterBelow(world, 0, -1, 0, super.boundingBox) && !this.isWaterBelow(world, 0, -1, 10, super.boundingBox) && !this.isWaterBelow(world, 10, -1, 0, super.boundingBox) && !this.isWaterBelow(world, 10, -1, 10, super.boundingBox)) {
            Block stone = Blocks.cobblestone;
            byte stoneMeta = 0;
            Block brick = Blocks.stonebrick;
            byte brickMeta = 2;
            Object groundID;
            Object undergroundID;
            if(biom.biomeID != BiomeGenBase.desert.biomeID && biom.biomeID != BiomeGenBase.desertHills.biomeID && biom.biomeID != BiomeGenBase.beach.biomeID) {
               groundID = Blocks.grass;
               undergroundID = Blocks.dirt;
            } else {
               groundID = Blocks.sand;
               undergroundID = Blocks.sand;
               stone = Blocks.sandstone;
               stoneMeta = 0;
               brick = Blocks.sandstone;
               brickMeta = 2;
            }

            this.fillWithAir(world, super.boundingBox, 3, 1, 0, 7, 3, 10);
            this.fillWithAir(world, super.boundingBox, 0, 1, 3, 10, 3, 7);
            this.fillWithAir(world, super.boundingBox, 1, 1, 1, 9, 3, 9);
            this.fillWithBlocks(world, super.boundingBox, 3, 0, 0, 7, 0, 10, (Block)groundID, (Block)groundID, false);
            this.fillWithBlocks(world, super.boundingBox, 0, 0, 3, 10, 0, 7, (Block)groundID, (Block)groundID, false);
            this.fillWithBlocks(world, super.boundingBox, 1, 0, 1, 9, 0, 9, (Block)groundID, (Block)groundID, false);
            byte ground = 1;
            Block altarBrick = Blocks.stonebrick;
            boolean altarMeta = true;
            this.place(altarBrick, 3, 4, ground, 4, super.boundingBox, world);
            this.place(altarBrick, 3, 4, ground, 5, super.boundingBox, world);
            this.place(altarBrick, 3, 4, ground, 6, super.boundingBox, world);
            this.place(altarBrick, 3, 5, ground, 4, super.boundingBox, world);
            this.place(Blocks.water, 0, 5, ground, 5, super.boundingBox, world);
            this.setDispenser(5, ground - 1, 5, random, world, 5);
            this.place(altarBrick, 3, 5, ground, 6, super.boundingBox, world);
            this.place(altarBrick, 3, 6, ground, 4, super.boundingBox, world);
            this.place(altarBrick, 3, 6, ground, 5, super.boundingBox, world);
            this.place(altarBrick, 3, 6, ground, 6, super.boundingBox, world);
            String mobType = "Witch";
            this.setSpawner(4, ground - 1, 4, "Witch", world);
            this.setSpawner(6, ground - 1, 6, "Witch", world);
            this.place(stone, stoneMeta, 1, ground, 2, super.boundingBox, world);
            this.place(stone, stoneMeta, 2, ground, 1, super.boundingBox, world);
            this.place(Blocks.web, 0, 3, ground, 0, super.boundingBox, world);
            this.place(stone, stoneMeta, 4, ground, 0, super.boundingBox, world);
            this.place(stone, stoneMeta, 6, ground, 0, super.boundingBox, world);
            this.place(stone, stoneMeta, 8, ground, 1, super.boundingBox, world);
            this.place(stone, stoneMeta, 9, ground, 2, super.boundingBox, world);
            this.place(stone, stoneMeta, 10, ground, 4, super.boundingBox, world);
            this.place(stone, stoneMeta, 10, ground, 6, super.boundingBox, world);
            this.place(stone, stoneMeta, 9, ground, 8, super.boundingBox, world);
            this.place(stone, stoneMeta, 8, ground, 9, super.boundingBox, world);
            this.place(stone, stoneMeta, 6, ground, 10, super.boundingBox, world);
            this.place(stone, stoneMeta, 4, ground, 10, super.boundingBox, world);
            this.place(stone, stoneMeta, 2, ground, 9, super.boundingBox, world);
            this.place(stone, stoneMeta, 1, ground, 8, super.boundingBox, world);
            this.place(stone, stoneMeta, 0, ground, 4, super.boundingBox, world);
            this.place(stone, stoneMeta, 0, ground, 6, super.boundingBox, world);
            int var17 = ground + 1;
            this.place(brick, brickMeta, 1, var17, 2, super.boundingBox, world);
            this.place(brick, brickMeta, 2, var17, 1, super.boundingBox, world);
            this.place(stone, stoneMeta, 4, var17, 0, super.boundingBox, world);
            this.place(stone, stoneMeta, 6, var17, 0, super.boundingBox, world);
            this.place(brick, brickMeta, 8, var17, 1, super.boundingBox, world);
            this.place(brick, brickMeta, 9, var17, 2, super.boundingBox, world);
            this.place(stone, stoneMeta, 10, var17, 4, super.boundingBox, world);
            this.place(stone, stoneMeta, 10, var17, 6, super.boundingBox, world);
            this.place(brick, brickMeta, 9, var17, 8, super.boundingBox, world);
            this.place(brick, brickMeta, 8, var17, 9, super.boundingBox, world);
            this.place(stone, stoneMeta, 6, var17, 10, super.boundingBox, world);
            this.place(stone, stoneMeta, 4, var17, 10, super.boundingBox, world);
            this.place(brick, brickMeta, 2, var17, 9, super.boundingBox, world);
            this.place(brick, brickMeta, 1, var17, 8, super.boundingBox, world);
            this.place(stone, stoneMeta, 0, var17, 4, super.boundingBox, world);
            this.place(stone, stoneMeta, 0, var17, 6, super.boundingBox, world);
            ++var17;
            this.place(stone, stoneMeta, 1, var17, 2, super.boundingBox, world);
            this.place(stone, stoneMeta, 2, var17, 1, super.boundingBox, world);
            this.place(brick, brickMeta, 3, var17, 1, super.boundingBox, world);
            this.place(brick, brickMeta, 4, var17, 0, super.boundingBox, world);
            this.place(brick, brickMeta, 5, var17, 0, super.boundingBox, world);
            this.place(brick, brickMeta, 6, var17, 0, super.boundingBox, world);
            this.place(brick, brickMeta, 7, var17, 1, super.boundingBox, world);
            this.place(stone, stoneMeta, 8, var17, 1, super.boundingBox, world);
            this.place(stone, stoneMeta, 9, var17, 2, super.boundingBox, world);
            this.place(brick, brickMeta, 9, var17, 3, super.boundingBox, world);
            this.place(brick, brickMeta, 10, var17, 4, super.boundingBox, world);
            this.place(brick, brickMeta, 10, var17, 5, super.boundingBox, world);
            this.place(brick, brickMeta, 10, var17, 6, super.boundingBox, world);
            this.place(brick, brickMeta, 9, var17, 7, super.boundingBox, world);
            this.place(Blocks.web, 0, 10, var17 - 1, 7, super.boundingBox, world);
            this.place(stone, stoneMeta, 9, var17, 8, super.boundingBox, world);
            this.place(stone, stoneMeta, 8, var17, 9, super.boundingBox, world);
            this.place(brick, brickMeta, 7, var17, 9, super.boundingBox, world);
            this.place(brick, brickMeta, 6, var17, 10, super.boundingBox, world);
            this.place(brick, brickMeta, 5, var17, 10, super.boundingBox, world);
            this.place(brick, brickMeta, 4, var17, 10, super.boundingBox, world);
            this.place(brick, brickMeta, 3, var17, 9, super.boundingBox, world);
            this.place(stone, stoneMeta, 2, var17, 9, super.boundingBox, world);
            this.place(stone, stoneMeta, 1, var17, 8, super.boundingBox, world);
            this.place(Blocks.web, 0, 0, var17, 7, super.boundingBox, world);
            this.place(brick, brickMeta, 1, var17, 3, super.boundingBox, world);
            this.place(brick, brickMeta, 0, var17, 4, super.boundingBox, world);
            this.place(brick, brickMeta, 0, var17, 5, super.boundingBox, world);
            this.place(brick, brickMeta, 0, var17, 6, super.boundingBox, world);
            this.place(brick, brickMeta, 1, var17, 7, super.boundingBox, world);

            for(int x = 0; x < 11; ++x) {
               for(int z = 0; z < 11; ++z) {
                  this.func_151554_b(world, (Block)undergroundID, 0, x, 0, z, super.boundingBox);
                  this.clearCurrentPositionBlocksUpwards(world, x, 4, z, super.boundingBox);
               }
            }

            this.spawnWitches(world, super.boundingBox, 7, 1, 4, 1);
            return true;
         } else {
            return false;
         }
      }
   }

   protected void func_143012_a(NBTTagCompound par1NBTTagCompound) {
      super.func_143012_a(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("WITCWCount", this.witchesSpawned);
   }

   protected void func_143011_b(NBTTagCompound par1NBTTagCompound) {
      super.func_143011_b(par1NBTTagCompound);
      if(par1NBTTagCompound.hasKey("WITCWCount")) {
         this.witchesSpawned = par1NBTTagCompound.getInteger("WITCWCount");
      } else {
         this.witchesSpawned = 0;
      }

   }
}
