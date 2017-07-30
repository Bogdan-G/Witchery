package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPitGrass extends BlockBase {

   private static final Material passThrough = new Material(MapColor.grassColor) {
      public boolean blocksMovement() {
         return true;
      }
      public boolean isOpaque() {
         return false;
      }
   };
   @SideOnly(Side.CLIENT)
   private IIcon iconTop;
   @SideOnly(Side.CLIENT)
   private IIcon iconSideSnowed;
   @SideOnly(Side.CLIENT)
   private IIcon iconSideOverlay;


   public BlockPitGrass() {
      super(passThrough);
      this.setHardness(0.6F);
      this.setStepSound(Block.soundTypeGrass);
      this.setTickRandomly(false);
   }

   public int getRenderType() {
      return Witchery.proxy.getPitGrassRenderId();
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public Item getItemDropped(int p_149650_1_, Random rand, int p_149650_3_) {
      return Item.getItemFromBlock(this);
   }

   public void updateTick(World world, int x, int y, int z, Random rand) {}

   public boolean isOpaqueCube() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
      return p_149691_1_ == 1?this.iconTop:(p_149691_1_ == 0?Blocks.dirt.getBlockTextureFromSide(p_149691_1_):super.blockIcon);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
      if(side == 1) {
         return this.iconTop;
      } else if(side == 0) {
         return Blocks.dirt.getBlockTextureFromSide(side);
      } else {
         Material material = world.getBlock(x, y + 1, z).getMaterial();
         return material != Material.snow && material != Material.craftedSnow?super.blockIcon:this.iconSideSnowed;
      }
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      super.blockIcon = iconRegister.registerIcon(this.getTextureName() + "_side");
      this.iconTop = iconRegister.registerIcon(this.getTextureName() + "_top");
      this.iconSideSnowed = iconRegister.registerIcon(this.getTextureName() + "_side_snowed");
      this.iconSideOverlay = iconRegister.registerIcon(this.getTextureName() + "_side_overlay");
   }

   @SideOnly(Side.CLIENT)
   public int getBlockColor() {
      double d0 = 0.5D;
      double d1 = 1.0D;
      return ColorizerGrass.getGrassColor(d0, d1);
   }

   @SideOnly(Side.CLIENT)
   public int getRenderColor(int meta) {
      return this.getBlockColor();
   }

   @SideOnly(Side.CLIENT)
   public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
      int l = 0;
      int i1 = 0;
      int j1 = 0;

      for(int k1 = -1; k1 <= 1; ++k1) {
         for(int l1 = -1; l1 <= 1; ++l1) {
            int i2 = world.getBiomeGenForCoords(x + l1, z + k1).getBiomeGrassColor(x + l1, y, z + k1);
            l += (i2 & 16711680) >> 16;
            i1 += (i2 & '\uff00') >> 8;
            j1 += i2 & 255;
         }
      }

      return (l / 9 & 255) << 16 | (i1 / 9 & 255) << 8 | j1 / 9 & 255;
   }

}
