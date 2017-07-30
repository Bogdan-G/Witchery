package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class BlockPerpertualIceStairs extends BlockStairs {

   public BlockPerpertualIceStairs(Block baseBlock, int baseMeta) {
      super(baseBlock, baseMeta);
      super.slipperiness = 0.98F;
      this.setLightOpacity(3);
      this.setHardness(2.0F);
      this.setResistance(5.0F);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      super.setBlockName(blockName);
      return this;
   }
}
