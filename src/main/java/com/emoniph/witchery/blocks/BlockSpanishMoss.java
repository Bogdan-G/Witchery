package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSpanishMoss extends BlockVine {

   public BlockSpanishMoss() {
      this.setHardness(0.2F);
      this.setStepSound(Block.soundTypeGrass);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      super.setBlockName(blockName);
      Blocks.fire.setFireInfo(this, 15, 100);
      return this;
   }

   public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public int getBlockColor() {
      return ColorizerFoliage.getFoliageColorBirch();
   }

   @SideOnly(Side.CLIENT)
   public int getRenderColor(int par1) {
      return ColorizerFoliage.getFoliageColorBirch();
   }

   public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      for(int i = 0; i < 2; ++i) {
         super.updateTick(par1World, par2, par3, par4, par5Random);
      }

   }
}
