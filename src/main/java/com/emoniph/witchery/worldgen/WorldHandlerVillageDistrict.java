package com.emoniph.witchery.worldgen;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.entity.EntityVillageGuard;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.worldgen.ComponentVillageKeep;
import com.emoniph.witchery.worldgen.ComponentVillageWatchTower;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.Church;
import net.minecraft.world.gen.structure.StructureVillagePieces.Field1;
import net.minecraft.world.gen.structure.StructureVillagePieces.Field2;
import net.minecraft.world.gen.structure.StructureVillagePieces.Hall;
import net.minecraft.world.gen.structure.StructureVillagePieces.House1;
import net.minecraft.world.gen.structure.StructureVillagePieces.House2;
import net.minecraft.world.gen.structure.StructureVillagePieces.House3;
import net.minecraft.world.gen.structure.StructureVillagePieces.House4Garden;
import net.minecraft.world.gen.structure.StructureVillagePieces.Path;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraft.world.gen.structure.StructureVillagePieces.WoodHut;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID;
import net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockMeta;

public class WorldHandlerVillageDistrict implements IVillageCreationHandler {

   private final Class pieceClazz;
   private final int weight;
   private final int quantityMin;
   private final int quantityMax;


   public WorldHandlerVillageDistrict(Class clazz, int weight, int min) {
      this(clazz, weight, min, min);
   }

   public WorldHandlerVillageDistrict(Class clazz, int weight, int min, int max) {
      this.pieceClazz = clazz;
      this.weight = weight;
      this.quantityMin = min;
      this.quantityMax = max;
   }

   public PieceWeight getVillagePieceWeight(Random rand, int size) {
      return new PieceWeight(this.pieceClazz, this.weight, this.quantityMax <= this.quantityMin?this.quantityMin:this.quantityMin + rand.nextInt(this.quantityMax - this.quantityMin + 1));
   }

   public Class getComponentClass() {
      return this.pieceClazz;
   }

