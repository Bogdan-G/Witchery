package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.util.ParticleEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDemonHeart extends BlockBaseContainer {

   public BlockDemonHeart() {
      super(Material.ground, BlockDemonHeart.TileEntityDemonHeart.class);
      super.registerWithCreateTab = false;
      this.setLightLevel(0.2F);
      this.setHardness(1.0F);
      this.setStepSound(Block.soundTypeGravel);
      this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.8F, 0.75F);
   }

   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
      int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      if(l == 0) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
      }

      if(l == 1) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
      }

      if(l == 2) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
      }

      if(l == 3) {
         par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
      }

   }

   public TileEntity createNewTileEntity(World world, int metadata) {
      return new BlockDemonHeart.TileEntityDemonHeart();
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

   public Item getItemDropped(int par1, Random rand, int fortune) {
      return Witchery.Items.GENERIC;
   }

   public int damageDropped(int par1) {
      return Witchery.Items.GENERIC.itemDemonHeart.damageValue;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return Witchery.Items.GENERIC.itemDemonHeart.createStack();
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      double yMid = (double)y + 0.8D;
      double mid1 = 0.35D + 0.3D * rand.nextDouble();
      double mid2 = 0.35D + 0.3D * rand.nextDouble();
      if(rand.nextInt(10) == 0) {
         world.spawnParticle(ParticleEffect.FLAME.toString(), (double)x + mid1, yMid, (double)z + mid2, 0.0D, 0.0D, 0.0D);
         world.spawnParticle(ParticleEffect.SMOKE.toString(), (double)x + mid1, yMid, (double)z + mid2, 0.0D, 0.0D, 0.0D);
      }

   }

   public static class TileEntityDemonHeart extends TileEntityBase {

      public long totalTicks() {
         return super.ticks;
      }

      public void updateEntity() {
         super.updateEntity();
         if(super.worldObj.isRemote && super.ticks % 25L == 0L) {
            super.worldObj.playSound(0.5D + (double)super.xCoord, 0.5D + (double)super.yCoord, 0.5D + (double)super.zCoord, "witchery:random.heartbeat", 0.8F, 1.0F, false);
         }

      }
   }
}
