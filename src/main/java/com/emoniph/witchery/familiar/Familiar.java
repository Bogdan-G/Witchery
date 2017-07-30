package com.emoniph.witchery.familiar;

import com.emoniph.witchery.entity.EntityOwl;
import com.emoniph.witchery.entity.EntityToad;
import com.emoniph.witchery.entity.EntityWitchCat;
import com.emoniph.witchery.familiar.IFamiliar;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public abstract class Familiar {

   private static final String[] NAMES_TOAD = new String[]{"Casper", "Wart", "Langston", "Croaker", "Prince Charming", "Frog-n-stien", "Randolph", "Evileye", "Churchill", "Santa", "Dillinger", "Spuds"};
   private static final String[] NAMES_CAT = new String[]{"Pyewackett", "Salem", "Gobbolino", "Sabbath", "Norris", "Crookshanks", "Binx", "Voodoo", "Raven", "Simpkin", "Fishbone", "Kismet"};
   private static final String[] NAMES_OWL = new String[]{"Archimedes", "Dumbledornithologist", "Al Travis", "Baltimore", "Cornelius", "Hadwig", "Hoot", "Merlin", "Owl Capone", "Pigwidgeon", "Athena", "Albertine"};
   private static final String FAMILIAR_TAG_KEY = "WITCFamiliar";
   private static final String FAMILIAR_UUID_MOST = "UUIDMost";
   private static final String FAMILIAR_UUID_LEAST = "UUIDLeast";
   private static final String FAMILIAR_NAME = "FamiliarName";
   private static final String FAMILIAR_TYPE = "FamiliarType";
   private static final String FAMILIAR_COLOR = "FamiliarColor";
   private static final String FAMILIAR_SUMMONED = "FamiliarSummoned";
   public static final int FAMILIAR_NONE = 0;
   public static final int FAMILIAR_CAT = 1;
   public static final int FAMILIAR_TOAD = 2;
   public static final int FAMILIAR_OWL = 3;
   private static final float REDIRECTED_DAMAGE_PCT_FAR = 0.01F;
   private static final float REDIRECTED_DAMAGE_PCT_NEAR = 0.1F;
   private static final float MAX_HEALTH = 50.0F;
   private static final float FAMILIAR_NEAR_DISTANCE_SQ = 576.0F;


   public static void bindToPlayer(EntityPlayer player, EntityTameable familiarEntity) {
      if(canBecomeFamiliar((EntityTameable)familiarEntity) && TameableUtil.isOwner((EntityTameable)familiarEntity, player)) {
         NBTTagCompound nbtTag = Infusion.getNBT(player);
         if(nbtTag != null) {
            EntityTameable currentFamiliar = getFamiliarEntity(player);
            if(currentFamiliar != null) {
               ((IFamiliar)currentFamiliar).clearFamiliar();
            }

            if(familiarEntity instanceof EntityOcelot) {
               EntityOcelot familiar = (EntityOcelot)familiarEntity;
               EntityWitchCat nbtFamiliar = new EntityWitchCat(familiar.worldObj);
               nbtFamiliar.cloneOcelot(familiar);
               nbtFamiliar.setTameSkin(1);
               familiar.setDead();
               nbtFamiliar.worldObj.spawnEntityInWorld(nbtFamiliar);
               nbtFamiliar.worldObj.setEntityState(nbtFamiliar, (byte)7);
               familiarEntity = nbtFamiliar;
            }

            IFamiliar familiar1 = (IFamiliar)familiarEntity;
            NBTTagCompound nbtFamiliar1 = new NBTTagCompound();
            nbtFamiliar1.setLong("UUIDMost", ((EntityTameable)familiarEntity).getUniqueID().getMostSignificantBits());
            nbtFamiliar1.setLong("UUIDLeast", ((EntityTameable)familiarEntity).getUniqueID().getLeastSignificantBits());
            String name = "Familiar";
            if(familiarEntity instanceof EntityOwl) {
               name = NAMES_OWL[player.worldObj.rand.nextInt(NAMES_OWL.length)];
               nbtFamiliar1.setInteger("FamiliarType", 3);
               nbtFamiliar1.setByte("FamiliarColor", Byte.valueOf((byte)((EntityOwl)familiar1).getFeatherColor()).byteValue());
            } else if(familiarEntity instanceof EntityToad) {
               name = NAMES_TOAD[player.worldObj.rand.nextInt(NAMES_OWL.length)];
               nbtFamiliar1.setInteger("FamiliarType", 2);
               nbtFamiliar1.setByte("FamiliarColor", Byte.valueOf((byte)((EntityToad)familiar1).getSkinColor()).byteValue());
            } else if(familiarEntity instanceof EntityOcelot) {
               name = NAMES_CAT[player.worldObj.rand.nextInt(NAMES_OWL.length)];
               nbtFamiliar1.setInteger("FamiliarType", 1);
               nbtFamiliar1.setByte("FamiliarColor", Byte.valueOf((byte)0).byteValue());
            }

            if(!((EntityTameable)familiarEntity).hasCustomNameTag() && name != null && !name.isEmpty()) {
               ((EntityTameable)familiarEntity).setCustomNameTag(name);
            }

            nbtFamiliar1.setString("FamiliarName", ((EntityTameable)familiarEntity).getCustomNameTag());
            nbtFamiliar1.setByte("FamiliarSummoned", Byte.valueOf((byte)1).byteValue());
            nbtTag.setTag("WITCFamiliar", nbtFamiliar1);
            familiar1.setMaxHealth(50.0F);
         }
      }

   }

   public static boolean canBecomeFamiliar(EntityTameable familiarEntity) {
      return familiarEntity != null && familiarEntity.isTamed() && (familiarEntity instanceof EntityWitchCat || familiarEntity instanceof EntityOcelot || familiarEntity instanceof EntityToad || familiarEntity instanceof EntityOwl);
   }

   public static EntityTameable getFamiliarEntityByID(EntityPlayer player, UUID uuidFamiliar) {
      if(uuidFamiliar != null) {
         List list = player.worldObj.loadedEntityList;

         for(int server = 0; server < list.size(); ++server) {
            Object arr$ = list.get(server);
            if(arr$ instanceof EntityTameable) {
               EntityTameable len$ = (EntityTameable)arr$;
               if(len$.getUniqueID().equals(uuidFamiliar)) {
                  return len$;
               }
            }
         }

         if(!player.worldObj.isRemote) {
            MinecraftServer var12 = MinecraftServer.getServer();
            WorldServer[] var13 = var12.worldServers;
            int var14 = var13.length;

            for(int i$ = 0; i$ < var14; ++i$) {
               WorldServer worldServer = var13[i$];
               List list2 = worldServer.loadedEntityList;

               for(int i = 0; i < list2.size(); ++i) {
                  Object obj = list2.get(i);
                  if(obj instanceof EntityTameable) {
                     EntityTameable tameableEntity = (EntityTameable)obj;
                     if(tameableEntity.getUniqueID().equals(uuidFamiliar)) {
                        return tameableEntity;
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   public static EntityTameable getFamiliarEntity(EntityPlayer player) {
      UUID uuidFamiliar = getFamiliarEntityID(player);
      EntityTameable familiar = getFamiliarEntityByID(player, uuidFamiliar);
      return familiar;
   }

   public static UUID getFamiliarEntityID(EntityPlayer player) {
      if(player != null) {
         NBTTagCompound nbtTag = Infusion.getNBT(player);
         if(nbtTag != null && nbtTag.hasKey("WITCFamiliar")) {
            NBTTagCompound nbtFamiliar = nbtTag.getCompoundTag("WITCFamiliar");
            if(nbtFamiliar != null && nbtFamiliar.hasKey("UUIDMost") && nbtFamiliar.hasKey("UUIDLeast")) {
               UUID uuidFamiliar = new UUID(nbtFamiliar.getLong("UUIDMost"), nbtFamiliar.getLong("UUIDLeast"));
               return uuidFamiliar;
            }
         }
      }

      return null;
   }

   public static boolean isPlayerBoundToFamiliar(EntityPlayer player, EntityTameable familiar) {
      if(player != null && familiar != null) {
         NBTTagCompound nbtTag = Infusion.getNBT(player);
         if(nbtTag != null && nbtTag.hasKey("WITCFamiliar")) {
            NBTTagCompound nbtFamiliar = nbtTag.getCompoundTag("WITCFamiliar");
            if(nbtFamiliar != null && nbtFamiliar.hasKey("UUIDMost") && nbtFamiliar.hasKey("UUIDLeast")) {
               UUID uuidFamiliar = new UUID(nbtFamiliar.getLong("UUIDMost"), nbtFamiliar.getLong("UUIDLeast"));
               return uuidFamiliar.equals(familiar.getUniqueID());
            }
         }
      }

      return false;
   }

   public static Familiar.FamiliarOwner getOwnerForFamiliar(EntityTameable familiar) {
      if(familiar != null && !familiar.worldObj.isRemote && familiar.isTamed()) {
         EntityLivingBase owner = familiar.getOwner();
         if(owner != null && owner instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)owner;
            UUID uuidFamiliar = getFamiliarEntityID(player);
            if(uuidFamiliar != null && uuidFamiliar.equals(familiar.getUniqueID())) {
               return new Familiar.FamiliarOwner(player, true);
            }

            return new Familiar.FamiliarOwner(player, false);
         }
      }

      return new Familiar.FamiliarOwner((EntityPlayer)null, false);
   }

   public static boolean hasActiveCurseMasteryFamiliar(EntityPlayer player) {
      int familiarType = getActiveFamiliarType(player);
      return familiarType == 1;
   }

   public static boolean hasActiveBrewMasteryFamiliar(EntityPlayer player) {
      int familiarType = getActiveFamiliarType(player);
      return familiarType == 2;
   }

   public static boolean hasActiveBroomMasteryFamiliar(EntityPlayer player) {
      int familiarType = getActiveFamiliarType(player);
      return familiarType == 3;
   }

   public static boolean hasActiveFamiliar(EntityPlayer player) {
      int familiarType = getActiveFamiliarType(player);
      return familiarType > 0;
   }

   public static int getActiveFamiliarType(EntityPlayer player) {
      if(player != null && !player.worldObj.isRemote) {
         NBTTagCompound nbtTag = Infusion.getNBT(player);
         if(nbtTag != null && nbtTag.hasKey("WITCFamiliar")) {
            NBTTagCompound nbtFamiliar = nbtTag.getCompoundTag("WITCFamiliar");
            if(nbtFamiliar.hasKey("FamiliarSummoned") && nbtFamiliar.hasKey("FamiliarType") && nbtFamiliar.hasKey("FamiliarName")) {
               byte summoned = nbtFamiliar.getByte("FamiliarSummoned");
               if(summoned == 1) {
                  int type = nbtFamiliar.getInteger("FamiliarType");
                  return type;
               }
            }
         }
      }

      return 0;
   }

   public static void handlePlayerHurt(LivingHurtEvent event, EntityPlayer player) {
      World world = event.entityLiving.worldObj;
      if(!world.isRemote && !event.isCanceled()) {
         UUID familiarID = getFamiliarEntityID(player);
         if(familiarID != null) {
            float totalDamage = event.ammount;
            float redirectedDamage = totalDamage * 0.01F;
            EntityTameable familiar = getFamiliarEntityByID(player, familiarID);
            if(familiar != null) {
               if(familiar.getDistanceSqToEntity(player) <= 576.0D) {
                  redirectedDamage = totalDamage * 0.1F;
               }

               if(redirectedDamage >= 1.0F) {
                  familiar.attackEntityFrom(event.source, redirectedDamage);
               }
            }

            event.ammount -= redirectedDamage;
         }
      }

   }

   public static void handleLivingDeath(LivingDeathEvent event) {
      World world = event.entityLiving.worldObj;
      if(!world.isRemote && !event.isCanceled()) {
         if(event.entityLiving instanceof EntityTameable) {
            EntityTameable player = (EntityTameable)event.entityLiving;
            if(couldBeFamiliar(player)) {
               Familiar.FamiliarOwner familiar = getOwnerForFamiliar(player);
               if(familiar.player != null && familiar.isOwner()) {
                  NBTTagCompound nbtTag = Infusion.getNBT(familiar.player);
                  familiar.player.attackEntityFrom(DamageSource.magic, familiar.player.getMaxHealth() * 2.0F);
                  dismissFamiliar(familiar.player, player);
                  event.setCanceled(true);
               } else if(familiar.player == null) {
                  player.setHealth(1.0F);
                  event.setCanceled(true);
               }
            }
         } else if(event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player1 = (EntityPlayer)event.entityLiving;
            EntityTameable familiar1 = getFamiliarEntity(player1);
            if(familiar1 != null && !familiar1.isDead) {
               dismissFamiliar(player1, familiar1);
            }
         }
      }

   }

   public static void dismissFamiliar(EntityPlayer player, EntityTameable familiar) {
      if(player != null && familiar != null && !player.worldObj.isRemote && isPlayerBoundToFamiliar(player, familiar)) {
         NBTTagCompound nbtTag = Infusion.getNBT(player);
         if(nbtTag != null && nbtTag.hasKey("WITCFamiliar")) {
            NBTTagCompound nbtFamiliar = nbtTag.getCompoundTag("WITCFamiliar");
            nbtFamiliar.setString("FamiliarName", familiar.getCustomNameTag());
            nbtFamiliar.setByte("FamiliarSummoned", Byte.valueOf((byte)0).byteValue());
            if(familiar instanceof EntityOwl) {
               nbtFamiliar.setByte("FamiliarColor", Byte.valueOf((byte)((EntityOwl)familiar).getFeatherColor()).byteValue());
            } else if(familiar instanceof EntityToad) {
               nbtFamiliar.setByte("FamiliarColor", Byte.valueOf((byte)((EntityToad)familiar).getSkinColor()).byteValue());
            }

            ParticleEffect.INSTANT_SPELL.send(SoundEffect.MOB_ENDERMEN_PORTAL, familiar, 1.0D, 1.0D, 16);
            familiar.setDead();
         }
      }

   }

   public static String getFamiliarName(EntityPlayer player) {
      NBTTagCompound nbtTag = Infusion.getNBT(player);
      if(nbtTag != null && nbtTag.hasKey("WITCFamiliar")) {
         NBTTagCompound nbtFamiliar = nbtTag.getCompoundTag("WITCFamiliar");
         if(nbtFamiliar.hasKey("FamiliarSummoned") && nbtFamiliar.hasKey("FamiliarType") && nbtFamiliar.hasKey("FamiliarName")) {
            byte summoned = nbtFamiliar.getByte("FamiliarSummoned");
            String name = nbtFamiliar.getString("FamiliarName");
            return name;
         }
      }

      return null;
   }

   public static EntityTameable summonFamiliar(EntityPlayer player, double x, double y, double z) {
      if(player != null && !player.worldObj.isRemote) {
         NBTTagCompound nbtTag = Infusion.getNBT(player);
         if(nbtTag != null && nbtTag.hasKey("WITCFamiliar")) {
            NBTTagCompound nbtFamiliar = nbtTag.getCompoundTag("WITCFamiliar");
            if(nbtFamiliar.hasKey("FamiliarSummoned") && nbtFamiliar.hasKey("FamiliarType") && nbtFamiliar.hasKey("FamiliarName")) {
               byte summoned = nbtFamiliar.getByte("FamiliarSummoned");
               if(summoned == 0) {
                  String name = nbtFamiliar.getString("FamiliarName");
                  int type = nbtFamiliar.getInteger("FamiliarType");
                  byte color = nbtFamiliar.getByte("FamiliarColor");
                  Object familiar = null;
                  switch(type) {
                  case 1:
                     familiar = new EntityWitchCat(player.worldObj);
                     break;
                  case 2:
                     familiar = new EntityToad(player.worldObj);
                     ((EntityToad)familiar).setSkinColor(color);
                     break;
                  case 3:
                     familiar = new EntityOwl(player.worldObj);
                     ((EntityOwl)familiar).setFeatherColor(color);
                     break;
                  default:
                     return null;
                  }

                  ((EntityTameable)familiar).setTamed(true);
                  TameableUtil.setOwner((EntityTameable)familiar, player);
                  ((EntityTameable)familiar).setCustomNameTag(name);
                  ((IFamiliar)familiar).setMaxHealth(50.0F);
                  ((EntityTameable)familiar).setLocationAndAngles(x, y, z, 0.0F, 0.0F);
                  player.worldObj.spawnEntityInWorld((Entity)familiar);
                  nbtFamiliar.setLong("UUIDMost", ((EntityTameable)familiar).getUniqueID().getMostSignificantBits());
                  nbtFamiliar.setLong("UUIDLeast", ((EntityTameable)familiar).getUniqueID().getLeastSignificantBits());
                  ParticleEffect.INSTANT_SPELL.send(SoundEffect.MOB_ENDERMEN_PORTAL, (Entity)familiar, 1.0D, 1.0D, 16);
                  nbtFamiliar.setByte("FamiliarSummoned", Byte.valueOf((byte)1).byteValue());
                  return (EntityTameable)familiar;
               }
            }
         }
      }

      return null;
   }

   public static boolean couldBeFamiliar(EntityTameable entity) {
      if(entity instanceof IFamiliar) {
         IFamiliar familiar = (IFamiliar)entity;
         return familiar.isFamiliar();
      } else {
         return false;
      }
   }


   public static class FamiliarOwner {

      private final EntityPlayer player;
      private final boolean owner;


      public FamiliarOwner(EntityPlayer player, boolean owner) {
         this.player = player;
         this.owner = owner;
      }

      public EntityPlayer getPlayer() {
         return this.player;
      }

      public boolean isOwner() {
         return this.owner;
      }

      public EntityPlayer getCurrentOwner() {
         return this.owner?this.player:null;
      }
   }
}
