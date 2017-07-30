package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.ai.EntityAIWanderWithRestriction;
import com.emoniph.witchery.item.ItemGeneralContract;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import com.emoniph.witchery.util.TimeUtil;
import java.util.HashMap;
import java.util.Random;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityImp extends EntityTameable implements IMob, IEntitySelector, EntityAIWanderWithRestriction.IHomeLocationProvider {

   private float field_70926_e;
   private float field_70924_f;
   private boolean field_70928_h;
   private static final int MAX_WANDER_RANGE = 16;
   private int secretsShared;
   private int homeX;
   private int homeY;
   private int homeZ;
   private long lastGiftTime;
   private long powerUpExpiry;
   private static final HashMap shinies = new HashMap();
   private static final int REWARD_AFFECTION_LEVEL = 20;
   private static final long GIFT_DELAY_TICKS;
   private static final ItemStack[] EXTRA_DROPS;
   private static final String[] DEMON_NAMES;


   public EntityImp(World par1World) {
      super(par1World);
      super.isImmuneToFire = true;
      this.setSize(0.4F, 1.3F);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, new EntityAILeapAtTarget(this, 0.4F));
      super.tasks.addTask(3, new EntityAIAttackOnCollide(this, 1.0D, true));
      super.tasks.addTask(4, new EntityAIWanderWithRestriction(this, 1.0D, this));
      super.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(6, new EntityAILookIdle(this));
      super.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
      super.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
      super.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
      super.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, false, true, this));
      this.setTamed(false);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(18, Integer.valueOf(0));
      super.dataWatcher.addObject(19, Integer.valueOf(0));
   }

   private void setAffection(int affection) {
      super.dataWatcher.updateObject(18, Integer.valueOf(affection));
   }

   private int getAffection() {
      return super.dataWatcher.getWatchableObjectInt(18);
   }

   private void setPowered(boolean powered) {
      if(!super.worldObj.isRemote) {
         super.dataWatcher.updateObject(19, Integer.valueOf(powered?1:0));
      }

   }

   public boolean isPowered() {
      return super.dataWatcher.getWatchableObjectInt(19) == 1;
   }

   public boolean isEntityApplicable(Entity target) {
      return !this.isTamed()?target instanceof EntityPlayer:target == this.getAttackTarget();
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.imp.name");
   }

   public boolean isAIEnabled() {
      return true;
   }

   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("Affection", this.getAffection());
      par1NBTTagCompound.setInteger("SecretsShared", this.secretsShared);
      par1NBTTagCompound.setLong("LastGiftTime", this.lastGiftTime);
      par1NBTTagCompound.setLong("PowerUpUntil2", this.powerUpExpiry);
      par1NBTTagCompound.setInteger("HomeLocX", this.homeX);
      par1NBTTagCompound.setInteger("HomeLocY", this.homeY);
      par1NBTTagCompound.setInteger("HomeLocZ", this.homeZ);
   }

   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setAffection(par1NBTTagCompound.getInteger("Affection"));
      this.secretsShared = par1NBTTagCompound.getInteger("SecretsShared");
      this.lastGiftTime = par1NBTTagCompound.getLong("LastGiftTime");
      long time = TimeUtil.getServerTimeInTicks();
      if(par1NBTTagCompound.hasKey("PowerUpUntil2")) {
         this.powerUpExpiry = par1NBTTagCompound.getLong("PowerUpUntil2");
      } else if(par1NBTTagCompound.hasKey("PowerUpUntil")) {
         this.powerUpExpiry = par1NBTTagCompound.getLong("PowerUpUntil");
         if(this.powerUpExpiry > 0L) {
            this.powerUpExpiry = time + (long)TimeUtil.minsToTicks(60);
         }
      }

      if(time < this.powerUpExpiry) {
         this.setPowered(true);
      }

      this.homeX = par1NBTTagCompound.getInteger("HomeLocX");
      this.homeY = par1NBTTagCompound.getInteger("HomeLocY");
      this.homeZ = par1NBTTagCompound.getInteger("HomeLocZ");
   }

   protected String getLivingSound() {
      return "witchery:mob.imp.laugh";
   }

   protected float getSoundPitch() {
      return this.isPowered()?(super.rand.nextFloat() - super.rand.nextFloat()) * 0.2F + 0.7F:(super.rand.nextFloat() - super.rand.nextFloat()) * 0.2F + 1.1F;
   }

   protected String getHurtSound() {
      return "witchery:mob.imp.hit";
   }

   protected String getDeathSound() {
      return "witchery:mob.imp.death";
   }

   protected float getSoundVolume() {
      return 0.5F;
   }

   public int getTalkInterval() {
      return TimeUtil.secsToTicks(40);
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
      if(!super.worldObj.isRemote && TimeUtil.secondsElapsed(300, (long)super.ticksExisted) && TameableUtil.hasOwner(this)) {
         EntityLivingBase owner = this.getOwner();
         if(owner instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)owner;
            this.setAffection(Math.max(0, this.getAffection() - 1));
            if(this.getAffection() == 0 && super.ticksExisted > TimeUtil.minsToTicks(60) && super.worldObj.rand.nextDouble() < 0.01D) {
               ParticleEffect.FLAME.send(SoundEffect.WITCHERY_MOB_IMP_LAUGH, this, 1.0D, 1.0D, 16);
               ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.goodbye", new Object[]{this.getCommandSenderName()});
               this.setDead();
            }
         }
      }

      if(!super.worldObj.isRemote && this.powerUpExpiry > 0L && this.isPowerupExpired()) {
         this.setPowered(false);
         this.powerUpExpiry = 0L;
      }

      if(super.ticksExisted % 20 == 0) {
         if(this.isPowered()) {
            if((double)super.width != 0.6D) {
               this.setSize(0.6F, 1.3F);
            }

            if(!super.worldObj.isRemote) {
               this.heal(1.0F);
            }
         } else if((double)super.width != 0.4D) {
            this.setSize(0.4F, 1.3F);
         }
      }

      if(super.ticksExisted % 400 == 0) {
         this.heal(1.0F);
      }

   }

   private boolean isPowerupExpired() {
      return TimeUtil.getServerTimeInTicks() >= this.powerUpExpiry;
   }

   public void onUpdate() {
      super.onUpdate();
      if(super.worldObj.isRemote && this.isPowered()) {
         super.worldObj.spawnParticle(ParticleEffect.FLAME.toString(), super.posX - (double)super.width * 0.5D + super.worldObj.rand.nextDouble() * (double)super.width, 0.1D + super.posY + super.worldObj.rand.nextDouble() * 2.0D, super.posZ - (double)super.width * 0.5D + super.worldObj.rand.nextDouble() * (double)super.width, 0.0D, 0.0D, 0.0D);
      }

   }

   public boolean attackEntityFrom(DamageSource source, float damage) {
      return super.attackEntityFrom(source, Math.min(damage, this.isPowered()?5.0F:15.0F));
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.isPowered()?8.0F:4.0F);
   }

   public boolean interact(EntityPlayer player) {
      ItemStack stack = player.inventory.getCurrentItem();
      if(stack == null) {
         return true;
      } else if(super.worldObj.isRemote) {
         return false;
      } else {
         if(this.isTamed()) {
            if(Witchery.Items.GENERIC.itemDemonHeart.isMatch(stack)) {
               if(!player.capabilities.isCreativeMode) {
                  --stack.stackSize;
                  if(stack.stackSize <= 0) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                  }
               }

               if(!super.worldObj.isRemote) {
                  this.powerUpExpiry = TimeUtil.getServerTimeInTicks() + (long)TimeUtil.minsToTicks(60);
                  this.setPowered(true);
                  SoundEffect.WITCHERY_MOB_IMP_LAUGH.playAtPlayer(super.worldObj, player, 0.5F, this.getSoundPitch());
                  ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.gift.power", new Object[]{this.getCommandSenderName()});
               }
            } else if(Witchery.Items.GENERIC.itemIcyNeedle.isMatch(stack)) {
               if(!player.capabilities.isCreativeMode) {
                  --stack.stackSize;
                  if(stack.stackSize <= 0) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                  }
               }

               if(!super.worldObj.isRemote) {
                  this.powerUpExpiry = 0L;
                  this.setPowered(false);
                  SoundEffect.WITCHERY_MOB_IMP_LAUGH.playAtPlayer(super.worldObj, player, 0.5F, this.getSoundPitch());
                  ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.gift.powerloss", new Object[]{this.getCommandSenderName()});
               }
            } else {
               String stackForPlayer;
               if(!ItemGeneralContract.isBoundContract(stack)) {
                  if(!super.worldObj.isRemote) {
                     Integer var8 = (Integer)shinies.get(stack.getItem());
                     if(var8 != null && stack.getItemDamage() == 0) {
                        long EXPERIENCE_NEEDED = TimeUtil.getServerTimeInTicks();
                        if(!player.capabilities.isCreativeMode) {
                           --stack.stackSize;
                           if(stack.stackSize <= 0) {
                              player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                           }
                        }

                        int var11 = this.getAffection() + var8.intValue();
                        SoundEffect.WITCHERY_MOB_IMP_LAUGH.playAtPlayer(super.worldObj, player, 0.5F, this.getSoundPitch());
                        if(var11 >= 20 && (EXPERIENCE_NEEDED > this.lastGiftTime + GIFT_DELAY_TICKS || player.capabilities.isCreativeMode) && super.rand.nextInt(Math.max(1, 10 - Math.max(var11 - 20, 0))) == 0) {
                           this.lastGiftTime = EXPERIENCE_NEEDED;
                           var11 = 0;
                           ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.gift.reciprocate", new Object[]{this.getCommandSenderName()});
                           stackForPlayer = null;
                           ItemStack var12;
                           switch(this.secretsShared) {
                           case 0:
                              var12 = Witchery.Items.GENERIC.itemBrewSoulHunger.createStack();
                              ++this.secretsShared;
                              break;
                           case 1:
                              var12 = Witchery.Items.GENERIC.itemBrewSoulFear.createStack();
                              ++this.secretsShared;
                              break;
                           case 2:
                              var12 = Witchery.Items.GENERIC.itemBrewSoulAnguish.createStack();
                              ++this.secretsShared;
                              break;
                           case 3:
                              var12 = Witchery.Items.GENERIC.itemContractTorment.createStack();
                              ++this.secretsShared;
                              break;
                           default:
                              var12 = EXTRA_DROPS[super.rand.nextInt(EXTRA_DROPS.length)].copy();
                           }

                           if(var12 != null) {
                              ParticleEffect.INSTANT_SPELL.send(SoundEffect.NOTE_HARP, this, 1.0D, 1.0D, 16);
                              super.worldObj.spawnEntityInWorld(new EntityItem(super.worldObj, super.posX, super.posY, super.posZ, var12));
                           }
                        } else if(EXPERIENCE_NEEDED < this.lastGiftTime + GIFT_DELAY_TICKS) {
                           ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.gift.toomany", new Object[]{this.getCommandSenderName()});
                        } else {
                           ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.gift.like", new Object[]{this.getCommandSenderName()});
                        }

                        this.setAffection(var11);
                     } else {
                        SoundEffect.WITCHERY_MOB_IMP_LAUGH.playAtPlayer(super.worldObj, player, 0.5F, this.getSoundPitch());
                        ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.gift.hate", new Object[]{this.getCommandSenderName()});
                     }
                  }

                  return true;
               }

               if(!super.worldObj.isRemote) {
                  SoundEffect.WITCHERY_MOB_IMP_LAUGH.playAtPlayer(super.worldObj, player, 0.5F, this.getSoundPitch());
                  if(!this.isPowered()) {
                     if(this.getAffection() >= 20) {
                        long boundEntity = TimeUtil.getServerTimeInTicks();
                        if(boundEntity <= this.lastGiftTime + GIFT_DELAY_TICKS && !player.capabilities.isCreativeMode) {
                           ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.spell.toooften", new Object[]{this.getCommandSenderName()});
                        } else {
                           ItemGeneralContract contract = ItemGeneralContract.getContract(stack);
                           EntityLivingBase affection = Witchery.Items.TAGLOCK_KIT.getBoundEntity(super.worldObj, player, stack, Integer.valueOf(1));
                           if(affection != null) {
                              if(contract.activate(stack, affection)) {
                                 this.lastGiftTime = boundEntity;
                                 ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.spell.feelthefire", new Object[]{this.getCommandSenderName(), affection.getCommandSenderName()});
                                 if(!player.capabilities.isCreativeMode) {
                                    --stack.stackSize;
                                    if(stack.stackSize <= 0) {
                                       player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                                    }
                                 }
                              } else {
                                 ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.spell.failed", new Object[]{this.getCommandSenderName(), affection.getCommandSenderName()});
                              }
                           } else {
                              stackForPlayer = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(stack, Integer.valueOf(1));
                              ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.spell.cannotfind", new Object[]{this.getCommandSenderName(), stackForPlayer});
                           }
                        }
                     } else {
                        ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.spell.notliked", new Object[]{this.getCommandSenderName()});
                     }
                  } else {
                     ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.spell.toomuchpower", new Object[]{this.getCommandSenderName()});
                  }
               }
            }
         } else if(Witchery.Items.GENERIC.itemContractOwnership.isMatch(stack)) {
            if(!super.worldObj.isRemote) {
               EntityLivingBase var9 = ItemGeneralContract.getBoundEntity(super.worldObj, player, stack);
               if(var9 == player) {
                  boolean var10 = true;
                  if(player.experienceLevel < 25 && !player.capabilities.isCreativeMode) {
                     SoundEffect.WITCHERY_MOB_IMP_LAUGH.playAtPlayer(super.worldObj, player, 0.5F, this.getSoundPitch());
                     ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.contract.noxp", new Object[]{this.getCommandSenderName()});
                  } else {
                     if(!player.capabilities.isCreativeMode) {
                        --stack.stackSize;
                        if(stack.stackSize <= 0) {
                           player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                        }
                     }

                     player.addExperienceLevel(-25);
                     this.setTamed(true);
                     TameableUtil.setOwner(this, player);
                     this.setAttackTarget((EntityLivingBase)null);
                     this.setPathToEntity((PathEntity)null);
                     this.homeX = (int)super.posX;
                     this.homeY = (int)super.posY;
                     this.homeZ = (int)super.posZ;
                     this.func_110163_bv();
                     SoundEffect.WITCHERY_MOB_IMP_LAUGH.playAtPlayer(super.worldObj, player, 0.5F, this.getSoundPitch());
                     ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.contract.deal", new Object[]{this.getCommandSenderName()});
                     this.setCustomNameTag(getDemonName(super.rand));
                  }
               } else if(var9 != null) {
                  SoundEffect.WITCHERY_MOB_IMP_LAUGH.playAtPlayer(super.worldObj, player, 0.5F, this.getSoundPitch());
                  ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.contract.notowners", new Object[]{this.getCommandSenderName()});
               } else {
                  SoundEffect.WITCHERY_MOB_IMP_LAUGH.playAtPlayer(super.worldObj, player, 0.5F, this.getSoundPitch());
                  ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "entity.witchery.imp.contract.unsigned", new Object[]{this.getCommandSenderName()});
               }
            }

            return true;
         }

         return super.interact(player);
      }
   }

   public EntityImp createChild(EntityAgeable par1EntityAgeable) {
      return null;
   }

   public boolean canMateWith(EntityAnimal par1EntityAnimal) {
      return false;
   }

   protected boolean canDespawn() {
      return true;
   }

   private static String getDemonName(Random rand) {
      return rand.nextInt(5) == 0?DEMON_NAMES[rand.nextInt(DEMON_NAMES.length)]:DEMON_NAMES[rand.nextInt(DEMON_NAMES.length)] + " " + DEMON_NAMES[rand.nextInt(DEMON_NAMES.length)];
   }

   public double getHomeX() {
      return (double)this.homeX;
   }

   public double getHomeY() {
      return (double)this.homeY;
   }

   public double getHomeZ() {
      return (double)this.homeZ;
   }

   public double getHomeRange() {
      return 16.0D;
   }

   static {
      shinies.put((new ItemStack(Items.diamond)).getItem(), Integer.valueOf(8));
      shinies.put((new ItemStack(Items.diamond_axe)).getItem(), Integer.valueOf(24));
      shinies.put((new ItemStack(Items.diamond_hoe)).getItem(), Integer.valueOf(16));
      shinies.put((new ItemStack(Items.diamond_sword)).getItem(), Integer.valueOf(16));
      shinies.put((new ItemStack(Items.diamond_shovel)).getItem(), Integer.valueOf(8));
      shinies.put((new ItemStack(Items.diamond_pickaxe)).getItem(), Integer.valueOf(24));
      shinies.put((new ItemStack(Items.emerald)).getItem(), Integer.valueOf(3));
      shinies.put((new ItemStack(Items.gold_ingot)).getItem(), Integer.valueOf(1));
      shinies.put((new ItemStack(Items.nether_star)).getItem(), Integer.valueOf(16));
      shinies.put((new ItemStack(Items.blaze_rod)).getItem(), Integer.valueOf(1));
      shinies.put((new ItemStack(Items.ghast_tear)).getItem(), Integer.valueOf(4));
      shinies.put((new ItemStack(Items.golden_axe)).getItem(), Integer.valueOf(3));
      shinies.put((new ItemStack(Items.golden_sword)).getItem(), Integer.valueOf(2));
      shinies.put((new ItemStack(Items.golden_hoe)).getItem(), Integer.valueOf(2));
      shinies.put((new ItemStack(Items.golden_shovel)).getItem(), Integer.valueOf(1));
      shinies.put((new ItemStack(Items.golden_pickaxe)).getItem(), Integer.valueOf(3));
      shinies.put((new ItemStack(Blocks.gold_block)).getItem(), Integer.valueOf(9));
      shinies.put((new ItemStack(Blocks.emerald_block)).getItem(), Integer.valueOf(27));
      shinies.put((new ItemStack(Blocks.diamond_block)).getItem(), Integer.valueOf(72));
      shinies.put((new ItemStack(Blocks.lapis_block)).getItem(), Integer.valueOf(7));
      shinies.put((new ItemStack(Blocks.redstone_block)).getItem(), Integer.valueOf(5));
      GIFT_DELAY_TICKS = (long)TimeUtil.minsToTicks(3);
      EXTRA_DROPS = new ItemStack[]{Witchery.Items.GENERIC.itemBatWool.createStack(5), Witchery.Items.GENERIC.itemDogTongue.createStack(5), Witchery.Items.GENERIC.itemToeOfFrog.createStack(2), Witchery.Items.GENERIC.itemOwletsWing.createStack(2), Witchery.Items.GENERIC.itemBranchEnt.createStack(1), Witchery.Items.GENERIC.itemInfernalBlood.createStack(2), Witchery.Items.GENERIC.itemCreeperHeart.createStack(2)};
      DEMON_NAMES = new String[]{"Ppaironael", "Aethon", "Tyrnak", "Beelzebuth", "Botis", "Moloch", "Taet", "Epnanaet", "Unonom", "Hexpemsazon", "Thayax", "Ethahoat", "Pruslas", "Ahtuxies", "Laripael", "Elxar", "Tarihimal", "Sapanolr", "Sahaminapiel", "Honed", "Oghmus", "Zedeson", "Halmaneop", "Nopoz", "Ekarnahox", "Sacuhatakael", "Ticos", "Arametheus", "Azmodaeus", "Larhepeis", "Topriraiz", "Rarahaimzah", "Tedrahamael", "Osaselael", "Phlegon", "Nelokhiel", "Haristum", "Zul", "Larhepeis", "Aamon", "Tramater", "Ehhbes", "Kra`an", "Quarax", "Hotesiatrem", "Surgat", "Nu`uhn", "Litedabh", "Unonom", "Bolenoz", "Hilopael", "Haristum", "Uhn", "Hiepacth", "Pemcapso", "Ankou", "Pundohien", "Koit", "Montobulus", "Amsaset", "Aropet", "Isnal", "Solael", "Exroh", "Sidragrosam", "Pnecamob", "Malashim", "Beelzebuth", "Ehohit", "Izatap", "Olon", "Assoaz", "Agalierept", "Krakus", "Umlaboor", "Aknrar", "Damaz", "Rhysus", "Pundohien", "Ba`al", "Rasuniolpas", "Anhoor", "Nyarlathotep", "Krakus", "Larhepeis", "Itakup", "Erdok", "Umlaboor", "Ezon", "Krakus", "Glassyalabolas", "Kra`an", "Ehnnat", "Terxor", "Asramel", "Tadal", "Arpzih", "Azmodaeus", "Henbolaron", "Rhysus"};
   }
}
