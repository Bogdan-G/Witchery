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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChalice extends BlockBaseContainer {

   public BlockChalice() {
      super(Material.anvil, BlockChalice.TileEntityChalice.class);
      super.registerWithCreateTab = false;
      this.setHardness(3.0F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.3F, 0.0F, 0.37F, 0.63F, 0.46F, 0.695F);
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

   public int damageDropped(int metadata) {
      return metadata == 1?Witchery.Items.GENERIC.itemChaliceFull.damageValue:Witchery.Items.GENERIC.itemChaliceEmpty.damageValue;
   }

   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5) {
      this.func_111046_k(par1World, par2, par3, par4);
   }

   private boolean func_111046_k(World par1World, int par2, int par3, int par4) {
      if(!this.canBlockStay(par1World, par2, par3, par4)) {
         this.dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
         par1World.setBlockToAir(par2, par3, par4);
         return false;
      } else {
         return true;
      }
   }

   public boolean canBlockStay(World world, int x, int y, int z) {
      Material material = world.getBlock(x, y - 1, z).getMaterial();
      return !world.isAirBlock(x, y - 1, z) && material != null && material.isOpaque() && material.isSolid();
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      return tileEntity != null && tileEntity instanceof BlockChalice.TileEntityChalice && ((BlockChalice.TileEntityChalice)tileEntity).isFilled()?Witchery.Items.GENERIC.itemChaliceFull.createStack():Witchery.Items.GENERIC.itemChaliceEmpty.createStack();
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
         world.spawnParticle(ParticleEffect.REDDUST.toString(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
      }

   }

   public static class TileEntityChalice extends TileEntity {

      private boolean filled;
      private boolean checkState;
      private static final String FILLED_KEY = "WITCFilled";


      public boolean isFilled() {
         return this.filled;
      }

      public void updateEntity() {
         if(!this.checkState) {
            this.checkState = true;
            if(this.filled && !super.worldObj.isRemote && super.worldObj.getBlock(super.xCoord, super.yCoord, super.zCoord) == Witchery.Blocks.CHALICE) {
               super.worldObj.setBlockMetadataWithNotify(super.xCoord, super.yCoord, super.zCoord, 1, 3);
            }
         }

         super.updateEntity();
      }

      public void writeToNBT(NBTTagCompound nbtTag) {
         super.writeToNBT(nbtTag);
         nbtTag.setBoolean("WITCFilled", this.filled);
      }

      public void readFromNBT(NBTTagCompound nbtTag) {
         super.readFromNBT(nbtTag);
         if(nbtTag.hasKey("WITCFilled")) {
            this.filled = nbtTag.getBoolean("WITCFilled");
         }

      }

      public Packet getDescriptionPacket() {
         NBTTagCompound nbtTag = new NBTTagCompound();
         this.writeToNBT(nbtTag);
         return new S35PacketUpdateTileEntity(super.xCoord, super.yCoord, super.zCoord, 1, nbtTag);
      }

      public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
         super.onDataPacket(net, packet);
         this.readFromNBT(packet.func_148857_g());
         super.worldObj.func_147479_m(super.xCoord, super.yCoord, super.zCoord);
      }

      public void setFilled(boolean filled) {
         if(!super.worldObj.isRemote) {
            this.filled = filled;
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
            if(super.worldObj.getBlock(super.xCoord, super.yCoord, super.zCoord) == Witchery.Blocks.CHALICE) {
               super.worldObj.setBlockMetadataWithNotify(super.xCoord, super.yCoord, super.zCoord, filled?1:0, 3);
            }
         }

      }
   }
}
