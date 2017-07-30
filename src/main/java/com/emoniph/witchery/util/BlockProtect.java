package com.emoniph.witchery.util;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.Config;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class BlockProtect {

   public static boolean canBreak(Block block, World world) {
      return canBreak(block, world, true);
   }

   public static boolean canBreak(Block block, World world, boolean denyContainers) {
      return block != null && block.hasTileEntity(0)?false:block != Blocks.dragon_egg && block != Blocks.bedrock && block != Witchery.Blocks.FORCE && block != Witchery.Blocks.BARRIER;
   }

   public static boolean canBreak(int x, int y, int z, World world) {
      return canBreak(x, y, z, world, true);
   }

   public static boolean canBreak(int x, int y, int z, World world, boolean denyContainers) {
      Block block = world.getBlock(x, y, z);
      return canBreak(block, world, denyContainers);
   }

   public static boolean checkModsForBreakOK(World world, int x, int y, int z, EntityLivingBase entity) {
      return checkModsForBreakOK(world, x, y, z, world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), entity);
   }

   public static boolean checkModsForBreakOK(World world, int x, int y, int z, Block block, int meta, EntityLivingBase entity) {
      boolean allowBreak = block.getBlockHardness(world, x, y, z) != -1.0F;
      if(allowBreak && entity != null && entity instanceof EntityPlayer && Config.instance().allowBlockBreakEvents) {
         BreakEvent event = new BreakEvent(x, y, z, world, block, meta, (EntityPlayer)entity);
         event.setCanceled(false);
         MinecraftForge.EVENT_BUS.post(event);
         allowBreak = !event.isCanceled();
      }

      return allowBreak;
   }
}
