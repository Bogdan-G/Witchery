package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.model.ModelHuntsmanSpear;
import com.emoniph.witchery.util.Config;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderHuntsmanSpear implements IItemRenderer {

   protected ModelHuntsmanSpear model = new ModelHuntsmanSpear();
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/huntsmanspear.png");
   double rx = 100.0D;
   double ry = -51.0D;
   double rz = -81.0D;
   double tx = 0.1D;
   double ty = 0.12D;
   double tz = -0.72D;
   double scale = 1.0D;
   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");


   public boolean handleRenderType(ItemStack item, ItemRenderType type) {
      switch(RenderHuntsmanSpear.NamelessClass787014950.$SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRenderType[type.ordinal()]) {
      case 1:
      case 2:
         return true;
      default:
         return false;
      }
   }

   public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
      return false;
   }

   public void renderItem(ItemRenderType type, ItemStack item, Object ... data) {
      switch(RenderHuntsmanSpear.NamelessClass787014950.$SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRenderType[type.ordinal()]) {
      case 1:
      case 2:
         GL11.glPushMatrix();
         Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_URL);
         GL11.glRotatef((float)this.rx, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef((float)this.ry, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef((float)this.rz, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef((float)this.tx, (float)this.ty, (float)this.tz);
         if(data.length > 1 && data[1] != null) {
            if(data[1] instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)data[1];
               if((EntityPlayer)data[1] == Minecraft.getMinecraft().renderViewEntity && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && (!(Minecraft.getMinecraft().currentScreen instanceof GuiInventory) && !(Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative) || RenderManager.instance.playerViewY != 180.0F)) {
                  if(player.swingProgress > 0.0F) {
                     GL11.glTranslatef(0.0F, 0.15F, 0.0F);
                  }

                  GL11.glRotatef(50.0F * ((double)player.swingProgress <= 0.5D && player.swingProgress != 0.0F?1.0F - player.swingProgress:player.swingProgress), 1.0F, 0.0F, 0.0F);
                  this.renderModel(player);
               } else {
                  if(player.swingProgress > 0.0F) {
                     if((double)player.swingProgress > 0.3D) {
                        GL11.glRotatef(50.0F, 1.0F, 0.0F, 0.0F);
                     }

                     GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
                  }

                  this.renderModel(player);
               }
            } else {
               this.renderModel((Entity)data[1]);
            }
         }

         GL11.glPopMatrix();
      default:
      }
   }

   private void renderModel(Entity player) {
      this.model.render(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
      Minecraft mc = Minecraft.getMinecraft();
      if(mc.gameSettings.fancyGraphics && Config.instance().render3dGlintEffect) {
         float f9 = (float)player.ticksExisted;
         mc.renderEngine.bindTexture(RES_ITEM_GLINT);
         GL11.glEnable(3042);
         float f10 = 0.5F;
         GL11.glColor4f(f10, f10, f10, 1.0F);
         GL11.glDepthFunc(514);
         GL11.glDepthMask(false);

         for(int k = 0; k < 2; ++k) {
            GL11.glDisable(2896);
            float f11 = 0.76F;
            GL11.glColor4f(0.0F * f11, 0.5F * f11, 0.0F * f11, 1.0F);
            GL11.glBlendFunc(768, 1);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            float f12 = f9 * (0.001F + (float)k * 0.003F) * 20.0F;
            float f13 = 0.33333334F;
            GL11.glScalef(f13, f13, f13);
            GL11.glRotatef(30.0F - (float)k * 60.0F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.0F, f12, 0.0F);
            GL11.glMatrixMode(5888);
            this.model.render(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
         }

         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glMatrixMode(5890);
         GL11.glDepthMask(true);
         GL11.glLoadIdentity();
         GL11.glMatrixMode(5888);
         GL11.glEnable(2896);
         GL11.glDisable(3042);
         GL11.glDepthFunc(515);
      }

   }


   // $FF: synthetic class
   static class NamelessClass787014950 {

      // $FF: synthetic field
      static final int[] $SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRenderType = new int[ItemRenderType.values().length];


      static {
         try {
            $SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRenderType[ItemRenderType.EQUIPPED.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRenderType[ItemRenderType.EQUIPPED_FIRST_PERSON.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
