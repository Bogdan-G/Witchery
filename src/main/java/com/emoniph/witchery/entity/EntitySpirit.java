package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityFlyingTameable;
import com.emoniph.witchery.entity.ai.EntityAIFlyerFlyToWaypoint;
import com.emoniph.witchery.entity.ai.EntityAIFlyerFollowOwner;
import com.emoniph.witchery.entity.ai.EntityAIFlyerLand;
import com.emoniph.witchery.entity.ai.EntityAIFlyerWander;
import com.emoniph.witchery.entity.ai.EntityAIFlyingTempt;
import com.emoniph.witchery.entity.ai.EntityAISitAndStay;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;

public class EntitySpirit extends EntityFlyingTameable {

   public EntityAIFlyingTempt aiTempt;
   private int timeToLive = -1;
   private int spiritType = 0;
   private static final ItemStack[] TEMPTATIONS = new ItemStack[]{Witchery.Items.GENERIC.itemFocusedWill.createStack()};
   private static Field fieldStructureGenerators;
   private static Field fieldVillageGenerator;


   public EntitySpirit(World world) {
      super(world);
      this.setSize(0.25F, 0.25F);
      this.getNavigator().setCanSwim(true);
      super.tasks.addTask(1, new EntityAISitAndStay(this));
      super.tasks.addTask(3, this.aiTempt = new EntityAIFlyingTempt(this, 0.6D, TEMPTATIONS, true));
      super.tasks.addTask(5, new EntityAIFlyerFollowOwner(this, 1.0D, 14.0F, 5.0F));
      super.tasks.addTask(8, new EntityAIFlyerFlyToWaypoint(this, EntityAIFlyerFlyToWaypoint.CarryRequirement.NONE));
      super.tasks.addTask(9, new EntityAIFlyerLand(this, 0.8D, true));
      super.tasks.addTask(10, new EntityAIFlyerWander(this, 0.8D, 10.0D));
      super.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F, 0.2F));
   }

   protected boolean canDespawn() {
      return true;
   }

   public void setTarget(String target, int type) {
      this.timeToLive = TimeUtil.secsToTicks(10);
      this.spiritType = type;

      try {
         if(target.equals("Village")) {
            IChunkProvider ex;
            for(ex = super.worldObj.getChunkProvider(); ex != null && ex instanceof ChunkProviderServer; ex = ((ChunkProviderServer)ex).currentChunkProvider) {
               ;
            }

            if(ex != null) {
               if(ex instanceof ChunkProviderFlat) {
                  if(fieldStructureGenerators == null) {
                     fieldStructureGenerators = ReflectionHelper.findField(ChunkProviderFlat.class, new String[]{"structureGenerators", "field_82696_f", "f"});
                  }

                  Iterator iterator = ((List)fieldStructureGenerators.get((ChunkProviderFlat)ex)).iterator();

                  while(iterator.hasNext()) {
                     if(this.setWaypointTo(iterator.next(), MapGenVillage.class)) {
                        return;
                     }
                  }
               } else if(ex instanceof ChunkProviderGenerate) {
                  if(fieldVillageGenerator == null) {
                     fieldVillageGenerator = ReflectionHelper.findField(ChunkProviderGenerate.class, new String[]{"villageGenerator", "field_73224_v", "v"});
                  }

                  if(fieldVillageGenerator != null) {
                     this.setWaypointTo(fieldVillageGenerator.get((ChunkProviderGenerate)ex), MapGenVillage.class);
                  }
               } else if(ex instanceof ChunkProviderHell) {
                  this.setWaypointTo(((ChunkProviderHell)ex).genNetherBridge);
               }
            }
         }
      } catch (IllegalAccessException var5) {
         ;
      }

   }

   private boolean setWaypointTo(Object objStructure, Class clazz) {
      if(objStructure != null && clazz.isAssignableFrom(objStructure.getClass())) {
         this.setWaypointTo((MapGenStructure)objStructure);
         return true;
      } else {
         return false;
      }
   }

   private void setWaypointTo(MapGenStructure mapStructure) {
      if(mapStructure != null) {
         ChunkPosition pos = mapStructure.func_151545_a(super.worldObj, (int)super.posX, (int)super.posY, (int)super.posZ);
         if(pos != null) {
            super.homeX = (double)pos.chunkPosX;
            super.homeY = (double)pos.chunkPosY;
            super.homeZ = (double)pos.chunkPosZ;
            super.waypoint = Witchery.Items.GENERIC.itemWaystone.createStack();
         }
      }

   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setInteger("SuicideIn", this.timeToLive);
      nbtRoot.setInteger("SpiritType", this.spiritType);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      if(nbtRoot.hasKey("SuicideIn")) {
         this.timeToLive = nbtRoot.getInteger("SuicideIn");
      } else {
         this.timeToLive = -1;
      }

      if(nbtRoot.hasKey("SpiritType")) {
         this.spiritType = nbtRoot.getInteger("SpiritType");
      } else {
         this.spiritType = 0;
      }

   }

   protected void updateAITick() {
      this.getNavigator().clearPathEntity();
      super.updateAITick();
      if(super.worldObj != null && !super.isDead && !super.worldObj.isRemote && this.timeToLive != -1 && --this.timeToLive == 0) {
         ParticleEffect.EXPLODE.send(SoundEffect.NONE, this, 1.0D, 1.0D, 16);
         this.setDead();
         if(!super.worldObj.isRemote) {
            this.dropFewItems(false, 0);
         }
      }

   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(21, Integer.valueOf(0));
   }

   protected int decreaseAirSupply(int par1) {
      return par1;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
      this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
      this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
   }

   public boolean isAIEnabled() {
      return true;
   }

   protected void dropFewItems(boolean par1, int par2) {
      if(this.spiritType != 2) {
         ItemStack stack;
         if(this.spiritType == 1) {
            stack = Witchery.Items.GENERIC.itemSubduedSpiritVillage.createStack();
         } else {
            stack = Witchery.Items.GENERIC.itemSubduedSpirit.createStack();
         }

         this.entityDropItem(stack, 0.0F);
      }
   }

   public void onLivingUpdate() {
      super.onLivingUpdate();
   }

   public void onUpdate() {
      super.onUpdate();
      if(super.worldObj.isRemote) {
         int color = this.getFeatherColor();
         float red = 1.0F;
         float green = 0.8F;
         float blue = 0.0F;
         if(color > 0) {
            red = (float)(color >> 16 & 255) / 255.0F;
            green = (float)(color >> 8 & 255) / 255.0F;
            blue = (float)(color & 255) / 255.0F;
         }

         Witchery.proxy.generateParticle(super.worldObj, super.posX - (double)super.width * 0.5D + super.worldObj.rand.nextDouble() * (double)super.width, 0.1D + super.posY + super.worldObj.rand.nextDouble() * 0.2D, super.posZ - (double)super.width * 0.5D + super.worldObj.rand.nextDouble() * (double)super.width, red, green, blue, 10, -0.1F);
      }

   }

   public int getTalkInterval() {
      return super.getTalkInterval() * 2;
   }

   protected String getLivingSound() {
      return null;
   }

   protected String getHurtSound() {
      return null;
   }

   protected String getDeathSound() {
      return null;
   }

   public boolean interact(EntityPlayer par1EntityPlayer) {
      return false;
   }

   public EntitySpirit spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
      return null;
   }

   public boolean isBreedingItem(ItemStack itemstack) {
      return itemstack != null && itemstack.getItem() == Items.bone;
   }

   public boolean canMateWith(EntityAnimal par1EntityAnimal) {
      return false;
   }

   public int getFeatherColor() {
      return super.dataWatcher.getWatchableObjectInt(21);
   }

   public void setFeatherColor(int par1) {
      super.dataWatcher.updateObject(21, Integer.valueOf(par1));
   }

   public boolean getCanSpawnHere() {
      if(super.worldObj.provider.dimensionId != Config.instance().dimensionDreamID) {
         return false;
      } else {
         boolean superGetCanSpawnHere = super.worldObj.checkNoEntityCollision(super.boundingBox) && super.worldObj.getCollidingBoundingBoxes(this, super.boundingBox).isEmpty() && !super.worldObj.isAnyLiquid(super.boundingBox);
         int i = MathHelper.floor_double(super.posX);
         int j = MathHelper.floor_double(super.boundingBox.minY);
         int k = MathHelper.floor_double(super.posZ);
         superGetCanSpawnHere = superGetCanSpawnHere && this.getBlockPathWeight(i, j, k) >= 0.0F && j >= 60;
         Block blockID = super.worldObj.getBlock(i, j - 1, k);
         return superGetCanSpawnHere && super.worldObj.rand.nextInt(10) == 0 && (blockID == Blocks.grass || blockID == Blocks.sand) && super.worldObj.getFullBlockLightValue(i, j, k) > 8;
      }
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.spirit.name");
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData par1EntityLivingData) {
      par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);
      return par1EntityLivingData;
   }

   public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
      return null;
   }

}
