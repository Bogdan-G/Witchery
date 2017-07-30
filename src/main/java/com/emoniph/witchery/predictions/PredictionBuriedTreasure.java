package com.emoniph.witchery.predictions;

import com.emoniph.witchery.predictions.PredictionAlwaysForced;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class PredictionBuriedTreasure extends PredictionAlwaysForced {

   protected final String chestGenHook;


   public PredictionBuriedTreasure(int id, int itemWeight, double selfFulfillmentProbabilityPerSec, String translationKey, int regularFulfillmentDurationInTicks, double regularFulfillmentProbability, String chestGenHook) {
      super(id, itemWeight, selfFulfillmentProbabilityPerSec, translationKey, regularFulfillmentDurationInTicks, regularFulfillmentProbability);
      this.chestGenHook = chestGenHook;
   }

   public boolean shouldTrySelfFulfill(World world, EntityPlayer player) {
      return false;
   }

   public boolean doSelfFulfillment(World world, EntityPlayer player) {
      return false;
   }

   public boolean checkIfFulfilled(World world, EntityPlayer player, HarvestDropsEvent event, boolean isPastDue, boolean veryOld) {
      if(!event.isCanceled() && (event.block == Blocks.grass || event.block == Blocks.dirt || event.block == Blocks.sand || event.block == Blocks.mycelium) && event.y > 6 && this.shouldWeActivate(world, player, isPastDue) && !world.isAirBlock(event.x + 1, event.y - 1, event.z) && !world.isAirBlock(event.x - 1, event.y - 1, event.z) && !world.isAirBlock(event.x, event.y - 1, event.z + 1) && !world.isAirBlock(event.x, event.y - 1, event.z - 1) && !world.isAirBlock(event.x, event.y - 2, event.z)) {
         world.setBlock(event.x, event.y - 1, event.z, Blocks.chest);
         TileEntity tile = world.getTileEntity(event.x, event.y - 1, event.z);
         if(tile != null && tile instanceof TileEntityChest) {
            TileEntityChest chest = (TileEntityChest)tile;
            ChestGenHooks info = ChestGenHooks.getInfo(this.chestGenHook);
            WeightedRandomChestContent.generateChestContents(world.rand, info.getItems(world.rand), chest, info.getCount(world.rand));
         }

         return true;
      } else {
         return false;
      }
   }
}
