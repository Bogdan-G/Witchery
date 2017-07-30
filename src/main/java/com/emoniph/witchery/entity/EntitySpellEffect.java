package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffectProjectile;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntitySpellEffect extends Entity {

   private int xTile = -1;
   private int yTile = -1;
   private int zTile = -1;
   private Block inTile;
   private boolean inGround;
   public EntityLivingBase shootingEntity;
   private int ticksAlive;
   private int ticksInAir;
   private int lifetime = -1;
   private int effectLevel;
   public double accelerationX;
   public double accelerationY;
   public double accelerationZ;
   private int effectID;


   public EntitySpellEffect(World par1World) {
      super(par1World);
      this.setSize(0.5F, 0.5F);
      super.noClip = true;
   }

   public EntitySpellEffect setLifeTime(int ticks) {
      this.lifetime = ticks;
      return this;
   }

   protected void entityInit() {
      super.dataWatcher.addObject(6, Integer.valueOf(0));
      super.dataWatcher.addObject(15, Integer.valueOf(0));
   }

   public void setShooter(EntityLivingBase entity) {
      if(!super.worldObj.isRemote) {
         super.dataWatcher.updateObject(15, Integer.valueOf(entity.getEntityId()));
      }

   }

   public int getShooterID() {
      int id = super.dataWatcher.getWatchableObjectInt(15);
      return id;
   }

   public boolean isShooter(Entity entity) {
      int idOther = entity.getEntityId();
      int us = this.getShooterID();
      return idOther == us;
   }

   public void setEffectID(int effectID) {
      this.effectID = effectID;
      this.getDataWatcher().updateObject(6, Integer.valueOf(effectID));
   }

   public int getEffectID() {
      return this.getDataWatcher().getWatchableObjectInt(6);
   }

   public int getEffectLevel() {
      return this.effectLevel;
   }

   @SideOnly(Side.CLIENT)
   public boolean isInRangeToRenderDist(double par1) {
      double d1 = super.boundingBox.getAverageEdgeLength() * 4.0D;
      d1 *= 64.0D;
      return par1 < d1 * d1;
   }

   public EntitySpellEffect(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, SymbolEffect effect, int effectLevel) {
      super(par1World);
      this.effectLevel = effectLevel;
      this.setSize(1.0F, 1.0F);
      this.setLocationAndAngles(par2, par4, par6, super.rotationYaw, super.rotationPitch);
      this.setPosition(par2, par4, par6);
      double d6 = (double)MathHelper.sqrt_double(par8 * par8 + par10 * par10 + par12 * par12);
      this.accelerationX = par8 / d6 * 0.1D;
      this.accelerationY = par10 / d6 * 0.1D;
      this.accelerationZ = par12 / d6 * 0.1D;
      this.setEffectID(effect.getEffectID());
   }

   public EntitySpellEffect(World par1World, EntityLivingBase par2EntityLivingBase, double par3, double par5, double par7, SymbolEffect effect, int effectLevel) {
      super(par1World);
      this.shootingEntity = par2EntityLivingBase;
      this.effectLevel = effectLevel;
      this.setSize(1.0F, 1.0F);
      this.setLocationAndAngles(par2EntityLivingBase.posX, par2EntityLivingBase.posY, par2EntityLivingBase.posZ, par2EntityLivingBase.rotationYaw, par2EntityLivingBase.rotationPitch);
      this.setPosition(super.posX, super.posY, super.posZ);
      super.yOffset = 0.0F;
      super.motionX = super.motionY = super.motionZ = 0.0D;
      par3 += super.rand.nextGaussian() * 0.4D;
      par5 += super.rand.nextGaussian() * 0.4D;
      par7 += super.rand.nextGaussian() * 0.4D;
      double d3 = (double)MathHelper.sqrt_double(par3 * par3 + par5 * par5 + par7 * par7);
      this.accelerationX = par3 / d3 * 0.1D;
      this.accelerationY = par5 / d3 * 0.1D;
      this.accelerationZ = par7 / d3 * 0.1D;
      this.setEffectID(effect.getEffectID());
   }

   public void onUpdate() {
      if(!super.worldObj.isRemote && (this.shootingEntity != null && this.shootingEntity.isDead || !super.worldObj.blockExists((int)super.posX, (int)super.posY, (int)super.posZ))) {
         this.setDead();
      } else {
         super.onUpdate();
         if(this.inGround) {
            Block vec3 = super.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
            if(vec3 == this.inTile) {
               ++this.ticksAlive;
               if(this.ticksAlive == 600) {
                  this.setDead();
               }

               return;
            }

            this.inGround = false;
            super.motionX *= (double)(super.rand.nextFloat() * 0.2F);
            super.motionY *= (double)(super.rand.nextFloat() * 0.2F);
            super.motionZ *= (double)(super.rand.nextFloat() * 0.2F);
            this.ticksAlive = 0;
            this.ticksInAir = 0;
         } else {
            ++this.ticksInAir;
            if(this.ticksInAir == 200) {
               this.setDead();
            }
         }

         Vec3 var16 = Vec3.createVectorHelper(super.posX, super.posY, super.posZ);
         Vec3 vec31 = Vec3.createVectorHelper(super.posX + super.motionX, super.posY + super.motionY, super.posZ + super.motionZ);
         MovingObjectPosition movingobjectposition = super.worldObj.rayTraceBlocks(var16, vec31);
         var16 = Vec3.createVectorHelper(super.posX, super.posY, super.posZ);
         vec31 = Vec3.createVectorHelper(super.posX + super.motionX, super.posY + super.motionY, super.posZ + super.motionZ);
         if(movingobjectposition != null) {
            vec31 = Vec3.createVectorHelper(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
         }

         Entity entity = null;
         List list = super.worldObj.getEntitiesWithinAABBExcludingEntity(this, super.boundingBox.addCoord(super.motionX, super.motionY, super.motionZ).expand(1.0D, 1.0D, 1.0D));
         double d0 = 0.0D;
         boolean remote = super.worldObj.isRemote;

         for(int f1 = 0; f1 < list.size(); ++f1) {
            Entity f2 = (Entity)list.get(f1);
            if(f2.canBeCollidedWith() && (!this.isShooter(f2) || this.ticksInAir >= 25)) {
               float effect = 0.3F;
               AxisAlignedBB f3 = f2.boundingBox.expand((double)effect, (double)effect, (double)effect);
               MovingObjectPosition movingobjectposition1 = f3.calculateIntercept(var16, vec31);
               if(movingobjectposition1 != null) {
                  double d1 = var16.distanceTo(movingobjectposition1.hitVec);
                  if(d1 < d0 || d0 == 0.0D) {
                     entity = f2;
                     d0 = d1;
                  }
               }
            }
         }

         if(entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
         }

         if(movingobjectposition != null || this.lifetime != -1 && Math.max(--this.lifetime, 0) == 0) {
            this.onImpact(movingobjectposition);
         }

         super.posX += super.motionX;
         super.posY += super.motionY;
         super.posZ += super.motionZ;
         float var17 = MathHelper.sqrt_double(super.motionX * super.motionX + super.motionZ * super.motionZ);
         super.rotationYaw = (float)(Math.atan2(super.motionZ, super.motionX) * 180.0D / 3.141592653589793D) + 90.0F;

         for(super.rotationPitch = (float)(Math.atan2((double)var17, super.motionY) * 180.0D / 3.141592653589793D) - 90.0F; super.rotationPitch - super.prevRotationPitch < -180.0F; super.prevRotationPitch -= 360.0F) {
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
         float var19 = this.getMotionFactor();
         if(this.isInWater()) {
            for(int var18 = 0; var18 < 4; ++var18) {
               float var20 = 0.25F;
               super.worldObj.spawnParticle("bubble", super.posX - super.motionX * (double)var20, super.posY - super.motionY * (double)var20, super.posZ - super.motionZ * (double)var20, super.motionX, super.motionY, super.motionZ);
            }

            var19 = 0.8F;
         }

         SymbolEffect var21 = EffectRegistry.instance().getEffect(this.getEffectID());
         if(var21 == null) {
            this.setDead();
         } else {
            if(var21.fallsToEarth() && this.getEffectLevel() == 1) {
               this.accelerationX *= 0.8D;
               this.accelerationY *= 0.8D;
               this.accelerationZ *= 0.8D;
               super.motionX += this.accelerationX;
               super.motionY += this.accelerationY;
               super.motionZ += this.accelerationZ;
               super.motionY -= 0.05D;
            } else {
               super.motionX += this.accelerationX;
               super.motionY += this.accelerationY;
               super.motionZ += this.accelerationZ;
               super.motionX *= (double)var19;
               super.motionY *= (double)var19;
               super.motionZ *= (double)var19;
            }

            super.worldObj.spawnParticle(var21.isCurse()?ParticleEffect.MOB_SPELL.toString():ParticleEffect.SLIME.toString(), super.posX, super.posY + 0.5D, super.posZ, 0.0D, 0.0D, 0.0D);
            if(var21.isCurse()) {
               super.worldObj.spawnParticle(var21.isCurse()?ParticleEffect.FLAME.toString():ParticleEffect.SLIME.toString(), super.posX, super.posY + 0.5D, super.posZ, 0.0D, 0.0D, 0.0D);
            }

            this.setPosition(super.posX, super.posY, super.posZ);
         }
      }

   }

   protected float getMotionFactor() {
      return 0.95F;
   }

   protected void onImpact(MovingObjectPosition mop) {
      if(!super.worldObj.isRemote) {
         SymbolEffect effect = EffectRegistry.instance().getEffect(this.getEffectID());
         if(effect != null && effect instanceof SymbolEffectProjectile) {
            if(effect.isCurse()) {
               ParticleEffect.MOB_SPELL.send(SoundEffect.MOB_ENDERDRAGON_HIT, this, 1.0D, 1.0D, 16);
            } else {
               ParticleEffect.SLIME.send(SoundEffect.MOB_SLIME_SMALL, this, 1.0D, 1.0D, 16);
            }

            ((SymbolEffectProjectile)effect).onCollision(super.worldObj, this.shootingEntity, mop, this);
         }
      }

      this.setDead();
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      nbtRoot.setShort("xTile", (short)this.xTile);
      nbtRoot.setShort("yTile", (short)this.yTile);
      nbtRoot.setShort("zTile", (short)this.zTile);
      nbtRoot.setByte("inTile", (byte)Block.getIdFromBlock(this.inTile));
      nbtRoot.setByte("inGround", (byte)(this.inGround?1:0));
      nbtRoot.setTag("direction", this.newDoubleNBTList(new double[]{super.motionX, super.motionY, super.motionZ}));
      nbtRoot.setInteger("EffectID", this.effectID);
      nbtRoot.setInteger("lifetime", this.lifetime);
      nbtRoot.setInteger("effectLevel", this.effectLevel);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      this.xTile = nbtRoot.getShort("xTile");
      this.yTile = nbtRoot.getShort("yTile");
      this.zTile = nbtRoot.getShort("zTile");
      this.inTile = Block.getBlockById(nbtRoot.getByte("inTile") & 255);
      this.inGround = nbtRoot.getByte("inGround") == 1;
      if(nbtRoot.hasKey("lifetime")) {
         this.lifetime = nbtRoot.getInteger("lifetime");
      } else {
         this.lifetime = -1;
      }

      if(nbtRoot.hasKey("direction") && nbtRoot.hasKey("EffectID")) {
         this.effectID = nbtRoot.getInteger("EffectID");
         this.setEffectID(this.effectID);
         NBTTagList nbttaglist = nbtRoot.getTagList("direction", 6);
         super.motionX = nbttaglist.func_150309_d(0);
         super.motionY = nbttaglist.func_150309_d(1);
         super.motionZ = nbttaglist.func_150309_d(2);
      } else {
         this.setDead();
      }

      this.effectLevel = Math.max(nbtRoot.getInteger("effectLevel"), 1);
   }

   public boolean canBeCollidedWith() {
      return true;
   }

   public float getCollisionBorderSize() {
      return 1.0F;
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      if(this.isEntityInvulnerable()) {
         return false;
      } else {
         this.setBeenAttacked();
         Entity entity = par1DamageSource.getEntity();
         boolean canDeflect = entity != null && this.getEffectID() != 5 && entity instanceof EntityPlayer && ((EntityPlayer)entity).getHeldItem() != null && ((EntityPlayer)entity).getHeldItem().getItem() == Witchery.Items.MYSTIC_BRANCH;
         if(canDeflect) {
            Vec3 vec3 = par1DamageSource.getEntity().getLookVec();
            if(vec3 != null) {
               super.motionX = vec3.xCoord;
               super.motionY = vec3.yCoord;
               super.motionZ = vec3.zCoord;
               this.accelerationX = super.motionX * 0.1D;
               this.accelerationY = super.motionY * 0.1D;
               this.accelerationZ = super.motionZ * 0.1D;
            }

            if(par1DamageSource.getEntity() instanceof EntityLivingBase) {
               this.shootingEntity = (EntityLivingBase)par1DamageSource.getEntity();
            }

            return true;
         } else {
            return false;
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   @SideOnly(Side.CLIENT)
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }
}
