package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.World;

public class RiteCookItem extends Rite {

   private final float radius;
   private final double burnChance;


   public RiteCookItem(float radius, double burnChance) {
      this.radius = radius;
      this.burnChance = burnChance;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteCookItem.StepCookItem(this));
   }

   private static class StepCookItem extends RitualStep {

      private final RiteCookItem rite;


      public StepCookItem(RiteCookItem rite) {
         super(false);
         this.rite = rite;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               ArrayList items = this.rite.getItemsInRadius(world, posX, posY, posZ, this.rite.radius);
               int count = 0;
               Iterator i$ = items.iterator();

               while(i$.hasNext()) {
                  EntityItem item = (EntityItem)i$.next();
                  ItemStack cookedStack = FurnaceRecipes.smelting().getSmeltingResult(item.getEntityItem());
                  if(cookedStack != null && cookedStack.getItem() instanceof ItemFood && item.getEntityItem().stackSize > 0) {
                     int size = item.getEntityItem().stackSize;
                     int burnCount = 0;

                     for(int burntEntity = 0; burntEntity < size; ++burntEntity) {
                        if(world.rand.nextDouble() < this.rite.burnChance) {
                           ++burnCount;
                        }
                     }

                     item.setDead();
                     EntityItem var16;
                     if(size - burnCount > 0) {
                        cookedStack.stackSize = size - burnCount;
                        var16 = new EntityItem(world, (double)posX, (double)posY + 0.05D, (double)posZ, cookedStack);
                        var16.motionX = 0.0D;
                        var16.motionZ = 0.0D;
                        world.spawnEntityInWorld(var16);
                     }

                     if(burnCount > 0) {
                        var16 = new EntityItem(world, (double)posX, (double)posY + 0.05D, (double)posZ, new ItemStack(Items.coal, burnCount, 1));
                        var16.motionX = 0.0D;
                        var16.motionZ = 0.0D;
                        world.spawnEntityInWorld(var16);
                     }

                     ++count;
                  }
               }

               if(count == 0) {
                  return RitualStep.Result.ABORTED_REFUND;
               }

               ParticleEffect.FLAME.send(SoundEffect.MOB_GHAST_FIREBALL, world, (double)posX, (double)posY, (double)posZ, 3.0D, 2.0D, 16);
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
