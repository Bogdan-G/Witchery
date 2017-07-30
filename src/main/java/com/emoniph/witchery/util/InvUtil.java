package com.emoniph.witchery.util;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvUtil {

   public static int getSlotContainingItem(InventoryPlayer inventory, Item item, int damage) {
      for(int k = 0; k < inventory.mainInventory.length; ++k) {
         ItemStack stack = inventory.mainInventory[k];
         if(stack != null && stack.getItem() == item && stack.getItemDamage() == damage) {
            return k;
         }
      }

      return -1;
   }

   public static int getSlotContainingItem(InventoryPlayer inventory, Item item) {
      for(int k = 0; k < inventory.mainInventory.length; ++k) {
         ItemStack stack = inventory.mainInventory[k];
         if(stack != null && stack.getItem() == item) {
            return k;
         }
      }

      return -1;
   }

   public static int getSlotContainingItem(IInventory inventory, Item item, int damage) {
      for(int k = 0; k < inventory.getSizeInventory(); ++k) {
         ItemStack stack = inventory.getStackInSlot(k);
         if(stack != null && stack.getItem() == item && stack.getItemDamage() == damage) {
            return k;
         }
      }

      return -1;
   }

   public static boolean hasItem(InventoryPlayer inventory, Item item, int damage) {
      return getSlotContainingItem(inventory, item, damage) >= 0;
   }

   public static boolean consumeItem(InventoryPlayer inventory, Item item, int damage) {
      int j = getSlotContainingItem(inventory, item, damage);
      if(j < 0) {
         return false;
      } else {
         if(--inventory.mainInventory[j].stackSize <= 0) {
            inventory.mainInventory[j] = null;
         }

         return true;
      }
   }

   public static int getItemStackCount(IInventory inv) {
      int itemCount = 0;
      if(inv != null) {
         for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if(stack != null) {
               ++itemCount;
            }
         }
      }

      return itemCount;
   }
}
