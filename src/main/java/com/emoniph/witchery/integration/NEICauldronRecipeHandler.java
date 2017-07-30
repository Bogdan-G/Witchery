package com.emoniph.witchery.integration;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.brewing.action.BrewActionRitualRecipe;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class NEICauldronRecipeHandler extends TemplateRecipeHandler {

   public void loadTransferRects() {
      this.transferRects.add(new RecipeTransferRect(new Rectangle(92, 31, 24, 18), "witchery_brewing_plus", new Object[0]));
   }

   public Class getGuiClass() {
      return GuiCrafting.class;
   }

   public String getRecipeName() {
      return StatCollector.translateToLocal("tile.witchery:cauldron.name");
   }

   public void loadCraftingRecipes(String outputId, Object ... results) {
      if(outputId.equals("witchery_brewing_plus") && this.getClass() == NEICauldronRecipeHandler.class) {
         Iterator i$ = WitcheryBrewRegistry.INSTANCE.getRecipes().iterator();

         while(i$.hasNext()) {
            BrewActionRitualRecipe ritual = (BrewActionRitualRecipe)i$.next();
            Iterator i$1 = ritual.getExpandedRecipes().iterator();

            while(i$1.hasNext()) {
               BrewActionRitualRecipe.Recipe recipe = (BrewActionRitualRecipe.Recipe)i$1.next();
               this.arecipes.add(new NEICauldronRecipeHandler.CachedKettleRecipe(recipe.result, recipe.ingredients));
            }
         }
      } else {
         super.loadCraftingRecipes(outputId, results);
      }

   }

   public void loadCraftingRecipes(ItemStack result) {
      Iterator i$ = WitcheryBrewRegistry.INSTANCE.getRecipes().iterator();

      while(i$.hasNext()) {
         BrewActionRitualRecipe ritual = (BrewActionRitualRecipe)i$.next();
         Iterator i$1 = ritual.getExpandedRecipes().iterator();

         while(i$1.hasNext()) {
            BrewActionRitualRecipe.Recipe recipe = (BrewActionRitualRecipe.Recipe)i$1.next();
            if(result.isItemEqual(recipe.result)) {
               this.arecipes.add(new NEICauldronRecipeHandler.CachedKettleRecipe(recipe.result, recipe.ingredients));
            }
         }
      }

   }

   public void loadUsageRecipes(ItemStack ingredient) {
      Iterator i$ = WitcheryBrewRegistry.INSTANCE.getRecipes().iterator();

      while(i$.hasNext()) {
         BrewActionRitualRecipe ritual = (BrewActionRitualRecipe)i$.next();
         Iterator i$1 = ritual.getExpandedRecipes().iterator();

         while(i$1.hasNext()) {
            BrewActionRitualRecipe.Recipe recipe = (BrewActionRitualRecipe.Recipe)i$1.next();
            ItemStack[] arr$ = recipe.ingredients;
            int len$ = arr$.length;

            for(int i$2 = 0; i$2 < len$; ++i$2) {
               ItemStack stack = arr$[i$2];
               if(stack.isItemEqual(ingredient)) {
                  this.arecipes.add(new NEICauldronRecipeHandler.CachedKettleRecipe(recipe.result, recipe.ingredients));
               }
            }
         }
      }

   }

   public String getGuiTexture() {
      return "witchery:textures/gui/witchesCauldron.png";
   }

   public void drawExtras(int recipe) {}

   public String getOverlayIdentifier() {
      return "witchery_brewing_plus";
   }

   public class CachedKettleRecipe extends CachedRecipe {

      PositionedStack result;
      PositionedStack[] inputs = new PositionedStack[6];


      public CachedKettleRecipe(ItemStack result, ItemStack[] recipe) {
         //super(NEICauldronRecipeHandler.this);
         super();
         this.result = new PositionedStack(result, 119, 31);

         for(int i = 0; i < recipe.length; ++i) {
            if(recipe[i] != null) {
               this.inputs[i] = new PositionedStack(recipe[i], i * 18 + 10, 6);
            } else {
               this.inputs[i] = null;
            }
         }

      }

      public PositionedStack getResult() {
         return this.result;
      }

      public ArrayList getIngredients() {
         ArrayList recipestacks = new ArrayList();
         recipestacks.add(this.result);
         PositionedStack[] arr$ = this.inputs;
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
