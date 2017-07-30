package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityVillageGuard;
import com.emoniph.witchery.entity.ai.EntityAIAvoidEntityConditionally;
import com.emoniph.witchery.entity.ai.EntityAIDigBlocks;
import com.emoniph.witchery.entity.ai.EntityAIDropOffBlocks;
import com.emoniph.witchery.entity.ai.EntityAIGoblinMate;
import com.emoniph.witchery.entity.ai.EntityAILookAtTradePlayerGeneric;
import com.emoniph.witchery.entity.ai.EntityAIMoveIndoorsLeashAware;
import com.emoniph.witchery.entity.ai.EntityAIPickUpBlocks;
import com.emoniph.witchery.entity.ai.EntityAITradePlayerGeneric;
import com.emoniph.witchery.entity.ai.EntityAIWorship;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityGoblin extends EntityAgeable implements IMerchant, INpc, IEntitySelector, EntityAIAvoidEntityConditionally.IAvoidEntities {

   private int randomTickDivider;
   private boolean isMating;
   Village villageObj;
   private EntityPlayer buyingPlayer;
   private MerchantRecipeList buyingList;
   private int timeUntilReset;
   private boolean needsInitilization;
   private int wealth;
   private String lastBuyingPlayer;
   private boolean isLookingForHome;
   private float field_82191_bN;
   public static final Map villagersSellingList = new HashMap();
   public static final Map blacksmithSellingList = new HashMap();
   private EntityAIWorship aiWorship;
   private boolean preventDespawn;
   private static final double KOBOLDITE_HARVEST_CHANCE = 0.02D;
   private boolean testingLeashRange;


   public EntityGoblin(World par1World) {
      this(par1World, 0);
   }

   public EntityGoblin(World par1World, int par2) {
      super(par1World);
      this.setProfession(super.rand.nextInt(4));
      this.setSize(0.6F, 0.95F);
      this.getNavigator().setBreakDoors(true);
      this.getNavigator().setAvoidsWater(true);
      super.tasks.addTask(0, new EntityAISwimming(this));
      super.tasks.addTask(1, this.aiWorship = new EntityAIWorship(this, (double)(TimeUtil.secsToTicks(30) + super.rand.nextInt(10))));
      super.tasks.addTask(2, new EntityAIPickUpBlocks(this, 24.0D));
      super.tasks.addTask(2, new EntityAIDropOffBlocks(this, 24.0D));
      super.tasks.addTask(2, new EntityAIDigBlocks(this, 16.0D, 0.02D));
      super.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityPlayer.class, 8.0F, 0.6D, 0.6D));
      super.tasks.addTask(3, new EntityAIAvoidEntityConditionally(this, EntityVillageGuard.class, 12.0F, 0.8D, 0.8D, this));
      super.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, true));
      super.tasks.addTask(4, new EntityAITradePlayerGeneric(this, this));
      super.tasks.addTask(4, new EntityAILookAtTradePlayerGeneric(this, this));
      super.tasks.addTask(5, new EntityAIMoveIndoorsLeashAware(this));
      super.tasks.addTask(5, new EntityAIRestrictOpenDoor(this));
      super.tasks.addTask(6, new EntityAIOpenDoor(this, true));
      super.tasks.addTask(7, new EntityAIMoveTowardsRestriction(this, 0.6D));
      super.tasks.addTask(8, new EntityAIGoblinMate(this));
      super.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
      super.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityGoblin.class, 5.0F, 0.02F));
      super.tasks.addTask(9, new EntityAIWander(this, 0.6D));
      super.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
      super.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      super.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 0, true, true, this));
   }

   public boolean isEntityApplicable(Entity entity) {
      double R = 8.0D;
      return entity instanceof EntityVillager?super.worldObj.getEntitiesWithinAABB(EntityGoblin.class, AxisAlignedBB.getBoundingBox(super.posX - 8.0D, super.posY - 8.0D, super.posZ - 8.0D, super.posX + 8.0D, super.posY + 8.0D, super.posZ + 8.0D)).size() >= 3:true;
   }

   public boolean shouldAvoid() {
      double R = 8.0D;
      return super.worldObj.getEntitiesWithinAABB(EntityGoblin.class, AxisAlignedBB.getBoundingBox(super.posX - 8.0D, super.posY - 8.0D, super.posZ - 8.0D, super.posX + 8.0D, super.posY + 8.0D, super.posZ + 8.0D)).size() >= 3;
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.goblin.name");
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
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

   public boolean isAIEnabled() {
      return true;
   }

   protected void updateAITick() {
      if(--this.randomTickDivider <= 0) {
         super.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY), MathHelper.floor_double(super.posZ));
         this.randomTickDivider = 70 + super.rand.nextInt(50);
         this.villageObj = super.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY), MathHelper.floor_double(super.posZ), 32);
         if(this.villageObj == null) {
            this.detachHome();
         } else {
            this.preventDespawn = true;
            ChunkCoordinates iterator = this.villageObj.getCenter();
            this.setHomeArea(iterator.posX, iterator.posY, iterator.posZ, (int)((float)this.villageObj.getVillageRadius() * 0.6F));
            if(this.isLookingForHome) {
               this.isLookingForHome = false;
               this.villageObj.setDefaultPlayerReputation(5);
            }
         }
      }

      if(!this.isTrading() && this.timeUntilReset > 0) {
         --this.timeUntilReset;
         if(this.timeUntilReset <= 0) {
            if(this.needsInitilization) {
               if(this.buyingList.size() > 1) {
                  Iterator var3 = this.buyingList.iterator();

                  while(var3.hasNext()) {
                     MerchantRecipe merchantrecipe = (MerchantRecipe)var3.next();
                     if(merchantrecipe.isRecipeDisabled()) {
                        merchantrecipe.func_82783_a(super.rand.nextInt(6) + super.rand.nextInt(6) + 2);
                     }
                  }
               }

               this.addDefaultEquipmentAndRecipies(1);
               this.needsInitilization = false;
               if(this.villageObj != null && this.lastBuyingPlayer != null) {
                  super.worldObj.setEntityState(this, (byte)14);
                  this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
               }
            }

            this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
         }
      }

      super.updateAITick();
   }

   protected void updateLeashedState() {
      try {
         this.testingLeashRange = true;
         if(this.getLeashed()) {
            this.preventDespawn = true;
         }

         super.updateLeashedState();
      } finally {
         this.testingLeashRange = false;
      }

   }

   public float getDistanceToEntity(Entity par1Entity) {
      float distance = super.getDistanceToEntity(par1Entity);
      if(this.testingLeashRange && distance < 9.0F) {
         distance *= 0.5F;
      }

      return distance;
   }

   public void onUpdate() {
      super.onUpdate();
      if(!super.worldObj.isRemote) {
         this.setBesideClimbableBlock(super.isCollidedHorizontally);
      }

   }

   public boolean isOnLadder() {
      return super.isOnLadder();
   }

   public boolean isWorking() {
      return super.dataWatcher.getWatchableObjectByte(18) == 1;
   }

   public void setWorking(boolean par1) {
      byte b0 = super.dataWatcher.getWatchableObjectByte(18);
      if(par1 && b0 != 1 || !par1 && b0 == 1) {
         super.dataWatcher.updateObject(18, Byte.valueOf((byte)(par1?1:0)));
      }

   }

   public boolean isWorshipping() {
      return super.dataWatcher.getWatchableObjectByte(18) == 2;
   }

   public void setWorshipping(boolean worshiping) {
      byte b0 = super.dataWatcher.getWatchableObjectByte(18);
      if(worshiping && b0 != 2 || !worshiping && b0 == 2) {
         super.dataWatcher.updateObject(18, Byte.valueOf((byte)(worshiping?2:0)));
      }

   }

   public void beginWorship(TileEntity tile) {
      this.aiWorship.begin(tile);
   }

   public boolean isBesideClimbableBlock() {
      return (super.dataWatcher.getWatchableObjectByte(17) & 1) != 0;
   }

   public void setBesideClimbableBlock(boolean par1) {
      byte b0 = super.dataWatcher.getWatchableObjectByte(17);
      if(par1) {
         b0 = (byte)(b0 | 1);
      } else {
         b0 &= -2;
      }

      super.dataWatcher.updateObject(17, Byte.valueOf(b0));
   }

   public boolean interact(EntityPlayer player) {
      ItemStack stack = player.inventory.getCurrentItem();
      boolean heldSpawnEgg = stack != null && stack.getItem() == Items.spawn_egg;
      if(!heldSpawnEgg && this.isEntityAlive() && !this.isTrading() && !this.isChild() && !player.isSneaking()) {
         if(this.getLeashed()) {
            if(this.getHeldItem() == null) {
               if(stack != null && stack.getItem() instanceof ItemPickaxe) {
                  this.setCurrentItemOrArmor(0, stack);
                  player.setCurrentItemOrArmor(0, (ItemStack)null);
               }
            } else {
               if(!super.worldObj.isRemote) {
                  ItemStack goblinItem = this.getHeldItem();
                  if(!player.inventory.addItemStackToInventory(goblinItem)) {
                     this.entityDropItem(goblinItem, 1.0F);
                  } else if(player instanceof EntityPlayerMP) {
                     ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                  }
               }

               this.setCurrentItemOrArmor(0, (ItemStack)null);
            }
         } else if(!this.isWorking() && !this.isWorshipping() && this.villageObj != null && !super.worldObj.isRemote) {
            this.setCustomer(player);
            player.displayGUIMerchant(this, this.getCommandSenderName());
         }

         return true;
      } else {
         return super.interact(player);
      }
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(16, Integer.valueOf(0));
      super.dataWatcher.addObject(17, Byte.valueOf((byte)0));
      super.dataWatcher.addObject(18, Byte.valueOf((byte)0));
      super.dataWatcher.addObject(19, Byte.valueOf((byte)0));
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setInteger("Profession", this.getProfession());
      nbtRoot.setInteger("Riches", this.wealth);
      nbtRoot.setBoolean("Worshipping", this.isWorshipping());
      if(this.buyingList != null) {
         nbtRoot.setTag("Offers", this.buyingList.getRecipiesAsTags());
      }

      nbtRoot.setBoolean("PreventDespawn", this.preventDespawn);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      this.setProfession(nbtRoot.getInteger("Profession"));
      this.wealth = nbtRoot.getInteger("Riches");
      if(nbtRoot.getBoolean("Worshipping") && !super.worldObj.isRemote) {
         this.setWorshipping(true);
      }

      if(nbtRoot.hasKey("Offers", 10)) {
         NBTTagCompound nbttagcompound1 = nbtRoot.getCompoundTag("Offers");
         this.buyingList = new MerchantRecipeList(nbttagcompound1);
      }

      this.preventDespawn = nbtRoot.getBoolean("PreventDespawn");
   }

   protected float getSoundPitch() {
      return 1.2F;
   }

   protected boolean canDespawn() {
      return !Config.instance().goblinDespawnBlock && this.villageObj == null && !this.preventDespawn && !this.isWorshipping();
   }

   protected String getLivingSound() {
      return this.isTrading()?"witchery:mob.goblin.haggle":"witchery:mob.goblin.idle";
   }

   protected String getHurtSound() {
      return "witchery:mob.goblin.hit";
   }

   protected String getDeathSound() {
      return "witchery:mob.goblin.death";
   }

   public void setProfession(int par1) {
      super.dataWatcher.updateObject(16, Integer.valueOf(par1));
   }

   public int getProfession() {
      return super.dataWatcher.getWatchableObjectInt(16);
   }

   public boolean isMating() {
      return this.isMating;
   }

   public void setMating(boolean par1) {
      this.isMating = par1;
   }

   public void setRevengeTarget(EntityLivingBase par1EntityLivingBase) {
      super.setRevengeTarget(par1EntityLivingBase);
      if(this.villageObj != null && par1EntityLivingBase != null) {
         this.villageObj.addOrRenewAgressor(par1EntityLivingBase);
         if(par1EntityLivingBase instanceof EntityPlayer) {
            if(this.isChild()) {
               this.villageObj.setReputationForPlayer(par1EntityLivingBase.getCommandSenderName(), -3);
            }

            if(this.isEntityAlive()) {
               super.worldObj.setEntityState(this, (byte)13);
            }
         }
      }

   }

   public void onDeath(DamageSource par1DamageSource) {
      if(this.villageObj != null) {
         Entity entity = par1DamageSource.getEntity();
         if(entity != null) {
            if(!(entity instanceof EntityPlayer) && entity instanceof IMob) {
               this.villageObj.endMatingSeason();
            }
         } else if(entity == null) {
            EntityPlayer entityplayer = super.worldObj.getClosestPlayerToEntity(this, 16.0D);
            if(entityplayer != null) {
               this.villageObj.endMatingSeason();
            }
         }
      }

      super.onDeath(par1DamageSource);
   }

   public boolean getCanSpawnHere() {
      int i = MathHelper.floor_double(super.posX);
      int j = MathHelper.floor_double(super.boundingBox.minY);
      int k = MathHelper.floor_double(super.posZ);
      return super.worldObj.getBlock(i, j - 1, k) == Blocks.grass && super.worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
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
      super.livingSoundTime = -this.getTalkInterval();
      this.playSound("witchery:mob.goblin.yes", this.getSoundVolume(), this.getSoundPitch());
      if(par1MerchantRecipe.hasSameIDsAs((MerchantRecipe)this.buyingList.get(this.buyingList.size() - 1))) {
         this.timeUntilReset = 40;
         this.needsInitilization = true;
         if(this.buyingPlayer != null) {
            this.lastBuyingPlayer = this.buyingPlayer.getCommandSenderName();
         } else {
            this.lastBuyingPlayer = null;
         }
      }

      if(par1MerchantRecipe.getItemToBuy().getItem() == Items.emerald) {
         this.wealth += par1MerchantRecipe.getItemToBuy().stackSize;
      }

   }

   public void func_110297_a_(ItemStack par1ItemStack) {
      if(!super.worldObj.isRemote && super.livingSoundTime > -this.getTalkInterval() + 20) {
         super.livingSoundTime = -this.getTalkInterval();
         if(par1ItemStack != null) {
            this.playSound("witchery:mob.goblin.yes", this.getSoundVolume(), this.getSoundPitch());
         } else {
            this.playSound("witchery:mob.goblin.no", this.getSoundVolume(), this.getSoundPitch());
         }
      }

   }

   public MerchantRecipeList getRecipes(EntityPlayer par1EntityPlayer) {
      if(this.buyingList == null) {
         this.addDefaultEquipmentAndRecipies(1);
      }

      return this.buyingList;
   }

   private float adjustProbability(float par1) {
      float f1 = par1 + this.field_82191_bN;
      return f1 > 0.9F?0.9F - (f1 - 0.9F):f1;
   }

   private void addDefaultEquipmentAndRecipies(int par1) {
      if(this.buyingList != null) {
         this.field_82191_bN = MathHelper.sqrt_float((float)this.buyingList.size()) * 0.2F;
      } else {
         this.field_82191_bN = 0.0F;
      }

      MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
      boolean shuffle = true;
      switch(this.getProfession()) {
      case 0:
         addItemToSwapForAnEmerald(merchantrecipelist, Items.wheat, super.rand, this.adjustProbability(0.9F));
         addItemToSwapForAnEmerald(merchantrecipelist, Item.getItemFromBlock(Blocks.wool), super.rand, this.adjustProbability(0.5F));
         addItemToSwapForAnEmerald(merchantrecipelist, Items.chicken, super.rand, this.adjustProbability(0.5F));
         addItemToSwapForAnEmerald(merchantrecipelist, Items.cooked_fished, super.rand, this.adjustProbability(0.4F));
         addItemToBuyOrSell(merchantrecipelist, Items.bread, super.rand, this.adjustProbability(0.9F));
         addItemToBuyOrSell(merchantrecipelist, Items.melon, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.apple, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.cookie, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.shears, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.flint_and_steel, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.cooked_chicken, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.arrow, super.rand, this.adjustProbability(0.5F));
         if(super.rand.nextFloat() < this.adjustProbability(0.5F)) {
            merchantrecipelist.add(new MerchantRecipe(new ItemStack(Blocks.gravel, 10), new ItemStack(Items.emerald), new ItemStack(Items.flint, 4 + super.rand.nextInt(2), 0)));
         }
         break;
      case 1:
      case 2:
         shuffle = false;
         if(this.buyingList == null) {
            merchantrecipelist.add(new MerchantRecipe(Witchery.Items.GENERIC.itemKobolditeDust.createStack(9), new ItemStack(Items.gold_nugget, 5), Witchery.Items.GENERIC.itemKobolditeNugget.createStack()));
         } else if(this.buyingList.size() == 1) {
            merchantrecipelist.add(new MerchantRecipe(Witchery.Items.GENERIC.itemKobolditeDust.createStack(16), new ItemStack(Items.gold_ingot), Witchery.Items.GENERIC.itemKobolditeNugget.createStack(2)));
         } else if(this.buyingList.size() == 2) {
            merchantrecipelist.add(new MerchantRecipe(Witchery.Items.GENERIC.itemKobolditeNugget.createStack(9), new ItemStack(Items.emerald), Witchery.Items.GENERIC.itemKobolditeIngot.createStack()));
         }
         break;
      case 3:
         addItemToSwapForAnEmerald(merchantrecipelist, Items.coal, super.rand, this.adjustProbability(0.7F));
         addItemToSwapForAnEmerald(merchantrecipelist, Items.iron_ingot, super.rand, this.adjustProbability(0.5F));
         addItemToSwapForAnEmerald(merchantrecipelist, Items.gold_ingot, super.rand, this.adjustProbability(0.5F));
         addItemToSwapForAnEmerald(merchantrecipelist, Items.diamond, super.rand, this.adjustProbability(0.5F));
         addItemToBuyOrSell(merchantrecipelist, Items.iron_sword, super.rand, this.adjustProbability(0.5F));
         addItemToBuyOrSell(merchantrecipelist, Items.diamond_sword, super.rand, this.adjustProbability(0.5F));
         addItemToBuyOrSell(merchantrecipelist, Items.iron_axe, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.diamond_axe, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.iron_pickaxe, super.rand, this.adjustProbability(0.5F));
         addItemToBuyOrSell(merchantrecipelist, Items.diamond_pickaxe, super.rand, this.adjustProbability(0.5F));
         addItemToBuyOrSell(merchantrecipelist, Items.iron_shovel, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.diamond_shovel, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.iron_hoe, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.diamond_hoe, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.iron_boots, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.diamond_boots, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.iron_helmet, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.diamond_helmet, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.iron_chestplate, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.diamond_chestplate, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.iron_leggings, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.diamond_leggings, super.rand, this.adjustProbability(0.2F));
         addItemToBuyOrSell(merchantrecipelist, Items.chainmail_boots, super.rand, this.adjustProbability(0.1F));
         addItemToBuyOrSell(merchantrecipelist, Items.chainmail_helmet, super.rand, this.adjustProbability(0.1F));
         addItemToBuyOrSell(merchantrecipelist, Items.chainmail_chestplate, super.rand, this.adjustProbability(0.1F));
         addItemToBuyOrSell(merchantrecipelist, Items.chainmail_leggings, super.rand, this.adjustProbability(0.1F));
         break;
      case 4:
         addItemToSwapForAnEmerald(merchantrecipelist, Items.coal, super.rand, this.adjustProbability(0.7F));
         addItemToSwapForAnEmerald(merchantrecipelist, Items.porkchop, super.rand, this.adjustProbability(0.5F));
         addItemToSwapForAnEmerald(merchantrecipelist, Items.beef, super.rand, this.adjustProbability(0.5F));
         addItemToBuyOrSell(merchantrecipelist, Items.saddle, super.rand, this.adjustProbability(0.1F));
         addItemToBuyOrSell(merchantrecipelist, Items.leather_chestplate, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.leather_boots, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.leather_helmet, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.leather_leggings, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.cooked_porkchop, super.rand, this.adjustProbability(0.3F));
         addItemToBuyOrSell(merchantrecipelist, Items.cooked_beef, super.rand, this.adjustProbability(0.3F));
      }

      if(merchantrecipelist.isEmpty()) {
         merchantrecipelist.add(new MerchantRecipe(getItemStackToSwapForAnEmerald(Items.gold_ingot, super.rand), Items.emerald));
      }

      if(shuffle) {
         Collections.shuffle(merchantrecipelist);
      }

      if(this.buyingList == null) {
         this.buyingList = new MerchantRecipeList();
      }

      for(int l = 0; l < par1 && l < merchantrecipelist.size(); ++l) {
         this.buyingList.addToListWithCheck((MerchantRecipe)merchantrecipelist.get(l));
      }

   }

   @SideOnly(Side.CLIENT)
   public void setRecipes(MerchantRecipeList par1MerchantRecipeList) {}

   public static void addItemToSwapForAnEmerald(MerchantRecipeList buyList, Item item, Random rand, float probability) {
      if(rand.nextFloat() < probability) {
         if(rand.nextInt(3) == 0) {
            buyList.add(new MerchantRecipe(getItemStackToSwapForAnEmerald(item, rand), Witchery.Items.GENERIC.itemKobolditeDust.createStack()));
         } else {
            buyList.add(new MerchantRecipe(getItemStackToSwapForAnEmerald(item, rand), Items.emerald));
         }
      }

   }

   private static ItemStack getItemStackToSwapForAnEmerald(Item item, Random rand) {
      return new ItemStack(item, getQuantityToSwapForAnEmerald(item, rand), 0);
   }

   private static int getQuantityToSwapForAnEmerald(Item item, Random rand) {
      Tuple tuple = (Tuple)villagersSellingList.get(item);
      return tuple == null?1:(((Integer)tuple.getFirst()).intValue() >= ((Integer)tuple.getSecond()).intValue()?((Integer)tuple.getFirst()).intValue():((Integer)tuple.getFirst()).intValue() + rand.nextInt(((Integer)tuple.getSecond()).intValue() - ((Integer)tuple.getFirst()).intValue()));
   }

   public static void addItemToBuyOrSell(MerchantRecipeList list, Item item, Random rand, float probability) {
      if(rand.nextFloat() < probability) {
         int i = quantityToBuyOrSell(item, rand);
         ItemStack itemstack;
         ItemStack itemstack1;
         if(i < 0) {
            itemstack = Witchery.Items.GENERIC.itemKobolditeNugget.createStack(1);
            itemstack1 = new ItemStack(item, -i, 0);
         } else {
            itemstack = Witchery.Items.GENERIC.itemKobolditeNugget.createStack(i);
            itemstack1 = new ItemStack(item, 1, 0);
         }

         list.add(new MerchantRecipe(itemstack, itemstack1));
      }

   }

   private static int quantityToBuyOrSell(Item item, Random rand) {
      Tuple tuple = (Tuple)blacksmithSellingList.get(item);
      return tuple == null?1:(((Integer)tuple.getFirst()).intValue() >= ((Integer)tuple.getSecond()).intValue()?((Integer)tuple.getFirst()).intValue():((Integer)tuple.getFirst()).intValue() + rand.nextInt(((Integer)tuple.getSecond()).intValue() - ((Integer)tuple.getFirst()).intValue()));
   }

   @SideOnly(Side.CLIENT)
   public void handleHealthUpdate(byte par1) {
      if(par1 == 12) {
         this.generateRandomParticles("heart");
      } else if(par1 == 13) {
         this.generateRandomParticles("angryVillager");
      } else if(par1 == 14) {
         this.generateRandomParticles("happyVillager");
      } else {
         super.handleHealthUpdate(par1);
      }

   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);
      int trade = super.rand.nextInt(5);
      this.setProfession(trade);
      return par1EntityLivingData;
   }

   @SideOnly(Side.CLIENT)
   private void generateRandomParticles(String par1Str) {
      for(int i = 0; i < 5; ++i) {
         double d0 = super.rand.nextGaussian() * 0.02D;
         double d1 = super.rand.nextGaussian() * 0.02D;
         double d2 = super.rand.nextGaussian() * 0.02D;
         super.worldObj.spawnParticle(par1Str, super.posX + (double)(super.rand.nextFloat() * super.width * 2.0F) - (double)super.width, super.posY + 1.0D + (double)(super.rand.nextFloat() * super.height), super.posZ + (double)(super.rand.nextFloat() * super.width * 2.0F) - (double)super.width, d0, d1, d2);
      }

   }

   public void setLookingForHome() {
      this.isLookingForHome = true;
   }

   public EntityGoblin createChild(EntityAgeable par1EntityAgeable) {
      EntityGoblin entityvillager = new EntityGoblin(super.worldObj);
      entityvillager.onSpawnWithEgg((IEntityLivingData)null);
      return entityvillager;
   }

   static {
      villagersSellingList.put(Items.coal, new Tuple(Integer.valueOf(16), Integer.valueOf(24)));
      villagersSellingList.put(Items.iron_ingot, new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
      villagersSellingList.put(Items.gold_ingot, new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
      villagersSellingList.put(Items.diamond, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
      villagersSellingList.put(Items.paper, new Tuple(Integer.valueOf(24), Integer.valueOf(36)));
      villagersSellingList.put(Items.book, new Tuple(Integer.valueOf(11), Integer.valueOf(13)));
      villagersSellingList.put(Items.written_book, new Tuple(Integer.valueOf(1), Integer.valueOf(1)));
      villagersSellingList.put(Items.ender_pearl, new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
      villagersSellingList.put(Items.ender_eye, new Tuple(Integer.valueOf(2), Integer.valueOf(3)));
      villagersSellingList.put(Items.porkchop, new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
      villagersSellingList.put(Items.beef, new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
      villagersSellingList.put(Items.chicken, new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
      villagersSellingList.put(Items.cooked_fished, new Tuple(Integer.valueOf(9), Integer.valueOf(13)));
      villagersSellingList.put(Items.wheat_seeds, new Tuple(Integer.valueOf(34), Integer.valueOf(48)));
      villagersSellingList.put(Items.melon_seeds, new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
      villagersSellingList.put(Items.pumpkin_seeds, new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
      villagersSellingList.put(Items.wheat, new Tuple(Integer.valueOf(18), Integer.valueOf(22)));
      villagersSellingList.put(Item.getItemFromBlock(Blocks.wool), new Tuple(Integer.valueOf(14), Integer.valueOf(22)));
      villagersSellingList.put(Items.rotten_flesh, new Tuple(Integer.valueOf(36), Integer.valueOf(64)));
      blacksmithSellingList.put(Items.flint_and_steel, new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.shears, new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.iron_sword, new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
      blacksmithSellingList.put(Items.diamond_sword, new Tuple(Integer.valueOf(12), Integer.valueOf(14)));
      blacksmithSellingList.put(Items.iron_axe, new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.diamond_axe, new Tuple(Integer.valueOf(9), Integer.valueOf(12)));
      blacksmithSellingList.put(Items.iron_pickaxe, new Tuple(Integer.valueOf(7), Integer.valueOf(9)));
      blacksmithSellingList.put(Items.diamond_pickaxe, new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
      blacksmithSellingList.put(Items.iron_shovel, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
      blacksmithSellingList.put(Items.diamond_shovel, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.iron_hoe, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
      blacksmithSellingList.put(Items.diamond_hoe, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.iron_boots, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
      blacksmithSellingList.put(Items.diamond_boots, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.iron_helmet, new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
      blacksmithSellingList.put(Items.diamond_helmet, new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.iron_chestplate, new Tuple(Integer.valueOf(10), Integer.valueOf(14)));
      blacksmithSellingList.put(Items.diamond_chestplate, new Tuple(Integer.valueOf(16), Integer.valueOf(19)));
      blacksmithSellingList.put(Items.iron_leggings, new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
      blacksmithSellingList.put(Items.diamond_leggings, new Tuple(Integer.valueOf(11), Integer.valueOf(14)));
      blacksmithSellingList.put(Items.chainmail_boots, new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
      blacksmithSellingList.put(Items.chainmail_helmet, new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
      blacksmithSellingList.put(Items.chainmail_chestplate, new Tuple(Integer.valueOf(11), Integer.valueOf(15)));
      blacksmithSellingList.put(Items.chainmail_leggings, new Tuple(Integer.valueOf(9), Integer.valueOf(11)));
      blacksmithSellingList.put(Items.bread, new Tuple(Integer.valueOf(-4), Integer.valueOf(-2)));
      blacksmithSellingList.put(Items.melon, new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
      blacksmithSellingList.put(Items.apple, new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
      blacksmithSellingList.put(Items.cookie, new Tuple(Integer.valueOf(-10), Integer.valueOf(-7)));
      blacksmithSellingList.put(Item.getItemFromBlock(Blocks.glass), new Tuple(Integer.valueOf(-5), Integer.valueOf(-3)));
      blacksmithSellingList.put(Item.getItemFromBlock(Blocks.bookshelf), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.leather_chestplate, new Tuple(Integer.valueOf(4), Integer.valueOf(5)));
      blacksmithSellingList.put(Items.leather_boots, new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.leather_helmet, new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.leather_leggings, new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
      blacksmithSellingList.put(Items.saddle, new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
      blacksmithSellingList.put(Items.experience_bottle, new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
      blacksmithSellingList.put(Items.redstone, new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
      blacksmithSellingList.put(Items.compass, new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
      blacksmithSellingList.put(Items.clock, new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
      blacksmithSellingList.put(Item.getItemFromBlock(Blocks.glowstone), new Tuple(Integer.valueOf(-3), Integer.valueOf(-1)));
      blacksmithSellingList.put(Items.cooked_porkchop, new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
      blacksmithSellingList.put(Items.cooked_beef, new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
      blacksmithSellingList.put(Items.cooked_chicken, new Tuple(Integer.valueOf(-8), Integer.valueOf(-6)));
      blacksmithSellingList.put(Items.ender_eye, new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
      blacksmithSellingList.put(Items.arrow, new Tuple(Integer.valueOf(-12), Integer.valueOf(-8)));
   }
}
