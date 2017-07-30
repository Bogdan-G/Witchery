package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.TimeUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionPoisonWeapons extends PotionBase implements IHandleLivingHurt {

   public PotionPoisonWeapons(int id, int color) {
      super(id, color);
   }

   public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
      if(!world.isRemote && event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase && this.isValidDamageType(event.source.getDamageType())) {
         EntityLivingBase attacker = (EntityLivingBase)event.source.getEntity();
         PotionEffect poisonedAttack = attacker.getActivePotionEffect(this);
         if(poisonedAttack != null) {
            switch(poisonedAttack.getAmplifier()) {
            case 0:
               entity.addPotionEffect(new PotionEffect(Potion.poison.id, TimeUtil.secsToTicks(5), 0));
               break;
            case 1:
               entity.addPotionEffect(new PotionEffect(Potion.poison.id, TimeUtil.secsToTicks(5), 1));
               break;
            case 2:
               entity.addPotionEffect(new PotionEffect(Potion.poison.id, TimeUtil.secsToTicks(15), 1));
               break;
            case 3:
            default:
               entity.addPotionEffect(new PotionEffect(Potion.wither.id, TimeUtil.secsToTicks(20), 0));
            }
         }
      }

   }

   private boolean isValidDamageType(String damageType) {
      return damageType.equals("mob") || damageType.equals("player");
   }

   public boolean handleAllHurtEvents() {
      return true;
   }
}
