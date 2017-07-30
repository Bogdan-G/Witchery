package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.item.ItemDeathsClothes;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class ItemDeathsHand extends ItemBase {

   public ItemDeathsHand() {
      this.setMaxStackSize(1);
      this.setFull3D();
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack stack) {
      return EnumRarity.epic;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips) {
      String localText = Witchery.resource(this.getUnlocalizedName() + ".tip");
      if(localText != null) {
         String[] arr$ = localText.split("\n");
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            if(!s.isEmpty()) {
               list.add(s);
            }
         }
      }

   }

   public void onUpdate(ItemStack stack, World world, Entity entity, int inventorySlot, boolean isHeldItem) {
      if(entity instanceof EntityPlayer && !world.isRemote) {
         EntityPlayer player = (EntityPlayer)entity;
         if(this.isDeployed(stack) && TimeUtil.secondsElapsed(1, world.getWorldTime())) {
            if(!ItemDeathsClothes.isFullSetWorn(player)) {
               this.setDeployed(player, stack, false);
            } else {
               int level = player.getFoodStats().getFoodLevel();
               if(level > 0) {
                  player.getFoodStats().addStats(level == 1?-1:-2, 0.0F);
               }
            }
         }
      }

   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity otherEntity) {
      if(!player.worldObj.isRemote && otherEntity instanceof EntityLivingBase) {
         EntityLivingBase victim = (EntityLivingBase)otherEntity;
         float MAX_DAMAGE = 15.0F;
         float DAMAGE_PERCENTAGE = 0.1F;
         boolean deployed = this.isDeployed(stack);
         float damagePct = 0.1F;
         float minDamage = 2.0F;
         byte hungerRestore = 0;
         byte healthRestore = 0;
         if(deployed) {
            int maxHealth = player.getFoodStats().getFoodLevel();
            if(maxHealth == 0) {
               damagePct = 0.5F;
               minDamage = 4.0F;
               hungerRestore = 10;
               healthRestore = 3;
            } else if(maxHealth <= 4) {
               damagePct = 0.25F;
               minDamage = 4.0F;
               hungerRestore = 3;
               healthRestore = 2;
            } else if(maxHealth <= 10) {
               damagePct = 0.2F;
               minDamage = 3.0F;
               hungerRestore = 2;
               healthRestore = 1;
            } else if(maxHealth <= 20) {
               damagePct = 0.15F;
               minDamage = 3.0F;
               hungerRestore = 1;
            } else {
               damagePct = 0.15F;
               minDamage = 3.0F;
            }
         }

         if(deployed) {
            double maxHealth2 = 1.5D;
            AxisAlignedBB flag = AxisAlignedBB.getBoundingBox(victim.posX - 1.5D, victim.boundingBox.minY, victim.posZ - 1.5D, victim.posX + 1.5D, victim.boundingBox.maxY, victim.posZ + 1.5D);
            List entities = player.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, flag);
            Iterator i$ = entities.iterator();

            while(i$.hasNext()) {
               Object obj = i$.next();
               EntityLivingBase hitEntity = (EntityLivingBase)obj;
               if(hitEntity != player) {
                  float maxHealth1 = Math.min(hitEntity.getMaxHealth(), 20.0F);
                  float damage1 = Math.min(Math.max(maxHealth1 * damagePct, minDamage), 15.0F);
                  boolean flag1 = EntityUtil.touchOfDeath(hitEntity, player, damage1);
                  if(flag1) {
                     if(hungerRestore > 0) {
                        player.getFoodStats().addStats(hungerRestore, 0.0F);
                     }

                     if(healthRestore > 0) {
                        player.heal((float)healthRestore);
                     }
                  }
               }
            }
         } else {
            float maxHealth3 = Math.min(victim.getMaxHealth(), 20.0F);
            float damage = Math.min(Math.max(maxHealth3 * damagePct, minDamage), 15.0F);
            EntityUtil.touchOfDeath(victim, player, damage);
         }
      }

      return true;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if(!world.isRemote && ItemDeathsClothes.isFullSetWorn(player)) {
         if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
         }

         NBTTagCompound nbtItem = stack.getTagCompound();
         boolean deployed = !this.isDeployed(nbtItem);
         this.setDeployed(player, stack, nbtItem, deployed);
         if(deployed) {
            ParticleEffect.MOB_SPELL.send(SoundEffect.MOB_ENDERDRAGON_GROWL, player, 1.0D, 2.0D, 16);
         }
      }

      return stack;
   }

   private void setDeployed(EntityPlayer player, ItemStack stack, boolean deployed) {
      this.setDeployed(player, stack, stack.getTagCompound(), deployed);
   }

   private void setDeployed(EntityPlayer player, ItemStack stack, NBTTagCompound nbtItem, boolean deployed) {
      if(player != null && !player.worldObj.isRemote && nbtItem != null) {
         nbtItem.setBoolean("WITCScytheDeployed", deployed);
         if(player instanceof EntityPlayerMP) {
            ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
         }
      }

   }

   public boolean isDeployed(EntityLivingBase player) {
      ItemStack heldItem = player.getHeldItem();
      return heldItem != null && heldItem.getItem() == this?this.isDeployed(heldItem):false;
   }

   private boolean isDeployed(ItemStack stack) {
      return this.isDeployed(stack.getTagCompound());
   }

   private boolean isDeployed(NBTTagCompound nbtItem) {
      boolean deployed = nbtItem != null && nbtItem.getBoolean("WITCScytheDeployed");
      return deployed;
   }
}
