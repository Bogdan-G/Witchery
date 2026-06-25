package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.brewing.potions.PotionEnderInhibition;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.rites.RiteTeleportation;
import com.emoniph.witchery.util.Coord;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteTeleportToWaystone extends RiteTeleportation {

   public RiteTeleportToWaystone(int radius) {
      super(radius);
   }

   protected boolean teleport(World world, int posX, int posY, int posZ, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
      if(!world.isRemote) {
         ItemStack waystoneStack = null;
         Iterator bounds = ritual.sacrificedItems.iterator();

         while(bounds.hasNext()) {
            RitualStep.SacrificedItem list = (RitualStep.SacrificedItem)bounds.next();
            if(Witchery.Items.GENERIC.itemWaystoneBound.isMatch(list.itemstack) || Witchery.Items.GENERIC.itemWaystonePlayerBound.isMatch(list.itemstack)) {
               waystoneStack = list.itemstack;
               break;
            }
         }

         if(waystoneStack != null) {
            AxisAlignedBB bounds1 = AxisAlignedBB.getBoundingBox((double)(posX - super.radius), (double)(posY - super.radius), (double)(posZ - super.radius), (double)(posX + super.radius), (double)(posY + super.radius), (double)(posZ + super.radius));
            List list1 = world.getEntitiesWithinAABB(Entity.class, bounds1);
            boolean sent = false;
            Iterator iterator = list1.iterator();

            while(iterator.hasNext()) {
               Entity entity = (Entity)iterator.next();
               if(Coord.distance(entity.posX, entity.posY, entity.posZ, (double)posX, (double)posY, (double)posZ) < (double)super.radius && !PotionEnderInhibition.isActive(entity, 1) && Witchery.Items.GENERIC.teleportToLocation(world, waystoneStack, entity, super.radius, true)) {
                  sent = true;
               }
            }

            return sent;
         }
      }

      return false;
   }
}
