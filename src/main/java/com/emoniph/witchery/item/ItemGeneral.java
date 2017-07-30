package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockChalice;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.blocks.BlockCrystalBall;
import com.emoniph.witchery.blocks.BlockDreamCatcher;
import com.emoniph.witchery.blocks.BlockPlacedItem;
import com.emoniph.witchery.blocks.BlockWickerBundle;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.potions.PotionEnslaved;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.Shapeshift;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.entity.EntityBroom;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.entity.EntityDeathsHorse;
import com.emoniph.witchery.entity.EntityEye;
import com.emoniph.witchery.entity.EntityLordOfTorment;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.entity.EntitySummonedUndead;
import com.emoniph.witchery.entity.EntityTreefyd;
import com.emoniph.witchery.entity.EntityWitchProjectile;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrew;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.PlayerEffects;
import com.emoniph.witchery.infusion.infusions.InfusionInfernal;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.item.ItemGeneralContract;
import com.emoniph.witchery.item.brew.BrewFluid;
import com.emoniph.witchery.item.brew.BrewSolidifySpirit;
import com.emoniph.witchery.item.brew.BrewSoul;
import com.emoniph.witchery.network.PacketCamPos;
import com.emoniph.witchery.network.PacketParticles;
import com.emoniph.witchery.network.PacketPlayerStyle;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EffectSpiral;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ISpiralBlockAction;
import com.emoniph.witchery.util.MutableBlock;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TargetPointUtil;
import com.emoniph.witchery.util.TimeUtil;
import com.emoniph.witchery.util.TransformCreature;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemGeneral extends ItemBase {

   public final ArrayList subItems = new ArrayList();
   public final ArrayList weaves = new ArrayList();
   public final ItemGeneral.SubItem itemCandelabra;
   public final ItemGeneral.SubItem itemChaliceEmpty;
   public final ItemGeneral.SubItem itemChaliceFull;
   public final ItemGeneral.DreamWeave itemDreamMove;
   public final ItemGeneral.DreamWeave itemDreamDig;
   public final ItemGeneral.DreamWeave itemDreamEat;
   public final ItemGeneral.DreamWeave itemDreamNightmare;
   public final ItemGeneral.SubItem itemBoneNeedle;
   public final ItemGeneral.SubItem itemBroom;
   public final ItemGeneral.SubItem itemBroomEnchanted;
   public final ItemGeneral.SubItem itemAttunedStone;
   public final ItemGeneral.SubItem itemAttunedStoneCharged;
   public final ItemGeneral.SubItem itemWaystone;
   public final ItemGeneral.SubItem itemWaystoneBound;
   public final ItemGeneral.SubItem itemMutandis;
   public final ItemGeneral.SubItem itemMutandisExtremis;
   public final ItemGeneral.SubItem itemQuicklime;
   public final ItemGeneral.SubItem itemGypsum;
   public final ItemGeneral.SubItem itemAshWood;
   public final ItemGeneral.SubItem itemBelladonnaFlower;
   public final ItemGeneral.SubItem itemMandrakeRoot;
   private static final int DEMON_FOOD_DURATION = 2400;
   public final ItemGeneral.SubItem itemDemonHeart;
   public final ItemGeneral.SubItem itemBatWool;
   public final ItemGeneral.SubItem itemDogTongue;
   public final ItemGeneral.SubItem itemSoftClayJar;
   public final ItemGeneral.SubItem itemEmptyClayJar;
   public final ItemGeneral.SubItem itemFoulFume;
   public final ItemGeneral.SubItem itemDiamondVapour;
   public final ItemGeneral.SubItem itemOilOfVitriol;
   public final ItemGeneral.SubItem itemExhaleOfTheHornedOne;
   public final ItemGeneral.SubItem itemBreathOfTheGoddess;
   public final ItemGeneral.SubItem itemHintOfRebirth;
   public final ItemGeneral.SubItem itemWhiffOfMagic;
   public final ItemGeneral.SubItem itemReekOfMisfortune;
   public final ItemGeneral.SubItem itemOdourOfPurity;
   public final ItemGeneral.SubItem itemTearOfTheGoddess;
   public final ItemGeneral.SubItem itemRefinedEvil;
   public final ItemGeneral.SubItem itemDropOfLuck;
   public final ItemGeneral.SubItem itemRedstoneSoup;
   public final ItemGeneral.SubItem itemFlyingOintment;
   public final ItemGeneral.SubItem itemGhostOfTheLight;
   public final ItemGeneral.SubItem itemSoulOfTheWorld;
   public final ItemGeneral.SubItem itemSpiritOfOtherwhere;
   public final ItemGeneral.SubItem itemInfernalAnimus;
   public final ItemGeneral.SubItem itemBookOven;
   public final ItemGeneral.SubItem itemBookDistilling;
   public final ItemGeneral.SubItem itemBookCircleMagic;
   public final ItemGeneral.SubItem itemBookInfusions;
   public final ItemGeneral.SubItem itemOddPorkRaw;
   public final ItemGeneral.SubItem itemOddPorkCooked;
   public final ItemGeneral.SubItem itemDoorRowan;
   public final ItemGeneral.SubItem itemDoorAlder;
   public final ItemGeneral.SubItem itemDoorKey;
   public final ItemGeneral.SubItem itemRock;
   public final ItemGeneral.SubItem itemWeb;
   public final ItemGeneral.SubItem itemBrewOfVines;
   public final ItemGeneral.SubItem itemBrewOfWebs;
   public final ItemGeneral.SubItem itemBrewOfThorns;
   public final ItemGeneral.SubItem itemBrewOfInk;
   public final ItemGeneral.SubItem itemBrewOfSprouting;
   public final ItemGeneral.SubItem itemBrewOfErosion;
   public final ItemGeneral.SubItem itemRowanBerries;
   public final ItemGeneral.SubItem itemNecroStone;
   public final ItemGeneral.SubItem itemBrewOfRaising;
   public final ItemGeneral.SubItem itemSpectralDust;
   public final ItemGeneral.SubItem itemEnderDew;
   public final ItemGeneral.SubItem itemArtichoke;
   public final ItemGeneral.SubItem itemSeedsTreefyd;
   public final ItemGeneral.SubItem itemBrewGrotesque;
   public final ItemGeneral.SubItem itemImpregnatedLeather;
   public final ItemGeneral.SubItem itemFumeFilter;
   public final ItemGeneral.SubItem itemCreeperHeart;
   public final ItemGeneral.SubItem itemBrewOfLove;
   public final ItemGeneral.SubItem itemBrewOfIce;
   public final ItemGeneral.SubItem itemBrewOfTheDepths;
   public final ItemGeneral.SubItem itemIcyNeedle;
   public final ItemGeneral.SubItem itemFrozenHeart;
   public final ItemGeneral.SubItem itemInfernalBlood;
   public final ItemGeneral.SubItem itemBookHerbology;
   public final ItemGeneral.SubItem itemBranchEnt;
   public final ItemGeneral.SubItem itemMysticUnguent;
   public final ItemGeneral.SubItem itemDoorKeyring;
   public final ItemGeneral.SubItem itemBrewOfFrogsTongue;
   public final ItemGeneral.SubItem itemBrewOfCursedLeaping;
   public final ItemGeneral.SubItem itemBrewOfHitchcock;
   public final ItemGeneral.SubItem itemBrewOfInfection;
   public final ItemGeneral.SubItem itemOwletsWing;
   public final ItemGeneral.SubItem itemToeOfFrog;
   public final ItemGeneral.SubItem itemWormyApple;
   public final ItemGeneral.SubItem itemQuartzSphere;
   public final ItemGeneral.SubItem itemHappenstanceOil;
   public final ItemGeneral.SubItem itemSeerStone;
   public final ItemGeneral.SubItem itemBrewOfSleeping;
   public final ItemGeneral.SubItem itemBrewOfFlowingSpirit;
   public final ItemGeneral.SubItem itemBrewOfWasting;
   public final ItemGeneral.SubItem itemSleepingApple;
   public final ItemGeneral.SubItem itemDisturbedCotton;
   public final ItemGeneral.SubItem itemFancifulThread;
   public final ItemGeneral.SubItem itemTormentedTwine;
   public final ItemGeneral.SubItem itemGoldenThread;
   public final ItemGeneral.SubItem itemMellifluousHunger;
   public final ItemGeneral.DreamWeave itemDreamIntensity;
   public final ItemGeneral.SubItem itemPurifiedMilk;
   public final ItemGeneral.SubItem itemBookBiomes;
   public final ItemGeneral.SubItem itemBookWands;
   public final ItemGeneral.SubItem itemBatBall;
   public final ItemGeneral.SubItem itemBrewOfBats;
   public final ItemGeneral.SubItem itemCharmOfDisruptedDreams;
   public final ItemGeneral.SubItem itemWormwood;
   public final ItemGeneral.SubItem itemSubduedSpirit;
   public final ItemGeneral.SubItem itemFocusedWill;
   public final ItemGeneral.SubItem itemCondensedFear;
   public final ItemGeneral.SubItem itemBrewOfHollowTears;
   public final ItemGeneral.SubItem itemBrewOfSolidRock;
   public final ItemGeneral.SubItem itemBrewOfSolidDirt;
   public final ItemGeneral.SubItem itemBrewOfSolidSand;
   public final ItemGeneral.SubItem itemBrewOfSolidSandstone;
   public final ItemGeneral.SubItem itemBrewOfSolidErosion;
   public final ItemGeneral.SubItem itemInfusionBase;
   public final ItemGeneral.SubItem itemBrewOfSoaring;
   public final ItemGeneral.SubItem itemBrewGrave;
   public final ItemGeneral.SubItem itemBrewRevealing;
   public final ItemGeneral.SubItem itemBrewSubstitution;
   public final ItemGeneral.SubItem itemCongealedSpirit;
   public final ItemGeneral.SubItem itemBookBurning;
   public final ItemGeneral.SubItem itemGraveyardDust;
   public final ItemGeneral.SubItem itemBinkyHead;
   public final ItemGeneral.SubItem itemNullCatalyst;
   public final ItemGeneral.SubItem itemNullifiedLeather;
   public final ItemGeneral.SubItem itemBoltStake;
   public final ItemGeneral.SubItem itemBoltAntiMagic;
   public final ItemGeneral.SubItem itemBoltHoly;
   public final ItemGeneral.SubItem itemBoltSplitting;
   public final ItemGeneral.SubItem itemBrewSoulHunger;
   public final ItemGeneral.SubItem itemBrewSoulAnguish;
   public final ItemGeneral.SubItem itemBrewSoulFear;
   public final ItemGeneral.SubItem itemBrewSoulTorment;
   public final ItemGeneral.SubItem itemContractOwnership;
   public final ItemGeneral.SubItem itemContractTorment;
   public final ItemGeneral.SubItem itemContractBlaze;
   public final ItemGeneral.SubItem itemContractResistFire;
   public final ItemGeneral.SubItem itemContractEvaporate;
   public final ItemGeneral.SubItem itemContractFieryTouch;
   public final ItemGeneral.SubItem itemContractSmelting;
   public final ItemGeneral.SubItem itemWaystonePlayerBound;
   public final ItemGeneral.SubItem itemKobolditeDust;
   public final ItemGeneral.SubItem itemKobolditeNugget;
   public final ItemGeneral.SubItem itemKobolditeIngot;
   public final ItemGeneral.SubItem itemKobolditePentacle;
   public final ItemGeneral.SubItem itemDoorIce;
   public final ItemGeneral.SubItem itemAnnointingPaste;
   public final ItemGeneral.SubItem itemSubduedSpiritVillage;
   public final ItemGeneral.SubItem itemBoltSilver;
   public final ItemGeneral.SubItem itemWolfsbane;
   public final ItemGeneral.SubItem itemSilverDust;
   public final ItemGeneral.SubItem itemMuttonRaw;
   public final ItemGeneral.SubItem itemMuttonCooked;
   public final ItemGeneral.SubItem itemVampireBookPage;
   public final ItemGeneral.SubItem itemDarkCloth;
   public final ItemGeneral.SubItem itemWoodenStake;
   public final ItemGeneral.SubItem itemBloodWarm;
   public final ItemGeneral.SubItem itemBloodLiliths;
   public final ItemGeneral.SubItem itemHeartOfGold;
   @SideOnly(Side.CLIENT)
   private IIcon overlayGenericIcon;
   @SideOnly(Side.CLIENT)
   private IIcon overlayBroomIcon;
   @SideOnly(Side.CLIENT)
   private IIcon overlaySolidifierIcon;
   @SideOnly(Side.CLIENT)
   private IIcon overlayInfusedBrewIcon;


   public ItemGeneral() {
      this.itemCandelabra = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(0, "candelabra"), this.subItems);
      this.itemChaliceEmpty = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(1, "chalice"), this.subItems);
      this.itemChaliceFull = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(2, "chaliceFull"), this.subItems);
      this.itemDreamMove = ItemGeneral.DreamWeave.register(new ItemGeneral.DreamWeave(3, 0, "weaveMoveFast", Potion.moveSpeed, Potion.moveSlowdown, 7200, 0, 17, 10, null), this.subItems, this.weaves);
      this.itemDreamDig = ItemGeneral.DreamWeave.register(new ItemGeneral.DreamWeave(4, 1, "weaveDigFast", Potion.digSpeed, Potion.digSlowdown, 7200, 0, 17, 4, null), this.subItems, this.weaves);
      this.itemDreamEat = ItemGeneral.DreamWeave.register(new ItemGeneral.DreamWeave(5, 2, "weaveSaturation", Potion.field_76443_y, Potion.hunger, 4800, 0, 17, 16, null), this.subItems, this.weaves);
      this.itemDreamNightmare = ItemGeneral.DreamWeave.register(new ItemGeneral.DreamWeave(6, 3, "weaveNightmares", Potion.weakness, Potion.blindness, 1200, 0, 4, 4, null), this.subItems, this.weaves);
      this.itemBoneNeedle = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(7, "boneNeedle"), this.subItems);
      this.itemBroom = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(8, "broom"), this.subItems);
      this.itemBroomEnchanted = ItemGeneral.SubItem.register((new ItemGeneral.SubItem(9, "broomEnchanted", 3, null)).setEnchanted(true), this.subItems);
      this.itemAttunedStone = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(10, "attunedStone"), this.subItems);
      this.itemAttunedStoneCharged = ItemGeneral.SubItem.register((new ItemGeneral.SubItem(11, "attunedStoneCharged")).setEnchanted(true), this.subItems);
      this.itemWaystone = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(12, "waystone"), this.subItems);
      this.itemWaystoneBound = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(13, "waystoneBound", 1, false, null), this.subItems);
      this.itemMutandis = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(14, "mutandis"), this.subItems);
      this.itemMutandisExtremis = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(15, "mutandisExtremis"), this.subItems);
      this.itemQuicklime = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(16, "quicklime"), this.subItems);
      this.itemGypsum = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(17, "gypsum"), this.subItems);
      this.itemAshWood = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(18, "ashWood"), this.subItems);
      this.itemBelladonnaFlower = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(21, "belladonna"), this.subItems);
      this.itemMandrakeRoot = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(22, "mandrakeRoot"), this.subItems);
      this.itemDemonHeart = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(23, "demonHeart", 2, EnumAction.eat, new PotionEffect[]{new PotionEffect(Potion.field_76434_w.id, 2400, 4), new PotionEffect(Potion.regeneration.id, 2400, 1), new PotionEffect(Potion.damageBoost.id, 2400, 2), new PotionEffect(Potion.moveSpeed.id, 2400, 2), new PotionEffect(Potion.fireResistance.id, 2400, 2), new PotionEffect(Potion.confusion.id, 2400), new PotionEffect(Potion.hunger.id, 3600, 1)}), this.subItems);
      this.itemBatWool = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(24, "batWool"), this.subItems);
      this.itemDogTongue = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(25, "dogTongue"), this.subItems);
      this.itemSoftClayJar = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(26, "clayJarSoft"), this.subItems);
      this.itemEmptyClayJar = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(27, "clayJar"), this.subItems);
      this.itemFoulFume = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(28, "foulFume"), this.subItems);
      this.itemDiamondVapour = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(29, "diamondVapour"), this.subItems);
      this.itemOilOfVitriol = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(30, "oilOfVitriol"), this.subItems);
      this.itemExhaleOfTheHornedOne = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(31, "exhaleOfTheHornedOne"), this.subItems);
      this.itemBreathOfTheGoddess = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(32, "breathOfTheGoddess"), this.subItems);
      this.itemHintOfRebirth = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(33, "hintOfRebirth"), this.subItems);
      this.itemWhiffOfMagic = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(34, "whiffOfMagic"), this.subItems);
      this.itemReekOfMisfortune = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(35, "reekOfMisfortune"), this.subItems);
      this.itemOdourOfPurity = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(36, "odourOfPurity"), this.subItems);
      this.itemTearOfTheGoddess = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(37, "tearOfTheGoddess"), this.subItems);
      this.itemRefinedEvil = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(38, "refinedEvil"), this.subItems);
      this.itemDropOfLuck = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(39, "dropOfLuck"), this.subItems);
      this.itemRedstoneSoup = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(40, "redstoneSoup", 1, new PotionEffect[]{new PotionEffect(Potion.field_76434_w.id, 2400, 1)}), this.subItems);
      this.itemFlyingOintment = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(41, "flyingOintment", 2, new PotionEffect[]{new PotionEffect(Potion.poison.id, 1200, 2)}), this.subItems);
      this.itemGhostOfTheLight = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(42, "ghostOfTheLight", 2, new PotionEffect[]{new PotionEffect(Potion.poison.id, 1200, 1)}), this.subItems);
      this.itemSoulOfTheWorld = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(43, "soulOfTheWorld", 2, new PotionEffect[]{new PotionEffect(Potion.poison.id, 1200, 1)}), this.subItems);
      this.itemSpiritOfOtherwhere = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(44, "spiritOfOtherwhere", 2, new PotionEffect[]{new PotionEffect(Potion.poison.id, 1200, 1)}), this.subItems);
      this.itemInfernalAnimus = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(45, "infernalAnimus", 2, new PotionEffect[]{new PotionEffect(Potion.poison.id, 1200, 1), new PotionEffect(Potion.wither.id, 3600, 2)}), this.subItems);
      this.itemBookOven = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(46, "bookOven"), this.subItems);
      this.itemBookDistilling = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(47, "bookDistilling"), this.subItems);
      this.itemBookCircleMagic = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(48, "bookCircleMagic"), this.subItems);
      this.itemBookInfusions = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(49, "bookInfusions"), this.subItems);
      this.itemOddPorkRaw = ItemGeneral.SubItem.register(new ItemGeneral.Edible(50, "oddPorkchopRaw", 3, 0.3F, true, null), this.subItems);
      this.itemOddPorkCooked = ItemGeneral.SubItem.register(new ItemGeneral.Edible(51, "oddPorkchopCooked", 8, 0.8F, true, null), this.subItems);
      this.itemDoorRowan = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(52, "doorRowan"), this.subItems);
      this.itemDoorAlder = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(53, "doorAlder"), this.subItems);
      this.itemDoorKey = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(54, "doorKey"), this.subItems);
      this.itemRock = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(55, "rock"), this.subItems);
      this.itemWeb = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(56, "web"), this.subItems);
      this.itemBrewOfVines = ItemGeneral.SubItem.register(new ItemGeneral.Brew(57, "brewVines"), this.subItems);
      this.itemBrewOfWebs = ItemGeneral.SubItem.register(new ItemGeneral.Brew(58, "brewWeb"), this.subItems);
      this.itemBrewOfThorns = ItemGeneral.SubItem.register(new ItemGeneral.Brew(59, "brewThorns"), this.subItems);
      this.itemBrewOfInk = ItemGeneral.SubItem.register(new ItemGeneral.Brew(60, "brewInk"), this.subItems);
      this.itemBrewOfSprouting = ItemGeneral.SubItem.register(new ItemGeneral.Brew(61, "brewSprouting"), this.subItems);
      this.itemBrewOfErosion = ItemGeneral.SubItem.register(new ItemGeneral.Brew(62, "brewErosion"), this.subItems);
      this.itemRowanBerries = ItemGeneral.SubItem.register(new ItemGeneral.Edible(63, "berriesRowan", 1, 6.0F, false, null), this.subItems);
      this.itemNecroStone = ItemGeneral.SubItem.register((new ItemGeneral.SubItem(64, "necroStone", 1, null)).setEnchanted(true), this.subItems);
      this.itemBrewOfRaising = ItemGeneral.SubItem.register(new ItemGeneral.Brew(65, "brewRaising"), this.subItems);
      this.itemSpectralDust = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(66, "spectralDust"), this.subItems);
      this.itemEnderDew = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(67, "enderDew"), this.subItems);
      this.itemArtichoke = ItemGeneral.SubItem.register(new ItemGeneral.Edible(69, "artichoke", 20, 0.0F, false, null), this.subItems);
      this.itemSeedsTreefyd = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(70, "seedsTreefyd"), this.subItems);
      this.itemBrewGrotesque = ItemGeneral.SubItem.register((new ItemGeneral.Drinkable(71, "brewGrotesque", 1, new PotionEffect[0])).setPotion(true), this.subItems);
      this.itemImpregnatedLeather = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(72, "impregnatedLeather"), this.subItems);
      this.itemFumeFilter = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(73, "fumeFilter"), this.subItems);
      this.itemCreeperHeart = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(74, "creeperHeart", 1, EnumAction.eat, new PotionEffect[]{new PotionEffect(Potion.fireResistance.id, 20, 0)}), this.subItems);
      this.itemBrewOfLove = ItemGeneral.SubItem.register(new ItemGeneral.Brew(75, "brewLove"), this.subItems);
      this.itemBrewOfIce = ItemGeneral.SubItem.register(new ItemGeneral.Brew(76, "brewIce"), this.subItems);
      this.itemBrewOfTheDepths = ItemGeneral.SubItem.register((new ItemGeneral.Drinkable(77, "brewDepths", 1, new PotionEffect[0])).setPotion(true), this.subItems);
      this.itemIcyNeedle = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(78, "icyNeedle"), this.subItems);
      this.itemFrozenHeart = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(79, "frozenHeart", 1, EnumAction.eat, new PotionEffect[]{new PotionEffect(Potion.fireResistance.id, 20, 0)}), this.subItems);
      this.itemInfernalBlood = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(80, "infernalBlood"), this.subItems);
      this.itemBookHerbology = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(81, "bookHerbology"), this.subItems);
      this.itemBranchEnt = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(82, "entbranch"), this.subItems);
      this.itemMysticUnguent = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(83, "mysticunguent", 2, new PotionEffect[]{new PotionEffect(Potion.weakness.id, 1200, 1)}), this.subItems);
      this.itemDoorKeyring = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(84, "doorKeyring"), this.subItems);
      this.itemBrewOfFrogsTongue = ItemGeneral.SubItem.register(new ItemGeneral.Brew(85, "brewFrogsTongue"), this.subItems);
      this.itemBrewOfCursedLeaping = ItemGeneral.SubItem.register(new ItemGeneral.Brew(86, "brewCursedLeaping"), this.subItems);
      this.itemBrewOfHitchcock = ItemGeneral.SubItem.register(new ItemGeneral.Brew(87, "brewHitchcock"), this.subItems);
      this.itemBrewOfInfection = ItemGeneral.SubItem.register(new ItemGeneral.Brew(88, "brewInfection"), this.subItems);
      this.itemOwletsWing = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(89, "owletsWing"), this.subItems);
      this.itemToeOfFrog = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(90, "toeOfFrog"), this.subItems);
      this.itemWormyApple = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(91, "appleWormy", 0, EnumAction.eat, new PotionEffect[]{new PotionEffect(Potion.poison.id, 60)}), this.subItems);
      this.itemQuartzSphere = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(92, "quartzSphere"), this.subItems);
      this.itemHappenstanceOil = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(93, "happenstanceOil", 1, new PotionEffect[]{new PotionEffect(Potion.nightVision.id, 1200)}), this.subItems);
      this.itemSeerStone = ItemGeneral.SubItem.register((new ItemGeneral.SubItem(94, "seerStone", 1, null)).setEnchanted(true), this.subItems);
      this.itemBrewOfSleeping = ItemGeneral.SubItem.register((new ItemGeneral.Drinkable(95, "brewSleep", 1, new PotionEffect[0])).setPotion(true), this.subItems);
      this.itemBrewOfFlowingSpirit = ItemGeneral.SubItem.register(new BrewFluid(96, "brewFlowingSpirit", Witchery.Fluids.FLOWING_SPIRIT), this.subItems);
      this.itemBrewOfWasting = ItemGeneral.SubItem.register(new ItemGeneral.Brew(97, "brewWasting"), this.subItems);
      this.itemSleepingApple = ItemGeneral.SubItem.register(new ItemGeneral.Edible(98, "sleepingApple", 3, 3.0F, false, true, null), this.subItems);
      this.itemDisturbedCotton = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(99, "disturbedCotton"), this.subItems);
      this.itemFancifulThread = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(100, "fancifulThread"), this.subItems);
      this.itemTormentedTwine = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(101, "tormentedTwine"), this.subItems);
      this.itemGoldenThread = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(102, "goldenThread"), this.subItems);
      this.itemMellifluousHunger = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(103, "mellifluousHunger"), this.subItems);
      this.itemDreamIntensity = ItemGeneral.DreamWeave.register(new ItemGeneral.DreamWeave(104, 4, "weaveIntensity", Potion.nightVision, Potion.blindness, 300, 0, 17, 22, null), this.subItems, this.weaves);
      this.itemPurifiedMilk = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(105, "purifiedMilk", 1, new PotionEffect[0]), this.subItems);
      this.itemBookBiomes = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(106, "bookBiomes"), this.subItems);
      this.itemBookWands = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(107, "bookWands"), this.subItems);
      this.itemBatBall = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(108, "batBall"), this.subItems);
      this.itemBrewOfBats = ItemGeneral.SubItem.register(new ItemGeneral.Brew(109, "brewBats"), this.subItems);
      this.itemCharmOfDisruptedDreams = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(110, "charmDisruptedDreams"), this.subItems);
      this.itemWormwood = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(111, "wormwood"), this.subItems);
      this.itemSubduedSpirit = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(112, "subduedSpirit"), this.subItems);
      this.itemFocusedWill = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(113, "focusedWill"), this.subItems);
      this.itemCondensedFear = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(114, "condensedFear"), this.subItems);
      this.itemBrewOfHollowTears = ItemGeneral.SubItem.register(new BrewFluid(115, "brewHollowTears", Witchery.Fluids.HOLLOW_TEARS), this.subItems);
      this.itemBrewOfSolidRock = ItemGeneral.SubItem.register(new BrewSolidifySpirit(116, "brewSolidStone", Blocks.stone), this.subItems);
      this.itemBrewOfSolidDirt = ItemGeneral.SubItem.register(new BrewSolidifySpirit(117, "brewSolidDirt", Blocks.dirt), this.subItems);
      this.itemBrewOfSolidSand = ItemGeneral.SubItem.register(new BrewSolidifySpirit(118, "brewSolidSand", Blocks.sand), this.subItems);
      this.itemBrewOfSolidSandstone = ItemGeneral.SubItem.register(new BrewSolidifySpirit(119, "brewSolidSandstone", Blocks.sandstone), this.subItems);
      this.itemBrewOfSolidErosion = ItemGeneral.SubItem.register(new BrewSolidifySpirit(120, "brewSolidErosion", (Block)null), this.subItems);
      this.itemInfusionBase = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(121, "infusionBase", 2, new PotionEffect[]{new PotionEffect(Potion.wither.id, 200, 3)}), this.subItems);
      this.itemBrewOfSoaring = ItemGeneral.SubItem.register(new InfusedBrew(122, "brewSoaring", InfusedBrewEffect.Soaring), this.subItems);
      this.itemBrewGrave = ItemGeneral.SubItem.register(new InfusedBrew(123, "brewGrave", InfusedBrewEffect.Grave), this.subItems);
      this.itemBrewRevealing = ItemGeneral.SubItem.register(new ItemGeneral.Brew(124, "brewRevealing") {
         public ItemGeneral.Brew.BrewResult onImpact(World world, EntityLivingBase thrower, MovingObjectPosition mop, boolean enhanced, double brewX, double brewY, double brewZ, AxisAlignedBB brewBounds) {
            double RADIUS = enhanced?8.0D:5.0D;
            double RADIUS_SQ = RADIUS * RADIUS;
            AxisAlignedBB areaOfEffect = brewBounds.expand(RADIUS, RADIUS, RADIUS);
            List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, areaOfEffect);
            if(entities != null && !entities.isEmpty()) {
               Iterator entityIterator = entities.iterator();

               while(entityIterator.hasNext()) {
                  EntityLivingBase entityLiving = (EntityLivingBase)entityIterator.next();
                  double distanceSq = entityLiving.getDistanceSq(brewX, brewY, brewZ);
                  if(distanceSq <= RADIUS_SQ) {
                     double var10000 = 1.0D - Math.sqrt(distanceSq) / RADIUS;
                     if(entityLiving == mop.entityHit) {
                        double scalingFactor = 1.0D;
                     }

                     if(entityLiving.isPotionActive(Potion.invisibility)) {
                        entityLiving.removePotionEffect(Potion.invisibility.id);
                     }

                     if(entityLiving instanceof EntityPlayerMP && entityLiving.isInvisible()) {
                        entityLiving.setInvisible(false);
                     }

                     if(entityLiving instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer)entityLiving;
                        ExtendedPlayer playerEx = ExtendedPlayer.get(player);
                        if(playerEx != null && playerEx.getCreatureType() == TransformCreature.PLAYER) {
                           ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, player, 0.5D, 2.0D, 16);
                           Shapeshift.INSTANCE.shiftTo(player, TransformCreature.NONE);
                        }
                     }

                     if(entityLiving instanceof EntitySummonedUndead && ((EntitySummonedUndead)entityLiving).isObscured()) {
                        ((EntitySummonedUndead)entityLiving).setObscured(false);
                     }
                  }
               }
            }

            return ItemGeneral.Brew.BrewResult.SHOW_EFFECT;
         }
      }, this.subItems);
      this.itemBrewSubstitution = ItemGeneral.SubItem.register(new ItemGeneral.Brew(125, "brewSubstitution") {
         public ItemGeneral.Brew.BrewResult onImpact(World world, EntityLivingBase thrower, final MovingObjectPosition mop, boolean enhanced, double brewX, double brewY, double brewZ, AxisAlignedBB brewBounds) {
            if(mop != null && mop.typeOfHit != MovingObjectType.ENTITY) {
               int RADIUS = enhanced?6:4;
               final double RADIUS_SQ = (double)(RADIUS * RADIUS);
               AxisAlignedBB areaOfEffect = brewBounds.expand((double)RADIUS, (double)RADIUS, (double)RADIUS);
               List entities = world.getEntitiesWithinAABB(EntityItem.class, areaOfEffect);
               if(entities != null && !entities.isEmpty()) {
                  final ArrayList items = new ArrayList();
                  Iterator entityIterator = entities.iterator();

                  while(entityIterator.hasNext()) {
                     EntityItem refBlock = (EntityItem)entityIterator.next();
                     double refMeta = refBlock.getDistanceSq(brewX, brewY, brewZ);
                     if(refMeta <= RADIUS_SQ) {
                        ItemStack stack = refBlock.getEntityItem();
                        if(stack.getItem() instanceof ItemBlock) {
                           items.add(refBlock);
                        }
                     }
                  }

                  final Block refBlock1 = BlockUtil.getBlock(world, mop.blockX, mop.blockY, mop.blockZ);
                  final int refMeta1 = BlockUtil.getBlockMetadata(world, mop.blockX, mop.blockY, mop.blockZ);
                  if(items.size() > 0 && refBlock1 != null && BlockProtect.canBreak(refBlock1, world)) {
                     (new EffectSpiral(new ISpiralBlockAction() {

                        int stackIndex = 0;
                        int subCount = 0;

                        public void onSpiralActionStart(World world, int posX, int posY, int posZ) {}
                        public boolean onSpiralBlockAction(World world, int posX, int posY, int posZ) {
                           if(Coord.distanceSq((double)mop.blockX, (double)mop.blockY, (double)mop.blockZ, (double)posX, (double)posY, (double)posZ) < RADIUS_SQ) {
                              boolean found = false;
                              if(BlockUtil.getBlock(world, posX, posY, posZ) == refBlock1 && BlockUtil.isReplaceableBlock(world, posX, posY + 1, posZ)) {
                                 found = true;
                              } else if(BlockUtil.getBlock(world, posX, posY + 1, posZ) == refBlock1 && BlockUtil.isReplaceableBlock(world, posX, posY + 2, posZ)) {
                                 ++posY;
                                 found = true;
                              } else if(BlockUtil.getBlock(world, posX, posY - 1, posZ) == refBlock1 && BlockUtil.isReplaceableBlock(world, posX, posY, posZ)) {
                                 --posY;
                                 found = true;
                              } else if(BlockUtil.getBlock(world, posX, posY + 2, posZ) == refBlock1 && BlockUtil.isReplaceableBlock(world, posX, posY + 3, posZ)) {
                                 posY += 2;
                                 found = true;
                              } else if(BlockUtil.getBlock(world, posX, posY - 2, posZ) == refBlock1 && BlockUtil.isReplaceableBlock(world, posX, posY - 1, posZ)) {
                                 posY -= 2;
                                 found = true;
                              }

                              if(found) {
                                 ++this.subCount;
                                 ItemStack stack = ((EntityItem)items.get(this.stackIndex)).getEntityItem();
                                 BlockUtil.setBlock(world, posX, posY, posZ, (ItemBlock)stack.getItem(), stack.getItemDamage(), 3);
                                 ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, world, (double)posX, (double)posY, (double)posZ, 1.0D, 1.0D, 16);
                                 if(--stack.stackSize == 0) {
                                    ((EntityItem)items.get(this.stackIndex)).setDead();
                                    ++this.stackIndex;
                                 }
                              }
                           }

                           return this.stackIndex < items.size();
                        }
                        public void onSpiralActionStop(World world, int posX, int posY, int posZ) {
                           while(this.subCount > 0) {
                              int quantity = this.subCount > 64?64:this.subCount;
                              this.subCount -= quantity;
                              world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)posX, 1.5D + (double)posY, 0.5D + (double)posZ, new ItemStack(refBlock1, quantity, refMeta1)));
                           }

                        }
                     })).apply(world, mop.blockX, mop.blockY, mop.blockZ, RADIUS * 2, RADIUS * 2);
                     return ItemGeneral.Brew.BrewResult.SHOW_EFFECT;
                  }
               }

               return ItemGeneral.Brew.BrewResult.DROP_ITEM;
            } else {
               return ItemGeneral.Brew.BrewResult.DROP_ITEM;
            }
         }
      }, this.subItems);
      this.itemCongealedSpirit = ItemGeneral.SubItem.register((new ItemGeneral.Drinkable(126, "brewCongealedSpirit", 1, new PotionEffect[]{new PotionEffect(Potion.nightVision.id, TimeUtil.secsToTicks(30), 1)})).setPotion(true), this.subItems);
      this.itemBookBurning = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(127, "bookBurning"), this.subItems);
      this.itemGraveyardDust = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(128, "graveyardDust"), this.subItems);
      this.itemBinkyHead = ItemGeneral.SubItem.register(new ItemGeneral.BoltType(129, "binkyhead", null), this.subItems);
      this.itemNullCatalyst = ItemGeneral.SubItem.register(new ItemGeneral.BoltType(130, "nullcatalyst", null), this.subItems);
      this.itemNullifiedLeather = ItemGeneral.SubItem.register(new ItemGeneral.BoltType(131, "nullifiedleather", null), this.subItems);
      this.itemBoltStake = ItemGeneral.SubItem.register(new ItemGeneral.BoltType(132, "boltStake", null), this.subItems);
      this.itemBoltAntiMagic = ItemGeneral.SubItem.register(new ItemGeneral.BoltType(133, "boltAntiMagic", null), this.subItems);
      this.itemBoltHoly = ItemGeneral.SubItem.register(new ItemGeneral.BoltType(134, "boltHoly", null), this.subItems);
      this.itemBoltSplitting = ItemGeneral.SubItem.register(new ItemGeneral.BoltType(135, "boltSplitting", null), this.subItems);
      this.itemBrewSoulHunger = ItemGeneral.SubItem.register(new BrewSoul(136, "brewSoulHunger", EffectRegistry.CarnosaDiem), this.subItems);
      this.itemBrewSoulAnguish = ItemGeneral.SubItem.register(new BrewSoul(137, "brewSoulAnguish", EffectRegistry.Ignianima), this.subItems);
      this.itemBrewSoulFear = ItemGeneral.SubItem.register(new BrewSoul(138, "brewSoulFear", EffectRegistry.MORSMORDRE), this.subItems);
      this.itemBrewSoulTorment = ItemGeneral.SubItem.register(new BrewSoul(139, "brewSoulTorment", EffectRegistry.Tormentum), this.subItems);
      this.itemContractOwnership = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(140, "contract"), this.subItems);
      this.itemContractTorment = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(141, "contractTorment"), this.subItems);
      this.itemContractBlaze = ItemGeneral.SubItem.register(new ItemGeneralContract(142, "contractBlaze") {
         public boolean activate(ItemStack stack, EntityLivingBase targetEntity) {
            EntityCreature blaze = InfusionInfernal.spawnCreature(targetEntity.worldObj, EntityBlaze.class, targetEntity, 1, 2, ParticleEffect.FLAME, SoundEffect.RANDOM_FIZZ);
            if(blaze != null) {
               blaze.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
               blaze.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
               return true;
            } else {
               return false;
            }
         }
      }, this.subItems);
      this.itemContractResistFire = ItemGeneral.SubItem.register(new ItemGeneralContract(143, "contractResistFire") {
         public boolean activate(ItemStack stack, EntityLivingBase targetEntity) {
            targetEntity.addPotionEffect(new PotionEffect(Potion.fireResistance.id, TimeUtil.minsToTicks(15)));
            return true;
         }
      }, this.subItems);
      this.itemContractEvaporate = ItemGeneral.SubItem.register(new ItemGeneralContract(144, "contractEvaporate") {
         public boolean activate(ItemStack stack, EntityLivingBase targetEntity) {
            if(targetEntity instanceof EntityPlayer) {
               PlayerEffects.IMP_EVAPORATION.applyTo((EntityPlayer)targetEntity, TimeUtil.minsToTicks(10));
               return true;
            } else {
               return false;
            }
         }
      }, this.subItems);
      this.itemContractFieryTouch = ItemGeneral.SubItem.register(new ItemGeneralContract(145, "contractFieryTouch") {
         public boolean activate(ItemStack stack, EntityLivingBase targetEntity) {
            if(targetEntity instanceof EntityPlayer) {
               PlayerEffects.IMP_FIRE_TOUCH.applyTo((EntityPlayer)targetEntity, TimeUtil.minsToTicks(10));
               return true;
            } else {
               return false;
            }
         }
      }, this.subItems);
      this.itemContractSmelting = ItemGeneral.SubItem.register(new ItemGeneralContract(146, "contractSmelting") {
         public boolean activate(ItemStack stack, EntityLivingBase targetEntity) {
            if(targetEntity instanceof EntityPlayer) {
               PlayerEffects.IMP_METLING_TOUCH.applyTo((EntityPlayer)targetEntity, TimeUtil.minsToTicks(10));
               return true;
            } else {
               return false;
            }
         }
      }, this.subItems);
      this.itemWaystonePlayerBound = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(147, "waystoneCreatureBound", 1, false, null), this.subItems);
      this.itemKobolditeDust = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(148, "kobolditedust"), this.subItems);
      this.itemKobolditeNugget = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(149, "kobolditenugget"), this.subItems);
      this.itemKobolditeIngot = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(150, "kobolditeingot"), this.subItems);
      this.itemKobolditePentacle = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(151, "pentacle"), this.subItems);
      this.itemDoorIce = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(152, "doorIce"), this.subItems);
      this.itemAnnointingPaste = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(153, "annointingPaste"), this.subItems);
      this.itemSubduedSpiritVillage = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(154, "subduedSpiritVillage"), this.subItems);
      this.itemBoltSilver = ItemGeneral.SubItem.register(new ItemGeneral.BoltType(155, "boltSilver", null), this.subItems);
      this.itemWolfsbane = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(156, "wolfsbane"), this.subItems);
      this.itemSilverDust = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(157, "silverdust"), this.subItems);
      this.itemMuttonRaw = ItemGeneral.SubItem.register(new ItemGeneral.Edible(158, "muttonraw", 2, 0.2F, true, null), this.subItems);
      this.itemMuttonCooked = ItemGeneral.SubItem.register(new ItemGeneral.Edible(159, "muttoncooked", 6, 0.8F, true, null), this.subItems);
      this.itemVampireBookPage = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(160, "vbookPage"), this.subItems);
      this.itemDarkCloth = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(161, "darkCloth"), this.subItems);
      this.itemWoodenStake = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(162, "stake"), this.subItems);
      this.itemBloodWarm = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(163, "warmBlood", 1, new PotionEffect[0]) {
         public void onDrunk(World world, EntityPlayer player, ItemStack itemstack) {
            if(!world.isRemote) {
               ExtendedPlayer playerEx = ExtendedPlayer.get(player);
               if(playerEx.isVampire()) {
                  playerEx.increaseBloodPower(500);
               } else {
                  player.addPotionEffect(new PotionEffect(Potion.hunger.id, TimeUtil.secsToTicks(6)));
               }
            }

         }
      }, this.subItems);
      this.itemBloodLiliths = ItemGeneral.SubItem.register(new ItemGeneral.Drinkable(164, "lilithsBlood", 2, new PotionEffect[0]) {
         public void onDrunk(World world, EntityPlayer player, ItemStack itemstack) {
            if(!world.isRemote) {
               ExtendedPlayer playerEx = ExtendedPlayer.get(player);
               int level = playerEx.getVampireLevel();
               if(level == 10) {
                  playerEx.increaseBloodPower(2000);
               } else {
                  playerEx.increaseVampireLevel();
               }
            }

         }
      }, this.subItems);
      this.itemHeartOfGold = ItemGeneral.SubItem.register(new ItemGeneral.SubItem(165, "heartofgold"), this.subItems);
      this.setMaxDamage(0);
      this.setMaxStackSize(64);
      this.setHasSubtypes(true);
   }

   public boolean isBrew(int itemDamage) {
      return this.subItems.get(itemDamage) instanceof ItemGeneral.Brew;
   }

   public boolean isBrew(ItemStack stack) {
      return stack != null && stack.getItem() == this && this.isBrew(stack.getItemDamage());
   }

   public String getUnlocalizedName(ItemStack itemStack) {
      return this.getUnlocalizedName() + "." + ((ItemGeneral.SubItem)this.subItems.get(itemStack.getItemDamage())).unlocalizedName;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      String defaultIconName = this.getIconString();
      Iterator i$ = this.subItems.iterator();

      while(i$.hasNext()) {
         ItemGeneral.SubItem subItem = (ItemGeneral.SubItem)i$.next();
         if(subItem != null) {
            subItem.registerIcon(iconRegister, this);
         }
      }

      this.overlayGenericIcon = iconRegister.registerIcon(defaultIconName + ".genericoverlay");
      this.overlayBroomIcon = iconRegister.registerIcon(defaultIconName + ".broomOverlay");
      this.overlaySolidifierIcon = iconRegister.registerIcon(defaultIconName + ".brewSolidOverlay");
      this.overlayInfusedBrewIcon = iconRegister.registerIcon(defaultIconName + ".brewInfusedOverlay");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int damage) {
      return ((ItemGeneral.SubItem)this.subItems.get(Math.max(damage, 0))).icon;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
      return pass == 0?super.getIconFromDamageForRenderPass(damage, pass):(((ItemGeneral.SubItem)this.subItems.get(damage)).isSolidifier()?this.overlaySolidifierIcon:(((ItemGeneral.SubItem)this.subItems.get(damage)).isInfused()?this.overlayInfusedBrewIcon:(((ItemGeneral.SubItem)this.subItems.get(damage)).isPotion()?Items.potionitem.getIconFromDamageForRenderPass(this.subItems.get(damage) instanceof ItemGeneral.Brew?16384:0, pass):(this.itemBroomEnchanted.damageValue == damage?this.overlayBroomIcon:this.overlayGenericIcon))));
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int pass) {
      if(this.itemBroomEnchanted.isMatch(stack) && pass != 0) {
         int j = this.getBroomItemColor(stack);
         if(j > 15) {
            return super.getColorFromItemStack(stack, pass);
         } else {
            if(j < 0) {
               j = 12;
            }

            return ItemDye.field_150922_c[BlockColored.func_150031_c(j)];
         }
      } else {
         return super.getColorFromItemStack(stack, pass);
      }
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   public boolean hasEffect(ItemStack stack, int pass) {
      return pass == 0 && ((ItemGeneral.SubItem)this.subItems.get(stack.getItemDamage())).isEnchanted() || this.itemBroomEnchanted.isMatch(stack) || this.itemSubduedSpirit.isMatch(stack) || this.itemSubduedSpiritVillage.isMatch(stack);
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs tab, List itemList) {
      Iterator i$ = this.subItems.iterator();

      while(i$.hasNext()) {
         ItemGeneral.SubItem subItem = (ItemGeneral.SubItem)i$.next();
         if(subItem != null && subItem.showInCreativeTab) {
            itemList.add(subItem.createStack());
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.values()[((ItemGeneral.SubItem)this.subItems.get(itemstack.getItemDamage())).rarity];
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean includeHandlers) {
      String location = this.getBoundDisplayName(stack);
      if(location != null && !location.isEmpty()) {
         list.add(location);
      }

      String taglock = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(stack, Integer.valueOf(1));
      if(!taglock.isEmpty()) {
         list.add(String.format(Witchery.resource("item.witcheryTaglockKit.boundto"), new Object[]{taglock}));
      }

      NBTTagCompound localText;
      int len$;
      int i$;
      if(this.itemDoorKey.isMatch(stack)) {
         localText = stack.getTagCompound();
         if(localText != null && localText.hasKey("doorX") && localText.hasKey("doorY") && localText.hasKey("doorZ")) {
            int arr$ = localText.getInteger("doorX");
            len$ = localText.getInteger("doorY");
            i$ = localText.getInteger("doorZ");
            if(localText.hasKey("doorD") && localText.hasKey("doorDN")) {
               list.add(String.format("%s: %d, %d, %d", new Object[]{localText.getString("doorDN"), Integer.valueOf(arr$), Integer.valueOf(len$), Integer.valueOf(i$)}));
            } else {
               list.add(String.format("%d, %d, %d", new Object[]{Integer.valueOf(arr$), Integer.valueOf(len$), Integer.valueOf(i$)}));
            }
         }
      } else if(this.itemDoorKeyring.isMatch(stack)) {
         localText = stack.getTagCompound();
         if(localText != null && localText.hasKey("doorKeys")) {
            NBTTagList var16 = localText.getTagList("doorKeys", 10);
            if(var16 != null) {
               for(len$ = 0; len$ < var16.tagCount(); ++len$) {
                  NBTTagCompound var18 = var16.getCompoundTagAt(len$);
                  if(var18 != null && var18.hasKey("doorX") && var18.hasKey("doorY") && var18.hasKey("doorZ")) {
                     int s = var18.getInteger("doorX");
                     int doorY = var18.getInteger("doorY");
                     int doorZ = var18.getInteger("doorZ");
                     if(var18.hasKey("doorD") && var18.hasKey("doorDN")) {
                        list.add(String.format("%s: %d, %d, %d", new Object[]{var18.getString("doorDN"), Integer.valueOf(s), Integer.valueOf(doorY), Integer.valueOf(doorZ)}));
                     } else {
                        list.add(String.format("%d, %d, %d", new Object[]{Integer.valueOf(s), Integer.valueOf(doorY), Integer.valueOf(doorZ)}));
                     }
                  }
               }
            }
         }
      }

      if(stack.getItemDamage() == this.itemContractTorment.damageValue) {
         String var14 = Witchery.resource("item.witchery:ingredient.contractTorment.tip");
         if(var14 != null) {
            String[] var15 = var14.split("\n");
            len$ = var15.length;

            for(i$ = 0; i$ < len$; ++i$) {
               String var17 = var15[i$];
               if(!var17.isEmpty()) {
                  list.add(var17);
               }
            }
         }
      }

   }

   public String getBoundDisplayName(ItemStack itemstack) {
      NBTTagCompound tag = itemstack.getTagCompound();
      return tag != null && tag.hasKey("PosX") && tag.hasKey("PosY") && tag.hasKey("PosZ") && tag.hasKey("NameD")?String.format("%s: %d, %d, %d", new Object[]{tag.getString("NameD"), Integer.valueOf(tag.getInteger("PosX")), Integer.valueOf(tag.getInteger("PosY")), Integer.valueOf(tag.getInteger("PosZ"))}):"";
   }

   public void bindToLocation(World world, int posX, int posY, int posZ, int dimension, String dimensionName, ItemStack itemstack) {
      if(!itemstack.hasTagCompound()) {
         itemstack.setTagCompound(new NBTTagCompound());
      }

      itemstack.getTagCompound().setInteger("PosX", posX);
      itemstack.getTagCompound().setInteger("PosY", posY);
      itemstack.getTagCompound().setInteger("PosZ", posZ);
      itemstack.getTagCompound().setInteger("PosD", dimension);
      itemstack.getTagCompound().setString("NameD", dimensionName);
   }

   public boolean hasLocationBinding(ItemStack itemstack) {
      if(!itemstack.hasTagCompound()) {
         return false;
      } else {
         NBTTagCompound nbtTag = itemstack.getTagCompound();
         return nbtTag.hasKey("PosX") && nbtTag.hasKey("PosY") && nbtTag.hasKey("PosZ") && nbtTag.hasKey("PosD") && nbtTag.hasKey("NameD");
      }
   }

   public void copyLocationBinding(ItemStack from, ItemStack to) {
      if(this.hasLocationBinding(from)) {
         NBTTagCompound nbtTagFrom = from.getTagCompound();
         if(!to.hasTagCompound()) {
            to.setTagCompound(new NBTTagCompound());
         }

         NBTTagCompound nbtTagTo = to.getTagCompound();
         nbtTagTo.setInteger("PosX", nbtTagFrom.getInteger("PosX"));
         nbtTagTo.setInteger("PosY", nbtTagFrom.getInteger("PosY"));
         nbtTagTo.setInteger("PosZ", nbtTagFrom.getInteger("PosZ"));
         nbtTagTo.setInteger("PosD", nbtTagFrom.getInteger("PosD"));
         nbtTagTo.setString("NameD", nbtTagFrom.getString("NameD"));
         if(from.hasDisplayName()) {
            to.setStackDisplayName(from.getDisplayName());
         }
      }

   }

   public boolean copyLocationBinding(World world, ItemStack from, BlockCircle.TileEntityCircle.ActivatedRitual to) {
      if(!this.hasLocationBinding(from)) {
         return false;
      } else {
         NBTTagCompound nbtTagFrom = from.getTagCompound();
         if(nbtTagFrom.getInteger("PosD") != world.provider.dimensionId) {
            return false;
         } else {
            Coord coord = new Coord(nbtTagFrom.getInteger("PosX"), nbtTagFrom.getInteger("PosY"), nbtTagFrom.getInteger("PosZ"));
            to.setLocation(coord);
            return true;
         }
      }
   }

   public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer player) {
      ItemGeneral.SubItem subItem = (ItemGeneral.SubItem)this.subItems.get(itemstack.getItemDamage());
      if(this.itemWaystoneBound.isMatch(itemstack)) {
         if(!world.isRemote && player instanceof EntityPlayerMP) {
            Witchery.packetPipeline.sendTo((IMessage)(new PacketCamPos(false, false, (Entity)null)), (EntityPlayerMP)player);
         }

         return itemstack;
      } else {
         int objs;
         if(subItem instanceof ItemGeneral.Edible) {
            if(!player.capabilities.isCreativeMode) {
               --itemstack.stackSize;
               if(itemstack.stackSize <= 0) {
                  itemstack.stackSize = 0;
                  player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
               }
            }

            ItemGeneral.Edible drinkable = (ItemGeneral.Edible)subItem;
            if(subItem == this.itemArtichoke) {
               int c = player.getFoodStats().getFoodLevel();
               player.getFoodStats().addStats(drinkable.healAmount, drinkable.saturationModifier);
               objs = player.getFoodStats().getFoodLevel() - c;
               player.addPotionEffect(new PotionEffect(Potion.hunger.id, 3 * objs * 20, 2));
            } else if(subItem == this.itemSleepingApple) {
               player.getFoodStats().addStats(drinkable.healAmount, drinkable.saturationModifier);
               if(player.dimension == 0 && !world.isRemote && !WorldProviderDreamWorld.getPlayerIsGhost(player)) {
                  WorldProviderDreamWorld.sendPlayerToSpiritWorld(player, 1.0D);
                  itemstack.stackSize = 0;
                  world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                  return player.getCurrentEquippedItem() != null?player.getCurrentEquippedItem():itemstack;
               }
            } else {
               player.getFoodStats().addStats(drinkable.healAmount, drinkable.saturationModifier);
            }

            world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
         } else if(subItem instanceof ItemGeneral.Drinkable) {
            if(this.itemDemonHeart.isMatch(itemstack) && player.isSneaking()) {
               return itemstack;
            }

            if(!player.capabilities.isCreativeMode) {
               --itemstack.stackSize;
               if(itemstack.stackSize <= 0) {
                  itemstack.stackSize = 0;
                  player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
               }
            }

            ItemGeneral.Drinkable var10 = (ItemGeneral.Drinkable)subItem;
            PotionEffect[] var12 = var10.effects;
            objs = var12.length;

            int itemIndex;
            PotionEffect effect;
            for(itemIndex = 0; itemIndex < objs; ++itemIndex) {
               effect = var12[itemIndex];
               player.addPotionEffect(new PotionEffect(effect));
            }

            if(this.itemDemonHeart.isMatch(itemstack)) {
               player.setFire(2640);
            } else if(this.itemBrewGrotesque.isMatch(itemstack)) {
               if(!world.isRemote) {
                  Infusion.getNBT(player).setInteger("witcheryGrotesque", 1200);
                  Witchery.packetPipeline.sendToDimension(new PacketPlayerStyle(player), player.dimension);
               }
            } else if(this.itemBrewOfSleeping.isMatch(itemstack)) {
               if(player.dimension == 0 && !world.isRemote && !WorldProviderDreamWorld.getPlayerIsGhost(player)) {
                  WorldProviderDreamWorld.sendPlayerToSpiritWorld(player, 0.998D);
                  itemstack.stackSize = 0;
                  world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
                  return player.getCurrentEquippedItem() != null?player.getCurrentEquippedItem():itemstack;
               }
            } else if(this.itemPurifiedMilk.isMatch(itemstack)) {
               if(!world.isRemote && world.rand.nextInt(2) == 0) {
                  Collection var11 = player.getActivePotionEffects();
                  if(var11 != null && !var11.isEmpty()) {
                     Object[] var13 = var11.toArray();
                     itemIndex = world.rand.nextInt(var11.size());
                     effect = (PotionEffect)var13[itemIndex];
                     player.removePotionEffect(effect.getPotionID());
                  }
               }
            } else if(this.itemBrewOfTheDepths.isMatch(itemstack)) {
               if(!world.isRemote) {
                  Infusion.getNBT(player).setInteger("witcheryDepths", 300);
               }
            } else if(this.itemCreeperHeart.isMatch(itemstack)) {
               if(!world.isRemote) {
                  if(Config.instance().allowExplodingCreeperHearts) {
                     world.createExplosion(player, player.posX, player.posY, player.posZ, 3.0F, true);
                  } else {
                     world.createExplosion(player, player.posX, player.posY, player.posZ, 1.0F, false);
                  }
               }
            } else if(this.itemFrozenHeart.isMatch(itemstack)) {
               if(!world.isRemote) {
                  PlayerEffects.onDeath(player);
               }
            } else {
               var10.onDrunk(world, player, itemstack);
            }

            world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
         }

         return itemstack;
      }
   }

   public int getMaxItemUseDuration(ItemStack itemstack) {
      ItemGeneral.SubItem subItem = (ItemGeneral.SubItem)this.subItems.get(itemstack.getItemDamage());
      return !(subItem instanceof ItemGeneral.Edible) && !(subItem instanceof ItemGeneral.Drinkable)?(this.itemWaystoneBound.isMatch(itemstack)?1200:(this.itemContractTorment.isMatch(itemstack)?1200:(this.itemSeerStone.isMatch(itemstack)?1200:super.getMaxItemUseDuration(itemstack)))):32;
   }

   public EnumAction getItemUseAction(ItemStack itemstack) {
      ItemGeneral.SubItem subItem = (ItemGeneral.SubItem)this.subItems.get(itemstack.getItemDamage());
      return subItem instanceof ItemGeneral.Edible?EnumAction.eat:(subItem instanceof ItemGeneral.Drinkable?((ItemGeneral.Drinkable)subItem).useAction:(this.itemContractTorment.isMatch(itemstack)?EnumAction.bow:(this.itemSeerStone.isMatch(itemstack)?EnumAction.bow:super.getItemUseAction(itemstack))));
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int countdown) {
      World world = player.worldObj;
      int elapsedTicks = this.getMaxItemUseDuration(stack) - countdown;
      if(!world.isRemote && player instanceof EntityPlayerMP) {
         if(this.itemWaystoneBound.isMatch(stack)) {
            if(elapsedTicks % 20 == 0) {
               if(elapsedTicks == 0) {
                  NBTTagCompound living = stack.getTagCompound();
                  if(living != null && living.hasKey("PosX") && living.hasKey("PosY") && living.hasKey("PosZ") && living.hasKey("PosD")) {
                     int newX = living.getInteger("PosX");
                     int newY = living.getInteger("PosY");
                     int newZ = living.getInteger("PosZ");
                     int newD = living.getInteger("PosD");
                     EntityEye eye = new EntityEye(world);
                     eye.setLocationAndAngles((double)newX, (double)newY, (double)newZ, player.rotationYaw, 90.0F);
                     world.spawnEntityInWorld(eye);
                     Witchery.packetPipeline.sendTo((IMessage)(new PacketCamPos(true, elapsedTicks == 0, eye)), (EntityPlayerMP)player);
                  }
               } else {
                  Witchery.packetPipeline.sendTo((IMessage)(new PacketCamPos(true, false, (Entity)null)), (EntityPlayerMP)player);
               }
            }
         } else if(this.itemContractTorment.isMatch(stack)) {
            if(!world.isRemote) {
               if(elapsedTicks != 0 && elapsedTicks != 40) {
                  if(elapsedTicks != 80 && elapsedTicks != 120) {
                     if(elapsedTicks != 160 && elapsedTicks != 200 && elapsedTicks != 240) {
                        if(elapsedTicks == 280 && Infusion.aquireEnergy(world, player, 10, true)) {
                           if(this.circleNear(world, player)) {
                              ParticleEffect.MOB_SPELL.send(SoundEffect.NONE, player, 1.0D, 2.0D, 16);
                              ParticleEffect.FLAME.send(SoundEffect.NONE, player, 1.0D, 2.0D, 16);
                              ParticleEffect.FLAME.send(SoundEffect.NONE, player, 1.0D, 2.0D, 16);
                              player.clearItemInUse();
                              EntityCreature var12 = InfusionInfernal.spawnCreature(world, EntityLordOfTorment.class, (int)player.posX, (int)player.posY, (int)player.posZ, (EntityLivingBase)null, 2, 4, ParticleEffect.FLAME, SoundEffect.MOB_ENDERDRAGON_GROWL);
                              if(var12 != null) {
                                 if(!player.capabilities.isCreativeMode) {
                                    --stack.stackSize;
                                    if(stack.stackSize <= 0) {
                                       player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                                    }
                                 }

                                 var12.func_110163_bv();
                                 world.newExplosion(var12, var12.posX, var12.posY + (double)var12.getEyeHeight(), var12.posZ, 7.0F, false, world.getGameRules().getGameRuleBooleanValue("mobGriefing"));
                              } else {
                                 SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
                              }
                           } else {
                              SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
                              player.clearItemInUse();
                           }
                        }
                     } else if(Infusion.aquireEnergy(world, player, 10, true)) {
                        ParticleEffect.MOB_SPELL.send(SoundEffect.MOB_BLAZE_DEATH, player, 1.0D, 2.0D, 16);
                        ParticleEffect.FLAME.send(SoundEffect.NONE, player, 1.0D, 2.0D, 16);
                     }
                  } else if(Infusion.aquireEnergy(world, player, 10, true)) {
                     ParticleEffect.MOB_SPELL.send(SoundEffect.MOB_BLAZE_DEATH, player, 1.0D, 2.0D, 16);
                  }
               } else if(Infusion.aquireEnergy(world, player, 10, true)) {
                  if(elapsedTicks <= 0 && !this.circleNear(world, player)) {
                     SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
                     ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "item.witchery:ingredient.contractTorment.nostones", new Object[0]);
                     player.clearItemInUse();
                  } else {
                     SoundEffect.MOB_BLAZE_DEATH.playAtPlayer(world, player);
                  }
               }
            }
         } else if(this.itemSeerStone.isMatch(stack)) {
            EntityCovenWitch.summonCoven(world, player, new Coord(player), elapsedTicks);
         }
      }

   }

   private boolean circleNear(World world, EntityPlayer player) {
      int midX = MathHelper.floor_double(player.posX);
      int midY = MathHelper.floor_double(player.posY);
      int midZ = MathHelper.floor_double(player.posZ);
      int[][] PATTERN = new int[][]{{0, 0, 0, 0, 4, 3, 4, 0, 0, 0, 0}, {0, 0, 4, 3, 1, 1, 1, 3, 4, 0, 0}, {0, 4, 1, 1, 1, 1, 1, 1, 1, 4, 0}, {0, 3, 1, 1, 1, 1, 1, 1, 1, 3, 0}, {4, 1, 1, 1, 2, 2, 2, 1, 1, 1, 4}, {3, 1, 1, 1, 2, 1, 2, 1, 1, 1, 3}, {4, 1, 1, 1, 2, 2, 2, 1, 1, 1, 4}, {0, 3, 1, 1, 1, 1, 1, 1, 1, 3, 0}, {0, 4, 1, 1, 1, 1, 1, 1, 1, 4, 0}, {0, 0, 4, 3, 1, 1, 1, 3, 4, 0, 0}, {0, 0, 0, 0, 4, 3, 4, 0, 0, 0, 0}};
      int offsetZ = (PATTERN.length - 1) / 2;

      for(int z = 0; z < PATTERN.length - 1; ++z) {
         int worldZ = midZ - offsetZ + z;
         int offsetX = (PATTERN[z].length - 1) / 2;

         for(int x = 0; x < PATTERN[z].length; ++x) {
            int worldX = midX - offsetX + x;
            int value = PATTERN[PATTERN.length - 1 - z][x];
            if(value != 0 && !this.isPost(world, worldX, midY, worldZ, value == 2 || value == 4, value == 4, value == 3 || value == 4)) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean isPost(World world, int x, int y, int z, boolean bottomSolid, boolean midSolid, boolean topSolid) {
      Block blockBelow = BlockUtil.getBlock(world, x, y - 1, z);
      Block blockBottom = BlockUtil.getBlock(world, x, y, z);
      Block blockMid = BlockUtil.getBlock(world, x, y + 1, z);
      Block blockTop = BlockUtil.getBlock(world, x, y + 2, z);
      Block blockAbove = BlockUtil.getBlock(world, x, y + 3, z);
      if(blockBelow != null && blockBelow.getMaterial().isSolid()) {
         if(bottomSolid) {
            if(blockBottom == null || !blockBottom.getMaterial().isSolid()) {
               return false;
            }
         } else if(blockBottom != null && blockBottom.getMaterial().isSolid()) {
            return false;
         }

         if(midSolid) {
            if(blockMid == null || !blockMid.getMaterial().isSolid()) {
               return false;
            }
         } else if(blockMid != null && blockMid.getMaterial().isSolid()) {
            return false;
         }

         if(topSolid) {
            if(blockTop == null || !blockTop.getMaterial().isSolid()) {
               return false;
            }
         } else if(blockTop != null && blockTop.getMaterial().isSolid()) {
            return false;
         }

         return blockAbove == null || !blockAbove.getMaterial().isSolid();
      } else {
         return false;
      }
   }

   public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      Block block = BlockUtil.getBlock(world, x, y, z);
      if(this.itemWaystoneBound.isMatch(stack) && block == Witchery.Blocks.CRYSTAL_BALL) {
         if(!world.isRemote && BlockCrystalBall.tryConsumePower(world, player, x, y, z)) {
            NBTTagCompound tag = stack.getTagCompound();
            if(tag != null && tag.hasKey("PosX") && tag.hasKey("PosY") && tag.hasKey("PosZ") && tag.hasKey("PosD")) {
               int newX = tag.getInteger("PosX");
               int newY = tag.getInteger("PosY");
               int newZ = tag.getInteger("PosZ");
               int newD = tag.getInteger("PosD");
               double MAX_DISTANCE = 22500.0D;
               if(newD == player.dimension && player.getDistanceSq((double)newX, (double)newY, (double)newZ) <= 22500.0D) {
                  player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
               } else {
                  SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
               }
            } else {
               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }
         } else if(world.isRemote) {
            player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
         }

         return !world.isRemote;
      } else {
         return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
      }
   }

   public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int countdown) {
      if(!world.isRemote && this.itemWaystoneBound.isMatch(stack) && player instanceof EntityPlayerMP) {
         Witchery.packetPipeline.sendTo((IMessage)(new PacketCamPos(false, false, (Entity)null)), (EntityPlayerMP)player);
      }

   }

   public boolean isBook(ItemStack itemstack) {
      return this.itemBookOven.isMatch(itemstack) || this.itemBookDistilling.isMatch(itemstack) || this.itemBookCircleMagic.isMatch(itemstack) || this.itemBookInfusions.isMatch(itemstack) || this.itemBookHerbology.isMatch(itemstack) || this.itemBookBiomes.isMatch(itemstack) || this.itemBookWands.isMatch(itemstack) || this.itemBookBurning.isMatch(itemstack);
   }

   public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
      ItemGeneral.SubItem subItem = (ItemGeneral.SubItem)this.subItems.get(itemstack.getItemDamage());
      if(this.isBook(itemstack)) {
         this.openWitchcraftBook(world, player, itemstack);
      } else if(this.itemWolfsbane.isMatch(itemstack)) {
         if(!world.isRemote) {
            MovingObjectPosition item = InfusionOtherwhere.raytraceEntities(world, player, true, 2.0D);
            if(item != null && item.entityHit != null) {
               if(CreatureUtil.isWerewolf(item.entityHit, true)) {
                  ParticleEffect.FLAME.send(SoundEffect.WITCHERY_MOB_WOLFMAN_HOWL, item.entityHit, 0.5D, 1.5D, 16);
               } else {
                  SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
               }

               --itemstack.stackSize;
               if(itemstack.stackSize <= 0) {
                  itemstack.stackSize = 0;
                  player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
               }
            }
         }
      } else if(subItem instanceof ItemGeneral.Edible) {
         if(player.canEat(false) || ((ItemGeneral.Edible)subItem).eatAnyTime) {
            player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
         }
      } else if(subItem instanceof ItemGeneral.Drinkable) {
         player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
      } else if(this.itemContractTorment.isMatch(itemstack)) {
         player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
      } else if(subItem instanceof ItemGeneral.Brew) {
         this.throwBrew(itemstack, world, player, subItem);
      } else if(this.itemQuicklime.isMatch(itemstack)) {
         this.throwBrew(itemstack, world, player, this.itemQuicklime);
      } else if(this.itemNecroStone.isMatch(itemstack)) {
         this.useNecroStone(world, player, itemstack);
      } else if(this.itemBatBall.isMatch(itemstack)) {
         --itemstack.stackSize;
         if(itemstack.stackSize <= 0) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
         }

         if(!world.isRemote) {
            EntityItem var7 = new EntityItem(world, player.posX, player.posY + 1.3D, player.posZ, this.itemBatBall.createStack());
            var7.delayBeforeCanPickup = 5;
            var7.setLocationAndAngles(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
            var7.posX -= (double)(MathHelper.cos(var7.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
            var7.posY -= 0.10000000149011612D;
            var7.posZ -= (double)(MathHelper.sin(var7.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
            var7.setPosition(var7.posX, var7.posY, var7.posZ);
            var7.yOffset = 0.0F;
            float f = 0.4F;
            var7.motionX = (double)(-MathHelper.sin(var7.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(var7.rotationPitch / 180.0F * 3.1415927F) * f);
            var7.motionZ = (double)(MathHelper.cos(var7.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(var7.rotationPitch / 180.0F * 3.1415927F) * f);
            var7.motionY = (double)(-MathHelper.sin((var7.rotationPitch + 0.0F) / 180.0F * 3.1415927F) * f);
            this.setThrowableHeading(var7, var7.motionX, var7.motionY, var7.motionZ, 1.0F, 1.0F);
            world.spawnEntityInWorld(var7);
         }
      } else if(this.itemSeerStone.isMatch(itemstack)) {
         this.useSeerStone(world, player, itemstack);
      } else if(this.itemIcyNeedle.isMatch(itemstack)) {
         this.useIcyNeedle(world, player, itemstack);
         return player.getCurrentEquippedItem() != null?player.getCurrentEquippedItem():itemstack;
      }

      return super.onItemRightClick(itemstack, world, player);
   }

   public void setThrowableHeading(EntityItem item, double par1, double par3, double par5, float par7, float par8) {
      float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
      par1 /= (double)f2;
      par3 /= (double)f2;
      par5 /= (double)f2;
      par1 += item.worldObj.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
      par3 += item.worldObj.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
      par5 += item.worldObj.rand.nextGaussian() * 0.007499999832361937D * (double)par8;
      par1 *= (double)par7;
      par3 *= (double)par7;
      par5 *= (double)par7;
      item.motionX = par1;
      item.motionY = par3;
      item.motionZ = par5;
      float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
      item.prevRotationYaw = item.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / 3.141592653589793D);
      item.prevRotationPitch = item.rotationPitch = (float)(Math.atan2(par3, (double)f3) * 180.0D / 3.141592653589793D);
   }

   private void useIcyNeedle(World world, EntityPlayer player, ItemStack itemstack) {
      if(!player.capabilities.isCreativeMode) {
         --itemstack.stackSize;
         if(itemstack.stackSize <= 0) {
            itemstack.stackSize = 0;
            player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
         }
      }

      if(world.provider.dimensionId == Config.instance().dimensionDreamID) {
         WorldProviderDreamWorld.returnPlayerToOverworld(player);
         itemstack.stackSize = 0;
      } else if(WorldProviderDreamWorld.getPlayerIsGhost(player)) {
         WorldProviderDreamWorld.returnGhostPlayerToSpiritWorld(player);
         itemstack.stackSize = 0;
      } else {
         player.attackEntityFrom(DamageSource.causePlayerDamage(player), 1.0F);
      }

   }

   public void throwBrew(ItemStack itemstack, World world, EntityPlayer player) {
      if(itemstack != null && itemstack.getItem() == this) {
         ItemGeneral.SubItem subItem = (ItemGeneral.SubItem)this.subItems.get(itemstack.getItemDamage());
         this.throwBrew(itemstack, world, player, subItem);
      }

   }

   private void throwBrew(ItemStack itemstack, World world, EntityPlayer player, ItemGeneral.SubItem item) {
      if(!player.capabilities.isCreativeMode) {
         --itemstack.stackSize;
      }

      world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
      if(!world.isRemote) {
         world.spawnEntityInWorld(new EntityWitchProjectile(world, player, item));
      }

   }

   private void openWitchcraftBook(World world, EntityPlayer player, ItemStack itemstack) {
      int posX = MathHelper.floor_double(player.posX);
      int posY = MathHelper.floor_double(player.posY);
      int posZ = MathHelper.floor_double(player.posZ);
      player.openGui(Witchery.instance, 1, world, posX, posY, posZ);
   }

   public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int posX, int posY, int posZ, int side, float par8, float par9, float par10) {
      if(this.itemMutandis.isMatch(itemstack)) {
         return this.useMutandis(false, itemstack, player, world, posX, posY, posZ);
      } else if(this.itemAnnointingPaste.isMatch(itemstack)) {
         return this.useAnnointingPaste(itemstack, player, world, posX, posY, posZ);
      } else if(this.itemKobolditePentacle.isMatch(itemstack)) {
         if(world.getBlock(posX, posY, posZ) == Witchery.Blocks.ALTAR && side == 1 && world.getBlock(posX, posY + 1, posZ) == Blocks.air) {
            BlockPlacedItem.placeItemInWorld(itemstack, player, world, posX, posY + 1, posZ);
            player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            return !world.isRemote;
         } else {
            return super.onItemUse(itemstack, player, world, posX, posY, posZ, side, par8, par9, par10);
         }
      } else if(this.itemMutandisExtremis.isMatch(itemstack)) {
         return this.useMutandis(true, itemstack, player, world, posX, posY, posZ);
      } else if(!this.itemChaliceEmpty.isMatch(itemstack) && !this.itemChaliceFull.isMatch(itemstack)) {
         if(this.itemCandelabra.isMatch(itemstack)) {
            return this.placeBlock(Witchery.Blocks.CANDELABRA, itemstack, player, world, posX, posY, posZ, side, par8, par9, par10);
         } else if(this.itemBroomEnchanted.isMatch(itemstack)) {
            return this.placeBroom(itemstack, player, world, posX, posY, posZ, side, par8, par9, par10);
         } else if(this.subItems.get(itemstack.getItemDamage()) instanceof ItemGeneral.DreamWeave) {
            return this.placeDreamCatcher(world, player, itemstack, posX, posY, posZ, side);
         } else if(this.itemDoorRowan.isMatch(itemstack)) {
            return this.placeDoor(world, player, itemstack, posX, posY, posZ, side, Witchery.Blocks.DOOR_ROWAN);
         } else if(this.itemDoorAlder.isMatch(itemstack)) {
            return this.placeDoor(world, player, itemstack, posX, posY, posZ, side, Witchery.Blocks.DOOR_ALDER);
         } else if(this.itemDoorIce.isMatch(itemstack)) {
            return this.placeDoor(world, player, itemstack, posX, posY, posZ, side, Witchery.Blocks.PERPETUAL_ICE_DOOR);
         } else if(!this.itemSubduedSpirit.isMatch(itemstack) && !this.itemSubduedSpiritVillage.isMatch(itemstack)) {
            if(this.itemSeedsTreefyd.isMatch(itemstack)) {
               return this.placeTreefyd(world, player, itemstack, posX, posY, posZ, side);
            } else if(this.itemBinkyHead.isMatch(itemstack)) {
               return this.placeBinky(world, player, itemstack, posX, posY, posZ, side);
            } else if(this.itemInfernalBlood.isMatch(itemstack)) {
               return this.placeInfernalBlood(world, player, itemstack, posX, posY, posZ, side);
            } else if(this.itemDemonHeart.isMatch(itemstack) && player.isSneaking()) {
               return this.placeBlock(Witchery.Blocks.DEMON_HEART, itemstack, player, world, posX, posY, posZ, side, par8, par9, par10);
            } else if(this.itemBoneNeedle.isMatch(itemstack) && ExtendedPlayer.get(player).isVampire()) {
               Block block = world.getBlock(posX, posY, posZ);
               if(block == Blocks.wool && world.getBlockMetadata(posX, posY, posZ) == 0) {
                  ExtendedPlayer playerEx = ExtendedPlayer.get(player);
                  if(playerEx.getVampireLevel() >= 4 && playerEx.decreaseBloodPower(125, true)) {
                     world.setBlock(posX, posY, posZ, Witchery.Blocks.BLOODED_WOOL);
                     ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, world, (double)posX + 0.5D, (double)posY + 0.5D, (double)posZ + 0.5D, 1.0D, 1.0D, 16);
                     return true;
                  }
               }

               SoundEffect.NOTE_SNARE.playOnlyTo(player);
               return true;
            } else {
               return super.onItemUse(itemstack, player, world, posX, posY, posZ, side, par8, par9, par10);
            }
         } else {
            return this.useSubduedSpirit(world, player, itemstack, posX, posY, posZ, side);
         }
      } else {
         return this.placeBlock(Witchery.Blocks.CHALICE, itemstack, player, world, posX, posY, posZ, side, par8, par9, par10);
      }
   }

   private boolean placeBinky(World world, EntityPlayer player, ItemStack itemstack, int posX, int posY, int posZ, int side) {
      if(side != 1) {
         return false;
      } else {
         ++posY;
         Material material = world.getBlock(posX, posY, posZ).getMaterial();
         if(material == null || !material.isSolid()) {
            if(!world.isRemote) {
               EntityDeathsHorse horse = new EntityDeathsHorse(world);
               horse.setHorseTamed(true);
               horse.setHorseType(4);
               horse.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
               horse.func_110163_bv();
               horse.setCustomNameTag(Witchery.resource("item.witchery.horseofdeath.customname"));
               horse.setLocationAndAngles(0.5D + (double)posX, 0.01D + (double)posY, 0.5D + (double)posZ, 0.0F, 0.0F);
               NBTTagCompound nbtHorse = horse.getEntityData();
               if(nbtHorse != null) {
                  nbtHorse.setBoolean("WITCIsBinky", true);
               }

               ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, world, 0.5D + (double)posX, (double)posY, 0.5D + (double)posZ, 1.0D, 1.0D, 16);
               world.spawnEntityInWorld(horse);
            }

            --itemstack.stackSize;
         }

         return true;
      }
   }

   private boolean placeInfernalBlood(World world, EntityPlayer player, ItemStack itemstack, int posX, int posY, int posZ, int side) {
      Block block = world.getBlock(posX, posY, posZ);
      int meta = world.getBlockMetadata(posX, posY, posZ);
      if(block == Witchery.Blocks.WICKER_BUNDLE && BlockWickerBundle.limitToValidMetadata(meta) == 0) {
         if(!world.isRemote) {
            int uses = 5;

            for(int y = posY - 1; y <= posY + 1 && uses > 0; ++y) {
               for(int x = posX - 1; x <= posX + 1 && uses > 0; ++x) {
                  for(int z = posZ - 1; z <= posZ + 1 && uses > 0; ++z) {
                     Block b = world.getBlock(x, y, z);
                     int m = world.getBlockMetadata(x, y, z);
                     if(b == Witchery.Blocks.WICKER_BUNDLE && BlockWickerBundle.limitToValidMetadata(m) == 0) {
                        world.setBlock(x, y, z, b, m | 1, 3);
                        --uses;
                     }
                  }
               }
            }
         }

         --itemstack.stackSize;
         return true;
      } else {
         return false;
      }
   }

   private boolean placeTreefyd(World world, EntityPlayer player, ItemStack itemstack, int posX, int posY, int posZ, int side) {
      if(side != 1) {
         return false;
      } else {
         ++posY;
         Material material = world.getBlock(posX, posY, posZ).getMaterial();
         if(Blocks.tallgrass.canBlockStay(world, posX, posY, posZ) && (material == null || !material.isSolid())) {
            if(!world.isRemote) {
               world.setBlock(posX, posY, posZ, Blocks.tallgrass, 1, 3);
               EntityTreefyd entity = new EntityTreefyd(world);
               entity.setLocationAndAngles(0.5D + (double)posX, (double)posY, 0.5D + (double)posZ, 0.0F, 0.0F);
               entity.onSpawnWithEgg((IEntityLivingData)null);
               entity.func_110163_bv();
               entity.setOwner(player.getCommandSenderName());
               world.spawnEntityInWorld(entity);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SILVERFISH_KILL, entity, 1.0D, 2.0D, 16);
               ParticleEffect.EXPLODE.send(SoundEffect.NONE, entity, 1.0D, 2.0D, 16);
            }

            --itemstack.stackSize;
         }

         return true;
      }
   }

   private boolean useSeerStone(World world, EntityPlayer player, ItemStack stack) {
      if(player.isSneaking()) {
         if(!world.isRemote) {
            double MAX_TARGET_RANGE = 3.0D;
            MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 3.0D);
            if(mop != null) {
               switch(ItemGeneral.NamelessClass6324708.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
               case 1:
                  if(mop.entityHit instanceof EntityPlayer) {
                     this.readPlayer(player, (EntityPlayer)mop.entityHit);
                     return true;
                  }
                  break;
               default:
                  this.readPlayer(player, player);
               }
            } else {
               this.readPlayer(player, player);
            }
         }
      } else {
         player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      }

      return true;
   }

   private void readPlayer(EntityPlayer player, EntityPlayer targetPlayer) {
      String reading = "";
      NBTTagCompound nbtPlayer = Infusion.getNBT(targetPlayer);
      if(nbtPlayer != null && nbtPlayer.hasKey("WITCManifestDuration")) {
         int familiarName = nbtPlayer.getInteger("WITCManifestDuration");
         if(familiarName > 0) {
            reading = reading + String.format(Witchery.resource("item.witchery:ingredient.seerstone.manifestationtime"), new Object[]{Integer.valueOf(familiarName).toString()}) + " ";
         } else {
            reading = reading + Witchery.resource("item.witchery:ingredient.seerstone.nomanifestationtime") + " ";
         }
      } else {
         reading = reading + Witchery.resource("item.witchery:ingredient.seerstone.nomanifestationtime") + " ";
      }

      String familiarName1 = Familiar.getFamiliarName(targetPlayer);
      if(familiarName1 != null && !familiarName1.isEmpty()) {
         reading = reading + String.format(Witchery.resource("item.witchery:ingredient.seerstone.familiar"), new Object[]{familiarName1}) + " ";
      } else {
         reading = reading + Witchery.resource("item.witchery:ingredient.seerstone.nofamiliar") + " ";
      }

      int covenSize = EntityCovenWitch.getCovenSize(targetPlayer);
      if(covenSize > 0) {
         reading = reading + String.format(Witchery.resource("item.witchery:ingredient.seerstone.covensize"), new Object[]{Integer.valueOf(covenSize).toString()}) + " ";
      } else {
         reading = reading + Witchery.resource("item.witchery:ingredient.seerstone.nocoven") + " ";
      }

      String spellKnowledge = SymbolEffect.getKnowledge(targetPlayer);
      if(!spellKnowledge.isEmpty()) {
         reading = reading + String.format(Witchery.resource("item.witchery:ingredient.seerstone.knownspells"), new Object[]{spellKnowledge}) + " ";
      } else {
         reading = reading + Witchery.resource("item.witchery:ingredient.seerstone.nospells") + " ";
      }

      ExtendedPlayer playerEx = ExtendedPlayer.get(targetPlayer);
      int level;
      if(playerEx != null) {
         level = playerEx.getSkillPotionBottling();
         reading = reading + String.format(Witchery.resource("item.witchery:ingredient.seerstone.bottlingskill"), new Object[]{Integer.valueOf(level).toString()}) + " ";
      }

      if(nbtPlayer != null && (nbtPlayer.hasKey("witcheryCursed") || nbtPlayer.hasKey("witcheryInsanity") || nbtPlayer.hasKey("witcherySinking") || nbtPlayer.hasKey("witcheryOverheating") || nbtPlayer.hasKey("witcheryWakingNightmare"))) {
         if(nbtPlayer.hasKey("witcheryCursed")) {
            level = nbtPlayer.getInteger("witcheryCursed");
            if(!reading.isEmpty()) {
               reading = reading + ", ";
            }

            reading = reading + String.format(Witchery.resource("witchery.item.seerstone.misfortune"), new Object[]{Integer.valueOf(level)});
         }

         if(nbtPlayer.hasKey("witcheryInsanity")) {
            level = nbtPlayer.getInteger("witcheryInsanity");
            if(!reading.isEmpty()) {
               reading = reading + ", ";
            }

            reading = reading + String.format(Witchery.resource("witchery.item.seerstone.insanity"), new Object[]{Integer.valueOf(level)});
         }

         if(nbtPlayer.hasKey("witcherySinking")) {
            level = nbtPlayer.getInteger("witcherySinking");
            if(!reading.isEmpty()) {
               reading = reading + ", ";
            }

            reading = reading + String.format(Witchery.resource("witchery.item.seerstone.sinking"), new Object[]{Integer.valueOf(level)});
         }

         if(nbtPlayer.hasKey("witcheryOverheating")) {
            level = nbtPlayer.getInteger("witcheryOverheating");
            if(!reading.isEmpty()) {
               reading = reading + ", ";
            }

            reading = reading + String.format(Witchery.resource("witchery.item.seerstone.overheating"), new Object[]{Integer.valueOf(level)});
         }

         if(nbtPlayer.hasKey("witcheryWakingNightmare")) {
            level = nbtPlayer.getInteger("witcheryWakingNightmare");
            if(!reading.isEmpty()) {
               reading = reading + ", ";
            }

            reading = reading + String.format(Witchery.resource("witchery.item.seerstone.nightmare"), new Object[]{Integer.valueOf(level)});
         }
      } else {
         reading = reading + Witchery.resource("witchery.item.seerstone.notcursed");
      }

      ChatUtil.sendPlain(EnumChatFormatting.BLUE, player, reading);
   }

   private boolean useSubduedSpirit(World world, EntityPlayer player, ItemStack itemstack, int x, int y, int z, int side) {
      if(!world.isRemote) {
         EntityCreature creature = Infusion.spawnCreature(world, EntitySpirit.class, x, y, z, (EntityLivingBase)null, 0, 0, ParticleEffect.INSTANT_SPELL, (SoundEffect)null);
         if(creature != null) {
            EntitySpirit spirit = (EntitySpirit)creature;
            creature.func_110163_bv();
            if(this.itemSubduedSpiritVillage.isMatch(itemstack)) {
               spirit.setTarget("Village", 1);
            }

            if(!player.capabilities.isCreativeMode && --itemstack.stackSize == 0) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
               if(player instanceof EntityPlayerMP) {
                  ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean useNecroStone(World world, EntityPlayer player, ItemStack itemstack) {
      if(world.isRemote) {
         return false;
      } else {
         double MAX_TARGET_RANGE = 15.0D;
         MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 15.0D);
         if(mop != null) {
            boolean r;
            switch(ItemGeneral.NamelessClass6324708.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
            case 1:
               if(mop.entityHit instanceof EntityLivingBase) {
                  if(!player.isSneaking()) {
                     EntityLivingBase var14 = (EntityLivingBase)mop.entityHit;
                     r = true;
                     int var15 = 0;
                     AxisAlignedBB var17 = AxisAlignedBB.getBoundingBox(player.posX - 50.0D, player.posY - 15.0D, player.posZ - 50.0D, player.posX + 50.0D, player.posY + 15.0D, player.posZ + 50.0D);
                     Iterator var16 = world.getEntitiesWithinAABB(EntityLiving.class, var17).iterator();

                     while(var16.hasNext()) {
                        Object var18 = var16.next();
                        EntityLiving var19 = (EntityLiving)var18;
                        if(var19.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD && PotionEnslaved.isMobEnslavedBy(var19, player)) {
                           ++var15;
                           EntityUtil.setTarget(var19, var14);
                        }
                     }

                     if(var15 > 0) {
                        Witchery.packetPipeline.sendToAllAround(new PacketParticles(ParticleEffect.CRIT, SoundEffect.MOB_ZOMBIE_DEATH, var14, 0.5D, 2.0D), TargetPointUtil.from(var14, 16.0D));
                        return true;
                     }
                  } else {
                     if(InfusedBrewEffect.Grave.isActive(player) && InfusedBrewEffect.Grave.tryUseEffect(player, mop)) {
                        Witchery.packetPipeline.sendToAllAround(new PacketParticles(ParticleEffect.MOB_SPELL, SoundEffect.MOB_ZOMBIE_INFECT, mop.entityHit, 1.0D, 1.0D), TargetPointUtil.from(mop.entityHit, 16.0D));
                        return true;
                     }

                     Witchery.packetPipeline.sendToAllAround(new PacketParticles(ParticleEffect.SMOKE, SoundEffect.NOTE_SNARE, mop.entityHit, 1.0D, 1.0D), TargetPointUtil.from(mop.entityHit, 16.0D));
                  }
               }
               break;
            case 2:
               if(world.getBlock(mop.blockX, mop.blockY, mop.blockZ) == Witchery.Blocks.ALLURING_SKULL) {
                  return false;
               }

               if(BlockSide.TOP.isEqual(mop.sideHit)) {
                  int minionCount = 0;
                  r = true;
                  AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(player.posX - 50.0D, player.posY - 15.0D, player.posZ - 50.0D, player.posX + 50.0D, player.posY + 15.0D, player.posZ + 50.0D);
                  Iterator i$ = world.getEntitiesWithinAABB(EntityLiving.class, bounds).iterator();

                  while(i$.hasNext()) {
                     Object obj = i$.next();
                     EntityLiving creature = (EntityLiving)obj;
                     EntityCreature creature2 = creature instanceof EntityCreature?(EntityCreature)creature:null;
                     if(creature.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD && PotionEnslaved.isMobEnslavedBy(creature, player)) {
                        ++minionCount;
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

                  if(minionCount > 0) {
                     ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_POP, world, 0.5D + (double)mop.blockX, (double)(mop.blockY + 1), 0.5D + (double)mop.blockZ, 1.0D, 1.0D, 16);
                     return true;
                  }
               }
            case 3:
            }
         }

         SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
         return false;
      }
   }

   private boolean placeDoor(World world, EntityPlayer player, ItemStack itemstack, int posX, int posY, int posZ, int side, Block block) {
      if(side != 1) {
         return false;
      } else {
         ++posY;
         if(player.canPlayerEdit(posX, posY, posZ, side, itemstack) && player.canPlayerEdit(posX, posY + 1, posZ, side, itemstack)) {
            if(!block.canPlaceBlockAt(world, posX, posY, posZ)) {
               return false;
            } else {
               int i1 = MathHelper.floor_double((double)((player.rotationYaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
               ItemDoor.placeDoorBlock(world, posX, posY, posZ, i1, block);
               if(!world.isRemote && this.itemDoorRowan.isMatch(itemstack)) {
                  ItemStack keyStack = Witchery.Items.GENERIC.itemDoorKey.createStack();
                  if(!keyStack.hasTagCompound()) {
                     keyStack.setTagCompound(new NBTTagCompound());
                  }

                  NBTTagCompound nbtTag = keyStack.getTagCompound();
                  nbtTag.setInteger("doorX", posX);
                  nbtTag.setInteger("doorY", posY);
                  nbtTag.setInteger("doorZ", posZ);
                  nbtTag.setInteger("doorD", world.provider.dimensionId);
                  nbtTag.setString("doorDN", world.provider.getDimensionName());
                  world.spawnEntityInWorld(new EntityItem(world, player.posX, player.posY + 0.5D, player.posZ, keyStack));
               }

               --itemstack.stackSize;
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public static void setBlockToClay(World world, int x, int y, int z) {
      Block block = world.getBlock(x, y, z);
      Block blockAbove = world.getBlock(x, y + 1, z);
      if(block == Blocks.dirt && (blockAbove == Blocks.water || blockAbove == Blocks.flowing_water)) {
         world.setBlock(x, y, z, Blocks.clay);
         if(!world.isRemote) {
            ParticleEffect.INSTANT_SPELL.send(SoundEffect.MOB_SLIME_BIG, world, 0.5D + (double)x, 1.5D + (double)y, 0.5D + (double)z, 1.0D, 1.0D, 16);
         }
      }

   }

   private boolean useAnnointingPaste(ItemStack stack, EntityPlayer player, World world, int x, int y, int z) {
      if(!world.isRemote) {
         Block block = world.getBlock(x, y, z);
         world.getBlockMetadata(x, y, z);
         if(block == Blocks.cauldron) {
            world.setBlock(x, y, z, Witchery.Blocks.CAULDRON);
            --stack.stackSize;
            ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, (double)x, (double)y, (double)z, 1.0D, 1.0D, 16);
            ParticleEffect.LARGE_EXPLODE.send(SoundEffect.RANDOM_LEVELUP, world, (double)x, (double)y, (double)z, 1.0D, 1.0D, 16);
         }
      }

      return true;
   }

   private boolean useMutandis(boolean extremis, ItemStack itemstack, EntityPlayer player, World world, int posX, int posY, int posZ) {
      if(!world.isRemote) {
         Block block = world.getBlock(posX, posY, posZ);
         Block blockAbove = world.getBlock(posX, posY + 1, posZ);
         if(extremis && (block == Blocks.grass || block == Blocks.mycelium)) {
            if(world.rand.nextInt(2) == 0) {
               world.setBlock(posX, posY, posZ, (Block)(block == Blocks.grass?Blocks.mycelium:Blocks.grass));
            }

            ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, (double)posX, (double)(posY + 1), (double)posZ, 1.0D, 1.0D, 16);
            --itemstack.stackSize;
         } else if(extremis && block == Blocks.dirt && (blockAbove == Blocks.water || blockAbove == Blocks.flowing_water)) {
            if(world.rand.nextInt(2) == 0) {
               setBlockToClay(world, posX, posY, posZ);
               setBlockToClay(world, posX + 1, posY, posZ);
               setBlockToClay(world, posX - 1, posY, posZ);
               setBlockToClay(world, posX, posY, posZ + 1);
               setBlockToClay(world, posX, posY, posZ - 1);
            } else {
               ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, (double)posX, (double)(posY + 1), (double)posZ, 1.0D, 1.0D, 16);
            }

            --itemstack.stackSize;
         } else {
            int metadata = world.getBlockMetadata(posX, posY, posZ);
            ArrayList list;
            MutableBlock[] mutableBlock;
            if(block == Blocks.flower_pot && metadata > 0) {
               mutableBlock = new MutableBlock[]{new MutableBlock(Blocks.flower_pot, 1), new MutableBlock(Blocks.flower_pot, 2), new MutableBlock(Blocks.flower_pot, 3), new MutableBlock(Blocks.flower_pot, 4), new MutableBlock(Blocks.flower_pot, 5), new MutableBlock(Blocks.flower_pot, 6), new MutableBlock(Blocks.flower_pot, 7), new MutableBlock(Blocks.flower_pot, 8), new MutableBlock(Blocks.flower_pot, 9), new MutableBlock(Blocks.flower_pot, 10), new MutableBlock(Blocks.flower_pot, 11)};
               list = new ArrayList(Arrays.asList(mutableBlock));
            } else {
               mutableBlock = new MutableBlock[]{new MutableBlock(Blocks.sapling, 0), new MutableBlock(Blocks.sapling, 1), new MutableBlock(Blocks.sapling, 2), new MutableBlock(Blocks.sapling, 3), new MutableBlock(Blocks.sapling, 4), new MutableBlock(Blocks.sapling, 5), new MutableBlock(Witchery.Blocks.SAPLING, 0), new MutableBlock(Witchery.Blocks.SAPLING, 1), new MutableBlock(Witchery.Blocks.SAPLING, 2), new MutableBlock(Witchery.Blocks.EMBER_MOSS, 0), new MutableBlock(Blocks.tallgrass, 1), new MutableBlock(Blocks.waterlily), new MutableBlock(Blocks.brown_mushroom), new MutableBlock(Blocks.red_mushroom), new MutableBlock(Blocks.red_flower, 0), new MutableBlock(Blocks.yellow_flower), new MutableBlock(Witchery.Blocks.SPANISH_MOSS, 1)};
               list = new ArrayList(Arrays.asList(mutableBlock));
               String[] index = Config.instance().mutandisExtras;
               int len$ = index.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  String extra = index[i$];

                  try {
                     list.add(new MutableBlock(extra));
                  } catch (Throwable var18) {
                     ;
                  }
               }

               MutableBlock[] var21;
               if(extremis) {
                  var21 = new MutableBlock[]{new MutableBlock(Blocks.carrots, -1, Math.min(metadata, 7)), new MutableBlock(Blocks.potatoes, -1, Math.min(metadata, 7)), new MutableBlock(Blocks.wheat, -1, Math.min(metadata, 7)), new MutableBlock(Blocks.reeds, -1, Math.min(metadata, 7)), new MutableBlock(Witchery.Blocks.CROP_BELLADONNA, -1, Math.min(metadata, Witchery.Blocks.CROP_BELLADONNA.getNumGrowthStages())), new MutableBlock(Witchery.Blocks.CROP_MANDRAKE, -1, Math.min(metadata, Witchery.Blocks.CROP_MANDRAKE.getNumGrowthStages())), new MutableBlock(Witchery.Blocks.CROP_ARTICHOKE, -1, Math.min(metadata, Witchery.Blocks.CROP_ARTICHOKE.getNumGrowthStages())), new MutableBlock(Blocks.pumpkin_stem, -1, Math.min(metadata, 7)), new MutableBlock(Blocks.cactus), new MutableBlock(Blocks.melon_stem, -1, Math.min(metadata, 7)), new MutableBlock(Blocks.nether_wart, -1, Math.min(metadata, 3))};
                  list.addAll(Arrays.asList(var21));
               } else if(player.dimension == Config.instance().dimensionDreamID) {
                  var21 = new MutableBlock[]{new MutableBlock(Blocks.nether_wart, -1, 3)};
                  list.addAll(Arrays.asList(var21));
               }
            }

            MutableBlock var19 = new MutableBlock(block, metadata, 0);
            int var20 = list.indexOf(var19);
            if(var20 != -1) {
               list.remove(var20);
               ((MutableBlock)list.get(world.rand.nextInt(list.size()))).mutate(world, posX, posY, posZ);
               ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, world, (double)posX, (double)posY, (double)posZ, 1.0D, 1.0D, 16);
               --itemstack.stackSize;
            }
         }
      }

      return true;
   }

   private boolean placeBroom(ItemStack itemstack, EntityPlayer player, World world, int posX, int posY, int posZ, int side, float par8, float par9, float par10) {
      float f = 1.0F;
      float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * 1.0F;
      float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * 1.0F;
      double d0 = player.prevPosX + (player.posX - player.prevPosX) * 1.0D;
      double d1 = player.prevPosY + (player.posY - player.prevPosY) * 1.0D + 1.62D - (double)player.yOffset;
      double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * 1.0D;
      Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
      float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
      float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
      float f5 = -MathHelper.cos(-f1 * 0.017453292F);
      float f6 = MathHelper.sin(-f1 * 0.017453292F);
      float f7 = f4 * f5;
      float f8 = f3 * f5;
      double d3 = 5.0D;
      Vec3 vec31 = vec3.addVector((double)f7 * 5.0D, (double)f6 * 5.0D, (double)f8 * 5.0D);
      MovingObjectPosition movingobjectposition = world.rayTraceBlocks(vec3, vec31, true);
      if(movingobjectposition == null) {
         return super.onItemUse(itemstack, player, world, posX, posY, posZ, side, par8, par9, par10);
      } else {
         Vec3 vec32 = player.getLook(1.0F);
         boolean flag = false;
         float f9 = 1.0F;
         List list = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.addCoord(vec32.xCoord * 5.0D, vec32.yCoord * 5.0D, vec32.zCoord * 5.0D).expand(1.0D, 1.0D, 1.0D));

         int i;
         for(i = 0; i < list.size(); ++i) {
            Entity j = (Entity)list.get(i);
            if(j.canBeCollidedWith()) {
               float k = j.getCollisionBorderSize();
               AxisAlignedBB broomEntity = j.boundingBox.expand((double)k, (double)k, (double)k);
               if(broomEntity.isVecInside(vec3)) {
                  flag = true;
               }
            }
         }

         if(flag) {
            return super.onItemUse(itemstack, player, world, posX, posY, posZ, side, par8, par9, par10);
         } else {
            if(movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
               i = movingobjectposition.blockX;
               int var40 = movingobjectposition.blockY;
               int var42 = movingobjectposition.blockZ;
               if(world.getBlock(i, var40, var42) == Blocks.snow) {
                  --var40;
               }

               EntityBroom var41 = new EntityBroom(world, (double)((float)i + 0.5F), (double)((float)var40 + 1.0F), (double)((float)var42 + 0.5F));
               if(itemstack.hasDisplayName()) {
                  var41.setCustomNameTag(itemstack.getDisplayName());
               }

               this.setBroomEntityColor(var41, itemstack);
               var41.rotationYaw = player.rotationYaw;
               if(!world.getCollidingBoundingBoxes(var41, var41.boundingBox.expand(-0.1D, -0.1D, -0.1D)).isEmpty()) {
                  super.onItemUse(itemstack, player, world, posX, posY, posZ, side, par8, par9, par10);
               }

               var41.rotationYaw = (float)(((MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) - 1) * 90);
               if(!world.isRemote) {
                  world.spawnEntityInWorld(var41);
                  int l = MathHelper.floor_float(var41.rotationYaw * 256.0F / 360.0F);
                  Witchery.packetPipeline.sendToAllAround(new S17PacketEntityLookMove(var41.getEntityId(), (byte)0, (byte)0, (byte)0, (byte)Math.max(Math.min(l, 255), 0), (byte)0), world, TargetPointUtil.from(var41, 128.0D));
               }

               if(!player.capabilities.isCreativeMode) {
                  --itemstack.stackSize;
               }
            }

            return super.onItemUse(itemstack, player, world, posX, posY, posZ, side, par8, par9, par10);
         }
      }
   }

   private void setBroomEntityColor(EntityBroom broomEntity, ItemStack itemstack) {
      broomEntity.setBrushColor(this.getBroomItemColor(itemstack));
   }

   public void setBroomItemColor(ItemStack itemstack, int brushColor) {
      if(brushColor >= 0 && brushColor <= 15) {
         if(!itemstack.hasTagCompound()) {
            itemstack.setTagCompound(new NBTTagCompound());
         }

         NBTTagCompound nbtTag = itemstack.getTagCompound();
         nbtTag.setByte("BrushColor", Byte.valueOf((byte)brushColor).byteValue());
      }

   }

   public int getBroomItemColor(ItemStack stack) {
      NBTTagCompound nbtTag = stack.getTagCompound();
      return nbtTag != null && nbtTag.hasKey("BrushColor")?nbtTag.getByte("BrushColor") & 15:-1;
   }

   private boolean placeBlock(Block spawnBlock, ItemStack itemstack, EntityPlayer player, World world, int posX, int posY, int posZ, int side, float par8, float par9, float par10) {
      Block block = world.getBlock(posX, posY, posZ);
      if(block == Blocks.snow && (world.getBlockMetadata(posX, posY, posZ) & 7) < 1) {
         side = 1;
      } else if(block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush) {
         if(side == 0) {
            --posY;
         } else if(side == 1) {
            ++posY;
         } else if(side == 2) {
            --posZ;
         } else if(side == 3) {
            ++posZ;
         } else if(side == 4) {
            --posX;
         } else if(side == 5) {
            ++posX;
         }
      }

      if(!player.canPlayerEdit(posX, posY, posZ, side, itemstack)) {
         return false;
      } else if(itemstack.stackSize == 0) {
         return false;
      } else {
         if(world.canPlaceEntityOnSide(spawnBlock, posX, posY, posZ, false, side, (Entity)null, itemstack)) {
            int j1 = spawnBlock.onBlockPlaced(world, posX, posY, posZ, side, par8, par9, par10, 0);
            if(world.setBlock(posX, posY, posZ, spawnBlock, j1, 3)) {
               if(world.getBlock(posX, posY, posZ) == spawnBlock) {
                  spawnBlock.onBlockPlacedBy(world, posX, posY, posZ, player, itemstack);
                  spawnBlock.onPostBlockPlaced(world, posX, posY, posZ, j1);
                  if(spawnBlock == Witchery.Blocks.CHALICE) {
                     BlockChalice.TileEntityChalice tileEntity = (BlockChalice.TileEntityChalice)world.getTileEntity(posX, posY, posZ);
                     if(tileEntity != null) {
                        tileEntity.setFilled(this.itemChaliceFull.isMatch(itemstack));
                     }
                  }
               }

               world.playSoundEffect((double)((float)posX + 0.5F), (double)((float)posY + 0.5F), (double)((float)posZ + 0.5F), block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
               --itemstack.stackSize;
            }
         }

         return true;
      }
   }

   private boolean placeDreamCatcher(World world, EntityPlayer player, ItemStack itemstack, int posX, int posY, int posZ, int side) {
      if(side == 0) {
         return false;
      } else if(!world.getBlock(posX, posY, posZ).getMaterial().isSolid()) {
         return false;
      } else {
         if(side == 1) {
            ++posY;
         } else if(side == 2) {
            --posZ;
         } else if(side == 3) {
            ++posZ;
         } else if(side == 4) {
            --posX;
         } else if(side == 5) {
            ++posX;
         }

         if(!player.canPlayerEdit(posX, posY, posZ, side, itemstack)) {
            return false;
         } else if(!Witchery.Blocks.DREAM_CATCHER.canPlaceBlockAt(world, posX, posY, posZ)) {
            return false;
         } else if(world.isRemote) {
            return true;
         } else {
            if(side != 1) {
               world.setBlock(posX, posY, posZ, Witchery.Blocks.DREAM_CATCHER, side, 3);
               --itemstack.stackSize;
               BlockDreamCatcher.TileEntityDreamCatcher tileEntity = (BlockDreamCatcher.TileEntityDreamCatcher)world.getTileEntity(posX, posY, posZ);
               if(tileEntity != null) {
                  ItemGeneral.DreamWeave weave = (ItemGeneral.DreamWeave)this.subItems.get(itemstack.getItemDamage());
                  weave.setEffect(tileEntity);
               }
            }

            return true;
         }
      }
   }

   public static boolean isWaystoneBound(ItemStack stack) {
      NBTTagCompound tag = stack.getTagCompound();
      return tag != null && tag.hasKey("PosX") && tag.hasKey("PosY") && tag.hasKey("PosZ") && tag.hasKey("PosD");
   }

   public static int getWaystoneDimension(ItemStack stack) {
      NBTTagCompound tag = stack.getTagCompound();
      if(tag != null && tag.hasKey("PosX") && tag.hasKey("PosY") && tag.hasKey("PosZ") && tag.hasKey("PosD")) {
         int newX = tag.getInteger("PosX");
         int newY = tag.getInteger("PosY");
         int newZ = tag.getInteger("PosZ");
         int newD = tag.getInteger("PosD");
         return newD;
      } else {
         return 0;
      }
   }

   private boolean isRestrictedTeleportTarget(int source, int target) {
      return source == target?false:source == Config.instance().dimensionDreamID || source == Config.instance().dimensionMirrorID || target == Config.instance().dimensionDreamID || target == Config.instance().dimensionMirrorID;
   }

   public boolean teleportToLocation(World world, ItemStack itemstack, Entity entity, int radius, boolean presetPosition) {
      NBTTagCompound tag = itemstack.getTagCompound();
      if(tag != null && tag.hasKey("PosX") && tag.hasKey("PosY") && tag.hasKey("PosZ") && tag.hasKey("PosD")) {
         int target1 = tag.getInteger("PosX") - radius + world.rand.nextInt(radius * 2 + 1);
         int newY = tag.getInteger("PosY");
         int newZ = tag.getInteger("PosZ") - radius + world.rand.nextInt(radius * 2 + 1);
         int newD = tag.getInteger("PosD");
         if(!this.isRestrictedTeleportTarget(entity.dimension, newD)) {
            teleportToLocation(world, (double)target1, (double)newY, (double)newZ, newD, entity, presetPosition);
            return true;
         }
      } else if(tag != null) {
         EntityLivingBase target = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, itemstack, Integer.valueOf(1));
         if(entity != null && target != null && !this.isRestrictedTeleportTarget(entity.dimension, target.dimension)) {
            teleportToLocation(world, target.posX, target.posY, target.posZ, target.dimension, entity, presetPosition);
            return true;
         }
      }

      return false;
   }

   public static boolean teleportToLocationSafely(World world, double posX, double posY, double posZ, int dimension, Entity entity, boolean presetPosition) {
      WorldServer targetWorld = MinecraftServer.getServer().worldServerForDimension(dimension);
      int x = MathHelper.floor_double(posX);
      int y = MathHelper.floor_double(posY);
      int z = MathHelper.floor_double(posZ);

      for(int i = 0; i < 16; ++i) {
         int dy = y + i;
         if(dy < 250 && !BlockUtil.isReplaceableBlock(targetWorld, x, dy, z) && BlockUtil.isReplaceableBlock(targetWorld, x, dy + 1, z) && BlockUtil.isReplaceableBlock(targetWorld, x, dy + 2, z)) {
            teleportToLocation(world, (double)x, (double)(dy + 1), (double)z, dimension, entity, presetPosition);
            return true;
         }

         dy = y - i;
         if(i > 0 && dy > 1 && !BlockUtil.isReplaceableBlock(targetWorld, x, dy, z) && BlockUtil.isReplaceableBlock(targetWorld, x, dy + 1, z) && BlockUtil.isReplaceableBlock(targetWorld, x, dy + 2, z)) {
            teleportToLocation(world, (double)x, (double)(dy + 1), (double)z, dimension, entity, presetPosition);
            return true;
         }
      }

      return false;
   }

   public static void teleportToLocation(World world, double posX, double posY, double posZ, int dimension, Entity entity, boolean presetPosition) {
      teleportToLocation(world, posX, posY, posZ, dimension, entity, presetPosition, ParticleEffect.PORTAL, SoundEffect.MOB_ENDERMEN_PORTAL);
   }

   public static void teleportToLocation(World world, double posX, double posY, double posZ, int dimension, Entity entity, boolean presetPosition, ParticleEffect particle, SoundEffect sound) {
      boolean isVampire = CreatureUtil.isVampire(entity);
      if(isVampire) {
         Witchery.packetPipeline.sendToAllAround(new PacketParticles(ParticleEffect.SMOKE, SoundEffect.WITCHERY_RANDOM_POOF, entity, 0.5D, 2.0D), TargetPointUtil.from(entity, 16.0D));
      } else {
         Witchery.packetPipeline.sendToAllAround(new PacketParticles(particle, sound, entity, 0.5D, 2.0D), TargetPointUtil.from(entity, 16.0D));
      }

      if(entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         if(entity.dimension != dimension) {
            if(presetPosition) {
               player.setPosition(posX, posY, posZ);
            }

            travelToDimension(player, dimension);
         }

         player.setPositionAndUpdate(posX, posY, posZ);
      } else if(entity instanceof EntityLiving) {
         if(entity.dimension != dimension) {
            travelToDimension(entity, dimension, posX, posY, posZ);
         } else {
            entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
         }
      } else if(entity.dimension != dimension) {
         travelToDimension(entity, dimension, posX, posY, posZ);
      } else {
         entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
      }

      if(isVampire) {
         Witchery.packetPipeline.sendToAllAround(new PacketParticles(ParticleEffect.SMOKE, SoundEffect.WITCHERY_RANDOM_POOF, entity, 0.5D, 2.0D), TargetPointUtil.from(entity, 16.0D));
      } else {
         Witchery.packetPipeline.sendToAllAround(new PacketParticles(particle, sound, entity, 0.5D, 2.0D), TargetPointUtil.from(entity, 16.0D));
      }

   }

   public static void travelToDimension(EntityPlayer player, int dimension) {
      if(!player.worldObj.isRemote & player instanceof EntityPlayerMP) {
         MinecraftServer server = MinecraftServer.getServer();
         WorldServer newWorldServer = server.worldServerForDimension(dimension);
         server.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)player, dimension, new ItemGeneral.Teleporter2(newWorldServer));
      }

   }

   private static Entity travelToDimension(Entity thisE, int par1, double posX, double posY, double posZ) {
      if(!thisE.worldObj.isRemote && !thisE.isDead) {
         thisE.worldObj.theProfiler.startSection("changeDimension");
         MinecraftServer minecraftserver = MinecraftServer.getServer();
         int j = thisE.dimension;
         WorldServer worldserver = minecraftserver.worldServerForDimension(j);
         WorldServer worldserver1 = minecraftserver.worldServerForDimension(par1);
         thisE.dimension = par1;
         if(j == 1 && par1 == 1) {
            worldserver1 = minecraftserver.worldServerForDimension(0);
            thisE.dimension = 0;
         }

         thisE.worldObj.removeEntity(thisE);
         thisE.isDead = false;
         thisE.worldObj.theProfiler.startSection("reposition");
         minecraftserver.getConfigurationManager().transferEntityToWorld(thisE, j, worldserver, worldserver1, new ItemGeneral.Teleporter2(worldserver1));
         thisE.worldObj.theProfiler.endStartSection("reloading");
         Entity entity = EntityList.createEntityByName(EntityList.getEntityString(thisE), worldserver1);
         if(entity != null) {
            entity.copyDataFrom(thisE, true);
            entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
            worldserver1.spawnEntityInWorld(entity);
         }

         thisE.isDead = true;
         thisE.worldObj.theProfiler.endSection();
         worldserver.resetUpdateEntityTick();
         worldserver1.resetUpdateEntityTick();
         thisE.worldObj.theProfiler.endSection();
         return entity;
      } else {
         return null;
      }
   }

   // $FF: synthetic class
   static class NamelessClass6324708 {

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

   public static class Drinkable extends ItemGeneral.SubItem {

      protected PotionEffect[] effects;
      protected EnumAction useAction;


      protected Drinkable(int damageValue, String unlocalizedName, int rarity, PotionEffect ... effects) {
         this(damageValue, unlocalizedName, rarity, EnumAction.drink, effects);
      }

      protected Drinkable(int damageValue, String unlocalizedName, int rarity, EnumAction useAction, PotionEffect ... effects) {
         super(damageValue, unlocalizedName, rarity, null);
         this.effects = effects;
         this.useAction = useAction;
      }

      public void onDrunk(World world, EntityPlayer player, ItemStack itemstack) {}
   }

   public static class DreamWeave extends ItemGeneral.SubItem {

      public final int weaveID;
      public final int textureOffsetX;
      public final int textureOffsetY;
      private final Potion potionDream;
      private final Potion potionNightmare;
      private final int duration;
      private final int amplifier;


      private static ItemGeneral.DreamWeave register(ItemGeneral.DreamWeave subItem, ArrayList subItems, ArrayList weaves) {
         weaves.add(ItemGeneral.SubItem.register(subItem, subItems));
         return subItem;
      }

      private DreamWeave(int damageValue, int weaveID, String unlocalizedName, Potion potionDream, Potion potionNightmare, int duration, int amplifier, int textureX, int textureY) {
         super(damageValue, unlocalizedName, 1, null);
         this.potionDream = potionDream;
         this.potionNightmare = potionNightmare;
         this.duration = duration;
         this.amplifier = amplifier;
         this.textureOffsetX = textureX;
         this.textureOffsetY = textureY;
         this.weaveID = weaveID;
      }

      public void setEffect(BlockDreamCatcher.TileEntityDreamCatcher dreamCatcherEntity) {
         dreamCatcherEntity.setEffect(this);
      }

      public void applyEffect(EntityPlayer player, boolean isDream, boolean isEnhanced) {
         if(isDream) {
            player.addPotionEffect(new PotionEffect(this.potionDream.getId(), isEnhanced && this.potionDream.id == Potion.field_76443_y.id?this.duration + 2400:(isEnhanced?this.duration - 2400:this.duration), isEnhanced && this.potionDream.id != Potion.field_76443_y.id?this.amplifier + 1:this.amplifier));
         } else {
            player.addPotionEffect(new PotionEffect(this.potionNightmare.getId(), this.duration, isEnhanced?this.amplifier + 1:this.amplifier));
         }

      }

      // $FF: synthetic method
      DreamWeave(int x0, int x1, String x2, Potion x3, Potion x4, int x5, int x6, int x7, int x8, Object x9) {
         this(x0, x1, x2, x3, x4, x5, x6, x7, x8);
      }
   }

   public static class Brew extends ItemGeneral.SubItem {

      public Brew(int damageValue, String unlocalizedName) {
         super(damageValue, unlocalizedName);
         this.setPotion(true);
      }

      public ItemGeneral.Brew.BrewResult onImpact(World world, EntityLivingBase thrower, MovingObjectPosition mop, boolean enhanced, double brewX, double brewY, double brewZ, AxisAlignedBB brewBounds) {
         return ItemGeneral.Brew.BrewResult.SHOW_EFFECT;
      }

      protected static boolean setBlockIfNotSolid(World world, int x, int y, int z, Block block) {
         return setBlockIfNotSolid(world, x, y, z, block, 0);
      }

      protected static boolean setBlockIfNotSolid(World world, int x, int y, int z, Block block, int metadata) {
         if(world.getBlock(x, y, z).getMaterial().isSolid() && (block != Blocks.web || BlockUtil.getBlock(world, x, y, z) != Blocks.snow)) {
            return false;
         } else {
            BlockUtil.setBlock(world, x, y, z, block, metadata, 3);
            ParticleEffect.EXPLODE.send(SoundEffect.NONE, world, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z, 1.0D, 1.0D, 16);
            return true;
         }
      }

      public static enum BrewResult {

         DROP_ITEM("DROP_ITEM", 0),
         SHOW_EFFECT("SHOW_EFFECT", 1),
         HIDE_EFFECT("HIDE_EFFECT", 2);
         // $FF: synthetic field
         private static final ItemGeneral.Brew.BrewResult[] $VALUES = new ItemGeneral.Brew.BrewResult[]{DROP_ITEM, SHOW_EFFECT, HIDE_EFFECT};


         private BrewResult(String var1, int var2) {}

      }
   }

   public static class SubItem {

      public final int damageValue;
      private final String unlocalizedName;
      private final int rarity;
      private final boolean showInCreativeTab;
      protected boolean enchanted;
      protected boolean potion;
      @SideOnly(Side.CLIENT)
      private IIcon icon;
      // $FF: synthetic field
      static final boolean $assertionsDisabled = !ItemGeneral.class.desiredAssertionStatus();


      private static ItemGeneral.SubItem register(ItemGeneral.SubItem subItem, ArrayList subItems) {
         if(!$assertionsDisabled && subItems.size() != subItem.damageValue) {
            throw new AssertionError("Misalignement with subItem registration");
         } else {
            while(subItems.size() <= subItem.damageValue) {
               subItems.add((Object)null);
            }

            subItems.set(subItem.damageValue, subItem);
            return subItem;
         }
      }

      public boolean isSolidifier() {
         return false;
      }

      public boolean isInfused() {
         return false;
      }

      public SubItem(int damageValue, String unlocalizedName) {
         this(damageValue, unlocalizedName, 0, true);
      }

      private SubItem(int damageValue, String unlocalizedName, int rarity) {
         this(damageValue, unlocalizedName, rarity, true);
      }

      private SubItem(int damageValue, String unlocalizedName, int rarity, boolean showInCreativeTab) {
         this.damageValue = damageValue;
         this.unlocalizedName = unlocalizedName;
         this.rarity = rarity;
         this.showInCreativeTab = showInCreativeTab;
         this.enchanted = false;
         this.potion = false;
      }

      @SideOnly(Side.CLIENT)
      private void registerIcon(IIconRegister iconRegister, ItemGeneral itemIngredient) {
         this.icon = iconRegister.registerIcon(itemIngredient.getIconString() + "." + this.unlocalizedName);
      }

      public boolean isMatch(ItemStack itemstack) {
         return itemstack != null && Witchery.Items.GENERIC == itemstack.getItem() && itemstack.getItemDamage() == this.damageValue;
      }

      public ItemStack createStack(int stackSize) {
         return new ItemStack(Witchery.Items.GENERIC, stackSize, this.damageValue);
      }

      public ItemStack createStack() {
         return this.createStack(1);
      }

      public boolean isItemInInventory(InventoryPlayer inventory) {
         return this.getItemSlotFromInventory(inventory) != -1;
      }

      public int getItemSlotFromInventory(InventoryPlayer inventory) {
         for(int k = 0; k < inventory.mainInventory.length; ++k) {
            if(inventory.mainInventory[k] != null && inventory.mainInventory[k].getItem() == Witchery.Items.GENERIC && inventory.mainInventory[k].getItemDamage() == this.damageValue) {
               return k;
            }
         }

         return -1;
      }

      public boolean consumeItemFromInventory(InventoryPlayer inventory) {
         int j = this.getItemSlotFromInventory(inventory);
         if(j < 0) {
            return false;
         } else {
            if(--inventory.mainInventory[j].stackSize <= 0) {
               inventory.mainInventory[j] = null;
            }

            return true;
         }
      }

      public boolean isEnchanted() {
         return this.enchanted || this.potion;
      }

      public ItemGeneral.SubItem setEnchanted(boolean enchanted) {
         this.enchanted = enchanted;
         return this;
      }

      public ItemGeneral.SubItem setPotion(boolean potion) {
         this.potion = potion;
         return this;
      }

      public boolean isPotion() {
         return this.potion;
      }

      public BrewItemKey getBrewItemKey() {
         return new BrewItemKey(Witchery.Items.GENERIC, this.damageValue);
      }

      // $FF: synthetic method
      SubItem(int x0, String x1, int x2, Object x3) {
         this(x0, x1, x2);
      }

      // $FF: synthetic method
      SubItem(int x0, String x1, int x2, boolean x3, Object x4) {
         this(x0, x1, x2, x3);
      }

   }

   public static class BoltType extends ItemGeneral.SubItem {

      private BoltType(int damageValue, String unlocalizedName) {
         super(damageValue, unlocalizedName);
      }

      public static ItemGeneral.BoltType getBolt(ItemStack stack) {
         if(stack != null && stack.getItem() == Witchery.Items.GENERIC) {
            ItemGeneral.SubItem item = (ItemGeneral.SubItem)Witchery.Items.GENERIC.subItems.get(stack.getItemDamage());
            if(item instanceof ItemGeneral.BoltType) {
               return (ItemGeneral.BoltType)item;
            }
         }

         return null;
      }

      // $FF: synthetic method
      BoltType(int x0, String x1, Object x2) {
         this(x0, x1);
      }
   }

   public static class Edible extends ItemGeneral.SubItem {

      public final boolean eatAnyTime;
      private final int healAmount;
      private final float saturationModifier;
      private final boolean wolfsFavorite;


      private Edible(int damageValue, String unlocalizedName, int healAmount, float saturationModifier, boolean wolfsFavorite) {
         this(damageValue, unlocalizedName, healAmount, saturationModifier, wolfsFavorite, false);
      }

      private Edible(int damageValue, String unlocalizedName, int healAmount, float saturationModifier, boolean wolfsFavorite, boolean eatAnyTime) {
         super(damageValue, unlocalizedName);
         this.healAmount = healAmount;
         this.saturationModifier = saturationModifier;
         this.wolfsFavorite = wolfsFavorite;
         this.eatAnyTime = eatAnyTime;
      }

      // $FF: synthetic method
      Edible(int x0, String x1, int x2, float x3, boolean x4, Object x5) {
         this(x0, x1, x2, x3, x4);
      }

      // $FF: synthetic method
      Edible(int x0, String x1, int x2, float x3, boolean x4, boolean x5, Object x6) {
         this(x0, x1, x2, x3, x4, x5);
      }
   }

   private static class Teleporter2 extends Teleporter {

      public Teleporter2(WorldServer server) {
         super(server);
      }

      public boolean makePortal(Entity par1Entity) {
         return false;
      }

      public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
         return false;
      }

      public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {}

      public void removeStalePortalLocations(long par1) {}
   }
}
