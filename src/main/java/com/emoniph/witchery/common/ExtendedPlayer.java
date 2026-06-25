package com.emoniph.witchery.common;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.client.renderer.RenderReflection;
import com.emoniph.witchery.common.Shapeshift;
import com.emoniph.witchery.entity.EntityAttackBat;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.network.PacketExtendedPlayerSync;
import com.emoniph.witchery.network.PacketPartialExtendedPlayerSync;
import com.emoniph.witchery.network.PacketPlayerStyle;
import com.emoniph.witchery.network.PacketSelectPlayerAbility;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import com.emoniph.witchery.util.TransformCreature;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.village.Village;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPlayer implements IExtendedEntityProperties {

   private static final String EXT_PROP_NAME = "WitcheryExtendedPlayer";
   private final EntityPlayer player;
   private Hashtable incurablePotionEffectCache = new Hashtable();
   private static final int MAX_SKILL_LEVEL_POTION_BOTTLING = 100;
   private int skillLevelPotionBottling;
   private static final int MAX_SKILL_LEVEL_POTION_THROWING = 100;
   private int skillLevelPotionThrowing;
   public static final int MAX_HUMAN_BLOOD = 500;
   private int creatureType;
   private int werewolfLevel;
   private int vampireLevel;
   private int vampireLevelCap;
   private int spiritLevel;
   private boolean getPlayerData;
   private boolean isAstralProjecting;
   private int bloodPower;
   private int bloodReserve;
   private int vampireUltimate;
   private int vampireUltimateCharges;
   private int humanBlood;
   private int wolfmanQuestState;
   private int wolfmanQuestCounter;
   private long lastBoneFind;
   private long lastHowl;
   private ExtendedPlayer.VampirePower selectedVampirePower;
   private int vampireCooldown;
   private int vampireQuestCounter;
   private boolean vampVisionActive;
   private String lastPlayerSkin;
   @SideOnly(Side.CLIENT)
   private ThreadDownloadImageData downloadImageSkin;
   private ResourceLocation locationSkin;
   private NBTTagList cachedInventory;
   private boolean inventoryCanBeRestored;
   private static final int DEFAULT_ULTIMATE_CHARGES = 5;
   public int highlightTicks;
   public int cachedWorship;
   private final List visitedChunks;
   private final List visitedVampireChunks;
   boolean resetSleep;
   int cachedSky;
   private Coord mirrorWorldEntryPoint;
   static final long COOLDOWN_ESCAPE_1_TICKS = (long)TimeUtil.minsToTicks(5);
   static final long COOLDOWN_ESCAPE_2_TICKS = (long)TimeUtil.minsToTicks(60);
   long mirrorWorldEscapeCooldown1;
   long mirrorWorldEscapeCooldown2;
   private String unbreakableVowID = "";


   public static final void register(EntityPlayer player) {
      player.registerExtendedProperties("WitcheryExtendedPlayer", new ExtendedPlayer(player));
   }

   public static final ExtendedPlayer get(EntityPlayer player) {
      return (ExtendedPlayer)player.getExtendedProperties("WitcheryExtendedPlayer");
   }

   public ExtendedPlayer(EntityPlayer player) {
      this.selectedVampirePower = ExtendedPlayer.VampirePower.NONE;
      this.cachedWorship = -1;
      this.visitedChunks = new ArrayList();
      this.visitedVampireChunks = new ArrayList();
      this.mirrorWorldEscapeCooldown1 = Long.MIN_VALUE;
      this.mirrorWorldEscapeCooldown2 = Long.MIN_VALUE;
      this.player = player;
   }

   public void init(Entity entity, World world) {}

   public void saveNBTData(NBTTagCompound compound) {
      NBTTagCompound props = new NBTTagCompound();
      props.setInteger("PotionBottling", this.skillLevelPotionBottling);
      props.setInteger("WerewolfLevel", this.werewolfLevel);
      props.setInteger("CreatureType", this.creatureType);
      props.setInteger("VampireLevel", this.vampireLevel);
      props.setInteger("SpiritLevel", this.spiritLevel);
      props.setBoolean("AstralProjecting", this.isAstralProjecting);
      props.setInteger("BloodPower", this.bloodPower);
      props.setInteger("PotionThrowing", this.skillLevelPotionThrowing);
      props.setInteger("WolfmanQuestState", this.wolfmanQuestState);
      props.setInteger("WolfmanQuestCounter", this.wolfmanQuestCounter);
      props.setLong("LastBoneFind", this.lastBoneFind);
      props.setLong("LastHowl", this.lastHowl);
      NBTTagList nbtChunks = new NBTTagList();
      Iterator nbtVampireChunks = this.visitedChunks.iterator();

      while(nbtVampireChunks.hasNext()) {
         long i$ = ((Long)nbtVampireChunks.next()).longValue();
         NBTTagCompound tag = new NBTTagCompound();
         tag.setLong("Location", i$);
         nbtChunks.appendTag(tag);
      }

      props.setTag("WolfmanQuestChunks", nbtChunks);
      props.setInteger("BloodPower", this.bloodPower);
      props.setInteger("HumanBlood", this.humanBlood);
      props.setInteger("VampireUltimate", this.vampireUltimate);
      props.setInteger("VampireUltimateCharges", this.vampireUltimateCharges);
      props.setInteger("VampireLevelCap", this.vampireLevelCap);
      props.setInteger("VampireQuestCounter", this.vampireQuestCounter);
      NBTTagList nbtVampireChunks1 = new NBTTagList();
      Iterator i$1 = this.visitedVampireChunks.iterator();

      while(i$1.hasNext()) {
         long l = ((Long)i$1.next()).longValue();
         NBTTagCompound tag1 = new NBTTagCompound();
         tag1.setLong("Location", l);
         nbtVampireChunks1.appendTag(tag1);
      }

      props.setTag("VampireQuestChunks", nbtVampireChunks1);
      props.setInteger("BloodReserve", this.bloodReserve);
      props.setBoolean("VampireVision", this.vampVisionActive);
      if(this.cachedInventory != null) {
         props.setTag("CachedInventory2", this.cachedInventory.copy());
         props.setBoolean("CanRestoreInventory", this.inventoryCanBeRestored);
      }

      if(this.mirrorWorldEntryPoint != null) {
         props.setTag("MirrorWorldEntryPoint", this.mirrorWorldEntryPoint.toTagNBT());
      }

      if(this.lastPlayerSkin != null) {
         props.setString("LastPlayerSkin", this.lastPlayerSkin);
      }

      props.setLong("MirrorEscape1", this.mirrorWorldEscapeCooldown1);
      props.setLong("MirrorEscape2", this.mirrorWorldEscapeCooldown2);
      if (this.unbreakableVowID != null && !this.unbreakableVowID.isEmpty()) {
         props.setString("UnbreakableVowID", this.unbreakableVowID);
      }
      compound.setTag("WitcheryExtendedPlayer", props);
   }

   public void loadNBTData(NBTTagCompound compound) {
      if(compound.hasKey("WitcheryExtendedPlayer")) {
         NBTTagCompound nbtRoot = (NBTTagCompound)compound.getTag("WitcheryExtendedPlayer");
         this.skillLevelPotionBottling = MathHelper.clamp_int(nbtRoot.getInteger("PotionBottling"), 0, 100);
         this.skillLevelPotionThrowing = MathHelper.clamp_int(nbtRoot.getInteger("PotionThrowing"), 0, 100);
         this.setCreatureTypeOrdinal(nbtRoot.getInteger("CreatureType"));
         this.werewolfLevel = MathHelper.clamp_int(nbtRoot.getInteger("WerewolfLevel"), 0, 10);
         this.vampireLevel = nbtRoot.getInteger("VampireLevel");
         this.spiritLevel = nbtRoot.getInteger("SpiritLevel");
         this.isAstralProjecting = nbtRoot.getBoolean("AstralProjecting");
         this.bloodPower = nbtRoot.getInteger("BloodPower");
         this.wolfmanQuestState = MathHelper.clamp_int(nbtRoot.getInteger("WolfmanQuestState"), 0, ExtendedPlayer.QuestState.values().length - 1);
         this.wolfmanQuestCounter = MathHelper.clamp_int(nbtRoot.getInteger("WolfmanQuestCounter"), 0, 100);
         this.visitedChunks.clear();
         NBTTagList nbtChunks = nbtRoot.getTagList("WolfmanQuestChunks", 10);

         for(int nbtVampireChunks = 0; nbtVampireChunks < nbtChunks.tagCount(); ++nbtVampireChunks) {
            this.visitedChunks.add(Long.valueOf(nbtChunks.getCompoundTagAt(nbtVampireChunks).getLong("Location")));
         }

         this.lastBoneFind = nbtRoot.getLong("LastBoneFind");
         this.lastHowl = nbtRoot.getLong("LastHowl");
         this.humanBlood = MathHelper.clamp_int(nbtRoot.getInteger("HumanBlood"), 0, 500);
         this.vampireUltimate = nbtRoot.getInteger("VampireUltimate");
         this.vampireUltimateCharges = nbtRoot.getInteger("VampireUltimateCharges");
         this.vampireLevelCap = nbtRoot.getInteger("VampireLevelCap");
         this.vampireQuestCounter = nbtRoot.getInteger("VampireQuestCounter");
         NBTTagList var6 = nbtRoot.getTagList("VampireQuestChunks", 10);

         for(int i = 0; i < var6.tagCount(); ++i) {
            this.visitedVampireChunks.add(Long.valueOf(var6.getCompoundTagAt(i).getLong("Location")));
         }

         this.bloodReserve = nbtRoot.getInteger("BloodReserve");
         this.vampVisionActive = nbtRoot.getBoolean("VampireVision");
         if(nbtRoot.hasKey("CachedInventory2")) {
            this.cachedInventory = nbtRoot.getTagList("CachedInventory2", 10);
            this.inventoryCanBeRestored = nbtRoot.getBoolean("CanRestoreInventory");
         }

         if(nbtRoot.hasKey("MirrorWorldEntryPoint")) {
            this.mirrorWorldEntryPoint = Coord.fromTagNBT(nbtRoot.getCompoundTag("MirrorWorldEntryPoint"));
         }

         if(nbtRoot.hasKey("LastPlayerSkin")) {
            this.lastPlayerSkin = nbtRoot.getString("LastPlayerSkin");
         }

         this.mirrorWorldEscapeCooldown1 = nbtRoot.getLong("MirrorEscape1");
         this.mirrorWorldEscapeCooldown2 = nbtRoot.getLong("MirrorEscape2");
         if(nbtRoot.hasKey("UnbreakableVowID")) {
            this.unbreakableVowID = nbtRoot.getString("UnbreakableVowID");
         } else {
            this.unbreakableVowID = "";
         }
      }

   }

   public void setOtherPlayerSkin(String username) {
      this.lastPlayerSkin = username;
      this.locationSkin = null;
      this.sync();
   }

   public String getOtherPlayerSkin() {
      return this.lastPlayerSkin != null?this.lastPlayerSkin:"";
   }

   @SideOnly(Side.CLIENT)
   public ResourceLocation getLocationSkin() {
      if(this.locationSkin == null) {
         this.setupCustomSkin();
      }

      return this.locationSkin != null?this.locationSkin:null;
   }

   @SideOnly(Side.CLIENT)
   private void setupCustomSkin() {
      String ownerName = this.getOtherPlayerSkin();
      if(ownerName != null && !ownerName.isEmpty()) {
         this.locationSkin = AbstractClientPlayer.getLocationSkin(ownerName);
         this.downloadImageSkin = getDownloadImageSkin(this.locationSkin, ownerName);
      } else {
         this.locationSkin = null;
         this.downloadImageSkin = null;
      }

   }

   @SideOnly(Side.CLIENT)
   private static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation location, String name) {
      TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
      Object object = texturemanager.getTexture(location);
      if(object == null) {
         object = new ThreadDownloadImageData((File)null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[]{StringUtils.stripControlCodes(name)}), RenderReflection.SKIN, new ImageBufferDownload());
         texturemanager.loadTexture(location, (ITextureObject)object);
      }

      return (ThreadDownloadImageData)object;
   }

   public ResourceLocation getOtherPlayerSkinLocation() {
      return this.getLocationSkin();
   }

   public void cachePlayerInventory() {
      this.inventoryCanBeRestored = true;
   }

   public void backupPlayerInventory() {
      NBTTagList nbtInventory = new NBTTagList();
      this.player.inventory.writeToNBT(nbtInventory);
      this.cachedInventory = nbtInventory;
   }

   public void restorePlayerInventoryFrom(ExtendedPlayer original) {
      if(original != null && this.cachedInventory != null && this.inventoryCanBeRestored) {
         this.player.inventory.readFromNBT(original.cachedInventory);
         this.inventoryCanBeRestored = false;
         this.cachedInventory = null;
      }

   }

   public int getSkillPotionBottling() {
      return this.skillLevelPotionBottling;
   }

   public int increaseSkillPotionBottling() {
      this.skillLevelPotionBottling = Math.min(this.skillLevelPotionBottling + 1, 100);
      if(this.skillLevelPotionBottling == 30 || this.skillLevelPotionBottling == 60 || this.skillLevelPotionBottling == 90) {
         ChatUtil.sendTranslated(this.player, "witchery:brew.skillincrease", new Object[0]);
      }

      return this.getSkillPotionBottling();
   }

   public int getSkillPotionThrowing() {
      return this.skillLevelPotionThrowing;
   }

   public int increaseSkillPotionThrowing() {
      this.skillLevelPotionThrowing = Math.min(this.skillLevelPotionThrowing + 1, 100);
      return this.getSkillPotionThrowing();
   }

   public int getWerewolfLevel() {
      return this.werewolfLevel;
   }

   public void setWerewolfLevel(int level) {
      if(this.werewolfLevel != level && level >= 0 && level <= 10) {
         this.werewolfLevel = level;
         this.wolfmanQuestState = 0;
         this.wolfmanQuestCounter = 0;
         this.visitedChunks.clear();
         if(this.werewolfLevel == 0 && !this.player.worldObj.isRemote && (this.creatureType == 1 || this.creatureType == 2)) {
            Shapeshift.INSTANCE.shiftTo(this.player, TransformCreature.NONE);
         }

         this.sync();
      }

   }

   public void increaseWerewolfLevel() {
      if(this.werewolfLevel < 10) {
         this.setWerewolfLevel(this.werewolfLevel + 1);
         Shapeshift.INSTANCE.initCurrentShift(this.player);
      }

   }

   public int getSpiritLevel() {
      return this.spiritLevel;
   }

   public void setSpiritLevel(int level) {
      if(this.spiritLevel != level && level >= 0 && level <= 10) {
         this.spiritLevel = level;
         this.sync();
      }
   }

   public void increaseSpiritLevel() {
      if(this.spiritLevel < 10) {
         this.setSpiritLevel(this.spiritLevel + 1);
      }
   }

   public int getHumanBlood() {
      return this.humanBlood;
   }

   public void setHumanBlood(int blood) {
      if(this.humanBlood != blood) {
         this.humanBlood = MathHelper.clamp_int(blood, 0, 500);
         if(!this.player.worldObj.isRemote) {
            Witchery.packetPipeline.sendToAll((IMessage)(new PacketPartialExtendedPlayerSync(this, this.player)));
         }
      }

   }

   public int takeHumanBlood(int quantity, EntityLivingBase attacker) {
      if(!this.player.isPlayerSleeping()) {
         quantity = (int)Math.ceil((double)(0.66F * (float)quantity));
      }

      int remainder = Math.max(this.humanBlood - quantity, 0);
      int taken = this.humanBlood - remainder;
      this.setHumanBlood(remainder);
      if(this.humanBlood < (int)Math.ceil(250.0D)) {
         this.player.attackEntityFrom(new EntityDamageSource(DamageSource.magic.getDamageType(), attacker), 1.0F);
      } else if(!this.player.isPlayerSleeping()) {
         this.player.attackEntityFrom(new EntityDamageSource(DamageSource.magic.getDamageType(), attacker), 0.1F);
      }

      return taken;
   }

   public boolean isAstralProjecting() {
      return this.isAstralProjecting;
   }

   public void setAstralProjecting(boolean projecting) {
      if (this.isAstralProjecting != projecting) {
         this.isAstralProjecting = projecting;
         this.sync();
      }
   }

   public static boolean trySayAstralProjection(EntityPlayer player, String message) {
      if(message != null && (message.equalsIgnoreCase("ex corpus") || message.equalsIgnoreCase("astral"))) {
         ExtendedPlayer playerEx = get(player);
         if(playerEx != null && playerEx.getSpiritLevel() >= 10 && player.dimension != Config.instance().dimensionDreamID) {
            if (!playerEx.isAstralProjecting()) {
               com.emoniph.witchery.entity.EntityCorpse var21 = new com.emoniph.witchery.entity.EntityCorpse(player.worldObj);
               var21.setHealth(player.getHealth());
               var21.setCustomNameTag(player.getCommandSenderName());
               var21.setOwner(player.getCommandSenderName());
               var21.setLocationAndAngles(0.5D + (double)net.minecraft.util.MathHelper.floor_double(player.posX), player.posY, 0.5D + (double)net.minecraft.util.MathHelper.floor_double(player.posZ), 0.0F, 0.0F);
               player.worldObj.spawnEntityInWorld(var21);
               
               net.minecraft.nbt.NBTTagCompound nbt = com.emoniph.witchery.infusion.Infusion.getNBT(player);
               com.emoniph.witchery.dimension.WorldProviderDreamWorld.setPlayerIsGhost(nbt, true);
               playerEx.setAstralProjecting(true);
               
               com.emoniph.witchery.util.SoundEffect.RANDOM_FIZZ.playAtPlayer(player.worldObj, player);
            } else {
               java.util.List corpses = player.worldObj.getEntitiesWithinAABB(com.emoniph.witchery.entity.EntityCorpse.class, player.boundingBox.expand(256.0D, 256.0D, 256.0D));
               for (Object obj : corpses) {
                  com.emoniph.witchery.entity.EntityCorpse corpse = (com.emoniph.witchery.entity.EntityCorpse)obj;
                  if (corpse.getOwnerName().equals(player.getCommandSenderName())) {
                     if (player.ridingEntity != null) {
                        player.mountEntity((net.minecraft.entity.Entity)null);
                     }
                     com.emoniph.witchery.item.ItemGeneral.teleportToLocationSafely(player.worldObj, corpse.posX, corpse.posY + 1, corpse.posZ, player.dimension, player, true);
                     corpse.setDead();
                     break;
                  }
               }
               net.minecraft.nbt.NBTTagCompound nbt = com.emoniph.witchery.infusion.Infusion.getNBT(player);
               com.emoniph.witchery.dimension.WorldProviderDreamWorld.setPlayerIsGhost(nbt, false);
               playerEx.setAstralProjecting(false);
               player.removePotionEffect(net.minecraft.potion.Potion.invisibility.id);
               com.emoniph.witchery.util.SoundEffect.RANDOM_FIZZ.playAtPlayer(player.worldObj, player);
            }
            return true;
         }
      }
      return false;
   }

   public static boolean trySaySpiritForm(EntityPlayer player, String message) {
      if(message != null && (message.equalsIgnoreCase("ex spiritus") || message.equalsIgnoreCase("spirit"))) {
         ExtendedPlayer playerEx = get(player);
         if(playerEx != null && playerEx.getSpiritLevel() >= 4) {
            if (playerEx.getCreatureType() == com.emoniph.witchery.util.TransformCreature.SPIRIT) {
               com.emoniph.witchery.common.Shapeshift.INSTANCE.shiftTo(player, com.emoniph.witchery.util.TransformCreature.NONE);
               com.emoniph.witchery.util.SoundEffect.RANDOM_FIZZ.playAtPlayer(player.worldObj, player);
            } else if (playerEx.getCreatureType() == com.emoniph.witchery.util.TransformCreature.NONE) {
               com.emoniph.witchery.common.Shapeshift.INSTANCE.shiftTo(player, com.emoniph.witchery.util.TransformCreature.SPIRIT);
               com.emoniph.witchery.util.SoundEffect.RANDOM_FIZZ.playAtPlayer(player.worldObj, player);
            }
            return true;
         }
      }
      return false;
   }

   public void giveHumanBlood(int quantity) {
      if(this.humanBlood < 500) {
         this.setHumanBlood(this.humanBlood + quantity);
      }

   }

   public int getVampireLevel() {
      return this.vampireLevel;
   }

   public boolean isVampire() {
      return this.getVampireLevel() > 0;
   }

   public void setVampireLevel(int level) {
      if(this.vampireLevel != level && level >= 0 && level <= 10) {
         this.vampireLevel = level;
         this.vampireQuestCounter = 0;
         this.visitedVampireChunks.clear();
         if(this.vampireLevel == 0 && !this.player.worldObj.isRemote) {
            if(this.creatureType == 3) {
               Shapeshift.INSTANCE.shiftTo(this.player, TransformCreature.NONE);
            } else {
               Shapeshift.INSTANCE.initCurrentShift(this.player);
            }

            this.bloodPower = 0;
            this.humanBlood = 50;
            this.vampireUltimate = 0;
            this.vampireUltimateCharges = 0;
         } else {
            Shapeshift.INSTANCE.initCurrentShift(this.player);
         }

         this.selectedVampirePower = ExtendedPlayer.VampirePower.NONE;
         if(this.vampireLevel == 1) {
            this.bloodPower = 125;
         }

         if(this.vampireLevel > 0) {
            this.humanBlood = 0;
         }

         this.sync();
      }

   }

   public int getMaxBloodPower() {
      return 500 + (this.getWerewolfLevel() >= 2?(int)Math.floor((double)this.getVampireLevel() * 0.5D):this.getVampireLevel()) * 250;
   }

   public int getBloodPower() {
      return this.bloodPower;
   }

   public boolean decreaseBloodPower(int quantity, boolean exact) {
      if(this.player.capabilities.isCreativeMode) {
         return true;
      } else if(this.bloodPower >= (exact?quantity:1)) {
         this.setBloodPower(this.bloodPower - quantity);
         return true;
      } else {
         return false;
      }
   }

   public void increaseBloodPower(int quantity) {
      if(this.bloodPower < this.getMaxBloodPower()) {
         this.setBloodPower(this.bloodPower + quantity);
         if(Config.instance().allowVampireQuests && this.getVampireLevel() == 1 && this.getBloodPower() == this.getMaxBloodPower()) {
            this.increaseVampireLevel();
         }
      }

   }

   public void increaseVampireLevel() {
      if(this.vampireLevel < 10) {
         this.setVampireLevel(this.vampireLevel + 1);
         if(!this.player.worldObj.isRemote) {
            ChatUtil.sendTranslated(EnumChatFormatting.GOLD, this.player, "Your thirst grows stronger!", new Object[0]);
            SoundEffect.RANDOM_LEVELUP.playOnlyTo(this.player);
         }
      }

   }

   public void increaseVampireLevelCap(int levelCap) {
      if(levelCap > this.vampireLevelCap) {
         this.vampireLevelCap = Math.max(levelCap, 3);
      }

   }

   public boolean canIncreaseVampireLevel() {
      return Config.instance().allowVampireQuests && this.vampireLevel < this.vampireLevelCap;
   }

   public void increaseBloodPower(int quantity, int maxIncrease) {
      if(this.bloodPower < this.getMaxBloodPower() && this.bloodPower < maxIncrease) {
         this.setBloodPower(Math.min(this.bloodPower + quantity, maxIncrease));
      }

   }

   public void setBloodPower(int bloodLevel) {
      if(this.bloodPower != bloodLevel) {
         this.bloodPower = MathHelper.clamp_int(bloodLevel, 0, this.getMaxBloodPower());
         this.sync();
      }

   }

   public ExtendedPlayer.VampireUltimate getVampireUltimate() {
      ExtendedPlayer.VampireUltimate[] values = ExtendedPlayer.VampireUltimate.values();
      if(this.vampireUltimate < 0 || this.vampireUltimate >= values.length) {
         return values[0];
      }
      return values[this.vampireUltimate];
   }

   public void setVampireUltimate(ExtendedPlayer.VampireUltimate skill) {
      this.setVampireUltimate(skill, 5);
   }

   public void setVampireUltimate(ExtendedPlayer.VampireUltimate skill, int charges) {
      this.vampireUltimate = skill.ordinal();
      this.vampireUltimateCharges = charges;
      this.sync();
   }

   public int getVampireUltimateCharges() {
      return this.vampireUltimateCharges;
   }

   public ExtendedPlayer.VampirePower getSelectedVampirePower() {
      return this.selectedVampirePower;
   }

   public int getMaxAvailablePowerOrdinal() {
      return ExtendedPlayer.VampirePower.levels[this.vampireLevel];
   }

   public void useBloodReserve() {
      int temp = this.bloodReserve;
      if(this.bloodPower < this.getMaxBloodPower()) {
         this.bloodReserve = 0;
         this.increaseBloodPower(temp);
      }

   }

   public boolean isBloodReserveReady() {
      return this.bloodReserve > 0;
   }

   public void fillBloodReserve(int quantity) {
      this.bloodReserve = Math.min(this.bloodReserve + quantity, 250);
      this.sync();
   }

   public int getBloodReserve() {
      return this.isVampire()?this.bloodReserve:0;
   }

   public void setBloodReserve(int blood) {
      this.bloodReserve = blood;
   }

   public boolean isVampireVisionActive() {
      return this.vampireLevel >= 2 && this.vampVisionActive;
   }

   public void toggleVampireVision() {
      this.vampVisionActive = !this.vampVisionActive;
      if(!this.player.worldObj.isRemote) {
         if(!this.vampVisionActive) {
            this.player.removePotionEffect(Potion.nightVision.id);
         } else {
            this.player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 400, 0, true));
         }
      }

   }

   public void setSelectedVampirePower(ExtendedPlayer.VampirePower power, boolean syncToServer) {
      if(this.selectedVampirePower != power) {
         this.selectedVampirePower = power;
         this.highlightTicks = this.selectedVampirePower != ExtendedPlayer.VampirePower.NONE?100:0;
         if(syncToServer && this.player.worldObj.isRemote) {
            Witchery.packetPipeline.sendToServer(new PacketSelectPlayerAbility(this, false));
         }
      }

   }

   public void triggerSelectedVampirePower() {
      if(!this.player.worldObj.isRemote) {
         ExtendedPlayer.VampirePower power = this.getSelectedVampirePower();
         if(this.vampireCooldown <= 0) {
            this.vampireCooldown = 10;
            int dimension;
            int var14;
            switch(ExtendedPlayer.NamelessClass447548467.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[power.ordinal()]) {
            case 1:
               if(this.player.isSneaking()) {
                  this.toggleVampireVision();
               }
               break;
            case 2:
               if(this.getCreatureType() == TransformCreature.NONE) {
                  PotionEffect var13 = this.player.getActivePotionEffect(Potion.moveSpeed);
                  var14 = var13 == null?0:(int)Math.ceil(Math.log((double)(var13.getAmplifier() + 1)) / Math.log(2.0D));
                  if(this.vampireLevel >= 4 && (double)var14 <= Math.ceil((double)((float)(this.vampireLevel - 3) / 2.0F))) {
                     if(this.decreaseBloodPower(power.INITIAL_COST, true)) {
                        SoundEffect.RANDOM_FIZZ.playOnlyTo(this.player);
                        int var15 = var13 == null?2:(var13.getAmplifier() + 1) * 2;
                        dimension = var13 == null?TimeUtil.secsToTicks(10):var13.getDuration() + 60;
                        this.player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, dimension, var15 - 1, true));
                        this.player.addPotionEffect(new PotionEffect(Potion.jump.id, dimension, var14 + 1, true));
                     } else {
                        SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
                     }
                  } else {
                     SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
                  }
               } else {
                  SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
               }
               break;
            case 3:
               if(this.vampireLevel >= 7) {
                  if(this.getCreatureType() == TransformCreature.NONE) {
                     if(this.decreaseBloodPower(power.INITIAL_COST, true)) {
                        SoundEffect.RANDOM_FIZZ.playOnlyTo(this.player);
                        Shapeshift.INSTANCE.shiftTo(this.player, TransformCreature.BAT);
                     } else {
                        SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
                     }
                  } else if(this.getCreatureType() == TransformCreature.BAT) {
                     SoundEffect.RANDOM_FIZZ.playOnlyTo(this.player);
                     Shapeshift.INSTANCE.shiftTo(this.player, TransformCreature.NONE);
                  } else {
                     SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
                  }
               } else {
                  SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
               }
               break;
            case 4:
               if(this.vampireLevel >= 10 && this.vampireUltimateCharges > 0 && this.getCreatureType() == TransformCreature.NONE) {
                  switch(ExtendedPlayer.NamelessClass447548467.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate[this.getVampireUltimate().ordinal()]) {
                  case 1:
                     WorldInfo worldinfo = ((WorldServer)this.player.worldObj).getWorldInfo();
                     if(!worldinfo.isRaining()) {
                        var14 = (300 + this.player.worldObj.rand.nextInt(600)) * 20;
                        worldinfo.setThunderTime(var14);
                        worldinfo.setThundering(true);
                        worldinfo.setRainTime(var14);
                        worldinfo.setRaining(true);
                        SoundEffect.RANDOM_FIZZ.playOnlyTo(this.player);
                        if(!this.player.capabilities.isCreativeMode) {
                           --this.vampireUltimateCharges;
                           this.sync();
                        }
                     } else {
                        SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
                     }
                     break;
                  case 2:
                     for(var14 = 0; var14 < 15; ++var14) {
                        EntityLiving var17 = spawnCreature(this.player.worldObj, EntityAttackBat.class, this.player.posX, this.player.posY + 3.0D + this.player.worldObj.rand.nextDouble(), this.player.posZ, 1, 4, ParticleEffect.SMOKE, SoundEffect.WITCHERY_RANDOM_POOF);
                        if(var17 != null) {
                           EntityAttackBat var16 = (EntityAttackBat)var17;
                           var16.setOwner(this.player);
                           var16.setIsBatHanging(false);
                           NBTTagCompound var18 = var16.getEntityData();
                           var18.setBoolean("WITCNoDrops", true);
                        }
                     }

                     if(!this.player.capabilities.isCreativeMode) {
                        --this.vampireUltimateCharges;
                        this.sync();
                     }
                     break;
                  case 3:
                     boolean done = false;
                     if(this.player.dimension != Config.instance().dimensionDreamID) {
                        ChunkCoordinates coords = this.player.getBedLocation(this.player.dimension);
                        dimension = this.player.dimension;
                        Object world = this.player.worldObj;
                        if(coords == null) {
                           coords = this.player.getBedLocation(0);
                           dimension = 0;
                           world = MinecraftServer.getServer().worldServerForDimension(0);
                           if(coords == null) {
                              for(coords = ((World)world).getSpawnPoint(); ((World)world).getBlock(coords.posX, coords.posY, coords.posZ).isNormalCube() && coords.posY < 255; ++coords.posY) {
                                 ;
                              }
                           }
                        }

                        if(coords != null) {
                           double HOME_DIST = 6.0D;
                           double HOME_DIST_SQ = 36.0D;
                           coords = Blocks.bed.getBedSpawnPosition((IBlockAccess)world, coords.posX, coords.posY, coords.posZ, (EntityPlayer)null);
                           if(coords != null) {
                              ItemGeneral var10000;
                              if(dimension == this.player.dimension && this.player.getDistanceSq((double)coords.posX, this.player.posY, (double)coords.posZ) <= 36.0D) {
                                 Village village = ((World)world).villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.player.posX), MathHelper.floor_double(this.player.posY), MathHelper.floor_double(this.player.posZ), 512);
                                 if(village != null) {
                                    ChunkCoordinates townPos = village.getCenter();
                                    var10000 = Witchery.Items.GENERIC;
                                    if(ItemGeneral.teleportToLocationSafely(this.player.worldObj, (double)townPos.posX + 0.5D, (double)(townPos.posY + 1), (double)townPos.posZ + 0.5D, dimension, this.player, true)) {
                                       done = true;
                                    }
                                 }
                              } else {
                                 var10000 = Witchery.Items.GENERIC;
                                 if(ItemGeneral.teleportToLocationSafely(this.player.worldObj, (double)coords.posX + 0.5D, (double)(coords.posY + 1), (double)coords.posZ + 0.5D, dimension, this.player, true)) {
                                    done = true;
                                 }
                              }
                           }
                        }
                     }

                     if(!done) {
                        SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
                     } else if(!this.player.capabilities.isCreativeMode) {
                        --this.vampireUltimateCharges;
                        this.sync();
                     }
                     break;
                  default:
                     SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
                  }
               } else {
                  SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
               }
            }
         } else {
            SoundEffect.NOTE_SNARE.playOnlyTo(this.player);
         }
      }

   }

   public static EntityLiving spawnCreature(World world, Class creatureType, double posX, double posY, double posZ, int minRange, int maxRange, ParticleEffect effect, SoundEffect effectSound) {
      if(!world.isRemote) {
         int x = MathHelper.floor_double(posX);
         int y = MathHelper.floor_double(posY);
         int z = MathHelper.floor_double(posZ);
         int activeRadius = maxRange - minRange;
         int ax = world.rand.nextInt(activeRadius * 2 + 1);
         if(ax > activeRadius) {
            ax += minRange * 2;
         }

         int nx = x - maxRange + ax;
         int az = world.rand.nextInt(activeRadius * 2 + 1);
         if(az > activeRadius) {
            az += minRange * 2;
         }

         int nz = z - maxRange + az;

         int ny;
         for(ny = y; !world.isAirBlock(nx, ny, nz) && ny < y + 8; ++ny) {
            ;
         }

         while(world.isAirBlock(nx, ny, nz) && ny > 0) {
            --ny;
         }

         int hy;
         for(hy = 0; world.isAirBlock(nx, ny + hy + 1, nz) && hy < 6; ++hy) {
            ;
         }

         Log.instance().debug("Creature: hy: " + hy + " (" + nx + "," + ny + "," + nz + ")");
         if(hy >= 2) {
            try {
               Constructor ex = creatureType.getConstructor(new Class[]{World.class});
               EntityLiving creature = (EntityLiving)ex.newInstance(new Object[]{world});
               creature.setLocationAndAngles(0.5D + (double)nx, 0.05D + (double)ny + 1.0D, 0.5D + (double)nz, 0.0F, 0.0F);
               world.spawnEntityInWorld(creature);
               if(effect != null) {
                  effect.send(effectSound, world, 0.5D + (double)nx, 0.05D + (double)ny + 1.0D, 0.5D + (double)nz, 1.0D, (double)creature.height, 16);
               }

               return creature;
            } catch (NoSuchMethodException var24) {
               ;
            } catch (InvocationTargetException var25) {
               ;
            } catch (InstantiationException var26) {
               ;
            } catch (IllegalAccessException var27) {
               ;
            }
         }
      }

      return null;
   }

   public void tick() {
      if(this.vampireCooldown > 0) {
         --this.vampireCooldown;
      }
      
      if (!this.player.worldObj.isRemote && this.isAstralProjecting()) {
          this.player.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.potion.Potion.invisibility.id, 40, 0, true));
          
          if (this.player.ticksExisted % 20 == 0) {
              boolean hasEnergy = com.emoniph.witchery.infusion.Infusion.aquireEnergy(this.player.worldObj, this.player, 3, false);
              if (!hasEnergy) {
              // Forced return because out of energy
              trySayAstralProjection(this.player, "ex corpus");
              com.emoniph.witchery.util.ChatUtil.sendTranslated(net.minecraft.util.EnumChatFormatting.RED, this.player, "witchery.infuse.nocharges");
              
              // Apply exhaustion debuffs
              this.player.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.potion.Potion.confusion.id, 200, 1));
              this.player.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.potion.Potion.hunger.id, 200, 1));
              this.player.addPotionEffect(new net.minecraft.potion.PotionEffect(net.minecraft.potion.Potion.moveSlowdown.id, 200, 1));
              }
          }
      }
      
      if (!this.player.worldObj.isRemote && this.getCreatureType() == com.emoniph.witchery.util.TransformCreature.SPIRIT && this.player.ticksExisted % 40 == 0) {
          boolean hasEnergy = com.emoniph.witchery.infusion.Infusion.aquireEnergy(this.player.worldObj, this.player, 1, false);
          if (!hasEnergy) {
              com.emoniph.witchery.common.Shapeshift.INSTANCE.shiftTo(this.player, com.emoniph.witchery.util.TransformCreature.NONE);
              com.emoniph.witchery.util.SoundEffect.RANDOM_FIZZ.playAtPlayer(this.player.worldObj, this.player);
              com.emoniph.witchery.util.ChatUtil.sendTranslated(net.minecraft.util.EnumChatFormatting.RED, this.player, "witchery.infuse.nocharges");
          }
      }

   }

   public void updateWorship() {
      if(this.cachedWorship >= 0) {
         this.player.addPotionEffect(new PotionEffect(Witchery.Potions.WORSHIP.id, TimeUtil.secsToTicks(60), this.cachedWorship, true));
         this.cachedWorship = -1;
      }

      this.processSync();
   }

   public boolean cacheIncurablePotionEffect(Collection activePotionEffects) {
      boolean cached = false;
      Iterator i$ = activePotionEffects.iterator();

      while(i$.hasNext()) {
         PotionEffect activeEffect = (PotionEffect)i$.next();
         int potionID = activeEffect.getPotionID();
         if(potionID >= 0 && potionID < Potion.potionTypes.length && Potion.potionTypes[potionID] != null && Potion.potionTypes[potionID] instanceof PotionBase && activeEffect.getDuration() > 5) {
            PotionBase potion = (PotionBase)Potion.potionTypes[potionID];
            if(!potion.isCurable()) {
               this.incurablePotionEffectCache.put(Integer.valueOf(activeEffect.getPotionID()), activeEffect);
               cached = true;
            }
         }
      }

      return cached;
   }

   public void clearCachedIncurablePotionEffect(Potion potion) {
      this.incurablePotionEffectCache.remove(Integer.valueOf(potion.id));
   }

   public void restoreIncurablePotionEffects() {
      if(this.incurablePotionEffectCache.size() > 0) {
         Collection activeEffectList = this.player.getActivePotionEffects();
         Iterator i$ = activeEffectList.iterator();

         PotionEffect restoredEffect;
         while(i$.hasNext()) {
            restoredEffect = (PotionEffect)i$.next();
            this.incurablePotionEffectCache.remove(Integer.valueOf(restoredEffect.getPotionID()));
         }

         i$ = this.incurablePotionEffectCache.values().iterator();

         while(i$.hasNext()) {
            restoredEffect = (PotionEffect)i$.next();
            this.player.addPotionEffect(new PotionEffect(restoredEffect));
         }

         this.incurablePotionEffectCache.clear();
      }

   }

   public void setCachedWorship(int worship) {
      this.cachedWorship = worship;
   }

   public String getUnbreakableVowID() {
      return this.unbreakableVowID == null ? "" : this.unbreakableVowID;
   }

   public void setUnbreakableVowID(String vowID) {
      this.unbreakableVowID = vowID;
      this.sync();
   }

   public void addWorship(int level) {
      this.cachedWorship = level;
   }

   public void sync() {
      if(!this.player.worldObj.isRemote) {
         Witchery.packetPipeline.sendTo((IMessage)(new PacketExtendedPlayerSync(this)), this.player);
      }

   }

   public void applySyncData(int werewolfLevel, int creatureType, int vampireLevel, int spiritLevel, int bloodPower, int selectedPower, int ultimate, int ultimateCharges, int bloodReserve) {
      this.werewolfLevel = MathHelper.clamp_int(werewolfLevel, 0, 10);
      this.vampireLevel = MathHelper.clamp_int(vampireLevel, 0, 10);
      this.spiritLevel = MathHelper.clamp_int(spiritLevel, 0, 10);
      this.bloodPower = Math.max(0, bloodPower);
      this.vampireUltimateCharges = Math.max(0, ultimateCharges);
      this.bloodReserve = Math.max(0, bloodReserve);

      ExtendedPlayer.VampireUltimate[] ultimates = ExtendedPlayer.VampireUltimate.values();
      this.vampireUltimate = ultimate >= 0 && ultimate < ultimates.length ? ultimate : 0;

      ExtendedPlayer.VampirePower[] powers = ExtendedPlayer.VampirePower.values();
      this.selectedVampirePower = selectedPower >= 0 && selectedPower < powers.length ? powers[selectedPower] : ExtendedPlayer.VampirePower.NONE;

      TransformCreature[] creatures = TransformCreature.values();
      int newCreatureType = creatureType >= 0 && creatureType < creatures.length ? creatureType : 0;
      if (newCreatureType != this.creatureType) {
         this.setCreatureTypeOrdinal(newCreatureType);
      }
      
      net.minecraft.nbt.NBTTagCompound nbt = com.emoniph.witchery.infusion.Infusion.getNBT(this.player);
      this.isAstralProjecting = nbt.getBoolean("AstralProjecting");
   }

   public static void loadProxyData(EntityPlayer player) {
      if(player != null) {
         ExtendedPlayer playerEx = get(player);
         playerEx.sync();
      }

   }

   public int getCreatureTypeOrdinal() {
      return this.creatureType;
   }

   public TransformCreature getCreatureType() {
      return TransformCreature.values()[this.creatureType];
   }

   public void setCreatureType(TransformCreature type) {
      int ordinalType = type.ordinal();
      this.setCreatureTypeOrdinal(ordinalType);
   }

   public void setCreatureTypeOrdinal(int type) {
      if(type != this.creatureType) {
         this.creatureType = type;
         if(!this.player.worldObj.isRemote) {
            Witchery.packetPipeline.sendToAll((IMessage)(new PacketPlayerStyle(this.player)));
         }
      }

   }

   public long getLastBoneFind() {
      return this.lastBoneFind;
   }

   public void setLastBoneFind(long serverTime) {
      this.lastBoneFind = serverTime;
   }

   public long getLastHowl() {
      return this.lastHowl;
   }

   public void setLastHowl(long serverTime) {
      this.lastHowl = serverTime;
   }

   public ExtendedPlayer.QuestState getWolfmanQuestState() {
      return ExtendedPlayer.QuestState.values()[this.wolfmanQuestState];
   }

   public void setWolfmanQuestState(ExtendedPlayer.QuestState state) {
      this.wolfmanQuestState = state.ordinal();
   }

   public int getWolfmanQuestCounter() {
      return this.wolfmanQuestCounter;
   }

   public void increaseWolfmanQuestCounter() {
      ++this.wolfmanQuestCounter;
      if(this.wolfmanQuestCounter > 100) {
         this.wolfmanQuestCounter = 100;
      }

   }

   public boolean storeWolfmanQuestChunk(int x, int z) {
      long location = (long)x << 32 | (long)z & 4294967295L;
      if(this.visitedChunks.contains(Long.valueOf(location))) {
         return false;
      } else {
         this.visitedChunks.add(Long.valueOf(location));
         return true;
      }
   }

   public boolean storeVampireQuestChunk(int x, int z) {
      long location = (long)x << 32 | (long)z & 4294967295L;
      if(this.visitedVampireChunks.contains(Long.valueOf(location))) {
         return false;
      } else {
         this.visitedVampireChunks.add(Long.valueOf(location));
         return true;
      }
   }

   public int getVampireQuestCounter() {
      return this.vampireQuestCounter;
   }

   public void increaseVampireQuestCounter() {
      ++this.vampireQuestCounter;
      if(this.vampireQuestCounter > 10000) {
         this.vampireQuestCounter = 10000;
      }

   }

   public void resetVampireQuestCounter() {
      this.vampireQuestCounter = 0;
   }

   public void scheduleSync() {
      this.getPlayerData = true;
   }

   public void processSync() {
      if(this.getPlayerData) {
         this.getPlayerData = false;
         Iterator i$ = this.player.worldObj.playerEntities.iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            EntityPlayer otherPlayer = (EntityPlayer)obj;
            if(otherPlayer != this.player) {
               Witchery.packetPipeline.sendTo((IMessage)(new PacketPlayerStyle(otherPlayer)), this.player);
            }
         }
      }

   }

   public void checkSleep(boolean start) {
      if(start) {
         if(this.isVampire() && this.player.sleeping && this.player.worldObj.isDaytime()) {
            this.resetSleep = true;
            this.cachedSky = this.player.worldObj.skylightSubtracted;
            this.player.worldObj.skylightSubtracted = 4;
         }
      } else if(this.resetSleep) {
         this.resetSleep = false;
         this.player.worldObj.skylightSubtracted = this.cachedSky;
      }

   }

   public boolean hasVampireBook() {
      ItemStack[] arr$ = this.player.inventory.mainInventory;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ItemStack stack = arr$[i$];
         if(stack != null && stack.getItem() == Witchery.Items.VAMPIRE_BOOK) {
            return stack.getItemDamage() < 9;
         }
      }

      return false;
   }

   public void setMirrorWorldEntryPoint(int x, int y, int z) {
      this.mirrorWorldEntryPoint = new Coord(x, y, z);
   }

   public Coord getMirrorWorldEntryPoint() {
      return this.mirrorWorldEntryPoint;
   }

   public boolean isMirrorWorldEntryPoint(int x, int y, int z) {
      return this.mirrorWorldEntryPoint == null || this.mirrorWorldEntryPoint.isMatch(x, y, z);
   }

   public boolean canEscapeMirrorWorld(int slot) {
      return slot == 1?this.player.worldObj.getTotalWorldTime() >= this.mirrorWorldEscapeCooldown1 + COOLDOWN_ESCAPE_1_TICKS:(slot == 2?this.player.worldObj.getTotalWorldTime() >= this.mirrorWorldEscapeCooldown2 + COOLDOWN_ESCAPE_2_TICKS:false);
   }

   public void escapedMirrorWorld(int slot) {
      if(slot == 1) {
         this.mirrorWorldEscapeCooldown1 = this.player.worldObj.getTotalWorldTime();
      } else if(slot == 2) {
         this.mirrorWorldEscapeCooldown2 = this.player.worldObj.getTotalWorldTime();
      }

   }

   public long getCooldownSecs(int i) {
      return i == 1?(this.mirrorWorldEscapeCooldown1 + COOLDOWN_ESCAPE_1_TICKS - this.player.worldObj.getTotalWorldTime()) / 20L:(i == 2?(this.mirrorWorldEscapeCooldown2 + COOLDOWN_ESCAPE_2_TICKS - this.player.worldObj.getTotalWorldTime()) / 20L:0L);
   }


   // $FF: synthetic class
   static class NamelessClass447548467 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate;
      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower = new int[ExtendedPlayer.VampirePower.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.MESMERIZE.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.SPEED.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.BAT.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.ULTIMATE.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
            ;
         }

         $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate = new int[ExtendedPlayer.VampireUltimate.values().length];

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate[ExtendedPlayer.VampireUltimate.STORM.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate[ExtendedPlayer.VampireUltimate.SWARM.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate[ExtendedPlayer.VampireUltimate.FARM.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum QuestState {

      NOT_STATED("NOT_STATED", 0),
      STARTED("STARTED", 1),
      COMPLETE("COMPLETE", 2);
      // $FF: synthetic field
      private static final ExtendedPlayer.QuestState[] $VALUES = new ExtendedPlayer.QuestState[]{NOT_STATED, STARTED, COMPLETE};


      private QuestState(String var1, int var2) {}

   }

   public static enum VampirePower {

      NONE("NONE", 0, 0, 0, 0),
      DRINK("DRINK", 1, 0, 0, 1),
      MESMERIZE("MESMERIZE", 2, 50, 0, 2),
      SPEED("SPEED", 3, 10, 0, 4),
      BAT("BAT", 4, 50, 1, 7),
      ULTIMATE("ULTIMATE", 5, 50, 0, 10);
      public final int INITIAL_COST;
      public final int UPKEEP_COST;
      public final int LEVEL_CAP;
      private static int[] levels = new int[]{0, 1, 2, 2, 3, 3, 3, 4, 4, 4, 5};
      // $FF: synthetic field
      private static final ExtendedPlayer.VampirePower[] $VALUES = new ExtendedPlayer.VampirePower[]{NONE, DRINK, MESMERIZE, SPEED, BAT, ULTIMATE};


      private VampirePower(String var1, int var2, int initialCost, int upkeepCost, int levelCap) {
         this.INITIAL_COST = initialCost;
         this.UPKEEP_COST = upkeepCost;
         this.LEVEL_CAP = levelCap;
      }

   }

   public static enum VampireUltimate {

      NONE("NONE", 0),
      STORM("STORM", 1),
      SWARM("SWARM", 2),
      FARM("FARM", 3);
      // $FF: synthetic field
      private static final ExtendedPlayer.VampireUltimate[] $VALUES = new ExtendedPlayer.VampireUltimate[]{NONE, STORM, SWARM, FARM};


      private VampireUltimate(String var1, int var2) {}

   }
}
