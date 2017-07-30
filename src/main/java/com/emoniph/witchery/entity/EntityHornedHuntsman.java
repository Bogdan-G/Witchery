package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityHornedHuntsman extends EntityMob implements IBossDisplayData, IRangedAttackMob, IHandleDT {

   private int attackTimer;
   private boolean explosiveEntrance;
   long ticksSinceTeleport = 0L;


   public EntityHornedHuntsman(World par1World) {
      super(par1World);
      this.setSize(1.4F, 3.2F);
      super.isImmuneToFire = true;
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, true));
      super.tasks.addTask(3, new EntityAIMoveTowardsTarget(this, 1.0D, 48.0F));
      super.tasks.addTask(4, new EntityAIArrowAttack(this, 1.0D, 20, 60, 30.0F));
      super.tasks.addTask(5, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      super.tasks.addTask(7, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      super.experienceValue = 70;
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(16, Byte.valueOf((byte)0));
      super.dataWatcher.addObject(17, Integer.valueOf(0));
      super.dataWatcher.addObject(20, new Integer(0));
   }

   public void causeExplosiveEntrance() {
      this.explosiveEntrance = true;
   }

   public int getTotalArmorValue() {
      return 4;
   }

   public void setInWeb() {}

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.hornedHuntsman.name");
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void updateAITick() {
      super.updateAITick();
   }

   public int func_82212_n() {
      return super.dataWatcher.getWatchableObjectInt(20);
   }

   public void func_82215_s(int par1) {
      super.dataWatcher.updateObject(20, Integer.valueOf(par1));
   }

   public void func_82206_m() {
      this.func_82215_s(150);
      this.setHealth(this.getMaxHealth() / 4.0F);
   }

   protected void updateAITasks() {
      if(this.func_82212_n() > 0) {
         int i = this.func_82212_n() - 1;
         if(i <= 0) {
            if(this.explosiveEntrance) {
               super.worldObj.newExplosion(this, super.posX, super.posY + (double)this.getEyeHeight(), super.posZ, 6.0F, false, super.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
            }

            super.worldObj.playBroadcastSound(1013, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
         }

         this.func_82215_s(i);
         if(super.ticksExisted % 10 == 0) {
            this.heal(20.0F);
         }
      } else {
         super.updateAITasks();
         if(super.ticksExisted % 20 == 0) {
            this.heal(1.0F);
         }

         if(super.ticksExisted % 20 == 0 && super.worldObj.rand.nextInt(5) == 0 && this.getAttackTarget() != null && !super.worldObj.isRemote && this.getEntitySenses().canSee(this.getAttackTarget())) {
            double wolf = this.getDistanceSq(this.getAttackTarget().posX, this.getAttackTarget().boundingBox.minY, this.getAttackTarget().posZ);
            this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), 30.0F, 30.0F);
            float range = 30.0F;
            float f = MathHelper.sqrt_double(wolf) / range;
            float f1 = f;
            if(f < 0.1F) {
               f1 = 0.1F;
            }

            if(f1 > 1.0F) {
               f1 = 1.0F;
            }

            this.attackEntityWithRangedAttack(this.getAttackTarget(), f);
         }

         if(super.ticksExisted % (200 + super.worldObj.rand.nextInt(4) * 100) == 0 && this.getAttackTarget() != null && this.getDistanceSqToEntity(this.getAttackTarget()) <= 256.0D && !super.worldObj.isRemote && this.getEntitySenses().canSee(this.getAttackTarget())) {
            EntityWolf wolf1 = new EntityWolf(super.worldObj);
            wolf1.setLocationAndAngles(super.posX - 0.5D + super.worldObj.rand.nextDouble(), super.posY, super.posZ - 0.5D + super.worldObj.rand.nextDouble(), super.rotationYawHead, super.rotationPitch);
            wolf1.setAngry(true);
            wolf1.setAttackTarget(this.getAttackTarget());
            wolf1.addPotionEffect(new PotionEffect(Potion.resistance.id, 20000, 1));
            ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_FIZZ, wolf1, 2.0D, 2.0D, 10);
            super.worldObj.spawnEntityInWorld(wolf1);
         }

         if(!super.worldObj.isRemote && this.getNavigator().noPath() && this.getAttackTarget() != null && (long)super.ticksExisted - this.ticksSinceTeleport > 200L) {
            this.ticksSinceTeleport = (long)super.ticksExisted;
            this.teleportToEntity(this.getAttackTarget());
         }
      }

   }

   protected boolean teleportToEntity(Entity par1Entity) {
      Vec3 vec3 = Vec3.createVectorHelper(super.posX - par1Entity.posX, super.boundingBox.minY + (double)(super.height / 2.0F) - par1Entity.posY + (double)par1Entity.getEyeHeight(), super.posZ - par1Entity.posZ);
      vec3 = vec3.normalize();
      double d0 = 8.0D;
      double d1 = super.posX + (super.rand.nextDouble() - 0.5D) * 8.0D - vec3.xCoord * d0;
      double d2 = super.posY + (double)(super.rand.nextInt(16) - 8) - vec3.yCoord * d0;
      double d3 = super.posZ + (super.rand.nextDouble() - 0.5D) * 8.0D - vec3.zCoord * d0;
      return this.teleportTo(d1, d2, d3);
   }

   protected boolean teleportTo(double par1, double par3, double par5) {
      double d3 = super.posX;
      double d4 = super.posY;
      double d5 = super.posZ;
      super.posX = par1;
      super.posY = par3;
      super.posZ = par5;
      boolean flag = false;
      int i = MathHelper.floor_double(super.posX);
      int j = MathHelper.floor_double(super.posY);
      int k = MathHelper.floor_double(super.posZ);
      if(super.worldObj.blockExists(i, j, k)) {
         boolean short1 = false;

         while(!short1 && j > 0) {
            Block l = super.worldObj.getBlock(i, j - 1, k);
            if(l.getMaterial().blocksMovement()) {
               short1 = true;
            } else {
               --super.posY;
               --j;
            }
         }

         if(short1) {
            this.setPosition(super.posX, super.posY, super.posZ);
            if(super.worldObj.getCollidingBoundingBoxes(this, super.boundingBox).isEmpty() && !super.worldObj.isAnyLiquid(super.boundingBox)) {
               flag = true;
            }
         }
      }

      if(!flag) {
         this.setPosition(d3, d4, d5);
         return false;
      } else {
         short var30 = 128;

         for(int var31 = 0; var31 < var30; ++var31) {
            double d6 = (double)var31 / ((double)var30 - 1.0D);
            float f = (super.rand.nextFloat() - 0.5F) * 0.2F;
            float f1 = (super.rand.nextFloat() - 0.5F) * 0.2F;
            float f2 = (super.rand.nextFloat() - 0.5F) * 0.2F;
            double d7 = d3 + (super.posX - d3) * d6 + (super.rand.nextDouble() - 0.5D) * (double)super.width * 2.0D;
            double d8 = d4 + (super.posY - d4) * d6 + super.rand.nextDouble() * (double)super.height;
            double d9 = d5 + (super.posZ - d5) * d6 + (super.rand.nextDouble() - 0.5D) * (double)super.width * 2.0D;
            super.worldObj.spawnParticle("portal", d7, d8, d9, (double)f, (double)f1, (double)f2);
         }

         super.worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
         this.playSound("mob.endermen.portal", 1.0F, 1.0F);
         return true;
      }
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(400.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(50.0D);
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

   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      return super.attackEntityFrom(par1DamageSource, Math.min(par2, 15.0F));
   }

   public float getCapDT(DamageSource source, float damage) {
      return 15.0F;
   }

   public boolean canAttackClass(Class par1Class) {
      return super.canAttackClass(par1Class);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setBoolean("PlayerCreated", this.isPlayerCreated());
      par1NBTTagCompound.setInteger("Invul", this.func_82212_n());
      par1NBTTagCompound.setBoolean("explosiveEntrance", this.explosiveEntrance);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setPlayerCreated(par1NBTTagCompound.getBoolean("PlayerCreated"));
      this.func_82215_s(par1NBTTagCompound.getInteger("Invul"));
      if(par1NBTTagCompound.hasKey("explosiveEntrance")) {
         this.explosiveEntrance = par1NBTTagCompound.getBoolean("explosiveEntrance");
      } else {
         this.explosiveEntrance = false;
      }

   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      this.attackTimer = 10;
      super.worldObj.setEntityState(this, (byte)4);
      boolean flag = par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + super.rand.nextInt(15)));
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

   @SideOnly(Side.CLIENT)
   public int getAttackTimer() {
      return this.attackTimer;
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   protected String getLivingSound() {
      return "mob.enderdragon.growl";
   }

   protected String getHurtSound() {
      return "mob.horse.zombie.hit";
   }

   protected String getDeathSound() {
      return "mob.wither.death";
   }

   protected void func_145780_a(int par1, int par2, int par3, Block par4) {
      this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
   }

   protected void dropFewItems(boolean par1, int par2) {
      this.entityDropItem(new ItemStack(Items.skull, super.worldObj.rand.nextInt(3) == 0?3:2, 1), 0.0F);
      Enchantment enchantment = Enchantment.enchantmentsBookList[super.rand.nextInt(Enchantment.enchantmentsBookList.length)];
      int k = MathHelper.getRandomIntegerInRange(super.rand, Math.min(enchantment.getMinLevel() + 2, enchantment.getMaxLevel()), enchantment.getMaxLevel());
      ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, k));
      this.entityDropItem(itemstack, 0.0F);
      this.entityDropItem(Witchery.Items.GENERIC.itemInfernalBlood.createStack(), 0.0F);
      if(super.worldObj.rand.nextInt(4) == 0) {
         this.entityDropItem(new ItemStack(Witchery.Items.HUNTSMANS_SPEAR), 0.0F);
      }

   }

   protected Item getDropItem() {
      return null;
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

   protected boolean canDespawn() {
      return false;
   }

   public void attackEntityWithRangedAttack(EntityLivingBase targetEntity, float par2) {
      EntityArrow entityarrow = new EntityArrow(super.worldObj, this, targetEntity, 1.6F, (float)(14 - super.worldObj.difficultySetting.getDifficultyId() * 4));
      entityarrow.setDamage((double)(par2 * 8.0F) + super.rand.nextGaussian() * 0.25D + (double)((float)super.worldObj.difficultySetting.getDifficultyId() * 0.11F));
      entityarrow.setKnockbackStrength(2);
      this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
      super.worldObj.spawnEntityInWorld(entityarrow);
   }
}
