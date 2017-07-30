package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockSilverVat;
import com.emoniph.witchery.client.model.ModelSilverVat;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderSilverVat extends TileEntitySpecialRenderer {

   final ModelSilverVat model = new ModelSilverVat();
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/blocks/silvervat.png");


   public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      BlockSilverVat.TileEntitySilverVat tileEntityYour = (BlockSilverVat.TileEntitySilverVat)tileEntity;
      this.renderSilverVat(tileEntityYour, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
      this.bindTexture(TextureMap.locationBlocksTexture);
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3008);
      int color = 3432410;
      float red = (float)(color >>> 16 & 255) / 256.0F;
      float green = (float)(color >>> 8 & 255) / 256.0F;
      float blue = (float)(color & 255) / 256.0F;
      GL11.glColor4f(red, green, blue, 1.0F);
      float w = -0.375F;
      float depth = 1.1999999F;
      GL11.glTranslatef(w, depth, -w);
      GL11.glRotatef(270.0F, 1.0F, 0.0F, 0.0F);
      float s = 0.046875F;
      GL11.glScalef(0.046875F, 0.046875F, 0.046875F);
      IIcon icon = Witchery.Blocks.BREW.getIcon(0, 0);
      boolean x = false;
      boolean y = false;
      boolean u = true;
      boolean v = true;
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.setBrightness(200);
      tessellator.addVertexWithUV(0.0D, 16.0D, 0.0D, (double)icon.getMinU(), (double)icon.getMaxV());
      tessellator.addVertexWithUV(16.0D, 16.0D, 0.0D, (double)icon.getMaxU(), (double)icon.getMaxV());
      tessellator.addVertexWithUV(16.0D, 0.0D, 0.0D, (double)icon.getMaxU(), (double)icon.getMinV());
      tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, (double)icon.getMinU(), (double)icon.getMinV());
      tessellator.draw();
      GL11.glEnable(3008);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GL11.glPopMatrix();
   }

   public void renderSilverVat(BlockSilverVat.TileEntitySilverVat te, World world, int x, int y, int z) {
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      this.bindTexture(TEXTURE_URL);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.0F, -1.0F, 0.0F);
      this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, te);
   }

}
