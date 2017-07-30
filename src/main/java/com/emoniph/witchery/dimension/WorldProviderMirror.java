package com.emoniph.witchery.dimension;

import com.emoniph.witchery.dimension.WorldChunkManagerMirror;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldProviderMirror extends WorldProvider {

   public WorldProviderMirror() {
      super.hasNoSky = true;
   }

   public int getActualHeight() {
      return Config.instance().shrinkMirrorWorld?this.getHeight():super.getActualHeight();
   }

   public String getDimensionName() {
      return "Mirror";
   }

   public IChunkProvider createChunkGenerator() {
      return new WorldChunkManagerMirror(super.worldObj);
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
      return new ChunkCoordinates(4, 9, 4);
   }

   @SideOnly(Side.CLIENT)
   public boolean isSkyColored() {
      return false;
   }

   protected void generateLightBrightnessTable() {
      float f = 0.1F;

      for(int i = 0; i <= 15; ++i) {
         float f1 = 1.0F - (float)i / 15.0F;
         super.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
      }

   }

   @SideOnly(Side.CLIENT)
   public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
      return Vec3.createVectorHelper(0.0D, 0.03D, 0.1D);
   }

   @SideOnly(Side.CLIENT)
   public boolean doesXZShowFog(int p_76568_1_, int p_76568_2_) {
      return true;
   }

   public float calculateCelestialAngle(long par1, float par3) {
      return 0.5F;
   }

   public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
      return super.getSkyColor(cameraEntity, partialTicks);
   }

   private static boolean isSafeBlock(World world, int posX, int posY, int posZ) {
      boolean base = BlockUtil.isSolid(world, posX, posY, posZ);
      boolean air1 = !BlockUtil.isSolid(world, posX, posY + 1, posZ);
      boolean air2 = !BlockUtil.isSolid(world, posX, posY + 2, posZ);
      boolean isSafe = base && air1 && air2;
      return isSafe;
   }

   public float getSunBrightnessFactor(float par1) {
      return 0.0F;
   }

   public boolean canDoRainSnowIce(Chunk chunk) {
      return false;
   }
}
