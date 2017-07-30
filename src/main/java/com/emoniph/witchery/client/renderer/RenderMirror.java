package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.blocks.BlockMirror;
import com.emoniph.witchery.client.model.ModelMirror;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMirror extends TileEntitySpecialRenderer {

   final ModelMirror model = new ModelMirror();
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/blocks/mirror.png");
   private static final ResourceLocation TEXTURE2_URL = new ResourceLocation("witchery", "textures/blocks/mirror2.png");


   public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      BlockMirror.TileEntityMirror tileEntityYour = (BlockMirror.TileEntityMirror)tileEntity;
      this.renderMirror(tileEntityYour, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
      GL11.glPopMatrix();
   }

   public void renderMirror(BlockMirror.TileEntityMirror te, World world, int x, int y, int z) {
      GL11.glPushMatrix();
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      this.bindTexture(TEXTURE_URL);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      if(world != null) {
         int meta = world.getBlockMetadata(x, y, z);
         int facing = BlockMirror.getDirection(meta);
         float rotation = 0.0F;
         switch(facing) {
         case 0:
            rotation = 0.0F;
            break;
         case 1:
            rotation = 180.0F;
            break;
         case 2:
            rotation = 270.0F;
            break;
         case 3:
            rotation = 90.0F;
         }

         GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
         if(!BlockMirror.isBlockTopOfMirror(meta)) {
            GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
         }

         GL11.glTranslatef(0.0F, 0.1F, 0.42F);
      }

      GL11.glTranslatef(0.0F, -0.1F, 0.02F);
      this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      if(te.men > 0) {
         this.bindTexture(TEXTURE2_URL);
         this.model.backMiddle.render(0.0625F);
      }

      GL11.glPopMatrix();
   }

}
