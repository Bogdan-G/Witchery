package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBloodedWool extends BlockBase {

   public BlockBloodedWool() {
      super(Material.cloth);
      this.setHardness(0.8F);
      this.setStepSound(Block.soundTypeCloth);
   }
}
