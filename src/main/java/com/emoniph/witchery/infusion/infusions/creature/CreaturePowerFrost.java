package com.emoniph.witchery.infusion.infusions.creature;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CreaturePowerFrost extends CreaturePower {

    public CreaturePowerFrost(int powerID, Class creatureType) {
        super(powerID, creatureType);
    }

    public int activateCost(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
        return 2;
    }

    public void onActivate(World world, EntityPlayer player, int elapsedTicks, MovingObjectPosition mop) {
        if (!world.isRemote) {
            // A wave of bitter cold: chill and slow nearby foes.
            AxisAlignedBB bounds = player.boundingBox.expand(4.0D, 2.0D, 4.0D);
            List targets = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
            Iterator i$ = targets.iterator();
            while (i$.hasNext()) {
                EntityLivingBase target = (EntityLivingBase)i$.next();
                if (target == player) {
                    continue;
                }
                target.addPotionEffect(new PotionEffect(Witchery.Potions.CHILLED.id, TimeUtil.secsToTicks(10), 1));
                target.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, TimeUtil.secsToTicks(6), 1));
            }
        }
        SoundEffect.RANDOM_FIZZ.playAtPlayer(world, player);
    }

    public void onUpdate(World world, EntityPlayer player) {
        if (!world.isRemote && world.rand.nextInt(4) == 0) {
            // Leave a trail of frost in the player's wake.
            int x = MathHelper.floor_double(player.posX);
            int y = MathHelper.floor_double(player.boundingBox.minY);
            int z = MathHelper.floor_double(player.posZ);
            if (world.getBlock(x, y, z).getMaterial() == Material.air && Blocks.snow_layer.canPlaceBlockAt(world, x, y, z)) {
                float temp = world.getBiomeGenForCoords(x, z).getFloatTemperature(x, y, z);
                if (temp < 1.6F) {
                    world.setBlock(x, y, z, Blocks.snow_layer);
                }
            }
        }
    }

    public int getChargesPerSacrifice() {
        return 5;
    }
}
