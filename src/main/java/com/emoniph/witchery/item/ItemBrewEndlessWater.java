package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemBrewEndlessWater extends ItemBase {

   @SideOnly(Side.CLIENT)
   protected IIcon itemIconOverlay;


   public ItemBrewEndlessWater() {
      this.setMaxStackSize(1);
      this.setMaxDamage(99);
   }

   @SideOnly(Side.CLIENT)
   public boolean hasEffect(ItemStack stack, int pass) {
      return pass == 0;
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIcon(ItemStack stack, int pass) {
      return pass == 0?this.itemIconOverlay:super.itemIcon;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      super.registerIcons(iconRegister);
      this.itemIconOverlay = iconRegister.registerIcon("witchery:brew_overlay");
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int pass) {
      if(pass == 0) {
         boolean color = true;
         return 255;
      } else {
         return super.getColorFromItemStack(stack, pass);
      }
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean expanded) {
      String localText = String.format(Witchery.resource("item.witchery:brew.water.tip"), new Object[]{Integer.valueOf(stack.getMaxDamage() - stack.getItemDamage() + 1).toString(), Integer.valueOf(stack.getMaxDamage() + 1).toString()});
      if(localText != null) {
         String[] arr$ = localText.split("\n");
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String s = arr$[i$];
            if(!s.isEmpty()) {
               list.add(s);
            }
         }
      }

   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      if(!world.isRemote && stack.getItemDamage() <= stack.getMaxDamage()) {
         Block block = world.getBlock(x, y, z);
         if(block == Blocks.cauldron) {
            int face = world.getBlockMetadata(x, y, z);
            if(face < 3) {
               stack.damageItem(1, player);
               Blocks.cauldron.func_150024_a(world, x, y, z, 3);
               SoundEffect.WATER_SPLASH.playAtPlayer(world, player);
            }
         } else {
            ForgeDirection face1 = ForgeDirection.getOrientation(side);
            x += face1.offsetX;
            y += face1.offsetY;
            z += face1.offsetZ;
            if(block != null && BlockUtil.isReplaceableBlock(world, x, y, z, player)) {
               stack.damageItem(1, player);
               world.setBlock(x, y, z, Blocks.flowing_water);
               world.markBlockForUpdate(x, y, z);
               SoundEffect.WATER_SPLASH.playAtPlayer(world, player);
            }
         }
      }

      return false;
   }
}
