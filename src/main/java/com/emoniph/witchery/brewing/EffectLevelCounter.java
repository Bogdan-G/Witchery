package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.EffectLevel;

public class EffectLevelCounter {

   private int maxLevel;
   private int currentLevel;
   private int effects;


   public void increaseAvailableLevelIf(EffectLevel incement, EffectLevel ceilingLevel) {
      if(this.maxLevel < ceilingLevel.getLevel()) {
         this.maxLevel += incement.getLevel();
      }

   }

   public int remainingCapactiy() {
      return this.maxLevel - this.currentLevel;
   }

   public int usedCapacity() {
      return this.currentLevel;
   }

   public int getEffectCount() {
      return this.effects;
   }

   public boolean tryConsumeLevel(EffectLevel level) {
      if(this.canConsumeLevel(level)) {
         this.currentLevel += level.getLevel();
         ++this.effects;
         return true;
      } else {
         return false;
      }
   }

   public boolean canConsumeLevel(EffectLevel level) {
      return level.getLevel() + this.currentLevel <= this.maxLevel;
   }

   public boolean hasEffects() {
      return this.currentLevel > 0;
   }

   public boolean canIncreasePlayerSkill(int currentSkillLevel) {
      return this.currentLevel <= this.maxLevel && this.maxLevel != 0?(currentSkillLevel < 30?this.maxLevel > 1 && this.currentLevel > 1:(currentSkillLevel < 60?this.maxLevel >= 4 && this.currentLevel >= 4:(currentSkillLevel < 90?this.maxLevel >= 6 && this.currentLevel >= 6:(currentSkillLevel == 100?false:this.maxLevel >= 8 && this.currentLevel >= 8)))):false;
   }
}
