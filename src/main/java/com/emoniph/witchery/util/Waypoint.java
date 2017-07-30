package com.emoniph.witchery.util;

import com.emoniph.witchery.Witchery;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class Waypoint {

   public final boolean valid;
   public final double X;
   public final double Y;
   public final double Z;
   public final double D;


   public Waypoint(World world, double homeX, double homeY, double homeZ) {
      this.X = homeX;
      this.Y = homeY;
      this.Z = homeZ;
      this.D = (double)world.provider.dimensionId;
      this.valid = true;
   }

   public Waypoint(World world, ChunkPosition pos) {
      this.X = (double)pos.chunkPosX;
      this.Y = (double)pos.chunkPosY;
      this.Z = (double)pos.chunkPosZ;
      this.D = (double)world.provider.dimensionId;
      this.valid = true;
   }

   public Waypoint(World world, ItemStack stack, double homeX, double homeY, double homeZ) {
      if(Witchery.Items.GENERIC.itemWaystoneBound.isMatch(stack)) {
         NBTTagCompound entity = stack.getTagCompound();
         int x = entity.getInteger("PosX");
         int z = entity.getInteger("PosZ");
         if(world.getChunkFromBlockCoords(x, z).isChunkLoaded) {
            this.X = (double)x + 0.5D;
            this.Y = (double)entity.getInteger("PosY") + 1.5D;
            this.Z = (double)z + 0.5D;
            this.D = (double)entity.getInteger("PosD");
            this.valid = true;
         } else {
            this.X = homeX;
            this.Y = homeY;
            this.Z = homeZ;
            this.D = (double)world.provider.dimensionId;
            this.valid = false;
         }
      } else {
         EntityLivingBase entity1;
         if(Witchery.Items.GENERIC.itemWaystonePlayerBound.isMatch(stack)) {
            entity1 = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, stack, Integer.valueOf(1));
            if(entity1 != null) {
               this.X = entity1.posX;
               this.Y = entity1.posY + 1.0D;
               this.Z = entity1.posZ;
               this.D = (double)entity1.dimension;
               this.valid = true;
            } else {
               this.X = homeX;
               this.Y = homeY;
               this.Z = homeZ;
               this.D = (double)world.provider.dimensionId;
               this.valid = false;
            }
         } else if(stack != null && stack.getItem() == Witchery.Items.TAGLOCK_KIT) {
            entity1 = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, stack, Integer.valueOf(1));
            if(entity1 != null) {
               this.X = entity1.posX;
               this.Y = entity1.posY + 1.0D;
               this.Z = entity1.posZ;
               this.D = (double)entity1.dimension;
               this.valid = true;
            } else {
               this.X = homeX;
               this.Y = homeY;
               this.Z = homeZ;
               this.D = (double)world.provider.dimensionId;
               this.valid = false;
            }
         } else {
            this.X = homeX;
            this.Y = homeY;
            this.Z = homeZ;
            this.D = (double)world.provider.dimensionId;
            this.valid = false;
         }
      }

   }
}
