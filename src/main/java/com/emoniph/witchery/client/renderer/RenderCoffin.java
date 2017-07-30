package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.blocks.BlockCoffin;
import com.emoniph.witchery.client.model.ModelCoffin;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCoffin extends TileEntitySpecialRenderer {

   final ModelCoffin model = new ModelCoffin();
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/blocks/coffin.png");


   public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      BlockCoffin.TileEntityCoffin tileEntityGoddess = (BlockCoffin.TileEntityCoffin)tileEntity;
      this.renderGoddess(tileEntityGoddess, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, f);
      GL11.glPopMatrix();
   }

   public void renderGoddess(BlockCoffin.TileEntityCoffin tile, World world, int x, int y, int z, float f) {
      GL11.glPushMatrix();
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      this.bindTexture(TEXTURE_URL);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(0.0F, -0.1F, 0.0F);
      if(world != null) {
         int meta = world.getBlockMetadata(x, y, z);
         int direction = BlockCoffin.getDirection(meta);
         float rotation = 0.0F;
         switch(direction) {
         case 0:
            rotation = 0.0F;
            break;
         case 1:
            rotation = 90.0F;
            break;
         case 2:
            rotation = 180.0F;
            break;
         case 3:
            rotation = 270.0F;
         }

         GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
         if(!BlockCoffin.isBlockHeadOfBed(meta)) {
            this.model.sideLeft.rotationPointZ = 1.0F;
            this.model.sideRight.rotationPointZ = 1.0F;
            this.model.base.rotationPointZ = 1.0F;
            this.model.sideEnd.rotateAngleY = 3.1415927F;
            this.model.lidTop.rotationPointZ = 2.0F;
            this.model.lidMid.rotationPointZ = 1.0F;
            this.model.lid.rotateAngleY = 0.0F;
         } else {
            this.model.sideLeft.rotationPointZ = 0.0F;
            this.model.sideRight.rotationPointZ = 0.0F;
            this.model.sideEnd.rotationPointZ = 0.0F;
            this.model.sideEnd.rotateAngleY = 0.0F;
            this.model.base.rotationPointZ = 0.0F;
            this.model.lidTop.rotationPointZ = 0.0F;
            this.model.lidMid.rotationPointZ = 0.0F;
         }

         float f1 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * f;
         f1 = 1.0F - f1;
         f1 = 1.0F - f1 * f1 * f1;
         this.model.lid.rotateAngleZ = -(f1 * 3.1415927F / 2.0F);
      }

      this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      GL11.glPopMatrix();
   }

}
