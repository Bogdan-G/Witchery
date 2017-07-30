package com.emoniph.witchery.ritual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public abstract class Rite {

   protected boolean canRelocate = false;


   public abstract void addSteps(ArrayList var1, int var2);

   public ArrayList getItemsInRadius(World world, int x, int y, int z, float radius) {
      float RADIUS_SQ = radius * radius;
      double midX = 0.5D + (double)x;
      double midZ = 0.5D + (double)z;
      ArrayList resultList = new ArrayList();
      AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(midX - (double)radius, (double)y, midZ - (double)radius, midX + (double)radius, 1.0D + (double)y, midZ + (double)radius);
      List items = world.getEntitiesWithinAABB(EntityItem.class, bounds);
      Iterator i$ = items.iterator();

      while(i$.hasNext()) {
         Object obj = i$.next();
         EntityItem entity = (EntityItem)obj;
         if(entity.getDistanceSq(midX, (double)y, midZ) <= (double)RADIUS_SQ) {
            resultList.add(entity);
         }
      }

      return resultList;
   }

   public boolean relocatable() {
      return this.canRelocate;
   }
}
