package com.emoniph.witchery.blocks;

import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.BlockUtil;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockBaseContainer extends BlockContainer {

   protected boolean registerBlockName;
   protected boolean registerTileEntity;
   protected boolean registerWithCreateTab;
   protected final Class clazzTile;
   protected final Class clazzItem;


   public BlockBaseContainer(Material material, Class clazzTile) {
      this(material, clazzTile, (Class)null);
   }

   public BlockBaseContainer(Material material, Class clazzTile, Class clazzItem) {
      super(material);
      this.registerBlockName = true;
      this.registerTileEntity = true;
      this.registerWithCreateTab = true;
      this.clazzTile = clazzTile;
      this.clazzItem = clazzItem;
   }

   public Block setBlockName(String blockName) {
      if(this.registerWithCreateTab) {
         this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      }

      if(this.registerBlockName) {
         if(this.clazzItem == null) {
            BlockUtil.registerBlock(this, blockName);
         } else {
            BlockUtil.registerBlock(this, this.clazzItem, blockName);
         }
      }

      if(this.registerTileEntity) {
         GameRegistry.registerTileEntity(this.clazzTile, blockName);
      }

      return super.setBlockName(blockName);
   }

   public TileEntity createNewTileEntity(World world, int metadata) {
      try {
         return (TileEntity)this.clazzTile.newInstance();
      } catch (Throwable var4) {
         return null;
      }
   }
}
