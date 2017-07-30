package com.emoniph.witchery.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class GuiButtonUrl extends GuiButton {

   public final String nextPage;


   public GuiButtonUrl(int commandID, int x, int y, String page, String label) {
      super(commandID, x, y, 10, 10, label);
      this.nextPage = !page.isEmpty()?page.toLowerCase(Locale.ROOT).replace(" ", ""):label.toLowerCase(Locale.ROOT).replace(" ", "");
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if(super.visible) {
         String FORMAT_CHAR = "§";
         String FORMAT_CLEAR = "§r";
         boolean flag = mouseX >= super.xPosition && mouseY >= super.yPosition && mouseX < super.xPosition + super.width && mouseY < super.yPosition + super.height;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         mc.fontRenderer.drawString("§n" + super.displayString + "§r", super.xPosition, super.yPosition, flag?16711680:255);
      }

   }
}
