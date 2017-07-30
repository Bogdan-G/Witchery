package com.emoniph.witchery.crafting;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBook;
import com.emoniph.witchery.util.InvUtil;
import java.util.Arrays;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

public class RecipeShapelessBiomeCopy extends ShapelessRecipes {

   public RecipeShapelessBiomeCopy(ItemStack result, ItemStack ... ingredients) {
      super(result, Arrays.asList(ingredients));
   }

   public ItemStack getCraftingResult(InventoryCrafting matrix) {
      ItemStack result = super.getCraftingResult(matrix);
      int slot = InvUtil.getSlotContainingItem((IInventory)matrix, Witchery.Items.BIOME_BOOK, 0);
      if(slot != -1) {
         ItemStack stack = matrix.getStackInSlot(slot);
         int biomeNumber = ItemBook.getSelectedBiome(stack, 1000);
         result.setItemDamage(biomeNumber);
      }

      return result;
   }
}
