package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

public class RitePriorIncarnation extends Rite {

   private static final String PRIOR_INV_KEY = "WITCPriIncInv";
   private static final String PRIOR_USR_KEY = "WITCPriIncUsr";
   private static final String PRIOR_LOC_KEY = "WITCPriIncLoc";
   private final int radius;
   private final int aoe;


   public static boolean isRiteAllowed() {
      return Config.instance().allowDeathItemRecoveryRite && !Witchery.isDeathChestModInstalled;
   }

   public RitePriorIncarnation(int radius, int aoe) {
      this.radius = radius;
      this.aoe = aoe;
   }

   public void addSteps(ArrayList steps, int initialStage) {
      steps.add(new RitePriorIncarnation.StepPriorIncarnation(this, initialStage));
   }

   private static class StepPriorIncarnation extends RitualStep {

      private final RitePriorIncarnation rite;
      private int stage = 0;


      public StepPriorIncarnation(RitePriorIncarnation rite, int initialStage) {
         super(false);
         this.rite = rite;
         this.stage = initialStage;
      }

      public int getCurrentStage() {
         return this.stage;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(RitePriorIncarnation.isRiteAllowed() && !world.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            if(this.stage == 0 && ticks % 20L != 0L) {
               return RitualStep.Result.STARTING;
            } else {
               if(!world.isRemote) {
                  int var28 = this.rite.radius;
                  AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)(posX - var28), (double)posY, (double)(posZ - var28), (double)(posX + var28), (double)(posY + 1), (double)(posZ + var28));
                  boolean found = false;
                  Iterator i$ = world.getEntitiesWithinAABB(EntityPlayer.class, bounds).iterator();

                  while(i$.hasNext()) {
                     Object obj = i$.next();
                     EntityPlayer player = (EntityPlayer)obj;
                     if(Coord.distance(player.posX, player.posY, player.posZ, (double)posX, (double)posY, (double)posZ) <= (double)var28) {
                        NBTTagCompound nbt = Infusion.getNBT(player);
                        if(Config.instance().traceRites()) {
                           Log.instance().debug(String.format("Prior invocation for %s", new Object[]{player.getCommandSenderName()}));
                        }

                        if(nbt.hasKey("WITCPriIncInv") && nbt.hasKey("WITCPriIncLocX") && nbt.hasKey("WITCPriIncLocY") && nbt.hasKey("WITCPriIncLocZ")) {
                           NBTTagList tagList = nbt.getTagList("WITCPriIncInv", 10);
                           double x = nbt.getDouble("WITCPriIncLocX");
                           double y = nbt.getDouble("WITCPriIncLocY");
                           double z = nbt.getDouble("WITCPriIncLocZ");
                           double dSq = Coord.distanceSq((double)posX, (double)posY, (double)posZ, x, y, z);
                           if(Config.instance().traceRites()) {
                              Log.instance().debug(String.format("Distance to death %f items %d", new Object[]{Double.valueOf(Math.sqrt(dSq)), Integer.valueOf(tagList.tagCount())}));
                           }

                           if(dSq <= (double)(this.rite.aoe * this.rite.aoe) && tagList.tagCount() > 0) {
                              if(Config.instance().traceRites()) {
                                 Log.instance().debug(String.format("Recovering %d items", new Object[]{Integer.valueOf(tagList.tagCount())}));
                              }

                              for(int skeleton = 0; skeleton < tagList.tagCount(); ++skeleton) {
                                 NBTTagCompound baseTag = tagList.getCompoundTagAt(skeleton);
                                 if(baseTag != null && baseTag instanceof NBTTagCompound) {
                                    NBTTagCompound tag = (NBTTagCompound)baseTag;
                                    ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
                                    if(stack != null) {
                                       if(Config.instance().traceRites()) {
                                          Log.instance().debug(String.format(" - Recovered %s", new Object[]{stack.toString()}));
                                       }

                                       world.spawnEntityInWorld(new EntityItem(world, (double)posX, (double)posY, (double)posZ, stack));
                                    } else {
                                       Log.instance().warning("Prior Incarnation stack is null");
                                    }
                                 } else {
                                    Log.instance().warning("Prior Incarnation item has incorrect NBT type or is null " + baseTag);
                                 }
                              }

                              EntitySkeleton var29 = new EntitySkeleton(world);
                              var29.setLocationAndAngles((double)posX, (double)posY, (double)posZ, 0.0F, 0.0F);
                              var29.setCustomNameTag(player.getCommandSenderName());
                              world.spawnEntityInWorld(var29);
                              nbt.removeTag("WITCPriIncInv");
                              nbt.removeTag("WITCPriIncLocX");
                              nbt.removeTag("WITCPriIncLocY");
                              nbt.removeTag("WITCPriIncLocZ");
                              found = true;
                           }
                        }
                     }
                  }

                  if(found) {
                     ParticleEffect.HUGE_EXPLOSION.send(SoundEffect.RANDOM_FIZZ, world, (double)posX, (double)posY, (double)posZ, 3.0D, 3.0D, 16);
                  } else {
                     ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, world, (double)posX, (double)posY, (double)posZ, 1.0D, 2.0D, 16);
                  }
               }

