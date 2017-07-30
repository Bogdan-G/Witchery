package com.emoniph.witchery.ritual;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.ritual.RiteRegistry;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.Sacrifice;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Const;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class SacrificeItem extends Sacrifice {

   final ItemStack[] itemstacks;


   public SacrificeItem(ItemStack ... itemstacks) {
      this.itemstacks = itemstacks;
   }

   public void addDescription(StringBuffer sb) {
      ItemStack[] arr$ = this.itemstacks;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ItemStack itemstack = arr$[i$];
         sb.append("§8>§0 ");
         if(itemstack.getItem() == Items.potionitem) {
            List list = Items.potionitem.getEffects(itemstack);
            if(list != null && !list.isEmpty()) {
               PotionEffect effect = (PotionEffect)list.get(0);
               String s = itemstack.getDisplayName();
               if(effect.getAmplifier() > 0) {
                  s = s + " " + StatCollector.translateToLocal("potion.potency." + effect.getAmplifier()).trim();
               }

               if(effect.getDuration() > 20) {
                  s = s + " (" + Potion.getDurationString(effect) + ")";
               }

               sb.append(s);
            } else {
               sb.append(itemstack.getDisplayName());
            }
         } else {
            sb.append(itemstack.getDisplayName());
         }

         sb.append(Const.BOOK_NEWLINE);
      }

   }

   public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList entities, ArrayList grassperStacks) {
      ArrayList itemsToRemove = new ArrayList();
      ArrayList otherItemsToRemove = new ArrayList();
      int i$ = 0;

      while(i$ < this.itemstacks.length) {
         boolean itemToRemove = false;
         int i = 0;

         while(true) {
            if(i < entities.size()) {
               label61: {
                  if(entities.get(i) instanceof EntityItem) {
                     EntityItem entity = (EntityItem)entities.get(i);
                     if(isItemEqual(this.itemstacks[i$], entity.getEntityItem()) && !itemsToRemove.contains(entity) && distance(entity.posX, entity.posY, entity.posZ, (double)posX, (double)posY, (double)posZ) <= (double)maxDistance) {
                        itemsToRemove.add(entity);
                        itemToRemove = true;
                        break label61;
                     }
                  }

                  ++i;
                  continue;
               }
            }

            for(i = 0; i < grassperStacks.size(); ++i) {
               if(isItemEqual(this.itemstacks[i$], (ItemStack)grassperStacks.get(i)) && !otherItemsToRemove.contains(grassperStacks.get(i))) {
                  otherItemsToRemove.add(grassperStacks.get(i));
                  itemToRemove = true;
                  break;
               }
            }

            if(!itemToRemove) {
               return false;
            }

            ++i$;
            break;
         }
      }

      Iterator var14 = itemsToRemove.iterator();

      while(var14.hasNext()) {
         Entity var15 = (Entity)var14.next();
         entities.remove(var15);
      }

      var14 = otherItemsToRemove.iterator();

      while(var14.hasNext()) {
         ItemStack var16 = (ItemStack)var14.next();
         grassperStacks.remove(var16);
      }

      return true;
   }

   public static boolean isItemEqual(ItemStack itemstackToFind, ItemStack itemstackFound) {
      return (itemstackFound.getItem() == Witchery.Items.ARTHANA || itemstackFound.getItem() == Witchery.Items.BOLINE) && itemstackFound.getItem() == itemstackToFind.getItem() || itemstackFound.isItemEqual(itemstackToFind);
   }

   public void addSteps(ArrayList steps, AxisAlignedBB bounds, int maxDistance) {
      ItemStack[] arr$ = this.itemstacks;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ItemStack itemstack = arr$[i$];
         steps.add(new SacrificeItem.StepSacrificeItem(itemstack, bounds, maxDistance));
      }

   }

   protected static class StepSacrificeItem extends RitualStep {

      protected final ItemStack itemstack;
      protected final AxisAlignedBB bounds;
      protected final int maxDistance;
      protected boolean showMessages;


      public StepSacrificeItem(ItemStack itemstack, AxisAlignedBB bounds, int maxDistance) {
         super(false);
         this.itemstack = itemstack;
         this.bounds = bounds;
         this.maxDistance = maxDistance;
         this.showMessages = false;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               List itemEntities = world.getEntitiesWithinAABB(EntityItem.class, this.bounds);
               Iterator radius;
               Object x;
               EntityItem z;
               ItemStack blockID;
               if(Config.instance().traceRites()) {
                  radius = itemEntities.iterator();

                  while(radius.hasNext()) {
                     x = radius.next();
                     z = (EntityItem)x;
                     blockID = z.getEntityItem();
                     Log.instance().traceRite(String.format(" * found: %s, distance: %f", new Object[]{blockID.toString(), Double.valueOf(Sacrifice.distance(z.posX, z.posY, z.posZ, (double)posX, (double)posY, (double)posZ))}));
                  }
               }

               radius = itemEntities.iterator();

               while(radius.hasNext()) {
                  x = radius.next();
                  z = (EntityItem)x;
                  blockID = z.getEntityItem();
                  if(SacrificeItem.isItemEqual(this.itemstack, blockID) && Sacrifice.distance(z.posX, z.posY, z.posZ, (double)posX, (double)posY, (double)posZ) <= (double)this.maxDistance) {
                     ItemStack tile = ItemStack.copyItemStack(blockID);
                     tile.stackSize = 1;
                     ritual.sacrificedItems.add(new RitualStep.SacrificedItem(tile, new Coord(z)));
                     if(blockID.isStackable() && blockID.stackSize > 1) {
                        --blockID.stackSize;
                     } else {
                        world.removeEntity(z);
                     }

                     ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_POP, z, 0.5D, 1.0D, 16);
                     return RitualStep.Result.COMPLETED;
                  }
               }

               boolean var17 = true;

               for(int var18 = posX - 5; var18 <= posX + 5; ++var18) {
                  for(int var20 = posZ - 5; var20 <= posZ + 5; ++var20) {
                     Block var19 = world.getBlock(var18, posY, var20);
                     if(var19 == Witchery.Blocks.GRASSPER) {
                        TileEntity var21 = world.getTileEntity(var18, posY, var20);
                        if(var21 != null && var21 instanceof BlockGrassper.TileEntityGrassper) {
                           BlockGrassper.TileEntityGrassper grassper = (BlockGrassper.TileEntityGrassper)var21;
                           ItemStack stack = grassper.getStackInSlot(0);
                           if(stack != null && SacrificeItem.isItemEqual(this.itemstack, stack)) {
                              ItemStack sacrificedItemstack = ItemStack.copyItemStack(stack);
                              sacrificedItemstack.stackSize = 1;
                              ritual.sacrificedItems.add(new RitualStep.SacrificedItem(sacrificedItemstack, new Coord(var21)));
                              if(stack.isStackable() && stack.stackSize > 1) {
                                 --stack.stackSize;
                              } else {
                                 grassper.setInventorySlotContents(0, (ItemStack)null);
                              }

                              ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_POP, world, 0.5D + (double)var18, 0.8D + (double)posY, 0.5D + (double)var20, 0.5D, 1.0D, 16);
                              return RitualStep.Result.COMPLETED;
                           }
                        }
                     }
                  }
               }
            }

            if(this.showMessages) {
               RiteRegistry.RiteError("witchery.rite.missingitem", ritual.getInitiatingPlayerName(), world);
            }

            return RitualStep.Result.ABORTED_REFUND;
         }
      }

      public String toString() {
         return String.format("StepSacrificeItem: %s, maxDistance: %d", new Object[]{this.itemstack.toString(), Integer.valueOf(this.maxDistance)});
      }
   }
}
