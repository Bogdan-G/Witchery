package com.emoniph.witchery.entity.ai;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockVoidBramble;
import com.emoniph.witchery.brewing.EntityBrew;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.entity.EntityFlyingTameable;
import com.emoniph.witchery.entity.EntityWitchProjectile;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.TimeUtil;
import com.emoniph.witchery.util.Waypoint;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class EntityAIFlyerFlyToWaypoint extends EntityAIBase {

   private EntityFlyingTameable flyer;
   private EntityAIFlyerFlyToWaypoint.CarryRequirement carryRequirement;
   private static final double HIT_RADIUS = 1.0D;
   private static final double HIT_RADIUS_SQ = 1.0D;
   int courseTimer = 0;


   public EntityAIFlyerFlyToWaypoint(EntityFlyingTameable flyer, EntityAIFlyerFlyToWaypoint.CarryRequirement carryRestrictions) {
      this.flyer = flyer;
      this.carryRequirement = carryRestrictions;
      this.setMutexBits(7);
   }

   public boolean shouldExecute() {
      return this.flyer.waypoint != null && (this.flyer.getHeldItem() != null || this.carryRequirement != EntityAIFlyerFlyToWaypoint.CarryRequirement.HELD_ITEM);
   }

   public boolean continueExecuting() {
      boolean heldItem = this.flyer.getHeldItem() != null;
      boolean awayFromHome = this.flyer.getDistanceSq(this.flyer.homeX, this.flyer.posY, this.flyer.homeZ) > 1.0D || Math.abs(this.flyer.posY - this.flyer.homeY) > 1.0D;
      return heldItem && this.carryRequirement == EntityAIFlyerFlyToWaypoint.CarryRequirement.HELD_ITEM || this.flyer.waypoint != null || awayFromHome;
   }

   public void startExecuting() {}

   public void resetTask() {
      this.flyer.waypoint = null;
      this.flyer.setSitting(true);
      if(this.flyer.riddenByEntity != null) {
         this.flyer.riddenByEntity.mountEntity((Entity)null);
      }

      this.courseTimer = 0;
   }

   public void updateTask() {
      if(!this.flyer.isSitting()) {
         Waypoint waypoint = this.flyer.getWaypoint();
         if(this.carryRequirement == EntityAIFlyerFlyToWaypoint.CarryRequirement.ENTITY_LIVING) {
            if(this.flyer.getDistanceSq(waypoint.X, waypoint.Y, waypoint.Z) <= 1.0D) {
               List dX = this.flyer.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.flyer.boundingBox.expand(1.0D, 1.0D, 1.0D));
               if(dX != null && dX.size() > 1) {
                  if(!this.flyer.worldObj.isRemote) {
                     Iterator item = dX.iterator();

                     while(item.hasNext()) {
                        EntityLivingBase dY = (EntityLivingBase)item.next();
                        if(dY != this.flyer) {
                           dY.mountEntity(this.flyer);
                        }
                     }
                  }

                  this.flyer.waypoint = null;
                  waypoint = this.flyer.getWaypoint();
               }
            }
         } else if(this.flyer.getHeldItem() != null && this.flyer.getDistanceSq(waypoint.X, waypoint.Y, waypoint.Z) <= 1.0D) {
            if(!this.flyer.worldObj.isRemote) {
               ItemStack var16 = this.flyer.getHeldItem();
               this.flyer.setCurrentItemOrArmor(0, (ItemStack)null);
               if(Witchery.Items.GENERIC.isBrew(var16)) {
                  this.flyer.worldObj.playSoundAtEntity(this.flyer, "random.bow", 0.5F, 0.4F / (this.flyer.worldObj.rand.nextFloat() * 0.4F + 0.8F));
                  EntityWitchProjectile var18 = new EntityWitchProjectile(this.flyer.worldObj, this.flyer, (ItemGeneral.SubItem)Witchery.Items.GENERIC.subItems.get(var16.getItemDamage()));
                  var18.motionX = 0.0D;
                  var18.motionZ = 0.0D;
                  this.flyer.worldObj.spawnEntityInWorld(var18);
               } else if(Witchery.Items.BREW == var16.getItem() && WitcheryBrewRegistry.INSTANCE.isSplash(var16.getTagCompound())) {
                  this.flyer.worldObj.playSoundAtEntity(this.flyer, "random.bow", 0.5F, 0.4F / (this.flyer.worldObj.rand.nextFloat() * 0.4F + 0.8F));
                  EntityBrew var22 = new EntityBrew(this.flyer.worldObj, this.flyer, var16, false);
                  var22.motionX = 0.0D;
                  var22.motionZ = 0.0D;
                  this.flyer.worldObj.spawnEntityInWorld(var22);
               } else if(var16.getItem() == Items.potionitem && ItemPotion.isSplash(var16.getItemDamage())) {
                  this.flyer.worldObj.playSoundAtEntity(this.flyer, "random.bow", 0.5F, 0.4F / (this.flyer.worldObj.rand.nextFloat() * 0.4F + 0.8F));
                  EntityPotion var20 = new EntityPotion(this.flyer.worldObj, this.flyer, var16);
                  var20.motionX = 0.0D;
                  var20.motionZ = 0.0D;
                  this.flyer.worldObj.spawnEntityInWorld(var20);
               } else {
                  EntityItem var19 = new EntityItem(this.flyer.worldObj, this.flyer.posX, this.flyer.posY, this.flyer.posZ, var16);
                  if(var16.getItem() == Witchery.Items.SEEDS_MINDRAKE) {
                     var19.lifespan = TimeUtil.secsToTicks(3);
                  }

                  this.flyer.worldObj.spawnEntityInWorld(var19);
               }
            }

            this.flyer.waypoint = null;
            waypoint = this.flyer.getWaypoint();
         }

         double var17 = waypoint.X - this.flyer.posX;
         double var21 = waypoint.Y - this.flyer.posY;
         double dZ = waypoint.Z - this.flyer.posZ;
         double trajectory = var17 * var17 + var21 * var21 + dZ * dZ;
         trajectory = (double)MathHelper.sqrt_double(trajectory);
         if(trajectory >= 128.0D && this.carryRequirement == EntityAIFlyerFlyToWaypoint.CarryRequirement.HELD_ITEM) {
            BlockVoidBramble.teleportRandomly(this.flyer.worldObj, (int)waypoint.X, (int)waypoint.Y, (int)waypoint.Z, this.flyer, 16);
         }

         if(--this.courseTimer < 0) {
            this.courseTimer = 0;
         }

         if(this.courseTimer == 0) {
            double ACCELERATION;
            if(!this.isCourseTraversable(waypoint.X, waypoint.Y, waypoint.Z, trajectory)) {
               ACCELERATION = this.flyer.posX + (this.flyer.worldObj.rand.nextDouble() * 4.0D - 2.0D) * 6.0D;
               double newY = this.flyer.posY + (this.flyer.worldObj.rand.nextDouble() * 2.0D - 1.0D) * 4.0D;
               double newZ = this.flyer.posZ + (this.flyer.worldObj.rand.nextDouble() * 4.0D - 2.0D) * 6.0D;
               if(this.flyer.worldObj.rand.nextInt(2) != 0) {
                  var17 = ACCELERATION - this.flyer.posX;
                  dZ = newZ - this.flyer.posZ;
               }

               if(this.flyer.getDistanceSq(waypoint.X, waypoint.Y, waypoint.Z) <= 1.0D) {
                  var21 = (this.flyer.posY > waypoint.Y && newY > 0.0D?-newY:newY) - this.flyer.posY;
               } else {
                  var21 = newY - this.flyer.posY;
               }

               trajectory = var17 * var17 + var21 * var21 + dZ * dZ;
               trajectory = (double)MathHelper.sqrt_double(trajectory);
            }

            ACCELERATION = 0.2D;
            this.flyer.motionX += var17 / trajectory * 0.2D;
            this.flyer.motionZ += dZ / trajectory * 0.2D;
            this.flyer.motionY += var21 / trajectory * 0.2D + (this.flyer.posY < Math.min(waypoint.Y + (double)(this.carryRequirement == EntityAIFlyerFlyToWaypoint.CarryRequirement.HELD_ITEM?32:32), 255.0D)?0.1D:0.0D);
            this.courseTimer = 10;
         }

         this.flyer.renderYawOffset = this.flyer.rotationYaw = -((float)Math.atan2(this.flyer.motionX, this.flyer.motionZ)) * 180.0F / 3.1415927F;
      }

   }

   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
      double d4 = (par1 - this.flyer.posX) / par7;
      double d5 = (par3 - this.flyer.posY) / par7;
      double d6 = (par5 - this.flyer.posZ) / par7;
      AxisAlignedBB axisalignedbb = this.flyer.boundingBox.copy();

      for(int i = 1; (double)i < par7; ++i) {
         axisalignedbb.offset(d4, d5, d6);
         if(!this.flyer.worldObj.getCollidingBoundingBoxes(this.flyer, axisalignedbb).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public static enum CarryRequirement {

      NONE("NONE", 0),
      HELD_ITEM("HELD_ITEM", 1),
      ENTITY_LIVING("ENTITY_LIVING", 2);
      // $FF: synthetic field
      private static final EntityAIFlyerFlyToWaypoint.CarryRequirement[] $VALUES = new EntityAIFlyerFlyToWaypoint.CarryRequirement[]{NONE, HELD_ITEM, ENTITY_LIVING};


      private CarryRequirement(String var1, int var2) {}

   }
}
