package com.emoniph.witchery.crafting;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockBrazier;
import com.emoniph.witchery.blocks.BlockWitchCrop;
import com.emoniph.witchery.entity.EntityBanshee;
import com.emoniph.witchery.entity.EntityPoltergeist;
import com.emoniph.witchery.entity.EntitySpectre;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Const;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BrazierRecipes {

   private static final double POLTERGEIST_CHANCE = 0.05D;
   private static final BrazierRecipes INSTANCE = new BrazierRecipes();
   public final ArrayList recipes = new ArrayList();
   public static final BrazierRecipes.BrazierRecipe SPECTRES = new BrazierRecipes.BrazierRecipe("witchery.brazier.spectre", true, TimeUtil.secsToTicks(30), new ItemStack[]{Witchery.Items.GENERIC.itemWormwood.createStack(), Witchery.Items.GENERIC.itemBatWool.createStack(), Witchery.Items.GENERIC.itemGraveyardDust.createStack()}, null) {
      public int onBurning(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         if(TimeUtil.secondsElapsed(5, ticks)) {
            ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, world, 0.5D + (double)x, 1.0D + (double)y, 0.5D + (double)z, 0.5D, 1.0D, 16);
         }

         return 0;
      }
      public void onBurnt(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         EntityCreature creature = Infusion.spawnCreature(world, EntitySpectre.class, x, y, z, (EntityLivingBase)null, 1, 2, ParticleEffect.INSTANT_SPELL, SoundEffect.NOTE_HARP);
         CreatureUtil.spawnWithEgg(creature, true);
         if(world.rand.nextDouble() < 0.05D) {
            EntityCreature poltergeist = Infusion.spawnCreature(world, EntityPoltergeist.class, x, y, z, (EntityLivingBase)null, 6, 10, ParticleEffect.MOB_SPELL, (SoundEffect)null);
            CreatureUtil.spawnWithEgg(poltergeist, true);
         }

      }
   };
   public static final BrazierRecipes.BrazierRecipe BANSHEES = new BrazierRecipes.BrazierRecipe("witchery.brazier.banshee", true, TimeUtil.secsToTicks(30), new ItemStack[]{Witchery.Items.GENERIC.itemWormwood.createStack(), Witchery.Items.GENERIC.itemCondensedFear.createStack(), Witchery.Items.GENERIC.itemGraveyardDust.createStack()}, null) {
      public int onBurning(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         if(TimeUtil.secondsElapsed(5, ticks)) {
            ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, world, 0.5D + (double)x, 1.0D + (double)y, 0.5D + (double)z, 0.5D, 1.0D, 16);
         }

         return 0;
      }
      public void onBurnt(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         EntityCreature creature = Infusion.spawnCreature(world, EntityBanshee.class, x, y, z, (EntityLivingBase)null, 1, 2, ParticleEffect.INSTANT_SPELL, SoundEffect.NOTE_HARP);
         CreatureUtil.spawnWithEgg(creature, true);
         if(world.rand.nextDouble() < 0.05D) {
            EntityCreature poltergeist = Infusion.spawnCreature(world, EntityPoltergeist.class, x, y, z, (EntityLivingBase)null, 6, 10, ParticleEffect.MOB_SPELL, (SoundEffect)null);
            CreatureUtil.spawnWithEgg(poltergeist, true);
         }

      }
   };
   public static final BrazierRecipes.BrazierRecipe POLTERGEIST = new BrazierRecipes.BrazierRecipe("witchery.brazier.poltergeist", false, TimeUtil.secsToTicks(45), new ItemStack[]{Witchery.Items.GENERIC.itemWormwood.createStack(), Witchery.Items.GENERIC.itemRefinedEvil.createStack(), Witchery.Items.GENERIC.itemFocusedWill.createStack()}, null) {
      public int onBurning(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         if(TimeUtil.secondsElapsed(5, ticks)) {
            ParticleEffect.INSTANT_SPELL.send(SoundEffect.NONE, world, 0.5D + (double)x, 1.0D + (double)y, 0.5D + (double)z, 0.5D, 1.0D, 16);
         }

         return 0;
      }
      public void onBurnt(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         EntityCreature creature = Infusion.spawnCreature(world, EntityPoltergeist.class, x, y, z, (EntityLivingBase)null, 1, 2, ParticleEffect.INSTANT_SPELL, SoundEffect.NOTE_HARP);
         CreatureUtil.spawnWithEgg(creature, true);
         if(world.rand.nextDouble() < 0.05D) {
            EntityCreature poltergeist = Infusion.spawnCreature(world, EntityPoltergeist.class, x, y, z, (EntityLivingBase)null, 6, 10, ParticleEffect.MOB_SPELL, (SoundEffect)null);
            CreatureUtil.spawnWithEgg(poltergeist, true);
         }

      }
   };
   public static final BrazierRecipes.BrazierRecipe SMOKE = new BrazierRecipes.BrazierRecipe("witchery.brazier.smoke", true, TimeUtil.minsToTicks(5), new ItemStack[]{new ItemStack(Items.gunpowder), Witchery.Items.GENERIC.itemQuicklime.createStack(), new ItemStack(Items.glowstone_dust)}, null) {
      public int onBurning(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         ParticleEffect.EXPLODE.send(SoundEffect.NONE, world, 0.5D + (double)x, (double)y, 0.5D + (double)z, 16.0D, 4.0D, 64);
         return 0;
      }
   };
   public static final BrazierRecipes.BrazierRecipe STRONG = new BrazierRecipes.BrazierRecipe("witchery.brazier.strong", true, TimeUtil.minsToTicks(5), new ItemStack[]{Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), new ItemStack(Items.bone), new ItemStack(Items.blaze_powder)}, null) {
      public int onBurning(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         if(TimeUtil.secondsElapsed(3, ticks)) {
            boolean radius = true;
            boolean radiusSq = true;
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(0.5D + (double)x - 4.0D, (double)(y - 4), 0.5D + (double)z - 4.0D, 0.5D + (double)x + 4.0D, (double)(y + 4), 0.5D + (double)z + 4.0D);
            List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
            Iterator i$ = entities.iterator();

            while(i$.hasNext()) {
               Object obj = i$.next();
               EntityLivingBase entity = (EntityLivingBase)obj;
               if(entity.getDistanceSq(0.5D + (double)x, (double)y, 0.5D + (double)z) <= 16.0D) {
                  entity.addPotionEffect(new PotionEffect(Potion.damageBoost.id, TimeUtil.secsToTicks(6), 0));
               }
            }
         }

         return 0;
      }
   };
   public static final BrazierRecipes.BrazierRecipe TOUGH = new BrazierRecipes.BrazierRecipe("witchery.brazier.tough", true, TimeUtil.minsToTicks(5), new ItemStack[]{Witchery.Items.GENERIC.itemTearOfTheGoddess.createStack(), new ItemStack(Items.rotten_flesh), new ItemStack(Items.blaze_powder)}, null) {
      public int onBurning(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         if(TimeUtil.secondsElapsed(3, ticks)) {
            boolean radius = true;
            boolean radiusSq = true;
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(0.5D + (double)x - 4.0D, (double)(y - 4), 0.5D + (double)z - 4.0D, 0.5D + (double)x + 4.0D, (double)(y + 4), 0.5D + (double)z + 4.0D);
            List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
            Iterator i$ = entities.iterator();

            while(i$.hasNext()) {
               Object obj = i$.next();
               EntityLivingBase entity = (EntityLivingBase)obj;
               if(entity.getDistanceSq(0.5D + (double)x, (double)y, 0.5D + (double)z) <= 16.0D) {
                  entity.addPotionEffect(new PotionEffect(Potion.resistance.id, TimeUtil.secsToTicks(6), 0));
               }
            }
         }

         return 0;
      }
   };
   public static final BrazierRecipes.BrazierRecipe INVISIBLE = new BrazierRecipes.BrazierRecipe("witchery.brazier.invisible", true, TimeUtil.minsToTicks(10), new ItemStack[]{new ItemStack(Items.ender_pearl), new ItemStack(Items.spider_eye), new ItemStack(Items.blaze_rod)}, null) {
      public int onBurning(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         if(TimeUtil.secondsElapsed(3, ticks)) {
            boolean radius = true;
            boolean radiusSq = true;
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(0.5D + (double)x - 6.0D, (double)(y - 6), 0.5D + (double)z - 6.0D, 0.5D + (double)x + 6.0D, (double)(y + 6), 0.5D + (double)z + 6.0D);
            List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
            Iterator i$ = entities.iterator();

            while(i$.hasNext()) {
               Object obj = i$.next();
               EntityLivingBase entity = (EntityLivingBase)obj;
               if(entity.getDistanceSq(0.5D + (double)x, (double)y, 0.5D + (double)z) <= 36.0D) {
                  entity.addPotionEffect(new PotionEffect(Potion.invisibility.id, TimeUtil.secsToTicks(6), 0));
               }
            }
         }

         return 0;
      }
   };
   public static final BrazierRecipes.BrazierRecipe WILTING = new BrazierRecipes.BrazierRecipe("witchery.brazier.wilting", true, TimeUtil.minsToTicks(1), new ItemStack[]{Witchery.Items.GENERIC.itemCondensedFear.createStack(), Witchery.Items.GENERIC.itemWormyApple.createStack(), Witchery.Items.GENERIC.itemGraveyardDust.createStack()}, null) {
      public int onBurning(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         if(ticks % 5L == 0L) {
            int offsetY = (int)(ticks % 30L) / 5;
            boolean radius = true;
            boolean diameter = true;
            int posX = x - 3 + world.rand.nextInt(7);
            int posZ = z - 3 + world.rand.nextInt(7);
            int posY = y - 2 + offsetY;
            Block block = BlockUtil.getBlock(world, posX, posY, posZ);
            if(block != null && block instanceof IPlantable) {
               IPlantable plantable = (IPlantable)block;
               if(block instanceof BlockWitchCrop || plantable.getPlantType(world, posX, posY, posZ) == EnumPlantType.Crop) {
                  int currentMeta = plantable.getPlantMetadata(world, posX, posY, posZ);
                  if(currentMeta > 0) {
                     BlockUtil.setMetadata(world, posX, posY, posZ, currentMeta - 1);
                     ParticleEffect.MOB_SPELL.send(SoundEffect.NONE, world, 0.5D + (double)x, 1.0D + (double)y, 0.5D + (double)z, 0.3D, 0.5D, 8);
                     this.healNearbyUndead(world, x, y, z, 3);
                     return 2;
                  }
               }
            }
         }

         return 0;
      }
      private void healNearbyUndead(World world, int x, int y, int z, int radius) {
         AxisAlignedBB bb = AxisAlignedBB.getBoundingBox((double)(x - radius), (double)(y - radius), (double)(z - radius), (double)(x + radius), (double)(y + radius), (double)(z + radius));
         List entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
         Iterator i$ = entities.iterator();

         while(i$.hasNext()) {
            Object obj = i$.next();
            EntityLivingBase entity = (EntityLivingBase)obj;
            if(CreatureUtil.isUndead(entity)) {
               float maxHealth = entity.getMaxHealth();
               if(entity.getHealth() < maxHealth) {
                  entity.heal(maxHealth * 0.1F);
                  ParticleEffect.HEART.send(SoundEffect.NONE, entity, 0.5D, 1.0D, 8);
               }
            }
         }

      }
      public boolean getNeedsPower() {
         return false;
      }
   };


   public static BrazierRecipes instance() {
      return INSTANCE;
   }

   public BrazierRecipes.BrazierRecipe getRecipe(ItemStack[] availableItems) {
      Iterator i$ = this.recipes.iterator();

      BrazierRecipes.BrazierRecipe recipe;
      do {
         if(!i$.hasNext()) {
            return null;
         }

         recipe = (BrazierRecipes.BrazierRecipe)i$.next();
      } while(!recipe.isMatch(availableItems));

      return recipe;
   }


   public static class BrazierRecipe {

      public final int burnTicks;
      public final ItemStack[] items;
      public final boolean inBook;
      public String unlocalizedName;


      private BrazierRecipe(String unlocalizedName, boolean inBook, int burnTicks, ItemStack ... ingredients) {
         this.items = ingredients;
         this.burnTicks = burnTicks;
         this.unlocalizedName = unlocalizedName;
         this.inBook = inBook;
         BrazierRecipes.INSTANCE.recipes.add(this);
      }

      public boolean getNeedsPower() {
         return true;
      }

      public ArrayList getMutableModifiersList() {
         ArrayList available = new ArrayList();
         ItemStack[] arr$ = this.items;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ItemStack item = arr$[i$];
            if(item != null) {
               available.add(item);
            }
         }

         return available;
      }

      private boolean isMatch(ItemStack[] availableItems) {
         ArrayList availableItemList = new ArrayList();
         ItemStack[] arr$ = availableItems;
         int len$ = availableItems.length;

         int i$;
         ItemStack neededItem;
         for(i$ = 0; i$ < len$; ++i$) {
            neededItem = arr$[i$];
            if(neededItem != null) {
               availableItemList.add(neededItem);
            }
         }

         arr$ = this.items;
         len$ = arr$.length;

         for(i$ = 0; i$ < len$; ++i$) {
            neededItem = arr$[i$];
            int index = this.indexOf(availableItemList, neededItem);
            if(index == -1) {
               return false;
            }

            availableItemList.remove(index);
         }

         return true;
      }

      private int indexOf(ArrayList list, ItemStack item) {
         for(int i = 0; i < list.size(); ++i) {
            if(((ItemStack)list.get(i)).isItemEqual(item)) {
               return i;
            }
         }

         return -1;
      }

      public boolean uses(ItemStack ingredient) {
         if(ingredient == null) {
            return false;
         } else {
            ItemStack[] arr$ = this.items;
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               ItemStack item = arr$[i$];
               if(item != null && item.isItemEqual(ingredient)) {
                  return true;
               }
            }

            return false;
         }
      }

      public String getDescription() {
         StringBuffer sb = new StringBuffer();
         sb.append("§n");
         sb.append(Witchery.resource(this.unlocalizedName + ".name"));
         sb.append("§r");
         sb.append(Const.BOOK_NEWLINE);
         sb.append(Const.BOOK_NEWLINE);
         String description = Witchery.resource(this.unlocalizedName + ".desc");
         if(!description.equals(this.unlocalizedName + ".desc")) {
            sb.append(description);
            sb.append(Const.BOOK_NEWLINE);
            sb.append(Const.BOOK_NEWLINE);
         }

         ItemStack[] arr$ = this.items;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ItemStack stack = arr$[i$];
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

         return sb.toString();
      }

      public int onBurning(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {
         return 0;
      }

      public void onBurnt(World world, int x, int y, int z, long ticks, BlockBrazier.TileEntityBrazier tile) {}

      // $FF: synthetic method
      BrazierRecipe(String x0, boolean x1, int x2, ItemStack[] x3, Object x4) {
         this(x0, x1, x2, x3);
      }
   }
}
