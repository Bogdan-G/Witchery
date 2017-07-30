package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBaseContainer;
import com.emoniph.witchery.blocks.TileEntityBase;
import com.emoniph.witchery.util.BlockProtect;
import com.emoniph.witchery.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class BlockSlurp extends BlockBaseContainer {

   public BlockSlurp() {
      super(Material.glass, BlockSlurp.TileEntitySlurp.class);
      super.registerWithCreateTab = false;
   }

   public int getRenderType() {
      return -1;
   }

   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
      return null;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_) {
      return false;
   }

   public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
      return false;
   }

   public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chance, int fortune) {}

   public void replaceBlockAt(World world, int x, int y, int z, int timeoutTicks) {
      if(!world.isRemote) {
         Block block = world.getBlock(x, y, z);
         if(BlockProtect.canBreak(block, world) && BlockProtect.checkModsForBreakOK(world, x, y, z, FakePlayerFactory.getMinecraft((WorldServer)world))) {
            int meta = world.getBlockMetadata(x, y, z);
            world.setBlock(x, y, z, Witchery.Blocks.SLURP);
            BlockSlurp.TileEntitySlurp tile = (BlockSlurp.TileEntitySlurp)BlockUtil.getTileEntity(world, x, y, z, BlockSlurp.TileEntitySlurp.class);
            if(tile != null) {
               tile.saveBlock(timeoutTicks, block, meta);
            }
         }
      }

   }

   public static class TileEntitySlurp extends TileEntityBase {

      private Block savedBlock;
      private int savedMeta;
      private int timeout;


      public void updateEntity() {
         super.updateEntity();
         if(!super.worldObj.isRemote && super.ticks >= (long)this.timeout) {
            if(this.savedBlock == null) {
               super.worldObj.setBlockToAir(super.xCoord, super.yCoord, super.zCoord);
            } else {
               super.worldObj.setBlock(super.xCoord, super.yCoord, super.zCoord, this.savedBlock, Math.max(this.savedMeta, 0), 3);
            }
         }

      }

      public void saveBlock(int timeoutTicks, Block block) {
         this.saveBlock(timeoutTicks, block, 0);
      }

      public void saveBlock(int timeoutTicks, Block block, int meta) {
         this.savedBlock = block;
         this.savedMeta = meta;
         this.timeout = timeoutTicks;
      }

      public void writeToNBT(NBTTagCompound nbtRoot) {
         super.writeToNBT(nbtRoot);
         nbtRoot.setInteger("Timeout", Math.max(this.timeout, 0));
         if(this.savedBlock != null) {
            nbtRoot.setString("blockName", Block.blockRegistry.getNameForObject(this.savedBlock));
            nbtRoot.setInteger("blockMeta", this.savedMeta);
         }

      }

      public void readFromNBT(NBTTagCompound nbtRoot) {
         super.readFromNBT(nbtRoot);
         this.timeout = Math.max(nbtRoot.getInteger("Timeout"), 0);
         this.savedBlock = null;
         this.savedMeta = 0;
         if(nbtRoot.hasKey("blockName")) {
            String blockName = nbtRoot.getString("blockName");
            if(blockName != null && !blockName.isEmpty()) {
               this.savedBlock = Block.getBlockFromName(blockName);
               this.savedMeta = nbtRoot.getInteger("blockMeta");
            }
         }

      }
   }
}
