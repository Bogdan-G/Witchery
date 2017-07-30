package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.Coord;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionRaiseLand extends BrewActionEffect {

   public BrewActionRaiseLand(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, EffectLevel effectLevel) {
      super(itemKey, namePart, powerCost, new Probability(1.0D), effectLevel);
   }

   protected void doApplyRitualToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersRitual ritualModifiers, ModifiersEffect modifiers, ItemStack stack) {
      int r = (modifiers.getStrength() + 1) * 2;
      int rsq = r * r;

      for(int dx = -r; dx <= r; ++dx) {
         for(int dz = -r; dz <= r; ++dz) {
            if(dx * dx + dz * dz < rsq) {
               int nx = x + dx;
               int nz = z + dz;
               this.doApplyToBlock(world, nx, y, nz, ForgeDirection.UP, 1, modifiers, stack);
            }
         }
      }

   }

   protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {
      Coord coord = new Coord(targetEntity);
      this.doApplyToBlock(world, coord.x, coord.y - 1, coord.z, ForgeDirection.UP, 1, modifiers, stack);
   }

   protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
      while(!world.isAirBlock(x, y + 1, z) && y < 255) {
         ++y;
      }

      if(BlockProtect.canBreak(x, y, z, world) && BlockProtect.checkModsForBreakOK(world, x, y, z, modifiers.caster)) {
         int height = (modifiers.getStrength() + 1) * (modifiers.ritualised?2:3);
         if(!world.isRemote) {
            for(int bounds = 0; bounds < height; ++bounds) {
               int list = y - bounds;
               Block i$ = world.getBlock(x, list, z);
               int entity = world.getBlockMetadata(x, list, z);
               world.setBlockToAir(x, list, z);
               world.setBlock(x, list + height, z, i$, entity, 3);
            }
         }

         AxisAlignedBB var15 = AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 2), (double)(z + 1));
         List var14 = world.getEntitiesWithinAABB(EntityLivingBase.class, var15);
         Iterator var16 = var14.iterator();

         while(var16.hasNext()) {
            EntityLivingBase var17 = (EntityLivingBase)var16.next();
            if(var17 instanceof EntityPlayer) {
               var17.setPositionAndUpdate(0.5D + (double)x, (double)(y + height + 1), 0.5D + (double)z);
            } else {
               var17.setPositionAndUpdate(0.5D + (double)x, (double)(y + height + 1), 0.5D + (double)z);
            }
         }
      }

   }
}
