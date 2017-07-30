package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPlacedItem extends BlockBaseContainer {

   public static void placeItemInWorld(ItemStack stack, EntityPlayer player, World world, int x, int y, int z) {
      byte meta = 0;
      if(player != null) {
         int tile = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
         if(tile == 0) {
            meta = 2;
         }

         if(tile == 1) {
            meta = 5;
         }

         if(tile == 2) {
            meta = 3;
         }

         if(tile == 3) {
            meta = 4;
         }
      }

      world.setBlock(x, y, z, Witchery.Blocks.PLACED_ITEMSTACK, meta, 3);
      TileEntity tile1 = world.getTileEntity(x, y, z);
      if(tile1 != null && tile1 instanceof BlockPlacedItem.TileEntityPlacedItem) {
         ((BlockPlacedItem.TileEntityPlacedItem)tile1).setStack(stack);
      }

   }

   public BlockPlacedItem() {
      super(Material.ground, BlockPlacedItem.TileEntityPlacedItem.class);
      super.registerWithCreateTab = false;
      this.setHardness(0.0F);
      this.setStepSound(Block.soundTypeMetal);
      this.setBlockBounds(0.2F, 0.0F, 0.2F, 0.8F, 0.05F, 0.8F);
   }

   public void registerBlockIcons(IIconRegister p_149651_1_) {}

   protected String getTextureName() {
      return null;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
      if(par6EntityPlayer.capabilities.isCreativeMode) {
         par5 |= 8;
         par1World.setBlockMetadataWithNotify(par2, par3, par4, par5, 4);
      }

      this.dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
      super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      ArrayList drops = new ArrayList();
      if((metadata & 8) == 0) {
         TileEntity tile = world.getTileEntity(x, y, z);
         if(tile != null && tile instanceof BlockPlacedItem.TileEntityPlacedItem && ((BlockPlacedItem.TileEntityPlacedItem)tile).getStack() != null) {
            drops.add(((BlockPlacedItem.TileEntityPlacedItem)tile).getStack());
         }
      }

      return drops;
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
      TileEntity tile = world.getTileEntity(x, y, z);
      return tile != null && tile instanceof BlockPlacedItem.TileEntityPlacedItem && ((BlockPlacedItem.TileEntityPlacedItem)tile).getStack() != null?((BlockPlacedItem.TileEntityPlacedItem)tile).getStack().copy():new ItemStack(Witchery.Items.ARTHANA);
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
   public void randomDisplayTick(World world, int x, int y, int z, Random rand) {}

   @SideOnly(Side.CLIENT)
   public String getItemIconName() {
      return this.getTextureName();
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      return Blocks.iron_block.getBlockTextureFromSide(0);
   }

   public static class TileEntityPlacedItem extends TileEntity {

      private static final String ITEM_KEY = "WITCPlacedItem";
      private ItemStack stack;


      public boolean canUpdate() {
         return false;
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         if(this.stack != null) {
            NBTTagCompound nbtItem = new NBTTagCompound();
            this.stack.writeToNBT(nbtItem);
            nbtRoot.setTag("WITCPlacedItem", nbtItem);
         }

      }

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
         if(nbtRoot.hasKey("WITCPlacedItem")) {
            NBTTagCompound nbtItem = nbtRoot.getCompoundTag("WITCPlacedItem");
            ItemStack stack = ItemStack.loadItemStackFromNBT(nbtItem);
            this.stack = stack;
         }

      }

      public void setStack(ItemStack stack) {
         this.stack = stack;
         if(!super.worldObj.isRemote) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         }

      }

      public ItemStack getStack() {
         return this.stack;
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
   }
}
