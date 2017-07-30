package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.item.ItemGlassGoblet;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.RandomCollection;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityLilith extends EntityMob implements IBossDisplayData, IRangedAttackMob, IHandleDT {

   private int attackTimer;
   boolean isFriendly = false;
   int weaknessTimer;
   private static final RandomCollection SPELLS = createSpells();


   public EntityLilith(World world) {
      super(world);
      this.setSize(0.8F, 2.5F);
      super.isImmuneToFire = true;
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 20, 60, 30.0F));
      super.tasks.addTask(3, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      super.tasks.addTask(5, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      super.experienceValue = 60;
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(16, Byte.valueOf((byte)0));
      super.dataWatcher.addObject(17, Integer.valueOf(0));
      super.dataWatcher.addObject(20, new Integer(0));
      super.dataWatcher.addObject(21, new Integer(0));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(50.0D);
      this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
   }

   public int getTotalArmorValue() {
      return 8;
   }

   public void setInWeb() {}

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.lilith.name");
   }

   public boolean isAIEnabled() {
      return !this.isFriendly;
   }

   protected Entity findPlayerToAttack() {
      return this.isFriendly?null:super.findPlayerToAttack();
   }

   protected void updateAITick() {
      super.updateAITick();
   }

   public int getInvulnerableStartTicks() {
      return super.dataWatcher.getWatchableObjectInt(20);
   }

   public void setInvulnerableStartTicks(int par1) {
      super.dataWatcher.updateObject(20, Integer.valueOf(par1));
   }

   public int getLifetime() {
      return super.dataWatcher.getWatchableObjectInt(21);
   }

   public void setLifetime(int par1) {
      super.dataWatcher.updateObject(21, Integer.valueOf(par1));
   }

   public void setInvulnerableStart() {
      this.setInvulnerableStartTicks(150);
      this.setHealth(this.getMaxHealth() / 4.0F);
   }

   protected void updateAITasks() {
      if(this.getInvulnerableStartTicks() > 0) {
         int R = this.getInvulnerableStartTicks() - 1;
         if(R <= 0) {
            super.worldObj.playBroadcastSound(1013, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
         }

         this.setInvulnerableStartTicks(R);
         if(super.ticksExisted % 10 == 0) {
            this.heal(this.getMaxHealth() * 0.75F / 15.0F);
         }
      } else {
         super.updateAITasks();
         if(!super.worldObj.isRemote && !this.isPotionActive(Witchery.Potions.RESIZING)) {
            this.addPotionEffect(new PotionEffect(Witchery.Potions.RESIZING.id, 10000, 3, true));
         }

         this.setLifetime(this.getLifetime() + 1);
         if(super.ticksExisted % 20 == 0) {
            if(this.weaknessTimer > 0) {
               --this.weaknessTimer;
            }

            if(!this.isPotionActive(Witchery.Potions.CHILLED) && !this.isPotionActive(Potion.weakness) && this.weaknessTimer == 0) {
               this.heal(5.0F);
            } else if(this.weaknessTimer == 0) {
               this.heal(1.0F);
            }
         }

         if(super.ticksExisted % 20 == 0 && super.worldObj.rand.nextInt(5) == 0 && (this.getAttackTarget() != null || this.getLastAttacker() != null) && !super.worldObj.isRemote) {
            boolean var12 = true;
            double RY = 16.0D;
            double RSQ = 1024.0D;
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(super.posX - 32.0D, super.posY - 16.0D, super.posZ - 32.0D, super.posX + 32.0D, super.posY + 16.0D, super.posZ + 32.0D);
            List players = super.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bounds);
            Iterator i$ = players.iterator();

            while(i$.hasNext()) {
               EntityPlayer player = (EntityPlayer)i$.next();
               if(player.isPotionActive(Potion.fireResistance)) {
                  player.removePotionEffect(Potion.fireResistance.id);
               }

               if(super.worldObj.rand.nextInt(2) == 0) {
                  SoundEffect.MOB_ENDERDRAGON_GROWL.playAtPlayer(super.worldObj, player);

                  for(int i = 0; i < 3 + super.rand.nextInt(4); ++i) {
                     EntitySmallFireball fireball = new EntitySmallFireball(super.worldObj, player.posX + super.rand.nextDouble() * 4.0D - 2.0D, player.posY + (double)super.rand.nextInt(2) + 14.0D, player.posZ + super.rand.nextDouble() * 4.0D - 2.0D, 0.0D, -0.2D, 0.0D);
                     super.worldObj.spawnEntityInWorld(fireball);
                  }
               }
            }
         }
      }

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

   public boolean attackEntityFrom(DamageSource source, float damage) {
      boolean immune = false;
      if(immune) {
         return false;
      } else {
         if(source.getEntity() != null && source.getSourceOfDamage() instanceof EntityLargeFireball && source.getEntity() instanceof EntityPlayer) {
            this.weaknessTimer = 10;
         }

         return super.attackEntityFrom(source, Math.min(damage, 12.0F));
      }
   }

   public float getCapDT(DamageSource source, float damage) {
      return 12.0F;
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setInteger("Invul", this.getInvulnerableStartTicks());
      nbtRoot.setLong("Lifetime", (long)this.getLifetime());
      nbtRoot.setBoolean("Friendly", this.isFriendly);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      this.setInvulnerableStartTicks(nbtRoot.getInteger("Invul"));
      this.setLifetime(nbtRoot.getInteger("Lifetime"));
      this.isFriendly = nbtRoot.getBoolean("Friendly");
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
      return this.isFriendly?null:"witchery:mob.lilith.say";
   }

   protected String getHurtSound() {
      return "witchery:mob.lilith.hit";
   }

   protected String getDeathSound() {
      return this.isFriendly?"witchery:mob.lilith.hit":"witchery:mob.lilith.death";
   }

   protected void dropFewItems(boolean par1, int par2) {}

   public void onDeath(DamageSource source) {
      if(!super.worldObj.isRemote) {
         super.isDead = false;
         ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 2.0D, 16);
         this.setHealth(this.getMaxHealth());
         this.isFriendly = true;
         ArrayList effectsToRemove = new ArrayList();
         Collection effects = this.getActivePotionEffects();
         Iterator player = effects.iterator();

         while(player.hasNext()) {
            PotionEffect R = (PotionEffect)player.next();
            Potion RY = Potion.potionTypes[R.getPotionID()];
            if(PotionBase.isCurable(RY)) {
               effectsToRemove.add(RY);
            }
         }

         player = effectsToRemove.iterator();

         while(player.hasNext()) {
            Potion R1 = (Potion)player.next();
            this.removePotionEffect(R1.id);
         }

         EntityPlayer player1 = null;
         if(source != null && source.getEntity() != null && source.getEntity() instanceof EntityPlayer) {
            player1 = (EntityPlayer)source.getEntity();
            if(player1.dimension != super.dimension || player1.isDead || player1.getDistanceSqToEntity(this) > 4096.0D) {
               player1 = null;
            }
         }

         if(player1 == null) {
            boolean R2 = true;
            double RY1 = 16.0D;
            double RSQ = 1024.0D;
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(super.posX - 32.0D, super.posY - 16.0D, super.posZ - 32.0D, super.posX + 32.0D, super.posY + 16.0D, super.posZ + 32.0D);
            List players = super.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bounds);
            double distSq = 0.0D;
            Iterator i$ = players.iterator();

            while(i$.hasNext()) {
               EntityPlayer player2 = (EntityPlayer)i$.next();
               if(player1 == null) {
                  distSq = this.getDistanceSqToEntity(player2);
                  player1 = player2;
               } else {
                  double newDist = this.getDistanceSqToEntity(player2);
                  if(newDist < distSq) {
                     distSq = newDist;
                     player1 = player2;
                  }
               }
            }
         }

         if(player1 != null) {
            this.setPositionAndUpdate(player1.posX - 1.0D + super.rand.nextDouble() * 2.0D, player1.posY + 0.05D, player1.posZ - 1.0D + super.rand.nextDouble() * 2.0D);
            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 2.0D, 16);
            ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player1, "item.witchery:glassgoblet.lilithquestcomplete", new Object[0]);
            SoundEffect.WITCHERY_MOB_LILITH_TALK.playAt((EntityLiving)this);
         } else {
            this.setDead();
         }
      }

   }

   protected Item getDropItem() {
      return null;
   }

   protected boolean canDespawn() {
      return false;
   }

   protected boolean interact(EntityPlayer player) {
      if(!super.worldObj.isRemote && this.isFriendly) {
         ItemStack stack = player.getHeldItem();
         SoundEffect.WITCHERY_MOB_LILITH_TALK.playAt((EntityLiving)this, 1.0F);
         boolean vanish = false;
         if(stack == null) {
            ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player, "item.witchery:glassgoblet.lilithquestcomplete2", new Object[0]);
         } else if(stack.getItem() == Witchery.Items.BLOOD_GOBLET) {
            if(!ExtendedPlayer.get(player).isVampire()) {
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player, "item.witchery:glassgoblet.lilithquestcompletelife", new Object[0]);
               player.setCurrentItemOrArmor(0, (ItemStack)null);
               ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, player.worldObj, player.posX, player.posY + (double)player.height * 0.85D, player.posZ, 0.8D, 0.8D, 16);
               Witchery.Items.BLOOD_GOBLET.setBloodOwner(stack, ItemGlassGoblet.BloodSource.LILITH);
               super.worldObj.spawnEntityInWorld(new EntityItem(super.worldObj, player.posX, player.posY, player.posZ, stack));
               ExtendedPlayer.get(player).setHumanBlood(0);
               vanish = true;
            } else {
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player, "item.witchery:glassgoblet.lilithquestcompletelifefail", new Object[0]);
            }
         } else if(stack.getItem() == Witchery.Items.SEEDS_GARLIC) {
            if(ExtendedPlayer.get(player).isVampire()) {
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player, "item.witchery:glassgoblet.lilithquestcompletecure", new Object[0]);
               player.setCurrentItemOrArmor(0, (ItemStack)null);
               ExtendedPlayer.get(player).setVampireLevel(0);
               ParticleEffect.REDDUST.send(SoundEffect.RANDOM_FIZZ, player, 1.0D, 1.5D, 16);
               vanish = true;
            } else {
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player, "item.witchery:glassgoblet.lilithquestcompletecurefail", new Object[0]);
            }
         } else if(stack.getItem() == Item.getItemFromBlock(Blocks.red_flower) && stack.getItemDamage() == 0) {
            ExtendedPlayer enchants1 = ExtendedPlayer.get(player);
            if(enchants1.getVampireLevel() == 6 && enchants1.canIncreaseVampireLevel()) {
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player, "item.witchery:glassgoblet.lilithquestcompletebatflight", new Object[0]);
               player.setCurrentItemOrArmor(0, (ItemStack)null);
               enchants1.increaseVampireLevel();
               ParticleEffect.REDDUST.send(SoundEffect.RANDOM_FIZZ, player, 1.0D, 1.5D, 16);
               vanish = true;
            } else {
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player, "item.witchery:glassgoblet.lilithquestcompletebatflightfail", new Object[0]);
            }
         } else {
            List enchants = EnchantmentHelper.buildEnchantmentList(super.worldObj.rand, stack, 40);
            if(enchants != null && enchants.size() > 0) {
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player, "item.witchery:glassgoblet.lilithquestcompletemagic", new Object[0]);
               player.setCurrentItemOrArmor(0, (ItemStack)null);
               addEnchantmentsFromList(stack, enchants);
               if(stack.isItemStackDamageable()) {
                  stack.setItemDamage(0);
               }

               super.worldObj.spawnEntityInWorld(new EntityItem(super.worldObj, player.posX, player.posY, player.posZ, stack));
               vanish = true;
            } else {
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_PURPLE, player, "item.witchery:glassgoblet.lilithquestcomplete2", new Object[0]);
            }
         }

         if(vanish) {
            ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 2.0D, 16);
            this.setDead();
         }

         return true;
      } else {
         return false;
      }
   }

   private static void addEnchantmentsFromList(ItemStack stack, List list) {
      boolean flag = stack.getItem() == Items.book;
      if(flag) {
         stack.func_150996_a(Items.enchanted_book);
      }

      Map enchants = EnchantmentHelper.getEnchantments(stack);
      if(list != null) {
         Iterator iterator = list.iterator();

         while(iterator.hasNext()) {
            EnchantmentData enchantmentdata = (EnchantmentData)iterator.next();
            if(flag) {
               Items.enchanted_book.addEnchantment(stack, enchantmentdata);
            } else {
               if(stack.getTagCompound() == null) {
                  stack.setTagCompound(new NBTTagCompound());
               }

               if(!stack.getTagCompound().hasKey("ench", 9)) {
                  stack.getTagCompound().setTag("ench", new NBTTagList());
               }

               NBTTagList nbttaglist = stack.getTagCompound().getTagList("ench", 10);
               boolean addEnchant = true;

               for(int nbttagcompound = 0; nbttagcompound < nbttaglist.tagCount(); ++nbttagcompound) {
                  NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(nbttagcompound);
                  if(nbttagcompound1.getShort("id") == enchantmentdata.enchantmentobj.effectId) {
                     if(nbttagcompound1.getShort("lvl") < enchantmentdata.enchantmentLevel) {
                        nbttagcompound1.setShort("lvl", (short)enchantmentdata.enchantmentLevel);
                     }

                     addEnchant = false;
                     break;
                  }
               }

               if(addEnchant) {
                  NBTTagCompound var10 = new NBTTagCompound();
                  var10.setShort("id", (short)enchantmentdata.enchantmentobj.effectId);
                  var10.setShort("lvl", (short)((byte)enchantmentdata.enchantmentLevel));
                  nbttaglist.appendTag(var10);
               }

               stack.getTagCompound().setTag("ench", nbttaglist);
            }
         }
      }

   }

   private static RandomCollection createSpells() {
      RandomCollection spells = new RandomCollection();
      EffectRegistry.instance();
      spells.add(1.0D, EffectRegistry.Ignianima);
      EffectRegistry.instance();
      spells.add(5.0D, EffectRegistry.Flipendo);
      EffectRegistry.instance();
      spells.add(1.0D, EffectRegistry.Impedimenta);
      EffectRegistry.instance();
      spells.add(1.0D, EffectRegistry.Confundus);
      EffectRegistry.instance();
      spells.add(5.0D, EffectRegistry.Attraho);
      return spells;
   }

   public void attackEntityWithRangedAttack(EntityLivingBase targetEntity, float par2) {
      if(super.worldObj.rand.nextBoolean()) {
         this.attackTimer = 10;
         super.worldObj.setEntityState(this, (byte)4);
         double d0 = targetEntity.posX - super.posX;
         double d1 = targetEntity.boundingBox.minY + (double)(targetEntity.height / 2.0F) - (super.posY + (double)(super.height / 2.0F));
         double d2 = targetEntity.posZ - super.posZ;
         float f1 = MathHelper.sqrt_float(par2) * 0.5F;
         if(!super.worldObj.isRemote) {
            if(super.worldObj.rand.nextInt(3) == 0) {
               EntityLargeFireball count = new EntityLargeFireball(super.worldObj, this, d0 + super.rand.nextGaussian() * (double)f1, d1, d2 + super.rand.nextGaussian() * (double)f1);
               double effect = 1.0D;
               Vec3 vec3 = this.getLook(1.0F);
               count.posX = super.posX + vec3.xCoord * effect;
               count.posY = super.posY + (double)(super.height / 2.0F) + 0.5D;
               count.posZ = super.posZ + vec3.zCoord * effect;
               if(!super.worldObj.isRemote) {
                  super.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
                  super.worldObj.spawnEntityInWorld(count);
               }
            } else {
               super.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
               boolean count1 = super.rand.nextInt(10) == 0?true:true;
               EntitySpellEffect effect1 = new EntitySpellEffect(super.worldObj, this, d0 + super.rand.nextGaussian() * (double)f1, d1, d2 + super.rand.nextGaussian() * (double)f1, (SymbolEffect)SPELLS.next(), 1);
               double d8 = 1.0D;
               effect1.posX = super.posX;
               effect1.posY = super.posY + (double)(super.height / 2.0F);
               effect1.posZ = super.posZ;
               super.worldObj.spawnEntityInWorld(effect1);
               effect1.setShooter(this);
            }
         }
      }

   }

}
