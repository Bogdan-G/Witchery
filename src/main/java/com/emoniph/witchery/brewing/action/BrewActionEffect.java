package com.emoniph.witchery.brewing.action;

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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BrewActionEffect extends BrewAction {

   protected final EffectLevel effectLevel;


   public BrewActionEffect(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, EffectLevel effectLevel) {
      super(itemKey, namePart, powerCost, baseProbability, false);
      this.effectLevel = effectLevel;
   }

   public void prepareSplashPotion(World world, BrewActionList actionList, ModifiersImpact modifiers) {}

   public void prepareRitual(World world, int x, int y, int z, ModifiersRitual modifiers, ItemStack stack) {}

   public final void applyRitualToEntity(World world, EntityLivingBase targetEntity, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
      if(!modifiers.disableEntityTarget) {
         this.doApplyRitualToEntity(world, targetEntity, ritualModifiers, modifiers, stack);
         modifiers.reset();
      }

   }

   public final void applyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
      if(!modifiers.disableEntityTarget) {
         this.doApplyToEntity(world, targetEntity, modifiers, stack);
         modifiers.reset();
      }

   }

   protected void doApplyRitualToEntity(World world, EntityLivingBase targetEntity, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
      this.doApplyToEntity(world, targetEntity, modifiers, stack);
   }

   protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {}

   public final void applyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {
      if(!modifiers.disableBlockTarget) {
         this.doApplyToBlock(world, x, y, z, side, radius, modifiers, stack);
         modifiers.reset();
      }

   }

   public final void applyRitualToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
      if(!modifiers.disableBlockTarget) {
         this.doApplyRitualToBlock(world, x, y, z, side, radius, ritualModifiers, modifiers, stack);
         modifiers.reset();
      }

   }

   protected void doApplyRitualToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
      this.doApplyToBlock(world, x, y, z, side, radius, modifiers, stack);
   }

   protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {}

   public final boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
      return totalEffects.tryConsumeLevel(this.effectLevel);
   }

   public final void augmentEffectModifiers(ModifiersEffect modifiers) {}

   public final RitualStatus updateRitual(MinecraftServer server, BrewActionList actionList, World world, int x, int y, int z, ModifiersRitual modifiers, ModifiersImpact impactModifiers) {
      return RitualStatus.COMPLETE;
   }
}
