package com.emoniph.witchery.client.gui;

import com.emoniph.witchery.client.gui.GuiScreenWitchcraftBook;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
class GuiButtonNextPage extends GuiButton {

   private final boolean nextPage;


   public GuiButtonNextPage(int par1, int par2, int par3, boolean par4) {
      super(par1, par2, par3, 23, 13, "");
      this.nextPage = par4;
   }

   public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
      if(super.visible) {
         boolean flag = par2 >= super.xPosition && par3 >= super.yPosition && par2 < super.xPosition + super.width && par3 < super.yPosition + super.height;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         par1Minecraft.getTextureManager().bindTexture(GuiScreenWitchcraftBook.func_110404_g());
         int k = 0;
         int l = 192;
         if(flag) {
            k += 23;
         }

         if(!this.nextPage) {
            l += 13;
         }

         this.drawTexturedModalRect(super.xPosition, super.yPosition, k, l, 23, 13);
      }

   }
}
