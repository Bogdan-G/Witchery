package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class PotionFloating extends PotionBase {

   public PotionFloating(int id, int color) {
      super(id, color);
   }

   public boolean isReady(int duration, int amplifier) {
      return true;
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      int height = 3 + amplifier;
      int x = MathHelper.floor_double(entity.posX);
      int y = MathHelper.floor_double(entity.posY);
      int z = MathHelper.floor_double(entity.posZ);
      boolean isPlayer = entity instanceof EntityPlayer;
      boolean activeOnSide = isPlayer && entity.worldObj.isRemote || !isPlayer && !entity.worldObj.isRemote;
      entity.fallDistance = 0.0F;
      if(activeOnSide) {
         boolean raised = false;

         for(int i = 1; i <= height; ++i) {
            if(!entity.worldObj.isAirBlock(x, y - i, z)) {
               entity.motionY = 0.25D;
               raised = true;
               break;
            }
         }

         if(!raised) {
            entity.motionY = entity.worldObj.rand.nextInt(5) == 0?-0.05D:0.0D;
         }
      }

   }
}
