package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.util.KeyBindHelper;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CreaturePowerSpeed extends CreaturePower {

   public CreaturePowerSpeed(int powerID, Class creatureType) {
      super(powerID, creatureType);
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 400, 3));
      SoundEffect.RANDOM_FIZZ.playAtPlayer(world, player);
   }

   public void onUpdate(World world, EntityPlayer player) {
      Minecraft minecraft = Minecraft.getMinecraft();
      int var5 = MathHelper.floor_double(player.posX);
      int var6 = MathHelper.floor_double(player.posY - 2.0D);
      int var7 = MathHelper.floor_double(player.posZ);
      if(KeyBindHelper.isKeyBindDown(minecraft.gameSettings.keyBindForward) || KeyBindHelper.isKeyBindDown(minecraft.gameSettings.keyBindBack) || KeyBindHelper.isKeyBindDown(minecraft.gameSettings.keyBindLeft) || KeyBindHelper.isKeyBindDown(minecraft.gameSettings.keyBindRight)) {
         if(world.getBlock(var5, var6, var7) != Blocks.ice) {
            if(player.onGround && !player.isInWater()) {
               player.motionX *= 1.4500000476837158D;
               player.motionZ *= 1.4500000476837158D;
            }
         } else {
            player.motionX *= 1.100000023841858D;
            player.motionZ *= 1.100000023841858D;
         }
      }

   }
}
