package com.emoniph.witchery;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.crafting.DistilleryRecipes;
import com.emoniph.witchery.crafting.KettleRecipes;
import com.emoniph.witchery.crafting.RecipeAttachTaglock;
import com.emoniph.witchery.crafting.RecipeShapelessAddColor;
import com.emoniph.witchery.crafting.RecipeShapelessAddKeys;
import com.emoniph.witchery.crafting.RecipeShapelessAddPotion;
import com.emoniph.witchery.crafting.RecipeShapelessBiomeCopy;
import com.emoniph.witchery.crafting.RecipeShapelessPoppet;
import com.emoniph.witchery.crafting.RecipeShapelessRepair;
import com.emoniph.witchery.crafting.SpinningRecipes;
import com.emoniph.witchery.entity.EntityBabaYaga;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityEnt;
import com.emoniph.witchery.entity.EntityFamiliar;
import com.emoniph.witchery.entity.EntityImp;
import com.emoniph.witchery.entity.EntityOwl;
import com.emoniph.witchery.entity.EntityReflection;
import com.emoniph.witchery.entity.EntityToad;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.InfusionInfernal;
import com.emoniph.witchery.infusion.infusions.InfusionLight;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.infusion.infusions.InfusionOverworld;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerBat;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerBlaze;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerCreeper;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerEnderman;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerGhast;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerHeal;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerJump;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerPigMan;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerSkeleton;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerSpeed;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerSpider;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerSquid;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerZombie;
import com.emoniph.witchery.predictions.PredictionArrow;
import com.emoniph.witchery.predictions.PredictionBuriedTreasure;
import com.emoniph.witchery.predictions.PredictionFall;
import com.emoniph.witchery.predictions.PredictionFallInLove;
import com.emoniph.witchery.predictions.PredictionFight;
import com.emoniph.witchery.predictions.PredictionManager;
import com.emoniph.witchery.predictions.PredictionMultiMine;
import com.emoniph.witchery.predictions.PredictionNetherTrip;
import com.emoniph.witchery.predictions.PredictionRescue;
import com.emoniph.witchery.predictions.PredictionWet;
import com.emoniph.witchery.ritual.Circle;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualTraits;
import com.emoniph.witchery.ritual.Sacrifice;
import com.emoniph.witchery.ritual.SacrificeItem;
import com.emoniph.witchery.ritual.SacrificeLiving;
import com.emoniph.witchery.ritual.SacrificeMultiple;
import com.emoniph.witchery.ritual.SacrificeOptionalItem;
import com.emoniph.witchery.ritual.SacrificePower;
import com.emoniph.witchery.ritual.rites.RiteBanishDemon;
import com.emoniph.witchery.ritual.rites.RiteBindCircleToTalisman;
import com.emoniph.witchery.ritual.rites.RiteBindFamiliar;
import com.emoniph.witchery.ritual.rites.RiteBindSpiritsToFetish;
import com.emoniph.witchery.ritual.rites.RiteBlight;
import com.emoniph.witchery.ritual.rites.RiteBlindness;
import com.emoniph.witchery.ritual.rites.RiteCallCreatures;
import com.emoniph.witchery.ritual.rites.RiteCallFamiliar;
import com.emoniph.witchery.ritual.rites.RiteClimateChange;
import com.emoniph.witchery.ritual.rites.RiteCookItem;
import com.emoniph.witchery.ritual.rites.RiteCurseCreature;
import com.emoniph.witchery.ritual.rites.RiteCurseOfTheWolf;
import com.emoniph.witchery.ritual.rites.RiteCursePoppets;
import com.emoniph.witchery.ritual.rites.RiteEclipse;
import com.emoniph.witchery.ritual.rites.RiteFertility;
import com.emoniph.witchery.ritual.rites.RiteForestation;
import com.emoniph.witchery.ritual.rites.RiteGlyphicTransformation;
import com.emoniph.witchery.ritual.rites.RiteHellOnEarth;
import com.emoniph.witchery.ritual.rites.RiteInfusePlayers;
import com.emoniph.witchery.ritual.rites.RiteInfusionRecharge;
import com.emoniph.witchery.ritual.rites.RiteNaturesPower;
import com.emoniph.witchery.ritual.rites.RitePartEarth;
import com.emoniph.witchery.ritual.rites.RitePriorIncarnation;
import com.emoniph.witchery.ritual.rites.RiteProtectionCircleAttractive;
import com.emoniph.witchery.ritual.rites.RiteProtectionCircleBarrier;
import com.emoniph.witchery.ritual.rites.RiteProtectionCircleRepulsive;
import com.emoniph.witchery.ritual.rites.RiteRainOfToads;
import com.emoniph.witchery.ritual.rites.RiteRaiseColumn;
import com.emoniph.witchery.ritual.rites.RiteRaiseVolcano;
import com.emoniph.witchery.ritual.rites.RiteRemoveVampirism;
import com.emoniph.witchery.ritual.rites.RiteSetNBT;
import com.emoniph.witchery.ritual.rites.RiteSphereEffect;
import com.emoniph.witchery.ritual.rites.RiteSummonCreature;
import com.emoniph.witchery.ritual.rites.RiteSummonItem;
import com.emoniph.witchery.ritual.rites.RiteSummonSpectralStone;
import com.emoniph.witchery.ritual.rites.RiteTeleportEntity;
import com.emoniph.witchery.ritual.rites.RiteTeleportToWaystone;
import com.emoniph.witchery.ritual.rites.RiteTransposeOres;
import com.emoniph.witchery.ritual.rites.RiteWeatherCallStorm;
import com.emoniph.witchery.util.ClothColor;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Dye;
import cpw.mods.fml.common.registry.GameRegistry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class WitcheryRecipes {

   public Infusion infusionEnder;
   public Infusion infusionLight;
   public Infusion infusionWorld;
   public Infusion infusionBeast;


   public void preInit() {
      RecipeSorter.register("witchery:bindpoppet", RecipeShapelessPoppet.class, Category.SHAPELESS, "after:minecraft:shapeless");
      RecipeSorter.register("witchery:addpotion", RecipeShapelessAddPotion.class, Category.SHAPELESS, "after:minecraft:shapeless");
      RecipeSorter.register("witchery:repair", RecipeShapelessRepair.class, Category.SHAPELESS, "after:minecraft:shapeless");
      RecipeSorter.register("witchery:addcolor", RecipeShapelessAddColor.class, Category.SHAPELESS, "after:minecraft:shapeless");
      RecipeSorter.register("witchery:addkeys", RecipeShapelessAddKeys.class, Category.SHAPELESS, "after:minecraft:shapeless");
      RecipeSorter.register("witchery:attachtaglock", RecipeAttachTaglock.class, Category.SHAPELESS, "after:minecraft:shapeless");
      RecipeSorter.register("witchery:biomecopy", RecipeShapelessBiomeCopy.class, Category.SHAPELESS, "after:minecraft:shapeless");
      if(Config.instance().allowStatueGoddessRecipe) {
         GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.STATUE_GODDESS), new Object[]{"s#s", "shs", "###", Character.valueOf('h'), Witchery.Items.GENERIC.itemDemonHeart.createStack(), Character.valueOf('#'), new ItemStack(Blocks.stone), Character.valueOf('s'), new ItemStack(Items.nether_star)});
      }

      ItemStack ash = Witchery.Items.GENERIC.itemAshWood.createStack();
      ItemStack bone = new ItemStack(Items.bone);
      GameRegistry.addShapelessRecipe(Dye.BONE_MEAL.createStack(4), new Object[]{bone, ash, ash});
      GameRegistry.addShapelessRecipe(Dye.BONE_MEAL.createStack(5), new Object[]{bone, ash, ash, ash, ash});
      GameRegistry.addShapelessRecipe(Dye.BONE_MEAL.createStack(6), new Object[]{bone, ash, ash, ash, ash, ash, ash});
      GameRegistry.addShapelessRecipe(Dye.BONE_MEAL.createStack(7), new Object[]{bone, ash, ash, ash, ash, ash, ash, ash, ash});
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Witchery.Blocks.WICKER_BUNDLE, 1, 0), new Object[]{"###", "###", "###", Character.valueOf('#'), "treeSapling"}));
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.WICKER_BUNDLE, 5, 1), new Object[]{"#b#", "###", Character.valueOf('#'), new ItemStack(Witchery.Blocks.WICKER_BUNDLE), Character.valueOf('b'), Witchery.Items.GENERIC.itemInfernalBlood.createStack()});
      this.addPlantMineRecipe(0, new ItemStack(Blocks.red_flower), Witchery.Items.GENERIC.itemBrewOfWebs.createStack());
      this.addPlantMineRecipe(1, new ItemStack(Blocks.red_flower), Witchery.Items.GENERIC.itemBrewOfInk.createStack());
      this.addPlantMineRecipe(2, new ItemStack(Blocks.red_flower), Witchery.Items.GENERIC.itemBrewOfThorns.createStack());
      this.addPlantMineRecipe(3, new ItemStack(Blocks.red_flower), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack());
      this.addPlantMineRecipe(4, new ItemStack(Blocks.yellow_flower), Witchery.Items.GENERIC.itemBrewOfWebs.createStack());
      this.addPlantMineRecipe(5, new ItemStack(Blocks.yellow_flower), Witchery.Items.GENERIC.itemBrewOfInk.createStack());
      this.addPlantMineRecipe(6, new ItemStack(Blocks.yellow_flower), Witchery.Items.GENERIC.itemBrewOfThorns.createStack());
      this.addPlantMineRecipe(7, new ItemStack(Blocks.yellow_flower), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack());
      this.addPlantMineRecipe(8, new ItemStack(Blocks.deadbush), Witchery.Items.GENERIC.itemBrewOfWebs.createStack());
      this.addPlantMineRecipe(9, new ItemStack(Blocks.deadbush), Witchery.Items.GENERIC.itemBrewOfInk.createStack());
      this.addPlantMineRecipe(10, new ItemStack(Blocks.deadbush), Witchery.Items.GENERIC.itemBrewOfThorns.createStack());
      this.addPlantMineRecipe(11, new ItemStack(Blocks.deadbush), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack());
      GameRegistry.addShapelessRecipe(new ItemStack(Items.poisonous_potato, 2), new Object[]{new ItemStack(Items.poisonous_potato), new ItemStack(Items.potato), new ItemStack(Items.spider_eye)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.LEAPING_LILY, 5), new Object[]{"#p#", "c#c", "#b#", Character.valueOf('#'), new ItemStack(Blocks.waterlily), Character.valueOf('p'), new ItemStack(Items.potionitem, 1, 8194), Character.valueOf('b'), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), Character.valueOf('c'), new ItemStack(Items.glowstone_dust)});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemBoneNeedle.createStack(8), new Object[]{"ab", Character.valueOf('a'), new ItemStack(Items.bone), Character.valueOf('b'), new ItemStack(Items.flint)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.TAGLOCK_KIT), new Object[]{"ab", Character.valueOf('b'), Witchery.Items.GENERIC.itemBoneNeedle.createStack(), Character.valueOf('a'), new ItemStack(Items.glass_bottle)});
      ItemStack taglocks = new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1);
      ItemStack unboundPoppet = Witchery.Items.POPPET.unboundPoppet.createStack();
      GameRegistry.addRecipe(unboundPoppet, new Object[]{"xyx", "ayb", "x x", Character.valueOf('x'), new ItemStack(Blocks.wool), Character.valueOf('y'), new ItemStack(Witchery.Blocks.SPANISH_MOSS), Character.valueOf('a'), Witchery.Items.GENERIC.itemBoneNeedle.createStack(), Character.valueOf('b'), new ItemStack(Items.string)});
      ItemStack earthPoppet = Witchery.Items.POPPET.earthPoppet.createStack();
      GameRegistry.addRecipe(Witchery.Items.POPPET.earthPoppet.createStack(), new Object[]{" a ", "b#b", " c ", Character.valueOf('#'), Witchery.Items.POPPET.unboundPoppet.createStack(), Character.valueOf('b'), new ItemStack(Items.feather), Character.valueOf('a'), new ItemStack(Items.clay_ball), Character.valueOf('c'), new ItemStack(Blocks.dirt)});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(earthPoppet, new ItemStack[]{taglocks, earthPoppet}));
      ItemStack waterPoppet = Witchery.Items.POPPET.waterPoppet.createStack();
      GameRegistry.addRecipe(waterPoppet, new Object[]{" a ", "b#b", " a ", Character.valueOf('#'), Witchery.Items.POPPET.unboundPoppet.createStack(), Character.valueOf('a'), Witchery.Items.GENERIC.itemArtichoke.createStack(), Character.valueOf('b'), Dye.INK_SAC.createStack()});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(waterPoppet, new ItemStack[]{taglocks, waterPoppet}));
      ItemStack foodPoppet = Witchery.Items.POPPET.foodPoppet.createStack();
      GameRegistry.addRecipe(foodPoppet, new Object[]{" a ", "b#b", " a ", Character.valueOf('#'), unboundPoppet, Character.valueOf('b'), new ItemStack(Items.speckled_melon), Character.valueOf('a'), new ItemStack(Items.rotten_flesh)});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(foodPoppet, new ItemStack[]{taglocks, foodPoppet}));
      ItemStack firePoppet = Witchery.Items.POPPET.firePoppet.createStack();
      GameRegistry.addRecipe(firePoppet, new Object[]{" a ", "b#b", " a ", Character.valueOf('#'), unboundPoppet, Character.valueOf('b'), Witchery.Items.GENERIC.itemBatWool.createStack(), Character.valueOf('a'), new ItemStack(Witchery.Blocks.EMBER_MOSS)});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(firePoppet, new ItemStack[]{taglocks, firePoppet}));
      ItemStack antiVoodooPoppet = Witchery.Items.POPPET.antiVoodooPoppet.createStack();
      GameRegistry.addRecipe(antiVoodooPoppet, new Object[]{"ced", "a#b", "dfc", Character.valueOf('#'), unboundPoppet, Character.valueOf('a'), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), Character.valueOf('b'), Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), Character.valueOf('c'), new ItemStack(Blocks.yellow_flower), Character.valueOf('d'), new ItemStack(Blocks.red_flower), Character.valueOf('e'), new ItemStack(Blocks.red_mushroom), Character.valueOf('f'), new ItemStack(Blocks.brown_mushroom)});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(antiVoodooPoppet, new ItemStack[]{taglocks, antiVoodooPoppet}));
      ItemStack poppetProectionPoppet = Witchery.Items.POPPET.poppetProtectionPoppet.createStack();
      GameRegistry.addRecipe(poppetProectionPoppet, new Object[]{"gfg", "e#e", "glg", Character.valueOf('#'), antiVoodooPoppet, Character.valueOf('l'), Witchery.Items.GENERIC.itemDropOfLuck.createStack(), Character.valueOf('e'), Witchery.Items.GENERIC.itemEnderDew.createStack(), Character.valueOf('g'), new ItemStack(Items.gold_nugget), Character.valueOf('f'), Witchery.Items.GENERIC.itemToeOfFrog.createStack()});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(poppetProectionPoppet, new ItemStack[]{taglocks, poppetProectionPoppet}));
      ItemStack voodooPoppet = Witchery.Items.POPPET.voodooPoppet.createStack();
      GameRegistry.addRecipe(voodooPoppet, new Object[]{" d ", "a#b", " c ", Character.valueOf('#'), unboundPoppet, Character.valueOf('a'), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), Character.valueOf('b'), Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), Character.valueOf('c'), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), Character.valueOf('d'), new ItemStack(Items.fermented_spider_eye)});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(voodooPoppet, new ItemStack[]{taglocks, voodooPoppet}));
      ItemStack toolPoppet = Witchery.Items.POPPET.toolPoppet.createStack();
      GameRegistry.addRecipe(toolPoppet, new Object[]{" a ", "b#b", " a ", Character.valueOf('#'), unboundPoppet, Character.valueOf('a'), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack(), Character.valueOf('b'), Witchery.Items.GENERIC.itemDropOfLuck.createStack()});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(toolPoppet, new ItemStack[]{taglocks, toolPoppet}));
      ItemStack armorPoppet = Witchery.Items.POPPET.armorPoppet.createStack();
      GameRegistry.addRecipe(armorPoppet, new Object[]{" a ", "b#b", " d ", Character.valueOf('#'), unboundPoppet, Character.valueOf('a'), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack(), Character.valueOf('b'), Witchery.Items.GENERIC.itemDropOfLuck.createStack(), Character.valueOf('d'), Witchery.Items.GENERIC.itemDiamondVapour.createStack()});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(armorPoppet, new ItemStack[]{taglocks, armorPoppet}));
      ItemStack avoidDeathPoppet = Witchery.Items.POPPET.deathPoppet.createStack();
      GameRegistry.addRecipe(avoidDeathPoppet, new Object[]{"axb", "x#x", " x ", Character.valueOf('#'), unboundPoppet, Character.valueOf('a'), Witchery.Items.GENERIC.itemDropOfLuck.createStack(), Character.valueOf('b'), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), Character.valueOf('x'), new ItemStack(Items.gold_nugget)});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(avoidDeathPoppet, new ItemStack[]{taglocks, avoidDeathPoppet}));
      ItemStack vampiricPoppet = Witchery.Items.POPPET.vampiricPoppet.createStack();
      GameRegistry.addRecipe(vampiricPoppet, new Object[]{" b ", "c#c", " a ", Character.valueOf('#'), unboundPoppet, Character.valueOf('a'), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), Character.valueOf('b'), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), Character.valueOf('c'), Witchery.Items.GENERIC.itemBatWool.createStack()});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessPoppet(vampiricPoppet, new ItemStack[]{taglocks, taglocks, vampiricPoppet}));
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.POPPET_SHELF), new Object[]{"yzy", "zxz", "yzy", Character.valueOf('x'), ClothColor.GREEN.createStack(), Character.valueOf('y'), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Character.valueOf('z'), new ItemStack(Blocks.nether_brick)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.OVEN_IDLE), new Object[]{" z ", "xxx", "xzx", Character.valueOf('x'), new ItemStack(Items.iron_ingot), Character.valueOf('z'), new ItemStack(Blocks.iron_bars)});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemSoftClayJar.createStack(4), new Object[]{" # ", "###", Character.valueOf('#'), new ItemStack(Items.clay_ball)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.PLANKS, 4, 0), new Object[]{"#", Character.valueOf('#'), new ItemStack(Witchery.Blocks.LOG, 1, 0)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.PLANKS, 4, 1), new Object[]{"#", Character.valueOf('#'), new ItemStack(Witchery.Blocks.LOG, 1, 1)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.PLANKS, 4, 2), new Object[]{"#", Character.valueOf('#'), new ItemStack(Witchery.Blocks.LOG, 1, 2)});
      CraftingManager.getInstance().getRecipeList().add(0, getShapedRecipe(Witchery.Items.GENERIC.itemDoorRowan.createStack(), new Object[]{"##", "##", "##", Character.valueOf('#'), new ItemStack(Witchery.Blocks.PLANKS, 1, 0)}));
      CraftingManager.getInstance().getRecipeList().add(0, getShapedRecipe(Witchery.Items.GENERIC.itemDoorAlder.createStack(), new Object[]{"##", "##", "##", Character.valueOf('#'), new ItemStack(Witchery.Blocks.PLANKS, 1, 1)}));
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.STAIRS_ALDER, 4, 0), new Object[]{"#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Witchery.Blocks.PLANKS, 1, 1)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.STAIRS_HAWTHORN, 4, 0), new Object[]{"#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Witchery.Blocks.PLANKS, 1, 2)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.STAIRS_ROWAN, 4, 0), new Object[]{"#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Witchery.Blocks.PLANKS, 1, 0)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.SNOW_STAIRS, 4, 0), new Object[]{"#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.snow, 1, 0)});
      CraftingManager.getInstance().getRecipeList().add(0, getShapedRecipe(new ItemStack(Witchery.Blocks.SNOW_SLAB_SINGLE, 6, 0), new Object[]{"###", "###", Character.valueOf('#'), new ItemStack(Blocks.snow_layer, 1, 0)}));
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.SNOW_PRESSURE_PLATE, 1, 0), new Object[]{"##", Character.valueOf('#'), new ItemStack(Blocks.snow, 1, 0)});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemQuicklime.createStack(), new Object[]{"#", Character.valueOf('#'), Witchery.Items.GENERIC.itemAshWood.createStack()});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.ALTAR, 3), new Object[]{"abc", "xyx", "xyx", Character.valueOf('a'), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), Character.valueOf('b'), new ItemStack(Items.potionitem), Character.valueOf('c'), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), Character.valueOf('x'), new ItemStack(Blocks.stonebrick, 1, 0), Character.valueOf('y'), new ItemStack(Witchery.Blocks.LOG, 1, 0)});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemAttunedStone.createStack(), new Object[]{"a", "b", "c", Character.valueOf('a'), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack(), Character.valueOf('b'), new ItemStack(Items.diamond), Character.valueOf('c'), new ItemStack(Items.lava_bucket)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.DISTILLERY_IDLE), new Object[]{"bxb", "xxx", "yay", Character.valueOf('a'), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Character.valueOf('b'), Witchery.Items.GENERIC.itemEmptyClayJar.createStack(), Character.valueOf('y'), new ItemStack(Items.gold_ingot), Character.valueOf('x'), new ItemStack(Items.iron_ingot)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.KETTLE), new Object[]{"bxb", "xax", " y ", Character.valueOf('a'), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Character.valueOf('b'), new ItemStack(Items.stick), Character.valueOf('x'), new ItemStack(Items.string), Character.valueOf('y'), new ItemStack(Items.cauldron)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.BRAZIER), new Object[]{"#a#", " w ", "www", Character.valueOf('a'), Witchery.Items.GENERIC.itemNecroStone.createStack(), Character.valueOf('w'), new ItemStack(Items.stick), Character.valueOf('#'), new ItemStack(Items.iron_ingot)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.CHALK_RITUAL, 2, 0), new Object[]{"xax", "xyx", "xyx", Character.valueOf('a'), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), Character.valueOf('x'), Witchery.Items.GENERIC.itemAshWood.createStack(), Character.valueOf('y'), Witchery.Items.GENERIC.itemGypsum.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemWaystone.createStack(), new Object[]{"ab", Character.valueOf('a'), new ItemStack(Items.flint), Character.valueOf('b'), Witchery.Items.GENERIC.itemBoneNeedle.createStack()});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.ARTHANA), new Object[]{" y ", "xbx", " a ", Character.valueOf('a'), new ItemStack(Items.stick), Character.valueOf('b'), new ItemStack(Items.emerald), Character.valueOf('y'), new ItemStack(Items.gold_ingot), Character.valueOf('x'), new ItemStack(Items.gold_nugget)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.BOLINE), new Object[]{"y", "a", "b", Character.valueOf('a'), new ItemStack(Items.bone), Character.valueOf('b'), new ItemStack(Items.emerald), Character.valueOf('y'), new ItemStack(Items.iron_ingot)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.CIRCLE_TALISMAN), new Object[]{"yxy", "xax", "yxy", Character.valueOf('a'), new ItemStack(Items.diamond), Character.valueOf('x'), new ItemStack(Items.gold_ingot), Character.valueOf('y'), new ItemStack(Items.gold_nugget)});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemBroom.createStack(), new Object[]{" x ", " x ", "yyy", Character.valueOf('x'), new ItemStack(Items.stick), Character.valueOf('y'), new ItemStack(Witchery.Blocks.SAPLING, 1, 2)});
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemOddPorkRaw.createStack(), new Object[]{Witchery.Items.GENERIC.itemMutandis.createStack(), new ItemStack(Items.rotten_flesh)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.porkchop), new Object[]{Witchery.Items.GENERIC.itemMutandis.createStack(), Witchery.Items.GENERIC.itemOddPorkRaw.createStack()});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.porkchop), new Object[]{Witchery.Items.GENERIC.itemMutandis.createStack(), new ItemStack(Items.chicken)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.chicken), new Object[]{Witchery.Items.GENERIC.itemMutandis.createStack(), new ItemStack(Items.beef)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.beef), new Object[]{Witchery.Items.GENERIC.itemMutandis.createStack(), new ItemStack(Items.porkchop)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_porkchop), new Object[]{Witchery.Items.GENERIC.itemMutandis.createStack(), Witchery.Items.GENERIC.itemOddPorkCooked.createStack()});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_porkchop), new Object[]{Witchery.Items.GENERIC.itemMutandis.createStack(), new ItemStack(Items.cooked_chicken)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_chicken), new Object[]{Witchery.Items.GENERIC.itemMutandis.createStack(), new ItemStack(Items.cooked_beef)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_beef), new Object[]{Witchery.Items.GENERIC.itemMutandis.createStack(), new ItemStack(Items.cooked_porkchop)});
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemOddPorkCooked.createStack(), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.rotten_flesh)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_porkchop), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), Witchery.Items.GENERIC.itemOddPorkRaw.createStack()});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_porkchop), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.chicken)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_chicken), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.beef)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_beef), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.porkchop)});
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemOddPorkRaw.createStack(), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.rotten_flesh)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.porkchop), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), Witchery.Items.GENERIC.itemOddPorkRaw.createStack()});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.porkchop), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.chicken)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.chicken), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.beef)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.beef), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.porkchop)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_porkchop), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), Witchery.Items.GENERIC.itemOddPorkCooked.createStack()});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_porkchop), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.cooked_chicken)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_chicken), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.cooked_beef)});
      GameRegistry.addShapelessRecipe(new ItemStack(Items.cooked_beef), new Object[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.cooked_porkchop)});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemCandelabra.createStack(), new Object[]{"xxx", "yay", " y ", Character.valueOf('x'), new ItemStack(Blocks.torch), Character.valueOf('y'), new ItemStack(Items.iron_ingot), Character.valueOf('a'), Witchery.Items.GENERIC.itemAttunedStone.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemChaliceEmpty.createStack(), new Object[]{"yay", "yxy", " x ", Character.valueOf('x'), new ItemStack(Items.gold_ingot), Character.valueOf('y'), new ItemStack(Items.gold_nugget), Character.valueOf('a'), Witchery.Items.GENERIC.itemAttunedStone.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemChaliceFull.createStack(), new Object[]{"b", "a", Character.valueOf('a'), Witchery.Items.GENERIC.itemChaliceEmpty.createStack(), Character.valueOf('b'), Witchery.Items.GENERIC.itemRedstoneSoup.createStack()});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.DIVINER_WATER), new Object[]{"yay", "yay", "axa", Character.valueOf('a'), new ItemStack(Items.stick), Character.valueOf('y'), new ItemStack(Items.potionitem), Character.valueOf('x'), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack()});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.DIVINER_LAVA), new Object[]{" a ", " x ", "a a", Character.valueOf('x'), new ItemStack(Witchery.Items.DIVINER_WATER), Character.valueOf('a'), new ItemStack(Items.blaze_rod)});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemDreamMove.createStack(), new Object[]{"dxe", "bab", "cfc", Character.valueOf('a'), new ItemStack(Items.item_frame), Character.valueOf('b'), Witchery.Items.GENERIC.itemFancifulThread.createStack(), Character.valueOf('f'), Witchery.Items.GENERIC.itemTormentedTwine.createStack(), Character.valueOf('c'), new ItemStack(Items.feather), Character.valueOf('d'), new ItemStack(Items.potionitem, 1, 16450), Character.valueOf('e'), new ItemStack(Items.potionitem, 1, 16458), Character.valueOf('x'), Witchery.Items.GENERIC.itemDiamondVapour.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemDreamMove.createStack(), new Object[]{"dxe", "bab", "cfc", Character.valueOf('a'), new ItemStack(Items.item_frame), Character.valueOf('b'), Witchery.Items.GENERIC.itemFancifulThread.createStack(), Character.valueOf('f'), Witchery.Items.GENERIC.itemTormentedTwine.createStack(), Character.valueOf('c'), new ItemStack(Items.feather), Character.valueOf('d'), new ItemStack(Items.potionitem, 1, 16450), Character.valueOf('e'), new ItemStack(Items.potionitem, 1, 24650), Character.valueOf('x'), Witchery.Items.GENERIC.itemDiamondVapour.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemDreamDig.createStack(), new Object[]{"dxe", "bab", "cfc", Character.valueOf('a'), new ItemStack(Items.item_frame), Character.valueOf('b'), Witchery.Items.GENERIC.itemFancifulThread.createStack(), Character.valueOf('f'), Witchery.Items.GENERIC.itemTormentedTwine.createStack(), Character.valueOf('c'), new ItemStack(Items.feather), Character.valueOf('d'), new ItemStack(Items.potionitem, 1, 16457), Character.valueOf('e'), new ItemStack(Items.potionitem, 1, 16456), Character.valueOf('x'), Witchery.Items.GENERIC.itemDiamondVapour.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemDreamDig.createStack(), new Object[]{"dxe", "bab", "cfc", Character.valueOf('a'), new ItemStack(Items.item_frame), Character.valueOf('b'), Witchery.Items.GENERIC.itemFancifulThread.createStack(), Character.valueOf('f'), Witchery.Items.GENERIC.itemTormentedTwine.createStack(), Character.valueOf('c'), new ItemStack(Items.feather), Character.valueOf('d'), new ItemStack(Items.potionitem, 1, 16457), Character.valueOf('e'), new ItemStack(Items.potionitem, 1, 24648), Character.valueOf('x'), Witchery.Items.GENERIC.itemDiamondVapour.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemDreamEat.createStack(), new Object[]{"dxe", "bab", "cfc", Character.valueOf('a'), new ItemStack(Items.item_frame), Character.valueOf('b'), Witchery.Items.GENERIC.itemFancifulThread.createStack(), Character.valueOf('f'), Witchery.Items.GENERIC.itemTormentedTwine.createStack(), Character.valueOf('c'), new ItemStack(Items.feather), Character.valueOf('d'), new ItemStack(Items.potionitem, 1, 16421), Character.valueOf('e'), Witchery.Items.GENERIC.itemMellifluousHunger.createStack(), Character.valueOf('x'), Witchery.Items.GENERIC.itemDiamondVapour.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemDreamEat.createStack(), new Object[]{"dxe", "bab", "cfc", Character.valueOf('a'), new ItemStack(Items.item_frame), Character.valueOf('b'), Witchery.Items.GENERIC.itemFancifulThread.createStack(), Character.valueOf('f'), Witchery.Items.GENERIC.itemTormentedTwine.createStack(), Character.valueOf('c'), new ItemStack(Items.feather), Character.valueOf('d'), new ItemStack(Items.potionitem, 1, 16421), Character.valueOf('e'), Witchery.Items.GENERIC.itemMellifluousHunger.createStack(), Character.valueOf('x'), Witchery.Items.GENERIC.itemDiamondVapour.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemDreamNightmare.createStack(), new Object[]{"dxe", "bab", "cbc", Character.valueOf('a'), new ItemStack(Items.item_frame), Character.valueOf('b'), Witchery.Items.GENERIC.itemTormentedTwine.createStack(), Character.valueOf('c'), new ItemStack(Items.feather), Character.valueOf('d'), new ItemStack(Items.potionitem, 1, 16452), Character.valueOf('e'), new ItemStack(Items.potionitem, 1, 16454), Character.valueOf('x'), Witchery.Items.GENERIC.itemDiamondVapour.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemDreamIntensity.createStack(), new Object[]{"dxe", "bab", "cfc", Character.valueOf('a'), new ItemStack(Items.item_frame), Character.valueOf('b'), Witchery.Items.GENERIC.itemFancifulThread.createStack(), Character.valueOf('f'), Witchery.Items.GENERIC.itemTormentedTwine.createStack(), Character.valueOf('c'), new ItemStack(Items.feather), Character.valueOf('d'), Witchery.Items.GENERIC.itemBrewOfFlowingSpirit.createStack(), Character.valueOf('e'), Witchery.Items.GENERIC.itemBrewOfSleeping.createStack(), Character.valueOf('x'), Witchery.Items.GENERIC.itemDiamondVapour.createStack()});
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Witchery.Items.CAULDRON_BOOK), new Object[]{" c ", "a#b", Character.valueOf('#'), new ItemStack(Items.book), Character.valueOf('a'), "dyeBlack", Character.valueOf('b'), new ItemStack(Items.feather), Character.valueOf('c'), new ItemStack(Blocks.dirt)}));
      GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemBookHerbology.createStack(), new Object[]{" c ", "a#b", " d ", Character.valueOf('#'), new ItemStack(Items.book), Character.valueOf('a'), "dyeBlack", Character.valueOf('b'), new ItemStack(Items.feather), Character.valueOf('c'), new ItemStack(Blocks.red_flower), Character.valueOf('d'), new ItemStack(Blocks.yellow_flower)}));
      GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemBookWands.createStack(), new Object[]{" c ", "a#b", "   ", Character.valueOf('#'), new ItemStack(Items.book), Character.valueOf('a'), "dyeBlack", Character.valueOf('b'), new ItemStack(Items.feather), Character.valueOf('c'), Witchery.Items.GENERIC.itemBranchEnt.createStack()}));
      GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemBookBiomes.createStack(), new Object[]{" c ", "a#b", " d ", Character.valueOf('#'), new ItemStack(Items.book), Character.valueOf('a'), "dyeBlack", Character.valueOf('b'), new ItemStack(Items.feather), Character.valueOf('c'), new ItemStack(Blocks.sapling), Character.valueOf('d'), new ItemStack(Blocks.stone)}));
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Witchery.Items.BIOME_BOOK), new Object[]{" d ", "d#d", " d ", Character.valueOf('#'), Witchery.Items.GENERIC.itemBookBiomes.createStack(), Character.valueOf('d'), new ItemStack(Blocks.stone)}));
      GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemBookBurning.createStack(), new Object[]{" c ", "a#b", " d ", Character.valueOf('#'), new ItemStack(Items.book), Character.valueOf('a'), "dyeBlack", Character.valueOf('b'), new ItemStack(Items.feather), Character.valueOf('c'), Witchery.Items.GENERIC.itemAshWood.createStack(), Character.valueOf('d'), new ItemStack(Items.flint_and_steel)}));
      GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemBookOven.createStack(), new Object[]{" c ", "a#b", " d ", Character.valueOf('#'), new ItemStack(Items.book), Character.valueOf('a'), "dyeBlack", Character.valueOf('b'), new ItemStack(Items.feather), Character.valueOf('c'), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), Character.valueOf('d'), new ItemStack(Items.coal, 1, 1)}));
      GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemBookDistilling.createStack(), new Object[]{" c ", "a#b", " d ", Character.valueOf('#'), new ItemStack(Items.book), Character.valueOf('a'), "dyeBlack", Character.valueOf('b'), new ItemStack(Items.feather), Character.valueOf('c'), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), Character.valueOf('d'), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack()}));
      GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemBookCircleMagic.createStack(), new Object[]{" c ", "a#b", " d ", Character.valueOf('#'), new ItemStack(Items.book), Character.valueOf('a'), "dyeBlack", Character.valueOf('b'), new ItemStack(Items.feather), Character.valueOf('c'), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), Character.valueOf('d'), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack()}));
      GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemBookInfusions.createStack(), new Object[]{" c ", "a#b", " d ", Character.valueOf('#'), new ItemStack(Items.book), Character.valueOf('a'), "dyeBlack", Character.valueOf('b'), new ItemStack(Items.feather), Character.valueOf('c'), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), Character.valueOf('d'), Witchery.Items.GENERIC.itemOdourOfPurity.createStack()}));
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemWeb.createStack(), new Object[]{" s ", "sws", " s ", Character.valueOf('s'), new ItemStack(Items.string), Character.valueOf('w'), new ItemStack(Blocks.web)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.ALLURING_SKULL), new Object[]{" a ", "bcb", " d ", Character.valueOf('a'), Witchery.Items.GENERIC.itemNecroStone.createStack(), Character.valueOf('d'), Witchery.Items.POPPET.voodooPoppet.createStack(), Character.valueOf('c'), new ItemStack(Items.skull), Character.valueOf('b'), new ItemStack(Items.glowstone_dust)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.ALLURING_SKULL), new Object[]{" a ", "bcb", " d ", Character.valueOf('a'), Witchery.Items.GENERIC.itemNecroStone.createStack(), Character.valueOf('d'), Witchery.Items.POPPET.voodooPoppet.createStack(), Character.valueOf('c'), new ItemStack(Items.skull, 1, 1), Character.valueOf('b'), new ItemStack(Items.glowstone_dust)});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemSeedsTreefyd.createStack(2), new Object[]{"xax", "cyd", "xbx", Character.valueOf('x'), Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), Character.valueOf('y'), Witchery.Items.GENERIC.itemArtichoke.createStack(), Character.valueOf('c'), new ItemStack(Witchery.Blocks.EMBER_MOSS), Character.valueOf('d'), Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), Character.valueOf('a'), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack(), Character.valueOf('b'), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack()});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.POLYNESIA_CHARM, 1), new Object[]{"nin", "p#p", "nwn", Character.valueOf('#'), new ItemStack(Items.fish), Character.valueOf('i'), new ItemStack(Items.iron_ingot), Character.valueOf('p'), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Character.valueOf('w'), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack(), Character.valueOf('n'), new ItemStack(Items.nether_wart)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.DEVILS_TONGUE_CHARM, 1), new Object[]{"b#b", "dse", "btb", Character.valueOf('#'), new ItemStack(Witchery.Items.POLYNESIA_CHARM), Character.valueOf('d'), Witchery.Items.GENERIC.itemDemonHeart.createStack(), Character.valueOf('t'), Witchery.Items.GENERIC.itemDogTongue.createStack(), Character.valueOf('e'), Witchery.Items.GENERIC.itemRefinedEvil.createStack(), Character.valueOf('s'), new ItemStack(Items.skull), Character.valueOf('b'), new ItemStack(Items.blaze_powder)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.OVEN_FUMEFUNNEL), new Object[]{"ele", "ege", "bib", Character.valueOf('e'), new ItemStack(Items.bucket), Character.valueOf('l'), new ItemStack(Items.lava_bucket), Character.valueOf('b'), new ItemStack(Blocks.iron_block), Character.valueOf('g'), new ItemStack(Blocks.glowstone), Character.valueOf('i'), new ItemStack(Blocks.iron_bars)});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemFumeFilter.createStack(), new Object[]{"ggg", "sas", "ggg", Character.valueOf('g'), new ItemStack(Blocks.glass), Character.valueOf('s'), new ItemStack(Items.iron_ingot), Character.valueOf('a'), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()});
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.OVEN_FUMEFUNNEL_FILTERED), new Object[]{"b", "f", Character.valueOf('b'), new ItemStack(Witchery.Blocks.OVEN_FUMEFUNNEL), Character.valueOf('f'), Witchery.Items.GENERIC.itemFumeFilter.createStack()});
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemPurifiedMilk.createStack(3), new Object[]{new ItemStack(Items.milk_bucket), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemEmptyClayJar.createStack(), Witchery.Items.GENERIC.itemEmptyClayJar.createStack(), Witchery.Items.GENERIC.itemEmptyClayJar.createStack()});
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemPurifiedMilk.createStack(3), new Object[]{new ItemStack(Items.cake), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemEmptyClayJar.createStack(), Witchery.Items.GENERIC.itemEmptyClayJar.createStack(), Witchery.Items.GENERIC.itemEmptyClayJar.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemImpregnatedLeather.createStack(4), new Object[]{"mlm", "ldl", "mlm", Character.valueOf('l'), new ItemStack(Items.leather), Character.valueOf('d'), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), Character.valueOf('m'), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack()});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.WITCH_HAT), new Object[]{" l ", "sls", "lgl", Character.valueOf('s'), Witchery.Items.GENERIC.itemGoldenThread.createStack(), Character.valueOf('l'), Witchery.Items.GENERIC.itemImpregnatedLeather.createStack(), Character.valueOf('g'), new ItemStack(Items.glowstone_dust)});
      if(Config.instance().allowVoidBrambleRecipe) {
         GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.VOID_BRAMBLE, 4), new Object[]{"lml", "r#r", "lml", Character.valueOf('#'), new ItemStack(Witchery.Blocks.BRAMBLE), Character.valueOf('r'), new ItemStack(Items.nether_star), Character.valueOf('l'), Witchery.Items.GENERIC.itemDropOfLuck.createStack(), Character.valueOf('m'), Witchery.Items.GENERIC.itemMutandisExtremis.createStack()});
      }

      GameRegistry.addRecipe(new ItemStack(Items.gunpowder, 5), new Object[]{"#", Character.valueOf('#'), Witchery.Items.GENERIC.itemCreeperHeart.createStack()});
      GameRegistry.addShapelessRecipe(new ItemStack(Blocks.netherrack), new Object[]{Witchery.Items.GENERIC.itemInfernalBlood.createStack(), new ItemStack(Blocks.gravel)});
      ItemStack impregLeather = Witchery.Items.GENERIC.itemImpregnatedLeather.createStack();
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.WITCH_ROBES), new Object[]{"lsl", "l#l", "lll", Character.valueOf('s'), Witchery.Items.GENERIC.itemGoldenThread.createStack(), Character.valueOf('l'), impregLeather, Character.valueOf('#'), Witchery.Items.GENERIC.itemCreeperHeart.createStack()});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.NECROMANCERS_ROBES), new Object[]{"lsl", "l#l", "lll", Character.valueOf('s'), Witchery.Items.GENERIC.itemGoldenThread.createStack(), Character.valueOf('l'), impregLeather, Character.valueOf('#'), Witchery.Items.GENERIC.itemNecroStone.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemFrozenHeart.createStack(), new Object[]{"n", "h", "t", Character.valueOf('h'), Witchery.Items.GENERIC.itemCreeperHeart.createStack(), Character.valueOf('n'), Witchery.Items.GENERIC.itemIcyNeedle.createStack(), Character.valueOf('t'), new ItemStack(Items.ghast_tear)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.ICY_SLIPPERS), new Object[]{"lsl", "l#l", "dod", Character.valueOf('l'), impregLeather, Character.valueOf('s'), Witchery.Items.GENERIC.itemGoldenThread.createStack(), Character.valueOf('#'), Witchery.Items.GENERIC.itemFrozenHeart.createStack(), Character.valueOf('d'), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), Character.valueOf('o'), Witchery.Items.GENERIC.itemOdourOfPurity.createStack()});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.BITING_BELT), new Object[]{"#lh", "lsl", "l l", Character.valueOf('l'), impregLeather, Character.valueOf('s'), Witchery.Items.GENERIC.itemGoldenThread.createStack(), Character.valueOf('h'), Witchery.Items.GENERIC.itemMellifluousHunger.createStack(), Character.valueOf('#'), new ItemStack(Witchery.Items.PARASYTIC_LOUSE)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.SEEPING_SHOES), new Object[]{"lsl", "hrh", "mmm", Character.valueOf('l'), impregLeather, Character.valueOf('s'), Witchery.Items.GENERIC.itemGoldenThread.createStack(), Character.valueOf('h'), new ItemStack(Witchery.Items.WITCH_HAND), Character.valueOf('r'), Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), Character.valueOf('m'), new ItemStack(Items.milk_bucket)});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.RUBY_SLIPPERS), new Object[]{"aba", "tst", "aba", Character.valueOf('s'), new ItemStack(Witchery.Items.SEEPING_SHOES), Character.valueOf('t'), Witchery.Items.GENERIC.itemGoldenThread.createStack(), Character.valueOf('a'), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Character.valueOf('b'), Witchery.Items.GENERIC.itemInfernalBlood.createStack()});
      GameRegistry.addRecipe(new ItemStack(Witchery.Items.BARK_BELT), new Object[]{"ses", "gbg", "shs", Character.valueOf('b'), new ItemStack(Witchery.Items.BITING_BELT), Character.valueOf('s'), Witchery.Items.GENERIC.itemBrewOfFlowingSpirit.createStack(), Character.valueOf('g'), Witchery.Items.GENERIC.itemBranchEnt.createStack(), Character.valueOf('h'), Witchery.Items.GENERIC.itemCreeperHeart.createStack(), Character.valueOf('e'), new ItemStack(Items.emerald)});
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemWormyApple.createStack(), new Object[]{new ItemStack(Items.apple), new ItemStack(Items.rotten_flesh), new ItemStack(Items.sugar)});
      ItemStack louse = new ItemStack(Witchery.Items.PARASYTIC_LOUSE, 1, 32767);
      ItemStack belt = new ItemStack(Witchery.Items.BITING_BELT, 1, 32767);
      int[] lousePotions = new int[]{8200, 8202, 8264, 8266, 8193, 8194, 8196, 8225, 8226, 8227, 8228, 8229, 8230, 8232, 8233, 8234, 8236, 8238, 8257, 8258, 8259, 8260, 8261, 8262, 8264, 8265, 8266, 8268, 8270, 8201, 8206};
      int[] logs = lousePotions;
      int kobolditeIngot = lousePotions.length;

      int meats;
      int hunterItems;
      for(hunterItems = 0; hunterItems < kobolditeIngot; ++hunterItems) {
         meats = logs[hunterItems];
         GameRegistry.addShapelessRecipe(new ItemStack(Witchery.Items.PARASYTIC_LOUSE, 1, meats), new Object[]{louse, new ItemStack(Items.potionitem, 1, meats)});
         CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessAddPotion(new ItemStack(Witchery.Items.BITING_BELT, 1, meats), new ItemStack[]{belt, new ItemStack(Items.potionitem, 1, meats)}));
      }

      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessRepair(new ItemStack(Witchery.Items.WITCH_ROBES), new ItemStack[]{new ItemStack(Witchery.Items.WITCH_ROBES), impregLeather, impregLeather, impregLeather, impregLeather}));
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessRepair(new ItemStack(Witchery.Items.NECROMANCERS_ROBES), new ItemStack[]{new ItemStack(Witchery.Items.NECROMANCERS_ROBES), impregLeather, impregLeather, impregLeather, impregLeather}));
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessRepair(new ItemStack(Witchery.Items.WITCH_HAT), new ItemStack[]{new ItemStack(Witchery.Items.WITCH_HAT), impregLeather, impregLeather, impregLeather}));
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessRepair(new ItemStack(Witchery.Items.ICY_SLIPPERS), new ItemStack[]{new ItemStack(Witchery.Items.ICY_SLIPPERS), impregLeather, impregLeather, impregLeather}));
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessRepair(new ItemStack(Witchery.Items.RUBY_SLIPPERS), new ItemStack[]{new ItemStack(Witchery.Items.RUBY_SLIPPERS), impregLeather, impregLeather, impregLeather}));
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessRepair(new ItemStack(Witchery.Items.SEEPING_SHOES), new ItemStack[]{new ItemStack(Witchery.Items.SEEPING_SHOES), impregLeather, impregLeather, impregLeather}));
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessRepair(new ItemStack(Witchery.Items.BITING_BELT), new ItemStack[]{new ItemStack(Witchery.Items.BITING_BELT), impregLeather, impregLeather, impregLeather, impregLeather}));
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessRepair(new ItemStack(Witchery.Items.BARK_BELT), new ItemStack[]{new ItemStack(Witchery.Items.BARK_BELT), impregLeather, impregLeather, impregLeather, impregLeather}));
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessRepair(new ItemStack(Witchery.Items.BABAS_HAT), new ItemStack[]{new ItemStack(Witchery.Items.BABAS_HAT), impregLeather, impregLeather, impregLeather}));
      Dye[] var29 = Dye.DYES;
      kobolditeIngot = var29.length;

      for(hunterItems = 0; hunterItems < kobolditeIngot; ++hunterItems) {
         Dye var32 = var29[hunterItems];
         CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessAddColor(new ItemStack(Witchery.Items.BREW_BAG), new ItemStack[]{new ItemStack(Witchery.Items.BREW_BAG), var32.createStack()}));
      }

      GameRegistry.addRecipe(new ItemStack(Witchery.Items.BREW_BAG), new Object[]{"lll", "lsl", "lll", Character.valueOf('l'), impregLeather, Character.valueOf('s'), Witchery.Items.GENERIC.itemGoldenThread.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemCharmOfDisruptedDreams.createStack(), new Object[]{"lll", "lsl", "lll", Character.valueOf('l'), new ItemStack(Items.stick), Character.valueOf('s'), Witchery.Items.GENERIC.itemFancifulThread.createStack()});
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessAddKeys(Witchery.Items.GENERIC.itemDoorKeyring.createStack(), new ItemStack[]{Witchery.Items.GENERIC.itemDoorKey.createStack(), Witchery.Items.GENERIC.itemDoorKey.createStack()}));
      CraftingManager.getInstance().getRecipeList().add(new RecipeShapelessAddKeys(Witchery.Items.GENERIC.itemDoorKeyring.createStack(), new ItemStack[]{Witchery.Items.GENERIC.itemDoorKeyring.createStack(), Witchery.Items.GENERIC.itemDoorKey.createStack()}));
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemQuartzSphere.createStack(), new Object[]{"qbq", "bgb", "qbq", Character.valueOf('q'), new ItemStack(Items.quartz), Character.valueOf('b'), new ItemStack(Blocks.quartz_block), Character.valueOf('g'), new ItemStack(Blocks.glass)});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemSleepingApple.createStack(), new Object[]{" g ", "mam", "gsg", Character.valueOf('a'), Witchery.Items.GENERIC.itemWormyApple.createStack(), Character.valueOf('g'), Witchery.Items.GENERIC.itemMutandis.createStack(), Character.valueOf('m'), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack(), Character.valueOf('s'), Witchery.Items.GENERIC.itemBrewOfSleeping.createStack()});
      GameRegistry.addRecipe(Witchery.Items.GENERIC.itemBatBall.createStack(), new Object[]{"sbs", "b b", "sbs", Character.valueOf('s'), new ItemStack(Items.slime_ball), Character.valueOf('b'), new ItemStack(Witchery.Blocks.CRITTER_SNARE, 1, 1)});
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Witchery.Blocks.SPINNING_WHEEL), new Object[]{"aab", "aac", "wsw", Character.valueOf('a'), new ItemStack(Items.item_frame), Character.valueOf('b'), new ItemStack(Blocks.wool), Character.valueOf('c'), "stickWood", Character.valueOf('w'), "plankWood", Character.valueOf('s'), Witchery.Items.GENERIC.itemAttunedStone.createStack()}));
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemGraveyardDust.createStack(), new Object[]{Witchery.Items.GENERIC.itemSpectralDust.createStack(), Dye.BONE_MEAL.createStack(), Witchery.Items.GENERIC.itemMutandis.createStack()});
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Witchery.Blocks.FETISH_SCARECROW), new Object[]{"w#w", "sls", "wsw", Character.valueOf('#'), new ItemStack(Blocks.lit_pumpkin), Character.valueOf('w'), new ItemStack(Blocks.wool), Character.valueOf('s'), "stickWood", Character.valueOf('l'), Witchery.Items.GENERIC.itemTormentedTwine.createStack()}));
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.FETISH_WITCHS_LADDER), new Object[]{"fsf", "ftf", "fsf", Character.valueOf('f'), new ItemStack(Items.feather), Character.valueOf('s'), new ItemStack(Items.string), Character.valueOf('t'), Witchery.Items.GENERIC.itemFancifulThread.createStack()});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.FETISH_TREANT_IDOL), new Object[]{"o#o", "srs", "o o", Character.valueOf('#'), new ItemStack(Blocks.lit_pumpkin), Character.valueOf('o'), new ItemStack(Blocks.log, 1, 0), Character.valueOf('r'), new ItemStack(Witchery.Blocks.LOG, 1, 0), Character.valueOf('s'), Witchery.Items.GENERIC.itemTormentedTwine.createStack()});
      SpinningRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemFancifulThread.createStack(), new ItemStack(Witchery.Blocks.WISPY_COTTON, 4), new ItemStack[]{new ItemStack(Items.string), Witchery.Items.GENERIC.itemOdourOfPurity.createStack()});
      SpinningRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemTormentedTwine.createStack(), Witchery.Items.GENERIC.itemDisturbedCotton.createStack(4), new ItemStack[]{new ItemStack(Items.string), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack()});
      SpinningRecipes.instance().addRecipe(new ItemStack(Blocks.web), new ItemStack(Items.string, 8), new ItemStack[0]);
      SpinningRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemGoldenThread.createStack(3), new ItemStack(Blocks.hay_block), new ItemStack[]{Witchery.Items.GENERIC.itemWhiffOfMagic.createStack()});
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemNullCatalyst.createStack(2), new Object[]{new ItemStack(Items.nether_star), new ItemStack(Items.diamond), new ItemStack(Items.flint), new ItemStack(Items.magma_cream), new ItemStack(Items.magma_cream), new ItemStack(Items.magma_cream), new ItemStack(Items.magma_cream), new ItemStack(Items.magma_cream), new ItemStack(Items.magma_cream)});
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemNullCatalyst.createStack(2), new Object[]{Witchery.Items.GENERIC.itemNullCatalyst.createStack(), new ItemStack(Items.magma_cream), new ItemStack(Items.blaze_powder)});
      GameRegistry.addShapedRecipe(Witchery.Items.GENERIC.itemNullifiedLeather.createStack(3), new Object[]{"lll", "lcl", "lll", Character.valueOf('l'), new ItemStack(Items.leather), Character.valueOf('c'), Witchery.Items.GENERIC.itemNullCatalyst.createStack()});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.HUNTER_HAT), new Object[]{"lll", "l l", Character.valueOf('l'), Witchery.Items.GENERIC.itemNullifiedLeather.createStack()});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.HUNTER_COAT), new Object[]{"l l", "lll", "lll", Character.valueOf('l'), Witchery.Items.GENERIC.itemNullifiedLeather.createStack()});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.HUNTER_LEGS), new Object[]{"lll", "l l", "l l", Character.valueOf('l'), Witchery.Items.GENERIC.itemNullifiedLeather.createStack()});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.HUNTER_BOOTS), new Object[]{"l l", "l l", Character.valueOf('l'), Witchery.Items.GENERIC.itemNullifiedLeather.createStack()});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.SHELF_COMPASS), new Object[]{"gdg", "d#d", "gcg", Character.valueOf('g'), new ItemStack(Items.gold_ingot), Character.valueOf('d'), new ItemStack(Items.diamond), Character.valueOf('#'), new ItemStack(Items.clock), Character.valueOf('c'), Witchery.Items.GENERIC.itemNullCatalyst.createStack()});
      GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemBoltStake.createStack(9), new Object[]{" s ", "www", "fff", Character.valueOf('f'), new ItemStack(Items.feather), Character.valueOf('s'), new ItemStack(Items.string), Character.valueOf('w'), "stickWood"}));
      GameRegistry.addShapedRecipe(Witchery.Items.GENERIC.itemBoltSplitting.createStack(), new Object[]{" s ", "bbb", " f ", Character.valueOf('f'), new ItemStack(Items.feather), Character.valueOf('s'), new ItemStack(Items.string), Character.valueOf('b'), Witchery.Items.GENERIC.itemBoltStake.createStack()});
      GameRegistry.addShapedRecipe(Witchery.Items.GENERIC.itemBoltHoly.createStack(12), new Object[]{"aba", "ata", "aba", Character.valueOf('t'), new ItemStack(Items.ghast_tear), Character.valueOf('a'), Witchery.Items.GENERIC.itemBoltStake.createStack(), Character.valueOf('b'), new ItemStack(Items.bone)});
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemBoltAntiMagic.createStack(3), new Object[]{Witchery.Items.GENERIC.itemNullCatalyst.createStack(), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), Witchery.Items.GENERIC.itemBoltHoly.createStack(), Witchery.Items.GENERIC.itemBoltHoly.createStack(), Witchery.Items.GENERIC.itemBoltHoly.createStack()});
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Witchery.Items.CROSSBOW_PISTOL), new Object[]{"mbm", "swn", " m ", Character.valueOf('m'), new ItemStack(Items.iron_ingot), Character.valueOf('b'), new ItemStack(Items.bow), Character.valueOf('n'), Witchery.Items.GENERIC.itemBoneNeedle.createStack(), Character.valueOf('s'), new ItemStack(Items.string), Character.valueOf('w'), "stickWood"}));
      GameRegistry.addShapelessRecipe(Witchery.Items.POTIONS.potionAntidote.createStack(2), new Object[]{Witchery.Items.GENERIC.itemNullCatalyst.createStack(), new ItemStack(Items.potionitem, 1, 8196), new ItemStack(Items.potionitem, 1, 8196)});
      GameRegistry.addShapedRecipe(Witchery.Items.GENERIC.itemContractOwnership.createStack(), new Object[]{"ppp", "pfp", "pps", Character.valueOf('f'), Witchery.Items.GENERIC.itemOddPorkRaw.createStack(), Character.valueOf('p'), new ItemStack(Items.paper), Character.valueOf('s'), new ItemStack(Items.string)});
      GameRegistry.addRecipe(new RecipeAttachTaglock(Witchery.Items.GENERIC.itemContractOwnership.createStack(), new ItemStack[]{Witchery.Items.GENERIC.itemContractOwnership.createStack(), new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1)}));
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemContractBlaze.createStack(), new Object[]{Witchery.Items.GENERIC.itemContractOwnership.createStack(), new ItemStack(Items.blaze_rod), Witchery.Items.GENERIC.itemHintOfRebirth.createStack()});
      GameRegistry.addRecipe(new RecipeAttachTaglock(Witchery.Items.GENERIC.itemContractBlaze.createStack(), new ItemStack[]{Witchery.Items.GENERIC.itemContractBlaze.createStack(), new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1)}));
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemContractResistFire.createStack(), new Object[]{Witchery.Items.GENERIC.itemContractOwnership.createStack(), new ItemStack(Items.blaze_powder)});
      GameRegistry.addRecipe(new RecipeAttachTaglock(Witchery.Items.GENERIC.itemContractResistFire.createStack(), new ItemStack[]{Witchery.Items.GENERIC.itemContractResistFire.createStack(), new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1)}));
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemContractEvaporate.createStack(), new Object[]{Witchery.Items.GENERIC.itemContractOwnership.createStack(), new ItemStack(Items.magma_cream), new ItemStack(Items.blaze_rod)});
      GameRegistry.addRecipe(new RecipeAttachTaglock(Witchery.Items.GENERIC.itemContractEvaporate.createStack(), new ItemStack[]{Witchery.Items.GENERIC.itemContractEvaporate.createStack(), new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1)}));
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemContractFieryTouch.createStack(), new Object[]{Witchery.Items.GENERIC.itemContractOwnership.createStack(), new ItemStack(Witchery.Blocks.EMBER_MOSS), new ItemStack(Items.blaze_rod)});
      GameRegistry.addRecipe(new RecipeAttachTaglock(Witchery.Items.GENERIC.itemContractFieryTouch.createStack(), new ItemStack[]{Witchery.Items.GENERIC.itemContractFieryTouch.createStack(), new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1)}));
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemContractSmelting.createStack(), new Object[]{Witchery.Items.GENERIC.itemContractOwnership.createStack(), new ItemStack(Items.lava_bucket)});
      GameRegistry.addRecipe(new RecipeAttachTaglock(Witchery.Items.GENERIC.itemContractSmelting.createStack(), new ItemStack[]{Witchery.Items.GENERIC.itemContractSmelting.createStack(), new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1)}));
      GameRegistry.addShapelessRecipe(new ItemStack(Witchery.Items.LEONARDS_URN, 1, 1), new Object[]{new ItemStack(Witchery.Items.LEONARDS_URN, 1, 0), new ItemStack(Witchery.Items.LEONARDS_URN, 1, 0)});
      GameRegistry.addShapelessRecipe(new ItemStack(Witchery.Items.LEONARDS_URN, 1, 2), new Object[]{new ItemStack(Witchery.Items.LEONARDS_URN, 1, 1), new ItemStack(Witchery.Items.LEONARDS_URN, 1, 0)});
      GameRegistry.addShapelessRecipe(new ItemStack(Witchery.Items.LEONARDS_URN, 1, 3), new Object[]{new ItemStack(Witchery.Items.LEONARDS_URN, 1, 2), new ItemStack(Witchery.Items.LEONARDS_URN, 1, 0)});
      GameRegistry.addRecipe(new RecipeAttachTaglock(new ItemStack(Witchery.Items.PLAYER_COMPASS), new ItemStack[]{new ItemStack(Witchery.Items.PLAYER_COMPASS, 1, 32767), new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1)}));
      ItemStack[] var28 = new ItemStack[]{new ItemStack(Blocks.log, 1, 0), new ItemStack(Blocks.log, 1, 1), new ItemStack(Blocks.log, 1, 2), new ItemStack(Blocks.log, 1, 3), new ItemStack(Witchery.Blocks.LOG, 1, 0), new ItemStack(Witchery.Blocks.LOG, 1, 1), new ItemStack(Witchery.Blocks.LOG, 1, 2), new ItemStack(Blocks.log2, 1, 0), new ItemStack(Blocks.log2, 1, 1)};

      for(kobolditeIngot = 0; kobolditeIngot < var28.length; ++kobolditeIngot) {
         GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.STOCKADE, 9, kobolditeIngot), new Object[]{" w ", "wfw", "www", Character.valueOf('f'), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), Character.valueOf('w'), var28[kobolditeIngot]});
      }

      ItemStack var30 = Witchery.Items.GENERIC.itemKobolditeIngot.createStack();
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.KOBOLDITE_PICKAXE), new Object[]{"bab", "iii", " s ", Character.valueOf('i'), var30, Character.valueOf('a'), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack(), Character.valueOf('b'), new ItemStack(Items.lava_bucket), Character.valueOf('s'), new ItemStack(Items.stick)});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.STATUE_OF_WORSHIP), new Object[]{"sks", " s ", "s s", Character.valueOf('k'), var30, Character.valueOf('s'), new ItemStack(Blocks.stone)});
      GameRegistry.addShapedRecipe(Witchery.Items.GENERIC.itemKobolditePentacle.createStack(), new Object[]{"sks", "kdk", "sks", Character.valueOf('k'), var30, Character.valueOf('s'), Witchery.Items.GENERIC.itemKobolditeNugget.createStack(), Character.valueOf('d'), new ItemStack(Items.diamond)});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.KOBOLDITE_HELM), new Object[]{"iii", "iai", Character.valueOf('i'), var30, Character.valueOf('a'), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.EARMUFFS), new Object[]{"iii", "i i", "w w", Character.valueOf('i'), new ItemStack(Items.leather), Character.valueOf('w'), new ItemStack(Blocks.wool)});
      GameRegistry.addRecipe(new RecipeShapelessBiomeCopy(new ItemStack(Witchery.Items.BIOME_NOTE), new ItemStack[]{new ItemStack(Witchery.Items.BIOME_BOOK.setContainerItem(Witchery.Items.BIOME_BOOK)), new ItemStack(Items.paper)}));
      GameRegistry.addShapelessRecipe(Witchery.Items.GENERIC.itemAnnointingPaste.createStack(), new Object[]{new ItemStack(Witchery.Items.SEEDS_ARTICHOKE), new ItemStack(Witchery.Items.SEEDS_MANDRAKE), new ItemStack(Witchery.Items.SEEDS_BELLADONNA), new ItemStack(Witchery.Items.SEEDS_SNOWBELL)});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.SILVER_SWORD), new Object[]{"ddd", "dsd", "ddd", Character.valueOf('s'), new ItemStack(Items.golden_sword), Character.valueOf('d'), Witchery.Items.GENERIC.itemSilverDust.createStack()});
      Item[][] var31 = new Item[][]{{Witchery.Items.HUNTER_BOOTS, Witchery.Items.HUNTER_BOOTS_SILVERED}, {Witchery.Items.HUNTER_LEGS, Witchery.Items.HUNTER_LEGS_SILVERED}, {Witchery.Items.HUNTER_COAT, Witchery.Items.HUNTER_COAT_SILVERED}, {Witchery.Items.HUNTER_HAT, Witchery.Items.HUNTER_HAT_SILVERED}};

      for(meats = 0; meats < var31.length; ++meats) {
         CraftingManager.getInstance().addRecipe(new ItemStack(var31[meats][1]), new Object[]{"dwd", "w#w", "dsd", Character.valueOf('#'), new ItemStack(var31[meats][0]), Character.valueOf('s'), new ItemStack(Items.string), Character.valueOf('w'), Witchery.Items.GENERIC.itemWolfsbane.createStack(), Character.valueOf('d'), Witchery.Items.GENERIC.itemSilverDust.createStack()}).func_92100_c();
      }

      GameRegistry.addShapedRecipe(Witchery.Items.GENERIC.itemBoltSilver.createStack(3), new Object[]{" s ", "bbb", Character.valueOf('b'), Witchery.Items.GENERIC.itemBoltStake.createStack(), Character.valueOf('s'), Witchery.Items.GENERIC.itemSilverDust.createStack()});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.WOLF_ALTAR), new Object[]{" w ", "w#w", "#d#", Character.valueOf('w'), new ItemStack(Witchery.Blocks.WOLFHEAD, 1, 32767), Character.valueOf('#'), new ItemStack(Blocks.stone), Character.valueOf('d'), Witchery.Items.GENERIC.itemWolfsbane.createStack()});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.SILVER_VAT), new Object[]{"ibi", "ifi", Character.valueOf('i'), new ItemStack(Items.iron_ingot), Character.valueOf('b'), new ItemStack(Items.water_bucket), Character.valueOf('f'), new ItemStack(Blocks.furnace)});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.BEARTRAP), new Object[]{"iii", "bpb", "iii", Character.valueOf('p'), new ItemStack(Blocks.heavy_weighted_pressure_plate), Character.valueOf('i'), new ItemStack(Items.iron_ingot), Character.valueOf('b'), new ItemStack(Items.shears)});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.WOLFTRAP), new Object[]{"sns", "w#w", "sns", Character.valueOf('#'), new ItemStack(Witchery.Blocks.BEARTRAP), Character.valueOf('s'), Witchery.Items.GENERIC.itemSilverDust.createStack(), Character.valueOf('n'), Witchery.Items.GENERIC.itemNullCatalyst.createStack(), Character.valueOf('w'), Witchery.Items.GENERIC.itemWolfsbane.createStack()});
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Witchery.Blocks.GARLIC_GARLAND), new Object[]{"s s", "GsG", "GGG", Character.valueOf('G'), "cropGarlic", Character.valueOf('s'), new ItemStack(Items.string)}));
      ItemStack[] var34 = new ItemStack[]{new ItemStack(Items.beef), new ItemStack(Items.chicken), new ItemStack(Items.porkchop), new ItemStack(Items.fish), new ItemStack(Items.fish, 1), Witchery.Items.GENERIC.itemMuttonRaw.createStack()};
      ItemStack[] hunterItemsSilvered = var34;
      int cloth = var34.length;

      int DEFAULT_FORCE_CHANCE;
      for(DEFAULT_FORCE_CHANCE = 0; DEFAULT_FORCE_CHANCE < cloth; ++DEFAULT_FORCE_CHANCE) {
         ItemStack meat = hunterItemsSilvered[DEFAULT_FORCE_CHANCE];
         GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Witchery.Items.STEW_RAW), new Object[]{"cropGarlic", meat, new ItemStack(Items.potato), new ItemStack(Items.carrot), new ItemStack(Items.bowl), new ItemStack(Blocks.brown_mushroom)}));
      }

      Item[][] var33 = new Item[][]{{Witchery.Items.HUNTER_BOOTS_SILVERED, Witchery.Items.HUNTER_BOOTS_GARLICKED}, {Witchery.Items.HUNTER_LEGS_SILVERED, Witchery.Items.HUNTER_LEGS_GARLICKED}, {Witchery.Items.HUNTER_COAT_SILVERED, Witchery.Items.HUNTER_COAT_GARLICKED}, {Witchery.Items.HUNTER_HAT_SILVERED, Witchery.Items.HUNTER_HAT_GARLICKED}};

      for(cloth = 0; cloth < var33.length; ++cloth) {
         CraftingManager.getInstance().addRecipe(new ItemStack(var33[cloth][1]), new Object[]{" g ", "g#g", " s ", Character.valueOf('#'), new ItemStack(var33[cloth][0]), Character.valueOf('s'), new ItemStack(Items.string), Character.valueOf('g'), new ItemStack(Witchery.Items.SEEDS_GARLIC)}).func_92100_c();
      }

      for(cloth = 0; cloth < 9; ++cloth) {
         GameRegistry.addShapelessRecipe(new ItemStack(Witchery.Items.VAMPIRE_BOOK, 1, cloth + 1), new Object[]{new ItemStack(Witchery.Items.VAMPIRE_BOOK, 1, cloth), Witchery.Items.GENERIC.itemVampireBookPage.createStack()});
      }

      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.BLOOD_GOBLET), new Object[]{"b b", " b ", " g ", Character.valueOf('g'), new ItemStack(Blocks.glass), Character.valueOf('b'), new ItemStack(Items.glass_bottle)});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.BLOOD_CRUCIBLE), new Object[]{"s s", "blb", Character.valueOf('s'), new ItemStack(Blocks.stone_brick_stairs), Character.valueOf('b'), new ItemStack(Blocks.stonebrick), Character.valueOf('l'), new ItemStack(Blocks.stone_slab, 1, 5)});
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Witchery.Items.COFFIN), new Object[]{"ppp", "lbl", "lll", Character.valueOf('b'), new ItemStack(Items.bed), Character.valueOf('p'), "plankWood", Character.valueOf('l'), "logWood"}));
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.DAYLIGHT_COLLECTOR), new Object[]{"g g", " r ", "ici", Character.valueOf('g'), new ItemStack(Items.gold_ingot), Character.valueOf('r'), new ItemStack(Items.repeater), Character.valueOf('i'), new ItemStack(Items.iron_ingot), Character.valueOf('c'), new ItemStack(Blocks.daylight_detector)});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.VAMPIRE_HELMET), new Object[]{" i ", "i#i", " i ", Character.valueOf('i'), new ItemStack(Items.iron_ingot), Character.valueOf('#'), new ItemStack(Witchery.Items.VAMPIRE_HAT)});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.VAMPIRE_COAT_CHAIN), new Object[]{" i ", "i#i", " i ", Character.valueOf('i'), new ItemStack(Items.iron_ingot), Character.valueOf('#'), new ItemStack(Witchery.Items.VAMPIRE_COAT)});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.VAMPIRE_COAT_FEMALE_CHAIN), new Object[]{" i ", "i#i", " i ", Character.valueOf('i'), new ItemStack(Items.iron_ingot), Character.valueOf('#'), new ItemStack(Witchery.Items.VAMPIRE_COAT_FEMALE)});
      ItemStack var35 = Witchery.Items.GENERIC.itemDarkCloth.createStack();
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.VAMPIRE_HAT), new Object[]{"###", "# #", Character.valueOf('#'), var35});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.VAMPIRE_COAT), new Object[]{"# #", "###", "###", Character.valueOf('#'), var35});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.VAMPIRE_COAT_FEMALE), new Object[]{"# #", "#l#", "###", Character.valueOf('l'), new ItemStack(Items.leather), Character.valueOf('#'), var35});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.VAMPIRE_LEGS), new Object[]{"###", "# #", "# #", Character.valueOf('#'), var35});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.VAMPIRE_LEGS_KILT), new Object[]{"###", "###", "# #", Character.valueOf('#'), var35});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.VAMPIRE_BOOTS), new Object[]{"# #", "# #", Character.valueOf('#'), var35});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.CANE_SWORD), new Object[]{" #g", "#d#", "## ", Character.valueOf('g'), new ItemStack(Items.gold_ingot), Character.valueOf('d'), new ItemStack(Items.diamond_sword), Character.valueOf('#'), var35});
      GameRegistry.addShapedRecipe(new ItemStack(Witchery.Items.VAMPIRE_BOOK), new Object[]{"#s#", "#b#", "#g#", Character.valueOf('s'), new ItemStack(Items.nether_star), Character.valueOf('b'), new ItemStack(Items.book), Character.valueOf('g'), new ItemStack(Witchery.Items.SEEDS_GARLIC), Character.valueOf('#'), new ItemStack(Items.nether_wart)});

      for(DEFAULT_FORCE_CHANCE = 0; DEFAULT_FORCE_CHANCE < 16; ++DEFAULT_FORCE_CHANCE) {
         GameRegistry.addShapedRecipe(new ItemStack(Witchery.Blocks.SHADED_GLASS, 8, DEFAULT_FORCE_CHANCE), new Object[]{"###", "#r#", "###", Character.valueOf('r'), new ItemStack(Items.redstone), Character.valueOf('#'), new ItemStack(Blocks.stained_glass, 1, DEFAULT_FORCE_CHANCE)});
      }

      GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemWoodenStake.createStack(), new Object[]{"GGG", "GsG", "GGG", Character.valueOf('G'), "cropGarlic", Character.valueOf('s'), new ItemStack(Items.stick)}));
      OreDictionary.registerOre("plankWood", new ItemStack(Witchery.Blocks.PLANKS, 1, 32767));
      OreDictionary.registerOre("treeSapling", new ItemStack(Witchery.Blocks.SAPLING, 1, 32767));
      OreDictionary.registerOre("logWood", new ItemStack(Witchery.Blocks.LOG, 1, 32767));
      OreDictionary.registerOre("treeLeaves", new ItemStack(Witchery.Blocks.LEAVES, 1, 32767));
      OreDictionary.registerOre("stairWood", new ItemStack(Witchery.Blocks.STAIRS_ALDER, 1, 32767));
      OreDictionary.registerOre("stairWood", new ItemStack(Witchery.Blocks.STAIRS_HAWTHORN, 1, 32767));
      OreDictionary.registerOre("stairWood", new ItemStack(Witchery.Blocks.STAIRS_ROWAN, 1, 32767));
      OreDictionary.registerOre("cropGarlic", new ItemStack(Witchery.Items.SEEDS_GARLIC, 1, 32767));
      GameRegistry.addSmelting(Witchery.Items.GENERIC.itemSoftClayJar.createStack(), Witchery.Items.GENERIC.itemEmptyClayJar.createStack(), 0.0F);
      GameRegistry.addSmelting(Witchery.Items.GENERIC.itemOddPorkRaw.createStack(), Witchery.Items.GENERIC.itemOddPorkCooked.createStack(), 0.0F);
      GameRegistry.addSmelting(Witchery.Items.GENERIC.itemGoldenThread.createStack(), new ItemStack(Items.gold_nugget), 0.0F);
      GameRegistry.addSmelting(Witchery.Items.GENERIC.itemMuttonRaw.createStack(), Witchery.Items.GENERIC.itemMuttonCooked.createStack(), 0.0F);
      GameRegistry.addSmelting(new ItemStack(Witchery.Blocks.BLOODED_WOOL), Witchery.Items.GENERIC.itemDarkCloth.createStack(), 0.0F);
      GameRegistry.addSmelting(new ItemStack(Witchery.Items.STEW_RAW), new ItemStack(Witchery.Items.STEW), 1.0F);
      if(!Config.instance().smeltAllSaplingsToWoodAsh) {
         GameRegistry.addSmelting(Blocks.sapling, Witchery.Items.GENERIC.itemAshWood.createStack(), 0.0F);
         GameRegistry.addSmelting(new ItemStack(Witchery.Blocks.SAPLING), Witchery.Items.GENERIC.itemAshWood.createStack(), 0.0F);
      }

      GameRegistry.addSmelting(new ItemStack(Witchery.Blocks.LOG, 1, 0), new ItemStack(Items.coal, 1, 1), 0.0F);
      GameRegistry.addSmelting(new ItemStack(Witchery.Blocks.LOG, 1, 1), new ItemStack(Items.coal, 1, 1), 0.0F);
      GameRegistry.addSmelting(new ItemStack(Witchery.Blocks.LOG, 1, 2), new ItemStack(Items.coal, 1, 1), 0.0F);
      DistilleryRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemFoulFume.createStack(), Witchery.Items.GENERIC.itemQuicklime.createStack(), 1, Witchery.Items.GENERIC.itemGypsum.createStack(), Witchery.Items.GENERIC.itemOilOfVitriol.createStack(), new ItemStack(Items.slime_ball), (ItemStack)null);
      DistilleryRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), Dye.LAPIS_LAZULI.createStack(), 3, Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack(), new ItemStack(Items.slime_ball), Witchery.Items.GENERIC.itemFoulFume.createStack());
      DistilleryRecipes.instance().addRecipe(new ItemStack(Items.diamond), Witchery.Items.GENERIC.itemOilOfVitriol.createStack(), 3, Witchery.Items.GENERIC.itemDiamondVapour.createStack(), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), (ItemStack)null);
      DistilleryRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemDiamondVapour.createStack(), new ItemStack(Items.ghast_tear), 3, Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack(), Witchery.Items.GENERIC.itemFoulFume.createStack(), Witchery.Items.GENERIC.itemRefinedEvil.createStack());
      DistilleryRecipes.instance().addRecipe(new ItemStack(Items.ender_pearl), (ItemStack)null, 6, Witchery.Items.GENERIC.itemEnderDew.createStack(2), Witchery.Items.GENERIC.itemEnderDew.createStack(2), Witchery.Items.GENERIC.itemEnderDew.createStack(), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack());
      DistilleryRecipes.instance().addRecipe(new ItemStack(Items.blaze_powder), new ItemStack(Items.gunpowder), 1, new ItemStack(Items.glowstone_dust), new ItemStack(Items.glowstone_dust), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack(), (ItemStack)null);
      DistilleryRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemDemonHeart.createStack(), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), 4, Witchery.Items.GENERIC.itemInfernalBlood.createStack(2), Witchery.Items.GENERIC.itemInfernalBlood.createStack(2), Witchery.Items.GENERIC.itemRefinedEvil.createStack(), (ItemStack)null);
      DistilleryRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemDemonHeart.createStack(), new ItemStack(Blocks.netherrack), 2, new ItemStack(Blocks.soul_sand), Witchery.Items.GENERIC.itemInfernalBlood.createStack(), Witchery.Items.GENERIC.itemInfernalBlood.createStack(), (ItemStack)null);
      DistilleryRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfFlowingSpirit.createStack(), Witchery.Items.GENERIC.itemOilOfVitriol.createStack(), 2, Witchery.Items.GENERIC.itemFocusedWill.createStack(), Witchery.Items.GENERIC.itemCondensedFear.createStack(), Witchery.Items.GENERIC.itemBrewOfHollowTears.createStack(4), Witchery.Items.GENERIC.itemBrewOfHollowTears.createStack(4));
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfVines.createStack(3), 1, 0, 0.0F, -16753913, 0, new ItemStack[]{new ItemStack(Blocks.vine), new ItemStack(Blocks.red_mushroom), new ItemStack(Blocks.brown_mushroom), Witchery.Items.GENERIC.itemDogTongue.createStack(), new ItemStack(Items.wheat), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfWebs.createStack(3), 1, 0, 0.0F, -1, 0, new ItemStack[]{Witchery.Items.GENERIC.itemWeb.createStack(), new ItemStack(Blocks.red_mushroom), Witchery.Items.GENERIC.itemBatWool.createStack(), new ItemStack(Blocks.yellow_flower), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack(), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfThorns.createStack(3), 1, 0, 0.0F, -10027232, 0, new ItemStack[]{Dye.CACTUS_GREEN.createStack(), new ItemStack(Blocks.brown_mushroom), Witchery.Items.GENERIC.itemOilOfVitriol.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), new ItemStack(Blocks.red_flower), Witchery.Items.GENERIC.itemMandrakeRoot.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfInk.createStack(3), 1, 0, 0.0F, -13421773, 0, new ItemStack[]{Dye.INK_SAC.createStack(), Witchery.Items.GENERIC.itemQuicklime.createStack(), Witchery.Items.GENERIC.itemOilOfVitriol.createStack(), new ItemStack(Items.slime_ball), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), Witchery.Items.GENERIC.itemRowanBerries.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(3), 1, 0, 0.0F, -11258073, 0, new ItemStack[]{new ItemStack(Witchery.Blocks.SAPLING, 1, 0), new ItemStack(Witchery.Blocks.SAPLING, 1, 1), new ItemStack(Witchery.Blocks.SAPLING, 1, 2), Witchery.Items.GENERIC.itemDogTongue.createStack(), Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), new ItemStack(Blocks.red_flower)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfErosion.createStack(3), 1, 0, 0.0F, -4456656, 0, new ItemStack[]{Witchery.Items.GENERIC.itemOilOfVitriol.createStack(), Witchery.Items.GENERIC.itemOilOfVitriol.createStack(), Witchery.Items.GENERIC.itemQuicklime.createStack(), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), new ItemStack(Blocks.yellow_flower), new ItemStack(Items.magma_cream)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfRaising.createStack(3), 1, 0, 500.0F, -12120505, 0, new ItemStack[]{Witchery.Items.GENERIC.itemBatWool.createStack(), Witchery.Items.GENERIC.itemMutandis.createStack(), new ItemStack(Items.redstone), Witchery.Items.GENERIC.itemOilOfVitriol.createStack(), new ItemStack(Items.bone), new ItemStack(Items.rotten_flesh)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewGrotesque.createStack(3), 1, 0, 500.0F, -13491946, 0, new ItemStack[]{Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), Witchery.Items.GENERIC.itemArtichoke.createStack(), Witchery.Items.GENERIC.itemDogTongue.createStack(), new ItemStack(Items.golden_apple), new ItemStack(Items.poisonous_potato)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfLove.createStack(3), 1, 0, 0.0F, -23044, 0, new ItemStack[]{new ItemStack(Blocks.red_flower), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack(), Witchery.Items.GENERIC.itemArtichoke.createStack(), new ItemStack(Items.golden_carrot), new ItemStack(Blocks.waterlily), Dye.COCOA_BEANS.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfIce.createStack(3), 1, 0, 1000.0F, -13565953, 0, new ItemStack[]{Witchery.Items.GENERIC.itemIcyNeedle.createStack(), new ItemStack(Items.snowball), Witchery.Items.GENERIC.itemArtichoke.createStack(), new ItemStack(Items.speckled_melon), new ItemStack(Blocks.red_mushroom), Witchery.Items.GENERIC.itemOdourOfPurity.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfTheDepths.createStack(3), 1, 0, 0.0F, -15260093, 0, new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), Witchery.Items.GENERIC.itemArtichoke.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), new ItemStack(Blocks.waterlily), Dye.INK_SAC.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfInfection.createStack(3), 0, 0, 0.0F, -11112850, 0, new ItemStack[]{Witchery.Items.GENERIC.itemToeOfFrog.createStack(), Witchery.Items.GENERIC.itemCreeperHeart.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), new ItemStack(Items.rotten_flesh), Witchery.Items.GENERIC.itemMutandis.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfSleeping.createStack(3), 1, 0, 0.0F, -7710856, 0, new ItemStack[]{Witchery.Items.GENERIC.itemPurifiedMilk.createStack(), new ItemStack(Items.cookie), Witchery.Items.GENERIC.itemBrewOfLove.createStack(), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack(), Witchery.Items.GENERIC.itemIcyNeedle.createStack(), Witchery.Items.GENERIC.itemArtichoke.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfFlowingSpirit.createStack(3), 0, 0, 0.0F, -16711834, Config.instance().dimensionDreamID, new ItemStack[]{Witchery.Items.GENERIC.itemFancifulThread.createStack(), Witchery.Items.GENERIC.itemArtichoke.createStack(), Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), new ItemStack(Witchery.Blocks.SPANISH_MOSS), new ItemStack(Witchery.Blocks.GLINT_WEED), Witchery.Items.GENERIC.itemBatWool.createStack()}).setUnlocalizedName("witchery.brew.flowingspirit");
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfWasting.createStack(3), 1, 0, 0.0F, -12440546, 0, new ItemStack[]{Witchery.Items.GENERIC.itemMellifluousHunger.createStack(), new ItemStack(Items.rotten_flesh), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), new ItemStack(Witchery.Blocks.EMBER_MOSS), new ItemStack(Items.poisonous_potato), new ItemStack(Items.spider_eye)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfBats.createStack(3), 1, 0, 0.0F, -9809858, 0, new ItemStack[]{Witchery.Items.GENERIC.itemBatBall.createStack(), Witchery.Items.GENERIC.itemBatWool.createStack(), new ItemStack(Items.apple), new ItemStack(Items.sugar), new ItemStack(Items.fermented_spider_eye), new ItemStack(Items.gunpowder)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewSubstitution.createStack(3), 1, 0, 0.0F, -7010720, 0, new ItemStack[]{Witchery.Items.GENERIC.itemEnderDew.createStack(), Witchery.Items.GENERIC.itemEnderDew.createStack(), Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack(Items.egg), new ItemStack(Items.magma_cream), Witchery.Items.GENERIC.itemBranchEnt.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewRevealing.createStack(3), 1, 0, 0.0F, -4079167, 0, new ItemStack[]{new ItemStack(Items.carrot), new ItemStack(Items.spider_eye), new ItemStack(Items.spider_eye), new ItemStack(Items.potionitem, 1, 8198), new ItemStack(Blocks.brown_mushroom), Witchery.Items.GENERIC.itemOdourOfPurity.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfSolidDirt.createStack(3), 1, 0, 2000.0F, -11720688, 0, true, new ItemStack[]{new ItemStack(Blocks.dirt), Witchery.Items.GENERIC.itemFoulFume.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemMutandis.createStack(), Witchery.Items.GENERIC.itemAshWood.createStack(), new ItemStack(Witchery.Blocks.SPANISH_MOSS)}).setUnlocalizedName("witchery.brew.solidification");
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfSolidRock.createStack(3), 1, 0, 2000.0F, -8355712, 0, false, new ItemStack[]{new ItemStack(Blocks.stone), Witchery.Items.GENERIC.itemFoulFume.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemMutandis.createStack(), Witchery.Items.GENERIC.itemAshWood.createStack(), new ItemStack(Witchery.Blocks.SPANISH_MOSS)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfSolidSand.createStack(3), 1, 0, 2000.0F, -3495323, 0, false, new ItemStack[]{new ItemStack(Blocks.sand), Witchery.Items.GENERIC.itemFoulFume.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemMutandis.createStack(), Witchery.Items.GENERIC.itemAshWood.createStack(), new ItemStack(Witchery.Blocks.SPANISH_MOSS)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfSolidSandstone.createStack(3), 1, 0, 2000.0F, -8427008, 0, false, new ItemStack[]{new ItemStack(Blocks.sandstone), Witchery.Items.GENERIC.itemFoulFume.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemMutandis.createStack(), Witchery.Items.GENERIC.itemAshWood.createStack(), new ItemStack(Witchery.Blocks.SPANISH_MOSS)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfSolidErosion.createStack(3), 1, 0, 2000.0F, -3300, 0, false, new ItemStack[]{Witchery.Items.GENERIC.itemBrewOfErosion.createStack(), Witchery.Items.GENERIC.itemFoulFume.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemMutandis.createStack(), Witchery.Items.GENERIC.itemAshWood.createStack(), new ItemStack(Witchery.Blocks.SPANISH_MOSS)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfCursedLeaping.createStack(3), 1, 1, 0.0F, -16758145, 0, new ItemStack[]{new ItemStack(Items.bone), new ItemStack(Items.apple), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), new ItemStack(Items.feather), new ItemStack(Items.fish)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfFrogsTongue.createStack(3), 1, 2, 0.0F, -12938226, 0, new ItemStack[]{new ItemStack(Blocks.red_mushroom), new ItemStack(Items.wheat), Witchery.Items.GENERIC.itemBrewOfWebs.createStack(), Witchery.Items.GENERIC.itemArtichoke.createStack(), new ItemStack(Items.slime_ball), Witchery.Items.GENERIC.itemToeOfFrog.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemBrewOfHitchcock.createStack(3), 1, 3, 0.0F, -3908582, 0, new ItemStack[]{new ItemStack(Blocks.brown_mushroom), new ItemStack(Items.wheat_seeds), Witchery.Items.GENERIC.itemBrewOfThorns.createStack(), Witchery.Items.GENERIC.itemBatWool.createStack(), new ItemStack(Items.feather), Witchery.Items.GENERIC.itemOwletsWing.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemCongealedSpirit.createStack(), 0, 0, 2000.0F, -3096310, 0, new ItemStack[]{Witchery.Items.GENERIC.itemBrewOfHollowTears.createStack(), Witchery.Items.GENERIC.itemSubduedSpirit.createStack(), Witchery.Items.GENERIC.itemSubduedSpirit.createStack(), Witchery.Items.GENERIC.itemSubduedSpirit.createStack(), Witchery.Items.GENERIC.itemSubduedSpirit.createStack(), Witchery.Items.GENERIC.itemSubduedSpirit.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), 0, 0, 1000.0F, -59882, 0, new ItemStack[]{new ItemStack(Items.redstone), Witchery.Items.GENERIC.itemDropOfLuck.createStack(), Witchery.Items.GENERIC.itemBatWool.createStack(), Witchery.Items.GENERIC.itemDogTongue.createStack(), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), Witchery.Items.GENERIC.itemMandrakeRoot.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemFlyingOintment.createStack(), 0, 0, 3000.0F, -17620, 0, new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), new ItemStack(Items.potionitem, 1, 8258), new ItemStack(Items.diamond), new ItemStack(Items.feather), Witchery.Items.GENERIC.itemBatWool.createStack(), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemMysticUnguent.createStack(), 0, 0, 3000.0F, -14333109, 0, new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), new ItemStack(Items.potionitem, 1, 8265), new ItemStack(Items.diamond), new ItemStack(Witchery.Blocks.SAPLING, 1, 0), Witchery.Items.GENERIC.itemCreeperHeart.createStack(), Witchery.Items.GENERIC.itemInfernalBlood.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemHappenstanceOil.createStack(), 0, 0, 2000.0F, 8534058, 0, new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), new ItemStack(Items.potionitem, 1, 8262), new ItemStack(Items.ender_eye), new ItemStack(Items.golden_carrot), new ItemStack(Items.spider_eye), Witchery.Items.GENERIC.itemMandrakeRoot.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemGhostOfTheLight.createStack(2), 0, 0, 4000.0F, -5584658, 0, new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), new ItemStack(Items.potionitem, 1, 8270), new ItemStack(Items.potionitem, 1, 8259), Witchery.Items.POPPET.firePoppet.createStack(), new ItemStack(Blocks.torch), Witchery.Items.GENERIC.itemDogTongue.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemSoulOfTheWorld.createStack(2), 0, 0, 4000.0F, -16003328, 0, new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), new ItemStack(Items.potionitem, 1, 8257), new ItemStack(Items.golden_apple, 1, 1), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), new ItemStack(Witchery.Blocks.SAPLING, 1, 0)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemSpiritOfOtherwhere.createStack(2), 0, 0, 4000.0F, -7128833, 0, new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), new ItemStack(Items.potionitem, 1, 8258), new ItemStack(Items.ender_eye), new ItemStack(Items.ender_eye), Witchery.Items.GENERIC.itemDropOfLuck.createStack(), Witchery.Items.GENERIC.itemBatWool.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemSpiritOfOtherwhere.createStack(2), 0, 0, 4000.0F, -7128833, 0, false, new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), new ItemStack(Items.potionitem, 1, 16210), new ItemStack(Items.ender_eye), new ItemStack(Items.ender_eye), Witchery.Items.GENERIC.itemDropOfLuck.createStack(), Witchery.Items.GENERIC.itemBatWool.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemInfernalAnimus.createStack(2), 0, 0, 4000.0F, -7598080, 0, new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), new ItemStack(Items.potionitem, 1, 8236), Witchery.Items.POPPET.voodooPoppet.createStack(), Witchery.Items.GENERIC.itemDemonHeart.createStack(), Witchery.Items.GENERIC.itemRefinedEvil.createStack(), new ItemStack(Items.blaze_rod)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemInfernalAnimus.createStack(2), 0, 0, 4000.0F, -7598080, 0, false, new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), new ItemStack(Items.potionitem, 1, 16172), Witchery.Items.POPPET.voodooPoppet.createStack(), Witchery.Items.GENERIC.itemDemonHeart.createStack(), Witchery.Items.GENERIC.itemRefinedEvil.createStack(), new ItemStack(Items.blaze_rod)});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemInfusionBase.createStack(), 1, 0, 3000.0F, -10520657, 0, new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), Witchery.Items.GENERIC.itemBrewOfFlowingSpirit.createStack(), Witchery.Items.GENERIC.itemCreeperHeart.createStack(), Witchery.Items.GENERIC.itemToeOfFrog.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack(), Witchery.Items.GENERIC.itemDogTongue.createStack()});
      KettleRecipes.instance().addRecipe(Witchery.Items.GENERIC.itemInfusionBase.createStack(2), 0, 0, 3000.0F, -10520657, 0, new ItemStack[]{Witchery.Items.GENERIC.itemInfusionBase.createStack(), Witchery.Items.GENERIC.itemBrewOfFlowingSpirit.createStack(), Witchery.Items.GENERIC.itemHintOfRebirth.createStack(), Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), Witchery.Items.GENERIC.itemBatWool.createStack(), new ItemStack(Witchery.Blocks.BRAMBLE, 1, 1)});
      CreaturePower.Registry.instance().add(new CreaturePowerSpider(1, EntityCaveSpider.class));
      CreaturePower.Registry.instance().add(new CreaturePowerSpider(2, EntitySpider.class));
      CreaturePower.Registry.instance().add(new CreaturePowerCreeper(3));
      CreaturePower.Registry.instance().add(new CreaturePowerBat(4, EntityBat.class));
      CreaturePower.Registry.instance().add(new CreaturePowerSquid(5));
      CreaturePower.Registry.instance().add(new CreaturePowerGhast(6));
      CreaturePower.Registry.instance().add(new CreaturePowerBlaze(7));
      CreaturePower.Registry.instance().add(new CreaturePowerPigMan(8));
      CreaturePower.Registry.instance().add(new CreaturePowerZombie(9));
      CreaturePower.Registry.instance().add(new CreaturePowerSkeleton(10));
      CreaturePower.Registry.instance().add(new CreaturePowerJump(11, EntityMagmaCube.class));
      CreaturePower.Registry.instance().add(new CreaturePowerJump(12, EntitySlime.class));
      CreaturePower.Registry.instance().add(new CreaturePowerSpeed(13, EntitySilverfish.class));
      CreaturePower.Registry.instance().add(new CreaturePowerSpeed(14, EntityOcelot.class));
      CreaturePower.Registry.instance().add(new CreaturePowerSpeed(15, EntityWolf.class));
      CreaturePower.Registry.instance().add(new CreaturePowerSpeed(16, EntityHorse.class));
      CreaturePower.Registry.instance().add(new CreaturePowerEnderman(17));
      CreaturePower.Registry.instance().add(new CreaturePowerHeal(18, EntitySheep.class, 1));
      CreaturePower.Registry.instance().add(new CreaturePowerHeal(19, EntityCow.class, 1));
      CreaturePower.Registry.instance().add(new CreaturePowerHeal(20, EntityChicken.class, 1));
      CreaturePower.Registry.instance().add(new CreaturePowerHeal(21, EntityPig.class, 1));
      CreaturePower.Registry.instance().add(new CreaturePowerHeal(22, EntityVillager.class, 2));
      CreaturePower.Registry.instance().add(new CreaturePowerHeal(23, EntityMooshroom.class, 2));
      CreaturePower.Registry.instance().add(new CreaturePowerBat(24, EntityOwl.class));
      CreaturePower.Registry.instance().add(new CreaturePowerJump(25, EntityToad.class));
      RiteRegistry.addRecipe(1, 0, new RiteBindCircleToTalisman(), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.CIRCLE_TALISMAN), new ItemStack(Items.redstone)}), new SacrificePower(1000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[0]).setUnlocalizedName("witchery.rite.bindcircle");
      RiteRegistry.addRecipe(2, 1, new RiteSummonItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack(), RiteSummonItem.Binding.LOCATION), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemWaystone.createStack(), Witchery.Items.GENERIC.itemEnderDew.createStack(), new ItemStack(Items.glowstone_dust)}), new SacrificePower(500.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.bindwaystone");
      RiteRegistry.addRecipe(3, 3, new RiteSummonItem(Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack(), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemAttunedStone.createStack(), new ItemStack(Items.glowstone_dust), new ItemStack(Items.redstone), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemQuicklime.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0), new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.chargestone");
      RiteRegistry.addRecipe(4, 4, new RiteInfusionRecharge(10, 4, 40.0F, 0), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.potionitem, 1, 8193)}), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0), new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.infusionrecharge");
      RiteRegistry.addRecipe(5, 5, new RiteTeleportToWaystone(3), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemWaystoneBound.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 16, 0)}).setUnlocalizedName("witchery.rite.teleporttowaystone");
      RiteRegistry.addRecipe(6, 6, new RiteTeleportEntity(3), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemWaystone.createStack(), new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemEnderDew.createStack(), new ItemStack(Items.iron_axe)}), new SacrificePower(3000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 28, 0)}).setUnlocalizedName("witchery.rite.teleportentity");
      RiteRegistry.addRecipe(7, 7, new RiteTransposeOres(8, 30, new Block[]{Blocks.iron_ore, Blocks.gold_ore}), new SacrificeItem(new ItemStack[]{new ItemStack(Items.ender_pearl), new ItemStack(Items.iron_ingot), new ItemStack(Items.blaze_powder), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 40, 0)}).setUnlocalizedName("witchery.rite.teleportironore");
      RiteRegistry.addRecipe(8, 8, new RiteProtectionCircleRepulsive(4, 0.8F, 0), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.feather), new ItemStack(Items.redstone)}), new SacrificePower(500.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.protection");
      RiteRegistry.addRecipe(9, 9, new RiteProtectionCircleAttractive(4, 0.8F, 0), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.slime_ball), new ItemStack(Items.redstone)}), new SacrificePower(500.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.imprisonment");
      RiteRegistry.addRecipe(10, 10, new RiteProtectionCircleBarrier(4, 5, 1.2F, false, 0), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Blocks.obsidian), new ItemStack(Items.redstone)}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(500.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.barrier");
      RiteRegistry.addRecipe(11, 11, new RiteProtectionCircleBarrier(6, 6, 1.4F, true, 0), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Blocks.obsidian), new ItemStack(Items.glowstone_dust)}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(1000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.barrierlarge");
      RiteRegistry.addRecipe(12, 12, new RiteProtectionCircleBarrier(6, 4, 0.0F, true, 60), new SacrificeItem(new ItemStack[]{new ItemStack(Blocks.obsidian), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.barrierportable");
      RiteRegistry.addRecipe(13, 13, new RiteRaiseVolcano(8, 8), new SacrificeItem(new ItemStack[]{new ItemStack(Blocks.stone), new ItemStack(Items.magma_cream), new ItemStack(Items.golden_sword), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 16)}).setUnlocalizedName("witchery.rite.volcano");
      RiteRegistry.addRecipe(14, 14, new RiteWeatherCallStorm(0, 3, 8), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.wooden_sword), Witchery.Items.GENERIC.itemAshWood.createStack()}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(1000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.storm");
      RiteRegistry.addRecipe(15, 15, new RiteWeatherCallStorm(3, 7, 18), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.stone_sword), Witchery.Items.GENERIC.itemAshWood.createStack()}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.stormlarge");
      RiteRegistry.addRecipe(16, 16, new RiteWeatherCallStorm(3, 7, 18), new SacrificeItem(new ItemStack[]{new ItemStack(Items.iron_sword), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.stormportable");
      RiteRegistry.addRecipe(17, 17, new RiteEclipse(), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.stone_axe), Witchery.Items.GENERIC.itemQuicklime.createStack()}), new SacrificePower(3000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_AT_DAY), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.eclipse");
      RiteRegistry.addRecipe(18, 18, new RiteEclipse(), new SacrificeItem(new ItemStack[]{new ItemStack(Items.iron_axe), Witchery.Items.GENERIC.itemQuicklime.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.of(RitualTraits.ONLY_AT_DAY), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.eclipseportable");
      RiteRegistry.addRecipe(19, 19, new RitePartEarth(60, 1, 10), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemBrewOfErosion.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.partearth");
      RiteRegistry.addRecipe(20, 20, new RiteRaiseColumn(4, 8), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), new ItemStack(Blocks.cactus), new ItemStack(Items.gunpowder)}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack())}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.raiseearth");
      RiteRegistry.addRecipe(21, 23, new RiteBanishDemon(9), new SacrificeItem(new ItemStack[]{new ItemStack(Items.blaze_powder), Witchery.Items.GENERIC.itemWaystone.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.banishdemonportable");
      RiteRegistry.addRecipe(22, 24, new RiteBanishDemon(9), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.blaze_powder), Witchery.Items.GENERIC.itemWaystone.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.banishdemon");
      RiteRegistry.addRecipe(23, 25, new RiteSummonCreature(EntityDemon.class, false), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemRefinedEvil.createStack(), new ItemStack(Items.blaze_powder), new ItemStack(Items.ender_pearl)}), new SacrificeLiving(EntityVillager.class), new SacrificePower(3000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 40)}).setUnlocalizedName("witchery.rite.summondemon");
      RiteRegistry.addRecipe(24, 26, new RiteSummonCreature(EntityDemon.class, false), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemRefinedEvil.createStack(), new ItemStack(Items.blaze_rod), new ItemStack(Items.ender_pearl), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), new SacrificePower(3000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 40)}).setUnlocalizedName("witchery.rite.summondemonexpensive");
      RiteRegistry.addRecipe(25, 27, new RiteSummonCreature(EntityWither.class, false), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.skull, 1, 1), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), new ItemStack(Items.ender_pearl)}), new SacrificeLiving(EntityVillager.class), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 28), new Circle(0, 0, 40)}).setUnlocalizedName("witchery.rite.summonwither");
      RiteRegistry.addRecipe(26, 28, new RiteSummonCreature(EntityWither.class, false), new SacrificeItem(new ItemStack[]{new ItemStack(Items.skull, 1, 1), new ItemStack(Items.diamond), new ItemStack(Items.ender_pearl), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 28), new Circle(0, 0, 40)}).setUnlocalizedName("witchery.rite.summonwitherexpensive");
      this.infusionLight = new InfusionLight(1);
      Infusion.Registry.instance().add(this.infusionLight);
      RiteRegistry.addRecipe(27, 31, new RiteInfusePlayers(this.infusionLight, 200, 4), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemGhostOfTheLight.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0), new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.infusionlight");
      this.infusionWorld = new InfusionOverworld(2);
      Infusion.Registry.instance().add(this.infusionWorld);
      RiteRegistry.addRecipe(28, 32, new RiteInfusePlayers(this.infusionWorld, 200, 4), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemSoulOfTheWorld.createStack()}), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0), new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.infusionearth");
      this.infusionEnder = new InfusionOtherwhere(3);
      Infusion.Registry.instance().add(this.infusionEnder);
      RiteRegistry.addRecipe(29, 33, new RiteInfusePlayers(this.infusionEnder, 200, 4), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemSpiritOfOtherwhere.createStack()}), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 16, 0), new Circle(0, 28, 0)}).setUnlocalizedName("witchery.rite.infusionender");
      this.infusionBeast = new InfusionInfernal(4);
      Infusion.Registry.instance().add(this.infusionBeast);
      RiteRegistry.addRecipe(30, 34, new RiteInfusePlayers(this.infusionBeast, 200, 4), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemInfernalAnimus.createStack()}), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 16), new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.infusionhell");
      RiteRegistry.addRecipe(31, 35, new RiteSummonItem(Witchery.Items.GENERIC.itemBroomEnchanted.createStack(), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemBroom.createStack(), Witchery.Items.GENERIC.itemFlyingOintment.createStack()}), new SacrificePower(3000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle[]{new Circle(16, 0, 0), new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.infusionsky");
      RiteRegistry.addRecipe(32, 36, new RiteSummonItem(Witchery.Items.GENERIC.itemNecroStone.createStack(), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemAttunedStone.createStack(), new ItemStack(Items.bone), new ItemStack(Items.rotten_flesh), Witchery.Items.GENERIC.itemAshWood.createStack(), new ItemStack(Items.iron_sword), Witchery.Items.GENERIC.itemSpectralDust.createStack()}), new SacrificePower(1000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.necrostone");
      RiteRegistry.addRecipe(33, 30, new RiteSummonCreature(EntityFamiliar.class, true), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemDropOfLuck.createStack(), new ItemStack(Items.porkchop), new ItemStack(Items.gold_ingot), new ItemStack(Witchery.Items.ARTHANA)}), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.summonfamiliar");
      RiteRegistry.addRecipe(34, 2, new RiteSummonItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack(2), RiteSummonItem.Binding.COPY_LOCATION), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemWaystoneBound.createStack(), Witchery.Items.GENERIC.itemWaystone.createStack(), Witchery.Items.GENERIC.itemEnderDew.createStack(), new ItemStack(Items.redstone)}), new SacrificePower(500.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.bindwaystonecopy");
      RiteRegistry.addRecipe(35, 21, new RiteFertility(50, 15), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Dye.BONE_MEAL.createStack(), Witchery.Items.GENERIC.itemHintOfRebirth.createStack(), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), Witchery.Items.GENERIC.itemQuicklime.createStack(), Witchery.Items.GENERIC.itemGypsum.createStack(), Witchery.Items.GENERIC.itemMutandis.createStack()}), new SacrificePower(3000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.fertility");
      RiteRegistry.addRecipe(36, 22, new RiteFertility(50, 15), new SacrificeItem(new ItemStack[]{Dye.BONE_MEAL.createStack(), Witchery.Items.GENERIC.itemHintOfRebirth.createStack(), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), Witchery.Items.GENERIC.itemQuicklime.createStack(), Witchery.Items.GENERIC.itemGypsum.createStack(), Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.fertilityportable");
      RiteRegistry.addRecipe(37, 37, new RiteBlight(80, 15), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack(), Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack(), new ItemStack(Items.fermented_spider_eye), new ItemStack(Items.speckled_melon), new ItemStack(Items.rotten_flesh), new ItemStack(Items.diamond)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.curseblight");
      RiteRegistry.addRecipe(38, 38, new RiteBlindness(80, 15), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack(), Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack(), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), Witchery.Items.GENERIC.itemBrewOfInk.createStack(), new ItemStack(Items.poisonous_potato), new ItemStack(Items.spider_eye), new ItemStack(Items.diamond)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 16)}).setUnlocalizedName("witchery.rite.curseblindness");
      RiteRegistry.addRecipe(39, 39, new RiteHellOnEarth(20, 15, 200.0F), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), Witchery.Items.GENERIC.itemDemonHeart.createStack(), Witchery.Items.GENERIC.itemWaystone.createStack(), new ItemStack(Items.nether_star)}), new SacrificeLiving(EntityVillager.class), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(5000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_OVERWORLD, RitualTraits.ONLY_AT_NIGHT), new Circle[]{new Circle(0, 0, 16), new Circle(0, 28, 0), new Circle(0, 0, 40)}).setUnlocalizedName("witchery.rite.hellonearth");
      RiteRegistry.addRecipe(40, 29, new RiteSummonCreature(EntityWitch.class, false), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemDiamondVapour.createStack(), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), new ItemStack(Items.magma_cream), new ItemStack(Witchery.Items.ARTHANA), new ItemStack(Items.fermented_spider_eye)}), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 16)}).setUnlocalizedName("witchery.rite.summonwitch");
      RiteRegistry.addRecipe(41, 1, new RiteSummonItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack(), RiteSummonItem.Binding.LOCATION), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemWaystone.createStack(), Witchery.Items.GENERIC.itemEnderDew.createStack(), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.bindwaystoneportable");
      RiteRegistry.addRecipe(42, 2, new RiteSummonItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack(2), RiteSummonItem.Binding.COPY_LOCATION), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemWaystoneBound.createStack(), Witchery.Items.GENERIC.itemWaystone.createStack(), Witchery.Items.GENERIC.itemEnderDew.createStack(), Witchery.Items.GENERIC.itemQuicklime.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.bindwaystonecopyportable");
      RiteRegistry.addRecipe(43, 22, new RiteNaturesPower(14, 8, 150, 2), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), new ItemStack(Witchery.Blocks.SAPLING, 1, 0), new ItemStack(Witchery.Blocks.SAPLING, 1, 1), new ItemStack(Witchery.Blocks.SAPLING, 1, 2), new ItemStack(Blocks.sapling, 1, 0), new ItemStack(Blocks.sapling, 1, 1), new ItemStack(Blocks.sapling, 1, 2), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.naturespower");
      RiteRegistry.addRecipe(44, 36, new RitePriorIncarnation(5, 16), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemNecroStone.createStack(), Witchery.Items.GENERIC.itemDogTongue.createStack(), new ItemStack(Items.bone), Witchery.Items.GENERIC.itemSpectralDust.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 16)}).setUnlocalizedName("witchery.rite.priorincarnation");
      RiteRegistry.addRecipe(45, 0, new RiteBindCircleToTalisman(), new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.CIRCLE_TALISMAN), new ItemStack(Items.glowstone_dust), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[0]).setUnlocalizedName("witchery.rite.bindcircleportable");
      RiteRegistry.addRecipe(46, 20, new RiteRaiseColumn(6, 8), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), new ItemStack(Blocks.cactus), new ItemStack(Items.redstone)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.raiseearth");
      RiteRegistry.addRecipe(47, 20, new RiteRaiseColumn(9, 8), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), new ItemStack(Blocks.cactus), new ItemStack(Items.glowstone_dust)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(40, 0, 0)}).setUnlocalizedName("witchery.rite.raiseearth");
      RiteRegistry.addRecipe(48, 48, new RiteCurseCreature(true, "witcheryCursed", 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), new ItemStack(Items.fermented_spider_eye), new ItemStack(Items.gunpowder), Witchery.Items.GENERIC.itemBrewGrotesque.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_IN_STROM), new Circle[]{new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.cursecreature1");
      RiteRegistry.addRecipe(49, 49, new RiteCurseCreature(false, "witcheryCursed", 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), new ItemStack(Items.spider_eye), new ItemStack(Items.gunpowder), Witchery.Items.GENERIC.itemBrewOfLove.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.removecurse1");
      RiteRegistry.addRecipe(50, 35, new RiteSummonItem(new ItemStack(Witchery.Items.MYSTIC_BRANCH), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemBranchEnt.createStack(), Witchery.Items.GENERIC.itemMysticUnguent.createStack()}), new SacrificePower(3000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle[]{new Circle(16, 0, 0), new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.infusiontree");
      RiteRegistry.addRecipe(51, 20, new RiteCookItem(5.0F, 0.08D), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.blaze_rod), Witchery.Items.GENERIC.itemAshWood.createStack(), new ItemStack(Items.coal)}), new SacrificePower(1000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 16)}).setUnlocalizedName("witchery.rite.cookfood");
      RiteRegistry.addRecipe(52, 48, new RiteCurseCreature(true, "witcheryInsanity", 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), new ItemStack(Items.poisonous_potato), new ItemStack(Items.sugar), Witchery.Items.GENERIC.itemBrewGrotesque.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_IN_STROM), new Circle[]{new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.curseinsanity1");
      RiteRegistry.addRecipe(53, 49, new RiteCurseCreature(false, "witcheryInsanity", 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), new ItemStack(Items.potato), new ItemStack(Items.sugar), Witchery.Items.GENERIC.itemBrewOfLove.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.removeinsanity1");
      RiteRegistry.addRecipe(54, 1, new RiteBindFamiliar(7), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack(), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack(), new ItemStack(Items.diamond), Witchery.Items.GENERIC.itemInfernalBlood.createStack()}), new SacrificePower(8000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.bindfamiliar");
      RiteRegistry.addRecipe(55, 30, new RiteCallFamiliar(7), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), Witchery.Items.GENERIC.itemHintOfRebirth.createStack(), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack()}), new SacrificePower(1000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.callfamiliar");
      RiteRegistry.addRecipe(56, 50, new RiteCursePoppets(1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), Witchery.Items.POPPET.antiVoodooPoppet.createStack(), new ItemStack(Items.blaze_powder), Witchery.Items.GENERIC.itemSpectralDust.createStack()}), new SacrificePower(7000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.corruptvoodooprotection");
      RiteRegistry.addRecipe(57, 35, new RiteSummonItem(new ItemStack(Witchery.Blocks.CRYSTAL_BALL), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemQuartzSphere.createStack(), new ItemStack(Items.gold_ingot), Witchery.Items.GENERIC.itemHappenstanceOil.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.infusionfuture");
      RiteRegistry.addRecipe(58, 20, new RiteCookItem(5.0F, 0.16D), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack(), new ItemStack(Items.blaze_rod), Witchery.Items.GENERIC.itemAshWood.createStack(), new ItemStack(Items.blaze_powder)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 16)}).setUnlocalizedName("witchery.rite.cookfood");
      RiteRegistry.addRecipe(59, 48, new RiteCurseCreature(true, "witcherySinking", 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), Dye.INK_SAC.createStack(), new ItemStack(Items.nether_wart), Witchery.Items.GENERIC.itemBrewGrotesque.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_IN_STROM), new Circle[]{new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.cursesinking1");
      RiteRegistry.addRecipe(60, 49, new RiteCurseCreature(false, "witcherySinking", 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), Dye.BONE_MEAL.createStack(), new ItemStack(Items.nether_wart), Witchery.Items.GENERIC.itemBrewOfTheDepths.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.removesinking1");
      RiteRegistry.addRecipe(61, 35, new RiteSummonItem(Witchery.Items.GENERIC.itemSeerStone.createStack(), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemQuartzSphere.createStack(), new ItemStack(Blocks.obsidian), Witchery.Items.GENERIC.itemHappenstanceOil.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.infusionseerstone");
      RiteRegistry.addRecipe(62, 48, new RiteCurseCreature(true, "witcheryOverheating", 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), Witchery.Items.GENERIC.itemInfernalBlood.createStack(), new ItemStack(Items.blaze_rod), Witchery.Items.GENERIC.itemBrewGrotesque.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_IN_STROM), new Circle[]{new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.curseoverheating");
      RiteRegistry.addRecipe(63, 49, new RiteCurseCreature(false, "witcheryOverheating", 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), Witchery.Items.GENERIC.itemIcyNeedle.createStack(), new ItemStack(Items.blaze_rod), Witchery.Items.GENERIC.itemBrewOfTheDepths.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.cureoverheating");
      RiteRegistry.addRecipe(64, 22, new RiteClimateChange(16), new SacrificeItem(new ItemStack[]{new ItemStack(Items.spider_eye), Witchery.Items.GENERIC.itemToeOfFrog.createStack(), Witchery.Items.GENERIC.itemBatWool.createStack(), Witchery.Items.GENERIC.itemDogTongue.createStack(), Witchery.Items.GENERIC.itemOwletsWing.createStack(), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(40, 0, 0)}).setUnlocalizedName("witchery.rite.climatechange");
      RiteRegistry.addRecipe(65, 12, new RiteSphereEffect(8, Witchery.Blocks.PERPETUAL_ICE), new SacrificeItem(new ItemStack[]{new ItemStack(Items.diamond_sword), Witchery.Items.GENERIC.itemFrozenHeart.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.iceshell");
      RiteRegistry.addRecipe(66, 38, new RiteRainOfToads(5, 16, 10), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack(), Witchery.Items.GENERIC.itemRedstoneSoup.createStack(), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack(), Witchery.Items.GENERIC.itemToeOfFrog.createStack(), new ItemStack(Items.water_bucket), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.rainoffrogs");
      RiteRegistry.addRecipe(67, 4, new RiteGlyphicTransformation(), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemGypsum.createStack(), new ItemStack(Witchery.Items.ARTHANA)}), new SacrificePower(1000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[0]).setUnlocalizedName("witchery.rite.glyphictransform");
      RiteRegistry.addRecipe(68, 7, new RiteCallCreatures(64.0F, new Class[]{EntityPig.class, EntityChicken.class, EntityCow.class, EntitySheep.class, EntityMooshroom.class, EntityWolf.class, EntityOcelot.class}), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Items.milk_bucket), new ItemStack(Blocks.hay_block), new ItemStack(Items.apple), new ItemStack(Items.beef), new ItemStack(Items.fish), new ItemStack(Blocks.red_mushroom), new ItemStack(Items.carrot), new ItemStack(Items.wheat_seeds)}), new SacrificePower(6000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 40, 0)}).setUnlocalizedName("witchery.rite.callbeasts");
      RiteRegistry.addRecipe(69, 7, new RiteSetNBT(5, "WITCManifestDuration", 150, 25), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemSpectralDust.createStack(), Witchery.Items.GENERIC.itemMellifluousHunger.createStack(), Witchery.Items.GENERIC.itemNecroStone.createStack(), new ItemStack(Items.golden_pickaxe), new ItemStack(Witchery.Items.ARTHANA), new ItemStack(Items.gunpowder)}), new SacrificePower(5000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 16, 0)}).setUnlocalizedName("witchery.rite.manifest");
      RiteRegistry.addRecipe(70, 22, new RiteForestation(20, 8, 60, Blocks.sapling, 0), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Blocks.sapling, 1, 0), new ItemStack(Witchery.Blocks.WICKER_BUNDLE), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), Witchery.Items.GENERIC.itemBranchEnt.createStack()}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.forestation");
      RiteRegistry.addRecipe(71, 22, new RiteForestation(20, 8, 60, Blocks.sapling, 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Blocks.sapling, 1, 1), new ItemStack(Witchery.Blocks.WICKER_BUNDLE), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), Witchery.Items.GENERIC.itemBranchEnt.createStack()}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.forestation").setShowInBook(false);
      RiteRegistry.addRecipe(72, 22, new RiteForestation(20, 8, 60, Blocks.sapling, 2), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Blocks.sapling, 1, 2), new ItemStack(Witchery.Blocks.WICKER_BUNDLE), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), Witchery.Items.GENERIC.itemBranchEnt.createStack()}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.forestation").setShowInBook(false);
      RiteRegistry.addRecipe(73, 22, new RiteForestation(20, 8, 60, Blocks.sapling, 3), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Blocks.sapling, 1, 3), new ItemStack(Witchery.Blocks.WICKER_BUNDLE), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), Witchery.Items.GENERIC.itemBranchEnt.createStack()}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.forestation").setShowInBook(false);
      RiteRegistry.addRecipe(74, 22, new RiteForestation(20, 8, 60, Witchery.Blocks.SAPLING, 0), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Blocks.SAPLING, 1, 0), new ItemStack(Witchery.Blocks.WICKER_BUNDLE), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), Witchery.Items.GENERIC.itemBranchEnt.createStack()}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.forestation").setShowInBook(false);
      RiteRegistry.addRecipe(75, 22, new RiteForestation(20, 8, 60, Witchery.Blocks.SAPLING, 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Blocks.SAPLING, 1, 1), new ItemStack(Witchery.Blocks.WICKER_BUNDLE), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), Witchery.Items.GENERIC.itemBranchEnt.createStack()}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.forestation").setShowInBook(false);
      RiteRegistry.addRecipe(76, 22, new RiteForestation(20, 8, 60, Witchery.Blocks.SAPLING, 2), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Blocks.SAPLING, 1, 2), new ItemStack(Witchery.Blocks.WICKER_BUNDLE), Witchery.Items.GENERIC.itemBrewOfSprouting.createStack(), Witchery.Items.GENERIC.itemBranchEnt.createStack()}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.forestation").setShowInBook(false);
      RiteRegistry.addRecipe(77, 13, new RiteRaiseVolcano(8, 8), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Blocks.cobblestone), new ItemStack(Items.magma_cream), new ItemStack(Items.golden_sword)}), new SacrificeOptionalItem(Witchery.Items.GENERIC.itemWaystoneBound.createStack()), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 16)}).setUnlocalizedName("witchery.rite.volcano");
      RiteRegistry.addRecipe(78, 48, new RiteCurseCreature(true, "witcheryWakingNightmare", 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), Witchery.Items.GENERIC.itemMellifluousHunger.createStack(), Witchery.Items.GENERIC.itemTormentedTwine.createStack(), new ItemStack(Items.diamond), Witchery.Items.GENERIC.itemBrewGrotesque.createStack()}), new SacrificePower(10000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_IN_STROM), new Circle[]{new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.cursenightmare");
      RiteRegistry.addRecipe(79, 49, new RiteCurseCreature(false, "witcheryWakingNightmare", 1), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), new ItemStack(Items.golden_carrot), Witchery.Items.GENERIC.itemTormentedTwine.createStack(), Witchery.Items.GENERIC.itemBrewOfLove.createStack()}), new SacrificePower(2000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.curenightmare");
      RiteRegistry.addRecipe(80, 35, new RiteSummonItem(Witchery.Items.GENERIC.itemBrewOfSoaring.createStack(), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemInfusionBase.createStack(), Witchery.Items.GENERIC.itemBroom.createStack(), new ItemStack(Items.feather), new ItemStack(Witchery.Items.ARTHANA)}), new SacrificeLiving(EntityOwl.class), new SacrificePower(3000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0), new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.infusebrewsoaring");
      RiteRegistry.addRecipe(81, 35, new RiteSummonItem(Witchery.Items.GENERIC.itemBrewGrave.createStack(), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemInfusionBase.createStack(), new ItemStack(Items.bone), Witchery.Items.GENERIC.itemWeb.createStack(), Witchery.Items.GENERIC.itemNecroStone.createStack()}), new SacrificeLiving(EntityZombie.class), new SacrificePower(3000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0), new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.infusebrewgrave");
      RiteRegistry.addRecipe(82, 36, new RiteSummonItem(new ItemStack(Witchery.Items.SPECTRAL_STONE), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemNecroStone.createStack(), Witchery.Items.GENERIC.itemCongealedSpirit.createStack(), Witchery.Items.GENERIC.itemCondensedFear.createStack(), Witchery.Items.GENERIC.itemSpectralDust.createStack(), new ItemStack(Witchery.Items.BOLINE)}), new SacrificePower(6000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.spectralstone").setConsumeNecroStone();
      RiteRegistry.addRecipe(83, 1, new RiteSummonSpectralStone(5), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.SPECTRAL_STONE), Witchery.Items.GENERIC.itemSpectralDust.createStack(), new ItemStack(Witchery.Items.BOLINE)}), new SacrificePower(5000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.bindspectral");
      RiteRegistry.addRecipe(84, 1, new RiteBindSpiritsToFetish(5), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Blocks.FETISH_SCARECROW), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Witchery.Items.GENERIC.itemNecroStone.createStack(), new ItemStack(Witchery.Items.BOLINE)}), new SacrificePower(6000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.bindfetish");
      RiteRegistry.addRecipe(85, 1, new RiteBindSpiritsToFetish(5), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Blocks.FETISH_TREANT_IDOL), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Witchery.Items.GENERIC.itemNecroStone.createStack(), new ItemStack(Witchery.Items.BOLINE)}), new SacrificePower(6000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.bindfetish").setShowInBook(false);
      RiteRegistry.addRecipe(86, 1, new RiteBindSpiritsToFetish(5), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Blocks.FETISH_WITCHS_LADDER), Witchery.Items.GENERIC.itemAttunedStone.createStack(), Witchery.Items.GENERIC.itemNecroStone.createStack(), new ItemStack(Witchery.Items.BOLINE)}), new SacrificePower(6000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.bindfetish").setShowInBook(false);
      RiteRegistry.addRecipe(87, 26, new RiteSummonCreature(EntityImp.class, false), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemRefinedEvil.createStack(), Witchery.Items.GENERIC.itemInfernalBlood.createStack(), new ItemStack(Items.ender_pearl), Witchery.Items.GENERIC.itemAttunedStone.createStack()}), new SacrificePower(5000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.summonimp");
      RiteRegistry.addRecipe(88, 1, new RiteSummonItem(Witchery.Items.GENERIC.itemWaystonePlayerBound.createStack(), RiteSummonItem.Binding.ENTITY), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemWaystone.createStack(), Witchery.Items.GENERIC.itemEnderDew.createStack(), new ItemStack(Items.slime_ball), new ItemStack(Items.snowball)}), new SacrificePower(500.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.bindwaystonetoplayer");
      RiteRegistry.addRecipe(89, 1, new RiteSummonItem(Witchery.Items.GENERIC.itemWaystonePlayerBound.createStack(), RiteSummonItem.Binding.ENTITY), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemWaystone.createStack(), Witchery.Items.GENERIC.itemEnderDew.createStack(), new ItemStack(Items.slime_ball), Witchery.Items.GENERIC.itemIcyNeedle.createStack(), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.bindwaystonetoplayer");
      RiteRegistry.addRecipe(90, 1, new RiteSummonItem(new ItemStack(Witchery.Blocks.STATUE_OF_WORSHIP), RiteSummonItem.Binding.PLAYER), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Blocks.STATUE_OF_WORSHIP), Witchery.Items.GENERIC.itemBelladonnaFlower.createStack(), new ItemStack(Blocks.red_flower), new ItemStack(Blocks.yellow_flower)}), new SacrificePower(4000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.bindstatuetoplayer");
      RiteRegistry.addRecipe(91, 5, new RiteTeleportToWaystone(3), new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemWaystonePlayerBound.createStack()}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 16, 0)}).setUnlocalizedName("witchery.rite.teleporttowaystone");
      RiteRegistry.addRecipe(92, 48, new RiteCurseOfTheWolf(true), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack(), new ItemStack(Witchery.Blocks.WOLFHEAD, 1, 1), Witchery.Items.GENERIC.itemWolfsbane.createStack(), new ItemStack(Items.diamond), Witchery.Items.GENERIC.itemBrewGrotesque.createStack()}), new SacrificePower(10000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle[]{new Circle(0, 0, 28)}).setUnlocalizedName("witchery.rite.wolfcurse.book");
      RiteRegistry.addRecipe(93, 49, new RiteCurseOfTheWolf(false), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), new ItemStack(Witchery.Items.SILVER_SWORD), Witchery.Items.GENERIC.itemWolfsbane.createStack(), new ItemStack(Items.diamond), Witchery.Items.GENERIC.itemBrewOfLove.createStack()}), new SacrificePower(10000.0F, 20)}), EnumSet.of(RitualTraits.ONLY_AT_NIGHT), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.wolfcure.book");
      RiteRegistry.addRecipe(94, 49, new RiteRemoveVampirism(), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.TAGLOCK_KIT, 1, 1), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack(), new ItemStack(Witchery.Items.SILVER_SWORD), new ItemStack(Witchery.Items.SEEDS_GARLIC), new ItemStack(Items.diamond), Witchery.Items.GENERIC.itemBrewOfLove.createStack()}), new SacrificePower(10000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(16, 0, 0)}).setUnlocalizedName("witchery.rite.vampirecure.book");
      RiteRegistry.addRecipe(95, 35, new RiteSummonItem(new ItemStack(Witchery.Items.MIRROR), RiteSummonItem.Binding.NONE), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{Witchery.Items.GENERIC.itemBrewOfHollowTears.createStack(), new ItemStack(Items.gold_ingot), new ItemStack(Blocks.glass_pane)}), new SacrificePower(2000.0F, 20), new SacrificeLiving(EntityDemon.class)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(28, 0, 0)}).setUnlocalizedName("witchery.rite.infusionmirror");
      RiteRegistry.addRecipe(96, 28, new RiteSummonCreature(EntityReflection.class, false), new SacrificeMultiple(new Sacrifice[]{new SacrificeItem(new ItemStack[]{new ItemStack(Witchery.Items.MIRROR), Witchery.Items.GENERIC.itemEnderDew.createStack(), new ItemStack(Items.blaze_powder), Witchery.Items.GENERIC.itemQuartzSphere.createStack()}), new SacrificePower(5000.0F, 20)}), EnumSet.noneOf(RitualTraits.class), new Circle[]{new Circle(0, 0, 40)}).setUnlocalizedName("witchery.rite.summonreflection");
      double var36 = 0.05D;
      PredictionManager.instance().addPrediction(new PredictionFight(1, 13, 0.05D, "witchery.prediction.zombie", EntityZombie.class, false));
      PredictionManager.instance().addPrediction(new PredictionArrow(2, 13, 0.05D, "witchery.prediction.arrowhit"));
      PredictionManager.instance().addPrediction(new PredictionFight(3, 3, 0.05D, "witchery.prediction.ent", EntityEnt.class, false));
      PredictionManager.instance().addPrediction(new PredictionFall(4, 13, 0.05D, "witchery.prediction.fall"));
      PredictionManager.instance().addPrediction(new PredictionMultiMine(5, 8, 0.05D, "witchery.prediction.iron", 1212, 0.01D, Blocks.iron_ore, new ItemStack(Blocks.iron_ore), 8, 20));
      PredictionManager.instance().addPrediction(new PredictionMultiMine(6, 3, 0.05D, "witchery.prediction.diamond", 1208, 0.01D, Blocks.stone, new ItemStack(Items.diamond), 1, 1));
      PredictionManager.instance().addPrediction(new PredictionMultiMine(7, 3, 0.05D, "witchery.prediction.emerald", 1208, 0.01D, Blocks.stone, new ItemStack(Items.emerald), 1, 1));
      PredictionManager.instance().addPrediction(new PredictionBuriedTreasure(8, 2, 0.05D, "witchery.prediction.treasure", 1210, 0.01D, "mineshaftCorridor"));
      PredictionManager.instance().addPrediction(new PredictionFallInLove(9, 2, 0.05D, "witchery.prediction.love", 1210, 0.01D));
      PredictionManager.instance().addPrediction(new PredictionFight(10, 2, 0.05D, "witchery.prediction.bababad", EntityBabaYaga.class, false));
      PredictionManager.instance().addPrediction(new PredictionFight(11, 2, 0.05D, "witchery.prediction.babagood", EntityBabaYaga.class, true));
      PredictionManager.instance().addPrediction(new PredictionFight(12, 3, 0.05D, "witchery.prediction.friend", EntityWolf.class, true));
      PredictionManager.instance().addPrediction(new PredictionRescue(13, 13, 0.05D, "witchery.prediction.rescued", 1208, 0.01D, EntityOwl.class));
      PredictionManager.instance().addPrediction(new PredictionRescue(14, 13, 0.05D, "witchery.prediction.rescued", 1208, 0.01D, EntityWolf.class));
      PredictionManager.instance().addPrediction(new PredictionWet(15, 13, 0.05D, "witchery.prediction.wet"));
      PredictionManager.instance().addPrediction(new PredictionNetherTrip(16, 3, 0.05D, "witchery.prediction.tothenether"));
      PredictionManager.instance().addPrediction(new PredictionMultiMine(17, 13, 0.05D, "witchery.prediction.coal", 1208, 0.01D, Blocks.coal_ore, new ItemStack(Items.coal), 10, 20));
   }

   public void init() {
      ItemStack dust = Witchery.Items.GENERIC.itemSilverDust.createStack();
      ArrayList silverDust = OreDictionary.getOres("dustSilver");
      if(silverDust != null && !silverDust.isEmpty()) {
         GameRegistry.addShapelessRecipe(((ItemStack)silverDust.get(0)).copy(), new Object[]{dust, dust, dust, dust, dust, dust, dust, dust, dust});
      }

      ArrayList silverIngots = OreDictionary.getOres("ingotSilver");
      if(silverIngots != null && !silverIngots.isEmpty()) {
         GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Witchery.Items.SILVER_SWORD), new Object[]{"s", "s", "b", Character.valueOf('s'), "ingotSilver", Character.valueOf('b'), new ItemStack(Items.golden_sword)}));
         GameRegistry.addRecipe(new ShapedOreRecipe(Witchery.Items.GENERIC.itemBoltSilver.createStack(6), new Object[]{" s ", "bbb", "bbb", Character.valueOf('s'), "ingotSilver", Character.valueOf('b'), Witchery.Items.GENERIC.itemBoltStake.createStack()}));
         GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Witchery.Blocks.WOLFTRAP), new Object[]{"sns", "w#w", "sns", Character.valueOf('#'), new ItemStack(Witchery.Blocks.BEARTRAP), Character.valueOf('s'), "ingotSilver", Character.valueOf('n'), Witchery.Items.GENERIC.itemNullCatalyst.createStack(), Character.valueOf('w'), Witchery.Items.GENERIC.itemWolfsbane.createStack()}));
         Item[][] hunterItems = new Item[][]{{Witchery.Items.HUNTER_BOOTS, Witchery.Items.HUNTER_BOOTS_SILVERED}, {Witchery.Items.HUNTER_LEGS, Witchery.Items.HUNTER_LEGS_SILVERED}, {Witchery.Items.HUNTER_COAT, Witchery.Items.HUNTER_COAT_SILVERED}, {Witchery.Items.HUNTER_HAT, Witchery.Items.HUNTER_HAT_SILVERED}};
         Item[][] arr$ = hunterItems;
         int len$ = hunterItems.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Item[] hunterItem = arr$[i$];
            ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(hunterItem[1]), new Object[]{"dwd", "w#w", "dsd", Character.valueOf('#'), new ItemStack(hunterItem[0]), Character.valueOf('s'), new ItemStack(Items.string), Character.valueOf('w'), Witchery.Items.GENERIC.itemWolfsbane.createStack(), Character.valueOf('d'), "ingotSilver"}) {
               public ItemStack getCraftingResult(InventoryCrafting inv) {
                  ItemStack result = this.getRecipeOutput().copy();

                  for(int i = 0; i < inv.getSizeInventory(); ++i) {
                     ItemStack material = inv.getStackInSlot(i);
                     if(material != null && material.hasTagCompound()) {
                        result.setTagCompound((NBTTagCompound)material.stackTagCompound.copy());
                     }
                  }

                  return result;
               }
            };
            GameRegistry.addRecipe(recipe);
         }
      }

   }

   public void postInit() {
      if(Config.instance().smeltAllSaplingsToWoodAsh) {
         ArrayList saplingTypes = OreDictionary.getOres("treeSapling");
         Iterator i$ = saplingTypes.iterator();

         while(i$.hasNext()) {
            ItemStack stack = (ItemStack)i$.next();
            GameRegistry.addSmelting(stack, Witchery.Items.GENERIC.itemAshWood.createStack(), 0.0F);
         }
      }

   }

   private void addPlantMineRecipe(int damageValue, ItemStack plant, ItemStack brew) {
      GameRegistry.addRecipe(new ItemStack(Witchery.Blocks.TRAPPED_PLANT, 4, damageValue), new Object[]{"ccc", "bab", Character.valueOf('a'), plant, Character.valueOf('b'), new ItemStack(Blocks.stone_pressure_plate), Character.valueOf('c'), brew});
   }

   private static ShapedRecipes getShapedRecipe(ItemStack par1ItemStack, Object ... par2ArrayOfObj) {
      String s = "";
      int i = 0;
      int j = 0;
      int k = 0;
      if(par2ArrayOfObj[i] instanceof String[]) {
         String[] hashmap = (String[])((String[])((String[])par2ArrayOfObj[i++]));

         for(int aitemstack = 0; aitemstack < hashmap.length; ++aitemstack) {
            String shapedrecipes = hashmap[aitemstack];
            ++k;
            j = shapedrecipes.length();
            s = s + shapedrecipes;
         }
      } else {
         while(par2ArrayOfObj[i] instanceof String) {
            String var11 = (String)par2ArrayOfObj[i++];
            ++k;
            j = var11.length();
            s = s + var11;
         }
      }

      HashMap var10;
      for(var10 = new HashMap(); i < par2ArrayOfObj.length; i += 2) {
         Character var13 = (Character)par2ArrayOfObj[i];
         ItemStack var14 = null;
         if(par2ArrayOfObj[i + 1] instanceof Item) {
            var14 = new ItemStack((Item)par2ArrayOfObj[i + 1]);
         } else if(par2ArrayOfObj[i + 1] instanceof Block) {
            var14 = new ItemStack((Block)par2ArrayOfObj[i + 1], 1, 32767);
         } else if(par2ArrayOfObj[i + 1] instanceof ItemStack) {
            var14 = (ItemStack)par2ArrayOfObj[i + 1];
         }

         var10.put(var13, var14);
      }

      ItemStack[] var12 = new ItemStack[j * k];

      for(int var15 = 0; var15 < j * k; ++var15) {
         char c0 = s.charAt(var15);
         if(var10.containsKey(Character.valueOf(c0))) {
            var12[var15] = ((ItemStack)var10.get(Character.valueOf(c0))).copy();
         } else {
            var12[var15] = null;
         }
      }

      ShapedRecipes var16 = new ShapedRecipes(j, k, var12, par1ItemStack);
      return var16;
   }
}
