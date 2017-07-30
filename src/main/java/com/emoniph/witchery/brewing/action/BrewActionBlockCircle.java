package com.emoniph.witchery.brewing.action;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.util.BlockActionCircle;
import com.emoniph.witchery.util.Count;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BrewActionBlockCircle extends BrewActionEffect {

   public BrewActionBlockCircle(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, EffectLevel effectLevel) {
      super(itemKey, namePart, powerCost, new Probability(1.0D), effectLevel);
   }

   protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {}

   protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, final ModifiersEffect modifiers, ItemStack stack) {
      final Count counter = new Count();
      (new BlockActionCircle() {
         public void onBlock(World world, int x, int y, int z) {
            BrewActionBlockCircle.this.onCircleBlock(world, x, y, z, modifiers, counter);
         }
      }).processFilledCircle(world, x, y, z, radius + modifiers.getStrength());
   }

   protected abstract void onCircleBlock(World var1, int var2, int var3, int var4, ModifiersEffect var5, Count var6);
}
