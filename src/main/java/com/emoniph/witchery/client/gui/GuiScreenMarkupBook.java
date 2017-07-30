package com.emoniph.witchery.client.gui;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.client.gui.GuiButtonNavigate;
import com.emoniph.witchery.client.gui.GuiButtonUrl;
import com.emoniph.witchery.network.PacketSyncMarkupBook;
import com.emoniph.witchery.util.NBT;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenMarkupBook extends GuiScreen {

   private static final ResourceLocation BACKGROUND = new ResourceLocation("witchery:textures/gui/bookSingle.png");
   private final EntityPlayer player;
   private final ItemStack itemstack;
   private final int meta;
   private int updateCount;
   private int bookImageWidth = 192;
   private int bookImageHeight = 192;
   private GuiButtonNavigate buttonTopPage;
   private GuiButtonNavigate buttonPreviousPage;
   private GuiButtonNavigate buttonNextPage;
   private final List pageStack = new ArrayList();
   final List elements = new ArrayList();
   private GuiScreenMarkupBook.NextPage nextPage;


   public GuiScreenMarkupBook(EntityPlayer player, ItemStack itemstack) {
      this.player = player;
      this.itemstack = itemstack;
      this.meta = itemstack != null?itemstack.getItemDamage():0;
      NBTTagList nbtPageStack = NBT.get(itemstack).getTagList("pageStack", 8);

      for(int i = 0; i < nbtPageStack.tagCount(); ++i) {
         this.pageStack.add(nbtPageStack.getStringTagAt(i));
      }

   }

   public void updateScreen() {
      super.updateScreen();
      ++this.updateCount;
   }

   public void initGui() {
      Keyboard.enableRepeatEvents(true);
      this.constructPage();
   }

   private void constructPage() {
      String page = this.pageStack.size() > 0?(String)this.pageStack.get(this.pageStack.size() - 1):"toc";
      super.buttonList.clear();
      this.elements.clear();
      byte b0 = 2;
      int mid = (super.width - this.bookImageWidth) / 2;
      super.buttonList.add(this.buttonTopPage = new GuiButtonNavigate(1, mid + 120, b0 + 16, 2, BACKGROUND));
      super.buttonList.add(this.buttonPreviousPage = new GuiButtonNavigate(2, mid + 34, b0 + 16, 1, BACKGROUND));
      super.buttonList.add(this.buttonNextPage = new GuiButtonNavigate(3, mid + 120, b0 + 16, 0, BACKGROUND));
      String itemName = Item.itemRegistry.getNameForObject(this.itemstack.getItem());
      String untranslated = itemName + "." + page;
      StringBuilder markup = new StringBuilder(StatCollector.translateToLocal(untranslated));
      if(markup != null && !markup.toString().equals(untranslated)) {
         for(int i$ = 0; i$ < markup.length(); ++i$) {
            char element = markup.charAt(i$);
            switch(element) {
            case 91:
               this.elements.add(new GuiScreenMarkupBook.Element((GuiScreenMarkupBook.NamelessClass348268086)null));
               ((GuiScreenMarkupBook.Element)this.elements.get(this.elements.size() - 1)).append(element);
               break;
            case 93:
               GuiScreenMarkupBook.Element defaultNextPage = (GuiScreenMarkupBook.Element)this.elements.get(this.elements.size() - 1);
               if(defaultNextPage.tag.toString().equals("template")) {
                  String templatePathRoot = Item.itemRegistry.getNameForObject(this.itemstack.getItem());
                  String templatePath = templatePathRoot + "." + defaultNextPage.attribute;
                  String template = StatCollector.translateToLocal(templatePath);
                  if(!template.isEmpty()) {
                     String[] parms = defaultNextPage.text.toString().split("\\s");
                     Object[] components = new Object[parms.length];

                     for(int j = 0; j < parms.length; ++j) {
                        String[] kv = parms[j].split("=");
                        if(kv.length == 2) {
                           if(kv[0].matches("stack\\|\\d+")) {
                              StringBuilder index = new StringBuilder();
                              String[] index1 = kv[1].split(",");
                              int len$ = index1.length;

                              for(int i$1 = 0; i$1 < len$; ++i$1) {
                                 String stack = index1[i$1];
                                 index.append(String.format("[stack=%s]", new Object[]{stack}));
                              }

                              int var26 = Math.min(Integer.parseInt(kv[0].substring(kv[0].indexOf(124) + 1)), components.length - 1);
                              components[var26] = index.toString();
                           } else if(kv[0].matches("\\d+")) {
                              int var25 = Math.min(Integer.parseInt(kv[0]), components.length - 1);
                              components[var25] = kv[1];
                           }
                        }
                     }

                     markup.insert(i$ + 1, String.format(template, components));
                     this.elements.remove(this.elements.size() - 1);
                  }
               }

               this.elements.add(new GuiScreenMarkupBook.Element((GuiScreenMarkupBook.NamelessClass348268086)null));
               break;
            default:
               if(this.elements.size() == 0) {
                  this.elements.add(new GuiScreenMarkupBook.Element((GuiScreenMarkupBook.NamelessClass348268086)null));
               }

               ((GuiScreenMarkupBook.Element)this.elements.get(this.elements.size() - 1)).append(element);
            }
         }

         this.nextPage = null;
         Iterator var22 = this.elements.iterator();

         while(var22.hasNext()) {
            GuiScreenMarkupBook.Element var23 = (GuiScreenMarkupBook.Element)var22.next();
            GuiScreenMarkupBook.NextPage var24 = var23.constructButtons(super.buttonList, this.itemstack);
            if(var24 != null) {
               this.nextPage = var24;
            }
         }

         this.updateButtons();
      }
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.sendBookToServer();
   }

   private void updateButtons() {
      this.buttonNextPage.visible = this.nextPage != null && this.nextPage.visible;
      this.buttonPreviousPage.visible = this.pageStack.size() > 0;
      this.buttonTopPage.visible = this.pageStack.size() > 0;
   }

   private void sendBookToServer() {
      if(this.player != null) {
         Witchery.packetPipeline.sendToServer(new PacketSyncMarkupBook(this.player.inventory.currentItem, this.pageStack));
      }

   }

   protected void actionPerformed(GuiButton button) {
      if(button.enabled) {
         if(button.id == 0) {
            super.mc.displayGuiScreen((GuiScreen)null);
         } else if(button.id == 1) {
            if(this.pageStack.size() > 0) {
               this.pageStack.remove(this.pageStack.size() - 1);

               for(int i = this.pageStack.size() - 1; i >= 0 && !((String)this.pageStack.get(i)).startsWith("toc/"); --i) {
                  this.pageStack.remove(i);
               }
            }

            this.constructPage();
         } else if(button.id == 2) {
            if(this.pageStack.size() > 0) {
               this.pageStack.remove(this.pageStack.size() - 1);
               this.constructPage();
            }
         } else if(button.id == 3) {
            this.pageStack.add(this.nextPage.pageName);
            this.constructPage();
         } else if(button.id == 4) {
            this.pageStack.add(((GuiButtonUrl)button).nextPage);
            this.constructPage();
         }

         this.updateButtons();
      }

   }

   protected void keyTyped(char par1, int par2) {
      super.keyTyped(par1, par2);
   }

   public void drawScreen(int mouseX, int mouseY, float par3) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      super.mc.getTextureManager().bindTexture(BACKGROUND);
      int k = (super.width - this.bookImageWidth) / 2;
      byte b0 = 2;
      this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
      boolean maxWidth = true;
      int marginX = k + 36;
      this.buttonPreviousPage.xPosition = marginX;
      this.buttonPreviousPage.yPosition = 16;
      this.buttonTopPage.xPosition = k + this.bookImageWidth / 2 - this.buttonTopPage.width / 2 - 4;
      this.buttonTopPage.yPosition = 16;
      this.buttonNextPage.xPosition = k + this.bookImageWidth - this.buttonNextPage.width - 44;
      this.buttonNextPage.yPosition = 16;
      int[] pos = new int[]{0, 32};
      GuiScreenMarkupBook.RenderState state = new GuiScreenMarkupBook.RenderState(super.fontRendererObj, super.zLevel, mouseX, mouseY);
      Iterator i$ = this.elements.iterator();

      while(i$.hasNext()) {
         GuiScreenMarkupBook.Element element = (GuiScreenMarkupBook.Element)i$.next();
         element.draw(pos, marginX, 116, state);
      }

      super.drawScreen(mouseX, mouseY, par3);
      if(state.tooltipStack != null) {
         this.renderToolTip(state.tooltipStack, mouseX, mouseY + 16);
      }

   }

   protected void renderToolTip(ItemStack stack, int x, int y) {
      List list = stack.getTooltip(super.mc.thePlayer, super.mc.gameSettings.advancedItemTooltips);
      int font;
      if(list != null) {
         font = WitcheryBrewRegistry.INSTANCE.getAltarPower(stack);
         if(font >= 0) {
            list.add(String.format(Witchery.resource("witchery.brewing.ingredientpowercost"), new Object[]{Integer.valueOf(font), Integer.valueOf(MathHelper.ceiling_double_int(1.4D * (double)font))}));
         }
      }

      for(font = 0; font < list.size(); ++font) {
         if(font == 0) {
            list.set(font, stack.getRarity().rarityColor + (String)list.get(font));
         } else {
            list.set(font, EnumChatFormatting.GRAY + (String)list.get(font));
         }
      }

      FontRenderer var6 = stack.getItem().getFontRenderer(stack);
      this.drawHoveringText(list, x, y, var6 == null?super.fontRendererObj:var6);
   }


   private static class Element {

      private final StringBuilder tag;
      private final StringBuilder attribute;
      private final StringBuilder text;
      private GuiScreenMarkupBook.Element.Capture capture;
      private static final String FORMAT_CHAR = "§";
      private static final String FORMAT_CLEAR = "§r";
      private static final Hashtable FORMATS = getFormats();
      private GuiButtonUrl button;


      private Element() {
         this.tag = new StringBuilder();
         this.attribute = new StringBuilder();
         this.text = new StringBuilder();
         this.capture = GuiScreenMarkupBook.Element.Capture.TEXT;
      }

      public String toString() {
         return String.format("tag=%s attribute=%s text=%s", new Object[]{this.tag, this.attribute, this.text});
      }

      public void append(char c) {
         switch(c) {
         case 61:
            if(this.capture == GuiScreenMarkupBook.Element.Capture.TAG) {
               this.capture = GuiScreenMarkupBook.Element.Capture.ATTRIB;
               break;
            }
         case 9:
         case 32:
            if(this.capture == GuiScreenMarkupBook.Element.Capture.TAG || this.capture == GuiScreenMarkupBook.Element.Capture.ATTRIB) {
               this.capture = GuiScreenMarkupBook.Element.Capture.TEXT;
               break;
            }
         case 91:
            this.capture = GuiScreenMarkupBook.Element.Capture.TAG;
            break;
         default:
            if(this.capture == GuiScreenMarkupBook.Element.Capture.TAG) {
               this.tag.append(c);
            } else if(this.capture == GuiScreenMarkupBook.Element.Capture.ATTRIB) {
               this.attribute.append(c);
            } else {
               this.text.append(c);
            }
         }

      }

      private static Hashtable getFormats() {
         Hashtable formats = new Hashtable();
         formats.put("black", "§0");
         formats.put("darkblue", "§1");
         formats.put("darkgreen", "§2");
         formats.put("darkaqua", "§3");
         formats.put("darkred", "§4");
         formats.put("darkpurple", "§5");
         formats.put("darkyellow", "§6");
         formats.put("gray", "§7");
         formats.put("darkgray", "§8");
         formats.put("blue", "§9");
         formats.put("green", "§a");
         formats.put("aqua", "§b");
         formats.put("red", "§c");
         formats.put("purple", "§d");
         formats.put("yellow", "§e");
         formats.put("white", "§f");
         formats.put("b", "§l");
         formats.put("s", "§m");
         formats.put("u", "§n");
         formats.put("i", "§o");
         formats.put("h1", "§3§o");
         return formats;
      }

      public GuiScreenMarkupBook.NextPage constructButtons(List buttonList, ItemStack stack) {
         String tag = this.tag.toString();
         if(tag.equals("url")) {
            String attrib = this.attribute.toString();
            int pipeIndex = attrib.indexOf(124);
            if(pipeIndex != -1) {
               attrib = attrib.substring(0, pipeIndex);
            }

            this.button = new GuiButtonUrl(4, 0, 0, attrib, this.text.toString());
            buttonList.add(this.button);
         } else if(tag.equals("next")) {
            return new GuiScreenMarkupBook.NextPage(this.attribute.toString(), stack);
         }

         return null;
      }

      public void draw(int[] pos, int marginX, int maxWidth, GuiScreenMarkupBook.RenderState state) {
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         String tag = this.tag.toString();
         if(tag.equals("br")) {
            state.newline(pos);
         } else if(tag.equals("tab")) {
            boolean var26 = true;
            if(pos[0] + 10 > maxWidth) {
               state.newline(pos);
            } else {
               pos[0] += 10;
            }

         } else {
            int i$;
            String[] var25;
            String var29;
            if(tag.equals("img")) {
               var25 = this.attribute.toString().split("\\|");
               boolean var27 = true;
               var29 = var25.length > 0?var25[0]:"";
               String var34 = var25.length > 1?var25[1]:"left";
               String var31 = var25.length > 2?var25[2]:"top";
               i$ = var25.length > 3?this.parseInt(var25[3], 32):32;
               int var32 = var25.length > 4?this.parseInt(var25[4], i$):i$;
               if(!var29.isEmpty()) {
                  ResourceLocation var35 = new ResourceLocation(var29);
                  Minecraft.getMinecraft().getTextureManager().bindTexture(var35);
                  if(var34.equals("right")) {
                     pos[0] = maxWidth - i$;
                  } else if(var34.equals("center")) {
                     pos[0] = maxWidth / 2 - i$ / 2;
                  }

                  if(pos[0] + i$ > maxWidth) {
                     state.newline(pos);
                  }

                  int var37 = pos[1];
                  if(state.lineheight > var32) {
                     if(var31.equals("bottom")) {
                        var37 += state.lineheight - var32;
                     } else if(var31.equals("middle")) {
                        var37 += state.lineheight / 2 - var32 / 2;
                     }
                  }

                  drawTexturedQuadFit((double)(pos[0] + marginX), (double)var37, (double)i$, (double)var32, (double)state.zLevel);
                  pos[0] += i$;
                  state.adjustLineHeight(var32);
               }

            } else {
               int var30;
               if(tag.equals("url")) {
                  this.button.height = state.font.FONT_HEIGHT;
                  this.button.width = state.font.getStringWidth(this.text.toString());
                  if(pos[0] + this.button.width > maxWidth) {
                     state.newline(pos);
                  }

                  var25 = this.attribute.toString().split("\\|");
                  String var10000;
                  if(var25.length > 0) {
                     var10000 = var25[0];
                  } else {
                     var10000 = "";
                  }

                  var29 = var25.length > 1?var25[1]:"top";
                  this.button.xPosition = pos[0] + marginX;
                  var30 = pos[1];
                  if(state.lineheight > this.button.height) {
                     if(var29.equals("bottom")) {
                        var30 += state.lineheight - this.button.height;
                     } else if(var29.equals("middle")) {
                        var30 += state.lineheight / 2 - this.button.height / 2;
                     }
                  }

                  this.button.yPosition = var30;
                  pos[0] += this.button.width;
               } else if(!tag.equals("locked")) {
                  String postText;
                  int len$;
                  String word;
                  if(tag.equals("stack")) {
                     var25 = this.attribute.toString().split("\\|");
                     postText = var25.length > 0?var25[0]:"";
                     int var28 = 0;
                     var30 = 1;
                     len$ = 1;
                     if(var25.length > len$ && var25[len$].matches("\\d+")) {
                        var28 = this.parseInt(var25[len$], 0);
                        ++len$;
                     }

                     if(var25.length > len$ && var25[len$].matches("\\d+")) {
                        var30 = this.parseInt(var25[len$], 1);
                        ++len$;
                     }

                     String var33 = var25.length > len$?var25[len$]:"left";
                     ++len$;
                     word = var25.length > len$?var25[len$]:"top";
                     if(!postText.isEmpty()) {
                        boolean var36 = postText.equals("empty");
                        Item item = !var36?(Item)Item.itemRegistry.getObject(postText):null;
                        ItemStack stack = !var36?new ItemStack(item, var30, var28):null;
                        byte width1 = 18;
                        byte height = 18;
                        if(var33.equals("right")) {
                           pos[0] = maxWidth - width1;
                        } else if(var33.equals("center")) {
                           pos[0] = maxWidth / 2 - width1 / 2;
                        }

                        if(pos[0] + width1 > maxWidth) {
                           state.newline(pos);
                        }

                        int y = pos[1];
                        if(state.lineheight > height) {
                           if(word.equals("bottom")) {
                              y += state.lineheight - height;
                           } else if(word.equals("middle")) {
                              y += state.lineheight / 2 - height / 2;
                           }
                        }

                        if(!var36) {
                           RenderItem words1 = new RenderItem();
                           GL11.glPushMatrix();
                           GL11.glEnable(3042);
                           GL11.glBlendFunc(770, 771);
                           RenderHelper.enableGUIStandardItemLighting();
                           GL11.glEnable('\u803a');
                           GL11.glEnable(2929);
                           int arr$1 = pos[0] + marginX;
                           words1.renderItemAndEffectIntoGUI(state.font, Minecraft.getMinecraft().getTextureManager(), stack, arr$1, y);
                           words1.renderItemOverlayIntoGUI(state.font, Minecraft.getMinecraft().getTextureManager(), stack, arr$1, y);
                           RenderHelper.disableStandardItemLighting();
                           GL11.glPopMatrix();
                           if(state.mouseX >= arr$1 && state.mouseY >= y && state.mouseX <= arr$1 + width1 && state.mouseY <= y + height) {
                              state.tooltipStack = stack;
                           }

                           GL11.glDisable(2896);
                        }

                        pos[0] += width1;
                        state.adjustLineHeight(height);
                        String[] var38 = this.text.toString().split("(?<=\\s)");
                        String[] var39 = var38;
                        int len$1 = var38.length;

                        for(int i$1 = 0; i$1 < len$1; ++i$1) {
                           String word1 = var39[i$1];
                           int textWidth = state.font.getStringWidth(word1);
                           if(pos[0] + textWidth > maxWidth) {
                              state.newline(pos);
                              y = pos[1];
                           }

                           state.font.drawString(word1, marginX + pos[0], y + (height - state.font.FONT_HEIGHT) / 2, 0);
                           pos[0] += textWidth;
                        }
                     }

                  } else if(!tag.equals("next")) {
                     String preText = FORMATS.containsKey(tag)?(String)FORMATS.get(tag):"";
                     postText = FORMATS.containsKey(tag)?"§r":"";
                     String[] words = this.text.toString().split("(?<=\\s)");
                     String[] arr$ = words;
                     len$ = words.length;

                     for(i$ = 0; i$ < len$; ++i$) {
                        word = arr$[i$];
                        int width = state.font.getStringWidth(word);
                        if(pos[0] + width > maxWidth) {
                           state.newline(pos);
                        }

                        if(pos[0] != 0 || !word.trim().isEmpty()) {
                           state.font.drawString(preText + word + postText, marginX + pos[0], pos[1], 0);
                           pos[0] += width;
                        }
                     }

                     if(tag.equals("h1")) {
                        state.adjustLineHeight((int)Math.ceil((double)((float)state.lineheight * 1.5F)));
                        state.newline(pos);
                     }

                  }
               }
            }
         }
      }

      private int parseInt(String text, int defaultValue) {
         try {
            return Integer.parseInt(text);
         } catch (NumberFormatException var4) {
            return defaultValue;
         }
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

      // $FF: synthetic method
      Element(GuiScreenMarkupBook.NamelessClass348268086 x0) {
         this();
      }


      private static enum Capture {

         TAG("TAG", 0),
         ATTRIB("ATTRIB", 1),
         TEXT("TEXT", 2);
         // $FF: synthetic field
         private static final GuiScreenMarkupBook.Element.Capture[] $VALUES = new GuiScreenMarkupBook.Element.Capture[]{TAG, ATTRIB, TEXT};


         private Capture(String var1, int var2) {}

      }
   }

   // $FF: synthetic class
   static class NamelessClass348268086 {
   }

   private static class NextPage {

      public final String pageName;
      public final boolean visible;


      public NextPage(String attrib, ItemStack book) {
         int pipeIndex = attrib.indexOf(124);
         if(pipeIndex != -1) {
            this.pageName = attrib.substring(0, pipeIndex);
            this.visible = book.getItemDamage() >= Integer.parseInt(attrib.substring(pipeIndex + 1));
         } else {
            this.pageName = attrib;
            this.visible = true;
         }

      }
   }

   private static class RenderState {

      final FontRenderer font;
      final float zLevel;
      final int mouseX;
      final int mouseY;
      ItemStack tooltipStack;
      int lineheight;


      public RenderState(FontRenderer font, float zLevel, int mouseX, int mouseY) {
         this.font = font;
         this.zLevel = zLevel;
         this.mouseX = mouseX;
         this.mouseY = mouseY;
         this.lineheight = font.FONT_HEIGHT;
      }

      public void newline(int[] pos) {
         pos[0] = 0;
         pos[1] += this.lineheight + 1;
         this.lineheight = this.font.FONT_HEIGHT;
      }

      public void adjustLineHeight(int newHeight) {
         if(newHeight > this.lineheight) {
            this.lineheight = newHeight;
         }

      }
   }
}
