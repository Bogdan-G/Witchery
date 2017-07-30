package com.emoniph.witchery.item;

import com.emoniph.witchery.blocks.BlockPoppetShelf;
import com.emoniph.witchery.item.ItemBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemPoppetShelfCompass extends ItemBase {

   @SideOnly(Side.CLIENT)
   private IIcon[] icons;


   public ItemPoppetShelfCompass() {
      this.setMaxDamage(0);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      this.icons = new IIcon[6];

      for(int i = 0; i < this.icons.length; ++i) {
         this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
      }

      super.itemIcon = this.icons[0];
   }

   public IIcon getIconFromDamage(int damageValue) {
      return damageValue > 0 && damageValue < this.icons.length?this.icons[damageValue]:this.icons[0];
   }

   public void onUpdate(ItemStack stack, World world, Entity entity, int inventorySlot, boolean isHeldItem) {
      if(world.isRemote && world.rand.nextInt(20) == 0) {
         List list = world.loadedTileEntityList;
         double closest = Double.MAX_VALUE;
         Iterator i$ = list.iterator();

         while(i$.hasNext()) {
            TileEntity tile = (TileEntity)i$.next();
            if(tile instanceof BlockPoppetShelf.TileEntityPoppetShelf) {
               double distSq = entity.getDistanceSq((double)tile.xCoord, entity.posY, (double)tile.zCoord);
               if(distSq < closest) {
                  closest = distSq;
               }
            }
         }

         if(closest < 64.0D) {
            stack.setItemDamage(5);
         } else if(closest < 256.0D) {
            stack.setItemDamage(4);
         } else if(closest < 1024.0D) {
            stack.setItemDamage(3);
         } else if(closest < 4096.0D) {
            stack.setItemDamage(2);
         } else if(closest < 16384.0D) {
            stack.setItemDamage(1);
         } else {
            stack.setItemDamage(0);
         }
      }

   }

   public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
      item.setItemDamage(0);
      return super.onDroppedByPlayer(item, player);
   }
}
