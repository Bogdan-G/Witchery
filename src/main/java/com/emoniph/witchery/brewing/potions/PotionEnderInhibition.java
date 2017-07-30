package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.IHandleEnderTeleport;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class PotionEnderInhibition extends PotionBase implements IHandleEnderTeleport {

   public PotionEnderInhibition(int id, int color) {
      super(id, true, color);
   }

   public void onEnderTeleport(World world, EntityLivingBase entity, EnderTeleportEvent event, int amplifier) {
      event.setCanceled(true);
   }

   public static boolean isActive(Entity entity, int amplifier) {
      if(entity != null && entity instanceof EntityLivingBase) {
         EntityLivingBase living = (EntityLivingBase)entity;
         return living.isPotionActive(Witchery.Potions.ENDER_INHIBITION) && living.getActivePotionEffect(Witchery.Potions.ENDER_INHIBITION).getAmplifier() >= amplifier;
      } else {
         return false;
      }
   }
}
