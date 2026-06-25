package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingSetAttackTarget;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;

public class PotionComprehension extends PotionBase implements IHandleLivingSetAttackTarget {

   public PotionComprehension(int id, int color) {
      super(id, color);
   }

   public void onLivingSetAttackTarget(World world, EntityLiving entity, LivingSetAttackTargetEvent event, int amplifier) {
      if(event.target != null && event.target instanceof EntityPlayer && !(entity instanceof IBossDisplayData) && event.target.isPotionActive(this.id)) {
         entity.setAttackTarget((EntityLivingBase)null);
      }

   }
}
