package com.emoniph.witchery.integration;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;
import com.emoniph.witchery.crafting.KettleRecipes;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class NEIKettleRecipeHandler extends TemplateRecipeHandler {

   public void loadTransferRects() {
      this.transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "witchery_brewing", new Object[0]));
   }

   public Class getGuiClass() {
      return GuiCrafting.class;
   }

   public String getRecipeName() {
      return StatCollector.translateToLocal("tile.witchery:kettle.name");
   }

   public void loadCraftingRecipes(String outputId, Object ... results) {
      if(outputId.equals("witchery_brewing") && this.getClass() == NEIKettleRecipeHandler.class) {
         Iterator i$ = KettleRecipes.instance().recipes.iterator();

         while(i$.hasNext()) {
            KettleRecipes.KettleRecipe recipe = (KettleRecipes.KettleRecipe)i$.next();
            this.arecipes.add(new NEIKettleRecipeHandler.CachedKettleRecipe(recipe.output, recipe));
         }
      } else {
         super.loadCraftingRecipes(outputId, results);
      }

   }

   public void loadCraftingRecipes(ItemStack result) {
      KettleRecipes.KettleRecipe recipe = KettleRecipes.instance().findRecipeFor(result);
      if(recipe != null) {
         this.arecipes.add(new NEIKettleRecipeHandler.CachedKettleRecipe(recipe.output, recipe));
      }

   }

   public void loadUsageRecipes(ItemStack ingredient) {}

   public String getGuiTexture() {
      return "textures/gui/container/crafting_table.png";
   }

   public void drawExtras(int recipe) {}

   public String getOverlayIdentifier() {
      return "witchery_brewing";
   }

   public class CachedKettleRecipe extends CachedRecipe {

      PositionedStack result;
      PositionedStack[] inputs = new PositionedStack[6];


      public CachedKettleRecipe(ItemStack result, KettleRecipes.KettleRecipe recipe) {
         //super(NEIKettleRecipeHandler.this);
         super();
         this.result = new PositionedStack(result, 119, 24);

         for(int i = 0; i < recipe.inputs.length; ++i) {
            if(recipe.inputs[i] != null) {
               this.inputs[i] = new PositionedStack(recipe.inputs[i], i < 3?25:43, i * 18 % 54 + 6);
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
