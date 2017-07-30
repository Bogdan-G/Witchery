package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.worldgen.WitcheryComponent;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockSand;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class ComponentShack extends WitcheryComponent {

   public static final int DIM_X = 7;
   public static final int DIM_Y = 10;
   public static final int DIM_Z = 7;
   private int witchesSpawned = 0;
   public static final WeightedRandomChestContent[] shackChestContents = new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.glass_bottle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.cooked_fished, 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sapling), 1, 1, 1, 15), new WeightedRandomChestContent(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemRowanBerries.damageValue, 1, 2, 10), new WeightedRandomChestContent(Items.iron_shovel, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5)};
   private boolean hasMadeChest;
   private static final String CHEST_KEY = "WITCShackChest";


   public ComponentShack() {}

   public ComponentShack(int direction, Random random, int x, int z) {
      super(direction, random, x, z, 7, 10, 7);
   }

   public boolean addComponentParts(World world, Random random) {
      BiomeGenBase biom = world.getBiomeGenForCoords(this.getXWithOffset(0, 0), this.getZWithOffset(0, 0));
      int groundAvg = this.calcGroundHeight(world, super.boundingBox);
      if(groundAvg < 0) {
         return true;
      } else {
         super.boundingBox.offset(0, groundAvg - super.boundingBox.maxY + 10 - 1, 0);
         if(!this.isWaterBelow(world, 0, -1, 0, super.boundingBox) && !this.isWaterBelow(world, 0, -1, 6, super.boundingBox) && !this.isWaterBelow(world, 6, -1, 0, super.boundingBox) && !this.isWaterBelow(world, 6, -1, 6, super.boundingBox)) {
            BlockGrass groundID = Blocks.grass;
            Object undergroundID = Blocks.dirt;
            if(biom.biomeID == BiomeGenBase.desert.biomeID || biom.biomeID == BiomeGenBase.desertHills.biomeID || biom.biomeID == BiomeGenBase.beach.biomeID) {
               BlockSand var11 = Blocks.sand;
               undergroundID = Blocks.sand;
            }

            this.fillWithAir(world, super.boundingBox, 0, 1, 0, 6, 9, 6);
            this.fillWithBlocks(world, super.boundingBox, 0, 0, 1, 6, 1, 5, Blocks.cobblestone, Blocks.cobblestone, false);
            this.fillWithBlocks(world, super.boundingBox, 0, 2, 1, 6, 3, 5, Blocks.planks, Blocks.planks, false);
            this.fillWithAir(world, super.boundingBox, 1, 1, 2, 5, 3, 4);
            this.place(Blocks.log, 1, 0, 1, 1, super.boundingBox, world);
            this.place(Blocks.log, 1, 0, 2, 1, super.boundingBox, world);
            this.place(Blocks.log, 1, 0, 3, 1, super.boundingBox, world);
            this.place(Blocks.log, 1, 0, 1, 5, super.boundingBox, world);
            this.place(Blocks.log, 1, 0, 2, 5, super.boundingBox, world);
            this.place(Blocks.log, 1, 0, 3, 5, super.boundingBox, world);
            this.place(Blocks.log, 1, 6, 1, 1, super.boundingBox, world);
            this.place(Blocks.log, 1, 6, 2, 1, super.boundingBox, world);
            this.place(Blocks.log, 1, 6, 3, 1, super.boundingBox, world);
            this.place(Blocks.log, 1, 6, 1, 5, super.boundingBox, world);
            this.place(Blocks.log, 1, 6, 2, 5, super.boundingBox, world);
            this.place(Blocks.log, 1, 6, 3, 5, super.boundingBox, world);
            int meta = super.coordBaseMode != 3 && super.coordBaseMode != 1?8:4;
            this.place(Blocks.log, 1 | meta, 0, 4, 2, super.boundingBox, world);
            this.place(Blocks.log, 1 | meta, 0, 4, 3, super.boundingBox, world);
            this.place(Blocks.log, 1 | meta, 0, 4, 4, super.boundingBox, world);
            this.place(Blocks.log, 1 | meta, 6, 4, 2, super.boundingBox, world);
            this.place(Blocks.log, 1 | meta, 6, 4, 3, super.boundingBox, world);
            this.place(Blocks.log, 1 | meta, 6, 4, 4, super.boundingBox, world);

            int i;
            for(i = 0; i < 7; ++i) {
               this.place(Blocks.spruce_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3), i, 3, 0, super.boundingBox, world);
               this.place(Blocks.spruce_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3), i, 4, 1, super.boundingBox, world);
               this.place(Blocks.spruce_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3), i, 5, 2, super.boundingBox, world);
               this.place(Blocks.planks, 1, i, 5, 3, super.boundingBox, world);
               this.place(Blocks.spruce_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 2), i, 5, 4, super.boundingBox, world);
               this.place(Blocks.spruce_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 2), i, 4, 5, super.boundingBox, world);
               this.place(Blocks.spruce_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 2), i, 3, 6, super.boundingBox, world);
            }

            this.place(Blocks.glass_pane, 0, 2, 2, 1, super.boundingBox, world);
            this.place(Blocks.glass_pane, 0, 2, 2, 5, super.boundingBox, world);
            this.place(Blocks.glass_pane, 0, 4, 2, 5, super.boundingBox, world);
            this.place(Blocks.glass_pane, 0, 0, 2, 3, super.boundingBox, world);
            this.place(Blocks.glass_pane, 0, 6, 2, 3, super.boundingBox, world);
            this.placeDoorAtCurrentPosition(world, super.boundingBox, random, 4, 1, 1, this.getMetadataWithOffset(Blocks.wooden_door, 1));
            this.place(Blocks.planks, 2, 1, 1, 4, super.boundingBox, world);
            this.place(Blocks.torch, 0, 1, 2, 4, super.boundingBox, world);
            this.place(Blocks.birch_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 1), 1, 1, 3, super.boundingBox, world);
            this.place(Blocks.birch_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3), 2, 1, 4, super.boundingBox, world);
            this.place(Blocks.fence, 0, 2, 1, 3, super.boundingBox, world);
            this.place(Blocks.wooden_pressure_plate, 0, 2, 2, 3, super.boundingBox, world);
            int j;
            if(!this.hasMadeChest) {
               i = this.getYWithOffset(0);
               j = this.getXWithOffset(7, 1);
               int kc = this.getZWithOffset(7, 1);
               if(super.boundingBox.isVecInside(j, i, kc)) {
                  this.hasMadeChest = true;
                  this.generateStructureChestContents(world, super.boundingBox, random, 1, 1, 2, shackChestContents, 1 + random.nextInt(3));
               }
            }

            for(i = 0; i < 7; ++i) {
               for(j = 0; j < 7; ++j) {
                  this.clearCurrentPositionBlocksUpwards(world, j, 6, i, super.boundingBox);
                  this.func_151554_b(world, (Block)undergroundID, 0, j, 0, i, super.boundingBox);
               }
            }

            this.spawnWitches(world, super.boundingBox, 4, 1, 3, 1);
            return true;
         } else {
            return false;
         }
      }
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
            if(par1World.rand.nextInt(4) != 0) {
               EntityCovenWitch entityvillager = new EntityCovenWitch(par1World);
               entityvillager.func_110163_bv();
               entityvillager.setLocationAndAngles((double)j1 + 0.5D, (double)k1, (double)l1 + 0.5D, 0.0F, 0.0F);
               par1World.spawnEntityInWorld(entityvillager);
            }
         }
      }

   }

   protected void func_143012_a(NBTTagCompound par1NBTTagCompound) {
      super.func_143012_a(par1NBTTagCompound);
      par1NBTTagCompound.setBoolean("WITCShackChest", this.hasMadeChest);
      par1NBTTagCompound.setInteger("WITCWCount", this.witchesSpawned);
   }

   protected void func_143011_b(NBTTagCompound par1NBTTagCompound) {
      super.func_143011_b(par1NBTTagCompound);
      this.hasMadeChest = par1NBTTagCompound.getBoolean("WITCShackChest");
      if(par1NBTTagCompound.hasKey("WITCWCount")) {
         this.witchesSpawned = par1NBTTagCompound.getInteger("WITCWCount");
      } else {
         this.witchesSpawned = 0;
      }

   }

}
