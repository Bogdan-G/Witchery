package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionManaSiphon extends PotionBase implements IHandleLivingUpdate {

   public PotionManaSiphon(int id, int color) {
      super(id, true, color);
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getTotalWorldTime() % 40L == 0L) {
         Collection active = entity.getActivePotionEffects();
         if(active != null && !active.isEmpty()) {
            ArrayList toShorten = new ArrayList();
            Iterator i$ = active.iterator();

            while(i$.hasNext()) {
               PotionEffect effect = (PotionEffect)i$.next();
               int id = effect.getPotionID();
               if(id >= 0 && id < Potion.potionTypes.length && Potion.potionTypes[id] != null && id != this.id && !PotionBase.isDebuff(Potion.potionTypes[id]) && PotionBase.isCurable(Potion.potionTypes[id])) {
                  toShorten.add(effect);
               }
            }

            int drainPerTick = 20 * (amplifier + 1);
            Iterator i$1 = toShorten.iterator();

            while(i$1.hasNext()) {
               PotionEffect effect = (PotionEffect)i$1.next();
               int remaining = effect.getDuration() - drainPerTick;
               if(remaining <= 0) {
                  entity.removePotionEffect(effect.getPotionID());
               } else {
                  entity.removePotionEffect(effect.getPotionID());
                  entity.addPotionEffect(new PotionEffect(effect.getPotionID(), remaining, effect.getAmplifier(), effect.getIsAmbient()));
               }
            }
         }
      }

   }
}
