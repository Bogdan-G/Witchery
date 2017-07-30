package com.emoniph.witchery.infusion.infusions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBarrier;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.EntityUtil;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class InfusionLight extends Infusion {

   private static final int BARRIER_RADIUS = 2;
   private static final int BARRIER_HEIGHT = 3;
   private static final boolean BARRIER_BLOCKS_PLAYERS = true;
   private static final int AGGRO_DROP_RADIUS = 20;
   protected static final int BARRIER_TICKS_TO_LIVE_ = 200;


   public InfusionLight(int infusionID) {
      super(infusionID);
   }

   public IIcon getPowerBarIcon(EntityPlayer player, int index) {
      return Blocks.snow.getIcon(0, 0);
   }

   public void onLeftClickEntity(ItemStack itemstack, World world, EntityPlayer player, Entity otherEntity) {
      if(!world.isRemote) {
         if(otherEntity instanceof EntityLivingBase) {
            EntityLivingBase otherLivingEntity = (EntityLivingBase)otherEntity;
            boolean UPSHIFT = true;
            int posX = (int)otherEntity.posX;
            int posY = (int)otherEntity.posY + 4;
            int posZ = (int)otherEntity.posZ;
            if(world.isAirBlock(posX, posY, posZ) && world.isAirBlock(posX, posY + 1, posZ) && world.isAirBlock(posX, posY + 2, posZ) && world.isAirBlock(posX + 1, posY, posZ) && world.isAirBlock(posX + 1, posY + 1, posZ) && world.isAirBlock(posX + 1, posY + 2, posZ) && world.isAirBlock(posX, posY, posZ + 1) && world.isAirBlock(posX, posY + 1, posZ + 1) && world.isAirBlock(posX, posY + 2, posZ + 1) && world.isAirBlock(posX - 1, posY, posZ) && world.isAirBlock(posX - 1, posY + 1, posZ) && world.isAirBlock(posX - 1, posY + 2, posZ) && world.isAirBlock(posX, posY, posZ - 1) && world.isAirBlock(posX, posY + 1, posZ - 1) && world.isAirBlock(posX, posY + 2, posZ - 1) && this.consumeCharges(world, player, 5, true)) {
               this.drawFilledCircle(world, posX, posZ, posY - 1, 2, (EntityPlayer)null);

               for(int y = posY; y < posY + 3; ++y) {
                  this.drawCircle(world, posX, posZ, y, 2, (EntityPlayer)null);
               }

               this.drawFilledCircle(world, posX, posZ, posY + 3, 2, (EntityPlayer)null);
               otherLivingEntity.setPositionAndUpdate((double)posX, (double)posY, (double)posZ);
            }
         }

      }
   }

   public void onUsingItemTick(ItemStack itemstack, World world, EntityPlayer player, int countdown) {
      int elapsedTicks;
      if(world.isRemote) {
         if(!player.isRiding()) {
            elapsedTicks = MathHelper.floor_double(player.posX);
            int var6 = MathHelper.floor_double(player.posY - 2.0D);
            int var7 = MathHelper.floor_double(player.posZ);
            if(world.getBlock(elapsedTicks, var6, var7) != Blocks.ice) {
               if(player.onGround) {
                  if(!player.isInWater()) {
                     player.motionX *= 1.6500000476837158D;
                     player.motionZ *= 1.6500000476837158D;
                  } else {
                     player.motionX *= 1.100000023841858D;
                     player.motionZ *= 1.100000023841858D;
                  }
               }
            } else {
               player.motionX *= 1.100000023841858D;
               player.motionZ *= 1.100000023841858D;
            }
         }

      } else {
         elapsedTicks = this.getMaxItemUseDuration(itemstack) - countdown;
         if(elapsedTicks % 30 == 0 && elapsedTicks > 19) {
            if(this.consumeCharges(world, player, 1, true)) {
               this.bendLightAroundPlayer(world, player, true);
            } else {
               this.bendLightAroundPlayer(world, player, false);
            }
         }

      }
   }

   protected void bendLightAroundPlayer(World world, EntityPlayer player, boolean active) {
      if(active) {
         player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 30, 0, true));
         boolean r = true;
         AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(player.posX - 20.0D, player.posY, player.posZ - 20.0D, player.posX + 20.0D, player.posY + 2.0D, player.posZ + 20.0D);
         Iterator i$ = world.getEntitiesWithinAABB(EntityLiving.class, bounds).iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            EntityLiving entity = (EntityLiving)obj;
            if(entity.getAttackTarget() == player && Coord.distance(entity.posX, entity.posY, entity.posZ, player.posX, player.posY, player.posZ) <= 20.0D) {
               EntityUtil.dropAttackTarget(entity);
            }
         }
      } else {
         player.removePotionEffect(Potion.invisibility.id);
      }

   }

   public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer player, int countdown) {
      if(!world.isRemote) {
         this.bendLightAroundPlayer(world, player, false);
         int elapsedTicks = this.getMaxItemUseDuration(itemstack) - countdown;
         if(elapsedTicks < 20 && player.isSneaking()) {
            MovingObjectPosition hitMOP = InfusionOtherwhere.doCustomRayTrace(world, player, true, 16.0D);
            if(hitMOP != null) {
               int dx;
               int dy;
               int dz;
               switch(InfusionLight.NamelessClass1997579753.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[hitMOP.typeOfHit.ordinal()]) {
               case 1:
                  if(hitMOP.entityHit instanceof EntityLivingBase) {
                     EntityLivingBase var17 = (EntityLivingBase)hitMOP.entityHit;
                     if(this.consumeCharges(world, player, 3, true)) {
                        dx = (int)var17.posX;
                        dy = (int)var17.posY;
                        dz = (int)var17.posZ;
                        this.drawFilledCircle(world, dx, dz, dy - 1, 1, player);

                        for(int var18 = dy; var18 < dy + 3; ++var18) {
                           this.drawCircle(world, dx, dz, var18, 2, player);
                        }

                        this.drawFilledCircle(world, dx, dz, dy + 3, 2, player);
                     }
                  }
                  break;
               case 2:
                  if(BlockSide.TOP.isEqual(hitMOP.sideHit) && this.consumeCharges(world, player, 3, true)) {
                     placeBarrierShield(world, player, hitMOP);
                  } else if(!BlockSide.TOP.isEqual(hitMOP.sideHit) && !BlockSide.TOP.isEqual(hitMOP.sideHit) && this.consumeCharges(world, player, 3, true)) {
                     boolean b0 = false;
                     switch(hitMOP.sideHit) {
                     case 0:
                     case 1:
                        b0 = false;
                        break;
                     case 2:
                     case 3:
                        b0 = true;
                        break;
                     case 4:
                     case 5:
                        b0 = true;
                     }

                     dx = hitMOP.sideHit == 5?1:(hitMOP.sideHit == 4?-1:0);
                     dy = hitMOP.sideHit == 0?-1:(hitMOP.sideHit == 1?1:0);
                     dz = hitMOP.sideHit == 3?1:(hitMOP.sideHit == 2?-1:0);
                     boolean sproutExtent = true;
                     boolean isInitialBlockSolid = world.getBlock(hitMOP.blockX, hitMOP.blockY, hitMOP.blockZ).getMaterial().isSolid();

                     for(int i = hitMOP.sideHit == 1 && !isInitialBlockSolid?0:1; i < 16; ++i) {
                        int x = hitMOP.blockX + i * dx;
                        int y = hitMOP.blockY + i * dy;
                        int z = hitMOP.blockZ + i * dz;
                        if(!setBlockIfNotSolid(world, x, y, z)) {
                           break;
                        }
                     }
                  }
               case 3:
               }
            }
         }

      }
   }

   public static void placeBarrierShield(World world, EntityPlayer player, MovingObjectPosition hitMOP) {
      double f1 = (double)MathHelper.cos(-player.rotationYaw * 0.017453292F - 3.1415927F);
      double f2 = (double)MathHelper.sin(-player.rotationYaw * 0.017453292F - 3.1415927F);
      Vec3 loc = Vec3.createVectorHelper(f2, 0.0D, f1);
      Material material = world.getBlock(hitMOP.blockX, hitMOP.blockY, hitMOP.blockZ).getMaterial();
      byte yPlus = 1;
      if(material != null && !material.isSolid()) {
         yPlus = 0;
      }

      drawBarrierBlockColumn(world, player, hitMOP.blockX, hitMOP.blockY + yPlus, hitMOP.blockZ, 3);
      loc.rotateAroundY((float)Math.toRadians(90.0D));
      int newX = MathHelper.floor_double((double)hitMOP.blockX + 0.5D + loc.xCoord * 1.0D);
      int newZ = MathHelper.floor_double((double)hitMOP.blockZ + 0.5D + loc.zCoord * 1.0D);
      drawBarrierBlockColumn(world, player, newX, hitMOP.blockY + yPlus, newZ, 3);
      loc.rotateAroundY((float)Math.toRadians(180.0D));
      newX = MathHelper.floor_double((double)hitMOP.blockX + 0.5D + loc.xCoord * 1.0D);
      newZ = MathHelper.floor_double((double)hitMOP.blockZ + 0.5D + loc.zCoord * 1.0D);
      drawBarrierBlockColumn(world, player, newX, hitMOP.blockY + yPlus, newZ, 3);
   }

   private static boolean setBlockIfNotSolid(World world, int x, int y, int z) {
      if(world.getBlock(x, y, z).getMaterial().isReplaceable()) {
         BlockBarrier.setBlock(world, x, y, z, 200, true, (EntityPlayer)null);
         return true;
      } else {
         return false;
      }
   }

   private static void drawBarrierBlockColumn(World world, EntityPlayer player, int posX, int posY, int posZ, int height) {
      for(int offsetPosY = posY; offsetPosY < posY + height; ++offsetPosY) {
         Material material = world.getBlock(posX, offsetPosY, posZ).getMaterial();
         Block blockID = world.getBlock(posX, offsetPosY, posZ);
         if(material.isReplaceable() || blockID == Witchery.Blocks.BARRIER) {
            BlockBarrier.setBlock(world, posX, offsetPosY, posZ, 200, true, player);
         }
      }

   }

   protected void drawCircle(World world, int x0, int z0, int y, int radius, EntityPlayer player) {
      int x = radius;
      int z = 0;
      int radiusError = 1 - radius;

      while(x >= z) {
         this.drawPixel(world, x + x0, z + z0, y, player);
         this.drawPixel(world, z + x0, x + z0, y, player);
         this.drawPixel(world, -x + x0, z + z0, y, player);
         this.drawPixel(world, -z + x0, x + z0, y, player);
         this.drawPixel(world, -x + x0, -z + z0, y, player);
         this.drawPixel(world, -z + x0, -x + z0, y, player);
         this.drawPixel(world, x + x0, -z + z0, y, player);
         this.drawPixel(world, z + x0, -x + z0, y, player);
         ++z;
         if(radiusError < 0) {
            radiusError += 2 * z + 1;
         } else {
            --x;
            radiusError += 2 * (z - x + 1);
         }
      }

   }

   protected void drawFilledCircle(World world, int x0, int z0, int y, int radius, EntityPlayer player) {
      int x = radius;
      int z = 0;
      int radiusError = 1 - radius;

      while(x >= z) {
         this.drawLine(world, -x + x0, x + x0, z + z0, y, player);
         this.drawLine(world, -z + x0, z + x0, x + z0, y, player);
         this.drawLine(world, -x + x0, x + x0, -z + z0, y, player);
         this.drawLine(world, -z + x0, z + x0, -x + z0, y, player);
         ++z;
         if(radiusError < 0) {
            radiusError += 2 * z + 1;
         } else {
            --x;
            radiusError += 2 * (z - x + 1);
         }
      }

   }

   protected void drawLine(World world, int x1, int x2, int z, int y, EntityPlayer player) {
      for(int x = x1; x <= x2; ++x) {
         this.drawPixel(world, x, z, y, player);
      }

   }

   protected void drawPixel(World world, int x, int z, int y, EntityPlayer player) {
      Block blockID = world.getBlock(x, y, z);
      Material material = world.getBlock(x, y, z).getMaterial();
      if(!material.isSolid() || blockID == Witchery.Blocks.BARRIER || material == Material.air) {
         BlockBarrier.setBlock(world, x, y, z, 200, true, player);
      }

   }

   // $FF: synthetic class
   static class NamelessClass1997579753 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType = new int[MovingObjectType.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.ENTITY.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.BLOCK.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.MISS.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
