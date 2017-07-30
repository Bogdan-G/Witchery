package com.emoniph.witchery.common;

import net.minecraft.world.World;

public interface INullSource {

   World getWorld();

   int getPosX();

   int getPosY();

   int getPosZ();

   float getRange();

   boolean isPowerInvalid();
}
