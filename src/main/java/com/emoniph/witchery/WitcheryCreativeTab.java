package com.emoniph.witchery;

import com.emoniph.witchery.Witchery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class WitcheryCreativeTab extends CreativeTabs {

   public static final WitcheryCreativeTab INSTANCE = new WitcheryCreativeTab();


   private WitcheryCreativeTab() {
      super("tabWitchery");
   }

   public Item getTabIconItem() {
      return Witchery.Items.POPPET;
   }

   public ItemStack getIconItemStack() {
      return Witchery.Items.GENERIC.itemBroomEnchanted.createStack();
   }

}
