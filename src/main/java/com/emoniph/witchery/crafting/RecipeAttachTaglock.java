package com.emoniph.witchery.crafting;

import com.emoniph.witchery.Witchery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeAttachTaglock implements IRecipe {

   final ItemStack prototype;
   final ItemStack[] pattern;


   public RecipeAttachTaglock(ItemStack resultPoppet, ItemStack ... pattern) {
      this.prototype = resultPoppet;
      this.pattern = pattern;
   }

   public ItemStack getRecipeOutput() {
      return null;
   }

   public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World) {
      ArrayList arraylist = new ArrayList(Arrays.asList(this.pattern));

      for(int i = 0; i < 3; ++i) {
         for(int j = 0; j < 3; ++j) {
            ItemStack itemstack = par1InventoryCrafting.getStackInRowAndColumn(j, i);
            if(itemstack != null) {
               boolean flag = false;
               Iterator iterator = arraylist.iterator();

               while(iterator.hasNext()) {
                  ItemStack itemstack1 = (ItemStack)iterator.next();
                  if(itemstack.getItem() == itemstack1.getItem() && (itemstack1.getItemDamage() == 32767 || itemstack.getItemDamage() == itemstack1.getItemDamage())) {
                     flag = true;
                     arraylist.remove(itemstack1);
                     break;
                  }
               }

               if(!flag) {
                  return false;
               }
            }
         }
      }

      return arraylist.isEmpty();
   }

   public ItemStack getCraftingResult(InventoryCrafting inv) {
      ItemStack newPoppet = this.prototype.copy();
      int i = 0;

      for(int index = 1; i < inv.getSizeInventory(); ++i) {
         ItemStack stack = inv.getStackInSlot(i);
         if(stack != null && stack.getItem() == Witchery.Items.TAGLOCK_KIT) {
            Witchery.Items.TAGLOCK_KIT.addTagLockToPoppet(stack, newPoppet, Integer.valueOf(index++));
         }
      }

      return newPoppet;
   }

   private ItemStack findRecipeItemStack(InventoryCrafting inv, Item itemToFind) {
      for(int i = 0; i < inv.getSizeInventory(); ++i) {
         ItemStack stack = inv.getStackInSlot(i);
         if(stack != null && stack.getItem() == itemToFind) {
            return stack;
         }
      }

      return null;
   }

   public int getRecipeSize() {
      return this.pattern.length;
   }
}
