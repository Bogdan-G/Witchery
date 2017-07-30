package com.emoniph.witchery.integration;

import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Log;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class ModHook {

   protected boolean initialized = false;


   public abstract String getModID();

   public void init() {
      this.initialized = Config.instance().allowModIntegration && Loader.isModLoaded(this.getModID());
      if(this.initialized) {
         this.doInit();
         Log.instance().debug(String.format("Mod: %s support initialized", new Object[]{this.getModID()}));
      } else {
         Log.instance().debug(String.format("Mod: %s not found", new Object[]{this.getModID()}));
      }

   }

   protected abstract void doInit();

   public void postInit() {
      if(this.initialized) {
         this.doPostInit();
         Log.instance().debug(String.format("Mod: %s support post initialized", new Object[]{this.getModID()}));
      }

   }

   protected abstract void doPostInit();

   public void reduceMagicPower(EntityLivingBase entity, float factor) {
      if(this.initialized) {
         this.doReduceMagicPower(entity, factor);
      }

   }

   protected abstract void doReduceMagicPower(EntityLivingBase var1, float var2);

   public void boostBloodPowers(EntityPlayer player, float health) {}

   public boolean canVampireBeKilled(EntityPlayer player) {
      return false;
   }

   public void tryMakeItemModProof(ItemStack stack) {
      if(this.initialized) {
         this.makeItemModProof(stack);
      }

   }

   protected void makeItemModProof(ItemStack stack) {}
}
