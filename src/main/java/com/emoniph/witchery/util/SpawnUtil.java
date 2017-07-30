package com.emoniph.witchery.util;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SpawnUtil {

   public static void spawnEntityItem(World world, double x, double y, double z, Block block, int quantity) {
      spawnEntityItem(world, x, y, z, Item.getItemFromBlock(block), quantity, 0);
   }

   public static void spawnEntityItem(World world, double x, double y, double z, Item item, int quantity) {
      spawnEntityItem(world, x, y, z, item, quantity, 0);
   }

   public static void spawnEntityItem(World world, double x, double y, double z, Item item, int quantity, int damageValue) {
      if(!world.isRemote) {
         int maxStackSize = item.getItemStackLimit(new ItemStack(item));

         int remainder;
         for(remainder = 0; remainder < quantity / maxStackSize; ++remainder) {
            world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(item, maxStackSize, damageValue)));
         }

         remainder = quantity % maxStackSize;
         if(remainder > 0) {
            world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(item, remainder, damageValue)));
         }
      }

   }

   public static void spawnEntityItem(World world, double x, double y, double z, ItemStack stack) {
      world.spawnEntityInWorld(new EntityItem(world, x, y, z, stack.copy()));
   }
}
