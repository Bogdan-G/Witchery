package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionSinking extends PotionBase implements IHandleLivingUpdate {

   public PotionSinking(int id, int color) {
      super(id, true, color);
   }

   public void postContructInitialize() {
      this.setPermenant();
      this.setIncurable();
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         if(world.isRemote) {
            if(player.isInWater()) {
               if(player.motionY < -0.03D && !player.onGround) {
                  player.motionY *= 1.5D + Math.min(0.05D * (double)amplifier, 0.2D);
               } else if(!player.onGround && player.isInsideOfMaterial(Material.water) && player.motionY > 0.0D) {
                  ;
               }
            } else if(!player.capabilities.isCreativeMode && player.capabilities.allowFlying && player.capabilities.isFlying) {
               player.motionY = -0.20000000298023224D;
            }
         }
      } else if(world.isRemote && world.getTotalWorldTime() % 20L == 3L && entity.isInWater()) {
         if(entity.motionY < 0.0D) {
            entity.motionY *= 1.0D + Math.min(0.1D * (double)(amplifier + 2), 0.4D);
         } else if(entity.motionY > 0.0D) {
            entity.motionY *= 1.0D - Math.min(0.1D * (double)(amplifier + 2), 0.4D);
         }
      }

   }
}
