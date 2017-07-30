package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockPoppetShelf;
import com.emoniph.witchery.client.model.ModelPoppetChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPoppetChest extends TileEntitySpecialRenderer {

   final ModelPoppetChest model = new ModelPoppetChest();
   private RenderItem renderItems = new RenderItem() {
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
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/blocks/poppetShelf.png");


   public RenderPoppetChest() {
      this.renderItems.setRenderManager(RenderManager.instance);
   }

   public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      BlockPoppetShelf.TileEntityPoppetShelf tileEntityYour = (BlockPoppetShelf.TileEntityPoppetShelf)tileEntity;
      this.renderPoppetChest(tileEntityYour, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, Witchery.Blocks.POPPET_SHELF);
      GL11.glPopMatrix();
   }

   public void renderPoppetChest(BlockPoppetShelf.TileEntityPoppetShelf te, World world, int x, int y, int z, Block block) {
      GL11.glPushMatrix();
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      this.bindTexture(TEXTURE_URL);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
      this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      GL11.glPopMatrix();
      if(world != null) {
         ItemStack newStack = null;
         float rotational = (float)Minecraft.getSystemTime() / 3000.0F * 300.0F;
         EntityItem ei = new EntityItem(world);
         ei.hoverStart = 0.0F;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPushMatrix();
         GL11.glEnable('\u803a');
         GL11.glTranslatef(0.0F, 0.0F, 0.0F);
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 170.0F, 170.0F);
         GL11.glTranslatef(0.0F, 0.6F, 0.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glScalef(0.5F, 0.5F, 0.5F);
         float zShift = 0.0F;
         float yShift = 0.0F;
         float xShift = 0.0F;
         zShift = 0.5F;
         boolean fancy = Witchery.proxy.getGraphicsLevel();

         for(int i = 0; i < te.getSizeInventory() && i <= 46; ++i) {
            xShift += 0.5F;
            if(i == 3 || i == 6 || i == 9) {
               zShift += 0.5F;
               xShift = 0.5F;
            }

            if(te.getStackInSlot(i) != null && te.getStackInSlot(i).getItem() == Witchery.Items.POPPET) {
               newStack = te.getStackInSlot(i).copy();
               newStack.stackSize = 1;
               ei.setEntityItemStack(newStack);
               GL11.glPushMatrix();
               GL11.glTranslatef(xShift, yShift, zShift);
               if(fancy) {
                  GL11.glRotatef(rotational / 2.0F, 0.0F, 1.0F, 0.0F);
               }

               GL11.glPushMatrix();
               this.renderItems.doRender(ei, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
               GL11.glPopMatrix();
               GL11.glPopMatrix();
            }
         }

         GL11.glDisable('\u803a');
         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

}
