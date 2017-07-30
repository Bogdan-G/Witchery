package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.model.ModelWolfman;
import com.emoniph.witchery.client.renderer.RenderWolfman;
import com.emoniph.witchery.entity.EntityReflection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderReflection extends RenderBiped {

   RenderWolfman wolfman = new RenderWolfman(new ModelWolfman(), 0.5F);
   public static final ResourceLocation SKIN = new ResourceLocation("witchery", "textures/entities/reflection.png");


   public RenderReflection() {
      super(new ModelBiped(), 0.5F);
   }

   public void doRender(Entity entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      if(((EntityReflection)entity).getModel() == 0) {
         super.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      } else {
         this.wolfman.setRenderManager(super.renderManager);
         this.wolfman.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      }

   }

   public void doRender(EntityLiving entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      if(((EntityReflection)entity).getModel() == 0) {
         super.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      } else {
         this.wolfman.setRenderManager(super.renderManager);
         this.wolfman.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      }

   }

   public void doRender(EntityLivingBase entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      if(((EntityReflection)entity).getModel() == 0) {
         super.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      } else {
         this.wolfman.setRenderManager(super.renderManager);
         this.wolfman.doRender(entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      }

   }

   public void doRenderShadowAndFire(Entity entity, double p_76979_2_, double p_76979_4_, double p_76979_6_, float p_76979_8_, float p_76979_9_) {
      if(((EntityReflection)entity).getModel() == 0) {
         super.doRenderShadowAndFire(entity, p_76979_2_, p_76979_4_, p_76979_6_, p_76979_8_, p_76979_9_);
      } else {
         this.wolfman.setRenderManager(super.renderManager);
         this.wolfman.doRenderShadowAndFire(entity, p_76979_2_, p_76979_4_, p_76979_6_, p_76979_8_, p_76979_9_);
      }

   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return this.getEntityTexture((EntityReflection)entity);
   }

   protected ResourceLocation getEntityTexture(EntityReflection entity) {
      if(entity.getModel() == 0) {
         ResourceLocation skin = entity.getLocationSkin();
         if(skin == null) {
            skin = SKIN;
         }

         return skin;
      } else {
         RenderWolfman var10000 = this.wolfman;
         return RenderWolfman.TEXTURE;
      }
   }

}
