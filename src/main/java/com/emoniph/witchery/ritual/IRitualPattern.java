package com.emoniph.witchery.ritual;

import net.minecraft.world.World;

public interface IRitualPattern {
    boolean isMatch(World world, int posX, int posY, int posZ);
    int getRadius();
}
