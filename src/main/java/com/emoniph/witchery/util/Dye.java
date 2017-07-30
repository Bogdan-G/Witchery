package com.emoniph.witchery.util;

import com.emoniph.witchery.brewing.BrewItemKey;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public enum Dye {

   INK_SAC("INK_SAC", 0, 0, 1118481, "black"),
   ROSE_RED("ROSE_RED", 1, 1, 12464176, "red"),
   CACTUS_GREEN("CACTUS_GREEN", 2, 2, 5732898, "green"),
   COCOA_BEANS("COCOA_BEANS", 3, 3, 5057301, "brown"),
   LAPIS_LAZULI("LAPIS_LAZULI", 4, 4, 2247599, "blue"),
   PURPLE_DYE("PURPLE_DYE", 5, 5, 8532146, "purple"),
   CYAN_DYE("CYAN_DYE", 6, 6, 3968688, "cyan"),
   LIGHT_GRAY_DYE("LIGHT_GRAY_DYE", 7, 7, 11513789, "lightgray"),
   GRAY_DYE("GRAY_DYE", 8, 8, 7763574, "gray"),
   PINK_DYE("PINK_DYE", 9, 9, 15574987, "pink"),
   LIME_DYE("LIME_DYE", 10, 10, 8639516, "lime"),
   DANDELION_YELLOW("DANDELION_YELLOW", 11, 11, 15197994, "yellow"),
   LIGHT_BLUE_DYE("LIGHT_BLUE_DYE", 12, 12, 12179199, "lightblue"),
   MAGENTA_DYE("MAGENTA_DYE", 13, 13, 14383829, "magenta"),
   ORANGE_DYE("ORANGE_DYE", 14, 14, 15113780, "orange"),
   BONE_MEAL("BONE_MEAL", 15, 15, 16777215, "white");
   public final int damageValue;
   public final int rgb;
   public final String unlocalizedName;
   public static final Dye[] DYES = new Dye[]{INK_SAC, ROSE_RED, CACTUS_GREEN, COCOA_BEANS, LAPIS_LAZULI, PURPLE_DYE, CYAN_DYE, LIGHT_GRAY_DYE, GRAY_DYE, PINK_DYE, LIME_DYE, DANDELION_YELLOW, LIGHT_BLUE_DYE, MAGENTA_DYE, ORANGE_DYE, BONE_MEAL};
   // $FF: synthetic field
   private static final Dye[] $VALUES = new Dye[]{INK_SAC, ROSE_RED, CACTUS_GREEN, COCOA_BEANS, LAPIS_LAZULI, PURPLE_DYE, CYAN_DYE, LIGHT_GRAY_DYE, GRAY_DYE, PINK_DYE, LIME_DYE, DANDELION_YELLOW, LIGHT_BLUE_DYE, MAGENTA_DYE, ORANGE_DYE, BONE_MEAL};


   private Dye(String var1, int var2, int damageValue, int rgb, String unlocalizedName) {
      this.damageValue = damageValue;
      this.rgb = rgb;
      this.unlocalizedName = unlocalizedName;
   }

   public ItemStack createStack() {
      return this.createStack(1);
   }

   public ItemStack createStack(int quantity) {
      return new ItemStack(Items.dye, quantity, this.damageValue);
   }

   public static Dye fromStack(ItemStack potion) {
      return potion.getItem() == Items.dye && potion.getItemDamage() >= 0 && potion.getItemDamage() < DYES.length?DYES[potion.getItemDamage()]:BONE_MEAL;
   }

   public BrewItemKey getBrewItemKey() {
      return new BrewItemKey(Items.dye, this.damageValue);
   }

}
