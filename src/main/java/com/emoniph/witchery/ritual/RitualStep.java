package com.emoniph.witchery.ritual;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Coord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class RitualStep {

   protected boolean canRelocate = false;
   protected int sourceX;
   protected int sourceY;
   protected int sourceZ;


   protected RitualStep(boolean canRelocate) {
      this.canRelocate = canRelocate;
   }

   public RitualStep.Result run(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
      this.sourceX = posX;
      this.sourceZ = posZ;
      this.sourceY = posY;
      if(this.canRelocate && ritual.getLocation() != null) {
         Coord l = ritual.getLocation();
         int maxDistance = 50 + 50 * ritual.covenSize;
         int maxDistanceSq = maxDistance * maxDistance;
         if(l.distanceSqTo(this.sourceX, this.sourceY, this.sourceZ) > (double)maxDistanceSq) {
            EntityPlayer player = ritual.getInitiatingPlayer(world);
            if(player != null) {
               ChatUtil.sendTranslated(player, "witchery.rite.toofaraway", new Object[0]);
            }

            return RitualStep.Result.ABORTED_REFUND;
         }

         posX = l.x;
         posY = l.y;
         posZ = l.z;
      }

      return this.process(world, posX, posY, posZ, ticks, ritual);
   }

   public abstract RitualStep.Result process(World var1, int var2, int var3, int var4, long var5, BlockCircle.TileEntityCircle.ActivatedRitual var7);

   public int getCurrentStage() {
      return 0;
   }

   public static enum Result {

      STARTING("STARTING", 0),
      UPKEEP("UPKEEP", 1),
      COMPLETED("COMPLETED", 2),
      ABORTED("ABORTED", 3),
      ABORTED_REFUND("ABORTED_REFUND", 4);
      // $FF: synthetic field
      private static final RitualStep.Result[] $VALUES = new RitualStep.Result[]{STARTING, UPKEEP, COMPLETED, ABORTED, ABORTED_REFUND};


      private Result(String var1, int var2) {}

   }

   public static class SacrificedItem {

      public final ItemStack itemstack;
      public final Coord location;


      public SacrificedItem(ItemStack itemstack, Coord location) {
         this.itemstack = itemstack;
         this.location = location;
      }
   }
}
