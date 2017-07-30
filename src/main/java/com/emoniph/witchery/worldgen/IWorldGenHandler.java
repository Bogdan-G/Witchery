package com.emoniph.witchery.worldgen;

import java.util.Random;
import net.minecraft.world.World;

public interface IWorldGenHandler {

   boolean generate(World var1, Random var2, int var3, int var4);

   void initiate();

   int getExtentX();

   int getExtentZ();

   int getRange();
}
