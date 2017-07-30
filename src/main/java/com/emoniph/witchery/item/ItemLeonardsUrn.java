package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.item.ItemBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemLeonardsUrn extends ItemBase {

   @SideOnly(Side.CLIENT)
   private IIcon[] icons;


   public ItemLeonardsUrn() {
      this.setMaxStackSize(1);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips) {
      if(stack != null) {
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

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int damageValue) {
      return this.icons[Math.min(damageValue, this.icons.length - 1)];
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      this.icons = new IIcon[4];

      for(int i = 0; i < this.icons.length; ++i) {
         this.icons[i] = iconRegister.registerIcon(this.getIconString() + i);
      }

      super.itemIcon = this.icons[0];
   }

   public void getSubItems(Item item, CreativeTabs tab, List items) {
      for(int i = 0; i < 4; ++i) {
         items.add(new ItemStack(this, 1, i));
      }

   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if(!world.isRemote) {
         player.openGui(Witchery.instance, 8, world, 0, 0, 0);
      }

      return stack;
   }

   @SideOnly(Side.CLIENT)
   public boolean hasEffect(ItemStack stack) {
      return true;
   }

   public EnumRarity getRarity(ItemStack stack) {
      return EnumRarity.epic;
   }

   private static class SlotLeonardsUrn extends Slot {

      public SlotLeonardsUrn(IInventory inventory, int slot, int x, int y) {
         super(inventory, slot, x, y);
      }

      public boolean isItemValid(ItemStack stack) {
         return isBrew(stack);
      }

      public static boolean isBrew(ItemStack stack) {
         return stack != null && Witchery.Items.BREW == stack.getItem() && WitcheryBrewRegistry.INSTANCE.isSplash(stack.getTagCompound());
      }
   }

   public static class ContainerLeonardsUrn extends Container {

      private int numRows = 1;
      private ItemStack bag;


      public ContainerLeonardsUrn(IInventory playerInventory, IInventory bagInventory, ItemStack bag) {
         bagInventory.openInventory();
         int size = bagInventory.getSizeInventory();
         int slot = 0;
         this.addSlotToContainer(new ItemLeonardsUrn.SlotLeonardsUrn(bagInventory, slot++, 80, 22));
         if(size >= 2) {
            this.addSlotToContainer(new ItemLeonardsUrn.SlotLeonardsUrn(bagInventory, slot++, 80, 70));
         }

         if(size >= 3) {
            this.addSlotToContainer(new ItemLeonardsUrn.SlotLeonardsUrn(bagInventory, slot++, 103, 46));
         }

         if(size >= 4) {
            this.addSlotToContainer(new ItemLeonardsUrn.SlotLeonardsUrn(bagInventory, slot++, 56, 46));
         }

         int col;
         for(col = 0; col < 3; ++col) {
            for(int col1 = 0; col1 < 9; ++col1) {
               this.addSlotToContainer(new Slot(playerInventory, col1 + col * 9 + 9, 8 + col1 * 18, 103 + col * 18));
            }
         }

         for(col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(playerInventory, col, 8 + col * 18, 161));
         }

         this.bag = bag;
      }

      public boolean canInteractWith(EntityPlayer player) {
         ItemStack itemStack = null;
         if(player.getCurrentEquippedItem() != null) {
            itemStack = player.getCurrentEquippedItem();
         }

         return itemStack != null && itemStack.getItem() == Witchery.Items.LEONARDS_URN;
      }

      public ItemStack transferStackInSlot(EntityPlayer player, int index) {
         ItemStack returnStack = null;
         Slot slot = (Slot)super.inventorySlots.get(index);
         if(slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            if(!ItemLeonardsUrn.SlotLeonardsUrn.isBrew(itemStack)) {
               return returnStack;
            }

            returnStack = itemStack.copy();
            if(index < this.numRows * 9) {
               if(!this.mergeItemStack(itemStack, this.numRows * 9, super.inventorySlots.size(), true)) {
                  return null;
               }
            } else if(!this.mergeItemStack(itemStack, 0, this.numRows * 9, false)) {
               return null;
            }

            if(itemStack.stackSize == 0) {
               slot.putStack((ItemStack)null);
            } else {
               slot.onSlotChanged();
            }
         }

         return returnStack;
      }

      protected boolean mergeItemStack(ItemStack stack, int slotCount, int invSize, boolean upper) {
         boolean flag1 = false;
         int k = slotCount;
         if(upper) {
            k = invSize - 1;
         }

         int maxStackSize = upper?stack.getMaxStackSize():1;
         Slot slot;
         ItemStack itemstack1;
         if(stack.isStackable()) {
            while(stack.stackSize > 0 && (!upper && k < invSize || upper && k >= slotCount)) {
               slot = (Slot)super.inventorySlots.get(k);
               itemstack1 = slot.getStack();
               if(itemstack1 != null && itemstack1.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, itemstack1)) {
                  int l = itemstack1.stackSize + stack.stackSize;
                  if(l <= maxStackSize) {
                     stack.stackSize = 0;
                     itemstack1.stackSize = l;
                     slot.onSlotChanged();
                     flag1 = true;
                  } else if(itemstack1.stackSize < maxStackSize) {
                     stack.stackSize -= maxStackSize - itemstack1.stackSize;
                     itemstack1.stackSize = maxStackSize;
                     slot.onSlotChanged();
                     flag1 = true;
                  }
               }

               if(upper) {
                  --k;
               } else {
                  ++k;
               }
            }
         }

         if(stack.stackSize > 0) {
            if(upper) {
               k = invSize - 1;
            } else {
               k = slotCount;
            }

            while(!upper && k < invSize || upper && k >= slotCount) {
               slot = (Slot)super.inventorySlots.get(k);
               itemstack1 = slot.getStack();
               if(itemstack1 == null) {
                  slot.putStack(upper?stack.copy():stack.splitStack(1));
                  slot.onSlotChanged();
                  if(upper) {
                     stack.stackSize = 0;
                  }

                  flag1 = true;
                  break;
               }

               if(upper) {
                  --k;
               } else {
                  ++k;
               }
            }
         }

         return flag1;
      }
   }

   public static class InventoryLeonardsUrn extends InventoryBasic {

      protected String title;
      protected EntityPlayer player;
      protected ItemStack urnStack;
      protected boolean locked;


      public InventoryLeonardsUrn(EntityPlayer player) {
         this(player, (ItemStack)null);
      }

      public InventoryLeonardsUrn(EntityPlayer player, ItemStack stack) {
         super("", false, stack != null?stack.getItemDamage() + 1:(player.getHeldItem() != null?player.getHeldItem().getItemDamage() + 1:1));
         this.locked = false;
         this.urnStack = stack;
         this.player = player;
         if(!this.hasInventory()) {
            this.createInventory();
         }

         this.readFromNBT();
      }

      public int getInventoryStackLimit() {
         return 1;
      }

      public void markDirty() {
         super.markDirty();
         if(!this.locked) {
            this.writeToNBT();
         }

      }

      public void openInventory() {
         this.readFromNBT();
      }

      public void closeInventory() {
         this.writeToNBT();
      }

      public String getInventoryName() {
         return this.title;
      }

      protected boolean hasInventory() {
         return this.urnStack != null?this.urnStack != null && this.urnStack.hasTagCompound() && this.urnStack.getTagCompound().hasKey("Inventory"):this.player.getHeldItem() != null && this.player.getHeldItem().hasTagCompound() && this.player.getHeldItem().getTagCompound().hasKey("Inventory");
      }

      protected void createInventory() {
         this.title = new String(this.urnStack != null?this.urnStack.getDisplayName():this.player.getHeldItem().getDisplayName());
         this.writeToNBT();
      }

      protected void writeToNBT() {
         ItemStack urnStack = this.urnStack != null?this.urnStack:this.player.getHeldItem();
         if(urnStack != null && urnStack.getItem() == Witchery.Items.LEONARDS_URN) {
            if(!urnStack.hasTagCompound()) {
               urnStack.setTagCompound(new NBTTagCompound());
            }

            NBTTagCompound nbtRoot = urnStack.getTagCompound();
            NBTTagCompound name = new NBTTagCompound();
            name.setString("Name", this.getInventoryName());
            nbtRoot.setTag("display", name);
            NBTTagList itemList = new NBTTagList();

            for(int inventory = 0; inventory < this.getSizeInventory(); ++inventory) {
               if(this.getStackInSlot(inventory) != null) {
                  NBTTagCompound slotEntry = new NBTTagCompound();
                  slotEntry.setByte("Slot", (byte)inventory);
                  this.getStackInSlot(inventory).writeToNBT(slotEntry);
                  itemList.appendTag(slotEntry);
               }
            }

            NBTTagCompound var7 = new NBTTagCompound();
            var7.setTag("Items", itemList);
            nbtRoot.setTag("Inventory", var7);
         }
      }

      protected void readFromNBT() {
         this.locked = true;
         ItemStack bag = this.urnStack != null?this.urnStack:this.player.getHeldItem();
         if(bag != null && bag.getItem() == Witchery.Items.LEONARDS_URN && bag.hasTagCompound()) {
            NBTTagCompound nbtRoot = bag.getTagCompound();
            this.title = nbtRoot.getCompoundTag("display").getString("Name");
            NBTTagList itemList = nbtRoot.getCompoundTag("Inventory").getTagList("Items", 10);

            for(int i = 0; i < itemList.tagCount(); ++i) {
               NBTTagCompound slotEntry = itemList.getCompoundTagAt(i);
               int j = slotEntry.getByte("Slot") & 255;
               if(j >= 0 && j < this.getSizeInventory()) {
                  this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(slotEntry));
               }
            }
         }

         this.locked = false;
      }
   }
}
