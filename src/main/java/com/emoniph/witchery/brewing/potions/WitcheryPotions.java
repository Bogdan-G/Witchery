package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBeartrap;
import com.emoniph.witchery.brewing.potions.IHandleEnderTeleport;
import com.emoniph.witchery.brewing.potions.IHandleHarvestDrops;
import com.emoniph.witchery.brewing.potions.IHandleLivingAttack;
import com.emoniph.witchery.brewing.potions.IHandleLivingDeath;
import com.emoniph.witchery.brewing.potions.IHandleLivingHurt;
import com.emoniph.witchery.brewing.potions.IHandleLivingJump;
import com.emoniph.witchery.brewing.potions.IHandleLivingSetAttackTarget;
import com.emoniph.witchery.brewing.potions.IHandleLivingUpdate;
import com.emoniph.witchery.brewing.potions.IHandlePlayerDrops;
import com.emoniph.witchery.brewing.potions.IHandlePreRenderLiving;
import com.emoniph.witchery.brewing.potions.IHandleRenderLiving;
import com.emoniph.witchery.brewing.potions.PotionAbsorbMagic;
import com.emoniph.witchery.brewing.potions.PotionAttractProjectiles;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.brewing.potions.PotionBrewingExpertise;
import com.emoniph.witchery.brewing.potions.PotionChilled;
import com.emoniph.witchery.brewing.potions.PotionColorful;
import com.emoniph.witchery.brewing.potions.PotionDarknessAllergy;
import com.emoniph.witchery.brewing.potions.PotionDiseased;
import com.emoniph.witchery.brewing.potions.PotionEnderInhibition;
import com.emoniph.witchery.brewing.potions.PotionEnslaved;
import com.emoniph.witchery.brewing.potions.PotionFeatherFall;
import com.emoniph.witchery.brewing.potions.PotionFeelNoPain;
import com.emoniph.witchery.brewing.potions.PotionFloating;
import com.emoniph.witchery.brewing.potions.PotionFortune;
import com.emoniph.witchery.brewing.potions.PotionGasMask;
import com.emoniph.witchery.brewing.potions.PotionGrotesque;
import com.emoniph.witchery.brewing.potions.PotionHellishAura;
import com.emoniph.witchery.brewing.potions.PotionIllFitting;
import com.emoniph.witchery.brewing.potions.PotionInsanity;
import com.emoniph.witchery.brewing.potions.PotionKeepEffectsOnDeath;
import com.emoniph.witchery.brewing.potions.PotionKeepInventory;
import com.emoniph.witchery.brewing.potions.PotionLove;
import com.emoniph.witchery.brewing.potions.PotionMortalCoil;
import com.emoniph.witchery.brewing.potions.PotionOverheating;
import com.emoniph.witchery.brewing.potions.PotionParalysis;
import com.emoniph.witchery.brewing.potions.PotionPoisonWeapons;
import com.emoniph.witchery.brewing.potions.PotionQueasy;
import com.emoniph.witchery.brewing.potions.PotionReflectDamage;
import com.emoniph.witchery.brewing.potions.PotionReflectProjectiles;
import com.emoniph.witchery.brewing.potions.PotionReincarnate;
import com.emoniph.witchery.brewing.potions.PotionRepellAttacker;
import com.emoniph.witchery.brewing.potions.PotionResizing;
import com.emoniph.witchery.brewing.potions.PotionSinking;
import com.emoniph.witchery.brewing.potions.PotionSnowTrail;
import com.emoniph.witchery.brewing.potions.PotionSpiked;
import com.emoniph.witchery.brewing.potions.PotionSprouting;
import com.emoniph.witchery.brewing.potions.PotionStoutBelly;
import com.emoniph.witchery.brewing.potions.PotionSunAllergy;
import com.emoniph.witchery.brewing.potions.PotionSwimming;
import com.emoniph.witchery.brewing.potions.PotionVolatility;
import com.emoniph.witchery.brewing.potions.PotionWakingNightmare;
import com.emoniph.witchery.brewing.potions.PotionWorship;
import com.emoniph.witchery.brewing.potions.PotionWrappedInVine;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.EntitySizeInfo;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.TransformCreature;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Pre;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class WitcheryPotions {

   private final List allEffects = new ArrayList();
   private final List livingUpdateEventHandlers = new ArrayList();
   private final List livingJumpEventHandlers = new ArrayList();
   private final List livingHurtEventHandlers = new ArrayList();
   private final List livingDeathEventHandlers = new ArrayList();
   private final List playerDropsEventHandlers = new ArrayList();
   private final List renderLivingEventHandlers = new ArrayList();
   private final List renderLivingPreEventHandlers = new ArrayList();
   private final List enderTeleportEventHandlers = new ArrayList();
   private final List harvestDropsEventHandlers = new ArrayList();
   private final List livingSetAttackTargetEventHandlers = new ArrayList();
   private final List livingAttackEventHandlers = new ArrayList();
   public final Potion PARALYSED = this.register("witchery:potion.paralysed", PotionParalysis.class);
   public final Potion WRAPPED_IN_VINE = this.register("witchery:potion.wrappedinvine", PotionWrappedInVine.class);
   public final Potion SPIKED = this.register("witchery:potion.spiked", PotionSpiked.class);
   public final Potion SPROUTING = this.register("witchery:potion.sprouting", PotionSprouting.class);
   public final Potion GROTESQUE = this.register("witchery:potion.grotesque", PotionGrotesque.class);
   public final Potion LOVE = this.register("witchery:potion.love", PotionLove.class);
   public final Potion SUN_ALLERGY = this.register("witchery:potion.allergysun", PotionSunAllergy.class);
   public final Potion CHILLED = this.register("witchery:potion.chilled", PotionChilled.class);
   public final Potion SNOW_TRAIL = this.register("witchery:potion.snowtrail", PotionSnowTrail.class);
   public final Potion FLOATING = this.register("witchery:potion.floating", PotionFloating.class);
   public final Potion NETHER_BOUND = this.register("witchery:potion.hellishaura", PotionHellishAura.class);
   public final Potion BREWING_EXPERT = this.register("witchery:potion.brewingexpertise", PotionBrewingExpertise.class);
   public final Potion DOUBLE_JUMP = this.register("witchery:potion.doublejump", PotionBase.class);
   public final Potion FEATHER_FALL = this.register("witchery:potion.featherfall", PotionFeatherFall.class);
   public final Potion DARKNESS_ALLERGY = this.register("witchery:potion.allergydark", PotionDarknessAllergy.class);
   public final Potion REINCARNATE = this.register("witchery:potion.reincarnate", PotionReincarnate.class);
   public final Potion INSANITY = this.register("witchery:potion.insane", PotionInsanity.class);
   public final Potion KEEP_INVENTORY = this.register("witchery:potion.keepinventory", PotionKeepInventory.class);
   public final Potion SINKING = this.register("witchery:potion.sinking", PotionSinking.class);
   public final Potion OVERHEATING = this.register("witchery:potion.overheating", PotionOverheating.class);
   public final Potion WAKING_NIGHTMARE = this.register("witchery:potion.wakingnightmare", PotionWakingNightmare.class);
   public final Potion QUEASY = this.register("witchery:potion.queasy", PotionQueasy.class);
   public final Potion SWIMMING = this.register("witchery:potion.swimming", PotionSwimming.class);
   public final Potion RESIZING = this.register("witchery:potion.resizing", PotionResizing.class);
   public final Potion COLORFUL = this.register("witchery:potion.colorful", PotionColorful.class);
   public final Potion ENDER_INHIBITION = this.register("witchery:potion.enderinhibition", PotionEnderInhibition.class);
   public final Potion ILL_FITTING = this.register("witchery:potion.illfitting", PotionIllFitting.class);
   public final Potion VOLATILITY = this.register("witchery:potion.volatility", PotionVolatility.class);
   public final Potion ENSLAVED = this.register("witchery:potion.enslaved", PotionEnslaved.class);
   public final Potion MORTAL_COIL = this.register("witchery:potion.mortalcoil", PotionMortalCoil.class);
   public final Potion ABSORB_MAGIC = this.register("witchery:potion.absorbmagic", PotionAbsorbMagic.class);
   public final Potion POISON_WEAPONS = this.register("witchery:potion.poisonweapons", PotionPoisonWeapons.class);
   public final Potion REFLECT_PROJECTILES = this.register("witchery:potion.reflectprojectiles", PotionReflectProjectiles.class);
   public final Potion REFLECT_DAMAGE = this.register("witchery:potion.reflectdamage", PotionReflectDamage.class);
   public final Potion ATTRACT_PROJECTILES = this.register("witchery:potion.attractprojectiles", PotionAttractProjectiles.class);
   public final Potion REPELL_ATTACKER = this.register("witchery:potion.repellattacker", PotionRepellAttacker.class);
   public final Potion STOUT_BELLY = this.register("witchery:potion.stoutbelly", PotionStoutBelly.class);
   public final Potion FEEL_NO_PAIN = this.register("witchery:potion.feelnopain", PotionFeelNoPain.class);
   public final Potion GAS_MASK = this.register("witchery:potion.gasmask", PotionGasMask.class);
   public final Potion DISEASED = this.register("witchery:potion.diseased", PotionDiseased.class);
   public final Potion FORTUNE = this.register("witchery:potion.fortune", PotionFortune.class);
   public final Potion WORSHIP = this.register("witchery:potion.worship", PotionWorship.class);
   public final Potion KEEP_EFFECTS = this.register("witchery:potion.keepeffects", PotionKeepEffectsOnDeath.class);
   public final Potion WOLFSBANE = this.register("witchery:potion.wolfsbane", PotionBase.class);


   private Potion register(String unlocalisedName, Class clazz) {
      int potionID = -1;
      WitcheryPotions.PotionArrayExtender.extendPotionArray();
      if(potionID < 1) {
         for(int ex = Config.instance().potionStartID; ex < Potion.potionTypes.length; ++ex) {
            if(Potion.potionTypes[ex] == null) {
               potionID = Config.instance().configuration.get("potions", unlocalisedName, ex).getInt();
               break;
            }
         }
      }

      if(potionID > 31 && potionID < Potion.potionTypes.length) {
         try {
            if(Potion.potionTypes[potionID] != null) {
               Log.instance().warning(String.format("Potion slot %d already occupided by %s is being overwriting with %s, you may want to change potion ids in the config file!", new Object[]{Integer.valueOf(potionID), Potion.potionTypes[potionID].getName(), unlocalisedName}));
            }

            if(potionID > 127) {
               Log.instance().warning(String.format("Using potion slot %d (for potion %s), can lead to problems, since there is a client/server syncing restriction of max 128 potion IDs. Use the PotionStartID configuration setting to lower the range witchery uses.", new Object[]{Integer.valueOf(potionID), unlocalisedName}));
            }

            Constructor var10 = clazz.getConstructor(new Class[]{Integer.TYPE, Integer.TYPE});
            PotionBase potion = (PotionBase)var10.newInstance(new Object[]{Integer.valueOf(potionID), Integer.valueOf(unlocalisedName.hashCode())});
            potion.setPotionName(unlocalisedName);
            this.allEffects.add(potion);
            if(potion instanceof IHandleLivingHurt) {
               this.livingHurtEventHandlers.add((IHandleLivingHurt)potion);
            }

            if(potion instanceof IHandleLivingDeath) {
               this.livingDeathEventHandlers.add((IHandleLivingDeath)potion);
            }

            if(potion instanceof IHandleLivingUpdate) {
               this.livingUpdateEventHandlers.add((IHandleLivingUpdate)potion);
            }

            if(potion instanceof IHandleRenderLiving) {
               this.renderLivingEventHandlers.add((IHandleRenderLiving)potion);
            }

            if(potion instanceof IHandlePreRenderLiving) {
               this.renderLivingPreEventHandlers.add((IHandlePreRenderLiving)potion);
            }

            if(potion instanceof IHandlePlayerDrops) {
               this.playerDropsEventHandlers.add((IHandlePlayerDrops)potion);
            }

            if(potion instanceof IHandleLivingJump) {
               this.livingJumpEventHandlers.add((IHandleLivingJump)potion);
            }

            if(potion instanceof IHandleEnderTeleport) {
               this.enderTeleportEventHandlers.add((IHandleEnderTeleport)potion);
            }

            if(potion instanceof IHandleLivingSetAttackTarget) {
               this.livingSetAttackTargetEventHandlers.add((IHandleLivingSetAttackTarget)potion);
            }

            if(potion instanceof IHandleHarvestDrops) {
               this.harvestDropsEventHandlers.add((IHandleHarvestDrops)potion);
            }

            if(potion instanceof IHandleLivingAttack) {
               this.livingAttackEventHandlers.add((IHandleLivingAttack)potion);
            }

            potion.postContructInitialize();
            return potion;
         } catch (NoSuchMethodException var6) {
            return null;
         } catch (InvocationTargetException var7) {
            return null;
         } catch (InstantiationException var8) {
            return null;
         } catch (IllegalAccessException var9) {
            return null;
         }
      } else {
         Log.instance().warning(String.format("Failed to assign potion %s to slot %d, max slot id is %d, you may want to change the potion ids in the config file!", new Object[]{unlocalisedName, Integer.valueOf(potionID), Integer.valueOf(Potion.potionTypes.length - 1)}));
         return null;
      }
   }

   public void preInit() {
      Config.instance().saveIfChanged();
   }

   public void init() {
      Iterator i$ = this.allEffects.iterator();

      while(i$.hasNext()) {
         Potion potion = (Potion)i$.next();
         if(potion.id > 0 && potion.id < Potion.potionTypes.length) {
            if(Potion.potionTypes[potion.id] != potion) {
               Log.instance().warning(String.format("Another mod has overwritten Witchery potion %s in slot %d! offender: %s.", new Object[]{potion.getName(), Integer.valueOf(potion.id), Potion.potionTypes[potion.id].getName()}));
            }
         } else {
            Log.instance().warning(String.format("Witchery potion has not been registered: %s!", new Object[]{potion.getName()}));
         }
      }

   }

   public static class ClientEventHooks {

      @SubscribeEvent(
         priority = EventPriority.NORMAL
      )
      public void onRenderLiving(Pre event) {
         if(event.entity != null && event.entity.worldObj != null && event.entity.worldObj.isRemote) {
            Iterator i$ = Witchery.Potions.renderLivingPreEventHandlers.iterator();

            while(i$.hasNext()) {
               IHandlePreRenderLiving handler = (IHandlePreRenderLiving)i$.next();
               if(event.isCanceled()) {
                  break;
               }

               if(event.entity.isPotionActive(handler.getPotion().id)) {
                  PotionEffect effect = event.entity.getActivePotionEffect(handler.getPotion());
                  handler.onLivingRender(event.entity.worldObj, event.entity, event, effect.getAmplifier());
               }
            }
         }

      }

      @SubscribeEvent(
         priority = EventPriority.NORMAL
      )
      public void onRenderLiving(Post event) {
         if(event.entity != null && event.entity.worldObj != null && event.entity.worldObj.isRemote) {
            Iterator i$ = Witchery.Potions.renderLivingEventHandlers.iterator();

            while(i$.hasNext()) {
               IHandleRenderLiving handler = (IHandleRenderLiving)i$.next();
               if(event.isCanceled()) {
                  break;
               }

               if(event.entity.isPotionActive(handler.getPotion().id)) {
                  PotionEffect effect = event.entity.getActivePotionEffect(handler.getPotion());
                  handler.onLivingRender(event.entity.worldObj, event.entity, event, effect.getAmplifier());
               }
            }
         }

      }

      @SubscribeEvent
      public void onDrawBlockHighlight(DrawBlockHighlightEvent event) {
         if(event != null && !event.isCanceled() && event.player != null) {
            if(!event.player.isPotionActive(Witchery.Potions.RESIZING) && (new EntitySizeInfo(event.player)).isDefault) {
               if(BlockBeartrap.checkForHiddenTrap(event.player, event.target)) {
                  event.setCanceled(true);
               }
            } else {
               double reach = (double)Minecraft.getMinecraft().playerController.getBlockReachDistance();
               MovingObjectPosition mop = event.player.rayTrace(reach, event.partialTicks);
               if(mop != null && !BlockBeartrap.checkForHiddenTrap(event.player, mop)) {
                  event.context.drawSelectionBox(event.player, mop, 0, event.partialTicks);
               }

               event.setCanceled(true);
            }
         }

      }
   }

   private static class PotionArrayExtender {

      private static boolean potionArrayExtended;


      private static void extendPotionArray() {
         if(!potionArrayExtended) {
            boolean RESERVED = true;
            int MAX_EXTRA = Math.min(Config.instance().potionStartID - 32, 0) + 96;
            Log.instance().debug("Extending the vanilla potions array");
            int existingArrayLength = Potion.potionTypes.length;
            Potion[] newPotionArray = new Potion[existingArrayLength + MAX_EXTRA];
            System.arraycopy(Potion.potionTypes, 0, newPotionArray, 0, existingArrayLength);
            setPrivateFinalValue(Potion.class, (Object)null, newPotionArray, new String[]{"potionTypes", "field_76425_a"});
            potionArrayExtended = true;
         }

      }

      private static void setPrivateFinalValue(Class classToAccess, Object instance, Object value, String ... fieldNames) {
         Field field = ReflectionHelper.findField(classToAccess, ObfuscationReflectionHelper.remapFieldNames(classToAccess.getName(), fieldNames));

         try {
            Field e = Field.class.getDeclaredField("modifiers");
            e.setAccessible(true);
            e.setInt(field, field.getModifiers() & -17);
            field.set(instance, value);
         } catch (Throwable var6) {
            var6.printStackTrace();
         }

      }
   }

   public static class EventHooks {

      @SubscribeEvent(
         priority = EventPriority.HIGHEST
      )
      public void onPlayerDrops(PlayerDropsEvent event) {
         Iterator i$ = Witchery.Potions.playerDropsEventHandlers.iterator();

         while(i$.hasNext()) {
            IHandlePlayerDrops handler = (IHandlePlayerDrops)i$.next();
            if(event.isCanceled()) {
               break;
            }

            if(event.entityLiving.isPotionActive(handler.getPotion())) {
               PotionEffect effect = event.entityLiving.getActivePotionEffect(handler.getPotion());
               handler.onPlayerDrops(event.entityPlayer.getEntityWorld(), event.entityPlayer, event, effect.getAmplifier());
            }
         }

      }

      @SubscribeEvent(
         priority = EventPriority.HIGH
      )
      public void onBlockHarvest(HarvestDropsEvent event) {
         Iterator i$ = Witchery.Potions.harvestDropsEventHandlers.iterator();

         while(i$.hasNext()) {
            IHandleHarvestDrops handler = (IHandleHarvestDrops)i$.next();
            if(event.isCanceled()) {
               break;
            }

            if(event.harvester != null && event.harvester.isPotionActive(handler.getPotion())) {
               PotionEffect effect = event.harvester.getActivePotionEffect(handler.getPotion());
               handler.onHarvestDrops(event.world, event.harvester, event, effect.getAmplifier());
            }
         }

      }

      @SubscribeEvent
      public void onLivingHurt(LivingHurtEvent event) {
         Iterator i$ = Witchery.Potions.livingHurtEventHandlers.iterator();

         while(i$.hasNext()) {
            IHandleLivingHurt handler = (IHandleLivingHurt)i$.next();
            if(event.isCanceled()) {
               break;
            }

            if(handler.handleAllHurtEvents() || event.entityLiving.isPotionActive(handler.getPotion())) {
               PotionEffect effect = event.entityLiving.getActivePotionEffect(handler.getPotion());
               handler.onLivingHurt(event.entityLiving.worldObj, event.entityLiving, event, effect != null?effect.getAmplifier():-1);
            }
         }

      }

      @SubscribeEvent
      public void onLivingUpdate(LivingUpdateEvent event) {
         Iterator i$ = Witchery.Potions.livingUpdateEventHandlers.iterator();

         while(i$.hasNext()) {
            IHandleLivingUpdate handler = (IHandleLivingUpdate)i$.next();
            if(event.isCanceled()) {
               break;
            }

            if(event.entityLiving.isPotionActive(handler.getPotion())) {
               PotionEffect effect = event.entityLiving.getActivePotionEffect(handler.getPotion());
               handler.onLivingUpdate(event.entityLiving.worldObj, event.entityLiving, event, effect.getAmplifier(), effect.getDuration());
            }
         }

      }

      @SubscribeEvent
      public void onLivingAttack(LivingAttackEvent event) {
         Iterator i$ = Witchery.Potions.livingAttackEventHandlers.iterator();

         while(i$.hasNext()) {
            IHandleLivingAttack handler = (IHandleLivingAttack)i$.next();
            if(event.isCanceled()) {
               break;
            }

            if(event.entityLiving.isPotionActive(handler.getPotion())) {
               PotionEffect effect = event.entityLiving.getActivePotionEffect(handler.getPotion());
               handler.onLivingAttack(event.entityLiving.worldObj, event.entityLiving, event, effect.getAmplifier());
            }
         }

         if(Witchery.modHooks.isAM2Present && !event.isCanceled() && !event.entity.worldObj.isRemote && event.source == DamageSource.inWall && event.entity instanceof EntityPlayer && (ExtendedPlayer.get((EntityPlayer)event.entity).getCreatureType() == TransformCreature.WOLF || ExtendedPlayer.get((EntityPlayer)event.entity).getCreatureType() == TransformCreature.BAT) && !event.entity.worldObj.getBlock(MathHelper.floor_double(event.entity.posX), MathHelper.floor_double(event.entity.posY), MathHelper.floor_double(event.entity.posZ)).isNormalCube()) {
            event.setCanceled(true);
         }

      }

      @SubscribeEvent
      public void onLivingJump(LivingJumpEvent event) {
         Iterator i$ = Witchery.Potions.livingJumpEventHandlers.iterator();

         while(i$.hasNext()) {
            IHandleLivingJump handler = (IHandleLivingJump)i$.next();
            if(event.isCanceled()) {
               break;
            }

            if(event.entityLiving.isPotionActive(handler.getPotion())) {
               PotionEffect effect = event.entityLiving.getActivePotionEffect(handler.getPotion());
               handler.onLivingJump(event.entityLiving.worldObj, event.entityLiving, event, effect.getAmplifier());
            }
         }

      }

      @SubscribeEvent
      public void onEnderTeleport(EnderTeleportEvent event) {
         if(event.entityLiving != null && (event.entityLiving.worldObj.provider.dimensionId == Config.instance().dimensionTormentID || event.entityLiving.worldObj.provider.dimensionId == Config.instance().dimensionMirrorID)) {
            event.setCanceled(true);
         } else {
            Iterator i$ = Witchery.Potions.enderTeleportEventHandlers.iterator();

            while(i$.hasNext()) {
               IHandleEnderTeleport handler = (IHandleEnderTeleport)i$.next();
               if(event.isCanceled()) {
                  break;
               }

               if(event.entityLiving.isPotionActive(handler.getPotion())) {
                  PotionEffect effect = event.entityLiving.getActivePotionEffect(handler.getPotion());
                  handler.onEnderTeleport(event.entityLiving.worldObj, event.entityLiving, event, effect.getAmplifier());
               }
            }

         }
      }

      @SubscribeEvent
      public void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
         if(event.entityLiving instanceof EntityLiving) {
            EntityLiving livingEntity = (EntityLiving)event.entityLiving;
            if(livingEntity != null && Witchery.Potions.ENSLAVED != null && event.target != null && event.target instanceof EntityPlayer) {
               EntityPlayer i$ = (EntityPlayer)event.target;
               if(!livingEntity.isPotionActive(Witchery.Potions.ENSLAVED) && PotionEnslaved.isMobEnslavedBy(livingEntity, i$)) {
                  livingEntity.setAttackTarget((EntityLivingBase)null);
               }
            }

            Iterator i$1 = Witchery.Potions.livingSetAttackTargetEventHandlers.iterator();

            while(i$1.hasNext()) {
               IHandleLivingSetAttackTarget handler = (IHandleLivingSetAttackTarget)i$1.next();
               if(event.isCanceled()) {
                  break;
               }

               if(event.entityLiving.isPotionActive(handler.getPotion())) {
                  PotionEffect effect = event.entityLiving.getActivePotionEffect(handler.getPotion());
                  handler.onLivingSetAttackTarget(event.entityLiving.worldObj, livingEntity, event, effect.getAmplifier());
               }
            }
         }

      }

      @SubscribeEvent(
         priority = EventPriority.LOW
      )
      public void onLivingDeath(LivingDeathEvent event) {
         Iterator player = Witchery.Potions.livingDeathEventHandlers.iterator();

         while(player.hasNext()) {
            IHandleLivingDeath activeEffects = (IHandleLivingDeath)player.next();
            if(event.isCanceled()) {
               break;
            }

            if(event.entityLiving.isPotionActive(activeEffects.getPotion())) {
               PotionEffect permenantEffectList = event.entityLiving.getActivePotionEffect(activeEffects.getPotion());
               activeEffects.onLivingDeath(event.entityLiving.worldObj, event.entityLiving, event, permenantEffectList.getAmplifier());
            }
         }

         if(!event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player1 = (EntityPlayer)event.entityLiving;
            Collection activeEffects1 = player1.getActivePotionEffects();
            if(activeEffects1.size() > 0) {
               ArrayList permenantEffectList1 = new ArrayList();
               int allPermentantLevel = -1;
               if(player1.isPotionActive(Witchery.Potions.KEEP_EFFECTS)) {
                  PotionEffect nbtEffectList = player1.getActivePotionEffect(Witchery.Potions.KEEP_EFFECTS);
                  allPermentantLevel = nbtEffectList.getAmplifier();
               }

               Iterator nbtEffectList2 = activeEffects1.iterator();

               while(nbtEffectList2.hasNext()) {
                  PotionEffect nbtPlayer = (PotionEffect)nbtEffectList2.next();
                  int permenantEffect = nbtPlayer.getPotionID();
                  if(permenantEffect >= 0 && permenantEffect < Potion.potionTypes.length && Potion.potionTypes[permenantEffect] != null) {
                     if(Potion.potionTypes[permenantEffect] instanceof PotionBase) {
                        PotionBase nbtEffect = (PotionBase)Potion.potionTypes[permenantEffect];
                        if(nbtEffect.isPermenant()) {
                           permenantEffectList1.add(nbtPlayer);
                           continue;
                        }
                     }

                     Potion nbtEffect1 = Potion.potionTypes[permenantEffect];
                     if(!PotionBase.isDebuff(nbtEffect1) && allPermentantLevel >= nbtPlayer.getAmplifier()) {
                        permenantEffectList1.add(nbtPlayer);
                     }
                  }
               }

               if(permenantEffectList1.size() > 0) {
                  NBTTagList nbtEffectList1 = new NBTTagList();
                  Iterator nbtPlayer1 = permenantEffectList1.iterator();

                  while(nbtPlayer1.hasNext()) {
                     PotionEffect permenantEffect1 = (PotionEffect)nbtPlayer1.next();
                     NBTTagCompound nbtEffect2 = new NBTTagCompound();
                     permenantEffect1.writeCustomPotionEffectToNBT(nbtEffect2);
                     nbtEffectList1.appendTag(nbtEffect2);
                  }

                  NBTTagCompound nbtPlayer2 = Infusion.getNBT(player1);
                  nbtPlayer2.setTag("WITCPoSpawn", nbtEffectList1);
               }
            }
         }

      }
   }
}
