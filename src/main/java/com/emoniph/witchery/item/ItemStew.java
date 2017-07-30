package com.emoniph.witchery.item;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.ItemUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemStew extends ItemFood {

   public ItemStew(int hunger, float saturation) {
      super(hunger, saturation, false);
   }

   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      return super.setUnlocalizedName(itemName);
   }

   public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
      super.onEaten(stack, world, player);
      ItemStack bowlStack = new ItemStack(Items.bowl);
      if(!player.inventory.addItemStackToInventory(bowlStack)) {
         world.spawnEntityInWorld(new EntityItem(world, player.posX + 0.5D, player.posY + 1.5D, player.posZ + 0.5D, bowlStack));
      } else if(player instanceof EntityPlayerMP) {
         ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
      }

      return stack;
   }
}
