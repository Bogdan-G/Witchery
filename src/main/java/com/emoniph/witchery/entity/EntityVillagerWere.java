package com.emoniph.witchery.entity;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityWolfman;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityVillagerWere extends EntityVillager {

   private boolean infectious;


   public EntityVillagerWere(World world) {
      this(world, 0, false);
   }

   public EntityVillagerWere(World world, int profession, boolean infectious) {
      super(world, profession);
      this.infectious = infectious;
   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      nbtRoot.setBoolean("Infectious", this.infectious);
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      this.infectious = nbtRoot.getBoolean("Infectious");
   }

   protected void updateAITasks() {
      super.updateAITasks();
      if(!super.worldObj.isRemote && super.ticksExisted % 100 == 3 && !this.isChild() && CreatureUtil.isFullMoon(super.worldObj) && !this.isPotionActive(Witchery.Potions.WOLFSBANE)) {
         this.convertToWolfman();
      }

   }

   protected void convertToWolfman() {
      EntityWolfman entity = new EntityWolfman(super.worldObj);
      if(this.infectious) {
         entity.setInfectious();
      }

      entity.setFormerProfession(this.getProfession(), super.wealth, super.buyingList);
      entity.func_110163_bv();
      entity.copyLocationAndAnglesFrom(this);
      entity.onSpawnWithEgg((IEntityLivingData)null);
      super.worldObj.removeEntity(this);
      super.worldObj.spawnEntityInWorld(entity);
      super.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, (int)super.posX, (int)super.posY, (int)super.posZ, 0);
      SoundEffect.WITCHERY_MOB_WOLFMAN_HOWL.playAt(super.worldObj, super.posX, super.posY, super.posZ);
   }
}
