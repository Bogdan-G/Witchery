package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveIndoors;

public class EntityAIMoveIndoorsLeashAware extends EntityAIMoveIndoors {

   private EntityCreature creature;


   public EntityAIMoveIndoorsLeashAware(EntityCreature creature) {
      super(creature);
      this.creature = creature;
   }

   public boolean shouldExecute() {
      return this.creature != null && !this.creature.getLeashed() && super.shouldExecute();
   }
}
