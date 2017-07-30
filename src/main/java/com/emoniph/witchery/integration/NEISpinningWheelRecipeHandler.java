package com.emoniph.witchery.integration;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;
import com.emoniph.witchery.blocks.BlockSpinningWheelGUI;
import com.emoniph.witchery.crafting.SpinningRecipes;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class NEISpinningWheelRecipeHandler extends TemplateRecipeHandler {

   public Class getGuiClass() {
      return BlockSpinningWheelGUI.class;
   }

   public String getRecipeName() {
      return StatCollector.translateToLocal("tile.witchery:spinningwheel.name");
   }

   public void loadTransferRects() {
      this.transferRects.add(new RecipeTransferRect(new Rectangle(74, 9, 24, 18), "witchery_spinning", new Object[0]));
   }

   public void loadCraftingRecipes(String outputId, Object ... results) {
      if(outputId.equals("witchery_spinning") && this.getClass() == NEISpinningWheelRecipeHandler.class) {
         Iterator i$ = SpinningRecipes.instance().recipes.iterator();

         while(i$.hasNext()) {
            SpinningRecipes.SpinningRecipe recipe = (SpinningRecipes.SpinningRecipe)i$.next();
            this.arecipes.add(new NEISpinningWheelRecipeHandler.CachedSpinningRecipe(recipe.getResult(), recipe));
         }
      } else {
         super.loadCraftingRecipes(outputId, results);
      }

   }

   public void loadCraftingRecipes(ItemStack result) {
      SpinningRecipes.SpinningRecipe recipe = SpinningRecipes.instance().findRecipeFor(result);
      if(recipe != null) {
         this.arecipes.add(new NEISpinningWheelRecipeHandler.CachedSpinningRecipe(result, recipe));
      }

   }

   public void loadUsageRecipes(ItemStack ingredient) {
      SpinningRecipes.SpinningRecipe recipe = SpinningRecipes.instance().findRecipeUsing(ingredient);
      if(recipe != null) {
         this.arecipes.add(new NEISpinningWheelRecipeHandler.CachedSpinningRecipe(ingredient, recipe));
      }

   }

   public String getGuiTexture() {
      return "witchery:textures/gui/spinningwheel.png";
   }

   public void drawExtras(int recipe) {
      this.drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
   }

   public String getOverlayIdentifier() {
      return "witchery_spinning";
   }

   public class CachedSpinningRecipe extends CachedRecipe {

      PositionedStack fibre;
      PositionedStack output;
      PositionedStack add1;
      PositionedStack add2;
      PositionedStack add3;


      public CachedSpinningRecipe(ItemStack result, SpinningRecipes.SpinningRecipe recipe) {
         //super(NEISpinningWheelRecipeHandler.this);
         super();
         this.fibre = new PositionedStack(recipe.fibre, 51, 9);
         this.output = new PositionedStack(recipe.getResult(), 113, 9);
         if(recipe.modifiers.length > 0 && recipe.modifiers[0] != null) {
            this.add1 = new PositionedStack(recipe.modifiers[0], 51, 42);
         }

         if(recipe.modifiers.length > 1 && recipe.modifiers[1] != null) {
            this.add2 = new PositionedStack(recipe.modifiers[1], 69, 42);
         }

         if(recipe.modifiers.length > 2 && recipe.modifiers[2] != null) {
            this.add3 = new PositionedStack(recipe.modifiers[2], 87, 42);
         }

      }

      public PositionedStack getResult() {
         return this.output;
      }

      public ArrayList getIngredients() {
         ArrayList recipestacks = new ArrayList();
         recipestacks.add(this.fibre);
         recipestacks.add(this.output);
         if(this.add1 != null) {
            recipestacks.add(this.add1);
         }

         if(this.add2 != null) {
            recipestacks.add(this.add2);
         }

         if(this.add3 != null) {
            recipestacks.add(this.add3);
         }

         return recipestacks;
      }
   }
}
