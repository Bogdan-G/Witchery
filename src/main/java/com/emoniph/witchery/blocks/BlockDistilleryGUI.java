package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockDistillery;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BlockDistilleryGUI extends GuiContainer {

   private static final ResourceLocation IMAGE_URL = new ResourceLocation("witchery", "textures/gui/distiller.png");
   private BlockDistillery.TileEntityDistillery furnaceInventory;


   public BlockDistilleryGUI(InventoryPlayer par1InventoryPlayer, BlockDistillery.TileEntityDistillery par2TileEntityFurnace) {
      super(new BlockDistillery.ContainerDistillery(par1InventoryPlayer, par2TileEntityFurnace));
      this.furnaceInventory = par2TileEntityFurnace;
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      String s = this.furnaceInventory.hasCustomInventoryName()?this.furnaceInventory.getInventoryName():I18n.format(this.furnaceInventory.getInventoryName(), new Object[0]);
      super.fontRendererObj.drawString(s, super.xSize / 2 - super.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
      super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      super.mc.getTextureManager().bindTexture(IMAGE_URL);
      int k = (super.width - super.xSize) / 2;
      int l = (super.height - super.ySize) / 2;
      this.drawTexturedModalRect(k, l, 0, 0, super.xSize, super.ySize);
      int brewTime = this.furnaceInventory.getCookProgressScaled(38);
      if(this.furnaceInventory.powerLevel <= 0) {
         this.drawTexturedModalRect(k + 35, l + 58, 197, 0, 9, 9);
      }

      if(brewTime > 0) {
         this.drawTexturedModalRect(k + 68, l + 14, 176, 29, brewTime, 35);
         int k1 = (800 - this.furnaceInventory.furnaceCookTime) / 2 % 7;
         switch(k1) {
         case 0:
            brewTime = 29;
            break;
         case 1:
            brewTime = 24;
            break;
         case 2:
            brewTime = 20;
            break;
         case 3:
            brewTime = 16;
            break;
         case 4:
            brewTime = 11;
            break;
         case 5:
            brewTime = 6;
            break;
         case 6:
            brewTime = 0;
         }

         if(brewTime > 0) {
            this.drawTexturedModalRect(k + 33, l + 20 + 29 - brewTime, 185, 29 - brewTime, 12, brewTime);
         }
      }

   }

}
