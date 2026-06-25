package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionBrittle extends PotionBase implements IHandleLivingHurt {

    public PotionBrittle(int id, int color) {
        super(id, true, color);
    }

    protected boolean isDebuff() {
        return true;
    }

    public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
        if (amplifier < 0) {
            return;
        }
        // Brittle flesh: every wound bites deeper.
        float multiplier = 1.0F + 0.5F * (float)(amplifier + 1);
        event.ammount *= multiplier;
    }

    public boolean handleAllHurtEvents() {
        return false;
    }
}
