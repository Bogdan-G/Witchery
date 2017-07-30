package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class ItemBook extends ItemBase {

   static final String CURRENT_PAGE_KEY = "CurrentPage";
   public static final Type[] BIOME_TYPES = new Type[]{Type.BEACH, Type.FOREST, Type.HILLS, Type.MESA, Type.MOUNTAIN, Type.PLAINS, Type.SANDY, Type.SNOWY, Type.SWAMP, Type.WASTELAND, Type.RIVER, Type.OCEAN, Type.SPOOKY, Type.MAGICAL};


   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      int posX = MathHelper.floor_double(player.posX);
      int posY = MathHelper.floor_double(player.posY);
      int posZ = MathHelper.floor_double(player.posZ);
      player.openGui(Witchery.instance, 6, world, posX, posY, posZ);
      return stack;
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean expandedTooltip) {
      list.add(String.format(Witchery.resource("witchery.biomebook.currentpage"), new Object[]{getSelectedBiome(getSelectedBiome(stack, 1000)).biomeName}));
      list.add("");
      String[] arr$ = Witchery.resource("item.witchery:biomebook2.tip").split("\n");
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         String s = arr$[i$];
         if(!s.isEmpty()) {
            list.add(s);
         }
      }

   }

   public static int getSelectedBiome(ItemStack stack, int maxPages) {
      NBTTagCompound stackCompound = stack.getTagCompound();
      return stackCompound != null && stackCompound.hasKey("CurrentPage")?Math.min(Math.max(stackCompound.getInteger("CurrentPage"), 0), Math.max(maxPages, 1) - 1):0;
   }

   public static BiomeGenBase getSelectedBiome(int page) {
      new ArrayList();
      int i = 0;
      Type[] arr$ = BIOME_TYPES;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Type biomeType = arr$[i$];
         BiomeGenBase[] biomesInType = BiomeDictionary.getBiomesForType(biomeType);

         for(int j = 0; j < biomesInType.length; ++j) {
            if(i++ == page) {
               return biomesInType[j];
            }
         }
      }

      return BiomeGenBase.plains;
   }

   public ItemStack getContainerItem(ItemStack stack) {
      return !this.hasContainerItem(stack)?null:stack.copy();
   }

   public static void setSelectedBiome(ItemStack itemstack, int pageIndex) {
      if(itemstack.getTagCompound() == null) {
         itemstack.setTagCompound(new NBTTagCompound());
      }

      itemstack.getTagCompound().setInteger("CurrentPage", pageIndex);
   }

}
