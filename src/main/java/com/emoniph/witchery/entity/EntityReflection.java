package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.client.renderer.RenderReflection;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityBolt;
import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.RandomCollection;
import com.emoniph.witchery.util.TransformCreature;
import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
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
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class EntityReflection extends EntityMob implements IBossDisplayData, IRangedAttackMob, IHandleDT {

   private int attackTimer;
   private boolean freeSpawn;
   private boolean isVampire;
   private int livingTicks = -1;
   private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
   private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityLivingBase.class, 1.2D, false);
   private String owner = "";
   private EntityReflection.Task task;
   private static final RandomCollection SPELLS = createSpells();
   @SideOnly(Side.CLIENT)
   private ThreadDownloadImageData downloadImageSkin;
   @SideOnly(Side.CLIENT)
   private ResourceLocation locationSkin;
   private String lastSkinOwner;


   public EntityReflection(World world) {
      super(world);
      this.task = EntityReflection.Task.NONE;
      this.setSize(0.6F, 1.8F);
      super.isImmuneToFire = true;
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(3, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      super.tasks.addTask(5, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      super.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
      super.experienceValue = 50;
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(17, "");
      super.dataWatcher.addObject(18, Byte.valueOf((byte)0));
   }

   public String getOwnerSkin() {
      return super.dataWatcher.getWatchableObjectString(17);
   }

   public String getOwnerName() {
      return this.owner;
   }

   public void setOwnerSkin(String skinName) {
      super.dataWatcher.updateObject(17, skinName);
   }

   public void setOwner(String par1Str) {
      this.func_110163_bv();
      this.owner = par1Str;
   }

   public EntityPlayer getOwnerEntity() {
      return super.worldObj.getPlayerEntityByName(this.getOwnerName());
   }

   public void setModel(int model) {
      super.dataWatcher.updateObject(18, Byte.valueOf((byte)model));
   }

   public int getModel() {
      return super.dataWatcher.getWatchableObjectByte(18);
   }

   public void setLifetime(int ticks) {
      this.livingTicks = ticks;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(50.0D);
      this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
   }

   public void setInWeb() {}

   public String getCommandSenderName() {
      if(this.hasCustomNameTag()) {
         return this.getCustomNameTag();
      } else {
         String owner = this.getOwnerName();
         return owner != null && !owner.isEmpty()?owner:StatCollector.translateToLocal("entity.witchery.reflection.name");
      }
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void updateAITick() {
      super.updateAITick();
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

      if(!super.worldObj.isRemote && super.ticksExisted % 30 == 1) {
         if(!this.freeSpawn && super.dimension != Config.instance().dimensionMirrorID) {
            this.setDead();
            return;
         }

         if(this.livingTicks > -1 && --this.livingTicks == 0) {
            this.setDead();
            return;
         }

         double R = 10.0D;
         double RY = 8.0D;
         AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(super.posX - R, super.posY - RY, super.posZ - R, super.posX + R, super.posY + RY, super.posZ + R);
         List players = super.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bounds);
         EntityPlayer ownerEntity = this.getOwnerEntity();
         boolean ownerFound = false;
         EntityPlayer closest = null;
         double distance = Double.MAX_VALUE;
         Iterator resetGear = players.iterator();

         while(resetGear.hasNext()) {
            EntityPlayer skinName = (EntityPlayer)resetGear.next();
            double held = skinName.getDistanceSqToEntity(this);
            if(closest == null || held < distance) {
               closest = skinName;
               distance = held;
            }

            if(ownerEntity == skinName) {
               ownerFound = true;
            }
         }

         if(ownerEntity == null || !ownerFound) {
            if(closest != null) {
               this.setOwner(closest.getCommandSenderName());
            } else {
               this.setOwner("");
            }
         }

         boolean var25 = true;
         String var26 = this.getOwnerName();
         if(!this.getOwnerName().isEmpty()) {
            EntityPlayer var28 = ownerEntity != null && ownerFound?ownerEntity:this.getOwnerEntity();
            if(var28 != null) {
               for(int bestWeapon = 1; bestWeapon <= 4; ++bestWeapon) {
                  ItemStack bestDamage = var28.getEquipmentInSlot(bestWeapon);
                  if(bestDamage != null) {
                     bestDamage = bestDamage.copy();
                  }

                  this.setCurrentItemOrArmor(bestWeapon, bestDamage);
               }

               ItemStack var30 = null;
               double var31 = 0.0D;

               ItemStack stack;
               for(int playerEx = 0; playerEx < 9; ++playerEx) {
                  stack = var28.inventory.getStackInSlot(playerEx);
                  if(stack != null) {
                     Multimap effects = stack.getAttributeModifiers();
                     Iterator effect = effects.get(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName()).iterator();
                     double damage = 0.0D;

                     while(effect.hasNext()) {
                        AttributeModifier modifier = (AttributeModifier)effect.next();
                        if(modifier.getOperation() == 0) {
                           damage += modifier.getAmount();
                        }
                     }

                     if(damage > var31) {
                        var30 = stack;
                        var31 = damage;
                     }
                  }
               }

               ExtendedPlayer var32 = ExtendedPlayer.get(var28);
               if(var32 != null) {
                  this.setModel(var32.getCreatureType() == TransformCreature.WOLFMAN?1:0);
                  this.isVampire = var32.isVampire();
                  if(var32.getCreatureType() == TransformCreature.PLAYER) {
                     var26 = var32.getOtherPlayerSkin();
                  }
               }

               stack = var30 != null?var30:var28.getEquipmentInSlot(0);
               if(stack != null) {
                  stack = stack.copy();
                  Witchery.modHooks.makeItemModProof(stack);
               }

               if(this.getModel() == 1) {
                  stack = null;
                  this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
               } else {
                  this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
               }

               this.setCurrentItemOrArmor(0, stack);
               var25 = false;
               if(super.ticksExisted % 60 == 1) {
                  this.clearActivePotions();
                  Iterator var33 = var28.getActivePotionEffects().iterator();

                  while(var33.hasNext()) {
                     PotionEffect var34 = (PotionEffect)var33.next();
                     this.addPotionEffect(new PotionEffect(var34));
                  }
               }
            }
         }

         if(var25) {
            for(int var29 = 0; var29 <= 4; ++var29) {
               this.setCurrentItemOrArmor(var29, (ItemStack)null);
            }
         }

         this.setOwnerSkin(var26);
         ItemStack var27 = this.getHeldItem();
         if(var27 != null) {
            if(var27.getItem() == Witchery.Items.MYSTIC_BRANCH) {
               if(this.task == EntityReflection.Task.MELEE) {
                  super.tasks.removeTask(this.aiAttackOnCollide);
               }

               super.tasks.addTask(2, this.aiArrowAttack);
               this.task = EntityReflection.Task.RANGED;
            } else if(var27.getItem() != Witchery.Items.CROSSBOW_PISTOL && !(var27.getItem() instanceof ItemBow)) {
               if(this.task == EntityReflection.Task.RANGED) {
                  super.tasks.removeTask(this.aiArrowAttack);
               }

               super.tasks.addTask(2, this.aiAttackOnCollide);
               this.task = EntityReflection.Task.MELEE;
            } else {
               if(this.task == EntityReflection.Task.MELEE) {
                  super.tasks.removeTask(this.aiAttackOnCollide);
               }

               super.tasks.addTask(2, this.aiArrowAttack);
               this.task = EntityReflection.Task.RANGED;
            }
         } else {
            if(this.task == EntityReflection.Task.RANGED) {
               super.tasks.removeTask(this.aiArrowAttack);
            }

            super.tasks.addTask(2, this.aiAttackOnCollide);
            this.task = EntityReflection.Task.MELEE;
         }

         if(this.isEntityAlive() && this.getAttackTarget() != null && this.getNavigator().noPath() && this.getEntitySenses().canSee(this.getAttackTarget())) {
            EntityLivingBase var10001 = this.getAttackTarget();
            EffectRegistry.instance();
            this.castSpell(var10001, 1.0F, EffectRegistry.Attraho);
         }
      }

      if(!super.worldObj.isRemote && super.worldObj.rand.nextDouble() < 0.05D && this.getAttackTarget() != null && (this.getAttackTarget().isAirBorne || this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer)this.getAttackTarget()).capabilities.isFlying) && !this.getAttackTarget().isPotionActive(Potion.moveSlowdown)) {
         this.getAttackTarget().addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 200, 5));
      }

   }

   public void onDeath(DamageSource p_70645_1_) {
      super.onDeath(p_70645_1_);
      Witchery.Blocks.MIRROR.demonSlain(super.worldObj, super.posX, super.posY, super.posZ);
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      return super.attackEntityFrom(source, Math.min(damage, 6.0F));
   }

   public float getCapDT(DamageSource source, float damage) {
      return 2.0F;
   }

   public boolean canAttackClass(Class par1Class) {
      return super.canAttackClass(par1Class);
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setString("Owner", this.getOwnerName());
      nbtRoot.setString("OwnerSkin", this.getOwnerSkin());
      nbtRoot.setInteger("Model", this.getModel());
      nbtRoot.setBoolean("FreeSpawn", this.freeSpawn);
      nbtRoot.setBoolean("Vampire", this.isVampire);
      nbtRoot.setInteger("LivingTicks", this.livingTicks);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      this.setOwner(nbtRoot.getString("Owner"));
      this.setOwnerSkin(nbtRoot.getString("OwnerSkin"));
      this.freeSpawn = nbtRoot.getBoolean("FreeSpawn");
      this.livingTicks = nbtRoot.getInteger("LivingTicks");
      this.isVampire = nbtRoot.getBoolean("Vampire");
      this.setModel(nbtRoot.getInteger("Model"));
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      this.attackTimer = 10;
      boolean flag = super.attackEntityAsMob(par1Entity);
      return flag;
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if(par1 == 4) {
         this.attackTimer = 10;
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
      return "witchery:mob.reflection.say";
   }

   protected String getHurtSound() {
      return "witchery:mob.reflection.hit";
   }

   protected String getDeathSound() {
      return "witchery:mob.reflection.death";
   }

   protected void func_145780_a(int par1, int par2, int par3, Block par4) {
      super.func_145780_a(par1, par2, par3, par4);
   }

   protected void dropFewItems(boolean par1, int par2) {
      this.entityDropItem(Witchery.Items.GENERIC.itemDemonHeart.createStack(), 0.0F);
   }

   protected void dropEquipment(boolean p_82160_1_, int p_82160_2_) {}

   protected Item getDropItem() {
      return null;
   }

   protected boolean canDespawn() {
      return false;
   }

   private static RandomCollection createSpells() {
      RandomCollection spells = new RandomCollection();
      EffectRegistry.instance();
      spells.add(14.0D, EffectRegistry.Ignianima);
      EffectRegistry.instance();
      spells.add(2.0D, EffectRegistry.Expelliarmus);
      EffectRegistry.instance();
      spells.add(2.0D, EffectRegistry.Flipendo);
      EffectRegistry.instance();
      spells.add(2.0D, EffectRegistry.Impedimenta);
      EffectRegistry.instance();
      spells.add(1.0D, EffectRegistry.Confundus);
      return spells;
   }

   public void attackEntityWithRangedAttack(EntityLivingBase targetEntity, float par2) {
      ItemStack held = this.getHeldItem();
      if(held != null) {
         this.attackTimer = 10;
         super.worldObj.setEntityState(this, (byte)4);
         if(held.getItem() == Witchery.Items.MYSTIC_BRANCH) {
            if(super.worldObj.rand.nextBoolean()) {
               this.castSpell(targetEntity, par2, (SymbolEffect)SPELLS.next());
            }
         } else {
            int i;
            int j;
            if(held.getItem() == Witchery.Items.CROSSBOW_PISTOL) {
               EntityBolt entityarrow = new EntityBolt(super.worldObj, this, targetEntity, 1.6F, (float)(14 - super.worldObj.difficultySetting.getDifficultyId() * 4));
               i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
               j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
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
            } else {
               EntityArrow entityarrow1 = new EntityArrow(super.worldObj, this, targetEntity, 1.6F, (float)(14 - super.worldObj.difficultySetting.getDifficultyId() * 3));
               i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
               j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
               entityarrow1.setDamage((double)(par2 * 2.0F) + super.rand.nextGaussian() * 0.25D + (double)((float)super.worldObj.difficultySetting.getDifficultyId() * 0.11F));
               if(i > 0) {
                  entityarrow1.setDamage(entityarrow1.getDamage() + (double)i * 0.5D + 0.5D);
               }

               if(j > 0) {
                  entityarrow1.setKnockbackStrength(j);
               }

               if(EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0) {
                  entityarrow1.setFire(100);
               }

               this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
               super.worldObj.spawnEntityInWorld(entityarrow1);
            }
         }

      }
   }

   private void castSpell(EntityLivingBase targetEntity, float par2, SymbolEffect spell) {
      double d0 = targetEntity.posX - super.posX;
      double d1 = targetEntity.boundingBox.minY + (double)(targetEntity.height / 2.0F) - (super.posY + (double)(super.height / 2.0F));
      double d2 = targetEntity.posZ - super.posZ;
      float f1 = MathHelper.sqrt_float(par2) * 0.5F;
      if(!super.worldObj.isRemote) {
         super.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
         boolean count = super.rand.nextInt(10) == 0?true:true;
         EntitySpellEffect effect = new EntitySpellEffect(super.worldObj, this, d0 + super.rand.nextGaussian() * (double)f1, d1, d2 + super.rand.nextGaussian() * (double)f1, spell, 1);
         double d8 = 1.0D;
         effect.posX = super.posX;
         effect.posY = super.posY + (double)(super.height / 2.0F);
         effect.posZ = super.posZ;
         super.worldObj.spawnEntityInWorld(effect);
         effect.setShooter(this);
      }

   }

   @SideOnly(Side.CLIENT)
   public ResourceLocation getLocationSkin() {
      if(this.locationSkin == null || !this.lastSkinOwner.equals(this.getOwnerName())) {
         this.setupCustomSkin();
      }

      return this.locationSkin != null?this.locationSkin:null;
   }

   @SideOnly(Side.CLIENT)
   private void setupCustomSkin() {
      String ownerName = this.getOwnerSkin();
      if(ownerName != null && !ownerName.isEmpty()) {
         this.locationSkin = AbstractClientPlayer.getLocationSkin(ownerName);
         this.downloadImageSkin = getDownloadImageSkin(this.locationSkin, ownerName);
         this.lastSkinOwner = ownerName;
      } else {
         this.locationSkin = null;
         this.downloadImageSkin = null;
         this.lastSkinOwner = "";
      }

   }

   @SideOnly(Side.CLIENT)
   public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation location, String name) {
      TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
      Object object = texturemanager.getTexture(location);
      if(object == null) {
         object = new ThreadDownloadImageData((File)null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[]{StringUtils.stripControlCodes(name)}), RenderReflection.SKIN, new ImageBufferDownload());
         texturemanager.loadTexture(location, (ITextureObject)object);
      }

      return (ThreadDownloadImageData)object;
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
      this.freeSpawn = true;
      return super.onSpawnWithEgg(data);
   }

   public boolean isVampire() {
      return this.isVampire;
   }


   private static enum Task {

      NONE("NONE", 0),
      MELEE("MELEE", 1),
      RANGED("RANGED", 2);
      // $FF: synthetic field
      private static final EntityReflection.Task[] $VALUES = new EntityReflection.Task[]{NONE, MELEE, RANGED};


      private Task(String var1, int var2) {}

   }
}
