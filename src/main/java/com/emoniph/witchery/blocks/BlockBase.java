package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBase extends Block {

   protected boolean registerBlockName;
   protected boolean registerWithCreateTab;
   protected final Class clazzItem;


   public BlockBase(Material material, Class clazzItem) {
      super(material);
      this.registerBlockName = true;
      this.registerWithCreateTab = true;
      this.clazzItem = clazzItem;
   }

   public BlockBase(Material material) {
      this(material, (Class)null);
   }

   public Block setBlockName(String blockName) {
      if(this.registerWithCreateTab) {
         this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      }

      if(this.registerBlockName) {
         if(this.clazzItem == null) {
            BlockUtil.registerBlock(this, blockName);
         } else {
            BlockUtil.registerBlock(this, this.clazzItem, blockName);
         }
      }

      return super.setBlockName(blockName);
   }
}
