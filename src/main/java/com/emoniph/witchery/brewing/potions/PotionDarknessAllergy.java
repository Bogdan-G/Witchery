package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class PotionDarknessAllergy extends PotionBase {

   public PotionDarknessAllergy(int id, int color) {
      super(id, true, color);
      this.setIncurable();
   }

   public boolean isReady(int duration, int amplifier) {
      return duration % 20 == 4;
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      int x = MathHelper.floor_double(entity.posX);
      int y = MathHelper.floor_double(entity.posY);
      int z = MathHelper.floor_double(entity.posZ);
      int lightLevel = entity.worldObj.getBlockLightValue(x, y, z);
      if(lightLevel < 2 + amplifier * 2) {
         entity.attackEntityFrom(DamageSource.outOfWorld, 1.0F);
      }

   }
}
