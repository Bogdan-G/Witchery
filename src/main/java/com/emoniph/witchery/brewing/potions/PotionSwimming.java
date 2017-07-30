package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.KeyBindHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionSwimming extends PotionBase implements IHandleLivingUpdate {

   public PotionSwimming(int id, int color) {
      super(id, color);
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         if(world.isRemote && player.isInWater() && !player.isPotionActive(Witchery.Potions.PARALYSED)) {
            Minecraft minecraft = Minecraft.getMinecraft();
            if(KeyBindHelper.isKeyBindDown(Minecraft.getMinecraft().gameSettings.keyBindForward)) {
               byte amplifier1 = 3;
               player.motionX *= 1.15D + 0.03D * (double)amplifier1;
               player.motionZ *= 1.15D + 0.03D * (double)amplifier1;
            }
         }
      } else if(world.isRemote && world.getTotalWorldTime() % 10L == 3L && entity.isInWater() && entity.isPotionActive(Witchery.Potions.PARALYSED)) {
         entity.motionX *= 1.15D + 0.03D * (double)amplifier;
         entity.motionZ *= 1.15D + 0.03D * (double)amplifier;
      }

   }
}
