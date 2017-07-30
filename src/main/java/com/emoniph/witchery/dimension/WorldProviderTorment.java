package com.emoniph.witchery.dimension;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.dimension.WorldChunkManagerTorment;
import com.emoniph.witchery.entity.EntityLordOfTorment;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.ServerUtil;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.ForgeModContainer;

public class WorldProviderTorment extends WorldProvider {

   public static final String SPIRIT_WORLD_TORMENT_PLAYER_KEY = "WITCForceTorment";
   public static final String SPIRIT_WORLD_TORMENT_LEVEL_KEY = "WITCForceTormentLevel";
   public static final int TORMENT_NONE = 0;
   public static final int TORMENT_BEGIN = 1;
   public static final int TORMENT_BEGIN_WITH_BOSS = 2;
   public static final int TORMENT_END = 3;


   public String getDimensionName() {
      return "Torment";
   }

   public IChunkProvider createChunkGenerator() {
      return new WorldChunkManagerTorment(super.worldObj);
   }

   public boolean canRespawnHere() {
      return false;
   }

   public boolean isSurfaceWorld() {
      return false;
   }

   public boolean canDoLightning(Chunk chunk) {
      return false;
   }

   public boolean isBlockHighHumidity(int x, int y, int z) {
      return false;
   }

   public boolean isDaytime() {
      return false;
   }

   public ChunkCoordinates getSpawnPoint() {
      return new ChunkCoordinates(8, 14, 8);
   }

   @SideOnly(Side.CLIENT)
   public boolean isSkyColored() {
      return true;
   }

   public float calculateCelestialAngle(long par1, float par3) {
      return 1.0F;
   }

   public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
      float f1 = super.worldObj.getCelestialAngle(partialTicks);
      float f2 = MathHelper.cos(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F;
      if(f2 < 0.0F) {
         f2 = 0.0F;
      }

      if(f2 > 1.0F) {
         f2 = 1.0F;
      }

      int i = MathHelper.floor_double(cameraEntity.posX);
      int j = MathHelper.floor_double(cameraEntity.posY);
      int k = MathHelper.floor_double(cameraEntity.posZ);
      GameSettings settings = Minecraft.getMinecraft().gameSettings;
      int[] ranges = ForgeModContainer.blendRanges;
      int distance = 0;
      if(settings.fancyGraphics && settings.renderDistanceChunks >= 0 && settings.renderDistanceChunks < ranges.length) {
         distance = ranges[settings.renderDistanceChunks];
      }

      int r = 0;
      int g = 0;
      int b = 0;
      int divider = 0;

      int multiplier;
      for(multiplier = -distance; multiplier <= distance; ++multiplier) {
         for(int l = -distance; l <= distance; ++l) {
            super.worldObj.getBiomeGenForCoords(i + multiplier, k + l);
            int f5 = 16711680;
            r += (f5 & 16711680) >> 16;
            g += (f5 & '\uff00') >> 8;
            b += f5 & 255;
            ++divider;
         }
      }

      multiplier = (r / divider & 255) << 16 | (g / divider & 255) << 8 | b / divider & 255;
      float f4 = (float)(multiplier >> 16 & 255) / 255.0F;
      float var20 = (float)(multiplier >> 8 & 255) / 255.0F;
      float f6 = (float)(multiplier & 255) / 255.0F;
      return Vec3.createVectorHelper((double)f4, (double)var20, (double)f6);
   }

