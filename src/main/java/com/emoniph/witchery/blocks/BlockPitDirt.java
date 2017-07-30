package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPitDirt extends BlockBase {

   private static final Material passThrough = new Material(MapColor.dirtColor) {
      public boolean blocksMovement() {
         return true;
      }
      public boolean isOpaque() {
         return false;
      }
   };
   @SideOnly(Side.CLIENT)
   private IIcon iconPodzolTop;
   @SideOnly(Side.CLIENT)
   private IIcon iconPodzolSide;


   public BlockPitDirt() {
      super(passThrough);
      this.setHardness(0.5F);
      this.setStepSound(Block.soundTypeGravel);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
      if(p_149691_2_ == 2) {
         if(p_149691_1_ == 1) {
            return this.iconPodzolTop;
         }

         if(p_149691_1_ != 0) {
            return this.iconPodzolSide;
         }
      }

      return super.blockIcon;
   }

   public int damageDropped(int meta) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
      int i1 = world.getBlockMetadata(x, y, z);
      if(i1 == 2) {
         if(side == 1) {
            return this.iconPodzolTop;
         }

         if(side != 0) {
            Material material = world.getBlock(x, y + 1, z).getMaterial();
            if(material == Material.snow || material == Material.craftedSnow) {
               return Blocks.grass.getIcon(world, x, y, z, side);
            }

            Block block = world.getBlock(x, y + 1, z);
            if(block != Blocks.dirt && block != Blocks.grass) {
               return this.iconPodzolSide;
            }
         }
      }

      return super.blockIcon;
   }

   protected ItemStack createStackedBlock(int p_149644_1_) {
      if(p_149644_1_ == 1) {
         p_149644_1_ = 0;
      }

      return super.createStackedBlock(p_149644_1_);
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
      list.add(new ItemStack(this, 1, 0));
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      super.registerBlockIcons(iconRegister);
      this.iconPodzolTop = iconRegister.registerIcon(this.getTextureName() + "_" + "podzol_top");
      this.iconPodzolSide = iconRegister.registerIcon(this.getTextureName() + "_" + "podzol_side");
   }

   public int getDamageValue(World world, int x, int y, int z) {
      int l = world.getBlockMetadata(x, y, z);
      if(l == 1) {
         l = 0;
      }

      return l;
   }

}
