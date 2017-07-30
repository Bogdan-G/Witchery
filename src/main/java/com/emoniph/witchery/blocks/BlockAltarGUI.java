package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockAltar;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BlockAltarGUI extends GuiScreen {

   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery:textures/gui/altar.png");
   private final BlockAltar.TileEntityAltar tileEntity;
   private static final int SIZE_OF_TEXTURE_X = 176;
   private static final int SIZE_OF_TEXTURE_Y = 88;


   public BlockAltarGUI(BlockAltar.TileEntityAltar tileEntity) {
      this.tileEntity = tileEntity;
   }

   public void drawScreen(int x, int y, float f) {
      this.drawDefaultBackground();
      super.mc.getTextureManager().bindTexture(TEXTURE_URL);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      int posX = (super.width - 176) / 2;
      int posY = (super.height - 88) / 2;
      this.drawTexturedModalRect(posX, posY, 0, 0, 176, 88);
      this.drawCenteredString(super.fontRendererObj, "Altar", super.width / 2, super.height / 2 - 20, 16777215);
      String power = String.format("%.0f / %.0f (x%d)", new Object[]{Float.valueOf(this.tileEntity.getCorePower()), Float.valueOf(this.tileEntity.getCoreMaxPower()), Integer.valueOf(this.tileEntity.getCoreSpeed())});
      this.drawCenteredString(super.fontRendererObj, power, super.width / 2, super.height / 2, 16777215);
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   protected void keyTyped(char par1, int par2) {
      if(par2 == 1 || par2 == super.mc.gameSettings.keyBindInventory.getKeyCode()) {
         super.mc.thePlayer.closeScreen();
      }

   }

}
