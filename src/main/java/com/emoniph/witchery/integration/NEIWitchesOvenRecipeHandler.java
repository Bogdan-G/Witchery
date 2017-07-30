package com.emoniph.witchery.integration;

import codechicken.nei.ItemList;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;
import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockWitchesOvenGUI;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.StatCollector;

public class NEIWitchesOvenRecipeHandler extends TemplateRecipeHandler {

   public static ArrayList afuels;
   public static TreeSet efuels;


   public Class getGuiClass() {
      return BlockWitchesOvenGUI.class;
   }

   public String getRecipeName() {
      return StatCollector.translateToLocal("tile.witchery:witchesovenidle.name");
   }

   public void loadCraftingRecipes(String outputId, Object ... results) {
      if(outputId.equals("witchery_cooking") && this.getClass() == NEIWitchesOvenRecipeHandler.class) {
         Map recipes = FurnaceRecipes.smelting().getSmeltingList();
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 0), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 1), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemHintOfRebirth.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 2), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 3), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemFoulFume.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Witchery.Blocks.SAPLING, 1, 0), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Witchery.Blocks.SAPLING, 1, 1), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Witchery.Blocks.SAPLING, 1, 2), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack()));
         Iterator i$ = recipes.entrySet().iterator();

         while(i$.hasNext()) {
            Entry recipe = (Entry)i$.next();
            ItemStack result = (ItemStack)recipe.getValue();
            ItemStack ingred = new ItemStack(((ItemStack)recipe.getKey()).getItem(), 1, -1);
            ItemStack byproduct = Witchery.Items.GENERIC.itemFoulFume.createStack();
            if(ingred.getItem() == Item.getItemFromBlock(Blocks.sapling)) {
               byproduct = ingred.getItemDamage() == 0?Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack():(ingred.getItemDamage() == 2?Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack():(ingred.getItemDamage() == 1?Witchery.Items.GENERIC.itemHintOfRebirth.createStack():Witchery.Items.GENERIC.itemFoulFume.createStack()));
               this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(ingred, result, byproduct));
            } else if(ingred.getItem() == Item.getItemFromBlock(Witchery.Blocks.SAPLING)) {
               byproduct = ingred.getItemDamage() == 0?Witchery.Items.GENERIC.itemWhiffOfMagic.createStack():(ingred.getItemDamage() == 2?Witchery.Items.GENERIC.itemOdourOfPurity.createStack():(ingred.getItemDamage() == 1?Witchery.Items.GENERIC.itemReekOfMisfortune.createStack():Witchery.Items.GENERIC.itemFoulFume.createStack()));
               this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(ingred, result, byproduct));
            } else if(Witchery.Items.GENERIC.itemAshWood.isMatch(result) || result.getItem() == Items.coal && result.getItemDamage() == 1 || ingred.getItem() instanceof ItemFood) {
               this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(ingred, result, byproduct));
            }
         }
      } else {
         super.loadCraftingRecipes(outputId, results);
      }

   }

   public void loadCraftingRecipes(ItemStack result2) {
      Map recipes = FurnaceRecipes.smelting().getSmeltingList();
      Iterator i$ = recipes.entrySet().iterator();

      while(i$.hasNext()) {
         Entry recipe = (Entry)i$.next();
         ItemStack result = (ItemStack)recipe.getKey();
         if(NEIServerUtils.areStacksSameType(result, result2)) {
            ItemStack ingred = new ItemStack(((ItemStack)recipe.getValue()).getItem(), 1, -1);
            ItemStack byproduct = Witchery.Items.GENERIC.itemFoulFume.createStack();
            if(ingred.getItem() == Item.getItemFromBlock(Blocks.sapling)) {
               byproduct = ingred.getItemDamage() == 0?Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack():(ingred.getItemDamage() == 2?Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack():(ingred.getItemDamage() == 1?Witchery.Items.GENERIC.itemHintOfRebirth.createStack():Witchery.Items.GENERIC.itemFoulFume.createStack()));
               this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(ingred, result, byproduct));
            } else if(ingred.getItem() == Item.getItemFromBlock(Witchery.Blocks.SAPLING)) {
               byproduct = ingred.getItemDamage() == 0?Witchery.Items.GENERIC.itemWhiffOfMagic.createStack():(ingred.getItemDamage() == 2?Witchery.Items.GENERIC.itemOdourOfPurity.createStack():(ingred.getItemDamage() == 1?Witchery.Items.GENERIC.itemReekOfMisfortune.createStack():Witchery.Items.GENERIC.itemFoulFume.createStack()));
               this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(ingred, result, byproduct));
            } else if(Witchery.Items.GENERIC.itemAshWood.isMatch(result) || ingred.getItem() == Items.coal || ingred.getItem() instanceof ItemFood) {
               this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(ingred, result, byproduct));
            }
         }
      }

      if(Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.isMatch(result2)) {
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 0), Witchery.Items.GENERIC.itemAshWood.createStack(), result2));
      } else if(Witchery.Items.GENERIC.itemBreathOfTheGoddess.isMatch(result2)) {
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 2), Witchery.Items.GENERIC.itemAshWood.createStack(), result2));
      } else if(Witchery.Items.GENERIC.itemHintOfRebirth.isMatch(result2)) {
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 1), Witchery.Items.GENERIC.itemAshWood.createStack(), result2));
      } else if(Witchery.Items.GENERIC.itemWhiffOfMagic.isMatch(result2)) {
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Witchery.Blocks.SAPLING, 1, 0), Witchery.Items.GENERIC.itemAshWood.createStack(), result2));
      } else if(Witchery.Items.GENERIC.itemOdourOfPurity.isMatch(result2)) {
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Witchery.Blocks.SAPLING, 1, 2), Witchery.Items.GENERIC.itemAshWood.createStack(), result2));
      } else if(Witchery.Items.GENERIC.itemReekOfMisfortune.isMatch(result2)) {
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Witchery.Blocks.SAPLING, 1, 1), Witchery.Items.GENERIC.itemAshWood.createStack(), result2));
      } else if(Witchery.Items.GENERIC.itemFoulFume.isMatch(result2)) {
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.log), new ItemStack(Items.coal, 1), result2));
      }

   }

   public void loadUsageRecipes(String inputId, Object ... ingredients) {
      if(inputId.equals("fuel") && this.getClass() == NEIWitchesOvenRecipeHandler.class) {
         this.loadCraftingRecipes("witchery_cooking", new Object[0]);
      } else {
         super.loadUsageRecipes(inputId, ingredients);
      }

   }

   public void loadUsageRecipes(ItemStack ingred) {
      Map recipes = FurnaceRecipes.smelting().getSmeltingList();
      Iterator i$ = recipes.entrySet().iterator();

      while(i$.hasNext()) {
         Entry recipe = (Entry)i$.next();
         ItemStack result = (ItemStack)recipe.getValue();
         if(ingred.getItem() == ((ItemStack)recipe.getKey()).getItem()) {
            ItemStack byproduct = Witchery.Items.GENERIC.itemFoulFume.createStack();
            if(ingred.getItem() == Item.getItemFromBlock(Blocks.sapling)) {
               byproduct = ingred.getItemDamage() == 0?Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack():(ingred.getItemDamage() == 1?Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack():(ingred.getItemDamage() == 2?Witchery.Items.GENERIC.itemHintOfRebirth.createStack():Witchery.Items.GENERIC.itemFoulFume.createStack()));
               this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(ingred, result, byproduct));
            } else if(ingred.getItem() == Item.getItemFromBlock(Witchery.Blocks.SAPLING)) {
               byproduct = ingred.getItemDamage() == 0?Witchery.Items.GENERIC.itemWhiffOfMagic.createStack():(ingred.getItemDamage() == 1?Witchery.Items.GENERIC.itemOdourOfPurity.createStack():(ingred.getItemDamage() == 2?Witchery.Items.GENERIC.itemReekOfMisfortune.createStack():Witchery.Items.GENERIC.itemFoulFume.createStack()));
               this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(ingred, result, byproduct));
            } else if(Witchery.Items.GENERIC.itemAshWood.isMatch(result) || ingred.getItem() == Items.coal || ingred.getItem() instanceof ItemFood) {
               this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(ingred, result, byproduct));
            }
         }
      }

      if(Witchery.Items.GENERIC.itemEmptyClayJar.isMatch(ingred)) {
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 0), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemExhaleOfTheHornedOne.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 1), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemHintOfRebirth.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 2), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemBreathOfTheGoddess.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.sapling, 1, 3), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemFoulFume.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Witchery.Blocks.SAPLING, 1, 0), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemWhiffOfMagic.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Witchery.Blocks.SAPLING, 1, 1), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemReekOfMisfortune.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Witchery.Blocks.SAPLING, 1, 2), Witchery.Items.GENERIC.itemAshWood.createStack(), Witchery.Items.GENERIC.itemOdourOfPurity.createStack()));
         this.arecipes.add(new NEIWitchesOvenRecipeHandler.SmeltingPair(new ItemStack(Blocks.log), new ItemStack(Items.coal, 1, 1), Witchery.Items.GENERIC.itemFoulFume.createStack()));
      }

   }

   public String getGuiTexture() {
      return "witchery:textures/gui/witchesOven.png";
   }

   public void loadTransferRects() {
      this.transferRects.add(new RecipeTransferRect(new Rectangle(50, 23, 18, 18), "fuel", new Object[0]));
      this.transferRects.add(new RecipeTransferRect(new Rectangle(74, 9, 24, 18), "witchery_cooking", new Object[0]));
   }

   public void drawExtras(int recipe) {
      this.drawProgressBar(51, 25, 176, 0, 14, 14, 48, 7);
      this.drawProgressBar(74, 9, 176, 14, 24, 16, 48, 0);
   }

   private static Set excludedFuels() {
      HashSet efuels = new HashSet();
      efuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
      efuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
      efuels.add(Item.getItemFromBlock(Blocks.standing_sign));
      efuels.add(Item.getItemFromBlock(Blocks.wall_sign));
      efuels.add(Item.getItemFromBlock(Blocks.wooden_door));
      efuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
      return efuels;
   }

   private static void findFuels() {
      afuels = new ArrayList();
      Set efuels = excludedFuels();
      Iterator i$ = ItemList.items.iterator();

      while(i$.hasNext()) {
         ItemStack item = (ItemStack)i$.next();
         if(item != null && !efuels.contains(item.getItem())) {
            int burnTime = TileEntityFurnace.getItemBurnTime(item);
            if(burnTime > 0) {
               afuels.add(new NEIWitchesOvenRecipeHandler.FuelPair(item.copy(), burnTime));
            }
         }
      }

   }

   private static void findFuelsOnce() {
      if(afuels == null) {
         findFuels();
      }

   }

   public String getOverlayIdentifier() {
      return "witchery_cooking";
   }

   public TemplateRecipeHandler newInstance() {
      findFuelsOnce();
      return super.newInstance();
   }

   public static class FuelPair {

      public PositionedStack stack;
      public int burnTime;


      public FuelPair(ItemStack ingred, int burnTime) {
         this.stack = new PositionedStack(ingred, 51, 42);
         this.burnTime = burnTime;
      }
   }

   public class SmeltingPair extends CachedRecipe {

      PositionedStack ingred;
      PositionedStack result;
      PositionedStack byproduct;
      PositionedStack jar;


      public SmeltingPair(ItemStack ingred, ItemStack result, ItemStack byproduct) {
         //super(NEIWitchesOvenRecipeHandler.this);
         super();
         ingred.stackSize = 1;
         this.ingred = new PositionedStack(ingred, 51, 6);
         this.result = new PositionedStack(result, 113, 10);
         this.byproduct = new PositionedStack(byproduct, 113, 42);
         this.jar = new PositionedStack(Witchery.Items.GENERIC.itemEmptyClayJar.createStack(), 78, 42);
      }

      public List getIngredients() {
         return this.getCycledIngredients(NEIWitchesOvenRecipeHandler.this.cycleticks / 48, Arrays.asList(new PositionedStack[]{this.ingred}));
      }

      public PositionedStack getResult() {
         return this.result;
      }

      public PositionedStack getOtherStack() {
         NEIWitchesOvenRecipeHandler.findFuelsOnce();
         return NEIWitchesOvenRecipeHandler.afuels != null && NEIWitchesOvenRecipeHandler.afuels.size() > 0?((NEIWitchesOvenRecipeHandler.FuelPair)NEIWitchesOvenRecipeHandler.afuels.get(NEIWitchesOvenRecipeHandler.this.cycleticks / 48 % NEIWitchesOvenRecipeHandler.afuels.size())).stack:null;
      }

      public List getOtherStacks() {
         ArrayList stacks = new ArrayList();
         PositionedStack stack = this.getOtherStack();
         if(stack != null) {
            stacks.add(stack);
         }

         stacks.add(this.byproduct);
         stacks.add(this.jar);
         return stacks;
      }
   }
}
