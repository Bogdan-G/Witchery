package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.EntityAIVillagerMateNow;
import com.emoniph.witchery.brewing.potions.EntityAIZombieMateNow;
import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionLove extends PotionBase implements IHandleLivingUpdate {

   public PotionLove(int id, int color) {
      super(id, color);
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getTotalWorldTime() % 20L == 7L) {
         if(entity instanceof EntityAnimal) {
            EntityAnimal villager = (EntityAnimal)entity;
            if(villager.getGrowingAge() >= 0 && !villager.isInLove()) {
               villager.setGrowingAge(0);
               villager.func_146082_f((EntityPlayer)null);
            }
         } else {
            Iterator ai;
            Object obj;
            EntityAITaskEntry task;
            if(entity instanceof EntityZombie) {
               EntityZombie villager2 = (EntityZombie)entity;
               if(!villager2.isChild()) {
                  ai = villager2.tasks.taskEntries.iterator();

                  while(ai.hasNext()) {
                     obj = ai.next();
                     task = (EntityAITaskEntry)obj;
                     if(task.action instanceof EntityAIZombieMateNow) {
                        ((EntityAIZombieMateNow)task.action).beginMating();
                        return;
                     }
                  }

                  EntityAIZombieMateNow ai1 = new EntityAIZombieMateNow(villager2);
                  ai1.beginMating();
                  villager2.tasks.addTask(1, ai1);
               }
            } else if(entity instanceof EntityVillager) {
               EntityVillager villager1 = (EntityVillager)entity;
               if(!villager1.isChild() && !villager1.isMating()) {
                  ai = villager1.tasks.taskEntries.iterator();

                  while(ai.hasNext()) {
                     obj = ai.next();
                     task = (EntityAITaskEntry)obj;
                     if(task.action instanceof EntityAIVillagerMateNow) {
                        ((EntityAIVillagerMateNow)task.action).beginMating();
                        return;
                     }
                  }

                  EntityAIVillagerMateNow ai2 = new EntityAIVillagerMateNow(villager1);
                  ai2.beginMating();
                  villager1.tasks.addTask(1, ai2);
               }
            } else if(entity instanceof EntityPlayer && world.rand.nextInt(2) == 0) {
               ParticleEffect.HEART.send(SoundEffect.NONE, world, event.entityLiving.posX, event.entityLiving.posY + 1.0D, event.entityLiving.posZ, 0.6D, 2.0D, 16);
            }
         }
      }

   }
}
