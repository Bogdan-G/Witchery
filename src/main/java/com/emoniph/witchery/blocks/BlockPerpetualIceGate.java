package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class BlockPerpetualIceGate extends BlockFenceGate {

   public BlockPerpetualIceGate() {
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      this.setLightOpacity(3);
      this.setHardness(2.0F);
      this.setResistance(5.0F);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   @SideOnly(Side.CLIENT)
   public int getRenderBlockPass() {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
      return Blocks.ice.getBlockTextureFromSide(p_149691_1_);
   }
}
