package com.emoniph.witchery.infusion.infusions.spirit;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.infusion.infusions.spirit.InfusedSpiritEffect;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import net.minecraft.tileentity.TileEntity;

public class InfusedSpiritScreamerEffect extends InfusedSpiritEffect {

   public InfusedSpiritScreamerEffect(int id, int spirits, int spectres, int banshees, int poltergeists) {
      super(id, "screamer", spirits, spectres, banshees, poltergeists);
   }

   public boolean doUpdateEffect(TileEntity tile, boolean triggered, ArrayList foundEntities) {
      if(triggered) {
         ParticleEffect.REDDUST.send(tile.getBlockType() != Witchery.Blocks.FETISH_WITCHS_LADDER?SoundEffect.WITCHERY_MOB_SPECTRE_SPECTRE_HIT:SoundEffect.NONE, tile.getWorldObj(), 0.5D + (double)tile.xCoord, 0.3D + (double)tile.yCoord, 0.5D + (double)tile.zCoord, 0.2D, 0.5D, 16);
      }

      return triggered;
   }

   public boolean isRedstoneSignaller() {
      return true;
   }

   public double getRadius() {
      return 16.0D;
   }
}
