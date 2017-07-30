package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockDisease extends BlockFluidClassic {

   @SideOnly(Side.CLIENT)
   protected IIcon[] icons;


   public BlockDisease(Fluid fluid) {
      super(fluid, Material.snow);
      super.quantaPerBlock = 1;
      this.setHardness(100.0F);
      this.setLightOpacity(1);
   }

   public int getMaxRenderHeightMeta() {
      return 16;
   }

   public void updateTick(World world, int x, int y, int z, Random rand) {
      super.updateTick(world, x, y, z, rand);
      int chance = Config.instance().diseaseRemovalChance;
      if(chance > 0) {
         if(world.rand.nextInt(chance) == 0) {
            world.setBlockToAir(x, y, z);
         }

         world.scheduleBlockUpdate(x, y, z, this, super.tickRate);
      }

   }

   public Block setBlockName(String blockName) {
      BlockUtil.registerBlock(this, blockName);
      return super.setBlockName(blockName);
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      return side != 0 && side != 1?this.icons[1]:this.icons[0];
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.icons = new IIcon[]{iconRegister.registerIcon(this.getTextureName() + "_still"), iconRegister.registerIcon(this.getTextureName() + "_flow")};
      if(super.stack != null && super.stack.getFluid() != null) {
         super.stack.getFluid().setIcons(this.icons[0], this.icons[1]);
      }

   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote && entity != null && entity instanceof EntityLivingBase) {
         EntityLivingBase livingEntity = (EntityLivingBase)entity;
         if(!CreatureUtil.isImmuneToDisease(livingEntity) && !livingEntity.isPotionActive(Witchery.Potions.DISEASED) && world.rand.nextInt(3) == 0) {
            livingEntity.addPotionEffect(new PotionEffect(Witchery.Potions.DISEASED.id, TimeUtil.minsToTicks(1 + world.rand.nextInt(4))));
         }
      }

   }

   public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
      return super.canDisplace(world, x, y, z);
   }

   public boolean displaceIfPossible(World world, int x, int y, int z) {
      return super.displaceIfPossible(world, x, y, z);
   }
}
