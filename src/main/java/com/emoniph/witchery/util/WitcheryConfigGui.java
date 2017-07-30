package com.emoniph.witchery.util;

import com.emoniph.witchery.Witchery;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

public class WitcheryConfigGui extends GuiConfig {

   public WitcheryConfigGui(GuiScreen parent) {
      super(parent, (new ConfigElement(Witchery.config.getCategory("general"))).getChildElements(), "witchery", false, false, GuiConfig.getAbridgedConfigPath(Witchery.config.toString()));
   }
}
