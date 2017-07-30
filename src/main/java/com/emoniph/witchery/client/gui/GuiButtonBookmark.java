package com.emoniph.witchery.client.gui;

import com.emoniph.witchery.client.gui.GuiScreenWitchcraftBook;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class GuiButtonBookmark extends GuiButton {

   public final int nextPage;


   public GuiButtonBookmark(int commandID, int x, int y, int pageIndex, String label) {
      super(commandID, x, y, 60, 11, label);
      this.nextPage = pageIndex;
   }

   public void drawButton(Minecraft mc, int par2, int par3) {
      if(super.visible) {
         boolean flag = par2 >= super.xPosition && par3 >= super.yPosition && par2 < super.xPosition + super.width && par3 < super.yPosition + super.height;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         mc.getTextureManager().bindTexture(GuiScreenWitchcraftBook.DOUBLE_BOOK_TEXTURE);
         this.drawTexturedModalRect(super.xPosition, super.yPosition, 26, 220 + (flag?super.height:0), super.width, super.height);
         mc.fontRenderer.drawString(super.displayString, super.xPosition + 2, super.yPosition + 2, 0);
      }

   }
}
