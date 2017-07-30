package com.emoniph.witchery.brewing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public abstract class EntityThrowableBase extends Entity {

   private int field_145788_c = -1;
   private int field_145786_d = -1;
   private int field_145787_e = -1;
   private Block field_145785_f;
   protected boolean inGround;
   public int throwableShake;
   private EntityLivingBase thrower;
   private String throwerName;
   private int ticksInGround;
   private int ticksInAir;


   public EntityThrowableBase(World world) {
      super(world);
      this.setSize(0.25F, 0.25F);
   }

   protected void entityInit() {}

   @SideOnly(Side.CLIENT)
   public boolean isInRangeToRenderDist(double p_70112_1_) {
      double d1 = super.boundingBox.getAverageEdgeLength() * 4.0D;
      d1 *= 64.0D;
      return p_70112_1_ < d1 * d1;
   }

   public EntityThrowableBase(World world, EntityLivingBase thrower, float pitchShift) {
      super(world);
      this.thrower = thrower;
      this.setSize(0.25F, 0.25F);
      this.setLocationAndAngles(thrower.posX, thrower.posY + (double)thrower.getEyeHeight(), thrower.posZ, thrower.rotationYaw, thrower.rotationPitch);
      super.posX -= (double)(MathHelper.cos(super.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
      super.posY -= 0.10000000149011612D;
      super.posZ -= (double)(MathHelper.sin(super.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
      this.setPosition(super.posX, super.posY, super.posZ);
      super.yOffset = 0.0F;
      float f = 0.6F;
      super.motionX = (double)(-MathHelper.sin(super.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(super.rotationPitch / 180.0F * 3.1415927F) * f);
      super.motionZ = (double)(MathHelper.cos(super.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(super.rotationPitch / 180.0F * 3.1415927F) * f);
      super.motionY = (double)(-MathHelper.sin((super.rotationPitch + pitchShift) / 180.0F * 3.1415927F) * f);
      this.setThrowableHeading(super.motionX, super.motionY, super.motionZ, this.func_70182_d(), 1.0F);
   }

   public EntityThrowableBase(World world, double x, double y, double z, float pitchShift) {
      super(world);
      this.ticksInGround = 0;
      this.setSize(0.25F, 0.25F);
      this.setPosition(x, y, z);
      super.yOffset = 0.0F;
   }

   protected float func_70182_d() {
      return 1.5F;
   }

   protected float func_70183_g() {
      return 0.0F;
   }

   protected int getMaxAirTicks() {
      return 200;
   }

   protected int getMaxGroundTicks() {
      return 1200;
   }

   public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_) {
      float f2 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
      p_70186_1_ /= (double)f2;
      p_70186_3_ /= (double)f2;
      p_70186_5_ /= (double)f2;
      p_70186_1_ += super.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
      p_70186_3_ += super.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
      p_70186_5_ += super.rand.nextGaussian() * 0.007499999832361937D * (double)p_70186_8_;
      p_70186_1_ *= (double)p_70186_7_;
      p_70186_3_ *= (double)p_70186_7_;
      p_70186_5_ *= (double)p_70186_7_;
      super.motionX = p_70186_1_;
      super.motionY = p_70186_3_;
      super.motionZ = p_70186_5_;
      float f3 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
      super.prevRotationYaw = super.rotationYaw = (float)(Math.atan2(p_70186_1_, p_70186_5_) * 180.0D / 3.141592653589793D);
      super.prevRotationPitch = super.rotationPitch = (float)(Math.atan2(p_70186_3_, (double)f3) * 180.0D / 3.141592653589793D);
      this.ticksInGround = 0;
   }

   @SideOnly(Side.CLIENT)
   public void setVelocity(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
      super.motionX = p_70016_1_;
      super.motionY = p_70016_3_;
      super.motionZ = p_70016_5_;
      if(super.prevRotationPitch == 0.0F && super.prevRotationYaw == 0.0F) {
         float f = MathHelper.sqrt_double(p_70016_1_ * p_70016_1_ + p_70016_5_ * p_70016_5_);
         super.prevRotationYaw = super.rotationYaw = (float)(Math.atan2(p_70016_1_, p_70016_5_) * 180.0D / 3.141592653589793D);
         super.prevRotationPitch = super.rotationPitch = (float)(Math.atan2(p_70016_3_, (double)f) * 180.0D / 3.141592653589793D);
      }

   }

   public void onUpdate() {
      super.lastTickPosX = super.posX;
      super.lastTickPosY = super.posY;
      super.lastTickPosZ = super.posZ;
      super.onUpdate();
      if(this.throwableShake > 0) {
         --this.throwableShake;
      }

      if(this.inGround) {
         if(super.worldObj.getBlock(this.field_145788_c, this.field_145786_d, this.field_145787_e) == this.field_145785_f) {
            ++this.ticksInGround;
            if(this.ticksInGround >= this.getMaxGroundTicks()) {
               if(!super.worldObj.isRemote) {
                  this.onSetDead();
               } else {
                  this.onClientSetDead();
               }

               this.setDead();
            }

            return;
         }

         this.inGround = false;
         super.motionX *= (double)(super.rand.nextFloat() * 0.2F);
         super.motionY *= (double)(super.rand.nextFloat() * 0.2F);
         super.motionZ *= (double)(super.rand.nextFloat() * 0.2F);
         this.ticksInGround = 0;
         this.ticksInAir = 0;
      } else {
         ++this.ticksInAir;
         if(this.ticksInAir >= this.getMaxAirTicks()) {
            if(!super.worldObj.isRemote) {
               this.onSetDead();
            } else {
               this.onClientSetDead();
            }

            this.setDead();
         }
      }

      Vec3 vec3 = Vec3.createVectorHelper(super.posX, super.posY, super.posZ);
      Vec3 vec31 = Vec3.createVectorHelper(super.posX + super.motionX, super.posY + super.motionY, super.posZ + super.motionZ);
      MovingObjectPosition movingobjectposition = super.worldObj.rayTraceBlocks(vec3, vec31);
      vec3 = Vec3.createVectorHelper(super.posX, super.posY, super.posZ);
      vec31 = Vec3.createVectorHelper(super.posX + super.motionX, super.posY + super.motionY, super.posZ + super.motionZ);
      if(movingobjectposition != null) {
         vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
      }

      if(!super.worldObj.isRemote) {
         Entity f1 = null;
         List f2 = super.worldObj.getEntitiesWithinAABBExcludingEntity(this, super.boundingBox.addCoord(super.motionX, super.motionY, super.motionZ).expand(1.0D, 1.0D, 1.0D));
         double f3 = 0.0D;
         EntityLivingBase f4 = this.getThrower();

         for(int j = 0; j < f2.size(); ++j) {
            Entity entity1 = (Entity)f2.get(j);
            if(entity1.canBeCollidedWith() && entity1 instanceof EntityLivingBase && (entity1 != f4 || this.ticksInAir >= 5)) {
               float f = 0.3F;
               AxisAlignedBB axisalignedbb = entity1.boundingBox.expand((double)f, (double)f, (double)f);
               MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
               if(movingobjectposition1 != null) {
                  double d1 = vec3.distanceTo(movingobjectposition1.hitVec);
                  if(d1 < f3 || f3 == 0.0D) {
                     f1 = entity1;
                     f3 = d1;
                  }
               }
            }
         }

         if(f1 != null) {
            movingobjectposition = new MovingObjectPosition(f1);
         }
      }

      if(movingobjectposition != null && (movingobjectposition.typeOfHit != MovingObjectType.BLOCK || super.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) != Blocks.portal)) {
         this.onImpact(movingobjectposition);
      }

      super.posX += super.motionX;
      super.posY += super.motionY;
      super.posZ += super.motionZ;
      float var16 = MathHelper.sqrt_double(super.motionX * super.motionX + super.motionZ * super.motionZ);
      super.rotationYaw = (float)(Math.atan2(super.motionX, super.motionZ) * 180.0D / 3.141592653589793D);

      for(super.rotationPitch = (float)(Math.atan2(super.motionY, (double)var16) * 180.0D / 3.141592653589793D); super.rotationPitch - super.prevRotationPitch < -180.0F; super.prevRotationPitch -= 360.0F) {
         ;
      }

      while(super.rotationPitch - super.prevRotationPitch >= 180.0F) {
         super.prevRotationPitch += 360.0F;
      }

      while(super.rotationYaw - super.prevRotationYaw < -180.0F) {
         super.prevRotationYaw -= 360.0F;
      }

      while(super.rotationYaw - super.prevRotationYaw >= 180.0F) {
         super.prevRotationYaw += 360.0F;
      }

      super.rotationPitch = super.prevRotationPitch + (super.rotationPitch - super.prevRotationPitch) * 0.2F;
      super.rotationYaw = super.prevRotationYaw + (super.rotationYaw - super.prevRotationYaw) * 0.2F;
      float var17 = 0.99F;
      float var18 = this.getGravityVelocity();
      if(this.isInWater()) {
         for(int i = 0; i < 4; ++i) {
            float var19 = 0.25F;
            super.worldObj.spawnParticle("bubble", super.posX - super.motionX * (double)var19, super.posY - super.motionY * (double)var19, super.posZ - super.motionZ * (double)var19, super.motionX, super.motionY, super.motionZ);
         }

         var17 = 0.8F;
      }

      super.motionX *= (double)var17;
      super.motionY *= (double)var17;
      super.motionZ *= (double)var17;
      super.motionY -= (double)var18;
      this.setPosition(super.posX, super.posY, super.posZ);
   }

   protected void onSetDead() {}

   @SideOnly(Side.CLIENT)
   protected void onClientSetDead() {}

   protected float getGravityVelocity() {
      return 0.03F;
   }

   protected abstract void onImpact(MovingObjectPosition var1);

   public void writeEntityToNBT(NBTTagCompound p_70014_1_) {
      p_70014_1_.setShort("xTile", (short)this.field_145788_c);
      p_70014_1_.setShort("yTile", (short)this.field_145786_d);
      p_70014_1_.setShort("zTile", (short)this.field_145787_e);
      p_70014_1_.setByte("inTile", (byte)Block.getIdFromBlock(this.field_145785_f));
      p_70014_1_.setByte("shake", (byte)this.throwableShake);
      p_70014_1_.setByte("inGround", (byte)(this.inGround?1:0));
      if((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof EntityPlayer) {
         this.throwerName = this.thrower.getCommandSenderName();
      }

      p_70014_1_.setString("ownerName", this.throwerName == null?"":this.throwerName);
   }

   public void readEntityFromNBT(NBTTagCompound p_70037_1_) {
      this.field_145788_c = p_70037_1_.getShort("xTile");
      this.field_145786_d = p_70037_1_.getShort("yTile");
      this.field_145787_e = p_70037_1_.getShort("zTile");
      this.field_145785_f = Block.getBlockById(p_70037_1_.getByte("inTile") & 255);
      this.throwableShake = p_70037_1_.getByte("shake") & 255;
      this.inGround = p_70037_1_.getByte("inGround") == 1;
      this.throwerName = p_70037_1_.getString("ownerName");
      if(this.throwerName != null && this.throwerName.length() == 0) {
         this.throwerName = null;
      }

   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   public EntityLivingBase getThrower() {
      if(this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
         this.thrower = super.worldObj.getPlayerEntityByName(this.throwerName);
      }

      return this.thrower;
   }
}
