package com.emoniph.witchery.integration;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.integration.ModHook;
import net.minecraft.entity.EntityLivingBase;

public class ModHookTinkersConstruct extends ModHook {

   public String getModID() {
      return "TConstruct";
   }

   protected void doInit() {
      Witchery.modHooks.isTinkersPresent = true;
   }

   protected void doPostInit() {}

   protected void doReduceMagicPower(EntityLivingBase entity, float factor) {}
}
