/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.eventhandler.SubscribeEvent
 *  cpw.mods.fml.relauncher.Side
 *  cpw.mods.fml.relauncher.SideOnly
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.monster.EntityWitch
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.EnumRarity
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 *  net.minecraftforge.event.entity.living.LivingDeathEvent
 */
package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.item.WitchHandAbilities;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class ItemWitchHand
extends ItemBase {
    public ItemWitchHand() {
        this.setMaxStackSize(1);
        this.setFull3D();
    }

    @SideOnly(value=Side.CLIENT)
    public EnumRarity getRarity(ItemStack itemstack) {
        return EnumRarity.uncommon;
    }

    public void onUpdate(ItemStack itemstack, World world, Entity entity, int par4, boolean par5) {
        if (entity instanceof EntityPlayer) {
            Infusion.Registry.instance().get((EntityPlayer)entity).onUpdate(itemstack, world, (EntityPlayer)entity, par4, par5);
        }
    }

    public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity) {
        if (!player.worldObj.isRemote && entity instanceof EntityLivingBase) {
            WitchHandAbilities.onAttack(player, (EntityLivingBase)entity);
        }
        Infusion.Registry.instance().get(player).onLeftClickEntity(itemstack, player.worldObj, player, entity);
        return true;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
        WitchHandAbilities.onRightClick(world, player);
        player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
        return itemstack;
    }

    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 400;
    }

    public void onUsingTick(ItemStack itemstack, EntityPlayer player, int countdown) {
        Infusion.Registry.instance().get(player).onUsingItemTick(itemstack, player.worldObj, player, countdown);
    }

    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int countdown) {
        if (world.isRemote || !Infusion.isOnCooldown(world, itemstack)) {
            Infusion.Registry.instance().get(player).onPlayerStoppedUsing(itemstack, world, player, countdown);
        }
    }

    public static class EventHooks {
        @SubscribeEvent
        public void onLivingDeath(LivingDeathEvent event) {
            Entity entitySource;
            if (!event.entityLiving.worldObj.isRemote && (event.entityLiving instanceof EntityWitch || event.entityLiving instanceof EntityCovenWitch) && (entitySource = event.source.getSourceOfDamage()) != null && entitySource instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer)entitySource;
                boolean hasArthana = player.getHeldItem() != null && (player.getHeldItem().getItem() == Witchery.Items.ARTHANA || player.getHeldItem().getItem() == Witchery.Items.WITCH_HAND);
                double d = player.worldObj.rand.nextDouble();
                double d2 = hasArthana ? 0.5 : 0.33;
                if (d < d2) {
                    ItemStack itemstack = new ItemStack(Witchery.Items.WITCH_HAND);
                    EntityItem entityItem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, itemstack);
                    event.entityLiving.worldObj.spawnEntityInWorld((Entity)entityItem);
                }
            }
        }
    }
}

