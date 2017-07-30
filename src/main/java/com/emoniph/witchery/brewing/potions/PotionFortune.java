package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleHarvestDrops;
import com.emoniph.witchery.brewing.potions.PotionBase;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class PotionFortune extends PotionBase implements IHandleHarvestDrops {

   public PotionFortune(int id, int color) {
      super(id, color);
   }

   public void onHarvestDrops(World world, EntityPlayer player, HarvestDropsEvent event, int amplifier) {
      if(!event.world.isRemote && !event.isSilkTouching && event.block != null && !event.block.hasTileEntity(event.blockMetadata) && event.drops.size() > 0) {
         ArrayList drops = event.block.getDrops(event.world, event.x, event.y, event.z, event.blockMetadata, event.fortuneLevel + (amplifier > 2?2:1));
         event.drops.clear();
         event.drops.addAll(drops);
      }

   }
}
