package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.util.KeyBindHelper;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class CreaturePowerSquid extends CreaturePower {

   public CreaturePowerSquid(int powerID) {
      super(powerID, EntitySquid.class);
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      if(mop != null && mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit != null && mop.entityHit instanceof EntityLivingBase) {
         EntityLivingBase targetPlayer = (EntityLivingBase)mop.entityHit;
         world.playSoundAtEntity(player, "random.fizz", 1.0F, 1.0F / (world.rand.nextFloat() * 0.4F + 1.2F) + 0.5F);
         targetPlayer.addPotionEffect(new PotionEffect(Potion.blindness.id, 200));
      }

   }

   public void onUpdate(World world, EntityPlayer player) {
      if(player.isInWater()) {
         Minecraft minecraft = Minecraft.getMinecraft();
         if(KeyBindHelper.isKeyBindDown(Minecraft.getMinecraft().gameSettings.keyBindForward)) {
            player.motionX *= 1.149999976158142D;
            player.motionZ *= 1.149999976158142D;
         }
      }

   }

   public void onDamage(World world, EntityPlayer player, LivingHurtEvent event) {
      if(event.source == DamageSource.drown) {
         int currentEnergy = Infusion.getCurrentEnergy(player);
         if(currentEnergy >= 1) {
            Infusion.setCurrentEnergy(player, currentEnergy - 1);
            SoundEffect.RANDOM_FIZZ.playAtPlayer(world, player);
            player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 1200, 1));
            player.setAir(300);
            event.setCanceled(true);
         }
      }

      super.onDamage(world, player, event);
   }
}
