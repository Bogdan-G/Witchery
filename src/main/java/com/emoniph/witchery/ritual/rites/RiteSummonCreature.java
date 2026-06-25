package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityImp;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteSummonCreature extends Rite {

   private final Class creatureToSummon;
   private boolean bindTameable;


   public RiteSummonCreature(Class creatureToSummon, boolean bindTameable) {
      this.creatureToSummon = creatureToSummon;
      this.bindTameable = bindTameable;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteSummonCreature.StepSummonCreature(this));
   }

   private static class StepSummonCreature extends RitualStep {

      private final RiteSummonCreature rite;


      public StepSummonCreature(RiteSummonCreature rite) {
         super(false);
         this.rite = rite;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               int[][] PATTERN = new int[][]{{0, 0, 1, 1, 1, 0, 0}, {0, 1, 1, 1, 1, 1, 0}, {1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 2, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1}, {0, 1, 1, 1, 1, 1, 0}, {0, 0, 1, 1, 1, 0, 0}};
               int obstructions = 0;

               for(int MAX_OBSTRUCTIONS = posY + 1; MAX_OBSTRUCTIONS <= posY + 3; ++MAX_OBSTRUCTIONS) {
                  int ex = (PATTERN.length - 1) / 2;

                  for(int entity = 0; entity < PATTERN.length - 1; ++entity) {
                     int entitylivingData = posZ - ex + entity;
                     int offsetX = (PATTERN[entity].length - 1) / 2;

                     for(int x = 0; x < PATTERN[entity].length; ++x) {
                        int worldX = posX - offsetX + x;
                        int val = PATTERN[PATTERN.length - 1 - entity][x];
                        Material material;
                        if(val == 1) {
                           material = world.getBlock(worldX, MAX_OBSTRUCTIONS, entitylivingData).getMaterial();
                           if(material != null && material.isSolid()) {
                              ++obstructions;
                           }
                        } else if(val == 2) {
                           material = world.getBlock(worldX, MAX_OBSTRUCTIONS, entitylivingData).getMaterial();
                           if(material != null && material.isSolid()) {
                              obstructions += 100;
                           }
                        }
                     }
                  }
               }

               boolean var24 = true;
               if(obstructions > 1) {
                  ParticleEffect.LARGE_SMOKE.send(SoundEffect.NOTE_SNARE, world, (double)posX, (double)posY, (double)posZ, 0.5D, 2.0D, 16);
                  RiteRegistry.RiteError("witchery.rite.obstructedcircle", ritual.getInitiatingPlayerName(), world);
                  return RitualStep.Result.ABORTED_REFUND;
               }

               try {
                  Constructor var23 = this.rite.creatureToSummon.getConstructor(new Class[]{World.class});
                  EntityLiving var25 = (EntityLiving)var23.newInstance(new Object[]{world});
                  EntityPlayer var26;
                  if(var25 instanceof EntityDemon) {
                     ((EntityDemon)var25).setPlayerCreated(true);
                  } else {
                     if(var25 instanceof EntityImp && ritual.covenSize == 0) {
                        var26 = ritual.getInitiatingPlayer(world);
                        SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
                        if(var26 != null) {
                           ChatUtil.sendTranslated(EnumChatFormatting.RED, var26, "witchery.rite.coventoosmall", new Object[0]);
                        }

                        return RitualStep.Result.ABORTED_REFUND;
                     }

                     if(this.rite.bindTameable && var25 instanceof EntityTameable) {
                        ((EntityTameable)var25).setTamed(true);
                        TameableUtil.setOwner((EntityTameable)var25, ritual.getInitiatingPlayer(world));
                     }
                  }

                  var25.setLocationAndAngles(0.5D + (double)posX, 1.0D + (double)posY, 0.5D + (double)posZ, 1.0F, 0.0F);
                  world.spawnEntityInWorld(var25);
                  var26 = null;
                  var25.onSpawnWithEgg(null);
                  ParticleEffect.PORTAL.send(SoundEffect.RANDOM_FIZZ, var25, 0.5D, 1.0D, 16);
               } catch (NoSuchMethodException var19) {
                  ;
               } catch (InvocationTargetException var20) {
                  ;
               } catch (InstantiationException var21) {
                  ;
               } catch (IllegalAccessException var22) {
                  ;
               }
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
