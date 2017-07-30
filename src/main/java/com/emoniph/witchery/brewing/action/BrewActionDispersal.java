package com.emoniph.witchery.brewing.action;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.Dispersal;
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

public class BrewActionDispersal extends BrewAction {

   protected final Dispersal dispersal;


   public BrewActionDispersal(BrewItemKey itemKey, AltarPower powerCost, Dispersal dispersal) {
      super(itemKey, new BrewNamePart(dispersal.getUnlocalizedName(), BrewNamePart.Position.PREFIX), powerCost, Probability.CERTAIN, true);
      this.dispersal = dispersal;
   }

   public final void applyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {}

   public final void prepareSplashPotion(World world, BrewActionList actionList, ModifiersImpact modifiers) {
      modifiers.setGeneralDispersal(this.dispersal);
   }

   public final void applyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect effectModifiers, ItemStack stack) {}

   public final boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
      return true;
   }

   public final void augmentEffectModifiers(ModifiersEffect modifiers) {}

   public final void prepareRitual(World world, int x, int y, int z, ModifiersRitual modifiers, ItemStack stack) {}

   public final RitualStatus updateRitual(MinecraftServer server, BrewActionList actionList, World world, int x, int y, int z, ModifiersRitual modifiers, ModifiersImpact impactModifiers) {
      return RitualStatus.COMPLETE;
   }
}
