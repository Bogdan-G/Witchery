package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.Witchery;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;

public class EntityAIAttackCloseTargetOnCollide extends EntityAIAttackOnCollide {

   EntityCreature field_75441_b;
   Class field_75444_h;
   double maxDistance;


   public EntityAIAttackCloseTargetOnCollide(EntityCreature par1EntityLiving, Class par2Class, double par3, boolean par4, double maxDistance) {
      this(par1EntityLiving, par3, par4, maxDistance);
      this.field_75444_h = par2Class;
   }

   public EntityAIAttackCloseTargetOnCollide(EntityCreature par1EntityLiving, double par2, boolean par3, double maxDistance) {
      super(par1EntityLiving, par2, par3);
      this.field_75441_b = par1EntityLiving;
      this.maxDistance = maxDistance;
   }

   public boolean shouldExecute() {
      boolean execute = super.shouldExecute();
      if(execute && !this.isTargetNearby()) {
         execute = false;
      }

      return execute;
   }

   protected boolean isTargetNearby() {
      EntityLivingBase entityTarget = this.field_75441_b != null?this.field_75441_b.getAttackTarget():null;
      return entityTarget != null && this.field_75441_b.getDistanceSqToEntity(entityTarget) <= this.maxDistance * this.maxDistance && this.field_75441_b.getNavigator().getPathToEntityLiving(entityTarget) != null && (entityTarget.getHeldItem() == null || entityTarget.getHeldItem().getItem() != Witchery.Items.DEVILS_TONGUE_CHARM);
   }

   public boolean continueExecuting() {
      boolean execute = super.continueExecuting();
      if(execute && !this.isTargetNearby()) {
         execute = false;
      }

      return execute;
   }
}
