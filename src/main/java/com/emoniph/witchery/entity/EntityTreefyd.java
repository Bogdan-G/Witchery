package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityCorpse;
import com.emoniph.witchery.entity.EntityCovenWitch;
import com.emoniph.witchery.entity.EntityEnt;
import com.emoniph.witchery.entity.EntityFlyingTameable;
import com.emoniph.witchery.entity.EntityGoblin;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.entity.ai.EntityAITreefydWander;
import com.emoniph.witchery.familiar.IFamiliar;
import com.emoniph.witchery.item.ItemTaglockKit;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityTreefyd extends EntityMob implements IEntitySelector {

   public EntityTreefyd.CreatureID testID = new EntityTreefyd.CreatureID(new UUID(0L, 0L), "");
   private static ArrayList groupables = null;
   private ArrayList knownPlayers = new ArrayList();
   private ArrayList knownCreatureTypes = new ArrayList();
   private ArrayList knownCreatures = new ArrayList();


   public EntityTreefyd(World par1World) {
      super(par1World);
      this.setSize(0.4F, 1.8F);
      this.getNavigator().setAvoidsWater(true);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
      super.tasks.addTask(5, new EntityAITreefydWander(this, 0.8D));
      super.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      super.tasks.addTask(6, new EntityAILookIdle(this));
      super.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 0, false, true, this));
      super.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
   }

   public boolean isEntityApplicable(Entity entity) {
      if(entity.getClass() != this.getClass() && !(entity instanceof EntityHornedHuntsman) && !(entity instanceof EntityEnt) && !(entity instanceof EntityFlying) && !(entity instanceof EntityFlyingTameable) && !(entity instanceof EntityAmbientCreature) && !(entity instanceof EntityWaterMob) && !this.isFamiliar(entity) && !(entity instanceof EntityCovenWitch) && !(entity instanceof EntityCorpse)) {
         if(entity instanceof EntityPlayer) {
            EntityPlayer creature = (EntityPlayer)entity;
            String ownerName = this.getOwnerName();
            if(ownerName != null && !ownerName.isEmpty() && creature.getCommandSenderName().equals(ownerName)) {
               return false;
            }

            if(this.knownPlayers != null && this.knownPlayers.contains(creature.getCommandSenderName())) {
               return false;
            }
         }

         if(entity instanceof EntityLiving) {
            EntityLiving creature1 = (EntityLiving)entity;
            if(this.knownCreatureTypes != null && this.knownCreatureTypes.contains(creature1.getCommandSenderName())) {
               return false;
            }

            this.testID.id = entity.getUniqueID();
            if(this.knownCreatures != null && this.knownCreatures.contains(this.testID)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public int getTalkInterval() {
      return super.getTalkInterval() * 2;
   }

   protected boolean interact(EntityPlayer player) {
      if(!super.worldObj.isRemote && player != null && player.getCommandSenderName().equals(this.getOwnerName())) {
         ItemStack stack = player.getHeldItem();
         if(stack != null && stack.getItem() == Witchery.Items.TAGLOCK_KIT) {
            this.setAttackTarget((EntityLivingBase)null);
            ItemTaglockKit.BoundType var8 = Witchery.Items.TAGLOCK_KIT.getBoundEntityType(stack, Integer.valueOf(1));
            switch(EntityTreefyd.NamelessClass2093483214.$SwitchMap$com$emoniph$witchery$item$ItemTaglockKit$BoundType[var8.ordinal()]) {
            case 1:
               String otherUsername = Witchery.Items.TAGLOCK_KIT.getBoundUsername(stack, Integer.valueOf(1));
               if(!player.getCommandSenderName().equals(otherUsername)) {
                  if(!player.isSneaking() && !this.knownPlayers.contains(otherUsername)) {
                     this.knownPlayers.add(otherUsername);
                  } else {
                     if(!player.isSneaking() || !this.knownPlayers.contains(otherUsername)) {
                        this.showCurrentKnownEntities(player);
                        return super.interact(player);
                     }

                     this.knownPlayers.remove(otherUsername);
                  }

                  --stack.stackSize;
                  if(stack.stackSize <= 0) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                  }

                  if(player instanceof EntityPlayerMP) {
                     ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                  }

                  this.showCurrentKnownEntities(player);
                  return true;
               }
               break;
            case 2:
               UUID otherCreature = Witchery.Items.TAGLOCK_KIT.getBoundCreatureID(stack, Integer.valueOf(1));
               String creatureName = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(stack, Integer.valueOf(1));
               if(!otherCreature.equals(this.getUniqueID())) {
                  if(this.isGroupableCreature(otherCreature, creatureName)) {
                     if(!player.isSneaking() && !this.knownCreatureTypes.contains(creatureName)) {
                        this.knownCreatureTypes.add(creatureName);
                     } else {
                        if(!player.isSneaking() || !this.knownCreatureTypes.contains(creatureName)) {
                           this.showCurrentKnownEntities(player);
                           return super.interact(player);
                        }

                        this.knownCreatureTypes.remove(creatureName);
                     }
                  } else {
                     EntityTreefyd.CreatureID creatureID = new EntityTreefyd.CreatureID(otherCreature, creatureName);
                     if(!player.isSneaking() && !this.knownCreatures.contains(creatureID)) {
                        this.knownCreatures.add(creatureID);
                     } else {
                        if(!player.isSneaking() || !this.knownCreatures.contains(creatureID)) {
                           this.showCurrentKnownEntities(player);
                           return super.interact(player);
                        }

                        this.knownCreatures.remove(creatureID);
                     }
                  }

                  --stack.stackSize;
                  if(stack.stackSize <= 0) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                  }

                  if(player instanceof EntityPlayerMP) {
                     ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                  }
               }

               this.showCurrentKnownEntities(player);
               return true;
            case 3:
            }
         } else if(stack != null && Witchery.Items.GENERIC.itemSeedsTreefyd.isMatch(stack)) {
            if(!super.worldObj.isRemote) {
               EntityTreefyd entity = new EntityTreefyd(super.worldObj);
               entity.setLocationAndAngles(0.5D + super.posX, super.posY, 0.5D + super.posZ, 0.0F, 0.0F);
               entity.onSpawnWithEgg((IEntityLivingData)null);
               entity.setOwner(player.getCommandSenderName());
               entity.func_110163_bv();
               entity.knownPlayers = (ArrayList)this.knownPlayers.clone();
               entity.knownCreatureTypes = (ArrayList)this.knownCreatureTypes.clone();
               entity.knownCreatures = (ArrayList)this.knownCreatures.clone();
               super.worldObj.spawnEntityInWorld(entity);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SILVERFISH_KILL, this, 1.0D, 2.0D, 16);
               ParticleEffect.EXPLODE.send(SoundEffect.NONE, this, 1.0D, 2.0D, 16);
            }

            if(!player.capabilities.isCreativeMode) {
               --stack.stackSize;
            }
         } else if(stack != null && Witchery.Items.GENERIC.itemCreeperHeart.isMatch(stack)) {
            if(!super.worldObj.isRemote) {
               this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
               this.setHealth(this.getMaxHealth());
               this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
               ParticleEffect.SLIME.send(SoundEffect.MOB_SILVERFISH_KILL, this, 0.5D, 2.0D, 16);
            }

            if(!player.capabilities.isCreativeMode) {
               --stack.stackSize;
            }
         } else if(stack != null && Witchery.Items.GENERIC.itemDemonHeart.isMatch(stack)) {
            if(!super.worldObj.isRemote) {
               this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(150.0D);
               this.setHealth(this.getMaxHealth());
               this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
               ParticleEffect.FLAME.send(SoundEffect.MOB_ENDERDRAGON_GROWL, this, 0.5D, 2.0D, 16);
            }

            if(!player.capabilities.isCreativeMode) {
               --stack.stackSize;
            }
         } else if(stack != null && stack.getItem() == Witchery.Items.BOLINE && !super.worldObj.isRemote) {
            this.setSentinal(!this.isSentinal());
         }

         this.showCurrentKnownEntities(player);
      }

      return super.interact(player);
   }

   private boolean isGroupableCreature(UUID otherCreature, String creatureName) {
      if(groupables == null) {
         groupables = new ArrayList();
         this.addGroupableType(EntityVillager.class);
         this.addGroupableType(EntityGoblin.class);
         this.addGroupableType(EntitySheep.class);
         this.addGroupableType(EntityCow.class);
         this.addGroupableType(EntityMooshroom.class);
         this.addGroupableType(EntityChicken.class);
         this.addGroupableType(EntityPig.class);
         this.addGroupableType(EntityHorse.class);
      }

      return groupables.contains(creatureName);
   }

   private void addGroupableType(Class className) {
      String name = (String)EntityList.classToStringMapping.get(className);
      if(name != null) {
         String localName = StatCollector.translateToLocal("entity." + name + ".name");
         groupables.add(localName);
      }

   }

   private void showCurrentKnownEntities(EntityPlayer player) {
      StringBuffer sb = new StringBuffer();
      String ownerName = this.getOwnerName();
      if(ownerName != null && !ownerName.isEmpty()) {
         sb.append(this.getOwnerName());
      }

      Iterator message;
      String cid;
      for(message = this.knownPlayers.iterator(); message.hasNext(); sb.append(cid)) {
         cid = (String)message.next();
         if(sb.length() > 0) {
            sb.append(", ");
         }
      }

      message = this.knownCreatureTypes.iterator();

      while(message.hasNext()) {
         cid = (String)message.next();
         if(sb.length() > 0) {
            sb.append(", ");
         }

         sb.append("#");
         sb.append(cid);
      }

      EntityTreefyd.CreatureID cid1;
      for(message = this.knownCreatures.iterator(); message.hasNext(); sb.append(cid1.toString())) {
         cid1 = (EntityTreefyd.CreatureID)message.next();
         if(sb.length() > 0) {
            sb.append(", ");
         }
      }

      String message1 = this.getCommandSenderName() + " (" + sb.toString() + ")";
      ChatUtil.sendPlain(player, message1);
   }

   private boolean isFamiliar(Entity entity) {
      if(entity instanceof IFamiliar) {
         IFamiliar familiar = (IFamiliar)entity;
         return familiar.isFamiliar();
      } else {
         return false;
      }
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.treefyd.name");
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
   }

   public boolean isAIEnabled() {
      return true;
   }

   public boolean attackEntityAsMob(Entity entity) {
      if(!super.worldObj.isRemote && entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         if(!player.isPotionActive(Potion.blindness)) {
            player.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
         }
      }

      return super.attackEntityAsMob(entity);
   }

   public int getMaxSafePointTries() {
      return this.getAttackTarget() == null?3:3 + (int)(this.getHealth() - 1.0F);
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(17, "");
      super.dataWatcher.addObject(18, Integer.valueOf(Integer.valueOf(0).intValue()));
   }

   public boolean isSentinal() {
      return super.dataWatcher.getWatchableObjectInt(18) == 1;
   }

   protected void setSentinal(boolean screaming) {
      super.dataWatcher.updateObject(18, Integer.valueOf(Integer.valueOf(screaming?1:0).intValue()));
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      if(this.getOwnerName() == null) {
         nbtRoot.setString("Owner", "");
      } else {
         nbtRoot.setString("Owner", this.getOwnerName());
      }

      NBTTagList nbtCreatures;
      Iterator i$;
      String creatureID;
      NBTTagCompound nbtKnownCreature;
      if(this.knownPlayers.size() > 0) {
         nbtCreatures = new NBTTagList();
         i$ = this.knownPlayers.iterator();

         while(i$.hasNext()) {
            creatureID = (String)i$.next();
            nbtKnownCreature = new NBTTagCompound();
            nbtKnownCreature.setString("PlayerName", creatureID);
            nbtCreatures.appendTag(nbtKnownCreature);
         }

         nbtRoot.setTag("KnownPlayers", nbtCreatures);
      }

      if(this.knownCreatureTypes.size() > 0) {
         nbtCreatures = new NBTTagList();
         i$ = this.knownCreatureTypes.iterator();

         while(i$.hasNext()) {
            creatureID = (String)i$.next();
            nbtKnownCreature = new NBTTagCompound();
            nbtKnownCreature.setString("CreatureTypeName", creatureID);
            nbtCreatures.appendTag(nbtKnownCreature);
         }

         nbtRoot.setTag("KnownCreatureTypes", nbtCreatures);
      }

      if(this.knownCreatures.size() > 0) {
         nbtCreatures = new NBTTagList();
         i$ = this.knownCreatures.iterator();

         while(i$.hasNext()) {
            EntityTreefyd.CreatureID creatureID1 = (EntityTreefyd.CreatureID)i$.next();
            nbtKnownCreature = new NBTTagCompound();
            nbtKnownCreature.setLong("CreatureMost", creatureID1.id.getMostSignificantBits());
            nbtKnownCreature.setLong("CreatureLeast", creatureID1.id.getLeastSignificantBits());
            nbtKnownCreature.setString("CreatureName", creatureID1.name);
            nbtCreatures.appendTag(nbtKnownCreature);
         }

         nbtRoot.setTag("KnownCreatures", nbtCreatures);
      }

      nbtRoot.setBoolean("SentinalPlant", this.isSentinal());
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      String s = nbtRoot.getString("Owner");
      if(s.length() > 0) {
         this.setOwner(s);
      }

      NBTTagList nbtCreatures;
      int i;
      NBTTagCompound nbtKnownCreature;
      String playerName;
      if(nbtRoot.hasKey("KnownPlayers")) {
         nbtCreatures = nbtRoot.getTagList("KnownPlayers", 10);
         this.knownPlayers = new ArrayList();

         for(i = 0; i < nbtCreatures.tagCount(); ++i) {
            nbtKnownCreature = nbtCreatures.getCompoundTagAt(i);
            playerName = nbtKnownCreature.getString("PlayerName");
            if(playerName != null && !playerName.isEmpty()) {
               this.knownPlayers.add(playerName);
            }
         }
      }

      if(nbtRoot.hasKey("KnownCreatureTypes")) {
         nbtCreatures = nbtRoot.getTagList("KnownCreatureTypes", 10);
         this.knownCreatureTypes = new ArrayList();

         for(i = 0; i < nbtCreatures.tagCount(); ++i) {
            nbtKnownCreature = nbtCreatures.getCompoundTagAt(i);
            playerName = nbtKnownCreature.getString("CreatureTypeName");
            if(playerName != null && !playerName.isEmpty()) {
               this.knownCreatureTypes.add(playerName);
            }
         }
      }

      if(nbtRoot.hasKey("KnownCreatures")) {
         nbtCreatures = nbtRoot.getTagList("KnownCreatures", 10);
         this.knownCreatures = new ArrayList();

         for(i = 0; i < nbtCreatures.tagCount(); ++i) {
            nbtKnownCreature = nbtCreatures.getCompoundTagAt(i);
            playerName = nbtKnownCreature.getString("PlayerName");
            long uuidMost = nbtKnownCreature.getLong("CreatureMost");
            long uuidLeast = nbtKnownCreature.getLong("CreatureLeast");
            String cname = nbtKnownCreature.getString("CreatureName");
            if(uuidMost != 0L || uuidLeast != 0L) {
               UUID creatureID = new UUID(uuidMost, uuidLeast);
               this.knownCreatures.add(new EntityTreefyd.CreatureID(creatureID, cname));
            }
         }
      }

      if(nbtRoot.hasKey("SentinalPlant")) {
         this.setSentinal(nbtRoot.getBoolean("SentinalPlant"));
      }

   }

   public String getOwnerName() {
      return super.dataWatcher.getWatchableObjectString(17);
   }

   public void setOwner(String par1Str) {
      this.func_110163_bv();
      super.dataWatcher.updateObject(17, par1Str);
   }

   public EntityPlayer getOwnerEntity() {
      return super.worldObj.getPlayerEntityByName(this.getOwnerName());
   }

   public void onUpdate() {
      super.onUpdate();
   }

   protected String getHurtSound() {
      return "mob.silverfish.hit";
   }

   protected String getLivingSound() {
      return "witchery:mob.treefyd.treefyd_say";
   }

   protected String getDeathSound() {
      return "mob.creeper.death";
   }

   protected Item getDropItem() {
      return Item.getItemFromBlock(Blocks.red_flower);
   }

   protected void dropRareDrop(int par1) {
      this.entityDropItem(Witchery.Items.GENERIC.itemSeedsTreefyd.createStack(), 0.0F);
   }

   protected boolean canDespawn() {
      return false;
   }


   // $FF: synthetic class
   static class NamelessClass2093483214 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$item$ItemTaglockKit$BoundType = new int[ItemTaglockKit.BoundType.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$item$ItemTaglockKit$BoundType[ItemTaglockKit.BoundType.PLAYER.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$item$ItemTaglockKit$BoundType[ItemTaglockKit.BoundType.CREATURE.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$item$ItemTaglockKit$BoundType[ItemTaglockKit.BoundType.NONE.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   private static class CreatureID {

      UUID id;
      String name;


      public CreatureID(UUID id, String name) {
         this.id = id;
         this.name = name;
      }

      public boolean equals(Object obj) {
         return obj == null?false:(obj == this?true:(obj instanceof UUID?this.id.equals((UUID)obj):(obj.getClass() == this.getClass()?this.id.equals(((EntityTreefyd.CreatureID)obj).id):false)));
      }

      public String toString() {
         return this.name;
      }
   }
}
