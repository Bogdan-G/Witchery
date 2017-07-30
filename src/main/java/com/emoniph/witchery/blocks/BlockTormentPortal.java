package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.dimension.WorldProviderTorment;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.ParticleEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTormentPortal extends BlockBreakable {

   private static final double MORE_TORMENT_CHANCE = 0.05D;


   public BlockTormentPortal() {
      super("portal", Material.portal, false);
      this.setTickRandomly(true);
      this.setBlockUnbreakable();
      this.setResistance(9999.0F);
      this.setStepSound(Block.soundTypeGlass);
      this.setLightLevel(0.75F);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return null;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      float f;
      float f1;
      if(par1IBlockAccess.getBlock(par2 - 1, par3, par4) == Blocks.air && par1IBlockAccess.getBlock(par2 + 1, par3, par4) == Blocks.air) {
         f = 0.125F;
         f1 = 0.5F;
         this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
      } else {
         f = 0.5F;
         f1 = 0.125F;
         this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
      }

   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      if(par1IBlockAccess.getBlock(par2, par3, par4) == this) {
         return false;
      } else {
         boolean flag = par1IBlockAccess.getBlock(par2 - 1, par3, par4) == this && par1IBlockAccess.getBlock(par2 - 2, par3, par4) != this;
         boolean flag1 = par1IBlockAccess.getBlock(par2 + 1, par3, par4) == this && par1IBlockAccess.getBlock(par2 + 2, par3, par4) != this;
         boolean flag2 = par1IBlockAccess.getBlock(par2, par3, par4 - 1) == this && par1IBlockAccess.getBlock(par2, par3, par4 - 2) != this;
         boolean flag3 = par1IBlockAccess.getBlock(par2, par3, par4 + 1) == this && par1IBlockAccess.getBlock(par2, par3, par4 + 2) != this;
         boolean flag4 = flag || flag1;
         boolean flag5 = flag2 || flag3;
         return flag4 && par5 == 4?true:(flag4 && par5 == 5?true:(flag5 && par5 == 2?true:flag5 && par5 == 3));
      }
   }

   public int quantityDropped(Random par1Random) {
      return 0;
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote && entity instanceof EntityPlayer && entity.ridingEntity == null && entity.riddenByEntity == null && entity instanceof EntityPlayer) {
         if(entity.dimension == Config.instance().dimensionTormentID && world.rand.nextDouble() >= 0.05D) {
            WorldProviderTorment.setPlayerMustTorment((EntityPlayer)entity, 3, -2);
         } else {
            WorldProviderTorment.setPlayerMustTorment((EntityPlayer)entity, 1, -1);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public int getRenderBlockPass() {
      return 1;
   }

   public int colorMultiplier(IBlockAccess iblockaccess, int x, int y, int z) {
      return 16711714;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
      if(par5Random.nextInt(100) == 0) {
         par1World.playSound((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "portal.portal", 0.5F, par5Random.nextFloat() * 0.4F + 0.8F, false);
      }

      for(int l = 0; l < 2; ++l) {
         double d0 = (double)((float)par2 + par5Random.nextFloat());
         double d1 = (double)((float)par3 + par5Random.nextFloat());
         double d2 = (double)((float)par4 + par5Random.nextFloat());
         par1World.spawnParticle(ParticleEffect.FLAME.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
      }

   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return null;
   }
}
