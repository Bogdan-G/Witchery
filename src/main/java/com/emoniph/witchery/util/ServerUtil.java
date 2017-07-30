package com.emoniph.witchery.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class ServerUtil {

   public static WorldServer getWorld(int dimension) {
      WorldServer[] arr$ = MinecraftServer.getServer().worldServers;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         WorldServer world = arr$[i$];
         if(world.provider.dimensionId == dimension) {
            return world;
         }
      }

      return null;
   }
}
