package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.model.ModelBrewBottle;
import com.emoniph.witchery.util.RenderUtil;
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
public class RenderBrewBottle implements IItemRenderer {

   protected ModelBrewBottle model = new ModelBrewBottle();
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/brewbottle.png");
   double rx = 100.0D;
   double ry = -51.0D;
   double rz = -81.0D;
   double tx = 0.125D;
   double ty = 0.12D;
   double tz = -0.85D;
   double scale = 1.0D;


   public boolean handleRenderType(ItemStack item, ItemRenderType type) {
      switch(RenderBrewBottle.NamelessClass568264657.$SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRenderType[type.ordinal()]) {
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
      switch(RenderBrewBottle.NamelessClass568264657.$SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRenderType[type.ordinal()]) {
      case 1:
      case 2:
         GL11.glPushMatrix();
         if(data.length > 1 && data[1] != null) {
            if(data[1] instanceof EntityPlayer) {
               EntityPlayer SCALE = (EntityPlayer)data[1];
               GL11.glRotatef((float)this.rx, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef((float)this.ry + 10.0F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef((float)this.rz, 0.0F, 0.0F, 1.0F);
               GL11.glTranslatef((float)this.tx - 0.05F, (float)this.ty + 0.2F, (float)this.tz + 0.1F);
               Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_URL);
               RenderUtil.blend(true);
               float SCALE1;
               if((EntityPlayer)data[1] == Minecraft.getMinecraft().renderViewEntity && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && (!(Minecraft.getMinecraft().currentScreen instanceof GuiInventory) && !(Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative) || RenderManager.instance.playerViewY != 180.0F)) {
                  GL11.glRotatef(10.0F, 0.0F, 1.0F, 0.0F);
                  GL11.glTranslatef(0.3F, 0.05F, 0.0F);
                  SCALE1 = 1.5F;
                  GL11.glScalef(1.5F, 1.5F, 1.5F);
                  this.model.render(SCALE, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
               } else {
                  SCALE1 = 1.3F;
                  GL11.glScalef(1.3F, 1.3F, 1.3F);
                  this.model.render(SCALE, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
               }

               RenderUtil.blend(false);
            } else {
               Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_URL);
               GL11.glRotatef((float)this.rx, 1.0F, 0.0F, 0.0F);
               GL11.glRotatef((float)this.ry, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef((float)this.rz, 0.0F, 0.0F, 1.0F);
               GL11.glTranslatef((float)this.tx, (float)this.ty, (float)this.tz);
               float SCALE2 = (float)this.scale;
               GL11.glScalef(SCALE2, SCALE2, SCALE2);
               this.model.render((Entity)data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            }
         }

         GL11.glPopMatrix();
      default:
      }
   }


   // $FF: synthetic class
   static class NamelessClass568264657 {

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
