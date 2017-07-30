package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityMindrake extends EntityTameable implements IEntitySelector {

   public EntityMindrake(World world) {
      super(world);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
      super.tasks.addTask(3, new EntityAIWander(this, 0.8D));
      super.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(5, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIOwnerHurtTarget(this));
      super.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false, true, this));
      super.experienceValue = 0;
      this.setSize(0.6F, 0.8F);
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.mindrake.name");
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
   }

   public boolean isAIEnabled() {
      return true;
   }

   public int getMaxSafePointTries() {
      return this.getAttackTarget() == null?3:3 + (int)(this.getHealth() - 1.0F);
   }

   protected void entityInit() {
      super.entityInit();
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
   }

   public void onUpdate() {
      super.onUpdate();
   }

   protected String getLivingSound() {
      return null;
   }

   protected String getHurtSound() {
      return "mob.ghast.scream";
   }

   protected String getDeathSound() {
      return "mob.ghast.death";
   }

   public boolean isEntityApplicable(Entity target) {
      return !this.isTamed()?target instanceof EntityPlayer:false;
   }

   public boolean attackEntityAsMob(Entity entity) {
      if(!super.worldObj.isRemote) {
         super.worldObj.createExplosion(this, super.posX, super.posY, super.posZ, 1.5F, false);
         this.setDead();
         Block block = BlockUtil.getBlock(super.worldObj, super.posX, super.boundingBox.minY - 1.0D, super.posZ);
         if(block != null && (block.getMaterial() == Material.grass || block.getMaterial() == Material.ground)) {
            BlockUtil.setBlock(super.worldObj, super.posX, super.boundingBox.minY, super.posZ, super.rand.nextInt(2) == 0?Blocks.red_flower:Blocks.yellow_flower);
         }
      }

      return true;
   }

   public void onDeath(DamageSource par1DamageSource) {
      super.onDeath(par1DamageSource);
      if(!super.worldObj.isRemote) {
         super.worldObj.createExplosion(this, super.posX, super.posY, super.posZ, 1.0F, false);
      }

   }

   protected void dropFewItems(boolean par1, int par2) {
      this.entityDropItem(new ItemStack(Witchery.Items.SEEDS_MANDRAKE, super.worldObj.rand.nextDouble() <= 0.25D?2:1), 0.0F);
   }

   public EntityAgeable createChild(EntityAgeable var1) {
      return null;
   }
}
