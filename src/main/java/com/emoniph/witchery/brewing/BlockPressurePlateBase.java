package com.emoniph.witchery.brewing;

import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.TileEntityCursedBlock;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPressurePlate.Sensitivity;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPressurePlateBase extends BlockBaseContainer {

   private Sensitivity sensitivity;
   private String textureName;
   private Block original;


   public BlockPressurePlateBase(Block original, String textureName, Sensitivity sensitivity) {
      super(original.getMaterial(), TileEntityCursedBlock.class);
      this.textureName = textureName;
      this.sensitivity = sensitivity;
      this.original = original;
      this.setHardness(0.5F);
      this.setStepSound(original.stepSound);
      this.setTickRandomly(true);
      this.func_150063_b(this.func_150066_d(15));
      super.registerWithCreateTab = false;
   }

   public void replaceButton(World world, int x, int y, int z, ModifiersImpact impactModifiers, NBTTagCompound nbtBrew) {
      int meta = world.getBlockMetadata(x, y, z);
      world.setBlock(x, y, z, this, meta, 3);
      world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
      TileEntityCursedBlock tile = (TileEntityCursedBlock)BlockUtil.getTileEntity(world, x, y, z, TileEntityCursedBlock.class);
      if(tile != null) {
         tile.initalise(impactModifiers, nbtBrew);
      }

   }

   public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
      this.func_150063_b(world.getBlockMetadata(x, y, z));
   }

   protected void func_150063_b(int p_150063_1_) {
      boolean flag = this.func_150060_c(p_150063_1_) > 0;
      float f = 0.0625F;
      if(flag) {
         this.setBlockBounds(f, 0.0F, f, 1.0F - f, 0.03125F, 1.0F - f);
      } else {
         this.setBlockBounds(f, 0.0F, f, 1.0F - f, 0.0625F, 1.0F - f);
      }

   }

   public int tickRate(World world) {
      return 20;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean renderAsNormalBlock() {
      return false;
   }

   public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z) {
      return true;
   }

   public boolean canPlaceBlockAt(World world, int x, int y, int z) {
      return World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) || BlockFence.func_149825_a(world.getBlock(x, y - 1, z));
   }

   public void onNeighborBlockChange(World world, int x, int y, int z, Block p_149695_5_) {
      boolean flag = false;
      if(!World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !BlockFence.func_149825_a(world.getBlock(x, y - 1, z))) {
         flag = true;
      }

      if(flag) {
         this.dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
         world.setBlockToAir(x, y, z);
      }

   }

   public void updateTick(World world, int x, int y, int z, Random rand) {
      if(!world.isRemote) {
         int l = this.func_150060_c(world.getBlockMetadata(x, y, z));
         if(l > 0) {
            this.func_150062_a(world, x, y, z, l);
         }
      }

   }

   public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
      if(!world.isRemote) {
         int metadata = world.getBlockMetadata(x, y, z);
         int l = this.func_150060_c(metadata);
         if(l == 0) {
            int i1 = this.func_150065_e(world, x, y, z);
            boolean flag = l > 0;
            boolean flag1 = i1 > 0;
            if(l != i1) {
               int md = this.func_150066_d(i1);
               if(!world.isRemote) {
                  TileEntityCursedBlock tile = (TileEntityCursedBlock)BlockUtil.getTileEntity(world, x, y, z, TileEntityCursedBlock.class);
                  if(tile != null && !tile.applyToEntityAndDestroy(entity)) {
                     world.setBlock(x, y, z, this.original);
                     world.setBlockMetadataWithNotify(x, y, z, md, 2);
                     this.func_150064_a_(world, x, y, z);
                     world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
                     if(!flag1 && flag) {
                        world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
                     } else if(flag1 && !flag) {
                        world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
                     }

                     if(flag1) {
                        world.scheduleBlockUpdate(x, y, z, this.original, this.tickRate(world));
                     }

                     return;
                  }
               }

               world.setBlockMetadataWithNotify(x, y, z, md, 2);
               this.func_150064_a_(world, x, y, z);
               world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            }

            if(!flag1 && flag) {
               world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
            } else if(flag1 && !flag) {
               world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
            }

            if(flag1) {
               world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
            }
         }
      }

   }

   protected void func_150062_a(World world, int x, int y, int z, int p_150062_5_) {
      int i1 = this.func_150065_e(world, x, y, z);
      boolean flag = p_150062_5_ > 0;
      boolean flag1 = i1 > 0;
      if(p_150062_5_ != i1) {
         int metadata = this.func_150066_d(i1);
         world.setBlockMetadataWithNotify(x, y, z, metadata, 2);
         this.func_150064_a_(world, x, y, z);
         world.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
      }

      if(!flag1 && flag) {
         world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
      } else if(flag1 && !flag) {
         world.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
      }

      if(flag1) {
         world.scheduleBlockUpdate(x, y, z, this, this.tickRate(world));
      }

   }

   protected AxisAlignedBB func_150061_a(int x, int y, int z) {
      float f = 0.125F;
      return AxisAlignedBB.getBoundingBox((double)((float)x + f), (double)y, (double)((float)z + f), (double)((float)(x + 1) - f), (double)y + 0.25D, (double)((float)(z + 1) - f));
   }

   public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
      if(this.func_150060_c(p_149749_6_) > 0) {
         this.func_150064_a_(world, x, y, z);
      }

      super.breakBlock(world, x, y, z, block, p_149749_6_);
   }

   protected void func_150064_a_(World world, int x, int y, int z) {
      world.notifyBlocksOfNeighborChange(x, y, z, this);
      world.notifyBlocksOfNeighborChange(x, y - 1, z, this);
   }

   public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int p_149709_5_) {
      return this.func_150060_c(world.getBlockMetadata(x, y, z));
   }

   public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int p_149748_5_) {
      return p_149748_5_ == 1?this.func_150060_c(world.getBlockMetadata(x, y, z)):0;
   }

   public boolean canProvidePower() {
      return true;
   }

   public void setBlockBoundsForItemRender() {
      float f = 0.5F;
      float f1 = 0.125F;
      float f2 = 0.5F;
      this.setBlockBounds(0.5F - f, 0.5F - f1, 0.5F - f2, 0.5F + f, 0.5F + f1, 0.5F + f2);
   }

   public int getMobilityFlag() {
      return 1;
   }

   protected int func_150066_d(int p_150066_1_) {
      return p_150066_1_ > 0?1:0;
   }

   protected int func_150060_c(int p_150060_1_) {
      return p_150060_1_ == 1?15:0;
   }

   protected int func_150065_e(World world, int x, int y, int z) {
      List list = null;
      if(this.sensitivity == Sensitivity.everything) {
         list = world.getEntitiesWithinAABBExcludingEntity((Entity)null, this.func_150061_a(x, y, z));
      }

      if(this.sensitivity == Sensitivity.mobs) {
         list = world.getEntitiesWithinAABB(EntityLivingBase.class, this.func_150061_a(x, y, z));
      }

      if(this.sensitivity == Sensitivity.players) {
         list = world.getEntitiesWithinAABB(EntityPlayer.class, this.func_150061_a(x, y, z));
      }

      if(list != null && !list.isEmpty()) {
         Iterator iterator = list.iterator();

         while(iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if(!entity.doesEntityNotTriggerPressurePlate()) {
               return 15;
            }
         }
      }

      return 0;
   }

   @SideOnly(Side.CLIENT)
   public void registerBlockIcons(IIconRegister iconRegister) {
      super.blockIcon = iconRegister.registerIcon(this.textureName);
   }

   public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
      return new ItemStack(this.original);
   }

   public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
      return Item.getItemFromBlock(this.original);
   }
}
