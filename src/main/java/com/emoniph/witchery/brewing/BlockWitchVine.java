package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWitchVine extends BlockBase {

   public BlockWitchVine() {
      super(new Material(Material.vine.getMaterialMapColor()) {
         {
            this.setBurning();
            this.setNoPushMobility();
         }
      });
      super.registerWithCreateTab = false;
      this.setHardness(0.2F);
      this.setStepSound(Block.soundTypeGrass);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      this.setBlockBoundsBasedOnState(world, x, y, z);
      return super.getCollisionBoundingBoxFromPool(world, x, y, z);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
      this.setBoundsBasedOnMetadata(world.getBlockMetadata(x, y, z));
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
      this.setBlockBoundsBasedOnState(world, x, y, z);
      return super.getSelectedBoundingBoxFromPool(world, x, y, z);
   }

   @SideOnly(Side.CLIENT)
   public int getBlockColor() {
      return ColorizerFoliage.getFoliageColorBasic();
   }

   @SideOnly(Side.CLIENT)
   public int getRenderColor(int meta) {
      return ColorizerFoliage.getFoliageColorBasic();
   }

   @SideOnly(Side.CLIENT)
   public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
      return world.getBiomeGenForCoords(x, z).getBiomeFoliageColor(x, y, z);
   }

   public void setBoundsBasedOnMetadata(int meta) {
      float f = 0.125F;
      if(meta == 2) {
         this.setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
      } else if(meta == 3) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
      } else if(meta == 4) {
         this.setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      } else if(meta == 5) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
      }

   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return Witchery.proxy.getVineRenderId();
   }

   public Item getItemDropped(int metadata, Random rand, int fortune) {
      return null;
   }

   public int quantityDropped(Random rand) {
      return 0;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return null;
   }

   public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
      return true;
   }
}
