package com.emoniph.witchery.item;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.ItemUtil;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;

public class ItemWitchSlab extends ItemSlab {

   public ItemWitchSlab(BlockSlab slab, BlockSlab singleSlab, BlockSlab doubleSlab) {
      super(slab, singleSlab, doubleSlab, slab == doubleSlab);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public ItemBlock setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      return super.setUnlocalizedName(itemName);
   }

   public String getUnlocalizedName(ItemStack itemStack) {
      return super.getUnlocalizedName(itemStack);
   }
}
