package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.util.ParticleEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCandelabra extends BlockBaseContainer {

   public BlockCandelabra() {
      super(Material.anvil, BlockCandelabra.TileEntityCandelabra.class);
      super.registerWithCreateTab = false;
      this.setLightLevel(1.0F);
      this.setHardness(2.0F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int quantityDropped(Random rand) {
      return 1;
   }

   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
      return Witchery.Items.GENERIC;
   }

   public int damageDropped(int par1) {
      return Witchery.Items.GENERIC.itemCandelabra.damageValue;
   }

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
      this.func_111046_k(par1World, par2, par3, par4);
   }

   private boolean func_111046_k(World par1World, int par2, int par3, int par4) {
      if(!this.canBlockStay(par1World, par2, par3, par4)) {
         if(!par1World.isRemote) {
            this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
         }

         return false;
      } else {
         return true;
      }
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return Witchery.Items.GENERIC.itemCandelabra.createStack();
   }

   public boolean canBlockStay(World world, int x, int y, int z) {
      Material material = world.getBlock(x, y - 1, z).getMaterial();
      return !world.isAirBlock(x, y - 1, z) && material != null && material.isOpaque() && material.isSolid();
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      double yMid = (double)y + 1.05D;
      double yOuter = (double)y + 0.9D;
      double mid = 0.5D;
      double near = 0.2D;
      double far = 0.8D;
      if(rand.nextInt(4) != 0) {
         world.spawnParticle(ParticleEffect.FLAME.toString(), (double)x + 0.5D, yMid, (double)z + 0.5D, 0.0D, 0.0D, 0.0D);
         world.spawnParticle(ParticleEffect.SMOKE.toString(), (double)x + 0.5D, yMid, (double)z + 0.5D, 0.0D, 0.0D, 0.0D);
      }

      if(rand.nextInt(4) != 0) {
         world.spawnParticle(ParticleEffect.FLAME.toString(), (double)x + 0.8D, yOuter, (double)z + 0.5D, 0.0D, 0.0D, 0.0D);
         world.spawnParticle(ParticleEffect.SMOKE.toString(), (double)x + 0.8D, yOuter, (double)z + 0.5D, 0.0D, 0.0D, 0.0D);
      }

      if(rand.nextInt(4) != 0) {
         world.spawnParticle(ParticleEffect.FLAME.toString(), (double)x + 0.2D, yOuter, (double)z + 0.5D, 0.0D, 0.0D, 0.0D);
         world.spawnParticle(ParticleEffect.SMOKE.toString(), (double)x + 0.2D, yOuter, (double)z + 0.5D, 0.0D, 0.0D, 0.0D);
      }

      if(rand.nextInt(4) != 0) {
         world.spawnParticle(ParticleEffect.FLAME.toString(), (double)x + 0.5D, yOuter, (double)z + 0.8D, 0.0D, 0.0D, 0.0D);
         world.spawnParticle(ParticleEffect.SMOKE.toString(), (double)x + 0.5D, yOuter, (double)z + 0.8D, 0.0D, 0.0D, 0.0D);
      }

      if(rand.nextInt(4) != 0) {
         world.spawnParticle(ParticleEffect.FLAME.toString(), (double)x + 0.5D, yOuter, (double)z + 0.2D, 0.0D, 0.0D, 0.0D);
         world.spawnParticle(ParticleEffect.SMOKE.toString(), (double)x + 0.5D, yOuter, (double)z + 0.2D, 0.0D, 0.0D, 0.0D);
      }

   }

   public static class TileEntityCandelabra extends TileEntity {

      public boolean canUpdate() {
         return false;
      }
   }
}
