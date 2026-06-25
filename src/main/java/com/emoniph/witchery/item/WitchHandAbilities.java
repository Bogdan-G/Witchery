/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.entity.monster.IMob
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntitySmallFireball
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 */
package com.emoniph.witchery.item;

import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WitchHandAbilities {
    public static void onAttack(EntityPlayer player, EntityLivingBase target) {
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        int infId = Infusion.getInfusionID(player);
        float baseDamage = 1.0f + (float)1 * 0.15f;
        if (infId == 1) {
            if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                baseDamage *= 3.0f;
            }
        } else if (infId == 3) {
            if (target.isBurning()) {
                baseDamage += 4.0f;
            }
        } else if (infId == 4) {
            player.heal(baseDamage * 0.25f);
        } else if (infId == 2 && player.worldObj.rand.nextInt(4) == 0) {
            Vec3 look = target.getLookVec();
            player.setPositionAndUpdate(target.posX - look.xCoord * 2.0, target.posY, target.posZ - look.zCoord * 2.0);
        }
        target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)player), baseDamage);
        if (!player.worldObj.isRemote) {
            //ext.increaseWitchXP(2);
        }
    }

    public static void onRightClick(World world, EntityPlayer player) {
        NBTTagCompound nbt;
        ExtendedPlayer ext = ExtendedPlayer.get(player);
        int infId = Infusion.getInfusionID(player);
        if (!world.isRemote) {
            //ext.increaseWitchXP(1);
        }
        int baseCost = player.isSneaking() ? 20 : 5;
        List witches = world.getEntitiesWithinAABB(EntityCovenWitch.class, player.boundingBox.expand(16.0, 16.0, 16.0));
        int covenCount = 0;
        for (Object obj : witches) {
            Object witch = (EntityCovenWitch)((Object)obj);
            if (!((net.minecraft.entity.passive.EntityTameable)witch).isTamed() || !player.getUniqueID().toString().equals(((net.minecraft.entity.passive.EntityTameable)witch).func_152113_b())) continue;
            ++covenCount;
        }
        if (covenCount > 0) {
            float multiplier = 1.0f - (float)covenCount * 0.15f;
            if (multiplier < 0.1f) {
                multiplier = 0.1f;
            }
            baseCost = (int)((float)baseCost * multiplier);
        }
        if ((nbt = Infusion.getNBT((Entity)player)) != null) {
            int currentEnergy = nbt.getInteger("witcheryInfusionCharges");
            if (currentEnergy < baseCost && !player.capabilities.isCreativeMode) {
                SoundEffect.NOTE_SNARE.playOnlyTo(player);
                return;
            }
            if (!player.capabilities.isCreativeMode) {
                Infusion.setCurrentEnergy(player, currentEnergy - baseCost);
            }
        }
        if (player.isSneaking()) {
            if (infId == 1) {
                List mobs = world.getEntitiesWithinAABB(IMob.class, player.boundingBox.expand(6.0, 6.0, 6.0));
                for (Object obj : mobs) {
                    EntityLivingBase mob = (EntityLivingBase)obj;
                    mob.addPotionEffect(new PotionEffect(Potion.blindness.id, 100));
                    mob.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)player), 5.0f);
                }
                ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_EXPLODE, (Entity)player, 2.0, 2.0, 16);
            } else if (infId == 2) {
                List mobs = world.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(10.0, 10.0, 10.0));
                for (Object obj : mobs) {
                    EntityLivingBase mob = (EntityLivingBase)obj;
                    if (mob == player) continue;
                    double px = player.posX;
                    double py = player.posY;
                    double pz = player.posZ;
                    player.setPositionAndUpdate(mob.posX, mob.posY, mob.posZ);
                    mob.setPositionAndUpdate(px, py, pz);
                    SoundEffect.MOB_ENDERMEN_PORTAL.playOnlyTo(player);
                    break;
                }
            } else if (infId == 3) {
                Vec3 look = player.getLookVec();
                player.motionX = look.xCoord * 3.5;
                player.motionY = 0.5;
                player.motionZ = look.zCoord * 3.5;
                player.velocityChanged = true;
                SoundEffect.MOB_GHAST_FIREBALL.playOnlyTo(player);
            } else if (infId == 4) {
                List mobs = world.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(5.0, 5.0, 5.0));
                for (Object obj : mobs) {
                    EntityLivingBase mob = (EntityLivingBase)obj;
                    if (mob == player) continue;
                    mob.motionY += 1.2;
                    mob.velocityChanged = true;
                    mob.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)player), 4.0f);
                }
                ParticleEffect.LARGE_EXPLODE.send(SoundEffect.RANDOM_EXPLODE, (Entity)player, 1.0, 2.0, 16);
            }
        } else if (infId == 1) {
            player.addPotionEffect(new PotionEffect(Potion.resistance.id, 100, 4));
            SoundEffect.NOTE_SNARE.playOnlyTo(player);
        } else if (infId == 2) {
            Vec3 look = player.getLookVec();
            boolean teleported = ItemGeneral.teleportToLocationSafely(world, player.posX + look.xCoord * 15.0, player.posY + look.yCoord * 15.0, player.posZ + look.zCoord * 15.0, player.dimension, (Entity)player, true);
            if (teleported) {
                SoundEffect.MOB_ENDERMEN_PORTAL.playOnlyTo(player);
                player.fallDistance = 0.0f;
            } else {
                SoundEffect.NOTE_SNARE.playOnlyTo(player);
            }
        } else if (infId == 3) {
            EntitySmallFireball fb = new EntitySmallFireball(world, (EntityLivingBase)player, player.getLookVec().xCoord, player.getLookVec().yCoord, player.getLookVec().zCoord);
            fb.posY = player.posY + (double)player.getEyeHeight();
            world.spawnEntityInWorld((Entity)fb);
            SoundEffect.MOB_GHAST_FIREBALL.playOnlyTo(player);
        } else if (infId == 4) {
            List mobs = world.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(12.0, 12.0, 12.0));
            for (Object obj : mobs) {
                EntityLivingBase mob = (EntityLivingBase)obj;
                if (mob == player) continue;
                Vec3 dir = Vec3.createVectorHelper((double)(player.posX - mob.posX), (double)(player.posY - mob.posY), (double)(player.posZ - mob.posZ));
                dir = dir.normalize();
                mob.motionX = dir.xCoord * 1.5;
                mob.motionY = dir.yCoord * 1.0;
                mob.motionZ = dir.zCoord * 1.5;
                mob.velocityChanged = true;
                mob.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 60, 2));
                break;
            }
            SoundEffect.WITCHERY_RANDOM_POOF.playOnlyTo(player);
        }
    }
}

