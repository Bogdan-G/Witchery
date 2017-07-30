package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public interface IHandleLivingUpdate {

   PotionBase getPotion();

   void onLivingUpdate(World var1, EntityLivingBase var2, LivingUpdateEvent var3, int var4, int var5);
}
