package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class BrewActionMarionette extends BrewActionEffect {

    public BrewActionMarionette(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, EffectLevel effectLevel) {
        super(itemKey, namePart, powerCost, baseProbability, effectLevel);
    }

    @Override
    protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
        if (!world.isRemote && modifiers.caster != null && modifiers.caster != targetEntity) {
            NBTTagCompound nbt = targetEntity.getEntityData();
            nbt.setLong("WitcheryMarionetteCasterMost", modifiers.caster.getUniqueID().getMostSignificantBits());
            nbt.setLong("WitcheryMarionetteCasterLeast", modifiers.caster.getUniqueID().getLeastSignificantBits());

            targetEntity.addPotionEffect(new PotionEffect(Witchery.Potions.MARIONETTE.id, modifiers.getModifiedDuration(1200), modifiers.getStrength()));
        }
    }
}
