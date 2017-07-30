package com.emoniph.witchery.util;

import com.emoniph.witchery.Witchery;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotClayJar extends Slot {

   public SlotClayJar(IInventory par2IInventory, int par3, int par4, int par5) {
      super(par2IInventory, par3, par4, par5);
   }

   public boolean isItemValid(ItemStack itemstack) {
      return itemstack != null && Witchery.Items.GENERIC.itemEmptyClayJar.isMatch(itemstack);
   }

   public int getSlotStackLimit() {
      return 64;
   }
}
