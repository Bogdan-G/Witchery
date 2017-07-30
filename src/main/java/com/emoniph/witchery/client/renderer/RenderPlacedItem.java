package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.blocks.BlockPlacedItem;
import com.emoniph.witchery.client.renderer.RenderItem3d;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class RenderPlacedItem extends TileEntitySpecialRenderer {

   private final RenderItem3d renderItems = new RenderItem3d(true) {
      public byte getMiniItemCountForItemStack(ItemStack stack) {
         return (byte)1;
      }
      public byte getMiniBlockCountForItemStack(ItemStack stack) {
         return (byte)1;
      }
      public boolean shouldBob() {
         return false;
      }
      public boolean shouldSpreadItems() {
         return false;
      }
   };


   public RenderPlacedItem() {
      this.renderItems.setRenderManager(RenderManager.instance);
   }

   public void renderTileEntityAt(TileEntity tileEntity, double d0, double d1, double d2, float f) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d0, (float)d1, (float)d2);
      this.renderPlacedItem((BlockPlacedItem.TileEntityPlacedItem)tileEntity, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
      GL11.glPopMatrix();
   }

   public void renderPlacedItem(BlockPlacedItem.TileEntityPlacedItem te, World world, int x, int y, int z) {
      if(world != null) {
         EntityItem ei = new EntityItem(world);
         ei.hoverStart = 0.0F;
         if(te != null && te.getStack() != null) {
            ei.setEntityItemStack(te.getStack().copy());
         } else {
            ei.setEntityItemStack(new ItemStack(Items.bone));
         }

         GL11.glTranslatef(0.5F, 0.05F, 0.5F);
         if(world != null) {
            int meta = world.getBlockMetadata(x, y, z);
            float rotation = 0.0F;
            switch(meta) {
            case 2:
               rotation = 0.0F;
               break;
            case 3:
               rotation = 180.0F;
               break;
            case 4:
               rotation = 90.0F;
               break;
            case 5:
               rotation = 270.0F;
            }

            GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
         }

         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         GL11.glTranslatef(0.0F, -0.1F, 0.0F);
         this.renderItems.doRender(ei, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
      }

   }
}
