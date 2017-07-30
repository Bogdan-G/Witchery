package com.emoniph.witchery.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderItem3d extends Render {

   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   private RenderBlocks renderBlocksRi = new RenderBlocks();
   private Random random = new Random();
   public boolean renderWithColor = true;
   public float zLevel;
   public static boolean renderInFrame;
   private static final String __OBFID = "CL_00001003";
   protected final boolean alwaysFancy;


   public RenderItem3d(boolean alwaysFancy) {
      super.shadowSize = 0.15F;
      super.shadowOpaque = 0.75F;
      this.alwaysFancy = alwaysFancy;
   }

   public void doRender(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9) {
      ItemStack itemstack = par1EntityItem.getEntityItem();
      if(itemstack.getItem() != null) {
         this.bindEntityTexture(par1EntityItem);
         this.random.setSeed(187L);
         GL11.glPushMatrix();
         float f2 = this.shouldBob()?MathHelper.sin(((float)par1EntityItem.age + par9) / 10.0F + par1EntityItem.hoverStart) * 0.1F + 0.1F:0.0F;
         float f3 = (((float)par1EntityItem.age + par9) / 20.0F + par1EntityItem.hoverStart) * 57.295776F;
         byte b0 = 1;
         if(par1EntityItem.getEntityItem().stackSize > 1) {
            b0 = 2;
         }

         if(par1EntityItem.getEntityItem().stackSize > 5) {
            b0 = 3;
         }

         if(par1EntityItem.getEntityItem().stackSize > 20) {
            b0 = 4;
         }

         if(par1EntityItem.getEntityItem().stackSize > 40) {
            b0 = 5;
         }

         byte var21 = this.getMiniBlockCount(itemstack, b0);
         GL11.glTranslatef((float)par2, (float)par4 + f2, (float)par6);
         GL11.glEnable('\u803a');
         if(!ForgeHooksClient.renderEntityItem(par1EntityItem, itemstack, f2, f3, this.random, super.renderManager.renderEngine, super.field_147909_c, var21)) {
            float f6;
            float f7;
            int k;
            float f4;
            int var23;
            if(itemstack.getItemSpriteNumber() == 0 && itemstack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType())) {
               Block var22 = Block.getBlockFromItem(itemstack.getItem());
               GL11.glRotatef(f3, 0.0F, 1.0F, 0.0F);
               if(renderInFrame) {
                  GL11.glScalef(1.25F, 1.25F, 1.25F);
                  GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                  GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
               }

               float var24 = 0.25F;
               k = var22.getRenderType();
               if(k == 1 || k == 19 || k == 12 || k == 2) {
                  var24 = 0.5F;
               }

               if(var22.getRenderBlockPass() > 0) {
                  GL11.glAlphaFunc(516, 0.1F);
                  GL11.glEnable(3042);
                  OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               }

               GL11.glScalef(var24, var24, var24);

               for(var23 = 0; var23 < var21; ++var23) {
                  GL11.glPushMatrix();
                  if(var23 > 0) {
                     f6 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                     f7 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                     f4 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F / var24;
                     GL11.glTranslatef(f6, f7, f4);
                  }

                  this.renderBlocksRi.renderBlockAsItem(var22, itemstack.getItemDamage(), 1.0F);
                  GL11.glPopMatrix();
               }

               if(var22.getRenderBlockPass() > 0) {
                  GL11.glDisable(3042);
               }
            } else {
               float f5;
               if(itemstack.getItem().requiresMultipleRenderPasses()) {
                  if(renderInFrame) {
                     GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                     GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                  } else {
                     GL11.glScalef(0.5F, 0.5F, 0.5F);
                  }

                  for(int iicon = 0; iicon < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++iicon) {
                     this.random.setSeed(187L);
                     IIcon i = itemstack.getItem().getIcon(itemstack, iicon);
                     if(this.renderWithColor) {
                        k = itemstack.getItem().getColorFromItemStack(itemstack, iicon);
                        f5 = (float)(k >> 16 & 255) / 255.0F;
                        f6 = (float)(k >> 8 & 255) / 255.0F;
                        f7 = (float)(k & 255) / 255.0F;
                        GL11.glColor4f(f5, f6, f7, 1.0F);
                        this.renderDroppedItem(par1EntityItem, i, var21, par9, f5, f6, f7, iicon);
                     } else {
                        this.renderDroppedItem(par1EntityItem, i, var21, par9, 1.0F, 1.0F, 1.0F, iicon);
                     }
                  }
               } else {
                  if(itemstack != null && itemstack.getItem() instanceof ItemCloth) {
                     GL11.glAlphaFunc(516, 0.1F);
                     GL11.glEnable(3042);
                     OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                  }

                  if(renderInFrame) {
                     GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                     GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                  } else {
                     GL11.glScalef(0.5F, 0.5F, 0.5F);
                  }

                  IIcon var25 = itemstack.getIconIndex();
                  if(this.renderWithColor) {
                     var23 = itemstack.getItem().getColorFromItemStack(itemstack, 0);
                     f4 = (float)(var23 >> 16 & 255) / 255.0F;
                     f5 = (float)(var23 >> 8 & 255) / 255.0F;
                     f6 = (float)(var23 & 255) / 255.0F;
                     this.renderDroppedItem(par1EntityItem, var25, var21, par9, f4, f5, f6);
                  } else {
                     this.renderDroppedItem(par1EntityItem, var25, var21, par9, 1.0F, 1.0F, 1.0F);
                  }

                  if(itemstack != null && itemstack.getItem() instanceof ItemCloth) {
                     GL11.glDisable(3042);
                  }
               }
            }
         }

         GL11.glDisable('\u803a');
         GL11.glPopMatrix();
      }

   }

   protected ResourceLocation getEntityTexture(EntityItem par1EntityItem) {
      return super.renderManager.renderEngine.getResourceLocation(par1EntityItem.getEntityItem().getItemSpriteNumber());
   }

   private void renderDroppedItem(EntityItem par1EntityItem, IIcon par2Icon, int par3, float par4, float par5, float par6, float par7) {
      this.renderDroppedItem(par1EntityItem, par2Icon, par3, par4, par5, par6, par7, 0);
   }

   private void renderDroppedItem(EntityItem par1EntityItem, IIcon par2Icon, int par3, float par4, float par5, float par6, float par7, int pass) {
      Tessellator tessellator = Tessellator.instance;
      if(par2Icon == null) {
         TextureManager f14 = Minecraft.getMinecraft().getTextureManager();
         ResourceLocation f15 = f14.getResourceLocation(par1EntityItem.getEntityItem().getItemSpriteNumber());
         par2Icon = ((TextureMap)f14.getTexture(f15)).getAtlasSprite("missingno");
      }

      float var27 = ((IIcon)par2Icon).getMinU();
      float var26 = ((IIcon)par2Icon).getMaxU();
      float f4 = ((IIcon)par2Icon).getMinV();
      float f5 = ((IIcon)par2Icon).getMaxV();
      float f6 = 1.0F;
      float f7 = 0.5F;
      float f8 = 0.25F;
      float f10;
      if(!this.alwaysFancy && !super.renderManager.options.fancyGraphics) {
         for(int var29 = 0; var29 < par3; ++var29) {
            GL11.glPushMatrix();
            if(var29 > 0) {
               f10 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
               float var28 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
               float var30 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F;
               GL11.glTranslatef(f10, var28, var30);
            }

            if(!renderInFrame) {
               GL11.glRotatef(180.0F - super.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            }

            GL11.glColor4f(par5, par6, par7, 1.0F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            tessellator.addVertexWithUV((double)(0.0F - f7), (double)(0.0F - f8), 0.0D, (double)var27, (double)f5);
            tessellator.addVertexWithUV((double)(f6 - f7), (double)(0.0F - f8), 0.0D, (double)var26, (double)f5);
            tessellator.addVertexWithUV((double)(f6 - f7), (double)(1.0F - f8), 0.0D, (double)var26, (double)f4);
            tessellator.addVertexWithUV((double)(0.0F - f7), (double)(1.0F - f8), 0.0D, (double)var27, (double)f4);
            tessellator.draw();
            GL11.glPopMatrix();
         }
      } else {
         GL11.glPushMatrix();
         if(renderInFrame) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
         } else {
            GL11.glRotatef((((float)par1EntityItem.age + par4) / 20.0F + par1EntityItem.hoverStart) * 57.295776F, 0.0F, 1.0F, 0.0F);
         }

         float l = 0.0625F;
         f10 = 0.021875F;
         ItemStack f16 = par1EntityItem.getEntityItem();
         int f17 = f16.stackSize;
         byte b0;
         if(f17 < 2) {
            b0 = 1;
         } else if(f17 < 16) {
            b0 = 2;
         } else if(f17 < 32) {
            b0 = 3;
         } else {
            b0 = 4;
         }

         byte var31 = this.getMiniItemCount(f16, b0);
         GL11.glTranslatef(-f7, -f8, -((l + f10) * (float)var31 / 2.0F));

         for(int k = 0; k < var31; ++k) {
            float f11;
            float f13;
            float f12;
            if(k > 0 && this.shouldSpreadItems()) {
               f11 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
               f12 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
               f13 = (this.random.nextFloat() * 2.0F - 1.0F) * 0.3F / 0.5F;
               GL11.glTranslatef(f11, f12, l + f10);
            } else {
               GL11.glTranslatef(0.0F, 0.0F, l + f10);
            }

            if(f16.getItemSpriteNumber() == 0) {
               this.bindTexture(TextureMap.locationBlocksTexture);
            } else {
               this.bindTexture(TextureMap.locationItemsTexture);
            }

            GL11.glColor4f(par5, par6, par7, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, var26, f4, var27, f5, ((IIcon)par2Icon).getIconWidth(), ((IIcon)par2Icon).getIconHeight(), l);
            if(f16.hasEffect(pass)) {
               GL11.glDepthFunc(514);
               GL11.glDisable(2896);
               super.renderManager.renderEngine.bindTexture(RES_ITEM_GLINT);
               GL11.glEnable(3042);
               GL11.glBlendFunc(768, 1);
               f11 = 0.76F;
               GL11.glColor4f(0.5F * f11, 0.25F * f11, 0.8F * f11, 1.0F);
               GL11.glMatrixMode(5890);
               GL11.glPushMatrix();
               f12 = 0.125F;
               GL11.glScalef(f12, f12, f12);
               f13 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
               GL11.glTranslatef(f13, 0.0F, 0.0F);
               GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
               ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, l);
               GL11.glPopMatrix();
               GL11.glPushMatrix();
               GL11.glScalef(f12, f12, f12);
               f13 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
               GL11.glTranslatef(-f13, 0.0F, 0.0F);
               GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
               ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, l);
               GL11.glPopMatrix();
               GL11.glMatrixMode(5888);
               GL11.glDisable(3042);
               GL11.glEnable(2896);
               GL11.glDepthFunc(515);
            }
         }

         GL11.glPopMatrix();
      }

   }

   public void renderItemIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5) {
      this.renderItemIntoGUI(par1FontRenderer, par2TextureManager, par3ItemStack, par4, par5, false);
   }

   public void renderItemIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5, boolean renderEffect) {
      int k = par3ItemStack.getItemDamage();
      Object object = par3ItemStack.getIconIndex();
      GL11.glEnable(3042);
      OpenGlHelper.glBlendFunc(770, 771, 1, 0);
      int l;
      float f;
      float f3;
      float f4;
      if(par3ItemStack.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(par3ItemStack.getItem()).getRenderType())) {
         par2TextureManager.bindTexture(TextureMap.locationBlocksTexture);
         Block var19 = Block.getBlockFromItem(par3ItemStack.getItem());
         GL11.glPushMatrix();
         GL11.glTranslatef((float)(par4 - 2), (float)(par5 + 3), -3.0F + this.zLevel);
         GL11.glScalef(10.0F, 10.0F, 10.0F);
         GL11.glTranslatef(1.0F, 0.5F, 1.0F);
         GL11.glScalef(1.0F, 1.0F, -1.0F);
         GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         l = par3ItemStack.getItem().getColorFromItemStack(par3ItemStack, 0);
         f3 = (float)(l >> 16 & 255) / 255.0F;
         f4 = (float)(l >> 8 & 255) / 255.0F;
         f = (float)(l & 255) / 255.0F;
         if(this.renderWithColor) {
            GL11.glColor4f(f3, f4, f, 1.0F);
         }

         GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
         this.renderBlocksRi.useInventoryTint = this.renderWithColor;
         this.renderBlocksRi.renderBlockAsItem(var19, k, 1.0F);
         this.renderBlocksRi.useInventoryTint = true;
         GL11.glPopMatrix();
      } else if(par3ItemStack.getItem().requiresMultipleRenderPasses()) {
         GL11.glDisable(2896);
         GL11.glEnable(3008);
         par2TextureManager.bindTexture(TextureMap.locationItemsTexture);
         GL11.glDisable(3008);
         GL11.glDisable(3553);
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(0, 0, 0, 0);
         GL11.glColorMask(false, false, false, true);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         Tessellator resourcelocation = Tessellator.instance;
         resourcelocation.startDrawingQuads();
         resourcelocation.setColorOpaque_I(-1);
         resourcelocation.addVertex((double)(par4 - 2), (double)(par5 + 18), (double)this.zLevel);
         resourcelocation.addVertex((double)(par4 + 18), (double)(par5 + 18), (double)this.zLevel);
         resourcelocation.addVertex((double)(par4 + 18), (double)(par5 - 2), (double)this.zLevel);
         resourcelocation.addVertex((double)(par4 - 2), (double)(par5 - 2), (double)this.zLevel);
         resourcelocation.draw();
         GL11.glColorMask(true, true, true, true);
         GL11.glEnable(3553);
         GL11.glEnable(3008);
         Item item = par3ItemStack.getItem();

         for(l = 0; l < item.getRenderPasses(k); ++l) {
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            par2TextureManager.bindTexture(item.getSpriteNumber() == 0?TextureMap.locationBlocksTexture:TextureMap.locationItemsTexture);
            IIcon iicon = item.getIcon(par3ItemStack, l);
            int i1 = par3ItemStack.getItem().getColorFromItemStack(par3ItemStack, l);
            f = (float)(i1 >> 16 & 255) / 255.0F;
            float f1 = (float)(i1 >> 8 & 255) / 255.0F;
            float f2 = (float)(i1 & 255) / 255.0F;
            if(this.renderWithColor) {
               GL11.glColor4f(f, f1, f2, 1.0F);
            }

            GL11.glDisable(2896);
            GL11.glEnable(3008);
            this.renderIcon(par4, par5, iicon, 16, 16);
            GL11.glDisable(3008);
            GL11.glEnable(2896);
            if(renderEffect && par3ItemStack.hasEffect(l)) {
               this.renderEffect(par2TextureManager, par4, par5);
            }
         }

         GL11.glDisable(3008);
         GL11.glEnable(2896);
      } else {
         GL11.glDisable(2896);
         ResourceLocation var20 = par2TextureManager.getResourceLocation(par3ItemStack.getItemSpriteNumber());
         par2TextureManager.bindTexture(var20);
         if(object == null) {
            object = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(var20)).getAtlasSprite("missingno");
         }

         l = par3ItemStack.getItem().getColorFromItemStack(par3ItemStack, 0);
         f3 = (float)(l >> 16 & 255) / 255.0F;
         f4 = (float)(l >> 8 & 255) / 255.0F;
         f = (float)(l & 255) / 255.0F;
         if(this.renderWithColor) {
            GL11.glColor4f(f3, f4, f, 1.0F);
         }

         GL11.glDisable(2896);
         GL11.glEnable(3008);
         this.renderIcon(par4, par5, (IIcon)object, 16, 16);
         GL11.glDisable(3008);
         GL11.glEnable(2896);
         if(renderEffect && par3ItemStack.hasEffect(0)) {
            this.renderEffect(par2TextureManager, par4, par5);
         }

         GL11.glEnable(2896);
      }

      GL11.glEnable(2884);
   }

   public void renderItemAndEffectIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, final ItemStack par3ItemStack, int par4, int par5) {
      if(par3ItemStack != null) {
         this.zLevel += 50.0F;

         try {
            if(!ForgeHooksClient.renderInventoryItem(super.field_147909_c, par2TextureManager, par3ItemStack, this.renderWithColor, this.zLevel, (float)par4, (float)par5)) {
               this.renderItemIntoGUI(par1FontRenderer, par2TextureManager, par3ItemStack, par4, par5, true);
            }
         } catch (Throwable var9) {
            CrashReport crashreport = CrashReport.makeCrashReport(var9, "Rendering item");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
            crashreportcategory.addCrashSectionCallable("Item Type", new Callable() {

               private static final String __OBFID = "CL_00001004";

               public String call() {
                  return String.valueOf(par3ItemStack.getItem());
               }
            });
            crashreportcategory.addCrashSectionCallable("Item Aux", new Callable() {

               private static final String __OBFID = "CL_00001005";

               public String call() {
                  return String.valueOf(par3ItemStack.getItemDamage());
               }
            });
            crashreportcategory.addCrashSectionCallable("Item NBT", new Callable() {

               private static final String __OBFID = "CL_00001006";

               public String call() {
                  return String.valueOf(par3ItemStack.getTagCompound());
               }
            });
            crashreportcategory.addCrashSectionCallable("Item Foil", new Callable() {

               private static final String __OBFID = "CL_00001007";

               public String call() {
                  return String.valueOf(par3ItemStack.hasEffect());
               }
            });
            throw new ReportedException(crashreport);
         }

         this.zLevel -= 50.0F;
      }

   }

   public void renderEffect(TextureManager manager, int x, int y) {
      GL11.glDepthFunc(514);
      GL11.glDisable(2896);
      GL11.glDepthMask(false);
      manager.bindTexture(RES_ITEM_GLINT);
      GL11.glEnable(3008);
      GL11.glEnable(3042);
      GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
      this.renderGlint(x * 431278612 + y * 32178161, x - 2, y - 2, 20, 20);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glDisable(3008);
      GL11.glEnable(2896);
      GL11.glDepthFunc(515);
   }

   private void renderGlint(int par1, int par2, int par3, int par4, int par5) {
      for(int j1 = 0; j1 < 2; ++j1) {
         OpenGlHelper.glBlendFunc(772, 1, 0, 0);
         float f = 0.00390625F;
         float f1 = 0.00390625F;
         float f2 = (float)(Minecraft.getSystemTime() % (long)(3000 + j1 * 1873)) / (3000.0F + (float)(j1 * 1873)) * 256.0F;
         float f3 = 0.0F;
         Tessellator tessellator = Tessellator.instance;
         float f4 = 4.0F;
         if(j1 == 1) {
            f4 = -1.0F;
         }

         tessellator.startDrawingQuads();
         tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + par5), (double)this.zLevel, (double)((f2 + (float)par5 * f4) * f), (double)((f3 + (float)par5) * f1));
         tessellator.addVertexWithUV((double)(par2 + par4), (double)(par3 + par5), (double)this.zLevel, (double)((f2 + (float)par4 + (float)par5 * f4) * f), (double)((f3 + (float)par5) * f1));
         tessellator.addVertexWithUV((double)(par2 + par4), (double)(par3 + 0), (double)this.zLevel, (double)((f2 + (float)par4) * f), (double)((f3 + 0.0F) * f1));
         tessellator.addVertexWithUV((double)(par2 + 0), (double)(par3 + 0), (double)this.zLevel, (double)((f2 + 0.0F) * f), (double)((f3 + 0.0F) * f1));
         tessellator.draw();
      }

   }

   public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5) {
      this.renderItemOverlayIntoGUI(par1FontRenderer, par2TextureManager, par3ItemStack, par4, par5, (String)null);
   }

   public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5, String par6Str) {
      if(par3ItemStack != null) {
         if(par3ItemStack.stackSize > 1 || par6Str != null) {
            String health = par6Str == null?String.valueOf(par3ItemStack.stackSize):par6Str;
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glDisable(3042);
            par1FontRenderer.drawStringWithShadow(health, par4 + 19 - 2 - par1FontRenderer.getStringWidth(health), par5 + 6 + 3, 16777215);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
         }

         if(par3ItemStack.getItem().showDurabilityBar(par3ItemStack)) {
            double health1 = par3ItemStack.getItem().getDurabilityForDisplay(par3ItemStack);
            int j1 = (int)Math.round(13.0D - health1 * 13.0D);
            int k = (int)Math.round(255.0D - health1 * 255.0D);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            GL11.glDisable(3008);
            GL11.glDisable(3042);
            Tessellator tessellator = Tessellator.instance;
            int l = 255 - k << 16 | k << 8;
            int i1 = (255 - k) / 4 << 16 | 16128;
            this.renderQuad(tessellator, par4 + 2, par5 + 13, 13, 2, 0);
            this.renderQuad(tessellator, par4 + 2, par5 + 13, 12, 1, i1);
            this.renderQuad(tessellator, par4 + 2, par5 + 13, j1, 1, l);
            GL11.glEnable(3553);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         }
      }

   }

   private void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6) {
      par1Tessellator.startDrawingQuads();
      par1Tessellator.setColorOpaque_I(par6);
      par1Tessellator.addVertex((double)(par2 + 0), (double)(par3 + 0), 0.0D);
      par1Tessellator.addVertex((double)(par2 + 0), (double)(par3 + par5), 0.0D);
      par1Tessellator.addVertex((double)(par2 + par4), (double)(par3 + par5), 0.0D);
      par1Tessellator.addVertex((double)(par2 + par4), (double)(par3 + 0), 0.0D);
      par1Tessellator.draw();
   }

   public void renderIcon(int par1, int par2, IIcon par3Icon, int par4, int par5) {
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par5), (double)this.zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
      tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + par5), (double)this.zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMaxV());
      tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + 0), (double)this.zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMinV());
      tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)this.zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMinV());
      tessellator.draw();
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return this.getEntityTexture((EntityItem)par1Entity);
   }

   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRender((EntityItem)par1Entity, par2, par4, par6, par8, par9);
   }

   public boolean shouldSpreadItems() {
      return true;
   }

   public boolean shouldBob() {
      return true;
   }

   public byte getMiniBlockCount(ItemStack stack, byte original) {
      return original;
   }

   public byte getMiniItemCount(ItemStack stack, byte original) {
      return original;
   }

}
