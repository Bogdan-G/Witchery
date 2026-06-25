package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.item.ItemGeneral;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionEtherealChains extends PotionBase implements IHandleLivingUpdate {

    public PotionEtherealChains(int id, int color) {
        super(id, true, color);
    }

    @Override
    public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
        if (!world.isRemote) {
            NBTTagCompound nbt = entity.getEntityData();
            
            if (!nbt.hasKey("witcheryEtherealX")) {
                nbt.setDouble("witcheryEtherealX", entity.posX);
                nbt.setDouble("witcheryEtherealY", entity.posY);
                nbt.setDouble("witcheryEtherealZ", entity.posZ);
            } else {
                double startX = nbt.getDouble("witcheryEtherealX");
                double startY = nbt.getDouble("witcheryEtherealY");
                double startZ = nbt.getDouble("witcheryEtherealZ");
                
                if (entity.getDistanceSq(startX, startY, startZ) > 25.0D) {
                    ItemGeneral.teleportToLocation(world, startX, startY, startZ, entity.dimension, entity, true);
                }
            }
            
            if (duration <= 2) {
                nbt.removeTag("witcheryEtherealX");
                nbt.removeTag("witcheryEtherealY");
                nbt.removeTag("witcheryEtherealZ");
            }
        }
    }
}
