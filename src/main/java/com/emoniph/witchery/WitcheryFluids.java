package com.emoniph.witchery;

import com.emoniph.witchery.brewing.FluidBrew;
import com.emoniph.witchery.item.ItemBucketSpirit;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;

public final class WitcheryFluids {

   public final Fluid FLOWING_SPIRIT = register(new Fluid("witchery:fluidspirit")).setDensity(500).setViscosity(2000);
   public final Fluid HOLLOW_TEARS = register(new Fluid("witchery:hollowtears")).setDensity(100).setViscosity(1500);
   public final Fluid BREW = register(new FluidBrew("witchery:brew")).setDensity(100).setViscosity(1500);
   public final Fluid BREW_GAS = register(new Fluid("witchery:brewgas")).setGaseous(true);
   public final Fluid BREW_LIQUID = register(new Fluid("witchery:brewliquid"));
   public final Fluid DISEASE = register(new Fluid("witchery:fluiddisease")).setDensity(50).setViscosity(3000);


   private static Fluid register(Fluid fluid) {
      FluidRegistry.registerFluid(fluid);
      return fluid;
   }

   public static ItemBucketSpirit bind(ItemBucketSpirit bucket, Fluid fluid, Block block) {
      fluid.setBlock(block);
      bucket.setFluidBlock(block);
      bind(bucket, fluid, 1000);
      return bucket;
   }

   public static Item bind(Item bucket, Fluid fluid, int quantity) {
      FluidContainerRegistry.registerFluidContainer(new FluidContainerData(new FluidStack(fluid, quantity), new ItemStack(bucket), new ItemStack(Items.bucket)));
      return bucket;
   }
}
