package com.emoniph.witchery.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.HarvestType;
import powercrystals.minefactoryreloaded.api.IFactoryHarvestable;

public class MFRHarvestable implements IFactoryHarvestable {

   private Block source;
   private HarvestType harvestType;
   private int stages;


   public MFRHarvestable(Block source, HarvestType harvestType, int stages) {
      this.source = source;
      this.harvestType = harvestType;
      this.stages = stages;
   }

   public Block getPlant() {
      return this.source;
   }

   public HarvestType getHarvestType() {
      return this.harvestType;
   }

   public boolean breakBlock() {
      return this.stages == 0;
   }

   public boolean canBeHarvested(World world, Map harvesterSettings, int x, int y, int z) {
      return this.stages == 0 || this.stages > 0 && world.getBlockMetadata(x, y, z) == this.stages;
   }

   public List getDrops(World world, Random rand, Map harvesterSettings, int x, int y, int z) {
      if(harvesterSettings.get("silkTouch") != null && ((Boolean)harvesterSettings.get("silkTouch")).booleanValue() && this.harvestType == HarvestType.TreeLeaf) {
         ArrayList drops = new ArrayList();
         drops.add(new ItemStack(this.source, 1, world.getBlockMetadata(x, y, z) & 3));
         return drops;
      } else {
         return this.source.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
      }
   }

   public void preHarvest(World world, int x, int y, int z) {}

   public void postHarvest(World world, int x, int y, int z) {
      if(this.stages > 0 && world.getBlockMetadata(x, y, z) == this.stages) {
         world.setBlockToAir(x, y, z);
      }

   }
}
