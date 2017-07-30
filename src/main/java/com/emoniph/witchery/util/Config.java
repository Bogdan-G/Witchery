package com.emoniph.witchery.util;

import com.emoniph.witchery.worldgen.ComponentVillageWatchTower;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.StructureVillagePieces.Church;
import net.minecraft.world.gen.structure.StructureVillagePieces.Field1;
import net.minecraft.world.gen.structure.StructureVillagePieces.Field2;
import net.minecraft.world.gen.structure.StructureVillagePieces.Hall;
import net.minecraft.world.gen.structure.StructureVillagePieces.House1;
import net.minecraft.world.gen.structure.StructureVillagePieces.House2;
import net.minecraft.world.gen.structure.StructureVillagePieces.House3;
import net.minecraft.world.gen.structure.StructureVillagePieces.House4Garden;
import net.minecraft.world.gen.structure.StructureVillagePieces.WoodHut;
import net.minecraftforge.common.config.Configuration;

public class Config {

   private static final Config INSTANCE = new Config();
   public Configuration configuration;
   public boolean smeltAllSaplingsToWoodAsh;
   public boolean guiOnLeft;
   public boolean doubleFumeFilterChance;
   public boolean allowModIntegration;
   public boolean allowThaumcraft;
   public boolean allowMineFactoryReloaded;
   public boolean allowArsMagica2;
   public boolean allowForestry;
   public boolean allowTreecapitator;
   public boolean allowNotEnoughItems;
   public boolean generateApothecaries;
   public boolean generateWitchHuts;
   public boolean generateBookShops;
   public int apothecaryID;
   public boolean generateCovens;
   public boolean generateWickerMen;
   public boolean generateShacks;
   public boolean generateGoblinHuts;
   public boolean allowDeathItemRecoveryRite;
   public boolean respectOtherDeathChestMods;
   public int worldGenFrequency;
   public boolean worldGenTwilightForest;
   public boolean allowStatueGoddessRecipe;
   public String[] strawmanSpawnerRules;
   public boolean allowHellOnEarthFires;
   public boolean allowVoidBrambleRecipe;
   public boolean allowBiomeChanging;
   public int covenWitchSpawnWeight;
   public int goblinSpawnWeight;
   public int branchIconSet;
   public int dimensionDreamID;
   public int dimensionTormentID;
   public int dimensionMirrorID;
   public int percentageOfPlayersSleepingForBuff;
   public boolean render3dGlintEffect;
   public boolean renderHuntsmanGlintEffect;
   public boolean allowMystcraft;
   public boolean restrictPoppetShelvesToVanillaAndSpiritDimensions;
   public boolean allowBlockBreakEvents;
   public boolean allowDeathsHoodToFreezeVictims;
   public int decurseTeleportPullRadius;
   public int decurseDirectedRadius;
   public boolean allowDecurseTeleport;
   public boolean allowDecurseDirected;
   public boolean restrictTaglockCollectionOnNonPVPServers;
   public boolean restrictTaglockCollectionForStaffMembers;
   public int goblinSpawnRate;
   public String[] mutandisExtras;
   public int hobgoblinGodSpawnChance;
   public boolean hudShowVampireTargetBloodText;
   public String[] townBooks;
   public double vampireHunterSpawnChance;
   public int potionStartID;
   public boolean dupStaffSaveTemplate;
   private boolean traceRitesEnabled;
   private boolean debugging;
   public int hellhoundSpawnRate;
   public boolean allowExplodingCreeperHearts;
   public float mantrapAlpha;
   public int townZombieMode;
   public boolean allowVolatilityPotionBlockDamage;
   public boolean allowStakingVampires;
   public boolean allowCovenWitchVisits;
   public boolean allowVampireRitual;
   public boolean allowVampireQuests;
   public boolean shrinkMirrorWorld;
   private static final String CategoryEntity = "Entity";
   private List naturePowerReplaceableBlocks;
   public boolean allowVampireWolfHybrids;
   public int townWallChance;
   public int townWallWeight;
   public int townKeepChance;
   public int townKeepWeight;
   public boolean townAllowSandy;
   public boolean townAllowPlains;
   public boolean townAllowMountain;
   public boolean townAllowHills;
   public boolean townAllowForest;
   public boolean townAllowSnowy;
   public boolean townAllowWasteland;
   public boolean townAllowJungle;
   public boolean townAllowMesa;
   public List townParts;
   public boolean goblinDespawnBlock;
   public int diseaseRemovalChance;
   public int vampireDeathItemKeepAliveMins;
   public int spawnWeightSpirit;
   public double fairestSpawnChance;
   public boolean allowPlayerToPlayerWolfInfection;
   public int riteOfEclipseCooldownInSecs;
   public boolean allowChatMasquerading;
   public boolean allowNameplateMasquerading;


