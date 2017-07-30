package com.emoniph.witchery.brewing.action;

import com.emoniph.witchery.brewing.AltarPower;
import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.BrewNamePart;
import com.emoniph.witchery.brewing.EffectLevelCounter;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersImpact;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.Probability;
import com.emoniph.witchery.brewing.RitualStatus;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.brewing.action.BrewActionList;
import com.emoniph.witchery.util.EntityUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionRitualRecipe extends BrewAction {

   private final BrewActionRitualRecipe.Recipe[] recipes;
   private final List expandedRecipes = new ArrayList();


   public BrewActionRitualRecipe(BrewItemKey itemKey, AltarPower powerCost, BrewActionRitualRecipe.Recipe ... recipes) {
      super(itemKey, (BrewNamePart)null, powerCost, new Probability(1.0D), false);
      this.recipes = recipes;
      BrewActionRitualRecipe.Recipe[] arr$ = recipes;
      int len$ = recipes.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         BrewActionRitualRecipe.Recipe recipe = arr$[i$];
         this.expandedRecipes.add(recipe.getExpandedRecipe(itemKey));
      }

   }

   public List getExpandedRecipes() {
      return this.expandedRecipes;
   }

   public final void applyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers, ItemStack stack) {}

   public final void applyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect effectModifiers, ItemStack stack) {}

   public final void prepareSplashPotion(World world, BrewActionList actionList, ModifiersImpact modifiers) {}

   public final boolean triggersRitual() {
      return true;
   }

   public final boolean canAdd(BrewActionList actionList, boolean isCauldronFull, boolean hasEffects) {
      return isCauldronFull && this.getRecipeResult(actionList) != null;
   }

   public final RitualStatus updateRitual(MinecraftServer server, BrewActionList actionList, World world, int x, int y, int z, ModifiersRitual modifiers, ModifiersImpact impactModifiers) {
      ItemStack result = this.getRecipeResult(actionList);
      if(result != null) {
         EntityItem item = new EntityItem(world, (double)x + 0.5D, (double)y + 1.5D, (double)z + 0.5D, result.copy());
         item.motionX = 0.0D;
         item.motionY = 0.2D;
         item.motionZ = 0.0D;
         EntityUtil.spawnEntityInWorld(world, item);
         return RitualStatus.COMPLETE;
      } else {
         return RitualStatus.FAILED;
      }
   }

   private ItemStack getRecipeResult(BrewActionList actionList) {
      BrewActionRitualRecipe.Recipe[] arr$ = this.recipes;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         BrewActionRitualRecipe.Recipe recipe = arr$[i$];
         if(recipe.ingredients.length <= 0) {
            return recipe.result;
         }

         ArrayList neededItems = new ArrayList();
         neededItems.addAll(Arrays.asList(recipe.ingredients));
         Iterator i$1 = actionList.actions.iterator();

         while(i$1.hasNext()) {
            BrewAction action = (BrewAction)i$1.next();
            this.removeFromNeededItems(neededItems, action.ITEM_KEY);
         }

         if(neededItems.size() == 0) {
            return recipe.result;
         }
      }

      return null;
   }

   private void removeFromNeededItems(ArrayList neededItems, BrewItemKey item) {
      Iterator iterator = neededItems.iterator();

      ItemStack stack;
      do {
         if(!iterator.hasNext()) {
            return;
         }

         stack = (ItemStack)iterator.next();
      } while(stack.getItem() != item.ITEM || stack.getItemDamage() != item.DAMAGE);

      iterator.remove();
   }

   public final boolean augmentEffectLevels(EffectLevelCounter totalEffects) {
      return true;
   }

   public final void augmentEffectModifiers(ModifiersEffect modifiers) {}

   public final void prepareRitual(World world, int x, int y, int z, ModifiersRitual modifiers, ItemStack stack) {}

   public static class Recipe {

      public final ItemStack result;
      public final ItemStack[] ingredients;


      public Recipe(ItemStack result, ItemStack ... ingredients) {
         this.result = result;
         this.ingredients = ingredients;
      }

      public Recipe(ItemStack result, ItemStack[] ingredients, ItemStack finalIngredient) {
         this.result = result;
         this.ingredients = (ItemStack[])((ItemStack[])Arrays.copyOf(ingredients, ingredients.length + 1));
         this.ingredients[this.ingredients.length - 1] = finalIngredient;
      }

      private BrewActionRitualRecipe.Recipe getExpandedRecipe(BrewItemKey itemKey) {
         return new BrewActionRitualRecipe.Recipe(this.result, this.ingredients, itemKey.toStack());
      }
   }
}
