package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.Dispersal;
import com.emoniph.witchery.brewing.DispersalInstant;
import com.emoniph.witchery.util.EntityPosition;
import net.minecraft.entity.player.EntityPlayer;

public class ModifiersImpact {

   public int extent;
   public int lifetime;
   private Dispersal dispersal;
   private boolean onlyInstant;
   public final EntityPlayer thrower;
   public final EntityPosition impactPosition;
   public final boolean ritualised;
   public final int covenSize;


   public ModifiersImpact(EntityPosition impactPosition, boolean ritualised, int covenSize, EntityPlayer thrower) {
      this.thrower = thrower;
      this.impactPosition = impactPosition;
      this.ritualised = ritualised;
      this.covenSize = covenSize;
   }

   public void setGeneralDispersal(Dispersal dispersal) {
      this.dispersal = dispersal;
   }

   public Dispersal getDispersal() {
      return (Dispersal)(this.onlyInstant?new DispersalInstant():this.dispersal);
   }

   public ModifiersImpact setOnlyInstant() {
      this.onlyInstant = true;
      return this;
   }
}
