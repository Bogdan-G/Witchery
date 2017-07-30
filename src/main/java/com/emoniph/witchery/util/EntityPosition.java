package com.emoniph.witchery.util;

import com.emoniph.witchery.util.BlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class EntityPosition {

   public final double x;
   public final double y;
   public final double z;


   public EntityPosition(int x, int y, int z) {
      this(0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z);
   }

   public EntityPosition(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public EntityPosition(BlockPosition position) {
      this.x = (double)position.x;
      this.y = (double)position.y;
      this.z = (double)position.z;
   }

   public EntityPosition(Entity entity) {
      this(entity.posX, entity.posY, entity.posZ);
   }

   public EntityPosition(MovingObjectPosition mop) {
      if(mop.typeOfHit == MovingObjectType.ENTITY) {
         this.x = mop.entityHit.posX;
         this.y = mop.entityHit.posY;
         this.z = mop.entityHit.posZ;
      } else if(mop.typeOfHit == MovingObjectType.BLOCK) {
         this.x = (double)mop.blockX + 0.5D;
         this.y = (double)mop.blockY + 0.5D;
         this.z = (double)mop.blockZ + 0.5D;
      } else {
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 0.0D;
      }

   }

   public double getDistanceSqToEntity(Entity entity) {
      double d0 = this.x - entity.posX;
      double d1 = this.y - entity.posY;
      double d2 = this.z - entity.posZ;
      return d0 * d0 + d1 * d1 + d2 * d2;
   }

   public AxisAlignedBB getBounds(double radius) {
      AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(this.x - radius, this.y - radius, this.z - radius, this.x + radius, this.y + radius, this.z + radius);
      return aabb;
   }

   public boolean occupiedBy(Entity entity) {
      return entity != null && entity.posX == this.x && entity.posY == this.y && entity.posZ == this.z;
   }
}
