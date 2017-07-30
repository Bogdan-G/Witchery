package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;

public class BlockPerpetualIceFence extends BlockFence {

   public BlockPerpetualIceFence(String name) {
      super(name, Material.ice);
      this.setHardness(2.0F);
      this.setResistance(5.0F);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   @SideOnly(Side.CLIENT)
   public int getRenderBlockPass() {
      return 1;
   }

   public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      return super.canConnectFenceTo(world, x, y, z) || block == Witchery.Blocks.PERPETUAL_ICE_FENCE_GATE;
   }
}
