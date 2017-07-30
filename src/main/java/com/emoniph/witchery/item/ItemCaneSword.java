package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.util.ItemUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemCaneSword extends ItemSword {

   public ItemCaneSword() {
      super(ToolMaterial.EMERALD);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      return super.setUnlocalizedName(itemName);
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack stack) {
      return EnumRarity.rare;
   }

   public int getItemEnchantability() {
      return ToolMaterial.IRON.getEnchantability();
   }

   public Multimap getAttributeModifiers(ItemStack stack) {
      HashMultimap multimap = HashMultimap.create();
      float damage = this.isDrawn(stack)?4.0F + this.func_150931_i():1.0F;
      multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(Item.field_111210_e, "Weapon modifier", (double)damage, 0));
      return multimap;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      NBTTagCompound nbtItem = stack.getTagCompound();
      boolean deployed = this.isDrawn(nbtItem);
      if(player.isSneaking()) {
         if(!world.isRemote) {
            this.setDrawn(player, stack, nbtItem, !deployed);
            if(deployed) {
               SoundEffect.WITCHERY_RANDOM_SWORD_DRAW.playAtPlayer(world, player, 1.0F, 1.0F);
            } else {
               SoundEffect.WITCHERY_RANDOM_SWORD_SHEATHE.playAtPlayer(world, player, 1.0F, 1.0F);
            }
         }
      } else if(deployed) {
         super.onItemRightClick(stack, world, player);
      } else {
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if(playerEx.isVampire() && playerEx.isBloodReserveReady() && playerEx.getBloodPower() < playerEx.getMaxBloodPower()) {
            ParticleEffect.REDDUST.send(SoundEffect.WITCHERY_RANDOM_DRINK, world, player.posX, player.posY + (double)player.height * 0.85D, player.posZ, 0.5D, 0.5D, 16);
            playerEx.useBloodReserve();
         } else {
            SoundEffect.NOTE_SNARE.playOnlyTo(player);
         }
      }

      return stack;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean moreTips) {
      String localText = String.format(Witchery.resource(this.getUnlocalizedName() + ".tip"), new Object[]{Integer.valueOf(ExtendedPlayer.get(player).getBloodReserve())});
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

   public boolean isDrawn(EntityLivingBase player) {
      ItemStack heldItem = player.getHeldItem();
      return heldItem != null && heldItem.getItem() == this?this.isDrawn(heldItem):false;
   }

   private boolean isDrawn(ItemStack stack) {
      return this.isDrawn(stack.getTagCompound());
   }

   private boolean isDrawn(NBTTagCompound nbtItem) {
      boolean deployed = nbtItem != null && nbtItem.getBoolean("WITCBladeDeployed");
      return deployed;
   }

   private void setDrawn(EntityPlayer player, ItemStack stack, boolean deployed) {
      this.setDrawn(player, stack, stack.getTagCompound(), deployed);
   }

   private void setDrawn(EntityPlayer player, ItemStack stack, NBTTagCompound nbtItem, boolean deployed) {
      if(player != null && !player.worldObj.isRemote && nbtItem != null) {
         nbtItem.setBoolean("WITCBladeDeployed", deployed);
         if(player instanceof EntityPlayerMP) {
            ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
         }
      }

   }
}
