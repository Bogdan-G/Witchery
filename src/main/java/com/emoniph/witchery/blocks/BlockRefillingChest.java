package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.InvUtil;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

public class BlockRefillingChest extends BlockChest {

   public BlockRefillingChest() {
      super(0);
      this.setResistance(9999.0F);
      this.setBlockUnbreakable();
   }

   public Block setBlockName(String blockName) {
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      BlockUtil.registerBlock(this, blockName);
      GameRegistry.registerTileEntity(BlockRefillingChest.TileEntityRefillingChest.class, blockName);
      return super.setBlockName(blockName);
   }

   public TileEntity createNewTileEntity(World world, int metadata) {
      return new BlockRefillingChest.TileEntityRefillingChest();
   }

   public static class TileEntityRefillingChest extends TileEntityChest {

      protected long ticks = 0L;
      private static final int MAX_ITEMS_FOR_REFILL = 0;


      public void updateEntity() {
         super.updateEntity();
         if(this.ticks == 0L) {
            this.initiate();
         } else if(this.ticks >= Long.MAX_VALUE) {
            this.ticks = 1L;
         }

         ++this.ticks;
         this.doUpdate();
      }

      protected void initiate() {
         this.doUpdate();
      }

      protected void doUpdate() {
         if(!super.worldObj.isRemote && super.worldObj.provider.dimensionId == Config.instance().dimensionTormentID && TimeUtil.secondsElapsed(3600, this.ticks) && InvUtil.getItemStackCount(this) <= 0) {
            int numItems = 2 + super.worldObj.rand.nextInt(4);
            ChestGenHooks gen = ChestGenHooks.getInfo("dungeonChest");
            WeightedRandomChestContent.generateChestContents(super.worldObj.rand, gen.getItems(super.worldObj.rand), this, numItems);
         }

      }

      public void writeToNBT(NBTTagCompound nbtChest) {
         super.writeToNBT(nbtChest);
         nbtChest.setLong("WITCLifeTicks", this.ticks);
      }

      public void readFromNBT(NBTTagCompound nbtChest) {
         super.readFromNBT(nbtChest);
         this.ticks = nbtChest.getLong("WITCLifeTicks");
      }
   }
}
