package com.emoniph.witchery.item;

import com.emoniph.witchery.entity.EntityParasyticLouse;
import com.emoniph.witchery.item.ItemBase;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemParasyticLouse extends ItemBase {

   public ItemParasyticLouse() {
      this.setMaxStackSize(1);
      this.setHasSubtypes(true);
   }

   public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
      if(par3World.isRemote) {
         return true;
      } else {
         Block i1 = par3World.getBlock(par4, par5, par6);
         par4 += Facing.offsetsXForSide[par7];
         par5 += Facing.offsetsYForSide[par7];
         par6 += Facing.offsetsZForSide[par7];
         double d0 = 0.0D;
         if(par7 == 1 && i1.getRenderType() == 11) {
            d0 = 0.5D;
         }

         Entity entity = this.spawnCreature(par1ItemStack, par3World, (double)par4 + 0.5D, (double)par5 + d0, (double)par6 + 0.5D);
         if(entity != null) {
            if(entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName()) {
               ((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
            }

            if(!par2EntityPlayer.capabilities.isCreativeMode) {
               --par1ItemStack.stackSize;
            }
         }

         return true;
      }
   }

   public String getItemStackDisplayName(ItemStack stack) {
      return super.getItemStackDisplayName(stack);
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips) {
      List effects = Items.potionitem.getEffects(stack.getItemDamage());
      if(effects != null && !effects.isEmpty()) {
         PotionEffect effect = (PotionEffect)effects.get(0);
         String s1 = effect.getEffectName();
         s1 = s1 + ".postfix";
         String s2 = "§6" + StatCollector.translateToLocal(s1).trim() + "§r";
         if(effect.getAmplifier() > 0) {
            s2 = s2 + " " + StatCollector.translateToLocal("potion.potency." + effect.getAmplifier()).trim();
         }

         if(effect.getDuration() > 20) {
            s2 = s2 + " [" + Potion.getDurationString(effect) + "]";
         }

         list.add(s2);
      }

   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      if(par2World.isRemote) {
         return par1ItemStack;
      } else {
         MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);
         if(movingobjectposition == null) {
            return par1ItemStack;
         } else {
            if(movingobjectposition.typeOfHit == MovingObjectType.BLOCK) {
               int i = movingobjectposition.blockX;
               int j = movingobjectposition.blockY;
               int k = movingobjectposition.blockZ;
               if(!par2World.canMineBlock(par3EntityPlayer, i, j, k)) {
                  return par1ItemStack;
               }

               if(!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack)) {
                  return par1ItemStack;
               }

               if(par2World.getBlock(i, j, k).getMaterial() == Material.water) {
                  Entity entity = this.spawnCreature(par1ItemStack, par2World, (double)i, (double)j, (double)k);
                  if(entity != null) {
                     if(entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName()) {
                        ((EntityLiving)entity).setCustomNameTag(par1ItemStack.getDisplayName());
                     }

                     if(!par3EntityPlayer.capabilities.isCreativeMode) {
                        --par1ItemStack.stackSize;
                     }
                  }
               }
            }

            return par1ItemStack;
         }
      }
   }

   private Entity spawnCreature(ItemStack stack, World par0World, double par2, double par4, double par6) {
      EntityParasyticLouse entity = new EntityParasyticLouse(par0World);
      int damage = stack.getItemDamage();
      if(damage > 0) {
         entity.setBitePotionEffect(damage);
      }

      if(entity != null && entity instanceof EntityLivingBase) {
         entity.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
         entity.func_110163_bv();
         entity.rotationYawHead = entity.rotationYaw;
         entity.renderYawOffset = entity.rotationYaw;
         entity.onSpawnWithEgg((IEntityLivingData)null);
         par0World.spawnEntityInWorld(entity);
         entity.playLivingSound();
      }

      return entity;
   }
}
