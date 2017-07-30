package com.emoniph.witchery.crafting;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.util.Const;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;

public class DistilleryRecipes {

   private static final DistilleryRecipes INSTANCE = new DistilleryRecipes();
   public final ArrayList recipes = new ArrayList();


   public static DistilleryRecipes instance() {
      return INSTANCE;
   }

   public DistilleryRecipes.DistilleryRecipe addRecipe(ItemStack input1, ItemStack input2, int jars, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4) {
      DistilleryRecipes.DistilleryRecipe recipe = new DistilleryRecipes.DistilleryRecipe(input1, input2, jars, output1, output2, output3, output4, (DistilleryRecipes.NamelessClass1159417362)null);
      this.recipes.add(recipe);
      return recipe;
   }

   public DistilleryRecipes.DistilleryRecipe getDistillingResult(ItemStack input1, ItemStack intput2, ItemStack jars) {
      Iterator i$ = this.recipes.iterator();

      DistilleryRecipes.DistilleryRecipe recipe;
      do {
         if(!i$.hasNext()) {
            return null;
         }

         recipe = (DistilleryRecipes.DistilleryRecipe)i$.next();
      } while(!recipe.isMatch(input1, intput2, jars));

      return recipe;
   }

   public DistilleryRecipes.DistilleryRecipe findRecipeFor(ItemStack result) {
      Iterator i$ = this.recipes.iterator();

      DistilleryRecipes.DistilleryRecipe recipe;
      do {
         if(!i$.hasNext()) {
            return null;
         }

         recipe = (DistilleryRecipes.DistilleryRecipe)i$.next();
      } while(!recipe.resultsIn(result));

      return recipe;
   }

   public DistilleryRecipes.DistilleryRecipe findRecipeUsing(ItemStack ingredient) {
      Iterator i$ = this.recipes.iterator();

      DistilleryRecipes.DistilleryRecipe recipe;
      do {
         if(!i$.hasNext()) {
            return null;
         }

         recipe = (DistilleryRecipes.DistilleryRecipe)i$.next();
      } while(!recipe.uses(ingredient));

      return recipe;
   }


   public static class DistilleryRecipe {

      public final ItemStack[] inputs;
      public final int jars;
      public final ItemStack[] outputs;


      private DistilleryRecipe(ItemStack input1, ItemStack input2, int jars, ItemStack output1, ItemStack output2, ItemStack output3, ItemStack output4) {
         this.inputs = new ItemStack[]{input1, input2};
         this.jars = jars;
         this.outputs = new ItemStack[]{output1, output2, output3, output4};
      }

      private boolean isMatch(ItemStack input1, ItemStack input2, ItemStack jars) {
         return (this.jars == 0 || jars != null && jars.stackSize >= this.jars) && (this.isMatch(input1, this.inputs[0]) && this.isMatch(input2, this.inputs[1]) || this.isMatch(input1, this.inputs[1]) && this.isMatch(input2, this.inputs[0]));
      }

      private boolean isMatch(ItemStack a, ItemStack b) {
         return a == null && b == null || a != null && b != null && a.getItem() == b.getItem() && (!a.getHasSubtypes() || a.getItemDamage() == b.getItemDamage());
      }

      public int getJars() {
         return this.jars;
      }

      public ItemStack[] getOutputs() {
         return this.outputs;
      }

