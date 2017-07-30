package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class CreaturePowerCreeper extends CreaturePower {

   private static final float WEB_DAMAGE = 1.0F;
   private final int explosionRadius = 3;


   public CreaturePowerCreeper(int powerID) {
      super(powerID, EntityCreeper.class);
   }

   public int activateCost(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      int baseCost = super.activateCost(world, player, elapsedTicks, mop);
      return elapsedTicks >= 60?baseCost * 2:baseCost;
   }

   public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
      if(!world.isRemote) {
         double var10002 = player.posX;
         double var10003 = player.posY;
         double var10004 = player.posZ;
         float var10005;
         if(elapsedTicks >= 60) {
            this.getClass();
            var10005 = 3.0F * 2.0F;
         } else {
            this.getClass();
            var10005 = 3.0F;
         }

         world.createExplosion(player, var10002, var10003, var10004, var10005, world.getGameRules().getGameRuleBooleanValue("mobGriefing"));
      }

   }

   public void onDamage(World world, EntityPlayer player, LivingHurtEvent event) {
      if(event.source.isFireDamage()) {
         StackTraceElement[] stack = Thread.currentThread().getStackTrace();
         StackTraceElement[] arr$ = stack;
         int len$ = stack.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            StackTraceElement element = arr$[i$];
            if(element.getMethodName().equals("onStruckByLightning")) {
               int power = Infusion.getNBT(player).getInteger("witcheryInfusionCharges");
               Infusion.getNBT(player).setInteger("witcheryInfusionCharges", Math.min(power + 25, 200));
               Infusion.syncPlayer(world, player);
               if(event.isCancelable()) {
                  event.setCanceled(true);
               }

               return;
            }
         }
      }

   }
}
