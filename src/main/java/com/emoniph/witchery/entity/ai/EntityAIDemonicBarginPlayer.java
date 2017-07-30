package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.entity.EntityDemon;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityAIDemonicBarginPlayer extends EntityAIBase {

   private EntityDemon trader;


   public EntityAIDemonicBarginPlayer(EntityDemon trader) {
      this.trader = trader;
      this.setMutexBits(5);
   }

   public boolean shouldExecute() {
      if(!this.trader.isEntityAlive()) {
         return false;
      } else if(this.trader.isInWater()) {
         return false;
      } else if(!this.trader.onGround) {
         return false;
      } else if(this.trader.velocityChanged) {
         return false;
      } else {
         EntityPlayer entityplayer = this.trader.getCustomer();
         return entityplayer == null?false:(this.trader.getDistanceSqToEntity(entityplayer) > 16.0D?false:entityplayer.openContainer instanceof Container);
      }
   }

   public void startExecuting() {
      this.trader.getNavigator().clearPathEntity();
   }

   public void resetTask() {
      this.trader.setCustomer((EntityPlayer)null);
      this.trader.targetTasks.onUpdateTasks();
   }
}
