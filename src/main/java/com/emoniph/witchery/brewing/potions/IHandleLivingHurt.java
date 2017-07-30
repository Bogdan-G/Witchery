package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface IHandleLivingHurt {

   PotionBase getPotion();

   void onLivingHurt(World var1, EntityLivingBase var2, LivingHurtEvent var3, int var4);

   boolean handleAllHurtEvents();
}
