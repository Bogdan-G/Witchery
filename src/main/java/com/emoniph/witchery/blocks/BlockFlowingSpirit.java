package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityNightmare;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.CreatureUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockFlowingSpirit extends BlockFluidClassic {

   protected final boolean nightmareBane;
   protected final boolean igniteSpiritPortals;
   protected final PotionEffect goodyEffect;
   protected final PotionEffect baddyEffect;
   @SideOnly(Side.CLIENT)
   protected IIcon[] icons;


   public BlockFlowingSpirit(Fluid fluid, PotionEffect goodyEffect, PotionEffect baddyEffect, boolean nightmareBane, boolean igniteSpiritPortals) {
      super(fluid, Material.water);
      super.quantaPerBlock = 5;
      this.setHardness(100.0F);
      this.setLightOpacity(3);
      this.goodyEffect = goodyEffect;
      this.baddyEffect = baddyEffect;
      this.nightmareBane = nightmareBane;
      this.igniteSpiritPortals = igniteSpiritPortals;
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

   public void onBlockAdded(World world, int x, int y, int z) {
      if(!this.igniteSpiritPortals || world.provider.dimensionId != Config.instance().dimensionDreamID || world.getBlock(x, y - 1, z) != Blocks.snow || world.getBlockMetadata(x, y, z) != 0 || !Witchery.Blocks.SPIRIT_PORTAL.tryToCreatePortal(world, x, y, z)) {
         super.onBlockAdded(world, x, y, z);
      }

   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote && entity != null) {
         if(entity instanceof EntityLivingBase) {
            EntityLivingBase item = (EntityLivingBase)entity;
            if(!CreatureUtil.isUndead(item) && !CreatureUtil.isDemonic(item) && (!this.nightmareBane || !(item instanceof EntityNightmare))) {
               if(!item.isPotionActive(this.goodyEffect.getPotionID())) {
                  item.addPotionEffect(new PotionEffect(this.goodyEffect));
               }
            } else if(!item.isPotionActive(this.baddyEffect.getPotionID())) {
               item.addPotionEffect(new PotionEffect(this.baddyEffect));
            }
         } else if(this.nightmareBane && entity instanceof EntityItem) {
            EntityItem item1 = (EntityItem)entity;
            ItemStack stack = item1.getEntityItem();
            if(Witchery.Items.GENERIC.itemDisturbedCotton.isMatch(stack)) {
               ItemStack newStack = new ItemStack(Witchery.Blocks.WISPY_COTTON, stack.stackSize);
               item1.setEntityItemStack(newStack);
            }
         }
      }

   }

   public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
      return world.getBlock(x, y, z).getMaterial().isLiquid()?false:super.canDisplace(world, x, y, z);
   }

   public boolean displaceIfPossible(World world, int x, int y, int z) {
      return world.getBlock(x, y, z).getMaterial().isLiquid()?false:super.displaceIfPossible(world, x, y, z);
   }
}
