package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;

public class PotionFeatherFall extends PotionBase {

   public PotionFeatherFall(int id, int color) {
      super(id, color);
   }

   public boolean isReady(int duration, int amplifier) {
      return true;
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      int activationDistance = amplifier >= 2?3:(amplifier >= 1?4:5);
      int maxFallDistance = amplifier >= 3?3:(amplifier >= 2?4:(amplifier >= 1?5:6));
      if(entity.fallDistance >= (float)activationDistance && entity.motionY < -0.2D) {
         entity.motionY = -0.2D;
         if(entity.fallDistance > (float)maxFallDistance) {
            entity.fallDistance = (float)maxFallDistance;
         }
      }

   }
}
