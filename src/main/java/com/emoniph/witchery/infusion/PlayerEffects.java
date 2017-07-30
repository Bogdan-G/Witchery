package com.emoniph.witchery.infusion;

import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.Log;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

public class PlayerEffects {

   private static final ArrayList effects = new ArrayList();
   public static final String KEY_EFFECTS = "witchery.effects";
   public static final PlayerEffects.PlayerEffect IMP_FIRE_TOUCH = new PlayerEffects.PlayerEffect("witchery.imp.firetouch", effects) {
      protected void doUpdate(EntityPlayer player, int worldTicks) {}
      protected void doHarvest(EntityPlayer player, HarvestDropsEvent event) {}
      protected void doInteract(EntityPlayer player, PlayerInteractEvent event) {
         World world = player.worldObj;
         if(world.rand.nextDouble() < 0.2D) {
            Block block = BlockUtil.getBlock(world, event.x, event.y, event.z);
            if(block != null && block != Blocks.air) {
               int par4 = event.x;
               int par5 = event.y;
               int par6 = event.z;
               int par7 = event.face;
               if(par7 == 0) {
                  --par5;
               }

               if(par7 == 1) {
                  ++par5;
               }

               if(par7 == 2) {
                  --par6;
               }

               if(par7 == 3) {
                  ++par6;
               }

               if(par7 == 4) {
                  --par4;
               }

               if(par7 == 5) {
                  ++par4;
               }

               if(event.action == Action.LEFT_CLICK_BLOCK) {
                  par4 = par4 - 1 + world.rand.nextInt(3);
                  par6 = par6 - 1 + world.rand.nextInt(3);
               }

               if(world.isAirBlock(par4, par5, par6) && !world.isAirBlock(par4, par5 - 1, par6)) {
                  world.playSoundEffect((double)par4 + 0.5D, (double)par5 + 0.5D, (double)par6 + 0.5D, SoundEffect.FIRE_FIRE.toString(), 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                  world.setBlock(par4, par5, par6, Blocks.fire);
               }
            }
         }

      }
   };
   public static final PlayerEffects.PlayerEffect IMP_EVAPORATION = new PlayerEffects.PlayerEffect("witchery.imp.evaporation", effects) {
      protected void doUpdate(EntityPlayer player, int worldTicks) {
         if(player.worldObj.rand.nextInt(5) == 0) {
            int midX = MathHelper.floor_double(player.posX);
            int midY = MathHelper.floor_double(player.posY);
            int midZ = MathHelper.floor_double(player.posZ);
            boolean R = true;
            boolean RSq = true;
            boolean found = false;

            for(int x = midX - 3; x <= midX + 3; ++x) {
               for(int z = midZ - 3; z <= midZ + 3; ++z) {
                  for(int y = midY + 2; y >= midY - 1; --y) {
                     if(player.getDistanceSq((double)x, (double)y, (double)z) <= 9.0D) {
                        Block block = BlockUtil.getBlock(player.worldObj, x, y, z);
                        if((block == Blocks.water || block == Blocks.flowing_water) && player.worldObj.isAirBlock(x, y + 1, z)) {
                           player.worldObj.setBlockToAir(x, y, z);
                           ParticleEffect.EXPLODE.send(SoundEffect.NONE, player.worldObj, (double)x, (double)(y + 1), (double)z, 1.0D, 1.0D, 16);
                           found = true;
                        }
                     }
                  }
               }
            }

            if(found) {
               SoundEffect.RANDOM_FIZZ.playAt(player.worldObj, player.posX, player.posY, player.posZ, 1.0F, 2.6F + (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.8F);
            }
         }

      }
      protected void doHarvest(EntityPlayer player, HarvestDropsEvent event) {}
      protected void doInteract(EntityPlayer player, PlayerInteractEvent event) {}
   };
   public static final PlayerEffects.PlayerEffect IMP_METLING_TOUCH = new PlayerEffects.PlayerEffect("witchery.im.meltingtouch", effects) {
      protected void doUpdate(EntityPlayer player, int worldTicks) {}
      protected void doHarvest(EntityPlayer player, HarvestDropsEvent event) {
         ArrayList newDrops = new ArrayList();
         Iterator i$ = event.drops.iterator();

         ItemStack newDrop;
         while(i$.hasNext()) {
            newDrop = (ItemStack)i$.next();
            ItemStack smeltedDrop = FurnaceRecipes.smelting().getSmeltingResult(newDrop);
            if(smeltedDrop != null) {
               Log.instance().debug("Smelting Touch: " + newDrop.toString() + " -> " + smeltedDrop.toString());
               ItemStack smelted = smeltedDrop.copy();
               if(player.worldObj.rand.nextDouble() < 0.25D) {
                  ++smelted.stackSize;
               }

               newDrops.add(smelted);
            } else {
               Log.instance().debug("Smelting Touch: " + newDrop.toString() + " -> none");
               newDrops.add(newDrop);
            }
         }

         event.drops.clear();
         i$ = newDrops.iterator();

         while(i$.hasNext()) {
            newDrop = (ItemStack)i$.next();
            event.drops.add(newDrop);
         }

      }
      protected void doInteract(EntityPlayer player, PlayerInteractEvent event) {}
   };
   private static final int TICKS_PER_UPDATE = 20;


   public static void onDeath(EntityPlayer player) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      if(nbtPlayer != null && nbtPlayer.hasKey("witchery.effects")) {
         NBTTagCompound nbtEffects = nbtPlayer.getCompoundTag("witchery.effects");
         Iterator i$ = effects.iterator();

         while(i$.hasNext()) {
            PlayerEffects.PlayerEffect effect = (PlayerEffects.PlayerEffect)i$.next();
            effect.removeFrom(nbtEffects);
         }

         if(nbtEffects.hasNoTags()) {
            nbtPlayer.removeTag("witchery.effects");
         }
      }

   }

   public static void onUpdate(EntityPlayer player, long ticks) {
      if(ticks % 20L == 3L) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(nbtPlayer != null && nbtPlayer.hasKey("witchery.effects")) {
            NBTTagCompound nbtEffects = nbtPlayer.getCompoundTag("witchery.effects");
            Iterator i$ = effects.iterator();

            while(i$.hasNext()) {
               PlayerEffects.PlayerEffect effect = (PlayerEffects.PlayerEffect)i$.next();
               effect.update(nbtEffects, 20, player);
            }

            if(nbtEffects.hasNoTags()) {
               nbtPlayer.removeTag("witchery.effects");
            }
         }
      }

   }

