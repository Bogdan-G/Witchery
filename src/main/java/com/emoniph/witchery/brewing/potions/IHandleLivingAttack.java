package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public interface IHandleLivingAttack {

   PotionBase getPotion();

   void onLivingAttack(World var1, EntityLivingBase var2, LivingAttackEvent var3, int var4);
}
