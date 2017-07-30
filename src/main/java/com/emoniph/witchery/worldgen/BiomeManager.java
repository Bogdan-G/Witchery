package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.util.Log;
import java.util.ArrayList;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class BiomeManager {

   public static final ArrayList DISALLOWED_BIOMES = new ArrayList();


   public static void addModBiomes() {
      DISALLOWED_BIOMES.clear();
      ArrayList list = new ArrayList();
      BiomeGenBase[] arr$ = BiomeDictionary.getBiomesForType(Type.SWAMP);
      int len$ = arr$.length;

      int i$;
      BiomeGenBase biome;
      for(i$ = 0; i$ < len$; ++i$) {
         biome = arr$[i$];
         if(!list.contains(Integer.valueOf(biome.biomeID))) {
            list.add(Integer.valueOf(biome.biomeID));
         }
      }

      arr$ = BiomeDictionary.getBiomesForType(Type.RIVER);
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         biome = arr$[i$];
         if(!list.contains(Integer.valueOf(biome.biomeID))) {
            list.add(Integer.valueOf(biome.biomeID));
         }
      }

      arr$ = BiomeDictionary.getBiomesForType(Type.OCEAN);
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         biome = arr$[i$];
         if(!list.contains(Integer.valueOf(biome.biomeID))) {
            list.add(Integer.valueOf(biome.biomeID));
         }
      }

      arr$ = BiomeDictionary.getBiomesForType(Type.MOUNTAIN);
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         biome = arr$[i$];
         if(!list.contains(Integer.valueOf(biome.biomeID))) {
            list.add(Integer.valueOf(biome.biomeID));
         }
      }

      arr$ = BiomeDictionary.getBiomesForType(Type.JUNGLE);
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         biome = arr$[i$];
         if(!list.contains(Integer.valueOf(biome.biomeID))) {
            list.add(Integer.valueOf(biome.biomeID));
         }
      }

      arr$ = BiomeDictionary.getBiomesForType(Type.BEACH);
      len$ = arr$.length;

      for(i$ = 0; i$ < len$; ++i$) {
         biome = arr$[i$];
         if(!list.contains(Integer.valueOf(biome.biomeID))) {
            list.add(Integer.valueOf(biome.biomeID));
         }
      }

      Log.instance().debug("Found " + list.size() + " biomes to ignore for world gen.");
      if(list.size() > 0) {
         DISALLOWED_BIOMES.addAll(list);
      }

   }

   public static BiomeGenBase[] biomesWithout(Type ... biomesWithout) {
      ArrayList biomes = new ArrayList();
      BiomeGenBase[] arr$ = BiomeGenBase.getBiomeGenArray();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         BiomeGenBase biome = arr$[i$];
         if(biome != null) {
            boolean skip = false;

            for(int i = 0; i < biomesWithout.length; ++i) {
               if(BiomeDictionary.isBiomeOfType(biome, biomesWithout[i])) {
                  skip = true;
                  break;
               }
            }

            if(!skip) {
               biomes.add(biome);
            }
         }
      }

      return (BiomeGenBase[])biomes.toArray(new BiomeGenBase[biomes.size()]);
   }

}
