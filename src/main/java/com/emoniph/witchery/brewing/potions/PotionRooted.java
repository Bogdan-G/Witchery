package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionRooted extends PotionBase implements IHandleLivingUpdate {

   public PotionRooted(int id, int color) {
      super(id, true, color);
   }

   public void postContructInitialize() {
      this.func_111184_a(SharedMonsterAttributes.movementSpeed, "B17D31C2-7E44-4C0A-9F3E-2A0E8A4F1D6C", -100.0D, 2);
   }

   public void applyAttributesModifiersToEntity(EntityLivingBase entity, BaseAttributeMap attributes, int amplifier) {
      if(!(entity instanceof IBossDisplayData)) {
         super.applyAttributesModifiersToEntity(entity, attributes, amplifier);
      }

   }

   public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap attributes, int amplifier) {
      if(!(entity instanceof IBossDisplayData)) {
         super.removeAttributesModifiersFromEntity(entity, attributes, amplifier);
      }

   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!(entity instanceof IBossDisplayData)) {
         entity.motionX = 0.0D;
         entity.motionZ = 0.0D;
         if(entity.onGround) {
            entity.motionY = 0.0D;
         }
      }

   }
}
