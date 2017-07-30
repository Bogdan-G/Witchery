package com.emoniph.witchery.item;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.blocks.BlockWitchCrop;
import com.emoniph.witchery.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class ItemWitchSeeds extends ItemSeeds {

   private final boolean waterPlant;


   public ItemWitchSeeds(BlockWitchCrop plantedBlock, ItemStack cropItemStack, Block soilBlock, boolean waterPlant) {
      super(plantedBlock, soilBlock);
      this.waterPlant = waterPlant;
      this.setMaxDamage(0);
      this.setMaxStackSize(64);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      plantedBlock.setSeedItem(new ItemStack(this));
      plantedBlock.setCropItem(cropItemStack);
   }

   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      return super.setUnlocalizedName(itemName);
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if(this.waterPlant) {
         MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, true);
         if(mop != null && mop.typeOfHit == MovingObjectType.BLOCK && mop.sideHit == 1) {
            float f = (float)mop.hitVec.xCoord - (float)mop.blockX;
            float f1 = (float)mop.hitVec.yCoord - (float)mop.blockY;
            float f2 = (float)mop.hitVec.zCoord - (float)mop.blockZ;
            stack.tryPlaceItemIntoWorld(player, world, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, f, f1, f2);
         }
      }

      return super.onItemRightClick(stack, world, player);
   }

   public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
      return this.waterPlant?EnumPlantType.Water:super.getPlantType(world, x, y, z);
   }
}
