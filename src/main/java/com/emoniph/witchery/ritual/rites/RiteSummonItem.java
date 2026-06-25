package com.emoniph.witchery.ritual.rites;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockCircle;
import com.emoniph.witchery.predictions.PredictionManager;
import com.emoniph.witchery.ritual.Rite;
import com.emoniph.witchery.ritual.RitualStep;
import com.emoniph.witchery.util.Coord;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class RiteSummonItem extends Rite {

   private final ItemStack itemToSummon;
   private final RiteSummonItem.Binding binding;


   public RiteSummonItem(ItemStack itemToSummon, RiteSummonItem.Binding binding) {
      this.itemToSummon = itemToSummon;
      this.binding = binding;
   }

   public void addSteps(ArrayList steps, int intialStage) {
      steps.add(new RiteSummonItem.StepSummonItem(this));
   }

   public static enum Binding {

      NONE("NONE", 0),
      LOCATION("LOCATION", 1),
      ENTITY("ENTITY", 2),
      COPY_LOCATION("COPY_LOCATION", 3),
      PLAYER("PLAYER", 4);
      // $FF: synthetic field
      private static final RiteSummonItem.Binding[] $VALUES = new RiteSummonItem.Binding[]{NONE, LOCATION, ENTITY, COPY_LOCATION, PLAYER};


      private Binding(String var1, int var2) {}

   }

   private static class StepSummonItem extends RitualStep {

      private final RiteSummonItem rite;


      public StepSummonItem(RiteSummonItem rite) {
         super(false);
         this.rite = rite;
      }

      public RitualStep.Result process(World world, int posX, int posY, int posZ, long ticks, BlockCircle.TileEntityCircle.ActivatedRitual ritual) {
         if(ticks % 20L != 0L) {
            return RitualStep.Result.STARTING;
         } else {
            if(!world.isRemote) {
               ItemStack itemstack = ItemStack.copyItemStack(this.rite.itemToSummon);
               if(this.rite.binding == RiteSummonItem.Binding.LOCATION) {
                  Witchery.Items.GENERIC.bindToLocation(world, posX, posY, posZ, world.provider.dimensionId, world.provider.getDimensionName(), itemstack);
               } else {
                  boolean entity;
                  AxisAlignedBB bounds;
                  Iterator nbtRoot;
                  Object obj;
                  EntityPlayer player;
                  if(this.rite.binding == RiteSummonItem.Binding.ENTITY) {
                     entity = true;
                     Object item = null;
                     bounds = AxisAlignedBB.getBoundingBox((double)(posX - 4), (double)posY, (double)(posZ - 4), (double)(posX + 4), (double)(posY + 1), (double)(posZ + 4));
                     nbtRoot = world.getEntitiesWithinAABB(EntityPlayer.class, bounds).iterator();

                     while(nbtRoot.hasNext()) {
                        obj = nbtRoot.next();
                        player = (EntityPlayer)obj;
                        if(Coord.distance(player.posX, player.posY, player.posZ, (double)posX, (double)posY, (double)posZ) <= 4.0D) {
                           item = player;
                        }
                     }

                     if(item != null) {
                        bounds = AxisAlignedBB.getBoundingBox((double)(posX - 4), (double)posY, (double)(posZ - 4), (double)(posX + 4), (double)(posY + 1), (double)(posZ + 4));
                        nbtRoot = world.getEntitiesWithinAABB(EntityLiving.class, bounds).iterator();

                        while(nbtRoot.hasNext()) {
                           obj = nbtRoot.next();
                           EntityLiving player1 = (EntityLiving)obj;
                           if(Coord.distance(player1.posX, player1.posY, player1.posZ, (double)posX, (double)posY, (double)posZ) <= 4.0D) {
                              item = player1;
                           }
                        }
                     }

                     if(item == null) {
                        return RitualStep.Result.ABORTED_REFUND;
                     }

                     Witchery.Items.TAGLOCK_KIT.setTaglockForEntity(itemstack, (EntityPlayer)null, (Entity)item, false, Integer.valueOf(1));
                  } else if(this.rite.binding == RiteSummonItem.Binding.PLAYER) {
                     entity = true;
                     EntityPlayer item1 = null;
                     bounds = AxisAlignedBB.getBoundingBox((double)(posX - 4), (double)posY, (double)(posZ - 4), (double)(posX + 4), (double)(posY + 1), (double)(posZ + 4));
                     nbtRoot = world.getEntitiesWithinAABB(EntityPlayer.class, bounds).iterator();

                     while(nbtRoot.hasNext()) {
                        obj = nbtRoot.next();
                        player = (EntityPlayer)obj;
                        if(Coord.distance(player.posX, player.posY, player.posZ, (double)posX, (double)posY, (double)posZ) <= 4.0D) {
                           item1 = player;
                        }
                     }

                     if(item1 == null) {
                        return RitualStep.Result.ABORTED_REFUND;
                     }

                     NBTTagCompound nbtRoot1 = new NBTTagCompound();
                     nbtRoot1.setString("WITCBoundPlayer", item1.getCommandSenderName());
                     itemstack.setTagCompound(nbtRoot1);
                  } else if(this.rite.binding == RiteSummonItem.Binding.COPY_LOCATION) {
                     Iterator entity2 = ritual.sacrificedItems.iterator();

                     while(entity2.hasNext()) {
                        RitualStep.SacrificedItem item2 = (RitualStep.SacrificedItem)entity2.next();
                        if(Witchery.Items.GENERIC.hasLocationBinding(item2.itemstack)) {
                           Witchery.Items.GENERIC.copyLocationBinding(item2.itemstack, itemstack);
                           break;
                        }
                     }
                  }
               }

               if(itemstack.getItem() == Item.getItemFromBlock(Witchery.Blocks.CRYSTAL_BALL)) {
                  EntityPlayer entity1 = ritual.getInitiatingPlayer(world);
                  if(entity1 != null) {
                     PredictionManager.instance().setFortuneTeller(entity1, true);
                  }
               }

               EntityItem entity3 = new EntityItem(world, 0.5D + (double)posX, (double)posY + 1.5D, 0.5D + (double)posZ, itemstack);
               entity3.motionX = 0.0D;
               entity3.motionY = 0.3D;
               entity3.motionZ = 0.0D;
               world.spawnEntityInWorld(entity3);
               ParticleEffect.SPELL.send(SoundEffect.RANDOM_FIZZ, entity3, 0.5D, 0.5D, 16);
            }

            return RitualStep.Result.COMPLETED;
         }
      }
   }
}
