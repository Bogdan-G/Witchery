package com.emoniph.witchery.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePlayerGeneric extends EntityAIWatchClosest {

   private final IMerchant merchant;


   public EntityAILookAtTradePlayerGeneric(EntityLiving entity, IMerchant merchant) {
      super(entity, EntityPlayer.class, 8.0F);
      this.merchant = merchant;
   }

   public boolean shouldExecute() {
      EntityPlayer customer = this.merchant.getCustomer();
      if(customer != null) {
         super.closestEntity = customer;
         return true;
      } else {
         return false;
      }
   }
}
