package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.brewing.potions.PotionEnslaved;
import com.emoniph.witchery.entity.EntityBanshee;
import com.emoniph.witchery.entity.EntityPoltergeist;
import com.emoniph.witchery.entity.EntitySpectre;
import com.emoniph.witchery.entity.EntitySummonedUndead;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.BlockUtil;
import com.emoniph.witchery.util.CreatureUtil;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import com.emoniph.witchery.util.TimeUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemSpectralStone extends ItemBase {

   @SideOnly(Side.CLIENT)
   IIcon item0;
   @SideOnly(Side.CLIENT)
   IIcon item1;
   @SideOnly(Side.CLIENT)
   IIcon item2;
   @SideOnly(Side.CLIENT)
   IIcon item3;
   private static final int SPECTRE = 1;
   private static final int BANSHEE = 2;
   private static final int POLTERGEIST = 3;
   private static final int TICKS_TO_ACTIVATE = 40;


   public ItemSpectralStone() {
      this.setMaxStackSize(16);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item item, CreativeTabs tab, List itemList) {
      itemList.add(new ItemStack(item, 1, 0));
      itemList.add(new ItemStack(item, 1, 17));
      itemList.add(new ItemStack(item, 1, 18));
      itemList.add(new ItemStack(item, 1, 19));
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      super.registerIcons(iconRegister);
      this.item0 = super.itemIcon;
      this.item1 = iconRegister.registerIcon(this.getIconString() + ".spectre");
      this.item2 = iconRegister.registerIcon(this.getIconString() + ".banshee");
      this.item3 = iconRegister.registerIcon(this.getIconString() + ".poltergeist");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int damage) {
      switch(this.getBeingFromMeta(damage)) {
      case 0:
      default:
         return this.item0;
      case 1:
         return this.item1;
      case 2:
         return this.item2;
      case 3:
         return this.item3;
      }
   }

   public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean extraTip) {
      int creature = this.getBeingFromMeta(stack.getItemDamage());
      int quantity = Math.min(this.getQuantityFromMeta(stack.getItemDamage()), 4);
      switch(creature) {
      case 1:
         list.add(String.format("%s: %d", new Object[]{Witchery.resource("entity.witchery.spectre.name"), Integer.valueOf(quantity)}));
         break;
      case 2:
         list.add(String.format("%s: %d", new Object[]{Witchery.resource("entity.witchery.banshee.name"), Integer.valueOf(quantity)}));
         break;
      case 3:
         list.add(String.format("%s: %d", new Object[]{Witchery.resource("entity.witchery.poltergeist.name"), Integer.valueOf(quantity)}));
      }

   }

   public boolean hasEffect(ItemStack stack) {
      return true;
   }

   public static int metaFromCreature(Class creatureType, int quantity) {
      return creatureType == EntitySpectre.class?1 | quantity << 4:(creatureType == EntityBanshee.class?2 | quantity << 4:(creatureType == EntityPoltergeist.class?3 | quantity << 4:0));
   }

   private int getBeingFromMeta(int meta) {
      int critter = meta & 15;
      if(critter < 0 || critter > 15) {
         critter = 0;
      }

      return critter;
   }

   private int getQuantityFromMeta(int meta) {
      int quantity = meta >>> 4 & 7;
      if(quantity < 0 || quantity >= 8) {
         quantity = 0;
      }

      return quantity;
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack stack) {
      return EnumRarity.rare;
   }

   public EnumAction getItemUseAction(ItemStack stack) {
      return EnumAction.bow;
   }

   public int getMaxItemUseDuration(ItemStack stack) {
      return TimeUtil.secsToTicks(20);
   }

   public void onUsingTick(ItemStack stack, EntityPlayer player, int countdown) {
      World world = player.worldObj;
      int elapsedTicks = this.getMaxItemUseDuration(stack) - countdown;
      if(elapsedTicks == 40) {
         SoundEffect.NOTE_PLING.playOnlyTo(player);
      }

   }

   public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int countdown) {
      int elapsedTicks = this.getMaxItemUseDuration(stack) - countdown;
      int creature = this.getBeingFromMeta(stack.getItemDamage());
      int quantity = Math.min(this.getQuantityFromMeta(stack.getItemDamage()), 3);
      if(elapsedTicks >= 40 && creature > 0 && quantity > 0) {
         MovingObjectPosition mop = InfusionOtherwhere.doCustomRayTrace(world, player, true, 16.0D);
         int[] coords = BlockUtil.getBlockCoords(world, mop, true);
         Class theClass = null;
         if(coords != null) {
            switch(creature) {
            case 1:
               theClass = EntitySpectre.class;
               break;
            case 2:
               theClass = EntityBanshee.class;
               break;
            case 3:
               theClass = EntityPoltergeist.class;
               break;
            default:
               SoundEffect.NOTE_SNARE.playOnlyTo(player);
               return;
            }

            for(int newStack = 0; newStack < quantity; ++newStack) {
               EntitySummonedUndead entity = (EntitySummonedUndead)Infusion.spawnCreature(world, theClass, coords[0], coords[1], coords[2], (EntityLivingBase)null, 0, 1, ParticleEffect.INSTANT_SPELL, (SoundEffect)null);
               if(entity != null) {
                  CreatureUtil.spawnWithEgg(entity, true);
                  entity.setSummoner(player.getCommandSenderName());
                  PotionEnslaved.setEnslaverForMob(entity, player);
               }
            }

            if(!player.capabilities.isCreativeMode) {
               if(stack.stackSize > 1) {
                  ItemStack var13 = stack.splitStack(1);
                  var13.setItemDamage(0);
                  if(!player.inventory.addItemStackToInventory(var13)) {
                     if(!world.isRemote) {
                        world.spawnEntityInWorld(new EntityItem(world, player.posX + 0.5D, player.posY + 1.5D, player.posZ + 0.5D, var13));
                     }
                  } else if(player instanceof EntityPlayerMP) {
                     ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                  }
               } else {
                  stack.setItemDamage(0);
               }
            }
         } else {
            SoundEffect.NOTE_SNARE.playOnlyTo(player, 1.0F, 1.0F);
         }
      } else {
         SoundEffect.NOTE_SNARE.playOnlyTo(player, 1.0F, 1.0F);
      }

   }

   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
      player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
      return stack;
   }
}
