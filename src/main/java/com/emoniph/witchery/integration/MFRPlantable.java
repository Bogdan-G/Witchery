package com.emoniph.witchery.integration;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import powercrystals.minefactoryreloaded.api.ReplacementBlock;

public class MFRPlantable implements IFactoryPlantable {

   protected Item seedItemID;
   protected Block cropBlockID;


   public MFRPlantable(Item seeds, Block plantedBlock) {
      this.seedItemID = seeds;
      this.cropBlockID = plantedBlock;
   }

   public boolean canBePlantedHere(World world, int x, int y, int z, ItemStack stack) {
      world.getBlock(x, y - 1, z);
      return !world.isAirBlock(x, y, z)?false:this.cropBlockID.canPlaceBlockAt(world, x, y, z) && this.cropBlockID.canBlockStay(world, x, y, z) || this.cropBlockID instanceof IPlantable && this.cropBlockID.canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable)this.cropBlockID);
   }

   public void prePlant(World world, int x, int y, int z, ItemStack stack) {}

   public void postPlant(World world, int x, int y, int z, ItemStack stack) {}

   public Item getSeed() {
      return this.seedItemID;
   }

   public boolean canBePlanted(ItemStack stack, boolean forFermenting) {
      return stack.getItem() == this.seedItemID;
   }

   public ReplacementBlock getPlantedBlock(World world, int x, int y, int z, ItemStack stack) {
      return new ReplacementBlock(this.cropBlockID);
   }
}
