package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.util.KeyBindHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class CreaturePowerBat extends CreaturePower {

   public CreaturePowerBat(int powerID, Class creatureType) {
      super(powerID, creatureType);
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 400));
   }

   public void onUpdate(World world, EntityPlayer player) {
      Minecraft minecraft = Minecraft.getMinecraft();
      if(KeyBindHelper.isKeyBindDown(minecraft.gameSettings.keyBindJump) && player.motionY > 0.0D) {
         player.motionY += 0.06699999910593032D;
      }

      if(!player.onGround && KeyBindHelper.isKeyBindDown(minecraft.gameSettings.keyBindJump)) {
         player.motionY = 0.41999998688697815D;
      }

   }

   public void onFalling(World worldObj, EntityPlayer player, LivingFallEvent event) {
      if(event.distance > 5.0F) {
         event.distance = 5.0F;
      }

   }
}
