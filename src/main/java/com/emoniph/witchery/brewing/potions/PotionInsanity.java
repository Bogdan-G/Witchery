package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.entity.EntityIllusionCreeper;
import com.emoniph.witchery.entity.EntityIllusionSpider;
import com.emoniph.witchery.entity.EntityIllusionZombie;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PotionInsanity extends PotionBase {

   public PotionInsanity(int id, int color) {
      super(id, true, color);
   }

   public void postContructInitialize() {
      this.setIncurable();
      this.setPermenant();
      this.hideInventoryText();
   }

   public boolean isReady(int duration, int amplifier) {
      return duration % 20 == 13;
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      if(entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         World world = entity.worldObj;
         int level = Math.max(0, amplifier) + 1;
         int x = MathHelper.floor_double(entity.posX);
         int y = MathHelper.floor_double(entity.posY);
         int z = MathHelper.floor_double(entity.posZ);
         if(world.rand.nextInt(level > 2?25:(level > 1?30:35)) == 0) {
            Class sound = null;
            switch(world.rand.nextInt(3)) {
            case 0:
            default:
               sound = EntityIllusionCreeper.class;
               break;
            case 1:
               sound = EntityIllusionSpider.class;
               break;
            case 2:
               sound = EntityIllusionZombie.class;
            }

            boolean MAX_DISTANCE = true;
            boolean MIN_DISTANCE = true;
            Infusion.spawnCreature(world, sound, x, y, z, player, 4, 9);
         } else if(level >= 4 && world.rand.nextInt(20) == 0) {
            SoundEffect sound1 = SoundEffect.NONE;
            switch(world.rand.nextInt(3)) {
            case 0:
            case 2:
            case 3:
            default:
               sound1 = SoundEffect.RANDOM_EXPLODE;
               break;
            case 1:
               sound1 = SoundEffect.MOB_ENDERMAN_IDLE;
            }

            sound1.playOnlyTo((EntityPlayer)entity, 1.0F, 1.0F);
         }
      }

   }

   public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
      int factor = effect.getDuration() / 60 % 7;
      String s1 = I18n.format(Witchery.resource("witchery:potion.insanity." + factor), new Object[0]);
      mc.fontRenderer.drawStringWithShadow(s1, x + 10 + 18, y + 6, 16777215);
      String s = Potion.getDurationString(effect);
      mc.fontRenderer.drawStringWithShadow(s, x + 10 + 18, y + 6 + 10, 8355711);
   }
}
