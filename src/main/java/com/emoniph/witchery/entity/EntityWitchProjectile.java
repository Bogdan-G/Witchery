package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.blocks.BlockCircleGlyph;
import com.emoniph.witchery.brewing.potions.PotionEnslaved;
import com.emoniph.witchery.entity.EntityDemon;
import com.emoniph.witchery.entity.EntityEnt;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.entity.EntityOwl;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.network.PacketPushTarget;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.EarthItems;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class EntityWitchProjectile extends EntityThrowable {

   private int damageValue;
   private boolean skipFX = false;
   private static final String DAMAGE_VALUE_KEY = "damageValue";


   public EntityWitchProjectile(World world) {
      super(world);
   }

   public EntityWitchProjectile(World world, EntityLivingBase entityLiving, ItemGeneral.SubItem generalSubItem) {
      super(world, entityLiving);
      this.setDamageValue(generalSubItem.damageValue);
   }

   public EntityWitchProjectile(World world, double posX, double posY, double posZ, ItemGeneral.SubItem generalSubItem) {
      super(world, posX, posY, posZ);
      this.setDamageValue(generalSubItem.damageValue);
   }

   protected void entityInit() {
      super.dataWatcher.addObject(6, Integer.valueOf(0));
      super.entityInit();
   }

   public void setDamageValue(int damageValue) {
      this.damageValue = damageValue;
      this.getDataWatcher().updateObject(6, Integer.valueOf(damageValue));
   }

   public int getDamageValue() {
      return this.getDataWatcher().getWatchableObjectInt(6);
   }

   public boolean isPotion() {
      return Witchery.Items.GENERIC.subItems.get(this.damageValue) instanceof ItemGeneral.Brew || Witchery.Items.GENERIC.itemQuicklime.damageValue == this.damageValue;
   }

   protected float getGravityVelocity() {
      return this.isPotion()?0.05F:super.getGravityVelocity();
   }

   protected float func_70182_d() {
      return this.isPotion()?0.5F:super.func_70182_d();
   }

   protected float func_70183_g() {
      return this.isPotion()?-20.0F:super.func_70183_g();
   }

   protected void onImpact(MovingObjectPosition mop) {
      if(!super.worldObj.isRemote && mop != null) {
         boolean enhanced = false;
         EntityLivingBase thrower = this.getThrower();
         if(thrower != null && thrower instanceof EntityPlayer) {
            enhanced = Familiar.hasActiveBrewMasteryFamiliar((EntityPlayer)thrower);
         }

         this.skipFX = false;
         if(Witchery.Items.GENERIC.itemBrewOfVines.damageValue == this.damageValue) {
            this.impactVines(mop, enhanced);
         } else if(Witchery.Items.GENERIC.itemBrewOfThorns.damageValue == this.damageValue) {
            this.impactThorns(mop, enhanced);
         } else if(Witchery.Items.GENERIC.itemBrewOfWebs.damageValue == this.damageValue) {
            this.impactWebBig(mop, enhanced);
         } else if(Witchery.Items.GENERIC.itemBrewOfInk.damageValue == this.damageValue) {
            this.impactInk(mop, enhanced);
         } else if(Witchery.Items.GENERIC.itemBrewOfWasting.damageValue == this.damageValue) {
            this.impactWasting(mop, enhanced);
         } else if(Witchery.Items.GENERIC.itemBrewOfSprouting.damageValue == this.damageValue) {
            this.impactSprout(mop, enhanced);
         } else if(Witchery.Items.GENERIC.itemBrewOfErosion.damageValue == this.damageValue) {
            this.impactErosion(mop, enhanced);
         } else if(Witchery.Items.GENERIC.itemBrewOfLove.damageValue == this.damageValue) {
            this.impactLove(mop, enhanced);
         } else if(Witchery.Items.GENERIC.itemWeb.damageValue == this.damageValue) {
            this.impactWebSmall(mop);
            this.skipFX = true;
         } else if(Witchery.Items.GENERIC.itemRock.damageValue == this.damageValue) {
            this.impactRock(mop);
            this.skipFX = true;
         } else if(Witchery.Items.GENERIC.itemBrewOfRaising.damageValue == this.damageValue) {
            this.impactRaising(mop);
         } else if(Witchery.Items.GENERIC.itemQuicklime.damageValue == this.damageValue) {
            this.impactQuicklime(mop);
         } else if(Witchery.Items.GENERIC.itemBrewOfIce.damageValue == this.damageValue) {
            this.impactIce(mop);
         } else if(Witchery.Items.GENERIC.itemBrewOfFrogsTongue.damageValue == this.damageValue) {
            this.impactFrogsTongue(mop, false);
         } else if(Witchery.Items.GENERIC.itemBrewOfCursedLeaping.damageValue == this.damageValue) {
            this.impactLeaping(mop, false);
         } else if(Witchery.Items.GENERIC.itemBrewOfHitchcock.damageValue == this.damageValue) {
            this.impactHitchcock(mop);
         } else if(Witchery.Items.GENERIC.itemBrewOfInfection.damageValue == this.damageValue) {
            this.impactInfection(mop, enhanced);
         } else if(Witchery.Items.GENERIC.itemBrewOfBats.damageValue == this.damageValue) {
            this.impactBats(mop, enhanced);
         } else {
            ItemGeneral.SubItem item = (ItemGeneral.SubItem)Witchery.Items.GENERIC.subItems.get(this.damageValue);
            if(item instanceof ItemGeneral.Brew) {
               ItemGeneral.Brew brew = (ItemGeneral.Brew)item;
               ItemGeneral.Brew.BrewResult result = brew.onImpact(super.worldObj, thrower, mop, enhanced, super.posX, super.posY, super.posZ, super.boundingBox);
               if(result == ItemGeneral.Brew.BrewResult.DROP_ITEM) {
                  EntityItem itemEntity = null;
                  if(mop != null) {
                     ItemStack newBrewStack = brew.createStack();
                     switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
                     case 1:
                        itemEntity = new EntityItem(super.worldObj, (double)mop.blockX + 0.5D, (double)(mop.blockY + (mop.sideHit == 0?-1:1)) + 0.5D, (double)mop.blockZ + 0.5D, newBrewStack);
                        break;
                     case 2:
                        itemEntity = new EntityItem(super.worldObj, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, newBrewStack);
                     }
                  }

                  this.skipFX = true;
                  if(itemEntity != null) {
                     super.worldObj.spawnEntityInWorld(itemEntity);
                  }
               } else {
                  this.skipFX = result == ItemGeneral.Brew.BrewResult.HIDE_EFFECT;
               }
            }
         }

         if(!this.skipFX) {
            super.worldObj.playAuxSFX(2002, (int)Math.round(super.posX), (int)Math.round(super.posY), (int)Math.round(super.posZ), 2);
         }
      }

      this.setDead();
   }

   private void impactBats(MovingObjectPosition mop, boolean enhanced) {
      switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
      case 1:
         this.explodeBats(super.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, enhanced);
         break;
      case 2:
         int RADIUS = MathHelper.floor_double(mop.entityHit.posX);
         int y = MathHelper.floor_double(mop.entityHit.posY);
         int axisalignedbb = MathHelper.floor_double(mop.entityHit.posZ);
         this.explodeBats(super.worldObj, RADIUS, y, axisalignedbb, -1, enhanced);
      }

      double RADIUS1 = enhanced?4.0D:3.0D;
      AxisAlignedBB axisalignedbb1 = super.boundingBox.expand(RADIUS1, 2.0D, RADIUS1);
      List list1 = super.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb1);
      if(list1 != null && !list1.isEmpty()) {
         Iterator iterator = list1.iterator();

         while(iterator.hasNext()) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)iterator.next();
            double d0 = entitylivingbase.getDistanceSq(super.posX, super.posY, super.posZ);
            if(d0 < RADIUS1 * RADIUS1) {
               double d1 = 1.0D - Math.sqrt(d0) / RADIUS1;
               if(entitylivingbase == mop.entityHit) {
                  d1 = 1.0D;
               }

               int j = (int)(d1 * 100.0D + 0.5D);
               entitylivingbase.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, j, 5));
               if(entitylivingbase instanceof EntityLiving) {
                  EntityUtil.dropAttackTarget((EntityLiving)entitylivingbase);
               }
            }
         }
      }

   }

   private void explodeBats(World world, int posX, int posY, int posZ, int side, boolean enhanced) {
      int x = posX + (side == 4?-1:(side == 5?1:0));
      int z = posZ + (side == 2?-1:(side == 3?1:0));
      int y = posY + (side == 0?-1:(side == 1?1:0));
      if(side == 1 && !world.getBlock(x, posY, z).getMaterial().isSolid()) {
         --y;
      }

      int NUM_BATS = enhanced?14:10;

      for(int i = 0; i < NUM_BATS; ++i) {
         EntityBat bat = new EntityBat(world);
         NBTTagCompound nbtBat = bat.getEntityData();
         nbtBat.setBoolean("WITCNoDrops", true);
         bat.setLocationAndAngles((double)x, (double)y, (double)z, 0.0F, 0.0F);
         super.worldObj.spawnEntityInWorld(bat);
      }

      ParticleEffect.LARGE_EXPLODE.send(SoundEffect.MOB_ENDERMEN_PORTAL, world, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z, 3.0D, 3.0D, 16);
   }

   private void impactInfection(MovingObjectPosition mop, boolean enhanced) {
      if(mop.typeOfHit == MovingObjectType.BLOCK) {
         Block itemEntity = super.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
         int newBrewStack = super.worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
         if((itemEntity == Blocks.stone || itemEntity == Blocks.cobblestone || itemEntity == Blocks.stonebrick && newBrewStack == 0) && BlockProtect.canBreak(mop.blockX, mop.blockZ, mop.blockY, super.worldObj)) {
            if(itemEntity == Blocks.stone) {
               super.worldObj.setBlock(mop.blockX, mop.blockY, mop.blockZ, Blocks.monster_egg, 0, 3);
            } else if(itemEntity == Blocks.cobblestone) {
               super.worldObj.setBlock(mop.blockX, mop.blockY, mop.blockZ, Blocks.monster_egg, 1, 3);
            } else if(itemEntity == Blocks.stonebrick) {
               super.worldObj.setBlock(mop.blockX, mop.blockY, mop.blockZ, Blocks.monster_egg, 2, 3);
            }

            return;
         }
      } else if(mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
         EntityLivingBase itemEntity2 = (EntityLivingBase)mop.entityHit;
         if(itemEntity2 instanceof EntityVillager) {
            EntityZombie newBrewStack2 = new EntityZombie(super.worldObj);
            newBrewStack2.copyLocationAndAnglesFrom(itemEntity2);
            super.worldObj.removeEntity(itemEntity2);
            newBrewStack2.onSpawnWithEgg((IEntityLivingData)null);
            newBrewStack2.setVillager(true);
            if(itemEntity2.isChild()) {
               newBrewStack2.setChild(true);
            }

            super.worldObj.spawnEntityInWorld(newBrewStack2);
            super.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1016, (int)newBrewStack2.posX, (int)newBrewStack2.posY, (int)newBrewStack2.posZ, 0);
         } else {
            float newBrewStack3 = enhanced?4.0F:1.0F;
            itemEntity2.attackEntityFrom(DamageSource.causeThrownDamage(itemEntity2, this.getThrower()), newBrewStack3);
            itemEntity2.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 100, 8));
         }

         return;
      }

      EntityItem itemEntity1 = null;
      if(mop != null) {
         ItemStack newBrewStack1 = Witchery.Items.GENERIC.itemBrewOfInfection.createStack();
         switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
         case 1:
            itemEntity1 = new EntityItem(super.worldObj, (double)mop.blockX + 0.5D, (double)(mop.blockY + (mop.sideHit == 0?-1:1)) + 0.5D, (double)mop.blockZ + 0.5D, newBrewStack1);
            break;
         case 2:
            itemEntity1 = new EntityItem(super.worldObj, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, newBrewStack1);
         }
      }

      this.skipFX = true;
      if(itemEntity1 != null) {
         super.worldObj.spawnEntityInWorld(itemEntity1);
      }

   }

   private void impactHitchcock(MovingObjectPosition mop) {
      if(mop.typeOfHit == MovingObjectType.ENTITY && mop.entityHit instanceof EntityLivingBase) {
         EntityLivingBase var7 = (EntityLivingBase)mop.entityHit;
         int var6 = super.worldObj.rand.nextInt(2) + 3;

         for(int i = 0; i < var6; ++i) {
            EntityOwl owl = new EntityOwl(super.worldObj);
            owl.setLocationAndAngles(var7.posX - 2.0D + (double)super.worldObj.rand.nextInt(5), var7.posY + (double)var7.height + 1.0D + (double)super.worldObj.rand.nextInt(2), var7.posZ - 2.0D + (double)super.worldObj.rand.nextInt(5), 0.0F, 0.0F);
            owl.setAttackTarget(var7);
            owl.setTimeToLive(400);
            super.worldObj.spawnEntityInWorld(owl);
            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, owl, 1.0D, 1.0D, 16);
         }
      } else {
         EntityItem itemEntity = null;
         if(mop != null) {
            ItemStack newBrewStack = Witchery.Items.GENERIC.itemBrewOfHitchcock.createStack();
            switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
            case 1:
               itemEntity = new EntityItem(super.worldObj, (double)mop.blockX + 0.5D, (double)(mop.blockY + (mop.sideHit == 0?-1:1)) + 0.5D, (double)mop.blockZ + 0.5D, newBrewStack);
               break;
            case 2:
               itemEntity = new EntityItem(super.worldObj, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, newBrewStack);
            }
         }

         this.skipFX = true;
         if(itemEntity != null) {
            super.worldObj.spawnEntityInWorld(itemEntity);
         }
      }

   }

   private void impactLeaping(MovingObjectPosition mop, boolean enhanced) {
      Entity livingEntity = mop.entityHit;
      double RADIUS = enhanced?6.0D:5.0D;
      AxisAlignedBB axisalignedbb = super.boundingBox.expand(RADIUS, 2.0D, RADIUS);
      List list1 = super.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
      if(list1 != null && !list1.isEmpty()) {
         Iterator iterator = list1.iterator();

         while(iterator.hasNext()) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)iterator.next();
            double d0 = entitylivingbase.getDistanceSq(super.posX, super.posY, super.posZ);
            if(d0 < RADIUS * RADIUS) {
               double d1 = 1.0D - 0.5D * (Math.sqrt(d0) / RADIUS / 2.0D);
               if(entitylivingbase == livingEntity) {
                  d1 = 1.0D;
               }

               int j = (int)(d1 * 400.0D + 0.5D);
               double LEAP = d1 * 1.6D;
               entitylivingbase.addPotionEffect(new PotionEffect(Potion.jump.id, 200, 3));
               if(entitylivingbase instanceof EntityPlayer) {
                  Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(entitylivingbase.motionX, entitylivingbase.motionY + LEAP, entitylivingbase.motionZ)), (EntityPlayer)entitylivingbase);
               } else {
                  entitylivingbase.motionY += LEAP;
               }
            }
         }
      }

   }

   private void impactFrogsTongue(MovingObjectPosition mop, boolean enhanced) {
      if(!super.worldObj.isRemote && this.getThrower() != null) {
         double RADIUS = enhanced?5.0D:4.0D;
         double RADIUS_SQ = RADIUS * RADIUS;
         EntityLivingBase thrower = this.getThrower();
         boolean pulled = false;
         AxisAlignedBB axisalignedbb = super.boundingBox.expand(RADIUS, 2.0D, RADIUS);
         List entityLivingList = super.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
         if(entityLivingList != null && !entityLivingList.isEmpty()) {
            Iterator iterator = entityLivingList.iterator();

            while(iterator.hasNext()) {
               EntityLivingBase livingEntity = (EntityLivingBase)iterator.next();
               double distanceSq = livingEntity.getDistanceSq(super.posX, super.posY, super.posZ);
               if(distanceSq < RADIUS_SQ && livingEntity != this.getThrower()) {
                  this.pull(super.worldObj, livingEntity, thrower.posX, thrower.posY, thrower.posZ, 0.05D, 0.0D);
               }
            }
         }
      }

   }

   private void pull(World world, Entity entity, double posX, double posY, double posZ, double dy, double yy) {
      if(!(entity instanceof EntityDragon) && !(entity instanceof EntityHornedHuntsman)) {
         double d = posX - entity.posX;
         double d1 = posY - entity.posY;
         double d2 = posZ - entity.posZ;
         float distance = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
         float f2 = 0.1F + (float)dy;
         double mx = d / (double)distance * (double)f2 * (double)distance;
         double my = yy == 0.0D?0.4D:d1 / (double)distance * (double)distance * 0.2D + 0.2D + yy;
         double mz = d2 / (double)distance * (double)f2 * (double)distance;
         if(entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(Potion.jump.id, 20, 1));
         }

         if(entity instanceof EntityPlayer) {
            Witchery.packetPipeline.sendTo((IMessage)(new PacketPushTarget(mx, my, mz)), (EntityPlayer)entity);
         } else {
            entity.motionX = mx;
            entity.motionY = my;
            entity.motionZ = mz;
         }

      }
   }

   private void impactIce(MovingObjectPosition mop) {
      int x;
      int y;
      int z;
      switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
      case 1:
         boolean FREEZE_RANGE = true;
         if(super.worldObj.getBlock(mop.blockX + 1, mop.blockY, mop.blockZ).getMaterial() == Material.water || super.worldObj.getBlock(mop.blockX - 1, mop.blockY, mop.blockZ).getMaterial() == Material.water || super.worldObj.getBlock(mop.blockX, mop.blockY + 1, mop.blockZ).getMaterial() == Material.water || super.worldObj.getBlock(mop.blockX, mop.blockY - 1, mop.blockZ).getMaterial() == Material.water || super.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ + 1).getMaterial() == Material.water || super.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ - 1).getMaterial() == Material.water) {
            this.freezeSurroundingWater(super.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.blockX, mop.blockY, mop.blockZ, 3);
            return;
         }

         boolean SHIELD_HEIGHT = true;
         if(mop.sideHit == 1) {
            explodeIceShield(super.worldObj, this.getThrower(), mop.blockX, mop.blockY, mop.blockZ, 3);
            return;
         }

         if(mop.sideHit != 0) {
            boolean itemEntity1 = false;
            switch(mop.sideHit) {
            case 0:
            case 1:
               itemEntity1 = false;
               break;
            case 2:
            case 3:
               itemEntity1 = true;
               break;
            case 4:
            case 5:
               itemEntity1 = true;
            }

            x = mop.sideHit == 5?1:(mop.sideHit == 4?-1:0);
            y = mop.sideHit == 0?-1:(mop.sideHit == 1?1:0);
            z = mop.sideHit == 3?1:(mop.sideHit == 2?-1:0);
            explodeIceShield(super.worldObj, this.getThrower(), mop.blockX + x, mop.blockY + y, mop.blockZ + z, 3);
            return;
         }

         EntityItem itemEntity = null;
         if(mop != null) {
            ItemStack x1 = Witchery.Items.GENERIC.itemBrewOfIce.createStack();
            switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
            case 1:
               itemEntity = new EntityItem(super.worldObj, (double)mop.blockX + 0.5D, (double)(mop.blockY + (mop.sideHit == 0?-1:1)) + 0.5D, (double)mop.blockZ + 0.5D, x1);
               break;
            case 2:
               itemEntity = new EntityItem(super.worldObj, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, x1);
            }
         }

         this.skipFX = true;
         if(itemEntity != null) {
            super.worldObj.spawnEntityInWorld(itemEntity);
         }
         break;
      case 2:
         x = (int)Math.round(mop.entityHit.posX);
         y = MathHelper.floor_double(mop.entityHit.posY);
         z = (int)Math.round(mop.entityHit.posZ);
         explodeIceBlock(super.worldObj, x, y, z, -1, mop.entityHit);
      }

   }

   private void freezeSurroundingWater(World world, int x, int y, int z, int x0, int y0, int z0, int range) {
      if(Math.abs(x0 - x) < range && Math.abs(y0 - y) < range && Math.abs(z0 - z) < range) {
         if(this.freezeWater(world, x + 1, y, z)) {
            this.freezeSurroundingWater(world, x + 1, y, z, x0, y0, z0, range);
         }

         if(this.freezeWater(world, x - 1, y, z)) {
            this.freezeSurroundingWater(world, x - 1, y, z, x0, y0, z0, range);
         }

         if(this.freezeWater(world, x, y, z + 1)) {
            this.freezeSurroundingWater(world, x, y, z + 1, x0, y0, z0, range);
         }

         if(this.freezeWater(world, x, y, z - 1)) {
            this.freezeSurroundingWater(world, x, y, z - 1, x0, y0, z0, range);
         }

         if(this.freezeWater(world, x, y + 1, z)) {
            this.freezeSurroundingWater(world, x, y + 1, z, x0, y0, z0, range);
         }

         if(this.freezeWater(world, x, y - 1, z)) {
            this.freezeSurroundingWater(world, x, y - 1, z, x0, y0, z0, range);
         }

      }
   }

   private boolean freezeWater(World world, int x, int y, int z) {
      if(world.getBlock(x, y, z).getMaterial() == Material.water) {
         world.setBlock(x, y, z, Blocks.ice);
         return true;
      } else {
         return false;
      }
   }

   public static void explodeIceBlock(World world, int posX, int posY, int posZ, int side, Entity entity) {
      int x = posX + (side == 4?-1:(side == 5?1:0));
      int z = posZ + (side == 2?-1:(side == 3?1:0));
      int y = posY + (side == 0?-1:(side == 1?1:0)) - 1;
      if(side == 1 && !world.getBlock(x, posY, z).getMaterial().isSolid()) {
         --y;
      }

      Block block = Blocks.ice;
      boolean resistent = entity instanceof EntityDemon || entity instanceof EntityBlaze || entity instanceof EntityDragon || entity instanceof EntityHornedHuntsman || entity instanceof EntityEnt || entity instanceof EntityWither || entity instanceof EntityIronGolem;
      if(resistent) {
         setBlockIfNotSolid(world, x, y + 1, z, Blocks.flowing_water);
      } else {
         int HEIGHT = resistent?2:4;

         for(int creeper = 0; creeper < HEIGHT; ++creeper) {
            setBlockIfNotSolid(world, x - 2, y + creeper, z - 1, block);
            setBlockIfNotSolid(world, x - 2, y + creeper, z, block);
            setBlockIfNotSolid(world, x - 1, y + creeper, z + 1, block);
            setBlockIfNotSolid(world, x, y + creeper, z + 1, block);
            setBlockIfNotSolid(world, x + 1, y + creeper, z, block);
            setBlockIfNotSolid(world, x + 1, y + creeper, z - 1, block);
            setBlockIfNotSolid(world, x, y + creeper, z - 2, block);
            setBlockIfNotSolid(world, x - 1, y + creeper, z - 2, block);
            setBlockIfNotSolid(world, x - 2, y + creeper, z - 2, block);
            setBlockIfNotSolid(world, x - 2, y + creeper, z + 1, block);
            setBlockIfNotSolid(world, x + 1, y + creeper, z + 1, block);
            setBlockIfNotSolid(world, x + 1, y + creeper, z - 2, block);
         }

         setBlockIfNotSolid(world, x, y, z, block);
         if(!resistent) {
            setBlockIfNotSolid(world, x, y + HEIGHT - 1, z, block);
            setBlockIfNotSolid(world, x - 1, y + HEIGHT - 1, z - 1, block);
            setBlockIfNotSolid(world, x - 1, y + HEIGHT - 1, z, block);
            setBlockIfNotSolid(world, x, y + HEIGHT - 1, z - 1, block);
         }

         if(entity instanceof EntityCreeper) {
            EntityCreeper var14 = (EntityCreeper)entity;
            boolean flag = world.getGameRules().getGameRuleBooleanValue("mobGriefing");
            if(var14.getPowered()) {
               world.createExplosion(var14, var14.posX, var14.posY, var14.posZ, 6.0F, flag);
            } else {
               world.createExplosion(var14, var14.posX, var14.posY, var14.posZ, 3.0F, flag);
            }

            var14.setDead();
         }
      }

   }

   public static void explodeIceShield(World world, EntityLivingBase player, int posX, int posY, int posZ, int height) {
      double f1 = player != null?(double)MathHelper.cos(-player.rotationYaw * 0.017453292F - 3.1415927F):0.0D;
      double f2 = player != null?(double)MathHelper.sin(-player.rotationYaw * 0.017453292F - 3.1415927F):0.0D;
      Vec3 loc = Vec3.createVectorHelper(f2, 0.0D, f1);
      if(!world.getBlock(posX, posY, posZ).getMaterial().isSolid()) {
         --posY;
      }

      explodeIceColumn(world, posX, posY + 1, posZ, height);
      loc.rotateAroundY((float)Math.toRadians(90.0D));
      int newX = MathHelper.floor_double((double)posX + 0.5D + loc.xCoord * 1.0D);
      int newZ = MathHelper.floor_double((double)posZ + 0.5D + loc.zCoord * 1.0D);
      explodeIceColumn(world, newX, posY + 1, newZ, height);
      loc.rotateAroundY((float)Math.toRadians(180.0D));
      newX = MathHelper.floor_double((double)posX + 0.5D + loc.xCoord * 1.0D);
      newZ = MathHelper.floor_double((double)posZ + 0.5D + loc.zCoord * 1.0D);
      explodeIceColumn(world, newX, posY + 1, newZ, height);
   }

   public static void explodeIceColumn(World world, int posX, int posY, int posZ, int height) {
      for(int offsetPosY = posY; offsetPosY < posY + height; ++offsetPosY) {
         setBlockIfNotSolid(world, posX, offsetPosY, posZ, Blocks.ice);
      }

   }

   private void impactLove(MovingObjectPosition mop, boolean enhanced) {
      double RADIUS = enhanced?5.0D:4.0D;
      AxisAlignedBB axisalignedbb = super.boundingBox.expand(RADIUS, 2.0D, RADIUS);
      List list1 = super.worldObj.getEntitiesWithinAABB(EntityLiving.class, axisalignedbb);
      if(list1 != null && !list1.isEmpty() && !super.worldObj.isRemote) {
         EntityLivingBase entityThrower = this.getThrower();
         EntityPlayer thrower = entityThrower != null && entityThrower instanceof EntityPlayer?(EntityPlayer)entityThrower:null;
         Iterator iterator = list1.iterator();
         ArrayList villagers = new ArrayList();
         ArrayList zombies = new ArrayList();

         while(iterator.hasNext()) {
            EntityLiving limit = (EntityLiving)iterator.next();
            double zombie = limit.getDistanceSq(super.posX, super.posY, super.posZ);
            if(zombie < RADIUS * RADIUS) {
               double baby = 1.0D - Math.sqrt(zombie) / RADIUS;
               if(limit == mop.entityHit) {
                  baby = 1.0D;
               }

               int j = (int)(baby * 400.0D + 0.5D);
               if(limit instanceof EntityAnimal) {
                  EntityAnimal zombie1 = (EntityAnimal)limit;
                  if(zombie1.getGrowingAge() >= 0) {
                     zombie1.setGrowingAge(0);
                     zombie1.func_146082_f((EntityPlayer)null);
                  }
               } else if(limit instanceof EntityVillager) {
                  EntityVillager var26 = (EntityVillager)limit;
                  if(var26.getGrowingAge() >= 0) {
                     villagers.add(var26);
                  }
               } else if(limit instanceof EntityZombie) {
                  EntityZombie var25 = (EntityZombie)limit;
                  if(!var25.isChild() && thrower != null) {
                     NBTTagCompound nbt = var25.getEntityData();
                     if(PotionEnslaved.isMobEnslavedBy(var25, thrower)) {
                        zombies.add(var25);
                     }
                  }
               }
            }
         }

         int var20 = 10;

         while(villagers.size() > 1 && var20-- > 0) {
            EntityVillager var22 = (EntityVillager)villagers.get(0);
            EntityVillager mate = (EntityVillager)villagers.get(1);
            var22.setPosition(mate.posX, mate.posY, mate.posZ);
            ParticleEffect.HEART.send(SoundEffect.NONE, mate, 1.0D, 2.0D, 8);
            this.giveBirth(var22, mate);
            villagers.remove(0);
            villagers.remove(0);
         }

         var20 = 10;

         while(zombies.size() > 1 && var20-- > 0) {
            EntityZombie var21 = (EntityZombie)zombies.get(0);
            EntityZombie var23 = (EntityZombie)zombies.get(1);
            var21.setPosition(var23.posX, var23.posY, var23.posZ);
            ParticleEffect.HEART.send(SoundEffect.NONE, var23, 1.0D, 2.0D, 8);
            var21.setVillager(true);
            var23.setVillager(true);
            EntityZombie var24 = new EntityZombie(super.worldObj);
            var24.setLocationAndAngles(var23.posX, var23.posY, var23.posZ, 0.0F, 0.0F);
            var24.setChild(true);
            super.worldObj.spawnEntityInWorld(var24);
            zombies.remove(0);
            zombies.remove(0);
         }
      }

   }

   private void giveBirth(EntityVillager villagerObj, EntityVillager mate) {
      EntityVillager entityvillager = villagerObj.createChild(mate);
      mate.setGrowingAge(6000);
      villagerObj.setGrowingAge(6000);
      entityvillager.setGrowingAge(-24000);
      entityvillager.setLocationAndAngles(villagerObj.posX, villagerObj.posY, villagerObj.posZ, 0.0F, 0.0F);
      super.worldObj.spawnEntityInWorld(entityvillager);
      super.worldObj.setEntityState(entityvillager, (byte)12);
   }

   private void impactQuicklime(MovingObjectPosition mop) {
      if(mop.typeOfHit == MovingObjectType.ENTITY) {
         if(mop.entityHit instanceof EntityLivingBase) {
            EntityLivingBase itemEntity1 = (EntityLivingBase)mop.entityHit;
            if(!itemEntity1.isPotionActive(Potion.blindness)) {
               itemEntity1.addPotionEffect(new PotionEffect(Potion.blindness.id, 60, 0));
            }

            float newBrewStack1 = mop.entityHit instanceof EntitySlime?4.0F:(itemEntity1.getHealth() == itemEntity1.getMaxHealth()?0.5F:0.1F);
            itemEntity1.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), newBrewStack1);
         }

         this.skipFX = true;
      } else {
         EntityItem itemEntity = null;
         if(mop != null) {
            ItemStack newBrewStack = Witchery.Items.GENERIC.itemQuicklime.createStack();
            switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
            case 1:
               itemEntity = new EntityItem(super.worldObj, (double)mop.blockX + 0.5D, (double)(mop.blockY + (mop.sideHit == 0?-1:1)) + 0.5D, (double)mop.blockZ + 0.5D, newBrewStack);
               break;
            case 2:
               itemEntity = new EntityItem(super.worldObj, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, newBrewStack);
            }
         }

         this.skipFX = true;
         if(itemEntity != null) {
            super.worldObj.spawnEntityInWorld(itemEntity);
         }

      }
   }

   private void impactRaising(MovingObjectPosition mop) {
      if(mop.typeOfHit == MovingObjectType.BLOCK && mop.sideHit == 1) {
         int itemEntity1 = mop.blockX;
         int newBrewStack1 = mop.blockY;
         int posZ = mop.blockZ;
         World world = super.worldObj;
         raiseDead(itemEntity1, newBrewStack1, posZ, world, this.getThrower());
      } else {
         EntityItem itemEntity = null;
         if(mop != null) {
            ItemStack newBrewStack = Witchery.Items.GENERIC.itemBrewOfRaising.createStack();
            switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
            case 1:
               itemEntity = new EntityItem(super.worldObj, (double)mop.blockX + 0.5D, (double)(mop.blockY + (mop.sideHit == 0?-1:1)) + 0.5D, (double)mop.blockZ + 0.5D, newBrewStack);
               break;
            case 2:
               itemEntity = new EntityItem(super.worldObj, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, newBrewStack);
            }
         }

         this.skipFX = true;
         if(itemEntity != null) {
            super.worldObj.spawnEntityInWorld(itemEntity);
         }

      }
   }

   public static void raiseDead(int posX, int posY, int posZ, World world, EntityLivingBase raiser) {
      int y0 = world.getBlock(posX, posY, posZ).getMaterial().isSolid()?posY:posY - 1;
      boolean MAX_SPAWNS = true;
      boolean MAX_DISTANCE = true;
      boolean MAX_DROP = true;
      EntityPlayer playerThrower = (EntityPlayer)((EntityPlayer)(raiser instanceof EntityPlayer?raiser:null));
      raiseUndead(world, posX, y0, posZ, playerThrower);
      byte extraCount = 0;
      double chance = world.rand.nextDouble();
      if(chance < 0.1D) {
         extraCount = 2;
      } else if(chance < 0.4D) {
         extraCount = 1;
      }

      int i = 0;

      while(i < extraCount) {
         int x = posX - 3 + world.rand.nextInt(6) + 1;
         int z = posZ - 3 + world.rand.nextInt(6) + 1;
         int y = -1;
         int dy = -6;

         while(true) {
            if(dy < 6) {
               if(!world.getBlock(x, y0 - dy, z).getMaterial().isSolid()) {
                  ++dy;
                  continue;
               }

               y = y0 - dy;
            }

            if(y != -1) {
               raiseUndead(world, x, y, z, playerThrower);
            }

            ++i;
            break;
         }
      }

   }

   private static void raiseUndead(World world, int posX, int posY, int posZ, EntityPlayer thrower) {
      if(!world.isRemote) {
         Block blockID = world.getBlock(posX, posY, posZ);
         if(blockID != Blocks.dirt && blockID != Blocks.stone && blockID != Blocks.grass && blockID != Blocks.netherrack && blockID != Blocks.mycelium && blockID != Blocks.soul_sand && blockID != Blocks.cobblestone && blockID != Blocks.gravel && blockID != Blocks.sand) {
            ++posY;
         }

         spawnParticles(world, ParticleEffect.SMOKE, 0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ);
         world.setBlockToAir(posX, posY, posZ);
         world.setBlockToAir(posX, posY + 1, posZ);
         EntityLiving undeadEntity = createUndeadCreature(world);
         undeadEntity.setLocationAndAngles(0.5D + (double)posX, 0.5D + (double)posY, 0.5D + (double)posZ, 1.0F, 0.0F);
         Object entitylivingData = null;
         undeadEntity.onSpawnWithEgg((IEntityLivingData)entitylivingData);
         undeadEntity.func_110163_bv();
         if(thrower != null) {
            try {
               PotionEnslaved.setEnslaverForMob(undeadEntity, thrower);
            } catch (Exception var9) {
               Log.instance().warning(var9, "Unhandled exception occurred setting enslaver from raiseUnded potion.");
            }
         }

         world.spawnEntityInWorld(undeadEntity);
      }

   }

   private static EntityLiving createUndeadCreature(World world) {
      double value = world.rand.nextDouble();
      return (EntityLiving)(value < 0.6D?new EntityZombie(world):(value < 0.97D?new EntitySkeleton(world):new EntityPigZombie(world)));
   }

   private void impactErosion(MovingObjectPosition mop, boolean enhanced) {
      int slot;
      int slot1;
      if(mop.typeOfHit == MovingObjectType.BLOCK) {
         if(BlockProtect.checkModsForBreakOK(super.worldObj, mop.blockX, mop.blockZ, mop.blockY, this.getThrower())) {
            boolean entity = true;
            byte ACID_DAMAGE = 0;
            int var8 = ACID_DAMAGE + this.drawFilledCircle(super.worldObj, mop.blockX, mop.blockZ, mop.blockY, 2);

            for(slot = 0; slot < 2; ++slot) {
               slot1 = slot + 1;
               var8 += this.drawFilledCircle(super.worldObj, mop.blockX, mop.blockZ, mop.blockY + slot1, 2 - slot1);
               var8 += this.drawFilledCircle(super.worldObj, mop.blockX, mop.blockZ, mop.blockY - slot1, 2 - slot1);
            }

            if(var8 > 0) {
               super.worldObj.spawnEntityInWorld(new EntityItem(super.worldObj, 0.5D + (double)mop.blockX, 0.5D + (double)mop.blockY, 0.5D + (double)mop.blockZ, new ItemStack(Blocks.obsidian, var8, 0)));
            }
         }
      } else if(mop.typeOfHit == MovingObjectType.ENTITY) {
         if(mop.entityHit instanceof EntityLivingBase) {
            EntityLivingBase var7 = (EntityLivingBase)mop.entityHit;
            float var9 = enhanced?10.0F:8.0F;
            var7.attackEntityFrom(DamageSource.causeThrownDamage(var7, this.getThrower()), var9);
            if(var7 instanceof EntityPlayer) {
               EntityPlayer var10 = (EntityPlayer)var7;
               if(this.causeAcidDamage(var7.getHeldItem())) {
                  var10.destroyCurrentEquippedItem();
               }

               for(slot1 = 0; slot1 < var10.inventory.armorInventory.length; ++slot1) {
                  if(this.causeAcidDamage(var10.inventory.armorInventory[slot1])) {
                     var10.inventory.armorInventory[slot1] = null;
                  }
               }
            } else {
               for(slot = 0; slot < 5; ++slot) {
                  if(this.causeAcidDamage(var7.getEquipmentInSlot(slot))) {
                     var7.setCurrentItemOrArmor(slot, (ItemStack)null);
                  }
               }
            }
         } else {
            this.skipFX = true;
            super.worldObj.spawnEntityInWorld(new EntityItem(super.worldObj, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, Witchery.Items.GENERIC.itemBrewOfErosion.createStack()));
         }
      }

   }

   private boolean causeAcidDamage(ItemStack itemstack) {
      boolean ITEM_ACID_DAMAGE = true;
      if(itemstack != null && itemstack.isItemStackDamageable() && EarthItems.instance().isMatch(itemstack)) {
         itemstack.damageItem(100, this.getThrower());
         return itemstack.getItemDamage() <= 0;
      } else {
         return false;
      }
   }

   protected int drawFilledCircle(World world, int x0, int z0, int y, int radius) {
      int x = radius;
      int z = 0;
      int radiusError = 1 - radius;
      int obsidianMelted = 0;

      while(x >= z) {
         obsidianMelted += this.drawLine(world, -x + x0, x + x0, z + z0, y);
         obsidianMelted += this.drawLine(world, -z + x0, z + x0, x + z0, y);
         obsidianMelted += this.drawLine(world, -x + x0, x + x0, -z + z0, y);
         obsidianMelted += this.drawLine(world, -z + x0, z + x0, -x + z0, y);
         ++z;
         if(radiusError < 0) {
            radiusError += 2 * z + 1;
         } else {
            --x;
            radiusError += 2 * (z - x + 1);
         }
      }

      return obsidianMelted;
   }

   protected int drawLine(World world, int x1, int x2, int z, int y) {
      int obsidianMelted = 0;

      for(int x = x1; x <= x2; ++x) {
         Block blockID = world.getBlock(x, y, z);
         if(blockID == Blocks.obsidian) {
            ++obsidianMelted;
         }

         if(blockID != Blocks.air && blockID != Blocks.lava && blockID != Blocks.flowing_lava && blockID != Blocks.fire && blockID != Blocks.flowing_water && blockID != Blocks.water && BlockProtect.canBreak(blockID, world)) {
            world.setBlockToAir(x, y, z);
            spawnParticles(super.worldObj, ParticleEffect.SPLASH, super.posX, super.posY, super.posZ);
         }
      }

      return obsidianMelted;
   }

   private void impactSprout(MovingObjectPosition mop, boolean enhanced) {
      if(mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
         int itemEntity1 = mop.blockX;
         int newBrewStack1 = mop.blockY;
         int posZ = mop.blockZ;
         World world = super.worldObj;
         int sideHit = mop.sideHit;
         growBranch(itemEntity1, newBrewStack1, posZ, world, sideHit, enhanced?20:15, super.boundingBox);
      } else {
         EntityItem itemEntity = null;
         if(mop != null) {
            ItemStack newBrewStack = Witchery.Items.GENERIC.itemBrewOfSprouting.createStack();
            switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
            case 1:
               itemEntity = new EntityItem(super.worldObj, (double)mop.blockX + 0.5D, (double)(mop.blockY + (mop.sideHit == 0?-1:1)) + 0.5D, (double)mop.blockZ + 0.5D, newBrewStack);
               break;
            case 2:
               itemEntity = new EntityItem(super.worldObj, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, newBrewStack);
            }
         }

         this.skipFX = true;
         if(itemEntity != null) {
            super.worldObj.spawnEntityInWorld(itemEntity);
         }
      }

   }

   public static void growBranch(int posX, int posY, int posZ, World world, int sideHit, int extent, AxisAlignedBB boundingBox) {
      Block blockID = world.getBlock(posX, posY, posZ);
      int j1 = world.getBlockMetadata(posX, posY, posZ);
      Block logBlock;
      if(blockID != Blocks.log && blockID != Blocks.planks && blockID != Blocks.sapling && blockID != Blocks.leaves) {
         if(blockID != Witchery.Blocks.LOG && blockID != Witchery.Blocks.PLANKS && blockID != Witchery.Blocks.SAPLING && blockID != Witchery.Blocks.LEAVES) {
            logBlock = world.rand.nextInt(2) == 0?Blocks.log:Witchery.Blocks.LOG;
            j1 = world.rand.nextInt(Blocks.log == logBlock?4:3);
         } else {
            logBlock = Witchery.Blocks.LOG;
         }
      } else {
         logBlock = Blocks.log;
      }

      Object leavesBlock = Blocks.log == logBlock?Blocks.leaves:Witchery.Blocks.LEAVES;
      byte b0 = 0;
      j1 &= 3;
      switch(sideHit) {
      case 0:
      case 1:
         b0 = 0;
         break;
      case 2:
      case 3:
         b0 = 8;
         break;
      case 4:
      case 5:
         b0 = 4;
      }

      int meta = j1 | b0;
      ParticleEffect particleEffect = ParticleEffect.EXPLODE;
      int dx = sideHit == 5?1:(sideHit == 4?-1:0);
      int dy = sideHit == 0?-1:(sideHit == 1?1:0);
      int dz = sideHit == 3?1:(sideHit == 2?-1:0);
      int sproutExtent = extent;
      boolean isInitialBlockSolid = world.getBlock(posX, posY, posZ).getMaterial().isSolid();

      int i;
      int x;
      int z;
      int y;
      for(i = sideHit == 1 && !isInitialBlockSolid?0:1; i < sproutExtent; ++i) {
         int axisalignedbb = posX + i * dx;
         int list1 = posY + i * dy;
         int iterator = posZ + i * dz;
         if(list1 >= 255 || !setBlockIfNotSolid(world, axisalignedbb, list1, iterator, logBlock, meta)) {
            break;
         }

         x = dx == 0 && world.rand.nextInt(4) == 0?world.rand.nextInt(3) - 1:0;
         y = dy == 0 && x == 0 && world.rand.nextInt(4) == 0?world.rand.nextInt(3) - 1:0;
         z = dz == 0 && x == 0 && y == 0 && world.rand.nextInt(4) == 0?world.rand.nextInt(3) - 1:0;
         if(x != 0 || y != 0 || z != 0) {
            setBlockIfNotSolid(world, axisalignedbb + x, list1 + y, iterator + z, (Block)leavesBlock, meta);
         }
      }

      if(sideHit == 1) {
         AxisAlignedBB var27 = boundingBox.expand(0.0D, 2.0D, 0.0D);
         List var29 = world.getEntitiesWithinAABB(EntityLivingBase.class, var27);
         if(var29 != null && !var29.isEmpty()) {
            Iterator var28 = var29.iterator();
            x = posX + i * dx;
            y = Math.min(posY + i * dy, 255);
            z = posZ + i * dz;

            while(var28.hasNext()) {
               EntityLivingBase entitylivingbase = (EntityLivingBase)var28.next();
               if(!world.getBlock(x, y + 1, z).getMaterial().isSolid() && !world.getBlock(x, y + 2, z).getMaterial().isSolid()) {
                  entitylivingbase.setPosition(0.5D + (double)x, (double)(y + 1), 0.5D + (double)z);
               }
            }
         }
      }

   }

   private void impactWasting(MovingObjectPosition mop, boolean enhanced) {
      Entity livingEntity = mop.entityHit;
      double x;
      double y;
      double z;
      if(mop.typeOfHit == MovingObjectType.ENTITY) {
         x = livingEntity.posX;
         y = livingEntity.posY;
         z = livingEntity.posZ;
      } else {
         x = (double)mop.blockX;
         y = (double)mop.blockY;
         z = (double)mop.blockZ;
      }

      explodeWasting(super.worldObj, x, y, z, livingEntity, super.boundingBox, enhanced);
   }

   public static void explodeWasting(World world, double posX, double posY, double posZ, Entity livingEntity, AxisAlignedBB boundingBox, boolean enhanced) {
      double RADIUS = enhanced?5.0D:4.0D;
      AxisAlignedBB axisalignedbb = boundingBox.expand(RADIUS, 2.0D, RADIUS);
      List list1 = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
      int x;
      if(list1 != null && !list1.isEmpty()) {
         Iterator BLOCK_RADIUS = list1.iterator();

         while(BLOCK_RADIUS.hasNext()) {
            EntityLivingBase BLOCK_RADIUS_SQ = (EntityLivingBase)BLOCK_RADIUS.next();
            double blockX = BLOCK_RADIUS_SQ.getDistanceSq(posX, posY, posZ);
            if(blockX < RADIUS * RADIUS) {
               double blockZ = 1.0D - Math.sqrt(blockX) / RADIUS;
               if(BLOCK_RADIUS_SQ == livingEntity) {
                  blockZ = 1.0D;
               }

               x = (int)(blockZ * 400.0D + 0.5D);
               if(BLOCK_RADIUS_SQ instanceof EntityPlayer) {
                  EntityPlayer z = (EntityPlayer)BLOCK_RADIUS_SQ;
                  int material = enhanced?6:10;
                  if(z.getFoodStats().getFoodLevel() > material) {
                     z.getFoodStats().addStats(-material, 0.0F);
                  }

                  z.addPotionEffect(new PotionEffect(Potion.hunger.id, x * 2, enhanced?2:1));
                  z.addPotionEffect(new PotionEffect(Potion.poison.id, Math.max(x / 3, 40), 0));
               } else {
                  BLOCK_RADIUS_SQ.addPotionEffect(new PotionEffect(Potion.wither.id, x * 2, enhanced?1:0));
                  BLOCK_RADIUS_SQ.addPotionEffect(new PotionEffect(Potion.poison.id, Math.max(x / 3, 40), 0));
               }
            }
         }
      }

      int var25 = (int)RADIUS - 1;
      int var24 = var25 * var25;
      int var26 = MathHelper.floor_double(posX);
      int blockY = MathHelper.floor_double(posY);
      int var27 = MathHelper.floor_double(posZ);

      for(int y = blockY - var25; y <= blockY + var25; ++y) {
         for(x = var26 - var25; x <= var26 + var25; ++x) {
            for(int var29 = var27 - var25; var29 <= var27 + var25; ++var29) {
               if(Coord.distanceSq((double)x, (double)y, (double)var29, (double)var26, (double)blockY, (double)var27) <= (double)var24) {
                  Material var28 = world.getBlock(x, y, var29).getMaterial();
                  if(var28 != null && (var28 == Material.leaves || (var28 == Material.plants || var28 == Material.vine) && var28.isReplaceable())) {
                     Block blockID = world.getBlock(x, y, var29);
                     if(!(blockID instanceof BlockCircle) && !(blockID instanceof BlockCircleGlyph)) {
                        blockID.dropBlockAsItem(world, x, y, var29, world.getBlockMetadata(x, y, var29), 0);
                        world.setBlockToAir(x, y, var29);
                     }
                  }
               }
            }
         }
      }

   }

   private void impactInk(MovingObjectPosition mop, boolean enhanced) {
      Entity livingEntity = mop.entityHit;
      explodeInk(super.worldObj, super.posX, super.posY, super.posZ, livingEntity, super.boundingBox, enhanced);
   }

   public static void explodeInk(World world, double posX, double posY, double posZ, Entity livingEntity, AxisAlignedBB boundingBox, boolean enhanced) {
      double RADIUS = enhanced?5.0D:4.0D;
      AxisAlignedBB axisalignedbb = boundingBox.expand(RADIUS, 2.0D, RADIUS);
      List list1 = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
      if(list1 != null && !list1.isEmpty()) {
         Iterator iterator = list1.iterator();

         while(iterator.hasNext()) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)iterator.next();
            double d0 = entitylivingbase.getDistanceSq(posX, posY, posZ);
            if(d0 < RADIUS * RADIUS) {
               double d1 = 1.0D - Math.sqrt(d0) / RADIUS;
               if(entitylivingbase == livingEntity) {
                  d1 = 1.0D;
               }

               int j = (int)(d1 * 400.0D + 0.5D);
               entitylivingbase.addPotionEffect(new PotionEffect(Potion.blindness.id, j, 0));
               if(entitylivingbase instanceof EntityLiving) {
                  EntityUtil.dropAttackTarget((EntityLiving)entitylivingbase);
               }
            }
         }
      }

   }

   private void impactRock(MovingObjectPosition mop) {
      if(mop.entityHit != null) {
         float DAMAGE = 6.0F;
         mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 6.0F);
      }

      spawnParticles(super.worldObj, ParticleEffect.EXPLODE, super.posX, super.posY, super.posZ);
   }

   private static void spawnParticles(World world, ParticleEffect effect, double posX, double posY, double posZ) {
      effect.send(SoundEffect.NONE, world, posX, posY, posZ, 1.0D, 1.0D, 8);
   }

   private void impactWebSmall(MovingObjectPosition mop) {
      switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
      case 1:
         if(super.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ) == Blocks.snow) {
            --mop.blockY;
            mop.sideHit = 1;
         }

         switch(mop.sideHit) {
         case 0:
            setBlockIfNotSolid(super.worldObj, mop.blockX, mop.blockY - 1, mop.blockZ, Blocks.web);
            return;
         case 1:
            setBlockIfNotSolid(super.worldObj, mop.blockX, mop.blockY + 1, mop.blockZ, Blocks.web);
            return;
         case 2:
            setBlockIfNotSolid(super.worldObj, mop.blockX - 1, mop.blockY, mop.blockZ, Blocks.web);
            return;
         case 3:
            setBlockIfNotSolid(super.worldObj, mop.blockX + 1, mop.blockY, mop.blockZ, Blocks.web);
            return;
         case 4:
            setBlockIfNotSolid(super.worldObj, mop.blockX, mop.blockY, mop.blockZ - 1, Blocks.web);
            return;
         case 5:
            setBlockIfNotSolid(super.worldObj, mop.blockX, mop.blockY, mop.blockZ + 1, Blocks.web);
            return;
         default:
            return;
         }
      case 2:
         super.worldObj.setBlock((int)mop.entityHit.posX, (int)mop.entityHit.posY, (int)mop.entityHit.posZ, Blocks.web);
      }

   }

   private void impactWebBig(MovingObjectPosition mop, boolean enhanced) {
      switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
      case 1:
         explodeWeb(super.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, enhanced);
         break;
      case 2:
         int x = MathHelper.floor_double(mop.entityHit.posX);
         int y = MathHelper.floor_double(mop.entityHit.posY);
         int z = MathHelper.floor_double(mop.entityHit.posZ);
         explodeWeb(super.worldObj, x, y, z, -1, enhanced);
      }

   }

   public static void explodeWeb(World world, int posX, int posY, int posZ, int side, boolean enhanced) {
      int x = posX + (side == 4?-1:(side == 5?1:0));
      int z = posZ + (side == 2?-1:(side == 3?1:0));
      int y = posY + (side == 0?-1:(side == 1?1:0));
      if(side == 1 && !world.getBlock(x, posY, z).getMaterial().isSolid()) {
         --y;
      }

      setBlockIfNotSolid(world, x, y, z, Blocks.web);
      setBlockIfNotSolid(world, x + 1, y, z, Blocks.web);
      setBlockIfNotSolid(world, x - 1, y, z, Blocks.web);
      setBlockIfNotSolid(world, x, y, z + 1, Blocks.web);
      setBlockIfNotSolid(world, x, y, z - 1, Blocks.web);
      if(enhanced) {
         setBlockIfNotSolid(world, x + 1, y, z + 1, Blocks.web);
         setBlockIfNotSolid(world, x - 1, y, z - 1, Blocks.web);
         setBlockIfNotSolid(world, x - 1, y, z + 1, Blocks.web);
         setBlockIfNotSolid(world, x + 1, y, z - 1, Blocks.web);
      }

      setBlockIfNotSolid(world, x, y + 1, z, Blocks.web);
      setBlockIfNotSolid(world, x, y - 1, z, Blocks.web);
   }

   private void impactThorns(MovingObjectPosition mop, boolean enhanced) {
      int x;
      int y;
      int z;
      switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
      case 1:
         if(mop.sideHit == 1 || super.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ) == Blocks.cactus) {
            y = mop.blockY;
            x = mop.blockX;
            z = mop.blockZ;
            int newBrewStack = enhanced?4:3;
            if(plantCactus(super.worldObj, x, y, z, newBrewStack)) {
               break;
            }
         }

         ItemStack newBrewStack1 = Witchery.Items.GENERIC.itemBrewOfThorns.createStack();
         x = mop.blockX + (mop.sideHit == 4?-1:(mop.sideHit == 5?1:0));
         z = mop.blockZ + (mop.sideHit == 2?-1:(mop.sideHit == 3?1:0));
         y = mop.blockY + (mop.sideHit == 0?-1:(mop.sideHit == 1?1:0));
         this.skipFX = true;
         super.worldObj.spawnEntityInWorld(new EntityItem(super.worldObj, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, newBrewStack1));
         break;
      case 2:
         int CACTUS_HEIGHT = enhanced?2:1;
         x = MathHelper.floor_double(mop.entityHit.posX);
         y = MathHelper.floor_double(mop.entityHit.posY);
         z = MathHelper.floor_double(mop.entityHit.posZ);
         boolean success = plantCactus(super.worldObj, x + 1, y, z, CACTUS_HEIGHT);
         success = success && plantCactus(super.worldObj, x - 1, y, z, CACTUS_HEIGHT);
         success = success && plantCactus(super.worldObj, x, y, z + 1, CACTUS_HEIGHT);
         success = success && plantCactus(super.worldObj, x, y, z - 1, CACTUS_HEIGHT);
         if(!success) {
            this.skipFX = true;
            super.worldObj.spawnEntityInWorld(new EntityItem(super.worldObj, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, Witchery.Items.GENERIC.itemBrewOfThorns.createStack()));
         }
      }

   }

   public static boolean plantCactus(World world, int x, int y, int z, int CACTUS_HEIGHT) {
      if(!world.getBlock(x, y, z).getMaterial().isSolid()) {
         --y;
      }

      Material material = world.getBlock(x, y, z).getMaterial();
      if(material != Material.clay && material != Material.craftedSnow && material != Material.grass && material != Material.ground && material != Material.rock && material != Material.sand && material != Material.snow && material != Material.sponge && material != Material.cactus) {
         return false;
      } else {
         Block i = world.getBlock(x, y, z);
         if(!BlockProtect.canBreak(i, world)) {
            return false;
         } else {
            if(material != Material.cactus) {
               world.setBlock(x, y, z, Blocks.sand);
            } else {
               while(world.getBlock(x, y, z) == Blocks.cactus) {
                  ++y;
               }

               --y;
            }

            for(int var7 = 1; var7 <= CACTUS_HEIGHT && y + var7 < 256 && setBlockIfNotSolid(world, x, y + var7, z, Blocks.cactus); ++var7) {
               ;
            }

            return true;
         }
      }
   }

   private void impactVines(MovingObjectPosition mop, boolean enhanced) {
      if(mop.typeOfHit == MovingObjectType.BLOCK && mop.sideHit != 0 && mop.sideHit != 1) {
         int var11 = mop.sideHit == 4?-1:(mop.sideHit == 5?1:0);
         int var12 = mop.sideHit == 2?-1:(mop.sideHit == 3?1:0);
         int y0 = mop.blockY;
         byte meta = 0;
         switch(mop.sideHit) {
         case 2:
            meta = 1;
            break;
         case 3:
            meta = 4;
            break;
         case 4:
            meta = 8;
            break;
         case 5:
            meta = 2;
         }

         ParticleEffect EFFECT = ParticleEffect.EXPLODE;
         int y = y0;
         int x = mop.blockX;
         int z = mop.blockZ;
         if(!this.isNotSolidOrLeaves(super.worldObj.getBlock(x + var11, y0, z + var12).getMaterial()) || !super.worldObj.getBlock(x, y0, z).getMaterial().isSolid()) {
            x += var11;
            z += var12;
         }

         while(this.isNotSolidOrLeaves(super.worldObj.getBlock(x + var11, y, z + var12).getMaterial()) && super.worldObj.getBlock(x, y, z).getMaterial().isSolid() && y > 0) {
            super.worldObj.setBlock(x + var11, y, z + var12, Blocks.vine, meta, 3);
            spawnParticles(super.worldObj, EFFECT, 0.5D + (double)x + (double)var11, 0.5D + (double)y, 0.5D + (double)z + (double)var12);
            --y;
            if(!this.isNotSolidOrLeaves(super.worldObj.getBlock(x + var11, y, z + var12).getMaterial()) || !super.worldObj.getBlock(x, y, z).getMaterial().isSolid()) {
               x += var11;
               z += var12;
               if(enhanced && (!this.isNotSolidOrLeaves(super.worldObj.getBlock(x + var11, y, z + var12).getMaterial()) || !super.worldObj.getBlock(x, y, z).getMaterial().isSolid())) {
                  x += var11;
                  z += var12;
               }
            }
         }

         y = y0 + 1;
         x = mop.blockX;
         z = mop.blockZ;
         if(!super.worldObj.getBlock(x, y, z).getMaterial().isSolid()) {
            x -= var11;
            z -= var12;
            if(enhanced && !super.worldObj.getBlock(x, y, z).getMaterial().isSolid()) {
               x -= var11;
               z -= var12;
            }
         }

         while(this.isNotSolidOrLeaves(super.worldObj.getBlock(x + var11, y, z + var12).getMaterial()) && super.worldObj.getBlock(x, y, z).getMaterial().isSolid() && y < 256) {
            super.worldObj.setBlock(x + var11, y, z + var12, Blocks.vine, meta, 3);
            spawnParticles(super.worldObj, EFFECT, 0.5D + (double)x + (double)var11, 0.5D + (double)y, 0.5D + (double)z + (double)var12);
            ++y;
            if(!super.worldObj.getBlock(x, y, z).getMaterial().isSolid()) {
               x -= var11;
               z -= var12;
               if(enhanced && !super.worldObj.getBlock(x, y, z).getMaterial().isSolid()) {
                  x -= var11;
                  z -= var12;
               }
            }
         }
      } else {
         EntityItem itemEntity = null;
         ItemStack newBrewStack = Witchery.Items.GENERIC.itemBrewOfVines.createStack();
         switch(EntityWitchProjectile.NamelessClass1399479900.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
         case 1:
            itemEntity = new EntityItem(super.worldObj, (double)mop.blockX + 0.5D, (double)(mop.blockY + (mop.sideHit == 0?-1:1)) + 0.5D, (double)mop.blockZ + 0.5D, newBrewStack);
            break;
         case 2:
            itemEntity = new EntityItem(super.worldObj, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, newBrewStack);
         }

         this.skipFX = true;
         super.worldObj.spawnEntityInWorld(itemEntity);
      }

   }

   private boolean isNotSolidOrLeaves(Material material) {
      return material == null || !material.isSolid() || material == Material.leaves;
   }

   private static boolean setBlockIfNotSolid(World world, int x, int y, int z, Block block) {
      return setBlockIfNotSolid(world, x, y, z, block, 0);
   }

   private static boolean setBlockIfNotSolid(World world, int x, int y, int z, Block block, int metadata) {
      if(world.getBlock(x, y, z).getMaterial().isSolid() && (block != Blocks.web || world.getBlock(x, y, z) != Blocks.snow)) {
         return false;
      } else {
         world.setBlock(x, y, z, block, metadata, 3);
         spawnParticles(world, ParticleEffect.EXPLODE, 0.5D + (double)x, 0.5D + (double)y, 0.5D + (double)z);
         return true;
      }
   }

   public void readEntityFromNBT(NBTTagCompound nbtTag) {
      super.readEntityFromNBT(nbtTag);
      if(nbtTag.hasKey("damageValue")) {
         this.damageValue = nbtTag.getInteger("damageValue");
         this.setDamageValue(this.damageValue);
      } else {
         this.setDead();
      }

   }

   public void writeEntityToNBT(NBTTagCompound nbtTag) {
      super.writeEntityToNBT(nbtTag);
      nbtTag.setInteger("damageValue", this.damageValue);
   }

   // $FF: synthetic class
   static class NamelessClass1399479900 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType = new int[MovingObjectType.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.BLOCK.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.ENTITY.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
