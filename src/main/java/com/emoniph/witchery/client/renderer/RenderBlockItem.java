package com.emoniph.witchery.client.renderer;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;

public class RenderBlockItem implements IItemRenderer {

   protected TileEntitySpecialRenderer render;
   protected TileEntity dummytile;


   public RenderBlockItem(TileEntitySpecialRenderer render, TileEntity dummy) {
      this.render = render;
      this.dummytile = dummy;
   }

   public boolean handleRenderType(ItemStack item, ItemRenderType type) {
      return true;
   }

   public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
      return true;
   }

   public void renderItem(ItemRenderType type, ItemStack item, Object ... data) {
      if(type == ItemRenderType.ENTITY) {
         GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
      }

      this.render.renderTileEntityAt(this.dummytile, 0.0D, 0.0D, 0.0D, 0.0F);
   }
}
