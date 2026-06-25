package com.emoniph.witchery.infusion.infusions.spirit;

import com.emoniph.witchery.infusion.infusions.spirit.InfusedSpiritEffect;
import com.emoniph.witchery.util.TimeUtil;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;

public class InfusedSpiritCurseBringerEffect extends InfusedSpiritEffect {

   public InfusedSpiritCurseBringerEffect(int id, int spirits, int spectres, int banshees, int poltergeists) {
      super(id, "cursebringer", spirits, spectres, banshees, poltergeists);
   }

   @Override
   public int getCooldownTicks() {
      return TimeUtil.secsToTicks(5);
   }

   @Override
   public double getRadius() {
      return 12.0D;
   }

   @Override
   public boolean doUpdateEffect(TileEntity tile, boolean triggered, ArrayList foundEntities) {
      if(triggered) {
         Iterator i$ = foundEntities.iterator();

         while(i$.hasNext()) {
            EntityLivingBase entity = (EntityLivingBase)i$.next();
            entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, TimeUtil.secsToTicks(10), 1));
            entity.addPotionEffect(new PotionEffect(Potion.weakness.id, TimeUtil.secsToTicks(10), 1));
            entity.addPotionEffect(new PotionEffect(Potion.blindness.id, TimeUtil.secsToTicks(10), 0));
            // Add a bit of wither effect too
            entity.addPotionEffect(new PotionEffect(Potion.wither.id, TimeUtil.secsToTicks(5), 0));
         }
      }

      return triggered;
   }
}
