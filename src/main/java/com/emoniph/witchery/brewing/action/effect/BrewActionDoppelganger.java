package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.brewing.ModifiersEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionDoppelganger extends BrewActionEffect {

    public BrewActionDoppelganger(BrewItemKey itemKey, BrewNamePart namePart, AltarPower power, Probability prob, EffectLevel effectLevel) {
        super(itemKey, namePart, power, prob, effectLevel);
    }

    @Override
    protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {
        spawnDoppelganger(world, x + 0.5D, y + 1.0D, z + 0.5D, modifiers.caster);
    }

    @Override
    protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
        spawnDoppelganger(world, targetEntity.posX, targetEntity.posY, targetEntity.posZ, modifiers.caster);
    }

    private void spawnDoppelganger(World world, double x, double y, double z, EntityPlayer caster) {
        if (!world.isRemote && caster != null) {
            EntityZombie zombie = new EntityZombie(world);
            zombie.setLocationAndAngles(x, y, z, caster.rotationYaw, caster.rotationPitch);
            
            for (int i = 0; i < 5; i++) {
                ItemStack gear = caster.getEquipmentInSlot(i);
                zombie.setCurrentItemOrArmor(i, gear != null ? gear.copy() : null);
                zombie.setEquipmentDropChance(i, 0.0F); // Ensure no free dupes
            }
            
            zombie.setCustomNameTag(caster.getCommandSenderName());
            world.spawnEntityInWorld(zombie);
            
            caster.addPotionEffect(new PotionEffect(Potion.invisibility.id, 200, 1)); // 10 seconds invis
        }
    }
}
