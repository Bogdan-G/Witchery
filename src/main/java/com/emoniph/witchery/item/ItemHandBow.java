package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryCreativeTab;
import com.emoniph.witchery.common.ExtendedPlayer;
import com.emoniph.witchery.entity.EntityBolt;
import com.emoniph.witchery.item.ItemGeneral;
import com.emoniph.witchery.item.ItemHunterClothes;
import com.emoniph.witchery.util.EntityUtil;
import com.emoniph.witchery.util.InvUtil;
import com.emoniph.witchery.util.ItemUtil;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import com.emoniph.witchery.util.TransformCreature;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashSet;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemHandBow extends ItemBow {

   private static final int TICKS_TO_LOAD = 10;
   private static final String BOLT_TYPE_CURRENT = "WITCBoltTypeCurrent";
   private static final String BOLT_TYPE_PREFERRED = "WITCBoltTypePreferred";
   private static ItemGeneral.SubItem[] BOLT_TYPES = null;


   public ItemHandBow() {
      this.setMaxDamage(768);
      this.setFull3D();
      this.setCreativeTab(WitcheryCreativeTab.INSTANCE);
   }

   public Item setUnlocalizedName(String itemName) {
      ItemUtil.registerItem(this, itemName);
      return super.setUnlocalizedName(itemName);
   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      ItemGeneral.SubItem loadedBoltType = getBoltType("WITCBoltTypeCurrent", stack);
      ExtendedPlayer playerEx = ExtendedPlayer.get(player);
      if((loadedBoltType != null || player.isSneaking()) && playerEx.getCreatureType() == TransformCreature.NONE) {
         player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      }

      return stack;
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
      int elapsed = this.getMaxItemUseDuration(stack) - count;
      ItemGeneral.SubItem loadedBoltType = getBoltType("WITCBoltTypeCurrent", stack);
      if(player.isSneaking() && (elapsed == 5 || elapsed == 10 || elapsed == 15)) {
         int boltTypesCount = this.getBoltTypesInInventory(player.inventory, loadedBoltType);
         if(boltTypesCount > 0) {
            SoundEffect.WITCHERY_RANDOM_CLICK.playOnlyTo(player, 1.0F, 1.0F);
         }
      }

      super.onUsingTick(stack, player, count);
   }

   private int getBoltTypesInInventory(IInventory inventory, ItemGeneral.SubItem typeToIgnore) {
      HashSet typesFound = new HashSet();

      for(int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
         ItemStack stack = inventory.getStackInSlot(slot);
         ItemGeneral.BoltType boltType = ItemGeneral.BoltType.getBolt(stack);
         if(boltType != null && boltType != typeToIgnore) {
            typesFound.add(boltType);
         }
      }

      return typesFound.size();
   }

   public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int count) {
      int elapsed = this.getMaxItemUseDuration(stack) - count;
      ItemGeneral.SubItem loadedBoltType = getBoltType("WITCBoltTypeCurrent", stack);
      if(loadedBoltType != null && (!player.isSneaking() || elapsed < 10)) {
         if(this.launchBolt(stack, world, player, elapsed >= TimeUtil.secsToTicks(1)?20:19)) {
            this.setBoltType("WITCBoltTypeCurrent", stack, (ItemGeneral.SubItem)null);
         }
      } else if(player.isSneaking() && elapsed >= 10) {
         int boltTypesCount = this.getBoltTypesInInventory(player.inventory, loadedBoltType);
         ItemGeneral.SubItem preferredBoltType;
         ItemGeneral.SubItem boltTypeToUse;
         if(loadedBoltType != null && boltTypesCount > 0) {
            preferredBoltType = this.getNextBoltType(loadedBoltType);
            if(!InvUtil.hasItem(player.inventory, Witchery.Items.GENERIC, preferredBoltType.damageValue)) {
               preferredBoltType = null;
               boltTypeToUse = loadedBoltType;

               while((boltTypeToUse = this.getNextBoltType(boltTypeToUse)) != loadedBoltType) {
                  if(InvUtil.hasItem(player.inventory, Witchery.Items.GENERIC, boltTypeToUse.damageValue)) {
                     preferredBoltType = boltTypeToUse;
                     break;
                  }
               }
            }

            if(preferredBoltType != null) {
               this.setBoltType("WITCBoltTypeCurrent", stack, preferredBoltType);
               this.setBoltType("WITCBoltTypePreferred", stack, preferredBoltType);
               SoundEffect.WITCHERY_RANDOM_WINDUP.playOnlyTo(player, 1.0F, 1.0F);
               if(!player.capabilities.isCreativeMode) {
                  InvUtil.consumeItem(player.inventory, Witchery.Items.GENERIC, preferredBoltType.damageValue);
                  ItemStack boltTypeToUse1 = loadedBoltType.createStack();
                  if(!player.inventory.addItemStackToInventory(boltTypeToUse1)) {
                     EntityUtil.spawnEntityInWorld(world, new EntityItem(world, player.posX, player.posY, player.posZ, boltTypeToUse1));
                  }
               }
            }
         } else if(loadedBoltType == null && boltTypesCount > 0) {
            preferredBoltType = getBoltType("WITCBoltTypePreferred", stack);
            if(preferredBoltType == null) {
               preferredBoltType = Witchery.Items.GENERIC.itemBoltStake;
            }

            boltTypeToUse = preferredBoltType;
            if(!InvUtil.hasItem(player.inventory, Witchery.Items.GENERIC, preferredBoltType.damageValue)) {
               boltTypeToUse = null;
               ItemGeneral.SubItem currentBoltType = preferredBoltType;

               while((currentBoltType = this.getNextBoltType(currentBoltType)) != preferredBoltType) {
                  if(InvUtil.hasItem(player.inventory, Witchery.Items.GENERIC, currentBoltType.damageValue)) {
                     boltTypeToUse = currentBoltType;
                     break;
                  }
               }
            }

            if(boltTypeToUse != null) {
               SoundEffect.WITCHERY_RANDOM_WINDUP.playOnlyTo(player, 1.0F, 1.0F);
               this.setBoltType("WITCBoltTypeCurrent", stack, boltTypeToUse);
               if(!player.capabilities.isCreativeMode) {
                  InvUtil.consumeItem(player.inventory, Witchery.Items.GENERIC, boltTypeToUse.damageValue);
               }
            }
         }
      }

   }

   private boolean launchBolt(ItemStack stack, World world, EntityPlayer player, int ticks) {
      boolean isInfinite = EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0 && world.rand.nextDouble() < 0.25D;
      ItemGeneral.SubItem boltType = getBoltType("WITCBoltTypeCurrent", stack);
      if(boltType != null) {
         float f = (float)ticks / 20.0F;
         f = (f * f + f * 2.0F) / 3.0F;
         if((double)f < 0.1D) {
            return true;
         }

         if(f > 1.0F) {
            f = 1.0F;
         }

         byte boltID = 0;
         byte boltCount = 1;
         float arcStart = 0.0F;
         float arcInc = 0.0F;
         float damage = 2.0F;
         if(boltType == Witchery.Items.GENERIC.itemBoltSilver) {
            boltID = 4;
         } else if(boltType == Witchery.Items.GENERIC.itemBoltHoly) {
            boltID = 3;
         } else if(boltType == Witchery.Items.GENERIC.itemBoltAntiMagic) {
            if(ItemHunterClothes.isFullSetWorn(player, false)) {
               boltID = 2;
               Witchery.modHooks.reducePowerLevels(player, 1.0F);
            } else {
               boltID = 1;
            }
         } else if(boltType == Witchery.Items.GENERIC.itemBoltSplitting) {
            boltCount = 3;
            arcStart = -20.0F;
            arcInc = 20.0F;
            damage = 1.0F;
         }

         for(int i = 0; i < boltCount; ++i) {
            EntityBolt bolt = new EntityBolt(world, player, f * 2.0F, arcStart + (float)i * arcInc);
            bolt.setShooter(player);
            bolt.setBoltType(boltID);
            bolt.setDamage((double)damage);
            bolt.canBePickedUp = !isInfinite && !player.capabilities.isCreativeMode?1:0;
            if(f == 1.0F) {
               bolt.setIsCritical(true);
            }

            if(boltType != Witchery.Items.GENERIC.itemBoltAntiMagic) {
               int powerBonus = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
               if(powerBonus > 0) {
                  bolt.setDamage(bolt.getDamage() + (double)powerBonus * 0.5D + 0.5D);
               }

               int knockbackBonus = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
               if(knockbackBonus > 0) {
                  bolt.setKnockbackStrength(knockbackBonus);
               }

               if(EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) {
                  bolt.setFire(100);
               }
            }

            EntityUtil.spawnEntityInWorld(world, bolt);
            EntityUtil.correctProjectileTrackerSync(world, bolt);
         }

         stack.damageItem(2, player);
         world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
         if(isInfinite && !world.isRemote) {
            if(player instanceof EntityPlayerMP) {
               ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
            }

            return false;
         }
      }

      return true;
   }

   public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      return par1ItemStack;
   }

   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 72000;
   }

   public EnumAction getItemUseAction(ItemStack par1ItemStack) {
      return EnumAction.bow;
   }

   public int getItemEnchantability() {
      return 1;
   }

   public static ItemGeneral.BoltType getLoadedBoltType(ItemStack stack) {
      ItemGeneral.SubItem boltType = getBoltType("WITCBoltTypeCurrent", stack);
      return boltType != null?(ItemGeneral.BoltType)boltType:null;
   }

   private static ItemGeneral.SubItem getBoltType(String key, ItemStack stack) {
      if(!stack.hasTagCompound()) {
         return null;
      } else {
         NBTTagCompound nbtRoot = stack.getTagCompound();
         int boltID = nbtRoot.getInteger(key);
         return intToBoltType(boltID);
      }
   }

   private void setBoltType(String key, ItemStack stack, ItemGeneral.SubItem boltType) {
      if(!stack.hasTagCompound()) {
         stack.setTagCompound(new NBTTagCompound());
      }

      NBTTagCompound nbtRoot = stack.getTagCompound();
      int boltID = this.boltTypeToInt(boltType);
      nbtRoot.setInteger(key, boltID);
   }

   private static ItemGeneral.SubItem[] getBoltTypes() {
      if(BOLT_TYPES == null) {
         BOLT_TYPES = new ItemGeneral.SubItem[]{Witchery.Items.GENERIC.itemBoltStake, Witchery.Items.GENERIC.itemBoltAntiMagic, Witchery.Items.GENERIC.itemBoltHoly, Witchery.Items.GENERIC.itemBoltSplitting, Witchery.Items.GENERIC.itemBoltSilver};
      }

      return BOLT_TYPES;
   }

   private static ItemGeneral.SubItem intToBoltType(int boltID) {
      return boltID > 0 && boltID <= getBoltTypes().length?BOLT_TYPES[boltID - 1]:null;
   }

   private int boltTypeToInt(ItemGeneral.SubItem boltType) {
      for(int i = 0; i < getBoltTypes().length; ++i) {
         if(getBoltTypes()[i] == boltType) {
            return i + 1;
         }
      }

      return 0;
   }

   private ItemGeneral.SubItem getNextBoltType(ItemGeneral.SubItem boltType) {
      return intToBoltType(this.getNextBoltTypeID(this.boltTypeToInt(boltType)));
   }

   private int getNextBoltTypeID(int boltID) {
      ++boltID;
      if(boltID > getBoltTypes().length) {
         boltID = 1;
      }

      return boltID;
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      super.itemIcon = par1IconRegister.registerIcon(this.getIconString());
   }

   @SideOnly(Side.CLIENT)
   public IIcon getItemIconForUseDuration(int par1) {
      return super.itemIcon;
   }

}
