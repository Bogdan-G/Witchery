package com.emoniph.witchery.infusion.infusions.symbols;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntitySpellEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class SymbolEffectImperio extends SymbolEffectProjectile {

    public static final Map<EntityLivingBase, EntityPlayer> IMPERIO_TARGETS = new WeakHashMap<EntityLivingBase, EntityPlayer>();
    public static final Set<EntityLivingBase> IMPERIO_STAYING_TARGETS = Collections.newSetFromMap(new WeakHashMap<EntityLivingBase, Boolean>());

    public SymbolEffectImperio(int effectID, String unlocalisedName) {
        super(effectID, unlocalisedName, 200, false, false, "witchery.pott.imperio", 100);
        this.setSize(1.0f);
        this.setColor(0x00FF00);
    }

    @Override
    public void onCollision(World world, EntityLivingBase caster, MovingObjectPosition mop, EntitySpellEffect spell) {
        if (!world.isRemote && caster instanceof EntityPlayer && mop != null && mop.entityHit instanceof EntityLivingBase) {
            EntityLivingBase target = (EntityLivingBase) mop.entityHit;
            EntityPlayer player = (EntityPlayer) caster;

            // Apply paralysis to target temporarily to signify mind control shock
            target.addPotionEffect(new PotionEffect(Witchery.Potions.PARALYSED.id, 100, 0, true));

            IMPERIO_TARGETS.put(target, player);

            player.addChatMessage(new ChatComponentText("La criatura ha caído bajo tu control. Usa /imperio para darle órdenes."));
        }
    }
}
