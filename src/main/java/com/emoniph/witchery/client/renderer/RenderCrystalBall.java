package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.blocks.BlockCrystalBall;
import com.emoniph.witchery.client.model.ModelCrystalBall;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCrystalBall extends TileEntitySpecialRenderer {

   final ModelCrystalBall model = new ModelCrystalBall();
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/blocks/crystalball.png");


   public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      BlockCrystalBall.TileEntityCrystalBall ball = (BlockCrystalBall.TileEntityCrystalBall)tileEntity;
      this.renderCrystalBall(ball, ball.getWorldObj(), ball.xCoord, ball.yCoord, ball.zCoord);
      GL11.glPopMatrix();
   }

   public void renderCrystalBall(BlockCrystalBall.TileEntityCrystalBall ball, World world, int x, int y, int z) {
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      this.bindTexture(TEXTURE_URL);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.0F, -1.0F, 0.0F);
      this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F, ball);
   }

}
