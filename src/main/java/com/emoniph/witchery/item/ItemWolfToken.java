package com.emoniph.witchery.item;

import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemWolfToken extends ItemBase {

   public ItemWolfToken() {
      super.autoGenerateTooltip = true;
      this.setMaxStackSize(1);
      this.setMaxDamage(0);
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.epic;
   }

   public EnumAction getItemUseAction(ItemStack itemstack) {
      return EnumAction.bow;
   }

   public int getMaxItemUseDuration(ItemStack itemstack) {
      return TimeUtil.secsToTicks(1);
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int countdown) {
      if(!player.worldObj.isRemote && countdown == 1) {
         ExtendedPlayer playerEx;
         int wolfLevel;
         if(player.isSneaking()) {
            playerEx = ExtendedPlayer.get(player);
            wolfLevel = playerEx.getVampireLevel() + 1;
            if(wolfLevel > 10) {
               wolfLevel = 0;
            }

            playerEx.setVampireLevel(wolfLevel);
            ChatUtil.sendTranslated(EnumChatFormatting.GREEN, player, "witchery.vampire.setlevel", new Object[]{Integer.valueOf(wolfLevel).toString()});
            ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_FIZZ, player, 1.5D, 1.5D, 16);
         } else {
            playerEx = ExtendedPlayer.get(player);
            wolfLevel = playerEx.getWerewolfLevel() + 1;
            if(wolfLevel > 10) {
               wolfLevel = 0;
            }

            playerEx.setWerewolfLevel(wolfLevel);
            ChatUtil.sendTranslated(EnumChatFormatting.GREEN, player, "witchery.werewolf.setlevel", new Object[]{Integer.valueOf(wolfLevel).toString()});
            ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_FIZZ, player, 1.5D, 1.5D, 16);
         }
      }

   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      return stack;
   }
}
