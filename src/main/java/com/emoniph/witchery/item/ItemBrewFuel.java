package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBase;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemBrewFuel extends ItemBase implements IFuelHandler {

   @SideOnly(Side.CLIENT)
   protected IIcon itemIconOverlay;
   private static final int[] COLORS = new int[]{16754270, 16748088, 16740620, 14702848};
   private static final int[] BURN_TIMES = new int[]{2400, 5000, 10000, '\uc350'};


   public ItemBrewFuel() {
      this.setMaxStackSize(64);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public Item setUnlocalizedName(String itemName) {
      GameRegistry.registerFuelHandler(this);
      return super.setUnlocalizedName(itemName);
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
         int color = Math.min(stack.getItemDamage(), COLORS.length);
         return COLORS[color];
      } else {
         return super.getColorFromItemStack(stack, pass);
      }
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean expanded) {
      String localText = Witchery.resource("item.witchery:brew.fuel." + Math.min(stack.getItemDamage(), BURN_TIMES.length));
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

   public int getBurnTime(ItemStack fuel) {
      if(fuel.getItem() == this) {
         int burnTime = BURN_TIMES[Math.min(fuel.getItemDamage(), BURN_TIMES.length)];
         return burnTime;
      } else {
         return 0;
      }
   }

}
