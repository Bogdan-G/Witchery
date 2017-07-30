package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionRepellAttacker extends PotionBase implements IHandleLivingHurt {

   public PotionRepellAttacker(int id, int color) {
      super(id, color);
   }

   public boolean handleAllHurtEvents() {
      return false;
   }

   public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
      if(!world.isRemote) {
         EntityLivingBase attacker = event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase?(EntityLivingBase)event.source.getEntity():null;
         double MAX_RANGE = 3.0D;
         double MAX_RANGE_SQ = 9.0D;
         if(attacker != null && attacker != entity && !event.source.isProjectile() && attacker.getDistanceSqToEntity(entity) < 9.0D) {
            EntityUtil.pushback(world, attacker, new EntityPosition(entity), 1.0D + (double)amplifier * 0.75D, 0.5D + (double)amplifier * 0.2D);
         }
      }

   }
}
