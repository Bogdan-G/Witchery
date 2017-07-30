package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntitySummonedUndead extends EntityMob {

   private int timeToLive = -1;


   public EntitySummonedUndead(World world) {
      super(world);
   }

   protected void entityInit() {
      super.entityInit();
      super.dataWatcher.addObject(17, "");
      super.dataWatcher.addObject(18, Integer.valueOf(Integer.valueOf(0).intValue()));
      super.dataWatcher.addObject(19, Integer.valueOf(Integer.valueOf(0).intValue()));
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      if(this.getSummonerName() == null) {
         nbtRoot.setString("Summoner", "");
      } else {
         nbtRoot.setString("Summoner", this.getSummonerName());
      }

      nbtRoot.setBoolean("Obscured", this.isObscured());
      nbtRoot.setInteger("SuicideIn", this.timeToLive);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      String s = nbtRoot.getString("Summoner");
      if(s.length() > 0) {
         this.setSummoner(s);
      }

      this.setObscured(nbtRoot.getBoolean("Obscured"));
      if(nbtRoot.hasKey("SuicideIn")) {
         this.timeToLive = nbtRoot.getInteger("SuicideIn");
      } else {
         this.timeToLive = -1;
      }

   }

   public void setTimeToLive(int i) {
      this.timeToLive = i;
   }

   public boolean isTemp() {
      return this.timeToLive != -1;
   }

   protected int decreaseAirSupply(int par1) {
      return par1;
   }

   public String getSummonerName() {
      return super.dataWatcher.getWatchableObjectString(17);
   }

   public void setSummoner(String par1Str) {
      this.func_110163_bv();
      super.dataWatcher.updateObject(17, par1Str);
   }

   public EntityPlayer getSummoner() {
      return super.worldObj.getPlayerEntityByName(this.getSummonerName());
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   protected void dropFewItems(boolean par1, int par2) {
      if(!this.isTemp()) {
         int chance = super.rand.nextInt(Math.max(4 - par2, 2));
         int quantity = chance == 0?1:0;
         if(quantity > 0) {
            this.entityDropItem(Witchery.Items.GENERIC.itemSpectralDust.createStack(quantity), 0.0F);
         }
      }

   }

   protected void updateAITick() {
      super.updateAITick();
      if(super.worldObj != null && !super.isDead && !super.worldObj.isRemote && this.timeToLive != -1 && (--this.timeToLive == 0 || this.getAttackTarget() == null || this.getAttackTarget().isDead)) {
         ParticleEffect.EXPLODE.send(SoundEffect.NONE, this, 1.0D, 1.0D, 16);
         this.setDead();
      }

   }

   public int getTalkInterval() {
      return super.getTalkInterval() * 3;
   }

   public boolean isScreaming() {
      return super.dataWatcher.getWatchableObjectInt(18) == 1;
   }

   protected void setScreaming(boolean screaming) {
      super.dataWatcher.updateObject(18, Integer.valueOf(Integer.valueOf(screaming?1:0).intValue()));
   }

   public boolean isObscured() {
      return super.dataWatcher.getWatchableObjectInt(19) == 1;
   }

   public void setObscured(boolean obscured) {
      super.dataWatcher.updateObject(19, Integer.valueOf(Integer.valueOf(obscured?1:0).intValue()));
   }

   public boolean attackEntityFrom(DamageSource damageSource, float damage) {
      return super.attackEntityFrom(damageSource, Math.min(damage, 15.0F));
   }
}
