package com.emoniph.witchery.integration;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.integration.ModHook;
import com.emoniph.witchery.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ModHookManager {

   private ArrayList hooks = new ArrayList();
   public boolean isTinkersPresent;
   public boolean isAM2Present;
   public boolean isMorphPresent;


   public void register(Class clazz) {
      try {
         ModHook e = (ModHook)clazz.newInstance();
         this.hooks.add(e);
      } catch (Throwable var3) {
         Log.instance().warning(var3, "unhandled exception loading ModHook");
      }

   }

   public void init() {
      Iterator i$ = this.hooks.iterator();

      while(i$.hasNext()) {
         ModHook hook = (ModHook)i$.next();

         try {
            hook.init();
         } catch (Throwable var4) {
            Log.instance().warning(var4, String.format("unhandled exception init for hook %s", new Object[]{hook.getModID()}));
         }
      }

   }

   public void postInit() {
      Iterator i$ = this.hooks.iterator();

      while(i$.hasNext()) {
         ModHook hook = (ModHook)i$.next();

         try {
            hook.postInit();
         } catch (Throwable var4) {
            Log.instance().warning(var4, String.format("unhandled exception post init for hook %s", new Object[]{hook.getModID()}));
         }
      }

   }

   public void reducePowerLevels(EntityLivingBase entity, float reduction) {
      if(entity != null && entity.worldObj != null && !entity.worldObj.isRemote) {
         if(entity instanceof EntityPlayer) {
            EntityPlayer i$ = (EntityPlayer)entity;
            int hook = Infusion.getMaxEnergy(i$);
            int e = Infusion.getCurrentEnergy(i$);
            if(hook > 0 && e > 0) {
               int reduceBy = Math.max((int)((float)hook * reduction), 1);
               int newMana = Math.max(e - reduceBy, 0);
               Infusion.setCurrentEnergy(i$, newMana);
            }
         }

         Iterator i$1 = this.hooks.iterator();

         while(i$1.hasNext()) {
            ModHook hook1 = (ModHook)i$1.next();

            try {
               hook1.reduceMagicPower(entity, reduction);
            } catch (Throwable var8) {
               Log.instance().warning(var8, String.format("unhandled exception post init for hook %s", new Object[]{hook1.getModID()}));
            }
         }

      }
   }

   public void boostBloodPowers(EntityPlayer player, float health) {
      Iterator i$ = this.hooks.iterator();

      while(i$.hasNext()) {
         ModHook hook = (ModHook)i$.next();

         try {
            hook.boostBloodPowers(player, health);
         } catch (Throwable var6) {
            Log.instance().warning(var6, String.format("unhandled exception post init for hook %s", new Object[]{hook.getModID()}));
         }
      }

   }

   public boolean canVampireBeKilled(EntityPlayer player) {
      Iterator i$ = this.hooks.iterator();

      while(i$.hasNext()) {
         ModHook hook = (ModHook)i$.next();

         try {
            if(hook.canVampireBeKilled(player)) {
               return true;
            }
         } catch (Throwable var5) {
            Log.instance().warning(var5, String.format("unhandled exception post init for hook %s", new Object[]{hook.getModID()}));
         }
      }

      return false;
   }

   public void makeItemModProof(ItemStack stack) {
      Iterator i$ = this.hooks.iterator();

      while(i$.hasNext()) {
         ModHook hook = (ModHook)i$.next();

         try {
            hook.tryMakeItemModProof(stack);
         } catch (Throwable var5) {
            Log.instance().warning(var5, String.format("unhandled exception post init for hook %s", new Object[]{hook.getModID()}));
         }
      }

   }
}
