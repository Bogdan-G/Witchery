package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockMirrorWall extends BlockBase {

   @SideOnly(Side.CLIENT)
   protected IIcon[] icons;


   public BlockMirrorWall() {
      super(Material.rock);
      this.setBlockUnbreakable();
      this.setResistance(9999.0F);
      this.disableStats();
   }

   public int getRenderType() {
      return super.getRenderType();
   }

   public int getBlockColor() {
      return 13426175;
   }

   @SideOnly(Side.CLIENT)
   public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
      return this.getBlockColor();
   }

   @SideOnly(Side.CLIENT)
   public int getRenderColor(int par1) {
      return this.getBlockColor();
   }

   protected boolean canSilkHarvest() {
      return false;
   }

   public int quantityDropped(Random rand) {
      return 0;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return side != 0 && side != 1?this.icons[1]:this.icons[0];
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.icons = new IIcon[]{iconRegister.registerIcon(this.getTextureName() + "_still"), iconRegister.registerIcon(this.getTextureName() + "_flow")};
   }
}
