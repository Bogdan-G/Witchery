package com.emoniph.witchery.util;


public enum BlockSide {

   NONE("NONE", 0, -2),
   RAYTRACE_FULL_LENGTH("RAYTRACE_FULL_LENGTH", 1, -1),
   BOTTOM("BOTTOM", 2, 0),
   TOP("TOP", 3, 1),
   EAST("EAST", 4, 2),
   WEST("WEST", 5, 3),
   NORTH("NORTH", 6, 4),
   SOUTH("SOUTH", 7, 5);
   final int sideID;
   // $FF: synthetic field
   private static final BlockSide[] $VALUES = new BlockSide[]{NONE, RAYTRACE_FULL_LENGTH, BOTTOM, TOP, EAST, WEST, NORTH, SOUTH};


   private BlockSide(String var1, int var2, int sideID) {
      this.sideID = sideID;
   }

   public boolean isEqual(int side) {
      return this.sideID == side;
   }

   public int getSideID() {
      return this.sideID;
   }

   public static BlockSide fromInteger(int integer) {
      switch(integer) {
      case -2:
      default:
         return NONE;
      case -1:
         return RAYTRACE_FULL_LENGTH;
      case 0:
         return BOTTOM;
      case 1:
         return TOP;
      case 2:
         return EAST;
      case 3:
         return WEST;
      case 4:
         return NORTH;
      case 5:
         return SOUTH;
      }
   }

}
