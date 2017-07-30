package com.emoniph.witchery.integration;

import baubles.api.BaublesApi;
import com.emoniph.witchery.integration.ModHook;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class ModHookBaubles extends ModHook {

   private static final String[] BANNED_ITEMS = new String[]{"item.superLavaPendant", "item.lavaPendant", "item.odinRing", "item.aesirRing"};


   public String getModID() {
      return "Baubles";
   }

   protected void doInit() {}

   protected void doPostInit() {}

   protected void doReduceMagicPower(EntityLivingBase entity, float factor) {}

   public boolean canVampireBeKilled(EntityPlayer player) {
      return ModHookBaubles.IntegrateBaubles.canVampireBeVilled(player);
   }


   private static class IntegrateBaubles {

      public static boolean canVampireBeVilled(EntityPlayer player) {
         IInventory inv = BaublesApi.getBaubles(player);
         if(inv == null) {
            return false;
         } else {
            for(int slot = 0; slot < inv.getSizeInventory(); ++slot) {
               ItemStack stack = inv.getStackInSlot(slot);
               if(stack != null) {
                  String[] arr$ = ModHookBaubles.BANNED_ITEMS;
                  int len$ = arr$.length;

                  for(int i$ = 0; i$ < len$; ++i$) {
                     String badItem = arr$[i$];
                     if(badItem.equals(stack.getUnlocalizedName())) {
                        return true;
                     }
                  }
               }
            }

            return false;
         }
      }
   }
}
