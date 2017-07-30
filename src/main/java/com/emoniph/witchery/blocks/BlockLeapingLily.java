package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.item.ItemLeapingLily;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class BlockLeapingLily extends BlockLilyPad {

   public BlockLeapingLily() {
      this.setHardness(0.0F);
      this.setLightLevel(0.4F);
      this.setStepSound(Block.soundTypeGrass);
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, ItemLeapingLily.class, blockName);
      return super.setBlockName(blockName);
   }

   protected boolean canPlaceBlockOn(Block block) {
      return block != null && block.getMaterial() != null && (block.getMaterial().isSolid() || block.getMaterial() == Material.water);
   }

   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      Material material = par1World.getBlock(par2, par3, par4).getMaterial();
      return super.canPlaceBlockAt(par1World, par2, par3, par4) && material != null && !material.isLiquid();
   }

   public boolean canBlockStay(World world, int posX, int posY, int posZ) {
      Material material = world.getBlock(posX, posY - 1, posZ).getMaterial();
      return material != null && (material.isSolid() || material.isLiquid()) && world.isAirBlock(posX, posY + 1, posZ);
   }

   public void onEntityCollidedWithBlock(World world, int posX, int posY, int posZ, Entity entity) {
      if(!world.isRemote && entity instanceof EntityLivingBase) {
         EntityLivingBase livingEntity = (EntityLivingBase)entity;
         if(!livingEntity.isPotionActive(Potion.moveSpeed)) {
            livingEntity.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 10, 0));
         }

         if(!livingEntity.isPotionActive(Potion.jump)) {
            livingEntity.addPotionEffect(new PotionEffect(Potion.jump.id, 10, 4));
         }
      }

   }
}
