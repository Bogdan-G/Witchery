package com.emoniph.witchery.util;


public class Count {

   protected int count;


   public void increment() {
      ++this.count;
   }

   public void decrement() {
      --this.count;
   }

   public int get() {
      return this.count;
   }

   public void incrementBy(int quantity) {
      this.count += quantity;
   }
}
