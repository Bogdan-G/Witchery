package com.emoniph.witchery.crafting;

import com.emoniph.witchery.Witchery;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeShapedPoppet implements IRecipe {

   final ItemStack prototype;
   final Item[] pattern;


   public RecipeShapedPoppet(ItemStack resultPoppet, Item[] pattern) {
      this.prototype = resultPoppet;
      this.pattern = pattern;
   }

   public boolean matches(InventoryCrafting inv, World world) {
      for(int i = 0; i < inv.getSizeInventory(); ++i) {
         ItemStack stack = inv.getStackInSlot(i);
         if(this.pattern[i] != null || stack != null) {
            if(stack == null || this.pattern[i] == null) {
               return false;
            }

            if(stack.getItem() != this.pattern[i]) {
               return false;
            }

            if(stack.getItem() == Witchery.Items.TAGLOCK_KIT && stack.getItemDamage() != 1) {
               return false;
            }
         }
      }

      return true;
   }

   public ItemStack getCraftingResult(InventoryCrafting inv) {
      ItemStack stackPoppet = this.prototype.copy();
      ItemStack stackTaglockKit = this.findTaglockKit(inv);
      if(stackTaglockKit != null) {
         Witchery.Items.TAGLOCK_KIT.addTagLockToPoppet(stackTaglockKit, stackPoppet, Integer.valueOf(1));
      }

      return stackPoppet;
   }

   private ItemStack findTaglockKit(InventoryCrafting inv) {
      for(int i = 0; i < inv.getSizeInventory(); ++i) {
         ItemStack stack = inv.getStackInSlot(1);
         if(stack.getItem() == Witchery.Items.TAGLOCK_KIT) {
            return stack;
         }
      }

      return null;
   }

   public int getRecipeSize() {
      return this.pattern.length;
   }

   public ItemStack getRecipeOutput() {
      return null;
   }
}
