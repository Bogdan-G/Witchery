package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockAreaMarker;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemHunterClothes;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteCurseCreature extends Rite {

   private final boolean curse;
   private final int level;
   private final String curseType;


   public RiteCurseCreature(boolean curse, String curseType, int level) {
      this.curse = curse;
      this.level = level;
      this.curseType = curseType;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteCurseCreature.StepCurseCreature(this));
   }

   private static class StepCurseCreature extends RitualStep {

      private final RiteCurseCreature rite;
      private static final int CURSE_MASTER_BONUS_LEVELS = 1;


      public StepCurseCreature(RiteCurseCreature rite) {
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
               int levelBuff = curseMasterPlayer != null && Familiar.hasActiveCurseMasteryFamiliar(curseMasterPlayer)?1:0;
               if(ritual.covenSize == 6) {
                  levelBuff += 2;
               } else if(ritual.covenSize >= 3) {
                  ++levelBuff;
               }

               Iterator i$ = ritual.sacrificedItems.iterator();

               while(i$.hasNext()) {
                  RitualStep.SacrificedItem item = (RitualStep.SacrificedItem)i$.next();
                  if(item.itemstack.getItem() == Witchery.Items.TAGLOCK_KIT && item.itemstack.getItemDamage() == 1) {
                     EntityLivingBase entity = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, item.itemstack, Integer.valueOf(1));
                     if(entity != null) {
                        NBTTagCompound nbtTag = entity instanceof EntityPlayer?Infusion.getNBT(entity):entity.getEntityData();
                        if(nbtTag != null) {
                           int currentLevel = nbtTag.hasKey(this.rite.curseType)?nbtTag.getInteger(this.rite.curseType):0;
                           if(this.rite.curse) {
                              EntityWitchHunter.blackMagicPerformed(curseMasterPlayer);
                              boolean newLevel = ItemHunterClothes.isCurseProtectionActive(entity) && (this.rite.curseType.equals("witcheryCursed") || this.rite.curseType.equals("witcheryWakingNightmare"));
                              if(!newLevel) {
                                 newLevel = BlockAreaMarker.AreaMarkerRegistry.instance().isProtectionActive(entity, this.rite);
                              }

                              if(!newLevel && !Witchery.Items.POPPET.voodooProtectionActivated(curseMasterPlayer, (ItemStack)null, entity, levelBuff > 0?3:1)) {
                                 nbtTag.setInteger(this.rite.curseType, Math.max(this.rite.level + levelBuff, currentLevel));
                                 cursed = true;
                                 if(entity instanceof EntityPlayer) {
                                    Infusion.syncPlayer(entity.worldObj, (EntityPlayer)entity);
                                 }
                              }

                              if(newLevel) {
                                 if(curseMasterPlayer != null) {
                                    ChatUtil.sendTranslated(EnumChatFormatting.RED, curseMasterPlayer, "witchery.rite.blackmagicdampening", new Object[0]);
                                 }
                              } else {
                                 complete = true;
                              }
                           } else {
                              int var18 = 0;
                              if(currentLevel > 0) {
                                 if(this.rite.level + levelBuff > currentLevel) {
                                    var18 = world.rand.nextInt(20) == 0?currentLevel + 1:0;
                                 } else if(this.rite.level + levelBuff < currentLevel) {
                                    var18 = world.rand.nextInt(4) == 0?0:currentLevel + 1;
                                 } else {
                                    var18 = world.rand.nextInt(4) == 0?currentLevel + 1:0;
                                 }
                              }

                              if(var18 == 0) {
                                 if(nbtTag.hasKey(this.rite.curseType)) {
                                    nbtTag.removeTag(this.rite.curseType);
                                 }

                                 if(entity.isPotionActive(Potion.poison)) {
                                    entity.removePotionEffect(Potion.poison.id);
                                 }

                                 if(entity.isPotionActive(Potion.weakness)) {
                                    entity.removePotionEffect(Potion.weakness.id);
                                 }

                                 if(entity.isPotionActive(Potion.blindness)) {
                                    entity.removePotionEffect(Potion.blindness.id);
                                 }

                                 if(entity.isPotionActive(Potion.digSlowdown)) {
                                    entity.removePotionEffect(Potion.digSlowdown.id);
                                 }

                                 if(entity.isPotionActive(Potion.moveSlowdown)) {
                                    entity.removePotionEffect(Potion.moveSlowdown.id);
                                 }
                              } else {
                                 nbtTag.setInteger(this.rite.curseType, var18);
                                 cursed = true;
                              }

                              if(entity instanceof EntityPlayer) {
                                 Infusion.syncPlayer(entity.worldObj, (EntityPlayer)entity);
                              }

                              complete = true;
                           }
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
