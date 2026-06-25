package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockAreaMarker;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityVillagerWere;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.entity.EntityWolfman;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.item.ItemHunterClothes;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteCurseOfTheWolf extends Rite {

   private final boolean curse;


   public RiteCurseOfTheWolf(boolean curse) {
      this.curse = curse;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteCurseOfTheWolf.StepCurseCreature(this));
   }

   private static class StepCurseCreature extends RitualStep {

      private final RiteCurseOfTheWolf rite;


      public StepCurseCreature(RiteCurseOfTheWolf rite) {
         super(false);
         this.rite = rite;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               boolean complete = false;
               boolean cursed = false;
               EntityPlayer curseMasterPlayer = ritual.getInitiatingPlayer(world);
               if(!CreatureUtil.isFullMoon(world)) {
                  ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.requiresfullmoon", new Object[0]);
                  return RitualStep.Result.ABORTED_REFUND;
               }

               if(!Familiar.hasActiveCurseMasteryFamiliar(curseMasterPlayer)) {
                  ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.requirescat", new Object[0]);
                  return RitualStep.Result.ABORTED_REFUND;
               }

               if(ritual.covenSize < 6) {
                  ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.requiresfullcoven", new Object[0]);
                  return RitualStep.Result.ABORTED_REFUND;
               }

               Iterator i$ = ritual.sacrificedItems.iterator();

               while(i$.hasNext()) {
                  RitualStep.SacrificedItem item = (RitualStep.SacrificedItem)i$.next();
                  if(item.itemstack.getItem() == Witchery.Items.TAGLOCK_KIT && item.itemstack.getItemDamage() == 1) {
                     EntityLivingBase entity = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, item.itemstack, Integer.valueOf(1));
                     if(entity != null) {
                        if(this.rite.curse) {
                           EntityWitchHunter.blackMagicPerformed(curseMasterPlayer);
                           boolean villager = ItemHunterClothes.isCurseProtectionActive(entity);
                           if(!villager) {
                              villager = BlockAreaMarker.AreaMarkerRegistry.instance().isProtectionActive(entity, this.rite);
                           }

                           if(!villager && !Witchery.Items.POPPET.voodooProtectionActivated(curseMasterPlayer, (ItemStack)null, entity, 3)) {
                              if(entity instanceof EntityPlayer) {
                                 EntityPlayer playerEx = (EntityPlayer)entity;
                                 ExtendedPlayer MAX_RANGE_SQ = ExtendedPlayer.get(playerEx);
                                 if(!Config.instance().allowVampireWolfHybrids && MAX_RANGE_SQ.isVampire()) {
                                    ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.hybridsnotallow", new Object[0]);
                                 } else if(MAX_RANGE_SQ.getWerewolfLevel() == 0) {
                                    MAX_RANGE_SQ.setWerewolfLevel(1);
                                    ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, playerEx, "witchery.werewolf.infection", new Object[0]);
                                    complete = true;
                                    cursed = true;
                                 } else {
                                    ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.alreadyactive", new Object[0]);
                                 }
                              } else if(entity instanceof EntityVillager && !(entity instanceof EntityVillagerWere)) {
                                 EntityVillager playerEx1 = (EntityVillager)entity;
                                 EntityWolfman.convertToCuredVillager(playerEx1, playerEx1.getProfession(), playerEx1.wealth, playerEx1.buyingList);
                                 complete = true;
                                 cursed = true;
                              } else {
                                 ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.nothuman", new Object[0]);
                              }
                           }

                           if(villager && curseMasterPlayer != null) {
                              ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.blackmagicdampening", new Object[0]);
                           }
                        } else if(entity instanceof EntityPlayer) {
                           EntityPlayer villager1 = (EntityPlayer)entity;
                           ExtendedPlayer playerEx2 = ExtendedPlayer.get(villager1);
                           if(playerEx2.getWerewolfLevel() > 0) {
                              double MAX_RANGE_SQ1 = 64.0D;
                              if(playerEx2.getWerewolfLevel() != 1 && villager1.getDistanceSq(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ) > 64.0D) {
                                 ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.toofar", new Object[0]);
                              } else {
                                 if(world.rand.nextInt(4) != 0) {
                                    playerEx2.setWerewolfLevel(0);
                                 } else {
                                    cursed = true;
                                 }

                                 complete = true;
                              }
                           } else {
                              ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.notactive", new Object[0]);
                           }
                        } else if(entity instanceof EntityVillagerWere) {
                           EntityVillagerWere villager2 = (EntityVillagerWere)entity;
                           EntityWolfman.convertToCuredVillager(villager2, villager2.getProfession(), villager2.wealth, villager2.buyingList);
                           complete = true;
                        } else if(entity instanceof EntityWolfman) {
                           EntityWolfman villager3 = (EntityWolfman)entity;
                           EntityWolfman.convertToCuredVillager(villager3, villager3.getFormerProfession(), villager3.getWealth(), villager3.getBuyingList());
                           complete = true;
                        } else {
                           ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.notactive", new Object[0]);
                        }
                     }
                     break;
                  }
               }

               if(!complete) {
                  return RitualStep.Result.ABORTED_REFUND;
               }

               if(cursed) {
                  ParticleEffect.FLAME.send(SoundEffect.MOB_ENDERDRAGON_GROWL, world, 0.5D + (double)posX, 0.1D + (double)posY, 0.5D + (double)posZ, 1.0D, 2.0D, 16);
               } else {
                  ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_LEVELUP, world, 0.5D + (double)posX, 0.1D + (double)posY, 0.5D + (double)posZ, 1.0D, 2.0D, 16);
               }
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
