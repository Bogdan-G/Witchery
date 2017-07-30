package com.emoniph.witchery.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class BubblesFX extends EntityFX {

   public static final ResourceLocation particles = new ResourceLocation("witchery:textures/particle/power.png");
   private boolean canMove = false;


   public BubblesFX(World world, double x, double y, double z) {
      super(world, x, y, z);
      super.noClip = true;
   }

   public void renderParticle(Tessellator tess, float partialTicks, float par3, float par4, float par5, float par6, float par7) {
      Minecraft.getMinecraft().renderEngine.bindTexture(particles);
      GL11.glDepthMask(false);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glAlphaFunc(516, 0.003921569F);
      tess.startDrawingQuads();
      tess.setBrightness(this.getBrightnessForRender(partialTicks));
      byte typeIndex = 16;
      int par1 = typeIndex + super.particleAge * 8 / 20 % 16;
      int particleTextureIndexX = par1 % 16;
      int particleTextureIndexY = par1 / 16;
      float f6 = (float)particleTextureIndexX / 16.0F;
      float f7 = f6 + 0.0624375F;
      float f8 = (float)particleTextureIndexY / 16.0F;
      float f9 = f8 + 0.0624375F;
      float scale = 0.1F * super.particleScale;
      float x = (float)(super.prevPosX + (super.posX - super.prevPosX) * (double)partialTicks - EntityFX.interpPosX);
      float y = (float)(super.prevPosY + (super.posY - super.prevPosY) * (double)partialTicks - EntityFX.interpPosY);
      float z = (float)(super.prevPosZ + (super.posZ - super.prevPosZ) * (double)partialTicks - EntityFX.interpPosZ);
      tess.setColorRGBA_F(super.particleRed, super.particleGreen, super.particleBlue, 1.0F);
      tess.addVertexWithUV((double)(x - par3 * scale - par6 * scale), (double)(y - par4 * scale), (double)(z - par5 * scale - par7 * scale), (double)f7, (double)f9);
      tess.addVertexWithUV((double)(x - par3 * scale + par6 * scale), (double)(y + par4 * scale), (double)(z - par5 * scale + par7 * scale), (double)f7, (double)f8);
      tess.addVertexWithUV((double)(x + par3 * scale + par6 * scale), (double)(y + par4 * scale), (double)(z + par5 * scale + par7 * scale), (double)f6, (double)f8);
      tess.addVertexWithUV((double)(x + par3 * scale - par6 * scale), (double)(y - par4 * scale), (double)(z + par5 * scale - par7 * scale), (double)f6, (double)f9);
      tess.draw();
      GL11.glDisable(3042);
      GL11.glDepthMask(true);
      GL11.glAlphaFunc(516, 0.1F);
   }

   public void onUpdate() {
      if(!super.worldObj.isRemote) {
         this.setDead();
      }

      super.prevPosX = super.posX;
      super.prevPosY = super.posY;
      super.prevPosZ = super.posZ;
      if(super.particleAge++ < Math.min(super.particleMaxAge, 600) && super.particleAge >= 0) {
         if((double)super.particleAge > (double)super.particleMaxAge * 0.9D) {
            super.noClip = false;
         }
      } else {
         this.setDead();
      }

      if(!super.isDead && this.canMove) {
         super.motionY -= 0.04D * (double)super.particleGravity;
         this.moveEntity(super.motionX, super.motionY, super.motionZ);
         if(super.onGround) {
            super.motionX *= 0.699999988079071D;
            super.motionZ *= 0.699999988079071D;
         }
      }

   }

   public int getFXLayer() {
      return 3;
   }

   public BubblesFX setMaxAge(int maxAge) {
      super.particleMaxAge = maxAge;
      return this;
   }

   public BubblesFX setGravity(float gravity) {
      super.particleGravity = gravity;
      return this;
   }

   public BubblesFX setCanMove(boolean canMove) {
      this.canMove = canMove;
      return this;
   }

   public BubblesFX setScale(float scale) {
      super.particleScale = scale;
      return this;
   }

}
