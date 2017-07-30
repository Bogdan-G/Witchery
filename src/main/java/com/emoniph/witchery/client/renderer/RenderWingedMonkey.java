package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.entity.EntityWingedMonkey;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderWingedMonkey extends RenderLiving {

   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/wingedmonkey.png");
   public static final float[][] fleeceColorTable = new float[][]{{1.0F, 1.0F, 1.0F}, {0.85F, 0.5F, 0.2F}, {0.7F, 0.3F, 0.85F}, {0.4F, 0.6F, 0.85F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.5F, 0.65F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.5F, 0.6F}, {0.5F, 0.25F, 0.7F}, {0.2F, 0.3F, 0.7F}, {0.4F, 0.3F, 0.2F}, {0.4F, 0.5F, 0.2F}, {0.6F, 0.2F, 0.2F}, {0.1F, 0.1F, 0.1F}};


   public RenderWingedMonkey(ModelBase par1ModelBase, float par2) {
      super(par1ModelBase, par2);
   }

   public void doRenderMonkey(EntityWingedMonkey entity, double par2, double par4, double par6, float par8, float par9) {
      float f1 = 1.0F;
      int j = entity.getFeatherColor();
      if(j != 0) {
         float alpha = 0.84313726F;
         float bR = 0.41568628F;
         float bG = 0.3137255F;
         float bB = 0.24313726F;
         GL11.glColor3f(f1 * fleeceColorTable[j][0] * 0.15686274F + 0.41568628F, f1 * fleeceColorTable[j][1] * 0.15686274F + 0.3137255F, f1 * fleeceColorTable[j][2] * 0.15686274F + 0.24313726F);
      }

      super.doRender(entity, par2, par4, par6, par8, par9);
   }

   public void doRender(EntityLiving entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderMonkey((EntityWingedMonkey)entity, par2, par4, par6, par8, par9);
   }

   public void doRender(EntityLivingBase par1, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderMonkey((EntityWingedMonkey)par1, par2, par4, par6, par8, par9);
   }

   public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderMonkey((EntityWingedMonkey)entity, par2, par4, par6, par8, par9);
   }

   protected ResourceLocation getEntityTexture(Entity par1Entity) {
      return TEXTURE_URL;
   }

}
