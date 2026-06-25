package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionMarionette extends PotionBase implements IHandleLivingUpdate {

    public PotionMarionette(int id, int color) {
        super(id, true, color);
    }

    @Override
    public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
        if (!world.isRemote) {
            NBTTagCompound nbt = entity.getEntityData();
            if (nbt.hasKey("WitcheryMarionetteCasterMost") && nbt.hasKey("WitcheryMarionetteCasterLeast")) {
                long most = nbt.getLong("WitcheryMarionetteCasterMost");
                long least = nbt.getLong("WitcheryMarionetteCasterLeast");
                UUID casterUUID = new UUID(most, least);

                EntityLivingBase caster = null;
                for (Object obj : world.loadedEntityList) {
                    if (obj instanceof EntityLivingBase) {
                        EntityLivingBase living = (EntityLivingBase) obj;
                        if (living.getUniqueID().equals(casterUUID)) {
                            caster = living;
                            break;
                        }
                    }
                }

                if (caster != null) {
                    entity.rotationYaw = caster.rotationYaw;
                    entity.rotationPitch = caster.rotationPitch;
                    entity.rotationYawHead = caster.rotationYawHead;

                    double dx = caster.posX - caster.prevPosX;
                    double dz = caster.posZ - caster.prevPosZ;

                    if (Math.abs(dx) > 0.01 || Math.abs(dz) > 0.01) {
                        entity.moveEntity(dx, 0, dz);
                    }
                }
            }
        }
    }
}
