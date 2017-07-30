package com.emoniph.witchery.entity;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.world.World;

public class EntityDeathsHorse extends EntityHorse {

   public EntityDeathsHorse(World world) {
      super(world);
      super.experienceValue = 0;
   }
}
