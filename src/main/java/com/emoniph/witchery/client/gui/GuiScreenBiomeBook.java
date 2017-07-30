package com.emoniph.witchery.client.gui;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.client.gui.GuiButtonBookmark;
import com.emoniph.witchery.client.gui.GuiButtonJumpPage;
import com.emoniph.witchery.client.gui.GuiButtonNextPage;
import com.emoniph.witchery.item.ItemBook;
import com.emoniph.witchery.network.PacketItemUpdate;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenBiomeBook extends GuiScreen {

   private static final ResourceLocation field_110405_a = new ResourceLocation("textures/gui/book.png");
   private final EntityPlayer player;
   private final ItemStack itemstack;
   private int updateCount;
   private int bookImageWidth = 192;
   private int bookImageHeight = 192;
   private int pageIndex;
   private NBTTagList bookPages;
   private String bookTitle = "";
   private GuiButtonNextPage buttonNextPage;
   private GuiButtonNextPage buttonPreviousPage;
   private GuiButton buttonDone;
   private GuiButtonJumpPage buttonJumpPage1;
   private GuiButtonJumpPage buttonJumpPage2;
   private GuiButtonJumpPage buttonJumpPage3;
   private GuiButtonJumpPage buttonJumpPage4;
   private GuiButtonJumpPage buttonJumpPage5;
   private GuiButtonJumpPage buttonJumpPage6;
   private GuiButtonJumpPage buttonJumpPage7;
   ArrayList biomes = new ArrayList();
   ArrayList sections = new ArrayList();
   ArrayList sectionNames = new ArrayList();


   public GuiScreenBiomeBook(EntityPlayer player, ItemStack itemstack) {
      this.player = player;
      this.itemstack = itemstack;
      this.bookTitle = itemstack.getDisplayName();
      Type[] arr$ = ItemBook.BIOME_TYPES;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Type biomeType = arr$[i$];
         this.addBiomes(biomeType);
      }

      this.pageIndex = ItemBook.getSelectedBiome(itemstack, this.biomes.size());
   }

   private void addBiomes(Type biomeType) {
      BiomeGenBase[] biomesInType = BiomeDictionary.getBiomesForType(biomeType);
      this.sections.add(Integer.valueOf(this.biomes.size()));
      this.sectionNames.add(Witchery.resource("witchery.book.biomes." + biomeType.toString().toLowerCase() + ".name"));

      for(int i = 0; i < biomesInType.length; ++i) {
         this.biomes.add(biomesInType[i]);
      }

   }

   private void storeCurrentPage() {
      ItemBook.setSelectedBiome(this.itemstack, this.pageIndex);
   }

   public void updateScreen() {
      super.updateScreen();
      ++this.updateCount;
   }

   public void initGui() {
      super.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      byte b0 = 2;
      int mid = (super.width - this.bookImageWidth) / 2;
      super.buttonList.add(this.buttonNextPage = new GuiButtonNextPage(1, mid + 120, b0 + 154, true));
      super.buttonList.add(this.buttonPreviousPage = new GuiButtonNextPage(2, mid + 38, b0 + 154, false));

      for(int i = this.sections.size() - 1; i >= 0; --i) {
         GuiButtonBookmark button = new GuiButtonBookmark(3 + i, mid + 160, 12 * i + 10, ((Integer)this.sections.get(i)).intValue(), (String)this.sectionNames.get(i));
         button.enabled = true;
         super.buttonList.add(button);
      }

      this.updateButtons();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.sendBookToServer(false);
   }

   private void updateButtons() {
      this.buttonNextPage.visible = this.pageIndex < this.biomes.size() - 1;
      this.buttonPreviousPage.visible = this.pageIndex > 0;
   }

   private void sendBookToServer(boolean par1) {
      if(this.player != null && this.pageIndex >= 0 && this.pageIndex < 1000 && this.player.inventory.currentItem >= 0 && this.player.inventory.getCurrentItem() != null) {
         Witchery.packetPipeline.sendToServer(new PacketItemUpdate(this.player.inventory.currentItem, this.pageIndex, this.player.inventory.getCurrentItem()));
      }

   }

   protected void actionPerformed(GuiButton button) {
      if(button.enabled) {
         if(button.id == 0) {
            super.mc.displayGuiScreen((GuiScreen)null);
         } else if(button.id == 1) {
            if(this.pageIndex < this.biomes.size() - 1) {
               ++this.pageIndex;
               this.storeCurrentPage();
            }
         } else if(button.id == 2) {
            if(this.pageIndex > 0) {
               --this.pageIndex;
               this.storeCurrentPage();
            }
         } else if(button instanceof GuiButtonBookmark) {
            GuiButtonBookmark but = (GuiButtonBookmark)button;
            this.pageIndex = but.nextPage;
            this.storeCurrentPage();
         }

         this.updateButtons();
      }

   }

   protected void keyTyped(char par1, int par2) {
      super.keyTyped(par1, par2);
   }

   public void drawScreen(int par1, int par2, float par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      super.mc.getTextureManager().bindTexture(field_110405_a);
      int k = (super.width - this.bookImageWidth) / 2;
      byte b0 = 2;
      this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
      if(this.biomes.size() > 0 && this.pageIndex >= 0 && this.pageIndex < this.biomes.size()) {
         String pageNumberText = I18n.format("book.pageIndicator", new Object[]{Integer.valueOf(this.pageIndex + 1), Integer.valueOf(this.biomes.size())});
         int pageNumberTextWitdh = super.fontRendererObj.getStringWidth(pageNumberText);
         super.fontRendererObj.drawString(pageNumberText, k - pageNumberTextWitdh + this.bookImageWidth - 44, b0 + 16, 0);
         BiomeGenBase biome = (BiomeGenBase)this.biomes.get(this.pageIndex);
         boolean maxWidth = true;
         boolean defaultColor = false;
         byte b01 = (byte)(b0 + this.drawSpiltString(biome.biomeName, k + 36, b0 + 32, 116, 0));
         b01 = (byte)(b01 + super.fontRendererObj.FONT_HEIGHT);
         b01 = (byte)(b01 + this.drawSpiltString("> " + String.format(Witchery.resource("witchery.biomebook.rainfall"), new Object[]{Float.valueOf(biome.rainfall)}), k + 36, b01 + 32, 116, 0));
         String temperatureFormat = Witchery.resource(biome.isHighHumidity()?"witchery.biomebook.temperaturehot":"witchery.biomebook.temperature");
         b01 = (byte)(b01 + this.drawSpiltString("> " + String.format(temperatureFormat, new Object[]{Float.valueOf(biome.temperature)}), k + 36, b01 + 32, 116, 0));
         b01 = (byte)(b01 + this.drawSpiltString("> " + String.format(Witchery.resource("witchery.biomebook.snows"), new Object[]{this.toYesNo(biome.getEnableSnow())}), k + 36, b01 + 32, 116, 0));
         byte var10000 = (byte)(b01 + this.drawSpiltString("> " + String.format(Witchery.resource("witchery.biomebook.lightning"), new Object[]{this.toYesNo(biome.canSpawnLightningBolt())}), k + 36, b01 + 32, 116, 0));
      }

      super.drawScreen(par1, par2, par3);
   }

   private int drawSpiltString(String text, int x, int y, int maxWidth, int color) {
      int height = super.fontRendererObj.splitStringWidth(text, maxWidth);
      super.fontRendererObj.drawSplitString(text, x, y, maxWidth, color);
      return height;
   }

   private String toYesNo(boolean val) {
      return Witchery.resource(val?"witchery.yes":"witchery.no");
   }

   public static void drawTexturedQuadFit(double x, double y, double width, double height, double zLevel) {
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV(x + 0.0D, y + height, zLevel, 0.0D, 1.0D);
      tessellator.addVertexWithUV(x + width, y + height, zLevel, 1.0D, 1.0D);
      tessellator.addVertexWithUV(x + width, y + 0.0D, zLevel, 1.0D, 0.0D);
      tessellator.addVertexWithUV(x + 0.0D, y + 0.0D, zLevel, 0.0D, 0.0D);
      tessellator.draw();
   }

   static ResourceLocation func_110404_g() {
      return field_110405_a;
   }

}
