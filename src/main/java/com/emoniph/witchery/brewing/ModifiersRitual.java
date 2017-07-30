package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.RitualStatus;
import com.emoniph.witchery.util.BlockPosition;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;

public class ModifiersRitual {

   public int permenance;
   public RitualStatus status;
   public final ArrayList targetLocations;
   public final int covenSize;
   public final int pulses;
   public final BlockPosition cauldronLocation;
   public final boolean leonard;
   public boolean taglockUsed;


   public ModifiersRitual(BlockPosition cauldronLocation, int covenSize, int ritualPulses, boolean lennyPresent) {
      this.status = RitualStatus.COMPLETE;
      this.targetLocations = new ArrayList();
      this.covenSize = covenSize;
      this.pulses = ritualPulses;
      this.cauldronLocation = cauldronLocation;
      this.leonard = lennyPresent;
   }

   public void setTarget(ItemStack stack) {
      this.setTarget(BlockPosition.from(stack));
   }

   public BlockPosition getTarget() {
      return this.getTarget(0);
   }

   public BlockPosition getTarget(int index) {
      return !this.targetLocations.isEmpty() && index < this.targetLocations.size()?(BlockPosition)this.targetLocations.get(index):this.cauldronLocation;
   }

   public void setTarget(BlockPosition target) {
      this.targetLocations.add(target);
   }

   public ModifiersRitual setRitualStatus(RitualStatus status) {
      this.status = status;
      return this;
   }

   public RitualStatus getRitualStatus() {
      return this.status;
   }
}
