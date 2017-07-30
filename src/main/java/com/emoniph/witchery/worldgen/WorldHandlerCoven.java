package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.worldgen.ComponentCoven;
import com.emoniph.witchery.worldgen.IWorldGenHandler;
import java.util.Random;
import net.minecraft.world.World;

public class WorldHandlerCoven implements IWorldGenHandler {

   private final double chance;
   private final int range;


   public WorldHandlerCoven(double chance, int range) {
      this.chance = chance;
      this.range = range;
   }

   public int getExtentX() {
      return 11;
   }

   public int getExtentZ() {
      return 11;
   }

   public int getRange() {
      return this.range;
   }

   public boolean generate(World world, Random random, int x, int z) {
      if(Config.instance().generateCovens && random.nextDouble() < this.chance) {
         int direction = random.nextInt(4);
         if(!(new ComponentCoven(direction, random, x, z)).addComponentParts(world, random)) {
            return false;
         } else {
            Log.instance().debug("coven " + x + " " + z + " dir=" + direction);
            return true;
         }
      } else {
         return false;
      }
   }

   public void initiate() {}
}
