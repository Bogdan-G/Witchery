package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteRemoveVampirism extends Rite {

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteRemoveVampirism.StepCurseCreature(this));
   }

   private static class StepCurseCreature extends RitualStep {

      private final RiteRemoveVampirism rite;


      public StepCurseCreature(RiteRemoveVampirism rite) {
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
                        if(entity instanceof EntityPlayer) {
                           EntityPlayer player = (EntityPlayer)entity;
                           ExtendedPlayer playerEx = ExtendedPlayer.get(player);
                           if(playerEx.isVampire()) {
                              double MAX_RANGE_SQ = 64.0D;
                              if(player.getDistanceSq(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ) <= 64.0D) {
                                 if(world.rand.nextInt(4) != 0) {
                                    playerEx.setVampireLevel(0);
                                 } else {
                                    cursed = true;
                                 }

                                 complete = true;
                              } else {
                                 ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.toofar", new Object[0]);
                              }
                           } else {
                              ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.wolfcurse.notactive", new Object[0]);
                           }
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
