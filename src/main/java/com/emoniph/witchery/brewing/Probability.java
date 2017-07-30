package com.emoniph.witchery.brewing;


public class Probability {

   public static final Probability CERTAIN = new Probability(1.0D);
   private final double probability;


   public Probability(double propbability) {
      this.probability = propbability;
   }

}
