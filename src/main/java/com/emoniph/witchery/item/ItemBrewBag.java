package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.EntityBrew;
import com.emoniph.witchery.brewing.WitcheryBrewRegistry;
import com.emoniph.witchery.infusion.infusions.symbols.StrokeSet;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.network.PacketBrewPrepared;
import com.emoniph.witchery.util.ChatUtil;
import com.emoniph.witchery.util.Dye;
import com.emoniph.witchery.util.SoundEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemBrewBag extends ItemBase {

   private static final float THRESHOLD_ORTHOGONAL = 7.0F;
   private static final float THRESHOLD_DIAGONAL = 3.5F;


   public ItemBrewBag() {
      this.setMaxStackSize(1);
      this.setFull3D();
   }

   public int getColorFromItemStack(ItemStack stack, int parse) {
      return getColor(stack);
   }

   public static void setColor(ItemStack stack, Dye color) {
      setColor(stack, color.rgb);
   }

   public static void setColor(ItemStack stack, int rgb) {
      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      NBTTagCompound nbtRoot = stack.getTagCompound();
      nbtRoot.setInteger("WITCColor", rgb);
   }

   public static int getColor(ItemStack stack) {
      if(stack.hasTagCompound()) {
         NBTTagCompound nbtRoot = stack.getTagCompound();
         if(nbtRoot.hasKey("WITCColor")) {
            return nbtRoot.getInteger("WITCColor");
         }
      }

      return Dye.COCOA_BEANS.rgb;
   }

   public int getMaxItemUseDuration(ItemStack stack) {
      return '\u8ca0';
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      if(!player.isSneaking()) {
         NBTTagCompound nbtTag = player.getEntityData();
         nbtTag.setByteArray("Strokes", new byte[0]);
         nbtTag.setFloat("startPitch", player.rotationPitch);
         nbtTag.setFloat("startYaw", player.rotationYawHead);
         nbtTag.removeTag("WITCLastBrewIndex");
         player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      } else if(!world.isRemote && player.isSneaking()) {
         player.openGui(Witchery.instance, 5, world, 0, 0, 0);
      }

      return stack;
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int countdown) {
      NBTTagCompound nbtTag;
      if(player.worldObj.isRemote) {
         nbtTag = player.getEntityData();
         if(nbtTag == null) {
            return;
         }

         if(player.isSneaking()) {
            nbtTag.removeTag("Strokes");
            nbtTag.setFloat("startPitch", player.rotationPitch);
            nbtTag.setFloat("startYaw", player.rotationYawHead);
            return;
         }

         float yawDiff = nbtTag.getFloat("startYaw") - player.rotationYawHead;
         float pitchDiff = nbtTag.getFloat("startPitch") - player.rotationPitch;
         byte[] oldStrokes = nbtTag.getByteArray("Strokes");
         byte[] strokes = oldStrokes;
         int strokesStart = oldStrokes.length;
         if(strokesStart == 0) {
            if(pitchDiff >= 3.5F && yawDiff <= -3.5F) {
               strokes = this.addNewStroke(nbtTag, oldStrokes, (byte)4);
            } else if(pitchDiff >= 3.5F && yawDiff >= 3.5F) {
               strokes = this.addNewStroke(nbtTag, oldStrokes, (byte)6);
            } else if(pitchDiff <= -3.5F && yawDiff <= -3.5F) {
               strokes = this.addNewStroke(nbtTag, oldStrokes, (byte)7);
            } else if(pitchDiff <= -3.5F && yawDiff >= 3.5F) {
               strokes = this.addNewStroke(nbtTag, oldStrokes, (byte)5);
            } else if(pitchDiff >= 7.0F) {
               strokes = this.addNewStroke(nbtTag, oldStrokes, (byte)0);
            } else if(pitchDiff <= -7.0F) {
               strokes = this.addNewStroke(nbtTag, oldStrokes, (byte)1);
            } else if(yawDiff <= -7.0F) {
               strokes = this.addNewStroke(nbtTag, oldStrokes, (byte)2);
            } else if(yawDiff >= 7.0F) {
               strokes = this.addNewStroke(nbtTag, oldStrokes, (byte)3);
            }

            if(strokes.length > strokesStart) {
               nbtTag.setFloat("startPitch", player.rotationPitch);
               nbtTag.setFloat("startYaw", player.rotationYawHead);
            }

            if(oldStrokes != strokes && strokes.length > 0) {
               int brewIndex = StrokeSet.Stroke.STROKE_TO_INDEX[strokes[0]];
               ItemBrewBag.InventoryBrewBag inv = new ItemBrewBag.InventoryBrewBag(player);
               ItemStack brew = inv.getStackInSlot(brewIndex);
               if(brew != null) {
                  Witchery.packetPipeline.sendToServer(new PacketBrewPrepared(brewIndex));
               } else {
                  nbtTag.removeTag("Strokes");
               }
            }
         }
      } else if(player.isSneaking()) {
         nbtTag = player.getEntityData();
         if(nbtTag.hasKey("WITCLastBrewIndex")) {
            nbtTag.removeTag("WITCLastBrewIndex");
         }
      }

   }

   public byte[] addNewStroke(NBTTagCompound nbtTag, byte[] strokes, byte stroke) {
      byte[] newStrokes = new byte[1];
      newStrokes[newStrokes.length - 1] = stroke;
      nbtTag.setByteArray("Strokes", newStrokes);
      return newStrokes;
   }

   public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int countdown) {
      NBTTagCompound nbtTag = player.getEntityData();
      if(nbtTag != null) {
         if(!world.isRemote && nbtTag.hasKey("WITCLastBrewIndex")) {
            int brewIndex = nbtTag.getInteger("WITCLastBrewIndex");
            nbtTag.removeTag("WITCLastBrewIndex");
            if(!player.isSneaking()) {
               if(brewIndex > -1) {
                  ItemBrewBag.InventoryBrewBag inv = new ItemBrewBag.InventoryBrewBag(player);
                  ItemStack brew = inv.getStackInSlot(brewIndex);
                  if(brew != null) {
                     if(brew.getItem() == Witchery.Items.BREW) {
                        if(!player.capabilities.isCreativeMode) {
                           --brew.stackSize;
                        }

                        world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (Item.itemRand.nextFloat() * 0.4F + 0.8F));
                        if(!world.isRemote) {
                           world.spawnEntityInWorld(new EntityBrew(world, player, brew, false));
                        }
                     } else {
                        Witchery.Items.GENERIC.throwBrew(brew, world, player);
                     }

                     if(brew.stackSize == 0) {
                        inv.setInventorySlotContents(brewIndex, (ItemStack)null);
                     }

                     inv.writeToNBT();
                  }
               } else {
                  ChatUtil.sendTranslated(EnumChatFormatting.RED, player, "witchery.infuse.branch.unknownsymbol", new Object[0]);
                  SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
               }
            }
         } else {
            nbtTag.removeTag("Strokes");
            nbtTag.removeTag("startYaw");
            nbtTag.removeTag("startPitch");
         }
      }

   }

   public static class ContainerBrewBag extends Container {

      private int numRows;
      private ItemStack bag;


      public ContainerBrewBag(IInventory playerInventory, IInventory bagInventory, ItemStack bag) {
         this.numRows = bagInventory.getSizeInventory() / 8;
         bagInventory.openInventory();
         int offset = (this.numRows - 4) * 18;

         int col;
         int col1;
         for(col = 0; col < this.numRows; ++col) {
            for(col1 = 0; col1 < 8; ++col1) {
               this.addSlotToContainer(new ItemBrewBag.SlotBrewBag(bagInventory, col1 + col * 8, 16 + col1 * 18, 18 + col * 18));
            }
         }

         for(col = 0; col < 3; ++col) {
            for(col1 = 0; col1 < 9; ++col1) {
               this.addSlotToContainer(new Slot(playerInventory, col1 + col * 9 + 9, 8 + col1 * 18, 103 + col * 18 + offset));
            }
         }

         for(col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(playerInventory, col, 8 + col * 18, 161 + offset));
         }

         this.bag = bag;
      }

      public boolean canInteractWith(EntityPlayer player) {
         ItemStack itemStack = null;
         if(player.getCurrentEquippedItem() != null) {
            itemStack = player.getCurrentEquippedItem();
         }

         return itemStack != null && itemStack.getItem() == Witchery.Items.BREW_BAG;
      }

      public ItemStack transferStackInSlot(EntityPlayer player, int index) {
         ItemStack returnStack = null;
         Slot slot = (Slot)super.inventorySlots.get(index);
         if(slot != null && slot.getHasStack()) {
            ItemStack itemStack = slot.getStack();
            if(!ItemBrewBag.SlotBrewBag.isBrew(itemStack)) {
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
   }

   private static class SlotBrewBag extends Slot {

      public SlotBrewBag(IInventory inventory, int slot, int x, int y) {
         super(inventory, slot, x, y);
      }

      public boolean isItemValid(ItemStack stack) {
         return isBrew(stack);
      }

      public static boolean isBrew(ItemStack stack) {
         return stack != null && (Witchery.Items.GENERIC.isBrew(stack) || Witchery.Items.BREW == stack.getItem() && WitcheryBrewRegistry.INSTANCE.isSplash(stack.getTagCompound()));
      }
   }

   public static class InventoryBrewBag extends InventoryBasic {

      protected String title;
      protected EntityPlayer player;
      protected boolean locked = false;


      public InventoryBrewBag(EntityPlayer player) {
         super("", false, 8);
         this.player = player;
         if(!this.hasInventory()) {
            this.createInventory();
         }

         this.readFromNBT();
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
         ItemStack bag = this.player.getHeldItem();
         return bag != null && bag.hasTagCompound() && bag.getTagCompound().hasKey("Inventory");
      }

      protected void createInventory() {
         ItemStack bag = this.player.getHeldItem();
         this.title = new String(bag.getDisplayName());
         this.writeToNBT();
      }

      protected void writeToNBT() {
         ItemStack bag = this.player.getHeldItem();
         if(bag != null && bag.getItem() == Witchery.Items.BREW_BAG) {
            if(!bag.hasTagCompound()) {
               bag.setTagCompound(new NBTTagCompound());
            }

            NBTTagCompound nbtRoot = bag.getTagCompound();
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
         ItemStack bag = this.player.getHeldItem();
         if(bag != null && bag.getItem() == Witchery.Items.BREW_BAG && bag.hasTagCompound()) {
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
