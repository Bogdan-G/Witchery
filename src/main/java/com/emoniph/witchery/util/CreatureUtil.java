package com.emoniph.witchery.util;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityBabaYaga;
import com.emoniph.witchery.entity.EntityBolt;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.entity.EntityImp;
import com.emoniph.witchery.entity.EntityLeonard;
import com.emoniph.witchery.entity.EntityLilith;
import com.emoniph.witchery.entity.EntityLordOfTorment;
import com.emoniph.witchery.entity.EntityMandrake;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.entity.EntityReflection;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.entity.EntityTreefyd;
import com.emoniph.witchery.entity.EntityVampire;
import com.emoniph.witchery.entity.EntityVillagerWere;
import com.emoniph.witchery.entity.EntityWolfman;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemVampireClothes;
import com.emoniph.witchery.util.BoltDamageSource;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.EntityDamageSourceIndirectSilver;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.InvUtil;
import com.emoniph.witchery.util.TransformCreature;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class CreatureUtil {

   private static Class classBloodMagicDemon;


   public static boolean isDemonic(Entity entity) {
      if(entity != null) {
         if(entity instanceof EntityDemon || entity instanceof EntityGhast || entity instanceof EntityBlaze || entity instanceof EntityMagmaCube || entity instanceof EntityLeonard || entity instanceof EntityLordOfTorment || entity instanceof EntityImp || entity instanceof EntityLilith || entity instanceof EntityWither) {
            EntityLiving living = (EntityLiving)entity;
            return true;
         }

         if(entity instanceof EntityPlayer) {
            return false;
         }

         if(isModDemon(entity)) {
            return true;
         }
      }

      return false;
   }

   private static boolean isModDemon(Entity entity) {
      if(classBloodMagicDemon == null) {
         try {
            classBloodMagicDemon = Class.forName("WayofTime.alchemicalWizardry.common.entity.mob.EntityDemon");
         } catch (ClassNotFoundException var2) {
            ;
         }
      }

      return classBloodMagicDemon != null?classBloodMagicDemon.isAssignableFrom(entity.getClass()):false;
   }

   public static boolean isUndead(Entity entity) {
      if(entity != null) {
         if(entity instanceof EntityLiving) {
            EntityLiving player1 = (EntityLiving)entity;
            return player1.isEntityUndead();
         }

         if(entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            ExtendedPlayer playerEx = ExtendedPlayer.get(player);
            return playerEx.isVampire() || InfusedBrewEffect.getActiveBrew(player) == InfusedBrewEffect.Grave;
         }
      }

      return false;
   }

   public static boolean isInsect(EntityLivingBase entity) {
      return entity != null?entity.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD:false;
   }

   public static boolean isSpirit(EntityLivingBase entity) {
      return entity == null?false:entity instanceof EntityMandrake || entity instanceof EntityHornedHuntsman || entity instanceof EntityTreefyd || entity instanceof EntityNightmare || entity instanceof EntitySpirit;
   }

   public static EntityLiving spawnWithEgg(EntityLiving entity, boolean requirePersistance) {
      if(entity != null) {
         entity.onSpawnWithEgg((IEntityLivingData)null);
         if(requirePersistance) {
            entity.func_110163_bv();
         }
      }

      return entity;
   }

   public static boolean isWitch(Entity entity) {
      if(entity != null) {
         if(entity instanceof EntityWitch || entity instanceof EntityBabaYaga) {
            return true;
         }

         if(entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if(InvUtil.hasItem(player.inventory, Witchery.Items.POPPET, Witchery.Items.POPPET.voodooPoppet.damageValue) || InvUtil.hasItem(player.inventory, Witchery.Items.POPPET, Witchery.Items.POPPET.vampiricPoppet.damageValue) || Infusion.getInfusionID(player) == Witchery.Recipes.infusionBeast.infusionID) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isWoodenDamage(DamageSource source) {
      if(source.getSourceOfDamage() != null && source.getSourceOfDamage() instanceof EntityLivingBase) {
         EntityLivingBase boltDamage = (EntityLivingBase)source.getSourceOfDamage();
         if(boltDamage instanceof EntityHornedHuntsman && !source.isProjectile()) {
            return true;
         }

         ItemStack stack = boltDamage.getEquipmentInSlot(0);
         if(stack != null && stack.getItem() instanceof ItemSword) {
            ItemSword sword = (ItemSword)stack.getItem();
            if(sword.getToolMaterialName().equalsIgnoreCase(ToolMaterial.WOOD.toString())) {
               return true;
            }
         }
      }

      if(source instanceof BoltDamageSource) {
         BoltDamageSource boltDamage1 = (BoltDamageSource)source;
         return boltDamage1.isWooden;
      } else {
         return false;
      }
   }

   public static boolean isSilverDamage(DamageSource source) {
      if(source instanceof EntityDamageSourceIndirectSilver) {
         return true;
      } else if(source.getSourceOfDamage() != null && source.getSourceOfDamage() instanceof EntityBolt) {
         EntityBolt entity1 = (EntityBolt)source.getSourceOfDamage();
         return entity1.isSilverDamage();
      } else {
         if(!source.isProjectile() && source.getEntity() != null && source.getEntity() instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase)source.getEntity();
            ItemStack stack = entity.getHeldItem();
            if(stack != null && stack.getItem() instanceof ItemSword) {
               ItemSword sword = (ItemSword)stack.getItem();
               String materialName = sword.getToolMaterialName();
               if(materialName != null) {
                  if(materialName.equals("SILVER")) {
                     return true;
                  }

                  int colonPos = materialName.lastIndexOf(":");
                  if(colonPos >= 0 && colonPos < materialName.length()) {
                     return materialName.substring(colonPos).equals(":SILVER");
                  }
               }
            }
         }

         return false;
      }
   }

   public static boolean isWerewolf(Entity entity) {
      return isWerewolf(entity, false);
   }

   public static boolean isWerewolf(Entity entity, boolean includeUnshifted) {
      if(entity == null) {
         return false;
      } else if(entity instanceof EntityWolfman) {
         return true;
      } else if(entity instanceof EntityReflection) {
         return ((EntityReflection)entity).getModel() == 1;
      } else if(entity instanceof EntityVillagerWere) {
         return includeUnshifted;
      } else if(entity instanceof EntityPlayer) {
         ExtendedPlayer name1 = ExtendedPlayer.get((EntityPlayer)entity);
         return includeUnshifted && name1.getWerewolfLevel() > 0?true:name1.getCreatureType() == TransformCreature.WOLF || name1.getCreatureType() == TransformCreature.WOLFMAN;
      } else if(!(entity instanceof EntityLiving)) {
         return false;
      } else {
         String name = entity.getClass().getSimpleName();
         return name != null && name.toUpperCase().contains("WEREWOLF");
      }
   }

   public static boolean isVampire(Entity entity) {
      if(entity == null) {
         return false;
      } else if(entity instanceof EntityVampire) {
         return true;
      } else if(entity instanceof EntityReflection) {
         return ((EntityReflection)entity).isVampire();
      } else if(entity instanceof EntityPlayer) {
         ExtendedPlayer name1 = ExtendedPlayer.get((EntityPlayer)entity);
         return name1.isVampire();
      } else if(!(entity instanceof EntityLiving)) {
         return false;
      } else {
         String name = entity.getClass().getSimpleName();
         return name != null && name.toUpperCase().contains("VAMPIRE");
      }
   }

   public static boolean isFullMoon(World world) {
      return (double)world.getCurrentMoonPhaseFactor() == 1.0D && !world.isDaytime();
   }

   public static boolean isImmuneToDisease(EntityLivingBase livingEntity) {
      return isUndead(livingEntity) || isDemonic(livingEntity) || isWerewolf(livingEntity, true) || livingEntity instanceof IBossDisplayData || livingEntity instanceof EntityGolem;
   }

   public static boolean isImmuneToPoison(EntityLivingBase livingEntity) {
      return isWerewolf(livingEntity, false);
   }

   public static boolean checkForVampireDeath(EntityLivingBase creature, DamageSource source) {
      boolean dead = false;
      if(!source.isFireDamage() && !(source instanceof EntityUtil.DamageSourceVampireFire)) {
         if(source instanceof EntityUtil.DamageSourceSunlight) {
            dead = true;
         } else if(creature instanceof EntityPlayer && Witchery.modHooks.canVampireBeKilled((EntityPlayer)creature)) {
            dead = true;
         } else if(source != DamageSource.inWall && source != DamageSource.outOfWorld) {
            if(source.getEntity() != null && (isWerewolf(source.getEntity()) || isVampire(source.getEntity()) || source.getEntity() instanceof IBossDisplayData)) {
               dead = true;
            } else if(isWerewolf(creature, true) && isSilverDamage(source)) {
               dead = true;
            }
         } else {
            dead = true;
         }
      } else if(ItemVampireClothes.isExtendedFlameProtectionActive(creature)) {
         dead = creature.worldObj.rand.nextInt(4) == 0;
      } else if(ItemVampireClothes.isFlameProtectionActive(creature)) {
         dead = creature.worldObj.rand.nextInt(4) != 0;
      } else {
         dead = true;
      }

      if(!dead) {
         creature.setHealth(1.0F);
         if(creature instanceof EntityPlayer) {
            ((EntityPlayer)creature).getFoodStats().addExhaustion(5.0F);
         }

         if(source.isExplosion() && creature.worldObj.rand.nextInt(4) == 0) {
            creature.setFire(2);
         }

         return false;
      } else {
         return true;
      }
   }

   public static boolean isInSunlight(EntityLivingBase entity) {
      World world = entity.worldObj;
      if(world.provider.dimensionId != Config.instance().dimensionDreamID && world.provider.dimensionId != Config.instance().dimensionTormentID && !world.provider.hasNoSky && world.provider.isSurfaceWorld() && world.isDaytime()) {
         int x = MathHelper.floor_double(entity.posX);
         int y = MathHelper.floor_double(entity.posY);
         int z = MathHelper.floor_double(entity.posZ);
         BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
         return biome.biomeName.equals("Ominous Woods")?false:(world.isRaining() && biome.canSpawnLightningBolt()?false:world.canBlockSeeTheSky(x, y + MathHelper.ceiling_double_int((double)entity.height), z));
      } else {
         return false;
      }
   }
}
