package com.emoniph.witchery.entity;

import com.emoniph.witchery.entity.EntitySummonedUndead;
import com.emoniph.witchery.util.EntityUtil;
import net.minecraft.enchantment.EnchantmentHelper;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntitySpectre extends EntitySummonedUndead {

   public EntitySpectre(World par1World) {
      super(par1World);
      this.getNavigator().setAvoidsWater(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityLivingBase.class, 1.0D, true));
      super.tasks.addTask(3, new EntityAIMoveTowardsTarget(this, 1.0D, 32.0F));
      super.tasks.addTask(4, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(6, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
   }

   protected void entityInit() {
      super.entityInit();
   }

   public int getTotalArmorValue() {
      int i = super.getTotalArmorValue() + 2;
      if(i > 20) {
         i = 20;
      }

      return i;
   }

   protected boolean isAIEnabled() {
      return true;
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
   }

   public void onUpdate() {
      super.onUpdate();
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      int i = 0;
      if(par1Entity instanceof EntityLivingBase) {
         EntityLivingBase flag = (EntityLivingBase)par1Entity;
         float j = flag.getMaxHealth();
         f = Math.max(j * 0.15F, 1.0F);
      }

      if(par1Entity instanceof EntityLivingBase) {
         f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)par1Entity);
         i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)par1Entity);
      }

      boolean flag1 = EntityUtil.touchOfDeath(par1Entity, this, f);
      if(flag1) {
         if(i > 0) {
            par1Entity.addVelocity((double)(-MathHelper.sin(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F));
            super.motionX *= 0.6D;
            super.motionZ *= 0.6D;
         }

         int j1 = EnchantmentHelper.getFireAspectModifier(this);
         if(j1 > 0) {
            par1Entity.setFire(j1 * 4);
         }
      }

      return flag1;
   }

   protected String getLivingSound() {
      return "witchery:mob.spectre.spectre_say";
   }

   protected String getHurtSound() {
      return "witchery:mob.spectre.spectre_die";
   }

   protected String getDeathSound() {
      return "witchery:mob.spectre.spectre_die";
   }

   protected void addRandomArmor() {
      if(super.rand.nextFloat() < 0.15F * super.worldObj.func_147462_b(super.posX, super.posY, super.posZ)) {
         int i = super.rand.nextInt(2);
         float f = super.worldObj.difficultySetting == EnumDifficulty.HARD?0.1F:0.25F;
         if(super.rand.nextFloat() < 0.095F) {
            ++i;
         }

         if(super.rand.nextFloat() < 0.095F) {
            ++i;
         }

         if(super.rand.nextFloat() < 0.095F) {
            ++i;
         }

         for(int j = 3; j >= 2; --j) {
            ItemStack itemstack = this.func_130225_q(j);
            if(j < 3 && super.rand.nextFloat() < f) {
               break;
            }

            if(itemstack == null) {
               Item item = getArmorItemForSlot(j + 1, i);
               if(item != null) {
                  this.setCurrentItemOrArmor(j + 1, new ItemStack(item));
               }
            }
         }
      }

   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.spectre.name");
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      IEntityLivingData par1EntityLivingData1 = super.onSpawnWithEgg(par1EntityLivingData);
      this.addRandomArmor();
      this.enchantEquipment();
      this.setObscured(true);
      return (IEntityLivingData)par1EntityLivingData1;
   }
}
