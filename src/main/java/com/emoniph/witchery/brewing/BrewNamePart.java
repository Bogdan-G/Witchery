package com.emoniph.witchery.brewing;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.BrewNameBuilder;

public class BrewNamePart {

   protected final String text;
   protected final String invertedText;
   protected final BrewNamePart.Position position;
   protected long baseDuration;
   protected long invertedDuration;


   public BrewNamePart(String resourceId) {
      this(resourceId, resourceId, BrewNamePart.Position.NONE);
   }

   public BrewNamePart(String resourceId, BrewNamePart.Position position) {
      this(resourceId, resourceId, position);
   }

   public BrewNamePart(String resourceId, String invertedResourceId) {
      this(resourceId, invertedResourceId, BrewNamePart.Position.NONE);
   }

   public BrewNamePart(String resourceId, String invertedResourceId, BrewNamePart.Position position) {
      this.text = Witchery.resource(resourceId);
      this.invertedText = Witchery.resource(invertedResourceId);
      this.position = position;
   }

   public void applyTo(BrewNameBuilder nameBuilder) {
      switch(BrewNamePart.NamelessClass2057284539.$SwitchMap$com$emoniph$witchery$brewing$BrewNamePart$Position[this.position.ordinal()]) {
      case 1:
         nameBuilder.append(this.text, this.invertedText, this.baseDuration, this.invertedDuration);
         break;
      case 2:
         nameBuilder.appendPrefix(this.text);
         break;
      case 3:
         nameBuilder.appendPostfix(this.text);
      }

   }

   public BrewNamePart setBaseDuration(long baseDuration, long invertedDuration) {
      this.baseDuration = baseDuration;
      this.invertedDuration = invertedDuration;
      return this;
   }

   public BrewNamePart setBaseDuration(int baseDuration) {
      return this.setBaseDuration((long)baseDuration, (long)baseDuration);
   }

   // $FF: synthetic class
   static class NamelessClass2057284539 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$brewing$BrewNamePart$Position = new int[BrewNamePart.Position.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$brewing$BrewNamePart$Position[BrewNamePart.Position.NONE.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$brewing$BrewNamePart$Position[BrewNamePart.Position.PREFIX.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$brewing$BrewNamePart$Position[BrewNamePart.Position.POSTFIX.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum Position {

      NONE("NONE", 0),
      PREFIX("PREFIX", 1),
      POSTFIX("POSTFIX", 2);
      // $FF: synthetic field
      private static final BrewNamePart.Position[] $VALUES = new BrewNamePart.Position[]{NONE, PREFIX, POSTFIX};


      private Position(String var1, int var2) {}

   }
}
