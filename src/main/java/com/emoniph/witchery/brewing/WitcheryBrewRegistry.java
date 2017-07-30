package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.blocks.BlockCircleGlyph;
import com.emoniph.witchery.blocks.BlockWitchCrop;
import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNameBuilder;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.BrewNamePartModifier;
import com.emoniph.witchery.brewing.DispersalGas;
import com.emoniph.witchery.brewing.DispersalInstant;
import com.emoniph.witchery.brewing.DispersalLiquid;
import com.emoniph.witchery.brewing.DispersalTriggered;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.EffectLevelCounter;
import com.emoniph.witchery.brewing.EntitySplatter;
import com.emoniph.witchery.brewing.ModifierYield;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.RitualStatus;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.brewing.action.BrewActionBlockCircle;
import com.emoniph.witchery.brewing.action.BrewActionDispersal;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.brewing.action.BrewActionImpactModifier;
import com.emoniph.witchery.brewing.action.BrewActionList;
import com.emoniph.witchery.brewing.action.BrewActionModifier;
import com.emoniph.witchery.brewing.action.BrewActionRitual;
import com.emoniph.witchery.brewing.action.BrewActionRitualEntityTarget;
import com.emoniph.witchery.brewing.action.BrewActionRitualRecipe;
import com.emoniph.witchery.brewing.action.BrewActionRitualSummonMob;
import com.emoniph.witchery.brewing.action.BrewActionSetColor;
import com.emoniph.witchery.brewing.action.BrewCurseEffect;
import com.emoniph.witchery.brewing.action.BrewPotionEffect;
import com.emoniph.witchery.brewing.action.effect.BrewActionBiomeChange;
import com.emoniph.witchery.brewing.action.effect.BrewActionBlight;
import com.emoniph.witchery.brewing.action.effect.BrewActionFelling;
import com.emoniph.witchery.brewing.action.effect.BrewActionLilify;
import com.emoniph.witchery.brewing.action.effect.BrewActionPlanting;
import com.emoniph.witchery.brewing.action.effect.BrewActionRaiseLand;
import com.emoniph.witchery.brewing.action.effect.BrewActionRaising;
import com.emoniph.witchery.brewing.action.effect.BrewActionSprouting;
import com.emoniph.witchery.brewing.action.effect.BrewActionTranspose;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.brewing.potions.PotionSnowTrail;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.Shapeshift;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityEnt;
import com.emoniph.witchery.entity.EntityLeonard;
import com.emoniph.witchery.entity.EntityOwl;
import com.emoniph.witchery.entity.EntitySummonedUndead;
import com.emoniph.witchery.entity.EntityToad;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.infusions.InfusionInfernal;
import com.emoniph.witchery.network.PacketPushTarget;
import com.emoniph.witchery.util.BlockActionCircle;
import com.emoniph.witchery.util.BlockActionReplaceSphere;
import com.emoniph.witchery.util.BlockActionSphere;
import com.emoniph.witchery.util.BlockPosition;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.CircleUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Count;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.Dye;
import com.emoniph.witchery.util.EntityDamageSourceIndirectSilver;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.SpawnUtil;
import com.emoniph.witchery.util.TimeUtil;
import com.emoniph.witchery.util.TransformCreature;
import com.emoniph.witchery.worldgen.WorldGenLargeWitchTree;
import com.emoniph.witchery.worldgen.WorldGenWitchTree;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;

public class WitcheryBrewRegistry {

   public static final WitcheryBrewRegistry INSTANCE = new WitcheryBrewRegistry();
   public static final int MAX_STRENGTH_BOOSTS = 7;
   public static final int MAX_DURATION_BOOSTS = 7;
   private final Hashtable ingredients = new Hashtable();
   private final List recipes = new ArrayList();


