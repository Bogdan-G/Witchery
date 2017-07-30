package com.emoniph.witchery.item;

import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class ItemEntityLocatorTexture extends TextureCompass {

   public ItemEntityLocatorTexture() {
      super("witchery:entitylocator");
   }

   public void updateCompass(World world, double playerX, double playerY, double playerZ, boolean p_94241_8_, boolean p_94241_9_) {
      if(!super.framesTextureData.isEmpty()) {
         double d3 = 0.0D;
         if(world != null && !p_94241_8_) {
            ChunkCoordinates i = world.getSpawnPoint();
            double d4 = (double)i.posX - playerX;
            double d5 = (double)i.posZ - playerY;
            playerZ %= 360.0D;
            d3 = -((playerZ - 90.0D) * 3.141592653589793D / 180.0D - Math.atan2(d5, d4));
            if(!world.provider.isSurfaceWorld()) {
               d3 = Math.random() * 3.141592653589793D * 2.0D;
            }
         }

         if(p_94241_9_) {
            super.currentAngle = d3;
         } else {
            double i1;
            for(i1 = d3 - super.currentAngle; i1 < -3.141592653589793D; i1 += 6.283185307179586D) {
               ;
            }

            while(i1 >= 3.141592653589793D) {
               i1 -= 6.283185307179586D;
            }

            if(i1 < -1.0D) {
               i1 = -1.0D;
            }

            if(i1 > 1.0D) {
               i1 = 1.0D;
            }

            super.angleDelta += i1 * 0.1D;
            super.angleDelta *= 0.8D;
            super.currentAngle += super.angleDelta;
         }

         int i2;
         for(i2 = (int)((super.currentAngle / 6.283185307179586D + 1.0D) * (double)super.framesTextureData.size()) % super.framesTextureData.size(); i2 < 0; i2 = (i2 + super.framesTextureData.size()) % super.framesTextureData.size()) {
            ;
         }

         if(i2 != super.frameCounter) {
            super.frameCounter = i2;
            TextureUtil.uploadTextureMipmap((int[][])((int[][])super.framesTextureData.get(super.frameCounter)), super.width, super.height, super.originX, super.originY, false, false);
         }
      }

   }
}
