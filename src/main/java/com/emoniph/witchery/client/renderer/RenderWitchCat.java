package com.emoniph.witchery.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderWitchCat extends RenderOcelot {

   private static final ResourceLocation blackTextures = new ResourceLocation("witchery", "textures/entities/blackcat.png");


   public RenderWitchCat(ModelBase par1ModelBase, float par2) {
      super(par1ModelBase, par2);
   }

   protected ResourceLocation getEntityTexture(EntityOcelot par1EntityOcelot) {
      return blackTextures;
   }

}
