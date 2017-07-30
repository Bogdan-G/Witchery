package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public interface IHandleEnderTeleport {

   PotionBase getPotion();

   void onEnderTeleport(World var1, EntityLivingBase var2, EnderTeleportEvent var3, int var4);
}
