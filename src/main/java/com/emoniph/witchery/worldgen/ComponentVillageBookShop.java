package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;

public class ComponentVillageBookShop extends Village {

   public static WeightedRandomChestContent[] bookshopChestContents = null;
   private boolean hasMadeChest;


   public static ComponentVillageBookShop construct(Start start, List pieces, Random rand, int p1, int p2, int p3, int p4, int p5) {
      StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 10, 8, 9, p4);
      return canVillageGoDeeper(bounds) && StructureComponent.findIntersecting(pieces, bounds) == null?new ComponentVillageBookShop(start, p5, rand, bounds, p4):null;
   }

   public ComponentVillageBookShop() {}

   public ComponentVillageBookShop(Start start, int componentType, Random rand, StructureBoundingBox bounds, int coordMode) {
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

         super.boundingBox.offset(0, super.field_143015_k - super.boundingBox.maxY + 8 - 1, 0);
      }

      this.fillWithBlocks(world, bounds, 1, 0, 1, 8, 0, 6, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 2, 0, 2, 7, 0, 5, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(world, bounds, 1, 1, 0, 8, 7, 6, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 1, 1, 3, 8, 5, 6, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 1, 6, 4, 8, 6, 5, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 1, 4, 1, 8, 4, 2, Blocks.cobblestone, Blocks.cobblestone, false);
      this.fillWithBlocks(world, bounds, 2, 1, 4, 7, 4, 5, Blocks.air, Blocks.air, false);
      this.fillWithBlocks(world, bounds, 2, 1, 3, 7, 3, 3, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(world, bounds, 3, 2, 3, 6, 3, 3, Blocks.air, Blocks.air, false);
      this.placeBlockAtCurrentPosition(world, Blocks.air, 0, 6, 1, 3, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 3, 4, 4, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 6, 4, 4, bounds);
      this.fillWithBlocks(world, bounds, 1, 2, 4, 1, 4, 5, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(world, bounds, 8, 2, 4, 8, 4, 5, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(world, bounds, 2, 2, 6, 7, 4, 6, Blocks.planks, Blocks.planks, false);
      this.fillWithBlocks(world, bounds, 1, 1, 1, 1, 3, 1, Blocks.fence, Blocks.fence, false);
      this.fillWithBlocks(world, bounds, 8, 1, 1, 8, 3, 1, Blocks.fence, Blocks.fence, false);
      int n = this.getMetadataWithOffset(Blocks.oak_stairs, 3);
      int s = this.getMetadataWithOffset(Blocks.oak_stairs, 2);
      int w = this.getMetadataWithOffset(Blocks.oak_stairs, 0);
      int e = this.getMetadataWithOffset(Blocks.oak_stairs, 1);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, n, 3, 0, 0, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, n, 4, 0, 0, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, n, 5, 0, 0, bounds);
      this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, n, 6, 0, 0, bounds);

      int j;
      for(j = 1; j <= 8; ++j) {
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, j, 5, 2, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, j, 6, 3, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, n, j, 7, 4, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, j, 5, 7, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, j, 6, 6, bounds);
         this.placeBlockAtCurrentPosition(world, Blocks.oak_stairs, s, j, 7, 5, bounds);
      }

      int k;
      if(!this.hasMadeChest) {
         j = this.getYWithOffset(1);
         k = this.getXWithOffset(2, 4);
         int k1 = this.getZWithOffset(2, 4);
         if(bounds.isVecInside(k, j, k1)) {
            Log.instance().debug(String.format("Bookshop %d %d %d - dir %d", new Object[]{Integer.valueOf(j), Integer.valueOf(k), Integer.valueOf(k1), Integer.valueOf(super.coordBaseMode)}));
            if(bookshopChestContents == null) {
               ArrayList list = new ArrayList();
               list.add(new WeightedRandomChestContent(Items.book, 0, 1, 1, 1));
               list.add(new WeightedRandomChestContent(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemVampireBookPage.damageValue, 1, 1, 3));
               list.add(new WeightedRandomChestContent(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemVampireBookPage.damageValue, 1, 1, 2));
               list.add(new WeightedRandomChestContent(Witchery.Items.GENERIC, Witchery.Items.GENERIC.itemVampireBookPage.damageValue, 1, 1, 1));
               String[] arr$ = Config.instance().townBooks;
               int len$ = arr$.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  String bonusBook = arr$[i$];

                  try {
                     String ex = bonusBook;
                     int meta = 0;
                     int comma = bonusBook.lastIndexOf(44);
                     if(comma >= 0) {
                        ex = bonusBook.substring(0, comma);
                        String item = bonusBook.substring(comma + 1);
                        meta = Integer.parseInt(item);
                     }

                     Item var22 = (Item)Item.itemRegistry.getObject(ex);
                     if(var22 != null) {
                        list.add(new WeightedRandomChestContent(var22, meta, 1, 1, 1));
                     }
                  } catch (Throwable var21) {
                     ;
                  }
               }

               bookshopChestContents = (WeightedRandomChestContent[])list.toArray(new WeightedRandomChestContent[list.size()]);
            }

            this.hasMadeChest = true;
            this.generateStructureChestContents(world, bounds, rand, 2, 1, 4, bookshopChestContents, 5 + rand.nextInt(6), new ItemStack[]{new ItemStack(Witchery.Items.VAMPIRE_BOOK)});
            this.addBookInFrame(world, 3, 3, 6, bookshopChestContents[rand.nextInt(bookshopChestContents.length)].theItemId.copy());
            this.addBookInFrame(world, 4, 3, 6, bookshopChestContents[rand.nextInt(bookshopChestContents.length)].theItemId.copy());
            this.addBookInFrame(world, 5, 3, 6, bookshopChestContents[rand.nextInt(bookshopChestContents.length)].theItemId.copy());
            this.addBookInFrame(world, 6, 3, 6, bookshopChestContents[rand.nextInt(bookshopChestContents.length)].theItemId.copy());
         }
      }

      for(j = 1; j < 7; ++j) {
         for(k = 1; k < 7; ++k) {
            this.clearCurrentPositionBlocksUpwards(world, k, 8, j, bounds);
            this.func_151554_b(world, Blocks.cobblestone, 0, k, -1, j, bounds);
         }
      }

      return true;
   }

   private boolean generateStructureChestContents(World world, StructureBoundingBox bounds, Random rand, int x, int y, int z, WeightedRandomChestContent[] contents, int quantity, ItemStack[] extraItems) {
      int i1 = this.getXWithOffset(x, z);
      int j1 = this.getYWithOffset(y);
      int k1 = this.getZWithOffset(x, z);
      if(bounds.isVecInside(i1, j1, k1) && world.getBlock(i1, j1, k1) != Blocks.chest) {
         world.setBlock(i1, j1, k1, Blocks.chest, 0, 2);
         TileEntityChest chest = (TileEntityChest)world.getTileEntity(i1, j1, k1);
         if(chest != null) {
            WeightedRandomChestContent.generateChestContents(rand, contents, chest, quantity);
            if(extraItems != null) {
               ItemStack[] arr$ = extraItems;
               int len$ = extraItems.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  ItemStack stack = arr$[i$];
                  chest.setInventorySlotContents(rand.nextInt(chest.getSizeInventory()), stack.copy());
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private void addBookInFrame(World world, int x, int y, int z, ItemStack stack) {
      int xWorld = this.getXWithOffset(x, z);
      int yWorld = this.getYWithOffset(y);
      int zWorld = this.getZWithOffset(x, z);
      boolean direction = false;
      byte direction1;
      switch(super.coordBaseMode) {
      case 0:
      default:
         direction1 = 2;
         break;
      case 1:
         direction1 = 3;
         break;
      case 2:
         direction1 = 0;
         break;
      case 3:
         direction1 = 1;
      }

      EntityItemFrame frame = new EntityItemFrame(world, xWorld, yWorld, zWorld, direction1);
      if(frame != null && frame.onValidSurface() && !world.isRemote) {
         world.spawnEntityInWorld(frame);
         frame.setDisplayedItem(stack);
      }

   }

   protected void func_143012_a(NBTTagCompound nbtRoot) {
      super.func_143012_a(nbtRoot);
      nbtRoot.setBoolean("Chest", this.hasMadeChest);
   }

   protected void func_143011_b(NBTTagCompound nbtRoot) {
      super.func_143011_b(nbtRoot);
      this.hasMadeChest = nbtRoot.getBoolean("Chest");
   }

}
