package com.emoniph.witchery.brewing.action;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevelCounter;
import com.emoniph.witchery.brewing.ModifierYield;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.RitualStatus;
import com.emoniph.witchery.brewing.action.BrewActionList;
import com.emoniph.witchery.util.BlockPosition;
import java.util.ArrayList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BrewAction {

   public final BrewItemKey ITEM_KEY;
   protected final boolean createsSplash;
   protected final AltarPower powerCost;
   protected final int forcedColor;
   protected final Probability baseProbability;
   protected final BrewNamePart namePart;
   private final ArrayList nullifiers;
   private final ArrayList priorNullifiers;
   private ModifierYield yieldModifier;


   protected BrewAction(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, boolean createsSplash) {
      this(itemKey, namePart, powerCost, baseProbability, createsSplash, -1);
   }

   protected BrewAction(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, boolean createsSplash, int forcedColor) {
      this.nullifiers = new ArrayList();
      this.priorNullifiers = new ArrayList();
      this.ITEM_KEY = itemKey;
      this.powerCost = powerCost;
      this.createsSplash = createsSplash;
      this.forcedColor = forcedColor;
      this.baseProbability = baseProbability;
      this.namePart = namePart;
   }

   public void setYieldModifier(ModifierYield counter) {
      this.yieldModifier = counter;
   }

   public void prepareYield(ModifierYield counter) {
      if(this.yieldModifier != null) {
         this.yieldModifier.applyTo(counter);
      }

   }

   public final BrewNamePart getNamePart() {
      return this.namePart;
   }

   public final void accumulatePower(AltarPower totalPower) {
      totalPower.accumulate(this.powerCost);
   }

   public final BrewAction addNullifier(BrewItemKey itemKey) {
      return this.addNullifier(itemKey, true);
   }

   public final BrewAction addNullifier(BrewItemKey itemKey, boolean onlyPrior) {
      if(onlyPrior) {
         this.priorNullifiers.add(itemKey);
      } else {
         this.nullifiers.add(itemKey);
      }

      return this;
   }

   public final boolean createsSplash() {
      return this.createsSplash;
   }

   public final int augmentColor(int color) {
      if(this.forcedColor == -1) {
         if(color == 0) {
            color = 17;
         }

         color = 37 * color + this.ITEM_KEY.hashCode();
         return color;
      } else {
         return this.forcedColor;
      }
   }

   public boolean removeWhenAddedToCauldron(World world) {
      return false;
   }

   public final void processNullifaction(ArrayList actionStack, NBTTagList nbtItems) {
      if(this.priorNullifiers.size() > 0 && actionStack.size() > 0 && this.priorNullifiers.contains(((BrewAction)actionStack.get(actionStack.size() - 1)).ITEM_KEY)) {
         actionStack.remove(actionStack.size() - 1);
         nbtItems.removeTag(actionStack.size() - 1);
      }

      if(this.nullifiers.size() > 0) {
         for(int i = actionStack.size() - 1; i >= 0; --i) {
            if(this.nullifiers.contains(((BrewAction)actionStack.get(i)).ITEM_KEY)) {
               actionStack.remove(i);
               nbtItems.removeTag(i);
            }
         }
      }

   }

   public boolean triggersRitual() {
      return false;
   }

   public boolean canAdd(BrewActionList actionList, boolean isCauldronFull, boolean hasEffects) {
      return true;
   }

   public boolean isRitualTargetLocationValid(MinecraftServer server, World world, int x, int y, int z, BlockPosition target, ModifiersRitual modifiers) {
      return true;
   }

   public abstract boolean augmentEffectLevels(EffectLevelCounter var1);

   public abstract void augmentEffectModifiers(ModifiersEffect var1);

   public abstract void prepareSplashPotion(World var1, BrewActionList var2, ModifiersImpact var3);

   public abstract void prepareRitual(World var1, int var2, int var3, int var4, ModifiersRitual var5, ItemStack var6);

   public abstract RitualStatus updateRitual(MinecraftServer var1, BrewActionList var2, World var3, int var4, int var5, int var6, ModifiersRitual var7, ModifiersImpact var8);

   public void applyRitualToEntity(World world, EntityLivingBase targetEntity, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
      this.applyToEntity(world, targetEntity, modifiers, stack);
   }

   public abstract void applyToEntity(World var1, EntityLivingBase var2, ModifiersEffect var3, ItemStack var4);

   public void applyRitualToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
      this.applyToBlock(world, x, y, z, side, radius, modifiers, stack);
   }

   public abstract void applyToBlock(World var1, int var2, int var3, int var4, ForgeDirection var5, int var6, ModifiersEffect var7, ItemStack var8);

   public int getDrinkSpeedModifiers() {
      return 0;
   }
}
