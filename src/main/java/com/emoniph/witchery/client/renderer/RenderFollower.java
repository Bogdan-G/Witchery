package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.entity.EntityFollower;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderFollower extends RenderBiped {

   private final ModelBiped model;
   private static final ResourceLocation TEXTURE_ELLE = new ResourceLocation("witchery", "textures/entities/lilithfol1.png");
   private static final ResourceLocation TEXTURE_FAIREST1 = new ResourceLocation("witchery", "textures/entities/fairest1.png");
   private static final ResourceLocation TEXTURE_FAIREST2 = new ResourceLocation("witchery", "textures/entities/fairest2.png");
   private static final ResourceLocation TEXTURE_FAIREST3 = new ResourceLocation("witchery", "textures/entities/fairest3.png");
   private static final ResourceLocation TEXTURE_FAIREST4 = new ResourceLocation("witchery", "textures/entities/fairest4.png");


   public RenderFollower(ModelBiped model) {
      this(model, 1.0F);
   }

   public RenderFollower(ModelBiped model, float scale) {
      super(model, 0.5F, scale);
      this.model = model;
   }

   protected ResourceLocation getEntityTexture(EntityLiving entity) {
      return this.getEntityTexture((EntityFollower)entity);
   }

   protected ResourceLocation getEntityTexture(EntityFollower entity) {
      switch(entity.getFollowerType()) {
      case 0:
         return TEXTURE_ELLE;
      case 1:
         return TEXTURE_FAIREST1;
      case 2:
         return TEXTURE_FAIREST2;
      case 3:
         return TEXTURE_FAIREST3;
      case 4:
         return TEXTURE_FAIREST4;
      case 5:
         return entity.getLocationSkin();
      default:
         return TEXTURE_FAIREST1;
      }
   }

}
