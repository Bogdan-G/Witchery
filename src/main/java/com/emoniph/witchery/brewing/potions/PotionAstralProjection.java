package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.item.ItemGeneral;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionAstralProjection extends PotionBase {

    public PotionAstralProjection(int id, int color) {
        super(id, false, color);
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entity, BaseAttributeMap attributes, int amplifier) {
        super.applyAttributesModifiersToEntity(entity, attributes, amplifier);
        if (!entity.worldObj.isRemote) {
            NBTTagCompound nbt = entity.getEntityData();
            if (!nbt.hasKey("WitcheryAstralX")) {
                nbt.setDouble("WitcheryAstralX", entity.posX);
                nbt.setDouble("WitcheryAstralY", entity.posY);
                nbt.setDouble("WitcheryAstralZ", entity.posZ);
                nbt.setInteger("WitcheryAstralDim", entity.dimension);
            }
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (!player.capabilities.isCreativeMode) {
                    player.capabilities.allowFlying = true;
                    player.sendPlayerAbilities();
                }
            }
            entity.addPotionEffect(new PotionEffect(Potion.invisibility.id, 999999, 0, true));
        }
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap attributes, int amplifier) {
        super.removeAttributesModifiersFromEntity(entity, attributes, amplifier);
        if (!entity.worldObj.isRemote) {
            NBTTagCompound nbt = entity.getEntityData();
            if (nbt.hasKey("WitcheryAstralX")) {
                double x = nbt.getDouble("WitcheryAstralX");
                double y = nbt.getDouble("WitcheryAstralY");
                double z = nbt.getDouble("WitcheryAstralZ");
                int dim = nbt.getInteger("WitcheryAstralDim");
                
                nbt.removeTag("WitcheryAstralX");
                nbt.removeTag("WitcheryAstralY");
                nbt.removeTag("WitcheryAstralZ");
                nbt.removeTag("WitcheryAstralDim");

                if (entity.dimension != dim) {
                    ItemGeneral.teleportToLocation(entity.worldObj, x, y, z, dim, entity, true);
                } else {
                    entity.setPositionAndUpdate(x, y, z);
                }
            }
            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;
                if (!player.capabilities.isCreativeMode) {
                    player.capabilities.allowFlying = false;
                    player.capabilities.isFlying = false;
                    player.sendPlayerAbilities();
                }
            }
            entity.removePotionEffect(Potion.invisibility.id);
        }
    }
}
