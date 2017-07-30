package com.emoniph.witchery.integration;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockDistilleryGUI;
import com.emoniph.witchery.crafting.DistilleryRecipes;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class NEIDistilleryRecipeHandler extends TemplateRecipeHandler {

   public Class getGuiClass() {
      return BlockDistilleryGUI.class;
   }

   public String getRecipeName() {
      return StatCollector.translateToLocal("tile.witchery:distilleryidle.name");
   }

   public void loadTransferRects() {
      this.transferRects.add(new RecipeTransferRect(new Rectangle(63, 4, 39, 35), "witchery_distilling", new Object[0]));
   }

   public void loadCraftingRecipes(String outputId, Object ... results) {
      if(outputId.equals("witchery_distilling") && this.getClass() == NEIDistilleryRecipeHandler.class) {
         Iterator i$ = DistilleryRecipes.instance().recipes.iterator();

         while(i$.hasNext()) {
            DistilleryRecipes.DistilleryRecipe recipe = (DistilleryRecipes.DistilleryRecipe)i$.next();
            this.arecipes.add(new NEIDistilleryRecipeHandler.CachedDistillingRecipe(recipe.outputs[0], recipe));
         }
      } else {
         super.loadCraftingRecipes(outputId, results);
      }

   }

   public void loadCraftingRecipes(ItemStack result) {
      DistilleryRecipes.DistilleryRecipe recipe = DistilleryRecipes.instance().findRecipeFor(result);
      if(recipe != null) {
         this.arecipes.add(new NEIDistilleryRecipeHandler.CachedDistillingRecipe(result, recipe));
      }

   }

   public void loadUsageRecipes(ItemStack ingredient) {
      DistilleryRecipes.DistilleryRecipe recipe = DistilleryRecipes.instance().findRecipeUsing(ingredient);
      if(recipe != null) {
         this.arecipes.add(new NEIDistilleryRecipeHandler.CachedDistillingRecipe(ingredient, recipe));
      }

   }

   public String getGuiTexture() {
      return "witchery:textures/gui/distiller.png";
   }

   public void drawExtras(int recipe) {
      this.drawProgressBar(63, 3, 176, 29, 39, 35, 200, 0);
      this.drawProgressBar(28, 8, 185, -2, 12, 30, 35, 3);
   }

   public String getOverlayIdentifier() {
      return "witchery_distilling";
   }

   public class CachedDistillingRecipe extends CachedRecipe {

      PositionedStack ingred1;
      PositionedStack ingred2;
      PositionedStack jars;
      PositionedStack[] outputs = new PositionedStack[6];


      public CachedDistillingRecipe(ItemStack result, DistilleryRecipes.DistilleryRecipe recipe) {
         //super(NEIDistilleryRecipeHandler.this);
         super();
         this.ingred1 = new PositionedStack(recipe.inputs[0], 43, 5);
         if(recipe.inputs[1] != null) {
            this.ingred2 = new PositionedStack(recipe.inputs[1], 43, 23);
         }

         if(recipe.jars > 0) {
            this.jars = new PositionedStack(Witchery.Items.GENERIC.itemEmptyClayJar.createStack(recipe.jars), 43, 43);
         }

         if(recipe.outputs[0] != null) {
            this.outputs[0] = new PositionedStack(recipe.outputs[0], 105, 5);
         }

         if(recipe.outputs[1] != null) {
            this.outputs[1] = new PositionedStack(recipe.outputs[1], 123, 5);
         }

         if(recipe.outputs[2] != null) {
            this.outputs[2] = new PositionedStack(recipe.outputs[2], 105, 23);
         }

         if(recipe.outputs[3] != null) {
            this.outputs[3] = new PositionedStack(recipe.outputs[3], 123, 23);
         }

      }

      public PositionedStack getResult() {
         return this.outputs[0];
      }

      public ArrayList getIngredients() {
         ArrayList recipestacks = new ArrayList();
         recipestacks.add(this.ingred1);
         if(this.ingred2 != null) {
            recipestacks.add(this.ingred2);
         }

         if(this.jars != null) {
            recipestacks.add(this.jars);
         }

         PositionedStack[] arr$ = this.outputs;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            PositionedStack posStack = arr$[i$];
            if(posStack != null) {
               recipestacks.add(posStack);
            }
         }

         return recipestacks;
      }
   }
}
