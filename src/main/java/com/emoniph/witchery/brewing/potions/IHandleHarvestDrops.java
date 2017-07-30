package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public interface IHandleHarvestDrops {

   PotionBase getPotion();

   void onHarvestDrops(World var1, EntityPlayer var2, HarvestDropsEvent var3, int var4);
}
