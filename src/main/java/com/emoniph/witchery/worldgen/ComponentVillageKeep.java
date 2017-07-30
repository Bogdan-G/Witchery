package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.entity.EntityVillageGuard;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;

public class ComponentVillageKeep extends Village {

   public static final WeightedRandomChestContent[] villageTowerChestContents = new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 6, 10), new WeightedRandomChestContent(Items.gold_nugget, 0, 1, 15, 20), new WeightedRandomChestContent(Items.golden_axe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_helmet, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.golden_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Items.diamond_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1)};
   private boolean hasMadeChest;
   private int guardsSpawned;


   public static ComponentVillageKeep construct(Start start, List pieces, Random rand, int p1, int p2, int p3, int p4, int p5) {
      StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 16, 26, 16, p4);
      return canVillageGoDeeper(bounds) && StructureComponent.findIntersecting(pieces, bounds) == null?new ComponentVillageKeep(start, p5, rand, bounds, p4):null;
   }

   public ComponentVillageKeep() {}

   public ComponentVillageKeep(Start start, int componentType, Random rand, StructureBoundingBox bounds, int coordMode) {
      super(start, componentType);
      super.coordBaseMode = coordMode;
      super.boundingBox = bounds;
   }

   private void fill(World world, StructureBoundingBox bounds, int x, int y, int z, int w, int h, int d, Block block) {
      this.fillWithBlocks(world, bounds, x, y, z, x + w - 1, y + h - 1, z + d - 1, block, block, false);
   }

   public boolean addComponentParts(World world, Random rand, StructureBoundingBox bounds) {
      boolean height = true;
      if(super.field_143015_k < 0) {
         super.field_143015_k = this.getAverageGroundLevel(world, bounds);
         if(super.field_143015_k < 0) {
            return true;
         }

         super.boundingBox.offset(0, super.field_143015_k - super.boundingBox.maxY + 26 - 1, 0);
      }

      this.fillWithBlocks(world, bounds, 1, 1, 1, 14, 26, 14, Blocks.air, Blocks.air, false);
      this.drawTower(world, bounds, 0, 0);
      this.drawTower(world, bounds, 8, 4);
      this.fill(world, bounds, 7, 0, 2, 3, 1, 3, Blocks.cobblestone);
      this.fill(world, bounds, 7, 4, 3, 3, 1, 2, Blocks.cobblestone);
      this.fill(world, bounds, 7, 5, 2, 3, 1, 1, Blocks.cobblestone);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 0, 8, 6, 2, bounds);
      int meta = this.getMetadataWithOffset(Blocks.log, 8);

      int x;
      for(x = 7; x <= 9; ++x) {
         this.placeBlockAtCurrentPosition(world, Blocks.log, meta, x, 4, 2, bounds);
      }

      this.fill(world, bounds, 7, 3, 3, 3, 1, 1, Blocks.fence);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 11, 7, 3, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 11, 7, 3, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 11, 9, 3, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 11, 9, 3, 4, bounds);
      meta = this.getMetadataWithOffset(Blocks.stone_stairs, 3);
      int meta2 = this.getMetadataWithOffset(Blocks.stone_stairs, 2);

      for(x = 7; x <= 9; ++x) {
         this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, meta, x, 0, 1, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, meta2, x, 0, 4, bounds);
      }

      this.fill(world, bounds, 2, 0, 9, 4, 16, 1, Blocks.cobblestone);
      this.fill(world, bounds, 2, 0, 14, 4, 16, 1, Blocks.cobblestone);
      this.fill(world, bounds, 1, 0, 10, 1, 16, 4, Blocks.cobblestone);
      this.fill(world, bounds, 6, 0, 10, 1, 16, 4, Blocks.cobblestone);
      this.fill(world, bounds, 2, 0, 10, 4, 1, 4, Blocks.cobblestone);
      this.fill(world, bounds, 1, 4, 9, 6, 1, 6, Blocks.cobblestone);
      this.fill(world, bounds, 1, 9, 9, 6, 1, 6, Blocks.cobblestone);
      this.fill(world, bounds, 1, 14, 9, 6, 1, 6, Blocks.cobblestone);
      this.fill(world, bounds, 3, 16, 9, 2, 1, 1, Blocks.cobblestone);
      this.fill(world, bounds, 3, 16, 14, 2, 1, 1, Blocks.cobblestone);
      this.fill(world, bounds, 1, 16, 11, 1, 1, 2, Blocks.cobblestone);
      this.fill(world, bounds, 6, 16, 11, 1, 1, 2, Blocks.cobblestone);
      this.fill(world, bounds, 3, 1, 14, 2, 3, 1, Blocks.log);
      this.fill(world, bounds, 1, 1, 11, 1, 3, 2, Blocks.log);
      this.fill(world, bounds, 3, 11, 9, 2, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 3, 6, 14, 2, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 3, 11, 14, 2, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 1, 6, 11, 1, 2, 2, Blocks.iron_bars);
      this.fill(world, bounds, 1, 11, 11, 1, 2, 2, Blocks.iron_bars);
      this.fill(world, bounds, 6, 11, 11, 1, 2, 2, Blocks.iron_bars);
      this.fill(world, bounds, 4, 1, 9, 1, 2, 1, Blocks.air);
      this.fill(world, bounds, 4, 5, 9, 1, 2, 1, Blocks.air);
      this.fill(world, bounds, 6, 1, 11, 1, 2, 1, Blocks.air);
      this.fill(world, bounds, 6, 5, 11, 1, 2, 1, Blocks.air);
      this.placeBlockAtCurrentPosition(world, Blocks.log, this.getMetadataWithOffset(Blocks.log, 8), 4, 7, 9, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.log, this.getMetadataWithOffset(Blocks.log, 4), 6, 7, 11, bounds);
      meta = this.getMetadataWithOffset(Blocks.ladder, 2);

      int n;
      for(n = 1; n <= 14; ++n) {
         this.placeBlockAtCurrentPosition(world, Blocks.ladder, meta, 2, n, 10, bounds);
      }

      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 2, 2, 13, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 2, 6, 13, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 2, 11, 13, bounds);
      this.fill(world, bounds, 11, 0, 9, 3, 19, 1, Blocks.cobblestone);
      this.fill(world, bounds, 11, 0, 13, 3, 19, 1, Blocks.cobblestone);
      this.fill(world, bounds, 10, 0, 10, 1, 19, 3, Blocks.cobblestone);
      this.fill(world, bounds, 14, 0, 10, 1, 19, 3, Blocks.cobblestone);
      this.fill(world, bounds, 11, 0, 10, 3, 1, 3, Blocks.cobblestone);
      this.fill(world, bounds, 10, 4, 9, 5, 1, 5, Blocks.cobblestone);
      this.fill(world, bounds, 10, 9, 9, 5, 1, 5, Blocks.cobblestone);
      this.fill(world, bounds, 10, 14, 9, 5, 1, 5, Blocks.cobblestone);
      this.fill(world, bounds, 10, 19, 9, 5, 1, 5, Blocks.cobblestone);
      this.fill(world, bounds, 12, 1, 13, 1, 3, 1, Blocks.log);
      this.fill(world, bounds, 14, 1, 11, 1, 3, 1, Blocks.log);
      this.fill(world, bounds, 12, 6, 13, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 12, 11, 9, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 12, 16, 9, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 12, 11, 13, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 12, 16, 13, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 14, 6, 11, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 14, 11, 11, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 14, 16, 11, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 10, 11, 11, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 10, 16, 11, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 12, 5, 9, 1, 2, 1, Blocks.air);
      this.fill(world, bounds, 12, 1, 9, 1, 2, 1, Blocks.air);
      this.fill(world, bounds, 10, 5, 11, 1, 2, 1, Blocks.air);
      this.fill(world, bounds, 10, 1, 11, 1, 2, 1, Blocks.air);
      this.placeBlockAtCurrentPosition(world, Blocks.log, this.getMetadataWithOffset(Blocks.log, 8), 12, 7, 9, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.log, this.getMetadataWithOffset(Blocks.log, 4), 10, 7, 11, bounds);
      meta = this.getMetadataWithOffset(Blocks.ladder, 2);

      for(n = 1; n <= 14; ++n) {
         this.placeBlockAtCurrentPosition(world, Blocks.ladder, meta, 11, n, 10, bounds);
      }

      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 11, 2, 12, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 11, 6, 12, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 11, 11, 12, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 11, 16, 12, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.log, 0, 11, 19, 10, bounds);
      this.fill(world, bounds, 10, 20, 9, 5, 2, 5, Blocks.planks);
      this.fill(world, bounds, 11, 22, 10, 3, 2, 3, Blocks.planks);
      this.fill(world, bounds, 12, 24, 11, 1, 2, 1, Blocks.planks);
      this.fill(world, bounds, 11, 20, 10, 3, 2, 3, Blocks.air);
      n = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
      int s = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
      int w = this.getMetadataWithOffset(Blocks.oak_stairs, 0);
      int e = this.getMetadataWithOffset(Blocks.oak_stairs, 1);

      for(x = 9; x <= 15; ++x) {
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, x, 20, 8, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, x, 20, 14, bounds);
      }

      for(x = 10; x <= 14; ++x) {
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, x, 22, 9, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, x, 22, 13, bounds);
      }

      for(x = 11; x <= 13; ++x) {
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, x, 24, 10, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, x, 24, 12, bounds);
      }

      int z;
      for(z = 9; z <= 13; ++z) {
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 9, 20, z, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 15, 20, z, bounds);
      }

      for(z = 10; z <= 12; ++z) {
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 10, 22, z, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 14, 22, z, bounds);
      }

      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 11, 24, 11, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 13, 24, 11, bounds);
      this.fill(world, bounds, 7, 0, 11, 3, 1, 2, Blocks.cobblestone);
      this.fill(world, bounds, 7, 4, 11, 3, 1, 1, Blocks.cobblestone);
      this.fill(world, bounds, 7, 1, 12, 3, 5, 1, Blocks.cobblestone);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 0, 8, 6, 12, bounds);
      this.fill(world, bounds, 7, 1, 12, 1, 4, 1, Blocks.log);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 8, 2, 11, bounds);
      this.fill(world, bounds, 9, 1, 12, 1, 4, 1, Blocks.log);
      meta = this.getMetadataWithOffset(Blocks.stone_stairs, 3);

      for(x = 7; x <= 9; ++x) {
         this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, meta, x, 0, 10, bounds);
      }

      this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 11, 7, 3, 11, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 11, 9, 3, 11, bounds);
      this.fill(world, bounds, 3, 0, 6, 2, 1, 3, Blocks.cobblestone);
      this.fill(world, bounds, 4, 4, 6, 1, 1, 3, Blocks.cobblestone);
      this.fill(world, bounds, 3, 1, 6, 1, 5, 3, Blocks.cobblestone);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 0, 3, 6, 7, bounds);
      this.fill(world, bounds, 3, 1, 6, 1, 4, 1, Blocks.log);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 4, 2, 7, bounds);
      this.fill(world, bounds, 3, 1, 8, 1, 4, 1, Blocks.log);
      meta = this.getMetadataWithOffset(Blocks.stone_stairs, 1);

      for(z = 6; z <= 8; ++z) {
         this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, meta, 5, 0, z, bounds);
      }

      this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 11, 4, 3, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 11, 4, 3, 8, bounds);
      this.fill(world, bounds, 12, 0, 6, 2, 1, 3, Blocks.cobblestone);
      this.fill(world, bounds, 12, 4, 6, 1, 1, 3, Blocks.cobblestone);
      this.fill(world, bounds, 13, 1, 6, 1, 5, 3, Blocks.cobblestone);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 0, 13, 6, 7, bounds);
      this.fill(world, bounds, 13, 1, 6, 1, 4, 1, Blocks.log);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 12, 2, 7, bounds);
      this.fill(world, bounds, 13, 1, 8, 1, 4, 1, Blocks.log);
      meta = this.getMetadataWithOffset(Blocks.stone_stairs, 0);

      for(z = 6; z <= 8; ++z) {
         this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, meta, 11, 0, z, bounds);
      }

      this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 11, 12, 3, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_slab, 11, 12, 3, 8, bounds);
      int k;
      if(!this.hasMadeChest) {
         byte var17 = 13;
         byte var19 = 12;
         byte j = 20;
         k = this.getYWithOffset(j);
         int j1 = this.getXWithOffset(var17, var19);
         int k1 = this.getZWithOffset(var17, var19);
         if(bounds.isVecInside(j1, k, k1)) {
            this.hasMadeChest = true;
            this.generateStructureChestContents(world, bounds, rand, var17, j, var19, villageTowerChestContents, 3 + rand.nextInt(6));
         }
      }

      for(int var18 = 1; var18 < 15; ++var18) {
         for(k = 1; k < 15; ++k) {
            this.clearCurrentPositionBlocksUpwards(world, k, 26, var18, bounds);
            this.func_151554_b(world, Blocks.cobblestone, 0, k, -1, var18, bounds);
         }
      }

      this.spawnGuards(world, bounds, 7, 1, 7, 3);
      this.spawnGuards(world, bounds, 5, 10, 4, 4);
      this.spawnGuards(world, bounds, 13, 10, 4, 5);
      return true;
   }

   public void drawTower(World world, StructureBoundingBox bounds, int offsetX, int flipX) {
      this.fill(world, bounds, 3 + offsetX, 0, 1, 3, 11, 1, Blocks.cobblestone);
      this.fill(world, bounds, 3 + offsetX, 0, 5, 3, 11, 1, Blocks.cobblestone);
      this.fill(world, bounds, 2 + offsetX, 0, 2, 1, 11, 3, Blocks.cobblestone);
      this.fill(world, bounds, 6 + offsetX, 0, 2, 1, 11, 3, Blocks.cobblestone);
      this.fill(world, bounds, 3 + offsetX, 0, 2, 3, 1, 3, Blocks.cobblestone);
      this.fill(world, bounds, 2 + offsetX, 4, 1, 5, 1, 5, Blocks.cobblestone);
      this.fill(world, bounds, 2 + offsetX, 9, 1, 5, 1, 5, Blocks.cobblestone);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 0, 4 + offsetX, 11, 1, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 0, 4 + offsetX, 11, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 0, 2 + offsetX, 11, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.cobblestone, 0, 6 + offsetX, 11, 3, bounds);
      this.fill(world, bounds, 4 + offsetX, 1, 1, 1, 3, 1, Blocks.log);
      this.fill(world, bounds, 2 + offsetX + flipX, 1, 3, 1, 3, 1, Blocks.log);
      this.fill(world, bounds, 4 + offsetX, 6, 1, 1, 2, 1, Blocks.iron_bars);
      this.fill(world, bounds, 2 + offsetX + flipX, 6, 3, 1, 2, 1, Blocks.iron_bars);
      this.placeBlockAtCurrentPosition(world, Blocks.log, this.getMetadataWithOffset(Blocks.log, 8), 4 + offsetX, 7, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.log, this.getMetadataWithOffset(Blocks.log, 4), 6 + offsetX - flipX, 7, 3, bounds);
      this.fill(world, bounds, 4 + offsetX, 5, 5, 1, 2, 1, Blocks.air);
      this.fill(world, bounds, 4 + offsetX, 1, 5, 1, 2, 1, Blocks.air);
      this.fill(world, bounds, 6 + offsetX - flipX, 5, 3, 1, 2, 1, Blocks.air);
      int meta = this.getMetadataWithOffset(Blocks.ladder, 2);

      for(int h = 1; h <= 9; ++h) {
         this.placeBlockAtCurrentPosition(world, Blocks.ladder, meta, 3 + offsetX, h, 2, bounds);
      }

      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 3 + offsetX, 2, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 3 + offsetX, 6, 4, bounds);
   }

   protected int getMetadataWithOffset(Block block, int meta) {
      if(block == Blocks.log) {
         int rawMeta = meta / 4;
         if(rawMeta == 0) {
            return meta;
         } else {
            switch(super.coordBaseMode) {
            case 0:
            case 2:
               return rawMeta == 2?4:8;
            case 1:
            case 3:
            default:
               return rawMeta == 1?8:4;
            }
         }
      } else {
         return super.getMetadataWithOffset(block, meta);
      }
   }

   protected void func_143012_a(NBTTagCompound nbtRoot) {
      super.func_143012_a(nbtRoot);
      nbtRoot.setBoolean("Chest", this.hasMadeChest);
      nbtRoot.setInteger("Guards", this.guardsSpawned);
   }

   protected void func_143011_b(NBTTagCompound nbtRoot) {
      super.func_143011_b(nbtRoot);
      this.hasMadeChest = nbtRoot.getBoolean("Chest");
      this.guardsSpawned = nbtRoot.getInteger("Guards");
   }

   private void spawnGuards(World world, StructureBoundingBox bounds, int x, int y, int z, int count) {
      if(this.guardsSpawned < count) {
         for(int guardNumber = this.guardsSpawned; guardNumber <= count; ++guardNumber) {
            int j1 = this.getXWithOffset(x, z);
            int k1 = this.getYWithOffset(y);
            int l1 = this.getZWithOffset(x, z);
            if(!bounds.isVecInside(j1, k1, l1)) {
               break;
            }

            ++this.guardsSpawned;
            EntityVillageGuard guard = new EntityVillageGuard(world);
            guard.setLocationAndAngles((double)j1 + 0.5D, (double)k1, (double)l1 + 0.5D, 0.0F, 0.0F);
            guard.func_110163_bv();
            guard.onSpawnWithEgg((IEntityLivingData)null);
            world.spawnEntityInWorld(guard);
         }
      }

   }

}