   public static Config instance() {
      return INSTANCE;
   }

   public void init(Configuration configuration, Configuration configuration_debug) {
      this.configuration = configuration;
      this.configuration.load();
      this.sync();
      configuration_debug.load();
      this.traceRitesEnabled = configuration_debug.get("Debug", "TraceRites", false).getBoolean(false);
      this.debugging = configuration_debug.get("Debug", "Debugging", false).getBoolean(false);
      this.dupStaffSaveTemplate = configuration_debug.get("Debug", "SaveDupStaffTemplate", false).getBoolean(false);
   }

   public void sync() {
      this.smeltAllSaplingsToWoodAsh = this.configuration.get("general", "AddSmeltingForAllSaplingsToWoodAsh", true).getBoolean(true);
      this.guiOnLeft = this.configuration.get("general", "GUIOnLeft", true).getBoolean(true);
      this.doubleFumeFilterChance = this.configuration.get("general", "DoubleFumeFilterChance", false).getBoolean(false);
      this.allowModIntegration = this.configuration.get("general", "AllowModIntegration", true).getBoolean(true);
      this.allowThaumcraft = this.configuration.get("general", "AllowModThaumcraft", true).getBoolean(true);
      this.allowMineFactoryReloaded = this.configuration.get("general", "AllowModMineFactoryReloaded", true).getBoolean(true);
      this.allowForestry = this.configuration.get("general", "AllowModForestry", true).getBoolean(true);
      this.allowTreecapitator = this.configuration.get("general", "AllowModTreecapitator", true).getBoolean(true);
      this.allowNotEnoughItems = this.configuration.get("general", "AllowModNEI", true).getBoolean(true);
      this.generateApothecaries = this.configuration.get("general", "GenerateApothecaries", true).getBoolean(true);
      this.apothecaryID = this.configuration.get("general", "ApothecaryVillagerID", 2435).getInt();
      this.generateCovens = this.configuration.get("general", "GenerateCovens", true).getBoolean(true);
      this.generateWickerMen = this.configuration.get("general", "GenerateWickerMen", true).getBoolean(true);
      this.generateShacks = this.configuration.get("general", "GenerateShacks", true).getBoolean(true);
      this.generateGoblinHuts = this.configuration.get("general", "GenerateHobgoblinHuts", true).getBoolean(true);
      this.allowDeathItemRecoveryRite = this.configuration.get("general", "AllowDeathItemRecoveryRite", true).getBoolean(true);
      this.respectOtherDeathChestMods = this.configuration.get("general", "RespectOtherDeathChestMods", true).getBoolean(true);
      this.worldGenTwilightForest = this.configuration.get("general", "WorldGenInTwilightForest", true).getBoolean(true);
      this.worldGenFrequency = this.configuration.get("general", "WorldGenFrequency", 12).getInt();
      this.allowStatueGoddessRecipe = this.configuration.get("general", "AllowGoddessStatueRecipe", false).getBoolean(false);
      this.allowHellOnEarthFires = this.configuration.get("general", "AllowHellOnEarthFires", true).getBoolean(true);
      this.allowVoidBrambleRecipe = this.configuration.get("general", "AllowVoidBrambleRecipe", false).getBoolean(false);
      this.allowBiomeChanging = this.configuration.get("general", "AllowBiomeModificationRituals", true).getBoolean(true);
      this.covenWitchSpawnWeight = this.configuration.get("general", "CovenWitchSpawnWeight", 2).getInt();
      this.goblinSpawnWeight = this.configuration.get("general", "HobgoblinSpawnChance", 2).getInt();
      this.goblinSpawnRate = this.configuration.get("general", "HobgoblinSpawnWeight", 4).getInt();
      this.goblinDespawnBlock = this.configuration.get("general", "HobgoblinDespawnBlock", true).getBoolean(true);
      this.branchIconSet = this.configuration.get("general", "BranchGlyphSet", 0).getInt();
      this.dimensionDreamID = this.configuration.get("general", "DreamDimensionID", -37).getInt();
      this.dimensionTormentID = this.configuration.get("general", "TormentDimensionID", -38).getInt();
      this.dimensionMirrorID = this.configuration.get("general", "MirrorDimensionID", -39).getInt();
      this.percentageOfPlayersSleepingForBuff = Math.max(Math.min(this.configuration.get("general", "PercentageOfPlayersSleepingForBuff", 100).getInt(), 100), 1);
      this.render3dGlintEffect = this.configuration.get("general", "Render3dGlintEffect", true).getBoolean(true);
      this.renderHuntsmanGlintEffect = this.configuration.get("general", "RenderHuntsmanGlintEffect", true).getBoolean(true);
      this.allowMystcraft = this.configuration.get("general", "AllowModMystcraft", true).getBoolean(true);
      this.allowArsMagica2 = this.configuration.get("general", "AllowModArsMagica2", true).getBoolean(true);
      this.restrictPoppetShelvesToVanillaAndSpiritDimensions = this.configuration.get("general", "RestrictPoppetShelvesToVanillaAndSpiritDimensions", true).getBoolean(true);
      this.allowBlockBreakEvents = this.configuration.get("general", "AllowInterModBlockBreakEvents", true).getBoolean(true);
      this.allowDeathsHoodToFreezeVictims = this.configuration.get("general", "AllowDeathsHoodToFreezeVictims", true).getBoolean(true);
      this.strawmanSpawnerRules = this.configuration.get("general", "StrawmanSpawnerRules", new String[]{"Zombie", "Zombie", "Skeleton"}).getStringList();
      this.generateWitchHuts = this.configuration.get("general", "GenerateWitchHuts", true).getBoolean(true);
      this.generateBookShops = this.configuration.get("general", "GenerateBookShops", true).getBoolean(true);
      this.decurseTeleportPullRadius = Math.min(Math.max(this.configuration.get("general", "DecurseTeleportPullProtectRadius", 32).getInt(), 0), 128);
      this.decurseDirectedRadius = Math.min(Math.max(this.configuration.get("general", "DecurseDirectedProtectRadius", 32).getInt(), 0), 128);
      this.allowDecurseDirected = this.configuration.get("general", "DecurseDirectedEnabled", false).getBoolean(false);
      this.allowDecurseTeleport = this.configuration.get("general", "DecurseTeleportPullEnabled", false).getBoolean(false);
      this.restrictTaglockCollectionOnNonPVPServers = this.configuration.get("general", "RestrictTaglockCollectionOnNonPVPServers", false).getBoolean(false);
      this.restrictTaglockCollectionForStaffMembers = this.configuration.get("general", "RestrictTaglockCollectionForOPs", false).getBoolean(false);
      this.potionStartID = Math.max(this.configuration.get("general", "PotionStartID", 32).getInt(), 32);
      this.mutandisExtras = this.configuration.get("general", "MutandisAdditionalBlocks", new String[]{"witchery:glintweed,0", "tallgrass,2"}).getStringList();
      this.hobgoblinGodSpawnChance = Math.max(Math.min(this.configuration.get("general", "HobgoblinGodSpawnChance", 10).getInt(), 100), 0);
      this.hellhoundSpawnRate = this.configuration.get("general", "HellhoundSpawnWeight", 25).getInt();
      this.spawnWeightSpirit = MathHelper.clamp_int(this.configuration.get("general", "SpiritSpawnWeight", 1).getInt(), 1, 1000);
      this.allowExplodingCreeperHearts = this.configuration.get("general", "CreeperHeartsExplodeWithDamageWhenEaten", true).getBoolean(true);
      this.mantrapAlpha = (float)Math.min(1.0D, Math.max(0.1D, this.configuration.get("general", "MantrapOpacity", 0.3D).getDouble(0.3D)));
      this.allowVolatilityPotionBlockDamage = this.configuration.get("general", "AllowVolatilityPotionBlockDamage", true).getBoolean(true);
      this.diseaseRemovalChance = this.configuration.get("general", "DiseaseBlockRemovalChance", 10).getInt();
      this.vampireDeathItemKeepAliveMins = this.configuration.get("general", "VampireDeathItemKeepAliveMins", 12).getInt();
      this.hudShowVampireTargetBloodText = this.configuration.get("general", "HUDShowVampireTargetBloodText", false).getBoolean(false);
      this.vampireHunterSpawnChance = (double)((float)Math.min(1.0D, Math.max(0.0D, this.configuration.get("general", "VampireHunterSpawnChance", 0.01D).getDouble(0.01D))));
      this.fairestSpawnChance = MathHelper.clamp_double(this.configuration.get("general", "NewFairestOfThemAllSpawnChance", 0.01D).getDouble(0.01D), 0.01D, 1.0D);
      this.allowPlayerToPlayerWolfInfection = this.configuration.get("general", "AllowPlayerToPlayerWolfInfection", true).getBoolean(true);
      this.riteOfEclipseCooldownInSecs = MathHelper.clamp_int(this.configuration.get("general", "RiteOfEclipseCooldownInSecs", 0).getInt(), 0, 3600);
      this.allowChatMasquerading = this.configuration.get("general", "AllowChatMasquerading", true).getBoolean(true);
      this.allowNameplateMasquerading = this.configuration.get("general", "AllowNameplateMasquerading", true).getBoolean(true);
      this.shrinkMirrorWorld = this.configuration.get("general", "ShrinkMirrorWorld", false).getBoolean(false);
      this.townZombieMode = Math.min(Math.max(this.configuration.get("general", "TownZombieAttackReductionMode", 1).getInt(), 0), 2);
      this.townParts = new ArrayList();
      new Config.Building(House4Garden.class, "GardenHouse", 3, 20, 3, 5, this);
      new Config.Building(House1.class, "House", 3, 20, 3, 5, this);
      new Config.Building(WoodHut.class, "WoodHut", 3, 20, 3, 5, this);
      new Config.Building(Hall.class, "Hall", 3, 20, 3, 5, this);
      new Config.Building(House3.class, "House3", 3, 20, 3, 5, this);
      new Config.Building(Field1.class, "SingleField", 3, 20, 3, 5, this);
      new Config.Building(Field2.class, "DoubleField", 3, 20, 3, 5, this);
      new Config.Building(House2.class, "Blacksmith", 1, 5, 0, 1, this);
      new Config.Building(Church.class, "Church", 0, 10, 0, 1, this);
      new Config.Building(ComponentVillageWatchTower.class, "GuardTower", 4, 20, 0, 1, this);
      this.townWallChance = Math.min(Math.max(this.configuration.get("general", "TownWallMode", 1).getInt(), 0), 2);
      this.townWallWeight = Math.min(Math.max(this.configuration.get("general", "TownWallWeight", 100).getInt(), 0), 1000);
      this.townKeepChance = Math.min(Math.max(this.configuration.get("general", "TownKeepMode", 1).getInt(), 0), 2);
      this.townKeepWeight = Math.min(Math.max(this.configuration.get("general", "TownKeepWeight", 100).getInt(), 0), 1000);
      this.townAllowSandy = this.configuration.get("general", "TownBiomeSandyAllowed", true).getBoolean(true);
      this.townAllowPlains = this.configuration.get("general", "TownBiomePlainsAllowed", true).getBoolean(true);
      this.townAllowMountain = this.configuration.get("general", "TownBiomeMountainAllowed", true).getBoolean(true);
      this.townAllowHills = this.configuration.get("general", "TownBiomeHillsAllowed", true).getBoolean(true);
      this.townAllowForest = this.configuration.get("general", "TownBiomeForestAllowed", true).getBoolean(true);
      this.townAllowSnowy = this.configuration.get("general", "TownBiomeSnowyAllowed", true).getBoolean(true);
      this.townAllowWasteland = this.configuration.get("general", "TownBiomeWastelandAllowed", true).getBoolean(true);
      this.townAllowMesa = this.configuration.get("general", "TownBiomeMesaAllowed", true).getBoolean(true);
      this.townAllowJungle = this.configuration.get("general", "TownBiomeJungleAllowed", false).getBoolean(false);
      this.townBooks = this.configuration.get("general", "TownBookshopAllowedBooks", new String[]{"book", "witchery:ingredient,46", "witchery:ingredient,47", "witchery:ingredient,48", "witchery:ingredient,49", "witchery:ingredient,81", "witchery:ingredient,106", "witchery:ingredient,107", "witchery:ingredient,127", "witchery:bookbiomes2", "witchery:cauldronbook", "Thaumcraft:ItemThaumonomicon", "TConstruct:manualBook", "TConstruct:manualBook,1", "TConstruct:manualBook,2", "TConstruct:manualBook,3"}).getStringList();
      String[] replaceableBlocks = this.configuration.get("general", "NaturesPowerReplaceableBlocks", new String[]{"mycelium"}).getStringList();
      this.naturePowerReplaceableBlocks = new ArrayList();
      String[] arr$ = replaceableBlocks;
      int len$ = replaceableBlocks.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String extra = arr$[i$];

         try {
            this.naturePowerReplaceableBlocks.add(new Config.BlockMeta(extra));
         } catch (Throwable var7) {
            ;
         }
      }

