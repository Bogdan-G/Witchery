package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class RiteClimateChange extends Rite {

   protected final int radius;


   public RiteClimateChange(int radius) {
      this.radius = radius;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteClimateChange.StepClimateChange(this, intialStage));
   }

   // $FF: synthetic class
   static class NamelessClass1888453163 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$ritual$rites$RiteClimateChange$WeatherChange = new int[RiteClimateChange.WeatherChange.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$ritual$rites$RiteClimateChange$WeatherChange[RiteClimateChange.WeatherChange.SUN.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$ritual$rites$RiteClimateChange$WeatherChange[RiteClimateChange.WeatherChange.RAIN.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$ritual$rites$RiteClimateChange$WeatherChange[RiteClimateChange.WeatherChange.THUNDER.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$ritual$rites$RiteClimateChange$WeatherChange[RiteClimateChange.WeatherChange.NONE.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   private static class StepClimateChange extends RitualStep {

      private final RiteClimateChange rite;
      private int stage = 0;
      private boolean activated;


      public StepClimateChange(RiteClimateChange rite, int initialStage) {
         super(false);
         this.rite = rite;
         this.stage = initialStage;
      }

      public int getCurrentStage() {
         return (byte)this.stage;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(!this.activated) {
            if(ticks % 20L != 0L) {
               return RitualStep.Result.STARTING;
            }

            this.activated = true;
            SoundEffect.RANDOM_FIZZ.playAt(world, (double)posX, (double)posY, (double)posZ);
         }

         if(!world.isRemote) {
            EntityPlayer player = ritual.getInitiatingPlayer(world);
            if(!Config.instance().allowBiomeChanging) {
               SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
               if(player != null) {
                  ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.disabled", new Object[0]);
               }

               return RitualStep.Result.ABORTED_REFUND;
            } else {
               BiomeGenBase biome = world.getBiomeGenForCoords(posX, posZ);
               if(world.provider.dimensionId != 1 && world.provider.dimensionId != -1 && biome != BiomeGenBase.sky && biome != BiomeGenBase.hell) {
                  if(ritual.covenSize < 4) {
                     SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
                     if(player != null) {
                        ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.coventoosmall", new Object[0]);
                     }

                     return RitualStep.Result.ABORTED_REFUND;
                  } else if(ticks % 20L != 0L) {
                     return RitualStep.Result.UPKEEP;
                  } else {
                     ++this.stage;
                     if(this.stage < 5) {
                        ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, world, 0.5D + (double)posX, 1.0D + (double)posY, 0.5D + (double)posZ, (double)((float)this.stage * 1.5F), (double)((float)this.stage * 1.1F), 16);
                     } else if(this.stage == 5) {
                        ParticleEffect.HUGE_EXPLOSION.send(SoundEffect.NONE, world, 0.5D + (double)posX, 1.0D + (double)posY, 0.5D + (double)posZ, (double)((float)this.stage * 2.0F), (double)((float)this.stage * 1.5F), 16);
                        double RADIUS = 8.0D;
                        List items = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox((double)posX - 8.0D, (double)(posY - 2), (double)posZ - 8.0D, (double)posX + 8.0D, (double)(posY + 2), (double)posZ + 8.0D));
                        Type biomeType = Type.END;
                        RiteClimateChange.WeatherChange weather = RiteClimateChange.WeatherChange.NONE;
                        int glowstone = 0;
                        Iterator biomes = items.iterator();

                        while(biomes.hasNext()) {
                           Object biomeID = biomes.next();
                           EntityItem maxRadius = (EntityItem)biomeID;
                           ItemStack chunkMap = maxRadius.getEntityItem();
                           if(chunkMap.isItemEqual(new ItemStack(Blocks.sapling, 1, 0))) {
                              biomeType = Type.FOREST;
                           } else if(chunkMap.isItemEqual(new ItemStack(Blocks.tallgrass, 1, 1))) {
                              biomeType = Type.PLAINS;
                           } else if(chunkMap.isItemEqual(new ItemStack(Blocks.obsidian))) {
                              biomeType = Type.MOUNTAIN;
                           } else if(chunkMap.isItemEqual(new ItemStack(Blocks.stone))) {
                              biomeType = Type.HILLS;
                           } else if(chunkMap.isItemEqual(new ItemStack(Items.slime_ball))) {
                              biomeType = Type.SWAMP;
                           } else if(chunkMap.isItemEqual(new ItemStack(Items.water_bucket))) {
                              biomeType = Type.WATER;
                           } else if(chunkMap.isItemEqual(new ItemStack(Blocks.cactus))) {
                              biomeType = Type.DESERT;
                              weather = RiteClimateChange.WeatherChange.SUN;
                           } else if(chunkMap.isItemEqual(Witchery.Items.GENERIC.itemIcyNeedle.createStack())) {
                              biomeType = Type.FROZEN;
                              weather = RiteClimateChange.WeatherChange.RAIN;
                           } else if(chunkMap.isItemEqual(new ItemStack(Blocks.sapling, 1, 3))) {
                              biomeType = Type.JUNGLE;
                           } else if(chunkMap.isItemEqual(new ItemStack(Blocks.netherrack))) {
                              biomeType = Type.WASTELAND;
                           } else if(chunkMap.isItemEqual(new ItemStack(Blocks.sand))) {
                              biomeType = Type.BEACH;
                           } else if(chunkMap.isItemEqual(new ItemStack(Blocks.red_mushroom))) {
                              biomeType = Type.MUSHROOM;
                           } else if(chunkMap.isItemEqual(new ItemStack(Items.skull))) {
                              biomeType = Type.MAGICAL;
                           } else {
                              if(chunkMap.getItem() != Items.glowstone_dust) {
                                 continue;
                              }

                              glowstone += chunkMap.stackSize;
                           }

                           world.removeEntity(maxRadius);
                           ParticleEffect.INSTANT_SPELL.send(SoundEffect.RANDOM_POP, maxRadius, 0.5D, 1.0D, 16);
                        }

                        if(biomeType == Type.END) {
                           SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
                           if(player != null) {
                              ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.missingbiomefoci", new Object[0]);
                           }

                           return RitualStep.Result.ABORTED_REFUND;
                        }

                        BiomeGenBase[] var29 = BiomeDictionary.getBiomesForType(biomeType);
                        if(var29 != null && var29.length != 0) {
                           int var30 = var29[glowstone > 0?Math.min(glowstone, var29.length) - 1:(var29.length >= 3?world.rand.nextInt(3):0)].biomeID;
                           int var32 = this.rite.radius * (ritual.covenSize - 3);
                           HashMap var31 = new HashMap();
                           this.drawFilledCircle(world, posX, posZ, var32, var31, weather, var30);
                           ArrayList chunks = new ArrayList();
                           Iterator packet = var31.entrySet().iterator();

                           while(packet.hasNext()) {
                              Entry worldinfo = (Entry)packet.next();
                              Chunk i = ((RiteClimateChange.StepClimateChange.ChunkCoord)worldinfo.getKey()).getChunk(world);
                              i.setBiomeArray((byte[])worldinfo.getValue());
                              chunks.add(i);
                           }

                           S26PacketMapChunkBulk var36 = new S26PacketMapChunkBulk(chunks);
                           Witchery.packetPipeline.sendToDimension(var36, world);
                           Iterator var33 = chunks.iterator();

                           while(var33.hasNext()) {
                              Object var35 = var33.next();
                              Chunk chunk = (Chunk)var35;
                              Iterator i$ = chunk.chunkTileEntityMap.values().iterator();

                              while(i$.hasNext()) {
                                 Object tileObj = i$.next();
                                 TileEntity tile = (TileEntity)tileObj;
                                 Packet packet2 = tile.getDescriptionPacket();
                                 if(packet2 != null) {
                                    world.markBlockForUpdate(tile.xCoord, tile.yCoord, tile.zCoord);
                                 }
                              }
                           }

                           if(world instanceof WorldServer) {
                              WorldInfo var34 = ((WorldServer)world).getWorldInfo();
                              int var37 = (300 + world.rand.nextInt(600)) * 20;
                              switch(RiteClimateChange.NamelessClass1888453163.$SwitchMap$com$emoniph$witchery$ritual$rites$RiteClimateChange$WeatherChange[weather.ordinal()]) {
                              case 1:
                                 if(world.isRaining() || world.isThundering()) {
                                    var34.setRainTime(0);
                                    var34.setThunderTime(0);
                                    var34.setRaining(false);
                                    var34.setThundering(false);
                                 }
                                 break;
                              case 2:
                                 if(!world.isRaining() && !world.isThundering()) {
                                    var34.setRainTime(var37);
                                    var34.setThunderTime(var37);
                                    var34.setRaining(true);
                                    var34.setThundering(false);
                                 }
                                 break;
                              case 3:
                                 if(!world.isThundering()) {
                                    var34.setRainTime(var37);
                                    var34.setThunderTime(var37);
                                    var34.setRaining(true);
                                    var34.setThundering(true);
                                 }
                              case 4:
                              }
                           }

                           return RitualStep.Result.COMPLETED;
                        }

                        SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
                        if(player != null) {
                           ChatUtil.sendTranslated(EnumChatFormatting.DARK_RED, player, "witchery.rite.missingbiomefoci", new Object[0]);
                        }

                        return RitualStep.Result.ABORTED_REFUND;
                     }

                     return RitualStep.Result.UPKEEP;
                  }
               } else {
                  SoundEffect.NOTE_SNARE.playAt(world, (double)posX, (double)posY, (double)posZ);
                  if(player != null) {
                     ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.rite.wrongdimension", new Object[0]);
                  }

                  return RitualStep.Result.ABORTED_REFUND;
               }
            }
         } else {
            return RitualStep.Result.COMPLETED;
         }
      }

      private static byte[] rotateMatrix(byte[] matrix, int n) {
         byte[] ret = new byte[matrix.length];

         for(int i = 0; i < matrix.length / n; ++i) {
            for(int j = 0; j < n; ++j) {
               ret[j * n + i] = matrix[i * n + n - j];
            }
         }

         return ret;
      }

      protected void drawFilledCircle(World world, int x0, int z0, int radius, HashMap chunkMap, RiteClimateChange.WeatherChange weather, int biomeID) {
         int x = radius;
         int z = 0;
         int radiusError = 1 - radius;

         while(x >= z) {
            this.drawLine(world, -x + x0, x + x0, z + z0, chunkMap, weather, biomeID);
            this.drawLine(world, -z + x0, z + x0, x + z0, chunkMap, weather, biomeID);
            this.drawLine(world, -x + x0, x + x0, -z + z0, chunkMap, weather, biomeID);
            this.drawLine(world, -z + x0, z + x0, -x + z0, chunkMap, weather, biomeID);
            ++z;
            if(radiusError < 0) {
               radiusError += 2 * z + 1;
            } else {
               --x;
               radiusError += 2 * (z - x + 1);
            }
         }

      }

      protected void drawLine(World world, int x1, int x2, int z, HashMap chunkMap, RiteClimateChange.WeatherChange weather, int biomeID) {
         for(int x = x1; x <= x2; ++x) {
            RiteClimateChange.StepClimateChange.ChunkCoord coord = new RiteClimateChange.StepClimateChange.ChunkCoord(x >> 4, z >> 4);
            byte[] map = (byte[])chunkMap.get(coord);
            if(map == null) {
               Chunk y = world.getChunkFromBlockCoords(x, z);
               map = (byte[])y.getBiomeArray().clone();
               chunkMap.put(coord, map);
            }

            map[(z & 15) << 4 | x & 15] = (byte)biomeID;
            if(weather == RiteClimateChange.WeatherChange.SUN) {
               int var12 = world.getTopSolidOrLiquidBlock(x, z);
               if(world.getBlock(x, var12, z) == Blocks.snow) {
                  world.setBlockToAir(x, var12, z);
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
               RiteClimateChange.StepClimateChange.ChunkCoord other = (RiteClimateChange.StepClimateChange.ChunkCoord)obj;
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

   public static enum WeatherChange {

      NONE("NONE", 0),
      SUN("SUN", 1),
      RAIN("RAIN", 2),
      THUNDER("THUNDER", 3);
      // $FF: synthetic field
      private static final RiteClimateChange.WeatherChange[] $VALUES = new RiteClimateChange.WeatherChange[]{NONE, SUN, RAIN, THUNDER};


      private WeatherChange(String var1, int var2) {}

   }
}
