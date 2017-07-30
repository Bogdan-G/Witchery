package com.emoniph.witchery.client.renderer;

import com.emoniph.witchery.common.ExtendedPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

public class RenderOtherPlayer extends RenderPlayer {

   protected ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
      ExtendedPlayer playerEx = ExtendedPlayer.get(entity);
      return playerEx.getOtherPlayerSkinLocation();
   }
}
