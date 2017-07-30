package com.emoniph.witchery.client;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.ModelOverlayRenderer;
import com.emoniph.witchery.brewing.potions.PotionResizing;
import com.emoniph.witchery.client.TransformBat;
import com.emoniph.witchery.client.TransformOtherPlayer;
import com.emoniph.witchery.client.TransformWolf;
import com.emoniph.witchery.client.TransformWolfman;
import com.emoniph.witchery.client.model.ModelGrotesque;
import com.emoniph.witchery.client.renderer.RenderOtherPlayer;
import com.emoniph.witchery.client.renderer.RenderVillagerBed;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.ExtendedVillager;
import com.emoniph.witchery.common.Shapeshift;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.entity.EntityVillageGuard;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.RenderUtil;
import com.emoniph.witchery.util.TransformCreature;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Pre;
import net.minecraftforge.client.event.RenderLivingEvent.Post;
import net.minecraftforge.client.event.RenderPlayerEvent.SetArmorModel;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

@SideOnly(Side.CLIENT)
public class ClientEvents {

   private static final ResourceLocation BLOODDROP_BG = new ResourceLocation("witchery:textures/gui/bdropfaded.png");
   private static final ResourceLocation BLOODDROP = new ResourceLocation("witchery:textures/gui/bdrop.png");
   private static final ModelGrotesque DEMON_HEAD_MODEL = new ModelGrotesque();
   private static final ResourceLocation TEXTURE = new ResourceLocation("witchery", "textures/entities/Demon.png");
   TransformWolf wolf = new TransformWolf();
   TransformWolfman wolfman = new TransformWolfman();
   TransformBat bat = new TransformBat();
   TransformOtherPlayer otherPlayer = new TransformOtherPlayer();
   RenderVillagerBed renderBed = new RenderVillagerBed();
   private static final ResourceLocation wolfSkin = new ResourceLocation("witchery", "textures/entities/werewolf_man.png");


