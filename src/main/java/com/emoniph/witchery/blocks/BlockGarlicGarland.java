package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityVampire;
import com.emoniph.witchery.ritual.rites.RiteProtectionCircleRepulsive;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGarlicGarland extends BlockBaseContainer {

   public BlockGarlicGarland() {
      super(Material.circuits, BlockGarlicGarland.TileEntityGarlicGarland.class);
      super.registerWithCreateTab = true;
      this.setHardness(0.2F);
   }

   public int getRenderType() {
      return -1;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean canBlockStay(World world, int x, int y, int z) {
      return super.canBlockStay(world, x, y, z);
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
      return false;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
      int side = world.getBlockMetadata(x, y, z);
      float minY = 0.8F;
      float maxY = 1.0F;
      float minX = 0.0F;
      float maxX = 0.15F;
      float minZ = 0.1F;
      float maxZ = 0.9F;
      if(side == 2) {
         this.setBlockBounds(0.1F, 0.8F, 1.0F, 0.9F, 1.0F, 0.85F);
      } else if(side == 3) {
         this.setBlockBounds(0.100000024F, 0.8F, 0.0F, 0.9F, 1.0F, 0.15F);
      } else if(side == 4) {
         this.setBlockBounds(1.0F, 0.8F, 0.1F, 0.85F, 1.0F, 0.9F);
      } else if(side == 5) {
         this.setBlockBounds(0.0F, (float)y + 0.8F, 0.1F, 0.15F, 1.0F, 0.9F);
      } else {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote && entity instanceof EntityVampire) {
         RiteProtectionCircleRepulsive.push(world, entity, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z);
      } else if(world.isRemote && entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         if(!player.capabilities.isCreativeMode && ExtendedPlayer.get((EntityPlayer)entity).isVampire()) {
            RiteProtectionCircleRepulsive.push(world, entity, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z, false);
         }
      }

   }

   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
      return super.getSelectedBoundingBoxFromPool(world, x, y, z);
   }

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
      int facing = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
      if(facing == 0) {
         world.setBlockMetadataWithNotify(x, y, z, 2, 2);
      } else if(facing == 1) {
         world.setBlockMetadataWithNotify(x, y, z, 5, 2);
      } else if(facing == 2) {
         world.setBlockMetadataWithNotify(x, y, z, 3, 2);
      } else if(facing == 3) {
         world.setBlockMetadataWithNotify(x, y, z, 4, 2);
      }

   }

   public static class TileEntityGarlicGarland extends TileEntity {

      public boolean canUpdate() {
         return false;
      }
   }
}
