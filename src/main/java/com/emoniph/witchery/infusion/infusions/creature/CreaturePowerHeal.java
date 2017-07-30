package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CreaturePowerHeal extends CreaturePower {

   public static final int DEFAULT_CHARGES_PER_SACRIFICE = 1;
   private final int charges;


   public CreaturePowerHeal(int powerID, Class creatureType, int charges) {
      super(powerID, creatureType);
      this.charges = charges;
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      player.addPotionEffect(new PotionEffect(Potion.heal.id, 10, 0));
      SoundEffect.RANDOM_FIZZ.playAtPlayer(world, player);
   }

   public int getChargesPerSacrifice() {
      return this.charges;
   }
}
