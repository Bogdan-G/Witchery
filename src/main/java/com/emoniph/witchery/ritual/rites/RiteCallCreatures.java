package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.brewing.potions.PotionEnderInhibition;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class RiteCallCreatures extends Rite {

   private final float radius;
   private final List creatureTypes;


   public RiteCallCreatures(float radius, Class[] creatureTypes) {
      this.radius = radius;
      this.creatureTypes = Arrays.asList(creatureTypes);
   }

   public void addSteps(ArrayList steps, int initialStage) {
      steps.add(new RiteCallCreatures.StepCallCreatures(this, initialStage));
   }

   private static class StepCallCreatures extends RitualStep {

      private final RiteCallCreatures rite;
      private int stage = 0;


      public StepCallCreatures(RiteCallCreatures rite, int stage) {
         super(false);
         this.rite = rite;
         this.stage = stage;
      }

      private void allure(World world, double posX, double posY, double posZ, int quad) {
         try {
            float e = 128.0F;
            float dy = 10.0F;
            AxisAlignedBB bounds = null;
            switch(quad) {
            case 0:
               bounds = AxisAlignedBB.getBoundingBox(posX, posY - 10.0D, posZ - 128.0D, posX + 128.0D, posY, posZ);
               break;
            case 1:
               bounds = AxisAlignedBB.getBoundingBox(posX - 128.0D, posY - 10.0D, posZ - 128.0D, posX, posY, posZ);
               break;
            case 2:
               bounds = AxisAlignedBB.getBoundingBox(posX, posY - 10.0D, posZ, posX + 128.0D, posY, posZ + 128.0D);
               break;
            case 3:
               bounds = AxisAlignedBB.getBoundingBox(posX - 128.0D, posY - 10.0D, posZ, posX, posY, posZ + 128.0D);
               break;
            case 4:
               bounds = AxisAlignedBB.getBoundingBox(posX - 128.0D, posY + 1.0D, posZ - 128.0D, posX, posY + 10.0D, posZ);
               break;
            case 5:
               bounds = AxisAlignedBB.getBoundingBox(posX, posY + 1.0D, posZ, posX + 128.0D, posY + 10.0D, posZ + 128.0D);
               break;
            case 6:
               bounds = AxisAlignedBB.getBoundingBox(posX - 128.0D, posY + 1.0D, posZ, posX, posY + 10.0D, posZ + 128.0D);
               break;
            case 7:
            default:
               bounds = AxisAlignedBB.getBoundingBox(posX, posY + 1.0D, posZ - 128.0D, posX + 128.0D, posY + 10.0D, posZ);
            }

            int count = 0;
            boolean minDistanceSq = true;
            Iterator i$ = world.getEntitiesWithinAABB(EntityCreature.class, bounds).iterator();

            while(i$.hasNext()) {
               Object obj = i$.next();
               EntityCreature creature = (EntityCreature)obj;
               if(this.rite.creatureTypes.contains(creature.getClass()) && creature.getDistanceSq(posX, posY, posZ) > 32.0D && !PotionEnderInhibition.isActive(creature, 0)) {
                  ItemGeneral var10000 = Witchery.Items.GENERIC;
                  ItemGeneral.teleportToLocation(world, posX - 2.0D + (double)world.rand.nextInt(5), posY, posZ - 2.0D + (double)world.rand.nextInt(5), world.provider.dimensionId, creature, true);
                  ++count;
                  if(count >= 2) {
                     break;
                  }
               }
            }
         } catch (Exception var17) {
            Log.instance().debug(String.format("Exception occurred alluring with a ritual! %s", new Object[]{var17.toString()}));
         }

      }

      public int getCurrentStage() {
         return this.stage;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 60L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               if(ritual.covenSize < 3) {
                  EntityPlayer player = ritual.getInitiatingPlayer(world);
                  SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
                  if(player != null) {
                     ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.coventoosmall", new Object[0]);
                  }

                  return RitualStep.Result.ABORTED_REFUND;
               }

               this.allure(world, (double)posX, (double)posY, (double)posZ, ++this.stage % 8);
            }

            return this.stage < 250?RitualStep.Result.UPKEEP:RitualStep.Result.COMPLETED;
         }
      }
   }
}
