package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.nbt.NBTTagCompound;

public class PotionBanishment extends PotionBase {

    public PotionBanishment(int id, int color) {
        super(id, true, color);
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entity, BaseAttributeMap attributes, int amplifier) {
        super.applyAttributesModifiersToEntity(entity, attributes, amplifier);
        if (!entity.worldObj.isRemote) {
            NBTTagCompound nbt = entity.getEntityData();
            if (!nbt.hasKey("WitcheryBanishX")) {
                nbt.setDouble("WitcheryBanishX", entity.posX);
                nbt.setDouble("WitcheryBanishY", entity.posY);
                nbt.setDouble("WitcheryBanishZ", entity.posZ);
                nbt.setInteger("WitcheryBanishDim", entity.dimension);
            }
            ItemGeneral.teleportToLocation(entity.worldObj, entity.posX, entity.posY + 10, entity.posZ, -1, entity, true, ParticleEffect.PORTAL, SoundEffect.MOB_ENDERMEN_PORTAL);
        }
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap attributes, int amplifier) {
        super.removeAttributesModifiersFromEntity(entity, attributes, amplifier);
        if (!entity.worldObj.isRemote) {
            NBTTagCompound nbt = entity.getEntityData();
            if (nbt.hasKey("WitcheryBanishX")) {
                double x = nbt.getDouble("WitcheryBanishX");
                double y = nbt.getDouble("WitcheryBanishY");
                double z = nbt.getDouble("WitcheryBanishZ");
                int dim = nbt.getInteger("WitcheryBanishDim");
                
                nbt.removeTag("WitcheryBanishX");
                nbt.removeTag("WitcheryBanishY");
                nbt.removeTag("WitcheryBanishZ");
                nbt.removeTag("WitcheryBanishDim");

                ItemGeneral.teleportToLocation(entity.worldObj, x, y, z, dim, entity, true, ParticleEffect.PORTAL, SoundEffect.MOB_ENDERMEN_PORTAL);
            }
        }
    }
}