   @SubscribeEvent
   public void onMouseEvent(MouseEvent event) {
      Minecraft mc = Minecraft.getMinecraft();
      ExtendedPlayer playerEx = ExtendedPlayer.get(mc.thePlayer);
      if(playerEx.isVampire() && event.dwheel != 0) {
         int MAXPOWER = playerEx.getMaxAvailablePowerOrdinal();
         if(mc.thePlayer.inventory.currentItem == 0) {
            int power = playerEx.getSelectedVampirePower().ordinal();
            if(event.dwheel > 0) {
               if(power == MAXPOWER) {
                  playerEx.setSelectedVampirePower(ExtendedPlayer.VampirePower.NONE, true);
               } else {
                  playerEx.setSelectedVampirePower(ExtendedPlayer.VampirePower.values()[power + 1], true);
                  event.setCanceled(true);
               }
            } else if(power > 0) {
               playerEx.setSelectedVampirePower(ExtendedPlayer.VampirePower.values()[power - 1], true);
               event.setCanceled(true);
            }
         } else if(mc.thePlayer.inventory.currentItem == 8 && event.dwheel < 0) {
            playerEx.setSelectedVampirePower(ExtendedPlayer.VampirePower.values()[MAXPOWER], true);
         }
      }

   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public void onRenderGameOverlay(Pre event) {
      switch(ClientEvents.NamelessClass737992562.$SwitchMap$net$minecraftforge$client$event$RenderGameOverlayEvent$ElementType[event.type.ordinal()]) {
      case 1:
         ClientEvents.GUIOverlay.INSTANCE.renderHotbar(event);
         break;
      case 2:
         EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if(playerEx.isVampire()) {
            float left = Config.instance().guiOnLeft?10.0F:(float)(event.resolution.getScaledWidth() - 10);
            float top = (float)event.resolution.getScaledHeight() * 0.5F + 16.0F;
            int maxBlood = playerEx.getMaxBloodPower();
            boolean pscale = true;
            Minecraft.getMinecraft().renderEngine.bindTexture(BLOODDROP_BG);

            int j;
            for(j = 0; j < maxBlood / 250; ++j) {
               drawTexturedRect(left, top - (float)(j * 8), 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F);
            }

            Minecraft.getMinecraft().renderEngine.bindTexture(BLOODDROP);
            int pblood = playerEx.getBloodPower();

            for(j = 0; j < pblood / 250; ++j) {
               drawTexturedRect(left, top - (float)(j * 8), 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F);
            }

            if(pblood % 250 > 0) {
               float movingPosition = 8.0F * ((float)pblood % 250.0F) / 250.0F;
               drawTexturedRect(left, top - (float)(j * 8) + 8.0F - movingPosition, 0.0F, 8.0F - movingPosition, 8.0F, movingPosition, 8.0F, 8.0F);
            }

            MovingObjectPosition var20 = InfusionOtherwhere.raytraceEntities(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer, true, 5.0D);
            if(var20 != null && var20.entityHit != null) {
               int blood = -1;
               if(var20.entityHit instanceof EntityVillager) {
                  EntityVillager tscale = (EntityVillager)var20.entityHit;
                  ExtendedVillager percent = ExtendedVillager.get(tscale);
                  if(percent.isClientSynced()) {
                     blood = percent.getBlood();
                  }
               } else if(var20.entityHit instanceof EntityVillageGuard) {
                  EntityVillageGuard var21 = (EntityVillageGuard)var20.entityHit;
                  blood = var21.getBlood();
               } else if(var20.entityHit instanceof EntityPlayer) {
                  EntityPlayer var22 = (EntityPlayer)var20.entityHit;
                  ExtendedPlayer var25 = ExtendedPlayer.get(var22);
                  if(!var25.isVampire()) {
                     blood = var25.getHumanBlood();
                  }
               }

               if(blood >= 0) {
                  boolean var24 = true;
                  int var23 = (int)((float)blood / 500.0F * 100.0F);
                  float midX = (float)(event.resolution.getScaledWidth() / 2);
                  float midY = (float)(event.resolution.getScaledHeight() / 2);
                  Minecraft.getMinecraft().renderEngine.bindTexture(BLOODDROP_BG);

                  int i;
                  for(i = 0; i < 4; ++i) {
                     drawTexturedRect(midX - 16.0F + (float)(i * 8), midY + 10.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F);
                  }

                  Minecraft.getMinecraft().renderEngine.bindTexture(BLOODDROP);

                  for(i = 0; i < var23 / 25; ++i) {
                     drawTexturedRect(midX - 16.0F + (float)(i * 8), midY + 10.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F);
                  }

                  float scale;
                  if(var23 % 25 > 0) {
                     scale = 8.0F * ((float)var23 % 25.0F) / 25.0F;
                     float text = scale / 8.0F;
                     float width = 8.0F - scale;
                     drawTexturedRect(midX - 16.0F + (float)(i * 8), midY + 10.0F + width, 0.0F, width, 8.0F, scale, 8.0F, 8.0F);
                  }

                  if(Config.instance().hudShowVampireTargetBloodText) {
                     scale = 0.5F;
                     GL11.glScalef(scale, scale, scale);
                     String var27 = String.format("%d/%d", new Object[]{Integer.valueOf(blood), Integer.valueOf(500)});
                     int var26 = RenderManager.instance.getFontRenderer().getStringWidth(var27);
                     RenderManager.instance.getFontRenderer().drawString(var27, (int)((float)(event.resolution.getScaledWidth() / 2 - var26 / 4) / scale), (int)((float)(event.resolution.getScaledHeight() / 2 + 22) / scale), 13369344);
                     GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
                  }
               }
            }
         }
      }

   }

   private static void drawTexturedRect(float p_146110_0_, float p_146110_1_, float p_146110_2_, float p_146110_3_, float p_146110_4_, float p_146110_5_, float p_146110_6_, float p_146110_7_) {
      float f4 = 1.0F / p_146110_6_;
      float f5 = 1.0F / p_146110_7_;
      Tessellator tessellator = Tessellator.instance;
      tessellator.startDrawingQuads();
      tessellator.addVertexWithUV((double)p_146110_0_, (double)(p_146110_1_ + p_146110_5_), 0.0D, (double)(p_146110_2_ * f4), (double)((p_146110_3_ + p_146110_5_) * f5));
      tessellator.addVertexWithUV((double)(p_146110_0_ + p_146110_4_), (double)(p_146110_1_ + p_146110_5_), 0.0D, (double)((p_146110_2_ + p_146110_4_) * f4), (double)((p_146110_3_ + p_146110_5_) * f5));
      tessellator.addVertexWithUV((double)(p_146110_0_ + p_146110_4_), (double)p_146110_1_, 0.0D, (double)((p_146110_2_ + p_146110_4_) * f4), (double)(p_146110_3_ * f5));
      tessellator.addVertexWithUV((double)p_146110_0_, (double)p_146110_1_, 0.0D, (double)(p_146110_2_ * f4), (double)(p_146110_3_ * f5));
      tessellator.draw();
   }

   @SubscribeEvent
   public void onSetArmorModel(SetArmorModel event) {
      EntityPlayer player = event.entityPlayer;
      if(!player.isInvisible() && Infusion.getNBT(player).hasKey("witcheryGrotesque")) {
         GL11.glPushMatrix();
         Minecraft mc = Minecraft.getMinecraft();
         mc.getTextureManager().bindTexture(TEXTURE);
         float par9 = event.partialRenderTick;
         float f6 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * par9;
         float f2 = this.interpolateRotation(player.prevRenderYawOffset, player.renderYawOffset, par9);
         float f3 = this.interpolateRotation(player.prevRotationYawHead, player.rotationYawHead, par9);
         DEMON_HEAD_MODEL.render(event.entityPlayer, 0.0F, 0.0F, 0.0F, f3 - f2, f6, 0.0625F);
         GL11.glPopMatrix();
      }

   }

   @SubscribeEvent
   public void onLivingJump(LivingJumpEvent event) {
      if(event.entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.entity;
         Shapeshift.INSTANCE.updateJump(player);
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGH
   )
   public void onPlayerPreRender(net.minecraftforge.client.event.RenderLivingEvent.Pre event) {
      if(event.entity instanceof EntityVillager) {
         ExtendedVillager player = ExtendedVillager.get((EntityVillager)event.entity);
         GL11.glPushMatrix();
         if(player != null && player.isSleeping()) {
            GL11.glTranslated(event.x, event.y, event.z);
            this.renderBed.render((float)event.x, (float)event.y, (float)event.z, 0);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslated(0.5D, -1.25D, 0.0D);
            event.entity.setRotationYawHead(90.0F);
            GL11.glTranslated(-event.x, -event.y, -event.z);
         }
      } else if(event.entity instanceof EntityPlayer) {
         EntityPlayer player1 = (EntityPlayer)event.entity;
         if(WorldProviderDreamWorld.getPlayerIsGhost(Infusion.getNBT(player1))) {
            RenderUtil.blend(true);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.51F);
         }

         ExtendedPlayer playerEx = ExtendedPlayer.get(player1);
         int creatureType = playerEx.getCreatureTypeOrdinal();
         if(creatureType > 0 && !(event.renderer instanceof RenderOtherPlayer)) {
            event.setCanceled(true);
            PotionEffect pe = player1.getActivePotionEffect(Witchery.Potions.RESIZING);
            if(pe != null) {
               GL11.glPushMatrix();
               GL11.glTranslated(event.x, event.y, event.z);
               float gui = PotionResizing.getModifiedScaleFactor(player1, pe.getAmplifier());
               GL11.glScalef(gui, gui, gui);
               GL11.glTranslated(-event.x, -event.y, -event.z);
            }

            boolean gui1 = player1.rotationYawHead == player1.rotationYaw && player1.prevRotationYawHead == player1.rotationYaw && RenderManager.instance.playerViewY == 180.0F && Minecraft.getMinecraft().currentScreen != null;
            float partialTicks = gui1?0.0F:ModelOverlayRenderer.getRenderPartialTicks();
            if(creatureType == 1) {
               this.wolf.render(event.entity.worldObj, event.entity, event.x, event.y, event.z, event.renderer, partialTicks, gui1);
            } else if(creatureType == 2) {
               this.wolfman.render(event.entity.worldObj, event.entity, event.x, event.y, event.z, event.renderer, partialTicks, gui1);
            } else if(creatureType == 3) {
               this.bat.render(event.entity.worldObj, event.entity, event.x, event.y, event.z, event.renderer, partialTicks, gui1);
            } else if(creatureType == 4 && playerEx.getOtherPlayerSkin() != null && !playerEx.getOtherPlayerSkin().equals("")) {
               this.otherPlayer.render(event.entity.worldObj, event.entity, event.x, event.y, event.z, event.renderer, partialTicks, gui1);
            }

            if(pe != null) {
               GL11.glPopMatrix();
            }
         }
      }

   }

   @SubscribeEvent
   public void onRenderLivingSpecialsPre(net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre event) {
      if(!event.isCanceled() && Config.instance().allowNameplateMasquerading && event.entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.entity;
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         if(playerEx.getCreatureType() == TransformCreature.PLAYER) {
            event.setCanceled(true);
            GL11.glAlphaFunc(516, 0.1F);
            EntityLivingBase p_77033_1_ = event.entity;
            double p_77033_2_ = event.x;
            double p_77033_4_ = event.y;
            double p_77033_6_ = event.z;
            RenderManager renderManager = RenderManager.instance;
            if(Minecraft.isGuiEnabled() && p_77033_1_ != renderManager.livingPlayer && !p_77033_1_.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) && p_77033_1_.riddenByEntity == null) {
               float f = 1.6F;
               float f1 = 0.016666668F * f;
               double d3 = p_77033_1_.getDistanceSqToEntity(renderManager.livingPlayer);
               float f2 = p_77033_1_.isSneaking()?32.0F:64.0F;
               if(d3 < (double)(f2 * f2)) {
                  String skin = playerEx.getOtherPlayerSkin();
                  if(skin == null || skin.isEmpty()) {
                     return;
                  }

                  String s = (new ChatComponentText(skin)).getFormattedText();
                  if(p_77033_1_.isSneaking()) {
                     FontRenderer fontrenderer = renderManager.getFontRenderer();
                     GL11.glPushMatrix();
                     GL11.glTranslatef((float)p_77033_2_ + 0.0F, (float)p_77033_4_ + p_77033_1_.height + 0.5F, (float)p_77033_6_);
                     GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                     GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                     GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                     GL11.glScalef(-f1, -f1, f1);
                     GL11.glDisable(2896);
                     GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                     GL11.glDepthMask(false);
                     GL11.glEnable(3042);
                     OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                     Tessellator tessellator = Tessellator.instance;
                     GL11.glDisable(3553);
                     tessellator.startDrawingQuads();
                     int i = fontrenderer.getStringWidth(s) / 2;
                     tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                     tessellator.addVertex((double)(-i - 1), -1.0D, 0.0D);
                     tessellator.addVertex((double)(-i - 1), 8.0D, 0.0D);
                     tessellator.addVertex((double)(i + 1), 8.0D, 0.0D);
                     tessellator.addVertex((double)(i + 1), -1.0D, 0.0D);
                     tessellator.draw();
                     GL11.glEnable(3553);
                     GL11.glDepthMask(true);
                     fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                     GL11.glEnable(2896);
                     GL11.glDisable(3042);
                     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                     GL11.glPopMatrix();
                  } else {
                     this.func_96449_a(p_77033_1_, p_77033_2_, p_77033_4_, p_77033_6_, s, f1, d3);
                  }
               }
            }
         }
      }

   }

