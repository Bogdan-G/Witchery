package com.emoniph.witchery.util;

import net.minecraft.world.World;

public interface ISpiralBlockAction {

   void onSpiralActionStart(World var1, int var2, int var3, int var4);

   boolean onSpiralBlockAction(World var1, int var2, int var3, int var4);

   void onSpiralActionStop(World var1, int var2, int var3, int var4);
}
