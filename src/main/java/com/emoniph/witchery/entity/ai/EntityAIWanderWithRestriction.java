package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.util.Coord;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIWanderWithRestriction extends EntityAIBase {

   private EntityCreature entity;
   private double xPosition;
   private double yPosition;
   private double zPosition;
   private double speed;
   private EntityAIWanderWithRestriction.IHomeLocationProvider home;


   public EntityAIWanderWithRestriction(EntityCreature creature, double speed, EntityAIWanderWithRestriction.IHomeLocationProvider home) {
      this.entity = creature;
      this.speed = speed;
      this.setMutexBits(1);
      this.home = home;
   }

   public boolean shouldExecute() {
      if(this.entity.getAge() >= 100) {
         return false;
      } else if(this.entity.getRNG().nextInt(120) != 0) {
         return false;
      } else {
         Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
         if(vec3 == null) {
            return false;
         } else if(Coord.distanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.home.getHomeX(), this.home.getHomeY(), this.home.getHomeZ()) > this.home.getHomeRange() * this.home.getHomeRange()) {
            return false;
         } else {
            this.xPosition = vec3.xCoord;
            this.yPosition = vec3.yCoord;
            this.zPosition = vec3.zCoord;
            return true;
         }
      }
   }

   public boolean continueExecuting() {
      return !this.entity.getNavigator().noPath();
   }

   public void startExecuting() {
      this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
   }

   public interface IHomeLocationProvider {

      double getHomeX();

      double getHomeY();

      double getHomeZ();

      double getHomeRange();
   }
}
