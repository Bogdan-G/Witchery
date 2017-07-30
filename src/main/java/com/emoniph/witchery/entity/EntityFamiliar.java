package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.ai.EntityAIFamiliarFindDiamonds;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TameableUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class EntityFamiliar extends EntityOcelot {

   private int searches = 0;
   private static final int WATCH_KEY_OBJ_TO_FIND = 23;
   private static final Item[] ITEMS = new Item[]{Items.diamond, Items.emerald, Items.gold_ingot, Items.iron_ingot, Items.redstone, Items.dye, Items.coal};
   private static final Block[] ORES = new Block[]{Blocks.diamond_ore, Blocks.emerald_ore, Blocks.gold_ore, Blocks.iron_ore, Blocks.redstone_ore, Blocks.lapis_ore, Blocks.coal_ore};
   private static final Integer[] ORE_DEPTH = new Integer[]{Integer.valueOf(14), Integer.valueOf(14), Integer.valueOf(30), Integer.valueOf(64), Integer.valueOf(16), Integer.valueOf(30), Integer.valueOf(64)};


   public EntityFamiliar(World world) {
      super(world);
      this.setSize(0.8F, 0.8F);
      this.getNavigator().setAvoidsWater(true);
      super.tasks.taskEntries.clear();
      super.tasks.addTask(1, new EntityAISwimming(this));
      super.tasks.addTask(2, super.aiSit);
      super.tasks.addTask(3, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
      super.tasks.addTask(4, new EntityAIFamiliarFindDiamonds(this, 1.33D));
      super.tasks.addTask(5, new EntityAILeapAtTarget(this, 0.3F));
      super.tasks.addTask(6, new EntityAIOcelotAttack(this));
      super.tasks.addTask(7, new EntityAIWander(this, 0.8D));
      super.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
   }

   protected void entityInit() {
      super.entityInit();
      this.setTameSkin(1);
      super.dataWatcher.addObject(23, Integer.valueOf(-1));
   }

   public String getCommandSenderName() {
      return this.hasCustomNameTag()?this.getCustomNameTag():StatCollector.translateToLocal("entity.witchery.familiar.name");
   }

   public void setItemIDToFind(int itemID) {
      super.dataWatcher.updateObject(23, Integer.valueOf(itemID));
   }

   public int getItemIDToFind() {
      return super.dataWatcher.getWatchableObjectInt(23);
   }

   public void writeEntityToNBT(NBTTagCompound nbtTag) {
      super.writeEntityToNBT(nbtTag);
      nbtTag.setInteger("ItemToFind", this.getItemIDToFind());
      nbtTag.setInteger("Searches", this.searches);
   }

   public void readEntityFromNBT(NBTTagCompound nbtTag) {
      super.readEntityFromNBT(nbtTag);
      this.setItemIDToFind(nbtTag.getInteger("ItemToFind"));
      this.searches = nbtTag.getInteger("Searches");
   }

   @SideOnly(Side.CLIENT)
   public float getShadowSize() {
      super.getShadowSize();
      return 0.0F;
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), 0.5F);
   }

   public boolean interact(EntityPlayer player) {
      if(this.isTamed() && TameableUtil.isOwner(this, player) && !super.worldObj.isRemote) {
         ItemStack item = player.getCurrentEquippedItem();
         int itemToFind = this.hasOre(item);
         if(itemToFind != -1) {
            this.setItemIDToFind(itemToFind);
            ++this.searches;
            --item.stackSize;
            if(item.stackSize <= 0) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
            }

            double[] probs = new double[]{0.0D, 0.6D, 0.75D, 0.85D, 0.95D};
            double chance = super.worldObj.rand.nextDouble();
            if(this.searches <= 5 && (this.searches <= 1 || chance >= probs[Math.max(this.searches - 1, 1)])) {
               SoundEffect.RANDOM_ORB.playAtPlayer(player.worldObj, player);
            } else {
               ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, this, 1.0D, 1.0D, 16);
               this.setDead();
            }
         } else {
            super.aiSit.setSitting(!this.isSitting());
         }
      }

      return true;
   }

   public Block getBlockIDToFind() {
      int idToFind = this.getItemIDToFind();
      return idToFind != -1?ORES[idToFind]:null;
   }

   public void clearItemToFind() {
      this.setItemIDToFind(-1);
   }

   public int getDepthToFind() {
      int idToFind = this.getItemIDToFind();
      return idToFind != -1?ORE_DEPTH[idToFind].intValue():1;
   }

   private int hasOre(ItemStack item) {
      return item == null?-1:Arrays.asList(ITEMS).indexOf(item.getItem());
   }

   protected String getLivingSound() {
      return "mob.pig.say";
   }

   protected String getHurtSound() {
      return "mob.pig.say";
   }

   protected String getDeathSound() {
      return "mob.pig.death";
   }

   protected void func_145780_a(int par1, int par2, int par3, Block par4) {
      this.playSound("mob.pig.step", 0.15F, 1.0F);
   }

   protected float getSoundVolume() {
      return 0.4F;
   }

   protected void dropFewItems(boolean par1, int par2) {
      this.entityDropItem(Witchery.Items.GENERIC.itemSpectralDust.createStack(), 0.0F);
   }

   public boolean isBreedingItem(ItemStack par1ItemStack) {
      return false;
   }

   public boolean canMateWith(EntityAnimal par1EntityAnimal) {
      return false;
   }

   public EntityOcelot createChild(EntityAgeable par1EntityAgeable) {
      return null;
   }

   public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {
      this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", super.rand.nextGaussian() * 0.05D, 1));
      return data;
   }

   public void setAISitting(boolean b) {
      this.setSitting(true);
      super.aiSit.setSitting(true);
   }

}
