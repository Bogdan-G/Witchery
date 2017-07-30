package com.emoniph.witchery.item;

import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class ItemDiviner extends ItemBase {

   private static final int MAX_DAMAGE = 50;
   private static final int DAMAGE_PER_USE = 1;
   private final Block blockToDetect;


   public ItemDiviner(Block blockToDetect) {
      this.blockToDetect = blockToDetect;
      this.setMaxStackSize(1);
      this.setMaxDamage(50);
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public EnumAction getItemUseAction(ItemStack itemstack) {
      return EnumAction.bow;
   }

   public int getMaxItemUseDuration(ItemStack itemstack) {
      return 400;
   }

   public void onUsingTick(ItemStack itemstack, EntityPlayer player, int countdown) {
      World world = player.worldObj;
      if(!world.isRemote) {
         int elapsedTicks = this.getMaxItemUseDuration(itemstack) - countdown;
         MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 6.0D);
         if(mop == null || mop.typeOfHit != MovingObjectType.BLOCK || !BlockSide.TOP.isEqual(mop.sideHit)) {
            SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
            player.clearItemInUse();
            return;
         }

         int posX = MathHelper.floor_double((double)mop.blockX);
         int posY = MathHelper.floor_double((double)mop.blockY) - elapsedTicks;
         int posZ = MathHelper.floor_double((double)mop.blockZ);
         Block block = world.getBlock(posX, posY, posZ);
         boolean foundBlock = false;
         boolean foundSomething = false;
         if(block == this.blockToDetect) {
            foundBlock = true;
            foundSomething = true;
         } else if(block == Blocks.bedrock) {
            foundBlock = false;
            foundSomething = true;
         }

         if(foundSomething || posY <= 1) {
            if(foundBlock) {
               ParticleEffect.MAGIC_CRIT.send(SoundEffect.RANDOM_ORB, world, 0.5D + (double)mop.blockX, (double)(mop.blockY + 1), 0.5D + (double)mop.blockZ, 0.5D, 0.5D, 8);
            } else {
               ParticleEffect.SMOKE.send(SoundEffect.NOTE_SNARE, world, 0.5D + (double)mop.blockX, (double)(mop.blockY + 1), 0.5D + (double)mop.blockZ, 0.5D, 0.5D, 8);
            }

            player.clearItemInUse();
            itemstack.damageItem(1, player);
         }
      }

   }

   public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
      player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
      return itemstack;
   }
}
