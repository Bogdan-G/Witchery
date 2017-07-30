package com.emoniph.witchery.brewing;

import com.emoniph.witchery.blocks.BlockBase;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockWitchWeb extends BlockBase {

   public BlockWitchWeb() {
      super(Material.cloth);
      this.setLightOpacity(1);
      this.setHardness(8.0F);
      super.registerWithCreateTab = false;
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      entity.setInWeb();
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public int getRenderType() {
      return 1;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int quantityDropped(Random rand) {
      return 0;
   }

   public Item getItemDropped(int metadata, Random rand, int fortune) {
      return null;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return null;
   }

   protected boolean canSilkHarvest() {
      return false;
   }
}
