package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.EffectLevelCounter;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.util.EntityPosition;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class ModifiersEffect {

   public int strength;
   public int strengthPenalty;
   public int duration;
   public boolean noParticles;
   public final EffectLevelCounter effectLevel;
   public boolean inverted;
   public boolean disableBlockTarget;
   public boolean disableEntityTarget;
   public boolean strengthCeilingDisabled;
   public boolean powerhCeilingDisabled;
   public final double powerScalingFactor;
   public final double durationScalingFactor;
   public final boolean isGlancing;
   public final EntityPosition impactLocation;
   public final boolean ritualised;
   public final int covenSize;
   public final EntityPlayer caster;
   public int totalStrength;
   public int totalDuration;
   public int permenance;
   public boolean protectedFromNegativePotions;
   private static final int[] covenToMaxStrength = new int[]{1, 1, 2, 2, 3, 3, 4};


   public ModifiersEffect(double powerScalingFactor, double durationScalingFactor, boolean glancing, EntityPosition position, boolean ritualised, int covenSize, EntityPlayer caster) {
      this.effectLevel = new EffectLevelCounter();
      this.powerScalingFactor = powerScalingFactor;
      this.durationScalingFactor = durationScalingFactor;
      this.isGlancing = glancing;
      this.impactLocation = position;
      this.ritualised = ritualised;
      this.caster = caster;
      this.covenSize = covenSize;
   }

   public ModifiersEffect(double powerScalingFactor, double durationScalingFactor, boolean glancing, EntityPosition position, ModifiersImpact impactModifiers) {
      this(powerScalingFactor, durationScalingFactor, glancing, position, impactModifiers.ritualised, impactModifiers.covenSize, impactModifiers.thrower);
   }

   public int getStrength() {
      return this.ritualised?Math.min(Math.max(this.strength - this.strengthPenalty, 0), covenToMaxStrength[Math.min(this.covenSize, covenToMaxStrength.length - 1)]):Math.max(this.strength - this.strengthPenalty, 0);
   }

   public int getModifiedDuration(int ticks) {
      return Math.min(MathHelper.ceiling_double_int(this.durationScalingFactor * (double)ticks * (double)(this.duration + 1)), Integer.MAX_VALUE);
   }

   public void reset() {
      this.inverted = false;
      this.strength = 0;
      this.duration = 0;
      this.noParticles = false;
   }

   public void increaseStrength(int strength) {
      if(this.totalStrength < 7 || this.powerhCeilingDisabled) {
         this.strength += strength;
         this.totalStrength += strength;
      }

   }

   public void increaseDuration(int duration) {
      if(this.totalDuration < 7 || this.powerhCeilingDisabled) {
         this.duration += duration;
         this.totalDuration += duration;
      }

   }

}
