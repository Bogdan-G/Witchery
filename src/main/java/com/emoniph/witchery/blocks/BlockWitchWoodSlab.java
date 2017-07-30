package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockWitchWoodSlab extends BlockSlab {

   public static final String[] BLOCK_TYPES = new String[]{"rowan", "alder", "hawthorn"};


   public BlockWitchWoodSlab(boolean doubleSlab) {
      super(doubleSlab, Material.wood);
      this.setHardness(2.0F);
      this.setResistance(5.0F);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, (Class)null, blockName);
      super.setBlockName(blockName);
      Blocks.fire.setFireInfo(this, 5, 20);
      return this;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
      return Witchery.Blocks.PLANKS.getIcon(p_149691_1_, p_149691_2_ & 7);
   }

   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
      return Item.getItemFromBlock(Witchery.Blocks.WOOD_SLAB_SINGLE);
   }

   protected ItemStack createStackedBlock(int p_149644_1_) {
      return new ItemStack(Item.getItemFromBlock(Witchery.Blocks.WOOD_SLAB_SINGLE), 2, p_149644_1_ & 7);
   }

   public String getUnlocalizedName() {
      return super.getUnlocalizedName();
   }

   public String func_150002_b(int p_150002_1_) {
      if(p_150002_1_ < 0 || p_150002_1_ >= BLOCK_TYPES.length) {
         p_150002_1_ = 0;
      }

      return super.getUnlocalizedName() + "." + BLOCK_TYPES[p_150002_1_];
   }

   @SideOnly(Side.CLIENT)
   public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
      return super.field_150004_a?Witchery.Items.SLAB_DOUBLE:Witchery.Items.SLAB_SINGLE;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
      if(p_149666_1_ != Item.getItemFromBlock(Witchery.Blocks.WOOD_SLAB_DOUBLE)) {
         ;
      }

      for(int i = 0; i < BLOCK_TYPES.length; ++i) {
         p_149666_3_.add(new ItemStack(p_149666_1_, 1, i));
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister p_149651_1_) {}

}
