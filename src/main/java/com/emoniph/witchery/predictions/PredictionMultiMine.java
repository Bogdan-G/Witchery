package com.emoniph.witchery.predictions;

import com.emoniph.witchery.predictions.PredictionAlwaysForced;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class PredictionMultiMine extends PredictionAlwaysForced {

   protected final Block block;
   protected final ItemStack itemPrototype;
   protected final int minExtra;
   protected final int maxExtra;


   public PredictionMultiMine(int id, int itemWeight, double selfFulfillmentProbabilityPerSec, String translationKey, int regularFulfillmentDurationInTicks, double regularFulfillmentProbability, Block block, ItemStack itemPrototype, int minExtra, int maxExtra) {
      super(id, itemWeight, selfFulfillmentProbabilityPerSec, translationKey, regularFulfillmentDurationInTicks, regularFulfillmentProbability);
      this.block = block;
      this.minExtra = minExtra;
      this.maxExtra = maxExtra;
      this.itemPrototype = itemPrototype;
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, HarvestDropsEvent event, boolean isPastDue, boolean veryOld) {
      if(!event.isCanceled() && (event.block == this.block || veryOld && event.block == Blocks.stone) && this.shouldWeActivate(world, player, isPastDue)) {
         int optional = this.maxExtra - this.minExtra;
         int totalExtra = this.minExtra + (optional > 1?world.rand.nextInt(optional) + 1:optional);

         for(int i = 0; i < totalExtra; ++i) {
            event.drops.add(this.itemPrototype.copy());
         }

         return true;
      } else {
         return false;
      }
   }
}