               return RitualStep.Result.COMPLETED;
            }
         } else {
            EntityPlayer r = ritual.getInitiatingPlayer(world);
            if(r != null) {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, r, "witchery.rite.disabled", new Object[0]);
            }

            return RitualStep.Result.ABORTED_REFUND;
         }
      }
   }

   public static class EventHooks {

      @SubscribeEvent
      public void onItemExpire(ItemExpireEvent event) {
         if(event.entityItem != null && !event.entityItem.worldObj.isRemote && RitePriorIncarnation.isRiteAllowed() && !event.isCanceled()) {
            ItemStack stack = event.entityItem.getEntityItem();
            NBTTagCompound nbtItem = stack.getTagCompound();
            if(nbtItem != null && nbtItem.hasKey("WITCPriIncUsr")) {
               String username = nbtItem.getString("WITCPriIncUsr");
               if(username != null && !username.isEmpty()) {
                  MinecraftServer server = MinecraftServer.getServer();
                  WorldServer[] arr$ = server.worldServers;
                  int len$ = arr$.length;

                  for(int i$ = 0; i$ < len$; ++i$) {
                     WorldServer world = arr$[i$];
                     EntityPlayer player = world.getPlayerEntityByName(username);
                     if(player != null) {
                        if(Config.instance().traceRites()) {
                           Log.instance().debug(String.format("Saving stack %s for player %s", new Object[]{stack.toString(), player.getCommandSenderName()}));
                        }

                        NBTTagCompound nbt = Infusion.getNBT(player);
                        NBTTagList list;
                        if(!nbt.hasKey("WITCPriIncInv")) {
                           list = new NBTTagList();
                           nbt.setTag("WITCPriIncInv", list);
                        }

                        list = nbt.getTagList("WITCPriIncInv", 10);
                        NBTTagCompound tagCompound = new NBTTagCompound();
                        nbtItem.removeTag("WITCPriIncUsr");
                        if(nbtItem.hasNoTags()) {
                           stack.setTagCompound((NBTTagCompound)null);
                        }

                        stack.writeToNBT(tagCompound);
                        list.appendTag(tagCompound);
                        break;
                     }
                  }
               }
            }
         }

      }

      @SubscribeEvent
      public void onEntityItemPickup(EntityItemPickupEvent event) {
         if(!event.item.worldObj.isRemote && RitePriorIncarnation.isRiteAllowed() && !event.isCanceled()) {
            ItemStack stack = event.item.getEntityItem();
            removePriorUserTag(stack);
         }

      }

      public static void removePriorUserTag(ItemStack stack) {
         if(stack != null) {
            NBTTagCompound nbtItem = stack.getTagCompound();
            if(nbtItem != null && nbtItem.hasKey("WITCPriIncUsr")) {
               if(Config.instance().traceRites()) {
                  Log.instance().debug(String.format("removing prio incarnation tag for player %s", new Object[]{nbtItem.getString("WITCPriIncUsr")}));
               }

               nbtItem.removeTag("WITCPriIncUsr");
               if(nbtItem.hasNoTags()) {
                  stack.setTagCompound((NBTTagCompound)null);
               }
            }
         }

      }

      @SubscribeEvent
      public void onPlayerDrops(PlayerDropsEvent event) {
         if(event.entityPlayer != null && !event.entityPlayer.worldObj.isRemote && event.entityPlayer.isPotionActive(Witchery.Potions.KEEP_INVENTORY)) {
            event.setCanceled(true);
         } else {
            if(event.entityPlayer != null && !event.entityPlayer.worldObj.isRemote && RitePriorIncarnation.isRiteAllowed() && !event.isCanceled()) {
               if(event.entityPlayer.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
                  return;
               }

               ArrayList drops = event.drops;
               if(drops != null && drops.size() > 0) {
                  EntityPlayer player = event.entityPlayer;
                  World world = player.worldObj;

                  for(int nbt = 0; nbt < drops.size(); ++nbt) {
                     ItemStack stack = ((EntityItem)drops.get(nbt)).getEntityItem();
                     if(stack != null) {
                        NBTTagCompound nbt1 = stack.getTagCompound();
                        if(nbt1 == null) {
                           nbt1 = new NBTTagCompound();
                           stack.setTagCompound(nbt1);
                        }

                        if(Config.instance().traceRites()) {
                           Log.instance().debug(String.format("Tagging stack %s for player %s", new Object[]{stack.toString(), player.getCommandSenderName()}));
                        }

                        nbt1.setString("WITCPriIncUsr", player.getCommandSenderName());
                     }
                  }

                  NBTTagCompound var8 = Infusion.getNBT(player);
                  if(var8.hasKey("WITCPriIncInv")) {
                     var8.removeTag("WITCPriIncInv");
                  }

                  var8.setDouble("WITCPriIncLocX", player.posX);
                  var8.setDouble("WITCPriIncLocY", player.posY);
                  var8.setDouble("WITCPriIncLocZ", player.posZ);
               }
            }

         }
      }
   }
}
