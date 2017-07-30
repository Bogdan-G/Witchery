package com.emoniph.witchery.util;

import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.EntityPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockPosition {

   public final int dimension;
   public final int x;
   public final int y;
   public final int z;


   public BlockPosition(int dimension, int x, int y, int z) {
      this.dimension = dimension;
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public BlockPosition(World world, int x, int y, int z) {
      this(world.provider.dimensionId, x, y, z);
   }

   public BlockPosition(World world, Coord coord) {
      this(world, coord.x, coord.y, coord.z);
   }

   public BlockPosition(World world, double x, double y, double z) {
      this(world.provider.dimensionId, MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
   }

   public BlockPosition(World world, EntityPosition position) {
      this(world, position.x, position.y, position.z);
   }

   public static BlockPosition from(ItemStack stack) {
      NBTTagCompound tag = stack.getTagCompound();
      if(tag != null && tag.hasKey("PosX") && tag.hasKey("PosY") && tag.hasKey("PosZ") && tag.hasKey("PosD")) {
         int newX = tag.getInteger("PosX");
         int newY = tag.getInteger("PosY");
         int newZ = tag.getInteger("PosZ");
         int newD = tag.getInteger("PosD");
         return new BlockPosition(newD, newX, newY, newZ);
      } else {
         return null;
      }
   }

   public World getWorld(MinecraftServer server) {
      WorldServer[] arr$ = server.worldServers;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         WorldServer world = arr$[i$];
         if(world.provider.dimensionId == this.dimension) {
            return world;
         }
      }

      return null;
   }
}
