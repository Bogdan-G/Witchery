package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityAITradePlayerGeneric extends EntityAIBase {

   private IMerchant merchant;
   private EntityLiving entity;


   public EntityAITradePlayerGeneric(IMerchant merchant, EntityLiving entity) {
      this.merchant = merchant;
      this.entity = entity;
      this.setMutexBits(5);
   }

   public boolean shouldExecute() {
      if(!this.entity.isEntityAlive()) {
         return false;
      } else if(this.entity.isInWater()) {
         return false;
      } else if(!this.entity.onGround) {
         return false;
      } else if(this.entity.velocityChanged) {
         return false;
      } else {
         EntityPlayer entityplayer = this.merchant.getCustomer();
         return entityplayer == null?false:(this.entity.getDistanceSqToEntity(entityplayer) > 16.0D?false:entityplayer.openContainer instanceof Container);
      }
   }

   public void startExecuting() {
      this.entity.getNavigator().clearPathEntity();
   }

   public void resetTask() {
      this.merchant.setCustomer((EntityPlayer)null);
   }
}
