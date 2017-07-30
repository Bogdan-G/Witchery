package com.emoniph.witchery.integration;

import com.emoniph.witchery.integration.ModHook;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.entity.EntityLivingBase;

public class ModHookWaila extends ModHook {

   public String getModID() {
      return "Waila";
   }

   protected void doInit() {
      FMLInterModComms.sendMessage(this.getModID(), "register", "com.emoniph.witchery.integration.ModHookWailaRegistrar.callbackRegister");
   }

   protected void doPostInit() {}

   protected void doReduceMagicPower(EntityLivingBase entity, float factor) {}
}
