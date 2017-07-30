package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;

public class PotionMortalCoil extends PotionBase {

   public PotionMortalCoil(int id, int color) {
      super(id, true, 0);
      this.setIncurable();
   }

   public boolean isReady(int duration, int amplifier) {
      return duration == 1;
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      EntityUtil.instantDeath(entity, (EntityLivingBase)null);
   }
}
