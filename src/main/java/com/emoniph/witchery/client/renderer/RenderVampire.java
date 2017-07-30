package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.model.ModelVampire;
import com.emoniph.witchery.entity.EntityVillageGuard;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderVampire extends RenderBiped {

   private static final ResourceLocation TEXTURE_URL = new ResourceLocation("witchery", "textures/entities/vampire.png");


   public RenderVampire() {
      super(new ModelVampire(), 0.5F);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return TEXTURE_URL;
   }

   protected ResourceLocation func_110832_a(EntityVillageGuard entity) {
      return TEXTURE_URL;
   }

}
