package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;

public class BlockSnowStairs extends BlockStairs {

   public BlockSnowStairs(Block baseBlock, int baseMeta) {
      super(baseBlock, baseMeta);
      this.setHardness(0.2F);
      this.setStepSound(Block.soundTypeSnow);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      super.setBlockName(blockName);
      return this;
   }
}
