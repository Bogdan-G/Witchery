package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemGeneral;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGeneralContract extends ItemGeneral.SubItem {

   public ItemGeneralContract(int damageValue, String unlocalizedName) {
      super(damageValue, unlocalizedName);
   }

   public static boolean isBoundContract(ItemStack stack) {
      if(stack.getItem() == Witchery.Items.GENERIC) {
         ItemGeneral.SubItem subItem = (ItemGeneral.SubItem)Witchery.Items.GENERIC.subItems.get(Math.max(stack.getItemDamage(), 0));
         return subItem instanceof ItemGeneralContract?Witchery.Items.TAGLOCK_KIT.isTaglockPresent(stack, Integer.valueOf(1)):false;
      } else {
         return false;
      }
   }

   public static EntityLivingBase getBoundEntity(World world, EntityPlayer player, ItemStack stack) {
      EntityLivingBase boundEntity = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, player, stack, Integer.valueOf(1));
      return boundEntity;
   }

   public static ItemGeneralContract getContract(ItemStack stack) {
      ItemGeneral.SubItem subItem = (ItemGeneral.SubItem)Witchery.Items.GENERIC.subItems.get(stack.getItemDamage());
      return subItem instanceof ItemGeneralContract?(ItemGeneralContract)subItem:null;
   }

   public boolean activate(ItemStack stack, EntityLivingBase targetEntity) {
      return false;
   }
}
