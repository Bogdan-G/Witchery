package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionSunAllergy extends PotionBase implements IHandleLivingUpdate {

   public PotionSunAllergy(int id, int color) {
      super(id, true, color);
      this.setIncurable();
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getWorldTime() % 20L == 0L && world.isDaytime()) {
         float f = entity.getBrightness(1.0F);
         if(f > 0.5F && !entity.isInWater() && world.rand.nextFloat() < f - 0.45F && world.canBlockSeeTheSky(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ))) {
            boolean burnEntity = true;
            ItemStack itemstack = entity.getEquipmentInSlot(4);
            if(itemstack != null && !(entity instanceof EntityPlayer)) {
               if(itemstack.isItemStackDamageable()) {
                  itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + world.rand.nextInt(2));
                  if(itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage()) {
                     entity.renderBrokenItemStack(itemstack);
                     entity.setCurrentItemOrArmor(4, (ItemStack)null);
                  }
               }

               burnEntity = false;
            }

            if(burnEntity) {
               if(entity instanceof EntityPlayer) {
                  entity.attackEntityFrom(DamageSource.outOfWorld, amplifier >= 3?2.0F:1.0F);
               } else {
                  entity.setFire(8);
               }
            }
         }
      }

   }
}
