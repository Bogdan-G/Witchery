package com.emoniph.witchery.item;

import com.emoniph.witchery.item.ItemBrewBag;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ItemBrewBagGUI extends GuiContainer {

   private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("witchery", "textures/gui/generic_48.png");
   private IInventory upperInventory;
   private IInventory lowerInventory;
   private int inventoryRows;


   public ItemBrewBagGUI(IInventory inventoryPlayer, IInventory inventoryBag) {
      super(new ItemBrewBag.ContainerBrewBag(inventoryPlayer, inventoryBag, (ItemStack)null));
      this.upperInventory = inventoryBag;
      this.lowerInventory = inventoryPlayer;
      this.inventoryRows = inventoryBag.getSizeInventory() / 8;
      super.ySize = 114 + this.inventoryRows * 18;
   }

   protected void drawGuiContainerForegroundLayer(int par1, int par2) {
      super.fontRendererObj.drawString(StatCollector.translateToLocal(this.upperInventory.getInventoryName()), 8, 6, 4210752);
      super.fontRendererObj.drawString(StatCollector.translateToLocal(this.lowerInventory.getInventoryName()), 8, super.ySize - 96 + 2, 4210752);
   }

   protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      super.mc.renderEngine.bindTexture(TEXTURE_LOCATION);
      int var5 = (super.width - super.xSize) / 2;
      int var6 = (super.height - super.ySize) / 2;
      this.drawTexturedModalRect(var5, var6, 0, 0, super.xSize, this.inventoryRows * 18 + 17);
      this.drawTexturedModalRect(var5, var6 + this.inventoryRows * 18 + 17, 0, 126, super.xSize, 96);
   }

}
