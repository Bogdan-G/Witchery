package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.action.effect.BrewActionSprouting;
import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionSprouting extends PotionBase implements IHandleLivingUpdate {

   public PotionSprouting(int id, int color) {
      super(id, color);
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getTotalWorldTime() % 20L == 9L && world.rand.nextInt(4) == 0) {
         BrewActionSprouting.growBranch(entity, 1 + amplifier);
      }

   }
}
