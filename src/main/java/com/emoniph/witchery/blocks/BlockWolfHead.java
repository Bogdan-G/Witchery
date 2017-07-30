package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.item.ItemWolfHead;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWolfHead extends BlockBaseContainer {

   public BlockWolfHead() {
      super(Material.circuits, BlockWolfHead.TileEntityWolfHead.class, ItemWolfHead.class);
      this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
   }

   public int getRenderType() {
      return -1;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {
      int l = p_149719_1_.getBlockMetadata(p_149719_2_, p_149719_3_, p_149719_4_) & 7;
      switch(l) {
      case 1:
      default:
         this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
         break;
      case 2:
         this.setBlockBounds(0.25F, 0.25F, 0.5F, 0.75F, 0.75F, 1.0F);
         break;
      case 3:
         this.setBlockBounds(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 0.5F);
         break;
      case 4:
         this.setBlockBounds(0.5F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
         break;
      case 5:
         this.setBlockBounds(0.0F, 0.25F, 0.25F, 0.5F, 0.75F, 0.75F);
      }

   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      this.setBlockBoundsBasedOnState(world, x, y, z);
      return super.getCollisionBoundingBoxFromPool(world, x, y, z);
   }

   public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
      int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
      world.setBlockMetadataWithNotify(x, y, z, l, 2);
   }

   @SideOnly(Side.CLIENT)
   public Item getItem(World world, int x, int y, int z) {
      return Item.getItemFromBlock(this);
   }

   public int getDamageValue(World world, int x, int y, int z) {
      TileEntity tileentity = world.getTileEntity(x, y, z);
      return tileentity != null && tileentity instanceof BlockWolfHead.TileEntityWolfHead?((BlockWolfHead.TileEntityWolfHead)tileentity).getSkullType():super.getDamageValue(world, x, y, z);
   }

   public void getSubBlocks(Item p_149666_1_, CreativeTabs p_149666_2_, List p_149666_3_) {
      super.getSubBlocks(p_149666_1_, p_149666_2_, p_149666_3_);
   }

   public int damageDropped(int blockMetadata) {
      return blockMetadata;
   }

   public void onBlockHarvested(World world, int x, int y, int z, int metadata, EntityPlayer player) {
      if(player.capabilities.isCreativeMode) {
         metadata |= 8;
         world.setBlockMetadataWithNotify(x, y, z, metadata, 4);
      }

      this.dropBlockAsItem(world, x, y, z, metadata, 0);
      super.onBlockHarvested(world, x, y, z, metadata, player);
   }

   public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
      super.breakBlock(world, x, y, z, block, metadata);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      ArrayList ret = new ArrayList();
      if((metadata & 8) == 0) {
         ItemStack itemstack = new ItemStack(this, 1, this.getDamageValue(world, x, y, z));
         BlockWolfHead.TileEntityWolfHead head = (BlockWolfHead.TileEntityWolfHead)BlockUtil.getTileEntity(world, x, y, z, BlockWolfHead.TileEntityWolfHead.class);
         if(head == null) {
            return ret;
         }

         ret.add(itemstack);
      }

      return ret;
   }

   public Item getItemDropped(int p_149650_1_, Random rand, int p_149650_3_) {
      return Item.getItemFromBlock(this);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {}

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
      return Blocks.soul_sand.getBlockTextureFromSide(p_149691_1_);
   }

   @SideOnly(Side.CLIENT)
   public String getItemIconName() {
      return this.getTextureName() + "_" + ItemWolfHead.field_94587_a[0];
   }

   public static class TileEntityWolfHead extends TileEntity {

      private int skullType;
      private int rotation;


      public boolean canUpdate() {
         return false;
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         nbtRoot.setByte("SkullType", (byte)(this.skullType & 255));
         nbtRoot.setByte("Rot", (byte)(this.rotation & 255));
      }

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
         this.skullType = nbtRoot.getByte("SkullType");
         this.rotation = nbtRoot.getByte("Rot");
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

      public void setSkullType(int skullType) {
         this.skullType = skullType;
         super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
      }

      public int getSkullType() {
         return this.skullType;
      }

      public void setRotation(int p_145903_1_) {
         this.rotation = p_145903_1_;
         super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
      }

      @SideOnly(Side.CLIENT)
      public int getRotation() {
         return this.rotation;
      }
   }
}
