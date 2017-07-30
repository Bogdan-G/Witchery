package com.emoniph.witchery.integration;

import WayofTime.alchemicalWizardry.api.event.SacrificeKnifeUsedEvent;
import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.integration.ModHook;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Log;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class ModHookBloodMagic extends ModHook {

   public String getModID() {
      return "AWWayofTime";
   }

   protected void doInit() {}

   protected void doPostInit() {
      try {
         MinecraftForge.EVENT_BUS.register(new ModHookBloodMagic.EventHooks());
      } catch (Throwable var2) {
         Log.instance().debug(String.format("Tried and failed to install hooks for Blood Magic dagger event. %s", new Object[]{var2.toString()}));
      }

   }

   protected void doReduceMagicPower(EntityLivingBase entity, float factor) {
      ModHookBloodMagic.IntegrateBloodMagic.reduceMagicPower(entity, factor);
   }

   public void boostBloodPowers(EntityPlayer player, float health) {
      ModHookBloodMagic.IntegrateBloodMagic.boostBloodPowers(player, health);
   }

   private static class IntegrateBloodMagic {

      public static void reduceMagicPower(EntityLivingBase entity, float factor) {
         if(entity != null && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            int essence = SoulNetworkHandler.getCurrentEssence(player.getCommandSenderName());
            if(Config.instance().isDebugging()) {
               Log.instance().debug(String.format("reduceMagicPower for Blood Magic (%s lp=%d)", new Object[]{player.getCommandSenderName(), Integer.valueOf(essence)}));
            }

            float reduction;
            if(essence <= 5000) {
               reduction = 5000.0F * factor;
            } else if(essence <= 25000) {
               reduction = 25000.0F * factor;
            } else if(essence <= 150000) {
               reduction = 150000.0F * factor;
            } else if(essence <= 1000000) {
               reduction = 1000000.0F * factor;
            } else if(essence <= 10000000) {
               reduction = 1.0E7F * factor;
            } else {
               reduction = 2.14748365E9F * factor;
            }

            reduction = Math.max(reduction, 1.0F);
            int newEssence = Math.max((int)((float)essence - reduction), 0);
            SoulNetworkHandler.setCurrentEssence(player.getCommandSenderName(), newEssence);
         }

      }

      public static void boostBloodPowers(EntityPlayer player, float health) {
         boolean LP_PER_LIFE = true;
         String playerName = player.getCommandSenderName();
         int newlevel = SoulNetworkHandler.getCurrentEssence(playerName) + (int)health * 100;
         SoulNetworkHandler.setCurrentEssence(playerName, newlevel);
      }
   }

   public static class EventHooks {

      @SubscribeEvent
      public void onSacrificeKnifeUsed(SacrificeKnifeUsedEvent event) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(event.player);
         if(playerEx != null && playerEx.isVampire()) {
            event.shouldDrainHealth = false;
            if(!event.player.worldObj.isRemote && !playerEx.decreaseBloodPower(event.healthDrained * 100, true)) {
               event.shouldFillAltar = false;
            }
         }

      }
   }
}
