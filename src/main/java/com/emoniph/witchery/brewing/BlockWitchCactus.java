package com.emoniph.witchery.brewing;

import com.emoniph.witchery.blocks.BlockBase;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockWitchCactus extends BlockBase {

   @SideOnly(Side.CLIENT)
   private IIcon iconTop;
   @SideOnly(Side.CLIENT)
   private IIcon iconBottom;


   public BlockWitchCactus() {
      super(Material.cactus);
      this.setHardness(0.4F);
      this.setStepSound(Block.soundTypeCloth);
      super.registerWithCreateTab = false;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      float f = 0.0625F;
      return AxisAlignedBB.getBoundingBox((double)((float)x + f), (double)y, (double)((float)z + f), (double)((float)(x + 1) - f), (double)((float)(y + 1) - f), (double)((float)(z + 1) - f));
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
      float f = 0.0625F;
      return AxisAlignedBB.getBoundingBox((double)((float)x + f), (double)y, (double)((float)z + f), (double)((float)(x + 1) - f), (double)(y + 1), (double)((float)(z + 1) - f));
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return side == 1?this.iconTop:(side == 0?this.iconBottom:super.blockIcon);
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public int getRenderType() {
      return 13;
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      entity.attackEntityFrom(DamageSource.cactus, 1.0F);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      super.blockIcon = iconRegister.registerIcon(this.getTextureName() + "_side");
      this.iconTop = iconRegister.registerIcon(this.getTextureName() + "_top");
      this.iconBottom = iconRegister.registerIcon(this.getTextureName() + "_bottom");
   }

   public Item getItemDropped(int meta, Random random, int fortune) {
      return null;
   }

   public int quantityDropped(int meta, int fortune, Random random) {
      return 0;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return null;
   }

   public boolean canBlockStay(World world, int x, int y, int z) {
      return !BlockUtil.isReplaceableBlock(world, x, y - 1, z);
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      if(!this.canBlockStay(world, x, y, z)) {
         world.func_147480_a(x, y, z, true);
      }

   }
}
