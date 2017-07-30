package com.emoniph.witchery.brewing;


public class AltarPower {

   private int power;


   public AltarPower(int power) {
      this.power = power;
   }

   public int getPower() {
      return this.power;
   }

   public void accumulate(AltarPower other) {
      this.power += other.power;
   }
}
