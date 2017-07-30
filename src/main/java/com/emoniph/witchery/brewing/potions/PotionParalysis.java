package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.entity.EntityVillageGuard;
import com.emoniph.witchery.entity.ai.EntityAIMoveTowardsVampire;
import com.emoniph.witchery.util.TimeUtil;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class PotionParalysis extends PotionBase implements IHandleLivingUpdate, IHandleLivingHurt {

   public PotionParalysis(int id, int color) {
      super(id, true, color);
      this.setIncurable();
   }

   public void postContructInitialize() {
      this.func_111184_a(SharedMonsterAttributes.movementSpeed, "E69059D5-CAE6-4695-9BE3-C6F0F22151E8", -40.0D, 2);
   }

   public void applyAttributesModifiersToEntity(EntityLivingBase entity, BaseAttributeMap attributes, int amplifier) {
      if(this.canApplyToEntity(entity, amplifier)) {
         super.applyAttributesModifiersToEntity(entity, attributes, amplifier);
      } else if(isVillager(entity)) {
         EntityCreature creature = (EntityCreature)entity;
         creature.setAttackTarget((EntityLivingBase)null);
         creature.setRevengeTarget((EntityLivingBase)null);
         creature.setTarget((Entity)null);
         creature.tasks.addTask(0, new EntityAIMoveTowardsVampire(creature, 0.8D, 1.0F, 16.0F));
      }

   }

   public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap attributes, int amplifier) {
      if(this.canApplyToEntity(entity, amplifier)) {
         super.removeAttributesModifiersFromEntity(entity, attributes, amplifier);
      } else if(isVillager(entity) && amplifier >= 5) {
         EntityCreature creature = (EntityCreature)entity;
         Iterator itr = creature.tasks.taskEntries.iterator();
         EntityAIBase task = null;

         while(itr.hasNext()) {
            EntityAITaskEntry entityaitaskentry = (EntityAITaskEntry)itr.next();
            EntityAIBase entityaibase1 = entityaitaskentry.action;
            if(entityaibase1 instanceof EntityAIMoveTowardsVampire) {
               task = entityaibase1;
               break;
            }
         }

         if(task != null) {
            creature.tasks.removeTask(task);
         }
      }

   }

   public void onLivingUpdate(World world, EntityLivingBase entity, LivingUpdateEvent event, int amplifier, int duration) {
      if(this.canApplyToEntity(entity, amplifier)) {
         if(!world.isRemote) {
            if(entity instanceof EntityCreeper) {
               ((EntityCreeper)entity).setCreeperState(-1);
            }

            if(amplifier >= 4 && duration <= 1 && entity instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)entity;
               player.addPotionEffect(new PotionEffect(Witchery.Potions.QUEASY.id, TimeUtil.secsToTicks(90), 0, true));
            }
         }

         if(entity.ticksExisted % 20 != 2 || !isVillager(entity) || amplifier < 5) {
            entity.motionY = -0.2D;
         }
      }

   }

   private boolean canApplyToEntity(EntityLivingBase entity, int amplifier) {
      return entity instanceof IBossDisplayData?false:(amplifier >= 5 && isVillager(entity)?false:!(entity instanceof EntityPlayer) || amplifier >= 2);
   }

   public static boolean isVillager(Entity entity) {
      return entity instanceof EntityVillager || entity instanceof EntityVillageGuard;
   }

   public void onLivingHurt(World world, EntityLivingBase entity, LivingHurtEvent event, int amplifier) {
      if(!world.isRemote && amplifier >= 4 && event.ammount >= 1.0F) {
         entity.removePotionEffect(super.id);
      }

   }

   public boolean handleAllHurtEvents() {
      return false;
   }
}
