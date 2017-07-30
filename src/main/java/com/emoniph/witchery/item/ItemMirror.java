package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockMirror;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.Config;
import com.emoniph.witchery.util.Coord;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

public class ItemMirror extends ItemBase {

   public ItemMirror() {
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advTooltips) {
      super.addInformation(stack, player, list, advTooltips);
      NBTTagCompound nbtRoot = stack.getTagCompound();
      if(nbtRoot != null && nbtRoot.getBoolean("DemonSlain")) {
         if(!Config.instance().isDebugging()) {
            list.add(Witchery.resource("item.witchery:mirror.tip.bridge"));
         }
      } else {
         list.add(Witchery.resource("item.witchery:mirror.tip.inhabited"));
      }

      if(Config.instance().isDebugging() && nbtRoot != null && nbtRoot.hasKey("DimCoords")) {
         int dim = nbtRoot.getInteger("Dimension");
         Coord coord = Coord.fromTagNBT(nbtRoot.getCompoundTag("DimCoords"));
         WorldProvider prov = WorldProvider.getProviderForDimension(dim);
         String dimName = prov != null?prov.getDimensionName():Integer.valueOf(dim).toString();
         list.add(String.format(Witchery.resource("item.witchery:mirror.tip.bridgeplus"), new Object[]{dimName, Integer.valueOf(coord.x), Integer.valueOf(coord.y), Integer.valueOf(coord.z)}));
      }

   }

   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
      if(world.isRemote) {
         return true;
      } else {
         byte meta = 0;
         BlockMirror mirror = Witchery.Blocks.MIRROR;
         switch(side) {
         case 0:
         case 1:
         default:
            break;
         case 2:
            meta = 0;
            --z;
            break;
         case 3:
            meta = 1;
            ++z;
            break;
         case 4:
            meta = 2;
            --x;
            break;
         case 5:
            meta = 3;
            ++x;
         }

         if(player.canPlayerEdit(x, y, z, side, stack) && player.canPlayerEdit(x, y - 1, z, side, stack)) {
            if(world.isAirBlock(x, y, z) && world.isAirBlock(x, y - 1, z)) {
               world.setBlock(x, y, z, mirror, meta | 4, 3);
               if(world.getBlock(x, y, z) == mirror) {
                  world.setBlock(x, y - 1, z, mirror, meta, 3);
                  Witchery.Blocks.MIRROR.loadFromItem(stack, player, world, x, y, z);
               }

               --stack.stackSize;
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }
}
