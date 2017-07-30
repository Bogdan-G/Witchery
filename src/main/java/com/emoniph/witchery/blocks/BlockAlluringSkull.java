package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.item.ItemAlluringSkull;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAlluringSkull extends BlockBaseContainer {

   private static final int UPDATE_FREQUENCY = 100;


   public BlockAlluringSkull() {
      super(Material.circuits, BlockAlluringSkull.TileEntityAlluringSkull.class, ItemAlluringSkull.class);
      this.setLightLevel(0.5F);
      this.setStepSound(Block.soundTypeStone);
      this.setBlockUnbreakable();
      this.setResistance(1000.0F);
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

   public boolean onBlockActivated(World world, int posX, int posY, int posZ, EntityPlayer player, int par6, float par7, float par8, float par9) {
      if(!world.isRemote) {
         BlockAlluringSkull.TileEntityAlluringSkull tileEntity = (BlockAlluringSkull.TileEntityAlluringSkull)world.getTileEntity(posX, posY, posZ);
         int type = tileEntity.getSkullType();
         ItemStack itemstack = player.getHeldItem();
         if(itemstack != null && Witchery.Items.GENERIC.itemNecroStone.isMatch(itemstack)) {
            if(type == 0) {
               ParticleEffect.FLAME.send(SoundEffect.MOB_HORSE_SKELETON_DEATH, world, 0.5D + (double)posX, 0.3D + (double)posY, 0.5D + (double)posZ, 0.5D, 0.5D, 16);
               tileEntity.setSkullType(type == 0?1:0);
            } else {
               ParticleEffect.EXPLODE.send(SoundEffect.MOB_HORSE_SKELETON_HIT, world, 0.5D + (double)posX, 0.3D + (double)posY, 0.5D + (double)posZ, 0.5D, 0.5D, 16);
               world.setBlockToAir(posX, posY, posZ);
               world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)posX, 0.8D + (double)posY, 0.5D + (double)posZ, new ItemStack(this)));
            }

            return true;
         }
      }

      return super.onBlockActivated(world, posX, posY, posZ, player, par6, par7, par8, par9);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      int l = par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 7;
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

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
      return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
   }

   public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
      int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
      par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return new ItemStack(this);
   }

   public int getDamageValue(World par1World, int par2, int par3, int par4) {
      TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
      return tileentity != null && tileentity instanceof BlockAlluringSkull.TileEntityAlluringSkull?((BlockAlluringSkull.TileEntityAlluringSkull)tileentity).getSkullType():super.getDamageValue(par1World, par2, par3, par4);
   }

   public int damageDropped(int par1) {
      return par1;
   }

   public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
      if(par6EntityPlayer.capabilities.isCreativeMode) {
         par5 |= 8;
         par1World.setBlockMetadataWithNotify(par2, par3, par4, par5, 4);
      }

      this.dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
      super.onBlockHarvested(par1World, par2, par3, par4, par5, par6EntityPlayer);
   }

   public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
      super.breakBlock(par1World, par2, par3, par4, par5, par6);
   }

   public ArrayList getDrops(World world, int x, int y, int z, int metadata, int fortune) {
      ArrayList drops = new ArrayList();
      if((metadata & 8) == 0) {
         ItemStack itemstack = new ItemStack(this);
         BlockAlluringSkull.TileEntityAlluringSkull tileentityskull = (BlockAlluringSkull.TileEntityAlluringSkull)world.getTileEntity(x, y, z);
         if(tileentityskull == null) {
            return drops;
         }

         drops.add(itemstack);
      }

      return drops;
   }

   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
      return Item.getItemFromBlock(this);
   }

   private boolean func_82528_d(World par1World, int par2, int par3, int par4, int par5) {
      if(par1World.getBlock(par2, par3, par4) != this) {
         return false;
      } else {
         TileEntity tileentity = par1World.getTileEntity(par2, par3, par4);
         return tileentity != null && tileentity instanceof BlockAlluringSkull.TileEntityAlluringSkull?((BlockAlluringSkull.TileEntityAlluringSkull)tileentity).getSkullType() == par5:false;
      }
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister par1IconRegister) {}

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      return Blocks.soul_sand.getBlockTextureFromSide(par1);
   }

   @SideOnly(Side.CLIENT)
   public String getItemIconName() {
      return this.getTextureName();
   }

   public static void allure(World world, double posX, double posY, double posZ, int quad) {
      try {
         float e = 64.0F;
         float dy = 10.0F;
         AxisAlignedBB bounds = null;
         switch(quad) {
         case 0:
            bounds = AxisAlignedBB.getBoundingBox(posX, posY - 10.0D, posZ - 64.0D, posX + 64.0D, posY, posZ);
            break;
         case 1:
            bounds = AxisAlignedBB.getBoundingBox(posX - 64.0D, posY - 10.0D, posZ - 64.0D, posX, posY, posZ);
            break;
         case 2:
            bounds = AxisAlignedBB.getBoundingBox(posX, posY - 10.0D, posZ, posX + 64.0D, posY, posZ + 64.0D);
            break;
         case 3:
            bounds = AxisAlignedBB.getBoundingBox(posX - 64.0D, posY - 10.0D, posZ, posX, posY, posZ + 64.0D);
            break;
         case 4:
            bounds = AxisAlignedBB.getBoundingBox(posX - 64.0D, posY + 1.0D, posZ - 64.0D, posX, posY + 10.0D, posZ);
            break;
         case 5:
            bounds = AxisAlignedBB.getBoundingBox(posX, posY + 1.0D, posZ, posX + 64.0D, posY + 10.0D, posZ + 64.0D);
            break;
         case 6:
            bounds = AxisAlignedBB.getBoundingBox(posX - 64.0D, posY + 1.0D, posZ, posX, posY + 10.0D, posZ + 64.0D);
            break;
         case 7:
         default:
            bounds = AxisAlignedBB.getBoundingBox(posX, posY + 1.0D, posZ - 64.0D, posX + 64.0D, posY + 10.0D, posZ);
         }

         Iterator i$ = world.getEntitiesWithinAABB(EntityCreature.class, bounds).iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            EntityCreature creature = (EntityCreature)obj;
            if(creature.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD && !creature.getNavigator().tryMoveToXYZ(posX, posY, posZ, 1.0D)) {
               int x = MathHelper.floor_double(posX);
               int y = MathHelper.floor_double(posY);
               int z = MathHelper.floor_double(posZ);
               PathEntity path = world.getEntityPathToXYZ(creature, x, y, z, 10.0F, true, false, false, true);
               if(path != null) {
                  creature.setPathToEntity(path);
               }
            }
         }
      } catch (Exception var18) {
         Log.instance().debug(String.format("Exception occurred alluring with a skull! %s", new Object[]{var18.toString()}));
      }

   }

   public static class TileEntityAlluringSkull extends TileEntityBase {

      private int skullType;
      private int skullRotation;
      private int quad = 0;


      public void updateEntity() {
         super.updateEntity();
         if(!super.worldObj.isRemote && this.skullType == 1 && super.ticks % 100L == 0L) {
            if(++this.quad >= 8) {
               this.quad = 0;
            }

            BlockAlluringSkull.allure(super.worldObj, (double)super.xCoord, (double)super.yCoord, (double)super.zCoord, this.quad);
         }

      }

      public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
         super.writeToNBT(par1NBTTagCompound);
         par1NBTTagCompound.setByte("SkullType", (byte)(this.skullType & 255));
         par1NBTTagCompound.setByte("Rot", (byte)(this.skullRotation & 255));
      }

      public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
         super.readFromNBT(par1NBTTagCompound);
         this.skullType = par1NBTTagCompound.getByte("SkullType");
         this.skullRotation = par1NBTTagCompound.getByte("Rot");
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

      public void setSkullType(int par1) {
         this.skullType = par1;
         super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
      }

      public int getSkullType() {
         return this.skullType;
      }

      public void setSkullRotation(int par1) {
         this.skullRotation = par1;
      }

      @SideOnly(Side.CLIENT)
      public int func_82119_b() {
         return this.skullRotation;
      }
   }
}
