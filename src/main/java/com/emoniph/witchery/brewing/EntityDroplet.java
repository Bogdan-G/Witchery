package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.EntityUtil;
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
import net.minecraftforge.common.util.ForgeDirection;

public class EntityDroplet extends Entity {

   private int field_145788_c = -1;
   private int field_145786_d = -1;
   private int field_145787_e = -1;
   private Block field_145785_f;
   protected boolean inGround;
   public int throwableShake;
   private int ticksInGround;
   private int ticksInAir;
   private NBTTagCompound nbtBrew;
   private int color;


   public EntityDroplet(World world) {
      super(world);
      this.setSize(0.25F, 0.25F);
   }

   public EntityDroplet(World world, double x, double y, double z, NBTTagCompound nbtBrew) {
      super(world);
      this.ticksInGround = 0;
      this.setSize(0.25F, 0.25F);
      this.setPosition(x, y, z);
      super.yOffset = 0.0F;
      this.nbtBrew = nbtBrew;
      this.setColor(WitcheryBrewRegistry.INSTANCE.getBrewColor(nbtBrew));
   }

   protected void entityInit() {
      super.dataWatcher.addObject(6, Integer.valueOf(0));
   }

   protected void setColor(int color) {
      this.getDataWatcher().updateObject(6, Integer.valueOf(color));
   }

   public int getColor() {
      return this.getDataWatcher().getWatchableObjectInt(6);
   }

   public NBTTagCompound getBrew() {
      return this.nbtBrew;
   }

   @SideOnly(Side.CLIENT)
   public boolean isInRangeToRenderDist(double distSq) {
      double d1 = super.boundingBox.getAverageEdgeLength() * 4.0D;
      d1 *= 64.0D;
      return distSq < d1 * d1;
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
   public void setVelocity(double motionX, double motionY, double motionZ) {
      super.motionX = motionX;
      super.motionY = motionY;
      super.motionZ = motionZ;
      if(super.prevRotationPitch == 0.0F && super.prevRotationYaw == 0.0F) {
         float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
         super.prevRotationYaw = super.rotationYaw = (float)(Math.atan2(motionX, motionZ) * 180.0D / 3.141592653589793D);
         super.prevRotationPitch = super.rotationPitch = (float)(Math.atan2(motionY, (double)f) * 180.0D / 3.141592653589793D);
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
            if(this.ticksInGround == 1200) {
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

         for(int f4 = 0; f4 < f2.size(); ++f4) {
            Entity entity1 = (Entity)f2.get(f4);
            if(entity1.canBeCollidedWith() && this.ticksInAir >= 5) {
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

      if(movingobjectposition != null) {
         if(movingobjectposition.typeOfHit == MovingObjectType.BLOCK && super.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ) == Blocks.portal) {
            this.setInPortal();
         } else {
            this.onImpact(movingobjectposition);
         }
      }

      super.posX += super.motionX;
      super.posY += super.motionY;
      super.posZ += super.motionZ;
      float var15 = MathHelper.sqrt_double(super.motionX * super.motionX + super.motionZ * super.motionZ);
      super.rotationYaw = (float)(Math.atan2(super.motionX, super.motionZ) * 180.0D / 3.141592653589793D);

      for(super.rotationPitch = (float)(Math.atan2(super.motionY, (double)var15) * 180.0D / 3.141592653589793D); super.rotationPitch - super.prevRotationPitch < -180.0F; super.prevRotationPitch -= 360.0F) {
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
      float var16 = 0.99F;
      float var17 = this.getGravityVelocity();
      if(this.isInWater()) {
         for(int i = 0; i < 4; ++i) {
            float var18 = 0.25F;
            super.worldObj.spawnParticle("bubble", super.posX - super.motionX * (double)var18, super.posY - super.motionY * (double)var18, super.posZ - super.motionZ * (double)var18, super.motionX, super.motionY, super.motionZ);
         }

         var16 = 0.8F;
      }

      super.motionX *= (double)var16;
      super.motionY *= (double)var16;
      super.motionZ *= (double)var16;
      super.motionY -= (double)var17;
      this.setPosition(super.posX, super.posY, super.posZ);
   }

   protected float getGravityVelocity() {
      return 0.03F;
   }

   protected void onImpact(MovingObjectPosition mop) {
      if(!super.worldObj.isRemote) {
         super.worldObj.playAuxSFX(1016, MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY), MathHelper.floor_double(super.posZ), this.getColor());
         if(mop != null) {
            ModifiersEffect modifiers = new ModifiersEffect(1.0D, 1.0D, false, new EntityPosition(this), true, 1, EntityUtil.playerOrFake(super.worldObj, (EntityLivingBase)((EntityPlayer)null)));
            switch(EntityDroplet.NamelessClass47113180.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
            case 1:
               WitcheryBrewRegistry.INSTANCE.applyToBlock(super.worldObj, mop.blockX, mop.blockY, mop.blockZ, ForgeDirection.getOrientation(mop.sideHit), 1, this.nbtBrew, modifiers);
               break;
            case 2:
               if(mop.entityHit instanceof EntityLivingBase) {
                  WitcheryBrewRegistry.INSTANCE.applyToEntity(super.worldObj, (EntityLivingBase)mop.entityHit, this.nbtBrew, modifiers);
               }
            case 3:
            }
         }
      }

      this.setDead();
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      nbtRoot.setShort("xTile", (short)this.field_145788_c);
      nbtRoot.setShort("yTile", (short)this.field_145786_d);
      nbtRoot.setShort("zTile", (short)this.field_145787_e);
      nbtRoot.setByte("inTile", (byte)Block.getIdFromBlock(this.field_145785_f));
      nbtRoot.setByte("shake", (byte)this.throwableShake);
      nbtRoot.setByte("inGround", (byte)(this.inGround?1:0));
      if(this.nbtBrew != null) {
         nbtRoot.setTag("Brew", this.nbtBrew.copy());
      }

   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      this.field_145788_c = nbtRoot.getShort("xTile");
      this.field_145786_d = nbtRoot.getShort("yTile");
      this.field_145787_e = nbtRoot.getShort("zTile");
      this.field_145785_f = Block.getBlockById(nbtRoot.getByte("inTile") & 255);
      this.throwableShake = nbtRoot.getByte("shake") & 255;
      this.inGround = nbtRoot.getByte("inGround") == 1;
      if(nbtRoot.hasKey("Brew", 10)) {
         this.nbtBrew = (NBTTagCompound)nbtRoot.getCompoundTag("Brew").copy();
         this.setColor(WitcheryBrewRegistry.INSTANCE.getBrewColor(nbtRoot));
      }

      if(this.nbtBrew == null) {
         this.setDead();
      }

   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   // $FF: synthetic class
   static class NamelessClass47113180 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType = new int[MovingObjectType.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.BLOCK.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.ENTITY.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.MISS.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
