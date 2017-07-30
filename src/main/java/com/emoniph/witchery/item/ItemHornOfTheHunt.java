package com.emoniph.witchery.item;

import com.emoniph.witchery.entity.EntityHornedHuntsman;
import com.emoniph.witchery.infusion.infusions.InfusionInfernal;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHornOfTheHunt extends ItemBase {

   public ItemHornOfTheHunt() {
      super.autoGenerateTooltip = true;
      this.setMaxDamage(1);
      this.setMaxStackSize(1);
   }

   public EnumAction getItemUseAction(ItemStack itemstack) {
      return EnumAction.bow;
   }

   public int getMaxItemUseDuration(ItemStack itemstack) {
      return TimeUtil.secsToTicks(2);
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int countdown) {
      if(!player.worldObj.isRemote && countdown == 1) {
         SoundEffect.WITCHERY_RANDOM_HORN.playAtPlayer(player.worldObj, player, 1.0F, 1.0F);
         EntityCreature creature = InfusionInfernal.spawnCreature(player.worldObj, EntityHornedHuntsman.class, (int)player.posX, (int)player.posY, (int)player.posZ, player, 2, 8, ParticleEffect.EXPLODE, SoundEffect.MOB_WITHER_SPAWN);
         if(creature != null) {
            EntityHornedHuntsman huntsman = (EntityHornedHuntsman)creature;
            huntsman.causeExplosiveEntrance();
            huntsman.func_110163_bv();
            huntsman.func_82206_m();
            stack.damageItem(2, player);
         }
      }

   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      return stack;
   }
}
