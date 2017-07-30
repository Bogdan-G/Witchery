package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityGrenade;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGrenade extends RenderSnowball {

   private final Item item;


   public RenderGrenade(Item item) {
      this(item, 0);
   }

   public RenderGrenade(Item item, int damageValue) {
      super(item, damageValue);
      this.item = item;
   }

   public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
      EntityGrenade brew = (EntityGrenade)entity;
      Item item = brew.getMode() == 0?Witchery.Items.SUN_GRENADE:Witchery.Items.DUP_GRENADE;
      IIcon icon = item.getIcon((ItemStack)null, 1);
      if(icon != null) {
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par2, (float)par4, (float)par6);
         GL11.glEnable('\u803a');
         GL11.glScalef(0.3F, 0.3F, 0.3F);
         this.bindEntityTexture(entity);
         Tessellator tessellator = Tessellator.instance;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPushMatrix();
         this.drawIcon(tessellator, item.getIcon((ItemStack)null, 0));
         GL11.glPopMatrix();
         this.drawIcon(tessellator, icon);
         GL11.glDisable('\u803a');
         GL11.glPopMatrix();
      }

   }

   private void drawIcon(Tessellator tessalator, IIcon icon) {
      float f = icon.getMinU();
      float f1 = icon.getMaxU();
      float f2 = icon.getMinV();
      float f3 = icon.getMaxV();
      float f4 = 1.0F;
      float f5 = 0.5F;
      float f6 = 0.25F;
      GL11.glRotatef(180.0F - super.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-super.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      tessalator.startDrawingQuads();
      tessalator.setNormal(0.0F, 1.0F, 0.0F);
      tessalator.addVertexWithUV((double)(0.0F - f5), (double)(0.0F - f6), 0.0D, (double)f, (double)f3);
      tessalator.addVertexWithUV((double)(f4 - f5), (double)(0.0F - f6), 0.0D, (double)f1, (double)f3);
      tessalator.addVertexWithUV((double)(f4 - f5), (double)(f4 - f6), 0.0D, (double)f1, (double)f2);
      tessalator.addVertexWithUV((double)(0.0F - f5), (double)(f4 - f6), 0.0D, (double)f, (double)f2);
      tessalator.draw();
   }
}
