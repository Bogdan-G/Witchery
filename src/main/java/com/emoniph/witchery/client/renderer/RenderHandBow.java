package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.client.model.ModelHandBow;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.item.ItemHandBow;
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
public class RenderHandBow implements IItemRenderer {

   protected ModelHandBow model = new ModelHandBow();
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/handbow.png");
   double rx = 100.0D;
   double ry = -51.0D;
   double rz = -81.0D;
   double tx = 0.125D;
   double ty = 0.12D;
   double tz = -0.85D;
   double scale = 1.0D;
   private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");


   public boolean handleRenderType(ItemStack item, ItemRenderType type) {
      switch(RenderHandBow.NamelessClass472725253.$SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRenderType[type.ordinal()]) {
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

   public void renderItem(ItemRenderType type, ItemStack stack, Object ... data) {
      switch(RenderHandBow.NamelessClass472725253.$SwitchMap$net$minecraftforge$client$IItemRenderer$ItemRenderType[type.ordinal()]) {
      case 1:
      case 2:
         GL11.glPushMatrix();
         ItemGeneral.BoltType boltType = ItemHandBow.getLoadedBoltType(stack);
         Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_URL);
         GL11.glRotatef((float)this.rx, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef((float)this.ry, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef((float)this.rz, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef((float)this.tx - 0.03F, (float)this.ty - 0.13F, (float)this.tz + 0.13F);
         float SCALE = (float)this.scale;
         GL11.glScalef(SCALE, SCALE, SCALE);
         if(data.length > 1 && data[1] != null) {
            if(data[1] instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)data[1];
               int useCount = player.getItemInUseCount() > 0?Witchery.Items.CROSSBOW_PISTOL.getMaxItemUseDuration(stack) - player.getItemInUseCount():0;
               if((EntityPlayer)data[1] == Minecraft.getMinecraft().renderViewEntity && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0 && (!(Minecraft.getMinecraft().currentScreen instanceof GuiInventory) && !(Minecraft.getMinecraft().currentScreen instanceof GuiContainerCreative) || RenderManager.instance.playerViewY != 180.0F)) {
                  if(player.getItemInUseCount() > 0) {
                     GL11.glRotatef(-25.0F, 0.0F, 1.0F, 0.0F);
                     GL11.glTranslatef(0.0F, 0.1F, 0.0F);
                  }

                  GL11.glTranslatef(0.2F, 0.1F, 0.0F);
                  this.renderModel(player, boltType, useCount);
               } else {
                  this.renderModel(player, boltType, useCount);
               }
            } else {
               this.renderModel((Entity)data[1], boltType, -1);
            }
         }

         GL11.glPopMatrix();
      default:
      }
   }

   private void renderModel(Entity player, ItemGeneral.BoltType boltType, int useCount) {
      if(boltType != null) {
         useCount = 100;
      } else if(!player.isSneaking() || useCount == -1) {
         useCount = 0;
      }

      this.model.render(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, boltType, useCount);
   }


   // $FF: synthetic class
   static class NamelessClass472725253 {

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
