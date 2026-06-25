package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.ritual.rites.RiteExpandingEffect;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class RiteHellOnEarth extends RiteExpandingEffect {

   private final float upkeepCost;
   static final int POWER_SOURCE_RADIUS = 16;


   public RiteHellOnEarth(int radius, int height, float upkeepCost) {
      super(radius, height, true);
      this.upkeepCost = upkeepCost;
   }

   public boolean isComplete(World world, int posX, int posY, int posZ, int radius, EntityPlayer player, long ticks, boolean fullyExpanded, boolean enhanced) {
      if(fullyExpanded && ticks % 40L == 0L) {
         IPowerSource powerSource = this.findNewPowerSource(world, posX, posY, posZ);
         if(powerSource == null) {
            return true;
         }

         if(!powerSource.consumePower(this.upkeepCost)) {
            return true;
         }

         double roll = world.rand.nextDouble();
         Object entity = null;
         if(roll < 0.02D) {
            entity = new EntityDemon(world);
         } else if(roll < 0.1D) {
            entity = new EntityGhast(world);
         } else if(roll < 0.4D) {
            entity = new EntityBlaze(world);
         } else if(roll < 0.6D) {
            entity = new EntityMagmaCube(world);
         } else {
            entity = new EntityPigZombie(world);
         }

         if(entity != null) {
            ((EntityLiving)entity).onSpawnWithEgg((IEntityLivingData)null);
            ((EntityLiving)entity).setLocationAndAngles(0.5D + (double)posX, 2.0D + (double)posY, 0.5D + (double)posZ, 0.0F, 0.0F);
            world.spawnEntityInWorld((Entity)entity);
            ParticleEffect.LARGE_EXPLODE.send(SoundEffect.MOB_BLAZE_DEATH, world, 0.5D + (double)posX, 2.0D + (double)posY, 0.5D + (double)posZ, 1.0D, 2.0D, 16);
         }
      }

      return false;
   }

   public boolean doRadiusAction(World world, int posX, int posY, int posZ, int radius, EntityPlayer player, boolean enhanced) {
      return true;
   }

   public void doBlockAction(World world, int posX, int posY, int posZ, int currentRadius, EntityPlayer player, boolean enhanced) {
      if(!world.isRemote) {
         Block blockID = world.getBlock(posX, posY, posZ);
         Block blockBelowID = world.getBlock(posX, posY - 1, posZ);
         if(blockID == Blocks.tallgrass) {
            if(Config.instance().allowHellOnEarthFires && enhanced) {
               world.setBlock(posX, posY, posZ, Blocks.fire);
            }

            this.blightGround(world, posX, posY - 1, posZ, blockBelowID, currentRadius);
         } else if(blockID != Blocks.red_flower && blockID != Blocks.yellow_flower && blockID != Blocks.carrots && blockID != Blocks.wheat && blockID != Blocks.potatoes && blockID != Blocks.pumpkin_stem && blockID != Blocks.melon_stem && blockID != Blocks.melon_block && blockID != Blocks.pumpkin) {
            if(blockID.getMaterial().isSolid()) {
               this.blightGround(world, posX, posY, posZ, blockID, currentRadius);
            } else if(blockBelowID.getMaterial().isSolid()) {
               this.blightGround(world, posX, posY - 1, posZ, blockBelowID, currentRadius);
            }
         } else {
            if(Config.instance().allowHellOnEarthFires && enhanced) {
               world.setBlock(posX, posY, posZ, Blocks.fire);
            }

            this.blightGround(world, posX, posY - 1, posZ, blockBelowID, currentRadius);
         }
      }

   }

   public void blightGround(World world, int posX, int posY, int posZ, Block blockBelowID, int currentRadius) {
      if(blockBelowID == Blocks.dirt || blockBelowID == Blocks.grass || blockBelowID == Blocks.mycelium || blockBelowID == Blocks.farmland || blockBelowID == Blocks.sand) {
         int rand = world.rand.nextInt(currentRadius < super.maxRadius / 3?2:(currentRadius < super.maxRadius / 2?4:6));
         if(rand == 0) {
            world.setBlock(posX, posY, posZ, Blocks.netherrack);
         }
      }

   }

   private IPowerSource findNewPowerSource(World world, int posX, int posY, int posZ) {
      ArrayList sources = PowerSources.instance() != null?PowerSources.instance().get(world, new Coord(posX, posY, posZ), 16):null;
      return sources != null && sources.size() > 0?((PowerSources.RelativePowerSource)sources.get(0)).source():null;
   }
}
