package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.Dye;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class EntityEnt extends EntityMob implements IEntitySelector {

   private int attackTimer;
   EntityPlayer fakePlayer = null;


   public EntityEnt(World par1World) {
      super(par1World);
      this.setSize(1.2F, 3.0F);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, true));
      super.tasks.addTask(3, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
      super.tasks.addTask(4, new EntityAIWander(this, 0.6D));
      super.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      super.tasks.addTask(6, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false, true, this));
      super.experienceValue = 25;
   }

   public boolean isEntityApplicable(Entity entity) {
      return true;
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(17, "");
      super.dataWatcher.addObject(16, Byte.valueOf((byte)0));
   }

   public boolean isScreaming() {
      return super.dataWatcher.getWatchableObjectByte(16) > 0;
   }

   public void setScreaming(boolean par1) {
      super.dataWatcher.updateObject(16, Byte.valueOf((byte)(par1?1:0)));
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.ent.name");
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void updateAITick() {
      super.updateAITick();
      if(!super.worldObj.isRemote && this.isEntityAlive()) {
         if(this.getAttackTarget() != null) {
            this.setScreaming(true);
         } else {
            this.setScreaming(false);
         }
      }

   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
      this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
   }

   protected int decreaseAirSupply(int par1) {
      return par1;
   }

   protected void collideWithEntity(Entity par1Entity) {
      super.collideWithEntity(par1Entity);
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if(this.attackTimer > 0) {
         --this.attackTimer;
      }

      int i;
      int j;
      int k;
      if(!super.worldObj.isRemote && super.worldObj.rand.nextInt(300) == 0) {
         try {
            i = MathHelper.floor_double(super.posX);
            j = MathHelper.floor_double(super.posY) - 1;
            k = MathHelper.floor_double(super.posZ);
            if((this.fakePlayer == null || this.fakePlayer.worldObj != super.worldObj) && super.worldObj instanceof WorldServer) {
               this.fakePlayer = new FakePlayer((WorldServer)super.worldObj, new GameProfile(UUID.randomUUID(), "[Minecraft]"));
            }

            if(this.fakePlayer != null) {
               ItemDye.applyBonemeal(Dye.BONE_MEAL.createStack(), super.worldObj, i, j, k, this.fakePlayer);
            }
         } catch (Throwable var5) {
            ;
         }
      }

      if(super.motionX * super.motionX + super.motionZ * super.motionZ > 2.500000277905201E-7D && super.rand.nextInt(5) == 0) {
         i = MathHelper.floor_double(super.posX);
         j = MathHelper.floor_double(super.posY - 0.20000000298023224D - (double)super.yOffset);
         k = MathHelper.floor_double(super.posZ);
         Block l = super.worldObj.getBlock(i, j, k);
         if(l != Blocks.air) {
            super.worldObj.spawnParticle("tilecrack_" + l + "_" + super.worldObj.getBlockMetadata(i, j, k), super.posX + ((double)super.rand.nextFloat() - 0.5D) * (double)super.width, super.boundingBox.minY + 0.1D, super.posZ + ((double)super.rand.nextFloat() - 0.5D) * (double)super.width, 4.0D * ((double)super.rand.nextFloat() - 0.5D), 0.5D, ((double)super.rand.nextFloat() - 0.5D) * 4.0D);
         }
      }

   }

   public boolean canAttackClass(Class par1Class) {
      return super.canAttackClass(par1Class);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      if(this.getOwnerName() == null) {
         par1NBTTagCompound.setString("Owner", "");
      } else {
         par1NBTTagCompound.setString("Owner", this.getOwnerName());
      }

   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      String s = par1NBTTagCompound.getString("Owner");
      if(s.length() > 0) {
         this.setOwner(s);
      }

   }

   public String getOwnerName() {
      return super.dataWatcher.getWatchableObjectString(17);
   }

   public void setOwner(String par1Str) {
      this.func_110163_bv();
      super.dataWatcher.updateObject(17, par1Str);
   }

   public EntityPlayer getOwnerEntity() {
      return super.worldObj.getPlayerEntityByName(this.getOwnerName());
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      this.attackTimer = 10;
      super.worldObj.setEntityState(this, (byte)4);
      boolean flag = super.attackEntityAsMob(par1Entity);
      if(flag) {
         par1Entity.motionY += 0.4000000059604645D;
      }

      this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
      return flag;
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if(par1 == 4) {
         this.attackTimer = 10;
         this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      Entity entity = par1DamageSource.getEntity();
      par2 = Math.min(par2, 15.0F);
      if(entity != null && entity instanceof EntityLivingBase && (par1DamageSource.damageType == "mob" || par1DamageSource.damageType == "player") && ((EntityLivingBase)entity).getHeldItem() != null && ((EntityLivingBase)entity).getHeldItem().getItem() instanceof ItemAxe) {
         par2 *= 3.0F;
      }

      return super.attackEntityFrom(par1DamageSource, par2);
   }

   @SideOnly(Side.CLIENT)
   public int getAttackTimer() {
      return this.attackTimer;
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   protected String getLivingSound() {
      return null;
   }

   protected String getHurtSound() {
      return "mob.horse.zombie.hit";
   }

   protected String getDeathSound() {
      return "mob.horse.zombie.death";
   }

   protected void func_145780_a(int par1, int par2, int par3, Block par4) {
      this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
   }

   protected void dropFewItems(boolean par1, int par2) {
      this.entityDropItem(Witchery.Items.GENERIC.itemBranchEnt.createStack(), 0.0F);
      this.entityDropItem(new ItemStack(Witchery.Blocks.SAPLING, 1, super.worldObj.rand.nextInt(3)), 0.0F);
   }

   public boolean isPlayerCreated() {
      return (super.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void setPlayerCreated(boolean par1) {
      this.func_110163_bv();
      byte b0 = super.dataWatcher.getWatchableObjectByte(16);
      if(par1) {
         super.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 1)));
      } else {
         super.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -2)));
      }

   }

   public void onDeath(DamageSource par1DamageSource) {
      super.onDeath(par1DamageSource);
   }

   protected boolean canDespawn() {
      return true;
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      return super.onSpawnWithEgg(par1EntityLivingData);
   }
}
