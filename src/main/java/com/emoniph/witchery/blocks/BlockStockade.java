package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBase;
import com.emoniph.witchery.util.MultiItemBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStockade extends BlockBase {

   public static final String[] WOOD_TEXTURES = new String[]{"log_oak", "log_spruce", "log_birch", "log_jungle", "witchery:log_rowan", "witchery:log_alder", "witchery:log_hawthorn", "log_acacia", "log_big_oak"};
   public static final String[] WOOD_NAMES = new String[]{"oak", "spruce", "birch", "jungle", "rowan", "alder", "hawthorn", "acacia", "big_oak"};
   public static final String[] ICE_TEXTURES = new String[]{"ice"};
   public static final String[] ICE_NAMES = new String[]{"ice"};
   @SideOnly(Side.CLIENT)
   private IIcon[] tree;
   @SideOnly(Side.CLIENT)
   private IIcon[] tree_top;
   private final boolean alpha;
   private boolean tipTexturing;


   public int damageDropped(int metadata) {
      if(metadata < 0 || metadata >= (this.alpha?ICE_NAMES.length:WOOD_NAMES.length)) {
         metadata = 0;
      }

      return metadata;
   }

   public BlockStockade(boolean alpha) {
      super(alpha?Material.ice:Material.wood, BlockStockade.ClassItemBlock.class);
      this.setHardness(25.0F);
      this.setResistance(20.0F);
      this.alpha = alpha;
   }

   public int getRenderType() {
      return Witchery.proxy.getStockageRenderId();
   }

   public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote && entity instanceof EntityLivingBase) {
         EntityLivingBase living = (EntityLivingBase)entity;
         living.attackEntityFrom(DamageSource.cactus, 3.0F);
      }

   }

   public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bb, List list, Entity entity) {
      boolean connectN = this.canConnectFenceTo(world, x, y, z - 1);
      boolean connectS = this.canConnectFenceTo(world, x, y, z + 1);
      boolean connectW = this.canConnectFenceTo(world, x - 1, y, z);
      boolean connectE = this.canConnectFenceTo(world, x + 1, y, z);
      float f = 0.375F;
      float f1 = 0.625F;
      float f2 = 0.375F;
      float f3 = 0.625F;
      if(connectN) {
         f2 = 0.0F;
      }

      if(connectS) {
         f3 = 1.0F;
      }

      if(!connectN && !connectS && !connectE && !connectW) {
         this.setBlockBounds(0.3F, 0.0F, 0.3F, 0.7F, 0.9F, 0.7F);
         super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
      }

      if(connectN || connectS) {
         this.setBlockBounds(0.3F, 0.0F, 0.05F, 0.7F, !connectE && !connectW?0.9F:1.0F, 0.95F);
         super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
      }

      if(connectE || connectW) {
         this.setBlockBounds(0.05F, 0.0F, 0.3F, 0.55F, !connectN && !connectS?0.9F:1.0F, 0.7F);
         super.addCollisionBoxesToList(world, x, y, z, bb, list, entity);
      }

   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
      boolean connectN = this.canConnectFenceTo(world, x, y, z - 1);
      boolean connectS = this.canConnectFenceTo(world, x, y, z + 1);
      boolean connectW = this.canConnectFenceTo(world, x - 1, y, z);
      boolean connectE = this.canConnectFenceTo(world, x + 1, y, z);
      float f = 0.3F;
      float f1 = 0.3F;
      float f2 = 0.7F;
      float f3 = 0.7F;
      if(connectN || connectS) {
         f1 = 0.05F;
         f3 = 0.95F;
      }

      if(connectE || connectW) {
         f = 0.05F;
         f2 = 0.95F;
      }

      this.setBlockBounds(f, 0.0F, f1, f2, (connectN || connectS) && (connectW || connectE)?1.0F:0.9F, f3);
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public int getRenderBlockPass() {
      return this.alpha?1:super.getRenderBlockPass();
   }

   public boolean getBlocksMovement(IBlockAccess p_149655_1_, int p_149655_2_, int p_149655_3_, int p_149655_4_) {
      return false;
   }

   public boolean canConnectFenceTo(IBlockAccess p_149826_1_, int p_149826_2_, int p_149826_3_, int p_149826_4_) {
      Block block = p_149826_1_.getBlock(p_149826_2_, p_149826_3_, p_149826_4_);
      return block == this || block == Blocks.fence_gate || block == Witchery.Blocks.PERPETUAL_ICE_FENCE_GATE;
   }

   @SideOnly(Side.CLIENT)
   public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
      if(world.getBlock(x, y, z) == this) {
         boolean sideX;
         boolean sideZ;
         boolean aboveX;
         boolean aboveZ;
         if(side == 1) {
            sideX = world.getBlock(x + 1, y, z) == this || world.getBlock(x - 1, y, z) == this;
            sideZ = world.getBlock(x, y, z + 1) == this || world.getBlock(x, y, z - 1) == this;
            aboveX = world.getBlock(x + 1, y - 1, z) == this || world.getBlock(x - 1, y - 1, z) == this;
            aboveZ = world.getBlock(x, y - 1, z + 1) == this || world.getBlock(x, y - 1, z - 1) == this;
            if(sideX && aboveX && sideZ && aboveZ) {
               return false;
            }

            if(aboveX && !sideX) {
               return true;
            }

            if(aboveZ && !sideZ) {
               return true;
            }

            return false;
         }

         if(side == 0) {
            sideX = world.getBlock(x + 1, y, z) == this || world.getBlock(x - 1, y, z) == this;
            sideZ = world.getBlock(x, y, z + 1) == this || world.getBlock(x, y, z - 1) == this;
            aboveX = world.getBlock(x + 1, y + 1, z) == this || world.getBlock(x - 1, y + 1, z) == this;
            aboveZ = world.getBlock(x, y + 1, z + 1) == this || world.getBlock(x, y + 1, z - 1) == this;
            if(aboveX && sideX && aboveZ && sideZ) {
               return false;
            }

            if(sideX && !aboveX) {
               return true;
            }

            if(sideZ && !aboveZ) {
               return true;
            }

            return false;
         }
      }

      return true;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(int side, int meta) {
      if(meta < 0 || meta >= (this.alpha?ICE_TEXTURES.length:WOOD_TEXTURES.length)) {
         meta = 0;
      }

      return side != 1 && side != 0 && !this.tipTexturing?this.tree[meta]:this.tree_top[meta];
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item block, CreativeTabs creativeTabs, List list) {
      for(int i = 0; i < (this.alpha?ICE_TEXTURES.length:WOOD_TEXTURES.length); ++i) {
         list.add(new ItemStack(this, 1, i));
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.tree = new IIcon[this.alpha?ICE_TEXTURES.length:WOOD_TEXTURES.length];
      this.tree_top = new IIcon[this.alpha?ICE_TEXTURES.length:WOOD_TEXTURES.length];
      int i;
      if(this.alpha) {
         for(i = 0; i < this.tree.length; ++i) {
            this.tree[i] = iconRegister.registerIcon(ICE_TEXTURES[i]);
            this.tree_top[i] = iconRegister.registerIcon(ICE_TEXTURES[i] + (ICE_TEXTURES[i].equals("ice")?"":"_top"));
         }
      } else {
         for(i = 0; i < this.tree.length; ++i) {
            this.tree[i] = iconRegister.registerIcon(WOOD_TEXTURES[i]);
            this.tree_top[i] = iconRegister.registerIcon(WOOD_TEXTURES[i] + "_top");
         }
      }

   }

   public void setTipTexture(boolean b) {
      this.tipTexturing = b;
   }


   public static class ClassItemBlock extends MultiItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      protected String[] getNames() {
         return ((BlockStockade)super.field_150939_a).alpha?BlockStockade.ICE_NAMES:BlockStockade.WOOD_NAMES;
      }
   }
}
