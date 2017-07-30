package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class CreaturePowerSkeleton extends CreaturePower {

   public CreaturePowerSkeleton(int powerID) {
      super(powerID, EntitySkeleton.class);
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      if(!world.isRemote) {
         float f = (float)elapsedTicks / 20.0F;
         f = (f * f + f * 2.0F) / 3.0F;
         if(f > 1.0F) {
            f = 1.0F;
         }

         world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
         EntityArrow entityarrow = new EntityArrow(world, player, f * 2.0F);
         if(f == 1.0F) {
            entityarrow.setIsCritical(true);
         }

         boolean EXTRA_PUNCH = true;
         entityarrow.setKnockbackStrength(1);
         boolean EXTRA_DAMAGE = true;
         entityarrow.setDamage(entityarrow.getDamage() + 0.5D + 0.5D);
         world.spawnEntityInWorld(entityarrow);
      }

   }

   public void onDamage(World world, EntityPlayer player, LivingHurtEvent event) {
      if(!world.isRemote && event.source == DamageSource.drown) {
         int currentEnergy = Infusion.getCurrentEnergy(player);
         if(currentEnergy >= 5) {
            Infusion.setCurrentEnergy(player, currentEnergy - 5);
            SoundEffect.RANDOM_FIZZ.playAtPlayer(world, player);
            player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 600, 0));
            player.setAir(30);
            event.setCanceled(true);
         }
      }

      super.onDamage(world, player, event);
   }
}
