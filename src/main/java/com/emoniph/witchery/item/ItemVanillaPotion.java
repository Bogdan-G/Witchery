package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemVanillaPotion extends ItemBase {

   public final ArrayList subItems = new ArrayList();
   public final ItemVanillaPotion.SubItem potionAntidote;
   @SideOnly(Side.CLIENT)
   private IIcon field_94591_c;
   @SideOnly(Side.CLIENT)
   private IIcon field_94590_d;
   @SideOnly(Side.CLIENT)
   private IIcon field_94592_ct;


   public ItemVanillaPotion() {
      this.potionAntidote = ItemVanillaPotion.SubItem.register(new ItemVanillaPotion.SubItem(0, "antidote", '\uff00', null) {
         public void onDrunk(World world, EntityPlayer player, ItemStack stack) {
            if(player != null && world != null && !world.isRemote) {
               if(player.isPotionActive(Potion.poison)) {
                  player.removePotionEffect(Potion.poison.id);
               }

               if(player.isPotionActive(Potion.wither)) {
                  player.removePotionEffect(Potion.wither.id);
               }
            }

         }
      }, this.subItems);
      this.setMaxStackSize(4);
      this.setHasSubtypes(true);
      this.setMaxDamage(0);
   }

   public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
      if(!player.capabilities.isCreativeMode) {
         --stack.stackSize;
      }

      if(!world.isRemote) {
         int damage = stack.getItemDamage();
         if(damage >= 0 && damage < this.subItems.size()) {
            ((ItemVanillaPotion.SubItem)this.subItems.get(damage)).onDrunk(world, player, stack);
         }
      }

      if(!player.capabilities.isCreativeMode) {
         if(stack.stackSize <= 0) {
            return new ItemStack(Items.glass_bottle);
         }

         player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
      }

      return stack;
   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 32;
   }

   public EnumAction getItemUseAction(ItemStack par1ItemStack) {
      return EnumAction.drink;
   }

   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
      return par1ItemStack;
   }

   public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int par1) {
      return this.field_94590_d;
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
      return par2 == 0?this.field_94592_ct:super.getIconFromDamageForRenderPass(par1, par2);
   }

   public String getUnlocalizedName(ItemStack itemStack) {
      int damage = itemStack.getItemDamage();

      assert damage >= 0 && damage < this.subItems.size() : "damage value is too large";

      return super.getUnlocalizedName() + "." + ((ItemVanillaPotion.SubItem)this.subItems.get(Math.max(damage, 0))).unlocalizedName;
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromDamage(int damage) {
      return damage >= 0 && damage < this.subItems.size()?((ItemVanillaPotion.SubItem)this.subItems.get(damage)).color:0;
   }

   @SideOnly(Side.CLIENT)
   public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
      return par2 > 0?16777215:this.getColorFromDamage(par1ItemStack.getItemDamage());
   }

   @SideOnly(Side.CLIENT)
   public boolean requiresMultipleRenderPasses() {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public boolean hasEffect(ItemStack par1ItemStack) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs creativeTabs, List itemList) {
      Iterator i$ = this.subItems.iterator();

      while(i$.hasNext()) {
         ItemVanillaPotion.SubItem subItem = (ItemVanillaPotion.SubItem)i$.next();
         if(subItem.showInCreativeTab) {
            itemList.add(subItem.createStack());
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      this.field_94590_d = par1IconRegister.registerIcon(this.getIconString() + "_" + "bottle_drinkable");
      this.field_94591_c = par1IconRegister.registerIcon(this.getIconString() + "_" + "bottle_splash");
      this.field_94592_ct = par1IconRegister.registerIcon(this.getIconString() + "_" + "overlay");
   }


   public static class SubItem {

      public final int damageValue;
      private final String unlocalizedName;
      private final int rarity;
      private final boolean showInCreativeTab;
      private final int color;
      @SideOnly(Side.CLIENT)
      private IIcon icon;
      // $FF: synthetic field
      static final boolean $assertionsDisabled = !ItemVanillaPotion.class.desiredAssertionStatus();


      private static ItemVanillaPotion.SubItem register(ItemVanillaPotion.SubItem subItem, ArrayList subItems) {
         if(!$assertionsDisabled && subItems.size() != subItem.damageValue) {
            throw new AssertionError("Misalignement with subItem registration");
         } else {
            subItems.add(subItem);
            return subItem;
         }
      }

      private SubItem(int damageValue, String unlocalizedName, int color) {
         this(damageValue, unlocalizedName, color, 0, true);
      }

      private SubItem(int damageValue, String unlocalizedName, int color, int rarity) {
         this(damageValue, unlocalizedName, color, rarity, true);
      }

      private SubItem(int damageValue, String unlocalizedName, int color, int rarity, boolean showInCreativeTab) {
         this.damageValue = damageValue;
         this.unlocalizedName = unlocalizedName;
         this.rarity = rarity;
         this.showInCreativeTab = showInCreativeTab;
         this.color = color;
      }

      @SideOnly(Side.CLIENT)
      private void registerIcon(IIconRegister iconRegister, ItemVanillaPotion itemIngredient) {
         this.icon = iconRegister.registerIcon(itemIngredient.getIconString() + "." + this.unlocalizedName);
      }

      public boolean isMatch(ItemStack itemstack) {
         return itemstack != null && Witchery.Items.POTIONS == itemstack.getItem() && itemstack.getItemDamage() == this.damageValue;
      }

      public ItemStack createStack(int stackSize) {
         return new ItemStack(Witchery.Items.POTIONS, stackSize, this.damageValue);
      }

      public ItemStack createStack() {
         return this.createStack(1);
      }

      public boolean isItemInInventory(InventoryPlayer inventory) {
         return this.getItemSlotFromInventory(inventory) != -1;
      }

      public int getItemSlotFromInventory(InventoryPlayer inventory) {
         for(int k = 0; k < inventory.mainInventory.length; ++k) {
            if(inventory.mainInventory[k] != null && inventory.mainInventory[k].getItem() == Witchery.Items.POTIONS && inventory.mainInventory[k].getItemDamage() == this.damageValue) {
               return k;
            }
         }

         return -1;
      }

      public boolean consumeItemFromInventory(InventoryPlayer inventory) {
         int j = this.getItemSlotFromInventory(inventory);
         if(j < 0) {
            return false;
         } else {
            if(--inventory.mainInventory[j].stackSize <= 0) {
               inventory.mainInventory[j] = null;
            }

            return true;
         }
      }

      public void onDrunk(World world, EntityPlayer player, ItemStack stack) {}

      // $FF: synthetic method
      SubItem(int x0, String x1, int x2, Object x3) {
         this(x0, x1, x2);
      }

   }
}
