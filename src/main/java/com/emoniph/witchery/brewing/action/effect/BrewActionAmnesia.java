package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.brewing.ModifiersEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;

public class BrewActionAmnesia extends BrewActionEffect {

    public BrewActionAmnesia(BrewItemKey itemKey, BrewNamePart namePart, AltarPower power, Probability prob, EffectLevel effectLevel) {
        super(itemKey, namePart, power, prob, effectLevel);
    }

    @Override
    protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
        if (!world.isRemote && targetEntity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) targetEntity;
            ItemStack[] mainInv = player.inventory.mainInventory;
            
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            for (int i = 0; i < mainInv.length; i++) {
                items.add(mainInv[i]);
            }
            
            Collections.shuffle(items);
            
            for (int i = 0; i < mainInv.length; i++) {
                mainInv[i] = items.get(i);
            }
            
            // Sync inventory to client
            if (player instanceof net.minecraft.entity.player.EntityPlayerMP) {
                ((net.minecraft.entity.player.EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
            }
        }
    }
}
