package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityWitchProjectile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWitchProjectile extends RenderSnowball {

   public RenderWitchProjectile(Item item) {
      this(item, 0);
   }

   public RenderWitchProjectile(Item item, int damageValue) {
      super(item, damageValue);
   }

   public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
      if(entity instanceof EntityWitchProjectile) {
         EntityWitchProjectile entityProjectile = (EntityWitchProjectile)entity;
         IIcon icon = Witchery.Items.GENERIC.getIconFromDamage(entityProjectile.getDamageValue());
         if(icon != null) {
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par2, (float)par4, (float)par6);
            GL11.glEnable('\u803a');
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            this.bindEntityTexture(entity);
            Tessellator tessellator = Tessellator.instance;
            if(entityProjectile.getDamageValue() != Witchery.Items.GENERIC.itemQuicklime.damageValue) {
               GL11.glPushMatrix();
               this.func_77026_22(tessellator, Items.potionitem.getIconFromDamageForRenderPass(16384, 1));
               GL11.glPopMatrix();
            }

            this.func_77026_22(tessellator, icon);
            GL11.glDisable('\u803a');
            GL11.glPopMatrix();
         }
      } else {
         super.doRender(entity, par2, par4, par6, par8, par9);
      }

   }

   private void func_77026_22(Tessellator par1Tessellator, IIcon par2Icon) {
      float f = par2Icon.getMinU();
      float f1 = par2Icon.getMaxU();
      float f2 = par2Icon.getMinV();
      float f3 = par2Icon.getMaxV();
      float f4 = 1.0F;
      float f5 = 0.5F;
      float f6 = 0.25F;
      GL11.glRotatef(180.0F - super.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-super.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      par1Tessellator.startDrawingQuads();
      par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
      par1Tessellator.addVertexWithUV((double)(0.0F - f5), (double)(0.0F - f6), 0.0D, (double)f, (double)f3);
      par1Tessellator.addVertexWithUV((double)(f4 - f5), (double)(0.0F - f6), 0.0D, (double)f1, (double)f3);
      par1Tessellator.addVertexWithUV((double)(f4 - f5), (double)(f4 - f6), 0.0D, (double)f1, (double)f2);
      par1Tessellator.addVertexWithUV((double)(0.0F - f5), (double)(f4 - f6), 0.0D, (double)f, (double)f2);
      par1Tessellator.draw();
   }
}
