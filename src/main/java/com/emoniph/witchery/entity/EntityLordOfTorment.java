package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.dimension.WorldProviderTorment;
import com.emoniph.witchery.entity.EntityFlyingMob;
import com.emoniph.witchery.entity.EntitySoulfire;
import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.entity.ai.EntityAIFlyerArrowAttack;
import com.emoniph.witchery.entity.ai.EntityAIFlyerLand;
import com.emoniph.witchery.entity.ai.EntityAIFlyerWander;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.DemonicDamageSource;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityLordOfTorment extends EntityFlyingMob implements IRangedAttackMob, IBossDisplayData, IHandleDT {

   private int attackTimer;
   private final HashSet attackers = new HashSet();


   public EntityLordOfTorment(World world) {
      super(world);
      this.setSize(0.6F, 1.9F);
      super.isImmuneToFire = true;
      super.experienceValue = 50;
      this.getNavigator().setCanSwim(true);
      this.getNavigator().setAvoidsWater(true);
      super.experienceValue = 80;
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIFlyerArrowAttack(this, 1.0D, 20, 60, 12.0F));
      super.tasks.addTask(5, new EntityAIFlyerLand(this, 0.8D, false));
      super.tasks.addTask(6, new EntityAIFlyerWander(this, 0.2D, 10.0D));
      super.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(8, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(500.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(50.0D);
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(16, Byte.valueOf((byte)0));
   }

   public void onUpdate() {
      super.onUpdate();
      if(super.worldObj.isRemote) {
         super.worldObj.spawnParticle(ParticleEffect.FLAME.toString(), super.posX - (double)super.width * 0.5D + super.worldObj.rand.nextDouble() * (double)super.width, 0.1D + super.posY + super.worldObj.rand.nextDouble() * 2.0D, super.posZ - (double)super.width * 0.5D + super.worldObj.rand.nextDouble() * (double)super.width, 0.0D, 0.0D, 0.0D);
      }

   }

   @SideOnly(Side.CLIENT)
   public boolean func_110182_bF() {
      return super.dataWatcher.getWatchableObjectByte(16) != 0;
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected int decreaseAirSupply(int air) {
      return air;
   }

   protected boolean canDespawn() {
      return false;
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if(this.attackTimer > 0) {
         --this.attackTimer;
      }

      if(super.worldObj.rand.nextDouble() < 0.05D && this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer)this.getAttackTarget()).capabilities.isFlying && !this.getAttackTarget().isPotionActive(Potion.moveSlowdown)) {
         this.getAttackTarget().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 5));
      }

   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte state) {
      if(state == 4) {
         this.attackTimer = 10;
         this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
      } else {
         super.handleHealthUpdate(state);
      }

   }

   public boolean attackEntityAsMob(Entity target) {
      this.attackTimer = 10;
      super.worldObj.setEntityState(this, (byte)4);
      boolean flag = target.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(7 + super.rand.nextInt(15)));
      if(flag) {
         target.motionY += 0.4000000059604645D;
      }

      this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
      return flag;
   }

   @SideOnly(Side.CLIENT)
   public int getAttackTimer() {
      return this.attackTimer;
   }

   public float getBrightness(float par1) {
      return 1.0F;
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.lordoftorment.name");
   }

   public void readFromNBT(NBTTagCompound nbtRoot) {
      super.readFromNBT(nbtRoot);
      if(nbtRoot.hasKey("WITCAttackers")) {
         NBTTagList nbtAttackers = nbtRoot.getTagList("WITCAttackers", 8);

         for(int i = 0; i < nbtAttackers.tagCount(); ++i) {
            String attacker = nbtAttackers.getStringTagAt(i);
            if(!this.attackers.contains(attacker)) {
               this.attackers.add(attacker);
            }
         }
      }

   }

   public void writeToNBT(NBTTagCompound nbtRoot) {
      super.writeToNBT(nbtRoot);
      NBTTagList nbtAttackers = new NBTTagList();
      boolean i = false;
      Iterator i$ = this.attackers.iterator();

      while(i$.hasNext()) {
         String attacker = (String)i$.next();
         nbtAttackers.appendTag(new NBTTagString(attacker));
      }

      nbtRoot.setTag("WITCAttackers", nbtAttackers);
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      if(source.isExplosion()) {
         return false;
      } else {
         if(source.getSourceOfDamage() != null && source.getSourceOfDamage() instanceof EntityPlayer) {
            EntityPlayer damageCap = (EntityPlayer)source.getSourceOfDamage();
            if(!this.attackers.contains(damageCap.getCommandSenderName())) {
               this.attackers.add(damageCap.getCommandSenderName());
            }
         }

         float damageCap1 = source instanceof DemonicDamageSource?8.0F:5.0F;
         boolean damaged = super.attackEntityFrom(source, Math.min(damage, damageCap1));
         if(!super.worldObj.isRemote && super.dimension != Config.instance().dimensionTormentID && this.getHealth() <= this.getMaxHealth() * 0.5F) {
            int tormentlevel = WorldProviderTorment.getRandomTormentLevel(super.worldObj);
            double R = 16.0D;
            double Ry = 32.0D;
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(super.posX - 16.0D, super.posY - 32.0D, super.posZ - 16.0D, super.posX + 16.0D, super.posY + 32.0D, super.posZ + 16.0D);
            List players = super.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bb);
            Iterator i$ = players.iterator();

            EntityPlayer otherPlayer;
            while(i$.hasNext()) {
               Object playerName = i$.next();
               otherPlayer = (EntityPlayer)playerName;
               WorldProviderTorment.setPlayerMustTorment(otherPlayer, 2, tormentlevel);
            }

            i$ = this.attackers.iterator();

            while(i$.hasNext()) {
               String playerName1 = (String)i$.next();
               otherPlayer = super.worldObj.getPlayerEntityByName(playerName1);
               if(otherPlayer != null && otherPlayer.dimension == super.dimension) {
                  WorldProviderTorment.setPlayerMustTorment(otherPlayer, 2, tormentlevel);
               }
            }

            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 2.0D, 16);
            this.setDead();
         }

         return damaged;
      }
   }

   public float getCapDT(DamageSource source, float damage) {
      return 5.0F;
   }

   protected String getLivingSound() {
      return "witchery:mob.torment.laugh";
   }

   protected String getHurtSound() {
      return "witchery:mob.torment.hit";
   }

   protected String getDeathSound() {
      return "witchery:mob.torment.death";
   }

   public int getTalkInterval() {
      return TimeUtil.secsToTicks(10);
   }

   protected Item getDropItem() {
      return null;
   }

   protected void dropFewItems(boolean par1, int par2) {
      Enchantment enchantment = Enchantment.enchantmentsBookList[super.rand.nextInt(Enchantment.enchantmentsBookList.length)];
      int k = MathHelper.getRandomIntegerInRange(super.rand, Math.min(enchantment.getMinLevel() + 3, enchantment.getMaxLevel()), enchantment.getMaxLevel());
      ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, k));
      this.entityDropItem(itemstack, 0.0F);
      Enchantment enchantment2 = Enchantment.enchantmentsBookList[super.rand.nextInt(Enchantment.enchantmentsBookList.length)];
      int k2 = MathHelper.getRandomIntegerInRange(super.rand, Math.min(enchantment2.getMinLevel() + 1, enchantment.getMaxLevel()), enchantment.getMaxLevel());
      ItemStack itemstack2 = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, k2));
      this.entityDropItem(itemstack, 0.0F);
      this.entityDropItem(Witchery.Items.GENERIC.itemDemonHeart.createStack(), 0.0F);
      this.entityDropItem(Witchery.Items.GENERIC.itemBrewSoulTorment.createStack(), 0.0F);
   }

   protected float getSoundVolume() {
      return 2.0F;
   }

   public boolean getCanSpawnHere() {
      return true;
   }

   public int getMaxSpawnedInChunk() {
      return 1;
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
   }

   public void attackEntityWithRangedAttack(EntityLivingBase targetEntity, float par2) {
      this.attackTimer = 10;
      super.worldObj.setEntityState(this, (byte)4);
      double d0 = targetEntity.posX - super.posX;
      double d1 = targetEntity.boundingBox.minY + (double)(targetEntity.height / 2.0F) - (super.posY + (double)(super.height / 2.0F));
      double d2 = targetEntity.posZ - super.posZ;
      float f1 = MathHelper.sqrt_float(par2) * 0.5F;
      if(!super.worldObj.isRemote) {
         super.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
         int count = super.rand.nextInt(10) == 0?9:3;
         EntitySpellEffect effect = new EntitySpellEffect(super.worldObj, this, d0 + super.rand.nextGaussian() * (double)f1, d1, d2 + super.rand.nextGaussian() * (double)f1, EffectRegistry.instance().getEffect(39), 1);
         double d8 = 1.0D;
         effect.posX = super.posX;
         effect.posY = super.posY + (double)(super.height / 2.0F);
         effect.posZ = super.posZ;
         super.worldObj.spawnEntityInWorld(effect);
         effect.setShooter(this);

         for(int i = 0; i < count; ++i) {
            EntitySoulfire fireballEntity = new EntitySoulfire(super.worldObj, this, d0 + super.rand.nextGaussian() * (double)f1, d1, d2 + super.rand.nextGaussian() * (double)f1);
            d8 = 1.0D;
            fireballEntity.posX = super.posX;
            fireballEntity.posY = super.posY + (double)(super.height / 2.0F) + 0.5D;
            fireballEntity.posZ = super.posZ;
            super.worldObj.spawnEntityInWorld(fireballEntity);
         }
      }

   }
}
