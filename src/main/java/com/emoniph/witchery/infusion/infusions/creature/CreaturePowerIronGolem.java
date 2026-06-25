package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CreaturePowerIronGolem extends CreaturePower {

    public CreaturePowerIronGolem(int powerID, Class creatureType) {
        super(powerID, creatureType);
    }

    public int activateCost(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
        return 2;
    }

    public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
        if (!world.isRemote) {
            // A golem's mighty blow: launch and bruise everything nearby.
            AxisAlignedBB bounds = player.boundingBox.expand(3.0D, 2.0D, 3.0D);
            List targets = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
            Iterator i$ = targets.iterator();
            while (i$.hasNext()) {
                EntityLivingBase target = (EntityLivingBase)i$.next();
                if (target == player) {
                    continue;
                }
                double dX = target.posX - player.posX;
                double dZ = target.posZ - player.posZ;
                double len = Math.sqrt(dX * dX + dZ * dZ);
                if (len > 0.001D) {
                    dX /= len;
                    dZ /= len;
                }
                target.addVelocity(dX * 2.0D, 0.5D, dZ * 2.0D);
                target.velocityChanged = true;
                target.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)player), 4.0F);
            }
        }
        ParticleEffect.CLOUD.send(SoundEffect.RANDOM_FIZZ, (Entity)player, 2.0D, 1.0D, 16);
    }

    public int getChargesPerSacrifice() {
        return 5;
    }
}
