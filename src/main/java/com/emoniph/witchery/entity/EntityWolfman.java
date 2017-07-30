package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.Shapeshift;
import com.emoniph.witchery.entity.EntityVillagerWere;
import com.emoniph.witchery.entity.EntityWitchHunter;
import com.emoniph.witchery.util.CreatureUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class EntityWolfman extends EntityMob implements IEntitySelector {

   private int formerProfession = -1;
   private int attackTimer;
   private boolean infectious;
   boolean isSitting;
   private MerchantRecipeList buyingList;
   private int wealth;
   public ItemStack itemInUse;
   public int itemInUseCount;
   @SideOnly(Side.CLIENT)
   private ResourceLocation skinOverride;


   public EntityWolfman(World world) {
      super(world);
      this.getNavigator().setBreakDoors(true);
      this.getNavigator().setAvoidsWater(true);
      super.tasks.addTask(0, new EntityAISwimming(this));
      super.tasks.addTask(1, new EntityAIBreakDoor(this));
      super.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
      super.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
      super.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityWitchHunter.class, 1.0D, true));
      super.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
      super.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
      super.tasks.addTask(7, new EntityAIWander(this, 1.0D));
      super.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(8, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true, false, this));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, false));
      this.setSize(0.6F, 1.8F);
      super.experienceValue = 20;
   }

   public boolean isEntityApplicable(Entity target) {
      if(target instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)target;
         return !Shapeshift.INSTANCE.isAnimalForm(player);
      } else {
         return false;
      }
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
   }

   protected void entityInit() {
      super.entityInit();
   }

   public boolean isSitting() {
      return this.isSitting;
   }

   public void setSitting(boolean p_70904_1_) {
      this.isSitting = p_70904_1_;
   }

   public int getTotalArmorValue() {
      int i = super.getTotalArmorValue() + 10;
      if(i > 20) {
         i = 20;
      }

      return i;
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.wolfman.name");
   }

   protected boolean isAIEnabled() {
      return true;
   }

   public void onLivingUpdate() {
      if(this.attackTimer > 0) {
         --this.attackTimer;
      }

      if(!super.worldObj.isRemote) {
         if(this.formerProfession != -1 && super.ticksExisted % 100 == 3 && !CreatureUtil.isFullMoon(super.worldObj) && !this.isPotionActive(Witchery.Potions.WOLFSBANE)) {
            convertToVillager(this, this.formerProfession, this.infectious, this.wealth, this.buyingList);
         } else if(super.ticksExisted % 40 == 4 && this.isPotionActive(Potion.poison)) {
            this.removePotionEffect(Potion.poison.id);
         }
      }

      super.onLivingUpdate();
   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      return CreatureUtil.isSilverDamage(source)?super.attackEntityFrom(source, Math.min(damage * 1.5F, 15.0F)):super.attackEntityFrom(source, Math.min(damage, 1.0F));
   }

   public boolean attackEntityAsMob(Entity targetEntity) {
      this.attackTimer = 10;
      super.worldObj.setEntityState(this, (byte)4);
      boolean flag = super.attackEntityAsMob(targetEntity);
      if(flag) {
         ;
      }

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

   public int getTalkInterval() {
      return super.getTalkInterval() * 4;
   }

   protected String getLivingSound() {
      return super.worldObj.rand.nextInt(20) == 0?"witchery:mob.wolfman.howl":"witchery:mob.wolfman.say";
   }

   protected String getHurtSound() {
      return "witchery:mob.wolfman.hit";
   }

   protected String getDeathSound() {
      return "witchery:mob.wolfman.death";
   }

   protected void dropFewItems(boolean p_70628_1_, int fortune) {
      super.dropFewItems(p_70628_1_, fortune);
   }

   protected Item getDropItem() {
      return Items.bone;
   }

   protected void dropRareDrop(int p_70600_1_) {
      switch(super.rand.nextInt(3)) {
      case 0:
         this.entityDropItem(Witchery.Items.GENERIC.itemSilverDust.createStack(super.worldObj.rand.nextInt(3) + 1), 0.0F);
         break;
      case 1:
         this.dropItem(Items.bone, 1);
         break;
      case 2:
         this.dropItem(Items.leather, 1);
      }

   }

   public void setFormerProfession(int profession, int wealth, MerchantRecipeList buyingList) {
      this.formerProfession = profession;
      this.buyingList = buyingList;
      this.wealth = wealth;
   }

   public int getFormerProfession() {
      return this.formerProfession;
   }

   public int getWealth() {
      return this.wealth;
   }

   public MerchantRecipeList getBuyingList() {
      return this.buyingList;
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setInteger("FormerProfession", this.formerProfession);
      nbtRoot.setBoolean("Infectious", this.infectious);
      nbtRoot.setInteger("Riches", this.wealth);
      if(this.buyingList != null) {
         nbtRoot.setTag("Offers", this.buyingList.getRecipiesAsTags());
      }

   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      this.formerProfession = nbtRoot.getInteger("FormerProfession");
      this.infectious = nbtRoot.getBoolean("Infectious");
      this.wealth = nbtRoot.getInteger("Riches");
      if(nbtRoot.hasKey("Offers", 10)) {
         NBTTagCompound nbttagcompound1 = nbtRoot.getCompoundTag("Offers");
         this.buyingList = new MerchantRecipeList(nbttagcompound1);
      }

   }

   public void setInfectious() {
      this.infectious = true;
   }

   public boolean isInfectious() {
      return this.infectious;
   }

   public void onKillEntity(EntityLivingBase targetEntity) {
      super.onKillEntity(targetEntity);
   }

   protected boolean canDespawn() {
      return false;
   }

   public static void convertToVillager(EntityLiving target, int profession, boolean infectious, int wealth, MerchantRecipeList buyingList) {
      if(target != null && !target.worldObj.isRemote) {
         EntityVillagerWere entity = new EntityVillagerWere(target.worldObj, profession, infectious);
         entity.copyLocationAndAnglesFrom(target);
         entity.setLookingForHome();
         entity.func_110163_bv();
         target.worldObj.removeEntity(target);
         target.worldObj.spawnEntityInWorld(entity);
         target.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, (int)target.posX, (int)target.posY, (int)target.posZ, 0);
      }

   }

   public static void convertToCuredVillager(EntityLiving target, int profession, int wealth, MerchantRecipeList buyingList) {
      if(target != null && !target.worldObj.isRemote) {
         EntityVillager entity = new EntityVillager(target.worldObj, profession);
         entity.copyLocationAndAnglesFrom(target);
         entity.setLookingForHome();
         entity.func_110163_bv();
         target.worldObj.removeEntity(target);
         target.worldObj.spawnEntityInWorld(entity);
         target.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, (int)target.posX, (int)target.posY, (int)target.posZ, 0);
      }

   }

   public void setItemInUse(ItemStack stack, int itemInUseCount) {
      this.itemInUse = stack;
      this.itemInUseCount = itemInUseCount;
   }

   @SideOnly(Side.CLIENT)
   public void setSkinResource(ResourceLocation skinOverride) {
      this.skinOverride = skinOverride;
   }

   @SideOnly(Side.CLIENT)
   public ResourceLocation getSkinResource() {
      return this.skinOverride;
   }
}
