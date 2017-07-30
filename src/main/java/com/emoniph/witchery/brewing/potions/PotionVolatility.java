package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionVolatility extends PotionBase implements IHandleLivingHurt {

   public PotionVolatility(int id, int color) {
      super(id, true, color);
      this.setIncurable();
   }

   public boolean handleAllHurtEvents() {
      return false;
   }

   public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
      if(!world.isRemote && this.isExplodableDamage(event.source)) {
         boolean breakable = Config.instance().allowVolatilityPotionBlockDamage;
         if(breakable) {
            Coord c = new Coord(entity);
            breakable = BlockProtect.checkModsForBreakOK(world, c.x, c.y, c.z, entity);
         }

         if(event.source.isExplosion() || world.rand.nextInt(5 - Math.min(amplifier, 3)) == 0) {
            if(world.rand.nextInt(amplifier + 3) == 0) {
               entity.removePotionEffect(super.id);
            }

            world.createExplosion(event.source.isExplosion()?entity:null, entity.posX, entity.posY, entity.posZ, Math.min(2.0F + 0.5F * (float)amplifier, 3.0F), breakable);
         }
      }

   }

   private boolean isExplodableDamage(DamageSource source) {
      return source != DamageSource.drown && source != DamageSource.inWall && source != DamageSource.fall && source != DamageSource.outOfWorld && source != DamageSource.starve;
   }
}
