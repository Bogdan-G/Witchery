package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;

public class PotionKeepEffectsOnDeath extends PotionBase {

   public PotionKeepEffectsOnDeath(int id, int color) {
      super(id, color);
   }

   public void postContructInitialize() {
      this.setPermenant();
   }
}
