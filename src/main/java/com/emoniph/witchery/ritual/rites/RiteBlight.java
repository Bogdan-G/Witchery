package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.ritual.rites.RiteExpandingEffect;
import com.emoniph.witchery.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class RiteBlight extends RiteExpandingEffect {

   public RiteBlight(int radius, int height) {
      super(radius, height, true);
   }

   public boolean doRadiusAction(World world, int posX, int posY, int posZ, int radius, EntityPlayer player, boolean enhanced) {
      double radiusSq = (double)(radius * radius);
      double minSq = (double)Math.max(0, (radius - 1) * (radius - 1));
      Iterator villagersToZombify = world.playerEntities.iterator();

      while(villagersToZombify.hasNext()) {
         Object cowsToSchroom = villagersToZombify.next();
         EntityPlayer animalsToSlay = (EntityPlayer)cowsToSchroom;
         double i$ = animalsToSlay.getDistanceSq(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ);
         if(i$ > minSq && i$ <= radiusSq) {
            if(Witchery.Items.POPPET.voodooProtectionActivated(player, (ItemStack)null, animalsToSlay, 6)) {
               return false;
            }

            if(!animalsToSlay.isPotionActive(Potion.confusion)) {
               animalsToSlay.addPotionEffect(new PotionEffect(Potion.confusion.id, 2400, 1));
            }
         }
      }

      ArrayList villagersToZombify1 = new ArrayList();
      ArrayList cowsToSchroom1 = new ArrayList();
      ArrayList animalsToSlay1 = new ArrayList();
      Iterator i$1 = world.loadedEntityList.iterator();

      while(i$1.hasNext()) {
         Object animal = i$1.next();
         double distanceSq;
         if(animal instanceof EntityVillager) {
            EntityVillager entityzombie = (EntityVillager)animal;
            distanceSq = entityzombie.getDistanceSq(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ);
            if(distanceSq > minSq && distanceSq <= radiusSq) {
               Log.instance().debug(String.format("Try Adding zombie %f %f %f", new Object[]{Double.valueOf(distanceSq), Double.valueOf(minSq), Double.valueOf(radiusSq)}));
               if(world.rand.nextInt(10) == 0) {
                  Log.instance().debug("Added zombie");
                  villagersToZombify1.add(entityzombie);
               }
            }
         } else if(animal instanceof EntityCow) {
            EntityCow entityzombie3 = (EntityCow)animal;
            distanceSq = entityzombie3.getDistanceSq(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ);
            if(distanceSq > minSq && distanceSq <= radiusSq) {
               Log.instance().debug(String.format("Try Adding mooschroom %f %f %f", new Object[]{Double.valueOf(distanceSq), Double.valueOf(minSq), Double.valueOf(radiusSq)}));
               if(world.rand.nextInt(20) == 0) {
                  Log.instance().debug("Added mooschroom");
                  cowsToSchroom1.add(entityzombie3);
               } else if(world.rand.nextInt(3) == 0) {
                  animalsToSlay1.add(entityzombie3);
               }
            }
         } else if(animal instanceof EntityAnimal) {
            EntityAnimal entityzombie2 = (EntityAnimal)animal;
            distanceSq = entityzombie2.getDistanceSq(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ);
            if(distanceSq > minSq && distanceSq <= radiusSq && world.rand.nextInt(3) == 0) {
               animalsToSlay1.add(entityzombie2);
            }
         }
      }

      i$1 = villagersToZombify1.iterator();

      while(i$1.hasNext()) {
         EntityVillager animal2 = (EntityVillager)i$1.next();
         EntityZombie entityzombie1 = new EntityZombie(world);
         entityzombie1.copyLocationAndAnglesFrom(animal2);
         world.removeEntity(animal2);
         entityzombie1.onSpawnWithEgg((IEntityLivingData)null);
         entityzombie1.setVillager(true);
         if(animal2.isChild()) {
            entityzombie1.setChild(true);
         }

         world.spawnEntityInWorld(entityzombie1);
         world.playAuxSFXAtEntity((EntityPlayer)null, 1016, (int)entityzombie1.posX, (int)entityzombie1.posY, (int)entityzombie1.posZ, 0);
      }

      i$1 = cowsToSchroom1.iterator();

      while(i$1.hasNext()) {
         EntityCow animal1 = (EntityCow)i$1.next();
         EntityMooshroom entityzombie4 = new EntityMooshroom(world);
         entityzombie4.copyLocationAndAnglesFrom(animal1);
         world.removeEntity(animal1);
         entityzombie4.onSpawnWithEgg((IEntityLivingData)null);
         world.spawnEntityInWorld(entityzombie4);
         world.playAuxSFXAtEntity((EntityPlayer)null, 1016, (int)entityzombie4.posX, (int)entityzombie4.posY, (int)entityzombie4.posZ, 0);
      }

      i$1 = animalsToSlay1.iterator();

      while(i$1.hasNext()) {
         EntityAnimal animal3 = (EntityAnimal)i$1.next();
         animal3.attackEntityFrom(DamageSource.magic, 20.0F);
      }

      return true;
   }

   public void doBlockAction(World world, int posX, int posY, int posZ, int currentRadius, EntityPlayer player, boolean enhanced) {
      if(!world.isRemote) {
         Block blockID = world.getBlock(posX, posY, posZ);
         Block blockBelowID = world.getBlock(posX, posY - 1, posZ);
         if(blockID == Blocks.tallgrass) {
            world.setBlockToAir(posX, posY, posZ);
            this.blightGround(world, posX, posY - 1, posZ, blockBelowID, enhanced);
         } else if(blockID != Blocks.red_flower && blockID != Blocks.yellow_flower && blockID != Blocks.carrots && blockID != Blocks.wheat && blockID != Blocks.potatoes && blockID != Blocks.pumpkin_stem && blockID != Blocks.melon_stem && blockID != Blocks.melon_block && blockID != Blocks.pumpkin) {
            if(blockID == Blocks.farmland) {
               world.setBlock(posX, posY, posZ, Blocks.sand);
            } else if(blockID.getMaterial().isSolid()) {
               this.blightGround(world, posX, posY, posZ, blockID, enhanced);
            } else if(blockBelowID.getMaterial().isSolid()) {
               this.blightGround(world, posX, posY - 1, posZ, blockBelowID, enhanced);
            }
         } else {
            world.setBlock(posX, posY, posZ, Blocks.deadbush);
            this.blightGround(world, posX, posY - 1, posZ, blockBelowID, enhanced);
         }
      }

   }

   public void blightGround(World world, int posX, int posY, int posZ, Block blockBelowID, boolean enhanced) {
      if(blockBelowID == Blocks.dirt || blockBelowID == Blocks.grass || blockBelowID == Blocks.mycelium || blockBelowID == Blocks.farmland) {
         int rand = world.rand.nextInt(enhanced?4:5);
         if(rand == 0) {
            world.setBlock(posX, posY, posZ, Blocks.sand);
         } else if(rand == 1) {
            world.setBlock(posX, posY, posZ, Blocks.dirt);
         }
      }

   }
}
