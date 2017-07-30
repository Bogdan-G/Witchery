package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingDeath;
import com.emoniph.witchery.brewing.potions.IHandlePlayerDrops;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.common.ExtendedPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class PotionKeepInventory extends PotionBase implements IHandlePlayerDrops, IHandleLivingDeath {

   public PotionKeepInventory(int id, int color) {
      super(id, color);
   }

   public void postContructInitialize() {}

   public void onPlayerDrops(World world, EntityPlayer player, PlayerDropsEvent event, int amplifier) {
      if(!event.entityPlayer.worldObj.isRemote) {
         if(event.entityPlayer.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            return;
         }

         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if(playerEx != null) {
            playerEx.cachePlayerInventory();
         }

         event.setCanceled(true);
      }

   }

   public void onLivingDeath(World world, EntityLivingBase entity, LivingDeathEvent event, int amplifier) {
      if(!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.entityLiving;
         if(player.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            return;
         }

         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if(playerEx != null) {
            playerEx.backupPlayerInventory();
         }
      }

   }
}