   public static void onHarvestDrops(EntityPlayer player, HarvestDropsEvent event) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      if(nbtPlayer != null && nbtPlayer.hasKey("witchery.effects")) {
         NBTTagCompound nbtEffects = nbtPlayer.getCompoundTag("witchery.effects");
         Iterator i$ = effects.iterator();

         while(i$.hasNext()) {
            PlayerEffects.PlayerEffect effect = (PlayerEffects.PlayerEffect)i$.next();
            effect.harvest(nbtEffects, event, player);
         }

         if(nbtEffects.hasNoTags()) {
            nbtPlayer.removeTag("witchery.effects");
         }
      }

   }

   public static void onInteract(EntityPlayer player, PlayerInteractEvent event) {
      NBTTagCompound nbtPlayer = Infusion.getNBT(player);
      if(nbtPlayer != null && nbtPlayer.hasKey("witchery.effects")) {
         NBTTagCompound nbtEffects = nbtPlayer.getCompoundTag("witchery.effects");
         Iterator i$ = effects.iterator();

         while(i$.hasNext()) {
            PlayerEffects.PlayerEffect effect = (PlayerEffects.PlayerEffect)i$.next();
            effect.interact(nbtEffects, event, player);
         }

         if(nbtEffects.hasNoTags()) {
            nbtPlayer.removeTag("witchery.effects");
         }
      }

   }


   public abstract static class PlayerEffect {

      protected final String unlocalizedName;


      protected PlayerEffect(String unlocalizedName, ArrayList effects) {
         this.unlocalizedName = unlocalizedName;
         effects.add(this);
      }

      public void interact(NBTTagCompound nbtEffects, PlayerInteractEvent event, EntityPlayer player) {
         if(nbtEffects.hasKey(this.unlocalizedName)) {
            this.doInteract(player, event);
         }

      }

      protected abstract void doInteract(EntityPlayer var1, PlayerInteractEvent var2);

      public void harvest(NBTTagCompound nbtEffects, HarvestDropsEvent event, EntityPlayer player) {
         if(nbtEffects.hasKey(this.unlocalizedName)) {
            this.doHarvest(player, event);
         }

      }

      protected abstract void doHarvest(EntityPlayer var1, HarvestDropsEvent var2);

      public void applyTo(EntityPlayer player, int durationTicks) {
         NBTTagCompound nbtPlayer = Infusion.getNBT(player);
         if(nbtPlayer != null) {
            if(!nbtPlayer.hasKey("witchery.effects")) {
               nbtPlayer.setTag("witchery.effects", new NBTTagCompound());
            }

            NBTTagCompound nbtEffects = nbtPlayer.getCompoundTag("witchery.effects");
            nbtEffects.setInteger(this.unlocalizedName, durationTicks);
         }

      }

      private void removeFrom(NBTTagCompound nbtEffects) {
         if(nbtEffects.hasKey(this.unlocalizedName)) {
            nbtEffects.removeTag(this.unlocalizedName);
         }

      }

      private void update(NBTTagCompound nbtEffects, int ticks, EntityPlayer player) {
         if(nbtEffects.hasKey(this.unlocalizedName)) {
            int remainingTicks = nbtEffects.getInteger(this.unlocalizedName);
            int newTicks = Math.max(remainingTicks - ticks, 0);
            if(newTicks == 0) {
               this.removeFrom(nbtEffects);
            } else {
               nbtEffects.setInteger(this.unlocalizedName, newTicks);
               this.doUpdate(player, ticks);
            }
         }

      }

      protected abstract void doUpdate(EntityPlayer var1, int var2);
   }
}
