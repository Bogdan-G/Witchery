package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;

public class PotionGasMask extends PotionBase {

   public PotionGasMask(int id, int color) {
      super(id, color);
   }

   public void postContructInitialize() {
      this.setIncurable();
   }
}
