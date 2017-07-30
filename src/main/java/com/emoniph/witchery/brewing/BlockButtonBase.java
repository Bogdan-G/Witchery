package com.emoniph.witchery.brewing;

import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.TileEntityCursedBlock;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class BlockButtonBase extends BlockBaseContainer {

   private final boolean isWood;


   protected BlockButtonBase(boolean wooden) {
      super(Material.circuits, TileEntityCursedBlock.class);
      this.setTickRandomly(true);
      this.isWood = wooden;
      super.registerWithCreateTab = false;
      this.setHardness(0.5F);
      this.setStepSound(Block.soundTypePiston);
   }

   public void replaceButton(World world, int x, int y, int z, ModifiersImpact impactModifiers, NBTTagCompound nbtBrew) {
      int meta = world.getBlockMetadata(x, y, z);
      world.setBlock(x, y, z, this, meta & 7, 3);
      TileEntityCursedBlock tile = (TileEntityCursedBlock)BlockUtil.getTileEntity(world, x, y, z, TileEntityCursedBlock.class);
      if(tile != null) {
         tile.initalise(impactModifiers, nbtBrew);
      }

   }

   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
      return Item.getItemFromBlock(this.isWood?Blocks.wooden_button:Blocks.stone_button);
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return new ItemStack(this.isWood?Blocks.wooden_button:Blocks.stone_button);
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public int tickRate(World world) {
      return this.isWood?30:20;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean canPlaceBlockOnSide(World world, int x, int y, int z, int side) {
      ForgeDirection dir = ForgeDirection.getOrientation(side);
      return dir == ForgeDirection.NORTH && world.isSideSolid(x, y, z + 1, ForgeDirection.NORTH) || dir == ForgeDirection.SOUTH && world.isSideSolid(x, y, z - 1, ForgeDirection.SOUTH) || dir == ForgeDirection.WEST && world.isSideSolid(x + 1, y, z, ForgeDirection.WEST) || dir == ForgeDirection.EAST && world.isSideSolid(x - 1, y, z, ForgeDirection.EAST);
   }

   public boolean canPlaceBlockAt(World world, int x, int y, int z) {
      return world.isSideSolid(x - 1, y, z, ForgeDirection.EAST) || world.isSideSolid(x + 1, y, z, ForgeDirection.WEST) || world.isSideSolid(x, y, z - 1, ForgeDirection.SOUTH) || world.isSideSolid(x, y, z + 1, ForgeDirection.NORTH);
   }

   public int onBlockPlaced(World world, int x, int y, int z, int side, float p_149660_6_, float p_149660_7_, float p_149660_8_, int p_149660_9_) {
      int j1 = world.getBlockMetadata(x, y, z);
      int k1 = j1 & 8;
      j1 &= 7;
      ForgeDirection dir = ForgeDirection.getOrientation(side);
      if(dir == ForgeDirection.NORTH && world.isSideSolid(x, y, z + 1, ForgeDirection.NORTH)) {
         j1 = 4;
      } else if(dir == ForgeDirection.SOUTH && world.isSideSolid(x, y, z - 1, ForgeDirection.SOUTH)) {
         j1 = 3;
      } else if(dir == ForgeDirection.WEST && world.isSideSolid(x + 1, y, z, ForgeDirection.WEST)) {
         j1 = 2;
      } else if(dir == ForgeDirection.EAST && world.isSideSolid(x - 1, y, z, ForgeDirection.EAST)) {
         j1 = 1;
      } else {
         j1 = this.func_150045_e(world, x, y, z);
      }

      return j1 + k1;
   }

   private int func_150045_e(World world, int x, int y, int z) {
      return world.isSideSolid(x - 1, y, z, ForgeDirection.EAST)?1:(world.isSideSolid(x + 1, y, z, ForgeDirection.WEST)?2:(world.isSideSolid(x, y, z - 1, ForgeDirection.SOUTH)?3:(world.isSideSolid(x, y, z + 1, ForgeDirection.NORTH)?4:1)));
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
      if(this.func_150044_m(world, x, y, z)) {
         int l = world.getBlockMetadata(x, y, z) & 7;
         boolean flag = false;
         if(!world.isSideSolid(x - 1, y, z, ForgeDirection.EAST) && l == 1) {
            flag = true;
         }

         if(!world.isSideSolid(x + 1, y, z, ForgeDirection.WEST) && l == 2) {
            flag = true;
         }

         if(!world.isSideSolid(x, y, z - 1, ForgeDirection.SOUTH) && l == 3) {
            flag = true;
         }

         if(!world.isSideSolid(x, y, z + 1, ForgeDirection.NORTH) && l == 4) {
            flag = true;
         }

         if(flag) {
            this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlockToAir(x, y, z);
         }
      }

   }

   private boolean func_150044_m(World world, int x, int y, int z) {
      if(!this.canPlaceBlockAt(world, x, y, z)) {
         this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
         world.setBlockToAir(x, y, z);
         return false;
      } else {
         return true;
      }
   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
      int l = world.getBlockMetadata(x, y, z);
      this.func_150043_b(l);
   }

   private void func_150043_b(int p_150043_1_) {
      int j = p_150043_1_ & 7;
      boolean flag = (p_150043_1_ & 8) > 0;
      float f = 0.375F;
      float f1 = 0.625F;
      float f2 = 0.1875F;
      float f3 = 0.125F;
      if(flag) {
         f3 = 0.0625F;
      }

      if(j == 1) {
         this.setBlockBounds(0.0F, f, 0.5F - f2, f3, f1, 0.5F + f2);
      } else if(j == 2) {
         this.setBlockBounds(1.0F - f3, f, 0.5F - f2, 1.0F, f1, 0.5F + f2);
      } else if(j == 3) {
         this.setBlockBounds(0.5F - f2, f, 0.0F, 0.5F + f2, f1, f3);
      } else if(j == 4) {
         this.setBlockBounds(0.5F - f2, f, 1.0F - f3, 0.5F + f2, f1, 1.0F);
      }

   }

   public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {}

   public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
      int i1 = world.getBlockMetadata(x, y, z);
      int j1 = i1 & 7;
      int k1 = 8 - (i1 & 8);
      if(k1 == 0) {
         return true;
      } else {
         if(!world.isRemote) {
            TileEntityCursedBlock tile = (TileEntityCursedBlock)BlockUtil.getTileEntity(world, x, y, z, TileEntityCursedBlock.class);
            if(tile != null && tile.nbtEffect != null && !tile.applyToEntityAndDestroy(player)) {
               world.setBlock(x, y, z, this.isWood?Blocks.wooden_button:Blocks.stone_button, j1 + k1, 3);
               world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
               world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
               this.func_150042_a(world, x, y, z, j1);
               world.setBlockMetadataWithNotify(x, y, z, j1 + k1, 3);
               world.scheduleBlockUpdate(x, y, z, this.isWood?Blocks.wooden_button:Blocks.stone_button, this.tickRate(world));
               return true;
            }

            world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
            this.func_150042_a(world, x, y, z, j1);
            world.setBlockMetadataWithNotify(x, y, z, j1 + k1, 3);
            world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
         }

         return true;
      }
   }

   public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
      if((p_149749_6_ & 8) > 0) {
         int i1 = p_149749_6_ & 7;
         this.func_150042_a(world, x, y, z, i1);
      }

      super.breakBlock(world, x, y, z, block, p_149749_6_);
   }

   public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int p_149709_5_) {
      return (world.getBlockMetadata(x, y, z) & 8) > 0?15:0;
   }

   public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int p_149748_5_) {
      int i1 = world.getBlockMetadata(x, y, z);
      if((i1 & 8) == 0) {
         return 0;
      } else {
         int j1 = i1 & 7;
         return j1 == 5 && p_149748_5_ == 1?15:(j1 == 4 && p_149748_5_ == 2?15:(j1 == 3 && p_149748_5_ == 3?15:(j1 == 2 && p_149748_5_ == 4?15:(j1 == 1 && p_149748_5_ == 5?15:0))));
      }
   }

   public boolean canProvidePower() {
      return true;
   }

   public void updateTick(World world, int x, int y, int z, Random rand) {
      if(!world.isRemote) {
         int l = world.getBlockMetadata(x, y, z);
         if((l & 8) != 0) {
            if(this.isWood) {
               this.func_150046_n(world, x, y, z);
            } else {
               world.setBlockMetadataWithNotify(x, y, z, l & 7, 3);
               int i1 = l & 7;
               this.func_150042_a(world, x, y, z, i1);
               world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
               world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            }
         }
      }

   }

   public void setBlockBoundsForItemRender() {
      float f = 0.1875F;
      float f1 = 0.125F;
      float f2 = 0.125F;
      this.setBlockBounds(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote && this.isWood && (world.getBlockMetadata(x, y, z) & 8) == 0) {
         this.func_150046_n(world, x, y, z);
      }

   }

   private void func_150046_n(World world, int x, int y, int z) {
      int l = world.getBlockMetadata(x, y, z);
      int i1 = l & 7;
      boolean flag = (l & 8) != 0;
      this.func_150043_b(l);
      List list = world.getEntitiesWithinAABB(EntityArrow.class, AxisAlignedBB.getBoundingBox((double)x + super.minX, (double)y + super.minY, (double)z + super.minZ, (double)x + super.maxX, (double)y + super.maxY, (double)z + super.maxZ));
      boolean flag1 = !list.isEmpty();
      if(flag1 && !flag) {
         world.setBlockMetadataWithNotify(x, y, z, i1 | 8, 3);
         this.func_150042_a(world, x, y, z, i1);
         world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
         world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
      }

      if(!flag1 && flag) {
         world.setBlockMetadataWithNotify(x, y, z, i1, 3);
         this.func_150042_a(world, x, y, z, i1);
         world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
         world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
      }

      if(flag1) {
         world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
      }

   }

   private void func_150042_a(World world, int x, int y, int z, int p_150042_5_) {
      world.notifyBlocksOfNeighborChange(x, y, z, this);
      if(p_150042_5_ == 1) {
         world.notifyBlocksOfNeighborChange(x - 1, y, z, this);
      } else if(p_150042_5_ == 2) {
         world.notifyBlocksOfNeighborChange(x + 1, y, z, this);
      } else if(p_150042_5_ == 3) {
         world.notifyBlocksOfNeighborChange(x, y, z - 1, this);
      } else if(p_150042_5_ == 4) {
         world.notifyBlocksOfNeighborChange(x, y, z + 1, this);
      } else {
         world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {}
}