      this.allowVampireWolfHybrids = this.configuration.get("general", "AllowVampireWerewolfHybrids", true).getBoolean(true);
      this.allowStakingVampires = this.configuration.get("general", "AllowStakingVampires", true).getBoolean(true);
      this.allowCovenWitchVisits = this.configuration.get("general", "AllowCovenWitchVisits", true).getBoolean(true);
      this.allowVampireQuests = this.configuration.get("general", "AllowVampireQuests", true).getBoolean(true);
      this.allowVampireRitual = this.configuration.get("general", "AllowVampireRitual", true).getBoolean(true);
      this.saveIfChanged();
   }

   public boolean isDebugging() {
      return this.debugging;
   }

   public boolean traceRites() {
      return this.traceRitesEnabled;
   }

   public void saveIfChanged() {
      if(this.configuration.hasChanged()) {
         this.configuration.save();
      }

   }

   public boolean isReduceZombeVillagerDamageActive() {
      return this.townZombieMode >= 1;
   }

   public boolean isZombeIgnoreVillagerActive() {
      return this.townZombieMode >= 2;
   }

   public boolean canReplaceNaturalBlock(Block block, int meta) {
      Iterator i$ = this.naturePowerReplaceableBlocks.iterator();

      Config.BlockMeta bm;
      do {
         if(!i$.hasNext()) {
            return false;
         }

         bm = (Config.BlockMeta)i$.next();
      } while(!bm.isMatch(block, meta));

      return true;
   }


   public static class Building {

      private static final String TOWN = "Town";
      public final int groups;
      public final int weight;
      public final int min;
      public final int max;
      public final Class clazz;


      public Building(Class clazz, String name, int groups, int weight, int min, int max, Config config) {
         this.clazz = clazz;
         this.groups = config.configuration.get("general", "Town" + name + "ClusterGroups", groups).getInt();
         this.weight = config.configuration.get("general", "Town" + name + "ClusterWeight", weight).getInt();
         this.min = config.configuration.get("general", "Town" + name + "ClusterMin", min).getInt();
         this.max = config.configuration.get("general", "Town" + name + "ClusterMax", max).getInt();
         config.townParts.add(this);
      }
   }

   private static class BlockMeta {

      private final Block block;
      private final int metadata;


      public BlockMeta(String extra) {
         String name = extra;
         int meta = 32767;
         int comma = extra.lastIndexOf(44);
         if(comma >= 0) {
            name = extra.substring(0, comma);
            String metaString = extra.substring(comma + 1);
            meta = Integer.parseInt(metaString);
         }

         this.block = Block.getBlockFromName(name);
         this.metadata = meta;
      }

      public boolean isMatch(Block b, int m) {
         return b == this.block && (this.metadata == 32767 || this.metadata == m);
      }
   }
}
