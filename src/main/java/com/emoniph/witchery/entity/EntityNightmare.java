package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityNightmare extends EntityMob implements IEntitySelector {

   private int attackTimer;
   private int defenseTimer;


   public EntityNightmare(World par1World) {
      super(par1World);
      super.isImmuneToFire = true;
      this.setSize(0.6F, 1.8F);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAIBreakDoor(this));
      super.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0D, true));
      super.tasks.addTask(4, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
      super.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
      super.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
      super.tasks.addTask(7, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(8, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, false, true, this));
      super.experienceValue = 25;
   }

   public boolean isEntityApplicable(Entity entity) {
      if(!(entity instanceof EntityPlayer)) {
         return false;
      } else {
         EntityPlayer player = (EntityPlayer)entity;
         String victim = this.getVictimName();
         return victim == null || victim.isEmpty() || player.getCommandSenderName().equalsIgnoreCase(victim);
      }
   }

   public int getTalkInterval() {
      return super.getTalkInterval() * 2;
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(17, "");
      super.dataWatcher.addObject(16, Byte.valueOf((byte)0));
      super.dataWatcher.addObject(21, Byte.valueOf((byte)0));
   }

   public void setInWeb() {}

   protected void fall(float par1) {}

   public boolean isScreaming() {
      return super.dataWatcher.getWatchableObjectByte(16) > 0;
   }

   public void setScreaming(boolean par1) {
      super.dataWatcher.updateObject(16, Byte.valueOf((byte)(par1?1:0)));
   }

   public boolean isDefended() {
      return super.dataWatcher.getWatchableObjectByte(21) > 0;
   }

   public void setDefended(boolean par1) {
      super.dataWatcher.updateObject(21, Byte.valueOf((byte)(par1?1:0)));
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.nightmare.name");
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void updateAITick() {
      super.updateAITick();
      if(!super.worldObj.isRemote && this.isEntityAlive()) {
         if(this.getAttackTarget() != null) {
            this.setScreaming(true);
         } else {
            this.setScreaming(false);
         }
      }

   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
      this.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
   }

   protected void updateEntityActionState() {
      super.updateEntityActionState();
   }

   protected int decreaseAirSupply(int par1) {
      return par1;
   }

   protected void collideWithEntity(Entity par1Entity) {
      super.collideWithEntity(par1Entity);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      if(this.getVictimName() == null) {
         par1NBTTagCompound.setString("Victim", "");
      } else {
         par1NBTTagCompound.setString("Victim", this.getVictimName());
      }

   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      String s = par1NBTTagCompound.getString("Victim");
      if(s.length() > 0) {
         this.setVictim(s);
      }

   }

   public String getVictimName() {
      String s = super.dataWatcher.getWatchableObjectString(17);
      return s != null?s:"";
   }

   public void setVictim(String par1Str) {
      super.dataWatcher.updateObject(17, par1Str);
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if(!super.worldObj.isRemote) {
         if(this.defenseTimer > 0 && --this.defenseTimer == 0) {
            this.setDefended(false);
         }

         if(!super.isDead && !this.getVictimName().isEmpty() && (this.getAttackTarget() == null || this.getAttackTarget().isDead || this.getDistanceSqToEntity(this.getAttackTarget()) > 256.0D) || super.worldObj.rand.nextInt(5) == 0 && this.getAttackTarget() instanceof EntityPlayer && WorldProviderDreamWorld.getPlayerHasNightmare((EntityPlayer)this.getAttackTarget()) == 0 && !this.isWakingNightmare((EntityPlayer)this.getAttackTarget())) {
            ParticleEffect.EXPLODE.send(SoundEffect.NONE, this, 1.0D, 2.0D, 16);
            this.setDead();
         }
      }

      if(this.attackTimer > 0) {
         --this.attackTimer;
      }

   }

   private boolean isWakingNightmare(EntityPlayer player) {
      NBTTagCompound nbtTag = Infusion.getNBT(player);
      return nbtTag != null && nbtTag.hasKey("witcheryWakingNightmare")?nbtTag.getInteger("witcheryWakingNightmare") > 0:player.isPotionActive(Witchery.Potions.WAKING_NIGHTMARE);
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if(par1 == 4) {
         this.attackTimer = 15;
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   public boolean attackEntityAsMob(Entity entity) {
      this.attackTimer = 15;
      super.worldObj.setEntityState(this, (byte)4);
      int i;
      if(entity != null && entity instanceof EntityPlayer) {
         EntityPlayer f = (EntityPlayer)entity;
         if(!this.findInInventory(f.inventory, Witchery.Items.GENERIC.itemCharmOfDisruptedDreams)) {
            i = f.worldObj.rand.nextInt(f.inventory.armorInventory.length);
            if(f.inventory.armorInventory[i] != null) {
               Infusion.dropEntityItemWithRandomChoice(f, f.inventory.armorInventory[i], true);
               f.inventory.armorInventory[i] = null;
            }
         }
      }

      float f1 = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      if(super.dimension != Config.instance().dimensionDreamID) {
         f1 = 0.5F;
      }

      i = 0;
      if(entity instanceof EntityLivingBase) {
         f1 += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLivingBase)entity);
         i += EnchantmentHelper.getKnockbackModifier(this, (EntityLivingBase)entity);
      }

      boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f1);
      if(flag) {
         if(i > 0) {
            entity.addVelocity((double)(-MathHelper.sin(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(super.rotationYaw * 3.1415927F / 180.0F) * (float)i * 0.5F));
            super.motionX *= 0.6D;
            super.motionZ *= 0.6D;
         }

         int j = EnchantmentHelper.getFireAspectModifier(this);
         if(j > 0) {
            entity.setFire(j * 4);
         }
      }

      return flag;
   }

   private boolean findInInventory(InventoryPlayer inventory, ItemGeneral.SubItem item) {
      for(int i = 0; i < inventory.mainInventory.length; ++i) {
         ItemStack stack = inventory.mainInventory[i];
         if(stack != null && item.isMatch(stack)) {
            return true;
         }
      }

      return false;
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      if(this.isDefended()) {
         return false;
      } else {
         boolean weakeningWeapon = false;
         if(source instanceof EntityDamageSource && ((EntityDamageSource)source).getEntity() != null && ((EntityDamageSource)source).getEntity() instanceof EntityLivingBase) {
            EntityLivingBase living = (EntityLivingBase)((EntityDamageSource)source).getEntity();
            if(living.getHeldItem() != null && living.getHeldItem().getItem() == Witchery.Items.HUNTSMANS_SPEAR) {
               weakeningWeapon = true;
            }
         }

         if(!super.worldObj.isRemote && super.worldObj.getBlock(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY), MathHelper.floor_double(super.posZ)) != Witchery.Blocks.FLOWING_SPIRIT) {
            this.defenseTimer = super.dimension == Config.instance().dimensionDreamID?(weakeningWeapon?40:80):(weakeningWeapon?30:40);
            this.setDefended(true);
         }

         return super.attackEntityFrom(source, Math.min(damage, 15.0F));
      }
   }

   @SideOnly(Side.CLIENT)
   public int getAttackTimer() {
      return this.attackTimer;
   }

   protected String getLivingSound() {
      return "witchery:mob.nightmare.nightmare_live";
   }

   protected String getHurtSound() {
      return "witchery:mob.nightmare.nightmare_dead";
   }

   protected String getDeathSound() {
      return "witchery:mob.nightmare.nightmare_hit";
   }

   protected void dropFewItems(boolean par1, int par2) {
      if(super.dimension == Config.instance().dimensionDreamID) {
         int chance = super.rand.nextInt(Math.max(10 - par2, 5));
         int quantity = par2 > 0 && chance == 0?2:1;
         this.entityDropItem(Witchery.Items.GENERIC.itemMellifluousHunger.createStack(quantity), 0.0F);
      }

   }

   public void onDeath(DamageSource source) {
      if(!super.worldObj.isRemote && source != null && source.getEntity() != null && source.getEntity() instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)source.getEntity();
         String victim = this.getVictimName();
         if(victim != null && !victim.isEmpty() && player.getCommandSenderName().equalsIgnoreCase(victim) && super.dimension == Config.instance().dimensionDreamID) {
            WorldProviderDreamWorld.setPlayerLastNightmareKillNow(player);
         }
      }

      super.onDeath(source);
   }

   protected boolean canDespawn() {
      return true;
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      return super.onSpawnWithEgg(par1EntityLivingData);
   }
}
