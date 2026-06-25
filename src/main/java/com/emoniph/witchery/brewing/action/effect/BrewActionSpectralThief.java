package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.brewing.ModifiersEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BrewActionSpectralThief extends BrewActionEffect {

    public BrewActionSpectralThief(BrewItemKey itemKey, BrewNamePart namePart, AltarPower power, Probability prob, EffectLevel effectLevel) {
        super(itemKey, namePart, power, prob, effectLevel);
    }

    @Override
    protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
        if (!world.isRemote && modifiers.caster != null && targetEntity != modifiers.caster) {
            ItemStack stolen = targetEntity.getEquipmentInSlot(0);
            if (stolen != null) {
                targetEntity.setCurrentItemOrArmor(0, null);
                if (!modifiers.caster.inventory.addItemStackToInventory(stolen)) {
                    modifiers.caster.entityDropItem(stolen, 0.0F);
                }
            }
        }
    }
}
