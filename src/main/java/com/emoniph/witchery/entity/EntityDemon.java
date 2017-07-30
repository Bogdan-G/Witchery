package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.entity.ai.EntityAIAttackCloseTargetOnCollide;
import com.emoniph.witchery.entity.ai.EntityAIDemonicBarginPlayer;
import com.emoniph.witchery.entity.ai.EntityAILookAtDemonicBarginPlayer;
import com.emoniph.witchery.util.Config;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Blocks;
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
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public class EntityDemon extends EntityGolem implements IRangedAttackMob, IMerchant {

   private int attackTimer;
   private EntityPlayer buyingPlayer;
   private MerchantRecipeList buyingList;
   private int tryEscape = -1;


   public EntityDemon(World par1World) {
      super(par1World);
      this.setSize(1.0F, 2.9F);
      super.isImmuneToFire = true;
      this.getNavigator().setAvoidsWater(true);
      super.tasks.addTask(1, new EntityAIAttackCloseTargetOnCollide(this, 1.0D, true, 3.0D));
      super.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F));
      super.tasks.addTask(3, new EntityAIDemonicBarginPlayer(this));
      super.tasks.addTask(4, new EntityAILookAtDemonicBarginPlayer(this));
      super.tasks.addTask(5, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
      super.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 0.6D, true));
      super.tasks.addTask(7, new EntityAIMoveTowardsRestriction(this, 1.0D));
      super.tasks.addTask(8, new EntityAIWander(this, 0.6D));
      super.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      super.tasks.addTask(10, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
      super.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, true));
      super.experienceValue = 10;
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(16, Byte.valueOf((byte)0));
      super.dataWatcher.addObject(17, Integer.valueOf(0));
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.demon.name");
   }

   public boolean interact(EntityPlayer par1EntityPlayer) {
      if(super.dimension == Config.instance().dimensionDreamID) {
         return super.interact(par1EntityPlayer);
      } else {
         ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();
         boolean flag = itemstack != null && (itemstack.getItem() == Items.spawn_egg || itemstack.getItem() == Items.name_tag);
         if(!flag && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
            if(!super.worldObj.isRemote) {
               this.setCustomer(par1EntityPlayer);
               par1EntityPlayer.displayGUIMerchant(this, this.getCommandSenderName());
            }

            return true;
         } else {
            return super.interact(par1EntityPlayer);
         }
      }
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void updateAITick() {
      super.updateAITick();
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
      return super.attackEntityFrom(par1DamageSource, Math.min(par2, 15.0F));
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
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

      if(super.dimension == Config.instance().dimensionDreamID && super.worldObj.provider instanceof WorldProviderDreamWorld && !((WorldProviderDreamWorld)super.worldObj.provider).isDemonicNightmare()) {
         this.setDead();
      }

      if(this.tryEscape == 0) {
         this.tryEscape = -1;
         super.worldObj.createExplosion(this, super.posX, super.posY, super.posZ, 3.0F, true);
      } else if(this.tryEscape > 0) {
         --this.tryEscape;
      }

      if(super.motionX * super.motionX + super.motionZ * super.motionZ > 2.500000277905201E-7D && super.rand.nextInt(5) == 0) {
         int i = MathHelper.floor_double(super.posX);
         int j = MathHelper.floor_double(super.posY - 0.20000000298023224D - (double)super.yOffset);
         int k = MathHelper.floor_double(super.posZ);
         Block l = super.worldObj.getBlock(i, j, k);
         if(l != Blocks.air) {
            super.worldObj.spawnParticle("tilecrack_" + l + "_" + super.worldObj.getBlockMetadata(i, j, k), super.posX + ((double)super.rand.nextFloat() - 0.5D) * (double)super.width, super.boundingBox.minY + 0.1D, super.posZ + ((double)super.rand.nextFloat() - 0.5D) * (double)super.width, 4.0D * ((double)super.rand.nextFloat() - 0.5D), 0.5D, ((double)super.rand.nextFloat() - 0.5D) * 4.0D);
         }
      }

   }

   public boolean canAttackClass(Class par1Class) {
      return super.canAttackClass(par1Class);
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setBoolean("PlayerCreated", this.isPlayerCreated());
      if(this.buyingList != null) {
         par1NBTTagCompound.setTag("Bargains", this.buyingList.getRecipiesAsTags());
      }

   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setPlayerCreated(par1NBTTagCompound.getBoolean("PlayerCreated"));
      if(par1NBTTagCompound.hasKey("Bargains")) {
         NBTTagCompound nbttagcompound1 = par1NBTTagCompound.getCompoundTag("Bargains");
         this.buyingList = new MerchantRecipeList(nbttagcompound1);
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
      return "mob.blaze.breathe";
   }

   protected String getHurtSound() {
      return "mob.wither.hurt";
   }

   protected String getDeathSound() {
      return "mob.wither.death";
   }

   protected void func_145780_a(int par1, int par2, int par3, Block par4) {
      this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
   }

   protected void dropFewItems(boolean par1, int par2) {
      if(par1) {
         int j = super.rand.nextInt(2 + par2);

         for(int k = 0; k < j; ++k) {
            this.dropItem(Items.magma_cream, 1);
         }
      }

   }

   protected Item getDropItem() {
      return Items.magma_cream;
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

   public void onDeath(DamageSource par1DamageSource) {
      super.onDeath(par1DamageSource);
   }

   protected boolean canDespawn() {
      return true;
   }

   public void attackEntityWithRangedAttack(EntityLivingBase targetEntity, float par2) {
      if(targetEntity.getHeldItem() == null || targetEntity.getHeldItem().getItem() != Witchery.Items.DEVILS_TONGUE_CHARM || super.worldObj.rand.nextDouble() < 0.05D) {
         double d0 = targetEntity.posX - super.posX;
         double d1 = targetEntity.boundingBox.minY + (double)(targetEntity.height / 2.0F) - (super.posY + (double)(super.height / 2.0F));
         double d2 = targetEntity.posZ - super.posZ;
         float f1 = MathHelper.sqrt_float(par2) * 0.5F;
         EntityLargeFireball fireballEntity = new EntityLargeFireball(super.worldObj, this, d0 + super.rand.nextGaussian() * (double)f1, d1, d2 + super.rand.nextGaussian() * (double)f1);
         double d8 = 1.0D;
         Vec3 vec3 = this.getLook(1.0F);
         fireballEntity.posX = super.posX + vec3.xCoord * d8;
         fireballEntity.posY = super.posY + (double)(super.height / 2.0F) + 0.5D;
         fireballEntity.posZ = super.posZ + vec3.zCoord * d8;
         if(!super.worldObj.isRemote) {
            super.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
            super.worldObj.spawnEntityInWorld(fireballEntity);
         }
      }

   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      this.addPotionEffect(new PotionEffect(Potion.jump.id, Integer.MAX_VALUE, 4));
      return super.onSpawnWithEgg(par1EntityLivingData);
   }

   public void setCustomer(EntityPlayer par1EntityPlayer) {
      this.buyingPlayer = par1EntityPlayer;
   }

   public EntityPlayer getCustomer() {
      return this.buyingPlayer;
   }

   public boolean isTrading() {
      return this.buyingPlayer != null;
   }

   public void useRecipe(MerchantRecipe par1MerchantRecipe) {
      par1MerchantRecipe.incrementToolUses();
      Item itemToBuy = par1MerchantRecipe.getItemToBuy().getItem();
      if(!super.worldObj.isRemote && (itemToBuy == Items.magma_cream || itemToBuy == Items.blaze_rod)) {
         this.playSound("mob.wither.shoot", this.getSoundVolume(), this.getSoundPitch());
         this.tryEscape = 50;
      } else {
         this.playSound("random.breath", this.getSoundVolume(), this.getSoundPitch());
      }

      if(this.getCustomer() != null && this.getCustomer().getHeldItem() != null && this.getCustomer().getHeldItem().getItem() == Witchery.Items.DEVILS_TONGUE_CHARM) {
         this.getCustomer().getHeldItem().damageItem(5, this.getCustomer());
         if(this.getCustomer().getHeldItem().stackSize <= 0) {
            this.getCustomer().destroyCurrentEquippedItem();
         }
      }

   }

   public void func_110297_a_(ItemStack par1ItemStack) {
      if(!super.worldObj.isRemote && super.livingSoundTime > -this.getTalkInterval() + 20) {
         super.livingSoundTime = -this.getTalkInterval();
         if(par1ItemStack != null) {
            this.playSound("random.breath", this.getSoundVolume(), this.getSoundPitch());
         } else {
            this.playSound("mob.wither.idle", this.getSoundVolume(), this.getSoundPitch());
         }
      }

   }

   protected void attackEntity(Entity entity, float par2) {
      if(super.attackTime <= 0 && par2 < 2.0F && entity.boundingBox.maxY > super.boundingBox.minY && entity.boundingBox.minY < super.boundingBox.maxY) {
         super.attackTime = 20;
         this.attackEntityAsMob(entity);
      }

      super.attackEntity(entity, par2);
   }

   public MerchantRecipeList getRecipes(EntityPlayer par1EntityPlayer) {
      if(this.buyingList == null) {
         this.addDefaultEquipmentAndRecipies(super.rand.nextInt(4) + 6);
      }

      if(this.getCustomer() != null && this.getCustomer().getHeldItem() != null && this.getCustomer().getHeldItem().getItem() == Witchery.Items.DEVILS_TONGUE_CHARM) {
         MerchantRecipeList list = new MerchantRecipeList();
         Iterator i$ = this.buyingList.iterator();

         while(i$.hasNext()) {
            Object recipeObj = i$.next();
            MerchantRecipe recipe = (MerchantRecipe)recipeObj;
            NBTTagCompound nbtTag = recipe.writeToTags();
            MerchantRecipe recipe2 = new MerchantRecipe(nbtTag);
            ItemStack cost = recipe2.getItemToBuy();
            cost.stackSize = Math.max(cost.stackSize - (cost.getItem() == Items.gold_ingot?5:(cost.getItem() == Items.emerald?2:(cost.getItem() == Items.diamond?0:1))), 1);
            list.add(recipe2);
         }

         return list;
      } else {
         return this.buyingList;
      }
   }

   private Item getCurrency() {
      double chance = super.rand.nextDouble();
      return chance < 0.2D?Items.blaze_rod:(chance < 0.4D?Items.magma_cream:(chance < 0.5D?Items.diamond:(chance < 0.75D?Items.emerald:Items.gold_ingot)));
   }

   private ItemStack getPrice(int basePriceInEmeralds) {
      Item currency = this.getCurrency();
      int multiplier = currency == Items.gold_ingot?1:(currency == Items.emerald?3:(currency == Items.diamond?5:4));
      int quantity = Math.max(1, basePriceInEmeralds / multiplier);
      return new ItemStack(currency, quantity);
   }

   private void addDefaultEquipmentAndRecipies(int par1) {
      MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
      boolean STOCK_REDUCTION = true;

      int j1;
      for(int currencyForHeart = 0; currencyForHeart < par1; ++currencyForHeart) {
         Enchantment heartRecipe = Enchantment.enchantmentsBookList[super.rand.nextInt(Enchantment.enchantmentsBookList.length)];
         j1 = MathHelper.getRandomIntegerInRange(super.rand, heartRecipe.getMinLevel(), heartRecipe.getMaxLevel());
         ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(heartRecipe, j1));
         int j = 2 + super.rand.nextInt(5 + j1 * 10) + 3 * j1;
         MerchantRecipe recipe = new MerchantRecipe(this.getPrice(j), itemstack);
         recipe.func_82783_a(-5);
         merchantrecipelist.add(recipe);
      }

      MerchantRecipe var10;
      if(super.rand.nextDouble() < 0.25D) {
         var10 = new MerchantRecipe(this.getPrice(super.rand.nextInt(3) + 8), Witchery.Items.GENERIC.itemSpectralDust.createStack(super.rand.nextInt(4) + 3));
         var10.func_82783_a(-5);
         merchantrecipelist.add(var10);
      }

      if(super.rand.nextDouble() < 0.25D) {
         var10 = new MerchantRecipe(this.getPrice(super.rand.nextInt(3) + 8), Witchery.Items.GENERIC.itemDogTongue.createStack(super.rand.nextInt(4) + 4));
         var10.func_82783_a(-5);
         merchantrecipelist.add(var10);
      }

      if(super.rand.nextDouble() < 0.15D) {
         var10 = new MerchantRecipe(this.getPrice(super.rand.nextInt(10) + 20), Witchery.Items.GENERIC.itemRedstoneSoup.createStack(1));
         var10.func_82783_a(-5);
         merchantrecipelist.add(var10);
      }

      if(super.rand.nextDouble() < 0.15D) {
         var10 = new MerchantRecipe(new ItemStack(Items.diamond), new ItemStack(Items.ghast_tear, 2));
         var10.func_82783_a(-5);
         merchantrecipelist.add(var10);
      }

      if(super.rand.nextDouble() < 0.15D) {
         var10 = new MerchantRecipe(new ItemStack(Items.diamond), new ItemStack(Items.ender_pearl, 2));
         var10.func_82783_a(-5);
         merchantrecipelist.add(var10);
      }

      Collections.shuffle(merchantrecipelist);
      Item var12 = this.getCurrency();
      MerchantRecipe var11 = new MerchantRecipe(new ItemStack(var12, var12 == Items.gold_ingot?30:3), Witchery.Items.GENERIC.itemDemonHeart.createStack(1));
      var11.func_82783_a(-5);
      merchantrecipelist.add(super.rand.nextInt(3), var11);
      if(this.buyingList == null) {
         this.buyingList = new MerchantRecipeList();
      }

      for(j1 = 0; j1 < par1 && j1 < merchantrecipelist.size(); ++j1) {
         this.buyingList.add(merchantrecipelist.get(j1));
      }

   }

   @SideOnly(Side.CLIENT)
   public void setRecipes(MerchantRecipeList par1MerchantRecipeList) {}
}
