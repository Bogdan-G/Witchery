package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.TimeUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionFeelNoPain extends PotionBase implements IHandleLivingHurt, IHandleLivingUpdate {

   public PotionFeelNoPain(int id, int color) {
      super(id, color);
      this.setIncurable();
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getWorldTime() % 20L == 2L && amplifier > 0 && !entity.isPotionActive(Potion.confusion) && !entity.isPotionActive(Witchery.Potions.STOUT_BELLY) && world.rand.nextInt(5 - Math.min(amplifier, 3)) == 0) {
         entity.addPotionEffect(new PotionEffect(Potion.confusion.id, TimeUtil.secsToTicks(6 + amplifier * 2)));
      }

   }

   public boolean handleAllHurtEvents() {
      return false;
   }

   public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
      if(!world.isRemote && entity instanceof EntityPlayer && (event.source.getDamageType() == "mob" || event.source.getDamageType() == "player" || event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase)) {
         EntityPlayer player = (EntityPlayer)entity;
         float currentHealth = entity.getHealth();
         float newHealth = EntityUtil.getHealthAfterDamage(event, currentHealth, entity);
         float damage = currentHealth - newHealth;
         int food = player.getFoodStats().getFoodLevel();
         if(food > 0) {
            int modifiedDamage = (int)Math.ceil(amplifier > 0?(double)Math.max(damage / (float)amplifier, amplifier > 1?1.0F:2.0F):(double)Math.max(damage * 2.0F, 3.0F));
            int foodPenalty = Math.min(modifiedDamage, food);
            player.getFoodStats().addStats(-foodPenalty, 2.0F);
            event.setCanceled(true);
         }
      }

   }
}
