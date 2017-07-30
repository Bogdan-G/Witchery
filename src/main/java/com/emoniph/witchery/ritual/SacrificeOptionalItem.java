package com.emoniph.witchery.ritual;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.blocks.BlockGrassper;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.ritual.Sacrifice;
import com.emoniph.witchery.ritual.SacrificeItem;
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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class SacrificeOptionalItem extends SacrificeItem {

   public SacrificeOptionalItem(ItemStack optionalSacrifice) {
      super(new ItemStack[]{optionalSacrifice});
   }

   public void addDescription(StringBuffer sb) {
      ItemStack[] arr$ = super.itemstacks;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ItemStack itemstack = arr$[i$];
         sb.append("§8> ");
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

         sb.append(" ");
         sb.append(Witchery.resource("witchery.rite.optional"));
         sb.append("§0");
         sb.append(Const.BOOK_NEWLINE);
      }

   }

   public boolean isMatch(World world, int posX, int posY, int posZ, int maxDistance, ArrayList entities, ArrayList grassperStacks) {
      super.isMatch(world, posX, posY, posZ, maxDistance, entities, grassperStacks);
      return true;
   }

   public void addSteps(ArrayList steps, AxisAlignedBB bounds, int maxDistance) {
      ItemStack[] arr$ = super.itemstacks;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ItemStack itemstack = arr$[i$];
         steps.add(new SacrificeOptionalItem.StepSacrificeOptionalItem(itemstack, bounds, maxDistance));
      }

   }

   private static class StepSacrificeOptionalItem extends SacrificeItem.StepSacrificeItem {

      public StepSacrificeOptionalItem(ItemStack itemstack, AxisAlignedBB bounds, int maxDistance) {
         super(itemstack, bounds, maxDistance);
         super.showMessages = false;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               List itemEntities = world.getEntitiesWithinAABB(EntityItem.class, super.bounds);
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
                  if(SacrificeItem.isItemEqual(super.itemstack, blockID) && Sacrifice.distance(z.posX, z.posY, z.posZ, (double)posX, (double)posY, (double)posZ) <= (double)super.maxDistance) {
                     boolean tile = false;
                     if(Witchery.Items.GENERIC.itemWaystoneBound.isMatch(blockID) && !Witchery.Items.GENERIC.copyLocationBinding(world, blockID, ritual)) {
                        tile = true;
                     }

                     if(!tile) {
                        ItemStack grassper = ItemStack.copyItemStack(blockID);
                        grassper.stackSize = 1;
                        ritual.sacrificedItems.add(new RitualStep.SacrificedItem(grassper, new Coord(z)));
                        if(blockID.isStackable() && blockID.stackSize > 1) {
                           --blockID.stackSize;
                        } else {
                           world.removeEntity(z);
                        }
                     }

                     if(!tile) {
                        ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_POP, z, 0.5D, 1.0D, 16);
                     } else {
                        ParticleEffect.SMOKE.send(SoundEffect.NOTE_PLING, z, 0.5D, 1.0D, 16);
                     }

                     return RitualStep.Result.COMPLETED;
                  }
               }

               boolean var18 = true;

               for(int var19 = posX - 5; var19 <= posX + 5; ++var19) {
                  for(int var21 = posZ - 5; var21 <= posZ + 5; ++var21) {
                     Block var20 = world.getBlock(var19, posY, var21);
                     if(var20 == Witchery.Blocks.GRASSPER) {
                        TileEntity var22 = world.getTileEntity(var19, posY, var21);
                        if(var22 != null && var22 instanceof BlockGrassper.TileEntityGrassper) {
                           BlockGrassper.TileEntityGrassper var23 = (BlockGrassper.TileEntityGrassper)var22;
                           ItemStack stack = var23.getStackInSlot(0);
                           if(stack != null && SacrificeItem.isItemEqual(super.itemstack, stack)) {
                              boolean skip = false;
                              if(Witchery.Items.GENERIC.itemWaystoneBound.isMatch(stack) && !Witchery.Items.GENERIC.copyLocationBinding(world, stack, ritual)) {
                                 skip = true;
                              }

                              if(!skip) {
                                 ItemStack sacrificedItemstack = ItemStack.copyItemStack(stack);
                                 sacrificedItemstack.stackSize = 1;
                                 ritual.sacrificedItems.add(new RitualStep.SacrificedItem(sacrificedItemstack, new Coord(var22)));
                                 if(stack.isStackable() && stack.stackSize > 1) {
                                    --stack.stackSize;
                                 } else {
                                    var23.setInventorySlotContents(0, (ItemStack)null);
                                 }
                              }

                              if(!skip) {
                                 ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_POP, world, 0.5D + (double)var19, 0.8D + (double)posY, 0.5D + (double)var21, 0.5D, 1.0D, 16);
                              } else {
                                 ParticleEffect.SMOKE.send(SoundEffect.NOTE_PLING, world, 0.5D + (double)var19, 0.8D + (double)posY, 0.5D + (double)var21, 0.5D, 1.0D, 16);
                              }

                              return RitualStep.Result.COMPLETED;
                           }
                        }
                     }
                  }
               }
            }

            return RitualStep.Result.COMPLETED;
         }
      }

      public String toString() {
         return String.format("StepSacrificeOptionalItem: %s, maxDistance: %d", new Object[]{super.itemstack.toString(), Integer.valueOf(super.maxDistance)});
      }
   }
}
