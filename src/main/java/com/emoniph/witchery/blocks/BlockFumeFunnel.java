package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.BlockWitchesOven;
import com.emoniph.witchery.util.ParticleEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFumeFunnel extends BlockBaseContainer {

   private final boolean filtered;


   public BlockFumeFunnel(boolean filtered) {
      super(Material.iron, BlockFumeFunnel.TileEntityFumeFunnel.class);
      super.registerTileEntity = !filtered;
      this.filtered = filtered;
      this.setHardness(3.5F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
   }

   public boolean isFiltered() {
      return this.filtered;
   }

   public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int par6, float par7, float par8, float par9) {
      if(world.isRemote) {
         return true;
      } else {
         int meta = world.getBlockMetadata(posX, posY, posZ);
         switch(meta) {
         case 2:
         case 3:
            if(BlockWitchesOven.isOven(world.getBlock(posX + 1, posY, posZ))) {
               ++posX;
            } else if(BlockWitchesOven.isOven(world.getBlock(posX - 1, posY, posZ))) {
               --posX;
            } else if(BlockWitchesOven.isOven(world.getBlock(posX, posY - 1, posZ))) {
               --posY;
            }
            break;
         case 4:
         case 5:
            if(BlockWitchesOven.isOven(world.getBlock(posX, posY, posZ + 1))) {
               ++posZ;
            } else if(BlockWitchesOven.isOven(world.getBlock(posX, posY, posZ - 1))) {
               --posZ;
            } else if(BlockWitchesOven.isOven(world.getBlock(posX, posY - 1, posZ))) {
               --posY;
            }
         }

         if(world.getTileEntity(posX, posY, posZ) instanceof BlockWitchesOven.TileEntityWitchesOven) {
            player.openGui(Witchery.instance, 2, world, posX, posY, posZ);
         }

         return true;
      }
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

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
      int metadata = world.getBlockMetadata(x, y, z);
      if(metadata == 1) {
         double d0 = (double)((float)x + 0.45F);
         double d1 = (double)((float)y + 0.4F);
         double d2 = (double)((float)z + 0.5F);
         world.spawnParticle(ParticleEffect.SMOKE.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
      }

   }

   public static class TileEntityFumeFunnel extends TileEntity {

      public boolean canUpdate() {
         return false;
      }
   }
}
