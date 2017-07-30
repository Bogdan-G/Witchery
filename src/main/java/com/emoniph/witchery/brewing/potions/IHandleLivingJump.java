package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;

public interface IHandleLivingJump {

   PotionBase getPotion();

   void onLivingJump(World var1, EntityLivingBase var2, LivingJumpEvent var3, int var4);
}
