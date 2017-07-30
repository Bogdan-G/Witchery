package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemDuplicationStaff extends ItemBase {

   public ItemDuplicationStaff() {
      this.setMaxStackSize(1);
      this.setFull3D();
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.epic;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if(!world.isRemote && player.isSneaking()) {
         MovingObjectPosition pickedBlock = InfusionOtherwhere.doCustomRayTrace(world, player, true, 6.0D);
         if((pickedBlock == null || pickedBlock.typeOfHit != MovingObjectType.BLOCK) && stack.hasTagCompound()) {
            NBTTagCompound nbtRoot = stack.getTagCompound();
            nbtRoot.removeTag("SavedSchematic");
            nbtRoot.removeTag("Marker");
            SoundEffect.RANDOM_FIZZ.playAtPlayer(world, player);
         }
      }

      return stack;
   }

   public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      if(stack.hasTagCompound() && stack.getTagCompound().hasKey("SavedSchematic")) {
         if(!player.isSneaking()) {
            int rotation = stack.getTagCompound().getInteger("Rotation");
            ++rotation;
            if(rotation > 3) {
               rotation = 0;
            }

            stack.getTagCompound().setInteger("Rotation", rotation);
            this.placeSchematic(stack, world, x, y + 1, z, player, ItemDuplicationStaff.Rotation.values()[rotation], true);
         } else {
            this.placeSchematic(stack, world, x, y + 1, z, player, ItemDuplicationStaff.Rotation.NONE, true);
            stack.getTagCompound().setInteger("Rotation", 0);
         }
      } else if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Marker")) {
         this.saveSchematic(stack, world, x, y, z, player);
      } else {
         this.setMarker(stack, world, x, y, z, player);
      }

      return !world.isRemote;
   }

   private void setMarker(ItemStack stack, World world, int x, int y, int z, EntityPlayer player) {
      if(!world.isRemote) {
         if(!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
         }

         NBTTagCompound nbtRoot = stack.getTagCompound();
         Coord marker = new Coord(x, y, z);
         nbtRoot.setTag("Marker", marker.toTagNBT());
         ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_POP, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 1.0D, 1.0D, 16);
      }

   }

   private void saveSchematic(ItemStack stack, World world, int x, int y, int z, EntityPlayer player) {
      if(!world.isRemote) {
         NBTTagCompound nbtRoot = stack.getTagCompound();
         if(nbtRoot != null) {
            PrintWriter writer = null;
            PrintWriter writer2 = null;

            try {
               Coord ex = Coord.fromTagNBT(nbtRoot.getCompoundTag("Marker"));
               NBTTagList nbtList = new NBTTagList();
               new ArrayList();
               int width = Math.max(ex.x, x) - Math.min(ex.x, x) - 1;
               int height = Math.max(ex.y, y) - Math.min(ex.y, y) - 1;
               int depth = Math.max(ex.z, z) - Math.min(ex.z, z) - 1;
               File file = Config.instance().dupStaffSaveTemplate?new File(String.format("%s/schematic.txt", new Object[]{Witchery.configDirectoryPath})):null;
               File file2 = Config.instance().dupStaffSaveTemplate?new File(String.format("%s/schematic2.txt", new Object[]{Witchery.configDirectoryPath})):null;
               if(file != null && !file.exists()) {
                  file.createNewFile();
               }

               writer = new PrintWriter(file);
               if(writer != null) {
                  writer.println(String.format("final NBTTagCompound nbtSchematic = new NBTTagCompound();", new Object[0]));
                  writer.println(String.format("final NBTTagList nbtList = new NBTTagList();", new Object[0]));
                  writer.println(String.format("NBTTagCompound nbtBlock;", new Object[0]));
               }

               if(file2 != null && !file2.exists()) {
                  file2.createNewFile();
               }

               writer2 = new PrintWriter(file2);
               if(writer2 != null) {
                  ;
               }

               if(ex.x != x && ex.y != y && ex.z != z) {
                  int nbtSchematic = Math.min(ex.x, x) + 1;
                  int minZ = Math.min(ex.z, z) + 1;
                  int minY = Math.min(ex.y, y) + 1;

                  for(int dx = nbtSchematic; dx < Math.max(ex.x, x); ++dx) {
                     for(int dz = minZ; dz < Math.max(ex.z, z); ++dz) {
                        for(int dy = minY; dy < Math.max(ex.y, y); ++dy) {
                           Block block = BlockUtil.getBlock(world, dx, dy, dz);
                           int meta = world.getBlockMetadata(dx, dy, dz);
                           NBTTagCompound nbtBlock = new NBTTagCompound();
                           String blockName = Block.blockRegistry.getNameForObject(block);
                           nbtBlock.setString("n", blockName);
                           if(meta != 0) {
                              nbtBlock.setInteger("m", meta);
                           }

                           nbtList.appendTag(nbtBlock);
                           if(writer != null) {
                              writer.println(String.format("nbtBlock = new NBTTagCompound();", new Object[0]));
                              writer.println(String.format("nbtBlock.setString(\"n\", \"%s\");", new Object[]{blockName}));
                              if(meta != 0) {
                                 writer.println(String.format("nbtBlock.setInteger(\"m\", %d);", new Object[]{Integer.valueOf(meta)}));
                              }

                              writer.println(String.format("nbtList.appendTag(nbtBlock);", new Object[0]));
                           }

                           if(writer2 != null && block != Blocks.air) {
                              String blockNameStripped = blockName.substring(blockName.indexOf(58) + 1);
                              writer2.println(String.format("placeBlockAtCurrentPosition(world, Blocks.%s, %s, %d, %d, %d, bounds);", new Object[]{blockNameStripped, blockNameStripped, Integer.valueOf(meta), Integer.valueOf(dx - nbtSchematic), Integer.valueOf(dy - minY), Integer.valueOf(dz - minZ)}));
                           }
                        }
                     }
                  }
               }

               if(nbtList.tagCount() > 0) {
                  NBTTagCompound var34 = new NBTTagCompound();
                  var34.setTag("blocks", nbtList);
                  var34.setInteger("xMax", width);
                  var34.setInteger("yMax", height);
                  var34.setInteger("zMax", depth);
                  if(writer != null) {
                     writer.println(String.format("nbtSchematic.setTag(\"blocks\", nbtList);", new Object[0]));
                     writer.println(String.format("nbtSchematic.setInteger(\"xMax\", %d);", new Object[]{Integer.valueOf(width)}));
                     writer.println(String.format("nbtSchematic.setInteger(\"yMax\", %d);", new Object[]{Integer.valueOf(height)}));
                     writer.println(String.format("nbtSchematic.setInteger(\"zMax\", %d);", new Object[]{Integer.valueOf(depth)}));
                  }

                  nbtRoot.setTag("SavedSchematic", var34);
                  nbtRoot.removeTag("Marker");
                  ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_POP, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 1.0D, 1.0D, 16);
               } else {
                  ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 1.0D, 1.0D, 16);
               }
            } catch (IOException var32) {
               ;
            } finally {
               if(writer != null) {
                  writer.close();
               }

               if(writer2 != null) {
                  writer2.close();
               }

            }
         }
      }

   }

   private void placeSchematic(ItemStack stack, World world, int x, int y, int z, EntityPlayer player, ItemDuplicationStaff.Rotation rotation, boolean clearAir) {
      if(!world.isRemote) {
         NBTTagCompound nbtRoot = stack.getTagCompound();
         if(nbtRoot != null) {
            NBTTagCompound nbtSchematic = nbtRoot.getCompoundTag("SavedSchematic");
            drawSchematicInWorld(world, x, y, z, rotation, clearAir, nbtSchematic);
         }
      }

   }

   public static void drawSchematicInWorld(World world, int x, int y, int z, ItemDuplicationStaff.Rotation rotation, boolean clearAir, NBTTagCompound nbtSchematic) {
      if(nbtSchematic != null) {
         NBTTagList nbtBlocks = nbtSchematic.getTagList("blocks", 10);
         int width = nbtSchematic.getInteger("xMax");
         int height = nbtSchematic.getInteger("yMax");
         int depth = nbtSchematic.getInteger("zMax");
         if(nbtBlocks != null && width > 0 && height > 0 && depth > 0) {
            int blockIndex = 0;
            int dx;
            int dz;
            if(rotation == ItemDuplicationStaff.Rotation.DEGREES_180) {
               for(dx = width - 1; dx >= 0; --dx) {
                  for(dz = depth - 1; dz >= 0; --dz) {
                     blockIndex = drawBlocks(world, dx + x, y, dz + z, nbtBlocks, height, blockIndex, rotation, clearAir);
                  }
               }
            } else if(rotation == ItemDuplicationStaff.Rotation.DEGREES_90) {
               for(dx = width - 1; dx >= 0; --dx) {
                  for(dz = 0; dz < depth; ++dz) {
                     blockIndex = drawBlocks(world, dz + x, y, dx + z, nbtBlocks, height, blockIndex, rotation, clearAir);
                  }
               }
            } else if(rotation == ItemDuplicationStaff.Rotation.DEGREES_270) {
               for(dx = 0; dx < width; ++dx) {
                  for(dz = depth - 1; dz >= 0; --dz) {
                     blockIndex = drawBlocks(world, dz + x, y, dx + z, nbtBlocks, height, blockIndex, rotation, clearAir);
                  }
               }
            } else {
               for(dx = 0; dx < width; ++dx) {
                  for(dz = 0; dz < depth; ++dz) {
                     blockIndex = drawBlocks(world, dx + x, y, dz + z, nbtBlocks, height, blockIndex, rotation, clearAir);
                  }
               }
            }
         }
      }

   }

   private static int drawBlocks(World world, int x, int y, int z, NBTTagList nbtBlocks, int height, int blockIndex, ItemDuplicationStaff.Rotation rotation, boolean clearAir) {
      for(int dy = 0; dy < height; ++dy) {
         NBTTagCompound nbtBlock = nbtBlocks.getCompoundTagAt(blockIndex++);
         String blockName = nbtBlock.getString("n");
         int blockMeta = nbtBlock.getInteger("m");
         Block block = Block.getBlockFromName(blockName);
         int direction;
         int other;
         if(block instanceof BlockStairs) {
            direction = blockMeta & 3;
            other = blockMeta >> 2 & 1;
            if(rotation == ItemDuplicationStaff.Rotation.DEGREES_180) {
               direction = (new int[]{1, 0, 3, 2})[direction];
            } else if(rotation == ItemDuplicationStaff.Rotation.DEGREES_90) {
               direction = (new int[]{3, 2, 0, 1})[direction];
            } else if(rotation == ItemDuplicationStaff.Rotation.DEGREES_270) {
               direction = (new int[]{2, 3, 1, 0})[direction];
            }

            blockMeta = direction | other << 2;
         } else if(block instanceof BlockRotatedPillar) {
            direction = blockMeta & 3;
            other = blockMeta >> 2 & 3;
            int other1 = blockMeta >> 4;
            if(rotation == ItemDuplicationStaff.Rotation.DEGREES_90 || rotation == ItemDuplicationStaff.Rotation.DEGREES_270) {
               other = (new int[]{0, 2, 1, 3})[other];
               blockMeta = direction | other << 2 | other1 << 4;
            }
         } else if(block instanceof BlockDoor && (blockMeta >> 4 & 1) == 0) {
            direction = blockMeta & 3;
            other = blockMeta >> 2;
            if(rotation == ItemDuplicationStaff.Rotation.DEGREES_180) {
               direction = (new int[]{2, 3, 0, 1})[direction];
            } else if(rotation == ItemDuplicationStaff.Rotation.DEGREES_90) {
               direction = (new int[]{3, 0, 1, 2})[direction];
            } else if(rotation == ItemDuplicationStaff.Rotation.DEGREES_270) {
               direction = (new int[]{1, 2, 3, 0})[direction];
            }

            blockMeta = direction | other << 2;
         }

         if(block != null && (clearAir || block != Blocks.air)) {
            world.setBlock(x, dy + y, z, block, blockMeta, 2);
         }
      }

      return blockIndex;
   }

   public static enum Rotation {

      NONE("NONE", 0),
      DEGREES_90("DEGREES_90", 1),
      DEGREES_180("DEGREES_180", 2),
      DEGREES_270("DEGREES_270", 3);
      // $FF: synthetic field
      private static final ItemDuplicationStaff.Rotation[] $VALUES = new ItemDuplicationStaff.Rotation[]{NONE, DEGREES_90, DEGREES_180, DEGREES_270};


      private Rotation(String var1, int var2) {}

   }
}
