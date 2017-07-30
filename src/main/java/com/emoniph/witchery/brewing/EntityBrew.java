package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.EntityThrowableBase;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.util.EntityPosition;
import com.emoniph.witchery.util.EntityUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBrew extends EntityThrowableBase {

   private ItemStack brewStack;
   private int color;
   private boolean isSpell;


   public EntityBrew(World world) {
      super(world);
   }

   public EntityBrew(World world, EntityLivingBase thrower, ItemStack brewStack, boolean isSpell) {
      super(world, thrower, isSpell?0.0F:-20.0F);
      this.brewStack = brewStack;
      this.setIsSpell(isSpell);
      this.setColor(WitcheryBrewRegistry.INSTANCE.getBrewColor(brewStack.getTagCompound()));
   }

   public EntityBrew(World world, double x, double y, double z, ItemStack brewStack, boolean isSpell) {
      super(world, x, y, z, isSpell?0.0F:-20.0F);
      this.brewStack = brewStack;
      this.setIsSpell(isSpell);
      this.setColor(WitcheryBrewRegistry.INSTANCE.getBrewColor(brewStack.getTagCompound()));
   }

   protected void entityInit() {
      super.dataWatcher.addObject(6, Integer.valueOf(0));
      super.dataWatcher.addObject(12, Byte.valueOf((byte)0));
      super.entityInit();
   }

   protected void setColor(int color) {
      this.getDataWatcher().updateObject(6, Integer.valueOf(color));
   }

   public int getColor() {
      return this.getDataWatcher().getWatchableObjectInt(6);
   }

   protected void setIsSpell(boolean spell) {
      this.getDataWatcher().updateObject(12, Byte.valueOf((byte)(spell?1:0)));
   }

   public boolean getIsSpell() {
      return this.getDataWatcher().getWatchableObjectByte(12) == 1;
   }

   public ItemStack getBrew() {
      return this.brewStack;
   }

   protected float getGravityVelocity() {
      return this.getIsSpell()?0.0F:0.05F;
   }

   protected float func_70182_d() {
      return this.getIsSpell()?4.0F:0.75F;
   }

   protected float func_70183_g() {
      return this.getIsSpell()?0.0F:-20.0F;
   }

   protected void onImpact(MovingObjectPosition mop) {
      if(!super.worldObj.isRemote && mop != null && WitcheryBrewRegistry.INSTANCE.impactSplashPotion(super.worldObj, this.brewStack, mop, new ModifiersImpact(new EntityPosition(this), false, 0, EntityUtil.playerOrFake(super.worldObj, this.getThrower())))) {
         super.worldObj.playAuxSFX(2002, MathHelper.floor_double(super.posX), MathHelper.floor_double(super.posY), MathHelper.floor_double(super.posZ), this.getColor());
      }

      this.setDead();
   }

   public void readEntityFromNBT(NBTTagCompound nbtRoot) {
      super.readEntityFromNBT(nbtRoot);
      if(nbtRoot.hasKey("Brew", 10)) {
         this.brewStack = ItemStack.loadItemStackFromNBT(nbtRoot.getCompoundTag("Brew"));
         this.setColor(WitcheryBrewRegistry.INSTANCE.getBrewColor(this.brewStack.getTagCompound()));
         if(nbtRoot.hasKey("Spell")) {
            this.setIsSpell(nbtRoot.getBoolean("Spell"));
         }
      }

      if(this.brewStack == null) {
         this.setDead();
      }

   }

   public void writeEntityToNBT(NBTTagCompound nbtRoot) {
      super.writeEntityToNBT(nbtRoot);
      if(this.brewStack != null) {
         nbtRoot.setTag("Brew", this.brewStack.writeToNBT(new NBTTagCompound()));
         nbtRoot.setBoolean("Spell", this.getIsSpell());
      }

   }
}
