package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

import java.util.List;

public class PotionSirenSong extends PotionBase implements IHandleLivingUpdate {

    public PotionSirenSong(int id, int color) {
        super(id, true, color);
    }

    @Override
    public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
        if (!world.isRemote && entity.ticksExisted % 10 == 0) {
            double radius = 16.0D;
            List mobs = world.getEntitiesWithinAABB(EntityMob.class, entity.boundingBox.expand(radius, radius, radius));

            for (Object obj : mobs) {
                if (obj instanceof EntityMob) {
                    EntityMob mob = (EntityMob) obj;
                    mob.setAttackTarget(null);
                    mob.setTarget(null);
                    mob.getNavigator().tryMoveToEntityLiving(entity, 0.8D);
                }
            }
        }
    }
}
