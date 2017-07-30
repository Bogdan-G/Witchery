package com.emoniph.witchery.network;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBook;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PacketItemUpdate implements IMessage {

   private int slot;
   private int damage;
   private int page;


   public PacketItemUpdate() {}

   public PacketItemUpdate(int slot, int page, ItemStack stack) {
      this.slot = slot;
      this.damage = stack.getItemDamage();
      this.page = page;
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.slot);
      buffer.writeInt(this.damage);
      buffer.writeInt(this.page);
   }

   public void fromBytes(ByteBuf buffer) {
      this.slot = buffer.readInt();
      this.damage = buffer.readInt();
      this.page = buffer.readInt();
   }

   public static class Handler implements IMessageHandler<PacketItemUpdate, IMessage> {

      public IMessage onMessage(PacketItemUpdate message, MessageContext ctx) {
         EntityPlayer player = Witchery.proxy.getPlayer(ctx);
         if(message.slot >= 0 && message.slot < player.inventory.getSizeInventory()) {
            ItemStack stack = player.inventory.getStackInSlot(message.slot);
            if(stack != null && stack.getItemDamage() == message.damage && message.page >= 0 && message.page < 1000) {
               if(Witchery.Items.GENERIC.isBook(stack)) {
                  if(!stack.hasTagCompound()) {
                     stack.setTagCompound(new NBTTagCompound());
                  }

                  stack.getTagCompound().setInteger("CurrentPage", message.page);
               } else if(stack.getItem() == Witchery.Items.BIOME_BOOK) {
                  ItemBook.setSelectedBiome(stack, message.page);
               }
            }
         }

         return null;
      }
   }
}
