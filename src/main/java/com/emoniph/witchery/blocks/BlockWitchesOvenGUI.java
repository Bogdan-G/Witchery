package com.emoniph.witchery.blocks;

import com.emoniph.witchery.blocks.BlockWitchesOven;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BlockWitchesOvenGUI extends GuiContainer {

   private static final ResourceLocation field_110410_t = new ResourceLocation("witchery", "textures/gui/witchesOven.png");
   private BlockWitchesOven.TileEntityWitchesOven furnaceInventory;


   public BlockWitchesOvenGUI(InventoryPlayer par1InventoryPlayer, BlockWitchesOven.TileEntityWitchesOven par2TileEntityFurnace) {
      super(new BlockWitchesOven.ContainerWitchesOven(par1InventoryPlayer, par2TileEntityFurnace));
      this.furnaceInventory = par2TileEntityFurnace;
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      String s = this.furnaceInventory != null?(this.furnaceInventory.hasCustomInventoryName()?this.furnaceInventory.getInventoryName():I18n.format(this.furnaceInventory.getInventoryName(), new Object[0])):"";
      super.fontRendererObj.drawString(s, super.xSize / 2 - super.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
      super.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8, super.ySize - 96 + 2, 4210752);
   }

   protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      super.mc.getTextureManager().bindTexture(field_110410_t);
      int k = (super.width - super.xSize) / 2;
      int l = (super.height - super.ySize) / 2;
      this.drawTexturedModalRect(k, l, 0, 0, super.xSize, super.ySize);
      if(this.furnaceInventory != null) {
         int i1;
         if(this.furnaceInventory.isBurning()) {
            i1 = this.furnaceInventory.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
         }

         i1 = this.furnaceInventory.getCookProgressScaled(24);
         this.drawTexturedModalRect(k + 79, l + 20, 176, 14, i1 + 1, 16);
      }

   }

}
