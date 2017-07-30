package com.emoniph.witchery.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class DemonicDamageSource extends DamageSource {

   protected Entity damageSourceEntity;


   public DemonicDamageSource(Entity par2Entity) {
      super("magic");
      this.damageSourceEntity = par2Entity;
      this.setDamageBypassesArmor();
      this.setMagicDamage();
      this.setDamageIsAbsolute();
   }

   public Entity getEntity() {
      return this.damageSourceEntity;
   }

   public IChatComponent func_151519_b(EntityLivingBase p_151519_1_) {
      ItemStack itemstack = this.damageSourceEntity instanceof EntityLivingBase?((EntityLivingBase)this.damageSourceEntity).getHeldItem():null;
      String s = "death.attack." + super.damageType;
      String s1 = s + ".item";
      return itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s1)?new ChatComponentTranslation(s1, new Object[]{p_151519_1_.func_145748_c_(), this.damageSourceEntity.func_145748_c_(), itemstack.func_151000_E()}):new ChatComponentTranslation(s, new Object[]{p_151519_1_.func_145748_c_(), this.damageSourceEntity.func_145748_c_()});
   }

   public boolean isDifficultyScaled() {
      return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof EntityPlayer);
   }
}
