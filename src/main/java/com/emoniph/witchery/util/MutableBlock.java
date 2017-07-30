package com.emoniph.witchery.util;

import com.emoniph.witchery.util.Log;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public class MutableBlock {

   private final Block block;
   private final int metadata;
   private final int newMetadata;


   public MutableBlock(Block block) {
      this(block, -1, 0);
   }

   public MutableBlock(Block block, int metadata) {
      this(block, metadata, 0);
   }

   public MutableBlock(Block block, int metadata, int newMetadata) {
      this.block = block;
      this.metadata = metadata;
      this.newMetadata = newMetadata;
   }

   public MutableBlock(String extra) {
      String name = extra;
      int meta = 0;
      int comma = extra.lastIndexOf(44);
      if(comma >= 0) {
         name = extra.substring(0, comma);
         String metaString = extra.substring(comma + 1);
         meta = Integer.parseInt(metaString);
      }

      this.block = Block.getBlockFromName(name);
      this.metadata = meta;
      this.newMetadata = 0;
   }

   public boolean equals(Object obj) {
      if(obj == this) {
         return true;
      } else if(obj != null && obj.getClass() == this.getClass()) {
         MutableBlock other = (MutableBlock)obj;
         return this.block == other.block && (this.metadata == -1 || other.metadata == -1 || this.metadata == other.metadata);
      } else {
         return false;
      }
   }

   public void mutate(World world, int posX, int posY, int posZ) {
      this.mutate(world, posX, posY, posZ, true);
   }

   public void mutate(World world, int posX, int posY, int posZ, boolean allowAnyPlacement) {
      try {
         if(this.metadata != -1) {
            if(allowAnyPlacement || this.block.canPlaceBlockAt(world, posX, posY, posZ)) {
               world.setBlock(posX, posY, posZ, this.block, this.metadata, 3);
            }
         } else if(this.newMetadata > 0) {
            if(allowAnyPlacement || this.block.canPlaceBlockAt(world, posX, posY, posZ)) {
               world.setBlock(posX, posY, posZ, this.block, this.newMetadata, 3);
            }
         } else if(allowAnyPlacement || this.block.canPlaceBlockAt(world, posX, posY, posZ)) {
            world.setBlock(posX, posY, posZ, this.block);
         }
      } catch (Exception var7) {
         Log.instance().debug(String.format("Exception occured mutating a plant %s", new Object[]{var7.toString()}));
      }

   }
}