   public static void setPlayerMustTorment(EntityPlayer player, int torment, int presetLevel) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      setPlayerMustTorment(nbtPlayer, torment, presetLevel);
   }

   public static void setPlayerMustTorment(NBTTagCompound nbtPlayer, int torment, int presetLevel) {
      nbtPlayer.setInteger("WITCForceTorment", torment);
      if(presetLevel > -1) {
         nbtPlayer.setInteger("WITCForceTormentLevel", presetLevel);
      } else if(presetLevel == -2 && nbtPlayer.hasKey("WITCForceTormentLevel")) {
         nbtPlayer.removeTag("WITCForceTormentLevel");
      }

   }

   public static int getRandomTormentLevel(World world) {
      return world.rand.nextInt(6);
   }

   public static int getPlayerMustTorment(EntityPlayer player) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      return getPlayerMustTorment(nbtPlayer);
   }

   public static int getPlayerMustTorment(NBTTagCompound nbtPlayer) {
      return nbtPlayer.getInteger("WITCForceTorment");
   }

   public static void updatePlayerEffects(World world, EntityPlayer player, NBTTagCompound nbtPlayer, long time, long counter) {
      if(!world.isRemote) {
         boolean done = false;
         if(counter % 20L == 0L) {
            int mustTorment = getPlayerMustTorment(nbtPlayer);
            ItemGeneral var10000;
            if(mustTorment != 1 && mustTorment != 2) {
               if(mustTorment == 3) {
                  setPlayerMustTorment(nbtPlayer, 0, -2);
                  if(player.isRiding()) {
                     player.mountEntity((Entity)null);
                  }

                  WorldServer var18 = MinecraftServer.getServer().worldServers[0];
                  ChunkCoordinates var20 = player.getBedLocation(0);
                  byte var19 = 0;
                  if(var20 == null) {
                     var20 = var18.getSpawnPoint();
                  }

                  if(var20 != null) {
                     int var21 = 0;
                     int var22 = var20.posY;

                     while(!isSafeBlock(var18, var20.posX, var20.posY, var20.posZ) && var20.posY > 1 && var20.posY < 255) {
                        var20.posY = var22 + var21;
                        if(var22 - var21 > 1) {
                           var21 = -var21;
                        }

                        if(var21 >= 0) {
                           ++var21;
                        }
                     }

                     ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, player, 1.0D, 2.0D, 16);
                     var10000 = Witchery.Items.GENERIC;
                     ItemGeneral.teleportToLocation(player.worldObj, (double)var20.posX, (double)(var20.posY + 1), (double)var20.posZ, var19, player, true);
                     ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, player, 1.0D, 2.0D, 16);
                  }
               }
            } else {
               int overworld = mustTorment == 2?nbtPlayer.getInteger("WITCForceTormentLevel"):getRandomTormentLevel(world);
               setPlayerMustTorment(nbtPlayer, 0, -1);
               if(player.isRiding()) {
                  player.mountEntity((Entity)null);
               }

               ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, player, 1.0D, 2.0D, 16);
               int coords = 12 + overworld * 15;
               player.setPositionAndUpdate(8.0D, (double)coords, 8.0D);
               var10000 = Witchery.Items.GENERIC;
               ItemGeneral.travelToDimension(player, Config.instance().dimensionTormentID);
               player.setPositionAndUpdate(8.0D, (double)coords, 8.0D);
               ParticleEffect.PORTAL.send(SoundEffect.MOB_ENDERMEN_PORTAL, player, 1.0D, 2.0D, 16);
               WorldServer dimension = ServerUtil.getWorld(Config.instance().dimensionTormentID);
               byte mod = 8;
               byte origY = 8;

               for(int found = mod - 1; found <= mod + 1; ++found) {
                  for(int lot = origY - 1; lot <= origY + 1; ++lot) {
                     if(!dimension.isAirBlock(found, coords, lot)) {
                        dimension.setBlockToAir(found, coords, lot);
                     }

                     if(!dimension.isAirBlock(found, coords + 1, lot)) {
                        dimension.setBlockToAir(found, coords + 1, lot);
                     }
                  }
               }

               if(mustTorment == 2) {
                  boolean var23 = false;
                  if(player.worldObj.provider.dimensionId == Config.instance().dimensionTormentID) {
                     Iterator var25 = player.worldObj.loadedEntityList.iterator();

                     while(var25.hasNext()) {
                        Object obj = var25.next();
                        if(obj instanceof EntityLordOfTorment) {
                           EntityLordOfTorment lot1 = (EntityLordOfTorment)obj;
                           if(lot1.posY >= (double)(coords - 2) && lot1.posY <= (double)(coords + 6 - 2)) {
                              var23 = true;
                              break;
                           }
                        }
                     }
                  }

                  if(!var23 && dimension != null) {
                     EntityLordOfTorment var24 = new EntityLordOfTorment(dimension);
                     var24.setPositionAndRotation(9.0D, (double)(coords - 1), 36.0D, 0.0F, 0.0F);
                     var24.func_110163_bv();
                     var24.setHealth(var24.getMaxHealth() * 0.5F);
                     dimension.spawnEntityInWorld(var24);
                  }
               }
            }
         }
      }

   }

   private static boolean isSafeBlock(World world, int posX, int posY, int posZ) {
      boolean base = BlockUtil.isSolid(world, posX, posY, posZ);
      boolean air1 = !BlockUtil.isSolid(world, posX, posY + 1, posZ);
      boolean air2 = !BlockUtil.isSolid(world, posX, posY + 2, posZ);
      boolean isSafe = base && air1 && air2;
      return isSafe;
   }
}
