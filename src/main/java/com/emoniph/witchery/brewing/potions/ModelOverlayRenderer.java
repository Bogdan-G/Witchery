package com.emoniph.witchery.brewing.potions;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionResizing;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelOverlayRenderer {

   private static Field fieldMainModel;
   private static Field fieldTimer;
   private static Method methodRotateCorpse;
   private static Method methodHandleRotationFloat;
   private static Method methodPreRenderCallback;
   private static Timer timer;


   public static float getRenderPartialTicks() {
      if(fieldTimer == null) {
         fieldTimer = ReflectionHelper.findField(Minecraft.class, new String[]{"timer", "field_71428_T", "Q"});

         try {
            Minecraft ex = Minecraft.getMinecraft();
            if(timer == null) {
               timer = (Timer)fieldTimer.get(ex);
            }
         } catch (IllegalAccessException var1) {
            ;
         }
      }

      return timer != null?timer.renderPartialTicks:0.0F;
   }

   private static void ensureInitialized(RendererLivingEntity originalRenderer) {
      if(fieldTimer == null) {
         fieldTimer = ReflectionHelper.findField(Minecraft.class, new String[]{"timer", "field_71428_T", "Q"});

         try {
            Minecraft ex = Minecraft.getMinecraft();
            if(timer == null) {
               timer = (Timer)fieldTimer.get(ex);
            }
         } catch (IllegalAccessException var2) {
            ;
         }
      }

      if(fieldMainModel == null) {
         fieldMainModel = ReflectionHelper.findField(RendererLivingEntity.class, new String[]{"mainModel", "field_77045_g", "i"});
      }

      if(methodRotateCorpse == null) {
         methodRotateCorpse = ReflectionHelper.findMethod(RendererLivingEntity.class, originalRenderer, new String[]{"rotateCorpse", "func_77043_a", "a"}, new Class[]{EntityLivingBase.class, Float.TYPE, Float.TYPE, Float.TYPE});
      }

      if(methodHandleRotationFloat == null) {
         methodHandleRotationFloat = ReflectionHelper.findMethod(RendererLivingEntity.class, originalRenderer, new String[]{"handleRotationFloat", "func_77044_a", "b"}, new Class[]{EntityLivingBase.class, Float.TYPE});
      }

      if(methodPreRenderCallback == null) {
         methodPreRenderCallback = ReflectionHelper.findMethod(RendererLivingEntity.class, originalRenderer, new String[]{"preRenderCallback", "func_77041_b", "a"}, new Class[]{EntityLivingBase.class, Float.TYPE});
      }

   }

   public static void render(EntityLivingBase entity, double x, double y, double z, RendererLivingEntity originalRenderer) {
      try {
         ensureInitialized(originalRenderer);
         ModelBase ex = (ModelBase)fieldMainModel.get(originalRenderer);
         renderModel(entity, x, y, z, originalRenderer, ex);
      } catch (IllegalAccessException var9) {
         ;
      }

   }

   public static void renderModel(EntityLivingBase entity, double x, double y, double z, RendererLivingEntity originalRenderer, ModelBase model) {
      ensureInitialized(originalRenderer);
      if(timer != null) {
         renderModelAsOverlay(entity, model, x, y, z, timer.renderPartialTicks, originalRenderer);
      }

   }

   private static void renderModelAsOverlay(EntityLivingBase entity, ModelBase mainModel, double x, double y, double z, float partialRenderTicks, RendererLivingEntity originalRenderer) {
      try {
         GL11.glPushMatrix();
         mainModel.onGround = renderSwingProgress(entity, partialRenderTicks);
         mainModel.isRiding = entity.isRiding();
         mainModel.isChild = entity.isChild();
         float ex = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialRenderTicks);
         float f3 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialRenderTicks);
         float f4;
         if(entity.isRiding() && entity.ridingEntity instanceof EntityLivingBase) {
            EntityLivingBase f13 = (EntityLivingBase)entity.ridingEntity;
            ex = interpolateRotation(f13.prevRenderYawOffset, f13.renderYawOffset, partialRenderTicks);
            f4 = MathHelper.wrapAngleTo180_float(f3 - ex);
            if(f4 < -85.0F) {
               f4 = -85.0F;
            }

            if(f4 >= 85.0F) {
               f4 = 85.0F;
            }

            ex = f3 - f4;
            if(f4 * f4 > 2500.0F) {
               ex += f4 * 0.2F;
            }
         }

         float f131 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialRenderTicks;
         renderLivingAt(entity, x, y, z);
         f4 = ((Float)methodHandleRotationFloat.invoke(originalRenderer, new Object[]{entity, Float.valueOf(partialRenderTicks)})).floatValue();
         methodRotateCorpse.invoke(originalRenderer, new Object[]{entity, Float.valueOf(f4), Float.valueOf(ex), Float.valueOf(partialRenderTicks)});
         float f5 = 0.0625F;
         GL11.glEnable('\u803a');
         GL11.glScalef(-1.0F, -1.0F, 1.0F);
         methodPreRenderCallback.invoke(originalRenderer, new Object[]{entity, Float.valueOf(partialRenderTicks)});
         GL11.glTranslatef(0.0F, -24.0F * f5 - 0.0078125F, 0.0F);
         float f6 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialRenderTicks;
         float f7 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialRenderTicks);
         if(entity.isChild()) {
            f7 *= 3.0F;
         }

         if(f6 > 1.0F) {
            f6 = 1.0F;
         }

         GL11.glEnable(3008);
         mainModel.setLivingAnimations(entity, f7, f6, partialRenderTicks);
         float SCALE = 1.01F;
         GL11.glScalef(1.01F, 1.01F, 1.01F);
         renderModel(entity, f7, f6, f4, f3 - ex, f131, f5, mainModel);
      } catch (IllegalAccessException var22) {
         ;
      } catch (InvocationTargetException var23) {
         ;
      } finally {
         GL11.glPopMatrix();
      }

   }

   private static float interpolateRotation(float par1, float par2, float partialRenderTicks) {
      float f3;
      for(f3 = par2 - par1; f3 < -180.0F; f3 += 360.0F) {
         ;
      }

      while(f3 >= 180.0F) {
         f3 -= 360.0F;
      }

      return par1 + partialRenderTicks * f3;
   }

   private static void renderLivingAt(EntityLivingBase entity, double p_77039_2_, double p_77039_4_, double p_77039_6_) {
      GL11.glTranslatef((float)p_77039_2_, (float)p_77039_4_, (float)p_77039_6_);
      if(entity != null && entity.isPotionActive(Witchery.Potions.RESIZING)) {
         PotionEffect resizing = entity.getActivePotionEffect(Witchery.Potions.RESIZING);
         if(resizing != null) {
            float scale = PotionResizing.getModifiedScaleFactor(entity, resizing.getAmplifier());
            GL11.glScalef(scale, scale, scale);
         }
      }

   }

   private static float renderSwingProgress(EntityLivingBase entity, float partialRenderTicks) {
      return entity.getSwingProgress(partialRenderTicks);
   }

   private static void renderModel(EntityLivingBase entity, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_, ModelBase mainModel) {
      if(!entity.isInvisible()) {
         mainModel.render(entity, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
      } else if(!entity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)) {
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
         GL11.glDepthMask(false);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glAlphaFunc(516, 0.003921569F);
         mainModel.render(entity, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
         GL11.glDisable(3042);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glPopMatrix();
         GL11.glDepthMask(true);
      } else {
         mainModel.setRotationAngles(p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_, entity);
      }

   }
}
