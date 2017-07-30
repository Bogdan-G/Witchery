package com.emoniph.witchery.brewing.action.effect;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevel;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.action.BrewActionEffect;
import com.emoniph.witchery.util.BlockActionCircle;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionBlight extends BrewActionEffect {

   public BrewActionBlight(BrewItemKey itemKey, BrewNamePart namePart, AltarPower powerCost, Probability baseProbability, EffectLevel effectLevel) {
      super(itemKey, namePart, powerCost, baseProbability, effectLevel);
   }

   protected void doApplyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, final ModifiersEffect modifiers, ItemStack stack) {
      if(BlockUtil.isReplaceableBlock(world, x, y, z)) {
         --y;
      }

      (new BlockActionCircle() {
         public void onBlock(World world, int x, int y, int z) {
            if(BlockProtect.checkModsForBreakOK(world, x, y, z, modifiers.caster)) {
               Block blockID = world.getBlock(x, y, z);
               Block blockBelowID = world.getBlock(x, y - 1, z);
               if(blockID == Blocks.tallgrass) {
                  world.setBlockToAir(x, y, z);
                  BrewActionBlight.this.blightGround(world, x, y - 1, z, blockBelowID);
               } else if(blockID != Blocks.red_flower && blockID != Blocks.yellow_flower && blockID != Blocks.carrots && blockID != Blocks.wheat && blockID != Blocks.potatoes && blockID != Blocks.pumpkin_stem && blockID != Blocks.melon_stem && blockID != Blocks.melon_block && blockID != Blocks.pumpkin && blockID != Blocks.double_plant) {
                  if(blockID == Blocks.farmland) {
                     world.setBlock(x, y, z, Blocks.sand);
                  } else if(blockID.getMaterial().isSolid()) {
                     BrewActionBlight.this.blightGround(world, x, y, z, blockID);
                  } else if(blockBelowID.getMaterial().isSolid()) {
                     BrewActionBlight.this.blightGround(world, x, y - 1, z, blockBelowID);
                  }
               } else {
                  world.setBlock(x, y, z, Blocks.deadbush);
                  BrewActionBlight.this.blightGround(world, x, y - 1, z, blockBelowID);
               }
            }

         }
      }).processFilledCircle(world, x, y + 1, z, radius);
   }

   protected void doApplyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack actionStack) {
      if(targetEntity instanceof EntityVillager && world.rand.nextInt(10 - modifiers.getStrength() * 2) == 0) {
         EntityZombie entityzombie1 = new EntityZombie(world);
         entityzombie1.copyLocationAndAnglesFrom(targetEntity);
         world.removeEntity(targetEntity);
         entityzombie1.onSpawnWithEgg((IEntityLivingData)null);
         entityzombie1.setVillager(true);
         if(targetEntity.isChild()) {
            entityzombie1.setChild(true);
         }

         world.spawnEntityInWorld(entityzombie1);
         world.playAuxSFXAtEntity((EntityPlayer)null, 1016, (int)entityzombie1.posX, (int)entityzombie1.posY, (int)entityzombie1.posZ, 0);
      } else if(targetEntity instanceof EntityCow && world.rand.nextInt(20 - modifiers.getStrength() * 3) == 0) {
         EntityMooshroom entityzombie = new EntityMooshroom(world);
         entityzombie.copyLocationAndAnglesFrom(targetEntity);
         world.removeEntity(targetEntity);
         entityzombie.onSpawnWithEgg((IEntityLivingData)null);
         world.spawnEntityInWorld(entityzombie);
         world.playAuxSFXAtEntity((EntityPlayer)null, 1016, (int)entityzombie.posX, (int)entityzombie.posY, (int)entityzombie.posZ, 0);
      } else if(targetEntity instanceof EntityAnimal && world.rand.nextInt(modifiers.getStrength() > 1?2:3) == 0) {
         targetEntity.attackEntityFrom(DamageSource.magic, 20.0F);
      }

   }

   public void blightGround(World world, int posX, int posY, int posZ, Block blockBelowID) {
      if(blockBelowID == Blocks.dirt || blockBelowID == Blocks.grass || blockBelowID == Blocks.mycelium || blockBelowID == Blocks.farmland) {
         int rand = world.rand.nextInt(5);
         if(rand == 0) {
            world.setBlock(posX, posY, posZ, Blocks.sand);
         } else if(rand == 1) {
            world.setBlock(posX, posY, posZ, Blocks.dirt);
         }
      }

   }
}
