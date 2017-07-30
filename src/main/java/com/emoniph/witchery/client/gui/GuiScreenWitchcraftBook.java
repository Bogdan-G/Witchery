package com.emoniph.witchery.client.gui;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.client.gui.GuiButtonJumpPage;
import com.emoniph.witchery.client.gui.GuiButtonNextPage;
import com.emoniph.witchery.crafting.BrazierRecipes;
import com.emoniph.witchery.crafting.DistilleryRecipes;
import com.emoniph.witchery.crafting.KettleRecipes;
import com.emoniph.witchery.infusion.infusions.spirit.InfusedSpiritEffect;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.network.PacketItemUpdate;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.util.Const;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiScreenWitchcraftBook extends GuiScreen {

   private static final ResourceLocation field_110405_a = new ResourceLocation("textures/gui/book.png");
   public static final ResourceLocation DOUBLE_BOOK_TEXTURE = new ResourceLocation("witchery", "textures/gui/bookDouble.png");
   private static final ResourceLocation[] field_110405_b = new ResourceLocation[]{new ResourceLocation("witchery", "textures/gui/circle_white_large.png"), new ResourceLocation("witchery", "textures/gui/circle_blue_large.png"), new ResourceLocation("witchery", "textures/gui/circle_red_large.png"), new ResourceLocation("witchery", "textures/gui/circle_white_medium.png"), new ResourceLocation("witchery", "textures/gui/circle_blue_medium.png"), new ResourceLocation("witchery", "textures/gui/circle_red_medium.png"), new ResourceLocation("witchery", "textures/gui/circle_white_small.png"), new ResourceLocation("witchery", "textures/gui/circle_blue_small.png"), new ResourceLocation("witchery", "textures/gui/circle_red_small.png")};
   private static final String[] sizes = new String[]{"§715x15§0", "§515x15§0", "§415x15§0", "§711x11§0", "§511x11§0", "§411x11§0", "§77x7§0", "§57x7§0", "§47x7§0"};
   private final EntityPlayer player;
   private final ItemStack itemstack;
   private int updateCount;
   private int bookImageWidth = 192;
   private int bookImageHeight = 192;
   private int bookTotalPages = 1;
   private int currPage;
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
   private static final String CURRENT_PAGE_KEY = "CurrentPage";


   public GuiScreenWitchcraftBook(EntityPlayer player, ItemStack itemstack) {
      this.player = player;
      this.itemstack = itemstack;
      this.bookTitle = itemstack.getDisplayName();
      this.bookPages = new NBTTagList();
      NBTTagCompound stackCompound;
      if(Witchery.Items.GENERIC.itemBookOven.isMatch(itemstack)) {
         stackCompound = new NBTTagCompound();
         stackCompound.setString("Summary", Witchery.resource("witchery.book.oven1"));
         this.bookPages.appendTag(stackCompound);
         stackCompound = new NBTTagCompound();
         stackCompound.setString("Summary", Witchery.resource("witchery.book.oven2"));
         this.bookPages.appendTag(stackCompound);
         stackCompound = new NBTTagCompound();
         stackCompound.setString("Summary", Witchery.resource("witchery.book.oven3"));
         this.bookPages.appendTag(stackCompound);
      } else {
         String intro;
         Iterator i$;
         if(Witchery.Items.GENERIC.itemBookDistilling.isMatch(itemstack)) {
            stackCompound = new NBTTagCompound();
            intro = Witchery.resource("witchery.book.distillery1");
            stackCompound.setString("Summary", intro);
            this.bookPages.appendTag(stackCompound);
            i$ = DistilleryRecipes.instance().recipes.iterator();

            while(i$.hasNext()) {
               DistilleryRecipes.DistilleryRecipe recipe = (DistilleryRecipes.DistilleryRecipe)i$.next();
               stackCompound = new NBTTagCompound();
               stackCompound.setString("Summary", recipe.getDescription());
               this.bookPages.appendTag(stackCompound);
            }
         } else {
            String var15;
            if(Witchery.Items.GENERIC.itemBookCircleMagic.isMatch(itemstack)) {
               stackCompound = new NBTTagCompound();
               intro = Witchery.resource("witchery.book.rites1");
               var15 = Witchery.resource("witchery.book.rites2");
               String var17 = Witchery.resource("witchery.book.rites.anycircle");
               stackCompound.setString("Summary", intro);
               stackCompound.setString("Summary2", var15);
               stackCompound.setByteArray("Circles", new byte[]{(byte)0, (byte)3, (byte)6});
               this.bookPages.appendTag(stackCompound);
               Iterator effect = RiteRegistry.instance().getSortedRituals().iterator();

               while(effect.hasNext()) {
                  RiteRegistry.Ritual ritual = (RiteRegistry.Ritual)effect.next();
                  if(ritual.showInBook()) {
                     stackCompound = new NBTTagCompound();
                     stackCompound.setString("Summary", ritual.getDescription());
                     byte[] circles = ritual.getCircles();
                     stackCompound.setByteArray("Circles", circles);
                     if(circles.length == 0) {
                        stackCompound.setString("Summary2", var17);
                     } else {
                        StringBuilder sb = new StringBuilder();
                        byte[] arr$ = circles;
                        int len$ = circles.length;

                        for(int i$1 = 0; i$1 < len$; ++i$1) {
                           byte cir = arr$[i$1];
                           if(sb.length() > 0) {
                              sb.append(", ");
                           }

                           sb.append(sizes[cir]);
                        }

                        stackCompound.setString("Summary2", sb.toString());
                     }

                     this.bookPages.appendTag(stackCompound);
                  }
               }
            } else if(Witchery.Items.GENERIC.itemBookInfusions.isMatch(itemstack)) {
               stackCompound = new NBTTagCompound();
               intro = Witchery.resource("witchery.book.brews1");
               stackCompound.setString("Summary", intro);
               this.bookPages.appendTag(stackCompound);
               i$ = KettleRecipes.instance().recipes.iterator();

               while(i$.hasNext()) {
                  KettleRecipes.KettleRecipe var16 = (KettleRecipes.KettleRecipe)i$.next();
                  if(var16.inBook) {
                     stackCompound = new NBTTagCompound();
                     stackCompound.setString("Summary", var16.getDescription());
                     this.bookPages.appendTag(stackCompound);
                  }
               }
            } else if(Witchery.Items.GENERIC.itemBookBurning.isMatch(itemstack)) {
               stackCompound = new NBTTagCompound();
               intro = Witchery.resource("witchery.book.burning1");
               stackCompound.setString("Summary", intro);
               this.bookPages.appendTag(stackCompound);
               i$ = BrazierRecipes.instance().recipes.iterator();

               while(i$.hasNext()) {
                  BrazierRecipes.BrazierRecipe var20 = (BrazierRecipes.BrazierRecipe)i$.next();
                  if(var20.inBook) {
                     stackCompound = new NBTTagCompound();
                     stackCompound.setString("Summary", var20.getDescription());
                     this.bookPages.appendTag(stackCompound);
                  }
               }

               stackCompound = new NBTTagCompound();
               var15 = Witchery.resource("witchery.book.burning2");
               stackCompound.setString("Summary", var15);
               this.bookPages.appendTag(stackCompound);
               Iterator var18 = InfusedSpiritEffect.effectList.iterator();

               while(var18.hasNext()) {
                  InfusedSpiritEffect var19 = (InfusedSpiritEffect)var18.next();
                  if(var19 != null && var19.isInBook()) {
                     stackCompound = new NBTTagCompound();
                     stackCompound.setString("Summary", var19.getDescription());
                     this.bookPages.appendTag(stackCompound);
                  }
               }
            } else if(Witchery.Items.GENERIC.itemBookHerbology.isMatch(itemstack)) {
               stackCompound = new NBTTagCompound();
               intro = Witchery.resource("witchery.book.herbology1");
               stackCompound.setString("Summary", intro);
               this.bookPages.appendTag(stackCompound);
               this.addPlantPage((Block)Witchery.Blocks.CROP_BELLADONNA, "witchery.book.herbology.belladonna", "witchery:textures/blocks/belladonna_stage_4.png");
               this.addPlantPage(Witchery.Blocks.EMBER_MOSS, "witchery.book.herbology.embermoss", "witchery:textures/blocks/embermoss.png");
               this.addPlantPage(Witchery.Blocks.GLINT_WEED, "witchery.book.herbology.glintweed", "witchery:textures/blocks/glintWeed.png");
               this.addPlantPage((Block)Witchery.Blocks.CROP_MANDRAKE, "witchery.book.herbology.mandrake", "witchery:textures/blocks/mandrake_stage_4.png");
               this.addPlantPage((Block)Witchery.Blocks.CROP_SNOWBELL, "witchery.book.herbology.snowbell", "witchery:textures/blocks/snowbell_stage_4.png");
               this.addPlantPage(Witchery.Blocks.SPANISH_MOSS, "witchery.book.herbology.spanishmoss", "witchery:textures/blocks/spanishMoss.png");
               this.addPlantPage(new ItemStack(Witchery.Blocks.BRAMBLE, 1, 1), "witchery.book.herbology.wildbramble", "witchery:textures/blocks/bramble_wild.png");
               this.addPlantPage(new ItemStack(Witchery.Blocks.BRAMBLE, 1, 0), "witchery.book.herbology.enderbramble", "witchery:textures/blocks/bramble_ender.png");
               this.addPlantPage(Witchery.Blocks.VOID_BRAMBLE, "witchery.book.herbology.voidbramble", "witchery:textures/blocks/voidBramble.png");
               this.addPlantPage((Block)Witchery.Blocks.CROP_ARTICHOKE, "witchery.book.herbology.artichoke", "witchery:textures/blocks/artichoke_stage_4.png");
               this.addPlantPage(Witchery.Blocks.GRASSPER, "witchery.book.herbology.grassper", "witchery:textures/blocks/grassperIcon.png");
               this.addPlantPage(Witchery.Blocks.CRITTER_SNARE, "witchery.book.herbology.crittersnare", "witchery:textures/blocks/critterSnare_empty.png");
               this.addPlantPage(Witchery.Blocks.BLOOD_ROSE, "witchery.book.herbology.bloodrose", "witchery:textures/blocks/bloodrose.png");
               this.addPlantPage(Witchery.Blocks.WISPY_COTTON, "witchery.book.herbology.somniancotton", "witchery:textures/blocks/somnianCotton.png");
               this.addPlantPage((Block)Witchery.Blocks.CROP_WOLFSBANE, "witchery.book.herbology.wolfsbane", "witchery:textures/blocks/wolfsbane_stage_7.png");
               this.addPlantPage((Block)Witchery.Blocks.CROP_GARLIC, "witchery.book.herbology.garlic", "witchery:textures/blocks/garlic_stage_5.png");
               this.addPlantPage(new ItemStack(Witchery.Blocks.SAPLING, 1, 1), "witchery.book.herbology.alder", "witchery:textures/blocks/sapling_alder.png");
               this.addPlantPage(new ItemStack(Witchery.Blocks.SAPLING, 1, 2), "witchery.book.herbology.hawthorn", "witchery:textures/blocks/sapling_hawthorn.png");
               this.addPlantPage(new ItemStack(Witchery.Blocks.SAPLING, 1, 0), "witchery.book.herbology.rowan", "witchery:textures/blocks/sapling_rowan.png");
            } else if(Witchery.Items.GENERIC.itemBookBiomes.isMatch(itemstack)) {
               stackCompound = new NBTTagCompound();
               intro = Witchery.resource("witchery.book.biomes1");
               stackCompound.setString("Summary", intro);
               this.bookPages.appendTag(stackCompound);
               this.addBiomes(Type.FOREST);
               this.addBiomes(Type.PLAINS);
               this.addBiomes(Type.MOUNTAIN);
               this.addBiomes(Type.HILLS);
               this.addBiomes(Type.SWAMP);
               this.addBiomes(Type.WATER);
               this.addBiomes(Type.DESERT);
               this.addBiomes(Type.FROZEN);
               this.addBiomes(Type.JUNGLE);
               this.addBiomes(Type.WASTELAND);
               this.addBiomes(Type.BEACH);
               this.addBiomes(Type.MUSHROOM);
               this.addBiomes(Type.MAGICAL);
            } else if(Witchery.Items.GENERIC.itemBookWands.isMatch(itemstack)) {
               stackCompound = new NBTTagCompound();
               intro = Witchery.resource("witchery.book.wands1");
               stackCompound.setString("Summary", intro);
               this.bookPages.appendTag(stackCompound);
               i$ = EffectRegistry.instance().getEffects().iterator();

               while(i$.hasNext()) {
                  SymbolEffect var21 = (SymbolEffect)i$.next();
                  if(var21.isVisible(player)) {
                     stackCompound = new NBTTagCompound();
                     stackCompound.setString("Summary", var21.getDescription());
                     this.bookPages.appendTag(stackCompound);
                  }
               }
            }
         }
      }

      this.bookTotalPages = this.bookPages.tagCount();
      stackCompound = itemstack.getTagCompound();
      if(stackCompound != null && stackCompound.hasKey("CurrentPage")) {
         this.currPage = Math.min(Math.max(stackCompound.getInteger("CurrentPage"), 0), Math.max(this.bookTotalPages, 1) - 1);
      }

   }

   private void addBiomes(Type biomeType) {
      String biomeKey = biomeType.toString().toLowerCase();
      String title = "§n" + Witchery.resource("witchery.book.biomes." + biomeKey + ".name") + "§r" + "\n\n" + "§8" + Witchery.resource("witchery.book.biomes.foci") + ": " + Witchery.resource("witchery.book.biomes." + biomeKey + ".item") + "§0" + Const.BOOK_NEWLINE;
      BiomeGenBase[] biomes = BiomeDictionary.getBiomesForType(biomeType);
      boolean ITEMS_PER_PAGE = true;
      StringBuilder sb = new StringBuilder();

      for(int glowstone = 1; glowstone <= biomes.length; ++glowstone) {
         sb.append(glowstone);
         sb.append(" : ");
         sb.append(biomes[glowstone - 1].biomeName);
         sb.append(Const.BOOK_NEWLINE);
         if(glowstone % 8 == 0 || glowstone == biomes.length) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("Summary", title + Const.BOOK_NEWLINE + sb.toString());
            this.bookPages.appendTag(compound);
            sb = new StringBuilder();
         }
      }

   }

   private void addPlantPage(ItemStack plantStack, String descriptionResourceID, String imageResourceID) {
      NBTTagCompound compound = new NBTTagCompound();
      compound.setString("Summary", "§n" + plantStack.getDisplayName() + "§r");
      compound.setString("Details", Witchery.resource(descriptionResourceID));
      compound.setString("Image", imageResourceID);
      this.bookPages.appendTag(compound);
   }

   private void addPlantPage(Block plantBlock, String descriptionResourceID, String imageResourceID) {
      NBTTagCompound compound = new NBTTagCompound();
      compound.setString("Summary", "§n" + plantBlock.getLocalizedName() + "§r");
      compound.setString("Details", Witchery.resource(descriptionResourceID));
      compound.setString("Image", imageResourceID);
      this.bookPages.appendTag(compound);
   }

   private void storeCurrentPage() {
      if(this.itemstack.getTagCompound() == null) {
         this.itemstack.setTagCompound(new NBTTagCompound());
      }

      this.itemstack.getTagCompound().setInteger("CurrentPage", this.currPage);
   }

   public void updateScreen() {
      super.updateScreen();
      ++this.updateCount;
   }

   public void initGui() {
      super.buttonList.clear();
      Keyboard.enableRepeatEvents(true);
      super.buttonList.add(this.buttonDone = new GuiButton(0, super.width / 2 - 100, 4 + this.bookImageHeight, 200, 20, I18n.format("gui.done", new Object[0])));
      int i = (super.width - this.bookImageWidth) / 2;
      byte b0 = 2;
      if(Witchery.Items.GENERIC.itemBookCircleMagic.isMatch(this.itemstack)) {
         super.buttonList.add(this.buttonNextPage = new GuiButtonNextPage(1, i + 180, b0 + 154, true));
         super.buttonList.add(this.buttonPreviousPage = new GuiButtonNextPage(2, i + 110, b0 + 154, false));
         super.buttonList.add(this.buttonJumpPage7 = new GuiButtonJumpPage(9, i + 214, b0 + 138, 69, 48, 248));
         super.buttonList.add(this.buttonJumpPage6 = new GuiButtonJumpPage(8, i + 214, b0 + 118, 58, 40, 248));
         super.buttonList.add(this.buttonJumpPage5 = new GuiButtonJumpPage(7, i + 214, b0 + 98, 47, 32, 248));
         super.buttonList.add(this.buttonJumpPage4 = new GuiButtonJumpPage(6, i + 214, b0 + 78, 29, 24, 248));
         super.buttonList.add(this.buttonJumpPage3 = new GuiButtonJumpPage(5, i + 214, b0 + 58, 23, 16, 248));
         super.buttonList.add(this.buttonJumpPage2 = new GuiButtonJumpPage(4, i + 214, b0 + 38, 17, 8, 248));
         super.buttonList.add(this.buttonJumpPage1 = new GuiButtonJumpPage(3, i + 214, b0 + 18, 2, 0, 248));
      } else {
         super.buttonList.add(this.buttonNextPage = new GuiButtonNextPage(1, i + 120, b0 + 154, true));
         super.buttonList.add(this.buttonPreviousPage = new GuiButtonNextPage(2, i + 38, b0 + 154, false));
      }

      this.updateButtons();
   }

   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.sendBookToServer(false);
   }

   private void updateButtons() {
      this.buttonNextPage.visible = this.currPage < this.bookTotalPages - 1;
      this.buttonPreviousPage.visible = this.currPage > 0;
   }

   private void sendBookToServer(boolean par1) {
      if(this.player != null && this.currPage >= 0 && this.currPage < 1000 && this.player.inventory.currentItem >= 0 && this.player.inventory.getCurrentItem() != null) {
         Witchery.packetPipeline.sendToServer(new PacketItemUpdate(this.player.inventory.currentItem, this.currPage, this.player.inventory.getCurrentItem()));
      }

   }

   protected void actionPerformed(GuiButton par1GuiButton) {
      if(par1GuiButton.enabled) {
         if(par1GuiButton.id == 0) {
            super.mc.displayGuiScreen((GuiScreen)null);
         } else if(par1GuiButton.id == 1) {
            if(this.currPage < this.bookTotalPages - 1) {
               ++this.currPage;
               this.storeCurrentPage();
            }
         } else if(par1GuiButton.id == 2) {
            if(this.currPage > 0) {
               --this.currPage;
               this.storeCurrentPage();
            }
         } else if(par1GuiButton instanceof GuiButtonJumpPage) {
            GuiButtonJumpPage but = (GuiButtonJumpPage)par1GuiButton;
            this.currPage = but.nextPage - 1;
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
      int k;
      byte b0;
      String s;
      String s1;
      String s2;
      NBTTagCompound compound;
      byte[] loc;
      byte[] location;
      int len$;
      int i$;
      byte circle;
      if(Witchery.Items.GENERIC.itemBookCircleMagic.isMatch(this.itemstack)) {
         super.mc.getTextureManager().bindTexture(DOUBLE_BOOK_TEXTURE);
         this.bookImageWidth = 256;
         k = (super.width - this.bookImageWidth) / 2;
         b0 = 2;
         this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
         String l = "";
         s = I18n.format("book.pageIndicator", new Object[]{Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages)});
         s1 = "";
         s2 = "";
         if(this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
            compound = this.bookPages.getCompoundTagAt(this.currPage);
            s1 = compound.getString("Summary");
            s2 = compound.getString("Summary2");
            if(compound.hasKey("Circles")) {
               loc = compound.getByteArray("Circles");
               location = loc;
               len$ = loc.length;

               for(i$ = 0; i$ < len$; ++i$) {
                  circle = location[i$];
                  super.mc.getTextureManager().bindTexture(field_110405_b[circle]);
                  this.drawTexturedModalRect(k, b0, -148, -36, this.bookImageWidth, this.bookImageHeight);
               }
            }
         }

         int hasImage = super.fontRendererObj.getStringWidth(s);
         super.fontRendererObj.drawString(s, k - hasImage + this.bookImageWidth - 16, b0 + 16, 0);
         super.fontRendererObj.drawSplitString(s1, k + 20, b0 + 16, 98, 0);
         if(!s2.isEmpty()) {
            int var18 = super.fontRendererObj.getStringWidth(s2);
            if(var18 < 90) {
               super.fontRendererObj.drawSplitString(s2, k + this.bookImageWidth / 4 * 3 - var18 / 2, b0 + 125, 98, 0);
            } else {
               super.fontRendererObj.drawSplitString(s2, k + 142, b0 + 125, 98, 0);
            }
         }
      } else {
         super.mc.getTextureManager().bindTexture(field_110405_a);
         k = (super.width - this.bookImageWidth) / 2;
         b0 = 2;
         this.drawTexturedModalRect(k, b0, 0, 0, this.bookImageWidth, this.bookImageHeight);
         s2 = "";
         s = I18n.format("book.pageIndicator", new Object[]{Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages)});
         s1 = "";
         boolean var19 = false;
         if(this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
            compound = this.bookPages.getCompoundTagAt(this.currPage);
            s1 = compound.getString("Summary");
            if(compound.hasKey("Circles")) {
               loc = compound.getByteArray("Circles");
               location = loc;
               len$ = loc.length;

               for(i$ = 0; i$ < len$; ++i$) {
                  circle = location[i$];
                  super.mc.getTextureManager().bindTexture(field_110405_b[circle]);
                  this.drawTexturedModalRect(k, b0, -62, -70, this.bookImageWidth, this.bookImageHeight);
               }
            }

            var19 = compound.hasKey("Image");
            if(var19) {
               String var20 = compound.getString("Image");
               ResourceLocation var21 = new ResourceLocation(var20);
               super.mc.getTextureManager().bindTexture(var21);
               drawTexturedQuadFit((double)(k - 32 + this.bookImageWidth - 44), (double)(b0 + 32), 32.0D, 32.0D, (double)super.zLevel);
            }

            if(compound.hasKey("Details")) {
               s2 = compound.getString("Details");
            }
         }

         int var17 = super.fontRendererObj.getStringWidth(s);
         super.fontRendererObj.drawString(s, k - var17 + this.bookImageWidth - 44, b0 + 16, 0);
         super.fontRendererObj.drawSplitString(s1, k + 36, b0 + 32, 116 - (var19?34:0), 0);
         if(s2 != null && !s2.isEmpty()) {
            super.fontRendererObj.drawSplitString(s2, k + 36, b0 + 32 + 34, 116, 0);
         }
      }

      super.drawScreen(par1, par2, par3);
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
