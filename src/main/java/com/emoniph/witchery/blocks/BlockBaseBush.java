package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public abstract class BlockBaseBush extends BlockBush {

   protected boolean registerBlockName;
   protected boolean registerWithCreateTab;
   protected final Class clazzItem;


   public BlockBaseBush(Material material) {
      this(material, (Class)null);
   }

   public BlockBaseBush(Material material, Class clazzItem) {
      super(material);
      this.registerBlockName = true;
      this.registerWithCreateTab = true;
      this.clazzItem = clazzItem;
   }

   public Block setBlockName(String blockName) {
      if(this.registerWithCreateTab) {
         this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      } else {
         this.setCreativeTab((CreativeTabs)null);
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
