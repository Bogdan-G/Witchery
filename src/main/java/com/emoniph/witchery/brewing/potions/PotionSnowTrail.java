package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.brewing.potions.PotionBase;
import com.emoniph.witchery.util.BlockActionCircle;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.Coord;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PotionSnowTrail extends PotionBase {

   public PotionSnowTrail(int id, int color) {
      super(id, color);
   }

   public boolean isReady(int duration, int amplifier) {
      return duration % 10 == 0;
   }

   public void performEffect(EntityLivingBase entity, int amplifier) {
      if(!entity.worldObj.isRemote) {
         for(int coord = 0; coord < 4; ++coord) {
            int i = MathHelper.floor_double(entity.posX + (double)((float)(coord % 2 * 2 - 1) * 0.25F));
            int j = MathHelper.floor_double(entity.posY);
            int k = MathHelper.floor_double(entity.posZ + (double)((float)(coord / 2 % 2 * 2 - 1) * 0.25F));
            if(entity.worldObj.getBlock(i, j, k).getMaterial() == Material.air) {
               float temp = entity.worldObj.getBiomeGenForCoords(i, k).getFloatTemperature(i, j, k);
               if(temp < 1.6F && Blocks.snow_layer.canPlaceBlockAt(entity.worldObj, i, j, k)) {
                  entity.worldObj.setBlock(i, j, k, Blocks.snow_layer);
               }
            }
         }

         if(entity instanceof EntitySnowman && entity.worldObj.rand.nextInt(20) == 0) {
            entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 3.0F, false);
            Coord var8 = new Coord(entity);
            createSnowCovering(entity.worldObj, var8.x, var8.y, var8.z, 8, (EntityPlayer)null);
            entity.setDead();
         }
      }

   }

   public static void createSnowCovering(World world, int x, int y, int z, int radius, EntityPlayer source) {
      if(BlockProtect.checkModsForBreakOK(world, x, y, z, source)) {
         (new BlockActionCircle() {
            public void onBlock(World world, int x, int y, int z) {
               boolean maxSearch = true;
               int i;
               int dy;
               Block block;
               if(world.isAirBlock(x, y, z)) {
                  for(i = 1; i < 8; ++i) {
                     dy = y - i;
                     block = world.getBlock(x, dy, z);
                     if(block.getMaterial() != Material.air) {
                        this.setBlockToSnow(world, x, dy + 1, z, block);
                        break;
                     }
                  }
               } else {
                  for(i = 1; i < 8; ++i) {
                     dy = y + i;
                     block = world.getBlock(x, dy, z);
                     if(block.getMaterial() == Material.air) {
                        Block blockBelow = world.getBlock(x, dy - 1, z);
                        this.setBlockToSnow(world, x, dy, z, blockBelow);
                        break;
                     }
                  }
               }

            }
            private void setBlockToSnow(World world, int x, int y, int z, Block blockBelow) {
               if(blockBelow.isOpaqueCube() || blockBelow.getMaterial() == Material.leaves) {
                  world.setBlock(x, y, z, Blocks.snow_layer);
               }

            }
         }).processFilledCircle(world, x, y, z, radius);
      }

   }
}
