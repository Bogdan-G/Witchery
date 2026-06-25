package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.ritual.rites.RiteExpandingEffect;
import java.util.Iterator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class RiteBlindness extends RiteExpandingEffect {

   public RiteBlindness(int radius, int height) {
      super(radius, height, true);
   }

   public boolean doRadiusAction(World world, int posX, int posY, int posZ, int radius, EntityPlayer player, boolean enhanced) {
      double radiusSq = (double)(radius * radius);
      double minSq = (double)Math.max(0, (radius - 1) * (radius - 1));
      Iterator i$ = world.playerEntities.iterator();

      Object obj;
      double distanceSq;
      while(i$.hasNext()) {
         obj = i$.next();
         EntityPlayer victim = (EntityPlayer)obj;
         distanceSq = victim.getDistanceSq(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ);
         if(distanceSq > minSq && distanceSq <= radiusSq) {
            if(Witchery.Items.POPPET.voodooProtectionActivated(player, (ItemStack)null, victim, 6)) {
               return false;
            }

            if(!victim.isPotionActive(Potion.blindness)) {
               victim.addPotionEffect(new PotionEffect(Potion.blindness.id, (enhanced?5:2) * 1200, 0));
            }
         }
      }

      i$ = world.loadedEntityList.iterator();

      while(i$.hasNext()) {
         obj = i$.next();
         if(obj instanceof EntityLiving) {
            EntityLiving victim1 = (EntityLiving)obj;
            distanceSq = victim1.getDistanceSq(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ);
            if(distanceSq > minSq && distanceSq <= radiusSq && !victim1.isPotionActive(Potion.blindness)) {
               victim1.addPotionEffect(new PotionEffect(Potion.blindness.id, (enhanced?5:2) * 1200, 0));
            }
         }
      }

      return true;
   }

   public void doBlockAction(World world, int posX, int posY, int posZ, int currentRadius, EntityPlayer player, boolean enhanced) {}
}