   private WitcheryBrewRegistry() {
      BrewItemKey triggeredKey = new BrewItemKey(Items.skull, 2);
      this.register((new BrewActionDispersal(new BrewItemKey(Items.gunpowder), new AltarPower(0), new DispersalInstant())).addNullifier(new BrewItemKey(Items.gunpowder), false).addNullifier(Witchery.Items.GENERIC.itemBatWool.getBrewItemKey(), false).addNullifier(Witchery.Items.GENERIC.itemArtichoke.getBrewItemKey(), false).addNullifier(Witchery.Items.GENERIC.itemWormwood.getBrewItemKey(), false).addNullifier(triggeredKey, false).addNullifier(new BrewItemKey(Items.wheat), false));
      this.register((new BrewActionDispersal(Witchery.Items.GENERIC.itemArtichoke.getBrewItemKey(), new AltarPower(0), new DispersalInstant())).addNullifier(new BrewItemKey(Items.gunpowder), false).addNullifier(Witchery.Items.GENERIC.itemBatWool.getBrewItemKey(), false).addNullifier(Witchery.Items.GENERIC.itemArtichoke.getBrewItemKey(), false).addNullifier(Witchery.Items.GENERIC.itemWormwood.getBrewItemKey(), false).addNullifier(triggeredKey, false).addNullifier(new BrewItemKey(Items.wheat), false));
      this.register((new BrewActionDispersal(Witchery.Items.GENERIC.itemBatWool.getBrewItemKey(), new AltarPower(0), new DispersalGas())).addNullifier(new BrewItemKey(Items.gunpowder), false).addNullifier(Witchery.Items.GENERIC.itemArtichoke.getBrewItemKey(), false).addNullifier(Witchery.Items.GENERIC.itemBatWool.getBrewItemKey(), false).addNullifier(Witchery.Items.GENERIC.itemWormwood.getBrewItemKey(), false).addNullifier(triggeredKey, false).addNullifier(new BrewItemKey(Items.wheat), false));
      this.register((new BrewActionDispersal(Witchery.Items.GENERIC.itemWormwood.getBrewItemKey(), new AltarPower(0), new DispersalLiquid())).addNullifier(new BrewItemKey(Items.gunpowder), false).addNullifier(Witchery.Items.GENERIC.itemBatWool.getBrewItemKey(), false).addNullifier(Witchery.Items.GENERIC.itemArtichoke.getBrewItemKey(), false).addNullifier(Witchery.Items.GENERIC.itemWormwood.getBrewItemKey(), false).addNullifier(triggeredKey, false).addNullifier(new BrewItemKey(Items.wheat), false));
      this.register((new BrewActionDispersal(triggeredKey, new AltarPower(0), new DispersalTriggered())).addNullifier(new BrewItemKey(Items.gunpowder), false).addNullifier(Witchery.Items.GENERIC.itemBatWool.getBrewItemKey(), false).addNullifier(Witchery.Items.GENERIC.itemArtichoke.getBrewItemKey(), false).addNullifier(Witchery.Items.GENERIC.itemWormwood.getBrewItemKey(), false).addNullifier(triggeredKey, false).addNullifier(new BrewItemKey(Items.wheat), false));
      this.register(new BrewActionImpactModifier(Witchery.Items.GENERIC.itemAshWood.getBrewItemKey(), new BrewNamePartModifier(0, 0, false, 1, 0), new AltarPower(50)) {
         protected void onPrepareSplashPotion(World world, ModifiersImpact modifiers) {
            if(modifiers.extent < 1) {
               ++modifiers.extent;
            }

         }
      });
      this.register(new BrewActionImpactModifier(Dye.COCOA_BEANS.getBrewItemKey(), new BrewNamePartModifier(0, 0, false, 1, 0), new AltarPower(100)) {
         protected void onPrepareSplashPotion(World world, ModifiersImpact modifiers) {
            if(modifiers.extent < 2) {
               ++modifiers.extent;
            }

         }
      });
      this.register(new BrewActionImpactModifier(new BrewItemKey(Witchery.Blocks.WISPY_COTTON), new BrewNamePartModifier(0, 0, false, 1, 0), new AltarPower(150)) {
         protected void onPrepareSplashPotion(World world, ModifiersImpact modifiers) {
            if(modifiers.extent < 3) {
               ++modifiers.extent;
            }

         }
      });
      this.register(new BrewActionImpactModifier(Witchery.Items.GENERIC.itemBelladonnaFlower.getBrewItemKey(), new BrewNamePartModifier(0, 0, false, 0, 1), new AltarPower(50)) {
         protected void onPrepareSplashPotion(World world, ModifiersImpact modifiers) {
            if(modifiers.lifetime < 1) {
               ++modifiers.lifetime;
            }

         }
      });
      this.register(new BrewActionImpactModifier(Dye.LAPIS_LAZULI.getBrewItemKey(), new BrewNamePartModifier(0, 0, false, 0, 1), new AltarPower(100)) {
         protected void onPrepareSplashPotion(World world, ModifiersImpact modifiers) {
            if(modifiers.lifetime < 2) {
               ++modifiers.lifetime;
            }

         }
      });
      this.register(new BrewActionImpactModifier(new BrewItemKey(Blocks.end_stone), new BrewNamePartModifier(0, 0, false, 0, 1), new AltarPower(150)) {
         protected void onPrepareSplashPotion(World world, ModifiersImpact modifiers) {
            if(modifiers.lifetime < 3) {
               ++modifiers.lifetime;
            }

         }
      });
      Dye[] arr$ = Dye.values();
      int len$ = arr$.length;

      int i$;
      Dye dye;
      for(i$ = 0; i$ < len$; ++i$) {
         dye = arr$[i$];
         this.register(new BrewActionSetColor(new BrewItemKey(Blocks.wool, 15 - dye.damageValue), new AltarPower(0), dye.rgb));
      }

      this.register(new BrewActionModifier(new BrewItemKey(Items.gold_nugget), (BrewNamePart)null, new AltarPower(50)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            modifiers.noParticles = true;
         }
      });
      this.register(new BrewActionModifier(Witchery.Items.GENERIC.itemRowanBerries.getBrewItemKey(), (BrewNamePart)null, new AltarPower(50)) {
         public int getDrinkSpeedModifiers() {
            return -8;
         }
      });
      this.register(new BrewActionModifier(Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.getBrewItemKey(), (BrewNamePart)null, new AltarPower(0)) {
         public int getDrinkSpeedModifiers() {
            return -4;
         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Witchery.Blocks.SPANISH_MOSS), (BrewNamePart)null, new AltarPower(50)) {
         public int getDrinkSpeedModifiers() {
            return -4;
         }
      });
      this.register(new BrewActionModifier(Witchery.Items.GENERIC.itemMandrakeRoot.getBrewItemKey(), (BrewNamePart)null, new AltarPower(0)) {
         public boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
            totalEffects.increaseAvailableLevelIf(new EffectLevel(1), new EffectLevel(1));
            return true;
         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Items.nether_wart), (BrewNamePart)null, new AltarPower(50)) {
         public boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
            totalEffects.increaseAvailableLevelIf(new EffectLevel(2), new EffectLevel(2));
            return true;
         }
      });
      this.register(new BrewActionModifier(Witchery.Items.GENERIC.itemTearOfTheGoddess.getBrewItemKey(), (BrewNamePart)null, new AltarPower(100)) {
         public boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
            totalEffects.increaseAvailableLevelIf(new EffectLevel(2), new EffectLevel(4));
            return true;
         }
      });
      this.register(new BrewActionModifier(Witchery.Items.GENERIC.itemDiamondVapour.getBrewItemKey(), (BrewNamePart)null, new AltarPower(150)) {
         public boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
            totalEffects.increaseAvailableLevelIf(new EffectLevel(2), new EffectLevel(6));
            return true;
         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Items.diamond), (BrewNamePart)null, new AltarPower(150)) {
         public boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
            totalEffects.increaseAvailableLevelIf(new EffectLevel(2), new EffectLevel(8));
            return true;
         }
      }).setYieldModifier(new ModifierYield(-2));
      this.register(new BrewActionModifier(new BrewItemKey(Items.nether_star), new BrewNamePartModifier(0, 0, false, 0, 0, true), new AltarPower(150)) {
         public boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
            totalEffects.increaseAvailableLevelIf(new EffectLevel(4), new EffectLevel(10));
            return true;
         }
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            modifiers.powerhCeilingDisabled = true;
         }
      });
      this.register(new BrewActionModifier(Witchery.Items.GENERIC.itemKobolditePentacle.getBrewItemKey(), (BrewNamePart)null, new AltarPower(1000)) {
         public boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
            totalEffects.increaseAvailableLevelIf(new EffectLevel(6), new EffectLevel(16));
            return true;
         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Items.glowstone_dust), new BrewNamePartModifier(1, 0, false, 0, 0), new AltarPower(50)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            if(modifiers.strength < 1) {
               modifiers.increaseStrength(1);
            }

         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Items.blaze_rod), new BrewNamePartModifier(1, 0, false, 0, 0), new AltarPower(100)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            if(modifiers.strength < 2) {
               modifiers.increaseStrength(1);
            }

         }
      });
      this.register(new BrewActionModifier(Witchery.Items.GENERIC.itemAttunedStoneCharged.getBrewItemKey(), new BrewNamePartModifier(1, 0, false, 0, 0), new AltarPower(150)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            if(modifiers.strength < 3) {
               modifiers.increaseStrength(1);
            }

         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Items.redstone), new BrewNamePartModifier(0, 1, false, 0, 0), new AltarPower(50)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            if(modifiers.duration < 1) {
               modifiers.increaseDuration(1);
            }

         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Blocks.obsidian), new BrewNamePartModifier(0, 1, false, 0, 0), new AltarPower(100)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            if(modifiers.duration < 2) {
               modifiers.increaseDuration(1);
            }

         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Witchery.Items.SEEDS_MINDRAKE), new BrewNamePartModifier(0, 1, false, 0, 0), new AltarPower(150)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            if(modifiers.duration < 3) {
               modifiers.increaseDuration(1);
            }

         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Items.fermented_spider_eye), new BrewNamePartModifier(0, 0, true, 0, 0), new AltarPower(25)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            modifiers.inverted = true;
         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Items.netherbrick), (BrewNamePart)null, new AltarPower(50)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            modifiers.disableBlockTarget = true;
         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Items.brick), (BrewNamePart)null, new AltarPower(50)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            modifiers.disableEntityTarget = true;
         }
      });
      this.register(new BrewActionModifier(new BrewItemKey(Items.fish, 2), (BrewNamePart)null, new AltarPower(200)) {
         public void augmentEffectModifiers(ModifiersEffect modifiers) {
            modifiers.strengthCeilingDisabled = true;
         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Items.snowball), (new BrewNamePart("witchery:brew.snow")).setBaseDuration(TimeUtil.minsToTicks(3)), new AltarPower(0), new Probability(1.0D), new EffectLevel(1)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            PotionSnowTrail.createSnowCovering(world, x, y, z, 2 + 2 * modifiers.getStrength(), modifiers.caster);
         }
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            BrewPotionEffect.applyPotionEffect(targetEntity, modifiers, Witchery.Potions.SNOW_TRAIL, TimeUtil.minsToTicks(3), modifiers.noParticles, modifiers.caster);
         }
      });
      this.register(new BrewPotionEffect(new BrewItemKey(Items.fish, 0), new BrewNamePart("witchery:brew.swimming"), new AltarPower(0), new Probability(1.0D), Witchery.Potions.SWIMMING, (long)TimeUtil.minsToTicks(3), new EffectLevel(1)));
      arr$ = new Dye[]{Dye.ROSE_RED, Dye.CACTUS_GREEN, Dye.PURPLE_DYE, Dye.CYAN_DYE, Dye.LIGHT_GRAY_DYE, Dye.GRAY_DYE, Dye.PINK_DYE, Dye.LIME_DYE, Dye.DANDELION_YELLOW, Dye.LIGHT_BLUE_DYE, Dye.MAGENTA_DYE, Dye.ORANGE_DYE};
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         final Dye dye0 = arr$[i$];
         this.register(new BrewCurseEffect(dye0.getBrewItemKey(), new BrewNamePart("witchery:potion.colorful." + dye0.unlocalizedName), new AltarPower(0), new Probability(1.0D), Witchery.Potions.COLORFUL, (long)TimeUtil.secsToTicks(90), new EffectLevel(1), false) {
            public void applyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
               if(!modifiers.disableEntityTarget) {
                  if(!modifiers.protectedFromNegativePotions) {
                     targetEntity.addPotionEffect(new PotionEffect(super.potion.id, super.baseDuration, dye0.ordinal(), true));
                  }

                  modifiers.reset();
               }

            }
         });
      }

      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemEnderDew.getBrewItemKey(), new BrewNamePart("witchery:potion.enderinhibition"), new AltarPower(200), new Probability(1.0D), Witchery.Potions.ENDER_INHIBITION, (long)TimeUtil.secsToTicks(90), new EffectLevel(1)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.wheat), new BrewNamePart("witchery:brew.moonshine"), new AltarPower(0), new Probability(1.0D), Witchery.Potions.FEEL_NO_PAIN, (long)TimeUtil.secsToTicks(90), new EffectLevel(1)));
      this.register(new BrewActionEffect(new BrewItemKey(Items.coal), new BrewNamePart("witchery:brew.extinguish"), new AltarPower(0), new Probability(1.0D), new EffectLevel(1)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            if(modifiers.getStrength() > 1 || !world.provider.isHellWorld) {
               if(modifiers.getStrength() > 0 && targetEntity instanceof EntityBlaze) {
                  targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(modifiers.caster, modifiers.caster), (float)(modifiers.getStrength() + 1) * 4.0F);
               }

               if(targetEntity.isBurning()) {
                  targetEntity.extinguish();
               }
            }

         }
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, final int radius, final ModifiersEffect modifiers, ItemStack stack) {
            if(modifiers.getStrength() > 1 || !world.provider.isHellWorld) {
               (new BlockActionCircle() {
                  public void onBlock(World world, int x, int y, int z) {
                     for(int dy = y - radius; dy <= y + radius; ++dy) {
                        Block block = world.getBlock(x, dy, z);
                        if(block == Blocks.fire && BlockProtect.checkModsForBreakOK(world, x, dy, z, block, world.getBlockMetadata(x, dy, z), modifiers.caster) && BlockProtect.canBreak(block, world)) {
                           world.setBlockToAir(x, dy, z);
                           SoundEffect.RANDOM_FIZZ.playAt(world, (double)x, (double)dy, (double)z, 1.0F, 2.0F);
                        }
                     }

                  }
               }).processFilledCircle(world, x, y, z, radius + (modifiers.ritualised?5:0));
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.stone), new BrewNamePart("witchery:brew.dissipate"), new AltarPower(0), new Probability(1.0D), new EffectLevel(1)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            if(targetEntity instanceof EntitySummonedUndead) {
               targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(modifiers.caster, modifiers.caster), (float)(modifiers.getStrength() + 1) * 5.0F);
            }

         }
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, final int radius, final ModifiersEffect modifiers, ItemStack stack) {
            (new BlockActionCircle() {
               public void onBlock(World world, int x, int y, int z) {
                  for(int dy = y - radius; dy <= y + radius; ++dy) {
                     Block block = world.getBlock(x, dy, z);
                     if(block == Witchery.Blocks.BREW_GAS) {
                        if(BlockProtect.checkModsForBreakOK(world, x, dy, z, block, world.getBlockMetadata(x, dy, z), modifiers.caster) && BlockProtect.canBreak(block, world)) {
                           world.setBlockToAir(x, dy, z);
                           SoundEffect.RANDOM_FIZZ.playAt(world, (double)x, (double)dy, (double)z, 1.0F, 2.0F);
                        }
                     } else if(block instanceof IFluidBlock && modifiers.getStrength() >= 1) {
                        IFluidBlock fluidBlock = (IFluidBlock)block;
                        if(fluidBlock.getFluid() != null && fluidBlock.getFluid().isGaseous() && BlockProtect.checkModsForBreakOK(world, x, dy, z, block, world.getBlockMetadata(x, dy, z), modifiers.caster) && BlockProtect.canBreak(block, world)) {
                           world.setBlockToAir(x, dy, z);
                           SoundEffect.RANDOM_FIZZ.playAt(world, (double)x, (double)dy, (double)z, 1.0F, 2.0F);
                        }
                     }
                  }

               }
            }).processFilledCircle(world, x, y, z, radius);
         }
      });
      this.register(new BrewActionBlockCircle(new BrewItemKey(Blocks.yellow_flower), new BrewNamePart("witchery:brew.flowers"), new AltarPower(200), new EffectLevel(1)) {

         private final Block[] BLOCKS;

         {
            this.BLOCKS = new Block[]{Blocks.yellow_flower, Blocks.red_flower, Blocks.red_flower, Blocks.red_flower, Blocks.red_flower, Blocks.red_flower, Blocks.red_flower, Blocks.red_flower, Blocks.red_flower, Blocks.red_flower};
         }
         protected void onCircleBlock(World world, int x, int y, int z, ModifiersEffect modifiers, Count counter) {
            for(int dy = y - 1; dy <= y + 1; ++dy) {
               if(BlockUtil.isReplaceableBlock(world, x, dy, z, modifiers.caster) && !world.getBlock(x, dy, z).getMaterial().isLiquid() && Blocks.yellow_flower.canPlaceBlockAt(world, x, dy, z) && world.rand.nextInt(8 - modifiers.getStrength()) == 0) {
                  int flowerIndex = world.rand.nextInt(this.BLOCKS.length);
                  int flowerMeta = Math.max(flowerIndex - 1, 0);
                  world.setBlock(x, dy, z, this.BLOCKS[flowerIndex], flowerMeta, 3);
               }
            }

         }
      });
      this.register(new BrewActionBlockCircle(Dye.BONE_MEAL.getBrewItemKey(), new BrewNamePart("witchery:brew.fertilization"), new AltarPower(250), new EffectLevel(1)) {

         private final ItemStack BONEMEAL;

         {
            this.BONEMEAL = Dye.BONE_MEAL.createStack();
         }
         protected void onCircleBlock(World world, int x, int y, int z, ModifiersEffect modifiers, Count counter) {
            for(int dy = y + 1; dy >= y - 1; --dy) {
               Block block = world.getBlock(x, dy, z);
               if(block instanceof IGrowable || block instanceof IPlantable || block instanceof BlockWitchCrop) {
                  for(int i = 0; i <= modifiers.getStrength(); ++i) {
                     ItemDye.func_150919_a(this.BONEMEAL, world, x, dy, z);
                  }

                  return;
               }
            }

         }
      });
      this.register(new BrewActionBlockCircle(new BrewItemKey(Items.apple), new BrewNamePart("witchery:brew.harvesting"), new AltarPower(0), new EffectLevel(1)) {
         protected void onCircleBlock(World world, int x, int y, int z, ModifiersEffect modifiers, Count counter) {
            for(int dy = y - 1; dy <= y + 1; ++dy) {
               Block block = world.getBlock(x, dy, z);
               if(block instanceof BlockBush) {
                  int meta = world.getBlockMetadata(x, dy, z);
                  ArrayList drops = new ArrayList();
                  drops.addAll(block.getDrops(world, x, dy, z, meta, Math.max(modifiers.getStrength() - 1, 0)));
                  world.setBlockToAir(x, dy, z);
                  counter.increment();
                  if(world.rand.nextInt(counter.get()) == 0) {
                     world.playAuxSFX(2001, x, dy, z, Block.getIdFromBlock(block) + (meta << 12));
                  }

                  Iterator i$ = drops.iterator();

                  while(i$.hasNext()) {
                     ItemStack drop = (ItemStack)i$.next();
                     world.spawnEntityInWorld(new EntityItem(world, 0.5D + (double)x, 0.5D + (double)dy, 0.5D + (double)z, drop.copy()));
                  }
               }
            }

         }
      });
      this.register(new BrewActionBlockCircle(new BrewItemKey(Blocks.dirt), new BrewNamePart("witchery:brew.tilling"), new AltarPower(0), new EffectLevel(1)) {
         protected void onCircleBlock(World world, int x, int y, int z, ModifiersEffect modifiers, Count counter) {
            for(int dy = y - 1; dy <= y + 1; ++dy) {
               Block block = world.getBlock(x, dy, z);
               if((block == Blocks.grass || block == Blocks.dirt || block == Blocks.sand && modifiers.getStrength() > 0 || block == Blocks.netherrack && modifiers.getStrength() > 1 || block == Blocks.soul_sand && modifiers.getStrength() > 2) && world.isAirBlock(x, dy + 1, z)) {
                  world.setBlock(x, dy, z, Blocks.farmland);
                  counter.increment();
                  if(world.rand.nextInt(counter.get()) == 0) {
                     world.playSoundEffect((double)((float)x + 0.5F), (double)((float)dy + 0.5F), (double)((float)z + 0.5F), block.stepSound.getStepResourcePath(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                  }
               }
            }

         }
      });
      this.register(new BrewActionPlanting(new BrewItemKey(Items.wheat_seeds), new BrewNamePart("witchery:brew.planting"), new AltarPower(0), new EffectLevel(1)));
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.brown_mushroom), new BrewNamePart("witchery:brew.pruning"), new AltarPower(0), new Probability(1.0D), new EffectLevel(1)) {
         protected void doApplyToBlock(World world, int posX, int posY, int posZ, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {
            int BLOCK_RADIUS = radius - 1;
            int BLOCK_RADIUS_SQ = BLOCK_RADIUS * BLOCK_RADIUS;
            int blockX = MathHelper.floor_double((double)posX);
            int blockY = MathHelper.floor_double((double)posY);
            int blockZ = MathHelper.floor_double((double)posZ);

            for(int y = blockY - BLOCK_RADIUS; y <= blockY + BLOCK_RADIUS; ++y) {
               for(int x = blockX - BLOCK_RADIUS; x <= blockX + BLOCK_RADIUS; ++x) {
                  for(int z = blockZ - BLOCK_RADIUS; z <= blockZ + BLOCK_RADIUS; ++z) {
                     if(Coord.distanceSq((double)x, (double)y, (double)z, (double)blockX, (double)blockY, (double)blockZ) <= (double)BLOCK_RADIUS_SQ && BlockProtect.checkModsForBreakOK(world, x, y, z, modifiers.caster)) {
                        Material material = world.getBlock(x, y, z).getMaterial();
                        if(material != null && (material == Material.leaves || (material == Material.plants || material == Material.vine) && material.isReplaceable())) {
                           Block blockID = world.getBlock(x, y, z);
                           if(!(blockID instanceof BlockCircle) && !(blockID instanceof BlockCircleGlyph)) {
                              blockID.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), modifiers.getStrength());
                              world.setBlockToAir(x, y, z);
                           }
                        }
                     }
                  }
               }
            }

         }
      });
      this.register(new BrewActionFelling(Items.string, 0, new AltarPower(0), new EffectLevel(1)));
      this.register(new BrewActionEffect(new BrewItemKey(Items.flint), new BrewNamePart("witchery:brew.pulverisation"), new AltarPower(250), new Probability(1.0D), new EffectLevel(1)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, final ModifiersEffect modifiers, ItemStack actionStack) {
            if(!world.isRemote) {
               (new BlockActionSphere() {
                  public void onBlock(World world, int x, int y, int z) {
                     Block block = world.getBlock(x, y, z);
                     if(BlockProtect.checkModsForBreakOK(world, x, y, z, block, world.getBlockMetadata(x, y, z), modifiers.caster) && BlockProtect.canBreak(block, world)) {
                        if(block == Blocks.stone) {
                           world.setBlock(x, y, z, Blocks.cobblestone);
                        } else if(block == Blocks.cobblestone) {
                           world.setBlock(x, y, z, Blocks.gravel);
                        } else if(block != Blocks.gravel && block != Blocks.sandstone) {
                           if(block != Blocks.sand) {
                              return;
                           }

                           world.setBlockToAir(x, y, z);
                           EntityUtil.spawnEntityInWorld(world, new EntityItem(world, (double)x, (double)y, (double)z, new ItemStack(Blocks.sand)));
                        } else {
                           world.setBlock(x, y, z, Blocks.sand);
                        }

                        ParticleEffect.SMOKE.send(SoundEffect.NONE, world, (double)x, (double)(y + 1), (double)z, 0.25D, 0.25D, 16);
                     }

                  }
               }).drawFilledSphere(world, x, y, z, Math.max(radius - 1, 1));
               SoundEffect.MOB_GHAST_FIREBALL.playAt(world, (double)x, (double)y, (double)z, 0.5F, 2.0F);
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.sand), new BrewNamePart("witchery:brew.tidehold"), new AltarPower(0), new Probability(1.0D), new EffectLevel(1)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, final ModifiersEffect modifiers, ItemStack actionStack) {
            if(!world.isRemote) {
               (new BlockActionSphere() {
                  public void onBlock(World world, int x, int y, int z) {
                     Block block = world.getBlock(x, y, z);
                     if(block == Blocks.water || block == Blocks.flowing_water) {
                        Witchery.Blocks.SLURP.replaceBlockAt(world, x, y, z, modifiers.getModifiedDuration(TimeUtil.secsToTicks(10)));
                     }

                  }
               }).drawFilledSphere(world, x, y, z, Math.max(radius + 2, 1));
            }

         }
      });
      this.register(new BrewActionLilify(new BrewItemKey(Blocks.waterlily), new BrewNamePart("witchery:brew.lilify"), new AltarPower(200), new EffectLevel(1)));
      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemWolfsbane.getBrewItemKey(), new BrewNamePart("witchery:potion.wolfsbane"), new AltarPower(0), new Probability(1.0D), Witchery.Potions.WOLFSBANE, (long)TimeUtil.secsToTicks(60), new EffectLevel(1)));
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemPurifiedMilk.getBrewItemKey(), new BrewNamePart("witchery:brew.removedebuffs"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            ArrayList effectsToRemove = new ArrayList();
            Collection effects = targetEntity.getActivePotionEffects();
            Iterator i$ = effects.iterator();

            while(i$.hasNext()) {
               PotionEffect potion = (PotionEffect)i$.next();
               Potion potion1 = Potion.potionTypes[potion.getPotionID()];
               if(PotionBase.isDebuff(potion1)) {
                  if(PotionBase.isCurable(potion1) && potion.getAmplifier() <= modifiers.getStrength()) {
                     effectsToRemove.add(potion1);
                  }

                  if(potion1 == Witchery.Potions.DISEASED && modifiers.getStrength() >= 2) {
                     effectsToRemove.add(potion1);
                     if(targetEntity instanceof EntityPlayer) {
                        EntityPlayer R = (EntityPlayer)targetEntity;
                        ExtendedPlayer coord = ExtendedPlayer.get(R);
                        if(coord != null) {
                           coord.clearCachedIncurablePotionEffect(potion1);
                        }
                     }

                     if(modifiers.getStrength() >= 3) {
                        boolean var18 = true;
                        Coord var17 = new Coord(targetEntity);

                        for(int x = -9; x <= 9; ++x) {
                           for(int z = -9; z <= 9; ++z) {
                              for(int y = -9; y <= 9; ++y) {
                                 Block block = world.getBlock(var17.x + x, var17.y + y, var17.z + z);
                                 if(block == Witchery.Blocks.DISEASE) {
                                    world.setBlockToAir(var17.x + x, var17.y + y, var17.z + z);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            i$ = effectsToRemove.iterator();

            while(i$.hasNext()) {
               Potion var16 = (Potion)i$.next();
               targetEntity.removePotionEffect(var16.id);
            }

         }
         protected void doApplyRitualToEntity(World world, EntityLivingBase targetEntity, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
            ArrayList effectsToRemove = new ArrayList();
            ArrayList effectsToAdd = new ArrayList();
            Collection effects = targetEntity.getActivePotionEffects();
            int modifiedStrength = modifiers.getStrength();
            Iterator i$ = effects.iterator();

            PotionEffect potionEffect;
            while(i$.hasNext()) {
               potionEffect = (PotionEffect)i$.next();
               Potion player = Potion.potionTypes[potionEffect.getPotionID()];
               if(PotionBase.isDebuff(player)) {
                  if(potionEffect.getAmplifier() < modifiedStrength) {
                     effectsToRemove.add(player);
                     if(world.rand.nextDouble() < 0.01D) {
                        effectsToAdd.add(new PotionEffect(potionEffect.getPotionID(), potionEffect.getDuration(), potionEffect.getAmplifier() + 1));
                     }
                  } else if(potionEffect.getAmplifier() == modifiedStrength) {
                     effectsToRemove.add(player);
                     if(world.rand.nextDouble() < 0.25D) {
                        effectsToAdd.add(new PotionEffect(potionEffect.getPotionID(), potionEffect.getDuration(), potionEffect.getAmplifier() + 1));
                     }
                  } else {
                     effectsToRemove.add(player);
                     if(world.rand.nextDouble() < 0.75D) {
                        effectsToAdd.add(new PotionEffect(potionEffect.getPotionID(), potionEffect.getDuration(), potionEffect.getAmplifier() + 1));
                     }
                  }
               }
            }

            i$ = effectsToRemove.iterator();

            while(i$.hasNext()) {
               Potion potionEffect1 = (Potion)i$.next();
               targetEntity.removePotionEffect(potionEffect1.id);
               if(!PotionBase.isCurable(potionEffect1) && targetEntity instanceof EntityPlayer) {
                  EntityPlayer player1 = (EntityPlayer)targetEntity;
                  ExtendedPlayer playerEx = ExtendedPlayer.get(player1);
                  if(playerEx != null) {
                     playerEx.clearCachedIncurablePotionEffect(potionEffect1);
                  }
               }
            }

            i$ = effectsToAdd.iterator();

            while(i$.hasNext()) {
               potionEffect = (PotionEffect)i$.next();
               targetEntity.addPotionEffect(potionEffect);
            }

            if(effectsToAdd.isEmpty()) {
               ParticleEffect.SPELL.send(SoundEffect.RANDOM_LEVELUP, targetEntity, 0.5D, 2.0D, 16);
            } else {
               ParticleEffect.MOB_SPELL.send(SoundEffect.MOB_ENDERDRAGON_GROWL, targetEntity, 0.5D, 2.0D, 16);
            }

         }
      });
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemReekOfMisfortune.getBrewItemKey(), new BrewNamePart("witchery:brew.removebuffs"), new AltarPower(250), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            ArrayList effectsToRemove = new ArrayList();
            Collection effects = targetEntity.getActivePotionEffects();
            Iterator i$ = effects.iterator();

            while(i$.hasNext()) {
               PotionEffect potion = (PotionEffect)i$.next();
               Potion potion1 = Potion.potionTypes[potion.getPotionID()];
               if(!PotionBase.isDebuff(potion1) && PotionBase.isCurable(potion1) && potion.getAmplifier() <= modifiers.getStrength()) {
                  effectsToRemove.add(potion1);
               }
            }

            i$ = effectsToRemove.iterator();

            while(i$.hasNext()) {
               Potion potion2 = (Potion)i$.next();
               targetEntity.removePotionEffect(potion2.id);
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.cobblestone), new BrewNamePart("witchery:brew.lavahold"), new AltarPower(100), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, final ModifiersEffect modifiers, ItemStack actionStack) {
            if(!world.isRemote) {
               (new BlockActionSphere() {
                  public void onBlock(World world, int x, int y, int z) {
                     Block block = world.getBlock(x, y, z);
                     if(block == Blocks.lava || block == Blocks.flowing_lava) {
                        Witchery.Blocks.SLURP.replaceBlockAt(world, x, y, z, modifiers.getModifiedDuration(TimeUtil.secsToTicks(10)));
                     }

                  }
               }).drawFilledSphere(world, x, y, z, Math.max(radius + 2, 1));
            }

         }
      });
      this.register(new BrewPotionEffect(new BrewItemKey(Witchery.Blocks.BRAMBLE, 0), new BrewNamePart("witchery:potion.repellattacker"), new AltarPower(250), new Probability(1.0D), Witchery.Potions.REPELL_ATTACKER, (long)TimeUtil.secsToTicks(90), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Blocks.gravel), new BrewNamePart("witchery:potion.gasmask"), new AltarPower(100), new Probability(1.0D), Witchery.Potions.GAS_MASK, (long)TimeUtil.secsToTicks(90), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.spider_eye), new BrewNamePart("witchery:brew.poison"), new AltarPower(0), new Probability(1.0D), Potion.poison, (long)TimeUtil.secsToTicks(45), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.sugar), new BrewNamePart("witchery:brew.movespeed", "witchery:brew.moveslow"), new AltarPower(100), new Probability(1.0D), Potion.moveSpeed, (long)TimeUtil.minsToTicks(3), Potion.moveSlowdown, (long)TimeUtil.secsToTicks(90), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.fish, 3), new BrewNamePart("witchery:brew.waterbreathing"), new AltarPower(100), new Probability(1.0D), Potion.waterBreathing, (long)TimeUtil.minsToTicks(3), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.magma_cream), new BrewNamePart("witchery:brew.resistfire"), new AltarPower(100), new Probability(1.0D), Potion.fireResistance, (long)TimeUtil.minsToTicks(3), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.golden_carrot), new BrewNamePart("witchery:brew.nightvision", "witchery:brew.invisibility"), new AltarPower(200), new Probability(1.0D), Potion.nightVision, (long)TimeUtil.minsToTicks(3), Potion.invisibility, (long)TimeUtil.minsToTicks(3), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.ghast_tear), new BrewNamePart("witchery:brew.regeneration", "witchery:brew.poison"), new AltarPower(200), new Probability(1.0D), Potion.regeneration, (long)TimeUtil.secsToTicks(45), Potion.poison, (long)TimeUtil.secsToTicks(45), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.blaze_powder), new BrewNamePart("witchery:brew.damageboost", "witchery:brew.weakness"), new AltarPower(200), new Probability(1.0D), Potion.damageBoost, (long)TimeUtil.minsToTicks(3), Potion.weakness, (long)TimeUtil.secsToTicks(90), new EffectLevel(2)));
      this.register((new BrewPotionEffect(new BrewItemKey(Items.speckled_melon), new BrewNamePart("witchery:brew.healing", "witchery:brew.harming"), new AltarPower(200), new Probability(1.0D), Potion.heal, 0L, Potion.harm, 0L, new EffectLevel(2))).setStrengthCeiling(1));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.reeds), new BrewNamePart("witchery:brew.floating"), new AltarPower(250), new Probability(1.0D), Witchery.Potions.FLOATING, (long)TimeUtil.secsToTicks(90), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.leather), new BrewNamePart("witchery:brew.jump"), new AltarPower(200), new Probability(1.0D), Potion.jump, (long)TimeUtil.minsToTicks(3), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.feather), new BrewNamePart("witchery:brew.featherfall"), new AltarPower(100), new Probability(1.0D), Witchery.Potions.FEATHER_FALL, (long)TimeUtil.minsToTicks(1), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Blocks.red_mushroom), new BrewNamePart("witchery:potion.poisonweapons"), new AltarPower(200), new Probability(1.0D), Witchery.Potions.POISON_WEAPONS, (long)TimeUtil.secsToTicks(90), new EffectLevel(2)));
      this.register(new BrewPotionEffect(new BrewItemKey(Blocks.web), new BrewNamePart("witchery:potion.reflectprojectiles", "witchery:potion.attractprojectiles"), new AltarPower(250), new Probability(1.0D), Witchery.Potions.REFLECT_PROJECTILES, (long)TimeUtil.secsToTicks(90), Witchery.Potions.ATTRACT_PROJECTILES, (long)TimeUtil.secsToTicks(45), new EffectLevel(2)));
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemBatBall.getBrewItemKey(), new BrewNamePart("witchery:brew.batburst"), new AltarPower(1000), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            int BAT_COUNT = (modifiers.powerScalingFactor == 1.0D && !modifiers.isGlancing && modifiers.strengthPenalty <= 0?10:1) + modifiers.getStrength();
            this.explodeBats(world, new Coord(x, y, z), side, BAT_COUNT);
         }
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            if(!(targetEntity instanceof EntityOwl) && !(targetEntity instanceof EntityBat)) {
               int BAT_COUNT = (modifiers.powerScalingFactor == 1.0D && !modifiers.isGlancing?10:1) + modifiers.getStrength();
               if(modifiers.powerScalingFactor == 1.0D && !modifiers.isGlancing || world.rand.nextInt(20) == 0) {
                  this.explodeBats(world, new Coord(targetEntity), ForgeDirection.UP, BAT_COUNT);
               }
            }

         }
         private void explodeBats(World world, Coord coord, ForgeDirection side, int bats) {
            int x = coord.x + side.offsetX;
            int z = coord.z + side.offsetZ;
            int y = coord.y + side.offsetY;
            int NUM_BATS = bats;

            for(int i = 0; i < NUM_BATS; ++i) {
               EntityBat bat = new EntityBat(world);
               EntityUtil.setNoDrops(bat);
               bat.setLocationAndAngles((double)x, (double)y, (double)z, 0.0F, 0.0F);
               world.spawnEntityInWorld(bat);
            }

            ParticleEffect.EXPLODE.send(SoundEffect.MOB_ENDERMEN_PORTAL, world, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z, 3.0D, 3.0D, 16);
         }
      });
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemOwletsWing.getBrewItemKey(), new BrewNamePart("witchery:brew.bodega"), new AltarPower(1000), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            if(modifiers.caster != null && Familiar.hasActiveBroomMasteryFamiliar(modifiers.caster) && !(targetEntity instanceof EntityOwl) && !(targetEntity instanceof EntityBat)) {
               int BIRD_COUNT = (modifiers.powerScalingFactor == 1.0D && !modifiers.isGlancing?3 + world.rand.nextInt(2):1) + modifiers.getStrength();
               if(modifiers.powerScalingFactor == 1.0D && !modifiers.isGlancing || world.rand.nextInt(20) == 0) {
                  this.explodeBirds(world, targetEntity, BIRD_COUNT);
               }
            }

         }
         private void explodeBirds(World world, EntityLivingBase victim, int birds) {
            for(int i = 0; i < birds; ++i) {
               EntityOwl owl = new EntityOwl(world);
               owl.setLocationAndAngles(victim.posX - 2.0D + (double)world.rand.nextInt(5), victim.posY + (double)victim.height + 1.0D + (double)world.rand.nextInt(2), victim.posZ - 2.0D + (double)world.rand.nextInt(5), 0.0F, 0.0F);
               owl.setAttackTarget(victim);
               owl.setTimeToLive(400);
               world.spawnEntityInWorld(owl);
               ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, owl, 1.0D, 1.0D, 16);
            }

         }
      });
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemBreathOfTheGoddess.getBrewItemKey(), new BrewNamePart("witchery:brew.airhike"), new AltarPower(750), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            double motionY = 0.6D + 0.2D * (double)modifiers.getStrength();
            targetEntity.fallDistance = 0.0F;
            if(targetEntity instanceof EntityPlayer) {
               Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(0.0D, motionY, 0.0D)), (EntityPlayer)targetEntity);
            } else {
               targetEntity.motionY = motionY;
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Items.slime_ball), new BrewNamePart("witchery:brew.frogtongue"), new AltarPower(150), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            if(modifiers.getStrength() > 0 && !(modifiers.caster instanceof FakePlayer)) {
               EntityUtil.pullTowards(world, targetEntity, new EntityPosition(modifiers.caster), 0.05D, 0.0D);
            } else {
               EntityUtil.pullTowards(world, targetEntity, modifiers.impactLocation, 0.05D, 0.0D);
            }

         }
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {
            double R = (double)radius;
            double R_SQ = R * R;
            EntityPosition position = modifiers.impactLocation;
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(position.x - R, position.y - R, position.z - R, position.x + R, position.y + R, position.z + R);
            List list1 = world.getEntitiesWithinAABB(Entity.class, bb);
            Iterator i$ = list1.iterator();

            while(i$.hasNext()) {
               Entity targetEntity = (Entity)i$.next();
               if(!(targetEntity instanceof EntityLivingBase)) {
                  double distanceSq = position.getDistanceSqToEntity(targetEntity);
                  if(distanceSq <= R_SQ) {
                     if(modifiers.getStrength() > 0 && !(modifiers.caster instanceof FakePlayer)) {
                        EntityUtil.pullTowards(world, targetEntity, new EntityPosition(modifiers.caster), 0.05D, 0.0D);
                     } else {
                        EntityUtil.pullTowards(world, targetEntity, modifiers.impactLocation, 0.05D, 0.0D);
                     }
                  }
               }
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.pumpkin), new BrewNamePart("witchery:brew.harmundead"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            int strength = Math.min(modifiers.getStrength(), modifiers.strengthCeilingDisabled?3:1);
            if(CreatureUtil.isUndead(targetEntity)) {
               targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(modifiers.caster, modifiers.caster), (float)MathHelper.ceiling_double_int((double)(10 << strength) * modifiers.powerScalingFactor));
            } else {
               targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(modifiers.caster, modifiers.caster), (float)MathHelper.ceiling_double_int((double)(3 << strength) * modifiers.powerScalingFactor));
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.red_flower, 1), new BrewNamePart("witchery:brew.harminsects"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            int strength = Math.min(modifiers.getStrength(), modifiers.strengthCeilingDisabled?3:1);
            if(CreatureUtil.isInsect(targetEntity)) {
               targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(modifiers.caster, modifiers.caster), (float)MathHelper.ceiling_double_int((double)(10 << strength) * modifiers.powerScalingFactor));
            } else {
               targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(modifiers.caster, modifiers.caster), (float)MathHelper.ceiling_double_int((double)(3 << strength) * modifiers.powerScalingFactor));
            }

         }
      });
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemOilOfVitriol.getBrewItemKey(), new BrewNamePart("witchery:brew.erosion"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, final ModifiersEffect modifiers, ItemStack actionStack) {
            final Count obsidianCount = new Count();

            for(int r = radius; r > 0; --r) {
               int var10000 = radius - r;
               (new BlockActionCircle() {
                  public void onBlock(World world, int x, int y, int z) {
                     Block block = world.getBlock(x, y, z);
                     if(BlockProtect.checkModsForBreakOK(world, x, y, z, block, world.getBlockMetadata(x, y, z), modifiers.caster) && BlockProtect.canBreak(block, world)) {
                        world.setBlockToAir(x, y, z);
                        ParticleEffect.SPLASH.send(SoundEffect.NONE, world, (double)x, (double)y, (double)z, 0.5D, 0.5D, 16);
                        obsidianCount.incrementBy(block == Blocks.obsidian?1:0);
                     }

                  }
               }).processFilledCircle(world, x, y, z, r);
            }

            SoundEffect.RANDOM_FIZZ.playAt(world, (double)x, (double)y, (double)z, 1.0F, 2.0F);
            SpawnUtil.spawnEntityItem(world, (double)x, (double)y, (double)z, Blocks.obsidian, obsidianCount.get());
         }
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            if(world.rand.nextInt(MathHelper.ceiling_double_int(5.0D / modifiers.powerScalingFactor)) == 0) {
               targetEntity.attackEntityFrom(DamageSource.causeThrownDamage(targetEntity, modifiers.caster), (float)MathHelper.ceiling_double_int(8.0D * modifiers.powerScalingFactor));
            }

            for(int slot = 0; slot < 5; ++slot) {
               ItemStack stack = targetEntity.getEquipmentInSlot(slot);
               if(stack != null && stack.isItemStackDamageable()) {
                  stack.damageItem(MathHelper.ceiling_double_int((50.0D + (double)world.rand.nextInt(25)) * modifiers.powerScalingFactor), modifiers.caster);
                  if(stack.getItemDamage() >= stack.getMaxDamage() || stack.stackSize <= 0) {
                     targetEntity.setCurrentItemOrArmor(slot, (ItemStack)null);
                  }
               }
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.netherrack), new BrewNamePart("witchery:brew.levelling"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int r, final ModifiersEffect modifiers, ItemStack actionStack) {
            final int y0 = modifiers.ritualised?y - 1:y;
            int radius = modifiers.ritualised?r + (modifiers.getStrength() + 1) * 3:r;
            final Count dirt = new Count();
            final Count stone = new Count();
            final Count sand = new Count();
            final Count sandstone = new Count();
            final Count netherrack = new Count();
            final Count endstone = new Count();
            final int s = modifiers.getStrength();
            int defaultAmount = modifiers.ritualised?64 + 32 * modifiers.getStrength():16;
            Block hitBlock = world.getBlock(x, y0, z);
            if(hitBlock == Blocks.dirt) {
               dirt.incrementBy(defaultAmount);
            } else if(hitBlock == Blocks.stone) {
               stone.incrementBy(defaultAmount);
            } else if(hitBlock == Blocks.sand) {
               sand.incrementBy(defaultAmount);
            } else if(hitBlock == Blocks.sandstone) {
               sandstone.incrementBy(defaultAmount);
            } else if(hitBlock == Blocks.netherrack) {
               netherrack.incrementBy(defaultAmount);
            } else if(hitBlock == Blocks.end_stone) {
               endstone.incrementBy(defaultAmount);
            }

            (new BlockActionCircle() {
               public void onBlock(World world, int x, int y, int z) {
                  for(int dy = y0 + 1; dy < y0 + 4 + s; ++dy) {
                     Block block = world.getBlock(x, dy, z);
                     if(block != Blocks.air && BlockProtect.checkModsForBreakOK(world, x, dy, z, block, world.getBlockMetadata(x, dy, z), modifiers.caster) && BlockProtect.canBreak(block, world)) {
                        if(block == Blocks.stone) {
                           stone.increment();
                        } else if(block == Blocks.sand) {
                           sand.increment();
                        } else if(block == Blocks.sandstone) {
                           sandstone.increment();
                        } else if(block == Blocks.netherrack) {
                           netherrack.increment();
                        } else if(block == Blocks.end_stone) {
                           endstone.increment();
                        } else {
                           dirt.increment();
                        }

                        world.setBlockToAir(x, dy, z);
                        ParticleEffect.SMOKE.send(SoundEffect.NONE, world, (double)x, (double)dy, (double)z, 0.5D, 0.5D, 16);
                     }
                  }

               }
            }).processFilledCircle(world, x, y0, z, radius);

            for(int dy = y0; dy >= y0 - (4 + s); --dy) {
               final int newy = dy;
               (new BlockActionCircle() {
                  public void onBlock(World world, int x, int y, int z) {
                     world.getBlock(x, newy, z);
                     if(BlockUtil.isReplaceableBlock(world, x, newy, z, modifiers.caster)) {
                        if(endstone.get() > 0) {
                           endstone.decrement();
                           world.setBlock(x, newy, z, Blocks.end_stone);
                        } else if(netherrack.get() > 0) {
                           netherrack.decrement();
                           world.setBlock(x, newy, z, Blocks.netherrack);
                        } else if(sandstone.get() > 0) {
                           sandstone.decrement();
                           world.setBlock(x, newy, z, Blocks.sandstone);
                        } else if(sand.get() > 0) {
                           sand.decrement();
                           world.setBlock(x, newy, z, Blocks.sand);
                        } else if(stone.get() > 0) {
                           stone.decrement();
                           world.setBlock(x, newy, z, Blocks.stone);
                        } else {
                           if(dirt.get() <= 0) {
                              return;
                           }

                           dirt.decrement();
                           world.setBlock(x, newy, z, Blocks.dirt);
                        }

                        ParticleEffect.SMOKE.send(SoundEffect.NONE, world, (double)x, (double)newy, (double)z, 0.5D, 0.5D, 16);
                     }

                  }
               }).processFilledCircle(world, x, y0, z, radius);
            }

            SoundEffect.RANDOM_FIZZ.playAt(world, (double)x, (double)y0, (double)z, 1.0F, 2.0F);
         }
      });
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemWeb.getBrewItemKey(), new BrewNamePart("witchery:brew.webs"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            this.placeWeb(world, new Coord(x, y, z), modifiers, side, modifiers.caster);
         }
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            this.placeWeb(world, new Coord(targetEntity), modifiers, ForgeDirection.UNKNOWN, modifiers.caster);
         }
         private void placeWeb(World world, Coord coord, ModifiersEffect modifiers, ForgeDirection side, EntityPlayer source) {
            Coord location = BlockUtil.getClosestPlantableBlock(world, coord.x, coord.y, coord.z, side, source, true);
            if(location != null) {
               Block web = Witchery.Blocks.WEB;
               BlockUtil.setBlockIfReplaceable(world, location.x, location.y, location.z, web);
               if(modifiers.getStrength() > 0) {
                  BlockUtil.setBlockIfReplaceable(world, location.x + 1, location.y, location.z, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x - 1, location.y, location.z, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x, location.y, location.z + 1, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x, location.y, location.z - 1, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x, location.y + 1, location.z, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x, location.y - 1, location.z, web);
               }

               if(modifiers.getStrength() > 1) {
                  BlockUtil.setBlockIfReplaceable(world, location.x + 1, location.y, location.z + 1, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x - 1, location.y, location.z + 1, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x + 1, location.y, location.z - 1, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x - 1, location.y, location.z - 1, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x + 1, location.y + 1, location.z, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x - 1, location.y + 1, location.z, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x, location.y + 1, location.z + 1, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x, location.y + 1, location.z - 1, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x, location.y - 1, location.z + 1, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x, location.y - 1, location.z - 1, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x + 1, location.y - 1, location.z, web);
                  BlockUtil.setBlockIfReplaceable(world, location.x - 1, location.y - 1, location.z, web);
               }
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.vine), (new BrewNamePart("witchery:brew.vines")).setBaseDuration(TimeUtil.secsToTicks(90)), new AltarPower(150), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            boolean meta = false;
            byte var14;
            switch(WitcheryBrewRegistry.NamelessClass1178169399.$SwitchMap$net$minecraftforge$common$util$ForgeDirection[side.ordinal()]) {
            case 1:
               var14 = 2;
               break;
            case 2:
               var14 = 3;
               break;
            case 3:
               var14 = 4;
               break;
            case 4:
               var14 = 5;
               break;
            default:
               return;
            }

            Block vine = Witchery.Blocks.VINE;
            int newX = x + side.offsetX;
            int newZ = z + side.offsetZ;

            int offsetY;
            for(offsetY = 0; BlockUtil.isReplaceableBlock(world, newX, y + offsetY, newZ) && y + offsetY > 0 && (modifiers.getStrength() > 0 || world.getBlock(x, y + offsetY, z).getMaterial().isSolid()); --offsetY) {
               world.setBlock(newX, y + offsetY, newZ, vine, var14, 3);
            }

            for(offsetY = 1; (BlockUtil.isReplaceableBlock(world, newX, y + offsetY, newZ) || world.getBlock(newX, y + offsetY, newZ).getMaterial() == Material.leaves) && world.getBlock(x, y + offsetY, z).getMaterial().isSolid() && y + offsetY < 255; ++offsetY) {
               world.setBlock(newX, y + offsetY, newZ, vine, var14, 3);
            }

         }
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            BrewPotionEffect.applyPotionEffect(targetEntity, modifiers, Witchery.Potions.WRAPPED_IN_VINE, TimeUtil.secsToTicks(90), modifiers.noParticles, modifiers.caster);
         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.cactus), (new BrewNamePart("witchery:brew.thorns")).setBaseDuration(TimeUtil.secsToTicks(90)), new AltarPower(150), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            Coord coord = null;
            if(world.getBlock(x, y, z) == Witchery.Blocks.CACTUS) {
               ++y;

               while(world.getBlock(x, y, z) == Witchery.Blocks.CACTUS) {
                  ++y;
               }

               if(BlockUtil.isReplaceableBlock(world, x, y, z)) {
                  coord = new Coord(x, y, z);
               }
            } else {
               coord = BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster);
            }

            if(coord != null) {
               int i = 0;

               for(int height = 3 + modifiers.getStrength(); i < height && BlockUtil.isReplaceableBlock(world, coord.x, coord.y + i, coord.z) && coord.y + i < 255; ++i) {
                  world.setBlock(coord.x, coord.y + i, coord.z, Witchery.Blocks.CACTUS);
               }
            }

         }
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            BrewPotionEffect.applyPotionEffect(targetEntity, modifiers, Witchery.Potions.SPIKED, TimeUtil.secsToTicks(90), modifiers.noParticles, modifiers.caster);
         }
      });
      this.register(new BrewActionSprouting(Witchery.Items.GENERIC.itemBranchEnt.getBrewItemKey(), (new BrewNamePart("witchery:brew.sprouting")).setBaseDuration(TimeUtil.secsToTicks(15)), new AltarPower(350), new Probability(1.0D), new EffectLevel(2)));
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemIcyNeedle.getBrewItemKey(), (new BrewNamePart("witchery:brew.cold")).setBaseDuration(TimeUtil.minsToTicks(3)), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            if(BlockProtect.checkModsForBreakOK(world, x, y, z, modifiers.caster)) {
               (new BlockActionReplaceSphere() {
                  protected boolean onShouldReplace(World world, int x, int y, int z, Block block) {
                     return block.getMaterial() == Material.water;
                  }
                  protected void onReplaceBlock(World world, int x, int y, int z, Block block) {
                     world.setBlock(x, y, z, Blocks.ice);
                  }
               }).replaceBlocks(world, x, y, z, 2 + 2 * modifiers.getStrength());
            }

         }
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            BrewPotionEffect.applyPotionEffect(targetEntity, modifiers, Witchery.Potions.CHILLED, TimeUtil.minsToTicks(3), modifiers.noParticles, modifiers.caster);
         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Items.stick), new BrewNamePart("witchery:brew.knockback"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            if(modifiers.impactLocation != null) {
               EntityUtil.pushback(world, targetEntity, modifiers.impactLocation, 1.0D + (double)modifiers.getStrength() * modifiers.powerScalingFactor, 0.5D + (double)modifiers.getStrength() * 0.2D);
            } else {
               double radius = (double)(3 + modifiers.getStrength());
               double radiusSq = radius * radius;
               EntityPosition position = new EntityPosition(targetEntity);
               List entities = world.getEntitiesWithinAABBExcludingEntity(targetEntity, position.getBounds(radius));
               Iterator i$ = entities.iterator();

               while(i$.hasNext()) {
                  Entity entity = (Entity)i$.next();
                  if((entity instanceof EntityLivingBase || entity instanceof EntityItem) && targetEntity.getDistanceSqToEntity(entity) <= radiusSq) {
                     EntityUtil.pushback(world, entity, position, 1.0D + (double)modifiers.getStrength() * modifiers.powerScalingFactor, 0.5D + (double)modifiers.getStrength() * 0.2D);
                  }
               }
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.sapling, 0), new BrewNamePart("witchery:brew.treeoak"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            Coord location = BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster);
            if(location != null) {
               ((WorldGenAbstractTree)(modifiers.getStrength() > 1?new WorldGenBigTree(true):new WorldGenTrees(true))).generate(world, world.rand, location.x, location.y, location.z);
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.sapling, 1), new BrewNamePart("witchery:brew.treespruce"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            Coord location = BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster);
            if(location != null) {
               ((WorldGenAbstractTree)(modifiers.getStrength() > 1?new WorldGenMegaPineTree(false, world.rand.nextBoolean()):new WorldGenTaiga2(true))).generate(world, world.rand, location.x, location.y, location.z);
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.sapling, 2), new BrewNamePart("witchery:brew.treebirch"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            Coord location = BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster);
            if(location != null) {
               (new WorldGenForest(true, false)).generate(world, world.rand, location.x, location.y, location.z);
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.sapling, 3), new BrewNamePart("witchery:brew.treejungle"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            Coord location = BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster);
            if(location != null) {
               ((WorldGenAbstractTree)(modifiers.strength > 2?new WorldGenMegaJungle(true, 10, 20, 3, 3):new WorldGenTrees(true, 4 + world.rand.nextInt(7), 3, 3, false))).generate(world, world.rand, location.x, location.y, location.z);
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.sapling, 4), new BrewNamePart("witchery:brew.treeacacia"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            Coord location = BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster);
            if(location != null) {
               (new WorldGenSavannaTree(true)).generate(world, world.rand, location.x, location.y, location.z);
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.sapling, 5), new BrewNamePart("witchery:brew.treedarkoak"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            Coord location = BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster);
            if(location != null) {
               (new WorldGenCanopyTree(true)).generate(world, world.rand, location.x, location.y, location.z);
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Witchery.Blocks.SAPLING, 0), new BrewNamePart("witchery:brew.treerowan"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            Coord location = BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster);
            if(location != null) {
               (new WorldGenWitchTree(true, 5, 0, 0, 1, false)).generate(world, world.rand, location.x, location.y, location.z);
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Witchery.Blocks.SAPLING, 1), new BrewNamePart("witchery:brew.treealder"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            Coord location = BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster);
            if(location != null) {
               WorldGenLargeWitchTree tree = new WorldGenLargeWitchTree(true, 1, 1, 0.5D);
               tree.setScale(0.6D, 0.5D, 0.5D);
               tree.generate(world, world.rand, location.x, location.y, location.z);
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Witchery.Blocks.SAPLING, 2), new BrewNamePart("witchery:brew.treehawthorn"), new AltarPower(200), new Probability(1.0D), new EffectLevel(2)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            Coord location = BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster);
            if(location != null) {
               WorldGenLargeWitchTree tree = new WorldGenLargeWitchTree(true, 2, 2);
               tree.setScale(0.8D, 1.2D, 1.0D);
               tree.generate(world, world.rand, location.x, location.y, location.z);
            }

         }
      });
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemHeartOfGold.getBrewItemKey(), new BrewNamePart("witchery:brew.animalattraction", "witchery:brew.animalrepulsion"), new AltarPower(500), new Probability(1.0D), new EffectLevel(4)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            double radius = (double)((modifiers.getStrength() + 1) * 16);
            double radiusSq = radius * radius;
            AxisAlignedBB bounds = targetEntity.boundingBox.expand(radius, radius, radius);
            int maxCreatures = (int)Math.ceil(((double)modifiers.getStrength() + 1.0D) * 2.0D * modifiers.powerScalingFactor);
            List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
            Iterator i$ = entities.iterator();

            while(i$.hasNext()) {
               EntityLivingBase otherEntity = (EntityLivingBase)i$.next();
               if(otherEntity != targetEntity && otherEntity.getDistanceSqToEntity(targetEntity) < radiusSq) {
                  if(otherEntity instanceof EntityTameable) {
                     EntityTameable animal = (EntityTameable)otherEntity;
                     if(Familiar.couldBeFamiliar(animal)) {
                        continue;
                     }

                     if(!modifiers.inverted) {
                        if(!animal.isTamed()) {
                           animal.setTamed(true);
                           animal.setPathToEntity((PathEntity)null);
                           animal.setAttackTarget((EntityLivingBase)null);
                           animal.setHealth(20.0F);
                           animal.func_152115_b(targetEntity.getUniqueID().toString());
                           world.setEntityState(animal, (byte)7);
                           if(animal instanceof EntityOcelot) {
                              ((EntityOcelot)animal).setTameSkin(1 + world.rand.nextInt(3));
                           }
                        }
                     } else if(animal.isTamed() && !animal.func_152114_e(targetEntity)) {
                        animal.setTamed(false);
                        animal.setPathToEntity((PathEntity)null);
                        animal.setAttackTarget((EntityLivingBase)null);
                        animal.func_152115_b("");
                        world.setEntityState(animal, (byte)6);
                        if(animal instanceof EntityOcelot) {
                           ((EntityOcelot)animal).setTameSkin(0);
                        }
                     }
                  }

                  if(otherEntity instanceof EntityAnimal) {
                     EntityAnimal var19 = (EntityAnimal)otherEntity;
                     if(!modifiers.inverted) {
                        if(!var19.getNavigator().tryMoveToXYZ(targetEntity.posX, targetEntity.posY, targetEntity.posZ, 1.0D)) {
                           var19.setPathToEntity(world.getEntityPathToXYZ(var19, MathHelper.floor_double(targetEntity.posX), MathHelper.floor_double(targetEntity.posY), MathHelper.floor_double(targetEntity.posZ), 10.0F, true, false, false, true));
                        }
                     } else {
                        byte RANGE = 6;
                        int newX = MathHelper.floor_double(targetEntity.posX) + (world.rand.nextBoolean()?1:-1) * (RANGE + world.rand.nextInt(RANGE));
                        int newZ = MathHelper.floor_double(targetEntity.posZ) + (world.rand.nextBoolean()?1:-1) * (RANGE + world.rand.nextInt(RANGE));

                        int newY;
                        for(newY = 62; !world.isAirBlock(newX, newY + 1, newZ); ++newY) {
                           ;
                        }

                        if(!var19.getNavigator().tryMoveToXYZ((double)newX, (double)newY, (double)newZ, 1.0D)) {
                           var19.setPositionAndUpdate(0.5D + (double)newX, (double)newY, 0.5D + (double)newZ);
                        }
                     }
                  }
               }
            }

         }
      });
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemSilverDust.getBrewItemKey(), new BrewNamePart("witchery:brew.harmwerewolves"), new AltarPower(1000), new Probability(1.0D), new EffectLevel(4)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            int strength = Math.min(modifiers.getStrength(), modifiers.strengthCeilingDisabled?3:1);
            if(CreatureUtil.isWerewolf(targetEntity)) {
               targetEntity.attackEntityFrom(new EntityDamageSourceIndirectSilver(modifiers.caster, modifiers.caster), (float)MathHelper.ceiling_double_int((double)(3 << strength) * modifiers.powerScalingFactor));
            } else {
               targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(modifiers.caster, modifiers.caster), (float)MathHelper.ceiling_double_int((double)(1 << strength) * modifiers.powerScalingFactor));
            }

         }
         protected void doApplyRitualToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
            if(!world.isRemote) {
               WorldInfo worldinfo = ((WorldServer)world).getWorldInfo();
               int i = (300 + world.rand.nextInt(600)) * 20;
               if(!worldinfo.isRaining()) {
                  worldinfo.setRainTime(i);
                  worldinfo.setRaining(true);
               }

               if(modifiers.getStrength() >= 1 && !worldinfo.isThundering()) {
                  worldinfo.setThunderTime(i);
                  worldinfo.setThundering(true);
               }
            }

         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Witchery.Items.SEEDS_GARLIC), new BrewNamePart("witchery:brew.weakenvampires"), new AltarPower(500), new Probability(1.0D), new EffectLevel(4)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            if(CreatureUtil.isVampire(targetEntity)) {
               if(targetEntity instanceof EntityPlayer) {
                  ExtendedPlayer playerEx = ExtendedPlayer.get((EntityPlayer)targetEntity);
                  playerEx.decreaseBloodPower(50 + 20 * modifiers.getStrength(), false);
               }

               BrewPotionEffect.applyPotionEffect(targetEntity, modifiers, Potion.weakness, TimeUtil.secsToTicks(90), false, modifiers.caster);
            }

         }
      });
      this.register(new BrewPotionEffect(new BrewItemKey(Items.skull, 1), new BrewNamePart("witchery:brew.wither"), new AltarPower(200), new Probability(1.0D), Potion.wither, (long)TimeUtil.secsToTicks(15), new EffectLevel(4)));
      this.register(new BrewActionEffect(new BrewItemKey(Witchery.Blocks.GLINT_WEED), new BrewNamePart("witchery:brew.inferno"), new AltarPower(750), new Probability(1.0D), new EffectLevel(3)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {
            if(!world.isRemote && !modifiers.isGlancing) {
               EntitySplatter.splatter(world, new Coord(x, y, z, side), modifiers.getStrength());
            }

         }
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            if(!world.isRemote && !modifiers.isGlancing && targetEntity instanceof EntityLivingBase) {
               new Coord(targetEntity);
               targetEntity.setFire(2 + 2 * modifiers.getStrength());
               if(modifiers.powerScalingFactor == 1.0D) {
                  EntitySplatter.splatter(world, new Coord(targetEntity), modifiers.powerScalingFactor == 1.0D?modifiers.getStrength():0);
               }
            }

         }
      });
      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemInfernalBlood.getBrewItemKey(), new BrewNamePart("witchery:brew.fear"), new AltarPower(500), new Probability(1.0D), Witchery.Potions.GROTESQUE, (long)TimeUtil.minsToTicks(3), new EffectLevel(2)));
      this.register(new BrewPotionEffect(Dye.INK_SAC.getBrewItemKey(), new BrewNamePart("witchery:brew.blindness"), new AltarPower(1000), new Probability(1.0D), Potion.blindness, (long)TimeUtil.secsToTicks(15), new EffectLevel(4)));
      this.register(new BrewPotionEffect(new BrewItemKey(Blocks.red_flower), new BrewNamePart("witchery:brew.love"), new AltarPower(500), new Probability(1.0D), Witchery.Potions.LOVE, (long)TimeUtil.secsToTicks(10), new EffectLevel(4)));
      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemDemonHeart.getBrewItemKey(), new BrewNamePart("witchery:brew.paralysis"), new AltarPower(750), new Probability(1.0D), Witchery.Potions.PARALYSED, (long)TimeUtil.secsToTicks(10), new EffectLevel(4)));
      this.register(new BrewCurseEffect(Witchery.Items.GENERIC.itemDropOfLuck.getBrewItemKey(), new BrewNamePart("witchery:brew.potionmaster", "witchery:brew.insanity"), new AltarPower(5000), new Probability(1.0D), Witchery.Potions.BREWING_EXPERT, (long)TimeUtil.minsToTicks(6), Witchery.Potions.INSANITY, (long)TimeUtil.minsToTicks(3), new EffectLevel(4), true));
      this.register(new BrewCurseEffect(new BrewItemKey(Items.rotten_flesh), new BrewNamePart("witchery:potion.diseased"), new AltarPower(2000), new Probability(1.0D), Witchery.Potions.DISEASED, (long)TimeUtil.minsToTicks(3), new EffectLevel(4), false));
      this.register(new BrewCurseEffect(Witchery.Items.GENERIC.itemDisturbedCotton.getBrewItemKey(), new BrewNamePart("witchery:brew.sinking"), new AltarPower(3000), new Probability(1.0D), Witchery.Potions.SINKING, (long)TimeUtil.minsToTicks(3), new EffectLevel(4), false));
      this.register(new BrewCurseEffect(new BrewItemKey(Witchery.Blocks.EMBER_MOSS), new BrewNamePart("witchery:brew.overheating"), new AltarPower(3000), new Probability(1.0D), Witchery.Potions.OVERHEATING, (long)TimeUtil.minsToTicks(3), new EffectLevel(4), false));
      this.register(new BrewCurseEffect(Witchery.Items.GENERIC.itemMellifluousHunger.getBrewItemKey(), new BrewNamePart("witchery:brew.wakingnightmare"), new AltarPower(10000), new Probability(1.0D), Witchery.Potions.WAKING_NIGHTMARE, (long)TimeUtil.minsToTicks(3), new EffectLevel(4), false));
      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemToeOfFrog.getBrewItemKey(), new BrewNamePart("witchery:brew.frogsleg"), new AltarPower(500), new Probability(1.0D), Witchery.Potions.DOUBLE_JUMP, (long)TimeUtil.minsToTicks(6), new EffectLevel(4)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.golden_apple), new BrewNamePart("witchery:brew.absorbsion"), new AltarPower(1000), new Probability(1.0D), Potion.field_76444_x, (long)TimeUtil.secsToTicks(30), new EffectLevel(4)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.golden_apple, 1), new BrewNamePart("witchery:brew.healthboost"), new AltarPower(1000), new Probability(1.0D), Potion.field_76434_w, (long)TimeUtil.minsToTicks(2), new EffectLevel(4)));
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemSubduedSpirit.getBrewItemKey(), new BrewNamePart("witchery:brew.wasting", "witchery:brew.fullness"), new AltarPower(500), new Probability(1.0D), new EffectLevel(4)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            int hungerTicks = modifiers.getModifiedDuration(TimeUtil.secsToTicks(20));
            int poisonTicks = Math.max(modifiers.getModifiedDuration(TimeUtil.secsToTicks(3)), 40);
            int strength = 1 + modifiers.getStrength() / 2;
            if(targetEntity instanceof EntityPlayer) {
               EntityPlayer victim = (EntityPlayer)targetEntity;
               int minLevel;
               if(modifiers.inverted) {
                  minLevel = 6 + 2 * modifiers.getStrength();
                  victim.getFoodStats().addStats(minLevel, (float)minLevel);
               } else {
                  minLevel = 10 - modifiers.getStrength();
                  if(victim.getFoodStats().getFoodLevel() > minLevel) {
                     victim.getFoodStats().addStats(-minLevel, 2.0F);
                  }

                  victim.addPotionEffect(new PotionEffect(Potion.hunger.id, hungerTicks, strength));
                  victim.addPotionEffect(new PotionEffect(Potion.poison.id, poisonTicks, 0));
               }
            } else if(modifiers.inverted) {
               targetEntity.addPotionEffect(new PotionEffect(Potion.regeneration.id, TimeUtil.secsToTicks(30), modifiers.getStrength()));
            } else {
               targetEntity.addPotionEffect(new PotionEffect(Potion.wither.id, hungerTicks, strength));
               targetEntity.addPotionEffect(new PotionEffect(Potion.poison.id, poisonTicks));
            }

         }
      });
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemOdourOfPurity.getBrewItemKey(), new BrewNamePart("witchery:brew.revealing"), new AltarPower(100), new Probability(1.0D), new EffectLevel(4)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            boolean doDamage = false;
            if(targetEntity.isPotionActive(Potion.invisibility)) {
               targetEntity.removePotionEffect(Potion.invisibility.id);
            }

            if(targetEntity instanceof EntityPlayer && targetEntity.isInvisible()) {
               targetEntity.setInvisible(false);
            }

            if(targetEntity instanceof EntityPlayer) {
               EntityPlayer strength = (EntityPlayer)targetEntity;
               ExtendedPlayer playerEx = ExtendedPlayer.get(strength);
               if(playerEx != null && playerEx.getCreatureType() == TransformCreature.PLAYER) {
                  ParticleEffect.SMOKE.send(SoundEffect.WITCHERY_RANDOM_POOF, strength, 0.5D, 2.0D, 16);
                  Shapeshift.INSTANCE.shiftTo(strength, TransformCreature.NONE);
               }
            }

            if(targetEntity instanceof EntitySummonedUndead && ((EntitySummonedUndead)targetEntity).isObscured()) {
               ((EntitySummonedUndead)targetEntity).setObscured(false);
            }

            int strength1 = modifiers.getStrength();
            if(doDamage && strength1 > 0) {
               targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(modifiers.caster, modifiers.caster), (float)strength1);
            }

         }
      });
      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemFoulFume.getBrewItemKey(), new BrewNamePart("witchery:potion.stoutbelly"), new AltarPower(1000), new Probability(1.0D), Witchery.Potions.STOUT_BELLY, (long)TimeUtil.secsToTicks(90), new EffectLevel(4)));
      this.register(new BrewActionBlight(new BrewItemKey(Items.poisonous_potato), new BrewNamePart("witchery:brew.blight"), new AltarPower(2000), new Probability(1.0D), new EffectLevel(4)));
      this.register(new BrewActionTranspose(new BrewItemKey(Items.ender_pearl), new BrewNamePart("witchery:brew.transpose"), new AltarPower(1000), new Probability(1.0D), new EffectLevel(4)));
      this.register(new BrewActionEffect(new BrewItemKey(Items.iron_ingot), new BrewNamePart("witchery:brew.transposeore"), new AltarPower(2000), new Probability(1.0D), new EffectLevel(4)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {
            int depth = radius + modifiers.strength;
            Block[] blockTypes = new Block[]{Blocks.iron_ore, Blocks.gold_ore, Blocks.lapis_ore, Blocks.emerald_ore};
            this.slurpOres(world, x, y, z, radius, depth, blockTypes, modifiers, y + 2);
         }
         protected void doApplyRitualToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
            int r = ritualModifiers.covenSize + radius;
            int maxDepth = 4 * ritualModifiers.covenSize * (1 + modifiers.getStrength());
            boolean steps = true;
            Block[] blockTypes = new Block[]{Blocks.iron_ore, Blocks.gold_ore, Blocks.lapis_ore, Blocks.emerald_ore};
            this.slurpOres(world, x, y - (ritualModifiers.pulses - 1) * 4, z, r, 4, blockTypes, modifiers, y + 2);
            ParticleEffect.FLAME.send(SoundEffect.FIREWORKS_BLAST1, world, (double)x, (double)y, (double)z, 1.0D, 1.0D, 16);
            ritualModifiers.setRitualStatus(ritualModifiers.pulses * 4 < maxDepth?RitualStatus.ONGOING:RitualStatus.COMPLETE);
         }
         private void slurpOres(World world, int posX, int posY, int posZ, int radius, int depth, Block[] blockTypes, ModifiersEffect modifiers, int returnY) {
            int r = radius;
            int maxType = modifiers.getStrength();

            for(int x = posX - radius; x <= posX + r; ++x) {
               for(int z = posZ - r; z <= posZ + r; ++z) {
                  for(int y = posY - depth; y <= posY + r; ++y) {
                     Block blockID = world.getBlock(x, y, z);

                     for(int t = 0; t < blockTypes.length && t < maxType; ++t) {
                        if(blockID == blockTypes[t]) {
                           ItemStack newStack = new ItemStack(blockID);
                           EntityItem entity = new EntityItem(world, (double)posX + 0.5D, (double)posY + 0.5D, (double)posZ + 0.5D, newStack);
                           if(!world.isRemote) {
                              world.setBlockToAir(x, y, z);
                              world.spawnEntityInWorld(entity);
                           }
                        }
                     }
                  }
               }
            }

         }
      });
      this.register(new BrewPotionEffect(new BrewItemKey(Blocks.tallgrass, 0), new BrewNamePart("witchery:potion.volatility"), new AltarPower(1000), new Probability(1.0D), Witchery.Potions.VOLATILITY, (long)TimeUtil.secsToTicks(180), new EffectLevel(4)));
      this.register(new BrewPotionEffect(new BrewItemKey(Blocks.deadbush), new BrewNamePart("witchery:potion.volatility"), new AltarPower(1000), new Probability(1.0D), Witchery.Potions.VOLATILITY, (long)TimeUtil.secsToTicks(180), new EffectLevel(4)));
      this.register(new BrewPotionEffect(new BrewItemKey(Blocks.soul_sand), new BrewNamePart("witchery:brew.allergydark"), new AltarPower(4000), new Probability(1.0D), Witchery.Potions.DARKNESS_ALLERGY, (long)TimeUtil.minsToTicks(2), new EffectLevel(4)));
      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemWhiffOfMagic.getBrewItemKey(), new BrewNamePart("witchery:potion.absorbmagic"), new AltarPower(2000), new Probability(1.0D), Witchery.Potions.ABSORB_MAGIC, (long)TimeUtil.secsToTicks(60), new EffectLevel(4)));
      this.register(new BrewActionRaising(Items.bone, new AltarPower(2000), new EffectLevel(4)));
      this.register(new BrewActionRaiseLand(new BrewItemKey(Items.quartz), new BrewNamePart("witchery:brew.raiseland"), new AltarPower(2000), new EffectLevel(4)));
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.ice), new BrewNamePart("witchery:brew.harmdemons"), new AltarPower(500), new Probability(1.0D), new EffectLevel(5)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            int strength = Math.min(modifiers.getStrength(), modifiers.strengthCeilingDisabled?3:1);
            if(targetEntity instanceof EntityLeonard) {
               ((EntityLeonard)targetEntity).attackEntityFromWeakness(MathHelper.ceiling_double_int((double)(10 << strength) * modifiers.powerScalingFactor));
            } else if(CreatureUtil.isDemonic(targetEntity)) {
               targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(modifiers.caster, modifiers.caster), (float)MathHelper.ceiling_double_int((double)(10 << strength) * modifiers.powerScalingFactor));
            } else {
               targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(modifiers.caster, modifiers.caster), (float)MathHelper.ceiling_double_int((double)(3 << strength) * modifiers.powerScalingFactor));
            }

         }
      });
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemFrozenHeart.getBrewItemKey(), new BrewNamePart("witchery:brew.iceshell"), new AltarPower(500), new Probability(1.0D), new EffectLevel(5)) {
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
            this.createSphere(world, modifiers, BlockUtil.getClosestPlantableBlock(world, x, y, z, side, modifiers.caster));
         }
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            boolean resistent = targetEntity instanceof EntityDemon || targetEntity instanceof EntityBlaze || targetEntity instanceof IBossDisplayData || targetEntity instanceof EntityEnt || targetEntity instanceof EntityIronGolem;
            if(!resistent) {
               BrewPotionEffect.applyPotionEffect(targetEntity, modifiers, Witchery.Potions.CHILLED, TimeUtil.secsToTicks(10), modifiers.noParticles, modifiers.caster);
               if(!modifiers.isGlancing) {
                  this.createSphere(world, modifiers, new Coord(targetEntity));
               }
            } else if(targetEntity.dimension != -1) {
               Coord coord = new Coord(targetEntity);
               BlockUtil.setBlockIfReplaceable(world, coord.x, coord.y, coord.z, Blocks.flowing_water);
            }

         }
         public void createSphere(final World world, ModifiersEffect modifiers, final Coord coord) {
            if(coord != null) {
               final int iceRadius = modifiers.getStrength() + (modifiers.getStrength() > 3?2:1);
               (new BlockActionSphere() {
                  protected void onBlock(World worldx, int x, int y, int z) {
                     BlockUtil.setBlockIfReplaceable(worldx, x, y, z, Witchery.Blocks.PERPETUAL_ICE);
                  }
                  protected void onComplete() {
                     this.fillWith(world, coord.x, coord.y, coord.z, iceRadius, Blocks.air, Witchery.Blocks.PERPETUAL_ICE);
                  }
               }).drawHollowSphere(world, coord.x, coord.y, coord.z, iceRadius);
            }

         }
      });
      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemSpectralDust.getBrewItemKey(), new BrewNamePart("witchery:potion.reflectdamage"), new AltarPower(2000), new Probability(1.0D), Witchery.Potions.REFLECT_DAMAGE, (long)TimeUtil.secsToTicks(90), new EffectLevel(5)));
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemRefinedEvil.getBrewItemKey(), new BrewNamePart("witchery:brew.hellgate"), new AltarPower(3000), new Probability(1.0D), new EffectLevel(5)) {
         public boolean isRitualTargetLocationValid(MinecraftServer server, World world, int x, int y, int z, BlockPosition target, ModifiersRitual modifiers) {
            return CircleUtil.isMediumCircle(target.getWorld(server), target.x, target.y, target.z, Witchery.Blocks.GLYPH_INFERNAL);
         }
         protected void doApplyRitualToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
            InfusionInfernal.spawnCreature(world, EntityDemon.class, x, y, z, (EntityLivingBase)null, 0, 2, ParticleEffect.FLAME, SoundEffect.MOB_ENDERDRAGON_GROWL);
         }
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {}
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            BrewPotionEffect.applyPotionEffect(targetEntity, modifiers, Witchery.Potions.NETHER_BOUND, TimeUtil.minsToTicks(3), modifiers.noParticles, modifiers.caster);
         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Items.gold_ingot), new BrewNamePart("witchery:brew.blast"), new AltarPower(500), new Probability(1.0D), new EffectLevel(5)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            if(modifiers.powerScalingFactor == 1.0D || world.rand.nextDouble() < modifiers.powerScalingFactor * 0.2D) {
               world.createExplosion(modifiers.caster, targetEntity.posX, targetEntity.posY, targetEntity.posZ, (float)modifiers.getStrength(), true);
            }

         }
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {
            world.createExplosion(modifiers.caster, (double)(x + side.offsetX) + 0.5D, (double)(y + side.offsetY) + 0.5D, (double)(z + side.offsetZ) + 0.5D, (float)modifiers.getStrength(), true);
         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Blocks.double_plant), new BrewNamePart("witchery:brew.poisontoad"), new AltarPower(500), new Probability(1.0D), new EffectLevel(5)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            if(!(targetEntity instanceof EntityToad) && modifiers.powerScalingFactor == 1.0D || world.rand.nextDouble() < modifiers.powerScalingFactor * 0.2D) {
               EntityToad toad = new EntityToad(world);
               toad.setLocationAndAngles(targetEntity.posX, targetEntity.posY + (double)targetEntity.height + 1.0D, targetEntity.posZ, 0.0F, 0.0F);
               toad.setTimeToLive(60 + modifiers.getStrength() * 40, true);
               world.spawnEntityInWorld(toad);
            }

         }
         protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack stack) {
            EntityToad toad = new EntityToad(world);
            toad.setLocationAndAngles(0.5D + (double)x, 2.5D + (double)y, 0.5D + (double)z, 0.0F, 0.0F);
            toad.setTimeToLive(60 + modifiers.getStrength() * 40, true);
            world.spawnEntityInWorld(toad);
         }
      });
      this.register(new BrewActionEffect(new BrewItemKey(Items.ender_eye), new BrewNamePart("witchery:brew.iceworld"), new AltarPower(2000), new Probability(1.0D), new EffectLevel(5)) {
         protected void doApplyToBlock(World world, int x, int y0, int z, ForgeDirection side, int radius, final ModifiersEffect modifiers, ItemStack actionStack) {
            if(!world.isRemote) {
               (new BlockActionSphere() {
                  public void onBlock(World world, int x, int y, int z) {
                     Block block = world.getBlock(x, y, z);
                     if(BlockProtect.checkModsForBreakOK(world, x, y, z, block, world.getBlockMetadata(x, y, z), modifiers.caster) && BlockProtect.canBreak(block, world)) {
                        world.getBlockMetadata(x, y, z);
                        if(block != Blocks.wooden_door) {
                           return;
                        }

                        int i1 = ((BlockDoor)block).func_150012_g(world, x, y, z);
                        if((i1 & 8) != 0) {
                           --y;
                        }

                        world.setBlockToAir(x, y, z);
                        world.setBlockToAir(x, y + 1, z);
                        ItemDoor.placeDoorBlock(world, x, y, z, i1 & 3, Witchery.Blocks.PERPETUAL_ICE_DOOR);
                        ParticleEffect.SMOKE.send(SoundEffect.NONE, world, (double)x, (double)(y + 1), (double)z, 0.25D, 0.25D, 16);
                     }

                  }
               }).drawFilledSphere(world, x, y0, z, (int)Math.ceil(Math.max((double)radius * 1.5D, 1.0D)));
               (new BlockActionSphere() {
                  public void onBlock(World world, int x, int y, int z) {
                     Block block = world.getBlock(x, y, z);
                     if(BlockProtect.checkModsForBreakOK(world, x, y, z, block, world.getBlockMetadata(x, y, z), modifiers.caster) && BlockProtect.canBreak(block, world)) {
                        int meta = world.getBlockMetadata(x, y, z);
                        if(block != Blocks.dirt && block != Blocks.grass && block != Blocks.mycelium && block != Blocks.sand) {
                           if(block != Blocks.cobblestone && block != Blocks.mossy_cobblestone && block != Blocks.log && block != Blocks.log2 && block != Witchery.Blocks.LOG) {
                              if(block != Blocks.stone && block != Blocks.stonebrick && block != Blocks.brick_block && block != Blocks.planks && block != Blocks.leaves && block != Blocks.leaves2 && block != Witchery.Blocks.LEAVES && block != Witchery.Blocks.PLANKS && block != Blocks.sandstone) {
                                 if(block != Blocks.stone_pressure_plate && block != Blocks.wooden_pressure_plate) {
                                    if(block != Blocks.stone_stairs && block != Blocks.brick_stairs && block != Blocks.stone_brick_stairs && block != Blocks.oak_stairs && block != Blocks.spruce_stairs && block != Blocks.sandstone_stairs && block != Blocks.birch_stairs && block != Blocks.jungle_stairs && block != Blocks.dark_oak_stairs && block != Blocks.acacia_stairs && block != Witchery.Blocks.STAIRS_ALDER && block != Witchery.Blocks.STAIRS_HAWTHORN && block != Witchery.Blocks.STAIRS_ROWAN) {
                                       if(block != Blocks.stone_slab && block != Blocks.wooden_slab && block != Witchery.Blocks.WOOD_SLAB_SINGLE) {
                                          if(block != Blocks.double_stone_slab && block != Blocks.double_wooden_slab && block != Witchery.Blocks.WOOD_SLAB_DOUBLE) {
                                             if(block != Blocks.fence && block != Blocks.cobblestone_wall) {
                                                if(block == Blocks.fence_gate) {
                                                   world.setBlock(x, y, z, Witchery.Blocks.PERPETUAL_ICE_FENCE_GATE);
                                                } else {
                                                   if(block != Witchery.Blocks.STOCKADE) {
                                                      return;
                                                   }

                                                   world.setBlock(x, y, z, Witchery.Blocks.STOCKADE_ICE);
                                                }
                                             } else {
                                                world.setBlock(x, y, z, Witchery.Blocks.PERPETUAL_ICE_FENCE);
                                             }
                                          } else {
                                             world.setBlock(x, y, z, Witchery.Blocks.PERPETUAL_ICE_SLAB_DOUBLE);
                                          }
                                       } else {
                                          world.setBlock(x, y, z, Witchery.Blocks.PERPETUAL_ICE_SLAB_SINGLE);
                                       }
                                    } else {
                                       world.setBlock(x, y, z, Witchery.Blocks.PERPETUAL_ICE_STAIRS, meta, 3);
                                    }
                                 } else {
                                    world.setBlock(x, y, z, Witchery.Blocks.PERPETUAL_ICE_PRESSURE_PLATE);
                                 }
                              } else {
                                 world.setBlock(x, y, z, Witchery.Blocks.PERPETUAL_ICE);
                              }
                           } else {
                              world.setBlock(x, y, z, Blocks.packed_ice);
                           }
                        } else {
                           world.setBlock(x, y, z, Blocks.snow);
                        }

                        ParticleEffect.SMOKE.send(SoundEffect.NONE, world, (double)x, (double)(y + 1), (double)z, 0.25D, 0.25D, 16);
                     }

                  }
               }).drawFilledSphere(world, x, y0, z, (int)Math.ceil(Math.max((double)radius * 1.5D, 1.0D)));
               SoundEffect.RANDOM_ORB.playAt(world, (double)x, (double)y0, (double)z, 0.5F, 2.0F);
            }

         }
      });
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemCondensedFear.getBrewItemKey(), new BrewNamePart("witchery:brew.drainmagic"), new AltarPower(1000), new Probability(1.0D), new EffectLevel(6)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
            Witchery.modHooks.reducePowerLevels(targetEntity, 0.25F * (1.0F + (float)modifiers.getStrength()) * (float)modifiers.powerScalingFactor);
         }
      });
      this.register(new BrewPotionEffect(new BrewItemKey(Items.clay_ball), new BrewNamePart("witchery:potion.fortune"), new AltarPower(1000), new Probability(1.0D), Witchery.Potions.FORTUNE, (long)TimeUtil.minsToTicks(3), new EffectLevel(6)));
      this.register(new BrewPotionEffect(new BrewItemKey(Items.fish, 1), new BrewNamePart("witchery:brew.allergysun"), new AltarPower(1000), new Probability(1.0D), Witchery.Potions.SUN_ALLERGY, (long)TimeUtil.secsToTicks(60), new EffectLevel(6)));
      this.register(new BrewPotionEffect(new BrewItemKey(Witchery.Blocks.BRAMBLE, 1), (new BrewNamePart("witchery:potion.illfitting")).setBaseDuration(TimeUtil.secsToTicks(6)), new AltarPower(8000), new Probability(1.0D), Witchery.Potions.ILL_FITTING, (long)TimeUtil.secsToTicks(6), new EffectLevel(6)));
      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemHintOfRebirth.getBrewItemKey(), new BrewNamePart("witchery:brew.reincarnate"), new AltarPower(2500), new Probability(1.0D), Witchery.Potions.REINCARNATE, (long)TimeUtil.minsToTicks(3), new EffectLevel(6)));
      this.register(new BrewActionEffect(Witchery.Items.GENERIC.itemCreeperHeart.getBrewItemKey(), new BrewNamePart("witchery:brew.durationboost"), new AltarPower(5000), new Probability(1.0D), new EffectLevel(6)) {
         protected void doApplyToEntity(World world, EntityLivingBase target, ModifiersEffect modifiers, ItemStack actionStack) {
            if(target.isPotionActive(Witchery.Potions.QUEASY)) {
               if(target.worldObj.rand.nextInt(3) == 0) {
                  target.addPotionEffect(new PotionEffect(Potion.blindness.id, TimeUtil.minsToTicks(2), 0, true));
               } else {
                  target.addPotionEffect(new PotionEffect(Potion.confusion.id, TimeUtil.minsToTicks(5), 0, true));
               }
            } else {
               Collection potionEffects = target.getActivePotionEffects();
               if(!potionEffects.isEmpty()) {
                  ArrayList newEffects = new ArrayList();
                  int durationBoost = MathHelper.ceiling_double_int(modifiers.powerScalingFactor * (double)TimeUtil.minsToTicks(3));
                  Iterator mins = potionEffects.iterator();

                  PotionEffect potionEffect;
                  while(mins.hasNext()) {
                     potionEffect = (PotionEffect)mins.next();
                     if(!Potion.potionTypes[potionEffect.getPotionID()].isInstant()) {
                        int remainingTicks = potionEffect.getDuration();
                        int newDuration = remainingTicks + Math.min(remainingTicks, durationBoost);
                        newEffects.add(new PotionEffect(potionEffect.getPotionID(), newDuration, potionEffect.getAmplifier(), potionEffect.getIsAmbient()));
                     }
                  }

                  target.clearActivePotions();
                  mins = newEffects.iterator();

                  while(mins.hasNext()) {
                     potionEffect = (PotionEffect)mins.next();
                     target.addPotionEffect(potionEffect);
                  }

                  int mins1 = 3 * Math.max(1, 4 - modifiers.getStrength());
                  target.addPotionEffect(new PotionEffect(Witchery.Potions.QUEASY.id, TimeUtil.minsToTicks(mins1), 0, true));
               }
            }

         }
      });
      this.register(new BrewPotionEffect(new BrewItemKey(Items.emerald), new BrewNamePart("witchery:brew.resizing"), new AltarPower(2500), new Probability(1.0D), Witchery.Potions.RESIZING, (long)TimeUtil.secsToTicks(20), new EffectLevel(6)));
      this.register(new BrewActionEffect(new BrewItemKey(Items.skull, 0), new BrewNamePart("witchery:brew.stealbuffs"), new AltarPower(100), new Probability(1.0D), new EffectLevel(6)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            double radius = (double)((modifiers.getStrength() + 1) * 8);
            double radiusSq = radius * radius;
            AxisAlignedBB bounds = targetEntity.boundingBox.expand(radius, radius, radius);
            int maxBuffs = (int)Math.ceil(((double)modifiers.getStrength() + 1.0D) * 2.0D * modifiers.powerScalingFactor);
            List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
            Iterator i$ = entities.iterator();

            while(i$.hasNext()) {
               EntityLivingBase otherEntity = (EntityLivingBase)i$.next();
               if(otherEntity != targetEntity && otherEntity.getDistanceSqToEntity(targetEntity) < radiusSq) {
                  Collection potionEffects = otherEntity.getActivePotionEffects();
                  ArrayList effectsToRemove = new ArrayList();
                  Iterator i$1 = potionEffects.iterator();

                  while(i$1.hasNext()) {
                     PotionEffect id = (PotionEffect)i$1.next();
                     Potion potion = Potion.potionTypes[id.getPotionID()];
                     if(!PotionBase.isDebuff(potion) && PotionBase.isCurable(potion) && id.getAmplifier() <= modifiers.getStrength()) {
                        PotionEffect myEffect = targetEntity.getActivePotionEffect(potion);
                        if(myEffect != null) {
                           if(myEffect.getDuration() <= id.getDuration() && myEffect.getAmplifier() <= id.getAmplifier()) {
                              targetEntity.addPotionEffect(new PotionEffect(id));
                              effectsToRemove.add(Integer.valueOf(id.getPotionID()));
                              --maxBuffs;
                           }
                        } else {
                           targetEntity.addPotionEffect(new PotionEffect(id));
                           effectsToRemove.add(Integer.valueOf(id.getPotionID()));
                           --maxBuffs;
                        }

                        if(maxBuffs <= 0) {
                           break;
                        }
                     }
                  }

                  i$1 = effectsToRemove.iterator();

                  while(i$1.hasNext()) {
                     int var20 = ((Integer)i$1.next()).intValue();
                     otherEntity.removePotionEffect(var20);
                  }

                  if(maxBuffs <= 0) {
                     break;
                  }
               }
            }

         }
      });
      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemFocusedWill.getBrewItemKey(), new BrewNamePart("witchery:brew.keepinventory"), new AltarPower(10000), new Probability(1.0D), Witchery.Potions.KEEP_INVENTORY, (long)TimeUtil.minsToTicks(6), new EffectLevel(8)));
      this.register(new BrewPotionEffect(Witchery.Items.GENERIC.itemRedstoneSoup.getBrewItemKey(), new BrewNamePart("witchery:potion.keepeffects"), new AltarPower(10000), new Probability(1.0D), Witchery.Potions.KEEP_EFFECTS, (long)TimeUtil.minsToTicks(6), new EffectLevel(8)));
      this.register(new BrewActionBiomeChange(new BrewItemKey(Witchery.Items.BIOME_NOTE, 32767), new BrewNamePart("witchery:brew.seasons"), new AltarPower(5000), new Probability(1.0D), new EffectLevel(8)));
      this.register(new BrewActionEffect(new BrewItemKey(Items.skull, 4), new BrewNamePart("witchery:brew.spreaddebuffs"), new AltarPower(2000), new Probability(1.0D), new EffectLevel(8)) {
         protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
            int strength = modifiers.getStrength();
            double radius = (double)((strength + 1) * 4);
            double radiusSq = radius * radius;
            AxisAlignedBB bounds = targetEntity.boundingBox.expand(radius, radius, radius);
            int maxBuffs = (int)Math.ceil(((double)strength + 1.0D) * modifiers.powerScalingFactor);
            List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bounds);
            ArrayList others = new ArrayList();
            Iterator effects = entities.iterator();

            while(effects.hasNext()) {
               EntityLivingBase effectsToRemove = (EntityLivingBase)effects.next();
               if(effectsToRemove != targetEntity && effectsToRemove.getDistanceSqToEntity(targetEntity) < radiusSq) {
                  others.add(effectsToRemove);
               }
            }

            Collection var22 = targetEntity.getActivePotionEffects();
            ArrayList var21 = new ArrayList();
            Iterator i$ = var22.iterator();

            while(i$.hasNext()) {
               PotionEffect id = (PotionEffect)i$.next();
               Potion potion = Potion.potionTypes[id.getPotionID()];
               if(PotionBase.isDebuff(potion) && PotionBase.isCurable(potion) && id.getAmplifier() <= Math.max(strength - 1, 1)) {
                  var21.add(Integer.valueOf(potion.id));
                  Iterator i$1 = others.iterator();

                  while(i$1.hasNext()) {
                     EntityLivingBase other = (EntityLivingBase)i$1.next();
                     other.addPotionEffect(new PotionEffect(id));
                  }

                  --maxBuffs;
                  if(maxBuffs <= 0) {
                     break;
                  }
               }
            }

            i$ = var21.iterator();

            while(i$.hasNext()) {
               int var23 = ((Integer)i$.next()).intValue();
               targetEntity.removePotionEffect(var23);
            }

         }
      });
      this.register(new BrewActionRitualSummonMob(new BrewItemKey(Witchery.Items.WITCH_HAT), new AltarPower(10000), new BrewActionRitualSummonMob.Recipe[]{new BrewActionRitualSummonMob.Recipe(EntityLeonard.class, new ItemStack[]{new ItemStack(Items.nether_wart), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), new ItemStack(Items.diamond), new ItemStack(Items.nether_star)})}));
      this.register(new BrewActionModifier(Witchery.Items.GENERIC.itemWaystoneBound.getBrewItemKey(), (BrewNamePart)null, new AltarPower(100)) {
         public void prepareRitual(World world, int x, int y, int z, ModifiersRitual modifiers, ItemStack stack) {
            modifiers.setTarget(stack);
         }
      });
      this.register(new BrewActionRitual(Witchery.Items.GENERIC.itemDogTongue.getBrewItemKey(), new AltarPower(250), true));
      this.register(new BrewActionRitualEntityTarget(new BrewItemKey(Witchery.Items.TAGLOCK_KIT, 32767), new AltarPower(1000)));
      this.register(new BrewActionRitualRecipe(new BrewItemKey(Witchery.Items.CHALK_RITUAL), new AltarPower(2000), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.CHALK_OTHERWHERE), new ItemStack[]{new ItemStack(Items.nether_wart), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), new ItemStack(Items.ender_pearl)}), new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.CHALK_GOLDEN), new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), new ItemStack(Items.gold_nugget)}), new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.CHALK_INFERNAL), new ItemStack[]{new ItemStack(Items.nether_wart), new ItemStack(Items.blaze_powder)})}));
      this.register(new BrewActionRitualRecipe(new BrewItemKey(Items.egg), new AltarPower(0), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(Witchery.Items.GENERIC.itemMutandis.createStack(6), new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack()})}));
      this.register(new BrewActionRitualRecipe(Witchery.Items.GENERIC.itemMutandis.getBrewItemKey(), new AltarPower(0), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Items.nether_wart), new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), Witchery.Items.GENERIC.itemDiamondVapour.createStack(), new ItemStack(Items.ender_pearl), new ItemStack(Items.wheat)}), new BrewActionRitualRecipe.Recipe(Witchery.Items.GENERIC.itemMutandisExtremis.createStack(), new ItemStack[]{new ItemStack(Items.nether_wart)})}));
      this.register(new BrewActionRitualRecipe(Witchery.Items.GENERIC.itemMutandisExtremis.getBrewItemKey(), new AltarPower(1800), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Blocks.end_stone, 2), new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), new ItemStack(Blocks.stone), new ItemStack(Blocks.end_stone)}), new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.MUTATING_SPRIG), new ItemStack[]{new ItemStack(Items.nether_wart), Witchery.Items.GENERIC.itemBranchEnt.createStack()}), new BrewActionRitualRecipe.Recipe(Witchery.Items.GENERIC.itemDropOfLuck.createStack(), new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), new ItemStack(Items.nether_wart), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), Witchery.Items.GENERIC.itemRefinedEvil.createStack()})}));
      this.register(new BrewActionRitualRecipe(new BrewItemKey(Blocks.snow), new AltarPower(3000), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.BREW_ENDLESS_WATER, 1, 0), new ItemStack[]{new ItemStack(Items.nether_wart), new ItemStack(Items.glowstone_dust), new ItemStack(Items.blaze_rod), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.BREW_ENDLESS_WATER, 1, 50), new ItemStack[]{new ItemStack(Items.nether_wart), new ItemStack(Items.glowstone_dust), new ItemStack(Items.blaze_rod)}), new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.BREW_ENDLESS_WATER, 1, 80), new ItemStack[]{new ItemStack(Items.nether_wart), new ItemStack(Items.glowstone_dust)}), new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.BREW_ENDLESS_WATER, 1, 95), new ItemStack[]{new ItemStack(Items.nether_wart)})}));
      this.register(new BrewActionRitualRecipe(new BrewItemKey(Items.coal, 1), new AltarPower(0), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.BREW_FUEL, 1, 3), new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), new ItemStack(Items.glowstone_dust), new ItemStack(Items.blaze_rod), Witchery.Items.GENERIC.itemAttunedStoneCharged.createStack()}), new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.BREW_FUEL, 1, 2), new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), new ItemStack(Items.glowstone_dust), new ItemStack(Items.blaze_rod)}), new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.BREW_FUEL, 1, 1), new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), new ItemStack(Items.glowstone_dust)}), new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.BREW_FUEL, 1, 0), new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack()})}));
      this.register(new BrewActionRitualRecipe(new BrewItemKey(Items.porkchop), new AltarPower(0), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Items.cooked_porkchop), new ItemStack[0])}));
      this.register(new BrewActionRitualRecipe(new BrewItemKey(Items.chicken), new AltarPower(0), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Items.cooked_chicken), new ItemStack[0])}));
      this.register(new BrewActionRitualRecipe(new BrewItemKey(Items.beef), new AltarPower(0), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Items.cooked_beef), new ItemStack[0])}));
      this.register(new BrewActionRitualRecipe(Witchery.Items.GENERIC.itemOddPorkRaw.getBrewItemKey(), new AltarPower(0), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(Witchery.Items.GENERIC.itemOddPorkCooked.createStack(), new ItemStack[0])}));
      this.register(new BrewActionRitualRecipe(Witchery.Items.GENERIC.itemMuttonRaw.getBrewItemKey(), new AltarPower(0), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(Witchery.Items.GENERIC.itemMuttonCooked.createStack(), new ItemStack[0])}));
      this.register(new BrewActionRitualRecipe(new BrewItemKey(Witchery.Items.WITCH_HAND), new AltarPower(0), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Items.rotten_flesh, 6), new ItemStack[0])}));
      this.register(new BrewActionRitualRecipe(Witchery.Items.GENERIC.itemTormentedTwine.getBrewItemKey(), new AltarPower(4000), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Blocks.PIT_GRASS, 4), new ItemStack[]{new ItemStack(Items.nether_wart), new ItemStack(Blocks.dirt), new ItemStack(Blocks.yellow_flower)}), new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Blocks.PIT_DIRT, 4), new ItemStack[]{Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), new ItemStack(Blocks.dirt)})}));
      this.register(new BrewActionRitualRecipe(new BrewItemKey(Items.compass), new AltarPower(5000), new BrewActionRitualRecipe.Recipe[]{new BrewActionRitualRecipe.Recipe(new ItemStack(Witchery.Items.PLAYER_COMPASS), new ItemStack[]{new ItemStack(Items.nether_wart), Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), new ItemStack(Blocks.vine), new ItemStack(Items.spider_eye)})}));
   }

   public List getRecipes() {
      return this.recipes;
   }

   private BrewAction register(BrewAction ingredient) {
      if(!this.ingredients.containsKey(ingredient.ITEM_KEY)) {
         this.ingredients.put(ingredient.ITEM_KEY, ingredient);
         if(ingredient instanceof BrewActionRitualRecipe) {
            this.recipes.add((BrewActionRitualRecipe)ingredient);
         }
      }

      return ingredient;
   }

   public int getAltarPower(ItemStack stack) {
      BrewItemKey key = BrewItemKey.fromStack(stack);
      if(key != null) {
         BrewAction action = (BrewAction)this.ingredients.get(key);
         if(action != null) {
            AltarPower power = new AltarPower(0);
            action.accumulatePower(power);
            return power.getPower();
         }
      }

      return -1;
   }

   public BrewAction getActionForItemStack(ItemStack stack) {
      return (BrewAction)this.ingredients.get(BrewItemKey.fromStack(stack));
   }

   public int getBrewColor(NBTTagCompound nbtRoot) {
      return nbtRoot != null?nbtRoot.getInteger("Color"):-16744448;
   }

   public AltarPower getPowerRequired(NBTTagCompound nbtBrew) {
      BrewActionList actionList = new BrewActionList(nbtBrew, this.ingredients);
      AltarPower totalPower = new AltarPower(0);
      Iterator i$ = actionList.actions.iterator();

      while(i$.hasNext()) {
         BrewAction action = (BrewAction)i$.next();
         action.accumulatePower(totalPower);
      }

      return totalPower;
   }

   public boolean isSplash(NBTTagCompound nbtBrew) {
      if(nbtBrew != null && !nbtBrew.hasKey("Splash")) {
         nbtBrew.setBoolean("Splash", false);
         BrewActionList actionList = new BrewActionList(nbtBrew, this.ingredients);
         Iterator i$ = actionList.actions.iterator();

         while(i$.hasNext()) {
            BrewAction action = (BrewAction)i$.next();
            if(action.createsSplash()) {
               nbtBrew.setBoolean("Splash", true);
               break;
            }
         }
      }

      return nbtBrew != null && nbtBrew.getBoolean("Splash");
   }

   public String getBrewName(NBTTagCompound nbtRoot) {
      BrewActionList actionList = new BrewActionList(nbtRoot, this.ingredients);
      BrewNameBuilder nameBuilder = new BrewNameBuilder(true);
      Iterator i$ = actionList.actions.iterator();

      while(i$.hasNext()) {
         BrewAction action = (BrewAction)i$.next();
         BrewNamePart name = action.getNamePart();
         if(name != null) {
            name.applyTo(nameBuilder);
         }
      }

      return nameBuilder.toString();
   }

   public String getBrewInformation(NBTTagCompound nbtRoot) {
      BrewActionList actionList = new BrewActionList(nbtRoot, this.ingredients);
      BrewNameBuilder nameBuilder = new BrewNameBuilder(false);
      Iterator tooltip = actionList.actions.iterator();

      while(tooltip.hasNext()) {
         BrewAction drinkSpeed = (BrewAction)tooltip.next();
         BrewNamePart name = drinkSpeed.getNamePart();
         if(name != null) {
            name.applyTo(nameBuilder);
         }
      }

      String tooltip1 = nameBuilder.toString();
      int drinkSpeed1 = this.getBrewDrinkSpeed(nbtRoot);
      if(drinkSpeed1 != 32) {
         if(drinkSpeed1 > 48) {
            tooltip1 = tooltip1 + String.format(Witchery.resource("witchery:brew.drinkspeed"), new Object[]{Witchery.resource("witchery:brew.drinkspeed.veryslow")});
         } else if(drinkSpeed1 > 32) {
            tooltip1 = tooltip1 + String.format(Witchery.resource("witchery:brew.drinkspeed"), new Object[]{Witchery.resource("witchery:brew.drinkspeed.slow")});
         } else if(drinkSpeed1 < 16) {
            tooltip1 = tooltip1 + String.format(Witchery.resource("witchery:brew.drinkspeed"), new Object[]{Witchery.resource("witchery:brew.drinkspeed.veryfast")});
         } else if(drinkSpeed1 < 32) {
            tooltip1 = tooltip1 + String.format(Witchery.resource("witchery:brew.drinkspeed"), new Object[]{Witchery.resource("witchery:brew.drinkspeed.fast")});
         }
      }

      return tooltip1;
   }

   public void updateBrewInformation(NBTTagCompound nbtRoot) {
      BrewActionList actionList = new BrewActionList(nbtRoot, this.ingredients);
      BrewNameBuilder nameBuilder = new BrewNameBuilder(false);
      Iterator tooltip = actionList.actions.iterator();

      while(tooltip.hasNext()) {
         BrewAction drinkSpeed = (BrewAction)tooltip.next();
         BrewNamePart effectCounter = drinkSpeed.getNamePart();
         if(effectCounter != null) {
            effectCounter.applyTo(nameBuilder);
         }
      }

      String tooltip1 = nameBuilder.toString();
      int drinkSpeed1 = this.getBrewDrinkSpeed(nbtRoot);
      if(drinkSpeed1 != 32) {
         if(drinkSpeed1 > 48) {
            tooltip1 = tooltip1 + String.format(Witchery.resource("witchery:brew.drinkspeed"), new Object[]{Witchery.resource("witchery:brew.drinkspeed.veryslow")});
         } else if(drinkSpeed1 > 32) {
            tooltip1 = tooltip1 + String.format(Witchery.resource("witchery:brew.drinkspeed"), new Object[]{Witchery.resource("witchery:brew.drinkspeed.slow")});
         } else if(drinkSpeed1 < 16) {
            tooltip1 = tooltip1 + String.format(Witchery.resource("witchery:brew.drinkspeed"), new Object[]{Witchery.resource("witchery:brew.drinkspeed.veryfast")});
         } else if(drinkSpeed1 < 32) {
            tooltip1 = tooltip1 + String.format(Witchery.resource("witchery:brew.drinkspeed"), new Object[]{Witchery.resource("witchery:brew.drinkspeed.fast")});
         }
      }

      nbtRoot.setString("BrewInfo", tooltip1);
      EffectLevelCounter effectCounter1 = new EffectLevelCounter();
      boolean actionFound = false;
      Iterator i$ = actionList.actions.iterator();

      while(i$.hasNext()) {
         BrewAction action = (BrewAction)i$.next();
         action.augmentEffectLevels(effectCounter1);
      }

      nbtRoot.setInteger("EffectCount", effectCounter1.getEffectCount());
      nbtRoot.setInteger("RemainingCapacity", effectCounter1.remainingCapactiy());
      nbtRoot.setInteger("UsedCapacity", effectCounter1.usedCapacity());
   }

   public int getUsedCapacity(NBTTagCompound nbtRoot) {
      return nbtRoot != null?nbtRoot.getInteger("UsedCapacity"):0;
   }

   public int getBrewDrinkSpeed(NBTTagCompound nbtRoot) {
      BrewActionList actionList = new BrewActionList(nbtRoot, this.ingredients);
      int drinkSpeed = 32;

      BrewAction action;
      for(Iterator i$ = actionList.actions.iterator(); i$.hasNext(); drinkSpeed += action.getDrinkSpeedModifiers()) {
         action = (BrewAction)i$.next();
      }

      return Math.max(drinkSpeed, 2);
   }

   public boolean canAdd(NBTTagCompound nbtRoot, BrewAction brewAction, boolean isCauldronFull) {
      if(nbtRoot.getBoolean("RitualTriggered")) {
         return false;
      } else {
         BrewActionList actionList = new BrewActionList(nbtRoot, this.ingredients);
         EffectLevelCounter effectCounter = new EffectLevelCounter();
         boolean actionFound = false;
         Iterator i$ = actionList.actions.iterator();

         while(i$.hasNext()) {
            BrewAction action = (BrewAction)i$.next();
            action.augmentEffectLevels(effectCounter);
            if(action == brewAction) {
               actionFound = true;
            } else if(action instanceof BrewActionEffect || action instanceof BrewPotionEffect) {
               actionFound = false;
            }
         }

         if(!brewAction.canAdd(actionList, isCauldronFull, effectCounter.hasEffects())) {
            return false;
         } else {
            return !actionFound && brewAction.augmentEffectLevels(effectCounter);
         }
      }
   }

   public EffectLevelCounter getBrewLevel(NBTTagCompound nbtRoot) {
      BrewActionList actionList = new BrewActionList(nbtRoot, this.ingredients);
      EffectLevelCounter effectCounter = new EffectLevelCounter();
      Iterator i$ = actionList.actions.iterator();

      while(i$.hasNext()) {
         BrewAction action = (BrewAction)i$.next();
         action.augmentEffectLevels(effectCounter);
      }

      return effectCounter;
   }

   public void nullifyItems(NBTTagCompound nbtRoot, NBTTagList nbtItems, BrewAction brewAction) {
      BrewActionList actionList = new BrewActionList(nbtRoot, this.ingredients);
      actionList.nullifyItems(brewAction, nbtItems);
   }

   public void explodeBrew(World world, NBTTagCompound nbtRoot, Entity immuneEntity, double x, double y, double z) {
      BrewActionList actionList = new BrewActionList(nbtRoot, this.ingredients);
      world.createExplosion(immuneEntity, x, y, z, Math.min(1.0F + (float)actionList.size() * 0.5F, 10.0F), false);
   }

   public ModifierYield getYieldModifier(NBTTagCompound nbtBrew) {
      BrewActionList actionList = new BrewActionList(nbtBrew, this.ingredients);
      ModifierYield counter = new ModifierYield(0);
      Iterator i$ = actionList.actions.iterator();

      while(i$.hasNext()) {
         BrewAction action = (BrewAction)i$.next();
         action.prepareYield(counter);
      }

      return counter;
   }

   public RitualStatus updateRitual(MinecraftServer server, World world, int x, int y, int z, NBTTagCompound nbtBrew, int covenSize, int ritualPulses, boolean lennyPresent) {
      BrewActionList actionList = new BrewActionList(nbtBrew, this.ingredients);
      ModifiersRitual modifiers = new ModifiersRitual(new BlockPosition(world, x, y, z), covenSize, ritualPulses, lennyPresent);
      actionList.prepareRitual(world, x, y, z, modifiers);
      ModifiersImpact modifiersImpact = new ModifiersImpact(new EntityPosition(modifiers.getTarget()), true, modifiers.covenSize, (EntityPlayer)null);
      Iterator i$ = actionList.actions.iterator();

      while(i$.hasNext()) {
         BrewAction action = (BrewAction)i$.next();
         action.prepareSplashPotion(world, actionList, modifiersImpact);
      }

      return actionList.getTopAction().updateRitual(server, actionList, world, x, y, z, modifiers, modifiersImpact);
   }

   public boolean impactSplashPotion(World world, ItemStack stack, MovingObjectPosition mop, ModifiersImpact modifiers) {
      return this.impactSplashPotion(world, new BrewActionList(stack.getTagCompound(), this.ingredients), mop, modifiers);
   }

   public boolean impactSplashPotion(World world, NBTTagCompound nbtBrew, MovingObjectPosition mop, ModifiersImpact modifiers) {
      return this.impactSplashPotion(world, new BrewActionList(nbtBrew, this.ingredients), mop, modifiers);
   }

   public boolean impactSplashPotion(World world, BrewActionList actionList, MovingObjectPosition mop, ModifiersImpact modifiers) {
      Iterator i$ = actionList.actions.iterator();

      while(i$.hasNext()) {
         BrewAction action = (BrewAction)i$.next();
         action.prepareSplashPotion(world, actionList, modifiers);
      }

      modifiers.getDispersal().onImpactSplashPotion(world, actionList.getTagCompound(), mop, modifiers);
      return true;
   }

   public void applyToEntity(World world, EntityLivingBase targetEntity, NBTTagCompound nbtBrew, ModifiersEffect modifiers) {
      BrewActionList actionList = new BrewActionList(nbtBrew, this.ingredients);
      actionList.applyToEntity(world, targetEntity, modifiers);
   }

   public void applyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, NBTTagCompound nbtBrew, ModifiersEffect modifiers) {
      BrewActionList actionList = new BrewActionList(nbtBrew, this.ingredients);
      actionList.applyToBlock(world, x, y, z, side, radius, modifiers);
   }

   public void applyRitualToEntity(World world, EntityLivingBase target, NBTTagCompound nbtBrew, ModifiersRitual ritualModifiers, ModifiersEffect modifiers) {
      BrewActionList actionList = new BrewActionList(nbtBrew, this.ingredients);
      actionList.applyRitualToEnitity(world, target, ritualModifiers, modifiers);
   }

   public void applyRitualToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, NBTTagCompound nbtBrew, ModifiersRitual ritualModifiers, ModifiersEffect effectModifiers) {
      BrewActionList actionList = new BrewActionList(nbtBrew, this.ingredients);
      actionList.applyRitualToBlock(world, x, y, z, side, radius, ritualModifiers, effectModifiers);
   }

   public void init() {}


   // $FF: synthetic class
   static class NamelessClass1178169399 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraftforge$common$util$ForgeDirection = new int[ForgeDirection.values().length];


      static {
         try {
            $SwitchMap$net$minecraftforge$common$util$ForgeDirection[ForgeDirection.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$net$minecraftforge$common$util$ForgeDirection[ForgeDirection.SOUTH.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraftforge$common$util$ForgeDirection[ForgeDirection.WEST.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraftforge$common$util$ForgeDirection[ForgeDirection.EAST.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