   protected void func_96449_a(EntityLivingBase p_96449_1_, double p_96449_2_, double p_96449_4_, double p_96449_6_, String p_96449_8_, float p_96449_9_, double p_96449_10_) {
      if(!p_96449_1_.isPlayerSleeping()) {
         this.func_147906_a(p_96449_1_, p_96449_8_, p_96449_2_, p_96449_4_, p_96449_6_, 64);
      }

   }

   protected void func_147906_a(Entity p_147906_1_, String p_147906_2_, double p_147906_3_, double p_147906_5_, double p_147906_7_, int p_147906_9_) {
      RenderManager renderManager = RenderManager.instance;
      double d3 = p_147906_1_.getDistanceSqToEntity(renderManager.livingPlayer);
      if(d3 <= (double)(p_147906_9_ * p_147906_9_)) {
         FontRenderer fontrenderer = renderManager.getFontRenderer();
         float f = 1.6F;
         float f1 = 0.016666668F * f;
         GL11.glPushMatrix();
         GL11.glTranslatef((float)p_147906_3_ + 0.0F, (float)p_147906_5_ + p_147906_1_.height + 0.5F, (float)p_147906_7_);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
         GL11.glScalef(-f1, -f1, f1);
         GL11.glDisable(2896);
         GL11.glDepthMask(false);
         GL11.glDisable(2929);
         GL11.glEnable(3042);
         OpenGlHelper.glBlendFunc(770, 771, 1, 0);
         Tessellator tessellator = Tessellator.instance;
         byte b0 = 0;
         if(p_147906_2_.equals("deadmau5")) {
            b0 = -10;
         }

         GL11.glDisable(3553);
         tessellator.startDrawingQuads();
         int j = fontrenderer.getStringWidth(p_147906_2_) / 2;
         tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
         tessellator.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
         tessellator.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
         tessellator.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
         tessellator.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
         tessellator.draw();
         GL11.glEnable(3553);
         fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, 553648127);
         GL11.glEnable(2929);
         GL11.glDepthMask(true);
         fontrenderer.drawString(p_147906_2_, -fontrenderer.getStringWidth(p_147906_2_) / 2, b0, -1);
         GL11.glEnable(2896);
         GL11.glDisable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }

   }

   @SubscribeEvent
   public void onPlayerPostRender(Post event) {
      if(event.entity instanceof EntityVillager) {
         GL11.glPopMatrix();
      } else if(event.entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)event.entity;
         if(WorldProviderDreamWorld.getPlayerIsGhost(Infusion.getNBT(player))) {
            RenderUtil.blend(false);
         }
      }

   }

   @SubscribeEvent
   public void onRenderHand(RenderHandEvent event) {
      Minecraft mc = Minecraft.getMinecraft();
      ExtendedPlayer playerEx = ExtendedPlayer.get(mc.thePlayer);
      TransformCreature creatureType = playerEx.getCreatureType();
      if(creatureType != TransformCreature.NONE && (mc.thePlayer.getHeldItem() == null || creatureType != TransformCreature.WOLFMAN && creatureType != TransformCreature.PLAYER)) {
         event.setCanceled(true);
         this.renderArm(event.renderPass, event.partialTicks, mc, mc.thePlayer.getHeldItem() != null, creatureType, playerEx);
      }

   }

   public void renderArm(int renderPass, float partialTicks, Minecraft mc, boolean hasItem, TransformCreature creatureType, ExtendedPlayer playerEx) {
      GL11.glClear(256);
      float farPlaneDistance = (float)(mc.gameSettings.renderDistanceChunks * 16);
      double cameraZoom = 1.0D;
      double cameraYaw = 0.0D;
      double cameraPitch = 0.0D;
      if(mc.entityRenderer.debugViewDirection <= 0) {
         GL11.glMatrixMode(5889);
         GL11.glLoadIdentity();
         float f1 = 0.07F;
         if(mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float)(-(renderPass * 2 - 1)) * f1, 0.0F, 0.0F);
         }

         if(cameraZoom != 1.0D) {
            GL11.glTranslatef((float)cameraYaw, (float)(-cameraPitch), 0.0F);
            GL11.glScaled(cameraZoom, cameraZoom, 1.0D);
         }

         Project.gluPerspective(this.getFOVModifier(partialTicks, mc.entityRenderer, mc), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);
         if(mc.playerController.enableEverythingIsScrewedUpMode()) {
            float f2 = 0.6666667F;
            GL11.glScalef(1.0F, f2, 1.0F);
         }

         GL11.glMatrixMode(5888);
         GL11.glLoadIdentity();
         if(mc.gameSettings.anaglyph) {
            GL11.glTranslatef((float)(renderPass * 2 - 1) * 0.1F, 0.0F, 0.0F);
         }

         GL11.glPushMatrix();
         this.hurtCameraEffect(partialTicks, mc);
         if(mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks, mc);
         }

         if(mc.gameSettings.thirdPersonView == 0 && !mc.renderViewEntity.isPlayerSleeping() && !mc.gameSettings.hideGUI && !mc.playerController.enableEverythingIsScrewedUpMode()) {
            mc.entityRenderer.enableLightmap((double)partialTicks);
            if(hasItem && (creatureType == TransformCreature.WOLF || creatureType == TransformCreature.BAT)) {
               if(mc.thePlayer.getItemInUseCount() == 0) {
                  GL11.glTranslatef(-0.4F, 0.1F, 0.0F);
                  GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
               }

               mc.entityRenderer.itemRenderer.renderItemInFirstPerson(partialTicks);
            } else if(creatureType == TransformCreature.WOLF || creatureType == TransformCreature.PLAYER || creatureType == TransformCreature.WOLFMAN) {
               this.renderEmptyHand(mc, partialTicks, creatureType, playerEx);
            }

            mc.entityRenderer.disableLightmap((double)partialTicks);
         }

         GL11.glPopMatrix();
         if(mc.gameSettings.thirdPersonView == 0 && !mc.renderViewEntity.isPlayerSleeping()) {
            mc.entityRenderer.itemRenderer.renderOverlays(partialTicks);
            this.hurtCameraEffect(partialTicks, mc);
         }

         if(mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(partialTicks, mc);
         }
      }

   }

   private void renderEmptyHand(Minecraft mc, float p_78440_1_, TransformCreature creatureType, ExtendedPlayer playerEx) {
      float f1 = 1.0F;
      EntityClientPlayerMP entityclientplayermp = mc.thePlayer;
      float f2 = entityclientplayermp.prevRotationPitch + (entityclientplayermp.rotationPitch - entityclientplayermp.prevRotationPitch) * p_78440_1_;
      GL11.glPushMatrix();
      GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(entityclientplayermp.prevRotationYaw + (entityclientplayermp.rotationYaw - entityclientplayermp.prevRotationYaw) * p_78440_1_, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GL11.glPopMatrix();
      float f3 = entityclientplayermp.prevRenderArmPitch + (entityclientplayermp.renderArmPitch - entityclientplayermp.prevRenderArmPitch) * p_78440_1_;
      float f4 = entityclientplayermp.prevRenderArmYaw + (entityclientplayermp.renderArmYaw - entityclientplayermp.prevRenderArmYaw) * p_78440_1_;
      GL11.glRotatef((entityclientplayermp.rotationPitch - f3) * 0.1F, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef((entityclientplayermp.rotationYaw - f4) * 0.1F, 0.0F, 1.0F, 0.0F);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      if(!entityclientplayermp.isInvisible()) {
         GL11.glPushMatrix();
         float f13 = 0.8F;
         float f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
         float f6 = MathHelper.sin(f5 * 3.1415927F);
         float f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * 3.1415927F);
         GL11.glTranslatef(-f7 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f5) * 3.1415927F * 2.0F) * 0.4F, -f6 * 0.4F);
         GL11.glTranslatef(0.8F * f13, -0.75F * f13 - (1.0F - f1) * 0.6F, -0.9F * f13);
         GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
         GL11.glEnable('\u803a');
         f5 = entityclientplayermp.getSwingProgress(p_78440_1_);
         f6 = MathHelper.sin(f5 * f5 * 3.1415927F);
         f7 = MathHelper.sin(MathHelper.sqrt_float(f5) * 3.1415927F);
         GL11.glRotatef(f7 * 70.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-f6 * 20.0F, 0.0F, 0.0F, 1.0F);
         if(creatureType == TransformCreature.WOLF) {
            float render = 1.5F;
            GL11.glRotatef(30.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(-0.3F, 0.1F, -0.5F);
            GL11.glScalef(1.0F, 1.0F, 2.0F);
            mc.getTextureManager().bindTexture(wolfSkin);
         } else if(creatureType == TransformCreature.WOLFMAN) {
            mc.getTextureManager().bindTexture(wolfSkin);
         } else if(creatureType == TransformCreature.PLAYER) {
            mc.getTextureManager().bindTexture(playerEx.getLocationSkin());
         }

         GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
         GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
         GL11.glScalef(1.0F, 1.0F, 1.0F);
         GL11.glTranslatef(5.6F, 0.0F, 0.0F);
         Render render1 = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
         RenderPlayer renderplayer = (RenderPlayer)render1;
         float f10 = 1.0F;
         GL11.glScalef(f10, f10, f10);
         renderplayer.renderFirstPersonArm(mc.thePlayer);
         GL11.glPopMatrix();
      }

      GL11.glDisable('\u803a');
      RenderHelper.disableStandardItemLighting();
   }

   private void hurtCameraEffect(float p_78482_1_, Minecraft mc) {
      EntityLivingBase entitylivingbase = mc.renderViewEntity;
      float f1 = (float)entitylivingbase.hurtTime - p_78482_1_;
      float f2;
      if(entitylivingbase.getHealth() <= 0.0F) {
         f2 = (float)entitylivingbase.deathTime + p_78482_1_;
         GL11.glRotatef(40.0F - 8000.0F / (f2 + 200.0F), 0.0F, 0.0F, 1.0F);
      }

      if(f1 >= 0.0F) {
         f1 /= (float)entitylivingbase.maxHurtTime;
         f1 = MathHelper.sin(f1 * f1 * f1 * f1 * 3.1415927F);
         f2 = entitylivingbase.attackedAtYaw;
         GL11.glRotatef(-f2, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-f1 * 14.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
      }

   }

   private void setupViewBobbing(float p_78475_1_, Minecraft mc) {
      if(mc.renderViewEntity instanceof EntityPlayer) {
         EntityPlayer entityplayer = (EntityPlayer)mc.renderViewEntity;
         float f1 = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
         float f2 = -(entityplayer.distanceWalkedModified + f1 * p_78475_1_);
         float f3 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * p_78475_1_;
         float f4 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * p_78475_1_;
         GL11.glTranslatef(MathHelper.sin(f2 * 3.1415927F) * f3 * 0.5F, -Math.abs(MathHelper.cos(f2 * 3.1415927F) * f3), 0.0F);
         GL11.glRotatef(MathHelper.sin(f2 * 3.1415927F) * f3 * 3.0F, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(Math.abs(MathHelper.cos(f2 * 3.1415927F - 0.2F) * f3) * 5.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(f4, 1.0F, 0.0F, 0.0F);
      }

   }

   private float getFOVModifier(float partialTicks, EntityRenderer er, Minecraft mc) {
      if(er.debugViewDirection > 0) {
         return 90.0F;
      } else {
         EntityLivingBase entityplayer = mc.renderViewEntity;
         float f1 = 70.0F;
         if(entityplayer.getHealth() <= 0.0F) {
            float block = (float)entityplayer.deathTime + partialTicks;
            f1 /= (1.0F - 500.0F / (block + 500.0F)) * 2.0F + 1.0F;
         }

         Block block1 = ActiveRenderInfo.getBlockAtEntityViewpoint(mc.theWorld, entityplayer, partialTicks);
         if(block1.getMaterial() == Material.water) {
            f1 = f1 * 60.0F / 70.0F;
         }

         return f1;
      }
   }

   private float interpolateRotation(float par1, float par2, float par3) {
      float f3;
      for(f3 = par2 - par1; f3 < -180.0F; f3 += 360.0F) {
         ;
      }

      while(f3 >= 180.0F) {
         f3 -= 360.0F;
      }

      return par1 + par3 * f3;
   }


   // $FF: synthetic class
   static class NamelessClass737992562 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate;
      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower;
      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraftforge$client$event$RenderGameOverlayEvent$ElementType = new int[ElementType.values().length];


      static {
         try {
            $SwitchMap$net$minecraftforge$client$event$RenderGameOverlayEvent$ElementType[ElementType.HOTBAR.ordinal()] = 1;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            $SwitchMap$net$minecraftforge$client$event$RenderGameOverlayEvent$ElementType[ElementType.TEXT.ordinal()] = 2;
         } catch (NoSuchFieldError var11) {
            ;
         }

         $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower = new int[ExtendedPlayer.VampirePower.values().length];

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.DRINK.ordinal()] = 1;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.MESMERIZE.ordinal()] = 2;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.SPEED.ordinal()] = 3;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.BAT.ordinal()] = 4;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.ULTIMATE.ordinal()] = 5;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[ExtendedPlayer.VampirePower.NONE.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
            ;
         }

         $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate = new int[ExtendedPlayer.VampireUltimate.values().length];

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate[ExtendedPlayer.VampireUltimate.FARM.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate[ExtendedPlayer.VampireUltimate.STORM.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate[ExtendedPlayer.VampireUltimate.SWARM.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate[ExtendedPlayer.VampireUltimate.NONE.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static class GUIOverlay extends GuiIngame {

      public static final ClientEvents.GUIOverlay INSTANCE = new ClientEvents.GUIOverlay();
      private static final ResourceLocation WIDGITS = new ResourceLocation("textures/gui/widgets.png");
      private static final ResourceLocation TEETH = new ResourceLocation("witchery", "textures/items/vteeth.png");
      private static final ResourceLocation EYE = new ResourceLocation("witchery", "textures/items/veye.png");
      private static final ResourceLocation BAT = new ResourceLocation("witchery", "textures/items/vbat.png");
      private static final ResourceLocation RUN = new ResourceLocation("witchery", "textures/items/vspeed.png");
      private static final ResourceLocation FIST = new ResourceLocation("witchery", "textures/items/vfist.png");
      private static final ResourceLocation STORM = new ResourceLocation("witchery", "textures/items/vstorm.png");
      private static final ResourceLocation COFFIN = new ResourceLocation("witchery", "textures/items/vcoffin.png");
      private static final int WHITE = 16777215;


      public GUIOverlay() {
         super(Minecraft.getMinecraft());
      }

      public void renderHotbar(Pre event) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(super.mc.thePlayer);
         if(playerEx.isVampire()) {
            int width = event.resolution.getScaledWidth();
            int height = event.resolution.getScaledHeight();
            super.mc.mcProfiler.startSection("actionBar");
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            super.mc.renderEngine.bindTexture(WIDGITS);
            InventoryPlayer inv = super.mc.thePlayer.inventory;
            this.drawTexturedModalRect(width / 2 - 91, height - 22, 0, 0, 182, 22);
            int vpower = playerEx.getSelectedVampirePower().ordinal();
            if(vpower != 0) {
               this.drawTexturedModalRect(width / 2 - 91 - 1 + -vpower * 20, height - 22 - 1, 0, 22, 24, 22);
            } else {
               this.drawTexturedModalRect(width / 2 - 91 - 1 + inv.currentItem * 20, height - 22 - 1, 0, 22, 24, 22);
            }

            GL11.glDisable(3042);
            GL11.glEnable('\u803a');
            RenderHelper.enableGUIStandardItemLighting();
            super.zLevel += 50.0F;
            int x = width / 2 - 90 + -20 + 2;
            int z = height - 16 - 3;
            if(playerEx.getVampireLevel() >= 1) {
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               super.mc.renderEngine.bindTexture(TEETH);
               func_146110_a(x, z, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
               if(playerEx.getVampireLevel() >= 2) {
                  x = width / 2 - 90 + -40 + 2;
                  super.mc.renderEngine.bindTexture(EYE);
                  func_146110_a(x, z, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
                  if(playerEx.getVampireLevel() >= 4) {
                     x = width / 2 - 90 + -60 + 2;
                     super.mc.renderEngine.bindTexture(RUN);
                     func_146110_a(x, z, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
                     if(playerEx.getVampireLevel() >= 7) {
                        x = width / 2 - 90 + -80 + 2;
                        super.mc.renderEngine.bindTexture(BAT);
                        func_146110_a(x, z, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
                        if(playerEx.getVampireLevel() >= 10) {
                           x = width / 2 - 90 + -100 + 2;
                           switch(ClientEvents.NamelessClass737992562.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate[playerEx.getVampireUltimate().ordinal()]) {
                           case 1:
                              super.mc.renderEngine.bindTexture(COFFIN);
                              break;
                           case 2:
                              super.mc.renderEngine.bindTexture(STORM);
                              break;
                           case 3:
                           default:
                              super.mc.renderEngine.bindTexture(FIST);
                           }

                           if(playerEx.getVampireUltimateCharges() != 0 && playerEx.getVampireUltimate() != ExtendedPlayer.VampireUltimate.NONE) {
                              func_146110_a(x, z, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
                           } else {
                              GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.2F);
                              func_146110_a(x, z, 0.0F, 0.0F, 16, 16, 16.0F, 16.0F);
                           }
                        }
                     }
                  }
               }

               GL11.glDisable(3042);
            }

            super.zLevel -= 50.0F;

            for(int i = 0; i < 9; ++i) {
               x = width / 2 - 90 + i * 20 + 2;
               z = height - 16 - 3;
               this.renderInventorySlot(i, x, z, event.partialTicks);
            }

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable('\u803a');
            super.mc.mcProfiler.endSection();
            this.renderToolHightlight(playerEx, width, height);
            event.setCanceled(true);
         }

      }

      protected void renderToolHightlight(ExtendedPlayer playerEx, int width, int height) {
         Minecraft mc = Minecraft.getMinecraft();
         if(mc.gameSettings.heldItemTooltips && --playerEx.highlightTicks > 0) {
            String name = "";
            switch(ClientEvents.NamelessClass737992562.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampirePower[playerEx.getSelectedVampirePower().ordinal()]) {
            case 1:
               name = Witchery.resource("witchery.vampirepower.feed");
               break;
            case 2:
               name = Witchery.resource("witchery.vampirepower.eye");
               break;
            case 3:
               name = Witchery.resource("witchery.vampirepower.speed");
               break;
            case 4:
               name = Witchery.resource("witchery.vampirepower.bat");
               break;
            case 5:
               switch(ClientEvents.NamelessClass737992562.$SwitchMap$com$emoniph$witchery$common$ExtendedPlayer$VampireUltimate[playerEx.getVampireUltimate().ordinal()]) {
               case 1:
                  name = String.format(Witchery.resource("witchery.vampirepower.uteleport"), new Object[]{Integer.valueOf(playerEx.getVampireUltimateCharges())});
                  break;
               case 2:
                  name = String.format(Witchery.resource("witchery.vampirepower.ustorm"), new Object[]{Integer.valueOf(playerEx.getVampireUltimateCharges())});
                  break;
               case 3:
                  name = String.format(Witchery.resource("witchery.vampirepower.ubats"), new Object[]{Integer.valueOf(playerEx.getVampireUltimateCharges())});
                  break;
               case 4:
                  name = String.format(Witchery.resource("witchery.vampirepower.unone"), new Object[]{Integer.valueOf(playerEx.getVampireUltimateCharges())});
               }
            case 6:
            }

            if(name.equals("")) {
               return;
            }

            int opacity = (int)((float)playerEx.highlightTicks * 256.0F / 10.0F);
            if(opacity > 255) {
               opacity = 255;
            }

            if(opacity > 0) {
               int y = height - 69;
               if(!mc.playerController.shouldDrawHUD()) {
                  y += 14;
               }

               GL11.glPushMatrix();
               GL11.glEnable(3042);
               OpenGlHelper.glBlendFunc(770, 771, 1, 0);
               FontRenderer font = RenderManager.instance.getFontRenderer();
               if(font != null) {
                  int x = (width - font.getStringWidth(name)) / 2;
                  font.drawStringWithShadow(name, x, y, 16777215 | opacity << 24);
               }

               GL11.glDisable(3042);
               GL11.glPopMatrix();
            }
         }

      }

      protected void renderExtraInventorySlot(ItemStack itemstack, int p_73832_2_, int p_73832_3_, float p_73832_4_) {
         if(itemstack != null) {
            float f1 = (float)itemstack.animationsToGo - p_73832_4_;
            if(f1 > 0.0F) {
               GL11.glPushMatrix();
               float f2 = 1.0F + f1 / 5.0F;
               GL11.glTranslatef((float)(p_73832_2_ + 8), (float)(p_73832_3_ + 12), 0.0F);
               GL11.glScalef(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
               GL11.glTranslatef((float)(-(p_73832_2_ + 8)), (float)(-(p_73832_3_ + 12)), 0.0F);
            }

            GuiIngame.itemRenderer.renderItemAndEffectIntoGUI(super.mc.fontRenderer, super.mc.getTextureManager(), itemstack, p_73832_2_, p_73832_3_);
            if(f1 > 0.0F) {
               GL11.glPopMatrix();
            }

            GuiIngame.itemRenderer.renderItemOverlayIntoGUI(super.mc.fontRenderer, super.mc.getTextureManager(), itemstack, p_73832_2_, p_73832_3_);
         }

      }

   }
}
