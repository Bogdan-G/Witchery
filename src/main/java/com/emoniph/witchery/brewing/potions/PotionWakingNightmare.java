package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionWakingNightmare extends PotionBase implements IHandleLivingUpdate {

   public PotionWakingNightmare(int id, int color) {
      super(id, true, color);
   }

   public void postContructInitialize() {
      this.setPermenant();
      this.setIncurable();
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getTotalWorldTime() % 20L == 3L && entity.dimension != Config.instance().dimensionDreamID && world.rand.nextInt(amplifier > 3?30:(amplifier > 1?60:180)) == 0) {
         double R = 16.0D;
         double H = 8.0D;
         AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(entity.posX - 16.0D, entity.posY - 8.0D, entity.posZ - 16.0D, entity.posX + 16.0D, entity.posY + 8.0D, entity.posZ + 16.0D);
         List entities = world.getEntitiesWithinAABB(EntityNightmare.class, bounds);
         boolean doNothing = false;
         Iterator i$ = entities.iterator();

         while(i$.hasNext()) {
            EntityNightmare nightmare = (EntityNightmare)i$.next();
            if(nightmare.getVictimName().equalsIgnoreCase(entity.getCommandSenderName())) {
               doNothing = true;
               break;
            }
         }

         if(!doNothing) {
            Infusion.spawnCreature(world, EntityNightmare.class, MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ), entity, 2, 6, (ParticleEffect)null, SoundEffect.NONE);
         }
      }

   }
}
