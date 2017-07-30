package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;

public class PotionQueasy extends PotionBase {

   public PotionQueasy(int id, int color) {
      super(id, true, color);
   }

   public void postContructInitialize() {
      this.setIncurable();
   }
}
