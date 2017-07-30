package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.entity.EntityFollower;
import com.emoniph.witchery.entity.EntityReflection;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PotionIllFitting extends PotionBase {

   public PotionIllFitting(int id, int color) {
      super(id, true, color);
   }

   public void postContructInitialize() {
      this.setPermenant();
      this.setIncurable();
   }

   public boolean isReady(int duration, int amplifier) {
      if(duration % 15 == 0) {
         switch(amplifier) {
         case 1:
            return duration <= 30;
         case 2:
            return duration <= 45;
         case 3:
            return duration <= 60;
         default:
            return duration <= 15;
         }
      } else {
         return false;
      }
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      World world = entity.worldObj;
      if(!world.isRemote && !isTargetBanned(entity)) {
         int slot = world.rand.nextInt(4) + 1;
         ItemStack armorPiece = entity.getEquipmentInSlot(slot);
         if(armorPiece != null) {
            entity.setCurrentItemOrArmor(slot, (ItemStack)null);
            EntityItem droppedItem = entity.entityDropItem(armorPiece, 0.0F);
            droppedItem.delayBeforeCanPickup = 5 + 5 * amplifier;
         }
      }

   }

   public static boolean isTargetBanned(EntityLivingBase entity) {
      return entity instanceof EntityReflection || entity instanceof EntityFollower;
   }
}
