package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.MultiItemBlock;
import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockShadedGlass extends BlockBreakable {

   private final IIcon[] icons = new IIcon[16];
   private static final String[] colors = (String[])Lists.reverse(Arrays.asList(ItemDye.field_150921_b)).toArray(new String[ItemDye.field_150921_b.length]);
   private boolean shaded;


   public BlockShadedGlass(boolean shaded) {
      super(shaded?"witchery:shadedglass":"glass", shaded?Material.rock:Material.glass, false);
      this.shaded = shaded;
      this.setHardness(0.3F);
      if(shaded) {
         this.setLightOpacity(15);
      }

      this.setStepSound(Block.soundTypeGlass);
      if(!shaded) {
         this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      }

   }

   public int onBlockPlaced(World p_149660_1_, int p_149660_2_, int p_149660_3_, int p_149660_4_, int p_149660_5_, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
      return super.onBlockPlaced(p_149660_1_, p_149660_2_, p_149660_3_, p_149660_4_, p_149660_5_, p_149660_6_, p_149660_7_, p_149660_8_, p_149660_9_);
   }

   public void onBlockAdded(World world, int x, int y, int z) {
      this.updatePoweredState(world, x, y, z);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, BlockShadedGlass.ClassItemBlock.class, blockName);
      return super.setBlockName(blockName);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int meta) {
      return this.icons[meta % this.icons.length];
   }

   public int damageDropped(int meta) {
      return meta;
   }

   public int quantityDropped(Random rand) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public static int func_149997_b(int meta) {
      return ~meta & 15;
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
      for(int i = 0; i < this.icons.length; ++i) {
         list.add(new ItemStack(item, 1, i));
      }

   }

   @SideOnly(Side.CLIENT)
   public int getRenderBlockPass() {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      for(int i = 0; i < this.icons.length; ++i) {
         this.icons[i] = iconRegister.registerIcon(this.getTextureName() + "_" + ItemDye.field_150921_b[func_149997_b(i)]);
      }

   }

   protected boolean canSilkHarvest() {
      return true;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      this.updatePoweredState(world, x, y, z);
   }

   public void updatePoweredState(World world, int x, int y, int z) {
      if(world.isBlockIndirectlyGettingPowered(x, y, z)) {
         if(!this.shaded) {
            world.setBlock(x, y, z, Witchery.Blocks.SHADED_GLASS_ON, world.getBlockMetadata(x, y, z), 3);
         }
      } else if(this.shaded) {
         world.setBlock(x, y, z, Witchery.Blocks.SHADED_GLASS, world.getBlockMetadata(x, y, z), 3);
      }

   }


   public static class ClassItemBlock extends MultiItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      protected String[] getNames() {
         return BlockShadedGlass.colors;
      }
   }
}
