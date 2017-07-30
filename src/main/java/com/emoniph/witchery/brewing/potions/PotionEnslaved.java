package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.EntityAIEnslaverHurtByTarget;
import com.emoniph.witchery.brewing.potions.IHandleLivingSetAttackTarget;
import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityEnt;
import com.emoniph.witchery.entity.EntityImp;
import com.emoniph.witchery.util.EntityUtil;
import java.util.Iterator;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionEnslaved extends PotionBase implements IHandleLivingSetAttackTarget, IHandleLivingUpdate {

   private static final String ENSLAVER_KEY = "WITCEnslaverName";


   public PotionEnslaved(int id, int color) {
      super(id, true, color);
   }

   public void onLivingSetAttackTarget(World world, EntityLiving entity, LivingSetAttackTargetEvent event, int amplifier) {
      if(event.target != null && event.target instanceof EntityPlayer && entity instanceof EntityLiving) {
         String enslaverName = getMobEnslaverName(entity);
         if(enslaverName.equals(event.target.getCommandSenderName())) {
            entity.setAttackTarget((EntityLivingBase)null);
         }
      }

   }

   public static boolean setEnslaverForMob(EntityLiving entity, EntityPlayer player) {
      if(entity != null && player != null) {
         String enslaverName = entity.getEntityData().getString("WITCEnslaverName");
         boolean isEnslaved = enslaverName != null && !enslaverName.isEmpty();
         if(isEnslaved && player.getCommandSenderName().equals(enslaverName)) {
            return false;
         } else {
            entity.getEntityData().setString("WITCEnslaverName", player.getCommandSenderName());
            entity.addPotionEffect(new PotionEffect(Witchery.Potions.ENSLAVED.id, Integer.MAX_VALUE));
            EntityUtil.dropAttackTarget(entity);
            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean isMobEnslavedBy(EntityLiving entity, EntityPlayer player) {
      return player != null && entity != null && entity.getEntityData() != null && player.getCommandSenderName().equals(entity.getEntityData().getString("WITCEnslaverName"));
   }

   public static boolean canCreatureBeEnslaved(EntityLivingBase entityLiving) {
      return entityLiving instanceof EntityLiving?!(entityLiving instanceof IBossDisplayData) && !(entityLiving instanceof EntityGolem) && !(entityLiving instanceof EntityDemon) && !(entityLiving instanceof EntityWitch) && !(entityLiving instanceof EntityImp) && !(entityLiving instanceof EntityEnt):false;
   }

   public static boolean isMobEnslaved(EntityLiving entity) {
      if(entity == null) {
         return false;
      } else {
         String enslaverName = entity.getEntityData().getString("WITCEnslaverName");
         return enslaverName != null && !enslaverName.isEmpty();
      }
   }

   public static String getMobEnslaverName(EntityLiving entity) {
      if(entity == null) {
         return "";
      } else {
         String enslaverName = entity.getEntityData().getString("WITCEnslaverName");
         return enslaverName;
      }
   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(!world.isRemote && world.getTotalWorldTime() % 20L == 3L && entity instanceof EntityCreature) {
         EntityCreature creature = (EntityCreature)entity;
         Iterator i$ = creature.targetTasks.taskEntries.iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            EntityAITaskEntry task = (EntityAITaskEntry)obj;
            if(task.action instanceof EntityAIEnslaverHurtByTarget) {
               return;
            }
         }

         creature.targetTasks.addTask(1, new EntityAIEnslaverHurtByTarget(creature));
      }

   }
}
