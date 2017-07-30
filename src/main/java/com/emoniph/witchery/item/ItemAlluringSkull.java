package com.emoniph.witchery.item;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.blocks.BlockAlluringSkull;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemAlluringSkull extends ItemBlock {

   @SideOnly(Side.CLIENT)
   private IIcon[] field_94586_c;
   public static final String[] field_94587_a = new String[]{"witchery:alluringSkull_off", "witchery:alluringSkull_on"};


   public ItemAlluringSkull(Block par1) {
      super(par1);
      this.setMaxDamage(0);
      this.setMaxStackSize(1);
   }

   public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
      if(par7 == 0) {
         return false;
      } else if(!par3World.getBlock(par4, par5, par6).getMaterial().isSolid()) {
         return false;
      } else {
         if(par7 == 1) {
            ++par5;
         }

         if(par7 == 2) {
            --par6;
         }

         if(par7 == 3) {
            ++par6;
         }

         if(par7 == 4) {
            --par4;
         }

         if(par7 == 5) {
            ++par4;
         }

         if(!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack)) {
            return false;
         } else if(!Witchery.Blocks.ALLURING_SKULL.canPlaceBlockAt(par3World, par4, par5, par6)) {
            return false;
         } else {
            if(!par3World.isRemote) {
               par3World.setBlock(par4, par5, par6, Witchery.Blocks.ALLURING_SKULL, par7, 2);
               int i1 = 0;
               if(par7 == 1) {
                  i1 = MathHelper.floor_double((double)(par2EntityPlayer.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
               }

               TileEntity tileentity = par3World.getTileEntity(par4, par5, par6);
               if(tileentity != null && tileentity instanceof BlockAlluringSkull.TileEntityAlluringSkull) {
                  ((BlockAlluringSkull.TileEntityAlluringSkull)tileentity).setSkullType(par1ItemStack.getItemDamage());
                  ((BlockAlluringSkull.TileEntityAlluringSkull)tileentity).setSkullRotation(i1);
               }
            }

            --par1ItemStack.stackSize;
            return true;
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public void registerIcons(IIconRegister par1IconRegister) {
      this.field_94586_c = new IIcon[field_94587_a.length];

      for(int i = 0; i < field_94587_a.length; ++i) {
         this.field_94586_c[i] = par1IconRegister.registerIcon(field_94587_a[i]);
      }

   }

   public IIcon getIconFromDamage(int par1) {
      if(par1 < 0 || par1 >= field_94587_a.length) {
         par1 = 0;
      }

      return this.field_94586_c[par1];
   }

   public String getUnlocalizedName(ItemStack par1ItemStack) {
      return super.getUnlocalizedName();
   }

}
