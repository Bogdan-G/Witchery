package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.Config;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDreamCatcher extends BlockBaseContainer {

   public BlockDreamCatcher() {
      super(Material.vine, BlockDreamCatcher.TileEntityDreamCatcher.class);
      super.registerWithCreateTab = false;
      this.disableStats();
      this.setHardness(1.0F);
      this.setStepSound(Block.soundTypeWood);
      float f = 0.25F;
      float f1 = 1.0F;
      this.setBlockBounds(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int par1, int par2) {
      return Blocks.planks.getBlockTextureFromSide(par1);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return null;
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
      return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
   }

   public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int posX, int posY, int posZ) {
      int side = par1IBlockAccess.getBlockMetadata(posX, posY, posZ);
      float bottom = 0.28125F;
      float top = 0.78125F;
      float left = 0.0F;
      float width = 1.0F;
      float depth = 0.125F;
      this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      float minY = 0.0F;
      float maxY = 0.87F;
      float minX = 0.0F;
      float maxX = 0.08F;
      float minZ = 0.25F;
      float maxZ = 0.75F;
      if(side == 2) {
         this.setBlockBounds(minZ, minY, 1.0F - minX, maxZ, maxY, 1.0F - maxX);
      } else if(side == 3) {
         this.setBlockBounds(1.0F - maxZ, minY, minX, 1.0F - minZ, maxY, maxX);
      } else if(side == 4) {
         this.setBlockBounds(1.0F - minX, minY, minZ, 1.0F - maxX, maxY, maxZ);
      } else if(side == 5) {
         this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
      }

   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean getBlocksMovement(IBlockAccess par1IBlockAccess, int par2, int par3, int par4) {
      return true;
   }

   public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
      return false;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public void breakBlock(World world, int posX, int posY, int posZ, Block par5, int par6) {
      if(!world.isRemote) {
         TileEntity tileEntity = world.getTileEntity(posX, posY, posZ);
         if(tileEntity != null && tileEntity instanceof BlockDreamCatcher.TileEntityDreamCatcher) {
            BlockDreamCatcher.TileEntityDreamCatcher tileEntityDreamCatcher = (BlockDreamCatcher.TileEntityDreamCatcher)tileEntity;
            ItemGeneral.DreamWeave weave = tileEntityDreamCatcher.getWeave();
            if(weave != null) {
               world.spawnEntityInWorld(new EntityItem(world, (double)posX, (double)posY, (double)posZ, weave.createStack()));
            }
         }
      }

      super.breakBlock(world, posX, posY, posZ, par5, par6);
   }

   public ArrayList getDrops(World world, int posX, int posY, int posZ, int metadata, int fortune) {
      ArrayList ret = new ArrayList();
      return ret;
   }

   public void onNeighborBlockChange(World world, int posX, int posY, int posZ, Block par5) {
      int metadata = world.getBlockMetadata(posX, posY, posZ);
      boolean flag = true;
      if(metadata == 2 && world.getBlock(posX, posY, posZ + 1).getMaterial().isSolid()) {
         flag = false;
      }

      if(metadata == 3 && world.getBlock(posX, posY, posZ - 1).getMaterial().isSolid()) {
         flag = false;
      }

      if(metadata == 4 && world.getBlock(posX + 1, posY, posZ).getMaterial().isSolid()) {
         flag = false;
      }

      if(metadata == 5 && world.getBlock(posX - 1, posY, posZ).getMaterial().isSolid()) {
         flag = false;
      }

      if(flag) {
         this.dropBlockAsItem(world, posX, posY, posZ, world.getBlockMetadata(posX, posY, posZ), 0);
         world.setBlockToAir(posX, posY, posZ);
      }

      super.onNeighborBlockChange(world, posX, posY, posZ, par5);
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {}

   public static boolean causesNightmares(World world, int posX, int posY, int posZ) {
      TileEntity tileEntity = world.getTileEntity(posX, posY, posZ);
      if(tileEntity != null && tileEntity instanceof BlockDreamCatcher.TileEntityDreamCatcher) {
         BlockDreamCatcher.TileEntityDreamCatcher tileEntityDreamCatcher = (BlockDreamCatcher.TileEntityDreamCatcher)tileEntity;
         return tileEntityDreamCatcher.dreamWeave == Witchery.Items.GENERIC.itemDreamNightmare;
      } else {
         return false;
      }
   }

   public static boolean enhancesDreams(World world, int x, int y, int z) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if(tileEntity != null && tileEntity instanceof BlockDreamCatcher.TileEntityDreamCatcher) {
         BlockDreamCatcher.TileEntityDreamCatcher tileEntityDreamCatcher = (BlockDreamCatcher.TileEntityDreamCatcher)tileEntity;
         return tileEntityDreamCatcher.dreamWeave == Witchery.Items.GENERIC.itemDreamIntensity;
      } else {
         return false;
      }
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      TileEntity tileEntity = world.getTileEntity(x, y, z);
      if(tileEntity != null && tileEntity instanceof BlockDreamCatcher.TileEntityDreamCatcher) {
         BlockDreamCatcher.TileEntityDreamCatcher catcherEntity = (BlockDreamCatcher.TileEntityDreamCatcher)tileEntity;
         if(catcherEntity.getWeave() != null) {
            return catcherEntity.getWeave().createStack();
         }
      }

      return Witchery.Items.GENERIC.itemDreamMove.createStack();
   }

   public static class TileEntityDreamCatcher extends TileEntity {

      private boolean buffIfDay;
      private boolean buffIfNight;
      private ItemGeneral.DreamWeave dreamWeave;
      private static final String DREAM_WEAVE_KEY = "WITCWeaveID";


      public void setEffect(ItemGeneral.DreamWeave dreamWeave) {
         this.dreamWeave = dreamWeave;
         if(!super.worldObj.isRemote) {
            super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
         }

      }

      public ItemGeneral.DreamWeave getWeave() {
         return this.dreamWeave;
      }

      public void updateEntity() {
         super.updateEntity();
         if(!super.worldObj.isRemote && this.dreamWeave != null) {
            if(this.buffIfDay || this.buffIfNight) {
               boolean day = super.worldObj.isDaytime();
               if(this.buffIfDay && day || this.buffIfNight && !day) {
                  boolean isDream = true;
                  boolean isEnhanced = false;
                  boolean r = true;
                  boolean done = false;

                  for(int bounds = super.yCoord - 5; bounds <= super.yCoord + 5 && !done; ++bounds) {
                     for(int list = super.xCoord - 5; list <= super.xCoord + 5 && !done; ++list) {
                        for(int i$ = super.zCoord - 5; i$ <= super.zCoord + 5 && !done; ++i$) {
                           if((bounds != super.yCoord || list != super.xCoord || i$ != super.zCoord) && super.worldObj.getBlock(list, bounds, i$) == Witchery.Blocks.DREAM_CATCHER) {
                              if(BlockDreamCatcher.causesNightmares(super.worldObj, list, bounds, i$)) {
                                 isDream = false;
                                 done = isEnhanced;
                              } else if(BlockDreamCatcher.enhancesDreams(super.worldObj, list, bounds, i$)) {
                                 isEnhanced = true;
                                 done = !isDream;
                              }
                           }
                        }
                     }
                  }

                  AxisAlignedBB var11 = AxisAlignedBB.getBoundingBox((double)(super.xCoord - 5), (double)(super.yCoord - 5), (double)(super.zCoord - 5), (double)(super.xCoord + 5), (double)(super.yCoord + 5), (double)(super.zCoord + 5));
                  List var12 = super.worldObj.getEntitiesWithinAABB(EntityPlayer.class, var11);
                  Iterator var13 = var12.iterator();

                  while(var13.hasNext()) {
                     EntityPlayer player = (EntityPlayer)var13.next();
                     ExtendedPlayer playerEx = ExtendedPlayer.get(player);
                     if(day && !playerEx.isVampire() || !day && playerEx.isVampire()) {
                        this.dreamWeave.applyEffect(player, isDream, isEnhanced);
                     }
                  }
               }

               this.buffIfDay = this.buffIfNight = false;
            }

            if(!this.buffIfDay && !this.buffIfNight && this.areAllPlayersAsleep(super.worldObj)) {
               this.buffIfDay = !super.worldObj.provider.isDaytime();
               this.buffIfNight = !this.buffIfDay;
            }
         }

      }

      private boolean areAllPlayersAsleep(World world) {
         Iterator iterator = world.playerEntities.iterator();
         int sleepThreshold = MathHelper.floor_float(0.01F * (float)Config.instance().percentageOfPlayersSleepingForBuff * (float)world.playerEntities.size());

         while(iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer)iterator.next();
            if(entityplayer.isPlayerSleeping()) {
               --sleepThreshold;
               if(sleepThreshold <= 0) {
                  return true;
               }
            }
         }

         return false;
      }

      public void writeToNBT(NBTTagCompound nbtTag) {
         super.writeToNBT(nbtTag);
         if(this.dreamWeave != null) {
            nbtTag.setInteger("WITCWeaveID", this.dreamWeave.weaveID);
         }

      }

      public void readFromNBT(NBTTagCompound nbtTag) {
         super.readFromNBT(nbtTag);
         if(nbtTag.hasKey("WITCWeaveID")) {
            int dreamWeaveID = nbtTag.getInteger("WITCWeaveID");
            this.dreamWeave = (ItemGeneral.DreamWeave)Witchery.Items.GENERIC.weaves.get(dreamWeaveID);
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
   }
}
