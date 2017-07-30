package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.BrewNameBuilder;
import com.emoniph.witchery.brewing.BrewNamePart;

public class BrewNamePartModifier extends BrewNamePart {

   int dispersalExtent;
   int dispersalDuration;
   int strength;
   int duration;
   boolean inverted;
   boolean removePowerCeiling;


   public BrewNamePartModifier(int strength, int duration, boolean invert, int dispersalExtent, int dispersalDuration) {
      this(strength, duration, invert, dispersalExtent, dispersalDuration, false);
   }

   public BrewNamePartModifier(int strength, int duration, boolean invert, int dispersalExtent, int dispersalDuration, boolean removeCeiling) {
      super("");
      this.strength = strength;
      this.duration = duration;
      this.inverted = invert;
      this.dispersalExtent = dispersalExtent;
      this.dispersalDuration = dispersalDuration;
      this.removePowerCeiling = removeCeiling;
   }

   public void applyTo(BrewNameBuilder nameBuilder) {
      nameBuilder.dispersalExtent += this.dispersalExtent;
      nameBuilder.dispersalDuration += this.dispersalDuration;
      nameBuilder.addStrength(this.strength);
      nameBuilder.addDuration(this.duration);
      if(this.inverted) {
         nameBuilder.inverted = true;
      }

      if(this.removePowerCeiling) {
         nameBuilder.removePowerCeiling = true;
      }

   }
}
