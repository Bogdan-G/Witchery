package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPerpetualIceSlab extends BlockSlab {

   public BlockPerpetualIceSlab(boolean doubleSlab) {
      super(doubleSlab, Material.ice);
      this.setLightOpacity(3);
      this.setHardness(2.0F);
      this.setResistance(5.0F);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      super.opaque = false;
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, (Class)null, blockName);
      super.setBlockName(blockName);
      return this;
   }

   @SideOnly(Side.CLIENT)
   public int getRenderBlockPass() {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess block, int x, int y, int z, int side) {
      if(super.field_150004_a) {
         return super.shouldSideBeRendered(block, x, y, z, side);
      } else if(side != 1 && side != 0 && !super.shouldSideBeRendered(block, x, y, z, side)) {
         return false;
      } else {
         int i1 = x + Facing.offsetsXForSide[Facing.oppositeSide[side]];
         int j1 = y + Facing.offsetsYForSide[Facing.oppositeSide[side]];
         int k1 = z + Facing.offsetsZForSide[Facing.oppositeSide[side]];
         boolean flag = (block.getBlockMetadata(i1, j1, k1) & 8) != 0;
         return flag?(side == 0?true:(side == 1 && super.shouldSideBeRendered(block, x, y, z, side)?true:!this.func_150003_aa(block.getBlock(x, y, z)) || (block.getBlockMetadata(x, y, z) & 8) == 0)):(side == 1?true:(side == 0 && super.shouldSideBeRendered(block, x, y, z, side)?true:!this.func_150003_aa(block.getBlock(x, y, z)) || (block.getBlockMetadata(x, y, z) & 8) != 0));
      }
   }

   public boolean isOpaqueCube() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   private boolean func_150003_aa(Block p_150003_0_) {
      return p_150003_0_ == Witchery.Blocks.PERPETUAL_ICE_SLAB_SINGLE || p_150003_0_ == Witchery.Blocks.PERPETUAL_ICE_SLAB_DOUBLE;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
      return Witchery.Blocks.PERPETUAL_ICE.getIcon(p_149691_1_, p_149691_2_ & 7);
   }

   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
      return Item.getItemFromBlock(Witchery.Blocks.PERPETUAL_ICE_SLAB_SINGLE);
   }

   protected ItemStack createStackedBlock(int p_149644_1_) {
      return new ItemStack(Item.getItemFromBlock(Witchery.Blocks.PERPETUAL_ICE_SLAB_SINGLE), 2, p_149644_1_ & 7);
   }

   public String func_150002_b(int p_150002_1_) {
      return super.getUnlocalizedName();
   }

   @SideOnly(Side.CLIENT)
   public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
      return super.field_150004_a?Witchery.Items.PERPERTUAL_ICE_SLAB_DOUBLE:Witchery.Items.PERPERTUAL_ICE_SLAB_SINGLE;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister p_149651_1_) {}
}
