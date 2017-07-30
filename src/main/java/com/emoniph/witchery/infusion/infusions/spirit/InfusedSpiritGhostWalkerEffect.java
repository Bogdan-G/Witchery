package com.emoniph.witchery.infusion.infusions.spirit;

import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.infusion.infusions.spirit.InfusedSpiritEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class InfusedSpiritGhostWalkerEffect extends InfusedSpiritEffect {

   public InfusedSpiritGhostWalkerEffect(int id, int spirits, int spectres, int banshees, int poltergeists) {
      super(id, "ghostwalker", spirits, spectres, banshees, poltergeists);
   }

   public double getRadius() {
      return 8.0D;
   }

   public boolean doUpdateEffect(TileEntity tile, boolean triggered, ArrayList foundEntities) {
      if(triggered) {
         Iterator i$ = foundEntities.iterator();

         while(i$.hasNext()) {
            EntityLivingBase entity = (EntityLivingBase)i$.next();
            if(entity instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)entity;
               if(WorldProviderDreamWorld.getPlayerIsGhost(player)) {
                  WorldProviderDreamWorld.setPlayerSkipNextManifestationReduction(player);
               }
            }
         }
      }

      return triggered;
   }
}
