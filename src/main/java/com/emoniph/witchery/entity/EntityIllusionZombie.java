package com.emoniph.witchery.entity;

import com.emoniph.witchery.entity.EntityIllusion;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.world.World;

public class EntityIllusionZombie extends EntityIllusion {

   public EntityIllusionZombie(World world) {
      super(world);
   }

   protected SoundEffect getFakeLivingSound() {
      return SoundEffect.MOB_ZOMBIE_SAY;
   }
}
