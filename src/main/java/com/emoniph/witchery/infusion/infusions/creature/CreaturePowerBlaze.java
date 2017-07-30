package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class CreaturePowerBlaze extends CreaturePower {

   public CreaturePowerBlaze(int powerID) {
      super(powerID, EntityBlaze.class);
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      if(!world.isRemote) {
         world.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)player.posX, (int)player.posY, (int)player.posZ, 0);
         boolean BALLS = true;

         for(int i = 0; i < 3; ++i) {
            float f = 1.0F;
            double motionX = (double)(-MathHelper.sin(player.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(player.rotationPitch / 180.0F * 3.1415927F) * f);
            double motionZ = (double)(MathHelper.cos(player.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(player.rotationPitch / 180.0F * 3.1415927F) * f);
            double motionY = (double)(-MathHelper.sin(player.rotationPitch / 180.0F * 3.1415927F) * f);
            EntitySmallFireball fireball = new EntitySmallFireball(world, player, motionX, motionY, motionZ);
            fireball.setLocationAndAngles(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ, fireball.rotationYaw, fireball.rotationPitch);
            fireball.setPosition(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
            motionX += world.rand.nextDouble() * 0.2D;
            motionZ += world.rand.nextDouble() * 0.2D;
            double d6 = (double)MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
            fireball.accelerationX = motionX / d6 * 0.1D;
            fireball.accelerationY = motionY / d6 * 0.1D;
            fireball.accelerationZ = motionZ / d6 * 0.1D;
            double d8 = 1.0D;
            Vec3 vec3 = player.getLook(1.0F);
            fireball.posX = player.posX + vec3.xCoord * d8;
            fireball.posY = player.posY + (double)(player.height / 2.0F) + 0.5D;
            fireball.posZ = player.posZ + vec3.zCoord * d8;
            world.spawnEntityInWorld(fireball);
         }
      }

   }

   public void onDamage(World world, EntityPlayer player, LivingHurtEvent event) {
      if(event.source.isFireDamage() && event.isCancelable()) {
         int currentEnergy = Infusion.getCurrentEnergy(player);
         if(currentEnergy >= 3 && !player.isPotionActive(Potion.fireResistance.id)) {
            Infusion.setCurrentEnergy(player, currentEnergy - 3);
            player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 200, 0));
            SoundEffect.RANDOM_FIZZ.playAtPlayer(world, player);
         }
      }

   }
}
