package com.emoniph.witchery.infusion.infusions;

import com.emoniph.witchery.brewing.potions.PotionEnslaved;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.lang.reflect.Field;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class InfusionInfernal extends Infusion {

   private static final int MAX_CHARGES = 20;


   public InfusionInfernal(int infusionID) {
      super(infusionID);
   }

   public IIcon getPowerBarIcon(EntityPlayer player, int index) {
      return Blocks.netherrack.getIcon(0, 0);
   }

   public void onLeftClickEntity(ItemStack itemstack, World world, EntityPlayer player, Entity otherEntity) {
      if(!world.isRemote && otherEntity instanceof EntityLivingBase) {
         EntityLivingBase entityLivingBase = (EntityLivingBase)otherEntity;
         if(player.isSneaking()) {
            if(PotionEnslaved.canCreatureBeEnslaved(entityLivingBase)) {
               EntityLiving r = (EntityLiving)entityLivingBase;
               if(PotionEnslaved.isMobEnslavedBy(r, player)) {
                  if(this.consumeCharges(world, player, 1, true)) {
                     this.trySacrificeCreature(world, player, r);
                  }
               } else if(this.consumeCharges(world, player, 5, true)) {
                  PotionEnslaved.setEnslaverForMob(r, player);
                  EntityUtil.dropAttackTarget((EntityLiving)otherEntity);
                  ParticleEffect.SPELL.send(SoundEffect.MOB_ZOMBIE_INFECT, r, 1.0D, 2.0D, 16);
               }
            } else {
               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
         } else {
            boolean var18 = true;
            if(this.consumeCharges(world, player, 1, true)) {
               int minionCount = 0;
               AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(player.posX - 50.0D, player.posY - 15.0D, player.posZ - 50.0D, player.posX + 50.0D, player.posY + 15.0D, player.posZ + 50.0D);
               Iterator i$ = world.getEntitiesWithinAABB(EntityLiving.class, bounds).iterator();

               while(i$.hasNext()) {
                  Object obj = i$.next();
                  EntityLiving nearbyLivingEntity = (EntityLiving)obj;
                  if(PotionEnslaved.isMobEnslavedBy(nearbyLivingEntity, player)) {
                     ++minionCount;
                     nearbyLivingEntity.setAttackTarget(entityLivingBase);
                     if(nearbyLivingEntity instanceof EntityGhast) {
                        try {
                           EntityGhast nearbyCreatureEntity = (EntityGhast)nearbyLivingEntity;
                           Field[] fields = EntityGhast.class.getDeclaredFields();
                           Field fieldTargetedEntity = fields[4];
                           fieldTargetedEntity.setAccessible(true);
                           fieldTargetedEntity.set(nearbyCreatureEntity, entityLivingBase);
                           Field fieldAggroCooldown = fields[5];
                           fieldAggroCooldown.setAccessible(true);
                           fieldAggroCooldown.set(nearbyCreatureEntity, Integer.valueOf(20000));
                        } catch (IllegalAccessException var16) {
                           Log.instance().warning(var16, "Exception occurred setting ghast target.");
                        } catch (Exception var17) {
                           Log.instance().debug(String.format("Exception occurred setting ghast target. %s", new Object[]{var17.toString()}));
                        }
                     }

                     if(nearbyLivingEntity instanceof EntityCreature) {
                        EntityCreature var19 = (EntityCreature)obj;
                        var19.setTarget(entityLivingBase);
                        var19.setRevengeTarget(entityLivingBase);
                        if(var19 instanceof EntityZombie || var19 instanceof EntityCreeper) {
                           var19.tasks.addTask(2, new EntityAIAttackOnCollide(var19, entityLivingBase.getClass(), 1.0D, false));
                        }
                     }
                  }
               }

               if(minionCount > 0) {
                  ParticleEffect.CRIT.send(SoundEffect.RANDOM_BREATH, entityLivingBase, 0.5D, 2.0D, 16);
               }
            }
         }
      }

   }

   private void trySacrificeCreature(World world, EntityPlayer player, EntityLiving creature) {
      CreaturePower power = CreaturePower.Registry.instance().get(creature);
      if(power != null) {
         int currentCreaturePowerID = CreaturePower.getCreaturePowerID(player);
         if(currentCreaturePowerID == power.getCreaturePowerID()) {
            int currentCharges = CreaturePower.getCreaturePowerCharges(player);
            CreaturePower.setCreaturePowerCharges(player, MathHelper.floor_double((double)Math.min(currentCharges + power.getChargesPerSacrifice(), 20)));
         } else {
            CreaturePower.setCreaturePowerID(player, power.getCreaturePowerID(), power.getChargesPerSacrifice());
         }

         syncPlayer(world, player);
         creature.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, (Entity)null), creature.getHealth() + 1.0F);
      } else {
         this.playFailSound(world, player);
      }

   }

   public void onHurt(World worldObj, EntityPlayer player, LivingHurtEvent event) {
      int creaturePowerID = CreaturePower.getCreaturePowerID(player);
      if(creaturePowerID > 0) {
         CreaturePower.Registry.instance().get(creaturePowerID).onDamage(player.worldObj, player, event);
      }

   }

   public void onFalling(World world, EntityPlayer player, LivingFallEvent event) {
      int creaturePowerID = CreaturePower.getCreaturePowerID(player);
      if(creaturePowerID > 0) {
         CreaturePower.Registry.instance().get(creaturePowerID).onFalling(world, player, event);
      }

   }

   public void onUsingItemTick(ItemStack itemstack, World world, EntityPlayer player, int countdown) {
      int elapsedTicks = this.getMaxItemUseDuration(itemstack) - countdown;
   }

   public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int countdown) {
      if(!world.isRemote) {
         int elapsedTicks = this.getMaxItemUseDuration(itemstack) - countdown;
         double MAX_TARGET_RANGE = 15.0D;
         MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 15.0D);
         int beastPowerID;
         if(player.isSneaking()) {
            if(mop != null) {
               switch(InfusionInfernal.NamelessClass796372378.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
               case 1:
                  this.playFailSound(world, player);
                  break;
               case 2:
                  if(BlockSide.TOP.isEqual(mop.sideHit)) {
                     beastPowerID = 0;
                     boolean power = true;
                     AxisAlignedBB chargesRequired = AxisAlignedBB.getBoundingBox(player.posX - 50.0D, player.posY - 15.0D, player.posZ - 50.0D, player.posX + 50.0D, player.posY + 15.0D, player.posZ + 50.0D);
                     Iterator currentCharges = world.getEntitiesWithinAABB(EntityLiving.class, chargesRequired).iterator();

                     while(currentCharges.hasNext()) {
                        Object obj = currentCharges.next();
                        EntityLiving creature = (EntityLiving)obj;
                        EntityCreature creature2 = creature instanceof EntityCreature?(EntityCreature)creature:null;
                        if(PotionEnslaved.isMobEnslavedBy(creature, player)) {
                           ++beastPowerID;
                           creature.setAttackTarget((EntityLivingBase)null);
                           creature.setRevengeTarget((EntityLivingBase)null);
                           if(creature2 != null) {
                              creature2.setTarget((Entity)null);
                           }

                           if((creature instanceof EntitySpider || !creature.getNavigator().tryMoveToXYZ((double)mop.blockX, (double)(mop.blockY + 1), (double)mop.blockZ, 1.0D)) && creature2 != null) {
                              creature2.setPathToEntity(world.getEntityPathToXYZ(creature, mop.blockX, mop.blockY + 1, mop.blockZ, 10.0F, true, false, false, true));
                           }
                        }
                     }

                     if(beastPowerID > 0) {
                        ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_POP, world, (double)mop.blockX, (double)(mop.blockY + 1), (double)mop.blockZ, 0.5D, 2.0D, 16);
                     }
                  }
               case 3:
               }
            } else {
               this.playFailSound(world, player);
            }
         } else {
            beastPowerID = CreaturePower.getCreaturePowerID(player);
            if(beastPowerID > 0) {
               CreaturePower var17 = CreaturePower.Registry.instance().get(beastPowerID);
               int var16 = var17.activateCost(world, player, elapsedTicks, mop);
               int var18 = CreaturePower.getCreaturePowerCharges(player);
               if(var18 - var16 >= 0 && this.consumeCharges(world, player, 1, true)) {
                  var17.onActivate(world, player, elapsedTicks, mop);
                  if(!player.capabilities.isCreativeMode) {
                     CreaturePower.setCreaturePowerCharges(player, var18 - var16);
                     syncPlayer(world, player);
                  }
               } else {
                  this.playFailSound(world, player);
               }
            } else {
               this.playFailSound(world, player);
            }
         }
      }

   }

   // $FF: synthetic class
   static class NamelessClass796372378 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType = new int[MovingObjectType.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.ENTITY.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.BLOCK.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.MISS.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
