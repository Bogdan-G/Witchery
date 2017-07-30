package com.emoniph.witchery.entity;

import net.minecraft.entity.player.EntityPlayer;

public interface IOwnable {

   String getOwnerName();

   void setOwner(String var1);

   EntityPlayer getOwnerEntity();
}
