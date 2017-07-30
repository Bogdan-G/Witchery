package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CreaturePowerZombie extends CreaturePower {

   public CreaturePowerZombie(int powerID) {
      super(powerID, EntityZombie.class);
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      if(!world.isRemote) {
         player.addPotionEffect(new PotionEffect(Potion.resistance.id, 600, 1));
         player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 600, 0));
      }

   }
}
