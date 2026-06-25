package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingAttack;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class PotionPacified extends PotionBase implements IHandleLivingAttack {

   public PotionPacified(int id, int color) {
      super(id, true, color);
   }

   public void onLivingAttack(World world, EntityLivingBase entity, LivingAttackEvent event, int amplifier) {
      EntityLivingBase attacker = event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase?(EntityLivingBase)event.source.getEntity():null;
      if(attacker != null && attacker.isPotionActive(this.id) && !(attacker instanceof IBossDisplayData)) {
         int level = attacker.getActivePotionEffect(this).getAmplifier();
         if(!event.source.isProjectile() || level >= 1) {
            event.setCanceled(true);
            if(attacker instanceof EntityPlayer && level >= 2) {
               attacker.attackEntityFrom(net.minecraft.util.DamageSource.magic, 1.0F);
            }
         }
      }

   }
}
