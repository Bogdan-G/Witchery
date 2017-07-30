package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockCoffin extends BlockBaseContainer {

   private static final int[][] DIRECTIONS = new int[][]{{0, 1}, {-1, 0}, {0, -1}, {1, 0}};


   public BlockCoffin() {
      super(Material.wood, BlockCoffin.TileEntityCoffin.class);
      super.registerWithCreateTab = false;
      this.setHardness(1.0F);
      this.disableStats();
      this.setupBounds();
   }

   public static int getDirection(int meta) {
      return meta & 3;
   }

   public boolean isBed(IBlockAccess world, int x, int y, int z, EntityLivingBase player) {
      return true;
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return true;
      } else {
         int i1 = world.getBlockMetadata(x, y, z);
         int origX = x;
         int origZ = z;
         int tile;
         if(!isBlockHeadOfBed(i1)) {
            tile = getDirection(i1);
            x += DIRECTIONS[tile][0];
            z += DIRECTIONS[tile][1];
            if(world.getBlock(x, y, z) != this) {
               return true;
            }

            i1 = world.getBlockMetadata(x, y, z);
         } else {
            tile = getDirection(i1);
            origX = x - DIRECTIONS[tile][0];
            origZ = z - DIRECTIONS[tile][1];
         }

         Iterator iterator;
         ChunkCoordinates chunkcoordinates;
         BlockCoffin.TileEntityCoffin tile1;
         EntityPlayer d22;
         EntityPlayer d01;
         if(player.isSneaking()) {
            tile1 = (BlockCoffin.TileEntityCoffin)BlockUtil.getTileEntity(world, x, y, z, BlockCoffin.TileEntityCoffin.class);
            if(tile1 != null) {
               if(tile1.open && isBedOccupied(i1)) {
                  d22 = null;
                  iterator = world.playerEntities.iterator();

                  while(iterator.hasNext()) {
                     d01 = (EntityPlayer)iterator.next();
                     if(d01.isPlayerSleeping()) {
                        chunkcoordinates = d01.playerLocation;
                        if(chunkcoordinates.posX == x && chunkcoordinates.posY == y && chunkcoordinates.posZ == z) {
                           d22 = d01;
                        }
                     }
                  }

                  if(d22 != null) {
                     return true;
                  }
               }

               if(world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN) || world.isSideSolid(origX, y + 1, origZ, ForgeDirection.DOWN)) {
                  return true;
               }

               tile1.open = !tile1.open;
               int d23;
               BlockCoffin.TileEntityCoffin iterator1;
               if(!isBlockHeadOfBed(i1)) {
                  d23 = getDirection(i1);
                  iterator1 = (BlockCoffin.TileEntityCoffin)BlockUtil.getTileEntity(world, x + DIRECTIONS[d23][0], y, z + DIRECTIONS[d23][1], BlockCoffin.TileEntityCoffin.class);
                  if(iterator1 != null) {
                     iterator1.open = tile1.open;
                     world.markBlockForUpdate(iterator1.xCoord, iterator1.yCoord, iterator1.zCoord);
                  }
               } else {
                  d23 = getDirection(i1);
                  iterator1 = (BlockCoffin.TileEntityCoffin)BlockUtil.getTileEntity(world, x - DIRECTIONS[d23][0], y, z - DIRECTIONS[d23][1], BlockCoffin.TileEntityCoffin.class);
                  if(iterator1 != null) {
                     iterator1.open = tile1.open;
                     world.markBlockForUpdate(iterator1.xCoord, iterator1.yCoord, iterator1.zCoord);
                  }
               }

               world.markBlockForUpdate(x, y, z);
            }

            return true;
         } else {
            tile1 = (BlockCoffin.TileEntityCoffin)BlockUtil.getTileEntity(world, x, y, z, BlockCoffin.TileEntityCoffin.class);
            if(tile1 != null) {
               if(!tile1.open) {
                  player.addChatComponentMessage(new ChatComponentTranslation("witchery.nosleep.closedcoffin", new Object[0]));
                  return true;
               } else if(world.provider.canRespawnHere() && world.getBiomeGenForCoords(x, z) != BiomeGenBase.hell) {
                  if(isBedOccupied(i1)) {
                     d22 = null;
                     iterator = world.playerEntities.iterator();

                     while(iterator.hasNext()) {
                        d01 = (EntityPlayer)iterator.next();
                        if(d01.isPlayerSleeping()) {
                           chunkcoordinates = d01.playerLocation;
                           if(chunkcoordinates.posX == x && chunkcoordinates.posY == y && chunkcoordinates.posZ == z) {
                              d22 = d01;
                           }
                        }
                     }

                     if(d22 != null) {
                        player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.occupied", new Object[0]));
                        return true;
                     }

                     setBedOccupied(world, x, y, z, false);
                  }

                  EnumStatus d21 = player.sleepInBedAt(x, y, z);
                  if(d21 == EnumStatus.OK) {
                     setBedOccupied(world, x, y, z, true);
                     return true;
                  } else {
                     if(d21 == EnumStatus.NOT_POSSIBLE_NOW) {
                        player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.noSleep", new Object[0]));
                     } else if(d21 == EnumStatus.NOT_SAFE) {
                        player.addChatComponentMessage(new ChatComponentTranslation("tile.bed.notSafe", new Object[0]));
                     }

                     return true;
                  }
               } else {
                  double d2 = (double)x + 0.5D;
                  double d0 = (double)y + 0.5D;
                  double d1 = (double)z + 0.5D;
                  world.setBlockToAir(x, y, z);
                  int k1 = getDirection(i1);
                  x += DIRECTIONS[k1][0];
                  z += DIRECTIONS[k1][1];
                  if(world.getBlock(x, y, z) == this) {
                     world.setBlockToAir(x, y, z);
                     d2 = (d2 + (double)x + 0.5D) / 2.0D;
                     d0 = (d0 + (double)y + 0.5D) / 2.0D;
                     d1 = (d1 + (double)z + 0.5D) / 2.0D;
                  }

                  world.newExplosion((Entity)null, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), 5.0F, true, true);
                  return true;
               }
            } else {
               return true;
            }
         }
      }
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

   public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
      super.setBlockBoundsBasedOnState(world, x, y, z);
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      int l = world.getBlockMetadata(x, y, z);
      int i1 = getDirection(l);
      if(isBlockHeadOfBed(l)) {
         if(world.getBlock(x - DIRECTIONS[i1][0], y, z - DIRECTIONS[i1][1]) != this) {
            world.setBlockToAir(x, y, z);
         }
      } else if(world.getBlock(x + DIRECTIONS[i1][0], y, z + DIRECTIONS[i1][1]) != this) {
         world.setBlockToAir(x, y, z);
         if(!world.isRemote) {
            this.dropBlockAsItem(world, x, y, z, l, 0);
         }
      }

   }

   public Item getItemDropped(int meta, Random rand, int p_149650_3_) {
      return isBlockHeadOfBed(meta)?Item.getItemById(0):Witchery.Items.COFFIN;
   }

   private void setupBounds() {}

   public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List boxes, Entity entity) {
      BlockCoffin.TileEntityCoffin tile = (BlockCoffin.TileEntityCoffin)BlockUtil.getTileEntity(world, x, y, z, BlockCoffin.TileEntityCoffin.class);
      if(tile != null && !tile.open) {
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         super.addCollisionBoxesToList(world, x, y, z, mask, boxes, entity);
      } else {
         int meta = world.getBlockMetadata(x, y, z);
         float baseHeight = 0.4375F;
         float wallThick = 0.05F;
         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, baseHeight, 1.0F);
         super.addCollisionBoxesToList(world, x, y, z, mask, boxes, entity);
         int direction = getDirection(meta);
         boolean head = isBlockHeadOfBed(meta);
         boolean n = true;
         boolean s = true;
         boolean e = true;
         boolean w = true;
         boolean n1 = false;
         boolean s1 = false;
         boolean e1 = false;
         boolean w1 = false;
         switch(direction) {
         case 0:
            n = !head;
            s = head;
            e1 = true;
            break;
         case 1:
            e = !head;
            w = head;
            s1 = true;
            break;
         case 2:
            s = !head;
            n = head;
            w1 = true;
            break;
         case 3:
            w = !head;
            e = head;
            n1 = true;
         }

         if(n) {
            this.setBlockBounds(0.0F, baseHeight, 0.0F, 1.0F, n1?2.0F:1.0F, wallThick);
            super.addCollisionBoxesToList(world, x, y, z, mask, boxes, entity);
         }

         if(s) {
            this.setBlockBounds(0.0F, baseHeight, 1.0F - wallThick, 1.0F, s1?2.0F:1.0F, 1.0F);
            super.addCollisionBoxesToList(world, x, y, z, mask, boxes, entity);
         }

         if(w) {
            this.setBlockBounds(0.0F, baseHeight, 0.0F, wallThick, w1?2.0F:1.0F, 1.0F);
            super.addCollisionBoxesToList(world, x, y, z, mask, boxes, entity);
         }

         if(e) {
            this.setBlockBounds(1.0F - wallThick, baseHeight, 0.0F, 1.0F, e1?2.0F:1.0F, 1.0F);
            super.addCollisionBoxesToList(world, x, y, z, mask, boxes, entity);
         }

         this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
         this.setupBounds();
      }
   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
      return AxisAlignedBB.getBoundingBox((double)x + super.minX, (double)y + super.minY, (double)z + super.minZ, (double)x + super.maxX, (double)y + 1.0D, (double)z + super.maxZ);
   }

   public static boolean isBlockHeadOfBed(int meta) {
      return (meta & 8) != 0;
   }

   public static boolean isBedOccupied(int meta) {
      return (meta & 4) != 0;
   }

   public static void setBedOccupied(World world, int x, int y, int z, boolean p_149979_4_) {
      int l = world.getBlockMetadata(x, y, z);
      if(p_149979_4_) {
         l |= 4;
      } else {
         l &= -5;
      }

      world.setBlockMetadataWithNotify(x, y, z, l, 3);
   }

   public static ChunkCoordinates func_149977_a(World world, int x, int y, int z, int p_149977_4_) {
      int i1 = world.getBlockMetadata(x, y, z);
      int j1 = BlockDirectional.getDirection(i1);

      for(int k1 = 0; k1 <= 1; ++k1) {
         int l1 = x - DIRECTIONS[j1][0] * k1 - 1;
         int i2 = z - DIRECTIONS[j1][1] * k1 - 1;
         int j2 = l1 + 2;
         int k2 = i2 + 2;

         for(int l2 = l1; l2 <= j2; ++l2) {
            for(int i3 = i2; i3 <= k2; ++i3) {
               if(World.doesBlockHaveSolidTopSurface(world, l2, y - 1, i3) && !world.getBlock(l2, y, i3).getMaterial().isOpaque() && !world.getBlock(l2, y + 1, i3).getMaterial().isOpaque()) {
                  if(p_149977_4_ <= 0) {
                     return new ChunkCoordinates(l2, y, i3);
                  }

                  --p_149977_4_;
               }
            }
         }
      }

      return null;
   }

   public void dropBlockAsItemWithChance(World world, int x, int y, int z, int p_149690_5_, float p_149690_6_, int p_149690_7_) {
      if(!isBlockHeadOfBed(p_149690_5_)) {
         super.dropBlockAsItemWithChance(world, x, y, z, p_149690_5_, p_149690_6_, 0);
      }

   }

   public int getMobilityFlag() {
      return 1;
   }

   @SideOnly(Side.CLIENT)
   public Item getItem(World world, int x, int y, int z) {
      return Witchery.Items.COFFIN;
   }

   public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
      if(player.capabilities.isCreativeMode && isBlockHeadOfBed(meta)) {
         int i1 = getDirection(meta);
         x -= DIRECTIONS[i1][0];
         z -= DIRECTIONS[i1][1];
         if(world.getBlock(x, y, z) == this) {
            world.setBlockToAir(x, y, z);
         }
      }

   }


   public static class TileEntityCoffin extends TileEntity {

      private boolean open;
      public float lidAngle;
      public float prevLidAngle;


      public void updateEntity() {
         this.prevLidAngle = this.lidAngle;
         if(this.open && this.lidAngle == 0.0F) {
            double f = (double)super.xCoord + 0.5D;
            double f2 = (double)super.zCoord + 0.5D;
            super.worldObj.playSoundEffect(f, (double)super.yCoord + 0.5D, f2, "random.chestopen", 0.5F, super.worldObj.rand.nextFloat() * 0.1F + 0.9F);
         }

         if(!this.open && this.lidAngle > 0.0F || this.open && this.lidAngle < 1.0F) {
            float f = 0.1F;
            float f1 = this.lidAngle;
            if(this.open) {
               this.lidAngle += 0.1F;
            } else {
               this.lidAngle -= 0.1F;
            }

            if(this.lidAngle > 1.0F) {
               this.lidAngle = 1.0F;
            }

            float f21 = 0.5F;
            if(this.lidAngle < f21 && f1 >= f21) {
               double d0 = (double)super.xCoord + 0.5D;
               double d2 = (double)super.zCoord + 0.5D;
               super.worldObj.playSoundEffect(d0, (double)super.yCoord + 0.5D, d2, "random.chestclosed", 0.5F, super.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if(this.lidAngle < 0.0F) {
               this.lidAngle = 0.0F;
            }
         }

      }

      public Packet getDescriptionPacket() {
         NBTTagCompound nbtTag = new NBTTagCompound();
         this.writeToNBT(nbtTag);
         nbtTag.setFloat("Angle", this.lidAngle);
         nbtTag.setFloat("AnglePrev", this.prevLidAngle);
         return new S35PacketUpdateTileEntity(super.xCoord, super.yCoord, super.zCoord, 1, nbtTag);
      }

      public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
         super.onDataPacket(net, packet);
         NBTTagCompound nbtTag = packet.func_148857_g();
         this.readFromNBT(nbtTag);
         this.lidAngle = nbtTag.getFloat("Angle");
         this.prevLidAngle = nbtTag.getFloat("AnglePrev");
         super.worldObj.func_147479_m(super.xCoord, super.yCoord, super.zCoord);
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         nbtRoot.setBoolean("Opened", this.open);
      }

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
         this.open = nbtRoot.getBoolean("Opened");
      }
   }
}
