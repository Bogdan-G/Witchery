package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.Config;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemEntityLocator extends ItemBase {

   @SideOnly(Side.CLIENT)
   private IIcon[] icons;


   public ItemEntityLocator() {
      this.setMaxDamage(0);
      this.setMaxStackSize(1);
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      this.icons = new IIcon[33];

      for(int i = 0; i < this.icons.length; ++i) {
         this.icons[i] = iconRegister.registerIcon(this.getIconString() + i);
      }

      super.itemIcon = this.icons[0];
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advTooltips) {
      super.addInformation(stack, player, list, advTooltips);
      String entityID = Witchery.Items.TAGLOCK_KIT.getBoundEntityDisplayName(stack, Integer.valueOf(1));
      if(entityID != null && !entityID.isEmpty()) {
         list.add(String.format(Witchery.resource("item.witcheryTaglockKit.boundto"), new Object[]{entityID}));
      } else {
         list.add(Witchery.resource("item.witcheryTaglockKit.unbound"));
      }

   }

   public IIcon getIconFromDamage(int damageValue) {
      return damageValue > 0 && damageValue < this.icons.length?this.icons[damageValue]:this.icons[0];
   }

   public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
      item.setItemDamage(0);
      return super.onDroppedByPlayer(item, player);
   }

   public void onUpdate(ItemStack stack, World world, Entity player, int inventorySlot, boolean isHeldItem) {
      if(world != null && world.isRemote && world.getWorldTime() % 10L == 2L) {
         if(Witchery.Items.TAGLOCK_KIT.isTaglockPresent(stack, Integer.valueOf(1))) {
            double d3 = 0.0D;
            EntityLivingBase target = Witchery.Items.TAGLOCK_KIT.getBoundEntity(world, (Entity)null, stack, Integer.valueOf(1));
            if(target != null && (target.dimension == player.dimension || target.dimension == 0 && player.dimension == Config.instance().dimensionDreamID)) {
               double i = player.posX;
               double playerZ = player.posZ;
               double d4 = target.posX - i;
               double d5 = target.posZ - playerZ;
               double playerYaw = (double)player.rotationYaw;
               playerYaw %= 360.0D;
               d3 = -((playerYaw - 90.0D) * 3.141592653589793D / 180.0D - Math.atan2(d5, d4));
            } else {
               d3 = Math.random() * 3.141592653589793D * 2.0D;
            }

            int SIZE = this.icons.length - 1;

            int i1;
            for(i1 = (int)((d3 / 6.283185307179586D + 1.0D) * (double)SIZE) % SIZE; i1 < 0; i1 = (i1 + SIZE) % SIZE) {
               ;
            }

            stack.setItemDamage(i1 + 1);
         } else {
            stack.setItemDamage(0);
         }
      }

   }
}
