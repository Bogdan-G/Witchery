package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;

public class EntityAIAttackOnCollide2 extends EntityAIAttackOnCollide {

   private Class clazz;


   public EntityAIAttackOnCollide2(EntityCreature creature, Class classToAttack, double speedTowardsTarget, boolean longMemory) {
      super(creature, classToAttack, speedTowardsTarget, longMemory);
      this.clazz = classToAttack;
   }

   public boolean appliesToClass(Class victimClass) {
      return victimClass == this.clazz;
   }
}
