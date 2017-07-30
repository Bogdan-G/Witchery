package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.blocks.BlockWolfHead;
import com.emoniph.witchery.client.model.ModelWolfHead;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWolfHead extends TileEntitySpecialRenderer {

   private static final ResourceLocation WOLF_TEXTURE = new ResourceLocation("textures/entity/wolf/wolf.png");
   private static final ResourceLocation HELLHOUND_TEXTURE = new ResourceLocation("witchery", "textures/entities/hellhound.png");
   public static RenderWolfHead field_147536_b;
   private ModelWolfHead field_147533_g = new ModelWolfHead(0, 0, 64, 32);
   private ModelWolfHead field_147538_h = new ModelWolfHead(0, 0, 64, 64);


   public void renderTileEntityAt(BlockWolfHead.TileEntityWolfHead tile, double x, double y, double z, float partialTicks) {
      this.render((float)x, (float)y, (float)z, tile.getBlockMetadata() & 7, (float)(tile.getRotation() * 360) / 16.0F, tile.getSkullType());
   }

   public void func_147497_a(TileEntityRendererDispatcher p_147497_1_) {
      super.func_147497_a(p_147497_1_);
      field_147536_b = this;
   }

   public void render(float x, float y, float z, int metadata, float rotation, int skullType) {
      ModelWolfHead modelskeletonhead = this.field_147533_g;
      switch(skullType) {
      case 0:
      default:
         this.bindTexture(WOLF_TEXTURE);
         break;
      case 1:
         this.bindTexture(HELLHOUND_TEXTURE);
      }

      GL11.glPushMatrix();
      GL11.glDisable(2884);
      if(metadata != 1) {
         switch(metadata) {
         case 2:
            GL11.glTranslatef(x + 0.5F, y + 0.25F, z + 0.74F);
            break;
         case 3:
            GL11.glTranslatef(x + 0.5F, y + 0.25F, z + 0.26F);
            rotation = 180.0F;
            break;
         case 4:
            GL11.glTranslatef(x + 0.74F, y + 0.25F, z + 0.5F);
            rotation = 270.0F;
            break;
         case 5:
         default:
            GL11.glTranslatef(x + 0.26F, y + 0.25F, z + 0.5F);
            rotation = 90.0F;
         }
      } else {
         GL11.glTranslatef(x + 0.5F, y, z + 0.5F);
      }

      float f4 = 0.0625F;
      GL11.glEnable('\u803a');
      GL11.glScalef(-1.0F, -1.0F, 1.0F);
      GL11.glEnable(3008);
      modelskeletonhead.render((Entity)null, 0.0F, 0.0F, 0.0F, rotation, 0.0F, f4);
      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
      this.renderTileEntityAt((BlockWolfHead.TileEntityWolfHead)tile, x, y, z, partialTicks);
   }

}
