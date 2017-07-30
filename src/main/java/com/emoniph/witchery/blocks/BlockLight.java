package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BlockLight extends BlockBase {

   public BlockLight() {
      super(Material.air);
      this.setLightLevel(1.0F);
      super.registerWithCreateTab = false;
   }

   public int getRenderType() {
      return -1;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_) {
      return false;
   }

   public void dropBlockAsItemWithChance(World world, int x, int y, int z, int side, float p_149690_6_, int p_149690_7_) {}
}
