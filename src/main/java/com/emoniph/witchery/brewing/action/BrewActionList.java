package com.emoniph.witchery.brewing.action;

import com.emoniph.witchery.brewing.BrewItemKey;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.ModifiersRitual;
import com.emoniph.witchery.brewing.action.BrewAction;
import com.emoniph.witchery.util.BlockPosition;
import java.util.ArrayList;
import java.util.Hashtable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BrewActionList {

   private final NBTTagCompound nbtBrew;
   public final ArrayList actions = new ArrayList();
   public final ArrayList items = new ArrayList();


   public BrewActionList(NBTTagCompound nbtBrew, Hashtable actionRegistry) {
      this.nbtBrew = nbtBrew;
      if(nbtBrew != null) {
         NBTTagList nbtItems = nbtBrew.getTagList("Items", 10);
         int i = 0;

         for(int count = nbtItems.tagCount(); i < count; ++i) {
            NBTTagCompound nbtItem = nbtItems.getCompoundTagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(nbtItem);
            BrewAction brewAction = (BrewAction)actionRegistry.get(BrewItemKey.fromStack(stack));
            if(brewAction != null) {
               this.actions.add(brewAction);
               this.items.add(stack);
            }
         }
      }

   }

   public ItemStack getTopItemStack() {
      return this.size() > 0?(ItemStack)this.items.get(this.size() - 1):null;
   }

   public BrewAction getTopAction() {
      return this.size() > 0?(BrewAction)this.actions.get(this.size() - 1):null;
   }

   public int size() {
      return this.actions.size();
   }

   public NBTTagCompound getTagCompound() {
      return this.nbtBrew;
   }

   public void nullifyItems(BrewAction brewAction, NBTTagList nbtItems) {
      brewAction.processNullifaction(this.actions, nbtItems);
   }

   public void applyToEntity(World world, EntityLivingBase targetEntity, ModifiersEffect modifiers) {
      for(int i = 0; i < this.actions.size(); ++i) {
         BrewAction action = (BrewAction)this.actions.get(i);
         if(action.augmentEffectLevels(modifiers.effectLevel)) {
            action.augmentEffectModifiers(modifiers);
            action.applyToEntity(world, targetEntity, modifiers, (ItemStack)this.items.get(i));
         }
      }

   }

   public void applyToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersEffect modifiers) {
      for(int i = 0; i < this.actions.size(); ++i) {
         BrewAction action = (BrewAction)this.actions.get(i);
         if(action.augmentEffectLevels(modifiers.effectLevel)) {
            action.augmentEffectModifiers(modifiers);
            action.applyToBlock(world, x, y, z, side, radius, modifiers, (ItemStack)this.items.get(i));
         }
      }

   }

   public void prepareRitual(World world, int x, int y, int z, ModifiersRitual modifiers) {
      for(int i = 0; i < this.actions.size(); ++i) {
         BrewAction action = (BrewAction)this.actions.get(i);
         action.prepareRitual(world, x, y, z, modifiers, (ItemStack)this.items.get(i));
      }

   }

   public void applyRitualToBlock(World world, int x, int y, int z, ForgeDirection side, int radius, ModifiersRitual ritualModifiers, ModifiersEffect modifiers) {
      for(int i = 0; i < this.actions.size(); ++i) {
         BrewAction action = (BrewAction)this.actions.get(i);
         if(action.augmentEffectLevels(modifiers.effectLevel)) {
            action.augmentEffectModifiers(modifiers);
            action.applyRitualToBlock(world, x, y, z, side, radius, ritualModifiers, modifiers, (ItemStack)this.items.get(i));
         }
      }

   }

   public void applyRitualToEnitity(World world, EntityLivingBase targetEntity, ModifiersRitual ritualModifiers, ModifiersEffect modifiers) {
      for(int i = 0; i < this.actions.size(); ++i) {
         BrewAction action = (BrewAction)this.actions.get(i);
         if(action.augmentEffectLevels(modifiers.effectLevel)) {
            action.augmentEffectModifiers(modifiers);
            action.applyRitualToEntity(world, targetEntity, ritualModifiers, modifiers, (ItemStack)this.items.get(i));
         }
      }

   }

   public boolean isTargetLocationValid(MinecraftServer server, World world, int x, int y, int z, BlockPosition target, ModifiersRitual modifiers) {
      for(int i = 0; i < this.actions.size(); ++i) {
         BrewAction action = (BrewAction)this.actions.get(i);
         if(!action.isRitualTargetLocationValid(server, world, x, y, z, target, modifiers)) {
            return false;
         }
      }

      return true;
   }
}
