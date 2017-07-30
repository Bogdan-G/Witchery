package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.entity.EntityTreefyd;
import net.minecraft.entity.ai.EntityAIWander;

public class EntityAITreefydWander extends EntityAIWander {

   private final EntityTreefyd treefyd;


   public EntityAITreefydWander(EntityTreefyd treefyd, double speed) {
      super(treefyd, speed);
      this.treefyd = treefyd;
   }

   public boolean shouldExecute() {
      return !this.treefyd.isSentinal() && super.shouldExecute();
   }

   public boolean continueExecuting() {
      return !this.treefyd.isSentinal() && super.continueExecuting();
   }
}