   public Object buildComponent(PieceWeight weight, Start startPiece, List pieces, Random rand, int p1, int p2, int p3, int p4, int p5) {
      Object object = null;
      if(this.pieceClazz == House4Garden.class) {
         object = House4Garden.func_74912_a(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == Church.class) {
         object = Church.func_74919_a(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == House1.class) {
         object = House1.func_74898_a(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == WoodHut.class) {
         object = WoodHut.func_74908_a(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == Hall.class) {
         object = Hall.func_74906_a(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == Field1.class) {
         object = Field1.func_74900_a(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == Field2.class) {
         object = Field2.func_74902_a(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == House2.class) {
         object = House2.func_74915_a(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == House3.class) {
         object = House3.func_74921_a(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == WorldHandlerVillageDistrict.Wall.class) {
         object = WorldHandlerVillageDistrict.Wall.func_74921_a(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == ComponentVillageWatchTower.class) {
         object = ComponentVillageWatchTower.construct(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      } else if(this.pieceClazz == ComponentVillageKeep.class) {
         object = ComponentVillageKeep.construct(startPiece, pieces, rand, p1, p2, p3, p4, p5);
      }

      return object == null?null:(Village)object;
   }

   public static void registerComponent(Class clazz, int weight, int min, int max) {
      VillagerRegistry.instance().registerVillageCreationHandler(new WorldHandlerVillageDistrict(clazz, weight, min, max));
   }

   public static void preInit() {
      try {
         MapGenStructureIO.func_143031_a(WorldHandlerVillageDistrict.Wall.class, "witchery:villagewall");
         MapGenStructureIO.func_143031_a(ComponentVillageKeep.class, "witchery:villagekeep");
         MapGenStructureIO.func_143031_a(ComponentVillageWatchTower.class, "witchery:villagewatchtower");
      } catch (Throwable var4) {
         ;
      }

      if(Config.instance().townWallChance > 0) {
         registerComponent(WorldHandlerVillageDistrict.Wall.class, Config.instance().townWallWeight, Config.instance().townWallChance == 2?1:0, 1);
      }

      if(Config.instance().townKeepChance > 0) {
         registerComponent(ComponentVillageKeep.class, Config.instance().townKeepWeight, Config.instance().townKeepChance == 2?1:0, 1);
      }

      VillagerRegistry register = VillagerRegistry.instance();
      Iterator i$ = Config.instance().townParts.iterator();

      while(i$.hasNext()) {
         Config.Building building = (Config.Building)i$.next();

         for(int i = 0; i < building.groups; ++i) {
            register.registerVillageCreationHandler(new WorldHandlerVillageDistrict(building.clazz, building.weight, building.min, building.max));
         }
      }

   }

   public static void init() {
      BiomeGenBase[] arr$ = BiomeGenBase.getBiomeGenArray();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         BiomeGenBase biome = arr$[i$];
         if(biome != null && !BiomeDictionary.isBiomeOfType(biome, Type.WET) && !BiomeDictionary.isBiomeOfType(biome, Type.OCEAN) && !BiomeDictionary.isBiomeOfType(biome, Type.BEACH) && !BiomeDictionary.isBiomeOfType(biome, Type.END) && !BiomeDictionary.isBiomeOfType(biome, Type.JUNGLE) && !BiomeDictionary.isBiomeOfType(biome, Type.NETHER) && !BiomeDictionary.isBiomeOfType(biome, Type.RIVER) && !BiomeDictionary.isBiomeOfType(biome, Type.WATER)) {
            boolean disallowed = !Config.instance().townAllowSandy && BiomeDictionary.isBiomeOfType(biome, Type.SANDY) || !Config.instance().townAllowPlains && BiomeDictionary.isBiomeOfType(biome, Type.PLAINS) || !Config.instance().townAllowMountain && BiomeDictionary.isBiomeOfType(biome, Type.MOUNTAIN) || !Config.instance().townAllowHills && BiomeDictionary.isBiomeOfType(biome, Type.HILLS) || !Config.instance().townAllowForest && BiomeDictionary.isBiomeOfType(biome, Type.FOREST) || !Config.instance().townAllowSnowy && BiomeDictionary.isBiomeOfType(biome, Type.SNOWY) || !Config.instance().townAllowWasteland && BiomeDictionary.isBiomeOfType(biome, Type.WASTELAND) || !Config.instance().townAllowJungle && BiomeDictionary.isBiomeOfType(biome, Type.JUNGLE) || !Config.instance().townAllowMesa && BiomeDictionary.isBiomeOfType(biome, Type.MESA);
            if(!disallowed) {
               net.minecraftforge.common.BiomeManager.addVillageBiome(biome, true);
            }
         }
      }

   }

   public static class EventHooks {

      @SubscribeEvent
      public void onGetVillageBlock(GetVillageBlockID event) {
         if(event.biome != null) {
            Block b = event.original;
            if(BiomeDictionary.isBiomeOfType(event.biome, Type.SANDY)) {
               if(b != Blocks.log && b != Blocks.log2) {
                  if(b == Blocks.cobblestone) {
                     event.replacement = Blocks.sandstone;
                  } else if(b == Blocks.planks) {
                     event.replacement = Blocks.planks;
                     event.setResult(Result.DENY);
                  } else if(b == Blocks.oak_stairs) {
                     event.replacement = Blocks.birch_stairs;
                  } else if(b == Blocks.stone_stairs) {
                     event.replacement = Blocks.sandstone_stairs;
                  } else if(b == Blocks.gravel) {
                     event.replacement = Blocks.sandstone;
                  } else if(b == Blocks.stonebrick) {
                     event.replacement = Blocks.sandstone;
                  } else if(b == Blocks.wooden_slab) {
                     event.replacement = Blocks.wooden_slab;
                  } else if(b == Blocks.stone_brick_stairs) {
                     event.replacement = Blocks.sandstone_stairs;
                  }
               } else {
                  event.replacement = Blocks.sandstone;
               }
            } else if(BiomeDictionary.isBiomeOfType(event.biome, Type.SNOWY)) {
               if(b != Blocks.log && b != Blocks.log2) {
                  if(b == Blocks.cobblestone) {
                     event.replacement = Blocks.snow;
                  } else if(b == Blocks.planks) {
                     event.replacement = Blocks.snow;
                  } else if(b == Blocks.oak_stairs) {
                     event.replacement = Witchery.Blocks.SNOW_STAIRS;
                  } else if(b == Blocks.stone_stairs) {
                     event.replacement = Witchery.Blocks.SNOW_STAIRS;
                  } else if(b == Blocks.gravel) {
                     event.replacement = Blocks.packed_ice;
                  } else if(b == Blocks.stonebrick) {
                     event.replacement = Blocks.snow;
                  } else if(b == Blocks.stone_slab) {
                     event.replacement = Witchery.Blocks.SNOW_SLAB_SINGLE;
                  } else if(b == Blocks.wooden_slab) {
                     event.replacement = Witchery.Blocks.SNOW_SLAB_SINGLE;
                  } else if(b == Blocks.fence) {
                     event.replacement = Witchery.Blocks.PERPETUAL_ICE_FENCE;
                  } else if(b == Blocks.dirt) {
                     event.replacement = Blocks.snow;
                  } else if(b == Blocks.wooden_pressure_plate) {
                     event.replacement = Witchery.Blocks.SNOW_PRESSURE_PLATE;
                  } else if(b == Blocks.stone_brick_stairs) {
                     event.replacement = Witchery.Blocks.SNOW_STAIRS;
                  }
               } else {
                  event.replacement = Blocks.packed_ice;
               }
            }

            if(event.replacement != null && event.replacement != event.original) {
               event.setResult(Result.DENY);
            }

         }
      }

      @SubscribeEvent
      public void onGetVillageBlockMeta(GetVillageBlockMeta event) {
         Block b = event.original;
         if(event.biome != null) {
            if(BiomeDictionary.isBiomeOfType(event.biome, Type.SANDY)) {
               if(b != Blocks.log && b != Blocks.log2) {
                  if(b == Blocks.cobblestone) {
                     event.replacement = 0;
                     event.setResult(Result.DENY);
                  } else if(b == Blocks.planks) {
                     event.replacement = 2;
                     event.setResult(Result.DENY);
                  } else if(b == Blocks.wooden_slab) {
                     event.replacement = 2;
                     event.setResult(Result.DENY);
                  } else if(b == Blocks.stone_slab) {
                     if(event.type != 3 && event.type != 0) {
                        if(event.type == 11 || event.type == 8) {
                           event.replacement = 9;
                           event.setResult(Result.DENY);
                        }
                     } else {
                        event.replacement = 1;
                        event.setResult(Result.DENY);
                     }
                  } else if(b == Blocks.stonebrick) {
                     event.replacement = 2;
                     event.setResult(Result.DENY);
                  }
               } else {
                  event.replacement = 2;
                  event.setResult(Result.DENY);
               }
            } else if(BiomeDictionary.isBiomeOfType(event.biome, Type.SNOWY)) {
               if(b != Blocks.log && b != Blocks.log2) {
                  if(b == Blocks.cobblestone) {
                     event.replacement = 0;
                     event.setResult(Result.DENY);
                  } else if(b == Blocks.planks) {
                     event.replacement = 0;
                     event.setResult(Result.DENY);
                  } else if(b == Blocks.stone_slab) {
                     if(event.type >= 8) {
                        event.replacement = 8;
                        event.setResult(Result.DENY);
                     } else {
                        event.replacement = 0;
                        event.setResult(Result.DENY);
                     }
                  } else if(b == Blocks.stonebrick) {
                     event.replacement = 0;
                     event.setResult(Result.DENY);
                  }
               } else {
                  event.replacement = 0;
                  event.setResult(Result.DENY);
               }
            }

         }
      }
   }

   public static class Wall extends Village {

      private Start start;
      private List pieces;
      private boolean hasMadeWallBlock;


      public Wall() {}

      public Wall(Start start, int componentType, Random rand, StructureBoundingBox bounds, int baseMode) {
         super(start, componentType);
         super.coordBaseMode = baseMode;
         super.boundingBox = bounds;
         this.start = start;
      }

      public static WorldHandlerVillageDistrict.Wall func_74921_a(Start startPiece, List pieces, Random rand, int p1, int p2, int p3, int p4, int p5) {
         StructureBoundingBox bounds = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, 2, 7, 2, p4);
         boolean create = canVillageGoDeeper(bounds) && StructureComponent.findIntersecting(pieces, bounds) == null && !containsWalls(pieces);
         return create?new WorldHandlerVillageDistrict.Wall(startPiece, p5, rand, bounds, p4):null;
      }

      private static boolean containsWalls(List pieces2) {
         return false;
      }

      public void buildComponent(StructureComponent component, List pieces, Random rand) {
         super.buildComponent(component, pieces, rand);
         this.pieces = pieces;
      }

      public boolean addComponentParts(World world, Random rand, StructureBoundingBox bounds) {
         if(super.field_143015_k < 0) {
            super.field_143015_k = this.getAverageGroundLevel(world, bounds);
            if(super.field_143015_k < 0) {
               return true;
            }

            super.boundingBox.offset(0, super.field_143015_k - super.boundingBox.maxY + 7 - 1, 0);
         }

         if(!this.hasMadeWallBlock) {
            byte x = 1;
            byte z = 1;
            int xCoord = this.getXWithOffset(x, z);
            int yCoord = this.getYWithOffset(1);
            int zCoord = this.getZWithOffset(x, z);
            if(this.pieces != null && bounds.isVecInside(xCoord, yCoord, zCoord)) {
               this.hasMadeWallBlock = true;
               world.setBlock(xCoord, yCoord, zCoord, Witchery.Blocks.WALLGEN);
               WorldHandlerVillageDistrict.Wall.BlockVillageWallGen.TileEntityVillageWallGen tile = (WorldHandlerVillageDistrict.Wall.BlockVillageWallGen.TileEntityVillageWallGen)BlockUtil.getTileEntity(world, xCoord, yCoord, zCoord, WorldHandlerVillageDistrict.Wall.BlockVillageWallGen.TileEntityVillageWallGen.class);
               if(tile != null) {
                  tile.setStructure(this.pieces, this.start);
               }
            }
         }

         return true;
      }

      protected void func_143012_a(NBTTagCompound nbtRoot) {
         super.func_143012_a(nbtRoot);
         nbtRoot.setBoolean("WallBlock", this.hasMadeWallBlock);
      }

      protected void func_143011_b(NBTTagCompound nbtRoot) {
         super.func_143011_b(nbtRoot);
         this.hasMadeWallBlock = nbtRoot.getBoolean("WallBlock");
      }

      public static void placeWalls(World world, List bb, int xCoord, int yCoord, int zCoord, BiomeGenBase biome, boolean desert) {
         int minX = Integer.MAX_VALUE;
         int minZ = Integer.MAX_VALUE;
         int maxX = Integer.MIN_VALUE;
         int maxZ = Integer.MIN_VALUE;
         Log.instance().debug(String.format("Generating town walls at [%d %d %d]", new Object[]{Integer.valueOf(xCoord), Integer.valueOf(yCoord), Integer.valueOf(zCoord)}));

         for(int a = 0; a < bb.size(); ++a) {
            minX = Math.min(((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(a)).minX, minX);
            minZ = Math.min(((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(a)).minZ, minZ);
            maxX = Math.max(((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(a)).maxX, maxX);
            maxZ = Math.max(((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(a)).maxZ, maxZ);
         }

         if(maxX != Integer.MIN_VALUE && minX != Integer.MAX_VALUE && maxZ != Integer.MIN_VALUE && minZ != Integer.MAX_VALUE) {
            byte[][] var45 = new byte[maxX - minX + 3][maxZ - minZ + 3];
            short[][] b = new short[maxX - minX + 3][maxZ - minZ + 3];

            int blockBase;
            int blockFence;
            int blockBaseMeta;
            int stairsBlock;
            int x;
            int guardDist;
            for(int range = 0; range < bb.size(); ++range) {
               blockBase = ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).maxX - ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).minX + 1;
               blockFence = blockBase / 2 + ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).minX - 1;
               stairsBlock = ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).maxZ - ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).minZ + 1;
               blockBaseMeta = stairsBlock / 2 + ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).minZ - 1;

               for(int event = ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).minX; event <= ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).maxX; ++event) {
                  for(int event2 = ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).minZ; event2 <= ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).maxZ; ++event2) {
                     guardDist = event - minX + 1;
                     x = event2 - minZ + 1;
                     if(!((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).ew && (event2 == ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).minZ || event2 == ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).maxZ) && event >= blockFence - 1 && event <= blockFence + 1) {
                        var45[guardDist][x] = 3;
                     } else if(((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).ew && (event == ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).minX || event == ((WorldHandlerVillageDistrict.Wall.StructureBounds)bb.get(range)).maxX) && event2 >= blockBaseMeta - 1 && event2 <= blockBaseMeta + 1) {
                        var45[guardDist][x] = 3;
                     } else {
                        var45[guardDist][x] = 2;
                     }
                  }
               }
            }

            byte var46 = 7;

            for(blockBase = 1; blockBase < var45.length - var46; ++blockBase) {
               for(blockFence = 1; blockFence < var45[blockBase].length - var46; ++blockFence) {
                  if(var45[blockBase][blockFence] == 2) {
                     for(stairsBlock = 1; stairsBlock < var46; ++stairsBlock) {
                        if(var45[blockBase + stairsBlock][blockFence] == 2 && var45[blockBase + stairsBlock - 1][blockFence] == 0) {
                           for(blockBaseMeta = stairsBlock; blockBaseMeta > 0; --blockBaseMeta) {
                              var45[blockBase + blockBaseMeta][blockFence] = 2;
                           }
                        }

                        if(var45[blockBase][blockFence + stairsBlock] == 2 && var45[blockBase][blockFence + stairsBlock - 1] == 0) {
                           for(blockBaseMeta = stairsBlock; blockBaseMeta > 0; --blockBaseMeta) {
                              var45[blockBase][blockFence + blockBaseMeta] = 2;
                           }
                        }
                     }
                  }
               }
            }

            boolean n;
            for(blockBase = 1; blockBase < var45.length - 1; ++blockBase) {
               for(blockFence = 1; blockFence < var45[blockBase].length - 1; ++blockFence) {
                  boolean var50 = var45[blockBase][blockFence - 1] == 0;
                  boolean var53 = var45[blockBase][blockFence + 1] == 0;
                  boolean var52 = var45[blockBase + 1][blockFence] == 0;
                  boolean var56 = var45[blockBase - 1][blockFence] == 0;
                  boolean var54 = var45[blockBase + 1][blockFence - 1] == 0;
                  boolean var58 = var45[blockBase - 1][blockFence + 1] == 0;
                  boolean z = var45[blockBase + 1][blockFence + 1] == 0;
                  n = var45[blockBase - 1][blockFence - 1] == 0;
                  if(!var50 && !var53 && !var52 && !var56 && !var54 && !z && !n && !var58) {
                     var45[blockBase][blockFence] = 1;
                  }
               }
            }

            Block var48 = Blocks.stonebrick;
            Block var47 = Blocks.fence;
            Block var49 = Blocks.stone_brick_stairs;
            blockBaseMeta = 0;
            GetVillageBlockID var51 = new GetVillageBlockID(biome, var48, blockBaseMeta);
            MinecraftForge.TERRAIN_GEN_BUS.post(var51);
            if(var51.getResult() == Result.DENY) {
               var48 = var51.replacement;
            } else if(desert) {
               var48 = Blocks.sandstone;
            }

            var51 = new GetVillageBlockID(biome, var47, 0);
            MinecraftForge.TERRAIN_GEN_BUS.post(var51);
            if(var51.getResult() == Result.DENY) {
               var47 = var51.replacement;
            }

            var51 = new GetVillageBlockID(biome, var49, 0);
            MinecraftForge.TERRAIN_GEN_BUS.post(var51);
            if(var51.getResult() == Result.DENY) {
               var49 = var51.replacement;
            } else if(desert) {
               var49 = Blocks.sandstone_stairs;
            }

            GetVillageBlockMeta var55 = new GetVillageBlockMeta(biome, var48, blockBaseMeta);
            MinecraftForge.TERRAIN_GEN_BUS.post(var55);
            if(var55.getResult() == Result.DENY) {
               blockBaseMeta = var55.replacement;
            } else if(desert) {
               blockBaseMeta = 2;
            }

            guardDist = 0;

            for(x = 1; x < var45.length - 1; ++x) {
               for(int var57 = 1; var57 < var45[x].length - 1; ++var57) {
                  n = var45[x][var57 - 1] >= 2;
                  boolean s = var45[x][var57 + 1] >= 2;
                  boolean e = var45[x + 1][var57] >= 2;
                  boolean w = var45[x - 1][var57] >= 2;
                  boolean ne = var45[x + 1][var57 - 1] >= 2;
                  boolean sw = var45[x - 1][var57 + 1] >= 2;
                  boolean se = var45[x + 1][var57 + 1] >= 2;
                  boolean nw = var45[x - 1][var57 - 1] >= 2;
                  if(var45[x][var57] >= 2) {
                     int dx = minX + x;
                     int dz = minZ + var57;
                     int solidCount = 0;

                     int dy;
                     int startY;
                     for(dy = yCoord; dy > 1 && solidCount < 9; --dy) {
                        solidCount = 0;

                        for(int minHeight = dx - 1; minHeight <= dx + 1; ++minHeight) {
                           for(startY = dz - 1; startY <= dz + 1; ++startY) {
                              Block near = world.getBlock(minHeight, dy, startY);
                              boolean lowestY = near.getMaterial().isReplaceable() || near.getMaterial() == Material.leaves || near.getMaterial() == Material.wood || near.getMaterial() == Material.plants;
                              if(near.isNormalCube() && !lowestY) {
                                 ++solidCount;
                              }
                           }
                        }
                     }

                     boolean var59 = true;
                     startY = dy + 9;
                     int var61 = Math.max(Math.max(Math.max(b[x - 1][var57], b[x + 1][var57]), b[x][var57 + 1]), b[x][var57 - 1]);
                     if(var61 > 0) {
                        if(var61 > startY) {
                           startY = var61 - 1;
                        } else if(var61 < startY) {
                           startY = var61 + 1;
                        }
                     }

                     int var60 = dy;
                     if(startY - dy > 0) {
                        b[x][var57] = (short)Math.min(Math.max(startY, 0), 32767);
                     }

                     for(dy = startY; dy > var60; --dy) {
                        if(dy == startY) {
                           if(!ne && !n && !e) {
                              setBlock(world, dx + 2, dy, dz - 2, var48, blockBaseMeta);
                              setBlock(world, dx + 2, dy, dz - 1, var48, blockBaseMeta);
                              setBlock(world, dx + 1, dy, dz - 2, var48, blockBaseMeta);
                              setBlock(world, dx + 2, dy + 1, dz - 2, var48, blockBaseMeta, false);
                              setBlock(world, dx + 2, dy + 1, dz - 1, var48, blockBaseMeta, false);
                              setBlock(world, dx + 1, dy + 1, dz - 2, var48, blockBaseMeta, false);
                           }

                           if(!nw && !n && !w) {
                              setBlock(world, dx - 2, dy, dz - 2, var48, blockBaseMeta);
                              setBlock(world, dx - 1, dy, dz - 2, var48, blockBaseMeta);
                              setBlock(world, dx - 2, dy, dz - 1, var48, blockBaseMeta);
                              setBlock(world, dx - 2, dy + 1, dz - 2, var48, blockBaseMeta, false);
                              setBlock(world, dx - 1, dy + 1, dz - 2, var48, blockBaseMeta, false);
                              setBlock(world, dx - 2, dy + 1, dz - 1, var48, blockBaseMeta, false);
                           }

                           if(!se && !s && !e) {
                              setBlock(world, dx + 2, dy, dz + 2, var48, blockBaseMeta);
                              setBlock(world, dx + 1, dy, dz + 2, var48, blockBaseMeta);
                              setBlock(world, dx + 2, dy, dz + 1, var48, blockBaseMeta);
                              setBlock(world, dx + 2, dy + 1, dz + 2, var48, blockBaseMeta, false);
                              setBlock(world, dx + 1, dy + 1, dz + 2, var48, blockBaseMeta, false);
                              setBlock(world, dx + 2, dy + 1, dz + 1, var48, blockBaseMeta, false);
                           }

                           if(!sw && !s && !w) {
                              setBlock(world, dx - 2, dy, dz + 2, var48, blockBaseMeta);
                              setBlock(world, dx - 1, dy, dz + 2, var48, blockBaseMeta);
                              setBlock(world, dx - 2, dy, dz + 1, var48, blockBaseMeta);
                              setBlock(world, dx - 2, dy + 1, dz + 2, var48, blockBaseMeta, false);
                              setBlock(world, dx - 1, dy + 1, dz + 2, var48, blockBaseMeta, false);
                              setBlock(world, dx - 2, dy + 1, dz + 1, var48, blockBaseMeta, false);
                           }

                           if(!n && !ne && !nw) {
                              setBlock(world, dx, dy, dz - 2, var48, blockBaseMeta);
                              setBlock(world, dx, dy + 1, dz - 2, var49, 0, false);
                           }

                           if(!e && !se && !ne) {
                              setBlock(world, dx + 2, dy, dz, var48, blockBaseMeta);
                              setBlock(world, dx + 2, dy + 1, dz, var49, 2, false);
                           }

                           if(!s && !se && !sw) {
                              setBlock(world, dx, dy, dz + 2, var48, blockBaseMeta);
                              setBlock(world, dx, dy + 1, dz + 2, var49, 0, false);
                           }

                           if(!w && !nw && !sw) {
                              setBlock(world, dx - 2, dy, dz, var48, blockBaseMeta);
                              setBlock(world, dx - 2, dy + 1, dz, var49, 2, false);
                           }

                           ++guardDist;
                           if(guardDist > 200) {
                              spawnGuard(world, dx, dy, dz);
                              guardDist = 0;
                           }
                        } else {
                           byte distCheck = 4;
                           boolean gate = var45[x][var57] == 3 && (x > distCheck && x < var45.length - distCheck && var45[x - distCheck][var57] == 2 && var45[x + distCheck][var57] == 2 || var57 > distCheck && var57 < var45[x].length - distCheck && var45[x][var57 - distCheck] == 2 && var45[x][var57 + distCheck] == 2);
                           if(gate && dy == startY - 3) {
                              world.setBlock(dx, dy, dz, var47);
                              if(var45[x + 1][var57] != 3 || var45[x - 1][var57] != 3) {
                                 if(var45[x + 1][var57] == 3) {
                                    world.setBlock(dx, dy, dz - 1, var49, 5, 2);
                                    world.setBlock(dx, dy, dz + 1, var49, 5, 2);
                                 } else if(var45[x - 1][var57] == 3) {
                                    world.setBlock(dx, dy, dz - 1, var49, 4, 2);
                                    world.setBlock(dx, dy, dz + 1, var49, 4, 2);
                                 } else if(var45[x][var57 + 1] != 3 || var45[x][var57 - 1] != 3) {
                                    if(var45[x][var57 - 1] == 3) {
                                       world.setBlock(dx - 1, dy, dz, var49, 6, 2);
                                       world.setBlock(dx + 1, dy, dz, var49, 6, 2);
                                    } else if(var45[x][var57 + 1] == 3) {
                                       world.setBlock(dx - 1, dy, dz, var49, 7, 2);
                                       world.setBlock(dx + 1, dy, dz, var49, 7, 2);
                                    }
                                 }
                              }
                           }

                           if(!gate || dy > startY - 3) {
                              setBlock(world, dx, dy, dz, var48, blockBaseMeta);
                              boolean ng = var45[x][var57 - 1] == 3;
                              boolean sg = var45[x][var57 + 1] == 3;
                              boolean eg = var45[x + 1][var57] == 3;
                              boolean wg = var45[x - 1][var57] == 3;
                              if(!ng) {
                                 setBlock(world, dx, dy, dz - 1, var48, blockBaseMeta);
                              }

                              if(!ng && !eg) {
                                 setBlock(world, dx + 1, dy, dz - 1, var48, blockBaseMeta);
                              }

                              if(!ng && !wg) {
                                 setBlock(world, dx - 1, dy, dz - 1, var48, blockBaseMeta);
                              }

                              if(!eg) {
                                 setBlock(world, dx + 1, dy, dz, var48, blockBaseMeta);
                              }

                              if(!sg) {
                                 setBlock(world, dx, dy, dz + 1, var48, blockBaseMeta);
                              }

                              if(!sg && !eg) {
                                 setBlock(world, dx + 1, dy, dz + 1, var48, blockBaseMeta);
                              }

                              if(!sg && !wg) {
                                 setBlock(world, dx - 1, dy, dz + 1, var48, blockBaseMeta);
                              }

                              if(!wg) {
                                 setBlock(world, dx - 1, dy, dz, var48, blockBaseMeta);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

      }

      private static void spawnGuard(World world, int x, int y, int z) {
         EntityVillageGuard guard = new EntityVillageGuard(world);
         guard.setLocationAndAngles((double)x + 0.5D, (double)y, (double)z + 0.5D, 0.0F, 0.0F);
         guard.func_110163_bv();
         guard.onSpawnWithEgg((IEntityLivingData)null);
         world.spawnEntityInWorld(guard);
      }

      private static void setBlock(World world, int x, int y, int z, Block block, int meta) {
         setBlock(world, x, y, z, block, meta, true);
      }

      private static void setBlock(World world, int x, int y, int z, Block block, int meta, boolean notStacked) {
         Block replaceBlock = world.getBlock(x, y, z);
         Material material = replaceBlock.getMaterial();
         if(material.isReplaceable() || material == Material.leaves || material == Material.wood || material == Material.plants) {
            world.setBlock(x, y, z, block, meta, 2);
         }

      }

      public static class StructureBounds extends StructureBoundingBox {

         public final boolean ew;


         public StructureBounds(Path path, int expansionX, int expansionZ) {
            this(path.getBoundingBox(), expansionX, expansionZ);
         }

         public StructureBounds(StructureBoundingBox bb, int expansionX, int expansionZ) {
            this(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, expansionX, expansionZ);
         }

         public StructureBounds(int x, int y, int z, int x2, int y2, int z2, int expansionX, int expansionZ) {
            this.ew = x2 - x > z2 - z;
            if(this.ew) {
               super.minX = x - expansionZ;
               super.maxX = x2 + expansionZ;
               super.minZ = z - expansionX;
               super.maxZ = z2 + expansionX;
            } else {
               super.minX = x - expansionX;
               super.maxX = x2 + expansionX;
               super.minZ = z - expansionZ;
               super.maxZ = z2 + expansionZ;
            }

            super.minY = y;
            super.maxY = y2;
         }
      }

      public static class BlockVillageWallGen extends BlockBaseContainer {

         public BlockVillageWallGen() {
            super(Material.rock, WorldHandlerVillageDistrict.Wall.BlockVillageWallGen.TileEntityVillageWallGen.class);
            super.registerWithCreateTab = false;
            this.setBlockUnbreakable();
            this.setResistance(10000.0F);
         }

         public static class TileEntityVillageWallGen extends TileEntityBase {

            private List bb;
            private BiomeGenBase biome;
            private boolean desert;


            public void updateEntity() {
               super.updateEntity();
               if(!super.worldObj.isRemote && this.bb != null && super.ticks > 40L) {
                  WorldHandlerVillageDistrict.Wall.placeWalls(super.worldObj, this.bb, super.xCoord, super.yCoord, super.zCoord, this.biome, this.desert);
                  this.bb = null;
                  super.worldObj.setBlockToAir(super.xCoord, super.yCoord, super.zCoord);
               } else if(!super.worldObj.isRemote && super.ticks > 1000L) {
                  this.bb = null;
                  super.worldObj.setBlockToAir(super.xCoord, super.yCoord, super.zCoord);
               }

            }

            public void setStructure(List pieces, Start start) {
               this.biome = start.biome;
               this.desert = start.inDesert;
               this.bb = new ArrayList();
               Iterator i$ = pieces.iterator();

               while(i$.hasNext()) {
                  Object obj = i$.next();
                  if(obj instanceof Path) {
                     this.bb.add(new WorldHandlerVillageDistrict.Wall.StructureBounds((Path)obj, 20, 7));
                  }
               }

            }
         }
      }
   }
}
