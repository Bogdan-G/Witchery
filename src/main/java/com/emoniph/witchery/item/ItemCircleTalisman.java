package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.item.ItemBase;
import com.emoniph.witchery.util.BlockSide;
import com.emoniph.witchery.util.ParticleEffect;
import com.emoniph.witchery.util.SoundEffect;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemCircleTalisman extends ItemBase {

   @SideOnly(Side.CLIENT)
   IIcon item0;
   @SideOnly(Side.CLIENT)
   IIcon item1;
   @SideOnly(Side.CLIENT)
   IIcon item2;
   @SideOnly(Side.CLIENT)
   IIcon item3;
   @SideOnly(Side.CLIENT)
   IIcon item4;
   @SideOnly(Side.CLIENT)
   IIcon item5;
   @SideOnly(Side.CLIENT)
   IIcon item6;
   @SideOnly(Side.CLIENT)
   IIcon item7;
   @SideOnly(Side.CLIENT)
   IIcon item8;
   @SideOnly(Side.CLIENT)
   IIcon item9;


   public ItemCircleTalisman() {
      this.setMaxStackSize(16);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   @SideOnly(Side.CLIENT)
   public EnumRarity getRarity(ItemStack itemstack) {
      return EnumRarity.uncommon;
   }

   public String getUnlocalizedName(ItemStack itemStack) {
      Integer damage = Integer.valueOf(itemStack.getItemDamage());
      return damage.equals(Integer.valueOf(0))?this.getUnlocalizedName():this.getUnlocalizedName() + "." + damage.toString();
   }

   public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
      return super.getUnlocalizedName(par1ItemStack);
   }

   public String getItemStackDisplayName(ItemStack itemStack) {
      String localizedName = super.getItemStackDisplayName(itemStack);
      int damage = itemStack.getItemDamage();
      return damage > 0?String.format("%s (%s)", new Object[]{localizedName, this.getChalkDisplayName(damage)}):localizedName;
   }

   private String getChalkDisplayName(int damage) {
      int small = damage & 7;
      int medium = damage >>> 3 & 7;
      int large = damage >>> 6 & 7;
      StringBuilder result = new StringBuilder();
      if(small > 0) {
         result.append(StatCollector.translateToLocal("circletalisman.small." + Integer.valueOf(small).toString()));
         result.append(", ");
      }

      if(medium > 0) {
         result.append(StatCollector.translateToLocal("circletalisman.medium." + Integer.valueOf(medium).toString()));
         result.append(", ");
      }

      if(large > 0) {
         result.append(StatCollector.translateToLocal("circletalisman.large." + Integer.valueOf(large).toString()));
         result.append(", ");
      }

      if(result.length() > 0) {
         result.setLength(result.length() - 2);
      }

      return result.toString();
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister iconRegister) {
      super.registerIcons(iconRegister);
      this.item0 = super.itemIcon;
      this.item1 = iconRegister.registerIcon(this.getIconString() + ".1");
      this.item2 = iconRegister.registerIcon(this.getIconString() + ".2");
      this.item3 = iconRegister.registerIcon(this.getIconString() + ".3");
      this.item4 = iconRegister.registerIcon(this.getIconString() + ".4");
      this.item5 = iconRegister.registerIcon(this.getIconString() + ".5");
      this.item6 = iconRegister.registerIcon(this.getIconString() + ".6");
      this.item7 = iconRegister.registerIcon(this.getIconString() + ".7");
      this.item8 = iconRegister.registerIcon(this.getIconString() + ".8");
      this.item9 = iconRegister.registerIcon(this.getIconString() + ".9");
   }

   @SideOnly(Side.CLIENT)
   public IIcon getIconFromDamage(int damage) {
      int small = damage & 7;
      int medium = damage >>> 3 & 7;
      int large = damage >>> 6 & 7;
      switch(large > 0?large + 6:(medium > 0?medium + 3:small)) {
      case 0:
      default:
         return this.item0;
      case 1:
         return this.item1;
      case 2:
         return this.item2;
      case 3:
         return this.item3;
      case 4:
         return this.item4;
      case 5:
         return this.item5;
      case 6:
         return this.item6;
      case 7:
         return this.item7;
      case 8:
         return this.item8;
      case 9:
         return this.item9;
      }
   }

   @SideOnly(Side.CLIENT)
   public void getSubItems(Item itemID, CreativeTabs tab, List itemList) {
      itemList.add(new ItemStack(itemID, 1, 0));
      itemList.add(new ItemStack(itemID, 1, 1));
      itemList.add(new ItemStack(itemID, 1, 2));
      itemList.add(new ItemStack(itemID, 1, 3));
      itemList.add(new ItemStack(itemID, 1, 8));
      itemList.add(new ItemStack(itemID, 1, 16));
      itemList.add(new ItemStack(itemID, 1, 24));
      itemList.add(new ItemStack(itemID, 1, 64));
      itemList.add(new ItemStack(itemID, 1, 128));
      itemList.add(new ItemStack(itemID, 1, 192));
   }

   public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int posX, int posY, int posZ, int side, float hitX, float hitY, float hitZ) {
      if((!BlockSide.TOP.isEqual(side) || world.getBlock(posX, posY, posZ) != Witchery.Blocks.CIRCLE) && !Witchery.Blocks.CIRCLE.canBlockStay(world, posX, posY + 1, posZ)) {
         SoundEffect.NOTE_SNARE.playAtPlayer(world, player);
         return true;
      } else {
         int damage = itemstack.getItemDamage();
         if(damage > 0) {
            if(!world.isRemote) {
               int a = damage & 7;
               int b = damage >>> 3 & 7;
               int c = damage >>> 6 & 7;
               boolean _ = false;
               int[][] PATTERN = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, c, c, c, c, c, c, c, 0, 0, 0, 0, 0}, {0, 0, 0, 0, c, 0, 0, 0, 0, 0, 0, 0, c, 0, 0, 0, 0}, {0, 0, 0, c, 0, 0, b, b, b, b, b, 0, 0, c, 0, 0, 0}, {0, 0, c, 0, 0, b, 0, 0, 0, 0, 0, b, 0, 0, c, 0, 0}, {0, c, 0, 0, b, 0, 0, a, a, a, 0, 0, b, 0, 0, c, 0}, {0, c, 0, b, 0, 0, a, 0, 0, 0, a, 0, 0, b, 0, c, 0}, {0, c, 0, b, 0, a, 0, 0, 0, 0, 0, a, 0, b, 0, c, 0}, {0, c, 0, b, 0, a, 0, 0, 4, 0, 0, a, 0, b, 0, c, 0}, {0, c, 0, b, 0, a, 0, 0, 0, 0, 0, a, 0, b, 0, c, 0}, {0, c, 0, b, 0, 0, a, 0, 0, 0, a, 0, 0, b, 0, c, 0}, {0, c, 0, 0, b, 0, 0, a, a, a, 0, 0, b, 0, 0, c, 0}, {0, 0, c, 0, 0, b, 0, 0, 0, 0, 0, b, 0, 0, c, 0, 0}, {0, 0, 0, c, 0, 0, b, b, b, b, b, 0, 0, c, 0, 0, 0}, {0, 0, 0, 0, c, 0, 0, 0, 0, 0, 0, 0, c, 0, 0, 0, 0}, {0, 0, 0, 0, 0, c, c, c, c, c, c, c, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
               int y = world.getBlock(posX, posY, posZ) == Witchery.Blocks.CIRCLE?posY:posY + 1;
               int pass = 0;

               boolean fail;
               for(fail = false; pass < 2 && !fail; ++pass) {
                  int newStack = (PATTERN.length - 1) / 2;

                  for(int z = 0; z < PATTERN.length - 1; ++z) {
                     int worldZ = posZ - newStack + z;
                     int offsetX = (PATTERN[z].length - 1) / 2;

                     for(int x = 0; x < PATTERN[z].length; ++x) {
                        int worldX = posX - offsetX + x;
                        int item = PATTERN[PATTERN.length - 1 - z][x];
                        Material material = world.getBlock(worldX, y, worldZ).getMaterial();
                        boolean solidBlock = material != null && (material.isOpaque() || material.isSolid());
                        switch(item) {
                        case 1:
                           if(!solidBlock && Witchery.Blocks.GLYPH_RITUAL.canBlockStay(world, worldX, y, worldZ)) {
                              if(pass == 1) {
                                 world.setBlock(worldX, y, worldZ, Witchery.Blocks.GLYPH_RITUAL, world.rand.nextInt(12), 3);
                              }
                           } else {
                              fail = true;
                           }
                           break;
                        case 2:
                           if(!solidBlock && Witchery.Blocks.GLYPH_OTHERWHERE.canBlockStay(world, worldX, y, worldZ)) {
                              if(pass == 1) {
                                 world.setBlock(worldX, y, worldZ, Witchery.Blocks.GLYPH_OTHERWHERE, world.rand.nextInt(12), 3);
                              }
                           } else {
                              fail = true;
                           }
                           break;
                        case 3:
                           if(!solidBlock && Witchery.Blocks.GLYPH_INFERNAL.canBlockStay(world, worldX, y, worldZ)) {
                              if(pass == 1) {
                                 world.setBlock(worldX, y, worldZ, Witchery.Blocks.GLYPH_INFERNAL, world.rand.nextInt(12), 3);
                              }
                           } else {
                              fail = true;
                           }
                           break;
                        case 4:
                           if(y != posY) {
                              if(Witchery.Blocks.CIRCLE.canBlockStay(world, worldX, y, worldZ)) {
                                 if(pass == 1) {
                                    world.setBlock(worldX, y, worldZ, Witchery.Blocks.CIRCLE);
                                 }
                              } else {
                                 fail = true;
                              }
                           }
                           break;
                        default:
                           continue;
                        }

                        if(pass == 1) {
                           ParticleEffect.SMOKE.send(SoundEffect.NONE, world, (double)worldX, (double)(posY + 1), (double)worldZ, 0.5D, 1.0D, 16);
                        }
                     }
                  }
               }

               if(fail) {
                  world.playSoundAtEntity(player, "note.snare", 0.5F, 0.4F / ((float)world.rand.nextDouble() * 0.4F + 0.8F));
               } else if(itemstack.stackSize > 1) {
                  ItemStack var29 = new ItemStack(this);
                  if(!player.inventory.addItemStackToInventory(var29)) {
                     world.spawnEntityInWorld(new EntityItem(world, player.posX + 0.5D, player.posY + 1.5D, player.posZ + 0.5D, var29));
                  } else if(player instanceof EntityPlayerMP) {
                     ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
                  }

                  --itemstack.stackSize;
                  if(itemstack.stackSize <= 0) {
                     player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
                  }
               } else {
                  itemstack.setItemDamage(0);
               }
            } else {
               world.playSoundAtEntity(player, "note.snare", 0.5F, 0.4F / ((float)world.rand.nextDouble() * 0.4F + 0.8F));
            }
         }

         return true;
      }
   }
}
