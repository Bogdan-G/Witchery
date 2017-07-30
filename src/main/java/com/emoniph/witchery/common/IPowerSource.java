package com.emoniph.witchery.common;

import com.emoniph.witchery.util.Coord;
import net.minecraft.world.World;

public interface IPowerSource {

   World getWorld();

   Coord getLocation();

   boolean isLocationEqual(Coord var1);

   boolean consumePower(float var1);

   float getCurrentPower();

   float getRange();

   int getEnhancementLevel();

   boolean isPowerInvalid();
}
