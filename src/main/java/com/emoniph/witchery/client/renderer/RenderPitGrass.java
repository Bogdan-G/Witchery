package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.Witchery;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

public class RenderPitGrass implements ISimpleBlockRenderingHandler {

   public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
      renderer.renderBlockAsItem(Blocks.grass, metadata, 1.0F);
   }

   public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
      return renderer.renderStandardBlock(Blocks.grass, x, y, z);
   }

   public boolean shouldRender3DInInventory(int modelId) {
      return true;
   }

   public int getRenderId() {
      return Witchery.proxy.getPitGrassRenderId();
   }
}
