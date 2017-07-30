package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;

public abstract class BlockBaseRotatedPillar extends BlockRotatedPillar {

   protected boolean registerBlockName = true;
   protected boolean registerWithCreateTab = true;
   protected final Class clazzItem;


   public BlockBaseRotatedPillar(Material material, Class clazzItem) {
      super(material);
      this.clazzItem = clazzItem;
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

   protected abstract IIcon getSideIcon(int var1);
}
