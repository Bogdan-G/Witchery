package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.entity.EntityGrenade;
import com.emoniph.witchery.item.ItemBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemSunGrenade extends ItemBase {

   private final int mode;
   @SideOnly(Side.CLIENT)
   protected IIcon itemIconOverlay;


   public ItemSunGrenade(int mode) {
      this.mode = mode;
      this.setMaxStackSize(16);
      this.setMaxDamage(0);
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean moreTips) {
      super.addInformation(stack, player, list, moreTips);
      if(this.mode == 1) {
         list.add(String.format(Witchery.resource("item.witchery:dupgrenade.tip"), new Object[]{getOwnerName(stack)}));
      }

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
      this.itemIconOverlay = iconRegister.registerIcon("witchery:ingredient.quartzSphere");
   }

   public EnumRarity getRarity(ItemStack stack) {
      return EnumRarity.uncommon;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return EnumAction.bow;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if(!player.capabilities.isCreativeMode) {
         --stack.stackSize;
      }

      world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
      if(!world.isRemote) {
         EntityGrenade grenade = new EntityGrenade(world, player, stack);
         grenade.setMode(this.mode);
         if(this.mode == 1) {
            grenade.setOwner(getOwnerName(stack));
         }

         world.spawnEntityInWorld(grenade);
      }

      return stack;
   }

   public static String getOwnerName(ItemStack stack) {
      if(stack.hasTagCompound()) {
         NBTTagCompound nbtRoot = stack.getTagCompound();
         return nbtRoot.getString("Owner");
      } else {
         return null;
      }
   }

   public static void setOwnerName(ItemStack stack, String name) {
      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      NBTTagCompound nbtRoot = stack.getTagCompound();
      nbtRoot.setString("Owner", name);
   }
}
