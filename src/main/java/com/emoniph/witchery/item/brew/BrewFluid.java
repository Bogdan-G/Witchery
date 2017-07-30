package com.emoniph.witchery.item.brew;

import com.emoniph.witchery.item.ItemGeneral;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BrewFluid extends ItemGeneral.Brew {

   protected final Fluid fluid;


   public BrewFluid(int damageValue, String unlocalisedName, Fluid fluid) {
      super(damageValue, unlocalisedName);
      this.fluid = fluid;
   }

   public ItemGeneral.Brew.BrewResult onImpact(World world, EntityLivingBase thrower, MovingObjectPosition mop, boolean enhanced, double brewX, double brewY, double brewZ, AxisAlignedBB brewBounds) {
      switch(BrewFluid.NamelessClass84047679.$SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[mop.typeOfHit.ordinal()]) {
      case 1:
         this.depositLiquid(world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, enhanced);
         break;
      case 2:
         int x = MathHelper.floor_double(mop.entityHit.posX);
         int y = MathHelper.floor_double(mop.entityHit.posY);
         int z = MathHelper.floor_double(mop.entityHit.posZ);
         this.depositLiquid(world, x, y, z, -1, enhanced);
      case 3:
      }

      return ItemGeneral.Brew.BrewResult.SHOW_EFFECT;
   }

   public void depositLiquid(World world, int posX, int posY, int posZ, int side, boolean enhanced) {
      int x = posX + (side == 4?-1:(side == 5?1:0));
      int z = posZ + (side == 2?-1:(side == 3?1:0));
      int y = posY + (side == 0?-1:(side == 1?1:0));
      if(side == 1 && !world.getBlock(x, posY, z).getMaterial().isSolid()) {
         --y;
      }

      setBlockIfNotSolid(world, x, y, z, this.fluid.getBlock());
   }

   // $FF: synthetic class
   static class NamelessClass84047679 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType = new int[MovingObjectType.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.BLOCK.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$MovingObjectPosition$MovingObjectType[MovingObjectType.ENTITY.ordinal()] = 2;
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
