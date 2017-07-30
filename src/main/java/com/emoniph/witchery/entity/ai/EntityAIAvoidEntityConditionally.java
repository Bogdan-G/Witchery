package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;

public class EntityAIAvoidEntityConditionally extends EntityAIAvoidEntity {

   private final EntityAIAvoidEntityConditionally.IAvoidEntities condition;


   public EntityAIAvoidEntityConditionally(EntityCreature entity, Class targetClass, float distanceFromEntity, double farSpeed, double nearSpeed, EntityAIAvoidEntityConditionally.IAvoidEntities condition) {
      super(entity, targetClass, distanceFromEntity, farSpeed, nearSpeed);
      this.condition = condition;
   }

   public boolean shouldExecute() {
      return super.shouldExecute() && !this.condition.shouldAvoid();
   }

   public interface IAvoidEntities {

      boolean shouldAvoid();
   }
}
