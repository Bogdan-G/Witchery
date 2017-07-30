package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.util.ChatUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockBloodRose extends BlockBaseContainer implements IPlantable {

   private final float RADIUS = 0.2F;
   @SideOnly(Side.CLIENT)
   private IIcon fullIcon;


   public BlockBloodRose() {
      super(Material.plants, BlockBloodRose.TileEntityBloodRose.class);
      this.setHardness(0.0F);
      this.setStepSound(Block.soundTypeGrass);
      this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.6F, 0.7F);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderType() {
      return 1;
   }

   public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
      return EnumPlantType.Plains;
   }

   public Block getPlant(IBlockAccess world, int x, int y, int z) {
      return this;
   }

   public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
      return world.getBlockMetadata(x, y, z);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return null;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      float f = 0.0625F;
      return AxisAlignedBB.getBoundingBox((double)((float)par2 + 0.5F - 0.2F + 0.0625F), (double)par3, (double)((float)par4 + 0.5F - 0.2F + 0.0625F), (double)((float)par2 + 0.5F + 0.2F - 0.0625F), (double)((float)par3 + 0.6F - 0.0625F), (double)((float)par4 + 0.5F + 0.2F - 0.0625F));
   }

   public void onEntityCollidedWithBlock(World world, int posX, int posY, int posZ, Entity entity) {
      if(!world.isRemote && entity instanceof EntityPlayer) {
         TileEntity tileentity = world.getTileEntity(posX, posY, posZ);
         if(tileentity != null && tileentity instanceof BlockBloodRose.TileEntityBloodRose) {
            BlockBloodRose.TileEntityBloodRose chest = (BlockBloodRose.TileEntityBloodRose)tileentity;
            chest.storePlayer((EntityPlayer)entity);
         }
      }

   }

   public int damageDropped(int par1) {
      return 0;
   }

   public Item getItemDropped(int par1, Random rand, int fortune) {
      return null;
   }

   public TileEntity createNewTileEntity(World world, int metadata) {
      BlockBloodRose.TileEntityBloodRose tileentitychest = new BlockBloodRose.TileEntityBloodRose();
      return tileentitychest;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister par1IconRegister) {
      super.blockIcon = par1IconRegister.registerIcon(this.getTextureName());
      this.fullIcon = par1IconRegister.registerIcon(this.getTextureName() + "_full");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return meta == 0?super.getIcon(side, meta):this.fullIcon;
   }

   public static class TileEntityBloodRose extends TileEntity {

      private String customName;
      public ArrayList players = new ArrayList();


      public boolean canUpdate() {
         return false;
      }

      public void sync() {
         super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
      }

      public void storePlayer(EntityPlayer player) {
         if(!super.worldObj.isRemote && player != null) {
            if(this.players.size() == 0) {
               this.players.add(player.getCommandSenderName());
            } else {
               this.players.set(0, player.getCommandSenderName());
            }

            if(this.getBlockMetadata() != 1) {
               super.worldObj.setBlockMetadataWithNotify(super.xCoord, super.yCoord, super.zCoord, 1, 3);
               this.sync();
            }
         }

      }

      public String popUserExcept(EntityPlayer usingPlayer, boolean excludeUser) {
         String missingPlayers = "";

         for(int i = this.players.size() - 1; i >= 0; --i) {
            String foundPlayerName = (String)this.players.get(i);
            if(excludeUser && foundPlayerName.equals(usingPlayer.getCommandSenderName())) {
               if(this.players.size() == 1) {
                  ChatUtil.sendTranslated(EnumChatFormatting.RED, usingPlayer, "tile.witcheryLeechChest.onlyowntaglock", new Object[0]);
                  return null;
               }
            } else {
               if(super.worldObj.getPlayerEntityByName(foundPlayerName) != null) {
                  this.players.remove(i);
                  this.sync();
                  if(this.players.size() == 0) {
                     super.worldObj.setBlockMetadataWithNotify(super.xCoord, super.yCoord, super.zCoord, 0, 3);
                  }

                  return foundPlayerName;
               }

               missingPlayers = missingPlayers + foundPlayerName + " ";
            }
         }

         if(!missingPlayers.isEmpty()) {
            ChatUtil.sendTranslated(EnumChatFormatting.RED, usingPlayer, "tile.witcheryLeechChest.playernotloggedin", new Object[]{missingPlayers});
         }

         return null;
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

      public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
         super.readFromNBT(par1NBTTagCompound);
         this.players.clear();
         NBTTagList nbtPlayersList = par1NBTTagCompound.getTagList("WITCPlayers", 10);

         for(int i = 0; i < nbtPlayersList.tagCount(); ++i) {
            NBTTagCompound nbtPlayer = nbtPlayersList.getCompoundTagAt(i);
            String s = nbtPlayer.getString("Player");
            if(s != null && !s.isEmpty()) {
               this.players.add(s);
            }
         }

      }

      public void writeToNBT(NBTTagCompound nbtTag) {
         super.writeToNBT(nbtTag);
         new NBTTagList();
         NBTTagList nbtPlayers = new NBTTagList();

         for(int i = 0; i < this.players.size(); ++i) {
            NBTTagCompound nbtPlayer = new NBTTagCompound();
            nbtPlayer.setString("Player", (String)this.players.get(i));
            nbtPlayers.appendTag(nbtPlayer);
         }

         nbtTag.setTag("WITCPlayers", nbtPlayers);
      }
   }
}
