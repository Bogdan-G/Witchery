package com.emoniph.witchery.brewing.action;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.EffectLevelCounter;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.RitualStatus;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.brewing.action.BrewActionList;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.util.CreatureUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewPotionEffect extends BrewAction {

   private static final int DEFAULT_STRENGTH_CEILING = 10;
   public final Potion potion;
   public final Potion invertedPotion;
   public final int baseDuration;
   public final int invertedDuration;
   public final EffectLevel effectLevel;
   protected int strengthCeiling;


   public BrewPotionEffect(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, Potion effect, long baseDuration, EffectLevel effectLevel) {
      this(itemKey, namePart, powerCost, baseProbability, effect, baseDuration, effect, baseDuration, effectLevel);
   }

   public BrewPotionEffect(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, Potion effect, long baseDuration, Potion invertedEffect, long invertedDuration, EffectLevel effectLevel) {
      super(itemKey, namePart.setBaseDuration(baseDuration, invertedDuration), powerCost, baseProbability, false);
      this.strengthCeiling = 10;
      this.potion = effect;
      this.invertedPotion = invertedEffect;
      this.baseDuration = (int)baseDuration;
      this.invertedDuration = (int)invertedDuration;
      this.effectLevel = effectLevel;
   }

   public BrewPotionEffect setStrengthCeiling(int ceiling) {
      this.strengthCeiling = ceiling;
      return this;
   }

   public void applyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
      if(!modifiers.disableEntityTarget) {
         if(modifiers.inverted) {
            if(!PotionBase.isDebuff(this.invertedPotion) || !modifiers.protectedFromNegativePotions) {
               applyPotionEffect(targetEntity, modifiers, this.invertedPotion, this.invertedDuration, modifiers.noParticles, modifiers.caster, this.strengthCeiling);
            }
         } else if(!PotionBase.isDebuff(this.potion) || !modifiers.protectedFromNegativePotions) {
            if(this.potion == Witchery.Potions.DOUBLE_JUMP) {
               if(modifiers.caster == null || !Familiar.hasActiveBrewMasteryFamiliar(modifiers.caster)) {
                  modifiers.reset();
                  return;
               }
            } else if(this.potion == Witchery.Potions.DISEASED && CreatureUtil.isImmuneToDisease(targetEntity)) {
               modifiers.reset();
               return;
            }

            applyPotionEffect(targetEntity, modifiers, this.potion, this.baseDuration, modifiers.noParticles, modifiers.caster, this.strengthCeiling);
         }

         modifiers.reset();
      }

   }

   public final boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
      return totalEffects.tryConsumeLevel(this.effectLevel);
   }

   public static void applyPotionEffect(EntityLivingBase entity, ModifiersEffect modifiers, Potion potion, int duration, boolean noParticles, EntityPlayer thrower) {
      applyPotionEffect(entity, modifiers, potion, duration, noParticles, thrower, 10);
   }

   public static void applyPotionEffect(EntityLivingBase entity, ModifiersEffect modifiers, Potion potion, int duration, boolean noParticles, EntityPlayer thrower, int strengthCeiling) {
      int strength = Math.min(modifiers.getStrength(), modifiers.strengthCeilingDisabled?10:strengthCeiling);
      if(potion.isInstant()) {
         potion.affectEntity(thrower, entity, strength, modifiers.powerScalingFactor);
      } else {
         entity.addPotionEffect(new PotionEffect(potion.id, modifiers.getModifiedDuration(duration), strength, noParticles));
      }

   }

   public final void prepareSplashPotion(World world, BrewActionList actionList, ModifiersImpact modifiers) {}

   public final void applyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {}

   public final void augmentEffectModifiers(ModifiersEffect modifiers) {}

   public final void prepareRitual(World world, int x, int y, int z, ModifiersRitual modifiers, ItemStack stack) {}

   public final RitualStatus updateRitual(MinecraftServer server, BrewActionList actionList, World world, int x, int y, int z, ModifiersRitual modifiers, ModifiersImpact impactModifiers) {
      return RitualStatus.COMPLETE;
   }
}
