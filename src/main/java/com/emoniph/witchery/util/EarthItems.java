package com.emoniph.witchery.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class EarthItems {

   private static final EarthItems INSTANCE = new EarthItems();
   private final List itemList;


   public static EarthItems instance() {
      return INSTANCE;
   }

   private EarthItems() {
      Item[] list = new Item[]{Items.iron_sword, Items.golden_sword, Items.iron_axe, Items.golden_axe, Items.iron_pickaxe, Items.golden_pickaxe, Items.golden_hoe, Items.iron_hoe, Items.iron_shovel, Items.golden_shovel, Items.gold_ingot, Items.gold_nugget, Items.iron_ingot, Items.iron_helmet, Items.iron_chestplate, Items.iron_leggings, Items.iron_boots, Items.golden_helmet, Items.golden_leggings, Items.golden_chestplate, Items.golden_boots, Items.chainmail_helmet, Items.chainmail_leggings, Items.chainmail_chestplate, Items.chainmail_boots};
      this.itemList = Arrays.asList(list);
   }

   public boolean isMatch(ItemStack itemstack) {
      return itemstack == null?false:this.itemList.contains(itemstack.getItem());
   }

   public boolean isOre(Block block) {
      return block == Blocks.iron_ore || block == Blocks.gold_ore;
   }

   public Item oreToIngot(Block block) {
      if(block == Blocks.iron_ore) {
         return Items.iron_ingot;
      } else if(block == Blocks.gold_ore) {
         return Items.gold_ingot;
      } else {
         int oreID = OreDictionary.getOreID(new ItemStack(block));
         if(oreID != -1) {
            String oreName = OreDictionary.getOreName(oreID);
            if(oreName.startsWith("ore")) {
               String ingotName = oreName.replace("ore", "ingot");
               String[] oreNames = OreDictionary.getOreNames();
               if(Arrays.asList(oreNames).contains(ingotName)) {
                  int ingotID = OreDictionary.getOreID(ingotName);
                  ArrayList ingotStacks = OreDictionary.getOres(Integer.valueOf(ingotID));
                  if(!ingotStacks.isEmpty()) {
                     return ((ItemStack)ingotStacks.get(0)).getItem();
                  }
               }
            }
         }

         return null;
      }
   }

}
