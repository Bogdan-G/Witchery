package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityWitchProjectile;
import com.emoniph.witchery.entity.IOwnable;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityBabaYaga extends EntityMob implements IBossDisplayData, IRangedAttackMob, IEntitySelector, IOwnable, IHandleDT {

   private static final UUID field_110184_bp = UUID.fromString("ab0df555-0786-4951-a8df-ca61749f164e");
   private static final AttributeModifier field_110185_bq = (new AttributeModifier(field_110184_bp, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
   private static final int[] witchDrops = new int[]{Witchery.Items.GENERIC.itemSpectralDust.damageValue, Witchery.Items.GENERIC.itemBatWool.damageValue, Witchery.Items.GENERIC.itemToeOfFrog.damageValue, Witchery.Items.GENERIC.itemOwletsWing.damageValue, Witchery.Items.GENERIC.itemDogTongue.damageValue, Witchery.Items.GENERIC.itemBrewOfVines.damageValue, Witchery.Items.GENERIC.itemBrewOfSprouting.damageValue, Witchery.Items.GENERIC.itemBrewOfHitchcock.damageValue, Witchery.Items.GENERIC.itemBrewOfCursedLeaping.damageValue, Witchery.Items.GENERIC.itemBrewOfFrogsTongue.damageValue};
   private int witchAttackTimer;
   private static final double MAX_HEALTH = 500.0D;
   long ticksSinceTeleport = 0L;


   public EntityBabaYaga(World par1World) {
      super(par1World);
      this.getNavigator().setAvoidsWater(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
      super.tasks.addTask(2, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(3, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false, true, this));
      super.experienceValue = 70;
   }

   protected void entityInit() {
      super.entityInit();
      this.getDataWatcher().addObject(21, Byte.valueOf((byte)0));
      super.dataWatcher.addObject(17, "");
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
      super.dataWatcher.updateObject(17, par1Str);
   }

   public EntityPlayer getOwnerEntity() {
      return super.worldObj.getPlayerEntityByName(this.getOwnerName());
   }

   public boolean isEntityApplicable(Entity entity) {
      if(entity != null && entity instanceof EntityPlayer) {
         String ownerName = this.getOwnerName();
         boolean isOwned = ((EntityPlayer)entity).getCommandSenderName().equalsIgnoreCase(ownerName);
         return !isOwned;
      } else {
         return true;
      }
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.babayaga.name");
   }

   public int getTotalArmorValue() {
      return 4;
   }

   protected String getLivingSound() {
      return "witchery:mob.baba.baba_living";
   }

   protected String getHurtSound() {
      return "mob.witch.hurt";
   }

   protected String getDeathSound() {
      return "witchery:mob.baba.baba_death";
   }

   public void setAggressive(boolean par1) {
      this.getDataWatcher().updateObject(21, Byte.valueOf((byte)(par1?1:0)));
   }

   public boolean getAggressive() {
      return this.getDataWatcher().getWatchableObjectByte(21) == 1;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(500.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
   }

   protected int decreaseAirSupply(int par1) {
      return par1;
   }

   protected void fall(float par1) {}

   public boolean isAIEnabled() {
      return true;
   }

   public void onLivingUpdate() {
      if(!super.worldObj.isRemote) {
         if(this.getAggressive()) {
            if(this.witchAttackTimer-- <= 0) {
               this.setAggressive(false);
               ItemStack owner = this.getHeldItem();
               this.setCurrentItemOrArmor(0, (ItemStack)null);
               if(owner != null && owner.getItem() == Items.potionitem) {
                  List distance = Items.potionitem.getEffects(owner);
                  if(distance != null) {
                     Iterator iterator = distance.iterator();

                     while(iterator.hasNext()) {
                        PotionEffect l = (PotionEffect)iterator.next();
                        this.addPotionEffect(new PotionEffect(l));
                     }
                  }
               }

               this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(field_110185_bq);
            }
         } else {
            short var7 = -1;
            if(super.rand.nextFloat() < 0.15F && this.isBurning() && !this.isPotionActive(Potion.fireResistance)) {
               var7 = 16307;
            } else if(super.rand.nextFloat() < 0.01F && this.getHealth() < this.getMaxHealth()) {
               var7 = 16341;
            } else if(super.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
               var7 = 16274;
            } else if(super.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D) {
               var7 = 16274;
            }

            if(var7 > -1) {
               this.setCurrentItemOrArmor(0, new ItemStack(Items.potionitem, 1, var7));
               this.witchAttackTimer = this.getHeldItem().getMaxItemUseDuration() - 20;
               this.setAggressive(true);
               IAttributeInstance var8 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
               var8.removeModifier(field_110185_bq);
               var8.applyModifier(field_110185_bq);
            }
         }

         if(super.rand.nextFloat() < 7.5E-4F) {
            super.worldObj.setEntityState(this, (byte)15);
         }

         if((this.getNavigator().noPath() || super.worldObj.rand.nextDouble() < 0.02D) && this.getAttackTarget() != null && (long)super.ticksExisted - this.ticksSinceTeleport > 100L) {
            this.ticksSinceTeleport = (long)super.ticksExisted;
            this.teleportToEntity(this.getAttackTarget());
         }

         if(super.worldObj.rand.nextDouble() < 0.05D && this.getAttackTarget() != null && (this.getAttackTarget().isAirBorne || this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer)this.getAttackTarget()).capabilities.isFlying) && !this.getAttackTarget().isPotionActive(Potion.moveSlowdown)) {
            this.getAttackTarget().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 5));
         }

         EntityPlayer var10 = this.getOwnerEntity();
         if(var10 != null) {
            double var9 = this.getDistanceSqToEntity(var10);
            if(var9 < 64.0D && super.ticksExisted % 100 == 0) {
               int var11 = super.rand.nextInt(3);
               int i1 = witchDrops[super.rand.nextInt(witchDrops.length - 3)];

               for(int j1 = 0; j1 < var11; ++j1) {
                  this.entityDropItem(new ItemStack(Witchery.Items.GENERIC, 1, i1), 0.0F);
               }
            }

            if(super.ticksExisted > 600) {
               this.setDead();
               ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 2.0D, 16);
            }
         }
      }

      super.onLivingUpdate();
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

   protected float applyPotionDamageCalculations(DamageSource par1DamageSource, float par2) {
      par2 = super.applyPotionDamageCalculations(par1DamageSource, par2);
      if(par1DamageSource.getEntity() == this) {
         par2 = 0.0F;
      }

      if(par1DamageSource.isMagicDamage()) {
         par2 = (float)((double)par2 * 0.15D);
      }

      return par2;
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if(par1 == 15) {
         for(int i = 0; i < super.rand.nextInt(35) + 10; ++i) {
            super.worldObj.spawnParticle("witchMagic", super.posX + super.rand.nextGaussian() * 0.12999999523162842D, super.boundingBox.maxY + 0.5D + super.rand.nextGaussian() * 0.12999999523162842D, super.posZ + super.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D);
         }
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      boolean result = super.attackEntityFrom(source, Math.min(damage, 15.0F));
      if(!super.worldObj.isRemote && source.getEntity() != null && source.getEntity() instanceof EntityLiving) {
         EntityLiving player = (EntityLiving)source.getEntity();
         if(player.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD || player instanceof EntityTameable && !((EntityTameable)player).isTamed()) {
            EntityCaveSpider spider = new EntityCaveSpider(super.worldObj);
            spider.setLocationAndAngles(player.posX, player.posY, player.posZ, player.rotationPitch, player.rotationYaw);
            EntityLivingBase target = this.getAttackTarget();
            spider.setAttackTarget(target);
            spider.setRevengeTarget(target);
            spider.setTarget(target);
            super.worldObj.spawnEntityInWorld(spider);
            ParticleEffect.MOB_SPELL.send(SoundEffect.RANDOM_POP, spider, 2.0D, 2.0D, 16);
            player.setDead();
         }
      }

      if(!super.worldObj.isRemote && source.getEntity() != null && source.getEntity() instanceof EntityPlayer) {
         EntityPlayer player1 = (EntityPlayer)source.getEntity();
         if(!CreatureUtil.isWoodenDamage(source)) {
            player1.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, player1), damage * 0.25F);
         }
      }

      return result;
   }

   public float getCapDT(DamageSource source, float damage) {
      return 15.0F;
   }

   public void onDeath(DamageSource par1DamageSource) {
      super.onDeath(par1DamageSource);
      if(!super.worldObj.isRemote) {
         ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 2.0D, 16);
      }

      this.setDead();
   }

   protected void dropFewItems(boolean par1, int par2) {
      int j = super.rand.nextInt(3) + 2;

      int k;
      for(int enchantment = 0; enchantment < j; ++enchantment) {
         k = super.rand.nextInt(3) + 1;
         int itemstack = witchDrops[super.rand.nextInt(witchDrops.length)];
         if(par2 > 0) {
            k += super.rand.nextInt(par2 + 1);
         }

         for(int j1 = 0; j1 < k; ++j1) {
            this.entityDropItem(new ItemStack(Witchery.Items.GENERIC, 1, itemstack), 0.0F);
         }
      }

      Enchantment var8 = Enchantment.enchantmentsBookList[super.rand.nextInt(Enchantment.enchantmentsBookList.length)];
      k = MathHelper.getRandomIntegerInRange(super.rand, Math.min(var8.getMinLevel() + 2, var8.getMaxLevel()), var8.getMaxLevel());
      ItemStack var9 = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(var8, k));
      this.entityDropItem(var9, 0.0F);
      this.entityDropItem(new ItemStack(Witchery.Items.BABAS_HAT), 0.0F);
   }

   public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2) {
      if(!this.getAggressive()) {
         if(super.worldObj.rand.nextInt(3) == 0) {
            ItemGeneral.SubItem entitypotion = null;
            switch(super.worldObj.rand.nextInt(12)) {
            case 0:
            case 1:
               entitypotion = Witchery.Items.GENERIC.itemBrewOfWebs;
               break;
            case 2:
            case 3:
               entitypotion = Witchery.Items.GENERIC.itemBrewOfThorns;
               break;
            case 4:
            case 5:
               entitypotion = Witchery.Items.GENERIC.itemBrewOfFrogsTongue;
               break;
            case 6:
            case 7:
               entitypotion = Witchery.Items.GENERIC.itemBrewOfInk;
               break;
            case 8:
            case 9:
               entitypotion = Witchery.Items.GENERIC.itemBrewOfHitchcock;
               break;
            case 10:
               entitypotion = Witchery.Items.GENERIC.itemBrewOfBats;
               break;
            case 11:
               entitypotion = Witchery.Items.GENERIC.itemBrewOfWasting;
               break;
            default:
               return;
            }

            EntityWitchProjectile d0 = new EntityWitchProjectile(super.worldObj, this, entitypotion);
            d0.rotationPitch -= -20.0F;
            double d01 = par1EntityLivingBase.posX + par1EntityLivingBase.motionX - super.posX;
            double d11 = par1EntityLivingBase.posY + (double)par1EntityLivingBase.getEyeHeight() - 1.100000023841858D - super.posY;
            double d21 = par1EntityLivingBase.posZ + par1EntityLivingBase.motionZ - super.posZ;
            float f11 = MathHelper.sqrt_double(d01 * d01 + d21 * d21);
            d0.setThrowableHeading(d01, d11 + (double)(f11 * 0.2F), d21, 0.75F, 8.0F);
            super.worldObj.spawnEntityInWorld(d0);
         } else {
            EntityPotion entitypotion1 = new EntityPotion(super.worldObj, this, 32732);
            entitypotion1.rotationPitch -= -20.0F;
            double d02 = par1EntityLivingBase.posX + par1EntityLivingBase.motionX - super.posX;
            double d1 = par1EntityLivingBase.posY + (double)par1EntityLivingBase.getEyeHeight() - 1.100000023841858D - super.posY;
            double d2 = par1EntityLivingBase.posZ + par1EntityLivingBase.motionZ - super.posZ;
            float f1 = MathHelper.sqrt_double(d02 * d02 + d2 * d2);
            if(f1 >= 8.0F && !par1EntityLivingBase.isPotionActive(Potion.moveSlowdown)) {
               entitypotion1.setPotionDamage(32698);
            } else if(par1EntityLivingBase.getHealth() >= 8.0F && !par1EntityLivingBase.isPotionActive(Potion.poison)) {
               entitypotion1.setPotionDamage(32660);
            } else if(f1 <= 3.0F && !par1EntityLivingBase.isPotionActive(Potion.weakness) && super.rand.nextFloat() < 0.25F) {
               entitypotion1.setPotionDamage(32696);
            }

            entitypotion1.setThrowableHeading(d02, d1 + (double)(f1 * 0.2F), d2, 0.75F, 8.0F);
            super.worldObj.spawnEntityInWorld(entitypotion1);
         }
      }

   }

}
