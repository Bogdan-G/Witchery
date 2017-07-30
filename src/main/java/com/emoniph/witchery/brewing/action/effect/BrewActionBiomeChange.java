package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.brewing.action.BrewActionList;
import com.emoniph.witchery.item.ItemBook;
import com.emoniph.witchery.util.Coord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionBiomeChange extends BrewActionEffect {

   public BrewActionBiomeChange(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, EffectLevel effectLevel) {
      super(itemKey, namePart, powerCost, baseProbability, effectLevel);
   }

   public void prepareSplashPotion(World world, BrewActionList actionList, ModifiersImpact modifiers) {
      super.prepareSplashPotion(world, actionList, modifiers);
      modifiers.setOnlyInstant();
   }

   protected void doApplyRitualToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
      BiomeGenBase biome = ItemBook.getSelectedBiome(stack.getItemDamage());
      int maxRadius = 16 + modifiers.getStrength() * 16;
      this.changeBiome(world, new Coord(x, y, z), maxRadius, biome);
   }

   protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
      BiomeGenBase biome = ItemBook.getSelectedBiome(actionStack.getItemDamage());
      this.changeBiome(world, new Coord(x, y, z), 1 + modifiers.getStrength(), biome);
   }

   protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {}

   protected void changeBiome(World world, Coord coord, int radius, BiomeGenBase biome) {
      HashMap chunkMap = new HashMap();
      this.drawFilledCircle(world, coord.x, coord.z, radius, chunkMap, biome);
      ArrayList chunks = new ArrayList();
      Iterator packet = chunkMap.entrySet().iterator();

      Chunk chunk;
      while(packet.hasNext()) {
         Entry i$ = (Entry)packet.next();
         chunk = ((BrewActionBiomeChange.ChunkCoord)i$.getKey()).getChunk(world);
         chunk.setBiomeArray((byte[])i$.getValue());
         chunks.add(chunk);
      }

      S26PacketMapChunkBulk packet1 = new S26PacketMapChunkBulk(chunks);
      Witchery.packetPipeline.sendToDimension(packet1, world);
      Iterator i$2 = chunks.iterator();

      while(i$2.hasNext()) {
         chunk = (Chunk)i$2.next();
         Iterator i$1 = chunk.chunkTileEntityMap.values().iterator();

         while(i$1.hasNext()) {
            Object tileObj = i$1.next();
            TileEntity tile = (TileEntity)tileObj;
            Packet packet2 = tile.getDescriptionPacket();
            if(packet2 != null) {
               world.markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
            }
         }
      }

   }

   private void drawFilledCircle(World world, int x0, int z0, int radius, HashMap chunkMap, BiomeGenBase biome) {
      if(radius == 1) {
         this.drawLine(world, x0, x0, z0, chunkMap, biome);
      } else {
         --radius;
         int x = radius;
         int z = 0;
         int radiusError = 1 - radius;

         while(x >= z) {
            this.drawLine(world, -x + x0, x + x0, z + z0, chunkMap, biome);
            this.drawLine(world, -z + x0, z + x0, x + z0, chunkMap, biome);
            this.drawLine(world, -x + x0, x + x0, -z + z0, chunkMap, biome);
            this.drawLine(world, -z + x0, z + x0, -x + z0, chunkMap, biome);
            ++z;
            if(radiusError < 0) {
               radiusError += 2 * z + 1;
            } else {
               --x;
               radiusError += 2 * (z - x + 1);
            }
         }
      }

   }

   private void drawLine(World world, int x1, int x2, int z, HashMap chunkMap, BiomeGenBase biome) {
      for(int x = x1; x <= x2; ++x) {
         BrewActionBiomeChange.ChunkCoord coord = new BrewActionBiomeChange.ChunkCoord(x >> 4, z >> 4);
         byte[] map = (byte[])chunkMap.get(coord);
         if(map == null) {
            Chunk y = world.getChunkFromBlockCoords(x, z);
            map = (byte[])y.getBiomeArray().clone();
            chunkMap.put(coord, map);
         }

         map[(z & 15) << 4 | x & 15] = (byte)biome.biomeID;
         if(biome.rainfall == 0.0F) {
            int var11 = world.getTopSolidOrLiquidBlock(x, z);
            if(world.getBlock(x, var11, z) == Blocks.snow_layer) {
               world.setBlockToAir(x, var11, z);
            }
         }
      }

   }

   private static class ChunkCoord {

      public final int X;
      public final int Z;


      public ChunkCoord(int x, int z) {
         this.X = x;
         this.Z = z;
      }

      public boolean equals(Object obj) {
         if(obj == this) {
            return true;
         } else if(obj != null && obj.getClass() == this.getClass()) {
            BrewActionBiomeChange.ChunkCoord other = (BrewActionBiomeChange.ChunkCoord)obj;
            return this.X == other.X && this.Z == other.Z;
         } else {
            return false;
         }
      }

      public int hashCode() {
         int result = this.X ^ this.X >>> 32;
         result = 31 * result + (this.Z ^ this.Z >>> 32);
         return result;
      }

      public Chunk getChunk(World world) {
         return world.getChunkFromChunkCoords(this.X, this.Z);
      }
   }
}
