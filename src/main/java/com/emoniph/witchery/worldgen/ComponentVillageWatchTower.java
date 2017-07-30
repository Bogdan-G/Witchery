package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.entity.EntityVillageGuard;
import java.util.List;
import java.util.Random;
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

public class ComponentVillageWatchTower extends Village {

   public static final WeightedRandomChestContent[] villageTowerChestContents = new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.fish, 0, 1, 3, 15), new WeightedRandomChestContent(Items.iron_axe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_sword, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_chestplate, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_helmet, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_leggings, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_boots, 0, 1, 1, 5), new WeightedRandomChestContent(Items.leather_chestplate, 0, 1, 1, 6), new WeightedRandomChestContent(Items.leather_helmet, 0, 1, 1, 6), new WeightedRandomChestContent(Items.leather_leggings, 0, 1, 1, 6), new WeightedRandomChestContent(Items.leather_boots, 0, 1, 1, 6), new WeightedRandomChestContent(Items.bow, 0, 1, 1, 8), new WeightedRandomChestContent(Items.arrow, 0, 2, 6, 8), new WeightedRandomChestContent(Items.saddle, 0, 1, 1, 3), new WeightedRandomChestContent(Items.iron_horse_armor, 0, 1, 1, 1), new WeightedRandomChestContent(Items.golden_horse_armor, 0, 1, 1, 1)};
   private boolean hasMadeChest;
   private int guardsSpawned;


   public static ComponentVillageWatchTower construct(Start start, List pieces, Random rand, int p1, int p2, int p3, int p4, int p5) {
      StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 8, 23, 8, p4);
      return canVillageGoDeeper(bounds) && StructureComponent.findIntersecting(pieces, bounds) == null?new ComponentVillageWatchTower(start, p5, rand, bounds, p4):null;
   }

   public ComponentVillageWatchTower() {}

   public ComponentVillageWatchTower(Start start, int componentType, Random rand, StructureBoundingBox bounds, int coordMode) {
      super(start, componentType);
      super.coordBaseMode = coordMode;
      super.boundingBox = bounds;
   }

   public boolean addComponentParts(World world, Random rand, StructureBoundingBox bounds) {
      boolean height = true;
      if(super.field_143015_k < 0) {
         super.field_143015_k = this.getAverageGroundLevel(world, bounds);
         if(super.field_143015_k < 0) {
            return true;
         }

         super.boundingBox.offset(0, super.field_143015_k - super.boundingBox.maxY + 23 - 1, 0);
      }

      this.fillWithBlocks(world, bounds, 2, 0, 2, 6, 17, 6, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 3, 13, 3, 5, 14, 5, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 2, 16, 3, 6, 17, 5, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 3, 16, 2, 5, 17, 6, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 3, 15, 1, 5, 16, 1, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 4, 14, 1, 4, 17, 1, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 3, 15, 7, 5, 16, 7, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 4, 14, 7, 4, 17, 7, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 1, 15, 3, 1, 16, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 1, 14, 4, 1, 17, 4, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 7, 15, 3, 7, 16, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 7, 14, 4, 7, 17, 4, Blocks.cobblestone, Blocks.cobblestone, false);
      this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, 2, 18, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, 2, 18, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, 6, 18, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, 6, 18, 6, bounds);
      this.fillWithBlocks(world, bounds, 2, 19, 2, 6, 19, 6, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(world, bounds, 3, 20, 3, 5, 20, 5, Blocks.planks, Blocks.planks, false);
      this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 4, 19, 4, bounds);
      int n = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
      int s = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
      int w = this.getMetadataWithOffset(Blocks.oak_stairs, 0);
      int e = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 2, 19, 1, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 3, 19, 1, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 4, 19, 1, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 5, 19, 1, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 6, 19, 1, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 2, 20, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 3, 20, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 4, 20, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 5, 20, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 6, 20, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 3, 21, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 4, 21, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, 5, 21, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 2, 19, 7, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 3, 19, 7, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 4, 19, 7, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 5, 19, 7, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 6, 19, 7, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 2, 20, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 3, 20, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 4, 20, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 5, 20, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 6, 20, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 3, 21, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 4, 21, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, 5, 21, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 1, 19, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 1, 19, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 1, 19, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 1, 19, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 1, 19, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 2, 20, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 2, 20, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 2, 20, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 2, 20, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 2, 20, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 3, 21, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 3, 21, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, w, 3, 21, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 7, 19, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 7, 19, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 7, 19, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 7, 19, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 7, 19, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 6, 20, 2, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 6, 20, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 6, 20, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 6, 20, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 6, 20, 6, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 5, 21, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 5, 21, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, e, 5, 21, 5, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.wooden_slab, 0, 4, 22, 4, bounds);
      this.fillWithBlocks(world, bounds, 4, 1, 2, 4, 2, 3, Blocks.air, Blocks.air, false);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 3, 2, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 5, 2, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 4, 14, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 4, 16, 4, bounds);
      this.fillWithBlocks(world, bounds, 2, 6, 2, 2, 14, 2, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 6, 6, 2, 6, 14, 2, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 6, 6, 6, 6, 14, 6, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 2, 6, 6, 2, 14, 6, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 4, 6, 2, 4, 12, 2, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 4, 6, 6, 4, 12, 6, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 6, 6, 4, 6, 12, 4, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 2, 6, 4, 2, 12, 4, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 2, 9, 2, 6, 9, 6, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 3, 0, 1, 5, 4, 1, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 4, 1, 1, 4, 3, 1, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 3, 0, 7, 5, 4, 7, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 4, 1, 7, 4, 3, 7, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 1, 0, 3, 1, 4, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 1, 1, 4, 1, 3, 4, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 7, 0, 3, 7, 4, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 7, 1, 4, 7, 3, 4, Blocks.air, Blocks.air, false);
      int offset = this.getMetadataWithOffset(Blocks.ladder, 3);

      int j;
      for(j = 1; j <= 12; ++j) {
         this.placeBlockAtCurrentPosition(world, Blocks.ladder, offset, 4, j, 4, bounds);
      }

      for(j = 13; j <= 15; ++j) {
         this.placeBlockAtCurrentPosition(world, Blocks.ladder, offset, 3, j, 5, bounds);
      }

      int k;
      if(!this.hasMadeChest) {
         j = this.getYWithOffset(13);
         k = this.getXWithOffset(5, 5);
         int k1 = this.getZWithOffset(5, 5);
         if(bounds.isVecInside(k, j, k1)) {
            this.hasMadeChest = true;
            this.generateStructureChestContents(world, bounds, rand, 5, 13, 5, villageTowerChestContents, 2 + rand.nextInt(4));
         }
      }

      for(j = 1; j < 7; ++j) {
         for(k = 1; k < 7; ++k) {
            this.clearCurrentPositionBlocksUpwards(world, k, 23, j, bounds);
            this.func_151554_b(world, Blocks.cobblestone, 0, k, -1, j, bounds);
         }
      }

      this.spawnGuards(world, bounds, 4, 16, 4, 3);
      return true;
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
