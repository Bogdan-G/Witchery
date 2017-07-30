package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.PotionBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionChilled extends PotionBase implements IHandleLivingHurt {

   public PotionChilled(int id, int color) {
      super(id, color);
   }

   public void postContructInitialize() {
      this.func_111184_a(SharedMonsterAttributes.movementSpeed, "7A20B8CD-7A97-4800-A7DC-5B464E31A11A", -0.1D, 2);
   }

   public boolean handleAllHurtEvents() {
      return false;
   }

   public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
      if(!world.isRemote && event.source.isFireDamage()) {
         event.ammount = Math.max(event.ammount - (float)(1 + amplifier), Math.min(event.ammount, amplifier >= 2?0.0F:1.0F));
      }

   }

   public boolean isReady(int duration, int amplifier) {
      int k = 25 >> amplifier;
      return k > 0?duration % k == 0:true;
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      if(entity instanceof EntityBlaze || amplifier >= 2) {
         entity.attackEntityFrom(DamageSource.magic, 1.0F);
      }

   }
}
