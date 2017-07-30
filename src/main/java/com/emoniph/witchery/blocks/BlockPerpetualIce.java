package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockPerpetualIce extends BlockIce {

   public BlockPerpetualIce() {
      this.setTickRandomly(false);
      super.slipperiness = 0.98F;
      this.setLightOpacity(3);
      this.setHardness(2.0F);
      this.setResistance(5.0F);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   public void updateTick(World world, int x, int y, int z, Random rand) {}

   public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
      return true;
   }

   public boolean func_149730_j() {
      return true;
   }
}
