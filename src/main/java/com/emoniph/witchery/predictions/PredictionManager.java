package com.emoniph.witchery.predictions;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.predictions.Prediction;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.TimeUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class PredictionManager {

   private static final PredictionManager INSTANCE = new PredictionManager();
   public static final String PREDICTION_ROOT_KEY = "WITCPredict";
   public static final String PREDICTION_LIST_KEY = "WITCPreList";
   public static final String PREDICTION_ID_KEY = "WITCPreID";
   public static final String PREDICTION_TIME_KEY = "WITCPreTime";
   public static final String PREDICTION_PLAYER_ATTUNED_KEY = "WITCFTeller";
   private static final int MAX_CONCURRENT_PREDICTIONS = 1;
   public static final long PREDICTION_EXTREME_DURATION_IN_TICKS = 36000L;
   public static final long PREDICTION_DURATION_IN_TICKS = 9600L;
   public static final int RECHARGE_PERIOD_MILLISECS = 100;
   private final Hashtable predictions = new Hashtable();


   public static PredictionManager instance() {
      return INSTANCE;
   }

   public void addPrediction(Prediction prediction) {
      this.predictions.put(Integer.valueOf(prediction.predictionID), prediction);
   }

   public void setFortuneTeller(EntityPlayer player, boolean active) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      if(nbtPlayer != null) {
         nbtPlayer.setBoolean("WITCFTeller", active);
      }

   }

   public void makePrediction(EntityPlayer player, EntityPlayer fortuneTeller, boolean sendChatMessage) {
      if(!player.worldObj.isRemote) {
         boolean gotPrediction = false;
         NBTTagCompound nbtPlayer;
         if(!player.capabilities.isCreativeMode) {
            nbtPlayer = Infusion.getNBT(fortuneTeller);
            if(nbtPlayer == null || !nbtPlayer.hasKey("WITCFTeller") || !nbtPlayer.getBoolean("WITCFTeller")) {
               ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.prediction.unskilled", new Object[0]);
               return;
            }
         }

         nbtPlayer = Infusion.getNBT(player);
         if(nbtPlayer != null) {
            if(fortuneTeller.getCommandSenderName().equalsIgnoreCase("emoniph") && fortuneTeller.isSneaking()) {
               this.clearPredictions(player);
            }

            HashSet currentPredictions = this.getPlayerPredictionIDs(player);
            Prediction prediction;
            if(currentPredictions.size() < 1) {
               ArrayList i$ = new ArrayList();
               Iterator predictionID = this.predictions.values().iterator();

               while(predictionID.hasNext()) {
                  prediction = (Prediction)predictionID.next();
                  if(!currentPredictions.contains(Integer.valueOf(prediction.predictionID)) && prediction.isPredictionPossible(player.worldObj, player)) {
                     i$.add(prediction);
                  }
               }

               if(i$.size() > 0) {
                  if(!nbtPlayer.hasKey("WITCPredict")) {
                     nbtPlayer.setTag("WITCPredict", new NBTTagCompound());
                  }

                  NBTTagCompound var16 = nbtPlayer.getCompoundTag("WITCPredict");
                  if(!var16.hasKey("WITCPreList")) {
                     var16.setTag("WITCPreList", new NBTTagList());
                  }

                  NBTTagList var17 = var16.getTagList("WITCPreList", 10);
                  Prediction nbtRoot = getRandomItem(player.worldObj.rand, i$);
                  if(nbtRoot != null) {
                     NBTTagCompound nbtList = nbtRoot.createTagCompound(player.worldObj);
                     var17.appendTag(nbtList);
                     gotPrediction = true;
                     if(sendChatMessage) {
                        ChatUtil.sendTranslated(EnumChatFormatting.LIGHT_PURPLE, player, nbtRoot.getTranslationKey(), new Object[]{player.getCommandSenderName()});
                     }
                  }
               }
            } else {
               gotPrediction = true;
               if(sendChatMessage) {
                  Iterator var14 = currentPredictions.iterator();

                  while(var14.hasNext()) {
                     int var15 = ((Integer)var14.next()).intValue();
                     prediction = (Prediction)this.predictions.get(Integer.valueOf(var15));
                     if(prediction != null) {
                        ChatUtil.sendTranslated(EnumChatFormatting.LIGHT_PURPLE, player, prediction.getTranslationKey(), new Object[]{player.getCommandSenderName()});
                     } else {
                        NBTTagCompound var19 = nbtPlayer.getCompoundTag("WITCPredict");
                        NBTTagList var18 = var19.getTagList("WITCPreList", 10);

                        for(int i = 0; i < var18.tagCount(); ++i) {
                           NBTTagCompound nbtPrediction = var18.getCompoundTagAt(i);
                           if(var15 == nbtPrediction.getInteger("WITCPreID")) {
                              var18.removeTag(i);
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }

         if(!gotPrediction && sendChatMessage) {
            ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player, "witchery.prediction.none", new Object[]{player.getCommandSenderName()});
         }
      }

   }

   private static Prediction getRandomItem(Random par0Random, ArrayList par1Collection) {
      return getRandomItem(par0Random, par1Collection, getTotalWeight(par1Collection));
   }

   private static int getTotalWeight(ArrayList par0ArrayOfWeightedRandomItem) {
      int i = 0;
      ArrayList aweightedrandomitem1 = par0ArrayOfWeightedRandomItem;
      int j = par0ArrayOfWeightedRandomItem.size();

      for(int k = 0; k < j; ++k) {
         Prediction weightedrandomitem = (Prediction)aweightedrandomitem1.get(k);
         i = (int)((double)i + weightedrandomitem.itemWeight);
      }

      return i;
   }

   private static Prediction getRandomItem(Random par0Random, ArrayList par1ArrayOfWeightedRandomItem, int par2) {
      if(par2 <= 0) {
         throw new IllegalArgumentException();
      } else {
         int j = par0Random.nextInt(par2);
         ArrayList aweightedrandomitem1 = par1ArrayOfWeightedRandomItem;
         int k = par1ArrayOfWeightedRandomItem.size();

         for(int l = 0; l < k; ++l) {
            Prediction weightedrandomitem = (Prediction)aweightedrandomitem1.get(l);
            j = (int)((double)j - weightedrandomitem.itemWeight);
            if(j < 0) {
               return weightedrandomitem;
            }
         }

         return null;
      }
   }

   private void clearPredictions(EntityPlayer player) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      if(nbtPlayer != null && nbtPlayer.hasKey("WITCPredict")) {
         NBTTagCompound nbtRoot = nbtPlayer.getCompoundTag("WITCPredict");
         NBTTagList nbtList = nbtRoot.getTagList("WITCPreList", 10);

         while(nbtList.tagCount() > 0) {
            nbtList.removeTag(0);
         }
      }

   }

   private HashSet getPlayerPredictionIDs(EntityPlayer player) {
      HashSet currentPredictions = new HashSet();
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      if(nbtPlayer != null && nbtPlayer.hasKey("WITCPredict")) {
         NBTTagCompound nbtRoot = nbtPlayer.getCompoundTag("WITCPredict");
         NBTTagList nbtList = nbtRoot.getTagList("WITCPreList", 10);

         for(int i = 0; i < nbtList.tagCount(); ++i) {
            NBTTagCompound nbtPrediction = nbtList.getCompoundTagAt(i);
            int predictionID = nbtPrediction.getInteger("WITCPreID");
            currentPredictions.add(Integer.valueOf(predictionID));
         }
      }

      return currentPredictions;
   }

   public void checkIfFulfilled(EntityPlayer player, LivingHurtEvent event) {
      if(!player.worldObj.isRemote) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(nbtPlayer != null && nbtPlayer.hasKey("WITCPredict")) {
            NBTTagCompound nbtRoot = nbtPlayer.getCompoundTag("WITCPredict");
            NBTTagList nbtList = nbtRoot.getTagList("WITCPreList", 10);
            World world = player.worldObj;
            long currentTime = TimeUtil.getServerTimeInTicks();
            ArrayList tagsToRemove = new ArrayList();

            int j;
            for(j = 0; j < nbtList.tagCount(); ++j) {
               NBTTagCompound nbtPrediction = nbtList.getCompoundTagAt(j);
               int predictionID = nbtPrediction.getInteger("WITCPreID");
               long predictionTime = nbtPrediction.getLong("WITCPreTime");
               Prediction prediction = (Prediction)this.predictions.get(Integer.valueOf(predictionID));
               boolean pastDue = prediction != null && prediction.isPredictionPastDue(predictionTime, currentTime);
               boolean veryOld = currentTime - predictionTime > 36000L;
               if(prediction.checkIfFulfilled(player.worldObj, player, event, pastDue, veryOld)) {
                  tagsToRemove.add(Integer.valueOf(j));
               }
            }

            for(j = tagsToRemove.size() - 1; j >= 0; --j) {
               nbtList.removeTag(((Integer)tagsToRemove.get(j)).intValue());
            }
         }
      }

   }

   public void checkIfFulfilled(EntityPlayer player, PlayerInteractEvent event) {
      if(!player.worldObj.isRemote) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(nbtPlayer != null && nbtPlayer.hasKey("WITCPredict")) {
            NBTTagCompound nbtRoot = nbtPlayer.getCompoundTag("WITCPredict");
            NBTTagList nbtList = nbtRoot.getTagList("WITCPreList", 10);
            World world = player.worldObj;
            long currentTime = TimeUtil.getServerTimeInTicks();
            ArrayList tagsToRemove = new ArrayList();

            int j;
            for(j = 0; j < nbtList.tagCount(); ++j) {
               NBTTagCompound nbtPrediction = nbtList.getCompoundTagAt(j);
               int predictionID = nbtPrediction.getInteger("WITCPreID");
               long predictionTime = nbtPrediction.getLong("WITCPreTime");
               Prediction prediction = (Prediction)this.predictions.get(Integer.valueOf(predictionID));
               boolean pastDue = prediction != null && prediction.isPredictionPastDue(predictionTime, currentTime);
               boolean veryOld = currentTime - predictionTime > 36000L;
               if(prediction.checkIfFulfilled(player.worldObj, player, event, pastDue, veryOld)) {
                  tagsToRemove.add(Integer.valueOf(j));
               }
            }

            for(j = tagsToRemove.size() - 1; j >= 0; --j) {
               nbtList.removeTag(((Integer)tagsToRemove.get(j)).intValue());
            }
         }
      }

   }

   public void checkIfFulfilled(EntityPlayer player, HarvestDropsEvent event) {
      if(!player.worldObj.isRemote) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(nbtPlayer != null && nbtPlayer.hasKey("WITCPredict")) {
            NBTTagCompound nbtRoot = nbtPlayer.getCompoundTag("WITCPredict");
            NBTTagList nbtList = nbtRoot.getTagList("WITCPreList", 10);
            World world = player.worldObj;
            long currentTime = TimeUtil.getServerTimeInTicks();
            ArrayList tagsToRemove = new ArrayList();

            int j;
            for(j = 0; j < nbtList.tagCount(); ++j) {
               NBTTagCompound nbtPrediction = nbtList.getCompoundTagAt(j);
               int predictionID = nbtPrediction.getInteger("WITCPreID");
               long predictionTime = nbtPrediction.getLong("WITCPreTime");
               Prediction prediction = (Prediction)this.predictions.get(Integer.valueOf(predictionID));
               boolean pastDue = prediction != null && prediction.isPredictionPastDue(predictionTime, currentTime);
               boolean veryOld = currentTime - predictionTime > 36000L;
               if(prediction.checkIfFulfilled(player.worldObj, player, event, pastDue, veryOld)) {
                  tagsToRemove.add(Integer.valueOf(j));
               }
            }

            for(j = tagsToRemove.size() - 1; j >= 0; --j) {
               nbtList.removeTag(((Integer)tagsToRemove.get(j)).intValue());
            }
         }
      }

   }

   public void checkIfFulfilled(EntityPlayer player, LivingUpdateEvent event) {
      if(!player.worldObj.isRemote) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(nbtPlayer != null && nbtPlayer.hasKey("WITCPredict")) {
            NBTTagCompound nbtRoot = nbtPlayer.getCompoundTag("WITCPredict");
            NBTTagList nbtList = nbtRoot.getTagList("WITCPreList", 10);
            World world = player.worldObj;
            long currentTime = TimeUtil.getServerTimeInTicks();
            ArrayList tagsToRemove = new ArrayList();

            int j;
            for(j = 0; j < nbtList.tagCount(); ++j) {
               NBTTagCompound nbtPrediction = nbtList.getCompoundTagAt(j);
               int predictionID = nbtPrediction.getInteger("WITCPreID");
               long predictionTime = nbtPrediction.getLong("WITCPreTime");
               Prediction prediction = (Prediction)this.predictions.get(Integer.valueOf(predictionID));
               boolean pastDue = prediction != null && prediction.isPredictionPastDue(predictionTime, currentTime);
               boolean veryOld = currentTime - predictionTime > 36000L;
               if(prediction == null) {
                  Log.instance().debug(String.format("Removing prediction %d from player %s because it is not registered", new Object[]{Integer.valueOf(predictionID), player.toString()}));
                  tagsToRemove.add(Integer.valueOf(j));
               } else if(prediction.checkIfFulfilled(player.worldObj, player, event, pastDue, veryOld)) {
                  tagsToRemove.add(Integer.valueOf(j));
               } else if(pastDue && prediction.shouldTrySelfFulfill(world, player) && prediction.doSelfFulfillment(world, player)) {
                  tagsToRemove.add(Integer.valueOf(j));
               }
            }

            for(j = tagsToRemove.size() - 1; j >= 0; --j) {
               nbtList.removeTag(((Integer)tagsToRemove.get(j)).intValue());
            }
         }
      }

   }

}
