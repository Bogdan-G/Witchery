package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBase;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockForce extends BlockBase {

   private final boolean transparent;


   public BlockForce(boolean transparent) {
      super(Material.rock);
      this.transparent = transparent;
      super.registerWithCreateTab = false;
      this.setBlockUnbreakable();
      this.setResistance(9999.0F);
      this.setLightOpacity(0);
      this.setStepSound(transparent?Block.soundTypeGlass:Block.soundTypeStone);
   }

   public int getRenderType() {
      return this.transparent?-1:super.getRenderType();
   }

   protected boolean canSilkHarvest() {
      return false;
   }

   public int quantityDropped(Random rand) {
      return 0;
   }

   public int getRenderBlockPass() {
      return 0;
   }

   public boolean isOpaqueCube() {
      return this.transparent?false:super.isOpaqueCube();
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return null;
   }
}
