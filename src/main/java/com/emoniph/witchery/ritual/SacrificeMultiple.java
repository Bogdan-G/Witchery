package com.emoniph.witchery.ritual;

import com.emoniph.witchery.ritual.Sacrifice;
import java.util.ArrayList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class SacrificeMultiple extends Sacrifice {

   private final Sacrifice[] sacrifices;


   public SacrificeMultiple(Sacrifice ... sacrifices) {
      this.sacrifices = sacrifices;
   }

   public void addDescription(StringBuffer sb) {
      Sacrifice[] arr$ = this.sacrifices;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Sacrifice sacrifice = arr$[i$];
         sacrifice.addDescription(sb);
      }

   }

   public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList entities, ArrayList grassperStacks) {
      Sacrifice[] arr$ = this.sacrifices;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Sacrifice sacrifice = arr$[i$];
         if(!sacrifice.isMatch(world, posX, posY, posZ, maxDistance, entities, grassperStacks)) {
            return false;
         }
      }

      return true;
   }

   public void addSteps(ArrayList steps, AxisAlignedBB bounds, int maxDistance) {
      Sacrifice[] arr$ = this.sacrifices;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Sacrifice sacrifice = arr$[i$];
         sacrifice.addSteps(steps, bounds, maxDistance);
      }

   }
}
