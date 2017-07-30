package com.emoniph.witchery.util;

import com.emoniph.witchery.common.INullSource;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.EntityPosition;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public final class Coord {

   public final int x;
   public final int y;
   public final int z;


   public Coord(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Coord(int x, int y, int z, ForgeDirection side) {
      this.x = x + side.offsetX;
      this.y = y + side.offsetY;
      this.z = z + side.offsetZ;
   }

   public Coord(TileEntity tileEntity) {
      this(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
   }

   public Coord(Entity entity) {
      this(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ));
   }

   public Coord(INullSource entity) {
      this(entity.getPosX(), entity.getPosY(), entity.getPosZ());
   }

   public Coord(MovingObjectPosition mop, EntityPosition alternativePos, boolean before) {
      if(mop != null) {
         switch(Coord.NamelessClass768241351.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
         case 1:
            if(before) {
               this.x = mop.blockX + (mop.sideHit == 4?-1:(mop.sideHit == 5?1:0));
               this.y = mop.blockY + (mop.sideHit == 0?-1:(mop.sideHit == 1?1:0));
               this.z = mop.blockZ + (mop.sideHit == 2?-1:(mop.sideHit == 3?1:0));
            } else {
               this.x = mop.blockX;
               this.y = mop.blockY;
               this.z = mop.blockZ;
            }
            break;
         case 2:
            this.x = MathHelper.floor_double(mop.entityHit.posX);
            this.y = MathHelper.floor_double(mop.entityHit.posY);
            this.z = MathHelper.floor_double(mop.entityHit.posZ);
            break;
         case 3:
         default:
            if(alternativePos != null) {
               this.x = MathHelper.floor_double(alternativePos.x);
               this.y = MathHelper.floor_double(alternativePos.y);
               this.z = MathHelper.floor_double(alternativePos.z);
            } else {
               this.x = 0;
               this.y = 0;
               this.z = 0;
            }
         }
      } else if(alternativePos != null) {
         this.x = MathHelper.floor_double(alternativePos.x);
         this.y = MathHelper.floor_double(alternativePos.y);
         this.z = MathHelper.floor_double(alternativePos.z);
      } else {
         this.x = 0;
         this.y = 0;
         this.z = 0;
      }

   }

   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      } else if(obj != null && obj.getClass() == this.getClass()) {
         Coord other = (Coord)obj;
         return this.x == other.x && this.y == other.y && this.z == other.z;
      } else {
         return false;
      }
   }

   public boolean isAtPosition(TileEntity tileEntity) {
      return tileEntity != null && this.x == tileEntity.xCoord && this.y == tileEntity.yCoord && this.z == tileEntity.zCoord;
   }

   public Coord north() {
      return this.north(1);
   }

   public Coord north(int n) {
      return new Coord(this.x, this.y, this.z - n);
   }

   public Coord south() {
      return this.south(1);
   }

   public Coord south(int n) {
      return new Coord(this.x, this.y, this.z + n);
   }

   public Coord east() {
      return this.east(1);
   }

   public Coord east(int n) {
      return new Coord(this.x + n, this.y, this.z);
   }

   public Coord west() {
      return this.west(1);
   }

   public Coord west(int n) {
      return new Coord(this.x - n, this.y, this.z);
   }

   public Coord northEast() {
      return new Coord(this.x + 1, this.y, this.z - 1);
   }

   public Coord northWest() {
      return new Coord(this.x - 1, this.y, this.z - 1);
   }

   public Coord southEast() {
      return new Coord(this.x + 1, this.y, this.z + 1);
   }

   public Coord southWest() {
      return new Coord(this.x - 1, this.y, this.z + 1);
   }

   public Block getBlock(World world) {
      return this.getBlock(world, 0, 0, 0);
   }

   public Block getBlock(World world, int offsetX, int offsetY, int offsetZ) {
      return world.getBlock(this.x + offsetX, this.y + offsetY, this.z + offsetZ);
   }

   public TileEntity getBlockTileEntity(World world) {
      return this.getBlockTileEntity(world, 0, 0, 0);
   }

   public TileEntity getBlockTileEntity(World world, int offsetX, int offsetY, int offsetZ) {
      return world.getTileEntity(this.x + offsetX, this.y + offsetY, this.z + offsetZ);
   }

   public Object getTileEntity(IBlockAccess world, Class clazz) {
      return BlockUtil.getTileEntity(world, this.x, this.y, this.z, clazz);
   }

   public int getBlockMetadata(World world) {
      return this.getBlockMetadata(world, 0, 0, 0);
   }

   public int getBlockMetadata(World world, int offsetX, int offsetY, int offsetZ) {
      return world.getBlockMetadata(this.x + offsetX, this.y + offsetY, this.z + offsetZ);
   }

   public void setNBT(NBTTagCompound nbtTag, String key) {
      nbtTag.setInteger(key + "X", this.x);
      nbtTag.setInteger(key + "Y", this.y);
      nbtTag.setInteger(key + "Z", this.z);
   }

   public static double distance(Coord first, Coord second) {
      double dX = (double)(first.x - second.x);
      double dY = (double)(first.y - second.y);
      double dZ = (double)(first.z - second.z);
      return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
   }

   public static double distance(double firstX, double firstY, double firstZ, double secondX, double secondY, double secondZ) {
      double dX = firstX - secondX;
      double dY = firstY - secondY;
      double dZ = firstZ - secondZ;
      return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
   }

   public static double distanceSq(double firstX, double firstY, double firstZ, double secondX, double secondY, double secondZ) {
      double dX = firstX - secondX;
      double dY = firstY - secondY;
      double dZ = firstZ - secondZ;
      return dX * dX + dY * dY + dZ * dZ;
   }

   public double distanceTo(Coord other) {
      double dX = (double)(other.x - this.x);
      double dY = (double)(other.y - this.y);
      double dZ = (double)(other.z - this.z);
      return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
   }

   public double distanceSqTo(Coord other) {
      double dX = (double)(other.x - this.x);
      double dY = (double)(other.y - this.y);
      double dZ = (double)(other.z - this.z);
      return dX * dX + dY * dY + dZ * dZ;
   }

   public double distanceSqTo(int x, int y, int z) {
      double dX = (double)(x - this.x);
      double dY = (double)(y - this.y);
      double dZ = (double)(z - this.z);
      return dX * dX + dY * dY + dZ * dZ;
   }

   public static Coord createFrom(NBTTagCompound nbtTag, String key) {
      return nbtTag.hasKey(key + "X") && nbtTag.hasKey(key + "Y") && nbtTag.hasKey(key + "Z")?new Coord(nbtTag.getInteger(key + "X"), nbtTag.getInteger(key + "Y"), nbtTag.getInteger(key + "Z")):null;
   }

   public boolean isWestOf(Coord coord) {
      return this.x < coord.x;
   }

   public boolean isNorthOf(Coord coord) {
      return this.z < coord.z;
   }

   public boolean setBlock(World world, Block block) {
      return world.setBlock(this.x, this.y, this.z, block);
   }

   public boolean setBlock(World world, Block block, int metadata, int flags) {
      return world.setBlock(this.x, this.y, this.z, block, metadata, flags);
   }

   public void setAir(World world) {
      world.setBlockToAir(this.x, this.y, this.z);
   }

   public void markBlockForUpdate(World world) {
      world.markBlockForUpdate(this.x, this.y, this.z);
   }

   public int getHeading(Coord destination) {
      double dX = (double)(this.x - destination.x);
      double dZ = (double)(this.z - destination.z);
      double yaw = Math.atan2(dZ, dX);
      double PI8 = 0.39269908169872414D;
      double PI2 = 1.5707963267948966D;
      return yaw > -0.39269908169872414D && yaw <= 0.39269908169872414D?6:(yaw > 0.39269908169872414D && yaw <= 1.1780972450961724D?7:(yaw > 1.1780972450961724D && yaw <= 1.9634954084936207D?0:(yaw > 1.9634954084936207D && yaw <= 2.748893571891069D?1:(yaw <= 2.748893571891069D && yaw > -2.748893571891069D?(yaw > -2.748893571891069D && yaw <= -1.9634954084936207D?3:(yaw > -1.9634954084936207D && yaw <= -1.1780972450961724D?4:5)):2))));
   }

   public String toString() {
      return String.format("%d, %d, %d", new Object[]{Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.z)});
   }

   public NBTTagCompound toTagNBT() {
      NBTTagCompound nbt = new NBTTagCompound();
      nbt.setInteger("posX", this.x);
      nbt.setInteger("posY", this.y);
      nbt.setInteger("posZ", this.z);
      return nbt;
   }

   public static Coord fromTagNBT(NBTTagCompound nbt) {
      return nbt.hasKey("posX") && nbt.hasKey("posY") && nbt.hasKey("posZ")?new Coord(nbt.getInteger("posX"), nbt.getInteger("posY"), nbt.getInteger("posZ")):null;
   }

   public boolean isMatch(int x, int y, int z) {
      return this.x == x && this.y == y && this.z == z;
   }

   // $FF: synthetic class
   static class NamelessClass768241351 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType = new int[MovingObjectType.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.BLOCK.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.ENTITY.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.MISS.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
