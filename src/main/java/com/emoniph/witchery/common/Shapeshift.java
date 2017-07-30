package com.emoniph.witchery.common;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionResizing;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityWolfman;
import com.emoniph.witchery.infusion.infusions.InfusionInfernal;
import com.emoniph.witchery.item.ItemHunterClothes;
import com.emoniph.witchery.network.PacketPushTarget;
import com.emoniph.witchery.network.PacketSyncEntitySize;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EntitySizeInfo;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import com.emoniph.witchery.util.TransformCreature;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class Shapeshift {

   public static final Shapeshift INSTANCE = new Shapeshift();
   public final Shapeshift.StatBoost[] boostWolfman = new Shapeshift.StatBoost[]{new Shapeshift.StatBoost(0.0F, 0.0D, 0.0D, 0, 0.0F, 0.0F, 0, 4.0F), new Shapeshift.StatBoost(0.0F, 0.0D, 0.0D, 0, 0.0F, 0.0F, 0, 4.0F), new Shapeshift.StatBoost(0.0F, 0.0D, 0.0D, 0, 0.0F, 0.0F, 0, 3.0F), new Shapeshift.StatBoost(0.0F, 0.0D, 0.0D, 0, 0.0F, 0.0F, 0, 3.0F), new Shapeshift.StatBoost(0.0F, 0.0D, 0.0D, 0, 0.0F, 0.0F, 0, 3.0F), new Shapeshift.StatBoost(0.2F, 0.20000000298023224D, 0.20000000298023224D, 20, 4.0F, 3.0F, 3, 2.0F), new Shapeshift.StatBoost(0.2F, 0.30000001192092896D, 0.20000000298023224D, 20, 4.0F, 3.0F, 4, 2.0F), new Shapeshift.StatBoost(0.4F, 0.4000000059604645D, 0.4000000059604645D, 20, 5.0F, 4.0F, 5, 2.0F), new Shapeshift.StatBoost(0.4F, 0.5D, 0.4000000059604645D, 30, 6.0F, 4.0F, 6, 2.0F), new Shapeshift.StatBoost(0.5F, 0.6000000238418579D, 0.6000000238418579D, 40, 7.0F, 5.0F, 7, 2.0F), new Shapeshift.StatBoost(0.5F, 0.6000000238418579D, 0.6000000238418579D, 40, 7.0F, 5.0F, 7, 2.0F)};
   public final Shapeshift.StatBoost[] boostWolf = new Shapeshift.StatBoost[]{new Shapeshift.StatBoost(0.0F, 0.0D, 0.0D, 0, 0.0F, 0.0F, 0, 4.0F), new Shapeshift.StatBoost(0.5F, 0.20000000298023224D, 0.20000000298023224D, 0, 1.0F, 0.0F, 2, 4.0F), new Shapeshift.StatBoost(0.5F, 0.20000000298023224D, 0.20000000298023224D, 0, 1.0F, 0.0F, 2, 3.0F), new Shapeshift.StatBoost(0.75F, 0.20000000298023224D, 0.30000001192092896D, 0, 2.0F, 0.0F, 2, 3.0F), new Shapeshift.StatBoost(0.75F, 0.20000000298023224D, 0.4000000059604645D, 0, 2.0F, 0.0F, 3, 3.0F), new Shapeshift.StatBoost(0.75F, 0.20000000298023224D, 0.5D, 0, 2.0F, 0.0F, 3, 2.0F), new Shapeshift.StatBoost(1.0F, 0.20000000298023224D, 0.6000000238418579D, 0, 2.0F, 1.0F, 3, 2.0F), new Shapeshift.StatBoost(1.25F, 0.30000001192092896D, 0.699999988079071D, 4, 2.0F, 1.0F, 4, 2.0F), new Shapeshift.StatBoost(1.5F, 0.30000001192092896D, 0.800000011920929D, 8, 3.0F, 2.0F, 4, 2.0F), new Shapeshift.StatBoost(1.75F, 0.30000001192092896D, 0.8999999761581421D, 12, 3.0F, 3.0F, 5, 2.0F), new Shapeshift.StatBoost(1.75F, 0.30000001192092896D, 1.0D, 12, 3.0F, 3.0F, 5, 2.0F)};
   public final Shapeshift.StatBoost[] boostVampire = new Shapeshift.StatBoost[]{new Shapeshift.StatBoost(0.0F), new Shapeshift.StatBoost(1.0F), new Shapeshift.StatBoost(1.0F), new Shapeshift.StatBoost(1.0F), new Shapeshift.StatBoost(2.0F), new Shapeshift.StatBoost(2.0F), new Shapeshift.StatBoost(2.0F), new Shapeshift.StatBoost(3.0F), new Shapeshift.StatBoost(3.0F), new Shapeshift.StatBoost(3.0F), new Shapeshift.StatBoost(3.0F)};
   public final Shapeshift.StatBoost[] boostBat = new Shapeshift.StatBoost[]{new Shapeshift.StatBoost(0.0F), (new Shapeshift.StatBoost(-6.0F)).setFlying(true), (new Shapeshift.StatBoost(-6.0F)).setFlying(true), (new Shapeshift.StatBoost(-6.0F)).setFlying(true), (new Shapeshift.StatBoost(-6.0F)).setFlying(true), (new Shapeshift.StatBoost(-6.0F)).setFlying(true), (new Shapeshift.StatBoost(-6.0F)).setFlying(true), (new Shapeshift.StatBoost(-6.0F)).setFlying(true), (new Shapeshift.StatBoost(-6.0F)).setFlying(true), (new Shapeshift.StatBoost(-6.0F)).setFlying(true), (new Shapeshift.StatBoost(-6.0F)).setFlying(true)};
   public static final AttributeModifier SPEED_MODIFIER = new AttributeModifier(UUID.fromString("10536417-7AA6-4033-A598-8E934CA77D98"), "witcheryWolfSpeed", 0.5D, 2);
   public static final AttributeModifier DAMAGE_MODIFIER = new AttributeModifier(UUID.fromString("46C5271C-193B-4D41-9CAB-D071AAEE9D4A"), "witcheryWolfDamage", 6.0D, 2);
   public static final AttributeModifier HEALTH_MODIFIER = new AttributeModifier(UUID.fromString("615920F9-6675-4779-8B18-6A62A3671E94"), "witcheryWolfHealth", 40.0D, 0);
   private static Field fieldExperienceValue;


   public void initCurrentShift(EntityPlayer player) {
      if(!player.worldObj.isRemote) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         EntitySizeInfo sizeInfo = new EntitySizeInfo(player);
         PotionResizing.setEntitySize(player, sizeInfo.defaultWidth, sizeInfo.defaultHeight);
         player.stepHeight = sizeInfo.stepSize;
         player.eyeHeight = sizeInfo.eyeHeight;
         BaseAttributeMap playerAttributes = player.getAttributeMap();
         Shapeshift.StatBoost boost = this.getStatBoost(player, playerEx);
         if(boost != null) {
            this.applyModifier(SharedMonsterAttributes.movementSpeed, SPEED_MODIFIER, (double)boost.speed, playerAttributes);
            this.applyModifier(SharedMonsterAttributes.attackDamage, DAMAGE_MODIFIER, (double)boost.damage, playerAttributes);
            this.applyModifier(SharedMonsterAttributes.maxHealth, HEALTH_MODIFIER, (double)boost.health, playerAttributes);
         } else {
            this.removeModifier(SharedMonsterAttributes.movementSpeed, SPEED_MODIFIER, playerAttributes);
            this.removeModifier(SharedMonsterAttributes.attackDamage, DAMAGE_MODIFIER, playerAttributes);
            this.removeModifier(SharedMonsterAttributes.maxHealth, HEALTH_MODIFIER, playerAttributes);
         }

         if(!player.capabilities.isCreativeMode) {
            player.capabilities.allowFlying = boost != null && boost.flying;
            if(!player.capabilities.allowFlying && player.capabilities.isFlying) {
               player.capabilities.isFlying = false;
            } else if(player.capabilities.allowFlying) {
               player.capabilities.isFlying = true;
            }

            player.sendPlayerAbilities();
         }

         Witchery.packetPipeline.sendToAll((IMessage)(new PacketSyncEntitySize(player)));
         Witchery.packetPipeline.sendTo((IMessage)(new PacketSyncEntitySize(player)), player);
      }

   }

   public void updatePlayerState(EntityPlayer player, ExtendedPlayer playerEx) {
      if(playerEx.getCreatureType() == TransformCreature.BAT) {
         if(player.capabilities.isFlying) {
            player.fallDistance = 0.0F;
         }

         if(!player.capabilities.allowFlying && !player.capabilities.isCreativeMode) {
            player.capabilities.allowFlying = true;
            player.sendPlayerAbilities();
         }
      }

   }

   public float updateFallState(EntityPlayer player, float distance) {
      ExtendedPlayer playerEx = ExtendedPlayer.get(player);
      Shapeshift.StatBoost boost = this.getStatBoost(player, playerEx);
      return boost != null?(boost.fall == -1?0.0F:Math.max(0.0F, distance - (float)boost.fall)):distance;
   }

   public float getResistance(EntityPlayer player, ExtendedPlayer playerEx) {
      Shapeshift.StatBoost boost = this.getStatBoost(player, playerEx);
      return boost != null?boost.resistance:0.0F;
   }

   public float getDamageCap(EntityPlayer player, ExtendedPlayer playerEx) {
      Shapeshift.StatBoost boost = this.getStatBoost(player, playerEx);
      return boost != null?boost.damageCap:0.0F;
   }

   public void updateJump(EntityPlayer player) {
      ExtendedPlayer playerEx = ExtendedPlayer.get(player);
      Shapeshift.StatBoost boost = this.getStatBoost(player, playerEx);
      if(boost != null) {
         player.motionY += boost.jump;
         if(player.isSprinting()) {
            float f = player.rotationYaw * 0.017453292F;
            player.motionX -= (double)MathHelper.sin(f) * boost.leap;
            player.motionZ += (double)MathHelper.cos(f) * boost.leap;
         }
      }

   }

   public void updateChargeDamage(LivingHurtEvent event, EntityPlayer player, ExtendedPlayer playerEx) {
      if(this.isWolfAnimalForm(playerEx)) {
         if(this.itemHasDamageAttribute(player.getHeldItem())) {
            event.ammount = 2.0F;
         } else {
            Shapeshift.StatBoost ACCELERATION = this.getStatBoost(player, playerEx);
            if(ACCELERATION != null && player.isSprinting()) {
               event.ammount += ACCELERATION.damage;
            }
         }
      }

      if(playerEx.getVampireLevel() >= 3 && playerEx.getCreatureType() == TransformCreature.NONE && player.isSneaking()) {
         double ACCELERATION1 = 3.0D;
         Vec3 look = player.getLookVec();
         double motionX = look.xCoord * 0.6D * 3.0D;
         double motionY = 0.8999999999999999D;
         double motionZ = look.zCoord * 0.6D * 3.0D;
         if(event.entityLiving instanceof EntityPlayer) {
            EntityPlayer targetPlayer = (EntityPlayer)event.entityLiving;
            Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(motionX, 0.8999999999999999D, motionZ)), targetPlayer);
         } else {
            event.entityLiving.motionX = motionX;
            event.entityLiving.motionY = 0.8999999999999999D;
            event.entityLiving.motionZ = motionZ;
         }
      }

   }

   private boolean itemHasDamageAttribute(ItemStack item) {
      if(item == null) {
         return false;
      } else {
         Multimap modifiers = item.getAttributeModifiers();
         if(modifiers == null) {
            return false;
         } else {
            boolean hasDamage = modifiers.containsKey(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
            return hasDamage;
         }
      }
   }

   public void rendArmor(EntityLivingBase victim, EntityPlayer player, ExtendedPlayer playerEx) {
      if(playerEx.getCreatureType() == TransformCreature.WOLFMAN && playerEx.getWerewolfLevel() >= 9) {
         int slot = 1 + victim.worldObj.rand.nextInt(4);
         ItemStack armor = victim.getEquipmentInSlot(slot);
         if(armor != null) {
            boolean ripOffArmor = !armor.isItemStackDamageable();
            if(!ripOffArmor) {
               int droppedItem = armor.getItemDamage();
               int rendAmount = (int)Math.ceil((double)((float)armor.getMaxDamage() * 0.25F));
               armor.damageItem(rendAmount, player);
               if(victim instanceof EntityPlayer && armor.getItem() instanceof ItemArmor) {
                  ItemArmor armorItem = (ItemArmor)armor.getItem();
                  armorItem.onArmorTick(victim.worldObj, (EntityPlayer)victim, armor);
               }

               ripOffArmor = armor.getItemDamage() <= droppedItem;
            }

            if(ripOffArmor && victim instanceof EntityPlayer) {
               victim.setCurrentItemOrArmor(slot, (ItemStack)null);
               EntityItem droppedItem1 = victim.entityDropItem(armor, 1.0F);
               if(droppedItem1 != null) {
                  droppedItem1.delayBeforeCanPickup = TimeUtil.secsToTicks(5);
               }
            }
         }
      }

   }

   public void processCreatureKilled(LivingDeathEvent event, EntityPlayer attacker, ExtendedPlayer playerEx) {
      if(this.isWolfAnimalForm(playerEx) && playerEx.getWerewolfLevel() >= 4 && !CreatureUtil.isUndead(event.entityLiving)) {
         ParticleEffect.REDDUST.send(attacker.worldObj.rand.nextInt(3) == 0?SoundEffect.WITCHERY_MOB_WOLFMAN_EAT:SoundEffect.NONE, event.entityLiving, 1.0D, 2.0D, 16);
         attacker.getFoodStats().addStats(8, 0.8F);
      }

   }

   public void processDigging(HarvestDropsEvent event, EntityPlayer player, ExtendedPlayer playerEx) {
      if(playerEx.getCreatureType() == TransformCreature.WOLF && playerEx.getWerewolfLevel() >= 3 && event.drops.size() == 1 && event.drops.get(0) != null && ((ItemStack)event.drops.get(0)).getItem() == Item.getItemFromBlock(Blocks.dirt)) {
         long lastFind = playerEx.getLastBoneFind();
         long serverTime = MinecraftServer.getSystemTimeMillis();
         if(lastFind + TimeUtil.secsToMillisecs(60) < serverTime && player.worldObj.rand.nextInt(20) == 0) {
            playerEx.setLastBoneFind(serverTime);
            event.drops.add(new ItemStack(Items.bone, player.worldObj.rand.nextInt(5) == 0?2:1));
         }
      }

   }

   public void checkForHowling(EntityPlayer player, ExtendedPlayer playerEx) {
      if(playerEx.getWerewolfLevel() == 6 && this.isWolfAnimalForm(playerEx) && playerEx.getWolfmanQuestState() == ExtendedPlayer.QuestState.STARTED && !player.worldObj.isDaytime()) {
         int var13 = MathHelper.floor_double(player.posX) >> 4;
         int z = MathHelper.floor_double(player.posZ) >> 4;
         SoundEffect.WITCHERY_MOB_WOLFMAN_HOWL.playAtPlayer(player.worldObj, player, 1.0F);
         if(playerEx.storeWolfmanQuestChunk(var13, z)) {
            playerEx.increaseWolfmanQuestCounter();
         } else {
            ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.werewolf.chunkvisited", new Object[0]);
         }
      } else {
         long lastHowl;
         long serverTime;
         if(playerEx.getCreatureType() == TransformCreature.WOLF && playerEx.getWerewolfLevel() >= 8) {
            lastHowl = playerEx.getLastHowl();
            serverTime = MinecraftServer.getSystemTimeMillis();
            if(!player.capabilities.isCreativeMode && lastHowl + TimeUtil.secsToMillisecs(60) >= serverTime) {
               SoundEffect.NOTE_SNARE.playAtPlayer(player.worldObj, player);
            } else {
               SoundEffect.WITCHERY_MOB_WOLFMAN_HOWL.playAtPlayer(player.worldObj, player, 1.0F);
               playerEx.setLastHowl(serverTime);

               for(int var14 = 0; var14 < 2 + player.worldObj.rand.nextInt(playerEx.getWerewolfLevel() - 7); ++var14) {
                  EntityCreature creature = InfusionInfernal.spawnCreature(player.worldObj, EntityWolf.class, (int)player.posX, (int)player.posY, (int)player.posZ, player.getLastAttacker(), 1, 6, ParticleEffect.SMOKE, SoundEffect.NONE);
                  if(creature != null) {
                     creature.addPotionEffect(new PotionEffect(Witchery.Potions.MORTAL_COIL.id, TimeUtil.secsToTicks(10)));
                     EntityWolf var15 = (EntityWolf)creature;
                     var15.setTamed(true);
                     var15.func_152115_b(player.getUniqueID().toString());
                     if(fieldExperienceValue == null) {
                        fieldExperienceValue = ReflectionHelper.findField(EntityLiving.class, new String[]{"experienceValue", "field_70728_aV", "aV"});
                     }

                     try {
                        if(fieldExperienceValue != null) {
                           fieldExperienceValue.set(var15, Integer.valueOf(0));
                        }
                     } catch (IllegalAccessException var12) {
                        ;
                     }

                     EntityUtil.setNoDrops(var15);
                  }
               }
            }
         } else if(playerEx.getCreatureType() == TransformCreature.WOLFMAN && playerEx.getWerewolfLevel() >= 7) {
            lastHowl = playerEx.getLastHowl();
            serverTime = MinecraftServer.getSystemTimeMillis();
            if(!player.capabilities.isCreativeMode && lastHowl + TimeUtil.secsToMillisecs(60) >= serverTime) {
               SoundEffect.NOTE_SNARE.playAtPlayer(player.worldObj, player);
            } else {
               SoundEffect.WITCHERY_MOB_WOLFMAN_HOWL.playAtPlayer(player.worldObj, player, 1.0F);
               playerEx.setLastHowl(serverTime);
               double radius = 16.0D;
               List entities = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, player.boundingBox.expand(radius, radius, radius));
               Iterator i$ = entities.iterator();

               while(i$.hasNext()) {
                  EntityLivingBase entity = (EntityLivingBase)i$.next();
                  if(!CreatureUtil.isWerewolf(entity, true) && !CreatureUtil.isVampire(entity)) {
                     entity.addPotionEffect(new PotionEffect(Witchery.Potions.PARALYSED.id, TimeUtil.secsToTicks(4 + player.worldObj.rand.nextInt(playerEx.getWerewolfLevel() - 6)), 3));
                  }
               }
            }
         }
      }

   }

   public void processWolfInfection(EntityLivingBase entityLiving, EntityPlayer attackingPlayer, ExtendedPlayer playerEx, float health) {
      if(playerEx.getWerewolfLevel() >= 10 && this.isWolfAnimalForm(playerEx)) {
         if(entityLiving instanceof EntityVillager) {
            if(health < entityLiving.getMaxHealth() * 0.25F && health > 0.0F && entityLiving.worldObj.rand.nextInt(4) == 1) {
               EntityVillager victim = (EntityVillager)entityLiving;
               EntityWolfman.convertToVillager(victim, victim.getProfession(), false, victim.wealth, victim.buyingList);
            }
         } else if(entityLiving instanceof EntityPlayer && Config.instance().allowPlayerToPlayerWolfInfection) {
            EntityPlayer victim1 = (EntityPlayer)entityLiving;
            ExtendedPlayer victimEx = ExtendedPlayer.get(victim1);
            if(health < entityLiving.getMaxHealth() * 0.25F && health > 0.0F && !ItemHunterClothes.isWolfProtectionActive(entityLiving) && entityLiving.worldObj.rand.nextInt(4) == 1 && (Config.instance().allowVampireWolfHybrids || !playerEx.isVampire()) && victimEx.getWerewolfLevel() == 0) {
               victimEx.setWerewolfLevel(1);
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, victim1, "witchery.werewolf.infection", new Object[0]);
            }
         }
      }

   }

   public void processWolfInfection(EntityLivingBase entityLiving, EntityWolfman attackingEntity, float health) {
      if(attackingEntity.isInfectious()) {
         if(entityLiving instanceof EntityVillager) {
            if(health < entityLiving.getMaxHealth() * 0.25F) {
               EntityVillager victim = (EntityVillager)entityLiving;
               EntityWolfman.convertToVillager(victim, victim.getProfession(), false, victim.wealth, victim.buyingList);
            }
         } else if(entityLiving instanceof EntityPlayer) {
            EntityPlayer victim1 = (EntityPlayer)entityLiving;
            ExtendedPlayer victimEx = ExtendedPlayer.get(victim1);
            if((Config.instance().allowVampireWolfHybrids || !victimEx.isVampire()) && victimEx.getWerewolfLevel() == 0) {
               victimEx.setWerewolfLevel(1);
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, victim1, "witchery.werewolf.infection", new Object[0]);
            }
         }
      }

   }

   public boolean isAnimalForm(EntityPlayer player) {
      return this.isWolfAnimalForm(ExtendedPlayer.get(player));
   }

   public boolean isWolfAnimalForm(ExtendedPlayer playerEx) {
      return playerEx.getCreatureType() == TransformCreature.WOLFMAN || playerEx.getCreatureType() == TransformCreature.WOLF;
   }

   public boolean isWolfmanAllowed(ExtendedPlayer playerEx) {
      return playerEx.getWerewolfLevel() >= 5;
   }

   public boolean canControlTransform(ExtendedPlayer playerEx) {
      return playerEx.getWerewolfLevel() >= 2;
   }

   public Shapeshift.StatBoost getStatBoost(EntityPlayer player, ExtendedPlayer playerEx) {
      TransformCreature creature = playerEx.getCreatureType();
      switch(Shapeshift.NamelessClass26779675.$SwitchMap$com$emoniph$witchery$util$TransformCreature[creature.ordinal()]) {
      case 1:
         return this.boostWolf[playerEx.getWerewolfLevel()];
      case 2:
         return this.boostWolfman[playerEx.getWerewolfLevel()];
      case 3:
         return this.boostBat[playerEx.getVampireLevel()];
      default:
         return playerEx.isVampire()?this.boostVampire[playerEx.getVampireLevel()]:null;
      }
   }

   public void applyModifier(IAttribute attribute, AttributeModifier modifier, double modification, BaseAttributeMap playerAttributes) {
      IAttributeInstance attributeInstance = playerAttributes.getAttributeInstance(attribute);
      AttributeModifier speedModifier = new AttributeModifier(modifier.getID(), modifier.getName(), modification, modifier.getOperation());
      attributeInstance.removeModifier(speedModifier);
      attributeInstance.applyModifier(speedModifier);
   }

   public void removeModifier(IAttribute attribute, AttributeModifier modifier, BaseAttributeMap playerAttributes) {
      IAttributeInstance attributeInstance = playerAttributes.getAttributeInstance(attribute);
      attributeInstance.removeModifier(modifier);
   }

   public void shiftTo(EntityPlayer player, TransformCreature creature) {
      ExtendedPlayer.get(player).setCreatureType(creature);
      this.initCurrentShift(player);
   }


   // $FF: synthetic class
   static class NamelessClass26779675 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$util$TransformCreature = new int[TransformCreature.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.WOLF.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.WOLFMAN.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.BAT.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static class StatBoost {

      public final double jump;
      public final double leap;
      public final int health;
      public final float damage;
      public final float resistance;
      public final float speed;
      public int fall;
      public final float damageCap;
      public boolean flying;


      public StatBoost(float damage) {
         this.jump = 0.0D;
         this.leap = 0.0D;
         this.health = 0;
         this.damage = damage;
         this.resistance = 0.0F;
         this.speed = 0.0F;
         this.fall = 0;
         this.damageCap = 0.0F;
      }

      public StatBoost(float speed, double jump, double leap, int health, float damage, float resistance, int fall, float damageCap) {
         this.jump = jump;
         this.leap = leap;
         this.health = health;
         this.damage = damage;
         this.resistance = resistance;
         this.speed = speed;
         this.fall = fall;
         this.damageCap = damageCap;
      }

      public Shapeshift.StatBoost setFlying(boolean active) {
         this.flying = active;
         this.fall = -1;
         return this;
      }
   }
}
