package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionProvoke extends PotionBase implements IHandleLivingUpdate {

   public PotionProvoke(int id, int color) {
      super(id, true, color);
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && entity instanceof EntityPlayer && world.getTotalWorldTime() % 20L == 0L) {
         EntityPlayer player = (EntityPlayer)entity;
         double range = (double)(8 + 4 * amplifier);
         AxisAlignedBB bounds = player.boundingBox.expand(range, range, range);
         List nearby = world.getEntitiesWithinAABB(EntityCreature.class, bounds);
         if(nearby != null) {
            for(int i = 0; i < nearby.size(); ++i) {
               EntityCreature creature = (EntityCreature)nearby.get(i);
               if(!(creature instanceof IBossDisplayData) && creature.getAttackTarget() == null && creature.getEntitySenses().canSee(player)) {
                  creature.setAttackTarget(player);
               }
            }
         }
      }

   }
}