      public String getDescription() {
         StringBuffer sb = new StringBuffer();
         sb.append(Witchery.resource("witchery.book.distillery.items"));
         sb.append(Const.BOOK_NEWLINE);
         sb.append(Const.BOOK_NEWLINE);
         ItemStack[] arr$ = this.inputs;
         int len$ = arr$.length;

         int i$;
         ItemStack stack;
         List list;
         PotionEffect effect;
         String s;
         for(i$ = 0; i$ < len$; ++i$) {
            stack = arr$[i$];
            if(stack != null) {
               sb.append("§8>§0 ");
               if(stack.getItem() == Item.getItemFromBlock(Blocks.red_mushroom)) {
                  sb.append(Witchery.resource("witchery.book.mushroomred"));
               } else if(stack.getItem() == Item.getItemFromBlock(Blocks.brown_mushroom)) {
                  sb.append(Witchery.resource("witchery.book.mushroombrown"));
               } else if(stack.getItem() == Items.potionitem) {
                  list = Items.potionitem.getEffects(stack);
                  if(list != null && !list.isEmpty()) {
                     effect = (PotionEffect)list.get(0);
                     s = stack.getDisplayName();
                     if(effect.getAmplifier() > 0) {
                        s = s + " " + StatCollector.translateToLocal("potion.potency." + effect.getAmplifier()).trim();
                     }

                     if(effect.getDuration() > 20) {
                        s = s + " (" + Potion.getDurationString(effect) + ")";
                     }

                     sb.append(s);
                  } else {
                     sb.append(stack.getDisplayName());
                  }
               } else {
                  sb.append(stack.getDisplayName());
               }

               sb.append(Const.BOOK_NEWLINE);
            }
         }

         sb.append(String.format("\n§8%s§0 %d\n", new Object[]{Witchery.resource("witchery.book.distillery.jars"), Integer.valueOf(this.jars)}));
         sb.append(Const.BOOK_NEWLINE);
         sb.append(Witchery.resource("witchery.book.distillery.results"));
         sb.append(Const.BOOK_NEWLINE);
         sb.append(Const.BOOK_NEWLINE);
         arr$ = this.outputs;
         len$ = arr$.length;

         for(i$ = 0; i$ < len$; ++i$) {
            stack = arr$[i$];
            if(stack != null) {
               sb.append("§8>§0 ");
               if(stack.getItem() == Item.getItemFromBlock(Blocks.red_mushroom)) {
                  sb.append(Witchery.resource("witchery.book.mushroomred"));
               } else if(stack.getItem() == Item.getItemFromBlock(Blocks.brown_mushroom)) {
                  sb.append(Witchery.resource("witchery.book.mushroombrown"));
               } else if(stack.getItem() == Items.potionitem) {
                  list = Items.potionitem.getEffects(stack);
                  if(list != null && !list.isEmpty()) {
                     effect = (PotionEffect)list.get(0);
                     s = stack.getDisplayName();
                     if(effect.getAmplifier() > 0) {
                        s = s + " " + StatCollector.translateToLocal("potion.potency." + effect.getAmplifier()).trim();
                     }

                     if(effect.getDuration() > 20) {
                        s = s + " (" + Potion.getDurationString(effect) + ")";
                     }

                     sb.append(s);
                  } else {
                     sb.append(stack.getDisplayName());
                  }
               } else {
                  sb.append(stack.getDisplayName());
               }

               sb.append(Const.BOOK_NEWLINE);
            }
         }

         return sb.toString();
      }

      public boolean resultsIn(ItemStack result) {
         ItemStack[] arr$ = this.outputs;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ItemStack stack = arr$[i$];
            if(stack != null && stack.isItemEqual(result)) {
               return true;
            }
         }

         return false;
      }

      public boolean uses(ItemStack ingredient) {
         ItemStack[] arr$ = this.inputs;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ItemStack stack = arr$[i$];
            if(stack != null && stack.isItemEqual(ingredient)) {
               return true;
            }
         }

         if(Witchery.Items.GENERIC.itemEmptyClayJar.isMatch(ingredient) && this.jars > 0) {
            return true;
         } else {
            return false;
         }
      }

      // $FF: synthetic method
      DistilleryRecipe(ItemStack x0, ItemStack x1, int x2, ItemStack x3, ItemStack x4, ItemStack x5, ItemStack x6, DistilleryRecipes.NamelessClass1159417362 x7) {
         this(x0, x1, x2, x3, x4, x5, x6);
      }
   }

   // $FF: synthetic class
   static class NamelessClass1159417362 {
   }
}
