package com.emoniph.witchery.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

public class RenderEntityViewer extends EntityRenderer {

   private final Minecraft mc;
   private float offsetY;


   public RenderEntityViewer(Minecraft mc) {
      super(mc, mc.getResourceManager());
      this.mc = mc;
   }

   public void setOffset(float offset) {
      this.offsetY = offset;
   }

   public float getOffset() {
      return this.offsetY;
   }

   private boolean canShiftView() {
      return this.mc.thePlayer != null && !this.mc.thePlayer.isPlayerSleeping() && !this.mc.thePlayer.isRiding();
   }

   public void updateCameraAndRender(float partialTicks) {
      if(this.canShiftView()) {
         this.mc.thePlayer.posY += (double)(-this.offsetY);
         this.mc.thePlayer.lastTickPosY += (double)(-this.offsetY);
         this.mc.thePlayer.prevPosY += (double)(-this.offsetY);
         float savedHeight = this.mc.thePlayer.eyeHeight;
         this.mc.thePlayer.eyeHeight = this.mc.thePlayer.getDefaultEyeHeight();
         super.updateCameraAndRender(partialTicks);
         this.mc.thePlayer.eyeHeight = savedHeight;
         this.mc.thePlayer.posY -= (double)(-this.offsetY);
         this.mc.thePlayer.lastTickPosY -= (double)(-this.offsetY);
         this.mc.thePlayer.prevPosY -= (double)(-this.offsetY);
      } else {
         super.updateCameraAndRender(partialTicks);
      }

   }

   public void getMouseOver(float partialTicks) {
      if(this.canShiftView()) {
         this.mc.thePlayer.posY += (double)(-this.offsetY);
         this.mc.thePlayer.prevPosY += (double)(-this.offsetY);
         this.mc.thePlayer.lastTickPosY += (double)(-this.offsetY);
         float savedHeight = this.mc.thePlayer.eyeHeight;
         this.mc.thePlayer.eyeHeight = this.mc.thePlayer.getDefaultEyeHeight();
         super.getMouseOver(partialTicks);
         this.mc.thePlayer.eyeHeight = savedHeight;
         this.mc.thePlayer.posY -= (double)(-this.offsetY);
         this.mc.thePlayer.prevPosY -= (double)(-this.offsetY);
         this.mc.thePlayer.lastTickPosY -= (double)(-this.offsetY);
      } else {
         super.getMouseOver(partialTicks);
      }

   }
}
