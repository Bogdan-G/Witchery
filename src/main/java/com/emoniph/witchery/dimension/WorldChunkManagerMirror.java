package com.emoniph.witchery.dimension;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.Config;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

public class WorldChunkManagerMirror implements IChunkProvider {

   private World world;


   public WorldChunkManagerMirror(World world) {
      this.world = world;
   }

   public boolean chunkExists(int i, int j) {
      return true;
   }

   public Chunk provideChunk(int x, int z) {
      Chunk chunk = new Chunk(this.world, x, z);
      byte[] abyte = chunk.getBiomeArray();
      Block wall = Witchery.Blocks.MIRROR_WALL;
      Block air = Blocks.air;

      for(int wallPointsXZ = 0; wallPointsXZ < abyte.length; ++wallPointsXZ) {
         abyte[wallPointsXZ] = (byte)BiomeGenBase.hell.biomeID;
      }

      int[] var15 = new int[]{1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1};
      int[] wallPointsY = new int[]{1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};

      for(int y = 0; y < 255; ++y) {
         int l = y >> 4;
         ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[l];
         if(extendedblockstorage == null) {
            extendedblockstorage = new ExtendedBlockStorage(y, !this.world.provider.hasNoSky);
            chunk.getBlockStorageArray()[l] = extendedblockstorage;
         }

         int _y = y & 15;

         for(int _x = 0; _x < 16; ++_x) {
            for(int _z = 0; _z < 16; ++_z) {
               if((!Config.instance().shrinkMirrorWorld || y < 128) && (wallPointsY[_y] == 1 || var15[_x] == 1 || var15[_z] == 1)) {
                  extendedblockstorage.func_150818_a(_x, _y, _z, wall);
                  extendedblockstorage.setExtBlockMetadata(_x, _y, _z, 0);
               } else {
                  extendedblockstorage.func_150818_a(_x, _y, _z, air);
                  extendedblockstorage.setExtBlockMetadata(_x, _y, _z, 0);
               }
            }
         }
      }

      chunk.generateSkylightMap();
      return chunk;
   }

   public Chunk loadChunk(int x, int z) {
      return this.provideChunk(x, z);
   }

   public void populate(IChunkProvider ichunkprovider, int i, int j) {}

   public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
      return true;
   }

   public boolean unloadQueuedChunks() {
      return false;
   }

   public boolean canSave() {
      return true;
   }

   public String makeString() {
      return "MirrorChunk";
   }

   public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k) {
      return null;
   }

   public ChunkPosition func_147416_a(World world, String s, int i, int j, int k) {
      return null;
   }

   public int getLoadedChunkCount() {
      return 0;
   }

   public void recreateStructures(int i, int j) {}

   public void saveExtraData() {}
}
