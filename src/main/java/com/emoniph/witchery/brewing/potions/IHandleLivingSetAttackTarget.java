package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

public interface IHandleLivingSetAttackTarget {

   PotionBase getPotion();

   void onLivingSetAttackTarget(World var1, EntityLiving var2, LivingSetAttackTargetEvent var3, int var4);
}
