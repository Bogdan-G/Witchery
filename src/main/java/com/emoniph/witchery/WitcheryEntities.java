package com.emoniph.witchery;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.EntityBrew;
import com.emoniph.witchery.brewing.EntityDroplet;
import com.emoniph.witchery.brewing.EntitySplatter;
import com.emoniph.witchery.entity.EntityAttackBat;
import com.emoniph.witchery.entity.EntityBabaYaga;
import com.emoniph.witchery.entity.EntityBanshee;
import com.emoniph.witchery.entity.EntityBolt;
import com.emoniph.witchery.entity.EntityBroom;
import com.emoniph.witchery.entity.EntityCorpse;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.entity.EntityDarkMark;
import com.emoniph.witchery.entity.EntityDeath;
import com.emoniph.witchery.entity.EntityDeathsHorse;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityEnt;
import com.emoniph.witchery.entity.EntityEye;
import com.emoniph.witchery.entity.EntityFamiliar;
import com.emoniph.witchery.entity.EntityFollower;
import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.entity.EntityGoblinGulg;
import com.emoniph.witchery.entity.EntityGoblinMog;
import com.emoniph.witchery.entity.EntityGrenade;
import com.emoniph.witchery.entity.EntityHellhound;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.entity.EntityIllusionCreeper;
import com.emoniph.witchery.entity.EntityIllusionSpider;
import com.emoniph.witchery.entity.EntityIllusionZombie;
import com.emoniph.witchery.entity.EntityImp;
import com.emoniph.witchery.entity.EntityItemWaystone;
import com.emoniph.witchery.entity.EntityLeonard;
import com.emoniph.witchery.entity.EntityLilith;
import com.emoniph.witchery.entity.EntityLordOfTorment;
import com.emoniph.witchery.entity.EntityLostSoul;
import com.emoniph.witchery.entity.EntityMandrake;
import com.emoniph.witchery.entity.EntityMindrake;
import com.emoniph.witchery.entity.EntityMirrorFace;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.entity.EntityOwl;
import com.emoniph.witchery.entity.EntityParasyticLouse;
import com.emoniph.witchery.entity.EntityPoltergeist;
import com.emoniph.witchery.entity.EntityReflection;
import com.emoniph.witchery.entity.EntitySoulfire;
import com.emoniph.witchery.entity.EntitySpectre;
import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.entity.EntityToad;
import com.emoniph.witchery.entity.EntityTreefyd;
import com.emoniph.witchery.entity.EntityVampire;
import com.emoniph.witchery.entity.EntityVillageGuard;
import com.emoniph.witchery.entity.EntityVillagerWere;
import com.emoniph.witchery.entity.EntityWingedMonkey;
import com.emoniph.witchery.entity.EntityWitchCat;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.entity.EntityWitchProjectile;
import com.emoniph.witchery.entity.EntityWolfman;
import com.emoniph.witchery.util.Config;
import cpw.mods.fml.common.registry.EntityRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class WitcheryEntities {

   private final ArrayList entities = new ArrayList();
   public final WitcheryEntities.EntityRef DEMON;
   public final WitcheryEntities.EntityRef BROOM;
   public final WitcheryEntities.EntityRef BREW;
   public final WitcheryEntities.EntityRef SPECTRAL_FAMILIAR;
   public final WitcheryEntities.EntityRef MANDRAKE;
   public final WitcheryEntities.EntityRef TREEFYD;
   public final WitcheryEntities.EntityRef HUNTSMAN;
   public final WitcheryEntities.EntityRef SPELL;
   public final WitcheryEntities.EntityRef ENT;
   public final WitcheryEntities.EntityRef ILLUSION_CREEPER;
   public final WitcheryEntities.EntityRef ILLUSION_SPIDER;
   public final WitcheryEntities.EntityRef ILLUSION_ZOMBIE;
   public final WitcheryEntities.EntityRef OWL;
   public final WitcheryEntities.EntityRef TOAD;
   public final WitcheryEntities.EntityRef CAT_FAMILIAR;
   public final WitcheryEntities.EntityRef LOUSE;
   public final WitcheryEntities.EntityRef EYE;
   public final WitcheryEntities.EntityRef BABA_YAGA;
   public final WitcheryEntities.EntityRef COVEN_WITCH;
   public final WitcheryEntities.EntityRef PLAYER_CORPSE;
   public final WitcheryEntities.EntityRef NIGHTMARE;
   public final WitcheryEntities.EntityRef SPECTRE;
   public final WitcheryEntities.EntityRef POLTERGEIST;
   public final WitcheryEntities.EntityRef BANSHEE;
   private static final int MIN_SPIRIT_GROUP = 2;
   private static final int MAX_SPIRIT_GROUP = 5;
   public final WitcheryEntities.EntityRef SPIRIT;
   public final WitcheryEntities.EntityRef DEATH;
   public final WitcheryEntities.EntityRef CROSSBOW_BOLT;
   public final WitcheryEntities.EntityRef WITCH_HUNTER;
   public final WitcheryEntities.EntityRef BINKY_HORSE;
   public final WitcheryEntities.EntityRef LORD_OF_TORMENT;
   public final WitcheryEntities.EntityRef SOULFIRE;
   public final WitcheryEntities.EntityRef IMP;
   public final WitcheryEntities.EntityRef DARK_MARK;
   public final WitcheryEntities.EntityRef MINDRAKE;
   public final WitcheryEntities.EntityRef GOBLIN;
   public final WitcheryEntities.EntityRef GOBLIN_MOG;
   public final WitcheryEntities.EntityRef GOBLIN_GULG;
   public final WitcheryEntities.EntityRef BREW2;
   public final WitcheryEntities.EntityRef ITEM_WAYSTONE;
   public final WitcheryEntities.EntityRef DROPLET;
   public final WitcheryEntities.EntityRef SPLATTER;
   public final WitcheryEntities.EntityRef LEONARD;
   public final WitcheryEntities.EntityRef LOST_SOUL;
   public final WitcheryEntities.EntityRef WOLFMAN;
   public final WitcheryEntities.EntityRef HELLHOUND;
   public final WitcheryEntities.EntityRef WERE_VILLAGER;
   public final WitcheryEntities.EntityRef VILLAGE_GUARD;
   public final WitcheryEntities.EntityRef VAMPIRE_VILLAGER;
   public final WitcheryEntities.EntityRef GRENADE;
   public final WitcheryEntities.EntityRef LILITH;
   public final WitcheryEntities.EntityRef FOLLOWER;
   public final WitcheryEntities.EntityRef WINGED_MONKEY;
   public final WitcheryEntities.EntityRef ATTACK_BAT;
   public final WitcheryEntities.EntityRef MIRROR_FACE;
   public final WitcheryEntities.EntityRef REFLECTION;


   public WitcheryEntities() {
      this.DEMON = (new WitcheryEntities.LivingRef(92, EntityDemon.class, "demon", this.entities)).setEgg(9502720, 11430927);
      this.BROOM = new WitcheryEntities.EntityRef(93, EntityBroom.class, "broom", this.entities);
      this.BREW = new WitcheryEntities.EntityRef(94, EntityWitchProjectile.class, "brew", 64, 3, this.entities);
      this.SPECTRAL_FAMILIAR = new WitcheryEntities.LivingRef(95, EntityFamiliar.class, "familiar", this.entities);
      this.MANDRAKE = (new WitcheryEntities.LivingRef(96, EntityMandrake.class, "mandrake", this.entities)).setEgg(128271104, 311408);
      this.TREEFYD = (new WitcheryEntities.LivingRef(97, EntityTreefyd.class, "treefyd", this.entities)).setEgg(5781801, 11217964);
      this.HUNTSMAN = (new WitcheryEntities.LivingRef(98, EntityHornedHuntsman.class, "hornedHuntsman", this.entities)).setEgg(11523, 4007964);
      this.SPELL = new WitcheryEntities.EntityRef(99, EntitySpellEffect.class, "spellEffect", 64, 3, this.entities);
      this.ENT = (new WitcheryEntities.LivingRef(100, EntityEnt.class, "ent", this.entities)).setEgg(5338965, 5724240);
      this.ILLUSION_CREEPER = new WitcheryEntities.LivingRef(101, EntityIllusionCreeper.class, "illusionCreeper", this.entities);
      this.ILLUSION_SPIDER = new WitcheryEntities.LivingRef(102, EntityIllusionSpider.class, "illusionSpider", this.entities);
      this.ILLUSION_ZOMBIE = new WitcheryEntities.LivingRef(103, EntityIllusionZombie.class, "illusionZombie", this.entities);
      this.OWL = (new WitcheryEntities.LivingRef(104, EntityOwl.class, "owl", this.entities)).setEgg(14869218, 6049609);
      this.TOAD = (new WitcheryEntities.LivingRef(105, EntityToad.class, "toad", this.entities)).setEgg(5780254, 3090974);
      this.CAT_FAMILIAR = new WitcheryEntities.LivingRef(106, EntityWitchCat.class, "cat", this.entities);
      this.LOUSE = new WitcheryEntities.LivingRef(107, EntityParasyticLouse.class, "louse", this.entities);
      this.EYE = new WitcheryEntities.LivingRef(108, EntityEye.class, "eye", 150, 1, this.entities);
      this.BABA_YAGA = (new WitcheryEntities.LivingRef(109, EntityBabaYaga.class, "babayaga", this.entities)).setEgg(7232598, 3881787);
      this.COVEN_WITCH = (new WitcheryEntities.LivingRef(110, EntityCovenWitch.class, "covenwitch", this.entities)).addSpawn(2, 1, 1, EnumCreatureType.creature, new BiomeGenBase[]{BiomeGenBase.swampland}).addSpawn(1, 1, 1, EnumCreatureType.creature, BiomeDictionary.getBiomesForType(Type.FOREST)).setEgg(1118481, 11523);
      this.PLAYER_CORPSE = new WitcheryEntities.LivingRef(111, EntityCorpse.class, "corpse", this.entities);
      this.NIGHTMARE = (new WitcheryEntities.LivingRef(112, EntityNightmare.class, "nightmare", this.entities)).setEgg(983101, 0);
      this.SPECTRE = (new WitcheryEntities.LivingRef(113, EntitySpectre.class, "spectre", this.entities)).setEgg(1052688, 16299031);
      this.POLTERGEIST = (new WitcheryEntities.LivingRef(114, EntityPoltergeist.class, "poltergeist", this.entities)).setEgg(12844917, 12844917);
      this.BANSHEE = (new WitcheryEntities.LivingRef(115, EntityBanshee.class, "banshee", this.entities)).setEgg(13683116, 10136945);
      this.SPIRIT = (new WitcheryEntities.LivingRef(116, EntitySpirit.class, "spirit", this.entities)).addSpawn(Config.instance().spawnWeightSpirit, 2, 5, EnumCreatureType.ambient, BiomeDictionary.getBiomesForType(Type.MESA)).addSpawn(Config.instance().spawnWeightSpirit, 2, 5, EnumCreatureType.ambient, BiomeDictionary.getBiomesForType(Type.FOREST)).addSpawn(Config.instance().spawnWeightSpirit, 2, 5, EnumCreatureType.ambient, BiomeDictionary.getBiomesForType(Type.PLAINS)).addSpawn(Config.instance().spawnWeightSpirit, 2, 5, EnumCreatureType.ambient, BiomeDictionary.getBiomesForType(Type.MOUNTAIN)).addSpawn(Config.instance().spawnWeightSpirit, 2, 5, EnumCreatureType.ambient, BiomeDictionary.getBiomesForType(Type.HILLS)).addSpawn(Config.instance().spawnWeightSpirit, 2, 5, EnumCreatureType.ambient, BiomeDictionary.getBiomesForType(Type.SWAMP)).addSpawn(Config.instance().spawnWeightSpirit, 2, 5, EnumCreatureType.ambient, BiomeDictionary.getBiomesForType(Type.SANDY)).addSpawn(Config.instance().spawnWeightSpirit, 2, 5, EnumCreatureType.ambient, BiomeDictionary.getBiomesForType(Type.SNOWY)).addSpawn(Config.instance().spawnWeightSpirit, 2, 5, EnumCreatureType.ambient, BiomeDictionary.getBiomesForType(Type.WASTELAND)).setEgg(16753968, 15649280);
      this.DEATH = (new WitcheryEntities.LivingRef(117, EntityDeath.class, "death", this.entities)).setEgg(0, 0);
      this.CROSSBOW_BOLT = new WitcheryEntities.EntityRef(118, EntityBolt.class, "bolt", 64, 10, this.entities);
      this.WITCH_HUNTER = (new WitcheryEntities.LivingRef(119, EntityWitchHunter.class, "witchhunter", this.entities)).setEgg(7893099, 2300953);
      this.BINKY_HORSE = new WitcheryEntities.LivingRef(120, EntityDeathsHorse.class, "deathhorse", this.entities);
      this.LORD_OF_TORMENT = (new WitcheryEntities.LivingRef(121, EntityLordOfTorment.class, "lordoftorment", this.entities)).setEgg(9502720, 3346705);
      this.SOULFIRE = new WitcheryEntities.EntityRef(122, EntitySoulfire.class, "soulfire", 64, 3, this.entities);
      this.IMP = (new WitcheryEntities.LivingRef(123, EntityImp.class, "imp", 64, 3, this.entities)).setEgg(5776143, 16738816);
      this.DARK_MARK = new WitcheryEntities.LivingRef(124, EntityDarkMark.class, "darkmark", 64, 3, this.entities);
      this.MINDRAKE = (new WitcheryEntities.LivingRef(125, EntityMindrake.class, "mindrake", 64, 3, this.entities)).setEgg(19463, 4200704);
      this.GOBLIN = (new WitcheryEntities.LivingRef(126, EntityGoblin.class, "goblin", 64, 3, this.entities)).addSpawn(Math.max(Config.instance().goblinSpawnRate, 1), 1, 2, EnumCreatureType.creature, BiomeDictionary.getBiomesForType(Type.SWAMP)).addSpawn(Math.max(Config.instance().goblinSpawnRate, 1), 1, 3, EnumCreatureType.creature, BiomeDictionary.getBiomesForType(Type.FOREST)).addSpawn(Math.max(Config.instance().goblinSpawnRate - 1, 1), 1, 2, EnumCreatureType.creature, BiomeDictionary.getBiomesForType(Type.PLAINS)).setEgg(10752, 15616);
      this.GOBLIN_MOG = (new WitcheryEntities.LivingRef(127, EntityGoblinMog.class, "goblinmog", 64, 3, this.entities)).setEgg(10752, 15616);
      this.GOBLIN_GULG = (new WitcheryEntities.LivingRef(128, EntityGoblinGulg.class, "goblingulg", 64, 3, this.entities)).setEgg(10752, 15616);
      this.BREW2 = new WitcheryEntities.EntityRef(129, EntityBrew.class, "brew2", 64, 1, this.entities);
      this.ITEM_WAYSTONE = new WitcheryEntities.EntityRef(130, EntityItemWaystone.class, "item", 64, 20, this.entities);
      this.DROPLET = new WitcheryEntities.EntityRef(131, EntityDroplet.class, "droplet", 64, 20, this.entities);
      this.SPLATTER = new WitcheryEntities.EntityRef(132, EntitySplatter.class, "splatter", 64, 20, this.entities);
      this.LEONARD = (new WitcheryEntities.LivingRef(133, EntityLeonard.class, "leonard", this.entities)).setEgg(12152634, 2818048);
      this.LOST_SOUL = (new WitcheryEntities.LivingRef(134, EntityLostSoul.class, "lostsoul", this.entities)).setEgg(12152634, 2818116);
      this.WOLFMAN = (new WitcheryEntities.LivingRef(135, EntityWolfman.class, "wolfman", this.entities)).setEgg(2960685, 6316128);
      this.HELLHOUND = (new WitcheryEntities.LivingRef(136, EntityHellhound.class, "hellhound", this.entities)).addSpawn(Math.max(Config.instance().hellhoundSpawnRate, 1), 1, 3, EnumCreatureType.monster, BiomeDictionary.getBiomesForType(Type.NETHER)).setEgg(14181632, 5968392);
      this.WERE_VILLAGER = (new WitcheryEntities.LivingRef(137, EntityVillagerWere.class, "werevillager", this.entities)).setEgg(5651507, 12422002);
      this.VILLAGE_GUARD = (new WitcheryEntities.LivingRef(138, EntityVillageGuard.class, "villageguard", this.entities)).setEgg(2236962, 5322800);
      this.VAMPIRE_VILLAGER = (new WitcheryEntities.LivingRef(139, EntityVampire.class, "vampire", this.entities)).setEgg(5322800, 13369344);
      this.GRENADE = new WitcheryEntities.EntityRef(140, EntityGrenade.class, "grenade", 64, 1, this.entities);
      this.LILITH = (new WitcheryEntities.LivingRef(141, EntityLilith.class, "lilith", this.entities)).setEgg(0, 2818048);
      this.FOLLOWER = new WitcheryEntities.LivingRef(142, EntityFollower.class, "follower", this.entities);
      this.WINGED_MONKEY = (new WitcheryEntities.LivingRef(143, EntityWingedMonkey.class, "wingedmonkey", this.entities)).setEgg(6846848, 7574709);
      this.ATTACK_BAT = new WitcheryEntities.LivingRef(144, EntityAttackBat.class, "attackbat", this.entities);
      this.MIRROR_FACE = new WitcheryEntities.LivingRef(145, EntityMirrorFace.class, "mirrorface", this.entities);
      this.REFLECTION = (new WitcheryEntities.LivingRef(146, EntityReflection.class, "reflection", this.entities)).setEgg(5596842, 6715391);
   }

   public List getEntites() {
      return this.entities;
   }

   public void init() {}

   public static class EntityRef {

      public final Class entity_class;
      public final String entity_name;
      public boolean can_capture;
      public boolean can_spawn;
      public boolean can_grind;
      private static int eggRoot = 6395;


      public EntityRef(int id, Class clazz, String name, ArrayList registry) {
         this(id, clazz, name, 80, 3, registry);
      }

      public EntityRef(int id, Class clazz, String name, int range, int updates, ArrayList registry) {
         this.entity_class = clazz;
         this.entity_name = name;
         EntityRegistry.registerModEntity(clazz, name, id, Witchery.instance, range, updates, true);
         registry.add(this);
      }

      public WitcheryEntities.EntityRef setPropsMFR(boolean canCapture, boolean canSpawn, boolean canGrind) {
         this.can_capture = canCapture;
         this.can_spawn = canSpawn;
         this.can_grind = canGrind;
         return this;
      }

      public WitcheryEntities.EntityRef setEgg(int color1, int color2) {
         int eggID = getUniqueEggId();
         EntityList.IDtoClassMapping.put(Integer.valueOf(eggID), this.entity_class);
         EntityList.entityEggs.put(Integer.valueOf(eggID), new EntityEggInfo(eggID, color1, color2));
         return this;
      }

      private static int getUniqueEggId() {
         do {
            ++eggRoot;
         } while(EntityList.getStringFromID(eggRoot) != null);

         return eggRoot;
      }

   }

   public static class LivingRef extends WitcheryEntities.EntityRef {

      public final Class living_class;


      public LivingRef(int id, Class clazz, String name, ArrayList registry) {
         super(id, clazz, name, 80, 3, registry);
         this.living_class = clazz;
      }

      public LivingRef(int id, Class clazz, String name, int range, int updates, ArrayList registry) {
         super(id, clazz, name, range, updates, registry);
         this.living_class = clazz;
      }

      public WitcheryEntities.LivingRef addSpawn(int weight, int min, int max, EnumCreatureType type, BiomeGenBase ... biomes) {
         EntityRegistry.addSpawn(this.living_class, weight, min, max, type, biomes);
         return this;
      }
   }
}
