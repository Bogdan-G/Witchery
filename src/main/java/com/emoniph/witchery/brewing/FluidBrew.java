package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidBrew extends Fluid {

   public FluidBrew(String fluidName) {
      super(fluidName);
   }

   public int getColor(FluidStack stack) {
      int color = WitcheryBrewRegistry.INSTANCE.getBrewColor(stack.tag);
      return color;
   }
}
