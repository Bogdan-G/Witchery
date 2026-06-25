package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionBerserk extends PotionBase implements IHandleLivingHurt {

   public PotionBerserk(int id, int color) {
      super(id, color);
   }

   public boolean handleAllHurtEvents() {
      return true;
   }

   public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
      if(!world.isRemote && event.ammount > 0.0F) {
         EntityLivingBase attacker = event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase?(EntityLivingBase)event.source.getEntity():null;
         if(attacker != null && attacker != entity && !event.source.isProjectile() && attacker.isPotionActive(this.id)) {
            int level = attacker.getActivePotionEffect(this).getAmplifier();
            event.ammount += event.ammount * 0.25F * (float)(level + 1);
         }

         if(entity.isPotionActive(this.id) && !event.source.isUnblockable()) {
            int defLevel = entity.getActivePotionEffect(this).getAmplifier();
            event.ammount += event.ammount * 0.2F * (float)(defLevel + 1);
         }
      }

   }
}
