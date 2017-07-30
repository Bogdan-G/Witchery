package com.emoniph.witchery.blocks;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseRotatedPillar;
import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.util.MultiItemBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockWickerBundle extends BlockBaseRotatedPillar {

   private static final String[] bundleType = new String[]{"plain", "bloodied"};
   @SideOnly(Side.CLIENT)
   private IIcon[] block_side;
   @SideOnly(Side.CLIENT)
   private IIcon[] block_top;


   public BlockWickerBundle() {
      super(Material.wood, BlockWickerBundle.ClassItemBlock.class);
      this.setHardness(0.5F);
      this.setStepSound(Block.soundTypeGrass);
   }

   public Block setBlockName(String blockName) {
      super.setBlockName(blockName);
      Blocks.fire.setFireInfo(this, 20, 20);
      return this;
   }

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xOffset, float yOffset, float zOffset) {
      ItemStack heldItem = player.getHeldItem();
      return heldItem != null && heldItem.getItem() == Items.flint_and_steel?tryIgniteMan(world, x, y, z, player.rotationYaw):false;
   }

   public static boolean tryIgniteMan(World world, int x, int y, int z, float rotationYaw) {
      boolean xleft = world.getBlock(x - 1, y, z) == Witchery.Blocks.WICKER_BUNDLE;
      boolean xright = world.getBlock(x + 1, y, z) == Witchery.Blocks.WICKER_BUNDLE;
      boolean zleft = world.getBlock(x, y, z - 1) == Witchery.Blocks.WICKER_BUNDLE;
      boolean zright = world.getBlock(x, y, z + 1) == Witchery.Blocks.WICKER_BUNDLE;
      int dx = x;
      int dy = y;
      int dz = z;
      byte fz = 0;
      byte fx = 0;
      if((!xleft && !xright || !zleft && !zright) && (xleft || xright || zleft || zright)) {
         if(!xleft && !xright) {
            if(zleft && !zright) {
               dz = z - 1;
            } else if(!zleft) {
               dz = z + 1;
            }

            while(world.getBlock(dx, dy - 1, dz) == Witchery.Blocks.WICKER_BUNDLE) {
               --dy;
            }

            while(world.getBlock(dx, dy, dz - 1) == Witchery.Blocks.WICKER_BUNDLE) {
               --dz;
            }

            fz = 1;
         } else {
            if(xleft && !xright) {
               dx = x - 1;
            } else if(!xleft) {
               dx = x + 1;
            }

            while(world.getBlock(dx, dy - 1, dz) == Witchery.Blocks.WICKER_BUNDLE) {
               --dy;
            }

            while(world.getBlock(dx - 1, dy, dz) == Witchery.Blocks.WICKER_BUNDLE) {
               --dx;
            }

            fx = 1;
         }

         if(!wicker(world, dx, dy + 7, dz) && !wicker(world, dx + 1 * fx, dy + 7, dz + 1 * fz) && !wicker(world, dx - 1 * fx, dy + 6, dz - 1 * fz) && wicker(world, dx, dy + 6, dz) && wicker(world, dx + 1 * fx, dy + 6, dz + 1 * fz) && !wicker(world, dx + 2 * fx, dy + 6, dz + 2 * fz) && !wicker(world, dx - 1 * fx, dy + 5, dz - 1 * fz) && wicker(world, dx, dy + 5, dz) && wicker(world, dx + 1 * fx, dy + 5, dz + 1 * fz) && !wicker(world, dx + 2 * fx, dy + 5, dz + 2 * fz) && !wicker(world, dx - 2 * fx, dy + 4, dz - 2 * fz) && wicker(world, dx - 1 * fx, dy + 4, dz - 1 * fz) && wicker(world, dx, dy + 4, dz) && wicker(world, dx + 1 * fx, dy + 4, dz + 1 * fz) && wicker(world, dx + 2 * fx, dy + 4, dz + 2 * fz) && !wicker(world, dx + 3 * fx, dy + 4, dz + 3 * fz) && !wicker(world, dx - 2 * fx, dy + 3, dz - 2 * fz) && wicker(world, dx - 1 * fx, dy + 3, dz - 1 * fz) && wicker(world, dx, dy + 3, dz) && wicker(world, dx + 1 * fx, dy + 3, dz + 1 * fz) && wicker(world, dx + 2 * fx, dy + 3, dz + 2 * fz) && !wicker(world, dx + 3 * fx, dy + 3, dz + 3 * fz) && !wicker(world, dx - 2 * fx, dy + 2, dz - 2 * fz) && wicker(world, dx - 1 * fx, dy + 2, dz - 1 * fz) && wicker(world, dx, dy + 2, dz) && wicker(world, dx + 1 * fx, dy + 2, dz + 1 * fz) && wicker(world, dx + 2 * fx, dy + 2, dz + 2 * fz) && !wicker(world, dx + 3 * fx, dy + 2, dz + 3 * fz) && !wicker(world, dx - 1 * fx, dy + 1, dz - 1 * fz) && wicker(world, dx, dy + 1, dz) && wicker(world, dx + 1 * fx, dy, dz + 1 * fz) && !wicker(world, dx + 2 * fx, dy + 1, dz + 2 * fz) && !wicker(world, dx - 1 * fx, dy, dz - 1 * fz) && wicker(world, dx, dy, dz) && wicker(world, dx + 1 * fx, dy, dz + 1 * fz) && !wicker(world, dx + 2 * fx, dy, dz + 2 * fz)) {
            world.setBlock(dx, dy + 6, dz, Blocks.fire);
            world.setBlock(dx + 1 * fx, dy + 6, dz + 1 * fz, Blocks.fire);
            world.setBlock(dx, dy + 3, dz, Blocks.fire);
            world.setBlock(dx + 1 * fx, dy + 3, dz + 1 * fz, Blocks.fire);
            world.setBlock(dx, dy + 2, dz, Blocks.fire);
            world.setBlock(dx + 1 * fx, dy + 2, dz + 1 * fz, Blocks.fire);
            world.setBlock(dx, dy + 1, dz, Blocks.fire);
            world.setBlock(dx + 1 * fx, dy + 1, dz + 1 * fz, Blocks.fire);
            world.setBlock(dx, dy + 0, dz, Blocks.fire);
            world.setBlock(dx + 1 * fx, dy + 0, dz + 1 * fz, Blocks.fire);
            world.setBlock(dx - 1 * fx, dy + 4, dz - 1 * fz, Blocks.fire);
            world.setBlock(dx + 2 * fx, dy + 4, dz + 2 * fz, Blocks.fire);
            EntityHornedHuntsman entity = new EntityHornedHuntsman(world);
            entity.setLocationAndAngles((double)dx + 1.0D * (double)fx + 0.5D * (double)fz, (double)dy + 0.1D, (double)dz + 1.0D * (double)fz + 0.5D * (double)fx, 180.0F + rotationYaw, 0.0F);
            entity.rotationYawHead = entity.rotationYaw;
            entity.renderYawOffset = entity.rotationYaw;
            entity.func_110163_bv();
            entity.func_82206_m();
            entity.playLivingSound();
            if(!world.isRemote) {
               world.spawnEntityInWorld(entity);
            }

            for(int j1 = 0; j1 < 120; ++j1) {
               world.spawnParticle("snowballpoof", (double)dx + world.rand.nextDouble(), (double)(dy - 2) + world.rand.nextDouble() * 3.9D, (double)(dz + 1) + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean wicker(World world, int x, int y, int z) {
      return world.getBlock(x, y, z) == Witchery.Blocks.WICKER_BUNDLE && limitToValidMetadata(world.getBlockMetadata(x, y, z)) == 1;
   }

   public int getRenderType() {
      return 31;
   }

   public int damageDropped(int meta) {
      return limitToValidMetadata(meta);
   }

   @SideOnly(Side.CLIENT)
   public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) {
      for(int i = 0; i < bundleType.length; ++i) {
         list.add(new ItemStack(item, 1, i));
      }

   }

   public int quantityDropped(Random par1Random) {
      return 1;
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      int metadata = world.getBlockMetadata(x, y, z);
      return new ItemStack(this, 1, metadata >= 0?limitToValidMetadata(metadata):0);
   }

   @SideOnly(Side.CLIENT)
   protected IIcon getSideIcon(int meta) {
      return this.block_side[MathHelper.clamp_int(meta, 0, 1)];
   }

   @SideOnly(Side.CLIENT)
   protected IIcon getTopIcon(int meta) {
      return this.block_top[MathHelper.clamp_int(meta, 0, 1)];
   }

   public static int limitToValidMetadata(int par0) {
      return par0 & bundleType.length - 1;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      this.block_side = new IIcon[bundleType.length];
      this.block_top = new IIcon[bundleType.length];

      for(int i = 0; i < bundleType.length; ++i) {
         this.block_side[i] = iconRegister.registerIcon(this.getTextureName() + "_" + bundleType[i] + "_side");
         this.block_top[i] = iconRegister.registerIcon(this.getTextureName() + "_" + bundleType[i] + "_top");
      }

   }

   public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
      boolean flammable = super.isFlammable(world, x, y, z, face);
      return flammable;
   }


   public static class ClassItemBlock extends MultiItemBlock {

      public ClassItemBlock(Block block) {
         super(block);
      }

      protected String[] getNames() {
         return BlockWickerBundle.bundleType;
      }
   }
}
