package com.emoniph.witchery.item.brew;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class BrewSolidifySpirit extends ItemGeneral.Brew {

   protected Block replacementBlock;


   public BrewSolidifySpirit(int subItemID, String unlocalisedName, Block block) {
      super(subItemID, unlocalisedName);
      this.replacementBlock = block;
   }

   public boolean isSolidifier() {
      return true;
   }

   public ItemGeneral.Brew.BrewResult onImpact(World world, EntityLivingBase thrower, MovingObjectPosition mop, boolean enhanced, double brewX, double brewY, double brewZ, AxisAlignedBB brewBounds) {
      if(mop.typeOfHit == MovingObjectType.ENTITY) {
         return ItemGeneral.Brew.BrewResult.DROP_ITEM;
      } else {
         Block blockHit = BlockUtil.getBlock(world, mop);
         int x = mop.blockX;
         int y = mop.blockY;
         int z = mop.blockZ;
         if(blockHit != Witchery.Blocks.HOLLOW_TEARS) {
            switch(mop.sideHit) {
            case 0:
               --y;
               break;
            case 1:
               ++y;
               break;
            case 2:
               --z;
               break;
            case 3:
               ++z;
               break;
            case 4:
               --x;
               break;
            case 5:
               ++x;
            }

            blockHit = BlockUtil.getBlock(world, x, y, z);
            if(blockHit != Witchery.Blocks.HOLLOW_TEARS) {
               return ItemGeneral.Brew.BrewResult.DROP_ITEM;
            }
         }

         BrewSolidifySpirit.SpreadEffect.spread(world, x, y, z, 64, new BrewSolidifySpirit.SpreadEffect(new Block[]{Witchery.Blocks.HOLLOW_TEARS}) {
            public boolean doEffect(World world, int posX, int posY, int posZ, Block block) {
               ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, world, 0.5D + (double)posX, 1.5D + (double)posY, 0.5D + (double)posZ, 2.0D, 2.0D, 16);
               if(BrewSolidifySpirit.this.replacementBlock == null) {
                  world.setBlockToAir(posX, posY, posZ);
                  Block blockBelow = BlockUtil.getBlock(world, posX, posY - 1, posZ);
                  if(blockBelow != null && BlockProtect.canBreak(blockBelow, world)) {
                     world.setBlockToAir(posX, posY - 1, posZ);
                  }
               } else {
                  BlockUtil.setBlock(world, posX, posY, posZ, BrewSolidifySpirit.this.replacementBlock, 0, 3);
               }

               return true;
            }
         });
         return ItemGeneral.Brew.BrewResult.SHOW_EFFECT;
      }
   }

   public abstract static class SpreadEffect {

      protected Block[] blocks;


      public SpreadEffect(Block ... blocksToSpreadOver) {
         this.blocks = blocksToSpreadOver;
      }

      public abstract boolean doEffect(World var1, int var2, int var3, int var4, Block var5);

      public static void spread(World world, int x0, int y0, int z0, int range, BrewSolidifySpirit.SpreadEffect effect) {
         spread(world, x0, y0, z0, x0, y0, z0, range, effect);
      }

      private static void spread(World world, int x, int y, int z, int x0, int y0, int z0, int range, BrewSolidifySpirit.SpreadEffect effect) {
         if(Math.abs(x0 - x) < range && Math.abs(y0 - y) < range && Math.abs(z0 - z) < range) {
            if(checkEffect(world, x + 1, y, z, effect)) {
               spread(world, x + 1, y, z, x0, y0, z0, range, effect);
            }

            if(checkEffect(world, x - 1, y, z, effect)) {
               spread(world, x - 1, y, z, x0, y0, z0, range, effect);
            }

            if(checkEffect(world, x, y, z + 1, effect)) {
               spread(world, x, y, z + 1, x0, y0, z0, range, effect);
            }

            if(checkEffect(world, x, y, z - 1, effect)) {
               spread(world, x, y, z - 1, x0, y0, z0, range, effect);
            }

            if(checkEffect(world, x, y + 1, z, effect)) {
               spread(world, x, y + 1, z, x0, y0, z0, range, effect);
            }

            if(checkEffect(world, x, y - 1, z, effect)) {
               spread(world, x, y - 1, z, x0, y0, z0, range, effect);
            }

         }
      }

      private static boolean checkEffect(World world, int x, int y, int z, BrewSolidifySpirit.SpreadEffect effect) {
         boolean continueSpread = false;
         Block foundBlock = BlockUtil.getBlock(world, x, y, z);
         if(foundBlock == null) {
            return continueSpread;
         } else {
            Block[] arr$ = effect.blocks;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               Block block = arr$[i$];
               if(foundBlock == block) {
                  continueSpread = effect.doEffect(world, x, y, z, block);
                  break;
               }
            }

            return continueSpread;
         }
      }
   }
}
