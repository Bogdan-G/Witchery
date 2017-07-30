package com.emoniph.witchery.item;

import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;

public class ItemBiomeNote extends ItemBase {

   public ItemBiomeNote() {
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
   }

   public String getItemStackDisplayName(ItemStack stack) {
      String name = super.getItemStackDisplayName(stack);
      BiomeGenBase biome = ItemBook.getSelectedBiome(stack.getItemDamage());
      return biome != null?String.format(name, new Object[]{biome.biomeName}):String.format(name, new Object[]{""}).trim();
   }
}
