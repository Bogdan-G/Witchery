package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public interface IHandleLivingDeath {

   PotionBase getPotion();

   void onLivingDeath(World var1, EntityLivingBase var2, LivingDeathEvent var3, int var4);
}
