package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityDeath extends EntityMob implements IBossDisplayData, IHandleDT {

   private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("e942c510-c256-11e3-8a33-0800200c9a66");
   private static final AttributeModifier attackingSpeedBoostModifier = (new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 6.199999809265137D, 0)).setSaved(false);
   private int teleportDelay;
   private int stareTimer;
   private Entity lastEntityToAttack;
   private boolean isAggressive;


   public EntityDeath(World par1World) {
      super(par1World);
      this.setSize(0.6F, 1.8F);
      super.stepHeight = 1.0F;
      super.isImmuneToFire = true;
      super.experienceValue = 80;
   }

   protected int decreaseAirSupply(int par1) {
      return par1;
   }

   protected boolean canDespawn() {
      return false;
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.death.name");
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1000.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(16, new Byte((byte)0));
      super.dataWatcher.addObject(17, new Byte((byte)0));
      super.dataWatcher.addObject(18, new Byte((byte)0));
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
   }

   protected Entity findPlayerToAttack() {
      EntityPlayer entityplayer = super.worldObj.getClosestVulnerablePlayerToEntity(this, 64.0D);
      if(entityplayer != null) {
         if(this.shouldAttackPlayer(entityplayer)) {
            this.isAggressive = true;
            if(this.stareTimer == 0) {
               super.worldObj.playSoundAtEntity(entityplayer, "mob.wither.spawn", 1.0F, 1.0F);
            }

            if(this.stareTimer++ == 5) {
               this.stareTimer = 0;
               this.setScreaming(true);
               return entityplayer;
            }
         } else {
            this.stareTimer = 0;
         }
      }

      return null;
   }

   private boolean shouldAttackPlayer(EntityPlayer par1EntityPlayer) {
      ItemStack itemstack = par1EntityPlayer.inventory.armorInventory[3];
      Vec3 vec3 = par1EntityPlayer.getLook(1.0F).normalize();
      Vec3 vec31 = Vec3.createVectorHelper(super.posX - par1EntityPlayer.posX, super.boundingBox.minY + (double)(super.height / 2.0F) - (par1EntityPlayer.posY + (double)par1EntityPlayer.getEyeHeight()), super.posZ - par1EntityPlayer.posZ);
      double d0 = vec31.lengthVector();
      vec31 = vec31.normalize();
      double d1 = vec3.dotProduct(vec31);
      return d1 > 1.0D - 0.025D / d0;
   }

   public void onLivingUpdate() {
      if(super.ticksExisted % 20 == 0) {
         this.heal(1.0F);
      }

      if(this.lastEntityToAttack != super.entityToAttack) {
         IAttributeInstance i = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
         i.removeModifier(attackingSpeedBoostModifier);
         if(super.entityToAttack != null) {
            i.applyModifier(attackingSpeedBoostModifier);
         }
      }

      this.lastEntityToAttack = super.entityToAttack;

      for(int var2 = 0; var2 < 2; ++var2) {
         super.worldObj.spawnParticle("portal", super.posX + (super.rand.nextDouble() - 0.5D) * (double)super.width, super.posY + super.rand.nextDouble() * (double)super.height - 0.25D, super.posZ + (super.rand.nextDouble() - 0.5D) * (double)super.width, (super.rand.nextDouble() - 0.5D) * 2.0D, -super.rand.nextDouble(), (super.rand.nextDouble() - 0.5D) * 2.0D);
      }

      if(this.isScreaming() && !this.isAggressive) {
         this.setScreaming(false);
      }

      super.isJumping = false;
      if(super.entityToAttack != null) {
         this.faceEntity(super.entityToAttack, 100.0F, 100.0F);
      }

      if(!super.worldObj.isRemote && this.isEntityAlive()) {
         if(super.entityToAttack != null) {
            if((!(super.entityToAttack instanceof EntityPlayer) || !this.shouldAttackPlayer((EntityPlayer)super.entityToAttack)) && super.worldObj.rand.nextInt(100) != 0) {
               if(super.entityToAttack.getDistanceSqToEntity(this) > 256.0D && this.teleportDelay++ >= 30 && this.teleportToEntity(super.entityToAttack)) {
                  this.teleportDelay = 0;
               }
            } else {
               if(super.entityToAttack.getDistanceSqToEntity(this) < 16.0D) {
                  this.teleportRandomly();
               }

               this.teleportDelay = 0;
            }
         } else {
            this.setScreaming(false);
            this.teleportDelay = 0;
         }
      }

      if(super.worldObj.rand.nextDouble() < 0.05D && this.getAttackTarget() != null && (this.getAttackTarget().isAirBorne || this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer)this.getAttackTarget()).capabilities.isFlying) && !this.getAttackTarget().isPotionActive(Potion.moveSlowdown)) {
         this.getAttackTarget().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 5));
      }

      super.onLivingUpdate();
   }

   protected boolean teleportRandomly() {
      double d0 = super.posX + (super.rand.nextDouble() - 0.5D) * 32.0D;
      double d1 = super.posY + (double)(super.rand.nextInt(64) - 32);
      double d2 = super.posZ + (super.rand.nextDouble() - 0.5D) * 32.0D;
      return this.teleportTo(d0, d1, d2);
   }

   protected boolean teleportToEntity(Entity par1Entity) {
      Vec3 vec3 = Vec3.createVectorHelper(super.posX - par1Entity.posX, super.boundingBox.minY + (double)(super.height / 2.0F) - par1Entity.posY + (double)par1Entity.getEyeHeight(), super.posZ - par1Entity.posZ);
      vec3 = vec3.normalize();
      double d0 = 16.0D;
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

   protected String getLivingSound() {
      return null;
   }

   protected String getHurtSound() {
      return "mob.skeleton.hurt";
   }

   protected String getDeathSound() {
      return "mob.skeleton.death";
   }

   protected Item getDropItem() {
      return Items.bone;
   }

   protected void dropFewItems(boolean par1, int par2) {
      this.entityDropItem(new ItemStack(Items.skull, 1, 0), 0.0F);
      this.entityDropItem(new ItemStack(Items.bone, 5, 0), 0.0F);
      Enchantment enchantment = Enchantment.enchantmentsBookList[super.rand.nextInt(Enchantment.enchantmentsBookList.length)];
      int k = MathHelper.getRandomIntegerInRange(super.rand, Math.min(enchantment.getMinLevel() + 2, enchantment.getMaxLevel()), enchantment.getMaxLevel());
      ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, k));
      this.entityDropItem(itemstack, 0.0F);
      if(super.worldObj.rand.nextInt(4) == 0) {
         ItemStack sword = new ItemStack(Items.diamond_sword);
         EnchantmentHelper.addRandomEnchantment(super.worldObj.rand, sword, 40);
         sword.setStackDisplayName(Witchery.resource("item.witchery.swordofdeath.customname"));
         this.entityDropItem(sword, 0.0F);
      }

      switch(super.worldObj.rand.nextInt(5)) {
      case 0:
         this.entityDropItem(new ItemStack(Items.saddle), 0.0F);
         this.entityDropItem(Witchery.Items.GENERIC.itemBinkyHead.createStack(), 0.0F);
         break;
      case 1:
         this.entityDropItem(new ItemStack(Witchery.Items.DEATH_HOOD), 0.0F);
         break;
      case 2:
         this.entityDropItem(new ItemStack(Witchery.Items.DEATH_ROBE), 0.0F);
         break;
      case 3:
         this.entityDropItem(new ItemStack(Witchery.Items.DEATH_FEET), 0.0F);
         break;
      case 4:
         this.entityDropItem(new ItemStack(Witchery.Items.DEATH_HAND), 0.0F);
      }

   }

   public float getCapDT(DamageSource source, float damage) {
      return 15.0F;
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      if(this.isEntityInvulnerable()) {
         return false;
      } else {
         this.setScreaming(true);
         if(par1DamageSource instanceof EntityDamageSource && par1DamageSource.getEntity() instanceof EntityPlayer) {
            this.isAggressive = true;
         }

         if(par1DamageSource instanceof EntityDamageSourceIndirect) {
            this.isAggressive = false;

            for(int i = 0; i < 64; ++i) {
               if(this.teleportRandomly()) {
                  return true;
               }
            }

            return super.attackEntityFrom(par1DamageSource, Math.min(par2, 15.0F));
         } else {
            return super.attackEntityFrom(par1DamageSource, Math.min(par2, 15.0F));
         }
      }
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

   public void onDeath(DamageSource par1DamageSource) {
      super.onDeath(par1DamageSource);
      if(!super.worldObj.isRemote) {
         ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 2.0D, 16);
      }

      this.setDead();
   }

   public boolean isScreaming() {
      return super.dataWatcher.getWatchableObjectByte(18) > 0;
   }

   public void setScreaming(boolean par1) {
      super.dataWatcher.updateObject(18, Byte.valueOf((byte)(par1?1:0)));
   }

}
