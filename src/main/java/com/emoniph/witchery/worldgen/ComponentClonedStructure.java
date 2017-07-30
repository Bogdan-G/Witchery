package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemDuplicationStaff;
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

public abstract class ComponentClonedStructure extends WitcheryComponent {

   private ItemDuplicationStaff.Rotation rotation;
   private int witchesSpawned = 0;
   public static final WeightedRandomChestContent[] shackChestContents = new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.glass_bottle, 0, 1, 1, 10), new WeightedRandomChestContent(Items.bread, 0, 1, 3, 15), new WeightedRandomChestContent(Items.apple, 0, 1, 3, 15), new WeightedRandomChestContent(Items.cooked_fished, 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.sapling), 1, 1, 1, 15), new WeightedRandomChestContent(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemRowanBerries.damageValue, 1, 2, 10), new WeightedRandomChestContent(Items.iron_shovel, 0, 1, 1, 5), new WeightedRandomChestContent(Items.iron_pickaxe, 0, 1, 1, 5)};
   private boolean hasMadeChest;
   private static final String CHEST_KEY = "WITCShackChest";


   public ComponentClonedStructure() {}

   public ComponentClonedStructure(int direction, Random random, int x, int z, int w, int h, int d) {
      super(direction, random, x, z, w, h, d);
      this.rotation = ItemDuplicationStaff.Rotation.values()[direction];
   }

   public boolean addComponentParts(World world, Random random) {
      BiomeGenBase biom = world.getBiomeGenForCoords(this.getXWithOffset(0, 0), this.getZWithOffset(0, 0));
      int groundAvg = this.calcGroundHeight(world, super.boundingBox);
      if(groundAvg < 0) {
         return true;
      } else {
         super.boundingBox.offset(0, groundAvg - super.boundingBox.maxY + super.boundingBox.getYSize() - 1, 0);
         if(!this.isWaterBelow(world, 0, -1, 0, super.boundingBox) && !this.isWaterBelow(world, 0, -1, super.boundingBox.getZSize() - 1, super.boundingBox) && !this.isWaterBelow(world, super.boundingBox.getXSize() - 1, -1, 0, super.boundingBox) && !this.isWaterBelow(world, super.boundingBox.getXSize() - 1, -1, super.boundingBox.getZSize() - 1, super.boundingBox)) {
            BlockGrass groundID = Blocks.grass;
            Object undergroundID = Blocks.dirt;
            if(biom.biomeID == BiomeGenBase.desert.biomeID || biom.biomeID == BiomeGenBase.desertHills.biomeID || biom.biomeID == BiomeGenBase.beach.biomeID) {
               BlockSand var10 = Blocks.sand;
               undergroundID = Blocks.sand;
            }

            NBTTagCompound nbtSchematic = this.getSchematic(world, random);
            ItemDuplicationStaff.drawSchematicInWorld(world, super.boundingBox.minX, super.boundingBox.minY, super.boundingBox.minZ, this.rotation, true, nbtSchematic);

            for(int i = 0; i < super.boundingBox.getXSize(); ++i) {
               for(int j = 0; j < super.boundingBox.getZSize(); ++j) {
                  this.func_151554_b(world, (Block)undergroundID, 0, j, 0, i, super.boundingBox);
               }
            }

            this.spawnWitches(world, super.boundingBox, super.boundingBox.getXSize() - 3, 1, 3, 1);
            return true;
         } else {
            return false;
         }
      }
   }

   protected abstract NBTTagCompound getSchematic(World var1, Random var2);

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
            this.spawnInhabitant(par1World, par2StructureBoundingBox);
         }
      }

   }

   protected abstract void spawnInhabitant(World var1, StructureBoundingBox var2);

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
