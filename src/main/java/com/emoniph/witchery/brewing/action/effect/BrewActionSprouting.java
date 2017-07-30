package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.TimeUtil;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionSprouting extends BrewActionEffect {

   public BrewActionSprouting(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, EffectLevel effectLevel) {
      super(itemKey, namePart, powerCost, baseProbability, effectLevel);
   }

   protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers, ItemStack actionStack) {
      AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1));
      growBranch(x, y, z, world, side.ordinal(), 8 + 2 * modifiers.getStrength(), bounds);
   }

   protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
      targetEntity.addPotionEffect(new PotionEffect(Witchery.Potions.SPROUTING.id, modifiers.getModifiedDuration(TimeUtil.secsToTicks(15)), modifiers.getStrength()));
   }

   public static void growBranch(EntityLivingBase entity, int extent) {
      Coord coord = new Coord(entity);
      growBranch(coord.x, coord.y - 1, coord.z, entity.worldObj, ForgeDirection.UP.ordinal(), extent, entity.boundingBox.expand(0.5D, 0.5D, 0.5D));
   }

   public static void growBranch(int posX, int posY, int posZ, World world, int sideHit, int extent, AxisAlignedBB boundingBox) {
      if(!world.isRemote) {
         Block blockID = world.getBlock(posX, posY, posZ);
         int j1 = world.getBlockMetadata(posX, posY, posZ);
         Block logBlock;
         if(blockID != Blocks.log && blockID != Blocks.planks && blockID != Blocks.sapling && blockID != Blocks.leaves) {
            if(blockID != Witchery.Blocks.LOG && blockID != Witchery.Blocks.PLANKS && blockID != Witchery.Blocks.SAPLING && blockID != Witchery.Blocks.LEAVES) {
               logBlock = world.rand.nextInt(2) == 0?Blocks.log:Witchery.Blocks.LOG;
               j1 = world.rand.nextInt(Blocks.log == logBlock?4:3);
            } else {
               logBlock = Witchery.Blocks.LOG;
            }
         } else {
            logBlock = Blocks.log;
         }

         Object leavesBlock = Blocks.log == logBlock?Blocks.leaves:Witchery.Blocks.LEAVES;
         byte b0 = 0;
         j1 &= 3;
         switch(sideHit) {
         case 0:
         case 1:
            b0 = 0;
            break;
         case 2:
         case 3:
            b0 = 8;
            break;
         case 4:
         case 5:
            b0 = 4;
         }

         int meta = j1 | b0;
         ParticleEffect particleEffect = ParticleEffect.EXPLODE;
         int dx = sideHit == 5?1:(sideHit == 4?-1:0);
         int dy = sideHit == 0?-1:(sideHit == 1?1:0);
         int dz = sideHit == 3?1:(sideHit == 2?-1:0);
         int sproutExtent = extent;
         boolean isInitialBlockSolid = world.getBlock(posX, posY, posZ).getMaterial().isSolid();

         int i;
         int x;
         int z;
         int y;
         for(i = sideHit == 1 && !isInitialBlockSolid?0:1; i <= sproutExtent; ++i) {
            int axisalignedbb = posX + i * dx;
            int list1 = posY + i * dy;
            int iterator = posZ + i * dz;
            if(list1 >= 255 || !BlockUtil.setBlockIfReplaceable(world, axisalignedbb, list1, iterator, logBlock, meta)) {
               break;
            }

            x = dx == 0 && world.rand.nextInt(4) == 0?world.rand.nextInt(3) - 1:0;
            y = dy == 0 && x == 0 && world.rand.nextInt(4) == 0?world.rand.nextInt(3) - 1:0;
            z = dz == 0 && x == 0 && y == 0 && world.rand.nextInt(4) == 0?world.rand.nextInt(3) - 1:0;
            if(x != 0 || y != 0 || z != 0) {
               BlockUtil.setBlockIfReplaceable(world, axisalignedbb + x, list1 + y, iterator + z, (Block)leavesBlock, meta);
            }
         }

         if(sideHit == 1) {
            AxisAlignedBB var27 = boundingBox.expand(0.0D, 2.0D, 0.0D);
            List var29 = world.getEntitiesWithinAABB(EntityLivingBase.class, var27);
            if(var29 != null && !var29.isEmpty()) {
               Iterator var28 = var29.iterator();
               x = posX + i * dx;
               y = Math.min(posY + i * dy, 255);
               z = posZ + i * dz;

               while(var28.hasNext()) {
                  EntityLivingBase entitylivingbase = (EntityLivingBase)var28.next();
                  if(!world.getBlock(x, y + 1, z).getMaterial().isSolid() && !world.getBlock(x, y + 2, z).getMaterial().isSolid()) {
                     if(entitylivingbase instanceof EntityPlayer) {
                        entitylivingbase.setPositionAndUpdate(0.5D + (double)x, (double)(y + 2), 0.5D + (double)z);
                     } else {
                        entitylivingbase.setPositionAndUpdate(0.5D + (double)x, (double)(y + 2), 0.5D + (double)z);
                     }
                  }
               }
            }
         }

      }
   }
}
