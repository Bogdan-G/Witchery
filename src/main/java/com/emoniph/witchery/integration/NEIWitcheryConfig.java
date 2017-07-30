package com.emoniph.witchery.integration;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.integration.NEICauldronRecipeHandler;
import com.emoniph.witchery.integration.NEIDistilleryRecipeHandler;
import com.emoniph.witchery.integration.NEIHighlightHandler;
import com.emoniph.witchery.integration.NEIKettleRecipeHandler;
import com.emoniph.witchery.integration.NEISpinningWheelRecipeHandler;
import com.emoniph.witchery.integration.NEIWitchesOvenRecipeHandler;
import com.emoniph.witchery.util.Config;
import cpw.mods.fml.common.Mod;
import net.minecraft.item.ItemStack;

public class NEIWitcheryConfig implements IConfigureNEI {

   public void loadConfig() {
      if(Config.instance().allowModIntegration && Config.instance().allowNotEnoughItems) {
         API.hideItem(new ItemStack(Witchery.Blocks.OVEN_BURNING));
         API.hideItem(new ItemStack(Witchery.Blocks.DISTILLERY_BURNING));
         API.hideItem(new ItemStack(Witchery.Blocks.BARRIER));
         API.hideItem(new ItemStack(Witchery.Blocks.FORCE));
         API.hideItem(new ItemStack(Witchery.Blocks.CIRCLE));
         API.hideItem(new ItemStack(Witchery.Blocks.GLYPH_RITUAL));
         API.hideItem(new ItemStack(Witchery.Blocks.GLYPH_INFERNAL));
         API.hideItem(new ItemStack(Witchery.Blocks.GLYPH_OTHERWHERE));
         API.hideItem(new ItemStack(Witchery.Blocks.CROP_BELLADONNA));
         API.hideItem(new ItemStack(Witchery.Blocks.CROP_MANDRAKE));
         API.hideItem(new ItemStack(Witchery.Blocks.CROP_ARTICHOKE));
         API.hideItem(new ItemStack(Witchery.Blocks.CROP_SNOWBELL));
         API.hideItem(new ItemStack(Witchery.Blocks.CROP_WORMWOOD));
         API.hideItem(new ItemStack(Witchery.Blocks.CROP_MINDRAKE));
         API.hideItem(new ItemStack(Witchery.Blocks.CHALICE));
         API.hideItem(new ItemStack(Witchery.Blocks.CANDELABRA));
         API.hideItem(new ItemStack(Witchery.Blocks.DREAM_CATCHER));
         API.hideItem(new ItemStack(Witchery.Blocks.DOOR_ALDER));
         API.hideItem(new ItemStack(Witchery.Blocks.DOOR_ROWAN));
         API.hideItem(new ItemStack(Witchery.Blocks.PERPETUAL_ICE_DOOR));
         API.hideItem(new ItemStack(Witchery.Blocks.GLOW_GLOBE));
         API.hideItem(new ItemStack(Witchery.Blocks.PLACED_ITEMSTACK));
         API.hideItem(new ItemStack(Witchery.Blocks.DEMON_HEART));
         API.hideItem(new ItemStack(Witchery.Blocks.FORCE));
         API.hideItem(new ItemStack(Witchery.Blocks.WEB));
         API.hideItem(new ItemStack(Witchery.Blocks.VINE));
         API.hideItem(new ItemStack(Witchery.Blocks.CACTUS));
         API.hideItem(new ItemStack(Witchery.Blocks.LILY));
         API.hideItem(new ItemStack(Witchery.Blocks.BREW_GAS));
         API.hideItem(new ItemStack(Witchery.Blocks.BREW_LIQUID));
         API.hideItem(new ItemStack(Witchery.Blocks.BREW));
         API.hideItem(new ItemStack(Witchery.Blocks.SLURP));
         API.hideItem(new ItemStack(Witchery.Items.BREW));
         API.hideItem(new ItemStack(Witchery.Items.BUCKET_BREW));
         API.hideItem(new ItemStack(Witchery.Blocks.CURSED_BUTTON_STONE));
         API.hideItem(new ItemStack(Witchery.Blocks.CURSED_BUTTON_WOOD));
         API.hideItem(new ItemStack(Witchery.Blocks.CURSED_LEVER));
         API.hideItem(new ItemStack(Witchery.Blocks.CURSED_SNOW_PRESSURE_PLATE));
         API.hideItem(new ItemStack(Witchery.Blocks.CURSED_STONE_PRESSURE_PLATE));
         API.hideItem(new ItemStack(Witchery.Blocks.CURSED_WOODEN_DOOR));
         API.hideItem(new ItemStack(Witchery.Blocks.CURSED_WOODEN_PRESSURE_PLATE));
         API.hideItem(new ItemStack(Witchery.Blocks.CROP_WOLFSBANE));
         API.hideItem(new ItemStack(Witchery.Blocks.CROP_GARLIC));
         API.hideItem(new ItemStack(Witchery.Blocks.WALLGEN));
         API.hideItem(new ItemStack(Witchery.Blocks.LIGHT));
         API.hideItem(new ItemStack(Witchery.Blocks.SHADED_GLASS_ON));
         API.hideItem(new ItemStack(Witchery.Blocks.MIRROR));
         API.hideItem(new ItemStack(Witchery.Blocks.MIRROR_UNBREAKABLE));
         API.registerRecipeHandler(new NEIWitchesOvenRecipeHandler());
         API.registerUsageHandler(new NEIWitchesOvenRecipeHandler());
         API.registerRecipeHandler(new NEIDistilleryRecipeHandler());
         API.registerUsageHandler(new NEIDistilleryRecipeHandler());
         API.registerRecipeHandler(new NEIKettleRecipeHandler());
         API.registerRecipeHandler(new NEICauldronRecipeHandler());
         API.registerUsageHandler(new NEICauldronRecipeHandler());
         API.registerRecipeHandler(new NEISpinningWheelRecipeHandler());
         API.registerUsageHandler(new NEISpinningWheelRecipeHandler());
         API.registerHighlightIdentifier(Witchery.Blocks.TRAPPED_PLANT, new NEIHighlightHandler(Witchery.Blocks.TRAPPED_PLANT));
         API.registerHighlightIdentifier(Witchery.Blocks.DOOR_ALDER, new NEIHighlightHandler(Witchery.Blocks.DOOR_ALDER));
         API.registerHighlightIdentifier(Witchery.Blocks.PIT_DIRT, new NEIHighlightHandler(Witchery.Blocks.PIT_DIRT));
         API.registerHighlightIdentifier(Witchery.Blocks.PIT_GRASS, new NEIHighlightHandler(Witchery.Blocks.PIT_GRASS));
      }

   }

   public String getName() {
      return ((Mod)Witchery.class.getAnnotation(Mod.class)).name();
   }

   public String getVersion() {
      return ((Mod)Witchery.class.getAnnotation(Mod.class)).version();
   }
}
