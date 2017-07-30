package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionReflectDamage extends PotionBase implements IHandleLivingHurt {

   public PotionReflectDamage(int id, int color) {
      super(id, color);
   }

   public boolean handleAllHurtEvents() {
      return false;
   }

   public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
      if(!world.isRemote) {
         EntityLivingBase attacker = event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase?(EntityLivingBase)event.source.getEntity():null;
         if(attacker != null && attacker != entity && (!event.source.isProjectile() || amplifier >= 2)) {
            float amount = (float)Math.ceil((double)(event.ammount * 0.1F * (float)(amplifier + (!event.source.isProjectile()?1:0))));
            attacker.attackEntityFrom(event.source, amount);
            event.ammount -= amount;
         }
      }

   }
}
