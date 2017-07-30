package com.emoniph.witchery.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection {

   private final NavigableMap map;
   private final Random random;
   private double total;


   public RandomCollection() {
      this(new Random());
   }

   public RandomCollection(Random random) {
      this.map = new TreeMap();
      this.total = 0.0D;
      this.random = random;
   }

   public void add(double weight, Object result) {
      if(weight > 0.0D) {
         this.total += weight;
         this.map.put(Double.valueOf(this.total), result);
      }

   }

   public Object next() {
      double value = this.random.nextDouble() * this.total;
      return this.map.ceilingEntry(Double.valueOf(value)).getValue();
   }
}
