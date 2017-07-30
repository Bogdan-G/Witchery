package com.emoniph.witchery.integration;

import am2.api.ArsMagicaApi;
import am2.api.IExtendedProperties;
import am2.api.enchantment.IAMEnchantmentHelper;
import am2.api.events.ReconstructorRepairEvent;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.integration.ModHook;
import com.emoniph.witchery.item.ItemChalk;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class ModHookArsMagica2 extends ModHook {

   public String getModID() {
      return "arsmagica2";
   }

   protected void doInit() {}

   protected void doPostInit() {
      Witchery.modHooks.isAM2Present = true;
      MinecraftForge.EVENT_BUS.register(new ModHookArsMagica2.EventHooks());
   }

   protected void doReduceMagicPower(EntityLivingBase entity, float factor) {
      ModHookArsMagica2.IntegrateAM2.doReduceMagicPower(entity, factor);
   }

   protected void makeItemModProof(ItemStack stack) {
      ModHookArsMagica2.IntegrateAM2.makeItemModProof(stack);
   }

   public static class EventHooks {

      @SubscribeEvent
      public void onReconstructorRepair(ReconstructorRepairEvent event) {
         if(event.item != null && !event.isCanceled()) {
            Item item = event.item.getItem();
            if(item == Witchery.Items.POPPET || item instanceof ItemChalk) {
               event.setCanceled(true);
            }
         }

      }
   }

   private static class IntegrateAM2 {

      public static void doReduceMagicPower(EntityLivingBase entity, float factor) {
         IExtendedProperties props = ArsMagicaApi.instance.getExtendedProperties(entity);
         if(props != null) {
            float maxMana = props.getMaxMana();
            float mana = props.getCurrentMana();
            if(maxMana > 0.0F && mana > 0.0F) {
               float reduction = Math.max(maxMana * factor, 1.0F);
               float newMana = Math.max(mana - reduction, 0.0F);
               props.setCurrentMana(newMana);
            }
         }

      }

      public static void makeItemModProof(ItemStack stack) {
         if(stack.isItemEnchantable() && ArsMagicaApi.instance != null) {
            IAMEnchantmentHelper helper = ArsMagicaApi.instance.getEnchantHelper();
            if(helper != null) {
               int id = helper.getSoulboundID();
               if(id >= 0 && id < Enchantment.enchantmentsList.length) {
                  stack.addEnchantment(Enchantment.enchantmentsList[id], 1);
               }
            }
         }

      }
   }
}
