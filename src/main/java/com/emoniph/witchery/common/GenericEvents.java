package com.emoniph.witchery.common;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockVoidBramble;
import com.emoniph.witchery.brewing.potions.PotionEnslaved;
import com.emoniph.witchery.brewing.potions.PotionParalysis;
import com.emoniph.witchery.brewing.potions.PotionResizing;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.ExtendedVillager;
import com.emoniph.witchery.common.Shapeshift;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityFollower;
import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.entity.EntityItemWaystone;
import com.emoniph.witchery.entity.EntityMindrake;
import com.emoniph.witchery.entity.EntityOwl;
import com.emoniph.witchery.entity.EntitySummonedUndead;
import com.emoniph.witchery.entity.EntityToad;
import com.emoniph.witchery.entity.EntityVillageGuard;
import com.emoniph.witchery.entity.EntityVillagerWere;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.entity.EntityWolfman;
import com.emoniph.witchery.entity.ai.EntityAIDigBlocks;
import com.emoniph.witchery.entity.ai.EntityAISleep;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemDeathsClothes;
import com.emoniph.witchery.item.ItemHunterClothes;
import com.emoniph.witchery.item.ItemMoonCharm;
import com.emoniph.witchery.item.ItemPoppet;
import com.emoniph.witchery.item.ItemVampireClothes;
import com.emoniph.witchery.network.PacketExtendedEntityRequestSyncToClient;
import com.emoniph.witchery.network.PacketHowl;
import com.emoniph.witchery.network.PacketParticles;
import com.emoniph.witchery.network.PacketPlayerStyle;
import com.emoniph.witchery.network.PacketSelectPlayerAbility;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.BoltDamageSource;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import com.emoniph.witchery.util.TargetPointUtil;
import com.emoniph.witchery.util.TimeUtil;
import com.emoniph.witchery.util.TransformCreature;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class GenericEvents {

   @SubscribeEvent
   public void onServerChat(ServerChatEvent event) {
      boolean chatMasqueradeAllowed = Config.instance().allowChatMasquerading;
      ExtendedPlayer playerEx = ExtendedPlayer.get(event.player);
      if(playerEx != null && chatMasqueradeAllowed && playerEx.getCreatureType() == TransformCreature.PLAYER && playerEx.getOtherPlayerSkin() != null && !playerEx.getOtherPlayerSkin().isEmpty()) {
         String disguise = playerEx.getOtherPlayerSkin();
         ChatComponentTranslation comp = new ChatComponentTranslation("chat.type.text", new Object[]{this.getPlayerChatName(event.player, disguise), ForgeHooks.newChatWithLinks(event.message)});
         event.component = comp;
         if(!event.player.worldObj.isRemote) {
            Iterator i$ = event.player.worldObj.playerEntities.iterator();

            while(i$.hasNext()) {
               Object otherPlayerObj = i$.next();
               EntityPlayer otherPlayer = (EntityPlayer)otherPlayerObj;
               if(otherPlayer.capabilities.isCreativeMode && MinecraftServer.getServer().getConfigurationManager().func_152596_g(otherPlayer.getGameProfile())) {
                  ChatUtil.sendTranslated(EnumChatFormatting.GOLD, otherPlayer, "witchery.rite.mirrormirror.opchatreveal", new Object[]{disguise, event.player.getCommandSenderName()});
               }
            }
         }
      }

   }

   private IChatComponent getPlayerChatName(EntityPlayerMP player, String otherName) {
      ChatComponentText chatcomponenttext = new ChatComponentText(ScorePlayerTeam.formatPlayerName(player.getTeam(), otherName));
      chatcomponenttext.getChatStyle().setChatClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/msg " + player.getCommandSenderName() + " "));
      return chatcomponenttext;
   }

   @SubscribeEvent
   public void onEntityConstructing(EntityConstructing event) {
      if(event.entity instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer)event.entity) == null) {
         ExtendedPlayer.register((EntityPlayer)event.entity);
      } else if(event.entity instanceof EntityVillager && ExtendedVillager.get((EntityVillager)event.entity) == null) {
         ExtendedVillager.register((EntityVillager)event.entity);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public void onEntityJoinWorld(EntityJoinWorldEvent event) {
      if(event.entity instanceof EntityLivingBase) {
         NBTTagCompound creature = event.entity.getEntityData();
         creature.setFloat("WITCInitialWidth", event.entity.width);
         creature.setFloat("WITCInitialHeight", event.entity.height);
      }

      if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
         EntityPlayer creature1 = (EntityPlayer)event.entity;
         ExtendedPlayer.loadProxyData(creature1);
         Shapeshift.INSTANCE.initCurrentShift(creature1);
         Infusion.syncPlayer(event.world, creature1);
         Iterator i$ = event.world.playerEntities.iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            EntityPlayer otherPlayer = (EntityPlayer)obj;
            if(otherPlayer != creature1) {
               Witchery.packetPipeline.sendTo((IMessage)(new PacketPlayerStyle(otherPlayer)), creature1);
            }
         }

         Witchery.packetPipeline.sendToDimension(new PacketPlayerStyle(creature1), event.world.provider.dimensionId);
         if(creature1.dimension != Config.instance().dimensionDreamID && WorldProviderDreamWorld.getPlayerIsSpiritWalking(creature1) && !WorldProviderDreamWorld.getPlayerIsGhost(creature1)) {
            WorldProviderDreamWorld.setPlayerMustAwaken(creature1, true);
         } else if(creature1.dimension == Config.instance().dimensionDreamID && !WorldProviderDreamWorld.getPlayerIsSpiritWalking(creature1)) {
            WorldProviderDreamWorld.changeDimension(creature1, 0);
            WorldProviderDreamWorld.findTopAndSetPosition(creature1.worldObj, creature1);
         }
      } else if(event.world.provider.dimensionId == Config.instance().dimensionDreamID && isDisallowedEntity(event.entity)) {
         event.setCanceled(true);
      }

      if(event.entity instanceof EntityVillager && !(event.entity instanceof EntityVillagerWere) && !(event.entity instanceof EntityVillageGuard)) {
         EntityVillager creature4 = (EntityVillager)event.entity;
         creature4.tasks.addTask(1, new EntityAISleep(creature4));
      } else if(event.entity instanceof EntityZombie) {
         EntityZombie creature2 = (EntityZombie)event.entity;
         creature2.targetTasks.addTask(3, new EntityAINearestAttackableTarget(creature2, EntityFollower.class, 0, false, false, new IEntitySelector() {
            public boolean isEntityApplicable(Entity entity) {
               return entity instanceof EntityFollower && ((EntityFollower)entity).getFollowerType() == 0;
            }
         }));
      } else if(event.entity instanceof EntitySkeleton) {
         EntitySkeleton creature3 = (EntitySkeleton)event.entity;
         creature3.targetTasks.addTask(3, new EntityAINearestAttackableTarget(creature3, EntityFollower.class, 0, true, false, new IEntitySelector() {
            public boolean isEntityApplicable(Entity entity) {
               return entity instanceof EntityFollower && ((EntityFollower)entity).getFollowerType() == 0;
            }
         }));
      }

      if(event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
         Witchery.packetPipeline.sendToServer(new PacketExtendedEntityRequestSyncToClient((EntityLivingBase)event.entity));
      }

   }

   @SubscribeEvent
   public void onPlayerCloneEvent(Clone event) {
      NBTTagCompound oldPlayerNBT = new NBTTagCompound();
      ExtendedPlayer oldPlayerEx = ExtendedPlayer.get(event.original);
      oldPlayerEx.saveNBTData(oldPlayerNBT);
      ExtendedPlayer newPlayerEx = ExtendedPlayer.get(event.entityPlayer);
      newPlayerEx.loadNBTData(oldPlayerNBT);
      newPlayerEx.restorePlayerInventoryFrom(oldPlayerEx);
   }

   private static boolean isDisallowedEntity(Entity entity) {
      if(entity instanceof EntityLiving) {
         Class cls = entity.getClass();
         String packageName = cls.getCanonicalName();
         return !packageName.startsWith("net.minecraft.entity") && !packageName.startsWith("com.emoniph.witchery")?true:entity instanceof EntityEnderman;
      } else {
         return false;
      }
   }

   @SubscribeEvent
   public void onPlayerWakeUpEvent(PlayerWakeUpEvent event) {
      World world = event.entityPlayer.worldObj;
      if(!world.isRemote) {
         EntityPlayer player = event.entityPlayer;
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if(playerEx.isVampire() && player.isPlayerFullyAsleep()) {
            int x = MathHelper.floor_double(player.posX);
            int y = MathHelper.floor_double(player.posY);
            int z = MathHelper.floor_double(player.posZ);
            if(world.getBlock(x, y, z) == Witchery.Blocks.COFFIN) {
               Iterator iterator = world.playerEntities.iterator();

               EntityPlayer entityplayer;
               do {
                  if(!iterator.hasNext()) {
                     long currentTime = world.getWorldTime() - 11000L;
                     world.setWorldTime(currentTime);
                     break;
                  }

                  entityplayer = (EntityPlayer)iterator.next();
               } while(entityplayer.isPlayerFullyAsleep());
            }
         }
      }

   }

   @SubscribeEvent
   public void onPlayerSleepInBed(PlayerSleepInBedEvent event) {
      World world = event.entityPlayer.worldObj;
      EntityPlayer player = event.entityPlayer;
      if(CreatureUtil.isWerewolf(event.entityPlayer)) {
         if(!world.isRemote) {
            ChatUtil.sendTranslated(EnumChatFormatting.RED, event.entityPlayer, "witchery.nosleep.wolf", new Object[0]);
            event.result = EnumStatus.OTHER_PROBLEM;
         }
      } else if(event.entityPlayer.isPotionActive(Witchery.Potions.RESIZING)) {
         if(!world.isRemote) {
            ChatUtil.sendTranslated(EnumChatFormatting.RED, event.entityPlayer, "witchery.nosleep.resized", new Object[0]);
            event.result = EnumStatus.OTHER_PROBLEM;
         }
      } else if(ExtendedPlayer.get(event.entityPlayer).isVampire() && world.getBlock(event.x, event.y, event.z) == Witchery.Blocks.COFFIN) {
         if(event.entityPlayer.worldObj.isDaytime()) {
            if(!world.isRemote) {
               if(player.isPlayerSleeping() || !player.isEntityAlive()) {
                  return;
               }

               if(!world.provider.isSurfaceWorld()) {
                  return;
               }

               if(!world.isDaytime()) {
                  event.result = EnumStatus.OTHER_PROBLEM;
                  return;
               }

               if(Math.abs(player.posX - (double)event.x) > 3.0D || Math.abs(player.posY - (double)event.y) > 2.0D || Math.abs(player.posZ - (double)event.z) > 3.0D) {
                  event.result = EnumStatus.TOO_FAR_AWAY;
                  return;
               }

               double l = 8.0D;
               double f = 5.0D;
               List list = world.getEntitiesWithinAABB(EntityMob.class, AxisAlignedBB.getBoundingBox((double)event.x - l, (double)event.y - f, (double)event.z - l, (double)event.x + l, (double)event.y + f, (double)event.z + l));
               if(!list.isEmpty()) {
                  event.result = EnumStatus.NOT_SAFE;
                  return;
               }
            }

            if(player.isRiding()) {
               player.mountEntity((Entity)null);
            }

            PotionResizing.setEntitySize(player, 0.2F, 0.2F);
            player.yOffset = 0.2F;
            if(world.blockExists(event.x, event.y, event.z)) {
               int l1 = world.getBlock(event.x, event.y, event.z).getBedDirection(world, event.x, event.y, event.z);
               float f1 = 0.5F;
               float f2 = 0.5F;
               switch(l1) {
               case 0:
                  f2 = 0.9F;
                  break;
               case 1:
                  f1 = 0.1F;
                  break;
               case 2:
                  f2 = 0.1F;
                  break;
               case 3:
                  f1 = 0.9F;
               }

               player.field_71079_bU = 0.0F;
               player.field_71089_bV = 0.0F;
               switch(l1) {
               case 0:
                  player.field_71089_bV = -1.8F;
                  break;
               case 1:
                  player.field_71079_bU = 1.8F;
                  break;
               case 2:
                  player.field_71089_bV = 1.8F;
                  break;
               case 3:
                  player.field_71079_bU = -1.8F;
               }

               player.setPosition((double)((float)event.x + f1), (double)((float)event.y + 0.9375F), (double)((float)event.z + f2));
            } else {
               player.setPosition((double)((float)event.x + 0.5F), (double)((float)event.y + 0.9375F), (double)((float)event.z + 0.5F));
            }

            player.sleeping = true;
            player.sleepTimer = 0;
            player.playerLocation = new ChunkCoordinates(event.x, event.y, event.z);
            player.motionX = player.motionZ = player.motionY = 0.0D;
            if(!world.isRemote) {
               world.updateAllPlayersSleepingFlag();
            }

            event.result = EnumStatus.OK;
            return;
         }

         if(!world.isRemote) {
            ChatUtil.sendTranslated(EnumChatFormatting.RED, event.entityPlayer, "witchery.nosleep.dayonly", new Object[0]);
            event.result = EnumStatus.OTHER_PROBLEM;
         }
      }

   }

   @SubscribeEvent
   public void onLivingDrops(LivingDropsEvent event) {
      if(!event.isCanceled() && event.entityLiving != null && !event.entityLiving.worldObj.isRemote) {
         if(event.entityLiving instanceof EntityLiving && EntityUtil.isNoDrops((EntityLiving)event.entityLiving)) {
            event.setCanceled(true);
            return;
         }

         if(event.entityLiving instanceof EntityHorse) {
            EntityHorse horse = (EntityHorse)event.entityLiving;
            NBTTagCompound nbtHorse = horse.getEntityData();
            if(nbtHorse != null && nbtHorse.getBoolean("WITCIsBinky")) {
               event.drops.clear();
               event.drops.add(new EntityItem(horse.worldObj, horse.posX, horse.posY, horse.posZ, Witchery.Items.GENERIC.itemBinkyHead.createStack()));
            }
         }
      }

   }

   @SubscribeEvent
   public void onItemToss(ItemTossEvent event) {
      if(!event.isCanceled() && !event.player.worldObj.isRemote && event.entityItem != null && event.entityItem.getEntityItem() != null) {
         if(event.entityItem.getEntityItem().getItem() == Witchery.Items.SEEDS_MINDRAKE) {
            event.entityItem.lifespan = TimeUtil.secsToTicks(3);
            NBTTagCompound nbtItem = event.entityItem.getEntityData();
            nbtItem.setString("WITCThrower", event.player.getCommandSenderName());
         } else if(Witchery.Items.GENERIC.itemWaystone.isMatch(event.entityItem.getEntityItem()) || Witchery.Items.GENERIC.itemWaystoneBound.isMatch(event.entityItem.getEntityItem()) || Witchery.Items.GENERIC.itemAttunedStone.isMatch(event.entityItem.getEntityItem()) || Witchery.Items.GENERIC.itemSubduedSpirit.isMatch(event.entityItem.getEntityItem()) || Witchery.Items.GENERIC.itemWaystonePlayerBound.isMatch(event.entityItem.getEntityItem())) {
            EntityUtil.spawnEntityInWorld(event.entity.worldObj, new EntityItemWaystone(event.entityItem));
            event.setCanceled(true);
         }
      }

   }

   @SubscribeEvent
   public void onItemExpireEvent(ItemExpireEvent event) {
      if(!event.isCanceled() && !event.entityItem.worldObj.isRemote && event.entityItem != null && event.entityItem.getEntityItem() != null && event.entityItem.getEntityItem().getItem() == Witchery.Items.SEEDS_MINDRAKE) {
         for(int i = 0; i < event.entityItem.getEntityItem().stackSize; ++i) {
            EntityMindrake mindrake = new EntityMindrake(event.entityItem.worldObj);
            mindrake.setLocationAndAngles(event.entityItem.posX, event.entityItem.posY, event.entityItem.posZ, 0.0F, 0.0F);
            NBTTagCompound nbtItem = event.entityItem.getEntityData();
            if(nbtItem.hasKey("WITCThrower")) {
               String thrower = nbtItem.getString("WITCThrower");
               if(thrower != null && !thrower.isEmpty()) {
                  mindrake.func_110163_bv();
                  mindrake.setTamed(true);
                  TameableUtil.setOwnerByUsername(mindrake, thrower);
               }
            }

            ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_POP, mindrake, 1.0D, 1.0D, 16);
            event.entityItem.worldObj.spawnEntityInWorld(mindrake);
         }
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public void onPlayerDrops(PlayerDropsEvent event) {
      if(!event.entityPlayer.worldObj.isRemote && !event.isCanceled() && ExtendedPlayer.get(event.entityPlayer).isVampire()) {
         int ticks = TimeUtil.minsToTicks(MathHelper.clamp_int(Config.instance().vampireDeathItemKeepAliveMins, 5, 30));

         EntityItem item;
         for(Iterator i$ = event.drops.iterator(); i$.hasNext(); item.lifespan = ticks) {
            item = (EntityItem)i$.next();
         }
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public void onEntityInteract(EntityInteractEvent event) {
      PotionEffect effect = event.entityPlayer.getActivePotionEffect(Witchery.Potions.PARALYSED);
      if(effect != null && effect.getAmplifier() >= 4) {
         event.setCanceled(true);
      } else {
         ExtendedPlayer playerEx = ExtendedPlayer.get(event.entityPlayer);
         ExtendedPlayer.VampirePower power = playerEx.getSelectedVampirePower();
         if(power != ExtendedPlayer.VampirePower.NONE) {
            if(power == ExtendedPlayer.VampirePower.DRINK && event.target instanceof EntityLivingBase) {
               if(!event.entityPlayer.worldObj.isRemote) {
                  float var12 = ((EntityLivingBase)event.target).isPotionActive(Witchery.Potions.PARALYSED)?2.1F:1.3F;
                  if(event.target.getDistanceSq(event.entityPlayer.posX, event.target.posY, event.entityPlayer.posZ) <= (double)(var12 * var12)) {
                     int victim = ItemVampireClothes.isDrinkBoostActive(event.entityPlayer)?15:10;
                     if(CreatureUtil.isWerewolf(event.target, true)) {
                        event.entityPlayer.attackEntityFrom(new EntityDamageSource(DamageSource.magic.getDamageType(), event.entityPlayer), 4.0F);
                        ParticleEffect.FLAME.send(SoundEffect.WITCHERY_RANDOM_DRINK, event.entityPlayer.worldObj, event.target.posX, event.target.posY + (double)event.target.height * 0.8D, event.target.posZ, 0.5D, 0.2D, 16);
                     } else if(event.target instanceof EntityVillageGuard) {
                        EntityVillageGuard village = (EntityVillageGuard)event.target;
                        playerEx.increaseBloodPower(village.takeBlood(playerEx.getCreatureType() == TransformCreature.NONE?victim:2, event.entityPlayer));
                        ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, event.entityPlayer.worldObj, village.posX, village.posY + (double)village.height * 0.8D, village.posZ, 0.5D, 0.2D, 16);
                        this.checkForBloodDrinkingWitnesses(event.entityPlayer, village);
                     } else if(event.target instanceof EntityVillager) {
                        EntityVillager var19 = (EntityVillager)event.target;
                        ExtendedVillager rep = ExtendedVillager.get(var19);
                        playerEx.increaseBloodPower(rep.takeBlood(playerEx.getCreatureType() == TransformCreature.NONE?victim:2, event.entityPlayer));
                        ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, event.entityPlayer.worldObj, var19.posX, var19.posY + (double)var19.height * 0.8D, var19.posZ, 0.5D, 0.2D, 16);
                        this.checkForBloodDrinkingWitnesses(event.entityPlayer, var19);
                        if(playerEx.getVampireLevel() == 2) {
                           if(Config.instance().allowVampireQuests && rep.getBlood() >= 250 && rep.getBlood() <= 280) {
                              if(playerEx.getVampireQuestCounter() >= 5) {
                                 playerEx.increaseVampireLevel();
                              } else {
                                 SoundEffect.NOTE_PLING.playOnlyTo(event.entityPlayer, 1.0F, 1.0F);
                                 playerEx.increaseVampireQuestCounter();
                              }
                           } else if(rep.getBlood() < 240) {
                              playerEx.resetVampireQuestCounter();
                           }
                        } else if(playerEx.getVampireLevel() == 8 && playerEx.canIncreaseVampireLevel() && this.villagerIsInCage(var19)) {
                           if(rep.getBlood() >= 250 && rep.getBlood() <= 280) {
                              if(playerEx.getVampireQuestCounter() >= 5) {
                                 playerEx.increaseVampireLevel();
                              } else {
                                 SoundEffect.NOTE_PLING.playOnlyTo(event.entityPlayer, 1.0F, 1.0F);
                                 playerEx.increaseVampireQuestCounter();
                              }
                           } else if(rep.getBlood() < 240) {
                              playerEx.resetVampireQuestCounter();
                           }
                        }
                     } else if(event.target instanceof EntityPlayer) {
                        EntityPlayer var22 = (EntityPlayer)event.target;
                        playerEx.increaseBloodPower(ExtendedPlayer.get(var22).takeHumanBlood(playerEx.getCreatureType() == TransformCreature.NONE?victim:2, event.entityPlayer));
                        ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, event.entityPlayer.worldObj, var22.posX, var22.posY + (double)var22.height * 0.8D, var22.posZ, 0.5D, 0.2D, 16);
                     } else if(event.target instanceof IAnimals) {
                        EntityLivingBase var21 = (EntityLivingBase)event.target;
                        var21.attackEntityFrom(new EntityDamageSource(DamageSource.magic.getDamageType(), event.entityPlayer), 1.0F);
                        playerEx.increaseBloodPower(2, (int)Math.ceil((double)((float)playerEx.getMaxBloodPower() * 0.25F)));
                        ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, event.entityPlayer.worldObj, var21.posX, var21.posY + (double)var21.height * 0.8D, var21.posZ, 0.5D, 0.2D, 16);
                     }
                  }

                  event.setCanceled(true);
               }
            } else if(power == ExtendedPlayer.VampirePower.MESMERIZE) {
               if(!event.entityPlayer.worldObj.isRemote) {
                  if(event.entityPlayer.isSneaking() && playerEx.getVampireLevel() >= 2) {
                     playerEx.toggleVampireVision();
                  } else if(playerEx.getCreatureType() == TransformCreature.NONE && playerEx.getVampireLevel() >= 2) {
                     if((!(event.target instanceof EntityVillager) || event.target instanceof EntityVillagerWere) && !(event.target instanceof EntityPlayer) && !(event.target instanceof EntityVillageGuard)) {
                        SoundEffect.NOTE_SNARE.playOnlyTo(event.entityPlayer, 1.0F, 0.5F);
                     } else {
                        EntityLivingBase heldStack = (EntityLivingBase)event.target;
                        if(!heldStack.isPotionActive(Witchery.Potions.PARALYSED)) {
                           if(playerEx.decreaseBloodPower(ExtendedPlayer.VampirePower.MESMERIZE.INITIAL_COST, true)) {
                              heldStack.addPotionEffect(new PotionEffect(Witchery.Potions.PARALYSED.id, TimeUtil.secsToTicks(5 + playerEx.getVampireLevel() / 2 + Math.max(0, (playerEx.getVampireLevel() - 4) / 2) + (ItemVampireClothes.isMezmeriseBoostActive(event.entityPlayer)?3:0)), playerEx.getVampireLevel() >= 8?5:4));
                              SoundEffect.WITCHERY_RANDOM_HYPNOSIS.playAtPlayer(event.entity.worldObj, event.entityPlayer, 0.5F, 1.0F);
                           } else {
                              SoundEffect.NOTE_SNARE.playOnlyTo(event.entityPlayer, 1.0F, 0.5F);
                           }
                        } else {
                           SoundEffect.NOTE_SNARE.playOnlyTo(event.entityPlayer, 1.0F, 0.5F);
                        }
                     }
                  } else {
                     SoundEffect.NOTE_SNARE.playOnlyTo(event.entityPlayer, 1.0F, 0.5F);
                  }

                  event.setCanceled(true);
               }
            } else {
               event.setCanceled(true);
            }
         }

         if(!event.isCanceled()) {
            if(event.target != null && !event.target.worldObj.isRemote && event.target instanceof EntityLiving && PotionEnslaved.isMobEnslavedBy((EntityLiving)event.target, event.entityPlayer)) {
               EntityPlayer var14 = event.entityPlayer;
               EntityLiving var18 = (EntityLiving)event.target;
               ItemStack var26 = var14.getHeldItem();
               if(Witchery.Items.GENERIC.itemGraveyardDust.isMatch(var26) && var18 instanceof EntitySummonedUndead) {
                  float var23 = var18.getMaxHealth() + 2.0F;
                  if(var23 <= 50.0F) {
                     IAttributeInstance list = var18.getEntityAttribute(SharedMonsterAttributes.maxHealth);
                     if(list != null) {
                        list.setBaseValue((double)var23);
                        var18.setHealth(var23);
                        var18.func_110163_bv();
                        Witchery.packetPipeline.sendToAllAround(new PacketParticles(ParticleEffect.INSTANT_SPELL, SoundEffect.MOB_SILVERFISH_KILL, var18, 0.5D, 1.0D), TargetPointUtil.from(var18, 16.0D));
                        if(!var14.capabilities.isCreativeMode) {
                           --var26.stackSize;
                           if(var14 instanceof EntityPlayerMP) {
                              ((EntityPlayerMP)var14).sendContainerToPlayer(var14.inventoryContainer);
                           }
                        }
                     }
                  }
               } else if(var26 != null && (var18 instanceof EntityZombie || var18 instanceof EntityPigZombie || var18 instanceof EntitySkeleton)) {
                  if(var26.getItem() instanceof ItemArmor) {
                     ItemArmor var25 = (ItemArmor)var26.getItem();
                     if(var18.getEquipmentInSlot(4 - var25.armorType) == null) {
                        var18.setCurrentItemOrArmor(4 - var25.armorType, var26.splitStack(1));
                        var18.func_110163_bv();
                        if(var14 instanceof EntityPlayerMP) {
                           ((EntityPlayerMP)var14).sendContainerToPlayer(var14.inventoryContainer);
                        }
                     }
                  } else if(var26.getItem() instanceof ItemSword && var18.getEquipmentInSlot(0) == null) {
                     var18.setCurrentItemOrArmor(0, var26.splitStack(1));
                     var18.func_110163_bv();
                     if(var14 instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)var14).sendContainerToPlayer(var14.inventoryContainer);
                     }
                  }
               }
            }

            if(event.target != null && !event.target.worldObj.isRemote && event.target instanceof EntityVillager) {
               EntityVillager var13 = (EntityVillager)event.target;
               ItemStack var16 = event.entityPlayer.getHeldItem();
               if(!var13.isChild() && var16 != null && var16.getItem() == Items.leather_chestplate && event.entityPlayer.isSneaking()) {
                  Village var24 = var13.villageObj;
                  if(var24 != null) {
                     int var28 = var24.getReputationForPlayer(event.entityPlayer.getCommandSenderName());
                     if(var28 >= 10) {
                        if(var24.getNumVillagers() > 8) {
                           List var27 = event.entity.worldObj.getEntitiesWithinAABB(EntityVillageGuard.class, AxisAlignedBB.getBoundingBox((double)(var24.getCenter().posX - var24.getVillageRadius()), (double)(var24.getCenter().posY - 4), (double)(var24.getCenter().posZ - var24.getVillageRadius()), (double)(var24.getCenter().posX + var24.getVillageRadius()), (double)(var24.getCenter().posY + 4), (double)(var24.getCenter().posZ + var24.getVillageRadius())));
                           int numGuards = var27.size();
                           if(numGuards < MathHelper.floor_double((double)var24.getNumVillagers() * 0.25D)) {
                              int villagerNumTrades = var13.buyingList == null?1:var13.buyingList.size();
                              if(!CreatureUtil.isWerewolf(event.target, true) && event.target.worldObj.rand.nextInt(villagerNumTrades * 2 + 1) == 0) {
                                 var13.playSound("mob.villager.yew", 1.0F, (var13.worldObj.rand.nextFloat() - var13.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
                                 ChatUtil.sendTranslated(EnumChatFormatting.GREEN, event.entityPlayer, "witchery.village.villageracceptsguardduty", new Object[0]);
                                 EntityVillageGuard.createFrom(var13);
                              } else {
                                 ChatUtil.sendTranslated(EnumChatFormatting.RED, event.entityPlayer, "witchery.village.villagerrefusesguardduty", new Object[0]);
                                 var13.playSound("mob.villager.no", 1.0F, (var13.worldObj.rand.nextFloat() - var13.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
                              }
                           } else {
                              ChatUtil.sendTranslated(EnumChatFormatting.BLUE, event.entityPlayer, "witchery.village.toomanyguards", new Object[0]);
                              SoundEffect.NOTE_SNARE.playOnlyTo(event.entityPlayer);
                           }
                        } else {
                           ChatUtil.sendTranslated(EnumChatFormatting.BLUE, event.entityPlayer, "witchery.village.villagetoosmall", new Object[0]);
                           SoundEffect.NOTE_SNARE.playOnlyTo(event.entityPlayer);
                        }
                     } else {
                        ChatUtil.sendTranslated(EnumChatFormatting.BLUE, event.entityPlayer, "witchery.village.reptoolow", new Object[0]);
                        SoundEffect.NOTE_SNARE.playOnlyTo(event.entityPlayer);
                     }
                  }
               }
            }

            if(!event.entity.worldObj.isRemote && event.target != null && event.target instanceof EntityWolf) {
               EntityWolf var17 = (EntityWolf)event.target;
               if(playerEx.getWerewolfLevel() == 7 && playerEx.getWolfmanQuestState() == ExtendedPlayer.QuestState.STARTED && playerEx.getCreatureType() == TransformCreature.WOLF && !var17.isTamed() && !var17.isAngry()) {
                  if(var17.worldObj.rand.nextInt(3) == 0) {
                     var17.setTamed(true);
                     var17.setPathToEntity((PathEntity)null);
                     var17.setAttackTarget((EntityLivingBase)null);
                     var17.func_70907_r().setSitting(true);
                     var17.setHealth(20.0F);
                     var17.func_152115_b(event.entityPlayer.getUniqueID().toString());
                     this.playTameEffect(var17, true);
                     var17.worldObj.setEntityState(var17, (byte)7);
                     playerEx.increaseWolfmanQuestCounter();
                  } else {
                     this.playTameEffect(var17, false);
                     var17.worldObj.setEntityState(var17, (byte)6);
                     if(var17.worldObj.rand.nextInt(10) == 0) {
                        var17.setAngry(true);
                        var17.setAttackTarget(event.entityPlayer);
                     }
                  }
               }
            }

            ItemStack var15 = event.entityPlayer.getHeldItem();
            if(var15 != null) {
               if(var15.getItem() == Witchery.Items.TAGLOCK_KIT) {
                  Witchery.Items.TAGLOCK_KIT.onEntityInteract(event.entityPlayer.worldObj, event.entityPlayer, var15, event);
                  if(event.isCanceled()) {
                     return;
                  }
               }

               if(var15.getItem() == Witchery.Items.BLOOD_GOBLET) {
                  Witchery.Items.BLOOD_GOBLET.onEntityInteract(event.entityPlayer.worldObj, event.entityPlayer, var15, event);
                  if(event.isCanceled()) {
                     return;
                  }
               }

               if(Witchery.Items.GENERIC.itemWoodenStake.isMatch(var15) && Config.instance().allowStakingVampires && event.target instanceof EntityPlayer) {
                  EntityPlayer var20 = (EntityPlayer)event.target;
                  if(ExtendedPlayer.get(var20).isVampire() && var20.sleeping) {
                     ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, var20.worldObj, event.target.posX, event.target.posY, event.target.posZ, (double)event.target.width, (double)event.target.height, 16);
                     EntityUtil.instantDeath(var20, event.entityPlayer);
                     if(!event.entityPlayer.capabilities.isCreativeMode) {
                        --var15.stackSize;
                     }

                     event.setCanceled(true);
                     return;
                  }
               }
            }

         }
      }
   }

   private boolean villagerIsInCage(EntityVillager target) {
      int ogX = MathHelper.floor_double(target.posX);
      int ogY = MathHelper.floor_double(target.posY);
      int ogZ = MathHelper.floor_double(target.posZ);
      return this.isCaged(target.worldObj, ogX, ogY, ogZ)?true:(this.isCaged(target.worldObj, ogX + 1, ogY, ogZ)?true:(this.isCaged(target.worldObj, ogX, ogY, ogZ + 1)?true:(this.isCaged(target.worldObj, ogX - 1, ogY, ogZ)?true:(this.isCaged(target.worldObj, ogX, ogY, ogZ - 1)?true:(this.isCaged(target.worldObj, ogX + 1, ogY, ogZ + 1)?true:(this.isCaged(target.worldObj, ogX + 1, ogY, ogZ - 1)?true:(this.isCaged(target.worldObj, ogX - 1, ogY, ogZ + 1)?true:this.isCaged(target.worldObj, ogX - 1, ogY, ogZ - 1))))))));
   }

   private boolean isCaged(World world, int x, int y, int z) {
      byte count = 0;
      Block bars = Blocks.iron_bars;
      int var7 = count + (world.getBlock(x + 1, y, z) == bars?1:0);
      var7 += world.getBlock(x, y, z + 1) == bars?1:0;
      var7 += world.getBlock(x - 1, y, z) == bars?1:0;
      var7 += world.getBlock(x, y, z - 1) == bars?1:0;
      var7 += world.getBlock(x + 1, y, z + 1) == bars?1:0;
      var7 += world.getBlock(x - 1, y, z + 1) == bars?1:0;
      var7 += world.getBlock(x + 1, y, z - 1) == bars?1:0;
      var7 += world.getBlock(x - 1, y, z - 1) == bars?1:0;
      ++y;
      var7 += world.getBlock(x + 1, y, z) == bars?1:0;
      var7 += world.getBlock(x, y, z + 1) == bars?1:0;
      var7 += world.getBlock(x - 1, y, z) == bars?1:0;
      var7 += world.getBlock(x, y, z - 1) == bars?1:0;
      var7 += world.getBlock(x + 1, y, z + 1) == bars?1:0;
      var7 += world.getBlock(x - 1, y, z + 1) == bars?1:0;
      var7 += world.getBlock(x + 1, y, z - 1) == bars?1:0;
      var7 += world.getBlock(x - 1, y, z - 1) == bars?1:0;
      if(var7 < 15) {
         return false;
      } else {
         count = 0;
         ++y;
         var7 = count + (!BlockUtil.isReplaceableBlock(world, x + 1, y, z)?1:0);
         var7 += !BlockUtil.isReplaceableBlock(world, x, y, z + 1)?1:0;
         var7 += !BlockUtil.isReplaceableBlock(world, x - 1, y, z)?1:0;
         var7 += !BlockUtil.isReplaceableBlock(world, x, y, z - 1)?1:0;
         var7 += !BlockUtil.isReplaceableBlock(world, x + 1, y, z + 1)?1:0;
         var7 += !BlockUtil.isReplaceableBlock(world, x - 1, y, z + 1)?1:0;
         var7 += !BlockUtil.isReplaceableBlock(world, x + 1, y, z - 1)?1:0;
         var7 += !BlockUtil.isReplaceableBlock(world, x - 1, y, z - 1)?1:0;
         var7 += !BlockUtil.isReplaceableBlock(world, x, y, z)?1:0;
         return var7 >= 9;
      }
   }

   private void checkForBloodDrinkingWitnesses(EntityPlayer player, EntityLivingBase victim) {
      AxisAlignedBB bounds = victim.boundingBox.expand(16.0D, 8.0D, 16.0D);
      List guards = victim.worldObj.getEntitiesWithinAABB(EntityVillageGuard.class, bounds);
      Iterator i$ = guards.iterator();

      while(i$.hasNext()) {
         EntityVillageGuard guard = (EntityVillageGuard)i$.next();
         if(!guard.isPotionActive(Witchery.Potions.PARALYSED) && guard.getEntitySenses().canSee(victim)) {
            guard.setAttackTarget(player);
         }
      }

   }

   @SubscribeEvent
   public void onPlayerInteract(PlayerInteractEvent event) {
      PotionEffect effect = event.entityPlayer.getActivePotionEffect(Witchery.Potions.PARALYSED);
      if(effect != null && effect.getAmplifier() >= 4) {
         event.setCanceled(true);
      } else {
         ExtendedPlayer playerEx = ExtendedPlayer.get(event.entityPlayer);
         if(playerEx.getSelectedVampirePower() != ExtendedPlayer.VampirePower.NONE) {
            if(event.action == net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
               switch(GenericEvents.NamelessClass2021840805.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[playerEx.getSelectedVampirePower().ordinal()]) {
               case 1:
               case 2:
               case 3:
               case 4:
                  Witchery.packetPipeline.sendToServer(new PacketSelectPlayerAbility(playerEx, true));
               default:
                  event.setCanceled(true);
               }
            }
         } else if(event.entityPlayer.worldObj.isRemote) {
            if((event.action == net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_AIR || event.action == net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) && event.entityPlayer.rotationPitch == -90.0F && event.entityPlayer.isSneaking()) {
               Witchery.packetPipeline.sendToServer(new PacketHowl());
            }
         } else if(event.action == net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.LEFT_CLICK_BLOCK && !event.entityPlayer.capabilities.isCreativeMode) {
            if(playerEx.isVampire() && event.world.getBlock(event.x, event.y, event.z) == Witchery.Blocks.GARLIC_GARLAND) {
               event.entityPlayer.setFire(1);
               event.setCanceled(true);
            } else {
               Block block;
               if(playerEx.getCreatureType() == TransformCreature.WOLF && playerEx.getWerewolfLevel() >= 3 && event.entityPlayer.isSneaking()) {
                  block = event.world.getBlock(event.x, event.y, event.z);
                  if(block == Blocks.grass || block == Blocks.sand || block == Blocks.dirt || block == Blocks.mycelium || block == Blocks.gravel) {
                     EntityAIDigBlocks.tryHarvestBlock(event.world, event.x, event.y, event.z, event.entityPlayer, event.entityPlayer);
                     event.setCanceled(true);
                  }
               } else if(playerEx.getVampireLevel() >= 6 && playerEx.getCreatureType() == TransformCreature.NONE && event.entityPlayer.isSneaking() && (event.entityPlayer.getHeldItem() == null || !event.entityPlayer.getHeldItem().getItem().func_150897_b(Blocks.stone)) && event.entityPlayer.getFoodStats().getFoodLevel() > 0) {
                  block = event.world.getBlock(event.x, event.y, event.z);
                  if(block == Blocks.stone || block == Blocks.netherrack || block == Blocks.cobblestone) {
                     EntityAIDigBlocks.tryHarvestBlock(event.world, event.x, event.y, event.z, event.entityPlayer, event.entityPlayer);
                     event.entityPlayer.addExhaustion(10.0F);
                     event.setCanceled(true);
                  }
               }
            }
         }

      }
   }

   private void playTameEffect(EntityTameable entity, boolean tamed) {
      String s = "heart";
      if(!tamed) {
         s = "smoke";
      }

      for(int i = 0; i < 7; ++i) {
         double d0 = entity.worldObj.rand.nextGaussian() * 0.02D;
         double d1 = entity.worldObj.rand.nextGaussian() * 0.02D;
         double d2 = entity.worldObj.rand.nextGaussian() * 0.02D;
         entity.worldObj.spawnParticle(s, entity.posX + (double)(entity.worldObj.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, entity.posY + 0.5D + (double)(entity.worldObj.rand.nextFloat() * entity.height), entity.posZ + (double)(entity.worldObj.rand.nextFloat() * entity.width * 2.0F) - (double)entity.width, d0, d1, d2);
      }

   }

   @SubscribeEvent
   public void onLivingUpdate(LivingUpdateEvent event) {
      if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.entity;
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         Shapeshift.INSTANCE.updatePlayerState(player, playerEx);
         playerEx.tick();
         if(playerEx.isVampire()) {
            int closestVillage = player.getFoodStats().prevFoodLevel;
            int isWolfman = player.getFoodStats().getFoodLevel();
            if(closestVillage < isWolfman) {
               player.getFoodStats().addStats(-player.getFoodStats().getFoodLevel(), 0.0F);
            }
         }

         if(event.entity.ticksExisted % 40 == 1) {
            if(playerEx.getWerewolfLevel() > 0) {
               boolean closestVillage1 = CreatureUtil.isFullMoon(player.worldObj);
               switch(GenericEvents.NamelessClass2021840805.$SwitchMap$com$emoniph$witchery$util$TransformCreature[playerEx.getCreatureType().ordinal()]) {
               case 1:
               case 2:
                  boolean isWolfman1 = playerEx.getCreatureType() == TransformCreature.WOLFMAN;
                  if(!closestVillage1 && !player.inventory.hasItem(Witchery.Items.MOON_CHARM) && !ItemMoonCharm.isWolfsbaneActive(player, playerEx)) {
                     Shapeshift.INSTANCE.shiftTo(player, TransformCreature.NONE);
                     ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_FIZZ, player, 1.5D, 1.5D, 16);
                  } else {
                     updateWerewolfEffects(player, isWolfman1);
                  }
                  break;
               case 3:
                  if(closestVillage1 && !player.inventory.hasItem(Witchery.Items.MOON_CHARM) && !ItemMoonCharm.isWolfsbaneActive(player, playerEx)) {
                     Shapeshift.INSTANCE.shiftTo(player, TransformCreature.WOLF);
                     ParticleEffect.EXPLODE.send(SoundEffect.WITCHERY_MOB_WOLFMAN_HOWL, player, 1.5D, 1.5D, 16);
                     updateWerewolfEffects(player, false);
                  }
               }
            }

            if(playerEx.isVampire()) {
               if(player.isInWater()) {
                  player.setAir(300);
               }

               if(playerEx.getCreatureType() == TransformCreature.BAT && !playerEx.decreaseBloodPower(ExtendedPlayer.VampirePower.BAT.UPKEEP_COST, true)) {
                  Shapeshift.INSTANCE.shiftTo(player, TransformCreature.NONE);
               }

               if(playerEx.getVampireLevel() == 3 && !player.worldObj.isDaytime()) {
                  if(playerEx.getVampireQuestCounter() < 300 && (playerEx.getVampireQuestCounter() < 10 || !player.capabilities.isCreativeMode)) {
                     if(Config.instance().allowVampireQuests) {
                        playerEx.increaseVampireQuestCounter();
                     }
                  } else if(playerEx.canIncreaseVampireLevel()) {
                     playerEx.increaseVampireLevel();
                  }
               }

               if(playerEx.getVampireLevel() == 7 && playerEx.canIncreaseVampireLevel()) {
                  Village closestVillage2 = player.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), 32);
                  if(closestVillage2 != null && playerEx.storeVampireQuestChunk(closestVillage2.getCenter().posX >> 4, closestVillage2.getCenter().posZ >> 4)) {
                     if(playerEx.getVampireQuestCounter() >= 3) {
                        playerEx.increaseVampireLevel();
                     } else {
                        playerEx.increaseVampireQuestCounter();
                        SoundEffect.NOTE_PLING.playOnlyTo(player, 1.0F, 1.0F);
                     }
                  }
               }

               if(playerEx.isVampireVisionActive()) {
                  player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 400, 0, true));
               }

               if(player.isPotionActive(Potion.poison)) {
                  player.removePotionEffect(Potion.poison.id);
               }

               if(player.isBurning() && player.isPotionActive(Potion.fireResistance)) {
                  player.attackEntityFrom(EntityUtil.DamageSourceVampireFire.SOURCE, 2.0F);
               }

               while(player.getFoodStats().getFoodLevel() < 20 && playerEx.decreaseBloodPower(5, true)) {
                  player.getFoodStats().addStats(1, 4.0F);
               }

               if(playerEx.getBloodPower() == 0 && player.getFoodStats().getFoodLevel() == 0) {
                  player.addPotionEffect(new PotionEffect(Potion.weakness.id, TimeUtil.secsToTicks(10), 8, true));
                  player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, TimeUtil.secsToTicks(10), 1, true));
                  player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, TimeUtil.secsToTicks(10), 1, true));
               }

               if(CreatureUtil.isInSunlight(player) && !player.capabilities.isCreativeMode) {
                  if(playerEx.getBloodPower() == 0 && player.ticksExisted > 400) {
                     EntityUtil.instantDeath(player, (EntityLivingBase)null);
                  }

                  if(playerEx.getVampireLevel() >= 5) {
                     playerEx.decreaseBloodPower(60, false);
                     player.addPotionEffect(new PotionEffect(Potion.weakness.id, TimeUtil.secsToTicks(10), 3, false));
                     player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, TimeUtil.secsToTicks(10), 0, true));
                     player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, TimeUtil.secsToTicks(10), 0, true));
                  } else {
                     playerEx.setBloodPower(0);
                  }

                  if(playerEx.getBloodPower() == 0) {
                     player.setFire(5);
                  }
               }
            } else {
               playerEx.giveHumanBlood(2);
            }
         }
      }

   }

   public static void updateWerewolfEffects(EntityPlayer player, boolean isWolfman) {
      player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 400, 0, true));
      if(player.isPotionActive(Potion.poison)) {
         player.removePotionEffect(Potion.poison.id);
      }

      for(int slot = isWolfman?0:1; slot <= 4; ++slot) {
         ItemStack stack = player.getEquipmentInSlot(slot);
         if(stack != null && stack.getItem() != Witchery.Items.MOON_CHARM && (player.openContainer == null || player.openContainer.windowId == 0 || slot != 0)) {
            player.entityDropItem(stack, 1.0F);
            player.setCurrentItemOrArmor(slot, (ItemStack)null);
         }
      }

   }

   @SubscribeEvent
   public void onLivingFall(LivingFallEvent event) {
      if(!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer) {
         event.distance = Shapeshift.INSTANCE.updateFallState((EntityPlayer)event.entity, event.distance);
      }

   }

   @SubscribeEvent
   public void onHarvestDrops(HarvestDropsEvent event) {
      if(!event.world.isRemote && event.harvester != null && !event.isCanceled()) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(event.harvester);
         Shapeshift.INSTANCE.processDigging(event, event.harvester, playerEx);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public void onLivingHurt(LivingHurtEvent event) {
      if(!event.entityLiving.worldObj.isRemote && !event.isCanceled()) {
         this.checkForChargeDamage(event);
         if(event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            float playerHealth = player.getHealth();
            ExtendedPlayer playerEx = ExtendedPlayer.get(player);
            if(event.source == DamageSource.drown && playerEx.isVampire()) {
               event.setCanceled(true);
               return;
            }

            boolean wolfForm = playerEx.getWerewolfLevel() > 0 && (playerEx.getCreatureType() == TransformCreature.WOLF || playerEx.getCreatureType() == TransformCreature.WOLFMAN);
            float healthAfterDamage;
            if(wolfForm && event.source != DamageSource.outOfWorld && event.source != DamageSource.inWall && event.source != DamageSource.drown && event.source != DamageSource.fall) {
               if(!event.source.isFireDamage()) {
                  healthAfterDamage = Shapeshift.INSTANCE.getResistance(player, playerEx);
                  event.ammount = Math.max(0.0F, event.ammount - healthAfterDamage);
               }

               if(!CreatureUtil.isWerewolf(event.source.getSourceOfDamage())) {
                  if(!CreatureUtil.isSilverDamage(event.source)) {
                     event.ammount = Math.max(Math.min(event.ammount, Shapeshift.INSTANCE.getDamageCap(player, playerEx)), 0.5F);
                  } else {
                     event.ammount += 5.0F;
                  }
               }

               if(event.ammount <= 0.0F) {
                  event.setCanceled(true);
                  return;
               }
            }

            if(ItemDeathsClothes.isFullSetWorn(player) && player.getHeldItem() != null && player.getHeldItem().getItem() == Witchery.Items.DEATH_HAND) {
               event.ammount = Math.min(event.ammount, 7.0F);
            }

            healthAfterDamage = EntityUtil.getHealthAfterDamage(event, playerHealth, player);
            if((player.dimension == Config.instance().dimensionDreamID || WorldProviderDreamWorld.getPlayerIsGhost(player)) && healthAfterDamage <= 0.0F && !player.capabilities.isCreativeMode) {
               event.setCanceled(true);
               event.setResult(Result.DENY);
               WorldProviderDreamWorld.setPlayerMustAwaken(player, true);
               return;
            }

            dropItemsOnHit(player);
            boolean ignoreProtection = wolfForm || event.source instanceof BoltDamageSource && ((BoltDamageSource)event.source).isPoweredDraining;
            boolean hasHunterSet = ItemHunterClothes.isFullSetWorn(player, false);
            if(hasHunterSet && event.source.isMagicDamage() && player.worldObj.rand.nextDouble() < 0.25D) {
               event.setCanceled(true);
               return;
            }

            double MOB_SPAWN_CHANCE;
            boolean louseUsed;
            if((event.source instanceof EntityDamageSource || event.source.isExplosion()) && !ignoreProtection) {
               ItemStack entitySource = player.getEquipmentInSlot(4);
               if(entitySource != null && entitySource.getItem() == Witchery.Items.BABAS_HAT && player.dimension != Config.instance().dimensionTormentID) {
                  boolean belt = true;
                  MOB_SPAWN_CHANCE = 0.25D;
                  louseUsed = true;
                  if(player.worldObj.rand.nextDouble() < 0.25D && Infusion.aquireEnergy(player.worldObj, player, 5, true)) {
                     BlockVoidBramble.teleportRandomly(player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), player, 6);
                     event.setCanceled(true);
                     return;
                  }
               }
            }

            if(event.source instanceof EntityDamageSource) {
               EntityDamageSource var21 = (EntityDamageSource)event.source;
               ItemStack var23 = player.getEquipmentInSlot(2);
               int var28;
               if(var23 != null && var23.getItem() == Witchery.Items.BARK_BELT && !CreatureUtil.isWoodenDamage(event.source)) {
                  int var22 = Math.min(Witchery.Items.BARK_BELT.getChargeLevel(var23), Witchery.Items.BARK_BELT.getMaxChargeLevel(player));
                  if(var22 > 0) {
                     World world = player.worldObj;
                     Random var27 = world.rand;
                     var28 = var22 > 1 && var27.nextDouble() < 0.25D?2:1;
                     Witchery.Items.BARK_BELT.setChargeLevel(var23, Math.max(var22 - var28, 0));
                     event.setCanceled(true);

                     for(int var32 = 0; var32 < var28; ++var32) {
                        double var31 = 1.0D * (double)(var27.nextInt(2) == 0?-1:1);
                        double var34 = 1.0D * (double)(var27.nextInt(2) == 0?-1:1);
                        EntityItem item = new EntityItem(world, player.posX + var31, player.posY + 1.5D, player.posZ + var34, new ItemStack(Items.stick));
                        item.delayBeforeCanPickup = 60;
                        item.lifespan = 60;
                        world.spawnEntityInWorld(item);
                     }

                     return;
                  }
               }

               MOB_SPAWN_CHANCE = 0.25D;
               if(player.getHeldItem() != null && player.getHeldItem().getItem() == Witchery.Items.HUNTSMANS_SPEAR && player.isBlocking() && player.worldObj.rand.nextDouble() < 0.25D && var21.getEntity() != null && var21.getEntity() instanceof EntityLivingBase) {
                  EntityLivingBase var24 = (EntityLivingBase)var21.getEntity();
                  if(var24.isEntityAlive()) {
                     EntityWolf stack = new EntityWolf(player.worldObj);
                     stack.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationPitch, player.rotationYawHead);
                     stack.setAttackTarget(var24);
                     stack.setTarget(var24);
                     stack.setAngry(true);
                     stack.addPotionEffect(new PotionEffect(Potion.wither.id, 12000, 1));
                     player.worldObj.spawnEntityInWorld(stack);
                  }
               }

               louseUsed = false;
               var28 = 0;

               ItemStack done;
               PotionEffect list;
               List potion;
               InventoryPlayer var10001;
               while(true) {
                  var10001 = player.inventory;
                  if(var28 >= InventoryPlayer.getHotbarSize()) {
                     break;
                  }

                  done = player.inventory.getStackInSlot(var28);
                  if(done != null && done.getItem() == Witchery.Items.PARASYTIC_LOUSE && done.getItemDamage() > 0) {
                     potion = Items.potionitem.getEffects(done.getItemDamage());
                     if(potion != null && !potion.isEmpty()) {
                        label476: {
                           list = new PotionEffect((PotionEffect)potion.get(0));
                           if(isPotionAggressive(list.getPotionID()) && event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase) {
                              ((EntityLivingBase)event.source.getEntity()).addPotionEffect(list);
                           } else {
                              if(list.getPotionID() != Potion.regeneration.id) {
                                 break label476;
                              }

                              player.addPotionEffect(list);
                           }

                           player.attackEntityFrom(DamageSource.magic, 1.0F);
                           done.setItemDamage(0);
                           louseUsed = true;
                           break;
                        }
                     } else {
                        done.setItemDamage(0);
                     }
                  }

                  ++var28;
               }

               PotionEffect effect;
               ItemStack var25;
               boolean var26;
               int var30;
               List var33;
               if(!louseUsed && Witchery.Items.BITING_BELT.isBeltWorn(player)) {
                  var25 = player.inventory.armorItemInSlot(1);
                  if(var25 != null && var25.hasTagCompound()) {
                     var26 = false;
                     if(var25.getTagCompound().hasKey("WITCPotion")) {
                        var30 = var25.getTagCompound().getInteger("WITCPotion");
                        var33 = Items.potionitem.getEffects(var30);
                        if(var33 != null && !var33.isEmpty()) {
                           effect = new PotionEffect((PotionEffect)var33.get(0));
                           if(!player.isPotionActive(effect.getPotionID()) && effect.getPotionID() != Potion.regeneration.id) {
                              var26 = true;
                              if(isPotionAggressive(effect.getPotionID()) && event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase) {
                                 ((EntityLivingBase)event.source.getEntity()).addPotionEffect(effect);
                              } else {
                                 player.addPotionEffect(effect);
                              }

                              player.attackEntityFrom(DamageSource.magic, 1.0F);
                              var25.getTagCompound().removeTag("WITCPotion");
                              if(var25.getTagCompound().hasNoTags()) {
                                 var25.setTagCompound((NBTTagCompound)null);
                              }
                           }
                        }
                     }

                     if(!var26 && var25.getTagCompound().hasKey("WITCPotion2")) {
                        var30 = var25.getTagCompound().getInteger("WITCPotion2");
                        var33 = Items.potionitem.getEffects(var30);
                        if(var33 != null && !var33.isEmpty()) {
                           effect = new PotionEffect((PotionEffect)var33.get(0));
                           if(!player.isPotionActive(effect.getPotionID()) && effect.getPotionID() != Potion.regeneration.id) {
                              if(isPotionAggressive(effect.getPotionID()) && event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase) {
                                 ((EntityLivingBase)event.source.getEntity()).addPotionEffect(effect);
                              } else {
                                 player.addPotionEffect(effect);
                              }

                              player.attackEntityFrom(DamageSource.magic, 1.0F);
                              var25.getTagCompound().removeTag("WITCPotion2");
                              if(var25.getTagCompound().hasNoTags()) {
                                 var25.setTagCompound((NBTTagCompound)null);
                              }
                           }
                        }
                     }
                  }
               }

               this.checkForRendArmor(event);
               if(!ignoreProtection && !playerEx.isVampire()) {
                  ItemPoppet var10000 = Witchery.Items.POPPET;
                  var25 = ItemPoppet.findBoundPoppetInWorld(Witchery.Items.POPPET.vampiricPoppet, player, 66, true, false);
                  if(var25 != null) {
                     EntityWitchHunter.blackMagicPerformed(player);
                     EntityLivingBase var29 = Witchery.Items.TAGLOCK_KIT.getBoundEntity(player.worldObj, player, var25, Integer.valueOf(2));
                     if(var29 != null && !Witchery.Items.POPPET.voodooProtectionActivated(player, var25, var29, true, false) && !ItemHunterClothes.isFullSetWorn(var29, false)) {
                        if(var29 instanceof EntityPlayer) {
                           var29.attackEntityFrom(event.source, event.ammount);
                           event.setCanceled(true);
                        } else if(var29 instanceof EntityLiving && var29.isEntityAlive()) {
                           var29.attackEntityFrom(event.source, Math.min(event.ammount, 15.0F));
                           if(!var29.isEntityAlive()) {
                              Witchery.Items.TAGLOCK_KIT.clearTaglock(var25, Integer.valueOf(2));
                           }

                           event.setCanceled(true);
                        }

                        return;
                     }
                  }
               }

               if(!louseUsed) {
                  var28 = 0;

                  while(true) {
                     var10001 = player.inventory;
                     if(var28 >= InventoryPlayer.getHotbarSize()) {
                        break;
                     }

                     done = player.inventory.getStackInSlot(var28);
                     if(done != null && done.getItem() == Witchery.Items.PARASYTIC_LOUSE && done.getItemDamage() > 0) {
                        potion = Items.potionitem.getEffects(done.getItemDamage());
                        if(potion != null && !potion.isEmpty()) {
                           list = new PotionEffect((PotionEffect)potion.get(0));
                           if(list.getPotionID() == Potion.regeneration.id) {
                              player.addPotionEffect(list);
                           }

                           player.attackEntityFrom(DamageSource.magic, 1.0F);
                           done.setItemDamage(0);
                           louseUsed = true;
                           break;
                        }

                        done.setItemDamage(0);
                     }

                     ++var28;
                  }
               }

               if(!louseUsed && Witchery.Items.BITING_BELT.isBeltWorn(player)) {
                  var25 = player.inventory.armorItemInSlot(1);
                  if(var25 != null && var25.hasTagCompound()) {
                     var26 = false;
                     if(var25.getTagCompound().hasKey("WITCPotion")) {
                        var30 = var25.getTagCompound().getInteger("WITCPotion");
                        var33 = Items.potionitem.getEffects(var30);
                        if(var33 != null && !var33.isEmpty()) {
                           effect = new PotionEffect((PotionEffect)var33.get(0));
                           if(!player.isPotionActive(effect.getPotionID()) && effect.getPotionID() == Potion.regeneration.id) {
                              var26 = true;
                              player.addPotionEffect(effect);
                              player.attackEntityFrom(DamageSource.magic, 1.0F);
                              var25.getTagCompound().removeTag("WITCPotion");
                              if(var25.getTagCompound().hasNoTags()) {
                                 var25.setTagCompound((NBTTagCompound)null);
                              }
                           }
                        }
                     }

                     if(!var26 && var25.getTagCompound().hasKey("WITCPotion2")) {
                        var30 = var25.getTagCompound().getInteger("WITCPotion2");
                        var33 = Items.potionitem.getEffects(var30);
                        if(var33 != null && !var33.isEmpty()) {
                           effect = new PotionEffect((PotionEffect)var33.get(0));
                           if(!player.isPotionActive(effect.getPotionID()) && effect.getPotionID() == Potion.regeneration.id) {
                              player.addPotionEffect(effect);
                              player.attackEntityFrom(DamageSource.magic, 1.0F);
                              var25.getTagCompound().removeTag("WITCPotion2");
                              if(var25.getTagCompound().hasNoTags()) {
                                 var25.setTagCompound((NBTTagCompound)null);
                              }
                           }
                        }
                     }
                  }
               }
            }

            if(healthAfterDamage <= 0.0F && !wolfForm && !playerEx.isVampire()) {
               Log.instance().debug(String.format("player terminal damage", new Object[0]));
               if(!event.source.damageType.equals(DamageSource.fall.damageType) && !event.source.damageType.equals(DamageSource.fallingBlock.damageType)) {
                  if(!event.source.isFireDamage() && !event.source.isExplosion()) {
                     if(event.source.damageType.equals(DamageSource.drown.damageType)) {
                        Witchery.Items.POPPET.cancelEventIfPoppetFound(player, Witchery.Items.POPPET.waterPoppet, event, true);
                        if(event.isCanceled()) {
                           player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 60, 0));
                        }
                     } else if(event.source.damageType.equals(DamageSource.starve.damageType)) {
                        Witchery.Items.POPPET.cancelEventIfPoppetFound(player, Witchery.Items.POPPET.foodPoppet, event, true);
                        if(event.isCanceled()) {
                           player.addPotionEffect(new PotionEffect(Potion.field_76443_y.id, 60, 0));
                        }
                     }
                  } else {
                     Witchery.Items.POPPET.cancelEventIfPoppetFound(player, Witchery.Items.POPPET.firePoppet, event, true);
                     if(event.isCanceled()) {
                        player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 60, 0));
                     }
                  }
               } else {
                  Witchery.Items.POPPET.cancelEventIfPoppetFound(player, Witchery.Items.POPPET.earthPoppet, event, false);
               }

               if(!event.isCanceled()) {
                  Witchery.Items.POPPET.cancelEventIfPoppetFound(player, Witchery.Items.POPPET.deathPoppet, event, true, ignoreProtection);
                  if(event.isCanceled()) {
                     if(!player.isBurning() && !event.source.isFireDamage() && !event.source.isExplosion()) {
                        if(event.source.damageType.equals(DamageSource.drown.damageType)) {
                           player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 120, 0));
                        } else if(event.source.damageType.equals(DamageSource.starve.damageType)) {
                           player.addPotionEffect(new PotionEffect(Potion.field_76443_y.id, 120, 0));
                        }
                     } else {
                        player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 120, 0));
                     }
                  }
               }
            }

            if(!event.isCanceled() && healthAfterDamage <= 2.0F && event.source.damageType.equals(DamageSource.starve.damageType)) {
               Witchery.Items.POPPET.cancelEventIfPoppetFound(player, Witchery.Items.POPPET.foodPoppet, event, true);
               if(event.isCanceled()) {
                  player.addPotionEffect(new PotionEffect(Potion.field_76443_y.id, 60, 0));
               }
            }

            Familiar.handlePlayerHurt(event, player);
            this.checkForWolfInfection(event, healthAfterDamage);
            Witchery.Items.POPPET.checkForArmorProtection(player);
         } else {
            if(event.entityLiving instanceof EntityGoblin && event.source == DamageSource.fall) {
               event.setCanceled(true);
               return;
            }

            if(event.entityLiving instanceof EntityVillager && event.source != null && event.source.getEntity() != null && (event.source.getEntity() instanceof EntityVillageGuard || event.source.getEntity() instanceof EntityWitchHunter)) {
               event.setCanceled(true);
               return;
            }

            if(Config.instance().isReduceZombeVillagerDamageActive() && event.entityLiving instanceof EntityVillager && event.source.getEntity() != null && event.source.getEntity() instanceof EntityZombie) {
               event.ammount = 0.5F;
            }

            this.checkForRendArmor(event);
            this.checkForWolfInfection(event, EntityUtil.getHealthAfterDamage(event, event.entityLiving.getHealth(), event.entityLiving));
         }
      }

   }

   public void checkForRendArmor(LivingHurtEvent event) {
      if(event.source.damageType.equals("player") && event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer) {
         EntityPlayer attackingPlayer = (EntityPlayer)event.source.getEntity();
         ExtendedPlayer playerEx = ExtendedPlayer.get(attackingPlayer);
         Shapeshift.INSTANCE.rendArmor(event.entityLiving, attackingPlayer, playerEx);
      }

   }

   public void checkForWolfInfection(LivingHurtEvent event, float health) {
      if(!event.isCanceled()) {
         if(event.source.damageType.equals("player") && event.source.getSourceOfDamage() != null && event.source.getSourceOfDamage() instanceof EntityPlayer) {
            EntityPlayer attackingPlayer = (EntityPlayer)event.source.getEntity();
            ExtendedPlayer playerEx = ExtendedPlayer.get(attackingPlayer);
            Shapeshift.INSTANCE.processWolfInfection(event.entityLiving, attackingPlayer, playerEx, health);
         } else if(event.source.damageType.equals("mob") && event.source.getSourceOfDamage() instanceof EntityWolfman) {
            Shapeshift.INSTANCE.processWolfInfection(event.entityLiving, (EntityWolfman)event.source.getSourceOfDamage(), health);
         }
      }

   }

   public void checkForChargeDamage(LivingHurtEvent event) {
      if(event.source.damageType.equals("player") && event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer) {
         EntityPlayer attackingPlayer = (EntityPlayer)event.source.getEntity();
         ExtendedPlayer playerEx = ExtendedPlayer.get(attackingPlayer);
         Shapeshift.INSTANCE.updateChargeDamage(event, attackingPlayer, playerEx);
      }

   }

   private static boolean isPotionAggressive(int potionID) {
      return potionID == Potion.digSlowdown.id || potionID == Potion.moveSlowdown.id || potionID == Potion.poison.id || potionID == Potion.wither.id || potionID == Potion.weakness.id || potionID == Potion.hunger.id;
   }

   private static void dropItemsOnHit(EntityPlayer player) {
      for(int i = 0; i < player.inventory.mainInventory.length; ++i) {
         ItemStack stack = player.inventory.mainInventory[i];
         if(Witchery.Items.GENERIC.itemBatBall.isMatch(stack)) {
            player.dropPlayerItemWithRandomChoice(stack, true);
            player.inventory.mainInventory[i] = null;
         }
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public void onLivingDeath(LivingDeathEvent event) {
      EntityPlayer attacker;
      ExtendedPlayer entitySource;
      if(event.entityLiving.worldObj.isRemote && !event.isCanceled()) {
         if(event.entityLiving instanceof EntityPlayer) {
            attacker = (EntityPlayer)event.entityLiving;
            entitySource = ExtendedPlayer.get(attacker);
            if(entitySource.isVampire()) {
               event.setCanceled(true);
               attacker.setHealth(1.0F);
               return;
            }
         }
      } else if(!event.entityLiving.worldObj.isRemote && (!event.isCancelable() || !event.isCanceled())) {
         if(event.entityLiving instanceof EntityPlayer) {
            attacker = (EntityPlayer)event.entityLiving;
            entitySource = ExtendedPlayer.get(attacker);
            if(entitySource.isVampire()) {
               if(attacker.getHealth() > 0.0F) {
                  event.setCanceled(true);
                  return;
               }

               if(!CreatureUtil.checkForVampireDeath(attacker, event.source)) {
                  event.setCanceled(true);
                  return;
               }
            }
         }

         this.dropExtraItemsFromNBT(event);
         Entity attacker1 = event.source.getEntity();
         if(attacker1 != null && attacker1 instanceof EntityPlayer) {
            EntityPlayer entitySource1 = (EntityPlayer)attacker1;
            ExtendedPlayer player = ExtendedPlayer.get(entitySource1);
            if(event.entity instanceof EntityHornedHuntsman && player.getWolfmanQuestState() == ExtendedPlayer.QuestState.STARTED) {
               player.setWolfmanQuestState(ExtendedPlayer.QuestState.COMPLETE);
            }

            if(player.hasVampireBook()) {
               boolean playerEx = event.entityLiving instanceof IBossDisplayData || (event.entityLiving instanceof EntityPigZombie || event.entityLiving instanceof EntityEnderman) && entitySource1.worldObj.rand.nextDouble() < 0.09D || PotionParalysis.isVillager(event.entityLiving) && entitySource1.worldObj.rand.nextDouble() < 0.1D || event.entityLiving.isEntityUndead() && entitySource1.worldObj.rand.nextDouble() < 0.02D;
               if(playerEx) {
                  EntityItem hasArthana = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY + 1.0D, event.entityLiving.posZ, Witchery.Items.GENERIC.itemVampireBookPage.createStack());
                  event.entityLiving.worldObj.spawnEntityInWorld(hasArthana);
               }
            }
         }

         Entity entitySource2 = event.source.getSourceOfDamage();
         if(entitySource2 != null && entitySource2 instanceof EntityPlayer) {
            EntityPlayer player1 = (EntityPlayer)entitySource2;
            ExtendedPlayer playerEx1 = ExtendedPlayer.get(player1);
            boolean hasArthana1 = player1.inventory.getCurrentItem() != null && player1.inventory.getCurrentItem().getItem() == Witchery.Items.ARTHANA;
            boolean var10000;
            if(player1.inventory.getCurrentItem() != null && player1.inventory.getCurrentItem().getItem() == Witchery.Items.CANE_SWORD && Witchery.Items.CANE_SWORD.isDrawn((EntityLivingBase)player1) && playerEx1.isVampire()) {
               var10000 = true;
            } else {
               var10000 = false;
            }

            ItemStack itemstack = null;
            Shapeshift.INSTANCE.processCreatureKilled(event, player1, playerEx1);
            if(playerEx1.getWerewolfLevel() == 5 && Shapeshift.INSTANCE.isWolfAnimalForm(playerEx1) && playerEx1.getWolfmanQuestState() == ExtendedPlayer.QuestState.STARTED) {
               if(event.entity instanceof IMob && !player1.onGround) {
                  playerEx1.increaseWolfmanQuestCounter();
               }
            } else if(playerEx1.getWerewolfLevel() == 8 && playerEx1.getCreatureType() == TransformCreature.WOLFMAN && playerEx1.getWolfmanQuestState() == ExtendedPlayer.QuestState.STARTED) {
               if(event.entity instanceof EntityPigZombie) {
                  playerEx1.increaseWolfmanQuestCounter();
               }
            } else if(playerEx1.getWerewolfLevel() == 9 && Shapeshift.INSTANCE.isWolfAnimalForm(playerEx1) && playerEx1.getWolfmanQuestState() == ExtendedPlayer.QuestState.STARTED && (event.entity instanceof EntityVillager || event.entity instanceof EntityPlayer)) {
               playerEx1.increaseWolfmanQuestCounter();
            }

            if(playerEx1.getVampireLevel() == 5 && playerEx1.canIncreaseVampireLevel() && event.entity instanceof EntityBlaze) {
               if(playerEx1.getVampireQuestCounter() >= 19) {
                  playerEx1.increaseVampireLevel();
               } else {
                  playerEx1.increaseVampireQuestCounter();
               }
            }

            int baseLooting = EnchantmentHelper.getLootingModifier(player1);
            double lootingFactor = 1.0D + (double)baseLooting;
            double halfLooting = 1.0D + (double)(baseLooting / 2);
            if(InfusedBrewEffect.getActiveBrew(player1) == InfusedBrewEffect.Grave) {
               float allowDrops = player1.getMaxHealth();
               if(event.entityLiving instanceof EntityPlayer) {
                  player1.getFoodStats().addStats(20, 0.9F);
                  player1.heal(allowDrops * 0.6F);
               } else if(event.entityLiving instanceof EntityVillager) {
                  player1.getFoodStats().addStats(20, 0.9F);
                  player1.heal(allowDrops * 0.4F);
               } else if(event.entityLiving instanceof EntityAnimal) {
                  player1.getFoodStats().addStats(8, 0.8F);
                  player1.heal(allowDrops * 0.1F);
               }
            }

            Witchery.Items.BLOOD_GOBLET.handleCreatureDeath(player1.worldObj, player1, event.entityLiving);
            boolean allowDrops1 = !EntityUtil.isNoDrops(event.entityLiving);
            if(allowDrops1) {
               if(event.entityLiving instanceof EntityVillager) {
                  ExtendedVillager entityItem = ExtendedVillager.get((EntityVillager)event.entityLiving);
                  playerEx1.fillBloodReserve(entityItem.getBlood());
               } else if(event.entityLiving instanceof EntityVillageGuard) {
                  EntityVillageGuard entityItem1 = (EntityVillageGuard)event.entityLiving;
                  playerEx1.fillBloodReserve(entityItem1.getBlood());
               } else if(event.entityLiving instanceof EntityPlayer) {
                  ExtendedPlayer entityItem5 = ExtendedPlayer.get((EntityPlayer)event.entityLiving);
                  playerEx1.fillBloodReserve(entityItem5.getHumanBlood());
               } else if(event.entityLiving instanceof EntitySkeleton) {
                  EntitySkeleton entityItem4 = (EntitySkeleton)event.entityLiving;
                  if(hasArthana1 && entityItem4.getSkeletonType() == 0 && event.entityLiving.worldObj.rand.nextDouble() <= Math.min(0.05D * lootingFactor, 1.0D)) {
                     itemstack = new ItemStack(Items.skull, 1, 0);
                  } else if(hasArthana1 && event.entityLiving.worldObj.rand.nextDouble() <= Math.min(0.04D * lootingFactor, 1.0D)) {
                     itemstack = Witchery.Items.GENERIC.itemSpectralDust.createStack();
                  }
               } else if(event.entityLiving instanceof EntityZombie) {
                  if(hasArthana1 && event.entityLiving.worldObj.rand.nextDouble() <= Math.min(0.02D * lootingFactor, 1.0D)) {
                     itemstack = new ItemStack(Items.skull, 1, 2);
                  } else if(hasArthana1 && event.entityLiving.worldObj.rand.nextDouble() <= Math.min(0.03D * lootingFactor, 1.0D)) {
                     itemstack = Witchery.Items.GENERIC.itemSpectralDust.createStack();
                  }
               } else if(event.entityLiving instanceof EntityCreeper) {
                  if(hasArthana1 && event.entityLiving.worldObj.rand.nextDouble() <= Math.min(0.01D * lootingFactor, 1.0D)) {
                     itemstack = new ItemStack(Items.skull, 1, 4);
                  } else if(event.entityLiving.worldObj.rand.nextDouble() <= Math.min((hasArthana1?0.08D:0.02D) * lootingFactor, 1.0D)) {
                     itemstack = Witchery.Items.GENERIC.itemCreeperHeart.createStack();
                  }
               } else if(event.entityLiving instanceof EntityDemon) {
                  if(hasArthana1 && event.entityLiving.worldObj.rand.nextDouble() <= Math.min(0.33D * halfLooting, 1.0D)) {
                     itemstack = Witchery.Items.GENERIC.itemDemonHeart.createStack();
                  }
               } else if(event.entityLiving instanceof EntityPlayer) {
                  if(hasArthana1 && event.entityLiving.worldObj.rand.nextDouble() <= Math.min(0.1D * halfLooting, 1.0D)) {
                     EntityPlayer entityItem3 = (EntityPlayer)event.entityLiving;
                     itemstack = new ItemStack(Items.skull, 1, 3);
                     NBTTagCompound name = itemstack.getTagCompound();
                     if(name == null) {
                        name = new NBTTagCompound();
                        itemstack.setTagCompound(name);
                     }

                     name.setString("SkullOwner", entityItem3.getCommandSenderName());
                  }
               } else if(event.entityLiving instanceof EntityBat) {
                  if(player1.worldObj.rand.nextDouble() <= (hasArthana1?(baseLooting > 0?1.0D:0.75D):0.33D)) {
                     itemstack = Witchery.Items.GENERIC.itemBatWool.createStack();
                  }
               } else if(event.entityLiving instanceof EntityWolf) {
                  if(player1.worldObj.rand.nextDouble() <= (hasArthana1?(baseLooting > 0?1.0D:0.75D):0.33D)) {
                     itemstack = Witchery.Items.GENERIC.itemDogTongue.createStack();
                  }

                  if(player1.worldObj.rand.nextInt(12) <= Math.min(baseLooting, 3)) {
                     event.entityLiving.entityDropItem(new ItemStack(Witchery.Blocks.WOLFHEAD, 1, 0), 0.0F);
                  }
               } else if(event.entityLiving instanceof EntityOwl) {
                  if(!((EntityOwl)event.entityLiving).isTemp() && player1.worldObj.rand.nextDouble() <= (hasArthana1?(baseLooting > 0?1.0D:0.5D):0.2D)) {
                     itemstack = Witchery.Items.GENERIC.itemOwletsWing.createStack();
                  }
               } else if(event.entityLiving instanceof EntitySheep) {
                  if(CreatureUtil.isWerewolf(entitySource2, false) && !((EntitySheep)event.entityLiving).isChild() && event.entityLiving.worldObj.rand.nextInt(4) != 0) {
                     itemstack = Witchery.Items.GENERIC.itemMuttonRaw.createStack();
                  }
               } else if(event.entityLiving instanceof EntityToad) {
                  if(!((EntityToad)event.entityLiving).isTemp() && player1.worldObj.rand.nextDouble() <= (hasArthana1?(baseLooting > 0?1.0D:0.5D):0.2D)) {
                     itemstack = Witchery.Items.GENERIC.itemToeOfFrog.createStack();
                  }
               } else {
                  try {
                     Class entityItem2 = event.entityLiving.getClass();
                     if(entityItem2 != null) {
                        String name1 = entityItem2.getSimpleName();
                        if(name1 != null && !name1.isEmpty()) {
                           String upperName = name1.toUpperCase(Locale.ROOT);
                           if(!upperName.contains("WOLF") && !name1.contains("Dog") && !name1.contains("Fox")) {
                              if((upperName.contains("FIREBAT") || name1.contains("Bat")) && player1.worldObj.rand.nextDouble() <= (hasArthana1?(baseLooting > 0?1.0D:0.75D):0.33D)) {
                                 itemstack = Witchery.Items.GENERIC.itemBatWool.createStack();
                              }
                           } else {
                              if(player1.worldObj.rand.nextDouble() <= (hasArthana1?(baseLooting > 0?1.0D:0.75D):0.33D)) {
                                 itemstack = Witchery.Items.GENERIC.itemDogTongue.createStack();
                              }

                              if((upperName.contains("WOLF") || name1.contains("Dog")) && player1.worldObj.rand.nextInt(12) <= Math.min(baseLooting, 3)) {
                                 event.entityLiving.entityDropItem(new ItemStack(Witchery.Blocks.WOLFHEAD, 1, 0), 0.0F);
                              }
                           }
                        }
                     }
                  } catch (Exception var18) {
                     Log.instance().debug(String.format("Exception occurred while determining dead creature type: %s", new Object[]{var18.toString()}));
                  }
               }
            }

            if(itemstack != null) {
               EntityItem entityItem6 = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY + 1.0D, event.entityLiving.posZ, itemstack);
               event.entityLiving.worldObj.spawnEntityInWorld(entityItem6);
            }
         }
      }

   }

   private void dropExtraItemsFromNBT(LivingDeathEvent event) {
      if(!event.entityLiving.worldObj.isRemote) {
         NBTTagCompound nbtEntityData = event.entityLiving.getEntityData();
         if(nbtEntityData.hasKey("WITCExtraDrops")) {
            NBTTagList nbtExtraDrops = nbtEntityData.getTagList("WITCExtraDrops", 10);

            for(int i = 0; i < nbtExtraDrops.tagCount(); ++i) {
               NBTTagCompound nbtTag = nbtExtraDrops.getCompoundTagAt(i);
               ItemStack extraStack = ItemStack.loadItemStackFromNBT(nbtTag);
               if(extraStack != null) {
                  EntityItem entityItem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY + 1.0D, event.entityLiving.posZ, extraStack);
                  event.entityLiving.worldObj.spawnEntityInWorld(entityItem);
               }
            }
         }
      }

   }

   // $FF: synthetic class
   static class NamelessClass2021840805 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower;
      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$util$TransformCreature = new int[TransformCreature.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.WOLF.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.WOLFMAN.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.NONE.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower = new int[ExtendedPlayer.VampirePower.values().length];

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.MESMERIZE.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.SPEED.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.BAT.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.ULTIMATE.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
