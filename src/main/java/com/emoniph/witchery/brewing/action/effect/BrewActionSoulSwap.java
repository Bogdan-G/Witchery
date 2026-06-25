package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.ParticleEffect;

public class BrewActionSoulSwap extends BrewActionEffect {

    public BrewActionSoulSwap(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, EffectLevel effectLevel) {
        super(itemKey, namePart, powerCost, baseProbability, effectLevel);
    }

    @Override
    protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
        if (!world.isRemote && modifiers.caster != null && modifiers.caster != targetEntity && modifiers.caster.dimension == targetEntity.dimension) {
            double casterX = modifiers.caster.posX;
            double casterY = modifiers.caster.posY;
            double casterZ = modifiers.caster.posZ;
            float casterYaw = modifiers.caster.rotationYaw;
            float casterPitch = modifiers.caster.rotationPitch;

            double targetX = targetEntity.posX;
            double targetY = targetEntity.posY;
            double targetZ = targetEntity.posZ;
            float targetYaw = targetEntity.rotationYaw;
            float targetPitch = targetEntity.rotationPitch;

            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, targetEntity, 1.0, 2.0, 16);
            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, modifiers.caster, 1.0, 2.0, 16);

            modifiers.caster.setPositionAndRotation(targetX, targetY, targetZ, targetYaw, targetPitch);
            modifiers.caster.setPositionAndUpdate(targetX, targetY, targetZ);
            
            targetEntity.setPositionAndRotation(casterX, casterY, casterZ, casterYaw, casterPitch);
            targetEntity.setPositionAndUpdate(casterX, casterY, casterZ);

            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, targetEntity, 1.0, 2.0, 16);
            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, modifiers.caster, 1.0, 2.0, 16);
        }
    }
}
