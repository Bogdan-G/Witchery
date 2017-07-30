package com.emoniph.witchery.infusion;

import com.emoniph.witchery.infusion.InfusedBrewEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class InfusedBrewSoaringEffect extends InfusedBrewEffect {

   public InfusedBrewSoaringEffect(int id, long durationMS) {
      super(id, durationMS, 16, 0);
   }

   public void immediateEffect(World world, EntityPlayer player, ItemStack stack) {}

   public void regularEffect(World world, EntityPlayer player) {}
}
