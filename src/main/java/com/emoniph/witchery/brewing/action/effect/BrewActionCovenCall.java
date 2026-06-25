package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.entity.EntityCovenWitch;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BrewActionCovenCall extends BrewActionEffect {

    public BrewActionCovenCall(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, EffectLevel effectLevel) {
        super(itemKey, namePart, powerCost, baseProbability, effectLevel);
    }

    @Override
    protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
        if (!world.isRemote && targetEntity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) targetEntity;
            int covenSize = EntityCovenWitch.getCovenSize(player);
            
            if (covenSize > 0) {
                int toSummon = Math.min(covenSize, modifiers.getStrength() + 1);
                for (int i = 0; i < toSummon; i++) {
                    EntityCovenWitch.summonCovenMember(world, player, 60);
                }
            }
        }
    }
}
