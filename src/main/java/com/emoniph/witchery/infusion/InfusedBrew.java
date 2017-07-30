package com.emoniph.witchery.infusion;

import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.emoniph.witchery.item.ItemGeneral;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class InfusedBrew extends ItemGeneral.Drinkable {

   private final InfusedBrewEffect effect;


   public InfusedBrew(int damageValue, String unlocalizedName, InfusedBrewEffect effect) {
      super(damageValue, unlocalizedName, 2, new PotionEffect[0]);
      this.effect = effect;
      super.potion = true;
   }

   public void onDrunk(World world, EntityPlayer player, ItemStack itemstack) {
      this.effect.drunk(world, player, itemstack);
   }

   public boolean isInfused() {
      return true;
   }
}
