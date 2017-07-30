package com.emoniph.witchery.infusion;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.dimension.WorldProviderTorment;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityIllusion;
import com.emoniph.witchery.entity.EntityIllusionCreeper;
import com.emoniph.witchery.entity.EntityIllusionSpider;
import com.emoniph.witchery.entity.EntityIllusionZombie;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.entity.EntityVillageGuard;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.entity.ai.EntityAIDigBlocks;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.emoniph.witchery.infusion.PlayerEffects;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.item.ItemHunterClothes;
import com.emoniph.witchery.network.PacketPlayerStyle;
import com.emoniph.witchery.network.PacketPlayerSync;
import com.emoniph.witchery.predictions.PredictionManager;
import com.emoniph.witchery.ritual.rites.RiteProtectionCircleRepulsive;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Dye;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class Infusion {

   public static final Infusion DEFUSED = new Infusion(0);
   public static final String INFUSION_CHARGES_KEY = "witcheryInfusionCharges";
   public static final String INFUSION_ID_KEY = "witcheryInfusionID";
   public static final String INFUSION_MAX_CHARGES_KEY = "witcheryInfusionChargesMax";
   public static final String INFUSION_NEXTSYNC = "WITCResyncLook";
   public static final String INFUSION_GROTESQUE = "witcheryGrotesque";
   public static final String INFUSION_DEPTHS = "witcheryDepths";
   public static final String INFUSION_CURSED = "witcheryCursed";
   public static final String INFUSION_INSANITY = "witcheryInsanity";
   public static final String INFUSION_SINKING = "witcherySinking";
   public static final String INFUSION_OVERHEAT = "witcheryOverheating";
   public static final String INFUSION_NIGHTMARE = "witcheryWakingNightmare";
   public final int infusionID;
   protected static final int DEFAULT_CHARGE_COST = 1;


   public static EntityItem dropEntityItemWithRandomChoice(EntityLivingBase entity, ItemStack par1ItemStack, boolean par2) {
      if(par1ItemStack != null && entity != null) {
         if(par1ItemStack.stackSize == 0) {
            return null;
         } else {
            EntityItem entityitem = new EntityItem(entity.worldObj, entity.posX, entity.posY - 0.30000001192092896D + (double)entity.getEyeHeight(), entity.posZ, par1ItemStack);
            entityitem.delayBeforeCanPickup = 40;
            float f = 0.1F;
            float f1;
            if(par2) {
               f1 = entity.worldObj.rand.nextFloat() * 0.5F;
               float f2 = entity.worldObj.rand.nextFloat() * 3.1415927F * 2.0F;
               entityitem.motionX = (double)(-MathHelper.sin(f2) * f1);
               entityitem.motionZ = (double)(MathHelper.cos(f2) * f1);
               entityitem.motionY = 0.20000000298023224D;
            } else {
               f = 0.3F;
               entityitem.motionX = (double)(-MathHelper.sin(entity.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(entity.rotationPitch / 180.0F * 3.1415927F) * f);
               entityitem.motionZ = (double)(MathHelper.cos(entity.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(entity.rotationPitch / 180.0F * 3.1415927F) * f);
               entityitem.motionY = (double)(-MathHelper.sin(entity.rotationPitch / 180.0F * 3.1415927F) * f + 0.1F);
               f = 0.02F;
               f1 = entity.worldObj.rand.nextFloat() * 3.1415927F * 2.0F;
               f *= entity.worldObj.rand.nextFloat();
               entityitem.motionX += Math.cos((double)f1) * (double)f;
               entityitem.motionY += (double)((entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * 0.1F);
               entityitem.motionZ += Math.sin((double)f1) * (double)f;
            }

            entity.worldObj.spawnEntityInWorld(entityitem);
            return entityitem;
         }
      } else {
         return null;
      }
   }

   public static EntityCreature spawnCreature(World world, Class creatureType, EntityLivingBase victim, int minRange, int maxRange, ParticleEffect effect, SoundEffect effectSound) {
      int x = MathHelper.floor_double(victim.posX);
      int y = MathHelper.floor_double(victim.posY);
      int z = MathHelper.floor_double(victim.posZ);
      return spawnCreature(world, creatureType, x, y, z, victim, minRange, maxRange, effect, effectSound);
   }

   public static EntityCreature spawnCreature(World world, Class creatureType, int x, int y, int z, EntityPlayer victim, int minRange, int maxRange) {
      return spawnCreature(world, creatureType, x, y, z, victim, minRange, maxRange, (ParticleEffect)null, SoundEffect.NONE);
   }

   public static EntityCreature spawnCreature(World world, Class creatureType, int x, int y, int z, EntityLivingBase victim, int minRange, int maxRange, ParticleEffect effect, SoundEffect effectSound) {
      if(!world.isRemote) {
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
               EntityCreature creature = (EntityCreature)ex.newInstance(new Object[]{world});
               if(victim instanceof EntityPlayer) {
                  EntityPlayer player = (EntityPlayer)victim;
                  if(creature instanceof EntityIllusion) {
                     ((EntityIllusion)creature).setVictim(player.getCommandSenderName());
                  } else if(creature instanceof EntityNightmare) {
                     ((EntityNightmare)creature).setVictim(player.getCommandSenderName());
                     creature.setAttackTarget(victim);
                  }
               }

               creature.setLocationAndAngles(0.5D + (double)nx, 0.05D + (double)ny + 1.0D, 0.5D + (double)nz, 0.0F, 0.0F);
               world.spawnEntityInWorld(creature);
               if(effect != null) {
                  effect.send(effectSound, world, 0.5D + (double)nx, 0.05D + (double)ny + 1.0D, 0.5D + (double)nz, 1.0D, (double)creature.height, 16);
               }

               return creature;
            } catch (NoSuchMethodException var20) {
               ;
            } catch (InvocationTargetException var21) {
               ;
            } catch (InstantiationException var22) {
               ;
            } catch (IllegalAccessException var23) {
               ;
            }
         }
      }

      return null;
   }

   public static boolean isOnCooldown(World world, ItemStack stack) {
      if(!world.isRemote) {
         NBTTagCompound nbtTag = stack.getTagCompound();
         if(nbtTag != null && nbtTag.hasKey("WITCCooldown")) {
            long currentTime = MinecraftServer.getSystemTimeMillis();
            if(currentTime < nbtTag.getLong("WITCCooldown")) {
               return true;
            }
         }
      }

      return false;
   }

   public static void setCooldown(World world, ItemStack stack, int milliseconds) {
      if(!world.isRemote) {
         if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
         }

         NBTTagCompound nbtTag = stack.getTagCompound();
         if(nbtTag != null) {
            long currentTime = MinecraftServer.getSystemTimeMillis();
            nbtTag.setLong("WITCCooldown", currentTime + (long)milliseconds);
         }
      }

   }

   public Infusion(int infusionID) {
      this.infusionID = infusionID;
   }

   public void onHurt(World worldObj, EntityPlayer player, LivingHurtEvent event) {}

   public void onFalling(World world, EntityPlayer player, LivingFallEvent event) {}

   public IIcon getPowerBarIcon(EntityPlayer player, int index) {
      return Blocks.planks.getIcon(0, 0);
   }

   protected boolean consumeCharges(World world, EntityPlayer player, int cost, boolean playFailSound) {
      if(player.capabilities.isCreativeMode) {
         return true;
      } else {
         int charges = getCurrentEnergy(player);
         if(charges - cost < 0) {
            world.playSoundAtEntity(player, "note.snare", 0.5F, 0.4F / ((float)Math.random() * 0.4F + 0.8F));
            this.clearInfusion(player);
            return false;
         } else {
            setCurrentEnergy(player, charges - cost);
            return true;
         }
      }
   }

   public void onUpdate(ItemStack itemstack, World world, EntityPlayer player, int par4, boolean par5) {}

   public void onLeftClickEntity(ItemStack itemstack, World world, EntityPlayer player, Entity otherEntity) {
      if(!world.isRemote) {
         world.playSoundAtEntity(player, "note.snare", 0.5F, 0.4F / ((float)Math.random() * 0.4F + 0.8F));
      }

   }

   public int getMaxItemUseDuration(ItemStack itemstack) {
      return 400;
   }

   public void onUsingItemTick(ItemStack itemstack, World world, EntityPlayer player, int countdown) {}

   public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int countdown) {
      if(!world.isRemote) {
         world.playSoundAtEntity(player, "note.snare", 0.5F, 0.4F / ((float)Math.random() * 0.4F + 0.8F));
      }

   }

   public void playSound(World world, EntityPlayer player, String sound) {
      world.playSoundAtEntity(player, sound, 0.5F, 0.4F / ((float)world.rand.nextDouble() * 0.4F + 0.8F));
   }

   public void playFailSound(World world, EntityPlayer player) {
      this.playSound(world, player, "note.snare");
   }

   public static NBTTagCompound getNBT(Entity player) {
      NBTTagCompound entityData = player.getEntityData();
      if(player.worldObj.isRemote) {
         return entityData;
      } else {
         NBTTagCompound persistedData = entityData.getCompoundTag("PlayerPersisted");
         if(!entityData.hasKey("PlayerPersisted")) {
            entityData.setTag("PlayerPersisted", persistedData);
         }

         return persistedData;
      }
   }

   public void infuse(EntityPlayer player, int charges) {
      if(!player.worldObj.isRemote) {
         NBTTagCompound nbt = getNBT(player);
         nbt.setInteger("witcheryInfusionID", this.infusionID);
         nbt.setInteger("witcheryInfusionCharges", charges);
         nbt.setInteger("witcheryInfusionChargesMax", charges);
         CreaturePower.setCreaturePowerID(player, 0, 0);
         syncPlayer(player.worldObj, player);
      }

   }

   private void clearInfusion(EntityPlayer player) {
      if(!player.worldObj.isRemote) {
         NBTTagCompound nbt = getNBT(player);
         nbt.removeTag("witcheryInfusionCharges");
         syncPlayer(player.worldObj, player);
      }

   }

   public static void setCurrentEnergy(EntityPlayer player, int currentEnergy) {
      if(!player.worldObj.isRemote) {
         NBTTagCompound nbt = getNBT(player);
         nbt.setInteger("witcheryInfusionCharges", currentEnergy);
         syncPlayer(player.worldObj, player);
      }

   }

   public static void syncPlayer(World world, EntityPlayer player) {
      if(!world.isRemote) {
         Witchery.packetPipeline.sendTo((IMessage)(new PacketPlayerSync(player)), player);
      }

   }

   public static int getInfusionID(EntityPlayer player) {
      NBTTagCompound nbt = getNBT(player);
      return nbt.hasKey("witcheryInfusionID")?nbt.getInteger("witcheryInfusionID"):0;
   }

   public static int getCurrentEnergy(EntityPlayer player) {
      NBTTagCompound nbt = getNBT(player);
      return nbt.hasKey("witcheryInfusionCharges")?nbt.getInteger("witcheryInfusionCharges"):0;
   }

   public static int getMaxEnergy(EntityPlayer player) {
      NBTTagCompound nbt = getNBT(player);
      return nbt.hasKey("witcheryInfusionChargesMax")?nbt.getInteger("witcheryInfusionChargesMax"):0;
   }

   public static void setEnergy(EntityPlayer player, int infusionID, int currentEnergy, int maxEnergy) {
      if(player.worldObj.isRemote) {
         NBTTagCompound nbt = getNBT(player);
         nbt.setInteger("witcheryInfusionID", infusionID);
         nbt.setInteger("witcheryInfusionCharges", currentEnergy);
         nbt.setInteger("witcheryInfusionChargesMax", maxEnergy);
      }

   }

   public static void setSinkingCurseLevel(EntityPlayer playerEntity, int sinkingLevel) {
      if(playerEntity.worldObj.isRemote) {
         NBTTagCompound nbt = getNBT(playerEntity);
         if(nbt.hasKey("witcherySinking") && sinkingLevel <= 0) {
            nbt.removeTag("witcherySinking");
         }

         nbt.setInteger("witcherySinking", sinkingLevel);
      }

   }

   public static int getSinkingCurseLevel(EntityPlayer player) {
      NBTTagCompound nbtTag = getNBT(player);
      return nbtTag.hasKey("witcherySinking")?nbtTag.getInteger("witcherySinking"):0;
   }

   public static boolean aquireEnergy(World world, EntityPlayer player, int cost, boolean showMessages) {
      NBTTagCompound nbtPlayer = getNBT(player);
      return nbtPlayer != null?aquireEnergy(world, player, nbtPlayer, cost, showMessages):false;
   }

   public static boolean aquireEnergy(World world, EntityPlayer player, NBTTagCompound nbtPlayer, int cost, boolean showMessages) {
      if(nbtPlayer != null && nbtPlayer.hasKey("witcheryInfusionID") && nbtPlayer.hasKey("witcheryInfusionCharges")) {
         if(!player.capabilities.isCreativeMode && nbtPlayer.getInteger("witcheryInfusionCharges") < cost) {
            if(showMessages) {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.nocharges", new Object[0]);
               SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            }

            return false;
         } else {
            if(!player.capabilities.isCreativeMode) {
               setCurrentEnergy(player, nbtPlayer.getInteger("witcheryInfusionCharges") - cost);
            }

            return true;
         }
      } else {
         if(showMessages) {
            ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.infusionrequired", new Object[0]);
            SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
         }

         return false;
      }
   }


   public static class Registry {

      private static final Infusion.Registry INSTANCE = new Infusion.Registry();
      private final ArrayList registry = new ArrayList();


      public static Infusion.Registry instance() {
         return INSTANCE;
      }

      public void add(Infusion infusion) {
         if(infusion.infusionID == this.registry.size() + 1) {
            this.registry.add(infusion);
         } else if(infusion.infusionID > this.registry.size() + 1) {
            for(int existingInfusion = this.registry.size(); existingInfusion < infusion.infusionID; ++existingInfusion) {
               this.registry.add((Object)null);
            }

            this.registry.add(infusion);
         } else {
            Infusion var3 = (Infusion)this.registry.get(infusion.infusionID);
            if(var3 != null) {
               Log.instance().warning(String.format("Creature power %s at id %d is being overwritten by another creature power %s.", new Object[]{var3, Integer.valueOf(infusion.infusionID), infusion}));
            }

            this.registry.set(infusion.infusionID, infusion);
         }

      }

      public Infusion get(EntityPlayer player) {
         int infusionID = Infusion.getInfusionID(player);
         return infusionID > 0?(Infusion)this.registry.get(infusionID - 1):Infusion.DEFUSED;
      }

      public Infusion get(int infusionID) {
         return infusionID > 0?(Infusion)this.registry.get(infusionID - 1):Infusion.DEFUSED;
      }

   }

   public static class EventHooks {

      private boolean isBannedSpiritObject(ItemStack stack) {
         if(stack == null) {
            return false;
         } else {
            Item item = stack.getItem();
            return item == Items.ender_pearl || item == Items.blaze_powder;
         }
      }

      @SubscribeEvent(
         priority = EventPriority.NORMAL
      )
      public void onEnderTeleport(EnderTeleportEvent event) {
         if(!event.isCanceled() && event.entityLiving != null && !event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer && ItemHunterClothes.isFullSetWorn(event.entityLiving, false)) {
            event.setCanceled(true);
         }

      }

      @SubscribeEvent(
         priority = EventPriority.NORMAL
      )
      public void FillBucket(FillBucketEvent event) {
         ItemStack result = this.attemptFill(event.world, event.target);
         if(result != null) {
            event.result = result;
            event.setResult(Result.ALLOW);
         }

      }

      private ItemStack attemptFill(World world, MovingObjectPosition p) {
         Block id = world.getBlock(p.blockX, p.blockY, p.blockZ);
         if(id == Witchery.Blocks.FLOWING_SPIRIT) {
            if(world.getBlockMetadata(p.blockX, p.blockY, p.blockZ) == 0) {
               world.setBlock(p.blockX, p.blockY, p.blockZ, Blocks.air);
               return new ItemStack(Witchery.Items.BUCKET_FLOWINGSPIRIT);
            }
         } else if(id == Witchery.Blocks.HOLLOW_TEARS && world.getBlockMetadata(p.blockX, p.blockY, p.blockZ) == 0) {
            world.setBlock(p.blockX, p.blockY, p.blockZ, Blocks.air);
            return new ItemStack(Witchery.Items.BUCKET_HOLLOWTEARS);
         }

         return null;
      }

      @SubscribeEvent
      public void onLivingDamage(LivingHurtEvent event) {
         if(event.entityLiving != null && event.entityLiving.worldObj != null && !event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer && !event.isCanceled()) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            PredictionManager.instance().checkIfFulfilled(player, event);
         }

      }

      @SubscribeEvent
      public void onServerChat(ServerChatEvent event) {
         if(event.player != null && !event.isCanceled() && !event.player.worldObj.isRemote && event.message != null) {
            Witchery.Items.RUBY_SLIPPERS.trySayTheresNoPlaceLikeHome(event.player, event.message);
         }

      }

      @SubscribeEvent
      public void onHarvestDrops(HarvestDropsEvent event) {
         if(event.harvester != null && event.harvester.worldObj != null && !event.harvester.worldObj.isRemote) {
            PredictionManager.instance().checkIfFulfilled(event.harvester, event);
            PlayerEffects.onHarvestDrops(event.harvester, event);
            EntityAIDigBlocks.onHarvestDrops(event.harvester, event);
         }

         if(!event.world.isRemote && event.world.provider.dimensionId == Config.instance().dimensionDreamID && !event.isCanceled()) {
            Iterator iterator = event.drops.iterator();

            while(iterator.hasNext()) {
               ItemStack stack = (ItemStack)iterator.next();
               if(stack != null && this.isBannedSpiritObject(stack)) {
                  iterator.remove();
               }
            }
         }

      }

      @SubscribeEvent
      public void onPlayerInteract(PlayerInteractEvent event) {
         if(event.entityLiving != null && event.entityLiving.worldObj != null && !event.entityLiving.worldObj.isRemote && event.entityLiving instanceof EntityPlayer && !event.isCanceled()) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            PredictionManager.instance().checkIfFulfilled(player, event);
            PlayerEffects.onInteract(player, event);
         }

      }

      @SubscribeEvent
      public void onLivingUpdate(LivingUpdateEvent event) {
         long counter = event.entityLiving.worldObj.getTotalWorldTime();
         if(event.entityLiving instanceof EntityPlayer) {
            EntityPlayer belt = (EntityPlayer)event.entityLiving;
            if(!event.entityLiving.worldObj.isRemote) {
               long blockID = TimeUtil.getServerTimeInTicks();
               if(counter % 4L == 0L) {
                  NBTTagCompound currentChargeLevel = Infusion.getNBT(belt);
                  this.handleBrewGrotesqueEffect(belt, currentChargeLevel);
                  WorldProviderDreamWorld.updatePlayerEffects(belt.worldObj, belt, currentChargeLevel, blockID, counter);
                  WorldProviderTorment.updatePlayerEffects(belt.worldObj, belt, currentChargeLevel, blockID, counter);
                  if(counter % 20L == 0L) {
                     this.handleSyncEffects(belt, currentChargeLevel);
                     this.handleBrewDepthsEffect(belt, currentChargeLevel);
                     this.handleCurseEffects(belt, currentChargeLevel);
                     this.handleSeepingShoesEffect(belt, currentChargeLevel);
                     InfusedBrewEffect.checkActiveEffects(belt.worldObj, belt, currentChargeLevel, counter % 1200L == 0L, blockID);
                  }

                  if(counter % 100L == 0L && !event.isCanceled()) {
                     PredictionManager.instance().checkIfFulfilled(belt, event);
                     if(Config.instance().allowCovenWitchVisits && currentChargeLevel.hasKey("WITCCoven") && belt.worldObj.rand.nextInt(20) == 0) {
                        ChunkCoordinates coords = belt.getBedLocation(belt.dimension);
                        if(coords != null && coords.getDistanceSquared((int)belt.posX, (int)belt.posY, (int)belt.posZ) < 256.0F) {
                           NBTTagList nbtCovenList = currentChargeLevel.getTagList("WITCCoven", 10);
                           if(nbtCovenList.tagCount() > 0) {
                              EntityCovenWitch.summonCovenMember(belt.worldObj, belt, 90);
                           }
                        }
                     }
                  }
               }

               PlayerEffects.onUpdate(belt, blockID);
               if(counter % 100L == 1L) {
                  EntityWitchHunter.handleWitchHunterEffects(belt, blockID);
               }
            }

            this.handleIcySlippersEffect(belt);
            this.handleFamiliarFollowerSync(belt);
         } else if(!event.entityLiving.worldObj.isRemote && counter % 20L == 0L) {
            this.handleCurseEffects(event.entityLiving, event.entityLiving.getEntityData());
         }

         if(counter % 100L == 0L) {
            ItemStack belt1 = event.entityLiving.getEquipmentInSlot(2);
            if(belt1 != null && belt1.getItem() == Witchery.Items.BARK_BELT) {
               Block blockID1 = event.entityLiving.worldObj.getBlock(MathHelper.floor_double(event.entityLiving.posX), MathHelper.floor_double(event.entityLiving.posY) - 1, MathHelper.floor_double(event.entityLiving.posZ));
               if(blockID1 == Blocks.grass || blockID1 == Blocks.mycelium) {
                  int maxChargeLevel = Witchery.Items.BARK_BELT.getMaxChargeLevel(event.entityLiving);
                  int currentChargeLevel1 = Witchery.Items.BARK_BELT.getChargeLevel(belt1);
                  if(currentChargeLevel1 < maxChargeLevel) {
                     Witchery.Items.BARK_BELT.setChargeLevel(belt1, Math.min(currentChargeLevel1 + 1, maxChargeLevel));
                     event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving, "witchery:random.wood_creak", 0.5F, (float)(0.8D + 2.0D * event.entityLiving.worldObj.rand.nextGaussian()));
                  }
               }
            }
         }

      }

      private void handleSeepingShoesEffect(EntityPlayer player, NBTTagCompound nbtTag) {
         if(player.onGround) {
            if(player.isPotionActive(Potion.poison) || player.isPotionActive(Potion.wither)) {
               ItemStack shoes = player.getEquipmentInSlot(1);
               if(shoes != null && shoes.getItem() == Witchery.Items.SEEPING_SHOES) {
                  boolean poisonRemoved = false;
                  if(player.isPotionActive(Potion.poison)) {
                     player.removePotionEffect(Potion.poison.id);
                     poisonRemoved = true;
                  }

                  if(player.isPotionActive(Potion.wither)) {
                     player.removePotionEffect(Potion.wither.id);
                     poisonRemoved = true;
                  }

                  if(poisonRemoved) {
                     int x = MathHelper.floor_double(player.posX);
                     int z = MathHelper.floor_double(player.posZ);
                     int y = MathHelper.floor_double(player.posY);
                     boolean RADIUS = true;
                     boolean RADIUS_SQ = true;

                     for(int dx = x - 3; dx <= x + 3; ++dx) {
                        for(int dz = z - 3; dz <= z + 3; ++dz) {
                           for(int dy = y - 1; dy <= y + 1; ++dy) {
                              if(Coord.distanceSq((double)dx, 1.0D, (double)dy, (double)x, 1.0D, (double)dy) <= 9.0D && player.worldObj.isAirBlock(dx, dy + 1, dz) && !player.worldObj.isAirBlock(dx, dy, dz)) {
                                 ItemDye.applyBonemeal(Dye.BONE_MEAL.createStack(), player.worldObj, dx, dy, dz, player);
                              }
                           }
                        }
                     }
                  }

               }
            }
         }
      }

      private void handleSyncEffects(EntityPlayer player, NBTTagCompound nbtPlayer) {
         if(!player.worldObj.isRemote && nbtPlayer.hasKey("WITCResyncLook")) {
            long nextSync = nbtPlayer.getLong("WITCResyncLook");
            if(nextSync <= MinecraftServer.getSystemTimeMillis()) {
               nbtPlayer.removeTag("WITCResyncLook");
               Witchery.packetPipeline.sendToDimension(new PacketPlayerStyle(player), player.dimension);
            }
         }

      }

      private void handleFamiliarFollowerSync(EntityPlayer player) {
         if(!player.worldObj.isRemote) {
            NBTTagCompound compound = player.getEntityData();
            NBTTagCompound pos;
            if(compound.hasKey("WITC_LASTPOS")) {
               pos = compound.getCompoundTag("WITC_LASTPOS");
               int lastDimension = pos.getInteger("D");
               if(lastDimension != player.dimension || Math.abs(pos.getDouble("X") - player.posX) > 32.0D || Math.abs(pos.getDouble("Z") - player.posZ) > 32.0D) {
                  if(lastDimension != player.dimension && player.dimension == -1 || lastDimension == -1) {
                     NBTTagCompound familiar = Infusion.getNBT(player);
                     familiar.setBoolean("WITCVisitedNether", true);
                  }

                  if(Familiar.hasActiveFamiliar(player)) {
                     EntityTameable var13 = Familiar.getFamiliarEntity(player);
                     if(var13 != null && !var13.isSitting()) {
                        int ipx = MathHelper.floor_double(player.posX) - 2;
                        int j = MathHelper.floor_double(player.posZ) - 2;
                        int k = MathHelper.floor_double(player.boundingBox.minY) - 2;
                        boolean done = false;

                        for(int l = 0; l <= 4 && !done; ++l) {
                           for(int i1 = 0; i1 <= 4 && !done; ++i1) {
                              for(int dy = 0; dy <= 4 && !done; ++dy) {
                                 if(player.worldObj.getBlock(ipx + l, k + dy - 1, j + i1).isSideSolid(player.worldObj, ipx + l, k + dy - 1, j + i1, ForgeDirection.UP) && !player.worldObj.getBlock(ipx + l, k + dy, j + i1).isNormalCube() && !player.worldObj.getBlock(ipx + l, k + dy + 1, j + i1).isNormalCube()) {
                                    ItemGeneral var10000 = Witchery.Items.GENERIC;
                                    ItemGeneral.teleportToLocation(player.worldObj, 0.5D + (double)ipx + (double)l, (double)(k + dy), 0.5D + (double)j + (double)i1, player.dimension, var13, true);
                                    done = true;
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               pos.setDouble("X", player.posX);
               pos.setDouble("Z", player.posZ);
               pos.setInteger("D", player.dimension);
            } else {
               pos = new NBTTagCompound();
               pos.setDouble("X", player.posX);
               pos.setDouble("Z", player.posZ);
               pos.setInteger("D", player.dimension);
               pos.setBoolean("visitedNether", player.dimension == -1);
            }
         }

      }

      private void handleIcySlippersEffect(EntityPlayer player) {
         ItemStack shoes = player.getCurrentArmor(0);
         if(shoes != null && shoes.getItem() == Witchery.Items.ICY_SLIPPERS) {
            int k = MathHelper.floor_double(player.posY - 1.0D);

            for(int i = 0; i < 4; ++i) {
               int j = MathHelper.floor_double(player.posX + (double)((float)(i % 2 * 2 - 1) * 0.5F));
               int l = MathHelper.floor_double(player.posZ + (double)((float)(i / 2 % 2 * 2 - 1) * 0.5F));
               Block blockID = player.worldObj.getBlock(j, k, l);
               if(blockID != Blocks.flowing_water && blockID != Blocks.water) {
                  if(blockID == Blocks.flowing_lava || blockID == Blocks.lava) {
                     player.worldObj.setBlock(j, k, l, Blocks.obsidian);
                     if(player.worldObj.rand.nextInt(10) == 0) {
                        shoes.damageItem(1, player);
                     }
                  }
               } else {
                  player.worldObj.setBlock(j, k, l, Blocks.ice);
               }
            }
         }

      }

      private void handleBrewDepthsEffect(EntityPlayer player, NBTTagCompound nbtTag) {
         if(nbtTag.hasKey("witcheryDepths")) {
            int timeLeft = nbtTag.getInteger("witcheryDepths");
            if(timeLeft > 0) {
               if(!player.isPotionActive(Potion.waterBreathing)) {
                  player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 6000));
               }

               if(!player.isInsideOfMaterial(Material.water)) {
                  if(!player.isPotionActive(Potion.wither)) {
                     player.addPotionEffect(new PotionEffect(Potion.wither.id, 100, 1));
                  }
               } else if(player.isPotionActive(Potion.wither)) {
                  player.removePotionEffect(Potion.wither.id);
               }
            }

            --timeLeft;
            if(timeLeft <= 0) {
               nbtTag.removeTag("witcheryDepths");
               if(player.isPotionActive(Potion.waterBreathing)) {
                  player.removePotionEffect(Potion.waterBreathing.id);
               }

               if(player.isPotionActive(Potion.poison)) {
                  player.removePotionEffect(Potion.poison.id);
               }
            } else {
               nbtTag.setInteger("witcheryDepths", timeLeft);
            }
         }

      }

      private void handleBrewGrotesqueEffect(EntityPlayer player, NBTTagCompound nbtTag) {
         if(nbtTag.hasKey("witcheryGrotesque")) {
            int timeLeft = nbtTag.getInteger("witcheryGrotesque");
            if(timeLeft > 0) {
               float radius = 4.0F;
               AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(player.posX - 4.0D, player.posY - 4.0D, player.posZ - 4.0D, player.posX + 4.0D, player.posY + 4.0D, player.posZ + 4.0D);
               List list = player.worldObj.getEntitiesWithinAABB(EntityLiving.class, bounds);
               Iterator iterator = list.iterator();

               while(iterator.hasNext()) {
                  EntityLiving entity = (EntityLiving)iterator.next();
                  boolean victim = !(entity instanceof EntityDemon) && !(entity instanceof IBossDisplayData) && !(entity instanceof EntityGolem) && !(entity instanceof EntityWitch);
                  if(victim && Coord.distance(entity.posX, entity.posY, entity.posZ, player.posX, player.posY, player.posZ) < 4.0D) {
                     RiteProtectionCircleRepulsive.push(player.worldObj, entity, player.posX, player.posY, player.posZ);
                  }
               }
            }

            --timeLeft;
            if(timeLeft <= 0) {
               nbtTag.removeTag("witcheryGrotesque");
               Witchery.packetPipeline.sendToDimension(new PacketPlayerStyle(player), player.dimension);
            } else {
               nbtTag.setInteger("witcheryGrotesque", timeLeft);
            }
         }

      }

      private void handleCurseEffects(EntityLivingBase entity, NBTTagCompound nbtTag) {
         if(entity != null && nbtTag != null) {
            int level;
            if(!(entity instanceof EntityPlayer) && nbtTag.hasKey("witcherySinking")) {
               level = nbtTag.getInteger("witcherySinking");
               if(level > 0) {
                  if(entity.isInWater() || entity instanceof EntityPlayer && !entity.onGround) {
                     if(entity.motionY < 0.0D) {
                        entity.motionY *= 1.0D + Math.min(0.1D * (double)level, 0.4D);
                     } else if(entity.motionY > 0.0D) {
                        entity.motionY *= 1.0D - Math.min(0.1D * (double)level, 0.4D);
                     }
                  }
               } else {
                  nbtTag.removeTag("witcherySinking");
               }
            }

            int x;
            if(nbtTag.hasKey("witcheryCursed")) {
               level = nbtTag.getInteger("witcheryCursed");
               if(level > 0) {
                  if(!entity.isPotionActive(Potion.blindness.id) && !entity.isPotionActive(Potion.weakness.id) && !entity.isPotionActive(Potion.digSlowdown.id) && !entity.isPotionActive(Potion.moveSlowdown.id) && !entity.isPotionActive(Potion.poison.id) && entity.worldObj.rand.nextInt(20) == 0) {
                     switch(entity.worldObj.rand.nextInt(level >= 5?6:(level >= 4?5:(level >= 3?4:(level >= 2?3:2))))) {
                     case 0:
                        entity.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 600, Math.min(level - 1, 4)));
                        break;
                     case 1:
                        entity.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 600, Math.min(level - 1, 4)));
                        break;
                     case 2:
                        entity.addPotionEffect(new PotionEffect(Potion.weakness.id, (13 + 2 * level) * 20, Math.min(level - 2, 4)));
                        break;
                     case 3:
                        entity.addPotionEffect(new PotionEffect(Potion.blindness.id, 5 * level * 20));
                        if(level > 5) {
                           entity.addPotionEffect(new PotionEffect(Potion.nightVision.id, 5 * level * 20));
                        }
                     case 4:
                     default:
                        break;
                     case 5:
                        if(entity instanceof EntityPlayer) {
                           EntityPlayer world = (EntityPlayer)entity;
                           x = world.inventory.currentItem;
                           if(world.inventory.mainInventory[x] != null) {
                              world.dropPlayerItemWithRandomChoice(world.inventory.mainInventory[x], true);
                              world.inventory.mainInventory[x] = null;
                           }
                        } else {
                           ItemStack world1 = entity.getHeldItem();
                           if(world1 != null) {
                              Infusion.dropEntityItemWithRandomChoice(entity, world1, true);
                              entity.setCurrentItemOrArmor(0, (ItemStack)null);
                           }
                        }
                     }
                  }
               } else {
                  nbtTag.removeTag("witcheryCursed");
               }
            }

            int y;
            World world2;
            if(nbtTag.hasKey("witcheryOverheating")) {
               level = nbtTag.getInteger("witcheryOverheating");
               if(level > 0) {
                  world2 = entity.worldObj;
                  if(!entity.isBurning() && world2.rand.nextInt(level > 2?20:(level > 1?25:30)) == 0) {
                     x = MathHelper.floor_double(entity.posX);
                     y = MathHelper.floor_double(entity.posZ);
                     BiomeGenBase z = world2.getBiomeGenForCoords(x, y);
                     if((double)z.temperature >= 1.5D && (!z.canSpawnLightningBolt() || !world2.isRaining()) && !entity.isInWater()) {
                        entity.setFire(Math.min(world2.rand.nextInt(level < 4?2:level - 1) + 1, 4));
                     }
                  }
               } else {
                  nbtTag.removeTag("witcheryOverheating");
               }
            }

            if(nbtTag.hasKey("witcheryWakingNightmare") && entity instanceof EntityPlayer) {
               EntityPlayer level1 = (EntityPlayer)entity;
               int world3 = nbtTag.getInteger("witcheryWakingNightmare");
               if(world3 > 0 && level1.dimension != Config.instance().dimensionDreamID) {
                  World x1 = level1.worldObj;
                  if(x1.rand.nextInt(world3 > 4?30:(world3 > 2?60:180)) == 0) {
                     double y1 = 16.0D;
                     double sound = 8.0D;
                     AxisAlignedBB MIN_DISTANCE = AxisAlignedBB.getBoundingBox(entity.posX - 16.0D, entity.posY - 8.0D, entity.posZ - 16.0D, entity.posX + 16.0D, entity.posY + 8.0D, entity.posZ + 16.0D);
                     List entities = x1.getEntitiesWithinAABB(EntityNightmare.class, MIN_DISTANCE);
                     boolean doNothing = false;
                     Iterator i$ = entities.iterator();

                     while(i$.hasNext()) {
                        Object obj = i$.next();
                        EntityNightmare nightmare = (EntityNightmare)obj;
                        if(nightmare.getVictimName().equalsIgnoreCase(level1.getCommandSenderName())) {
                           doNothing = true;
                           break;
                        }
                     }

                     if(!doNothing) {
                        Infusion.spawnCreature(x1, EntityNightmare.class, MathHelper.floor_double(level1.posX), MathHelper.floor_double(level1.posY), MathHelper.floor_double(level1.posZ), level1, 2, 6);
                     }
                  }
               } else {
                  nbtTag.removeTag("witcheryWakingNightmare");
               }
            }

            if(entity instanceof EntityPlayer && nbtTag.hasKey("witcheryInsanity")) {
               level = nbtTag.getInteger("witcheryInsanity");
               if(level > 0) {
                  world2 = entity.worldObj;
                  x = MathHelper.floor_double(entity.posX);
                  y = MathHelper.floor_double(entity.posY);
                  int z1 = MathHelper.floor_double(entity.posZ);
                  if(world2.rand.nextInt(level > 2?25:(level > 1?30:35)) == 0) {
                     Class sound1 = null;
                     switch(world2.rand.nextInt(3)) {
                     case 0:
                     default:
                        sound1 = EntityIllusionCreeper.class;
                        break;
                     case 1:
                        sound1 = EntityIllusionSpider.class;
                        break;
                     case 2:
                        sound1 = EntityIllusionZombie.class;
                     }

                     boolean MAX_DISTANCE = true;
                     boolean MIN_DISTANCE1 = true;
                     Infusion.spawnCreature(world2, sound1, x, y, z1, (EntityPlayer)entity, 4, 9);
                  } else if(level >= 4 && world2.rand.nextInt(20) == 0) {
                     SoundEffect sound2 = SoundEffect.NONE;
                     switch(world2.rand.nextInt(3)) {
                     case 0:
                     case 2:
                     case 3:
                     default:
                        sound2 = SoundEffect.RANDOM_EXPLODE;
                        break;
                     case 1:
                        sound2 = SoundEffect.MOB_ENDERMAN_IDLE;
                     }

                     sound2.playOnlyTo((EntityPlayer)entity, 1.0F, 1.0F);
                  }
               } else {
                  nbtTag.removeTag("witcheryInsanity");
               }
            }
         }

      }

      @SubscribeEvent(
         priority = EventPriority.HIGH
      )
      public void onLivingDeath(LivingDeathEvent event) {
         if(!event.entityLiving.worldObj.isRemote && !event.isCanceled()) {
            if(event.entityLiving instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)event.entity;
               World world = player.worldObj;
               NBTTagCompound nbtTag = Infusion.getNBT(player);
               if(nbtTag.hasKey("witcheryDepths")) {
                  nbtTag.removeTag("witcheryDepths");
               }

               PlayerEffects.onDeath(player);
            }

            Familiar.handleLivingDeath(event);
         }

      }

      @SubscribeEvent
      public void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) {
         if(event.target != null && event.entityLiving instanceof EntityLiving) {
            EntityLiving aggressorEntity = (EntityLiving)event.entityLiving;
            if(event.target instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)event.target;
               if(player.isInvisible()) {
                  if(aggressorEntity.worldObj.getClosestVulnerablePlayer(aggressorEntity.posX, aggressorEntity.posY, aggressorEntity.posZ, 16.0D) != event.target) {
                     aggressorEntity.setAttackTarget((EntityLivingBase)null);
                  }
               } else if(aggressorEntity.isPotionActive(Potion.blindness)) {
                  aggressorEntity.setAttackTarget((EntityLivingBase)null);
               } else {
                  ItemStack stack;
                  if(aggressorEntity instanceof EntityCreeper) {
                     stack = player.inventory.armorItemInSlot(2);
                     if(stack != null && stack.getItem() == Witchery.Items.WITCH_ROBES) {
                        aggressorEntity.setAttackTarget((EntityLivingBase)null);
                     }
                  } else if(aggressorEntity.isEntityUndead()) {
                     if(aggressorEntity instanceof EntityZombie && ExtendedPlayer.get(player).getVampireLevel() >= 10) {
                        aggressorEntity.setAttackTarget((EntityLivingBase)null);
                     } else {
                        stack = player.inventory.armorItemInSlot(2);
                        if(stack != null && stack.getItem() == Witchery.Items.NECROMANCERS_ROBES) {
                           aggressorEntity.setAttackTarget((EntityLivingBase)null);
                        }
                     }
                  }
               }
            }

            if(event.target instanceof EntityVillageGuard && event.entityLiving instanceof EntityGolem) {
               aggressorEntity.setAttackTarget((EntityLivingBase)null);
            } else if(Config.instance().isZombeIgnoreVillagerActive() && event.target instanceof EntityVillager && event.entityLiving instanceof EntityZombie) {
               aggressorEntity.setAttackTarget((EntityLivingBase)null);
            }
         }

      }

      @SubscribeEvent
      public void onLivingFall(LivingFallEvent event) {
         if(event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            Infusion.Registry.INSTANCE.get(player).onFalling(player.worldObj, player, event);
         }

      }

      @SubscribeEvent
      public void onLivingHurt(LivingHurtEvent event) {
         if(event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            if(event.source.isFireDamage() && event.isCancelable() && !event.isCanceled() && player.getCurrentArmor(2) != null && player.getCurrentArmor(2).getItem() == Witchery.Items.DEATH_ROBE) {
               if(!player.isPotionActive(Potion.fireResistance.id)) {
                  player.addPotionEffect(new PotionEffect(Potion.fireResistance.id, 100, 0));
               }

               event.setCanceled(true);
            }

            if(!event.isCanceled()) {
               Infusion.Registry.INSTANCE.get(player).onHurt(player.worldObj, player, event);
            }
         }

      }
   }
}
