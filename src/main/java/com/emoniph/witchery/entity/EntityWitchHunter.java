package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityBolt;
import com.emoniph.witchery.entity.EntityVillageGuard;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.network.PacketSound;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.village.Village;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class EntityWitchHunter extends EntityCreature implements IRangedAttackMob, IEntitySelector {

   private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
   private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityLivingBase.class, 1.2D, false);
   private String targetPlayerName;
   private static final double HUNTER_NOTICE_CHANCE = 0.1D;
   private static final long HUNTER_DELAY = (long)TimeUtil.minsToTicks(2);
   private static final double HUNTER_TRIGGER_CHANCE = 0.01D;


   public EntityWitchHunter(World par1World) {
      super(par1World);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(5, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(6, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, false, true, this));
      super.experienceValue = 5;
      this.targetPlayerName = "";
      if(par1World != null && !par1World.isRemote) {
         this.setCombatTask();
      }

   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.witchhunter.name");
   }

   public boolean isEntityApplicable(Entity entity) {
      if(!CreatureUtil.isUndead(entity) && !CreatureUtil.isDemonic(entity) && !(entity instanceof EntityWitch) && !CreatureUtil.isWerewolf(entity)) {
         if(!(entity instanceof EntityPlayer)) {
            return false;
         } else {
            EntityPlayer player = (EntityPlayer)entity;
            return CreatureUtil.isWitch(entity) || CreatureUtil.isWerewolf(entity) || CreatureUtil.isVampire(entity) || this.targetPlayerName != null && !this.targetPlayerName.isEmpty() && player.getCommandSenderName().equals(this.targetPlayerName);
         }
      } else {
         return true;
      }
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(13, new Byte((byte)0));
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected String getLivingSound() {
      return null;
   }

   protected String getHurtSound() {
      return "mob.villager.hit";
   }

   protected String getDeathSound() {
      return "mob.villager.death";
   }

   protected void func_145780_a(int par1, int par2, int par3, Block par4) {
      this.playSound("step.grass", 0.15F, 1.0F);
   }

   public boolean attackEntityAsMob(Entity targetEntity) {
      float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      int i = 0;
      if(targetEntity instanceof EntityLivingBase) {
         f += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)targetEntity);
         i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)targetEntity);
      }

      boolean flag = targetEntity.attackEntityFrom(DamageSource.causeMobDamage(this), f);
      if(flag) {
         if(i > 0) {
            targetEntity.addVelocity((double)(-MathHelper.sin(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F));
            super.motionX *= 0.6D;
            super.motionZ *= 0.6D;
         }

         int j = EnchantmentHelper.getFireAspectModifier(this);
         if(j > 0) {
            targetEntity.setFire(j * 4);
         }

         if(targetEntity instanceof EntityLivingBase) {
            EnchantmentHelper.func_151384_a((EntityLivingBase)targetEntity, this);
         }

         EnchantmentHelper.func_151385_b(this, targetEntity);
      }

      return flag;
   }

   public void onLivingUpdate() {
      this.updateArmSwingProgress();
      float f = this.getBrightness(1.0F);
      if(f > 0.5F) {
         super.entityAge += 2;
      }

      if(!super.worldObj.isRemote && super.ticksExisted % 20 == 2 && this.isPotionActive(Potion.poison)) {
         this.removePotionEffect(Potion.poison.id);
      }

      super.onLivingUpdate();
   }

   protected String getSwimSound() {
      return "game.hostile.swim";
   }

   protected String getSplashSound() {
      return "game.hostile.swim.splash";
   }

   public void updateRidden() {
      super.updateRidden();
      if(super.ridingEntity instanceof EntityCreature) {
         EntityCreature entitycreature = (EntityCreature)super.ridingEntity;
         super.renderYawOffset = entitycreature.renderYawOffset;
      }

   }

   public boolean attackEntityFrom(DamageSource damageSource, float damage) {
      if(damageSource.getEntity() != null && (damageSource.getEntity() instanceof EntityVillageGuard || damageSource.getEntity() instanceof EntityWitchHunter)) {
         return false;
      } else if(this.isEntityInvulnerable()) {
         return false;
      } else if(super.attackEntityFrom(damageSource, Math.min(damage, 9.0F))) {
         Entity entity = damageSource.getEntity();
         if(super.riddenByEntity != entity && super.ridingEntity != entity) {
            if(entity != this) {
               super.entityToAttack = entity;
            }

            return true;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   protected String func_146067_o(int distance) {
      return distance > 4?"game.hostile.hurt.fall.big":"game.hostile.hurt.fall.small";
   }

   protected void dropFewItems(boolean par1, int par2) {
      int j = super.rand.nextInt(3 + par2);

      for(int k = 0; k < j; ++k) {
         this.entityDropItem(Witchery.Items.GENERIC.itemBoltStake.createStack(), 0.0F);
      }

   }

   protected void dropRareDrop(int par1) {
      this.entityDropItem(Witchery.Items.GENERIC.itemBoltAntiMagic.createStack(2), 0.0F);
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);
      this.setHunterType(super.worldObj.rand.nextInt(3));
      this.setCurrentItemOrArmor(0, new ItemStack(Witchery.Items.CROSSBOW_PISTOL));
      this.enchantEquipment();
      return par1EntityLivingData;
   }

   public void setCombatTask() {
      super.tasks.removeTask(this.aiAttackOnCollide);
      super.tasks.removeTask(this.aiArrowAttack);
      ItemStack itemstack = this.getHeldItem();
      if(itemstack != null && itemstack.getItem() == Witchery.Items.CROSSBOW_PISTOL) {
         super.tasks.addTask(4, this.aiArrowAttack);
      } else {
         super.tasks.addTask(4, this.aiAttackOnCollide);
      }

   }

   public void attackEntityWithRangedAttack(EntityLivingBase par1EntityLivingBase, float par2) {
      EntityBolt entityarrow = new EntityBolt(super.worldObj, this, par1EntityLivingBase, 1.6F, (float)(14 - super.worldObj.difficultySetting.getDifficultyId() * 4));
      int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
      int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
      entityarrow.setDamage((double)(par2 * 2.0F) + super.rand.nextGaussian() * 0.25D + (double)((float)super.worldObj.difficultySetting.getDifficultyId() * 0.11F));
      if(i > 0) {
         entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
      }

      if(j > 0) {
         entityarrow.setKnockbackStrength(j);
      }

      if(EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || CreatureUtil.isVampire(this.getAttackTarget()) && super.worldObj.rand.nextInt(3) == 0) {
         entityarrow.setFire(100);
      }

      if(this.getAttackTarget() != null) {
         if(CreatureUtil.isWerewolf(this.getAttackTarget())) {
            entityarrow.setBoltType(4);
         } else if(CreatureUtil.isUndead(this.getAttackTarget())) {
            entityarrow.setBoltType(3);
         } else if(super.worldObj.rand.nextInt(4) == 0) {
            entityarrow.setBoltType(2);
         }
      }

      this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
      super.worldObj.spawnEntityInWorld(entityarrow);
   }

   public int getHunterType() {
      return super.dataWatcher.getWatchableObjectByte(13);
   }

   public void setHunterType(int par1) {
      super.dataWatcher.updateObject(13, Byte.valueOf((byte)par1));
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      if(nbtRoot.hasKey("HunterType")) {
         byte b0 = nbtRoot.getByte("HunterType");
         this.setHunterType(b0);
      }

      this.targetPlayerName = nbtRoot.getString("HunterTarget");
      this.setCombatTask();
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setByte("HunterType", (byte)this.getHunterType());
      nbtRoot.setString("HunterTarget", this.targetPlayerName);
   }

   public void setCurrentItemOrArmor(int slot, ItemStack stack) {
      super.setCurrentItemOrArmor(slot, stack);
      if(!super.worldObj.isRemote && slot == 0) {
         this.setCombatTask();
      }

   }

   public double getYOffset() {
      return super.getYOffset() - 0.5D;
   }

   public static void blackMagicPerformed(EntityPlayer player) {
      if(player != null && player.worldObj != null && !player.worldObj.isRemote) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(nbtPlayer != null && nbtPlayer.getLong("WITCHunterTrigger") <= 0L && player.worldObj.rand.nextDouble() < 0.1D) {
            long totalWorldTicks = TimeUtil.getServerTimeInTicks();
            nbtPlayer.setLong("WITCHunterTrigger", totalWorldTicks);
         }
      }

   }

   public static void handleWitchHunterEffects(EntityPlayer player, long totalWorldTicks) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      if(nbtPlayer != null) {
         long triggerTimeTicks = nbtPlayer.getLong("WITCHunterTrigger");
         if(triggerTimeTicks > 0L && totalWorldTicks >= triggerTimeTicks + HUNTER_DELAY && player.worldObj.rand.nextDouble() < 0.01D || isVampireActive(player, totalWorldTicks)) {
            nbtPlayer.removeTag("WITCHunterTrigger");
            boolean MAX_SPAWNS = true;
            boolean tries = true;
            int spawned = 0;

            for(int i = 0; i < 3 && spawned < 2; ++i) {
               EntityWitchHunter creature = (EntityWitchHunter)Infusion.spawnCreature(player.worldObj, EntityWitchHunter.class, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), player, 3, 8, ParticleEffect.SMOKE, (SoundEffect)null);
               if(creature != null) {
                  ++spawned;
                  creature.targetPlayerName = player.getCommandSenderName();
                  creature.onSpawnWithEgg((IEntityLivingData)null);
                  EntityUtil.setTarget(creature, player);
               }
            }

            if(spawned > 0) {
               Witchery.packetPipeline.sendTo((IMessage)(new PacketSound(SoundEffect.WITCHERY_RANDOM_THEYCOME, player, 1.0F, 1.0F)), player);
            }
         }
      }

   }

   private static boolean isVampireActive(EntityPlayer player, long totalWorldTicks) {
      if(Config.instance().vampireHunterSpawnChance > 0.0D && !player.capabilities.isCreativeMode) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if(playerEx.getVampireLevel() < 10) {
            return false;
         } else {
            if(player.worldObj.rand.nextDouble() < Config.instance().vampireHunterSpawnChance) {
               Village village = player.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), 128);
               if(village != null && village.getReputationForPlayer(player.getCommandSenderName()) < -1) {
                  List hunters = player.worldObj.getEntitiesWithinAABB(EntityWitchHunter.class, player.boundingBox.expand(64.0D, 16.0D, 64.0D));
                  return hunters == null || hunters.size() == 0;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   protected void attackEntity(Entity p_70785_1_, float p_70785_2_) {
      if(super.attackTime <= 0 && p_70785_2_ < 2.0F && p_70785_1_.boundingBox.maxY > super.boundingBox.minY && p_70785_1_.boundingBox.minY < super.boundingBox.maxY) {
         super.attackTime = 20;
         this.attackEntityAsMob(p_70785_1_);
      }

   }

   public float getBlockPathWeight(int p_70783_1_, int p_70783_2_, int p_70783_3_) {
      return 0.5F - super.worldObj.getLightBrightness(p_70783_1_, p_70783_2_, p_70783_3_);
   }

   protected boolean isValidLightLevel() {
      int i = MathHelper.floor_double(super.posX);
      int j = MathHelper.floor_double(super.boundingBox.minY);
      int k = MathHelper.floor_double(super.posZ);
      if(super.worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > super.rand.nextInt(32)) {
         return false;
      } else {
         int l = super.worldObj.getBlockLightValue(i, j, k);
         if(super.worldObj.isThundering()) {
            int i1 = super.worldObj.skylightSubtracted;
            super.worldObj.skylightSubtracted = 10;
            l = super.worldObj.getBlockLightValue(i, j, k);
            super.worldObj.skylightSubtracted = i1;
         }

         return l <= super.rand.nextInt(8);
      }
   }

   public boolean getCanSpawnHere() {
      return super.worldObj.difficultySetting != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
   }

   protected boolean func_146066_aG() {
      return true;
   }

}
