package com.emoniph.witchery.brewing;


public enum RitualStatus {

   COMPLETE("COMPLETE", 0),
   ONGOING("ONGOING", 1),
   FAILED("FAILED", 2),
   FAILED_DISTANCE("FAILED_DISTANCE", 3),
   FAILED_NO_COVEN("FAILED_NO_COVEN", 4),
   FAILED_INVALID_CIRCLES("FAILED_INVALID_CIRCLES", 5);
   // $FF: synthetic field
   private static final RitualStatus[] $VALUES = new RitualStatus[]{COMPLETE, ONGOING, FAILED, FAILED_DISTANCE, FAILED_NO_COVEN, FAILED_INVALID_CIRCLES};


   private RitualStatus(String var1, int var2) {}

}
