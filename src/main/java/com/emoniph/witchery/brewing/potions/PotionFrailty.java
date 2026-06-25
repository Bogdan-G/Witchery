package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionFrailty extends PotionBase implements IHandleLivingHurt {

   public PotionFrailty(int id, int color) {
      super(id, true, color);
   }

   public boolean handleAllHurtEvents() {
      return true;
   }

   public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
      if(!world.isRemote && event.ammount > 0.0F && !event.source.isProjectile() && event.source != DamageSource.magic) {
         EntityLivingBase attacker = event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase?(EntityLivingBase)event.source.getEntity():null;
         if(attacker != null && attacker != entity && attacker.isPotionActive(this.id)) {
            int level = attacker.getActivePotionEffect(this).getAmplifier();
            float recoil = event.ammount * 0.2F * (float)(level + 1);
            if(recoil >= 1.0F) {
               attacker.attackEntityFrom(DamageSource.magic, recoil);
            }
         }
      }

   }
}
