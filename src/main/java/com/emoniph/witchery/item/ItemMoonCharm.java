package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.common.Shapeshift;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import com.emoniph.witchery.util.TransformCreature;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemMoonCharm extends ItemBase {

   public ItemMoonCharm() {
      super.autoGenerateTooltip = true;
      this.setMaxStackSize(1);
      this.setMaxDamage(49);
   }

   public boolean getIsRepairable(ItemStack item, ItemStack otherMaterial) {
      return otherMaterial.isItemEqual(new ItemStack(Items.gold_ingot));
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.rare;
   }

   public EnumAction getItemUseAction(ItemStack itemstack) {
      return EnumAction.bow;
   }

   public int getMaxItemUseDuration(ItemStack itemstack) {
      return TimeUtil.secsToTicks(3);
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int countdown) {
      if(!player.worldObj.isRemote) {
         ExtendedPlayer playerEx = ExtendedPlayer.get(player);
         int level = playerEx.getWerewolfLevel();
         if(countdown == Math.max((level - 1) * 4, 1)) {
            if(!isWolfsbaneActive(player, playerEx) && Shapeshift.INSTANCE.canControlTransform(playerEx)) {
               switch(ItemMoonCharm.NamelessClass1387108829.$SwitchMap$com$emoniph$witchery$util$TransformCreature[playerEx.getCreatureType().ordinal()]) {
               case 1:
                  if(player.isSneaking() && Shapeshift.INSTANCE.isWolfmanAllowed(playerEx)) {
                     Shapeshift.INSTANCE.shiftTo(player, TransformCreature.WOLFMAN);
                  } else {
                     Shapeshift.INSTANCE.shiftTo(player, TransformCreature.WOLF);
                  }

                  ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_FIZZ, player, 1.5D, 1.5D, 16);
                  break;
               case 2:
                  if(player.isSneaking() && Shapeshift.INSTANCE.isWolfmanAllowed(playerEx)) {
                     Shapeshift.INSTANCE.shiftTo(player, TransformCreature.WOLFMAN);
                  } else {
                     Shapeshift.INSTANCE.shiftTo(player, TransformCreature.NONE);
                  }

                  ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_FIZZ, player, 1.5D, 1.5D, 16);
                  break;
               case 3:
                  if(player.isSneaking()) {
                     Shapeshift.INSTANCE.shiftTo(player, TransformCreature.NONE);
                  } else {
                     Shapeshift.INSTANCE.shiftTo(player, TransformCreature.WOLF);
                  }

                  ParticleEffect.EXPLODE.send(SoundEffect.RANDOM_FIZZ, player, 1.5D, 1.5D, 16);
                  break;
               default:
                  ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, player, 0.5D, 0.5D, 8);
               }
            } else {
               ParticleEffect.SMOKE.send(SoundEffect.NOTE_PLING, player, 0.5D, 0.5D, 8);
            }

            stack.damageItem(1, player);
         }
      }

   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      return stack;
   }

   public static boolean isWolfsbaneActive(EntityPlayer player, ExtendedPlayer playerEx) {
      PotionEffect potion = player.getActivePotionEffect(Witchery.Potions.WOLFSBANE);
      if(potion == null) {
         return false;
      } else {
         int amplifier = 1 + Math.max(0, potion.getAmplifier() * 3 - 1);
         return amplifier >= playerEx.getWerewolfLevel();
      }
   }

   // $FF: synthetic class
   static class NamelessClass1387108829 {

      // $FF: synthetic field
      static final int[] $SwitchMap$com$emoniph$witchery$util$TransformCreature = new int[TransformCreature.values().length];


      static {
         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.NONE.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.WOLF.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$com$emoniph$witchery$util$TransformCreature[TransformCreature.WOLFMAN.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
