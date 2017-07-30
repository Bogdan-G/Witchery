package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.Dispersal;
import com.emoniph.witchery.brewing.EntityDroplet;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.RitualStatus;
import com.emoniph.witchery.brewing.TileEntityBrewFluid;
import com.emoniph.witchery.util.BlockPosition;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.EntityUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class DispersalLiquid extends Dispersal {

   public void onImpactSplashPotion(World world, NBTTagCompound nbtBrew, MovingObjectPosition mop, ModifiersImpact modifiers) {
      Coord coord = new Coord(mop, modifiers.impactPosition, true);
      boolean replaceable = BlockUtil.isReplaceableBlock(world, coord.x, coord.y, coord.z, modifiers.thrower);
      if(replaceable) {
         coord.setBlock(world, Witchery.Blocks.BREW_LIQUID);
         TileEntityBrewFluid gas = (TileEntityBrewFluid)coord.getTileEntity(world, TileEntityBrewFluid.class);
         if(gas != null) {
            gas.initalise(modifiers, nbtBrew);
         }
      }

   }

   public String getUnlocalizedName() {
      return "witchery:brew.dispersal.liquid";
   }

   public RitualStatus onUpdateRitual(World world, int x, int y, int z, NBTTagCompound nbtBrew, ModifiersRitual modifiers, ModifiersImpact impactModifiers) {
      BlockPosition target = modifiers.getTarget();
      World targetWorld = target.getWorld(MinecraftServer.getServer());
      int radius = 16 + 8 * impactModifiers.extent;
      int halfQuantity = radius / 4;
      boolean height = true;
      double RSQ = (double)(radius * radius);
      int i = 0;

      for(int quantity = halfQuantity + world.rand.nextInt(halfQuantity); i < quantity; ++i) {
         int ny = 100 + world.rand.nextInt(20);
         int nx = x - radius + world.rand.nextInt(2 * radius);
         int nz = z - radius + world.rand.nextInt(2 * radius);
         if(Coord.distanceSq((double)x, (double)y, (double)z, (double)nx, (double)y, (double)nz) <= RSQ) {
            EntityUtil.spawnEntityInWorld(targetWorld, new EntityDroplet(targetWorld, (double)nx, (double)ny, (double)nz, nbtBrew));
         }
      }

      return modifiers.pulses < 10 + impactModifiers.lifetime * 5?RitualStatus.ONGOING:RitualStatus.COMPLETE;
   }
}
