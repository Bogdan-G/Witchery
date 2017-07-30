package com.emoniph.witchery.util;

import com.emoniph.witchery.util.WitcheryConfigGui;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
import cpw.mods.fml.client.IModGuiFactory.RuntimeOptionGuiHandler;
import java.util.Set;
import net.minecraft.client.Minecraft;

public class WitcheryGuiFactory implements IModGuiFactory {

   public void initialize(Minecraft minecraftInstance) {}

   public Class mainConfigGuiClass() {
      return WitcheryConfigGui.class;
   }

   public Set runtimeGuiCategories() {
      return null;
   }

   public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
      return null;
   }
}
