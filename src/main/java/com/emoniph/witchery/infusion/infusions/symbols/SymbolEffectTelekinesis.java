/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.network.simpleimpl.IMessage
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.world.World
 */
package com.emoniph.witchery.infusion.infusions.symbols;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffectProjectile;
import com.emoniph.witchery.network.PacketPushTarget;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class SymbolEffectTelekinesis
extends SymbolEffectProjectile {
    public SymbolEffectTelekinesis(int effectID, String unlocalisedName) {
        super(effectID, unlocalisedName);
    }

    @Override
    public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
        double radius = spell.getEffectLevel() == 1 ? 0.0 : (spell.getEffectLevel() == 2 ? 3.0 : 6.0);
        final boolean isSneaking = caster != null && caster.isSneaking();
        final double spellX = spell.motionX;
        final double spellZ = spell.motionZ;
        final double casterX = caster != null ? caster.posX : spell.posX;
        final double casterZ = caster != null ? caster.posZ : spell.posZ;
        EffectRegistry.applyEntityEffect(world, caster, mop, spell.posX, spell.posY, spell.posZ, radius, EntityLivingBase.class, new EffectRegistry.IEntityEffect<EntityLivingBase>(){

            @Override
            public void doAction(World world, EntityLivingBase actor, double x, double y, double z, EntityLivingBase target) {
                if (target != actor) {
                    double motionZ;
                    double motionX;
                    double acceleration;
                    double d = acceleration = isSneaking ? 2.5 : -1.5;
                    if (isSneaking) {
                        motionX = spellX * acceleration;
                        motionZ = spellZ * acceleration;
                    } else {
                        double dX = casterX - target.posX;
                        double dZ = casterZ - target.posZ;
                        double distance = Math.sqrt(dX * dX + dZ * dZ);
                        if (distance > 0.0) {
                            motionX = dX / distance * Math.abs(acceleration);
                            motionZ = dZ / distance * Math.abs(acceleration);
                        } else {
                            motionX = 0.0;
                            motionZ = 0.0;
                        }
                    }
                    double motionY = 0.4;
                    if (target instanceof EntityPlayer) {
                        EntityPlayer targetPlayer = (EntityPlayer)target;
                        Witchery.packetPipeline.sendTo((IMessage)new PacketPushTarget(motionX, motionY, motionZ), targetPlayer);
                    } else {
                        target.motionX = motionX;
                        target.motionY = motionY;
                        target.motionZ = motionZ;
                    }
                }
            }
        });
    }
}

