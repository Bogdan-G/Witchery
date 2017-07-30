package com.emoniph.witchery.util;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ItemUtil {

   public static void registerItem(Item item, String itemName) {
      int index = itemName.indexOf(58);
      if(index != -1) {
         itemName = itemName.substring(index + 1);
      }

      GameRegistry.registerItem(item, itemName);
   }
}
