package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.entity.EntityBanshee;
import com.emoniph.witchery.entity.EntityDeath;
import com.emoniph.witchery.entity.EntityPoltergeist;
import com.emoniph.witchery.entity.EntitySpectre;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.infusion.infusions.spirit.InfusedSpiritEffect;
import com.emoniph.witchery.item.ItemDeathsClothes;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteBindSpiritsToFetish extends Rite {

   private final int radius;


   public RiteBindSpiritsToFetish(int radius) {
      this.radius = radius;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteBindSpiritsToFetish.StepSpiritsToFetish(this));
   }

   private static class StepSpiritsToFetish extends RitualStep {

      private final RiteBindSpiritsToFetish rite;


      public StepSpiritsToFetish(RiteBindSpiritsToFetish rite) {
         super(false);
         this.rite = rite;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               int r = this.rite.radius;
               int r2 = r * r;
               AxisAlignedBB bb = AxisAlignedBB.getBoundingBox((double)(posX - r), (double)(posY - r), (double)(posZ - r), (double)(posX + r), (double)(posY + r), (double)(posZ + r));
               List entities = world.getEntitiesWithinAABB(EntityCreature.class, bb);
               ArrayList spectreList = new ArrayList();
               ArrayList spiritList = new ArrayList();
               ArrayList bansheeList = new ArrayList();
               ArrayList poltergeistList = new ArrayList();
               Iterator stack = entities.iterator();

               while(stack.hasNext()) {
                  Object result = stack.next();
                  if(result instanceof EntitySpectre) {
                     spectreList.add((EntitySpectre)result);
                  } else if(result instanceof EntityPoltergeist) {
                     poltergeistList.add((EntityPoltergeist)result);
                  } else if(result instanceof EntityBanshee) {
                     bansheeList.add((EntityBanshee)result);
                  } else if(result instanceof EntitySpirit) {
                     spiritList.add((EntitySpirit)result);
                  }
               }

               ItemStack stack1 = null;
               Iterator result2 = ritual.sacrificedItems.iterator();

               while(result2.hasNext()) {
                  RitualStep.SacrificedItem entity = (RitualStep.SacrificedItem)result2.next();
                  if(entity.itemstack.isItemEqual(new ItemStack(Witchery.Blocks.FETISH_SCARECROW))) {
                     stack1 = entity.itemstack;
                     break;
                  }

                  if(entity.itemstack.isItemEqual(new ItemStack(Witchery.Blocks.FETISH_TREANT_IDOL))) {
                     stack1 = entity.itemstack;
                     break;
                  }

                  if(entity.itemstack.isItemEqual(new ItemStack(Witchery.Blocks.FETISH_WITCHS_LADDER))) {
                     stack1 = entity.itemstack;
                     break;
                  }
               }

               if(stack1 == null) {
                  return RitualStep.Result.ABORTED_REFUND;
               }

               int result1 = InfusedSpiritEffect.tryBindFetish(world, stack1, spiritList, spectreList, bansheeList, poltergeistList);
               if(result1 == 0) {
                  return RitualStep.Result.ABORTED_REFUND;
               }

               if(result1 == 2) {
                  EntityPlayer entity2 = this.findDeathPlayer(world);
                  if(entity2 != null) {
                     ItemGeneral var10000 = Witchery.Items.GENERIC;
                     ItemGeneral.teleportToLocation(world, (double)posX, (double)posY, (double)posZ, world.provider.dimensionId, entity2, true);
                     ParticleEffect.INSTANT_SPELL.send(SoundEffect.MOB_WITHER_SPAWN, entity2, 0.5D, 1.5D, 16);
                  } else {
                     EntityDeath death = new EntityDeath(world);
                     death.setLocationAndAngles(0.5D + (double)posX, (double)posY + 0.1D, 0.5D + (double)posZ, 0.0F, 0.0F);
                     death.func_110163_bv();
                     world.spawnEntityInWorld(death);
                     ParticleEffect.INSTANT_SPELL.send(SoundEffect.MOB_WITHER_SPAWN, death, 0.5D, 1.5D, 16);
                  }
               } else {
                  EntityItem entity1 = new EntityItem(world, 0.5D + (double)posX, (double)posY + 1.5D, 0.5D + (double)posZ, stack1);
                  entity1.motionX = 0.0D;
                  entity1.motionY = 0.3D;
                  entity1.motionZ = 0.0D;
                  world.spawnEntityInWorld(entity1);
                  ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, entity1, 0.5D, 1.5D, 16);
               }
            }

            return RitualStep.Result.COMPLETED;
         }
      }

      private EntityPlayer findDeathPlayer(World world) {
         Iterator i$ = world.playerEntities.iterator();

         EntityPlayer player;
         do {
            if(!i$.hasNext()) {
               return null;
            }

            Object obj = i$.next();
            player = (EntityPlayer)obj;
         } while(!ItemDeathsClothes.isFullSetWorn(player) || player.getCurrentEquippedItem() == null || player.getCurrentEquippedItem().getItem() != Witchery.Items.DEATH_HAND);

         return player;
      }
   }
}
