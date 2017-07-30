package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityLostSoul;
import com.emoniph.witchery.entity.EntitySpellEffect;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.util.BlockActionSphere;
import com.emoniph.witchery.util.IHandleDT;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.RandomCollection;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityLeonard extends EntityMob implements IBossDisplayData, IRangedAttackMob, IHandleDT {

   private int attackTimer;
   private boolean isImmune;
   private int spawnDelay;
   private static final RandomCollection SPELLS = createSpells();


   public EntityLeonard(World world) {
      super(world);
      this.setSize(0.6F, 1.8F);
      super.isImmuneToFire = true;
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 20, 60, 30.0F));
      super.tasks.addTask(3, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      super.tasks.addTask(5, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.experienceValue = 100;
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
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(600.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(50.0D);
      this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
   }

   public int getTotalArmorValue() {
      return 0;
   }

   public void setInWeb() {}

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.leonard.name");
   }

   public boolean isAIEnabled() {
      return true;
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
         int SPAWN_DELAY = this.getInvulnerableStartTicks() - 1;
         if(SPAWN_DELAY <= 0) {
            super.worldObj.playBroadcastSound(1013, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
         }

         this.setInvulnerableStartTicks(SPAWN_DELAY);
         if(super.ticksExisted % 10 == 0) {
            this.heal(this.getMaxHealth() * 0.75F / 15.0F);
         }
      } else {
         super.updateAITasks();
         this.setLifetime(this.getLifetime() + 1);
         if(super.ticksExisted % 20 == 0) {
            this.heal(1.0F);
         }

         boolean var15;
         if(super.ticksExisted % 20 == 0 && super.worldObj.rand.nextInt(5) == 0 && (this.getAttackTarget() != null || this.getLastAttacker() != null) && !super.worldObj.isRemote) {
            var15 = true;
            double R = 40.0D;
            double RSQ = 1600.0D;
            AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(super.posX - 40.0D, super.posY - 40.0D, super.posZ - 40.0D, super.posX + 40.0D, super.posY + 40.0D, super.posZ + 40.0D);
            List bounds1 = super.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bounds);
            boolean souls = false;
            Iterator spawned = bounds1.iterator();

            while(spawned.hasNext()) {
               EntityPlayer i = (EntityPlayer)spawned.next();
               if(this.getDistanceSq(i.posX, super.posY, i.posZ) <= 1600.0D && !i.isDead && i.getHealth() > 0.0F && !i.isPotionActive(Witchery.Potions.MORTAL_COIL)) {
                  souls = true;
                  ParticleEffect.MOB_SPELL.send(SoundEffect.RANDOM_FIZZ, i, 1.0D, 2.0D, 40);
                  i.addPotionEffect(new PotionEffect(Witchery.Potions.MORTAL_COIL.id, TimeUtil.secsToTicks(90)));
               }
            }

            if(souls) {
               ParticleEffect.SPELL_COLORED.send(SoundEffect.NOTE_HARP, (Entity)this, 1.0D, 1.0D, 40, '\u9900');
            } else if(super.worldObj.rand.nextInt(5) == 1 && bounds1.size() > 0) {
               EntityPlayer var19 = (EntityPlayer)bounds1.get(super.worldObj.rand.nextInt(bounds1.size()));
               if(var19 != null && this.getDistanceSq(var19.posX, super.posY, var19.posZ) <= 1600.0D && !var19.isDead && var19.getHealth() > 0.0F) {
                  ParticleEffect.MOB_SPELL.send(SoundEffect.RANDOM_FIZZ, var19, 1.0D, 2.0D, 40);
                  label124:
                  switch(super.worldObj.rand.nextInt(10)) {
                  case 0:
                  case 1:
                  case 2:
                     ArrayList var22 = new ArrayList();
                     Collection soul = var19.getActivePotionEffects();
                     Iterator i$ = soul.iterator();

                     while(i$.hasNext()) {
                        PotionEffect potion = (PotionEffect)i$.next();
                        Potion potion1 = Potion.potionTypes[potion.getPotionID()];
                        if(!PotionBase.isDebuff(potion1) && PotionBase.isCurable(potion1)) {
                           var22.add(potion1);
                        }
                     }

                     i$ = var22.iterator();

                     while(true) {
                        if(!i$.hasNext()) {
                           break label124;
                        }

                        Potion var24 = (Potion)i$.next();
                        var19.removePotionEffect(var24.id);
                     }
                  case 3:
                  case 4:
                  case 5:
                     var19.addPotionEffect(new PotionEffect(Witchery.Potions.SINKING.id, TimeUtil.secsToTicks(60), 3));
                     ParticleEffect.SPELL_COLORED.send(SoundEffect.NOTE_HARP, (Entity)this, 1.0D, 1.0D, 40, 10027008);
                     break;
                  case 6:
                  case 7:
                  case 8:
                     var19.addPotionEffect(new PotionEffect(Witchery.Potions.INSANITY.id, TimeUtil.secsToTicks(60), 3));
                     ParticleEffect.SPELL_COLORED.send(SoundEffect.NOTE_HARP, (Entity)this, 1.0D, 1.0D, 40, 153);
                     break;
                  case 9:
                     var19.addPotionEffect(new PotionEffect(Witchery.Potions.OVERHEATING.id, TimeUtil.secsToTicks(60), 3));
                     ParticleEffect.SPELL_COLORED.send(SoundEffect.NOTE_HARP, (Entity)this, 1.0D, 1.0D, 40, '\u9999');
                  }
               }
            }
         }

         if(super.ticksExisted % 20 == 2) {
            if(super.worldObj.rand.nextInt(5) == 0) {
               (new BlockActionSphere() {
                  protected void onBlock(World world, int x, int y, int z) {
                     Block block = world.getBlock(x, y, z);
                     if(block == Witchery.Blocks.BREW_GAS || block == Witchery.Blocks.BREW_LIQUID) {
                        world.setBlock(x, y, z, Blocks.fire);
                     }

                  }
               }).drawFilledSphere(super.worldObj, MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY) + 2, MathHelper.floor_double(super.posZ), 4);
            }

            if((double)this.getHealth() < (double)this.getMaxHealth() * 0.5D) {
               if(this.getAttackTarget() != null || this.getLastAttacker() != null) {
                  if((double)this.getHealth() < (double)this.getMaxHealth() * 0.25D && super.worldObj.rand.nextInt(3) == 1 && !this.isPotionActive(Witchery.Potions.RESIZING)) {
                     this.addPotionEffect(new PotionEffect(Witchery.Potions.RESIZING.id, TimeUtil.secsToTicks(60), 3));
                  }

                  var15 = true;
                  boolean var16 = true;
                  double RY = 5.0D;
                  double RSQ1 = 225.0D;
                  AxisAlignedBB var17 = AxisAlignedBB.getBoundingBox(super.posX - 15.0D, super.posY - 5.0D, super.posZ - 15.0D, super.posX + 15.0D, super.posY + 5.0D, super.posZ + 15.0D);
                  List var18 = super.worldObj.getEntitiesWithinAABB(EntityLostSoul.class, var17);
                  if(var18.size() == 0) {
                     this.isImmune = false;
                     if(--this.spawnDelay <= 0) {
                        this.removeCoilEffects(15, 5.0D);
                        this.spawnDelay = 10;
                        int var20 = 0;

                        EntityLostSoul var21;
                        int var23;
                        for(var23 = 0; var23 < 4 + super.worldObj.rand.nextInt(2); ++var23) {
                           var21 = (EntityLostSoul)Infusion.spawnCreature(super.worldObj, EntityLostSoul.class, (int)super.posX, (int)super.posY + 1, (int)super.posZ, (EntityLivingBase)null, 1, 4, ParticleEffect.SMOKE, SoundEffect.RANDOM_POP);
                           if(var21 != null) {
                              var21.setTimeToLive(TimeUtil.secsToTicks(60 + super.worldObj.rand.nextInt(30)));
                              ++var20;
                           }
                        }

                        for(var23 = var20; var23 < 3; ++var23) {
                           var21 = (EntityLostSoul)Infusion.spawnCreature(super.worldObj, EntityLostSoul.class, (int)super.posX, (int)super.posY + 1, (int)super.posZ, (EntityLivingBase)null, 0, 0, ParticleEffect.SMOKE, SoundEffect.RANDOM_POP);
                           if(var21 != null) {
                              var21.setTimeToLive(TimeUtil.secsToTicks(60 + super.worldObj.rand.nextInt(30)));
                           }
                        }
                     }
                  } else {
                     this.isImmune = true;
                  }
               }
            } else {
               this.isImmune = false;
            }
         }
      }

   }

   public void onDeath(DamageSource source) {
      super.onDeath(source);
      this.removeCoilEffects(40, 40.0D);
   }

   private void removeCoilEffects(int R, double RY) {
      AxisAlignedBB bounds2 = AxisAlignedBB.getBoundingBox(super.posX - (double)R, super.posY - RY, super.posZ - (double)R, super.posX + (double)R, super.posY + RY, super.posZ + (double)R);
      List players = super.worldObj.getEntitiesWithinAABB(EntityPlayer.class, bounds2);
      Iterator i$ = players.iterator();

      while(i$.hasNext()) {
         EntityPlayer player = (EntityPlayer)i$.next();
         if(!player.isDead && player.getHealth() > 0.0F && player.isPotionActive(Witchery.Potions.MORTAL_COIL)) {
            player.removePotionEffect(Witchery.Potions.MORTAL_COIL.id);
            ExtendedPlayer playerEx = ExtendedPlayer.get(player);
            if(playerEx != null) {
               playerEx.clearCachedIncurablePotionEffect(Witchery.Potions.MORTAL_COIL);
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
      boolean immune = this.isImmune;
      if(immune) {
         return false;
      } else if(source.getDamageType().equals("player")) {
         if((double)this.getHealth() >= (double)this.getMaxHealth() * 0.25D) {
            return super.attackEntityFrom(source, Math.min(damage, 12.0F));
         } else {
            boolean isLarge = this.isPotionActive(Witchery.Potions.RESIZING) && this.getActivePotionEffect(Witchery.Potions.RESIZING).getAmplifier() >= 2;
            return super.attackEntityFrom(source, Math.min(damage, isLarge?1.0F:4.0F));
         }
      } else {
         return false;
      }
   }

   public float getCapDT(DamageSource source, float damage) {
      return !this.isImmune && source.getDamageType().equals("player")?2.0F:0.0F;
   }

   public void attackEntityFromWeakness(int damage) {
      if((double)this.getHealth() < (double)this.getMaxHealth() * 0.4D) {
         boolean isLarge = this.isPotionActive(Witchery.Potions.RESIZING) && this.getActivePotionEffect(Witchery.Potions.RESIZING).getAmplifier() >= 2;
         super.attackEntityFrom(DamageSource.magic, Math.min((float)damage, isLarge?8.0F:15.0F));
      }

   }

   public boolean canAttackClass(Class par1Class) {
      return super.canAttackClass(par1Class);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setBoolean("PlayerCreated", this.isPlayerCreated());
      par1NBTTagCompound.setInteger("Invul", this.getInvulnerableStartTicks());
      par1NBTTagCompound.setLong("Lifetime", (long)this.getLifetime());
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setPlayerCreated(par1NBTTagCompound.getBoolean("PlayerCreated"));
      this.setInvulnerableStartTicks(par1NBTTagCompound.getInteger("Invul"));
      this.setLifetime(par1NBTTagCompound.getInteger("Lifetime"));
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
      return "witchery:mob.leonard.say";
   }

   protected String getHurtSound() {
      return "witchery:mob.leonard.hit";
   }

   protected String getDeathSound() {
      return "witchery:mob.leonard.death";
   }

   protected void func_145780_a(int par1, int par2, int par3, Block par4) {
      super.func_145780_a(par1, par2, par3, par4);
   }

   protected void dropFewItems(boolean par1, int par2) {
      Enchantment enchantment = Enchantment.enchantmentsBookList[super.rand.nextInt(Enchantment.enchantmentsBookList.length)];
      int k = MathHelper.getRandomIntegerInRange(super.rand, Math.min(enchantment.getMinLevel() + 2, enchantment.getMaxLevel()), enchantment.getMaxLevel());
      ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, k));
      this.entityDropItem(itemstack, 0.0F);
      this.entityDropItem(Witchery.Items.GENERIC.itemDemonHeart.createStack(), 0.0F);
      this.entityDropItem(new ItemStack(Witchery.Items.LEONARDS_URN), 0.0F);
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
      if(super.worldObj.rand.nextBoolean()) {
         this.attackTimer = 10;
         super.worldObj.setEntityState(this, (byte)4);
         double d0 = targetEntity.posX - super.posX;
         double d1 = targetEntity.boundingBox.minY + (double)(targetEntity.height / 2.0F) - (super.posY + (double)(super.height / 2.0F));
         double d2 = targetEntity.posZ - super.posZ;
         float f1 = MathHelper.sqrt_float(par2) * 0.5F;
         if(!super.worldObj.isRemote) {
            super.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
            boolean count = super.rand.nextInt(10) == 0?true:true;
            EntitySpellEffect effect = new EntitySpellEffect(super.worldObj, this, d0 + super.rand.nextGaussian() * (double)f1, d1, d2 + super.rand.nextGaussian() * (double)f1, (SymbolEffect)SPELLS.next(), 1);
            double d8 = 1.0D;
            effect.posX = super.posX;
            effect.posY = super.posY + (double)(super.height / 2.0F);
            effect.posZ = super.posZ;
            super.worldObj.spawnEntityInWorld(effect);
            effect.setShooter(this);
         }
      }

   }

}
