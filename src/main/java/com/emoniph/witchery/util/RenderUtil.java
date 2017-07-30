package com.emoniph.witchery.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderUtil {

   public static void blend(boolean on) {
      if(on) {
         GL11.glPushAttrib(16448);
         GL11.glShadeModel(7425);
         GL11.glDisable(3008);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
      } else {
         GL11.glPopAttrib();
      }

   }

   public static void render2d(boolean on) {
      if(on) {
         GL11.glPushAttrib(8192);
         GL11.glDisable(2929);
         GL11.glDisable(2884);
         GL11.glDisable(2896);
      } else {
         GL11.glPopAttrib();
      }

   }
}
