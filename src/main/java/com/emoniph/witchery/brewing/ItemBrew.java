package com.emoniph.witchery.brewing;

import com.emoniph.witchery.brewing.EntityBrew;
import com.emoniph.witchery.brewing.ModifiersEffect;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.EntityPosition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemBrew extends ItemBase {

   @SideOnly(Side.CLIENT)
   protected IIcon itemIconOverlay;
   @SideOnly(Side.CLIENT)
   protected IIcon itemIconSplash;


   public ItemBrew() {
      this.setMaxStackSize(8);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
      super.registerWithCreativeTab = false;
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
      return pass == 0?this.itemIconOverlay:(stack != null && !WitcheryBrewRegistry.INSTANCE.isSplash(stack.getTagCompound())?super.itemIcon:this.itemIconSplash);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      super.registerIcons(iconRegister);
      this.itemIconOverlay = iconRegister.registerIcon("witchery:brew_overlay");
      this.itemIconSplash = iconRegister.registerIcon("witchery:brew_splash");
   }

   @SideOnly(Side.CLIENT)
   public String getItemStackDisplayName(ItemStack stack) {
      NBTTagCompound nbtRoot = stack.getTagCompound();
      return nbtRoot != null?nbtRoot.getString("BrewName"):super.getItemStackDisplayName(stack);
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack stack, int pass) {
      if(pass == 0) {
         NBTTagCompound nbtRoot = stack.getTagCompound();
         return WitcheryBrewRegistry.INSTANCE.getBrewColor(nbtRoot);
      } else {
         return super.getColorFromItemStack(stack, pass);
      }
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean expanded) {
      NBTTagCompound nbtRoot = stack.getTagCompound();
      if(nbtRoot != null) {
         String localText = nbtRoot.getString("BrewInfo");
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

   }

   public EnumRarity getRarity(ItemStack stack) {
      return EnumRarity.common;
   }

   public int getMaxItemUseDuration(ItemStack stack) {
      boolean DEFAULT_SPEED = true;
      NBTTagCompound nbtRoot = stack.getTagCompound();
      int drinkSpeed = nbtRoot != null?nbtRoot.getInteger("BrewDrinkSpeed"):32;
      return drinkSpeed > 0?drinkSpeed:32;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return WitcheryBrewRegistry.INSTANCE.isSplash(stack.getTagCompound())?EnumAction.bow:EnumAction.drink;
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if(WitcheryBrewRegistry.INSTANCE.isSplash(stack.getTagCompound())) {
         if(!player.capabilities.isCreativeMode) {
            --stack.stackSize;
         }

         world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
         if(!world.isRemote) {
            world.spawnEntityInWorld(new EntityBrew(world, player, stack, false));
         }
      } else {
         player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      }

      return stack;
   }

   public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
      if(!player.capabilities.isCreativeMode) {
         --stack.stackSize;
      }

      if(!world.isRemote) {
         ModifiersEffect modifiers = new ModifiersEffect(1.0D, 1.0D, false, (EntityPosition)null, false, 0, player);
         WitcheryBrewRegistry.INSTANCE.applyToEntity(world, player, stack.getTagCompound(), modifiers);
      }

      if(!player.capabilities.isCreativeMode) {
         if(stack.stackSize <= 0) {
            return new ItemStack(Items.glass_bottle);
         }

         player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
      }

      return stack;
   }
}
