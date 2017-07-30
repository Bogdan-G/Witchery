package com.emoniph.witchery.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class GuiButtonNavigate extends GuiButton {

   private final int type;
   private final ResourceLocation texture;


   public GuiButtonNavigate(int commandID, int x, int y, int type, ResourceLocation texture) {
      super(commandID, x, y, 23, 13, "");
      this.type = type;
      this.texture = texture;
   }

   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
      if(super.visible) {
         boolean mouseOver = mouseX >= super.xPosition && mouseY >= super.yPosition && mouseX < super.xPosition + super.width && mouseY < super.yPosition + super.height;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         mc.getTextureManager().bindTexture(this.texture);
         int k = 0;
         short l = 192;
         if(mouseOver) {
            k += 23;
         }

         int l1 = l + 13 * this.type;
         this.drawTexturedModalRect(super.xPosition, super.yPosition, k, l1, 23, 13);
      }

   }
}
