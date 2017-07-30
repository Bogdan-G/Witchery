package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.entity.EntityGoblin;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tileentity.TileEntity;

public class EntityAIWorship extends EntityAIBase {

   private final EntityGoblin goblin;
   private final double maxDuration;
   private int currentTick;
   private boolean shouldBegin;
   private int posX;
   private int posY;
   private int posZ;


   public EntityAIWorship(EntityGoblin goblin, double maxDuration) {
      this.goblin = goblin;
      this.maxDuration = maxDuration;
      this.setMutexBits(15);
   }

   public boolean shouldExecute() {
      return this.shouldBegin || this.goblin.isWorshipping();
   }

   public void startExecuting() {
      this.currentTick = 0;
      this.shouldBegin = false;
      this.goblin.setWorshipping(true);
      this.goblin.getNavigator().tryMoveToXYZ((double)this.posX, (double)this.posY, (double)this.posZ, 0.4D);
   }

   public boolean continueExecuting() {
      return (double)this.currentTick <= this.maxDuration || this.goblin.worldObj.rand.nextInt(3) == 0;
   }

   public void resetTask() {
      this.goblin.setWorshipping(false);
   }

   public void updateTask() {
      ++this.currentTick;
   }

   public void begin(TileEntity tile) {
      if(this.goblin.worldObj.rand.nextInt(3) != 0) {
         this.shouldBegin = true;
         this.posX = tile.xCoord;
         this.posY = tile.yCoord;
         this.posZ = tile.zCoord;
      }

   }
}
