package com.emoniph.witchery.util;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockFetish;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.Coord;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockUtil {

   public static Block registerBlock(Block block, String blockName) {
      int index = blockName.indexOf(58);
      if(index != -1) {
         blockName = blockName.substring(index + 1);
      }

      return GameRegistry.registerBlock(block, blockName);
   }

   public static Block registerBlock(Block block, Class clazzItem, String blockName) {
      int index = blockName.indexOf(58);
      if(index != -1) {
         blockName = blockName.substring(index + 1);
      }

      return GameRegistry.registerBlock(block, clazzItem, blockName);
   }

   public static Block getBlock(World world, int posX, int posY, int posZ) {
      return world.getBlock(posX, posY, posZ);
   }

   public static Block getBlock(World world, double posX, double posY, double posZ) {
      int x = MathHelper.floor_double(posX);
      int y = MathHelper.floor_double(posY);
      int z = MathHelper.floor_double(posZ);
      return getBlock(world, x, y, z);
   }

   public static Block getBlock(World world, MovingObjectPosition mop) {
      return getBlock(world, mop, false);
   }

   public static boolean isReplaceableBlock(World world, int posX, int posY, int posZ) {
      return isReplaceableBlock(world, posX, posY, posZ, (EntityLivingBase)null);
   }

   public static boolean isReplaceableBlock(World world, int posX, int posY, int posZ, EntityLivingBase player) {
      Block block = getBlock(world, posX, posY, posZ);
      if(player != null) {
         int meta = world.getBlockMetadata(posX, posY, posZ);
         if(!BlockProtect.checkModsForBreakOK(world, posX, posY, posZ, block, meta, player)) {
            return false;
         }
      }

      return block != null?block.getMaterial().isReplaceable():true;
   }

   public static Material getBlockMaterial(EntityPlayer player) {
      return getBlockMaterial(player, 0);
   }

   public static Material getBlockMaterial(EntityPlayer player, int yOffset) {
      int posX = MathHelper.floor_double(player.posX);
      int posY = MathHelper.floor_double(player.boundingBox.minY) + yOffset;
      int posZ = MathHelper.floor_double(player.posZ);
      return getBlockMaterial(player.worldObj, posX, posY, posZ);
   }

   public static Material getBlockMaterial(World world, int posX, int posY, int posZ) {
      Block block = getBlock(world, posX, posY, posZ);
      return block != null?block.getMaterial():Material.air;
   }

   public static Block getBlock(World world, MovingObjectPosition mop, boolean before) {
      if(mop == null) {
         return null;
      } else {
         int posX;
         int posY;
         int posZ;
         if(mop.typeOfHit == MovingObjectType.BLOCK) {
            if(before) {
               posX = mop.blockX + (mop.sideHit == 4?-1:(mop.sideHit == 5?1:0));
               posY = mop.blockZ + (mop.sideHit == 2?-1:(mop.sideHit == 3?1:0));
               posZ = mop.blockY + (mop.sideHit == 0?-1:(mop.sideHit == 1?1:0));
               if(mop.sideHit == 1 && !world.getBlock(posX, mop.blockY, posY).getMaterial().isSolid()) {
                  --posZ;
               }

               return getBlock(world, posX, posZ, posY);
            } else {
               return getBlock(world, mop.blockX, mop.blockY, mop.blockZ);
            }
         } else {
            posX = MathHelper.floor_double(mop.entityHit.posX);
            posY = MathHelper.floor_double(mop.entityHit.posY) - 1;
            posZ = MathHelper.floor_double(mop.entityHit.posZ);
            return getBlock(world, posX, posY, posZ);
         }
      }
   }

   public static int[] getBlockCoords(World world, MovingObjectPosition mop, boolean before) {
      if(mop == null) {
         return null;
      } else {
         int posX;
         int posY;
         int posZ;
         if(mop.typeOfHit == MovingObjectType.BLOCK) {
            if(before) {
               posX = mop.blockX + (mop.sideHit == 4?-1:(mop.sideHit == 5?1:0));
               posY = mop.blockZ + (mop.sideHit == 2?-1:(mop.sideHit == 3?1:0));
               posZ = mop.blockY + (mop.sideHit == 0?-1:(mop.sideHit == 1?1:0));
               if(mop.sideHit == 1 && !world.getBlock(posX, mop.blockY, posY).getMaterial().isSolid()) {
                  --posZ;
               }

               return new int[]{posX, posZ, posY};
            } else {
               return new int[]{mop.blockX, mop.blockY, mop.blockZ};
            }
         } else {
            posX = MathHelper.floor_double(mop.entityHit.posX);
            posY = MathHelper.floor_double(mop.entityHit.posY) - 1;
            posZ = MathHelper.floor_double(mop.entityHit.posZ);
            return new int[]{posX, posY, posZ};
         }
      }
   }

   public static int getBlockMetadata(World world, int posX, int posY, int posZ) {
      int blockMetadata = world.getBlockMetadata(posX, posY, posZ);
      return blockMetadata;
   }

   public static Object getTileEntity(IBlockAccess world, int posX, int posY, int posZ, Class clazz) {
      TileEntity tile = world.getTileEntity(posX, posY, posZ);
      return tile != null && clazz.isAssignableFrom(tile.getClass())?clazz.cast(tile):null;
   }

   public static void setBlock(World world, int posX, int posY, int posZ, Block newBlock, int newMetadata, int updateFlags) {
      world.setBlock(posX, posY, posZ, newBlock != null?newBlock:Blocks.air, newMetadata, updateFlags);
   }

   public static void setBlock(World world, int posX, int posY, int posZ, Block newBlock) {
      world.setBlock(posX, posY, posZ, newBlock != null?newBlock:Blocks.air);
   }

   public static void setBlock(World world, double posX, double posY, double posZ, Block block) {
      int x = MathHelper.floor_double(posX);
      int y = MathHelper.floor_double(posY);
      int z = MathHelper.floor_double(posZ);
      setBlock(world, x, y, z, block);
   }

   public static void setBlock(World world, int posX, int posY, int posZ, ItemBlock item, int damage, int updateFlags) {
      world.setBlock(posX, posY, posZ, item.field_150939_a, damage, updateFlags);
   }

   public static void setMetadata(World world, int posX, int posY, int posZ, int newMetadata) {
      setMetadata(world, posX, posY, posZ, newMetadata, 3);
   }

   public static void setMetadata(World world, int posX, int posY, int posZ, int newMetadata, int updateFlags) {
      world.setBlockMetadataWithNotify(posX, posY, posZ, newMetadata, updateFlags);
   }

   public static void setAirBlock(World world, int x, int y, int z) {
      world.setBlockToAir(x, y, z);
   }

   public static void setAirBlock(World world, int x, int y, int z, int updateFlags) {
      world.setBlock(x, y, z, Blocks.air, 0, updateFlags);
   }

   public static void notifyNeighborsOfBlockChange(World world, int xCoord, int yCoord, int zCoord, Block blockType) {
      world.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, blockType);
   }

   public static boolean isImmovableBlock(Block block) {
      return block == Witchery.Blocks.ALTAR || block == Witchery.Blocks.VOID_BRAMBLE;
   }

   public static boolean isImmovableBlock(TileEntity tile) {
      return tile instanceof BlockFetish.TileEntityFetish?((BlockFetish.TileEntityFetish)tile).isSpectral():false;
   }

   public static void setBlockDefaultDirection(World world, int posX, int posY, int posZ) {
      if(!world.isRemote) {
         Block l = world.getBlock(posX, posY, posZ - 1);
         Block i1 = world.getBlock(posX, posY, posZ + 1);
         Block j1 = world.getBlock(posX - 1, posY, posZ);
         Block k1 = world.getBlock(posX + 1, posY, posZ);
         byte b0 = 3;
         if(l.isOpaqueCube() && !i1.isOpaqueCube()) {
            b0 = 3;
         }

         if(i1.isOpaqueCube() && !l.isOpaqueCube()) {
            b0 = 2;
         }

         if(j1.isOpaqueCube() && !k1.isOpaqueCube()) {
            b0 = 5;
         }

         if(k1.isOpaqueCube() && !j1.isOpaqueCube()) {
            b0 = 4;
         }

         world.setBlockMetadataWithNotify(posX, posY, posZ, b0, 2);
      }

   }

   public static boolean isSolid(World world, int posX, int posY, int posZ) {
      Block block = getBlock(world, posX, posY, posZ);
      return block != null?!block.getMaterial().isReplaceable():false;
   }

   public static boolean isNormalCube(Block block) {
      return block.getMaterial().blocksMovement() && block.renderAsNormalBlock();
   }

   public static Coord getClosestPlantableBlock(World world, int x, int y, int z, ForgeDirection side, EntityLivingBase entity) {
      return getClosestPlantableBlock(world, x, y, z, side, entity, false);
   }

   public static Coord getClosestPlantableBlock(World world, int x, int y, int z, ForgeDirection side, EntityLivingBase entity, boolean allowAir) {
      boolean foundBase = false;
      if(isReplaceableBlock(world, x, y, z) && (!allowAir || !world.isAirBlock(x, y, z))) {
         do {
            --y;
         } while(isReplaceableBlock(world, x, y, z));

         foundBase = true;
      } else if(side != ForgeDirection.UP && side != ForgeDirection.UNKNOWN) {
         if(side != ForgeDirection.DOWN) {
            x += side.offsetX;
            z += side.offsetZ;
            if(isReplaceableBlock(world, x, y, z)) {
               --y;
               foundBase = !isReplaceableBlock(world, x, y, z);
            }
         }
      } else {
         foundBase = true;
      }

      if(foundBase) {
         Block replaceBlock = world.getBlock(x, y + 1, z);
         int replaceMeta = world.getBlockMetadata(x, y + 1, z);
         if(BlockProtect.checkModsForBreakOK(world, x, y + 1, z, replaceBlock, replaceMeta, entity)) {
            return new Coord(x, y + 1, z);
         }
      }

      return null;
   }

   public static boolean setBlockIfReplaceable(World world, int x, int y, int z, Block block) {
      return setBlockIfReplaceable(world, x, y, z, block, 0);
   }

   public static boolean setBlockIfReplaceable(World world, int x, int y, int z, Block block, int meta) {
      Block currentBlock = world.getBlock(x, y, z);
      if(currentBlock != null && currentBlock.isReplaceable(world, x, y, z)) {
         world.setBlock(x, y, z, block, meta, 3);
         return true;
      } else {
         return false;
      }
   }
}
