package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemEarmuffs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityMandrake extends EntityMob {

   public EntityMandrake(World world) {
      super(world);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
      super.tasks.addTask(3, new EntityAIWander(this, 0.8D));
      super.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(5, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      super.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
      super.experienceValue = 0;
      this.setSize(0.6F, 0.9F);
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.mandrake.name");
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.65D);
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

   protected String getLivingSound() {
      return "mob.ghast.scream";
   }

   protected String getHurtSound() {
      return "mob.ghast.scream";
   }

   protected String getDeathSound() {
      return "mob.ghast.death";
   }

   public boolean attackEntityAsMob(Entity entity) {
      if(!super.worldObj.isRemote && entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         if(!player.isPotionActive(Potion.confusion) && !ItemEarmuffs.isHelmWorn(player)) {
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300, 1));
         }
      }

      return true;
   }

   protected void dropFewItems(boolean par1, int par2) {
      this.entityDropItem(Witchery.Items.GENERIC.itemMandrakeRoot.createStack(), 0.0F);
      this.entityDropItem(new ItemStack(Witchery.Items.SEEDS_MANDRAKE, super.worldObj.rand.nextDouble() <= 0.25D?2:1), 0.0F);
   }
}
