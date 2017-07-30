package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockSpinningWheel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BlockSpinningWheelGUI extends GuiContainer {

   private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("witchery", "textures/gui/spinningwheel.png");
   private BlockSpinningWheel.TileEntitySpinningWheel spinningWheel;


   public BlockSpinningWheelGUI(InventoryPlayer playerInventory, BlockSpinningWheel.TileEntitySpinningWheel spinningWheel) {
      super(new BlockSpinningWheel.ContainerSpinningWheel(playerInventory, spinningWheel));
      this.spinningWheel = spinningWheel;
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      String s = this.spinningWheel.hasCustomInventoryName()?this.spinningWheel.getInventoryName():I18n.format(this.spinningWheel.getInventoryName(), new Object[0]);
      super.fontRendererObj.drawString(s, super.xSize / 2 - super.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
      super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      super.mc.getTextureManager().bindTexture(TEXTURE_LOCATION);
      int k = (super.width - super.xSize) / 2;
      int l = (super.height - super.ySize) / 2;
      this.drawTexturedModalRect(k, l, 0, 0, super.xSize, super.ySize);
      if(this.spinningWheel.powerLevel <= 0) {
         this.drawTexturedModalRect(k + 35, l + 58, 197, 0, 9, 9);
      }

      int i1 = this.spinningWheel.getCookProgressScaled(24);
      this.drawTexturedModalRect(k + 79, l + 20, 176, 14, i1 + 1, 16);
   }

}
