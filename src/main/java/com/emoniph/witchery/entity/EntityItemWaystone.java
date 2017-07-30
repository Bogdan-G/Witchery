package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionEnderInhibition;
import com.emoniph.witchery.common.IPowerSource;
import com.emoniph.witchery.common.PowerSources;
import com.emoniph.witchery.common.ServerTickEvents;
import com.emoniph.witchery.entity.EntitySpirit;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.CircleUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityItemWaystone extends EntityItem {

   public EntityItemWaystone(World world) {
      super(world);
   }

   public EntityItemWaystone(World world, double x, double y, double z) {
      super(world, x, y, z);
   }

   public EntityItemWaystone(World world, double x, double y, double z, ItemStack stack) {
      super(world, x, y, z, stack);
   }

   public EntityItemWaystone(EntityItem entityItem) {
      super(entityItem.worldObj, entityItem.posX, entityItem.posY, entityItem.posZ, entityItem.getEntityItem());
      super.delayBeforeCanPickup = entityItem.delayBeforeCanPickup;
      super.motionX = entityItem.motionX;
      super.motionY = entityItem.motionY;
      super.motionZ = entityItem.motionZ;
   }

   public void onCollideWithPlayer(EntityPlayer player) {
      double minPickupRange = 0.75D;
      double minPickupRangeSq = 0.5625D;
      if(this.getDistanceSqToEntity(player) <= 0.5625D) {
         super.onCollideWithPlayer(player);
      }

   }

   public void onUpdate() {
      super.onUpdate();
      if(!super.worldObj.isRemote && super.age > TimeUtil.secsToTicks(2) && super.age % 40 == 0) {
         Block glyph;
         Coord center;
         int originalStackSize;
         int remainingStackSize;
         double R;
         double RSq;
         EntityPosition centerPoint;
         AxisAlignedBB bounds1;
         if(Witchery.Items.GENERIC.itemWaystone.isMatch(this.getEntityItem())) {
            glyph = Witchery.Blocks.GLYPH_OTHERWHERE;
            center = isTinyBlockCircle(super.worldObj, new Coord(this), glyph);
            if(center != null) {
               originalStackSize = this.getEntityItem().stackSize;
               remainingStackSize = 0;
               R = 2.0D;
               RSq = 4.0D;
               centerPoint = new EntityPosition((double)center.x + 0.5D, (double)center.y + 0.5D, (double)center.z + 0.5D);
               bounds1 = AxisAlignedBB.getBoundingBox(centerPoint.x - 2.0D, centerPoint.y - 2.0D, centerPoint.z - 2.0D, centerPoint.x + 2.0D, centerPoint.y + 2.0D, centerPoint.z + 2.0D);
               ItemStack convertableStackSize = null;
               Object creature = null;
               double spirit = -1.0D;
               List nearbyPlayers = super.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bounds1);
               Iterator convertableStackSize1 = nearbyPlayers.iterator();

               while(convertableStackSize1.hasNext()) {
                  EntityPlayer convertableStackSize2 = (EntityPlayer)convertableStackSize1.next();
                  double creature1 = convertableStackSize2.getDistanceSq(centerPoint.x, convertableStackSize2.posY, centerPoint.z);
                  if(creature1 <= 4.0D && (creature == null || creature1 < spirit)) {
                     creature = convertableStackSize2;
                     spirit = creature1;
                  }
               }

               if(creature == null) {
                  List convertableStackSize6 = super.worldObj.getEntitiesWithinAABB(EntityLiving.class, bounds1);
                  Iterator convertableStackSize8 = convertableStackSize6.iterator();

                  while(convertableStackSize8.hasNext()) {
                     EntityLiving creature3 = (EntityLiving)convertableStackSize8.next();
                     double distSq = creature3.getDistanceSq(centerPoint.x, creature3.posY, centerPoint.z);
                     if(distSq <= 4.0D && (creature == null || distSq < spirit)) {
                        creature = creature3;
                        spirit = distSq;
                     }
                  }
               }

               if(creature != null) {
                  IPowerSource convertableStackSize5 = PowerSources.findClosestPowerSource(super.worldObj, center);
                  if(convertableStackSize5 != null) {
                     if(convertableStackSize5.consumePower(4000.0F)) {
                        int convertableStackSize7 = Math.min(originalStackSize, 1);
                        remainingStackSize = originalStackSize - convertableStackSize7;
                        convertableStackSize = Witchery.Items.GENERIC.itemWaystonePlayerBound.createStack(convertableStackSize7);
                        Witchery.Items.TAGLOCK_KIT.setTaglockForEntity(convertableStackSize, (EntityPlayer)null, (Entity)creature, false, Integer.valueOf(1));
                     } else {
                        ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, super.worldObj, center, 1.0D, 1.0D, 16);
                     }
                  } else {
                     ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, super.worldObj, center, 1.0D, 1.0D, 16);
                  }
               } else {
                  int convertableStackSize9 = Math.min(originalStackSize, 8);
                  remainingStackSize = originalStackSize - convertableStackSize9;
                  convertableStackSize = Witchery.Items.GENERIC.itemWaystoneBound.createStack(convertableStackSize9);
                  Witchery.Items.GENERIC.bindToLocation(super.worldObj, center.x, center.y, center.z, super.dimension, super.worldObj.provider.getDimensionName(), convertableStackSize);
               }

               if(convertableStackSize != null) {
                  EntityUtil.spawnEntityInWorld(super.worldObj, new EntityItem(super.worldObj, super.posX, super.posY, super.posZ, convertableStackSize));
                  if(remainingStackSize > 0) {
                     EntityUtil.spawnEntityInWorld(super.worldObj, new EntityItem(super.worldObj, super.posX, super.posY, super.posZ, Witchery.Items.GENERIC.itemWaystone.createStack(remainingStackSize)));
                  }

                  ParticleEffect.LARGE_EXPLODE.send(SoundEffect.RANDOM_POP, this, 1.0D, 1.0D, 16);
                  isInnerTinyBlockCircle(super.worldObj, center.x, center.y, center.z, glyph, true);
                  this.setDead();
               }
            }
         } else if(!Witchery.Items.GENERIC.itemWaystoneBound.isMatch(this.getEntityItem()) && !Witchery.Items.GENERIC.itemWaystonePlayerBound.isMatch(this.getEntityItem())) {
            boolean remainingStackSize1;
            int convertableStackSize4;
            EntityCreature creature2;
            EntitySpirit spirit1;
            if(Witchery.Items.GENERIC.itemAttunedStone.isMatch(this.getEntityItem())) {
               glyph = Witchery.Blocks.GLYPH_RITUAL;
               center = isTinyBlockCircle(super.worldObj, new Coord(this), glyph);
               if(center != null) {
                  originalStackSize = this.getEntityItem().stackSize;
                  remainingStackSize1 = false;
                  R = 2.0D;
                  RSq = 4.0D;
                  centerPoint = new EntityPosition((double)center.x + 0.5D, (double)center.y + 0.5D, (double)center.z + 0.5D);
                  bounds1 = AxisAlignedBB.getBoundingBox(centerPoint.x - 2.0D, centerPoint.y - 2.0D, centerPoint.z - 2.0D, centerPoint.x + 2.0D, centerPoint.y + 2.0D, centerPoint.z + 2.0D);
                  convertableStackSize4 = Math.min(originalStackSize, 1);
                  remainingStackSize = originalStackSize - convertableStackSize4;
                  creature2 = Infusion.spawnCreature(super.worldObj, EntitySpirit.class, (int)super.posX, (int)super.posY, (int)super.posZ, (EntityLivingBase)null, 0, 0, ParticleEffect.INSTANT_SPELL, (SoundEffect)null);
                  if(creature2 != null) {
                     spirit1 = (EntitySpirit)creature2;
                     creature2.func_110163_bv();
                     spirit1.setTarget("Village", 2);
                  }

                  if(remainingStackSize > 0) {
                     EntityUtil.spawnEntityInWorld(super.worldObj, new EntityItem(super.worldObj, super.posX, super.posY, super.posZ, Witchery.Items.GENERIC.itemAttunedStone.createStack(remainingStackSize)));
                  }

                  ParticleEffect.LARGE_EXPLODE.send(SoundEffect.RANDOM_POP, this, 1.0D, 1.0D, 16);
                  isInnerTinyBlockCircle(super.worldObj, center.x, center.y, center.z, glyph, true);
                  this.setDead();
               }
            } else if(Witchery.Items.GENERIC.itemSubduedSpirit.isMatch(this.getEntityItem())) {
               glyph = Witchery.Blocks.GLYPH_RITUAL;
               center = isTinyBlockCircle(super.worldObj, new Coord(this), glyph);
               if(center != null) {
                  originalStackSize = this.getEntityItem().stackSize;
                  remainingStackSize1 = false;
                  R = 2.0D;
                  RSq = 4.0D;
                  centerPoint = new EntityPosition((double)center.x + 0.5D, (double)center.y + 0.5D, (double)center.z + 0.5D);
                  bounds1 = AxisAlignedBB.getBoundingBox(centerPoint.x - 2.0D, centerPoint.y - 2.0D, centerPoint.z - 2.0D, centerPoint.x + 2.0D, centerPoint.y + 2.0D, centerPoint.z + 2.0D);
                  convertableStackSize4 = Math.min(originalStackSize, 1);
                  remainingStackSize = originalStackSize - convertableStackSize4;
                  creature2 = Infusion.spawnCreature(super.worldObj, EntitySpirit.class, (int)super.posX, (int)super.posY, (int)super.posZ, (EntityLivingBase)null, 0, 0, ParticleEffect.INSTANT_SPELL, (SoundEffect)null);
                  if(creature2 != null) {
                     spirit1 = (EntitySpirit)creature2;
                     creature2.func_110163_bv();
                     spirit1.setTarget("Village", 2);
                  }

                  if(remainingStackSize > 0) {
                     EntityUtil.spawnEntityInWorld(super.worldObj, new EntityItem(super.worldObj, super.posX, super.posY, super.posZ, Witchery.Items.GENERIC.itemSubduedSpirit.createStack(remainingStackSize)));
                  }

                  ParticleEffect.LARGE_EXPLODE.send(SoundEffect.RANDOM_POP, this, 1.0D, 1.0D, 16);
                  isInnerTinyBlockCircle(super.worldObj, center.x, center.y, center.z, glyph, true);
                  this.setDead();
               }
            }
         } else {
            glyph = Witchery.Blocks.GLYPH_OTHERWHERE;
            center = isSmallBlockCircle(super.worldObj, new Coord(this), glyph);
            if(center != null) {
               double originalStackSize1 = 4.0D;
               R = 16.0D;
               ItemStack RSq1 = this.getEntityItem().splitStack(1);
               if(this.getEntityItem().stackSize > 0) {
                  EntityUtil.spawnEntityInWorld(super.worldObj, new EntityItem(super.worldObj, super.posX, super.posY, super.posZ, this.getEntityItem()));
               }

               this.setDead();
               AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)center.x + 0.5D - 4.0D, (double)center.y + 0.5D - 4.0D, (double)center.z + 0.5D - 4.0D, (double)center.x + 0.5D + 4.0D, (double)center.y + 0.5D + 4.0D, (double)center.z + 0.5D + 4.0D);
               List centerPoint1 = super.worldObj.getEntitiesWithinAABB(Entity.class, bounds);
               Iterator bounds2 = centerPoint1.iterator();

               while(bounds2.hasNext()) {
                  Entity convertableStackSize3 = (Entity)bounds2.next();
                  if(!convertableStackSize3.isDead && convertableStackSize3.getDistanceSq(0.5D + (double)center.x, convertableStackSize3.posY, 0.5D + (double)center.z) <= 16.0D && (convertableStackSize3 instanceof EntityLivingBase || convertableStackSize3 instanceof EntityItem) && !PotionEnderInhibition.isActive(convertableStackSize3, 1)) {
                     ServerTickEvents.TASKS.add(new EntityItemWaystone.TeleportTask(super.worldObj, RSq1, convertableStackSize3));
                  }
               }
            }
         }
      }

   }

   private static Coord isTinyBlockCircle(World world, Coord coord, Block runeBlock) {
      int x = coord.x;
      int y = coord.y;
      int z = coord.z;
      return isInnerTinyBlockCircle(world, x, y, z, runeBlock, false)?coord:null;
   }

   private static boolean isInnerTinyBlockCircle(World world, int x, int y, int z, Block runeBlock, boolean explode) {
      int[][] circle = new int[][]{{x, z - 1}, {x + 1, z - 1}, {x + 1, z}, {x + 1, z + 1}, {x, z + 1}, {x - 1, z + 1}, {x - 1, z}, {x - 1, z - 1}};
      int[][] arr$ = circle;
      int len$ = circle.length;

      int i$;
      int[] coord;
      for(i$ = 0; i$ < len$; ++i$) {
         coord = arr$[i$];
         if(world.getBlock(coord[0], y, coord[1]) != runeBlock) {
            return false;
         }
      }

      if(explode) {
         arr$ = circle;
         len$ = circle.length;

         for(i$ = 0; i$ < len$; ++i$) {
            coord = arr$[i$];
            world.setBlockToAir(coord[0], y, coord[1]);
            ParticleEffect.EXPLODE.send(SoundEffect.NONE, world, 0.5D + (double)coord[0], (double)y, 0.5D + (double)coord[1], 0.5D, 0.5D, 16);
         }
      }

      return true;
   }

   private static Coord isSmallBlockCircle(World world, Coord coord, Block runeBlock) {
      int x = coord.x;
      int z = coord.z;
      int[][] circle = new int[][]{{0, 0}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}};
      int[][] arr$ = circle;
      int len$ = circle.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         int[] co = arr$[i$];
         if(CircleUtil.isSmallCircle(world, coord.x + co[0], coord.y, coord.z + co[1], runeBlock)) {
            return new Coord(coord.x - co[0], coord.y, coord.z - co[1]);
         }
      }

      return null;
   }

   private static class TeleportTask extends ServerTickEvents.ServerTickTask {

      ItemStack stone;
      Entity entity;


      public TeleportTask(World world, ItemStack stone, Entity entity) {
         super(world);
         this.stone = stone;
         this.entity = entity;
      }

      public boolean process() {
         if(!Witchery.Items.GENERIC.teleportToLocation(super.world, this.stone, this.entity, 0, true)) {
            ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, super.world, this.entity.posX, this.entity.posY, this.entity.posZ, 1.0D, 1.0D, 16);
         }

         return true;
      }
   }
}
