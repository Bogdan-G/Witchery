package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.util.ItemUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBase extends Item {

   protected boolean registerWithCreativeTab = true;
   protected boolean autoGenerateTooltip = false;


   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      if(this.registerWithCreativeTab) {
         this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
      }

      return super.setUnlocalizedName(itemName);
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean moreTips) {
      if(this.autoGenerateTooltip) {
         String localText = Witchery.resource(this.getUnlocalizedName() + ".tip");
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
}
