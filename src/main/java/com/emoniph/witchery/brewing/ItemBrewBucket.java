package com.emoniph.witchery.brewing;

import com.emoniph.witchery.item.ItemBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class ItemBrewBucket extends ItemBase {

   @SideOnly(Side.CLIENT)
   private IIcon overlayIcon;


   public ItemBrewBucket() {
      this.setMaxStackSize(1);
      this.setMaxDamage(0);
      super.registerWithCreativeTab = false;
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int pass) {
      if(pass == 0) {
         NBTTagCompound nbtRoot = stack.getTagCompound();
         int color = nbtRoot != null?nbtRoot.getInteger("Color"):-16744448;
         return color;
      } else {
         return super.getColorFromItemStack(stack, pass);
      }
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      super.registerIcons(iconRegister);
      this.overlayIcon = iconRegister.registerIcon(this.getIconString() + "_overlay");
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
      return pass == 0?this.overlayIcon:super.itemIcon;
   }

   public boolean hasEffect(ItemStack par1ItemStack, int pass) {
      return pass == 0;
   }
}
