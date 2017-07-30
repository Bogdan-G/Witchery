package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.Config;
import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.StatCollector;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.House1;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class ComponentVillageApothecary extends House1 {

   private int averageGroundLevel = -1;
   public static final WeightedRandomChestContent[] villageApothecaryChestContents = new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.diamond, 0, 1, 2, 3), new WeightedRandomChestContent(Items.glass_bottle, 0, 1, 10, 10), new WeightedRandomChestContent(Items.gold_ingot, 0, 1, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemBatWool.damageValue, 1, 5, 5), new WeightedRandomChestContent(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemDogTongue.damageValue, 1, 5, 5), new WeightedRandomChestContent(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemRowanBerries.damageValue, 1, 5, 5), new WeightedRandomChestContent(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemSpectralDust.damageValue, 1, 1, 3), new WeightedRandomChestContent(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemMutandis.damageValue, 1, 5, 5), new WeightedRandomChestContent(Items.clay_ball, 0, 4, 10, 6), new WeightedRandomChestContent(Items.skull, 0, 1, 1, 1), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sapling), 0, 3, 7, 5), new WeightedRandomChestContent(Witchery.Items.DIVINER_WATER, 0, 1, 1, 1), new WeightedRandomChestContent(Witchery.Items.POPPET, Witchery.Items.POPPET.voodooPoppet.damageValue, 1, 1, 1), new WeightedRandomChestContent(Witchery.Items.POPPET, Witchery.Items.POPPET.firePoppet.damageValue, 1, 1, 1)};
   private boolean hasMadeChest;
   private static final String CHEST_KEY = "WITCApocChest";


   public ComponentVillageApothecary() {}

   public ComponentVillageApothecary(Start componentVillageStartPiece, int componentType, Random random, StructureBoundingBox structureBoundingBox, int direction) {
      super(componentVillageStartPiece, componentType, random, structureBoundingBox, direction);
      super.coordBaseMode = direction;
      super.boundingBox = structureBoundingBox;
   }

   public static ComponentVillageApothecary buildComponent(Start startPiece, List list, Random random, int par3, int par4, int par5, int par6, int par7) {
      StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 9, 9, 6, par6);
      return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null?new ComponentVillageApothecary(startPiece, par7, random, structureboundingbox, par6):null;
   }

   public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox) {
      if(this.averageGroundLevel < 0) {
         this.averageGroundLevel = this.getAverageGroundLevel(par1World, par3StructureBoundingBox);
         if(this.averageGroundLevel < 0) {
            return true;
         }

         super.boundingBox.offset(0, this.averageGroundLevel - super.boundingBox.maxY + 9 - 1, 0);
      }

      this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 1, 7, 5, 4, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 8, 0, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 5, 0, 8, 5, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 6, 1, 8, 6, 4, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 7, 2, 8, 7, 3, Blocks.cobblestone, Blocks.cobblestone, false);
      int i = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
      int j = this.getMetadataWithOffset(Blocks.oak_stairs, 2);

      int l;
      for(int k = -1; k <= 2; ++k) {
         for(l = 0; l <= 8; ++l) {
            this.placeBlockAtCurrentPosition(par1World, Blocks.oak_stairs, i, l, 6 + k, k, par3StructureBoundingBox);
            this.placeBlockAtCurrentPosition(par1World, Blocks.oak_stairs, j, l, 6 + k, 5 - k, par3StructureBoundingBox);
         }
      }

      this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 0, 1, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 5, 8, 1, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 1, 0, 8, 1, 4, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 2, 1, 0, 7, 1, 0, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, 0, 0, 4, 0, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, 5, 0, 4, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 2, 5, 8, 4, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 2, 0, 8, 4, 0, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, 1, 0, 4, 4, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 2, 5, 7, 4, 5, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 8, 2, 1, 8, 4, 4, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 2, 0, 7, 4, 0, Blocks.planks, Blocks.planks, false);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 4, 2, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 5, 2, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 6, 2, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 4, 3, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 5, 3, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 6, 3, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 0, 2, 2, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 0, 2, 3, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 0, 3, 2, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 0, 3, 3, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 8, 2, 2, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 8, 2, 3, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 8, 3, 2, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 8, 3, 3, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 2, 2, 5, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 3, 2, 5, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 5, 2, 5, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 6, 2, 5, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 2, 3, 5, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 3, 3, 5, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 5, 3, 5, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.glass_pane, 0, 6, 3, 5, par3StructureBoundingBox);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 4, 1, 7, 4, 1, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(par1World, par3StructureBoundingBox, 1, 4, 4, 7, 4, 4, Blocks.planks, Blocks.planks, false);
      this.placeBlockAtCurrentPosition(par1World, Blocks.cauldron, 3, 7, 1, 1, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.birch_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 3) | 4, 5, 1, 3, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.birch_stairs, this.getMetadataWithOffset(Blocks.oak_stairs, 2) | 4, 5, 1, 1, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.wooden_slab, 10, 5, 1, 2, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.torch, 0, 5, 2, 3, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.flower_pot, 9, 5, 2, 1, par3StructureBoundingBox);
      int i1;
      if(!this.hasMadeChest) {
         i1 = this.getYWithOffset(0);
         int jc = this.getXWithOffset(7, 1);
         int kc = this.getZWithOffset(7, 1);
         if(par3StructureBoundingBox.isVecInside(jc, i1, kc)) {
            this.hasMadeChest = true;
            this.generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 7, 0, 1, villageApothecaryChestContents, 2 + par2Random.nextInt(4));
         }
      }

      this.placeBlockAtCurrentPosition(par1World, Blocks.air, 0, 1, 1, 0, par3StructureBoundingBox);
      this.placeBlockAtCurrentPosition(par1World, Blocks.air, 0, 1, 2, 0, par3StructureBoundingBox);
      this.placeDoorAtCurrentPosition(par1World, par3StructureBoundingBox, par2Random, 1, 1, 0, this.getMetadataWithOffset(Blocks.wooden_door, 1));
      this.generateStructureSign(par1World, par3StructureBoundingBox, par2Random, 1, 3, -1, StatCollector.translateToLocal("witchery.structure.apothecary.name"));
      if(this.getBlockAtCurrentPosition(par1World, 1, 0, -1, par3StructureBoundingBox) == Blocks.air && this.getBlockAtCurrentPosition(par1World, 1, -1, -1, par3StructureBoundingBox) != Blocks.air) {
         this.placeBlockAtCurrentPosition(par1World, Blocks.stone_stairs, this.getMetadataWithOffset(Blocks.stone_stairs, 3), 1, 0, -1, par3StructureBoundingBox);
      }

      for(l = 0; l < 6; ++l) {
         for(i1 = 0; i1 < 9; ++i1) {
            this.clearCurrentPositionBlocksUpwards(par1World, i1, 9, l, par3StructureBoundingBox);
            this.func_151554_b(par1World, Blocks.cobblestone, 0, i1, -1, l, par3StructureBoundingBox);
         }
      }

      this.spawnVillagers(par1World, par3StructureBoundingBox, 2, 1, 2, 1);
      return true;
   }

   protected boolean generateStructureSign(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, int par4, int par5, int par6, String text) {
      int i1 = this.getXWithOffset(par4, par6);
      int j1 = this.getYWithOffset(par5);
      int k1 = this.getZWithOffset(par4, par6);
      if(par2StructureBoundingBox.isVecInside(i1, j1, k1) && par1World.getBlock(i1, j1, k1) != Blocks.wall_sign) {
         boolean metadata = true;
         byte metadata1;
         switch(super.coordBaseMode) {
         case 0:
            metadata1 = 2;
            break;
         case 1:
            metadata1 = 5;
            break;
         case 2:
         default:
            metadata1 = 3;
            break;
         case 3:
            metadata1 = 4;
         }

         par1World.setBlock(i1, j1, k1, Blocks.wall_sign, metadata1, 2);
         TileEntitySign tileentitysign = (TileEntitySign)par1World.getTileEntity(i1, j1, k1);
         if(tileentitysign != null) {
            tileentitysign.signText = new String[]{"", text, "", ""};
         }

         return true;
      } else {
         return false;
      }
   }

   protected void func_143012_a(NBTTagCompound par1NBTTagCompound) {
      super.func_143012_a(par1NBTTagCompound);
      par1NBTTagCompound.setBoolean("WITCApocChest", this.hasMadeChest);
   }

   protected void func_143011_b(NBTTagCompound par1NBTTagCompound) {
      super.func_143011_b(par1NBTTagCompound);
      this.hasMadeChest = par1NBTTagCompound.getBoolean("WITCApocChest");
   }

   protected int getVillagerType(int par1) {
      return Config.instance().apothecaryID;
   }

}
