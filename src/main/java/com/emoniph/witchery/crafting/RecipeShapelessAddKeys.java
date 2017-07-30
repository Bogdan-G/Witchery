package com.emoniph.witchery.crafting;

import com.emoniph.witchery.Witchery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class RecipeShapelessAddKeys implements IRecipe {

   final ItemStack prototype;
   final ItemStack[] pattern;


   public RecipeShapelessAddKeys(ItemStack result, ItemStack ... pattern) {
      this.prototype = result;
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
                  if(itemstack.getItem() == itemstack1.getItem() && (!itemstack.getItem().getHasSubtypes() || itemstack.getItemDamage() == itemstack1.getItemDamage())) {
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
      ItemStack ring = this.findRecipeItemStack(inv, this.prototype.getItem(), this.prototype.getItemDamage());
      ItemStack result = ring != null?ring.copy():this.prototype.copy();

      for(int j = 0; j < inv.getSizeInventory(); ++j) {
         ItemStack key = inv.getStackInSlot(j);
         if(key != null && Witchery.Items.GENERIC.itemDoorKey.isMatch(key) && key.hasTagCompound()) {
            if(!result.hasTagCompound()) {
               result.setTagCompound(new NBTTagCompound());
            }

            NBTTagCompound sourceTag = key.getTagCompound();
            int sourceX = sourceTag.getInteger("doorX");
            int sourceY = sourceTag.getInteger("doorY");
            int sourceZ = sourceTag.getInteger("doorZ");
            boolean sourceHasD = sourceTag.hasKey("doorD") && sourceTag.hasKey("doorDN");
            NBTTagCompound nbtTag = result.getTagCompound();
            if(!nbtTag.hasKey("doorKeys")) {
               nbtTag.setTag("doorKeys", new NBTTagList());
            }

            NBTTagList keyList = nbtTag.getTagList("doorKeys", 10);

            for(int nbtNewKey = 0; nbtNewKey < keyList.tagCount(); ++nbtNewKey) {
               NBTTagCompound keyTag = keyList.getCompoundTagAt(nbtNewKey);
               if(keyTag != null && keyTag.hasKey("doorX") && keyTag.hasKey("doorY") && keyTag.hasKey("doorZ")) {
                  int doorX = keyTag.getInteger("doorX");
                  int doorY = keyTag.getInteger("doorY");
                  int doorZ = keyTag.getInteger("doorZ");
                  boolean doorHasD = keyTag.hasKey("doorD") && keyTag.hasKey("doorDN");
                  if(doorX == sourceX && doorY == sourceY && doorZ == sourceZ && sourceHasD == doorHasD && (!sourceHasD || sourceTag.getInteger("doorD") == keyTag.getInteger("doorD"))) {
                     return result;
                  }
               }
            }

            NBTTagCompound var19 = new NBTTagCompound();
            var19.setInteger("doorX", sourceX);
            var19.setInteger("doorY", sourceY);
            var19.setInteger("doorZ", sourceZ);
            if(sourceHasD) {
               var19.setInteger("doorD", sourceTag.getInteger("doorD"));
               var19.setString("doorDN", sourceTag.getString("doorDN"));
            }

            keyList.appendTag(var19);
         }
      }

      return result;
   }

   private ItemStack findRecipeItemStack(InventoryCrafting inv, Item itemToFind, int meta) {
      for(int i = 0; i < inv.getSizeInventory(); ++i) {
         ItemStack stack = inv.getStackInSlot(i);
         if(stack != null && stack.getItem() == itemToFind && stack.getItemDamage() == meta) {
            return stack;
         }
      }

      return null;
   }

   public int getRecipeSize() {
      return this.pattern.length;
   }
}
