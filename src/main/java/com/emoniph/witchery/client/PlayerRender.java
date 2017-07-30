package com.emoniph.witchery.client;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionResizing;
import com.emoniph.witchery.client.RenderEntityViewer;
import com.emoniph.witchery.client.renderer.RenderInfusionEnergyBar;
import com.emoniph.witchery.infusion.InfusedBrewEffect;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePower;
import com.emoniph.witchery.infusion.infusions.creature.CreaturePowerSpeed;
import com.emoniph.witchery.infusion.infusions.symbols.EffectRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.StrokeSet;
import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import com.emoniph.witchery.integration.ModHookMorph;
import com.emoniph.witchery.item.ItemBrewBag;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.EntitySizeInfo;
import com.emoniph.witchery.util.KeyBindHelper;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.RenderUtil;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class PlayerRender {

   protected static RenderInfusionEnergyBar infusionEnergyBar;
   protected static RenderInfusionEnergyBar creatureEnergyBar;
   private boolean remoteViewingActive = true;
   public static long ticksSinceActive = 0L;
   public static boolean moveCameraActive = false;
   public static int moveCameraToEntityID = 0;
   private static final ResourceLocation RADIAL_LOCATION = new ResourceLocation("witchery", "textures/gui/radial.png");
   EntityRenderer prevRender;
   RenderEntityViewer renderer;
   int currentBeastForm = 0;
   private static final ResourceLocation BARK_TEXTURES = new ResourceLocation("witchery", "textures/gui/creatures.png");
   private static RenderItem drawItems = new RenderItem();
   private int lastY = 0;
   private static final int[] glyphOffsetX = new int[]{0, 0, 1, -1, 1, -1, -1, 1};
   private static final int[] glyphOffsetY = new int[]{-1, 1, 0, 0, -1, 1, -1, 1};
   private static final ResourceLocation TEXTURE_GRID = new ResourceLocation("witchery", "textures/gui/grid.png");
   private Field fieldAccess = null;


   @SubscribeEvent
   public void onRenderTick(RenderTickEvent event) {
      EntityClientPlayerMP player;
      Minecraft mc;
      if(event.phase == Phase.START) {
         player = Minecraft.getMinecraft().thePlayer;
         mc = Minecraft.getMinecraft();
         if(player != null && mc.currentScreen == null) {
            if(Minecraft.getSystemTime() - ticksSinceActive > 3000L) {
               moveCameraActive = false;
            }

            this.remoteViewingActive = moveCameraActive;
            if(this.remoteViewingActive) {
               Iterator screen = player.worldObj.loadedEntityList.iterator();

               while(screen.hasNext()) {
                  Object maxEnergy = screen.next();
                  if(((Entity)maxEnergy).getEntityId() == moveCameraToEntityID && maxEnergy instanceof EntityLivingBase) {
                     EntityLivingBase powerID = (EntityLivingBase)maxEnergy;
                     if(!powerID.isDead) {
                        Minecraft.getMinecraft().renderViewEntity = powerID;
                     }
                     break;
                  }
               }
            } else {
               EntitySizeInfo var29 = new EntitySizeInfo(player);
               PotionEffect var31 = Witchery.Potions.RESIZING != null?player.getActivePotionEffect(Witchery.Potions.RESIZING):null;
               if((var31 != null || !var29.isDefault) && !ModHookMorph.isMorphed(player, true)) {
                  if(this.renderer == null) {
                     this.renderer = new RenderEntityViewer(mc);
                  }

                  if(mc.gameSettings.thirdPersonView == 0) {
                     if(mc.entityRenderer != this.renderer) {
                        this.prevRender = mc.entityRenderer;
                        mc.entityRenderer = this.renderer;
                     }
                  } else if(this.prevRender != null) {
                     mc.entityRenderer = this.prevRender;
                  }

                  float var34 = 1.8F;
                  float charges = var29.defaultHeight / 1.8F * (var31 != null?PotionResizing.getScaleFactor(var31.getAmplifier()):1.0F);
                  float belt;
                  float stack;
                  if(charges < 1.0F) {
                     belt = 1.8F * (1.0F - charges);
                     stack = this.renderer.getOffset();
                     if(stack < belt) {
                        stack = Math.min(stack + 0.01F, belt);
                     } else if(stack > belt) {
                        stack = Math.min(stack - 0.01F, belt);
                     }

                     this.renderer.setOffset(stack);
                  } else {
                     belt = -(1.8F * charges - 1.8F);
                     stack = this.renderer.getOffset();
                     if(stack > belt) {
                        stack = Math.max(stack - 0.01F, belt);
                     }

                     this.renderer.setOffset(stack);
                  }
               } else if(this.prevRender != null && mc.entityRenderer != this.prevRender) {
                  if(this.renderer != null) {
                     this.renderer.setOffset(0.0F);
                  }

                  mc.entityRenderer = this.prevRender;
               }
            }
         }
      } else if(event.phase == Phase.END) {
         player = Minecraft.getMinecraft().thePlayer;
         if(player != null && Minecraft.getMinecraft().currentScreen == null) {
            mc = Minecraft.getMinecraft();
            if(this.remoteViewingActive) {
               Minecraft.getMinecraft().renderViewEntity = player;
            }

            ScaledResolution var30 = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
            int var32 = Infusion.getMaxEnergy(player);
            double var37;
            if(var32 > 0) {
               if(infusionEnergyBar == null) {
                  infusionEnergyBar = new RenderInfusionEnergyBar(true);
               }

               double var33 = Config.instance().guiOnLeft?20.0D:(double)(var30.getScaledWidth() - 20);
               var37 = (double)var30.getScaledHeight() / 2.0D - 16.0D;
               Infusion inv = Infusion.Registry.instance().get(player);
               infusionEnergyBar.draw(var33, var37, (double)Infusion.getCurrentEnergy(player) / (double)var32, player, inv.infusionID);
            }

            int var36 = CreaturePower.getCreaturePowerID(player);
            int var35 = CreaturePower.getCreaturePowerCharges(player);
            if(var36 > 0) {
               if(creatureEnergyBar == null) {
                  creatureEnergyBar = new RenderInfusionEnergyBar(false);
               }

               var37 = Config.instance().guiOnLeft?30.0D:(double)(var30.getScaledWidth() - 30);
               double var42 = (double)var30.getScaledHeight() / 2.0D - 16.0D;
               creatureEnergyBar.draw(var37, var42, (double)var35, player, var36);
            }

            ItemStack var38 = player.getEquipmentInSlot(2);
            if(var38 != null && var38.getItem() == Witchery.Items.BARK_BELT) {
               int var40 = Math.min(Witchery.Items.BARK_BELT.getChargeLevel(var38), Witchery.Items.BARK_BELT.getMaxChargeLevel(player));
               this.drawBarkBeltCharges(player, var40, var30);
            }

            this.drawInfusedBrews(player, var30);
            ItemStack var44 = player.getItemInUse();
            int x;
            int y;
            int color;
            if(var44 != null && var44.getItem() == Witchery.Items.MYSTIC_BRANCH) {
               byte[] var39 = player.getEntityData().getByteArray("Strokes");
               mc.getTextureManager().bindTexture(TEXTURE_GRID);
               GL11.glPushMatrix();
               byte var41 = 0;
               if(Config.instance().branchIconSet == 1) {
                  var41 = 64;
               }

               try {
                  x = var30.getScaledWidth() / 2 - 8;
                  y = var30.getScaledHeight() / 2 - 8;
                  boolean var45 = true;
                  this.lastY = this.lastY == 120?0:this.lastY + 1;
                  color = this.lastY / 8;
                  int var46 = color > 7?15 - color:color;

                  for(int var48 = 0; var48 < var39.length; ++var48) {
                     x += glyphOffsetX[var39[var48]] * 16;
                     y += glyphOffsetY[var39[var48]] * 16;
                     drawTexturedModalRect(x, y, var39[var48] * 16 + var41, var46 * 16, 16, 16);
                  }

                  SymbolEffect var47 = EffectRegistry.instance().getEffect(var39);
                  if(var47 != null) {
                     String var49 = var47.getLocalizedName();
                     int tx = var30.getScaledWidth() / 2 - (int)(getStringWidth(var49) / 2.0D);
                     int ty = var30.getScaledHeight() / 2 + 20;
                     drawString(var49, (double)tx, (double)ty, 16777215);
                  }
               } finally {
                  GL11.glPopMatrix();
               }
            } else if(var44 != null && var44.getItem() == Witchery.Items.BREW_BAG && !player.isSneaking()) {
               ItemBrewBag.InventoryBrewBag var43 = new ItemBrewBag.InventoryBrewBag(player);
               byte[] strokes = player.getEntityData().getByteArray("Strokes");
               GL11.glPushMatrix();

               try {
                  x = var30.getScaledWidth() / 2 - 8;
                  y = var30.getScaledHeight() / 2 - 8;
                  if(strokes.length == 0) {
                     mc.getTextureManager().bindTexture(RADIAL_LOCATION);
                     GL11.glPushMatrix();
                     float scale = 0.33333334F;
                     GL11.glTranslatef((float)(x - 42 + 5), (float)(y - 42 + 5), 0.0F);
                     GL11.glScalef(scale, scale, scale);
                     color = ItemBrewBag.getColor(var44);
                     float red = (float)(color >>> 16 & 255) / 256.0F;
                     float green = (float)(color >>> 8 & 255) / 256.0F;
                     float blue = (float)(color & 255) / 256.0F;
                     GL11.glColor4f(red, green, blue, 1.0F);
                     drawTexturedModalRect(8, 8, 0, 0, 256, 256);
                     GL11.glPopMatrix();
                  }

                  this.drawBrewInSlot(var43, 0, strokes, x + 0, y - 32, 0, -11, 1);
                  this.drawBrewInSlot(var43, 1, strokes, x + 24, y - 24, 23, 6, 0);
                  this.drawBrewInSlot(var43, 2, strokes, x + 32, y - 0, 23, 6, 0);
                  this.drawBrewInSlot(var43, 3, strokes, x + 24, y + 24, 23, 6, 0);
                  this.drawBrewInSlot(var43, 4, strokes, x + 0, y + 32, 0, 19, 1);
                  this.drawBrewInSlot(var43, 5, strokes, x - 24, y + 24, -5, 6, 2);
                  this.drawBrewInSlot(var43, 6, strokes, x - 32, y - 0, -5, 6, 2);
                  this.drawBrewInSlot(var43, 7, strokes, x - 24, y - 24, -5, 6, 2);
               } finally {
                  GL11.glPopMatrix();
               }
            }
         }
      }

   }

   private void drawInfusedBrews(EntityClientPlayerMP player, ScaledResolution screen) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      InfusedBrewEffect effect = InfusedBrewEffect.getActiveBrew(nbtPlayer);
      if(effect != null) {
         String remainingTime = InfusedBrewEffect.getMinutesRemaining(player.worldObj, nbtPlayer, effect);
         if(remainingTime != null && !remainingTime.isEmpty()) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.getTextureManager().bindTexture(BARK_TEXTURES);
            int tx = screen.getScaledWidth() / 2 - 91;
            int screenHeight = screen.getScaledHeight();
            int top = screen.getScaledHeight() / 2 + 26;
            int screenMid = screenHeight / 2;
            int left = Config.instance().guiOnLeft?17:screen.getScaledWidth() - 23;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean ICON_WIDTH = true;
            boolean ICON_HEIGHT = true;
            drawTexturedModalRect(left, top, effect.imageMapX, effect.imageMapY, 16, 16);
            double width = getStringWidth(remainingTime) / 2.0D;
            drawString(remainingTime, (double)(left + 8) - width, (double)(top + 10), -285212673);
         }
      }

   }

   private void drawBarkBeltCharges(EntityClientPlayerMP player, int beltCharges, ScaledResolution screen) {
      if(beltCharges > 0 && !player.capabilities.isCreativeMode) {
         Minecraft mc = Minecraft.getMinecraft();
         mc.getTextureManager().bindTexture(BARK_TEXTURES);
         int tx = screen.getScaledWidth() / 2 - 91;
         int par2 = screen.getScaledHeight();
         int ty = par2 / 2;
         IAttributeInstance attributeinstance = mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
         int i2 = par2 - 39;
         float f = (float)attributeinstance.getAttributeValue();
         float f1 = mc.thePlayer.getAbsorptionAmount();
         int j2 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
         int k2 = Math.max(10 - (j2 - 2), 3);
         int l2 = Witchery.modHooks.isTinkersPresent?i2 - 10:i2 - (j2 - 1) * k2 - 10;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         boolean iconOffsetX = false;
         boolean ICON_WIDTH = true;
         boolean ICON_HEIGHT = true;
         boolean iconOffsetY = true;

         for(int i = 0; i < beltCharges; ++i) {
            drawTexturedModalRect(tx + i * 8, l2, 0, 248, 8, 8);
         }
      }

   }

   private void drawBrewInSlot(ItemBrewBag.InventoryBrewBag inv, int slot, byte[] strokes, int x, int y, int fx, int fy, int align) {
      ItemStack brew = inv.getStackInSlot(slot);
      if(brew != null && (strokes.length == 0 || strokes[0] == StrokeSet.Stroke.INDEX_TO_STROKE[slot])) {
         drawItem(x, y, brew);
         String s = brew.getDisplayName();
         if(s != null) {
            s.trim();
            double fontX = (double)(x + fx);
            double fontY = (double)(y + fy);
            if(align != 0) {
               double width = getStringWidth(s);
               if(align == 1) {
                  fontX -= width / 2.0D;
               } else if(align == 2) {
                  fontX -= width;
               }
            }

            drawString(s, fontX, fontY, 2013265919);
         }
      }

   }

   private static FontRenderer getFontRenderer(ItemStack stack) {
      if(stack != null && stack.getItem() != null) {
         FontRenderer f = stack.getItem().getFontRenderer(stack);
         if(f != null) {
            return f;
         }
      }

      return Minecraft.getMinecraft().fontRenderer;
   }

   private static void drawItem(int i, int j, ItemStack itemstack) {
      drawItem(i, j, itemstack, getFontRenderer(itemstack));
   }

   private static void drawItem(int i, int j, ItemStack itemstack, FontRenderer fontRenderer) {
      Minecraft mc = Minecraft.getMinecraft();
      GL11.glEnable(2896);
      GL11.glEnable(2929);
      drawItems.zLevel += 100.0F;

      try {
         drawItems.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
         drawItems.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
      } catch (Exception var6) {
         ;
      }

      drawItems.zLevel -= 100.0F;
      GL11.glDisable(2896);
      GL11.glDisable(2929);
   }

   public static void drawString(String s, double x, double y, int color) {
      RenderHelper.disableStandardItemLighting();
      RenderUtil.blend(true);
      RenderUtil.render2d(true);
      Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, (int)x, (int)y, color);
      RenderUtil.render2d(false);
      RenderUtil.blend(false);
   }

   public static double getStringWidth(String s) {
      GL11.glPushAttrib(262144);

      double val;
      try {
         val = (double)Minecraft.getMinecraft().fontRenderer.getStringWidth(s);
      } finally {
         GL11.glPopAttrib();
      }

      return val;
   }

   private static void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6) {
      double zLevel = 0.0D;
      float f = 0.00390625F;
      float f1 = 0.00390625F;
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
      tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
      tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
      tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
      tessellator.draw();
   }

   @SubscribeEvent
   public void onClientTick(ClientTickEvent event) {
      Minecraft minecraft;
      EntityClientPlayerMP player;
      int var16;
      int var18;
      if(event.phase == Phase.START) {
         minecraft = Minecraft.getMinecraft();
         player = minecraft.thePlayer;
         if(player != null) {
            boolean e = true;
            int arr$ = CreaturePower.getCreaturePowerID(player);
            if(arr$ > 0) {
               CreaturePower len$ = CreaturePower.Registry.instance().get(arr$);
               if(len$ != null) {
                  len$.onUpdate(player.worldObj, player);
                  e = !(len$ instanceof CreaturePowerSpeed);
               }
            }

            if(player.getHeldItem() != null && player.getHeldItem().getItem() != null && e && (player.getHeldItem().getItem() == Witchery.Items.MYSTIC_BRANCH || player.getHeldItem().getItem() == Witchery.Items.BREW_BAG) && player.isUsingItem()) {
               boolean var17 = Math.abs(player.motionX) <= 0.1D && Math.abs(player.motionZ) <= 0.1D;
               if(player.worldObj.getBlock(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY) - 2, MathHelper.floor_double(player.posZ)) != Blocks.ice) {
                  if(player.onGround) {
                     if(!player.isInWater()) {
                        if(var17) {
                           player.motionX *= 1.6500000476837158D;
                           player.motionZ *= 1.6500000476837158D;
                        }
                     } else if(var17) {
                        player.motionX *= 1.100000023841858D;
                        player.motionZ *= 1.100000023841858D;
                     }
                  }
               } else if(var17) {
                  player.motionX *= 1.100000023841858D;
                  player.motionZ *= 1.100000023841858D;
               }
            }

            var16 = Infusion.getSinkingCurseLevel(player);
            if(var16 > 0 && player.isInWater()) {
               if(player.motionY < -0.03D && !player.onGround) {
                  player.motionY *= 1.5D + Math.min(0.05D * (double)(var16 - 1), 0.2D);
               } else if(!player.onGround && player.isInsideOfMaterial(Material.water) && player.motionY > 0.0D) {
                  ;
               }
            } else if(var16 > 0) {
               if(!player.capabilities.isCreativeMode && player.capabilities.allowFlying && player.capabilities.isFlying) {
                  player.motionY = -0.20000000298023224D;
               }
            } else if(player.isPotionActive(Potion.moveSlowdown) && !player.capabilities.isCreativeMode && player.capabilities.allowFlying && player.capabilities.isFlying) {
               PotionEffect i$ = player.getActivePotionEffect(Potion.moveSlowdown);
               if(i$ != null && i$.getAmplifier() > 4) {
                  player.motionY = -0.20000000298023224D;
               }
            }

            if(var16 == 0 && BlockUtil.getBlockMaterial(player).isLiquid() && player.getCurrentArmor(0) != null && player.getCurrentArmor(0).getItem() == Witchery.Items.DEATH_FEET && player.motionY < 0.0D) {
               player.motionY += 0.1D;
            }

            if(player.onGround && KeyBindHelper.isKeyBindDown(minecraft.gameSettings.keyBindJump)) {
               var18 = MathHelper.floor_double(player.posX);
               int field = MathHelper.floor_double(player.posY);
               int z = MathHelper.floor_double(player.posZ);
               if(player.worldObj.getBlock(var18, field - 1, z) == Witchery.Blocks.LEAPING_LILY) {
                  player.playSound("random.bowhit", 1.0F, 0.4F / ((float)player.worldObj.rand.nextDouble() * 0.4F + 0.8F));
               }
            }
         }
      } else if(event.phase == Phase.END) {
         minecraft = Minecraft.getMinecraft();
         player = minecraft.thePlayer;
         if(player != null && minecraft.currentScreen != null && minecraft.currentScreen instanceof InventoryEffectRenderer && player.dimension == Config.instance().dimensionDreamID && !player.capabilities.isCreativeMode) {
            if(this.fieldAccess == null) {
               try {
                  Field[] var14 = GuiScreen.class.getDeclaredFields();
                  if(var14.length > 3) {
                     if(var14[3].getType() == List.class) {
                        Field var12 = var14[3];
                        var12.setAccessible(true);
                        this.fieldAccess = var12;
                     } else {
                        Field[] var15 = var14;
                        var16 = var14.length;

                        for(var18 = 0; var18 < var16; ++var18) {
                           Field var19 = var15[var18];
                           if(var19.getType() == List.class) {
                              var19.setAccessible(true);
                              this.fieldAccess = var19;
                              break;
                           }
                        }
                     }
                  }
               } catch (Exception var11) {
                  Log.instance().debug(String.format("Exception occurred setting player gui. %s", new Object[]{var11.toString()}));
               }
            }

            if(this.fieldAccess != null) {
               try {
                  List var13 = (List)this.fieldAccess.get(minecraft.currentScreen);
                  if(var13.size() > 0) {
                     var13.clear();
                  }
               } catch (IllegalAccessException var10) {
                  Log.instance().warning(var10, "Exception occurred setting player gui screen");
               }
            }
         }
      }

   }

}
