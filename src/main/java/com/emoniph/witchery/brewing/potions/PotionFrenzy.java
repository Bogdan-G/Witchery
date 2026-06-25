package com.emoniph.witchery.brewing.potions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import java.util.List;

public class PotionFrenzy extends PotionBase implements IHandleLivingUpdate {

    public PotionFrenzy(int id, int color) {
        super(id, true, color);
    }

    @Override
    public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
        if (!world.isRemote && entity instanceof EntityLiving && entity instanceof IMob) {
            if (entity.ticksExisted % 20 == 0) {
                EntityLiving mob = (EntityLiving) entity;
                double radius = 10.0 + (amplifier * 2.0);
                List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.boundingBox.expand(radius, radius, radius));
                
                if (list != null && !list.isEmpty()) {
                    EntityLivingBase newTarget = null;
                    for (int i = 0; i < 5; i++) {
                        EntityLivingBase potential = list.get(world.rand.nextInt(list.size()));
                        if (potential != entity && mob.canEntityBeSeen(potential)) {
                            newTarget = potential;
                            break;
                        }
                    }
                    if (newTarget != null) {
                        mob.setAttackTarget(newTarget);
                    }
                }
            }
        }
    }
}
