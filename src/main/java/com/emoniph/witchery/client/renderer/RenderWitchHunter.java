package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.client.model.ModelWitchHunter;
import com.emoniph.witchery.entity.EntityWitchHunter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderWitchHunter extends RenderBiped {

   private static final ResourceLocation TEXTURE1_URL = new ResourceLocation("witchery", "textures/entities/witchhunter1.png");
   private static final ResourceLocation TEXTURE2_URL = new ResourceLocation("witchery", "textures/entities/witchhunter2.png");
   private static final ResourceLocation TEXTURE3_URL = new ResourceLocation("witchery", "textures/entities/witchhunter3.png");


   public RenderWitchHunter() {
      super(new ModelWitchHunter(), 0.5F);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return this.func_110832_a((EntityWitchHunter)entity);
   }

   protected ResourceLocation func_110832_a(EntityWitchHunter entity) {
      switch(entity.getHunterType()) {
      case 1:
         return TEXTURE2_URL;
      case 2:
         return TEXTURE3_URL;
      default:
         return TEXTURE1_URL;
      }
   }

}
