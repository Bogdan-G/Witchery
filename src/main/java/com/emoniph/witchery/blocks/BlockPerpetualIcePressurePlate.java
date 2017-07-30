package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockPressurePlate.Sensitivity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockPerpetualIcePressurePlate extends BlockPressurePlate {

   public BlockPerpetualIcePressurePlate(Material material) {
      super(material == Material.ice?"ice":"snow", material, Sensitivity.everything);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      if(material == Material.ice) {
         this.setLightOpacity(3);
         this.setHardness(2.0F);
         this.setResistance(5.0F);
      } else {
         this.setHardness(0.2F);
         this.setStepSound(Block.soundTypeSnow);
      }

   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   @SideOnly(Side.CLIENT)
   public int getRenderBlockPass() {
      return super.blockMaterial == Material.ice?1:0;
   }

   protected int func_150065_e(World world, int x, int y, int z) {
      if(super.blockMaterial == Material.ice) {
         List list = world.getEntitiesWithinAABB(EntityLivingBase.class, this.func_150061_a(x, y, z));
         Iterator i$ = list.iterator();

         ItemStack footwear;
         do {
            if(!i$.hasNext()) {
               return 0;
            }

            EntityLivingBase entity = (EntityLivingBase)i$.next();
            footwear = entity.getEquipmentInSlot(1);
         } while(footwear == null || footwear.getItem() != Witchery.Items.ICY_SLIPPERS);

         return 15;
      } else {
         return super.func_150065_e(world, x, y, z);
      }
   }

   public boolean canPlaceBlockAt(World world, int x, int y, int z) {
      return super.canPlaceBlockAt(world, x, y, z) || world.getBlock(x, y - 1, z) == Witchery.Blocks.PERPETUAL_ICE_FENCE;
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      if(!this.canPlaceBlockAt(world, x, y, z)) {
         this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
         world.setBlockToAir(x, y, z);
      }

   }
}
