package com.emoniph.witchery.util;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public enum ClothColor {

   WHITE("WHITE", 0, 0),
   ORANGE("ORANGE", 1, 1),
   MAGENTA("MAGENTA", 2, 2),
   LIGHT_BLUE("LIGHT_BLUE", 3, 3),
   YELLOW("YELLOW", 4, 4),
   LIME("LIME", 5, 5),
   PINK("PINK", 6, 6),
   GRAY("GRAY", 7, 7),
   LIGHT_GRAY("LIGHT_GRAY", 8, 8),
   CYAN("CYAN", 9, 9),
   PURPLE("PURPLE", 10, 10),
   BLUE("BLUE", 11, 11),
   BROWN("BROWN", 12, 12),
   GREEN("GREEN", 13, 13),
   RED("RED", 14, 14),
   BLACK("BLACK", 15, 15);
   public final int damageValue;
   // $FF: synthetic field
   private static final ClothColor[] $VALUES = new ClothColor[]{WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK};


   private ClothColor(String var1, int var2, int damageValue) {
      this.damageValue = damageValue;
   }

   public ItemStack createStack() {
      return this.createStack(1);
   }

   private ItemStack createStack(int quantity) {
      return new ItemStack(Blocks.wool, quantity, this.damageValue);
   }

}
