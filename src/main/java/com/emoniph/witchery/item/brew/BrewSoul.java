package com.emoniph.witchery.item.brew;

import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.item.ItemGeneral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class BrewSoul extends ItemGeneral.Drinkable {

   private final SymbolEffect effect;


   public BrewSoul(int damageValue, String unlocalizedName, SymbolEffect effect) {
      super(damageValue, unlocalizedName, 1, new PotionEffect[0]);
      this.effect = effect;
      this.setPotion(true);
   }

   public void onDrunk(World world, EntityPlayer player, ItemStack itemstack) {
      this.effect.acquireKnowledge(player);
   }
}
