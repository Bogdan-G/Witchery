package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionVoodooLink extends PotionBase implements IHandleLivingHurt {

    public PotionVoodooLink(int id, int color) {
        super(id, true, color);
    }

    @Override
    public boolean handleAllHurtEvents() {
        return false;
    }

    @Override
    public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
        if (!world.isRemote && !event.isCanceled()) {
            NBTTagCompound nbt = entity.getEntityData();
            if (nbt.hasKey("WitcheryVoodooTargetMost") && nbt.hasKey("WitcheryVoodooTargetLeast")) {
                long most = nbt.getLong("WitcheryVoodooTargetMost");
                long least = nbt.getLong("WitcheryVoodooTargetLeast");
                UUID targetUUID = new UUID(most, least);

                EntityLivingBase target = null;
                for (Object obj : world.loadedEntityList) {
                    if (obj instanceof EntityLivingBase) {
                        EntityLivingBase living = (EntityLivingBase) obj;
                        if (living.getUniqueID().equals(targetUUID)) {
                            target = living;
                            break;
                        }
                    }
                }

                if (target != null && target != entity) {
                    target.attackEntityFrom(DamageSource.magic, event.ammount);
                    ParticleEffect.REDDUST.send(SoundEffect.DAMAGE_HIT, target, 1.0, 2.0, 16);
                    ParticleEffect.REDDUST.send(SoundEffect.NONE, entity, 1.0, 2.0, 16);
                }
            }
        }
    }
}
