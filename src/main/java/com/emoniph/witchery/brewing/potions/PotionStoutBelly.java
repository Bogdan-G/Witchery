package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionStoutBelly extends PotionBase implements IHandleLivingUpdate {

   public PotionStoutBelly(int id, int color) {
      super(id, color);
      this.setIncurable();
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getWorldTime() % 20L == 3L && amplifier > 0 && entity.isPotionActive(Potion.confusion)) {
         entity.removePotionEffect(Potion.confusion.id);
      }

   }
}
