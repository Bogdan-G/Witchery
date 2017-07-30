package com.emoniph.witchery.blocks;

import net.minecraft.tileentity.TileEntity;

public class TileEntityBase extends TileEntity {

   protected long ticks = 0L;


   public void updateEntity() {
      super.updateEntity();
      if(this.ticks == 0L) {
         this.initiate();
      } else if(this.ticks >= Long.MAX_VALUE) {
         this.ticks = 1L;
      }

      ++this.ticks;
   }

   protected void initiate() {}

   public void markBlockForUpdate(boolean notifyNeighbours) {
      super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
      if(notifyNeighbours && super.worldObj != null) {
         super.worldObj.notifyBlockChange(super.xCoord, super.yCoord, super.zCoord, this.getBlockType());
      }

   }
}
