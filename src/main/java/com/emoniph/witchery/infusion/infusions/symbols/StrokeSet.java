package com.emoniph.witchery.infusion.infusions.symbols;

import com.emoniph.witchery.infusion.infusions.symbols.SymbolEffect;
import java.nio.ByteBuffer;
import java.util.Hashtable;

public class StrokeSet {

   private final byte[] strokes;
   private final int level;


   public StrokeSet(int level, byte ... strokes) {
      this.strokes = strokes;
      this.level = level;
   }

   public StrokeSet(byte ... strokes) {
      this(1, strokes);
   }

   public void addTo(Hashtable table, Hashtable enhanced, SymbolEffect effect) {
      ByteBuffer bb = ByteBuffer.wrap(this.strokes);
      table.put(bb, effect);
      enhanced.put(bb, Integer.valueOf(this.level));
   }

   public void setDefaultFor(SymbolEffect effect) {
      effect.setDefaultStrokes(this.strokes);
   }

   public static class Stroke {

      public static final byte UP = 0;
      public static final byte DOWN = 1;
      public static final byte LEFT = 3;
      public static final byte RIGHT = 2;
      public static final byte UP_LEFT = 6;
      public static final byte UP_RIGHT = 4;
      public static final byte DOWN_LEFT = 5;
      public static final byte DOWN_RIGHT = 7;
      public static final int[] STROKE_TO_INDEX = new int[]{0, 4, 2, 6, 1, 5, 7, 3};
      public static final int[] INDEX_TO_STROKE = new int[]{0, 4, 2, 7, 1, 5, 3, 6};


   }
}
