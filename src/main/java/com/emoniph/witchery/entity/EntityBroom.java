package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class EntityBroom extends Entity {

   private boolean field_70279_a;
   private double speedMultiplier;
   private int broomPosRotationIncrements;
   private double broomX;
   private double broomY;
   private double broomZ;
   private double broomYaw;
   private double broomPitch;
   @SideOnly(Side.CLIENT)
   private double velocityX;
   @SideOnly(Side.CLIENT)
   private double velocityY;
   @SideOnly(Side.CLIENT)
   private double velocityZ;
   boolean riderHasOwlFamiliar;
   boolean riderHasSoaringBrew;


   public EntityBroom(World world) {
      super(world);
      this.riderHasOwlFamiliar = false;
      this.riderHasSoaringBrew = false;
      this.field_70279_a = true;
      this.speedMultiplier = 0.07D;
      super.preventEntitySpawning = true;
      this.setSize(1.2F, 0.5F);
      super.yOffset = super.height / 2.0F;
   }

   public EntityBroom(World world, double x, double y, double z) {
      this(world);
      this.setPosition(x, y + (double)super.yOffset, z);
      super.motionX = 0.0D;
      super.motionY = 0.0D;
      super.motionZ = 0.0D;
      super.prevPosX = x;
      super.prevPosY = y;
      super.prevPosZ = z;
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   protected void entityInit() {
      super.dataWatcher.addObject(10, "");
      super.dataWatcher.addObject(16, Byte.valueOf((byte)-1));
      super.dataWatcher.addObject(17, new Integer(0));
      super.dataWatcher.addObject(18, new Integer(1));
      super.dataWatcher.addObject(19, new Float(0.0F));
   }

   protected void dealFireDamage(int par1) {}

   public void setBrushColor(int color) {
      super.dataWatcher.updateObject(16, Byte.valueOf((byte)color));
   }

   public int getBrushColor() {
      return super.dataWatcher.getWatchableObjectByte(16);
   }

   public void setCustomNameTag(String par1Str) {
      super.dataWatcher.updateObject(10, par1Str);
   }

   public String getCustomNameTag() {
      return super.dataWatcher.getWatchableObjectString(10);
   }

   public boolean hasCustomNameTag() {
      return super.dataWatcher.getWatchableObjectString(10).length() > 0;
   }

   public AxisAlignedBB getCollisionBox(Entity par1Entity) {
      return par1Entity.boundingBox;
   }

   public AxisAlignedBB getBoundingBox() {
      return super.boundingBox;
   }

   public boolean canBePushed() {
      return true;
   }

   public double getMountedYOffset() {
      return (double)super.height * 0.55D;
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      if(this.isEntityInvulnerable()) {
         return false;
      } else if(!super.worldObj.isRemote && !super.isDead) {
         this.setForwardDirection(-this.getForwardDirection());
         this.setTimeSinceHit(10);
         this.setDamageTaken(this.getDamageTaken() + par2 * 10.0F);
         this.setBeenAttacked();
         boolean flag = par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode;
         if(flag || this.getDamageTaken() > 40.0F) {
            if(super.riddenByEntity != null) {
               super.riddenByEntity.mountEntity(this);
            }

            if(!flag) {
               ItemStack broomStack = Witchery.Items.GENERIC.itemBroomEnchanted.createStack();
               if(this.hasCustomNameTag()) {
                  broomStack.setStackDisplayName(this.getCustomNameTag());
               }

               int brushColor = this.getBrushColor();
               if(brushColor >= 0 && brushColor <= 15) {
                  Witchery.Items.GENERIC.setBroomItemColor(broomStack, brushColor);
               }

               this.entityDropItem(broomStack, 0.0F);
            }

            this.setDead();
         }

         return true;
      } else {
         return true;
      }
   }

   @SideOnly(Side.CLIENT)
   public void performHurtAnimation() {
      this.setForwardDirection(-this.getForwardDirection());
      this.setTimeSinceHit(10);
      this.setDamageTaken(this.getDamageTaken() * 11.0F);
   }

   public boolean canBeCollidedWith() {
      return !super.isDead && super.riddenByEntity == null;
   }

   @SideOnly(Side.CLIENT)
   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int par9) {
      if(this.field_70279_a) {
         this.broomPosRotationIncrements = par9 + 5;
      } else {
         double d3 = x - super.posX;
         double d4 = y - super.posY;
         double d5 = z - super.posZ;
         double d6 = d3 * d3 + d4 * d4 + d5 * d5;
         if(d6 <= 1.0D) {
            return;
         }

         this.broomPosRotationIncrements = 3;
      }

      this.broomX = x;
      this.broomY = y;
      this.broomZ = z;
      this.broomYaw = (double)yaw;
      this.broomPitch = (double)pitch;
      super.motionX = this.velocityX;
      super.motionY = this.velocityY;
      super.motionZ = this.velocityZ;
   }

   @SideOnly(Side.CLIENT)
   public void setVelocity(double x, double y, double z) {
      this.velocityX = super.motionX = x;
      this.velocityY = super.motionY = y;
      this.velocityZ = super.motionZ = z;
   }

   public void onUpdate() {
      super.onUpdate();
      if(super.ticksExisted % 100 == 0 && super.riddenByEntity != null && super.riddenByEntity instanceof EntityPlayer) {
         this.riderHasSoaringBrew = InfusedBrewEffect.Soaring.isActive((EntityPlayer)super.riddenByEntity);
      }

      if(this.getTimeSinceHit() > 0) {
         this.setTimeSinceHit(this.getTimeSinceHit() - 1);
      }

      if(this.getDamageTaken() > 0.0F) {
         this.setDamageTaken(this.getDamageTaken() - 1.0F);
      }

      super.prevPosX = super.posX;
      super.prevPosY = super.posY;
      super.prevPosZ = super.posZ;
      boolean b0 = true;
      double d0 = 0.0D;
      double initialHorzVelocity = Math.sqrt(super.motionX * super.motionX + super.motionZ * super.motionZ);
      double newHorzVelocity;
      double d5;
      if(initialHorzVelocity > 0.26249999999999996D) {
         newHorzVelocity = Math.cos((double)super.rotationYaw * 3.141592653589793D / 180.0D);
         d5 = Math.sin((double)super.rotationYaw * 3.141592653589793D / 180.0D);
      }

      double d10;
      double d11;
      if(super.worldObj.isRemote && this.field_70279_a) {
         if(this.broomPosRotationIncrements > 0) {
            newHorzVelocity = super.posX + (this.broomX - super.posX) / (double)this.broomPosRotationIncrements;
            d5 = super.posY + (this.broomY - super.posY) / (double)this.broomPosRotationIncrements;
            d11 = super.posZ + (this.broomZ - super.posZ) / (double)this.broomPosRotationIncrements;
            d10 = MathHelper.wrapAngleTo180_double(this.broomYaw - (double)super.rotationYaw);
            super.rotationYaw = (float)((double)super.rotationYaw + d10 / (double)this.broomPosRotationIncrements);
            super.rotationPitch = (float)((double)super.rotationPitch + (this.broomPitch - (double)super.rotationPitch) / (double)this.broomPosRotationIncrements);
            --this.broomPosRotationIncrements;
            this.setPosition(newHorzVelocity, d5, d11);
            this.setRotation(super.rotationYaw, super.rotationPitch);
         } else {
            newHorzVelocity = super.posX + super.motionX;
            d5 = super.posY + super.motionY;
            d11 = super.posZ + super.motionZ;
            this.setRotation((float)((double)super.rotationYaw + (this.broomYaw - (double)super.rotationYaw)), (float)((double)super.rotationPitch + (this.broomPitch - (double)super.rotationPitch)));
            this.setPosition(newHorzVelocity, d5, d11);
            super.motionX *= 0.9900000095367432D;
            super.motionZ *= 0.9900000095367432D;
         }
      } else {
         double SPEED_LIMIT;
         double MAX_ACCELERATION;
         if(super.riddenByEntity != null && super.riddenByEntity instanceof EntityLivingBase) {
            newHorzVelocity = (double)((EntityLivingBase)super.riddenByEntity).moveForward;
            if(newHorzVelocity > 0.0D) {
               d5 = -Math.sin((double)(super.riddenByEntity.rotationYaw * 3.1415927F / 180.0F));
               d11 = Math.cos((double)(super.riddenByEntity.rotationYaw * 3.1415927F / 180.0F));
               super.motionX += d5 * this.speedMultiplier * (0.1D + (this.riderHasSoaringBrew?0.1D:0.0D) + (this.riderHasOwlFamiliar?0.2D:0.0D));
               super.motionZ += d11 * this.speedMultiplier * (0.1D + (this.riderHasSoaringBrew?0.1D:0.0D) + (this.riderHasOwlFamiliar?0.2D:0.0D));
               SPEED_LIMIT = -Math.sin((double)(super.riddenByEntity.rotationPitch * 3.1415927F / 180.0F));
               if(SPEED_LIMIT > -0.5D && SPEED_LIMIT < 0.2D) {
                  SPEED_LIMIT = 0.0D;
               } else if(SPEED_LIMIT < 0.0D) {
                  SPEED_LIMIT *= 0.5D;
               }

               super.motionY = SPEED_LIMIT * this.speedMultiplier * 2.0D;
            } else if(newHorzVelocity == 0.0D && (this.riderHasOwlFamiliar || this.riderHasSoaringBrew)) {
               super.motionX *= 0.9D;
               super.motionZ *= 0.9D;
            }
         } else if(super.riddenByEntity == null) {
            this.riderHasOwlFamiliar = false;
            SPEED_LIMIT = super.motionX * 0.9D;
            MAX_ACCELERATION = super.motionZ * 0.9D;
            super.motionX = Math.abs(SPEED_LIMIT) < 0.01D?0.0D:SPEED_LIMIT;
            super.motionZ = Math.abs(MAX_ACCELERATION) < 0.01D?0.0D:MAX_ACCELERATION;
            if(!super.onGround) {
               super.motionY = -0.2D;
            }
         }

         newHorzVelocity = Math.sqrt(super.motionX * super.motionX + super.motionZ * super.motionZ);
         SPEED_LIMIT = 0.9D + (this.riderHasOwlFamiliar?0.3D:0.0D) + (this.riderHasSoaringBrew?0.3D:0.0D);
         if(newHorzVelocity > SPEED_LIMIT) {
            d5 = SPEED_LIMIT / newHorzVelocity;
            super.motionX *= d5;
            super.motionZ *= d5;
            super.motionY *= d5;
            newHorzVelocity = SPEED_LIMIT;
         }

         MAX_ACCELERATION = !this.riderHasSoaringBrew && !this.riderHasOwlFamiliar?0.35D:0.35D;
         double MAX_ACCELERATION_FACTOR = MAX_ACCELERATION * 100.0D;
         if(newHorzVelocity > initialHorzVelocity && this.speedMultiplier < MAX_ACCELERATION) {
            this.speedMultiplier += (MAX_ACCELERATION - this.speedMultiplier) / MAX_ACCELERATION_FACTOR;
            if(this.speedMultiplier > MAX_ACCELERATION) {
               this.speedMultiplier = MAX_ACCELERATION;
            }
         } else {
            this.speedMultiplier -= (this.speedMultiplier - 0.07D) / MAX_ACCELERATION_FACTOR;
            if(this.speedMultiplier < 0.07D) {
               this.speedMultiplier = 0.07D;
            }
         }

         this.moveEntity(super.motionX, super.motionY, super.motionZ);
         super.motionX *= 0.9900000095367432D;
         super.motionY *= 0.9900000095367432D;
         super.motionZ *= 0.9900000095367432D;
         super.rotationPitch = 0.0F;
         d5 = (double)super.rotationYaw;
         d11 = super.prevPosX - super.posX;
         d10 = super.prevPosZ - super.posZ;
         if(d11 * d11 + d10 * d10 > 0.001D) {
            d5 = (double)((float)(Math.atan2(d10, d11) * 180.0D / 3.141592653589793D));
         }

         double d12 = MathHelper.wrapAngleTo180_double(d5 - (double)super.rotationYaw);
         super.rotationYaw = (float)((double)super.rotationYaw + d12);
         this.setRotation(super.rotationYaw, super.rotationPitch);
         if(!super.worldObj.isRemote) {
            List list = super.worldObj.getEntitiesWithinAABBExcludingEntity(this, super.boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
            if(list != null && !list.isEmpty()) {
               for(int l = 0; l < list.size(); ++l) {
                  Entity entity = (Entity)list.get(l);
                  if(entity != super.riddenByEntity && entity.canBePushed() && entity instanceof EntityBroom) {
                     entity.applyEntityCollision(this);
                  }
               }
            }

            if(super.riddenByEntity != null && super.riddenByEntity.isDead) {
               super.riddenByEntity = null;
            }
         }
      }

   }

   public void updateRiderPosition() {
      super.updateRiderPosition();
   }

   protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setString("CustomName", this.getCustomNameTag());
      int brushColor = this.getBrushColor();
      if(brushColor >= 0) {
         par1NBTTagCompound.setByte("BrushColor", Byte.valueOf((byte)brushColor).byteValue());
      }

   }

   protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      if(par1NBTTagCompound.hasKey("CustomName") && par1NBTTagCompound.getString("CustomName").length() > 0) {
         this.setCustomNameTag(par1NBTTagCompound.getString("CustomName"));
      }

      if(par1NBTTagCompound.hasKey("BrushColor") && par1NBTTagCompound.getByte("BrushColor") >= 0) {
         this.setBrushColor(par1NBTTagCompound.getByte("BrushColor"));
      }

   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      return 0.0F;
   }

   public boolean interactFirst(EntityPlayer player) {
      if(super.riddenByEntity != null && super.riddenByEntity instanceof EntityPlayer && super.riddenByEntity != player) {
         return true;
      } else if(!super.worldObj.isRemote && player.getHeldItem() != null && player.getHeldItem().getItem() == Items.dye) {
         ItemStack itemstack = player.getHeldItem();
         int i = BlockColored.func_150032_b(itemstack.getItemDamage());
         this.setBrushColor(i);
         if(!player.capabilities.isCreativeMode) {
            --itemstack.stackSize;
         }

         if(itemstack.stackSize <= 0) {
            player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
         }

         return true;
      } else {
         if(!super.worldObj.isRemote) {
            this.riderHasOwlFamiliar = Familiar.hasActiveBroomMasteryFamiliar(player);
            this.riderHasSoaringBrew = InfusedBrewEffect.Soaring.isActive(player);
            player.mountEntity(this);
         }

         return true;
      }
   }

   public void setDamageTaken(float par1) {
      super.dataWatcher.updateObject(19, Float.valueOf(par1));
   }

   public float getDamageTaken() {
      return super.dataWatcher.getWatchableObjectFloat(19);
   }

   public void setTimeSinceHit(int par1) {
      super.dataWatcher.updateObject(17, Integer.valueOf(par1));
   }

   public int getTimeSinceHit() {
      return super.dataWatcher.getWatchableObjectInt(17);
   }

   public void setForwardDirection(int par1) {
      super.dataWatcher.updateObject(18, Integer.valueOf(par1));
   }

   public int getForwardDirection() {
      return super.dataWatcher.getWatchableObjectInt(18);
   }

   @SideOnly(Side.CLIENT)
   public void func_70270_d(boolean par1) {
      this.field_70279_a = par1;
   }

   public static class EventHooks {

      @SubscribeEvent
      public void onLivingFall(LivingFallEvent event) {
         if(event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            if(player.isRiding() && player.ridingEntity instanceof EntityBroom) {
               event.distance = 0.0F;
            }
         }

      }
   }
}
