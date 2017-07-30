package com.emoniph.witchery.brewing;


public class ModifierYield {

   private int bonus;
   private int penalty;


   public ModifierYield(int modifier) {
      if(modifier < 0) {
         this.penalty = Math.abs(modifier);
      } else if(modifier > 0) {
         this.bonus = modifier;
      }

   }

   public int getYieldModification() {
      return this.penalty - Math.min(this.bonus, this.penalty);
   }

   public void applyTo(ModifierYield counter) {
      counter.bonus += this.bonus;
      counter.penalty += this.penalty;
   }
}
