package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityCovenWitch;
import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.House1;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class ComponentVillageWitchHut extends House1 {

   private boolean isTallHouse;
   private int tablePosition;
   private int witchesSpawned = 0;


   public ComponentVillageWitchHut() {}

   public ComponentVillageWitchHut(Start par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5) {
      super(par1ComponentVillageStartPiece, par2, par3Random, par4StructureBoundingBox, par5);
      super.coordBaseMode = par5;
      super.boundingBox = par4StructureBoundingBox;
      this.isTallHouse = true;
      this.tablePosition = par3Random.nextInt(2) + 1;
   }

   protected void func_143012_a(NBTTagCompound par1NBTTagCompound) {
      super.func_143012_a(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("T", this.tablePosition);
      par1NBTTagCompound.setBoolean("C", this.isTallHouse);
      par1NBTTagCompound.setInteger("WITCWCount", this.witchesSpawned);
   }

   protected void func_143011_b(NBTTagCompound par1NBTTagCompound) {
      super.func_143011_b(par1NBTTagCompound);
      this.tablePosition = par1NBTTagCompound.getInteger("T");
      this.isTallHouse = par1NBTTagCompound.getBoolean("C");
      this.witchesSpawned = par1NBTTagCompound.getInteger("WITCWCount");
   }

   public static ComponentVillageWitchHut buildComponent(Start par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7) {
      StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 4, 6, 5, par6);
      return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(par1List, structureboundingbox) == null?new ComponentVillageWitchHut(par0ComponentVillageStartPiece, par7, par2Random, structureboundingbox, par6):null;
   }

   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      if(super.field_143015_k < 0) {
         super.field_143015_k = this.getAverageGroundLevel(par1World, par3StructureBoundingBox);
         if(super.field_143015_k < 0) {
            return true;
         }

         super.boundingBox.offset(0, super.field_143015_k - super.boundingBox.maxY + 6 - 1, 0);
      }

      this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 1, 3, 5, 4, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 3, 0, 4, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 3, Blocks.cobblestone, Blocks.cobblestone, false);
      if(!this.isTallHouse) {
         this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 1, 4, 1, 2, 4, 3, Blocks.planks, 1, Blocks.planks, 1, false);
      } else {
         this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 1, 5, 1, 2, 5, 3, Blocks.planks, 1, Blocks.planks, 1, false);
      }

      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 1, 4, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 2, 4, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 1, 4, 4, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 2, 4, 4, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 0, 4, 1, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 0, 4, 2, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 0, 4, 3, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 3, 4, 1, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 3, 4, 2, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 3, 4, 3, par3StructureBoundingBox);
      this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 0, 3, 0, Blocks.planks, 2, Blocks.planks, 2, false);
      this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 3, 1, 0, 3, 3, 0, Blocks.planks, 2, Blocks.planks, 2, false);
      this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 0, 1, 4, 0, 3, 4, Blocks.planks, 2, Blocks.planks, 2, false);
      this.fillWithMetadataBlocks(par1World, par3StructureBoundingBox, 3, 1, 4, 3, 3, 4, Blocks.planks, 2, Blocks.planks, 2, false);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 0, 3, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 3, 3, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 0, 3, 4, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.planks, 1, 3, 3, 4, par3StructureBoundingBox);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 1, 0, 3, 3, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 3, 1, 1, 3, 3, 3, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 0, 2, 3, 0, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 4, 2, 3, 4, Blocks.planks, Blocks.planks, false);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 0, 2, 2, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 3, 2, 2, par3StructureBoundingBox);
      if(this.tablePosition > 0) {
         this.placeBlockAtCurrentPosition(par1World, Blocks.cauldron, 3, 1, 1, 3, par3StructureBoundingBox);
         this.placeBlockAtCurrentPosition(par1World, Witchery.Blocks.LOG, 0, 2, 1, 3, par3StructureBoundingBox);
         this.placeBlockAtCurrentPosition(par1World, Blocks.flower_pot, 4, 2, 2, 3, par3StructureBoundingBox);
      }

      this.placeBlockAtCurrentPosition(par1World, Blocks.air, 0, 1, 1, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.air, 0, 1, 2, 0, par3StructureBoundingBox);
      this.placeDoorAtCurrentPosition(par1World, par3StructureBoundingBox, par2Random, 1, 1, 0, this.getMetadataWithOffset(Blocks.wooden_door, 1));
      if(this.getBlockAtCurrentPosition(par1World, 1, 0, -1, par3StructureBoundingBox) == Blocks.air && this.getBlockAtCurrentPosition(par1World, 1, -1, -1, par3StructureBoundingBox) != Blocks.air) {
         this.placeBlockAtCurrentPosition(par1World, Blocks.stone_stairs, this.getMetadataWithOffset(Blocks.stone_stairs, 3), 1, 0, -1, par3StructureBoundingBox);
      }

      for(int i = 0; i < 5; ++i) {
         for(int j = 0; j < 4; ++j) {
            this.clearCurrentPositionBlocksUpwards(par1World, j, 6, i, par3StructureBoundingBox);
            this.func_151554_b(par1World, Blocks.cobblestone, 0, j, -1, i, par3StructureBoundingBox);
         }
      }

      this.spawnWitches(par1World, par3StructureBoundingBox, 1, 1, 2, 1);
      return true;
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
            EntityCovenWitch entityvillager = new EntityCovenWitch(par1World);
            entityvillager.setLocationAndAngles((double)j1 + 0.5D, (double)k1, (double)l1 + 0.5D, 0.0F, 0.0F);
            entityvillager.func_110163_bv();
            par1World.spawnEntityInWorld(entityvillager);
         }
      }

   }
}
