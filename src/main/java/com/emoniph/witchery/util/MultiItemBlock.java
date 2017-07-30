package com.emoniph.witchery.util;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public abstract class MultiItemBlock extends ItemBlock {

   private String[] names = null;


   public MultiItemBlock(Block block) {
      super(block);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   private String[] internalGetNames() {
      if(this.names == null) {
         this.names = this.getNames();
      }

      return this.names;
   }

   protected abstract String[] getNames();

   public int getMetadata(int par1) {
      return par1;
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      int i = par1ItemStack.getItemDamage();
      String[] names = this.internalGetNames();
      if(i < 0 || i >= names.length) {
         i = 0;
      }

      return super.getUnlocalizedName() + "." + names[i];
   }
}
