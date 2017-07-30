package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.entity.EntityGoblin;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIPickUpBlocks extends EntityAIBase {

   protected final EntityGoblin entity;
   protected final double range;


   public EntityAIPickUpBlocks(EntityGoblin entity, double range) {
      this.entity = entity;
      this.range = range;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      return this.entity != null && !this.entity.isWorshipping() && this.entity.getHeldItem() == null && this.entity.getLeashed() && this.isItemInReachableDistance();
   }

   public void startExecuting() {
      AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(this.entity.posX - this.range, this.entity.posY - this.range, this.entity.posZ - this.range, this.entity.posX + this.range, this.entity.posY + this.range, this.entity.posZ + this.range);
      List items = this.entity.worldObj.getEntitiesWithinAABB(EntityItem.class, bb);
      double SPEED = 0.6D;
      Iterator i$ = items.iterator();

      while(i$.hasNext()) {
         EntityItem item = (EntityItem)i$.next();
         if(this.entity.getNavigator().tryMoveToEntityLiving(item, 0.6D)) {
            break;
         }
      }

   }

   public boolean continueExecuting() {
      return this.entity != null && !this.entity.isWorshipping() && this.entity.getHeldItem() == null && this.entity.getLeashed() && this.isItemInReachableDistance();
   }

   public void updateTask() {
      if(this.entity.getNavigator().noPath()) {
         AxisAlignedBB PICKUP_RANGE = AxisAlignedBB.getBoundingBox(this.entity.posX - this.range, this.entity.posY - this.range, this.entity.posZ - this.range, this.entity.posX + this.range, this.entity.posY + this.range, this.entity.posZ + this.range);
         List items = this.entity.worldObj.getEntitiesWithinAABB(EntityItem.class, PICKUP_RANGE);
         double bb = 0.6D;
         Iterator i$ = items.iterator();

         while(i$.hasNext()) {
            EntityItem item = (EntityItem)i$.next();
            if(this.entity.getNavigator().tryMoveToEntityLiving(item, 0.6D)) {
               break;
            }
         }
      } else {
         double PICKUP_RANGE1 = 1.5D;
         AxisAlignedBB bb1 = AxisAlignedBB.getBoundingBox(this.entity.posX - 1.5D, this.entity.posY - 1.5D, this.entity.posZ - 1.5D, this.entity.posX + 1.5D, this.entity.posY + 1.5D, this.entity.posZ + 1.5D);
         List items1 = this.entity.worldObj.getEntitiesWithinAABB(EntityItem.class, bb1);
         if(!items1.isEmpty()) {
            this.entity.setCurrentItemOrArmor(0, ((EntityItem)items1.get(0)).getEntityItem());
            if(!this.entity.worldObj.isRemote) {
               ((EntityItem)items1.get(0)).setDead();
            }
         }
      }

   }

   protected boolean isItemInReachableDistance() {
      AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(this.entity.posX - this.range, this.entity.posY - this.range, this.entity.posZ - this.range, this.entity.posX + this.range, this.entity.posY + this.range, this.entity.posZ + this.range);
      List items = this.entity.worldObj.getEntitiesWithinAABB(EntityItem.class, bb);
      double SPEED = 0.1D;
      Iterator i$ = items.iterator();

      EntityItem item;
      do {
         if(!i$.hasNext()) {
            return false;
         }

         item = (EntityItem)i$.next();
      } while(this.entity.getNavigator().getPathToEntityLiving(item) == null);

      return true;
   }
}
