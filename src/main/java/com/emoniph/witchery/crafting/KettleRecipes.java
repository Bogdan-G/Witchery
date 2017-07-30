package com.emoniph.witchery.crafting;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.familiar.Familiar;
import com.emoniph.witchery.util.Const;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class KettleRecipes {

   private static final KettleRecipes INSTANCE = new KettleRecipes();
   public final ArrayList recipes = new ArrayList();


   public static KettleRecipes instance() {
      return INSTANCE;
   }

   public KettleRecipes.KettleRecipe addRecipe(ItemStack output, int hatBonus, int familiarType, float powerRequired, int color, int dimension, boolean inBook, ItemStack ... inputs) {
      KettleRecipes.KettleRecipe recipe = new KettleRecipes.KettleRecipe(output, hatBonus, familiarType, powerRequired, color, dimension, inBook, inputs, (KettleRecipes.NamelessClass1435552689)null);
      this.recipes.add(recipe);
      return recipe;
   }

   public KettleRecipes.KettleRecipe addRecipe(ItemStack output, int hatBonus, int familiarType, float powerRequired, int color, int dimension, ItemStack ... inputs) {
      return this.addRecipe(output, hatBonus, familiarType, powerRequired, color, dimension, true, inputs);
   }

   public KettleRecipes.KettleRecipe getResult(ItemStack[] inputs, int length, boolean partial, World world) {
      Iterator i$ = this.recipes.iterator();

      KettleRecipes.KettleRecipe recipe;
      do {
         if(!i$.hasNext()) {
            return null;
         }

         recipe = (KettleRecipes.KettleRecipe)i$.next();
      } while(!recipe.isMatch(inputs, length, partial, world));

      return recipe;
   }

   public int getHatBonus(ItemStack stack) {
      Iterator i$ = this.recipes.iterator();

      KettleRecipes.KettleRecipe recipe;
      do {
         if(!i$.hasNext()) {
            return 0;
         }

         recipe = (KettleRecipes.KettleRecipe)i$.next();
      } while(!recipe.output.isItemEqual(stack));

      return recipe.hatBonus;
   }

   public boolean isBrewableBy(ItemStack stack, EntityPlayer player) {
      Iterator i$ = this.recipes.iterator();

      KettleRecipes.KettleRecipe recipe;
      do {
         if(!i$.hasNext()) {
            return false;
         }

         recipe = (KettleRecipes.KettleRecipe)i$.next();
      } while(!recipe.output.isItemEqual(stack));

      return recipe.isBrewableBy(player);
   }

   public KettleRecipes.KettleRecipe findRecipeFor(ItemStack result) {
      Iterator i$ = this.recipes.iterator();

      KettleRecipes.KettleRecipe recipe;
      do {
         if(!i$.hasNext()) {
            return null;
         }

         recipe = (KettleRecipes.KettleRecipe)i$.next();
      } while(!recipe.output.isItemEqual(result));

      return recipe;
   }


   public static class KettleRecipe {

      public final ItemStack[] inputs;
      public final ItemStack output;
      public final float power;
      final int color;
      final int hatBonus;
      final int familiarType;
      final int dimension;
      public final boolean inBook;
      private String unlocalizedName;


      private KettleRecipe(ItemStack output, int hatBonus, int familiarType, float power, int color, int dimension, boolean inBook, ItemStack ... inputs) {
         this.inputs = inputs;
         this.output = output;
         this.power = power;
         this.color = color;
         this.hatBonus = hatBonus;
         this.familiarType = familiarType;
         this.dimension = dimension;
         this.inBook = inBook;
      }

      public int getColor() {
         return this.color;
      }

      private boolean isMatch(ItemStack[] current, int currentLength, boolean partial, World world) {
         if(this.dimension != 0 && this.dimension != world.provider.dimensionId) {
            return false;
         } else if(!partial && currentLength != this.inputs.length) {
            return false;
         } else {
            ArrayList inputsToFind = new ArrayList(Arrays.asList(this.inputs));

            for(int j = 0; j < currentLength; ++j) {
               ItemStack itemstack = current[j];
               boolean foundOne = false;

               for(int i = 0; i < inputsToFind.size(); ++i) {
                  ItemStack input = (ItemStack)inputsToFind.get(i);
                  if(itemstack != null && input != null && itemstack.isItemEqual(input)) {
                     inputsToFind.remove(i);
                     foundOne = true;
                     break;
                  }
               }

               if(!foundOne) {
                  if(itemstack != null) {
                     return false;
                  }
                  break;
               }
            }

            return inputsToFind.size() == 0 || partial && inputsToFind.size() < this.inputs.length;
         }
      }

      private boolean allEmpty(ArrayList items) {
         Iterator i$ = items.iterator();

         ItemStack stack;
         do {
            if(!i$.hasNext()) {
               return true;
            }

            stack = (ItemStack)i$.next();
         } while(stack == null);

         return false;
      }

      public ItemStack getOutput(EntityPlayer player, boolean createCopy) {
         if(this.hatBonus > 0 && player != null && player.inventory != null && player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() == Witchery.Items.WITCH_HAT) {
            ItemStack stack = this.output.copy();
            stack.stackSize += this.hatBonus;
            return stack;
         } else {
            return createCopy?this.output.copy():this.output;
         }
      }

      public float getRequiredPower() {
         return this.power;
      }

      public String getDescription() {
         StringBuffer sb = new StringBuffer();
         sb.append("§n");
         sb.append(this.output.getDisplayName());
         sb.append("§r");
         sb.append(Const.BOOK_NEWLINE);
         sb.append(Const.BOOK_NEWLINE);
         if(this.unlocalizedName != null && !this.unlocalizedName.isEmpty()) {
            String arr$ = Witchery.resource(this.unlocalizedName);
            if(!arr$.isEmpty()) {
               sb.append(arr$);
               sb.append(Const.BOOK_NEWLINE);
               sb.append(Const.BOOK_NEWLINE);
            }
         }

         ItemStack[] var9 = this.inputs;
         int len$ = var9.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ItemStack stack = var9[i$];
            sb.append("§8>§0 ");
            if(stack.getItem() == Item.getItemFromBlock(Blocks.red_mushroom)) {
               sb.append(Witchery.resource("witchery.book.mushroomred"));
            } else if(stack.getItem() == Item.getItemFromBlock(Blocks.brown_mushroom)) {
               sb.append(Witchery.resource("witchery.book.mushroombrown"));
            } else if(stack.getItem() == Items.potionitem) {
               List list = Items.potionitem.getEffects(stack);
               if(list != null && !list.isEmpty()) {
                  PotionEffect effect = (PotionEffect)list.get(0);
                  String s = stack.getDisplayName();
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

         if(this.power > 0.0F) {
            sb.append(String.format("\n§8%s§0 %s\n", new Object[]{Witchery.resource("witchery.book.altarpower"), Integer.valueOf(MathHelper.floor_float(this.power))}));
         }

         return sb.toString();
      }

      public KettleRecipes.KettleRecipe setUnlocalizedName(String unlocalizedName) {
         this.unlocalizedName = unlocalizedName;
         return this;
      }

      public boolean isBrewableBy(EntityPlayer player) {
         if(this.familiarType == 0) {
            return true;
         } else if(player == null) {
            return false;
         } else {
            int familiarOfPlayer = Familiar.getActiveFamiliarType(player);
            return familiarOfPlayer == this.familiarType;
         }
      }

      // $FF: synthetic method
      KettleRecipe(ItemStack x0, int x1, int x2, float x3, int x4, int x5, boolean x6, ItemStack[] x7, KettleRecipes.NamelessClass1435552689 x8) {
         this(x0, x1, x2, x3, x4, x5, x6, x7);
      }
   }

   // $FF: synthetic class
   static class NamelessClass1435552689 {
   }
}
