package com.emoniph.witchery.integration;

import codechicken.nei.api.IHighlightHandler;
import codechicken.nei.api.ItemInfo.Layout;
import com.emoniph.witchery.Witchery;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class NEIHighlightHandler implements IHighlightHandler {

   private static final ItemStack yellowPlant = new ItemStack(Blocks.yellow_flower);
   private static final ItemStack redPlant = new ItemStack(Blocks.red_flower);
   private static final ItemStack shrubPlant = new ItemStack(Blocks.deadbush);
   private static final ItemStack door = new ItemStack(Blocks.wooden_door);
   private static final ItemStack dirt = new ItemStack(Blocks.dirt);
   private static final ItemStack grass = new ItemStack(Blocks.grass);
   private final Block block;


   public NEIHighlightHandler(Block block) {
      this.block = block;
   }

   public ItemStack identifyHighlight(World world, EntityPlayer player, MovingObjectPosition mop) {
      if(this.block == Witchery.Blocks.TRAPPED_PLANT) {
         if(mop == null || mop.typeOfHit == MovingObjectType.ENTITY) {
            return null;
         }

         int foundMeta = world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
         if(foundMeta == 5 || foundMeta == 6 || foundMeta == 7 || foundMeta == 4) {
            return yellowPlant;
         }

         if(foundMeta == 1 || foundMeta == 2 || foundMeta == 3 || foundMeta == 0) {
            return redPlant;
         }

         if(foundMeta == 9 || foundMeta == 10 || foundMeta == 11 || foundMeta == 8) {
            return shrubPlant;
         }
      } else {
         if(this.block == Witchery.Blocks.DOOR_ALDER) {
            return door;
         }

         if(this.block == Witchery.Blocks.PIT_DIRT) {
            return dirt;
         }

         if(this.block == Witchery.Blocks.PIT_GRASS) {
            return grass;
         }
      }

      return null;
   }

   public List handleTextData(ItemStack itemStack, World world, EntityPlayer player, MovingObjectPosition mop, List currenttip, Layout layout) {
      return null;
   }

}
