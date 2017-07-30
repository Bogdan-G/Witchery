package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockBrew extends BlockFluidClassic {

   @SideOnly(Side.CLIENT)
   protected IIcon[] icons;


   public BlockBrew(Fluid fluid) {
      super(fluid, Material.water);
      super.quantaPerBlock = 2;
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return side != 0 && side != 1?this.icons[1]:this.icons[0];
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.icons = new IIcon[]{iconRegister.registerIcon(this.getTextureName() + "_still"), iconRegister.registerIcon(this.getTextureName() + "_flow")};
      Witchery.Fluids.BREW.setIcons(this.icons[0], this.icons[1]);
   }

   public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
      return world.getBlock(x, y, z).getMaterial().isLiquid()?false:super.canDisplace(world, x, y, z);
   }

   public boolean displaceIfPossible(World world, int x, int y, int z) {
      return world.getBlock(x, y, z).getMaterial().isLiquid()?false:super.displaceIfPossible(world, x, y, z);
   }
}
