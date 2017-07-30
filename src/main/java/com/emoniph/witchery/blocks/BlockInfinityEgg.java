package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BlockInfinityEgg extends BlockDragonEgg {

   public BlockInfinityEgg() {
      this.setHardness(3.0F);
      this.setResistance(15.0F);
      this.setStepSound(Block.soundTypePiston);
      this.setLightLevel(0.125F);
   }

   public Block setBlockName(String blockName) {
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      return true;
   }

   public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {}
}
