package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.blocks.BlockBeartrap;
import com.emoniph.witchery.client.model.ModelBeartrap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBeartrap extends TileEntitySpecialRenderer {

   final ModelBeartrap model = new ModelBeartrap();
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/blocks/beartrap.png");


   public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float t) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)x, (float)y, (float)z);
      BlockBeartrap.TileEntityBeartrap mantrap = (BlockBeartrap.TileEntityBeartrap)tile;
      this.renderTileEntityAt(mantrap, tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
      GL11.glPopMatrix();
   }

   public void renderTileEntityAt(BlockBeartrap.TileEntityBeartrap tile, World world, int x, int y, int z) {
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.0F, -1.0F, 0.0F);
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
            rotation = 270.0F;
            break;
         case 5:
            rotation = 90.0F;
         }

         GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
      }

      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3008);
      this.bindTexture(TEXTURE_URL);
      this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, tile);
      GL11.glEnable(3008);
      GL11.glDisable(3042);
   }

}
